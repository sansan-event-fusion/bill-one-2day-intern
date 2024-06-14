import "react-toastify/dist/ReactToastify.css";

import { Button, Select } from "@mantine/core";
import React, { FC } from "react";
import { PdfDropZone } from "src/components/molcures/PdfDropZone";
import { useRecipients } from "src/hooks/recipient/useRecipient";
import {
  RecipientInvoiceUploadFormValues,
  useRecipientInvoiceUploadForm,
} from "src/hooks/useRecipientInvoiceUploadForm";
import { useSuppliers } from "src/hooks/recipient/useSuppliers";
import { useUploadInvoice } from "src/hooks/recipient/invoce/useUploadInvoice";
import { toast, ToastContainer } from "react-toastify";
import { useAuth } from "src/util/useAuth";

type RecipientInvoiceUploadFromProps = {
  onFileDrop: (file: File) => void;
};
export const RecipientInvoiceUploadForm: FC<
  RecipientInvoiceUploadFromProps
> = ({ onFileDrop }) => {
  const supplier = useSuppliers();
  const invoiceOwner = useRecipients();
  const { getTenantNameId } = useAuth();
  const { getInputProps, onSubmit, setFieldValue, reset } =
    useRecipientInvoiceUploadForm();

  const handleOnDrop = (files: File[]) => {
    //  TODO(FE): 課題3
  };

  const handleSubmit = async (value: RecipientInvoiceUploadFormValues) => {
    //  TODO(FE): 課題3
  };

  return (
    <>
      <p>選択中のテナント:{getTenantNameId()}</p>
      <form>

        <PdfDropZone onDrop={handleOnDrop} />
        <br />
        <Button type={"submit"}>送信</Button>
      </form>
    </>
  );
};
