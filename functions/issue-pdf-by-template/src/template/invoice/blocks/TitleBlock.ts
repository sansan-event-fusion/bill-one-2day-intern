import {InvoiceViewModel} from "../../../view-model/invoice/InvoiceViewModel";
import {FONT_SIZE, MARGIN, PdfGenerator} from "../../../lib/pdf-generator";
import {InvoiceBlockViewModel, RenderToPdfResult} from "../../../view-model/types/invoice-block-view-model";
import {StartPosition} from "../../shared/TemplateBlock";

export class TitleBlock implements InvoiceBlockViewModel {
    invoice: InvoiceViewModel

    constructor(invoice: InvoiceViewModel){
        this.invoice = invoice
    }

    renderToPdf(pdf: PdfGenerator, startPosition: StartPosition): RenderToPdfResult {
        return pdf.text({
            position: startPosition,
            text: {
                text: this.invoice.getTitle(),
                fontStyle: "bold",
                fontSize: FONT_SIZE.HEADER_1,
                maxWidth: pdf.getPdfWidth() - MARGIN.OUTLINE * 2,
            },
        })
    }

}