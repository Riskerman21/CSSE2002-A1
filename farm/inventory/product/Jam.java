package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 * A class representing an instance of jam.
 */
public class Jam extends Product {

    /**
     * Create a jam instance with no additional details.
     */
    public Jam() {
        super(Barcode.JAM, Quality.REGULAR);
    }

    /**
     * Create a jam instance with a quality value.
     * @param quality the quality level to assign to this jam.
     */
    public Jam(Quality quality) {
        super(Barcode.JAM, quality);
    }
}
