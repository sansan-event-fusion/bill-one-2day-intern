import React, { ReactNode, useCallback, useRef, useState } from "react";
import { createContext } from "src/util/createContext";
import { ConfirmModal } from "src/util/ConfirmModal/Modal";
import { useDisclosure } from "@mantine/hooks";
import { MantineSize } from "@mantine/core";

type Resolve = (value: boolean) => void;

type ShowConfirmOptions = {
  submitButtonLabel?: string;
  cancelButtonLabel?: string;
  size?: MantineSize;
  headerMessage?: string;
};

type ConfirmContext = {
  showConfirm: (
    message: ReactNode,
    options?: ShowConfirmOptions,
  ) => Promise<boolean>;
};
const [ConfirmModalContextProvider, useConfirm] =
  createContext<ConfirmContext>();

// 型パズルが激しいわけではないが、特定domain、Componentでのみ使用するわけではないのでutilに配置している。
const ConfirmModalProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [message, setMessage] = useState<React.ReactNode>("");
  const [options, setOptions] = useState<ShowConfirmOptions>({});
  const resolveRef = useRef<Resolve>();
  const [opened, { open, close }] = useDisclosure();

  const showConfirm = useCallback(
    (
      message: React.ReactNode,
      options?: ShowConfirmOptions,
    ): Promise<boolean> => {
      setMessage(message);
      setOptions(options || {});
      open();
      return new Promise((resolve) => {
        resolveRef.current = resolve;
      });
    },
    [],
  );

  const onCancel = useCallback(() => {
    if (resolveRef.current) {
      close();
      resolveRef.current(false);
    }
  }, []);

  const onAgree = useCallback(() => {
    if (resolveRef.current) {
      close();
      resolveRef.current(true);
    }
  }, []);

  return (
    <ConfirmModalContextProvider value={{ showConfirm }}>
      {children}
      {opened && (
        <ConfirmModal
          message={message}
          opened={opened}
          onClose={close}
          onCancel={onCancel}
          onConfirm={onAgree}
          size={options.size}
          cancelButtonLabel={options.cancelButtonLabel}
          confirmButtonLabel={options.submitButtonLabel}
          headerMessage={options.headerMessage}
        />
      )}
    </ConfirmModalContextProvider>
  );
};

export { ConfirmModalProvider, useConfirm };
