import {RenderToPdfResult, StartPosition, TemplateBlock} from "../../shared/TemplateBlock";
import {InvoiceViewModel} from "../../../view-model/invoice/InvoiceViewModel";
import {FONT_SIZE, MARGIN, PdfGenerator} from "../../../lib/pdf-generator";

export class IssuerInfoBlock implements TemplateBlock {
  private invoiceViewModel: InvoiceViewModel

  constructor(invoiceViewModel: InvoiceViewModel) {
    this.invoiceViewModel = invoiceViewModel
  }

  public renderToPdf(pdf: PdfGenerator, startPosition: StartPosition): RenderToPdfResult {
    const { startX } = startPosition
    const { endY: companyNameEndY } = this.renderCompanyName(pdf, startPosition)
    const { endY: zipCodeEndY } = this.renderZipCode(pdf, { startX, startY: companyNameEndY })
    const { endY: addressEndY } = this.renderAddress(pdf, { startX, startY: zipCodeEndY })
    return this.renderPhoneNumber(pdf, { startX, startY: addressEndY })
  }

  private renderCompanyName(pdf: PdfGenerator, startPosition: StartPosition): RenderToPdfResult {
    return pdf.text({
      position: startPosition,
      text: {
        text: this.invoiceViewModel.getTitle(),
        fontStyle: "bold",
        textAlign: "RIGHT",
        fontSize: FONT_SIZE.HEADER_4,
      },
    })
  }

  private renderZipCode(pdf: PdfGenerator, { startX, startY }: StartPosition): RenderToPdfResult {
    return pdf.text({
      position: {
        startX,
        startY: startY + MARGIN.SMALL * 2,
      },
      text: {
        text: "〒150-0001",//this.invoiceViewModel.spec.ts.getIssuerCompany(),
        textAlign: "RIGHT",
      },
    })
  }

  private renderAddress(pdf: PdfGenerator, { startX, startY }: StartPosition): RenderToPdfResult {
    const { endY: address1EndY } = pdf.text({
      position: {
        startX,
        startY: startY + MARGIN.SMALL,
      },
      text: {
        text: "東京都渋谷区神宮前5-52-2",//this.invoiceViewModel.spec.ts.getIssuerCompany().getAddress1(),
        textAlign: "RIGHT",
      },
    })

    return pdf.text({
      position: {
        startX,
        startY: address1EndY,
      },
      text: {
        text: "青山オーバルビル 13F",//this.invoiceViewModel.spec.ts.getIssuerCompany().getAddress2(),
        textAlign: "RIGHT",
      },
    })
  }

  private renderPhoneNumber(pdf: PdfGenerator, { startX, startY }: StartPosition): RenderToPdfResult {
    return pdf.text({
      position: {
        startX,
        startY: startY + MARGIN.SMALL * 1.5,
      },
      text: {
        text: "xxx-xxxx-xxxx",//this.invoiceViewModel.spec.ts.getIssuerCompany().getPhoneNumber(),
        textAlign: "RIGHT",
      },
    })
  }
}
