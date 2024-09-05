package farm.sales;

import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.sales.transaction.SpecialSaleTransaction;
import farm.sales.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * A record of all past transactions.
 */
public class TransactionHistory {
    private List<Transaction> transactions = new ArrayList<>();
    private List<Integer> positionList = new ArrayList<>();

    /**
     * constructor of the class the keeps track of all past transactions
     */
    public TransactionHistory() {}

    /**
     * Adds the given transaction to the record of all past transactions.
     *
     * @param transaction the transaction to add to the record.
     * @requires the transaction to be recorded has been finalised
     */
    public void recordTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Retrieves the most recent transaction.
     *
     * @return the most recent transaction added to the record.
     */
    public Transaction getLastTransaction() {
        return transactions.getLast();
    }

    /**
     * Calculates the gross earnings, i.e. total income, from all transactions.
     *
     * @return the gross earnings from all transactions in history, in cents.
     */
    public int getGrossEarnings() {
        int total = 0;
        for (Transaction t : transactions) {
            total += t.getTotal();
        }
        return total;
    }

    /**
     * Calculates the gross earnings, i.e. total income, from all sales of a particular product type.
     *
     * @param type the Barcode of the item of interest.
     * @return the gross earnings from all sales of the product type, in cents.
     */
    public int getGrossEarnings(Barcode type) {
        int total = 0;
        for (Transaction t : transactions) {
            for (Product p : t.getPurchases()) {
                if (p.getBarcode() == type) {
                    total += p.getBasePrice();
                }
            }
        }
        return total;
    }

    /**
     * Calculates the number of transactions made.
     *
     * @return the number of transactions in total.
     */
    public int getTotalTransactionsMade() {
        return transactions.size();
    }

    /**
     * Calculates the number of products sold over all transactions.
     *
     * @return the total number of products sold.
     */
    public int getTotalProductsSold() {
        int psold = 0;
        for (Transaction t : transactions) {
            psold += t.getPurchases().size();
        }
        return psold;
    }

    /**
     * Calculates the number of sold of a particular product type, over all transactions.
     *
     * @param type the Barcode for the product of interest
     * @return the total number of products sold, for that particular product.
     */
    public int getTotalProductsSold(Barcode type) {
        int psold = 0;
        for (Transaction t : transactions) {
            for (Product p : t.getPurchases()) {
                if (p.getBarcode() == type) {
                    psold += 1;
                }
            }
        }
        return psold;
    }

    /**
     * Retrieves the transaction with the highest gross earnings, i.e. reported total.
     * If there are multiple return the one that first was recorded.
     *
     * @return the transaction with the highest gross earnings.
     */
    public Transaction getHighestGrossingTransaction() {
        int hightestGross = -1;
        Transaction highestTransaction = new Transaction(null);
        for (Transaction t : transactions) {
            if (t.getTotal() > hightestGross) {
                highestTransaction = t;
                hightestGross = t.getTotal();
            }
        }
        return highestTransaction;
    }

    /**
     * Calculates which type of product has had the highest quantity sold overall.
     * If two products have sold the same quantity resulting in a tie,
     * return the one appearing first in the Barcode enum.
     *
     * @return the identifier for the product type of most popular product.
     */
    public Barcode getMostPopularProduct() {
        int numberOfProduct = 0;
        Product popularProduct = null;
        for (Transaction t : transactions) {
            for (Product p : t.getPurchases()) {
                if (getTotalProductsSold(p.getBarcode()) > numberOfProduct) {
                    popularProduct = p;
                    numberOfProduct = getTotalProductsSold(p.getBarcode());
                } else if (getTotalProductsSold(p.getBarcode()) == numberOfProduct) {
                    for (Barcode b : Barcode.values()) {
                        if (p.getBarcode() == b) {
                            popularProduct = p;
                            numberOfProduct = getTotalProductsSold(p.getBarcode());
                            break;
                        } else if (popularProduct != null && popularProduct.getBarcode() == b) {
                            break;
                        }
                    }
                }
            }
        }
        if (popularProduct == null) {
            return Barcode.EGG;
        } else {
            return popularProduct.getBarcode();
        }
    }

    /**
     * Calculates the average amount spent by customers across all transactions.
     *
     * @return the average amount spent overall, in cents (with decimals).
     */
    public double getAverageSpendPerVisit() {
        if (getTotalTransactionsMade() > 0) {
            return (double) getGrossEarnings() / getTotalTransactionsMade();
        }
        return 0.0d;
    }

    /**
     * Calculates the average amount a product has been discounted by, across all sales of that product.
     *
     * @param type identifier of the product of interest.
     * @return the average discount for the product, in cents (with decimals).
     */
    public double getAverageProductDiscount(Barcode type) {
        int discounts = 0;
        for (Transaction t : transactions) {
            if (t instanceof SpecialSaleTransaction) {
                discounts += ((SpecialSaleTransaction) t).getDiscountAmount(type);
            }
        }
        if (discounts > 0) {
            return (double) discounts / getTotalTransactionsMade();
        } else {
            return 0.0d;
        }

    }
}
