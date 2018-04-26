/*
 * Free ware. Made as an exercise.
 * These softwares are made really poorly. Use at your discretion
 */
package mylittleshop;
import java.util.*;
import java.sql.*;
/**
 * Class with main object to invoke the facility (aside from the database)
 * @author Hoang
 */


public class MyLittleShop {

    /**
     * Looks up the product with ID 1 in the product table.
     * Add your code here to do stuffs.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here            
        POS sys = new POS();
        /*System.out.println("Welcome to the POS prototype."
                + " Pick an option to begin.\n"
                + "Remember to turn on the Database\n"
                + "1) Connect to the database\n"
                + "2) Look up a product by ID\n"
                + "3) Import a product\n"
                + "4) Export a product\n"
                + "5) See the log of store #1\n"
                + "6) See the product list\n");*/
        Product pro = sys.lookup(1);
        System.out.println(pro.getID() + "\t" + pro.getName() + "\t" +
                pro.getUnit() + "\t" + pro.getPrice());
        sys.close();
      }
    
}
