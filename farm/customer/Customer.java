package farm.customer;

import farm.sales.Cart;

/**
 * A customer who interacts with the farmer's business.
 */
public class Customer {
    private String name;
    private int phoneNumber;
    private String address;
    private Cart cart;

    /**
     * Create a new customer instance with their details.
     * @param name The name of the customer.
     * @param phoneNumber The customer's phone number.
     * @param address The address of the customer.
     * @requires That the name and address is non-empty.
     * @requires That the phone number is a positive number.
     * @requires That the name and address are stripped of trailing whitespaces.
     */
    public Customer(String name, int phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.cart = new Cart();
    }

    /**
     * Retrieve the name of the customer.
     * @return The customers name.
     */
    public String getName() {
        return name;
    }

    /**
     * Update the current name of the customer with a new one.
     * @param newName The new name to override the current name.
     * @requires That the name is non-empty and that it's stripped of trailing whitespaces.
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Retrieve the phone number of the customer.
     * @return The customer's phone number.
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the current phone number of the customer to be newPhone.
     * @param newPhone The phone number to override the current phone number.
     * @requires The phone number is a positive number.
     */
    public void setPhoneNumber(int newPhone) {
        phoneNumber = newPhone;
    }

    /**
     * Retrieve the address of the customer.
     * @return The customer address.
     * @requires That the address is non-empty and that its stripped of trailing whitespaces.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the current address of the customer to be newAddress.
     * @param newAddress The address to override the current address.
     */
    public void setAddress(String newAddress) {
        address = newAddress;
    }

    /**
     * Retrieves the customers cart.
     * @return Their shopping cart.
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Returns a string representation of this customer class. The representation contains the name of the customer,
     * followed by their phone number and address separated by ' | '.
     * @return The formatted string representation of the customer.
     */
    @Override
    public String toString() {
        return "Name: " + name + " | Phone Number: " + phoneNumber + " | Address: " + address;
    }

    /**
     * Determines whether the provided object is equal to this customer instance.
     * For customers, equality is defined by having the same phone number and name; addresses are not considered.
     * @param obj The object with which to compare
     * @return true if the other object is a customer with the same phone number and name as the current customer.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Customer customer) {
            return this.hashCode() == customer.hashCode();
        }
        return false;
    }

    /**
     * A hashcode method that respects the equals(Object) method.
     * @return An appropriate hashcode value for this instance.
     */
    @Override
    public int hashCode() {
        return phoneNumber + name.hashCode();
    }
}
