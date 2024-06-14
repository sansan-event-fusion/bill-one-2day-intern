import "@mantine/core/styles.css";
import React, { FC, useState } from "react";
import { RecipientHeader } from "src/components/templates/recipient/RecipientHeader";
import { RecipientInvoicePdfArea } from "src/components/organisms/RecipientInvoicePdfArea";
import { GlobalFooter } from "src/components/templates/GlobalFooter";
import { Flex } from "@mantine/core";
import { RecipientInvoicesTable } from "src/components/organisms/recipient/Invoice/RecipientInvoicesTable";
import { useInvoices } from "src/hooks/recipient/invoce/useInvoices";
import { Invoice } from "src/domain/models/invoice";
import { useDisclosure } from "@mantine/hooks";
import { RecipientInvoiceEditModal } from "src/components/organisms/recipient/Invoice/RecipientInvoiceEditModal";
import { useSuppliers } from "src/hooks/recipient/useSuppliers";

export const RecipientInvoicesPage: FC = () => {
  const [selectedInvoice, setSelectedInvoice] = React.useState<Invoice | null>(
    null,
  );
  const [invoiceForEdit, setInvoiceForEdit] = useState<Invoice | null>(null);
  const [opened, { open, close }] = useDisclosure(false);
  const invoices = useInvoices();

  const suppliers = useSuppliers();

  const handleChangeInvoice = (invoice: Invoice) => {
    setSelectedInvoice(invoice);
  };

  const handleOpenInvoiceEditModal = (invoice: Invoice) => {
    setInvoiceForEdit(invoice);
    open();
  };

  return (
    <>
      <RecipientHeader />
      <div style={{ width: "100%", margin: "0 2%" }}>
        <h2>請求書一覧</h2>
        <Flex>
          <div style={{ width: "55%", marginRight: "1%" }}>
            <RecipientInvoicesTable
              data={invoices}
              selectedInvoice={selectedInvoice}
              handleChangeInvoice={handleChangeInvoice}
              handleOpenInvoiceEditModal={handleOpenInvoiceEditModal}
            />
          </div>

          <RecipientInvoicePdfArea
            invoiceResource={selectedInvoice?.url}
            width={40}
            height={75}
          />
        </Flex>

        {invoiceForEdit && suppliers && (
          <RecipientInvoiceEditModal
            isOpen={opened}
            onClose={close}
            invoice={invoiceForEdit}
            suppliers={suppliers}
          />
        )}
      </div>
      <GlobalFooter />
    </>
  );
};
