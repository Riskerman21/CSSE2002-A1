package farm.customer;

import farm.core.CustomerNotFoundException;
import farm.core.DuplicateCustomerException;

import java.util.ArrayList;
import java.util.List;

/**
 * The address book where the farmer stores their customers' details.
 * Keeps track of all the customers that come and visit the Farm.
 */
public class AddressBook {
    private List<Customer> customers = new ArrayList<>();

    /**
     * Add a new customer to the address book.
     * @param customer The customer to be added.
     * @throws DuplicateCustomerException If the customer already exists in the address book.
     * Contains a message of the Customers representation.
     * @ensures The address book contains no duplicate customers
     */
    public void addCustomer(Customer customer) throws DuplicateCustomerException {
        if (customers.contains(customer)) {
            throw new DuplicateCustomerException(customer.toString());
        } else {
            customers.add(customer);
        }
    }

    /**
     * Retrieve all customer records stored in the address book.
     * @return A list of all customers in the address book
     * @ensures The returned list is a shallow copy and cannot modify the original address book
     */
    public List<Customer> getAllRecords() {
        return new ArrayList<>(customers);
    }

    /**
     * Retrieve all customer records stored in the address book.
     * @param customer A list of all customers in the address book.
     * @return true iff the customer already exists, else false.
     */
    public boolean containsCustomer(Customer customer) {
        return customers.contains(customer);
    }

    /**
     * Lookup a customer in address book, if they exist using their details.
     * @param name The name of the customer to lookup.
     * @param phoneNumber The phone number of the customer.
     * @return The Customer iff they exist in the address book.
     * @throws CustomerNotFoundException If there is no customer matching the information in the address book.
     * @requires That the name is non-empty and has been stripped of its trailing whitespace
     * and that the phone number is a positive number.
     */
    public Customer getCustomer(String name, int phoneNumber) throws CustomerNotFoundException {
        for (Customer c : customers) {
            if (c.getName().equals(name)  && c.getPhoneNumber() ==  phoneNumber) {
                return c;
            }
        }
        throw new CustomerNotFoundException();
    }
}
