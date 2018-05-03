/*
 * Free ware. Made as an exercise.
 * These softwares are made really poorly. Use at your discretion
 */
package mylittleshopserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.util.*;
import java.sql.*;
import java.net.ServerSocket;
import java.net.Socket;

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
        Scanner s = new Scanner(System.in);            
        Server sys = new Server();
        ServerSocket listener = null;
        int clientNumber = 0;
        try {
            listener = new ServerSocket(9898);
        } catch (IOException e) {
            System.err.println(e);
        }

        try {
            while (trigger) {
                /*Create a new instance of a listener. Triggers a message
                  if a client connects
                */
                new Listener(listener.accept(), clientNumber++, sys).start();
                System.out.println("Client connected. Listening port 9898");
            }
        } catch (IOException e) {
            System.err.println(e);
        }finally {
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
                    + " # of Client: "+ clientNumber);
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("Hello, you are connected to the Little Shop."
                        + "Connected clients: "
                        + this.clientNumber + ".");
                out.println("Enter a line with only a 'q' to shutdown"
                        + " the server\n");

                while (true) {
                    String input = in.readLine();
                    System.out.println("Command received: " + input);
                    switch(input){
                        case "q":
                            sys.close();
                            System.exit(0);
                            break;
                        case "getproduct": 
                            out.println("Specify the id");
                            Integer searchID = Integer.parseInt(in.readLine());
                            out.println(sys.lookup(searchID).toString());
                            break;
                        case "getlog":
                            out.println("Specify the shop id");
                            ArrayList resultLog = sys.log(in.readLine());
                            out.println(resultLog.size());
                            Iterator itrLog = resultLog.iterator();
                            while(itrLog.hasNext()){
                                LogEntry entry = (LogEntry) itrLog.next();
                                out.println(entry.toString());
                            }
                            break;
                        case "getinventory":
                            out.println("Specify the shop id");
                            ArrayList resultInv = sys.getInventory(in.readLine());
                            out.println(resultInv.size());
                            Iterator itrInv = resultInv.iterator();
                            while(itrInv.hasNext()){
                                InventoryEntry entry = (InventoryEntry)itrInv.next();
                                out.println(entry.toString());
                            }
                                
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
