package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 * A class representing an instance of an egg.
 */
public class Egg extends Product {

    /**
     * Create an egg instance with no additional details.
     */
    public Egg() {
        super(Barcode.EGG, Quality.REGULAR);
    }

    /**
     * Create an egg instance with a quality value.
     * @param quality the quality level to assign to this egg.
     */
    public Egg(Quality quality) {
        super(Barcode.EGG, quality);
    }
}
