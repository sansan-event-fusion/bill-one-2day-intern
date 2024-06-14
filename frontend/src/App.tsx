import React, { FC } from "react";
import "./App.css";
import { AppRoute } from "src/router/router";
import { MantineProvider } from "@mantine/core";
import { QueryClient, QueryClientProvider } from "react-query";
import { ConfirmModalProvider } from "src/util/ConfirmModal";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export const App: FC = () => {
  const queryClient = new QueryClient();
  return (
    <MantineProvider>
      <QueryClientProvider client={queryClient}>
        <ConfirmModalProvider>
          <AppRoute />
          <ToastContainer />
        </ConfirmModalProvider>
      </QueryClientProvider>
    </MantineProvider>
  );
};
