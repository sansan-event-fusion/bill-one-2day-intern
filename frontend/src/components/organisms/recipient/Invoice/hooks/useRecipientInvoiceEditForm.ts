import { useForm } from "@mantine/form";
import { Invoice } from "src/domain/models/invoice";

export type RecipientInvoiceEditFormValues = {
  invoiceUUID: string | undefined;
  invoiceAmount: number | undefined;
  supplierUUID: string | undefined;
  paymentDeadline: Date | undefined;
};

export const useRecipientInvoiceEditForm = (
  invoice: Invoice,
  submit: (invoice: RecipientInvoiceEditFormValues) => Promise<void>,
) => {
  const { values, getInputProps, onSubmit } =
    useForm<RecipientInvoiceEditFormValues>();
  // TODO: 課題2
  const handleSubmit = () => {};

  return {
    values,
    submit: handleSubmit,
    getInputProps,
  };
};
