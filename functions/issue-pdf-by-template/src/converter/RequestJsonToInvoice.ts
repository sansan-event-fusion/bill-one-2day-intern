import {Invoice} from "../model/Invoice";
import {RequestJson} from "../types/request-json";
import {Title} from "../model/Title";
import {InvoiceAmountInfo, InvoiceTaxRate} from "../model/InvoiceAmountInfo";

export const RequestJsonToInvoice = (requestJson: RequestJson): Invoice => {

    return {
        title: new Title("請求書"),
        issuerInvoiceUUID: requestJson.issuerInvoiceUUID,
        recipientInfo:{
            recipientName:requestJson.recipientName,
            recipientOrganizationName:requestJson.recipientOrganizationName ?? null
        },
        issuerInfo:{
            issuerName:requestJson.issuerName,
            issuerOrganizationName:requestJson.issuerOrganizationName ?? null,
            issuerRegistrationNumber:"T1234567890123"
        },
        paymentInfo:{
            amount: requestJson.invoiceTaxExcludedAmount,
            publishedAt:new Date(),
            paymentDueDate: new Date(Date.parse(requestJson.paymentDueDate))
        },
        invoiceAmountInfo: new InvoiceAmountInfo(
            requestJson.taxRate as InvoiceTaxRate,
            requestJson.invoiceTaxExcludedAmount)
        }
}