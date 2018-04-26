/*
 * Free ware. Made as an exercise.
 * These softwares are made really poorly. Use at your discretion
 */
package mylittleshop;

/**
 * Generally describes the structure of the product goes in tandem with the
 * structure of the SQL database. Please refer to the 'products' table or the
 * 'loadShopDB.sql' file for details
 * @author Hoang
 */
public class Product {
    private final int product_ID;
    private final String name;
    private final String unit;
    private final int price;
    
    /**
     * Constructor of the product object.
     * @param id The product ID of the product. Unique for all entries in the
     * database. This variable is an integer. 
     * @param n The name of the product, is a string. At most 30 characters long
     * @param u The unit of the product, is a string. At most 12 characters long
     * @param p The price of the product, is an integer.
     */
    Product(int id, String n, String u, int p){
        this.product_ID = id;
        this.name = n;
        this.unit = u;
        this.price = p;
    }
    
    /**
     * Getter method for ID
     * @return The product ID as an integer primitive.
     */
    public int getID(){
        return this.product_ID;
    }
    
    /**
     * Getter method for the name of the product
     * @return The name as a String 
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Getter method for the unit of the product
     * @return The unit as a String
     */
    public String getUnit(){
        return this.unit;
    }
    
    /**
     * Getter method for the price of the product
     * @return The price as an integer primitive.
     */
    public int getPrice(){
        return this.price;
    }
}
