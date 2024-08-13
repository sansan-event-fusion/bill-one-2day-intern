import {RequestJson} from "./types/request-json";
import {CloudTasksClient, protos} from "@google-cloud/tasks";
import {v4 as uuidv4} from "uuid"
import {Result} from "./index";
import {getEnv} from "./env";
import * as grpc from '@grpc/grpc-js';


let tasksClient: CloudTasksClient


type CreateRequestIssuingArgs = {
    tenantNameId: string,
    generatedInvoiceUUID: string,
    issuerUUID: string,
    generateResult: Result,
}

type CreateRequestRecipientArgs = {
    issuedTenantNameId: string,
    generatedInvoiceUUID: string,
    generateResult: Result,
    issuerUUID: string,
    issuerName: string,
    recipientTenantNameId: string,
    recipientUUID: string,
    invoiceAmount: number,
    paymentDeadline: string,
}


export const createCloudTasks = async ({
                                           body, generateResult,
                                       }: {
    body: RequestJson,
    generateResult: Result
}): Promise<void> => {
    const requestToIssuing = createRequestIssuing({
        tenantNameId: body.tenantNameId,
        generatedInvoiceUUID: body.issuerInvoiceUUID,
        issuerUUID: body.issuerUUID,
        generateResult,
    })
    const requestToRecipient = createRequestRecipient({
        issuedTenantNameId: body.tenantNameId,
        generatedInvoiceUUID: body.issuerInvoiceUUID,
        issuerUUID: body.issuerUUID,
        issuerName: body.issuerName,
        recipientTenantNameId: body.recipientTenantNameId,
        recipientUUID: body.recipientUUID,
        invoiceAmount: body.invoiceAmount,
        paymentDeadline: body.paymentDueDate,
        generateResult,
    })


    if (!tasksClient) {
        tasksClient = new CloudTasksClient({
            projectId: getEnv("GCP_PROJECT_ID", "bill-one-2024"),
            port: 9090,
            servicePath: getEnv('CLOUD_TASKS_EMULATOR_HOST', '127.0.0.1'),
            sslCreds: grpc.credentials.createInsecure(),
        })
    }

    const queuePathIssuing = getEnv(
        'CLOUD_TASKS_QUEUE_PATH_ISSUING',
        'projects/bill-one-2024/locations/asia-northeast1/queues/issuing');
    const queuePathRecipient = getEnv(
        'CLOUD_TASKS_QUEUE_PATH_ISSUING',
        'projects/bill-one-2024/locations/asia-northeast1/queues/recipient');


    await ensureQueueExists(queuePathIssuing);
    await ensureQueueExists(queuePathRecipient);

    const taskToIssuing = {
        httpRequest: {
            httpMethod: protos.google.cloud.tasks.v2.HttpMethod.POST,
            url: getEnv('CLOUD_TASKS_CALLBACK_URL_ISSUING', 'projects/bill-one-2024/locations/asia-northeast1/queues/issuing'),
            body: Buffer.from(JSON.stringify(requestToIssuing)).toString('base64'),
            headers: {
                'Content-Type': 'application/json',
            },
        },
    };

    const taskToRecipient = {
        httpRequest: {
            httpMethod: protos.google.cloud.tasks.v2.HttpMethod.POST,
            url: getEnv('CLOUD_TASKS_CALLBACK_URL_RECIPIENT', 'projects/bill-one-2024/locations/asia-northeast1/queues/recipients'),
            body: Buffer.from(JSON.stringify(requestToRecipient)).toString('base64'),
            headers: {
                'Content-Type': 'application/json',
            },
        },
    };

    await tasksClient.createTask({parent: queuePathIssuing, task: taskToIssuing});
    await tasksClient.createTask({parent: queuePathRecipient, task: taskToRecipient});
}

const createRequestIssuing = ({
                                  tenantNameId: tenantNameId,
                                  generatedInvoiceUUID: generatedInvoiceUUID,
                                  generateResult,
                                  issuerUUID
                              }: CreateRequestIssuingArgs): protos.google.cloud.tasks.v2.ICreateTaskRequest => {
    const requestBody = JSON.stringify({
        generatedInvoiceUUID: generatedInvoiceUUID,
        generatedResult: generateResult,
        uploadedFilePathIssuing: "http://localhost:4443"
            + `/${getEnv("GCS_BUCKET_NAME_ISSUING", "issuing-dev-bucket")}`
            + "/issuing"
            + `/${issuerUUID}`
            + `/${getEnv("UPLOAD_FILE_PATH_ISSUING", "issuing-invoice")}`
            + `/${generatedInvoiceUUID}.pdf`,
    })

    return {
        task: {
            httpRequest: {
                httpMethod: protos.google.cloud.tasks.v2.HttpMethod.POST,
                url: getEnv("CLOUD_TASKS_CALLBACK_URL_ISSUING", "http://127.0.0.1:8082/event-handler/issuing/invoices/reflect-issued-result"),
                headers: {
                    "X-Tenant-Name-Id": `${tenantNameId}`,
                    "X-Cloud-Trace-Context": `${uuidv4().replace(/-/g, "")}/0`,
                    "Content-Type": "application/json",
                },
                body: Buffer.from(requestBody).toString("base64"),
            }
        }
    }
}


const createRequestRecipient = (
    {
        recipientTenantNameId,
        generatedInvoiceUUID,
        generateResult,
        issuerUUID,
        issuerName,
        recipientUUID,
        invoiceAmount,
        paymentDeadline
    }: CreateRequestRecipientArgs): protos.google.cloud.tasks.v2.ICreateTaskRequest => {
    const requestBody = JSON.stringify({
        tenantNameId: recipientTenantNameId,
        generatedInvoiceUUID: generatedInvoiceUUID,
        generatedResult: generateResult,
        issuerUUID: issuerUUID,
        issuerName: issuerName,
        recipientUUID: recipientUUID,
        invoiceAmount: invoiceAmount,
        paymentDeadline: paymentDeadline,
        uploadedFilePathRecipient: "http://localhost:4443"
            + `/${getEnv("GCS_BUCKET_NAME_RECIPIENT", "recipient-dev-bucket")}`
            + `/${recipientUUID}`
            + `/${getEnv("UPLOAD_FILE_PATH_RECIPIENT", "recipient-invoices")}`
            + `/${generatedInvoiceUUID}.pdf`,
    })
    console.log("request", requestBody)
    console.log(JSON.stringify(requestBody));


    return {
        task: {
            httpRequest: {
                httpMethod: protos.google.cloud.tasks.v2.HttpMethod.POST,
                url: getEnv("CLOUD_TASKS_CALLBACK_URL_RECIPIENT", "http://127.0.0.1:8081/event-handler/recipient/invoices/reflect-issued-result"),
                headers: {
                    "X-Tenant-Name-Id": `${recipientTenantNameId}`,
                    "X-Cloud-Trace-Context": `${uuidv4().replace(/-/g, "")}/0`,
                    "Content-Type": "application/json",
                },
                body: Buffer.from(requestBody).toString("base64"),
            }
        }
    }
}


const ensureQueueExists = async (queuePath: string) => {
    if (!tasksClient) {
        tasksClient = new CloudTasksClient({
            servicePath: getEnv('CLOUD_TASKS_EMULATOR_HOST', 'localhost'),
            port: parseInt(getEnv('CLOUD_TASKS_EMULATOR_PORT', '9090'), 10),
            sslCreds: grpc.credentials.createInsecure(), // SSLなしの接続を使用
        });
    }

    try {
        await tasksClient.getQueue({name: queuePath});
        console.log(`Queue ${queuePath} already exists.`);
    } catch (error) {
        console.error(error)
        throw error;
    }
};