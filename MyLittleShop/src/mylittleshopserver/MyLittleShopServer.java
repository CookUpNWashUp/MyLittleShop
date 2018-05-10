/*
 * Free ware. Made as an exercise.
 * These softwares are made really poorly. Use at your discretion
 */
package mylittleshopserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.*;
import com.sun.net.ssl.internal.ssl.Provider;
import java.security.Security;

/**
 * Class with main object to invoke the facility (aside from the database)
 *
 * @author Hoang
 */
public class MyLittleShopServer {

    /**
     * Looks up the product with ID 1 in the product table. Add your code here
     * to do stuffs.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean trigger = true;
        Server sys = new Server();

        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.keyStore", "vault/server/MLSServerKS.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "2Y9AMGsU4NVjpaxb");
        ServerSocket listener = null;
        int port = 9898;
        int clientNumber = 0;
        try {
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            listener = (SSLServerSocket) factory.createServerSocket(port);
        } catch (IOException e) {
            System.err.println(e);
        }

        try {
            while (trigger) {
                /*Create a new instance of a listener. Triggers a message
                  if a client connects
                 */
                new Listener((SSLSocket) listener.accept(), clientNumber++, sys).start();
                System.out.println("Client connected. Listening port: " + port);
            }
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                listener.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    private static class Listener extends Thread {

        private Socket socket;
        private int clientNumber;
        private Server sys;

        public Listener(Socket socket, int clientNumber, Server sys) {
            this.socket = socket;
            this.sys = sys;
            this.clientNumber = clientNumber;
            System.out.println("New connection at port."
                    + " # of Client: " + clientNumber);
        }

        @Override
        public void run() {

            try {
                String username = new String();
                boolean logInState = false;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                // A client inputs username and password

              
                out.println("Hello, you are connected to the Little Shop."
                        + "Connected clients: "
                        + this.clientNumber + ".");
                out.println("Enter a line with only a 'q' to shutdown"
                        + " the server\n");
                //Include all available APIs that will be provided
                // for the client side application
                while (true) {
                    String input = in.readLine();
                    System.out.println("Command received: " + input);
                    switch (input) {
                        case "login":
                            out.println("Please enter your username: ");
                            username = in.readLine();
                            out.println("Please enter your password: ");
                            String password = in.readLine();
                            logInState = sys.checkPassword(username, password);
                            out.println(logInState);
                            //Send the client the shopID of the client
                            if (logInState == true){
                                out.println(sys.getUserShop(username));
                                System.out.println(sys.getUserShop(username));
                            }
                            break;
                        case "logout":
                            String signal = in.readLine();
                            if(signal.length()>0) logInState = false;
                            break;
                        case "q":
                            sys.close();
                            System.exit(0);
                            break;
                        case "getproduct":
                            out.println("Specify the id");
                            Integer searchID = Integer.parseInt(in.readLine());
                            Product result = sys.lookup(searchID);
                            if (result != null) {
                                String resID = Integer.toString(result.getID());
                                String resName = result.getName();
                                String resUnit = result.getUnit();
                                String resPrice = Integer.toString(result.getPrice());
                                String resultString = resID + ',' + resName + ',' + resUnit + ',' + resPrice + '\n';
                                out.println(resultString);
                            } else {
                                out.println("0");
                            }
                            break;
                        case "getproductbyname":
                            out.println("Specify the name");
                            String searchName = in.readLine();
                            Product resultProduct = sys.lookupByName(searchName);
                            if (resultProduct != null) {
                                String resID = Integer.toString(resultProduct.getID());
                                String resName = resultProduct.getName();
                                String resUnit = resultProduct.getUnit();
                                String resPrice = Integer.toString(resultProduct.getPrice());
                                String resultString = resID + ',' + resName + ',' + resUnit + ',' + resPrice + '\n';
                                out.println(resultString);
                            } else {
                                out.println("0");
                            }
                            break;
                        case "getlog":
                            out.println("Specify the shop id");
                            ArrayList resultLog = sys.log(in.readLine());
                            //out.println(resultLog);
                            if (!resultLog.isEmpty() && resultLog != null) {
                                out.println(resultLog.size());
                                Iterator itrLog = resultLog.iterator();
                                while (itrLog.hasNext()) {
                                    LogEntry entry = (LogEntry) itrLog.next();
                                    String resLogID = Integer.toString(entry.getLogID());
                                    String resProID = Integer.toString(entry.getProductID());
                                    String resName = entry.getName();
                                    String resIm = Boolean.toString(entry.isIsImport());
                                    String resImport;
                                    if(resIm.equals("true")) resImport = "Import";
                                    else resImport = "Export";
                                    String resQuantity = Integer.toString(entry.getQuantity());
                                    String resTime = entry.getTime();
                                    String resultString = resLogID + ',' + resProID + ','  + resName + ','+ resImport + ',' + resQuantity + ',' + resTime + '\n';
                                    out.println(resultString);
                                }
                            } else {
                                out.println("0");
                            }
                            break;
                        case "getlogbydate":
                            out.println("Specify the timeframe (year->month->day"
                                    + " start->end) and the shopID: ");
                            ArrayList resultLogD = sys.getLogByDate(in.readLine(),
                                    in.readLine(), in.readLine(), in.readLine(),
                                    in.readLine(), in.readLine(), in.readLine());
                            if (!resultLogD.isEmpty()) {
                                out.println(resultLogD.size());
                                Iterator itrLog = resultLogD.iterator();
                                while (itrLog.hasNext()) {
                                    LogEntry entry = (LogEntry) itrLog.next();
                                    String resLogID = Integer.toString(entry.getLogID());
                                    String resProID = Integer.toString(entry.getProductID());
                                    String resName = entry.getName();
                                    String resIm = Boolean.toString(entry.isIsImport());
                                    String resImport;
                                    if(resIm.equals("true")) resImport = "Import";
                                    else resImport = "Export";
                                    String resQuantity = Integer.toString(entry.getQuantity());
                                    String resTime = entry.getTime();
                                    String resultString = resLogID + ',' + resProID + ','  + resName + ','+ resImport + ',' + resQuantity + ',' + resTime + '\n';
                                    out.println(resultString);
                                }
                            } else {
                                out.println("0");
                            }
                            break;    
                        case "getinventory":
                            out.println("Specify the shop id");
                            ArrayList resultInv = sys.getInventory(in.readLine());
                            if (!resultInv.isEmpty()) {
                                out.println(resultInv.size());
                                Iterator itrInv = resultInv.iterator();
                                while (itrInv.hasNext()) {
                                    InventoryEntry entry = (InventoryEntry) itrInv.next();
                                    String resID = Integer.toString(entry.getProduct_id());
                                    String resName = entry.getName();
                                    String resUnit = entry.getUnit();
                                    String resQuantity = Integer.toString(entry.getQuantity());
                                    String resultString = resID + ',' + resName + ',' + resUnit + ',' + resQuantity + '\n';
                                    out.println(resultString);

                                }
                            } else {
                                //String nullString = null;
                                out.println("0");
                            }
                            break;
                        case "getallproducts":
                            ArrayList resultAll = sys.lookupAll();
                            if (!resultAll.isEmpty()) {
                                out.println(resultAll.size());
                                Iterator itrProduct = resultAll.iterator();
                                while (itrProduct.hasNext()) {
                                    Product product = (Product) itrProduct.next();
                                    String resID = Integer.toString(product.getID());
                                    String resName = product.getName();
                                    String resUnit = product.getUnit();
                                    String resPrice = Integer.toString(product.getPrice());
                                    String resultString = resID + ',' + resName + ',' + resUnit + ',' + resPrice + '\n';
                                    out.println(resultString);
                                }
                            } else {
                                //String nullString = null;
                                out.println("0");
                            }
                            break;
                        case "deleteproduct":
                            out.println("Specify the id");
                            Integer searchProductID = Integer.parseInt(in.readLine());
                            out.println(sys.deleteProduct(searchProductID));
                            break;
                        case "import":
                            out.println("Specify the ID,quantity and the ShopID");
                            int importID = Integer.parseInt(in.readLine());
                            int importQuantity = Integer.parseInt(in.readLine());
                            String importShop = in.readLine();
                            out.println(sys.importProduct(importID, importQuantity, importShop));
                            break;
                        case "export":
                            out.println("Specify the ID,quantity and the ShopID");
                            int exportID = Integer.parseInt(in.readLine());
                            int exportQuantity = Integer.parseInt(in.readLine());
                            String exportShop = in.readLine();
                            out.println(sys.exportProduct(exportID, exportQuantity, exportShop));
                            break;
                        case "addproduct":
                            out.println("Specify the name,unit and the price");
                            String newName = in.readLine();
                            String newUnit = in.readLine();
                            int newPrice = Integer.parseInt(in.readLine());
                            out.println(sys.addProduct(newName, newUnit, newPrice));
                            break;
                        case "addshop":
                            out.println("Specify the name of the shop");
                            String shopName = in.readLine();
                            out.println(sys.addShop(shopName));
                            break;
                        case "deleteshop":
                            out.println("Specify the name of the shop");
                            String deleteShop = in.readLine();
                            out.println(sys.deleteShop(deleteShop));
                            break;
                    }
                }
            } catch (IOException e) {
                System.err.println("1 client disconnected");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Can't close the socket.");
                }
            }
        }

    }
}
