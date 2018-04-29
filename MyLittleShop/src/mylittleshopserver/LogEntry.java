/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mylittleshopserver;

/**
 *
 * @author Hoang
 */
public class LogEntry {
    private final int logID;
    private final int productID;
    private final boolean isImport;
    private final int quantity;
    private final String time;

    public LogEntry(int logID, int productID, boolean isImport,
            int quantity, String time) {
        this.logID = logID;
        this.productID = productID;
        this.isImport = isImport;
        this.quantity = quantity;
        //Time is not yet implemented
        this.time = "NA";
    }

    public int getLogID() {
        return logID;
    }

    public int getProductID() {
        return productID;
    }

    public boolean isIsImport() {
        return isImport;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "LogEntry{" + "logID=" + logID + ", productID=" 
                + productID + ", isImport=" + isImport + ", quantity=" 
                + quantity + ", time=" + time + '}';
    }
    
    
}
