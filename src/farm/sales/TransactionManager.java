package farm.sales;

import farm.core.*;
import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.sales.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * The controlling class for all transactions.
 * Opens and closes transactions, as well as
 * ensuring only one transaction is active at any given time.
 *
 */
public class TransactionManager {
    private List<Transaction> transactions;
    private Transaction currentransaction;

    /**
     * constructor for the class the initiates the list of transactions and
     * keeps track of the current one
     */
    public TransactionManager() {
        this.transactions = new ArrayList<>();
        this.currentransaction = null;
    }


    /**
     * Determine whether a transaction is currently in progress.
     *
     * @return true iff a transaction is in progress, else false.
     */
    public boolean hasOngoingTransaction() {
        for (Transaction t : transactions) {
            if (!t.isFinalised()) {
                return true;
            }
        }
        return false;
    }

    /**
     * the transaction to set as the manager's ongoing transaction.
     *
     * @param transaction the transaction to set as the manager's ongoing transaction.
     * @throws FailedTransactionException iff a transaction is already in progress.
     */
    public void setOngoingTransaction(Transaction transaction)
            throws FailedTransactionException {
        if (hasOngoingTransaction()) {
            throw new FailedTransactionException();
        } else {
            transactions.add(transaction);
            currentransaction = transaction;
        }
    }

    /**
     * Adds the given product to the cart of the customer associated with the current transaction.
     *
     * @param product the product to add to customer's cart.
     * @throws FailedTransactionException iff there is no ongoing transaction or
     * the transaction has already been finalised.
     * @requires the provided product is known to be valid for purchase, i.e.
     * has been successfully retrieved from the farm's inventory
     */
    public void registerPendingPurchase(Product product)
            throws FailedTransactionException {
        if (!hasOngoingTransaction()) {
            throw new FailedTransactionException();
        } else {
            currentransaction.getPurchases().add(product);
        }
    }

    /**
     * Finalises the currently ongoing transaction and makes readies the TransactionManager
     * to accept a new ongoing transaction.
     * @return the finalised transaction.
     * @throws FailedTransactionException iff there is no currently ongoing transaction to close.
     */
    public Transaction closeCurrentTransaction()
            throws FailedTransactionException {
        if (!hasOngoingTransaction()) {
            throw new FailedTransactionException();
        }
        currentransaction.finalise();
        return currentransaction;

    }
}
