import { fetchJSONOrNull, fetchURL } from "../util";
import { RecipientInvoiceUploadFormValues } from "src/hooks/useRecipientInvoiceUploadForm";
import { InvoiceResponse } from "src/domain/models/invoice";
import { RecipientInvoiceEditFormValues } from "src/components/organisms/recipient/Invoice/hooks/useRecipientInvoiceEditForm";

export const recipientInvoiceAPI = {
  upload: async (values: RecipientInvoiceUploadFormValues) => {
    // TODO(FE): 課題3
  },
  getInvoices: async (): Promise<InvoiceResponse[]> => {
    const response = await fetchJSONOrNull<InvoiceResponse[]>(
      "application/json",
      "http://localhost:8081/api/recipient/invoices",
      {
        method: "GET",
      },
    );
    return response ?? [];
  },
  getInvoice: async (invoiceUUID: string): Promise<InvoiceResponse | null> => {
    return await fetchJSONOrNull<InvoiceResponse>(
      "application/json",
      `http://localhost:8081/api/recipient/invoice/${invoiceUUID}`,
      {
        method: "GET",
      },
    );
  },
  updateInvoice: async (invoice: RecipientInvoiceEditFormValues) => {
    return await fetchURL(
      "application/json",
      `http://localhost:8081/api/recipient/invoice/${invoice.invoiceUUID}`,
      {
        method: "PUT",
        body: JSON.stringify({
          invoiceAmount: invoice.invoiceAmount,
          paymentDeadline: invoice.paymentDeadline,
          supplierUUID: invoice.supplierUUID,
        }),
      },
    );
  },
};
