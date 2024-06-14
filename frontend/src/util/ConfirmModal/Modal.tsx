import React, { FC, ReactNode } from "react";
import { Button, Group, MantineSize, Modal } from "@mantine/core";

type ConfirmModalProps = {
  opened: boolean;
  onCancel: () => void;
  onConfirm: () => void;
  onClose: () => void;
  message?: ReactNode;
  cancelButtonLabel?: string;
  confirmButtonLabel?: string;
  headerMessage?: string;
  size?: MantineSize;
};
export const ConfirmModal: FC<ConfirmModalProps> = ({
  opened,
  onCancel,
  onClose,
  onConfirm,
  message,
  cancelButtonLabel = "キャンセル",
  confirmButtonLabel = "OK",
  headerMessage = "確認",
  size = "xl",
}) => {
  return (
    <Modal onClose={onClose} opened={opened} size={size} title={headerMessage}>
      {message}
      <Group mt={"xl"} justify={"right"}>
        <Button variant={"default"} onClick={onCancel}>
          {cancelButtonLabel}
        </Button>
        <Button onClick={onConfirm}>{confirmButtonLabel}</Button>
      </Group>
    </Modal>
  );
};
