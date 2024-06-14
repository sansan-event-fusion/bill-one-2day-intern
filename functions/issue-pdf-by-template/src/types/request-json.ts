export type RequestJson = {
    tenantNameId: string
    issuerInvoiceUUID: string
    issuerUUID: string
    issuerName: string
    issuerOrganizationName: string | null
    recipientUUID: string
    recipientName: string
    recipientOrganizationName: string | null
    issuerRegistrationNumber: string
    invoiceTaxExcludedAmount: number
    taxRate: string
    invoiceNumber: string
    paymentDueDate: string
}