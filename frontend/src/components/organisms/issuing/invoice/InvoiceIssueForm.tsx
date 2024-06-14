import React, { FC } from "react";
import { Button, Flex, NumberInput, Select } from "@mantine/core";
import { DateInput } from "@mantine/dates";
import "@mantine/dates/styles.css";
import {
  InvoiceIssueFormValues,
  useInvoiceIssueForm,
} from "src/components/organisms/issuing/invoice/hooks/useInvoiceIssueForm";
import { useIssuer } from "src/hooks/issuing/useIssuer";
import { useIssueInvoice } from "src/hooks/issuing/invoice/useInvoiceIssue";
import { toast, ToastContainer } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { useConfirm } from "src/util/ConfirmModal";
import "react-toastify/dist/ReactToastify.css";
import { useSupplier } from "../../../../hooks/issuing/useSupplier";

type InvoiceIssueFormProps = {
  recipientTenant: string;
};
export const InvoiceIssueForm: FC<InvoiceIssueFormProps> = ({
  recipientTenant,
}) => {
  const account = useIssuer();
  const recipients = useSupplier(recipientTenant);

  const { mutateAsync: issueInvoice } = useIssueInvoice();

  const navigate = useNavigate();

  const { values, errors, getInputProps, reset, onSubmit } =
    useInvoiceIssueForm();

  const { showConfirm } = useConfirm();
  const handleSubmit = async (values: InvoiceIssueFormValues) => {
    if (!(await showConfirm("請求書を発行しますか？"))) {
      return;
    }

    const result = await issueInvoice(values);
    if (result.isSuccess) {
      toast("請求書の発行を開始しました。", {
        position: "bottom-left",
      });
      reset();
      navigate("/issuing/issue");
      return;
    }
    toast.error("請求書の発行に失敗しました。", {
      position: "bottom-left",
    });
  };

  const option = account.map((it) => {
    return {
      value: it.issuerUUID,
      label: it.fullName,
    };
  });

  const recipientOption = recipients.map((it) => {
    return {
      value: it.recipientUUID,
      label: it.fullName,
    };
  });

  return (
    <div style={{ marginLeft: "2%" }}>
      <p>選択中の取引先:{recipientTenant}</p>
      <form onSubmit={onSubmit((value) => handleSubmit(value))}>
        {option.length > 0 && (
          <Flex direction={"column"} w={"50%"}>
            <NumberInput
              label={"請求金額"}
              placeholder={"請求金額を入力"}
              error={errors.invoiceAmount}
              //ライブラリの仕様上nullではなくundefinedのため。
              value={values.invoiceAmount ?? undefined}
              {...getInputProps("invoiceAmount")}
            />
            <Select
              label={"取引先(受領者)"}
              placeholder={"発行先を選択"}
              error={errors.recipientUUID}
              data={recipientOption}
              value={values.recipientUUID ?? undefined}
              {...getInputProps("recipientUUID")}
            />
            <DateInput
              label={"支払い期日"}
              placeholder={"支払い期日を入力"}
              error={errors.paymentDeadline}
              value={values.paymentDeadline ?? undefined}
              {...getInputProps("paymentDeadline")}
            />
          </Flex>
        )}
        <br />
        <Button type={"submit"}>発行する</Button>
      </form>
      <ToastContainer />
    </div>
  );
};
