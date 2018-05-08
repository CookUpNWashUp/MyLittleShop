/*
 * Free ware. Definitely an exercise.
 * These softwares are made really poorly. Use at your discretion
 * 
 */
package mylittleshopserver;

/**
 *
 * @author Hoang
 */
public class LogEntry {

    private final int logID;
    private final int productID;
    private final String name;
    private final boolean isImport;
    private final int quantity;
    private final String time;

    /**
     * Constructor for a log entry. Each log entry responds to 1 type of product
     *
     * @param logID the id of a log
     * @param productID the id of the product in concern
     * @param isImport specify if the action was an import(buy) or an
     * export(sell)
     * @param quantity quantity of the product in concern
     * @param time Timestamp of the action
     */
    public LogEntry(int logID, int productID, String name, boolean isImport,
            int quantity, String time) {
        this.logID = logID;
        this.productID = productID;
        this.name = name;
        this.isImport = isImport;
        this.quantity = quantity;
        this.time = time;
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

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "LogEntry{" + "logID=" + logID + ", productID=" + productID
                + ", name=" + name + ", isImport=" + isImport + ", quantity="
                + quantity + ", time=" + time + '}';
    }

}
