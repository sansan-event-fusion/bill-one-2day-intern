import React, { FC, useState } from "react";
import { IssuingHeader } from "src/components/templates/issuing/IssuingHeader";
import { GlobalFooter } from "src/components/templates/GlobalFooter";
import { InvoiceIssueForm } from "src/components/organisms/issuing/invoice/InvoiceIssueForm";
import { Select } from "@mantine/core";

export const InvoiceIssuingPage: FC = () => {
  const [recipientTenant, setRecipientTenant] = useState<string | null>(null);

  return (
    <>
      <IssuingHeader />
      <div style={{ height: "90%" }}>
        <div style={{ marginLeft: "3%" }}>
          <h2>請求書発行</h2>
        </div>
        {recipientTenant !== null ? (
          <InvoiceIssueForm recipientTenant={recipientTenant} />
        ) : (
          <Select
            label="取引先のテナントを選択"
            data={["yonyon", "gogo", "rokuroku"]}
            onChange={setRecipientTenant}
          />
        )}
      </div>
      <GlobalFooter />
    </>
  );
};
