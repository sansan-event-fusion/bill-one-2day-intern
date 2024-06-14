import {Title} from "../model/Title";
import {FONT_SIZE, MARGIN, PdfGenerator} from "../lib/pdf-generator";
import {RenderTextPosition, RenderTextTextData} from "../lib/pdf-generator/text";
import {InvoiceBlockViewModel} from "./types/invoice-block-view-model";

export class InvoiceTitleViewModel implements InvoiceBlockViewModel {
    private readonly title: Title
    private readonly  startX = MARGIN.OUTLINE
    private readonly fontSize =FONT_SIZE.HEADER_1


    renderToPdf(pdfGenerator: PdfGenerator, position: { startY: number }) {
        return pdfGenerator.text({
            position: {
                startX: MARGIN.OUTLINE,
                startY: position.startY + MARGIN.OUTLINE,
            },
            text: this.calculateTextData(pdfGenerator.getPdfWidth()),
        })
    }

    private get text(): string {
        return this.title.getTitle()
    }

    private calculatePosition(startY: number): RenderTextPosition {
        return {
            startX: this.startX,
            startY: this.calculateStartY(startY),
        }
    }

    private calculateTextData(internalPageSizeWidth: number): RenderTextTextData {
        return {
            text: this.text,
            fontStyle: "bold",
            fontSize: this.fontSize,
            maxWidth: internalPageSizeWidth - MARGIN.OUTLINE * 2,
        }
    }

    private calculateStartY(startY: number) {
        return startY + MARGIN.OUTLINE
    }

    constructor(title: Title) {
        this.title = title
    }
}