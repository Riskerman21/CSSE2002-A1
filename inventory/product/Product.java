package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 * An abstract class representing an instance of a product.
 */
public abstract class Product {
    private Barcode barcode;
    private Quality quality;

    protected Product(Barcode barcode, Quality quality) {
        this.barcode = barcode;
        this.quality = quality;
    }

    /**
     * Accessor method for the product's identifier.
     * @return the identifying Barcode for this product.
     */
    public Barcode getBarcode() {
        return barcode;
    }

    /**
     * Retrieve the product's display name, for visual/textual representation.
     * @return the product's display name.
     */
    public String getDisplayName() {
        return barcode.getDisplayName();
    }

    /**
     * Retrieve the product's quality.
     * @return Retrieve the product's quality.
     */
    public  Quality getQuality() {
        return quality;
    }

    /**
     * Retrieve the products base sale price.
     * @return the price of the product. In cents.
     */
    public int getBasePrice() {
        return barcode.getBasePrice();
    }

    /**
     * Returns a string representation of this product class.
     * The representation contains the display name of the product, followed by its base
     * price and quality.
     *
     * @return The formatted string representation of the product.
     */
    @Override
    public String toString() {
        return getDisplayName() + ": " + getBasePrice() + "c " + "*" + getQuality() + "*";
    }

    /**
     * If two instances of product are equal to each other.
     * Equality is defined by having the same barcode, and quality.
     * @param obj The object with which to compare
     * @return true iff the other object is a product with the same barcode, and quality as the current product.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product product) {
            return hashCode() == product.hashCode();
        }
        return false;
    }

    /**
     * A hashcode method that respects the equals(Object) method.
     * @return An appropriate hashcode value for this instance.
     */
    @Override
    public int hashCode() {
        return getQuality().hashCode() + getBarcode().hashCode();
    }
}
