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
     * Sets up the server side socket and process requests from the client side
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean trigger = true;
        Server sys = new Server();
        //Registering the JSSE provider
        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.keyStore", "vault/server/MLSServerKS.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "2Y9AMGsU4NVjpaxb");
        SSLServerSocket listener = null;
        int clientNumber = 0;
        try {
            //Create a SSL socket
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            listener = (SSLServerSocket) factory.createServerSocket(9898);
        } catch (IOException e) {
            System.err.println(e);
        }

        try {
            while (trigger) {
                /*Create a new instance of a listener. Triggers a message
                  if a client connects
                  SSLServerSocket only accepts the connection. The actual
                  conversation happens on a SSLSocket that will be created on
                  the same port
                 */
                new Listener((SSLSocket) listener.accept(), clientNumber++, sys).start();
                System.out.println("Client connected. Listening port 9898");
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

    /**
     * Specifies the server behavior upon having received a client. The class is
     * threaded so as to be able to handle many clients at once This class is
     * private to the server so as to increase security. This is to be tested
     */
    private static class Listener extends Thread {

        private SSLSocket socket;
        private int clientNumber;
        private Server sys;

        /**
         * Constructor of the private class.
         *
         * @param socket The socket that's to be opened to handle client
         * connection
         * @param clientNumber The variable to count the current number of
         * connected client
         * @param sys The instance of the database.
         */
        public Listener(SSLSocket socket, int clientNumber, Server sys) {
            this.socket = socket;
            this.sys = sys;
            this.clientNumber = clientNumber;
            System.out.println("New connection at port."
                    + " # of Client: " + clientNumber);
        }

        @Override
        public void run() {
            //Scanner scanner = null;
            try {
                String username = new String();
                boolean logInState = false;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                // A client inputs username and password

                while (!logInState) {
                    out.println("Please enter your username: ");
                    username = in.readLine();
                    out.println("Please enter your password: ");
                    String password = in.readLine();
                    logInState = sys.checkPassword(username, password);
                    out.println(logInState);
                }
                //Send the client the shopID of the client
                out.println(sys.getUserShop(username));

                // Send a welcome message to the client.
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
                        //Service list
                        case "getlogbydate":
                            out.println("Specify the timeframe (year->month->day"
                                    + " start->end) and the shopID: ");
                            ArrayList resultLog = sys.getLogByDate(in.readLine(),
                                    in.readLine(), in.readLine(), in.readLine(),
                                    in.readLine(), in.readLine(), in.readLine());
                            if (!resultLog.isEmpty()) {
                                out.println(resultLog.size());
                                Iterator itrLog = resultLog.iterator();
                                while (itrLog.hasNext()) {
                                    LogEntry entry = (LogEntry) itrLog.next();
                                    out.println(entry.toString());
                                }
                            } else {
                                String nullString = null;
                                out.println(nullString);
                            }
                            break;
                        case "login":
                            out.println("Please enter your username: ");
                            username = in.readLine();
                            out.println("Please enter your password: ");
                            String password = in.readLine();
                            logInState = sys.checkPassword(username, password);
                            out.println(logInState);
                            //Send the client th
                            out.println(sys.getUserShop(username));
                        case "logout":
                            String signal = in.readLine();
                            if (signal.length() > 0) {
                                logInState = false;
                            }
                        case "q":
                            sys.close();
                            System.exit(0);
                            break;
                        case "getproduct":
                            out.println("Specify the id");
                            Integer searchID = Integer.parseInt(in.readLine());
                            try {
                                out.println(sys.lookup(searchID).toString());
                            } catch (NullPointerException npe) {
                                out.println("No such product");
                            }
                            break;
                        case "getallproduct":
                            ArrayList<Product> resultProductSet = sys.lookupAll();
                            if (!resultProductSet.isEmpty()) {
                                out.println(resultProductSet.size());
                                Iterator itrLog = resultProductSet.iterator();
                                while (itrLog.hasNext()) {
                                    Product entry = (Product) itrLog.next();
                                    out.println(entry.toString());
                                }
                            } else {
                                String nullString = null;
                                out.println(nullString);
                            }
                            break;
                        case "getlog":
                            out.println("Specify the shop id");
                            ArrayList resultLogAll = sys.log(in.readLine());
                            if (!resultLogAll.isEmpty()) {
                                out.println(resultLogAll.size());
                                Iterator itrLog = resultLogAll.iterator();
                                while (itrLog.hasNext()) {
                                    LogEntry entry = (LogEntry) itrLog.next();
                                    out.println(entry.toString());
                                }
                            } else {
                                String nullString = null;
                                out.println(nullString);
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
                                    out.println(entry.toString());
                                }
                            } else {
                                String nullString = null;
                                out.println(nullString);
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
