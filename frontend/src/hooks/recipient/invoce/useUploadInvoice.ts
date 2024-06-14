import { useMutation, useQueryClient } from "react-query";
import { RecipientInvoiceUploadFormValues } from "src/hooks/useRecipientInvoiceUploadForm";
import { recipientInvoiceAPI } from "src/api/recipient/invoice-api";

export const useUploadInvoice = () => {
  const queryClient = useQueryClient();
  return useMutation(
    async (args: RecipientInvoiceUploadFormValues) =>
      recipientInvoiceAPI.upload(args),
    {
      onSuccess: async () => {
        await queryClient.invalidateQueries("invoices");
      },
    },
  );
};
