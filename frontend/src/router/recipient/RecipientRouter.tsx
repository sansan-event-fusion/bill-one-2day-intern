import React, { FC } from "react";
import { Route, Routes } from "react-router-dom";
import { RecipientInvoicesPage } from "src/components/pages/recipient/RecipientInvoicesPage";
import { RecipientInvoiceUploadPage } from "src/components/pages/recipient/RecipientInvoiceUploadPage";
import { NotFoundPage } from "src/components/pages/NotFound";
import { RecipientRouteSuspense } from "src/router/recipient/RecipientRouteSuspense";

export const RecipientRouter: FC = () => {
  return (
    <Routes>
      <Route element={<RecipientRouteSuspense />}>
        <Route path="/invoices" element={<RecipientInvoicesPage />} />
        <Route path="/upload" element={<RecipientInvoiceUploadPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  );
};
