export type Invoice = {
  invoiceUUID: string;
  recipientUUID: string;
  recipientName: string;
  issuerName: string;
  invoiceOwner: string;
  supplier: string;
  supplierUUID: string;
  amount: number;
  registeredAt: Date;
  paymentDeadline: Date | undefined;
  memo: string;
  url: string;
};

export type IssuerInvoice = {
  supplierTenantNameId: string;
  issuerName: string;
  supplierName: string;
  amount: number;
  paymentDeadline: Date;
  recipientUUID: string;
  issuerInvoiceUUID: string;
  url: string | null;
};

export type InvoiceResponse = {
  invoiceUUID: string;
  recipientUUID: string;
  recipientName: string;
  tenantNameId: string;
  invoiceOwner: string;
  supplierName: string | null;
  supplierUUID: string | null;
  invoiceAmount: number | null;
  paymentDeadline: Date | null;
  registeredBy: string;
  registeredAt: Date;
  url: string;
};

export const responseToInvoice = (response: InvoiceResponse): Invoice => {
  return {
    invoiceUUID: response.invoiceUUID,
    recipientUUID: response.recipientUUID,
    recipientName: response.recipientName,
    issuerName: response.registeredBy,
    invoiceOwner: response.invoiceOwner,
    supplier: response.supplierName ?? "",
    supplierUUID: response.supplierUUID ?? "",
    amount: response.invoiceAmount ?? 0,
    registeredAt: response.registeredAt,
    paymentDeadline: response.paymentDeadline ?? undefined,
    memo: "",
    url: response.url,
  };
};

export type IssuerInvoiceResponse = {
  tenantNameId: string;
  issuerUUID: string;
  issuerName: string;
  recipientName: string;
  invoiceAmount: number;
  paymentDeadline: string;
  recipientUUID: string;
  issuerInvoiceUUID: string;
  url: string | null;
};

export const responseToIssuerInvoice = (
  response: IssuerInvoiceResponse,
): IssuerInvoice => {
  return {
    supplierTenantNameId: response.tenantNameId,
    issuerName: response.issuerName,
    supplierName: response.recipientName,
    amount: response.invoiceAmount,
    paymentDeadline: new Date(response.paymentDeadline),
    recipientUUID: response.recipientUUID,
    issuerInvoiceUUID: response.issuerInvoiceUUID,
    url: response.url,
  };
};
