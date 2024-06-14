import { InvoiceIssueParam } from "src/api/issuing/invoice";
import { fetchJSONOrNull, fetchURL } from "src/api/util";
import { createFailure, createSuccess } from "src/util/Result";
import { IssuerInvoiceResponse } from "src/domain/models/invoice";

export const issuingInvoiceAPI = {
  getIssuedInvoice: async () => {
    const response = await fetchJSONOrNull<{ rows: IssuerInvoiceResponse[] }>(
      "application/json",
      "http://localhost:8082/api/issuing/invoices",
      {
        method: "GET",
      },
    );
    return response?.rows ?? [];
  },
  issueInvoice: async (values: InvoiceIssueParam) => {
    const response = await fetchURL(
      "application/json",
      "http://localhost:8082/api/issuing/invoices/register",
      {
        method: "POST",
        body: JSON.stringify(values),
      },
    );
    if (!response.ok) {
      return createFailure(response.statusText);
    }
    return createSuccess();
  },
};
