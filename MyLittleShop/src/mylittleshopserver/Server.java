/*
 * Free ware. Made as an exercise.
 * These softwares are made really poorly. Use at your discretion
 */
package mylittleshopserver;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * Describes the POS functions of the program.
 *
 * @author Hoang
 * @version 1.0
 */
public class Server {

    private final Database shopdb;
    private Connection connection;

    /**
     * Constructor method. Establishes the connection to the database. This
     * might be changed in the future to support access control
     */
    Server() {
        this.shopdb = new Database("root", "root");
        this.shopdb.connect();
    }

    /**
     * Looks up the product in question by ID. Made to support the bar code
     * reader to be implemented
     *
     * @param ID The ID of the product as a primitive integer.
     * @return A product object, containing all relevant info
     */
    public Product lookup(int ID) {
        Product result = null;
        String SQLQuery = "SELECT * FROM products WHERE product_id=" + ID;
        ResultSet data = shopdb.query(SQLQuery);
        try {
            while (data.next()) {
                Product match = new Product(data.getInt("product_id"),
                        data.getString("name"), data.getString("unit"),
                        data.getInt("price"));
                result = match;
            }
        } catch (SQLException e) {
            System.err.println("SQLException: lookup failed");
        } finally {
            if (data != null) {
                try {
                    data.close();
                } catch (SQLException ignore) {
                }
            }
        }
        return result;
    }

    /**
     * Same with lookup(ID) but this time with string. Utilized by method
     * addProduct to initialize the logs.
     *
     * @param name
     * @return result A product object that has the matching name with the input
     */
    public Product lookupByName(String name) {
        Product result = null;
        String SQLQuery = "SELECT * FROM products WHERE name='" + name + "'";
        ResultSet data = shopdb.query(SQLQuery);
        try {
            while (data.next()) {
                Product match = new Product(data.getInt("product_id"),
                        data.getString("name"), data.getString("unit"),
                        data.getInt("price"));
                result = match;
            }
        } catch (SQLException e) {
            System.err.println("SQLException: lookup failed");
        } finally {
            if (data != null) {
                try {
                    data.close();
                } catch (SQLException ignore) {
                }
            }
        }
        return result;
    }

    /**
     * Return the details of all registered product
     *
     * @return an array list containing all available products
     */
    public ArrayList<Product> lookupAll() {
        ArrayList<Product> result = new ArrayList();
        String SQLQuery = "SELECT * FROM products";
        ResultSet data = shopdb.query(SQLQuery);
        try {
            while (data.next()) {
                Product entry = new Product(data.getInt("product_id"),
                        data.getString("name"), data.getString("unit"),
                        data.getInt("price"));
                result.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("SQLException: lookup failed");
        } finally {
            if (data != null) {
                try {
                    data.close();
                } catch (SQLException ignore) {
                }
            }
        }
        return result;
    }

    /**
     * Adds an entry of import to the sales log. This method is used when an
     * import is issued. This is for the convenience of the person doing the GUI
     *
     * @param ID The product ID as a primitive integer
     * @param quantity A primitive integer
     * @param ShopID A string. This is a feature to support many shops A shopID
     * syntax is shop + Shop ID (IE: 01) + log
     * @return updateState Indicates if the update is successful or not
     */
    public int importProduct(int ID, int quantity, String ShopID) {
        String SQLQuery = new String("INSERT INTO shop" + ShopID + "log "
                + "(product_id,isimport,quantity,time) "
                + "VALUES ("
                + ID + ",true," + quantity + ",'2014-01-01 06:30:00')");
        //System.out.println(SQLQuery);
        //Timestamps are contant for the timebeing.
        int updateState = shopdb.update(SQLQuery);
        return updateState;
    }

    /**
     * Same thing with importProduct() but the other way around.
     *
     * @param ID A primitive integer
     * @param quantity A primitive integer
     * @param ShopID The same with importProduct()
     * @return updateState Indicates if the the update was success or not
     */
    public int exportProduct(int ID, int quantity, String ShopID) {
        String SQLQuery = new String("INSERT INTO shop" + ShopID + "log "
                + "(product_id,isimport,quantity,time) "
                + "VALUES ("
                + ID + ",false," + quantity + ",'2014-01-01 06:30:00')");
        //System.out.println(SQLQuery);
        //Timestamps are contant for the timebeing.
        int updateState = shopdb.update(SQLQuery);
        return updateState;
    }

    /**
     * Makes a query to the database and inquire the logging of the specified
     * shop. After that,prints out the log. Will probably be remade to return a
     * certain kind of object for easy integration with the GUI. Currently
     * supports all shop. Access control will be looked into later
     *
     * @param ShopID The ID of the shop.
     * @return log An array list of log entries
     */
    public ArrayList<LogEntry> log(String ShopID) {
        String SQLQuery = "SELECT * FROM shop" + ShopID + "log"
                + " WHERE quantity>0";
        ArrayList<LogEntry> log = new ArrayList<>();
        ResultSet data = shopdb.query(SQLQuery);
        if (data != null) {
            try {
                while (data.next()) {
                    int log_id = data.getInt("log_id");
                    int product_id = data.getInt("product_id");
                    Boolean isimport = data.getBoolean("isimport");
                    int quantity = data.getInt("quantity");
                    //Timestamp is not yet implemented
                    //Has to return somesort of object
                    LogEntry entry = new LogEntry(log_id, product_id, isimport, quantity, "");
                    log.add(entry);
                }
            } catch (SQLException e) {
                System.err.println(e);
            } finally {
                if (data != null) {
                    try {
                        data.close();
                    } catch (SQLException ignore) {
                        System.err.println(ignore);
                    }
                }
            }
        }
        return log;
    }

    /**
     * Queries for the inventory of a specific shop. Might return null values if
     * the log tables are not initialized. Basically takes all imports minus the
     * exports.
     *
     * @param ShopID
     * @return inventoryLog A list of inventory entries. Contains the product
     * id, name, unit and the available quantity
     */
    public ArrayList<InventoryEntry> getInventory(String ShopID) {
        String shopName = "shop" + ShopID + "log";
        String SQLQuery = "SELECT inventory.product_id, products.NAME,"
                + " products.UNIT, inventory.quantity FROM("
                + "SELECT product_id, SUM(imports-exports) AS quantity"
                + " FROM (\n"
                + "SELECT A.product_id,imports,exports FROM (\n"
                + "SELECT product_id,SUM(quantity) AS Imports FROM " + shopName
                + " WHERE isimport=true GROUP BY product_id) A \n"
                + "LEFT OUTER JOIN (\n"
                + "SELECT product_id,SUM(quantity) AS Exports FROM " + shopName
                + " WHERE isimport=false GROUP BY product_id) B \n"
                + "ON A.product_id=B.product_id) C \n"
                + "GROUP BY product_id) inventory INNER JOIN products "
                + "ON products.product_id = inventory.product_id";
        ResultSet data = shopdb.query(SQLQuery);
        ArrayList<InventoryEntry> inventoryLog = new ArrayList<>();
        try {
            while (data.next()) {
                String name = data.getString("name");
                int product_id = data.getInt("product_id");
                String unit = data.getString("unit");
                int quantity = data.getInt("quantity");
                InventoryEntry entry = new InventoryEntry(product_id, name,
                        unit, quantity);
                inventoryLog.add(entry);
                System.out.println(entry.toString());
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (data != null) {
                try {
                    data.close();
                } catch (SQLException ignore) {
                    System.err.println(ignore);
                }
            }
        }
        return inventoryLog;
    }

    /**
     * Queries for all shop tables in the database. Not part of the APIs
     * provided to the client
     *
     * @return tableList A list of all tables in the database
     */
    private ArrayList<String> getTables() {
        ArrayList<String> tableList = new ArrayList<>();
        String SQLQuery = "SELECT tablename FROM SYS.SYSTABLES "
                + "WHERE tablename LIKE 'SHOP%'";
        ResultSet data = shopdb.query(SQLQuery);
        try {
            while (data.next()) {
                tableList.add(data.getString("tablename"));
            }
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (data != null) {
                try {
                    data.close();
                } catch (SQLException ignore) {
                    System.err.println(ignore);
                }
            }
        }
        return tableList;
    }

    /**
     * Adds a product to the database. Also initialize the log entry with the
     * product with an import and export of quantity 0. This is to make sure the
     * getinventory function returns a valid value
     *
     * @param name
     * @param unit
     * @param price
     * @return updateState Indicates if the operation is successful
     */
    public int addProduct(String name, String unit, int price) {
        String SQLQuery = "INSERT INTO products(name,unit,price) VALUES('"
                + name + "','" + unit + "'," + price + ")";
        int updateState = shopdb.update(SQLQuery);
        //Must initialize all shop logs with the new product to ensure the 
        //inventory function returns a value that's not null
        Product newProduct = this.lookupByName(name);
        ArrayList<String> tableList = this.getTables();
        Iterator itr = tableList.iterator();
        while (itr.hasNext()) {
            String entry = (String) itr.next();
            String SQLImportInit = "INSERT INTO " + entry
                    + " (product_id,isimport,quantity,time) "
                    + "VALUES ("
                    + newProduct.getID() + ",true," + 0 + ",'2014-01-01 06:30:00')";
            String SQLExportInit = "INSERT INTO " + entry
                    + " (product_id,isimport,quantity,time) "
                    + "VALUES ("
                    + newProduct.getID() + ",false," + 0 + ",'2014-01-01 06:30:00')";
            updateState = shopdb.update(SQLImportInit);
            updateState = shopdb.update(SQLExportInit);
        }
        //The operation can only be deemed as complete if the procedure has
        //also initialized all log tables
        return updateState;
    }

    /**
     * Delete a product from the product table The delete option has been set to
     * cascade on the database so doing this will delete all log entries
     * concerning the product
     *
     * @param id
     * @return updateState Indicates if the operation is successful
     */
    public int deleteProduct(int id) {
        String SQLQuery = "DELETE FROM products WHERE product_id = " + id;
        int updateState = shopdb.update(SQLQuery);
        return updateState;
    }

    /**
     * Adds a new shop to the database, synonymous with adding a new table
     * conforming to the structure of the LogEntry object.
     *
     * @param shopID the new shop ID of the shop that the user wants to create
     * @return the state of the request. -1 means failure
     */
    public int addShop(String shopID) {
        String SQLQuery = "CREATE TABLE Shop" + shopID + "Log(\n"
                + "log_ID INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY "
                + "(START WITH 1, INCREMENT BY 1),\n"
                + "product_ID INTEGER,\n"
                + "isImport BOOLEAN NOT NULL,\n"
                + "quantity INTEGER NOT NULL,\n"
                + "time TIMESTAMP NOT NULL DEFAULT '2014-01-01 06:30:00',\n"
                + "FOREIGN KEY(product_ID) REFERENCES products(product_ID) "
                + "ON DELETE CASCADE ON UPDATE RESTRICT\n"
                + ")";
        int updateState = shopdb.update(SQLQuery);
        //Initialize the newly added shop with the products
        ArrayList<Product> productList = this.lookupAll();
        Iterator itr = productList.iterator();
        while (itr.hasNext()) {
            Product entry = (Product) itr.next();
            updateState = this.importProduct(entry.getID(), 0, shopID);
            updateState = this.exportProduct(entry.getID(), 0, shopID);
        }
        return updateState;
    }
    
    /**
     * Adds a new user to the database
     * @param username
     * @param password
     * @param domain The name of the shop where the user operates
     * @return 
     */
    public int userRegister(String username, String password, String domain){
        int updateState = -1;
        String digestedPassword = password +"0x4B98C701E9C8FE347564F090DD6809843492880C";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(digestedPassword.getBytes());
            byte[] digestedBytes = md.digest();
            BigInteger number = new BigInteger(1, digestedBytes);
            digestedPassword = number.toString();
            //while(digestedPassword.length()<30) digestedPassword = "0"+digestedPassword;
            System.out.println(digestedPassword);
            System.out.println(digestedPassword.length());
        }catch (NoSuchAlgorithmException e){
            System.err.println("Can't use hash algorithm");
        }
        String SQLQuery = String.format("INSERT into USERINFORMATION" + "(username, password, domain)"
                + "values('%s','%s','%s')", username, digestedPassword, domain);
        System.out.println(SQLQuery);

        updateState = shopdb.update(SQLQuery);
        return updateState;
    }

    public boolean checkPassword(String username, String password) {
        boolean logInState = false;
        String digestedPassword = password +"0x4B98C701E9C8FE347564F090DD6809843492880C";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(digestedPassword.getBytes());
            byte[] digestedBytes = md.digest();
            BigInteger number = new BigInteger(1, digestedBytes);
            digestedPassword = number.toString();
            //while(digestedPassword.length()<30) digestedPassword = "0"+ digestedPassword;
            System.out.println(digestedPassword.length());
            System.out.println(digestedPassword);
        }catch (NoSuchAlgorithmException e){
            System.err.println("Can't use hash algorithm");
        }
        String SQLQuery = "SELECT username, password FROM USERINFORMATION WHERE"
                + " username ='" + username + "'AND password ='" + digestedPassword + "'";
        ResultSet data = shopdb.query(SQLQuery);
        try {
            if (data.next()) {
                logInState = true;
            } else {
                logInState = false;
            }
        } catch (SQLException e) {
            System.err.println("Password doesn't exist");
        }
        return logInState;
    }

    /**
     * Deletes an existing shop.
     *
     * @param shopID the ID of the shop the user wants to delete
     * @return the state of the request. -1 means failure
     */
    public int deleteShop(String shopID) {
        String SQLquery = "DROP TABLE shop" + shopID + "log";
        return shopdb.update(SQLquery);
    }

    /**
     * Closes the connection to the database
     */
    public void close() {
        shopdb.closeConnection();
    }
}
