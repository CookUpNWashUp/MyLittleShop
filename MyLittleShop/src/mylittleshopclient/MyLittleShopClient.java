/*
 * Free ware. Definitely an exercise.
 * These softwares are made really poorly. Use at your discretion
 * 
 */
package mylittleshopclient;

import javax.net.ssl.*;
import com.sun.net.ssl.*;
import com.sun.net.ssl.internal.ssl.Provider;
import java.security.Security;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Prototype client of the My Little Shop project. Console GUI only.
 * @author Hoang
 */
public class MyLittleShopClient {

    private BufferedReader in;
    private PrintWriter out;
  
    /**
     * Connects to the server and persistently process the input based on
     * the input from the user
     * For the list of all available APIs and the client side behavior, please
     * look at the if clauses in the while loop.
     * @throws IOException 
     */
    public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
        String serverAddress = "localhost";
        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.trustStore", "MLSTrustedKS.ks");
        System.setProperty("javax.net.ssl.trustStorePassword", "2Y9AMGsU4NVjpaxb");
        // Make connection
        //Socket socket = new Socket(serverAddress, 9898);
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket)sslsocketfactory.createSocket(serverAddress,9898);
        socket.startHandshake();
        //Initializing the streams for communication
        
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scan = new Scanner(System.in);
        //Log in protocol
        System.out.println(in.readLine() + "\n");
        out.println(scan.nextLine());
        System.out.println(in.readLine() + "\n");
        out.println(scan.nextLine());
        //Registration state check
        if (in.readLine().equals("true")){
            /*System.out.println(in.readLine() + "\n");
            out.println(scan.nextLine());
            out.println(scan.nextLine());
            out.println(scan.nextLine());*/
        }else{
            System.out.println("User not found");
        }
        // Consume the initial welcoming messages from the server
        for (int i = 0; i < 3; i++) {
           System.out.println(in.readLine() + "\n");
        }
        //Input handling to issue to the server
        
        while(true){
            String message = scan.nextLine();
            out.println(message);
            if (message.equals("q")){  
                System.exit(0);
                
            }else if (message.equals("getproduct")){
                System.out.println(in.readLine() + "\n");
                out.println(scan.nextLine());
                System.out.println(in.readLine() + "\n");
            }else if (message.equals("getlog")|| message.equals("getinventory")){
                System.out.println(in.readLine() + "\n");
                out.println(scan.nextLine());
                int size = 0;
                try{
                    size = Integer.parseInt(in.readLine());
                    for (int i=0;i<size;i++)
                    System.out.println(in.readLine() + "\n");
                }
                catch (NumberFormatException e){
                    System.out.println("No records found");
                }
                
                
            }else if (message.equals("deleteproduct")){
                System.out.println(in.readLine() + "\n");
                out.println(scan.nextLine());
                if (Integer.parseInt(in.readLine())==-1)
                    System.out.println("Request failed");
                else System.out.println("Request succeed");
            }else if (message.equals("export")||
                    message.equals("addproduct")||message.equals("import")){
                System.out.println(in.readLine() + "\n");
                out.println(scan.nextLine());
                out.println(scan.nextLine());
                out.println(scan.nextLine());
                if (Integer.parseInt(in.readLine())==-1)
                    System.out.println("Request failed");
                else System.out.println("Request succeed");
            }else if (message.equals("addshop")||message.equals("deleteshop")){
                System.out.println(in.readLine() + "\n");
                out.println(scan.nextLine());
                if (Integer.parseInt(in.readLine())==-1)
                    System.out.println("Request failed");
                else System.out.println("Request succeed");
            }else if (message.equals("getallproduct")){
                int size = 0;
                try{
                    size = Integer.parseInt(in.readLine());
                    for (int i=0;i<size;i++)
                    System.out.println(in.readLine() + "\n");
                }
                catch (NumberFormatException e){
                    System.out.println("No records found");
                }
            }
        }
    }

    /**
     * A main class to execute the client side application
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        // TODO code application logic here
        MyLittleShopClient client = new MyLittleShopClient();
        try{
            client.connectToServer();
        }catch(IOException e){
            System.err.println(e);
            System.err.println("Server offline");
        }
        
    }

}
