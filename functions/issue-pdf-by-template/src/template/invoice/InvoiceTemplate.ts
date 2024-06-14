import {MARGIN, PdfGenerator} from "../../lib/pdf-generator";
import {TitleBlock} from "./blocks/TitleBlock";
import {InvoiceViewModel} from "../../view-model/invoice/InvoiceViewModel";
import {InvoiceLinesBlock} from "./blocks/InvoiceLinesBlock";
import {TaxAmountDetailsBlock} from "./blocks/TaxAmountDetailsBlock";
import {IssuerInfoBlock} from "./blocks/IssuerInfoBlock";
import {IssueInfoBlock} from "./blocks/IssueInfoBlock";
import {RecipientInfoBlock} from "./blocks/RecipientAddressBlock";

export interface Template {
    renderToPdf(pdf: PdfGenerator): void
}

class RecipientInfo{
    constructor(
        private readonly recipientName: string,
        private  readonly recipientOrganizationName: string | null
    ){}
}

class IssuerInfo{
    constructor(
        private readonly issuerName: string,
        private readonly issuerOrganizationName: string | null,
        private readonly issuerRegistrationNumber: string
    ){}
}

class PaymentInfo{
    constructor(
        private readonly amount: number,
        private readonly publishedAt: Date,
        private readonly paymentDueDate: Date
    ){}
}
class Title{
    constructor(
        private readonly title: string
    ){}
}
class InvoiceNumber{
    constructor(
        private readonly invoiceNumber: string
    ) {}
}
export class InvoiceTemplate implements Template{

    private titleBlock: TitleBlock
    private recipientInfoBlock: RecipientInfoBlock
    private issuerInfoBlock: IssuerInfoBlock
    private invoiceLinesBlock: InvoiceLinesBlock
    private taxAmountDetailsBlock: TaxAmountDetailsBlock
    private issueInfoBlock: IssueInfoBlock


    renderToPdf(pdf: PdfGenerator) {
        // タイトル
        const { endY: titleEndY } = this.titleBlock.renderToPdf(pdf, {
            startX: MARGIN.OUTLINE,
            startY: MARGIN.OUTLINE,
        })

        // 受領者情報
        const {endY: recipientInfoEndY} = this.recipientInfoBlock.renderToPdf(pdf, {
            startX: MARGIN.OUTLINE,
            startY: titleEndY + MARGIN.OUTLINE,
        })

        // 発行者情報
        const {endY: issuerInfoEndY} = this.issuerInfoBlock.renderToPdf(pdf, {
            startX: MARGIN.OUTLINE,
            startY: titleEndY + MARGIN.OUTLINE,
        })

        // 支払い金額&日時情報
        const {endY: issueInfoEndY} = this.issueInfoBlock.renderToPdf(pdf, {
            startX: MARGIN.OUTLINE,
            startY: issuerInfoEndY + MARGIN.OUTLINE,
        })

        const { endY: taxAmountDetailsEndY } = this.taxAmountDetailsBlock.renderToPdf(pdf, {
            startX: MARGIN.OUTLINE,
            startY: issueInfoEndY + MARGIN.OUTLINE,
        })

        // 請求明細
        this.invoiceLinesBlock.renderToPdf(pdf, {
            startX: MARGIN.OUTLINE,
            startY: taxAmountDetailsEndY + MARGIN.OUTLINE,
        })

    }
    constructor(
        invoice: InvoiceViewModel
    ){
        this.titleBlock = new TitleBlock(invoice)
        this.issuerInfoBlock = new IssuerInfoBlock(invoice)
        this.invoiceLinesBlock = new InvoiceLinesBlock(invoice)
        this.taxAmountDetailsBlock = new TaxAmountDetailsBlock(invoice)
        this.issueInfoBlock = new IssueInfoBlock(invoice)
        this.recipientInfoBlock = new RecipientInfoBlock(invoice)
    }
}