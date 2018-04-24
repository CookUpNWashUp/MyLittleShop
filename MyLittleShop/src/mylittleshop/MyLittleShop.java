/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mylittleshop;
import java.util.*;
import java.sql.*;
/**
 *
 * @author Hoang
 */


public class MyLittleShop {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{ 
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        }catch(ClassNotFoundException e){
            System.out.println(e);
        }
        Properties connectionProps = new Properties();
        connectionProps.put("user","root");
        connectionProps.put("password","root");
        try{
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopdb","root","root");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM products");

            while (rs.next()) {
                int product_id = rs.getInt("product_id");
                String s = rs.getString("Name");
                int n = rs.getInt("Price");
                String unit = rs.getString("Unit");
                System.out.println(product_id +"\t"+s + "\t"+ unit + "\t" + n );
            }
        }catch(SQLException e){
            System.err.println(e);
        }                 
     
      }
    
}
