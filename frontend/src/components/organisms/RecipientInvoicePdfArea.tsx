import "@mantine/core/styles.css";
import { FC } from "react";
import { InvoicePlaceholder } from "src/components/organisms/InvoicePlaceholder";
import { useWindowSizeToPx } from "src/components/organisms/hooks/useWindowSizeToPx";

type RecipientInvoicePdfAreaProps = {
  invoiceResource: string | undefined | null;
  width: number;
  height: number;
};

export const RecipientInvoicePdfArea: FC<RecipientInvoicePdfAreaProps> = ({
  invoiceResource,
  width,
  height,
}) => {
  const { height: h, width: w } = useWindowSizeToPx(height, width);
  if (invoiceResource === "" || !invoiceResource)
    return <InvoicePlaceholder width={`${w}px`} height={`${h}px`} />;

  return (
    <embed
      src={invoiceResource}
      key={"pdfUrl"}
      type="application/pdf"
      width={`${w}px`}
      height={`${h}px`}
    />
  );
};
