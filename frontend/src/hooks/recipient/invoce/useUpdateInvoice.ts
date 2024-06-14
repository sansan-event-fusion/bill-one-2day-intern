import { RecipientInvoiceEditFormValues } from "src/components/organisms/recipient/Invoice/hooks/useRecipientInvoiceEditForm";
import { useMutation, useQueryClient } from "react-query";
import { recipientInvoiceAPI } from "src/api/recipient/invoice-api";

export const useUpdateInvoice = () => {
  const queryClient = useQueryClient();
  return useMutation(
    async (args: RecipientInvoiceEditFormValues) => {
      console.log("use mutation is called");
      await recipientInvoiceAPI.updateInvoice(args);
    },
    {
      onSuccess: async () => {
        await queryClient.invalidateQueries("invoices");
      },
    },
  );
};
