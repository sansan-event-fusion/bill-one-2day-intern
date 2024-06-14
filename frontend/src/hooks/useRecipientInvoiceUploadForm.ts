import { useForm } from "@mantine/form";
import { Recipient } from "src/domain/models/recipient";

export type RecipientInvoiceUploadFormValues = {
  invoiceOwner: Recipient | null;
  supplier: string | null;
  pdf: File | null;
};

export const useRecipientInvoiceUploadForm = () => {
  const { values, getInputProps, onSubmit, setFieldValue, reset } =
    useForm<RecipientInvoiceUploadFormValues>({
      initialValues: {
        invoiceOwner: null,
        supplier: null,
        pdf: null,
      },
      validate: {
        invoiceOwner: (value) => {
          if (value === null || "") {
            return "取引先を選択してください";
          }
        },
        supplier: (value) => {
          if (value === null || "") {
            return "取引先を選択してください";
          }
        },
        pdf: (value) => {
          if (value === null) {
            return "ファイルを追加してください";
          }
          if (value.type !== "application/pdf") {
            return "PDFファイルを選択してください";
          }
          if (value.size > 10485760) {
            return "ファイルサイズが大きすぎます";
          }
        },
      },
    });

  return {
    values,
    getInputProps,
    onSubmit,
    setFieldValue,
    reset,
  };
};
