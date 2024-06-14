import {RecipientInfo} from "./RecipientInfo";
import {PaymentInfo} from "./PaymentInfo";
import {IssuerInfo} from "./IssuerInfo";
import {Title} from "./Title";
import {InvoiceAmountInfo} from "./InvoiceAmountInfo";

export type Invoice ={
    title: Title
    issuerInvoiceUUID: string,
    recipientInfo: RecipientInfo
    issuerInfo:IssuerInfo
    invoiceAmountInfo:InvoiceAmountInfo
    paymentInfo:PaymentInfo
}