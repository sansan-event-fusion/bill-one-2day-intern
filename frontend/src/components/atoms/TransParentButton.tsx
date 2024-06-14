import { FC, MouseEventHandler } from "react";
import { Button } from "@mantine/core";
import { ButtonProps } from "@mantine/core/lib/components/Button/Button";

type TransParentButtonProps = ButtonProps & {
  // なぜかコンパイラに怒られるのでここで明示する。
  onClick?: MouseEventHandler<HTMLButtonElement>;
};
export const TransParentButton: FC<TransParentButtonProps> = ({
  onClick,
  ...rest
}) => {
  return (
    <Button
      style={{
        backgroundColor: "transparent",
        border: "none",
        cursor: "pointer",
      }}
      {...rest}
      onClick={onClick}
    />
  );
};
