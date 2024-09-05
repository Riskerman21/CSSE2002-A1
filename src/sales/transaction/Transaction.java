package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Egg;
import farm.inventory.product.Jam;
import farm.inventory.product.Milk;
import farm.inventory.product.Product;
import farm.sales.ReceiptPrinter;

import javax.naming.Name;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Transactions keeps track of what items are to be (or have been) purchased and by whom.
 */
public class Transaction {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private Customer customer;
    private boolean finished;
    private List<Product> bought = new ArrayList<>();

    /**
     * Construct a new transaction for an associated customer.
     * @param customer the customer who is starting the transaction (beginning to shop).
     */
    public Transaction(Customer customer) {
        this.customer = customer;
    }

    /**
     * Retrieves the customer associated with this transaction.
     *
     * @return the customer of the transaction.
     */
    public Customer getAssociatedCustomer() {
        return customer;
    }

    /**
     * Retrieves all products associated with the transaction.
     *
     * @return the list of purchases comprising the transaction.
     * @ensures the returned list is a shallow copy and cannot modify the original transaction
     */
    public List<Product> getPurchases() {
        if (isFinalised()) {
            return bought;
        }
        return getAssociatedCustomer().getCart().getContents();
    }

    /**
     * Calculates the total price of all the current products in the transaction.
     *
     * @return the total price calculated.
     */
    public int getTotal() {
        int total = 0;
        for (Product p : getPurchases()) {
            total += p.getBasePrice();
        }
        return total;
    }

    /**
     * Determines if the transaction is finalised (i.e. sale completed) or not.
     *
     * @return true iff the transaction is over, else false.
     */
    public boolean isFinalised() {
        return finished;
    }

    /**
     * Mark a transaction as finalised and update the transaction's internal state accordingly.
     */
    public void finalise() {
        finished = true;
        if (!bought.isEmpty()) {
            bought.clear();
        }
        bought.addAll(getAssociatedCustomer().getCart().getContents());
        getAssociatedCustomer().getCart().getContents().clear();
    }

    /**
     * Returns a string representation of this transaction and its current state.
     * The representation contains information about the customer, the transaction's status,
     * and the associated products.
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
                + ", Associated Products: " + products + "}";
    }

    /**
     * Converts the transaction into a formatted receipt for display.
     *
     * @return the styled receipt representation of this transaction.
     */
    public String getReceipt() {
        List<String> heading = new ArrayList<String>(List.of("Item", "Price"));

        List<List<String>> products = new ArrayList<List<String>>();
        for (Product p : getPurchases()) {
            String prices = df.format((float) ((float) p.getBasePrice() * 0.01));
            String price = "$" + prices;
            String name = p.getDisplayName();
            products.add(new ArrayList<String>(List.of(name, price)));
        }

        if (isFinalised()) {
            return  ReceiptPrinter.createReceipt(heading,
                    products,
                    "$" + df.format(((float) getTotal() / 100)),
                    getAssociatedCustomer().getName());
        } else {
            return ReceiptPrinter.createActiveReceipt();
        }
    }

}
