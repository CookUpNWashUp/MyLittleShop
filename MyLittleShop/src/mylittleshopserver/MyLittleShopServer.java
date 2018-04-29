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
        String command;
        boolean trigger = true;
        Scanner s = new Scanner(System.in);
        // TODO code application logic here            
        Server sys = new Server();
        /*System.out.println("Welcome to the POS prototype."
                + " Pick an option to begin.\n"
                + "Remember to turn on the Database\n"
                + "1) Connect to the database\n"
                + "2) Look up a product by ID\n"
                + "3) Import a product\n"
                + "4) Export a product\n"
                + "5) See the log of store #1\n"
                + "6) See the product list\n");*/
        /*Product pro = sys.lookup(1);
        System.out.println(pro.getID() + "\t" + pro.getName() + "\t" +
                pro.getUnit() + "\t" + pro.getPrice());
        sys.close();*/
        ServerSocket listener = null;
        int clientNumber = 0;
        try {
            listener = new ServerSocket(9898);
        } catch (IOException e) {
            System.err.println(e);
        }

        try {
            while (trigger) {
                //This construct refreshes the listener everytime something is called
                //Just needed to count the # of clients. Can be removed
                new Listener(listener.accept(), clientNumber++, sys).start();
                System.out.println("Server online. Listening port 9898");
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

                // Get messages from the client, line by line; return them
                // capitalized
                while (true) {
                    String input = in.readLine();
                    /*if (input == null || input.equals(".")) {
                        break;
                    }*/
                    System.out.println("Command received: " + input);
                    switch(input){
                        case "q":
                            System.exit(0);
                            break;
                        case "getproduct":
                            out.println("Specify the id");
                            Integer searchID = Integer.parseInt(in.readLine());
                            out.println(sys.lookup(searchID).toString());
                            break;
                        case "getlog":
                            out.println("Specify the shop id");
                            ArrayList result = sys.log(in.readLine());
                            out.println(result.size());
                            Iterator itr = result.iterator();
                            while(itr.hasNext()){
                                LogEntry entry = (LogEntry) itr.next();
                                out.println(entry.toString());
                            }
                            break;
                                
                    }
                    //out.println(input.toUpperCase());
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
