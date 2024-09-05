package farm.inventory;

import farm.core.*;
import farm.customer.Customer;
import farm.inventory.product.*;
import farm.inventory.product.data.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A fancy inventory which stores products in stacks, enabling quantity information.
 */
public class FancyInventory implements Inventory {
    private Stack<Product> productList = new Stack<>();

    /**
     * Adds a new product with corresponding barcode to the inventory.
     * @param barcode The barcode of the product to add.
     * @param quality The quality of added product.
     */
    @Override
    public void addProduct(Barcode barcode, Quality quality) {
        switch (barcode) {
            case Barcode.WOOL -> productList.add(new Wool(quality));
            case Barcode.MILK -> productList.add(new Milk(quality));
            case Barcode.EGG -> productList.add(new Egg(quality));
            case Barcode.JAM -> productList.add(new Jam(quality));
        }
    }

    /**
     * Adds multiple of the product with corresponding barcode to the inventory.
     * @param barcode the barcode of the product to add.
     * @param quality the quality of added product.
     * @param quantity the amount of the product to add.
     * @throws InvalidStockRequestException never thrown
     */
    @Override
    public void addProduct(Barcode barcode,
                           Quality quality,
                           int quantity)
            throws InvalidStockRequestException {
        for (int i = 0; i < quantity; i++) {
            addProduct(barcode, quality);
        }
    }

    /**
     * Determines if a product exists in the inventory with the given barcode.
     * @param barcode The barcode of the product to check.
     * @return true iff a product exists, else false.
     */
    public boolean existsProduct(Barcode barcode) {
        for (Product p : productList) {
            if (p.getBarcode() == barcode) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the highest quality product with corresponding barcode from the inventory.
     * @param barcode The barcode of the product to be removed.
     * @return A list containing the removed product if it exists, else an empty list.
     */
    public List<Product> removeProduct(Barcode barcode) {
        for (int i = Quality.values().length - 1; i >= 0; i--) {
            for (Product p : productList) {
                if (p.getBarcode() == barcode && p.getQuality() == Quality.values()[i]) {
                    productList.remove(p);
                    return List.of(p);
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Removes a given number of products with corresponding barcode from the inventory, choosing the highest quality products possible.
     * @param barcode The barcode of the product to be removed.
     * @param quantity The total amount of the product to remove from the inventory.
     * @return A list containing the removed product if it exists, else an empty list.
     * @throws FailedTransactionException -
     */
    public List<Product> removeProduct(Barcode barcode,
                                       int quantity)
            throws FailedTransactionException {
        List<Product> removedProduct = new ArrayList<>();

        for (int i = Quality.values().length - 1; i >= 0; i--) {
            for (Product p : productList) {
                if (quantity == removedProduct.size()) {
                    for (Product ps : removedProduct) {
                        productList.remove(ps);
                    }
                    return removedProduct;
                }
                if (p.getBarcode() == barcode && p.getQuality() == Quality.values()[i]) {
                    removedProduct.add(p);
                }
            }
        }
        for (Product p : removedProduct) {
            productList.remove(p);
        }
        return removedProduct;
    }

    /**
     * Retrieves the full stock currently held in the inventory.
     * @return An organised list containing all products currently stored in the inventory.
     */
    public List<Product> getAllProducts() {
        List<Product> listOfProducts = new ArrayList<>();
        for (Barcode b : Barcode.values()) {
            for (Product p : productList) {
                if (p.getBarcode() == b) {
                    listOfProducts.add(p);
                }
            }
        }
        return listOfProducts;
    }

    /**
     * Get the quantity of a specific product in the inventory.
     * @param barcode The barcode of the product.
     * @return The amount of the corresponding product currently in the inventory.
     */
    public int getStockedQuantity(Barcode barcode) {
        int total = 0;
        for (Product p : productList) {
            if (p.getBarcode() == barcode) {
                total += 1;
            }
        }
        return total;
    }
}
