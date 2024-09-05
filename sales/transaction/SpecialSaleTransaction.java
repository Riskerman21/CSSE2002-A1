package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.sales.ReceiptPrinter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A transaction type that builds on the functionality of a categorised transaction, allowing
 * store-wide discounts to be applied to all products of a nominated type.
 */
public class SpecialSaleTransaction extends CategorisedTransaction {

    private Map<Barcode, Integer> discounts = new HashMap<>();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * A transaction type that builds on the functionality of a categorised transaction,
     * allowing store-wide discounts to be applied to all products of a nominated type.
     *
     * @param customer the customer who the starting the transaction (beginning to shop).
     */
    public SpecialSaleTransaction(Customer customer) {
        super(customer);
    }

    /**
     * Construct a new special sale transaction for an associated customer, with a set of discounts
     * to be applied to nominated product types on purchasing.
     *
     * @param customer the customer who is starting the transaction (beginning to shop).
     * @param discounts a mapping from product barcodes to the associated discount applied on purchasing,
     * where discount amounts are specified as an integer percentage
     * @requires 0 <= discount amount <= 100
     */
    public SpecialSaleTransaction(Customer customer, Map<Barcode, Integer> discounts) {
        super(customer);
        this.discounts = discounts;
    }

    /**
     * Retrieves the discount percentage that will be applied for a particular product type, as an integer.
     *
     * @param type the product type.
     * @return the amount the product is discounted by, as an integer percentage.
     */
    public int getDiscountAmount(Barcode type) {
        if (discounts.containsKey(type)) {
            return discounts.get(type);
        }
        return 0;
    }

    /**
     * Determines the total price for the provided product type within this transaction,
     * with any specified discount applied as an integer percentage taken from the usual subtotal.
     *
     * @param type the product type whose subtotal should be calculated.
     * @return the total (discounted) price for all instances of that product type within this transaction.
     */
    @Override
    public int getPurchaseSubtotal(Barcode type) {
        if (!discounts.containsKey(type)) {
            return getPurchaseQuantity(type) * type.getBasePrice();
        }
        return (int) Math.ceil(getPurchaseQuantity(type) * type.getBasePrice()
                * ((1 - (discounts.get(type) * 0.01))));
    }


    /**
     * Retrieves the discount percentage that will be applied for a particular product type, as an integer.
     *
     * @return the total (discounted) price calculated.
     */
    @Override
    public int getTotal() {
        int total = 0;
        for (Barcode b : this.getPurchasedTypes()) {
            total += getPurchaseSubtotal(b);
        }
        return total;
    }


    /**
     * Calculates how much the customer has saved from discounts.
     *
     * @return the numerical savings from discounts.
     */
    public int getTotalSaved() {
        int total = 0;
        for (Product p : getPurchases()) {
            total += p.getBasePrice();
        }
        return total - getTotal();
    }

    /**
     * Returns a string representation of this transaction and its current state.
     *
     * @return The formatted string representation of the product.
     */
    @Override
    public String toString() {
        String check = "";
        String customer = getAssociatedCustomer().toString();
        String products = getPurchases().toString();

        if (isFinalised()) {
            check = "Finalised";

        } else {
            check = "Active";
        }
        return "Transaction {Customer" + customer.substring(4) + ", Status: " + check
                + ", Associated Products: " + products + ", Discounts: " + this.discounts + "}";
    }

    /**
     * Converts the transaction into a formatted receipt for display
     * @return the styled receipt representation of this transaction
     */
    @Override
    public String getReceipt() {
        List<String> heading = new ArrayList<String>(List.of("Item",
                "Qty",
                "Price (ea.)",
                "Subtotal"));

        List<List<String>> products = new ArrayList<List<String>>();
        for (Barcode b : Barcode.values()) {
            if (discounts.containsKey(b) && discounts.get(b) > 0 && getPurchaseQuantity(b) > 0) {
                float discount = this.discounts.get(b);
                products.add(new ArrayList<String>(List.of(
                        b.getDisplayName(),
                        Integer.toString(getPurchaseQuantity(b)),
                        "$" + df.format((float) b.getBasePrice() / 100),
                        "$" + df.format((float) getPurchaseSubtotal(b) / 100),
                        "Discount applied! " + (int) discount + "% off " + b.getDisplayName())));
            } else if (getPurchaseQuantity(b) > 0) {
                products.add(new ArrayList<String>(List.of(b.getDisplayName(),
                        Integer.toString(getPurchaseQuantity(b)),
                        "$" + df.format((float) b.getBasePrice() / 100),
                        "$" + df.format((float) getPurchaseSubtotal(b) / 100))));
            }
        }

        if (isFinalised() && getTotalSaved() > 0) {
            return ReceiptPrinter.createReceipt(
                        heading,
                        products,
                        "$" + df.format((float) getTotal() / 100),
                        getAssociatedCustomer().getName(),
                        "$" + df.format((float) getTotalSaved() / 100));
        } else if (isFinalised()) {
            return ReceiptPrinter.createReceipt(heading,
                    products,
                    "$" + df.format((float) getTotal() / 100),
                    getAssociatedCustomer().getName());

        } else {
            return ReceiptPrinter.createActiveReceipt();
        }

    }
}
