import {Invoice} from "../../model/Invoice";
import {PaymentInfo} from "../../model/PaymentInfo";
import {IssuerInfo} from "../../model/IssuerInfo";
import {RecipientInfo} from "../../model/RecipientInfo";
import BigNumber from "bignumber.js";
import {InvoiceAmountInfo, InvoiceTaxRate} from "../../model/InvoiceAmountInfo";

BigNumber.config({
    DECIMAL_PLACES: 0,                // 小数部2桁
    ROUNDING_MODE: BigNumber.ROUND_DOWN // 切り上げ
});

class TaxAmountDetail {
    constructor(
        private readonly taxRate: InvoiceTaxRate,
        private readonly totalAmount: BigNumber,
        private readonly taxAmount: BigNumber,
    ) {
    }

    public getTaxRate(): InvoiceTaxRate {
        return this.taxRate
    }

    public getTotalAmount(): BigNumber {
        return this.totalAmount
    }

    public getTaxAmount(): BigNumber {
        return this.taxAmount
    }
}

class TaxAmountDetails {
    private value: TaxAmountDetail[]

    constructor(value: TaxAmountDetail[]) {
        this.value = value
    }

    public isEmptyTaxRate10(): boolean {
        return this.isEmpty("10")
    }

    public getTaxRate10Total(): string {
        return this.getTotal("10")
    }

    public getTaxRate10NetTotal(): string {
        return this.getNetTotal("10")
    }

    public isEmptyTaxRate8(): boolean {
        return this.isEmpty("8")
    }

    public getTaxRate8Total(): string {
        return this.getTotal("8")
    }

    public getTaxRate8NetTotal(): string {
        return this.getNetTotal("8")
    }

    public isEmptyTaxRate5(): boolean {
        return this.isEmpty("5")
    }

    public getTaxRate5Total(): string {
        return this.getTotal("5")
    }

    public getTaxRate5NetTotal(): string {
        return this.getNetTotal("5")
    }

    public isEmptyTaxRate3(): boolean {
        return this.isEmpty("3")
    }

    public getTaxRate3Total(): string {
        return this.getTotal("3")
    }

    public getTaxRate3NetTotal(): string {
        return this.getNetTotal("3")
    }

    public isEmptyTaxRateH(): boolean {
        return this.isEmpty("H")
    }

    public getTaxRateHTotal(): string {
        return this.getTotal("H")
    }

    public getTaxRateHNetTotal(): string {
        return this.getNetTotal("H")
    }

    private isEmpty(taxRate: InvoiceTaxRate): boolean {
        const taxAmountDetail = this.getTaxAmountDetail(taxRate)
        return taxAmountDetail === undefined
    }

    private getTotal(taxRate: InvoiceTaxRate): string {
        const taxAmountDetail = this.getTaxAmountDetail(taxRate)
        if (taxAmountDetail === undefined) {
            return "-"
        }
        return `￥${taxAmountDetail.getTotalAmount().toFormat()}`
    }

    private getNetTotal(taxRate: InvoiceTaxRate): string {
        const taxAmountDetail = this.getTaxAmountDetail(taxRate)
        if (taxAmountDetail === undefined) {
            return "-"
        }
        return `￥${taxAmountDetail.getTaxAmount().toFormat()}`
    }

    private getTaxAmountDetail(taxRate: InvoiceTaxRate): TaxAmountDetail | undefined {
        return this.value.find((taxAmountDetail) => taxAmountDetail.getTaxRate() === taxRate)
    }
}

class InvoiceLine {
    constructor(
        private readonly number: number,
        private readonly transactionDetails: string,
        private readonly transactionDate: string | undefined,
        private readonly remarks: string | undefined,
        private readonly taxRate: InvoiceTaxRate,
        private readonly unitPrice: BigNumber,
        private readonly quantity: BigNumber,
        private readonly amount: BigNumber,
    ) {
    }

    public getNumber(): string {
        return this.number.toString()
    }

    public getTransactionDetails(): string {
        return this.transactionDetails
    }

    public getTransactionDate(): string {
        if (this.transactionDate === undefined) return ""
        return this.transactionDate
    }

    public getRemarks(): string {
        if (this.remarks === undefined) return ""
        return this.remarks
    }

    public getTaxRate(): string {
        if (this.taxRate === "H") return "非課税"

        return `${this.taxRate}%`
    }

    public getUnitPrice(): string {
        return `￥${this.unitPrice.toFormat()}`
    }

    public getQuantity(): string {
        return this.quantity.toString()
    }

    public getAmount(): string {
        return `￥${this.amount.toFormat()}`
    }
}

export class InvoiceViewModel {
    private readonly title: string
    private readonly recipientInfo: RecipientInfo
    private readonly issuerInfo: IssuerInfo
    private readonly paymentInfo: PaymentInfo
    private readonly invoiceLines: InvoiceLine[]
    private readonly taxAmountDetails: TaxAmountDetails
    readonly invoiceAmountInfo: InvoiceAmountInfo

    constructor(
        invoice: Invoice
    ) {
        this.title = invoice.title.getTitle()
        this.recipientInfo = invoice.recipientInfo
        this.issuerInfo = invoice.issuerInfo
        this.paymentInfo = invoice.paymentInfo
        this.invoiceAmountInfo = new InvoiceAmountInfo(
            invoice.invoiceAmountInfo.getTaxRate(),
            invoice.paymentInfo.amount
        )
        this.invoiceLines = [
            new InvoiceLine(
                1,
                "Item　1",
                // TODO: paymentDeadlineのパースがうまく行っていないのでなおす
                `${new Date().getFullYear()}/${("0" + (new Date().getMonth() + 1)).slice(-2)}/${("0" + new Date().getDate()).slice(-2)}`,
                "-",
                this.invoiceAmountInfo.getTaxRate(),
                new BigNumber(Math.floor(invoice.invoiceAmountInfo.getTaxExcludedAmountNumber())),
                new BigNumber(1),
                new BigNumber(Math.floor(invoice.invoiceAmountInfo.getTaxIncludedAmountNumber()))
            ),
        ]
        this.taxAmountDetails = new TaxAmountDetails(
            [new TaxAmountDetail(
                this.invoiceAmountInfo.getTaxRate(),
                new BigNumber(this.invoiceAmountInfo.getTaxIncludedAmountNumber()),
                new BigNumber(this.invoiceAmountInfo.getTaxAmountNumber())
            )]
        )
    }

    public getTitle(): string {
        return this.title
    }

    public getInvoiceLines(): InvoiceLine[] {
        return this.invoiceLines
    }

    public getTaxAmountDetails(): TaxAmountDetails {
        return this.taxAmountDetails
    }

    public getTaxExcludedAmount(): string {
        return `￥${BigNumber(this.invoiceAmountInfo.getTaxExcludedAmountNumber()).toFormat()}`
    }

    public getTaxAmount(): string {
        return `￥${BigNumber(this.invoiceAmountInfo.getTaxAmountNumber()).toFormat()}`
    }

    public getTaxIncludedAmount(): string {
        return `￥${BigNumber(this.invoiceAmountInfo.getTaxIncludedAmountNumber()).toFormat()}`
    }
}