import { PdfGenerator } from "../../lib/pdf-generator"

/**
 * Templateが利用するInterface
 */
export interface Template {
  renderToPdf(pdf: PdfGenerator): void
}
