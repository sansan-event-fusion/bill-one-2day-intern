import { FC } from "react";
import { Table } from "@mantine/core";
import { Invoice } from "src/domain/models/invoice";
import { TransParentButton } from "../../../atoms/TransParentButton";
import { BiDetail } from "react-icons/bi";
import "@mantine/core/styles.css";
type RecipientInvoicesTableProps = {
  data: Invoice[];
  selectedInvoice: Invoice | null;
  handleChangeInvoice: (invoice: Invoice) => void;
  handleOpenInvoiceEditModal: (invoice: Invoice) => void;
};

export const RecipientInvoicesTable: FC<RecipientInvoicesTableProps> = ({
  data,
  selectedInvoice,
  handleChangeInvoice,
  handleOpenInvoiceEditModal,
}) => {
  return (
    <>
      <Table stickyHeader={true} striped={true}>
        <Table.Thead bg={"#F5F5F5"} h={"10%"}>
          <Table.Tr>
            <Table.Th></Table.Th>
            <Table.Th>取引先</Table.Th>
            <Table.Th>支払期日</Table.Th>
            <Table.Th>金額</Table.Th>
            <Table.Th>所有者</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {data.map((invoice) => {
            return (
              <Table.Tr
                key={invoice.invoiceUUID}
                onClick={() => handleChangeInvoice(invoice)}
                bg={
                  selectedInvoice?.invoiceUUID === invoice.invoiceUUID
                    ? "#F7F7FC"
                    : "white"
                }
              >
                <Table.Td>
                  <TransParentButton
                    onClick={() => handleOpenInvoiceEditModal(invoice)}
                  >
                    <BiDetail style={{ color: "#2185D0", fontSize: "1.5em" }} />
                  </TransParentButton>
                </Table.Td>
                <Table.Td>{invoice.supplier}</Table.Td>
                <Table.Td>{invoice.paymentDeadline?.toString()}</Table.Td>
                <Table.Td>{invoice.amount}</Table.Td>
                <Table.Td>{invoice.recipientName}</Table.Td>
              </Table.Tr>
            );
          })}
        </Table.Tbody>
      </Table>
    </>
  );
};
