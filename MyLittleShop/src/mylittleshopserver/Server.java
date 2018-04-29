/*
 * Free ware. Made as an exercise.
 * These softwares are made really poorly. Use at your discretion
 */
package mylittleshopserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * Describes the POS functions of the program.
 * @author Hoang
 * @version 1.0
 */
public class Server {
    private final Database shopdb;
    
    /**
     * Constructor method. Establishes the connection to the database.
     * This might be changed in the future to support access control
     */
    Server(){
        this.shopdb = new Database("root","root");
        this.shopdb.connect();
    }
    
    /**
     * Looks up the product in question by ID. Made to support the bar code
     * reader to be implemented
     * @param ID The ID of the product as a primitive integer.
     * @return A product object, containing all relevant info
     */
    public Product lookup(int ID){
        Product result = null;
        String SQLQuery = "SELECT * FROM products WHERE product_id="+ID;
        ResultSet data = shopdb.query(SQLQuery);
        try{
            while(data.next()){
                Product match= new Product(data.getInt("product_id"),
                        data.getString("name"),data.getString("unit"),
                        data.getInt("price"));
                result = match;
            }
        }catch (SQLException e){
            System.err.println("SQLException: lookup failed");
        }finally{
            if (data != null){
                try{
                    data.close();
                }
                catch(SQLException ignore){
                }
            }
        }
        return result;
    }
    
    /**
     * Adds an entry of import to the sales log. This method is used when an
     * import is issued. This is for the convenience of the person doing the GUI
     * @param ID The product ID as a primitive integer
     * @param quantity A primitive integer
     * @param ShopID A string. This is a feature to support many shops
     * A shopID syntax is shop + Shop ID (IE: 01) + log
     */
    public void importProduct(int ID, int quantity,String ShopID){
        String SQLQuery = new String("INSERT INTO shop"+ShopID+"log "
                + "(product_id,isimport,quantity,time) "
                + "VALUES ("
                + ID + ",true," + quantity + ",'2014-01-01 06:30:00')");
        //System.out.println(SQLQuery);
        //Timestamps are contant for the timebeing.
        int updateState = shopdb.update(SQLQuery);
    }
    
    /**
     * Same thing with importProduct() but the other way around.
     * @param ID A primitive integer
     * @param quantity A primitive integer
     * @param ShopID The same with importProduct()
     */
    public void exportProduct(int ID, int quantity,String ShopID){
        String SQLQuery = new String("INSERT INTO shop"+ShopID+"log "
                + "(product_id,isimport,quantity,time) "
                + "VALUES ("
                + ID + ",false," + quantity + ",'2014-01-01 06:30:00')");
        //System.out.println(SQLQuery);
        //Timestamps are contant for the timebeing.
        int updateState = shopdb.update(SQLQuery);
    }
    
    /**
     * Makes a query to the database and inquire the logging of the specified
     * shop. After that,prints out the log. Will probably be remade to return
     * a certain kind of object for easy integration with the GUI.
     * Currently supports all shop. Access control will be looked into later
     * @param ShopID The ID of the shop.
     * @return log An array list of log entries
     */
    public ArrayList<LogEntry> log(String ShopID){
        String SQLQuery = "SELECT * FROM shop" + ShopID + "log";
        System.out.println(SQLQuery);
        ResultSet data = shopdb.query(SQLQuery);
        ArrayList<LogEntry> log = new ArrayList<>();
        try{
            while (data.next()) {
                int log_id = data.getInt("log_id");
                int product_id = data.getInt("product_id");
                Boolean isimport = data.getBoolean("isimport");
                int quantity = data.getInt("quantity");
                //Timestamp is not yet implemented
                //Has to return somesort of object
                LogEntry entry = new LogEntry(log_id,product_id,isimport,quantity,"");
                log.add(entry);
            }
        }catch(SQLException e){
            System.err.println(e);
        }finally{
            if (data != null){
                try{
                    data.close();
                }
                catch(SQLException ignore){
                }
            }
        }
        return log;
    }
    
    /**
     * Closes the connection to the database
     */
    public void close(){
        shopdb.closeConnection();
    }
}
