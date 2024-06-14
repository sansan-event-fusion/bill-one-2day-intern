import { useQuery } from "react-query";
import { recipientInvoiceAPI } from "src/api/recipient/invoice-api";
import {
  Invoice,
  InvoiceResponse,
  responseToInvoice,
} from "src/domain/models/invoice";

export const useInvoices = (): Invoice[] => {
  const result = useQuery<InvoiceResponse[], Error>(["invoices"], () =>
    recipientInvoiceAPI.getInvoices(),
  );
  return result.data?.map((data) => responseToInvoice(data)) ?? [];
};
