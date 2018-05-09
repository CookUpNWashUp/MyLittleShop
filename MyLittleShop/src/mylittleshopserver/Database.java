/*
 * Free ware. Made as an exercise.
 * These softwares are made really poorly. Use at your discretion
 */
package mylittleshopserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Statement;

/**
 * Specifies the interaction of the client software with the database
 * @author Hoang
 * @version 1.0
 */
public class Database {
    private final String user;
    private final String password;
    private Connection link;
    
    /**
     * Constructor method accepts the credential to the database.
     * This is for the purpose of access control.
     * @param userInput
     * @param passwordInput 
     */
    Database(String userInput, String passwordInput){
        this.user = userInput;
        this.password = passwordInput;
        this.link = null;
    }
    
    /**
     * Invokes a connection to the database. The connection is hardwired.
     * In later versions, the server can be respecified if needed
     */
    public void connect(){
        try{ 
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        }catch(ClassNotFoundException e){
            System.out.println(e);
        }
        String url = "jdbc:derby://localhost:1527/shopdb";
        try{
            this.link = DriverManager.getConnection(url,this.user,
                    this.password);
        }catch(SQLNonTransientConnectionException e){
            System.err.println("Connection error: Database not found");
            System.exit(1);
        }catch(SQLException f){
            System.err.println("Database error");
        }    
    }
    
    /**
     * Accepts a query and executes it on the server, based on the established
     * connection. Can't be called without connect() being called first.
     * Passing a ResultSet with a public method runs the risk of resource leak.
     * 
     * @param SQLQuery The intended SQL statement. Later version will probably
     * also accepts scripts
     * @return A result set of the query 
     */
    public ResultSet query(String SQLQuery){
        ResultSet data = null;
        try{
            Statement stmt = this.link.createStatement();
            data = stmt.executeQuery(SQLQuery);
            
        }catch(SQLException e){
            System.err.println(e);
        }
        
        return data;
    }
    /**
     * Same thing with query(), but to run INSERT INTO, DELETE, UPDATE.
     * @param SQLUpdate The intended SQL script. Later version will probably
     * also accepts scripts
     * @return The state of the update as a primitive integer. (-1) indicates an
     * error. Other than that, look at the documentation for 
     * Statement.executeUpdate()
     */
    public int update(String SQLUpdate){
        int updateState = -1;
        try{
            Statement stmt = this.link.createStatement();
            updateState = stmt.executeUpdate(SQLUpdate);
        }catch(SQLException e){
            System.err.println(e);
            System.err.println("SQLException: Update failed. Return equals -1");
        }
        return updateState;
    }
    
    /**
     * Closes the connection to the database
     */
    public void closeConnection(){
        if (this.link != null){
            try {
                this.link.close();
            }catch(SQLException ignore){}
        }
    }
}
