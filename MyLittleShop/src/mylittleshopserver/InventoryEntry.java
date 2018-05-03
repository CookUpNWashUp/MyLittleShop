
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
public class InventoryEntry {
    private final int product_id;
    private final String name;
    private final String unit;
    private final int quantity;

    public InventoryEntry(int product_id, String name, String unit, int inventory) {
        this.product_id = product_id;
        this.name = name;
        this.unit = unit;
        this.quantity = inventory;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "InventoryEntry{" + "product_id=" + product_id + ", name=" 
                + name + ", unit=" + unit + ", quantity=" + quantity + '}';
    }
    
    
}
