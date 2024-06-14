import {
  IssuerInvoice,
  responseToIssuerInvoice,
} from "src/domain/models/invoice";
import { useQuery } from "react-query";
import { issuingInvoiceAPI } from "src/api/issuing/invoice-api";

export const useIssuedInvoice = (): IssuerInvoice[] => {
  const response = useQuery("issuedInvoice", () =>
    issuingInvoiceAPI.getIssuedInvoice(),
  );
  return response.data?.map((data) => responseToIssuerInvoice(data)) ?? [];
};
