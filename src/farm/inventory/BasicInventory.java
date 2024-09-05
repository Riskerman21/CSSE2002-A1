package farm.inventory;

import farm.core.FailedTransactionException;
import farm.core.InvalidStockRequestException;
import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

import java.util.ArrayList;
import java.util.List;

/**
 * A very basic inventory that both stores and handles products individually.
 * Only supports operation on single Products at a time.
 */
public class BasicInventory implements Inventory {
    private List<Product> productList = new ArrayList<>();

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
     * Throws an InvalidStockRequestException
     * @param barcode the barcode of the product to add.
     * @param quality the quality of added product.
     * @param quantity the amount of the product to add.
     * @throws InvalidStockRequestException always, since Basic inventories never support quantities > 1.
     */
    @Override
    public void addProduct(Barcode barcode,
                           Quality quality,
                           int quantity)
            throws InvalidStockRequestException {
        if (quantity == 1) {
            addProduct(barcode, quality);
        } else {
            throw new InvalidStockRequestException(
                    "Current inventory is not fancy enough. Please supply products one at a time.");
            
        }
    }

    /**
     * Determines if a product exists in the inventory with the given barcode.
     * @param barcode The barcode of the product to check.
     * @return true iff a product exists, else false.
     */
    @Override
    public boolean existsProduct(Barcode barcode) {
        for (Product p : productList) {
            if (p.getBarcode() == barcode) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the first product with corresponding barcode from the inventory.
     * @param barcode The barcode of the product to be removed.
     * @return A list containing the removed product if it exists, else an empty list.
     */
    @Override
    public List<Product> removeProduct(Barcode barcode) {
        for (int i = Quality.values().length - 1; i >= 0; i--) {
            for (Product p : productList) {
                if (p.getBarcode() == barcode && p.getQuality() == Quality.values()[i]) {
                    productList.remove(p);
                    return List.of(p);
                }
            }
        }
        return List.of();
    }

    /**
     * Throws an FailedTransactionException
     * @param barcode The barcode of the product to be removed.
     * @param quantity The total amount of the product to remove from the inventory.
     * @return A list containing the removed product if it exists, else an empty list.
     * @throws FailedTransactionException always, since Basic inventories never support quantities > 1.
     */
    @Override
    public List<Product> removeProduct(Barcode barcode,
                                       int quantity)
            throws FailedTransactionException {
        throw new FailedTransactionException("Current inventory is not fancy enough. "
                    + "Please purchase products one at a time.");

    }

    /**
     * Retrieves the full stock currently held in the inventory.
     * @return A list containing all products currently stored in the inventory.
     */
    @Override
    public List<Product> getAllProducts() {
        return productList;
    }
}
