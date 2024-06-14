import "@mantine/core/styles.css";
import { Button } from "@mantine/core";
import { FC } from "react";
import { CommonHeader } from "src/components/templates/CommonHeader";

export const NotFoundPage: FC = () => {
  const handleJumpToHome = () => {
    window.location.href = "/";
  };
  return (
    <>
      <CommonHeader />
      <div>
        <h1>404 Not Found</h1>
        <Button onClick={handleJumpToHome}>トップページへ</Button>
      </div>
    </>
  );
};
