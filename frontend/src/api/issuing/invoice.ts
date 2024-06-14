import { InvoiceIssueFormValues } from "src/components/organisms/issuing/invoice/hooks/useInvoiceIssueForm";

export type InvoiceIssueParam = {
  invoiceAmount: number;
  recipientUUID: string;
  paymentDeadline: Date;
};

export const fromFormValuesToParam = (
  values: InvoiceIssueFormValues,
): InvoiceIssueParam => {
  if (
    values.invoiceAmount === null ||
    values.recipientUUID === null ||
    values.paymentDeadline === null
  ) {
    throw new Error("Invalid values");
  }

  return {
    invoiceAmount: values.invoiceAmount,
    recipientUUID: values.recipientUUID,
    paymentDeadline: values.paymentDeadline,
  };
};
