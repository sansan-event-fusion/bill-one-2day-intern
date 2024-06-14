import {Color, FontWeight, MARGIN, PdfGenerator} from "../../../lib/pdf-generator";
import {RenderToPdfResult, StartPosition, TemplateBlock} from "../../shared/TemplateBlock";
import {InvoiceViewModel} from "../../../view-model/invoice/InvoiceViewModel";
import {BILL_ONE_COLOR} from "../../../lib/pdf-generator/color";

type Row = [string, string, string, string]
type CustomFillColor = [Color | undefined, Color | undefined, Color | undefined, Color | undefined]
type CustomFontColor = [Color | undefined, Color | undefined, Color | undefined, Color | undefined]
type CustomFontStyle = [FontWeight | undefined, FontWeight | undefined, FontWeight | undefined, FontWeight | undefined]
type TaxDetailForTable =
  | {
      row: Row
      customFillColor: CustomFillColor
      customFontColor: CustomFontColor
      customFontStyle: CustomFontStyle
    }
  | undefined

export class TaxAmountDetailsBlock implements TemplateBlock {
  invoiceViewModel: InvoiceViewModel

  constructor(invoiceViewModel: InvoiceViewModel) {
    this.invoiceViewModel = invoiceViewModel
  }

  renderToPdf(pdf: PdfGenerator, startPosition: StartPosition): RenderToPdfResult {
    return this.renderTaxAmountDetails(pdf, startPosition)
  }

  private renderTaxAmountDetails(pdf: PdfGenerator, position: StartPosition): RenderToPdfResult {
    const taxDetailForTables: TaxDetailForTable[] = [
      this.getTaxRate10(),
      this.getTaxRate8(),
      this.getTaxRate5(),
      this.getTaxRate3(),
      this.getTaxRateH(),
      this.getTotal(),
      this.getInvoiceAmount(),
    ]

    const initialData: [Row[], CustomFillColor[], CustomFontColor[], CustomFontStyle[]] = [[], [], [], []]
    const [rows, customFillColors, customFontColors, customFontStyles]: [
      Row[],
      CustomFillColor[],
      CustomFontColor[],
      CustomFontStyle[],
    ] = taxDetailForTables.reduce((acc, current) => {
      if (current === undefined) {
        return acc
      }

      return [
        [...acc[0], current.row],
        [...acc[1], current.customFillColor],
        [...acc[2], current.customFontColor],
        [...acc[3], current.customFontStyle],
      ]
    }, initialData)

    const tableWidth = pdf.getPdfWidth() * 0.6
    return pdf.table({
      startY: position.startY + MARGIN.OUTLINE,
      bodies: rows,
      styles: {
        tableMargin: {
          left: MARGIN.OUTLINE,
        },
        tableWidth: tableWidth,
        tableBorderWidth: { top: 3, right: 0, bottom: 0.3, left: 0 },
        customMinCellWidth: [20, 100, 60, 100],
      },
      headerStyles: {},
      bodiesStyles: {
        fillColor: BILL_ONE_COLOR.WHITE,
        borderColor: { r: 170, g: 170, b: 170 },
        borderWidth: { top: 0.3, right: 0, bottom: 0, left: 0 },
        padding: { top: 10, right: 0, bottom: 10, left: 0 },
        customHalign: ["center", "right", "center", "right"],
        customPadding: [
          undefined,
          { top: 0, right: 5, bottom: 0, left: 0 },
          undefined,
          { top: 0, right: 5, bottom: 0, left: 0 },
        ],
        customFillColors,
        customFontColors,
        customFontStyles,
      },
    })
  }

  private getTaxRate10(): TaxDetailForTable {
    if (this.invoiceViewModel.getTaxAmountDetails().isEmptyTaxRate10()) {
      return undefined
    }

    return this.wrapStyleFor4Columns([
      "合計(10％対象)",
      this.invoiceViewModel.getTaxExcludedAmount(),
      "消費税10%",
      this.invoiceViewModel.getTaxAmountDetails().getTaxRate10NetTotal(),
    ])
  }

  private getTaxRate8(): TaxDetailForTable {
    if (this.invoiceViewModel.getTaxAmountDetails().isEmptyTaxRate8()) {
      return undefined
    }

    return this.wrapStyleFor4Columns([
      "合計(8％対象)",
      this.invoiceViewModel.getTaxAmountDetails().getTaxRate8Total(),
      "消費税8%",
      this.invoiceViewModel.getTaxAmountDetails().getTaxRate8NetTotal(),
    ])
  }

  private getTaxRate5(): TaxDetailForTable {
    if (this.invoiceViewModel.getTaxAmountDetails().isEmptyTaxRate5()) {
      return undefined
    }

    return this.wrapStyleFor4Columns([
      "合計(5％対象)",
      this.invoiceViewModel.getTaxAmountDetails().getTaxRate5Total(),
      "消費税5%",
      this.invoiceViewModel.getTaxAmountDetails().getTaxRate5NetTotal(),
    ])
  }

  private getTaxRate3(): TaxDetailForTable {
    if (this.invoiceViewModel.getTaxAmountDetails().isEmptyTaxRate3()) {
      return undefined
    }

    return this.wrapStyleFor4Columns([
      "合計(3％対象)",
      this.invoiceViewModel.getTaxAmountDetails().getTaxRate3Total(),
      "消費税3%",
      this.invoiceViewModel.getTaxAmountDetails().getTaxRate3NetTotal(),
    ])
  }

  private getTaxRateH(): TaxDetailForTable {
    if (this.invoiceViewModel.getTaxAmountDetails().isEmptyTaxRateH()) {
      return undefined
    }

    return {
      row: ["合計(非課税)", "", "", this.invoiceViewModel.getTaxAmountDetails().getTaxRateHTotal()],
      customFillColor: [BILL_ONE_COLOR.BLACK, undefined, undefined, undefined],
      customFontColor: [BILL_ONE_COLOR.WHITE, undefined, undefined, undefined],
      customFontStyle: ["bold", undefined, undefined, undefined],
    }
  }

  private getTotal(): TaxDetailForTable {
    return this.wrapStyleFor4Columns([
      "合計",
      this.invoiceViewModel.getTaxExcludedAmount(),
      "消費税合計",
      this.invoiceViewModel.getTaxAmount(),
    ])
  }

  private getInvoiceAmount(): TaxDetailForTable {
    return {
      row: ["支払金額", "", "", `${this.invoiceViewModel.getTaxIncludedAmount()}`],
      customFillColor: [BILL_ONE_COLOR.BLACK, undefined, undefined, undefined],
      customFontColor: [BILL_ONE_COLOR.WHITE, undefined, undefined, undefined],
      customFontStyle: ["bold", undefined, undefined, undefined],
    }
  }

  private wrapStyleFor4Columns(row: Row): TaxDetailForTable {
    return {
      row,
      customFillColor: [BILL_ONE_COLOR.BLACK, undefined, BILL_ONE_COLOR.BLACK, undefined],
      customFontColor: [BILL_ONE_COLOR.WHITE, undefined, BILL_ONE_COLOR.WHITE, undefined],
      customFontStyle: ["bold", undefined, "bold", undefined],
    }
  }
}
