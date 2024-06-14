import {RequestJson} from "./types/request-json";
import type {Request, Response} from "@google-cloud/functions-framework"
import * as ff from "@google-cloud/functions-framework"
import {RequestJsonToInvoice} from "./converter/RequestJsonToInvoice";
import {InvoiceViewModel} from "./view-model/invoice/InvoiceViewModel";
import {uploadBufferToStorage} from "./infrastructure/uploadBufferToStorage";
import {createCloudTasks} from "./createCloudTasks";
import {PdfGenerator} from "./lib/pdf-generator";
import {InvoiceTemplate} from "./template/invoice/InvoiceTemplate";
import dotenv from 'dotenv';
import {getEnv} from "./env";
import BigNumber from "bignumber.js";

export type Result = "SUCCEEDED" | "FAILED"
dotenv.config()

BigNumber.config({
    DECIMAL_PLACES: 0,                // 小数部2桁
    ROUNDING_MODE: BigNumber.ROUND_DOWN // 切り上げ
});


ff.http(
    getEnv('FUNCTION_NAME', "issueInvoicePdf"),
    async (req: Request, res: Response
    ) => {
        const body = req.body as RequestJson
        console.log("Received request:", body)
        const result = await generatePDF(body)

        try {
            await createCloudTasks({body, generateResult: result})
            console.log("CloudTasks created")
        } catch (e: unknown) {
            if (e instanceof Error) {
                console.error(e)
            }
        }

        //  Even if it failed to generate PDF, it will return 200 OK
        res.status(200).end()
    })

const generatePDF = async (body: RequestJson): Promise<Result> => {
    try {
        const invoice = RequestJsonToInvoice(body)
        const invoiceViewModel = new InvoiceViewModel(invoice)
        const pdfGenerator = new PdfGenerator()
        const invoiceTemplate = new InvoiceTemplate(invoiceViewModel)

        invoiceTemplate.renderToPdf(pdfGenerator)
        pdfGenerator.save()

        const arrayBuffer = pdfGenerator.getArrayBuffer()
        await uploadBufferToStorage(
            getEnv("GCS_BUCKET_NAME_ISSUING", "default-bucket"),
            "/issuing"
            + `/${body.issuerUUID}`
            + `/${getEnv("UPLOAD_FILE_PATH_ISSUING", "issuing-invoice")}`
            + `/${body.issuerInvoiceUUID}.pdf`,
            Buffer.from(arrayBuffer)
        )
        await uploadBufferToStorage(
            getEnv("GCS_BUCKET_NAME_RECIPIENT", "default-bucket"),
            `/recipient`
            + `/${body.recipientUUID}`
            + `/${getEnv("UPLOAD_FILE_PATH_RECIPIENT", "recipient-invoices")}`
            + `/${body.issuerInvoiceUUID}.pdf`,
            Buffer.from(arrayBuffer)
        )

        //  ローカルでアップロードしたファイルを確認したいとき
        // await downloadBufferFromStorage(
        //     getEnv("GCS_BUCKET_NAME_ISSUING", "default-bucket"),
        //     `${getEnv("UPLOAD_FILE_PATH_ISSUING", "default-upload-path")}` + `/${body.issuerInvoiceUUID}.pdf`,
        //     getEnv("LOCAL_SAVE_PATH", "./downloaded_issuer_invoice.pdf")
        // )
        // await downloadBufferFromStorage(
        //     getEnv("GCS_BUCKET_NAME_RECIPIENT", "default-bucket"),
        //     `${getEnv("UPLOAD_FILE_PATH_RECIPIENT", "default-upload-path")}` + `/${body.issuerInvoiceUUID}.pdf`,
        //     getEnv("LOCAL_SAVE_PATH", "./downloaded_recipient_invoice.pdf")
        // )

        return "SUCCEEDED"
    } catch (e: unknown) {
        if (e instanceof Error) {
            console.error(e)
        }
        return "FAILED"
    }
}