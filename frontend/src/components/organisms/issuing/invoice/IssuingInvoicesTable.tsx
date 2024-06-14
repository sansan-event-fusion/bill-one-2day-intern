import { Table } from "@mantine/core";
import { FC } from "react";
import { IssuerInvoice } from "src/domain/models/invoice";

type IssuingInvoicesTableProps = {
  data: IssuerInvoice[];
  selectedInvoice: IssuerInvoice | undefined;
  handleChangeInvoice: (inovice: IssuerInvoice) => void;
};

export const IssuingInvoicesTable: FC<IssuingInvoicesTableProps> = ({
  data,
  selectedInvoice,
  handleChangeInvoice,
}) => {
  console.log(data);
  return (
    <Table stickyHeader={true} striped={true}>
      <Table.Thead bg={"#F5F5F5"} h={"10%"}>
        <Table.Tr>
          <Table.Th>取引先</Table.Th>
          <Table.Th>担当者</Table.Th>
          <Table.Th>支払期日</Table.Th>
          <Table.Th>金額</Table.Th>
          <Table.Th>発行者</Table.Th>
        </Table.Tr>
      </Table.Thead>
      <Table.Tbody>
        {data.map((invoice) => {
          return (
            <Table.Tr
              key={invoice.issuerInvoiceUUID}
              style={{ height: "50px !important" }}
              bg={
                selectedInvoice?.issuerInvoiceUUID === invoice.issuerInvoiceUUID
                  ? "#F7F7FC"
                  : "white"
              }
              onClick={() => handleChangeInvoice(invoice)}
            >
              <Table.Td>{invoice.supplierTenantNameId}</Table.Td>
              <Table.Td>{invoice.supplierName}</Table.Td>
              <Table.Td>
                {invoice.paymentDeadline.toLocaleDateString()}
              </Table.Td>
              <Table.Td>{invoice.amount}</Table.Td>
              <Table.Td>{invoice.issuerName}</Table.Td>
            </Table.Tr>
          );
        })}
      </Table.Tbody>
    </Table>
  );
};
