
export type InvoiceTaxRate = "H" | "3" | "5" | "8"  | "10"

// 税率をマッピング
export const taxRateMap: { [key in InvoiceTaxRate]: number } = {
    "H": 0,   // "H" は免税
    "3": 0.03,
    "5": 0.05,
    "8": 0.08,
    "10": 0.10
};
export class InvoiceAmountInfo{
    private readonly taxRate: InvoiceTaxRate
    private readonly invoiceTaxExcludedAmount: number

    constructor(taxRate: string, invoiceTaxExcludedAmount: number) {
        this.taxRate = taxRate as InvoiceTaxRate
        this.invoiceTaxExcludedAmount = invoiceTaxExcludedAmount
    }

    public getTaxRate(): InvoiceTaxRate {
        return this.taxRate
    }

    public getTaxExcludedAmountNumber(): number {
        return this.invoiceTaxExcludedAmount
    }

    public getTaxAmountNumber(): number{
        const taxMultiplier = taxRateMap[this.taxRate]
        return this.invoiceTaxExcludedAmount * taxMultiplier
    }

    public  getTaxIncludedAmountNumber(): number{
        const taxRateNum = taxRateMap[this.taxRate]
        return Math.floor(this.invoiceTaxExcludedAmount*(1+taxRateNum))
    }
}
