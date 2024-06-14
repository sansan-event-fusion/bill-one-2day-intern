import { PdfGenerator } from "../../lib/pdf-generator"

export type StartPosition = {
  startX: number
  startY: number
}
export type RenderToPdfResult = {
  endY: number
}

/**
 * 各Blockで利用するInterface
 */
export interface TemplateBlock {
  renderToPdf(pdf: PdfGenerator, position: StartPosition): RenderToPdfResult
}
