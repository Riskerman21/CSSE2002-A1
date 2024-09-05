package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.sales.ReceiptPrinter;

import java.text.DecimalFormat;
import java.util.*;

/**
 *A transaction type that allows products to be categorised by their types,
 * not solely as isolated individual products. The resulting receipt therefore displays
 * purchased types with an associated quantity purchased and subtotal, rather than a single
 * line for each product.
 */
public class CategorisedTransaction extends Transaction {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Construct a new categorised transaction for an associated customer. Transactions should
     * always be active at the time of creation, i.e. a transaction cannot already be set to finalised
     * upon instantiation.
     *
     * @param customer the customer who is starting the transaction (beginning to shop).
     */
    public CategorisedTransaction(Customer customer) {
        super(customer);
    }

    /**
     * Retrieves all unique product types of the purchases associated with the transaction.
     *
     * @return a set of all product types in the transaction.
     */
    public Set<Barcode> getPurchasedTypes() {
        ArrayList<Barcode> barcodes = new ArrayList<>();


        for (Product p : getPurchases()) {
            barcodes.add(p.getBarcode());
        }
        return new HashSet<>(barcodes);
    }

    /**
     * Retrieves all products associated with the transaction, grouped by their type.
     *
     * @return the products in the transaction, grouped by their type.
     */
    public Map<Barcode, List<Product>> getPurchasesByType() {
        Map<Barcode, List<Product>> barcodeListHashMap = new HashMap<>();
        List<Product> products = getPurchases();
        List<Product> productsamebar = new ArrayList<>();
        for (Barcode barcode : getPurchasedTypes()) {
            for (Product p : products) {
                if (p.getBarcode() == barcode) {
                    productsamebar.add(p);
                }
            }
            barcodeListHashMap.put(barcode, productsamebar);
            productsamebar = new ArrayList<>();
        }
        return barcodeListHashMap;
    }

    /**
     * Retrieves the number of products of a particular type associated with the transaction.
     *
     * @param type the product type.
     * @return the number of products of the specified type associated with the transaction.
     */
    public int getPurchaseQuantity(Barcode type) {
        if (getPurchasesByType().containsKey(type)) {
            return getPurchasesByType().get(type).size();
        } else {
            return 0;
        }

    }

    /**
     * Determines the total price for the provided product type within this transaction.
     *
     * @param type the product type.
     * @return the total price for all instances of that product type within the transaction, or 0 if no
     * items of that type are associated with the transaction.
     */
    public int getPurchaseSubtotal(Barcode type) {
        return getPurchaseQuantity(type) * type.getBasePrice();
    }

    /**
     * Converts the transaction into a formatted receipt for display
     *
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
            if (getPurchaseQuantity(b) > 0) {
                String price = "$" + df.format((float) (getPurchaseSubtotal(b) * 0.01));
                String name = b.getDisplayName();
                String subprice = "$" + df.format((float) (b.getBasePrice() * 0.01));
                String qty = Integer.toString(getPurchaseQuantity(b));

                products.add(List.of(name, qty, subprice, price));
            }
        }

        if (isFinalised()) {
            return  ReceiptPrinter.createReceipt(heading,
                    products,
                    "$" + df.format((float) getTotal() / 100),
                    getAssociatedCustomer().getName());
        } else {
            return ReceiptPrinter.createActiveReceipt();
        }
    }

}
