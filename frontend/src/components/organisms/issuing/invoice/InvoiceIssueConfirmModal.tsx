import React, { FC } from "react";
import { Button, Group, Modal } from "@mantine/core";

type InvoiceIssueConfirmModalProps = {
  opened: boolean;
  onClose: () => void;
  submit: () => void;
};
export const InvoiceIssueConfirmModal: FC<InvoiceIssueConfirmModalProps> = ({
  opened,
  onClose,
  submit,
}) => {
  return (
    <Modal onClose={onClose} opened={opened} size={"md"} title={"確認"}>
      <p>請求書を発行しますか？</p>
      <p>発行すると取引先に請求書が送信されます。</p>
      <Group mt={"xl"} justify={"right"}>
        <Button variant={"default"} onClick={onClose}>
          キャンセル
        </Button>
        <Button onClick={() => submit()}>OK</Button>
      </Group>
    </Modal>
  );
};
