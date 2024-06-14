import {InvoiceAmountInfo} from "../../src/model/InvoiceAmountInfo";

describe('Invoice Tax Calculations', () => {

    test('calculateTaxAmount calculates correct tax amount', () => {
        const invoiceAmountInfo: InvoiceAmountInfo = new InvoiceAmountInfo("10", 1000);
        const taxAmount = invoiceAmountInfo.getTaxAmountNumber();

        expect(taxAmount).toBe(100); // 10% of 1000
    });

    test('handle tax exempt correctly', () => {
        const invoiceAmountInfo: InvoiceAmountInfo = new InvoiceAmountInfo("H", 200);
        const totalAmount = invoiceAmountInfo.getTaxIncludedAmountNumber();

        expect(totalAmount).toBe(200); // No tax added
    });

});

describe('TaxExcludedAmount', () => {
    test('getTaxExcludedAmount returns correct tax excluded amount', () => {
        const invoiceAmountInfo: InvoiceAmountInfo = new InvoiceAmountInfo("10", 1000);
        const taxExcludedAmount = invoiceAmountInfo.getTaxExcludedAmountNumber();

        expect(taxExcludedAmount).toBe(1000);
    });
})

describe('TaxIncludedAmount', () => {

    test('calculateTaxIncludedAmount calculates correct total amount', () => {
        const invoiceAmountInfo: InvoiceAmountInfo = new InvoiceAmountInfo("8", 200);
        const totalAmount = invoiceAmountInfo.getTaxIncludedAmountNumber();

        expect(totalAmount).toBe(216); // 200 + (8% of 200)
    });

})