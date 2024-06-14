import React, { FC } from "react";
import {
  Box,
  Button,
  Flex,
  Input,
  Modal,
  NumberInput,
  Select,
} from "@mantine/core";
import {
  RecipientInvoiceEditFormValues,
  useRecipientInvoiceEditForm,
} from "src/components/organisms/recipient/Invoice/hooks/useRecipientInvoiceEditForm";
import { Invoice } from "src/domain/models/invoice";
import { toast } from "react-toastify";
import { Tenant } from "src/domain/models/tenant";
import { useUpdateInvoice } from "src/hooks/recipient/invoce/useUpdateInvoice";

type RecipientInvoiceEditModalProps = {
  isOpen: boolean;
  onClose: () => void;
  suppliers: Tenant[];
  invoice: Invoice;
};

export const RecipientInvoiceEditModal: FC<RecipientInvoiceEditModalProps> = ({
  isOpen,
  onClose,
  suppliers,
  invoice,
}) => {
  const { mutateAsync: update } = useUpdateInvoice();

  //  TODO(FE): 課題2
  const handleSubmit = async (values: RecipientInvoiceEditFormValues) => {
    try {

    } catch (e) {
      console.error("Error during submit:", e); // エラー詳細をログに出力
      toast.error("請求書の更新に失敗しました。", {
        position: "bottom-left",
      });
    }
  };

  const { values, getInputProps, submit } = useRecipientInvoiceEditForm(
    invoice,
    handleSubmit,
  );

  return (
    <Modal.Root opened={isOpen} onClose={onClose}>
      <Modal.Overlay />
      <Modal.Content>
        <Modal.Header>
          <Modal.Title>請求書の編集</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form>
            <div>
              <label htmlFor={"invoiceAmount"}>請求金額</label>
              <NumberInput
                name={"invoiceAmount"}
                defaultValue={values.invoiceAmount}
                {...getInputProps("invoiceAmount")}
              />
              <label htmlFor={"supplierUUID"}>取引先</label>
              <Select
                name={"supplierUUID"}
                value={values.supplierUUID}
                data={suppliers.map((it) => {
                  return { value: it.tenantUUID, label: it.tenantNameId };
                })}
                {...getInputProps("supplierUUID")}
              />
              <label htmlFor={"paymentDeadline"}>支払期日</label>
              <Input
                name={"paymentDeadline"}
                type={"date"}
                value={values.paymentDeadline}
                {...getInputProps("paymentDeadline")}
              />
            </div>
            <Box>
              <Flex justify={"flex-end"} gap={"md"} m={"10"}>
                <Button onClick={onClose}>キャンセル</Button>
                <Button type="submit">更新</Button>
              </Flex>
            </Box>
          </form>
        </Modal.Body>
      </Modal.Content>
    </Modal.Root>
  );
};
