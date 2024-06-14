import {FONT_SIZE, MARGIN, PdfGenerator} from "../../../lib/pdf-generator";
import {RenderToPdfResult, StartPosition, TemplateBlock} from "../../shared/TemplateBlock";
import {InvoiceViewModel} from "../../../view-model/invoice/InvoiceViewModel";

export class RecipientInfoBlock implements TemplateBlock {
  invocieViewModel: InvoiceViewModel

  constructor(invoiceViewModel: InvoiceViewModel) {
    this.invocieViewModel = invoiceViewModel
  }

  renderToPdf(pdf: PdfGenerator, startPosition: StartPosition): RenderToPdfResult {
    const { endY: organizationNameEndY } = this.renderOrganizationName(pdf, startPosition)
    const { endY: registrationNumberEndY } = this.renderRegistrationNumber(pdf, {
      startX: startPosition.startX,
      startY: organizationNameEndY,
    })
    return this.renderContactName(pdf, {
      startX: startPosition.startX,
      startY: registrationNumberEndY,
    })
  }

  private renderOrganizationName(pdf: PdfGenerator, startPosition: StartPosition): RenderToPdfResult {
    return pdf.text({
      position: startPosition,
      text: {
        text: "Yonyon株式会社",
        fontStyle: "bold",
        fontSize: FONT_SIZE.HEADER_4,
      },
    })
  }

  private renderRegistrationNumber(pdf: PdfGenerator, { startX, startY }: StartPosition): RenderToPdfResult {
    // if (this..getSupplierAddress().isEmptyRegistrationNumber()) {
    //   return { endY: startY }
    // }

    return pdf.text({
      position: {
        startX: startX,
        startY: startY + MARGIN.SMALL * 1.5,
      },
      text: {
        text: "登録番号: T1234567890123"//this.selfBillingInvoice.getSupplierAddress().getRegistrationNumber(),
      },
    })
  }

  private renderContactName(pdf: PdfGenerator, { startX, startY }: StartPosition): RenderToPdfResult {
    return pdf.text({
      position: {
        startX: startX,
        startY: startY + MARGIN.SMALL * 1.5,
      },
      text: {
        text: "テスト受領者 様"//this.selfBillingInvoice.getSupplierAddress().getContactName(),
      },
    })
  }
}
