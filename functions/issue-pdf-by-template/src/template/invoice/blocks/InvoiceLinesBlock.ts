import {RenderToPdfResult, StartPosition, TemplateBlock} from "../../shared/TemplateBlock";
import {InvoiceViewModel} from "../../../view-model/invoice/InvoiceViewModel";
import {MARGIN, PdfGenerator} from "../../../lib/pdf-generator";
import {BILL_ONE_COLOR} from "../../../lib/pdf-generator/color";

export class InvoiceLinesBlock implements TemplateBlock {
  private invoice: InvoiceViewModel

  constructor(invoice: InvoiceViewModel) {
    this.invoice = invoice
  }

  public renderToPdf(pdf: PdfGenerator, { startY }: StartPosition): RenderToPdfResult {
    if (this.invoice.getInvoiceLines().length === 0) {
      return { endY: startY }
    }

    return pdf.table({
      startY: startY,
      header: ["No.", "品名", "取引日付", "摘要", "数量", "税率", "単価", "金額"],
      bodies: this.invoice.getInvoiceLines().map((line) => {
        return [
          line.getNumber(),
          line.getTransactionDetails(),
          line.getTransactionDate(),
          line.getRemarks(),
          line.getQuantity(),
          line.getTaxRate(),
          line.getUnitPrice(),
           line.getAmount(),
        ]
      }),
      styles: {
        tableMargin: { right: MARGIN.OUTLINE, left: MARGIN.OUTLINE },
        tableBorderWidth: { top: 3, right: 0, bottom: 0.2, left: 0 },
        customMinCellWidth: [40, 40, 40, 40, 40, 35, 70, 70],
      },
      headerStyles: {
        fillColor: { r: 249, g: 250, b: 251 },
        borderColor: { r: 50, g: 50, b: 50 },
        borderWidth: { top: 3, right: 1, bottom: 0.2, left: 1 },
        padding: { top: 10, right: 10, bottom: 10, left: 10 },
      },
      bodiesStyles: {
        borderColor: BILL_ONE_COLOR.BLACK,
        borderWidth: { top: 0, right: 1, bottom: 0.2, left: 1 },
        padding: { top: 10, right: 0, bottom: 10, left: 0 },
        customHalign: ["center", "center", "center", "center", "center", "center", "right", "right"],
        customPadding: [
          undefined,
          undefined,
          undefined,
          undefined,
          undefined,
          undefined,
          { top: 0, right: 5, bottom: 0, left: 0 },
          { top: 0, right: 5, bottom: 0, left: 0 },
        ],
        fillColor: { r: 245, g: 245, b: 245 },
        alternateRowFillColor: BILL_ONE_COLOR.WHITE,
      },
    })
  }
}
