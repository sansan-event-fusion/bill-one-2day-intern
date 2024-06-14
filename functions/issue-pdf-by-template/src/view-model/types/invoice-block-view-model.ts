import {PdfGenerator} from "../../lib/pdf-generator";

export type RenderToPdfResult ={
    endY:number
}
export type RenderOption = {
    tableWidth: number
}

export interface InvoiceBlockViewModel {
    renderToPdf(pdfGenerator: PdfGenerator, position: { startY: number }, renderOption: RenderOption): RenderToPdfResult
}