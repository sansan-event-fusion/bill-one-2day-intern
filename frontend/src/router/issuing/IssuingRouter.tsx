import React, { FC } from "react";
import { Route, Routes } from "react-router-dom";
import { IssuingInvoicesPage } from "src/components/pages/issuing/IssuingInvoicesPage";
import { InvoiceIssuingPage } from "src/components/pages/issuing/InvoiceIssuingPage";
import { NotFoundPage } from "src/components/pages/NotFound";
import { IssuingRouteSuspense } from "src/router/issuing/IssuingRouteSuspense";

export const IssuingRouter: FC = () => {
  return (
    <Routes>
      <Route element={<IssuingRouteSuspense />}>
        <Route path="/invoices" element={<IssuingInvoicesPage />} />
        <Route path="/issue" element={<InvoiceIssuingPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  );
};
