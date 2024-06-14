import {RenderToPdfResult, StartPosition, TemplateBlock} from "../../shared/TemplateBlock";
import {InvoiceViewModel} from "../../../view-model/invoice/InvoiceViewModel";
import {FONT_SIZE, MARGIN, PdfGenerator} from "../../../lib/pdf-generator";
import {BILL_ONE_COLOR} from "../../../lib/pdf-generator/color";
import timezone from 'dayjs/plugin/timezone'
import utc from 'dayjs/plugin/utc'
import dayjs from "dayjs";
dayjs.extend(timezone)
dayjs.extend(utc)

export class IssueInfoBlock implements TemplateBlock {
  private invoiceViewModel: InvoiceViewModel

  constructor(invoiceViewModel: InvoiceViewModel) {
    this.invoiceViewModel = invoiceViewModel
  }

  public renderToPdf(pdf: PdfGenerator, startPosition: StartPosition): RenderToPdfResult {
    const { endY: invoiceAmountEndY } = this.renderInvoiceAmount(pdf, startPosition)
    const { endY: invoiceNumberEndY } = this.renderInvoiceNumber(pdf, startPosition)
    const { endY: issueDateEndY } = this.renderIssueDate(pdf, {
      startX: startPosition.startX,
      startY: invoiceNumberEndY,
    })
    return {
      endY: invoiceAmountEndY > issueDateEndY ? invoiceAmountEndY : issueDateEndY,
    }
  }

  private renderInvoiceAmount(pdf: PdfGenerator, startPosition: StartPosition): RenderToPdfResult {
    const { backgroundWidth } = pdf.textWithBackground({
      position: startPosition,
      text: {
        text: "お支払金額",
        fontStyle: "bold",
        fontSize: FONT_SIZE.HEADER_4,
        textColor: BILL_ONE_COLOR.WHITE,
      },
      background: {
        backgroundType: "FILL",
        padding: 12,
        backgroundWidth: 75,
        backgroundFillColor: BILL_ONE_COLOR.BLACK,
      },
    })

    return pdf.textWithBackground({
      position: {
        startX: startPosition.startX + backgroundWidth,
        startY: startPosition.startY,
      },
      text: {
        text: this.invoiceViewModel.getTaxIncludedAmount(),
        fontStyle: "bold",
        fontSize: FONT_SIZE.HEADER_4,
        textAlign: "RIGHT",
      },
      background: {
        backgroundType: "STROKE",
        backgroundWidth: pdf.getPdfWidth() * 0.6 - backgroundWidth,
        padding: 12,
      },
    })
  }

  private renderInvoiceNumber(pdf: PdfGenerator, startPosition: StartPosition): RenderToPdfResult {
    return pdf.text({
      position: startPosition,
      text: {
        text: `請求書番号：invoice-0123`,
        textAlign: "RIGHT",
      },
    })
  }

  private renderIssueDate(pdf: PdfGenerator, { startX, startY }: StartPosition): RenderToPdfResult {
    return pdf.text({
      position: {
        startX,
        startY: startY + MARGIN.SMALL * 1.5,
      },
      text: {
        text: `発行日：${dayjs(Date.now()).tz("Asia/Tokyo").format("YYYY年MM月DD日")}`,
        textAlign: "RIGHT",
      },
    })
  }
}
