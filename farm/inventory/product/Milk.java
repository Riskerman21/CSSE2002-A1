package farm.inventory.product;


import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 * A class representing an instance of milk.
 */
public class Milk extends Product {

    /**
     * Create a milk instance with no additional details.
     */
    public Milk() {
        super(Barcode.MILK, Quality.REGULAR);
    }

    /**
     * Create a milk instance with a quality value.
     * @param quality the quality level to assign to this milk.
     */
    public Milk(Quality quality) {
        super(Barcode.MILK, quality);
    }
}

