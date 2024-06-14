import { useForm } from "@mantine/form";

export type InvoiceIssueFormValues = {
  invoiceAmount: number | null;
  recipientUUID: string | null;
  paymentDeadline: Date | null;
};
export const useInvoiceIssueForm = () => {
  const { values, errors, getInputProps, onSubmit, reset } =
    useForm<InvoiceIssueFormValues>({
      initialValues: {
        invoiceAmount: null,
        recipientUUID: null,
        paymentDeadline: null,
      },
      validate: {
        invoiceAmount: (value) => {
          if (value === null) {
            return "請求金額を入力してください";
          }
          return value < 0 ? "請求金額は0以上の数値で入力してください" : null;
        },
        recipientUUID: (value) => {
          if (value === null || value === "") {
            return "取引先を入力してください";
          }
          return value === "" ? "取引先を入力してください" : null;
        },
        paymentDeadline: (value) => {
          if (value === null) {
            return "支払期日を入力してください";
          }
        },
      },
    });

  return {
    values,
    errors,
    reset,
    getInputProps,
    onSubmit,
  };
};
