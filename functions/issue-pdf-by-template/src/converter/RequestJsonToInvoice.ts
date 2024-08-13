import {Invoice} from "../model/Invoice";
import {RequestJson} from "../types/request-json";
import {Title} from "../model/Title";
import {InvoiceAmountInfo, InvoiceTaxRate} from "../model/InvoiceAmountInfo";

export const RequestJsonToInvoice = (requestJson: RequestJson): Invoice => {

    return {
        title: new Title("請求書"),
        tenantNameId: requestJson.tenantNameId,
        recipientTenantNameId: requestJson.recipientTenantNameId,
        issuerInvoiceUUID: requestJson.issuerInvoiceUUID,
        recipientInfo:{
            recipientName:requestJson.recipientName,
            recipientOrganizationName:requestJson.recipientEmail ?? null
        },
        issuerInfo:{
            issuerName:requestJson.issuerName,
            issuerOrganizationName:requestJson.issuerEmail ?? null,
            issuerRegistrationNumber:"T1234567890123"
        },
        paymentInfo:{
            amount: requestJson.invoiceAmount,
            publishedAt:new Date(),
            paymentDueDate: new Date(Date.parse(requestJson.paymentDueDate))
        },
        invoiceAmountInfo: new InvoiceAmountInfo(
            "10",
            requestJson.invoiceAmount)
    }
}