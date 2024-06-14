import { InvoiceIssueFormValues } from "src/components/organisms/issuing/invoice/hooks/useInvoiceIssueForm";
import { useMutation, useQueryClient } from "react-query";
import { fromFormValuesToParam } from "src/api/issuing/invoice";
import { issuingInvoiceAPI } from "src/api/issuing/invoice-api";

export const useIssueInvoice = () => {
  const queryClient = useQueryClient();
  return useMutation(
    async (args: InvoiceIssueFormValues) =>
      issuingInvoiceAPI.issueInvoice(fromFormValuesToParam(args)),

    {
      onSuccess: async () => {
        await queryClient.invalidateQueries("recipients");
      },
    },
  );
};
