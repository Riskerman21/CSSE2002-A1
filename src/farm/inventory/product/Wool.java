package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 *A class representing an instance of wool.
 */
public class Wool extends Product {

    /**
     * Create a wool instance with no additional details.
     */
    public Wool() {
        super(Barcode.WOOL, Quality.REGULAR);
    }

    /**
     * Create a wool instance with a specific quality value.
     * @param quality the quality level to assign to this wool.
     */
    public Wool(Quality quality) {
        super(Barcode.WOOL, quality);
    }
}
