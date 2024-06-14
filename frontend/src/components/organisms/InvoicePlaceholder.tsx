import { FC } from "react";
import { Center } from "@mantine/core";

type InvoicePlaceholderProps = {
  width: string;
  height: string;
};
export const InvoicePlaceholder: FC<InvoicePlaceholderProps> = ({
  width,
  height,
}) => {
  return (
    <Center
      w={width}
      h={height}
      style={{
        zIndex: 10,
        color: "white",
        fontWeight: "bold",
        fontSize: "xl",
        textShadow:
          "2px 2px 10px #666, -2px 2px 10px #666, 2px -2px 10px #666, -2px -2px 10px #666",
        backgroundColor: "rgba(128, 128, 128, 0.7)",
        padding: "4px",
        whiteSpace: "pre-wrap",
      }}
    >
      請求書を選択するとここに表示されます
    </Center>
  );
};
