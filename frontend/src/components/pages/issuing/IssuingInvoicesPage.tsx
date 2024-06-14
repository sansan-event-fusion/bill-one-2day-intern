import { FC, useState } from "react";
import { IssuingHeader } from "src/components/templates/issuing/IssuingHeader";
import { GlobalFooter } from "src/components/templates/GlobalFooter";
import { IssuingInvoicesTable } from "src/components/organisms/issuing/invoice/IssuingInvoicesTable";
import { Flex } from "@mantine/core";
import { RecipientInvoicePdfArea } from "src/components/organisms/RecipientInvoicePdfArea";
import "@mantine/core/styles.css";
import { IssuerInvoice } from "src/domain/models/invoice";
import { useIssuedInvoice } from "src/hooks/issuing/invoice/useIssuedInvoice";

//  TODO(FE): 課題1
export const IssuingInvoicesPage: FC = () => {
  const [selectedInvoice, setSelectedInvoice] = useState<
    IssuerInvoice | undefined
  >(undefined);

  const handleChangeInvoice = (invoice: IssuerInvoice) => {
    setSelectedInvoice(invoice);
  };

  return (
    <>
      <IssuingHeader />
      <div style={{ width: "100%", margin: "0 2%" }}>
        <h2>発行済請求書一覧</h2>
        <Flex>
          <div style={{ width: "55%", marginRight: "1%" }}>
          </div>
          <RecipientInvoicePdfArea
            invoiceResource={selectedInvoice?.url}
            width={40}
            height={75}
          />
        </Flex>
      </div>
      <GlobalFooter />
    </>
  );
};
