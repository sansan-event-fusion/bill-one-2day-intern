export type RequestJson = {
    tenantNameId: string,
    recipientTenantNameId: string,
    issuerInvoiceUUID: string,
    issuerUUID: string,
    recipientUUID: string,
    issuerName: string,
    issuerEmail: string,
    recipientName: string,
    recipientEmail: string,
    paymentDueDate: string,
    invoiceAmount: number,
}