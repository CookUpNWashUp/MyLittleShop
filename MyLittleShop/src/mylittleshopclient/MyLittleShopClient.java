/*
 * Free ware. Definitely an exercise.
 * These softwares are made really poorly. Use at your discretion
 * 
 */
package mylittleshopclient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Hoang
 */
public class MyLittleShopClient {

    private BufferedReader in;
    private PrintWriter out;
    
    public void communicate(){
        String response;
        try {
            response = in.readLine();
            if (response == null || response.equals("")) {
                System.exit(0);
            }
        } catch (IOException ex) {
            response = "Error: " + ex;
        } catch (NullPointerException f){
            System.err.println("Connection not established."
                    + " Cant's communicate");
            System.exit(1);
        }
    }
    
    public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
        String serverAddress = "localhost";

        // Make connection and initialize streams
        Socket socket = new Socket(serverAddress, 9898);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Consume the initial welcoming messages from the server
        for (int i = 0; i < 3; i++) {
           System.out.println(in.readLine() + "\n");
        }
        //Send a string to the server
        Scanner scan = new Scanner(System.in);
        while(true){
            String message = scan.nextLine();
            out.println(message);
            if (message.equals("q")){

                System.exit(0);
            }else if (message.equals("getproduct")){
                System.out.println(in.readLine() + "\n");
                out.println(scan.nextLine());
                System.out.println(in.readLine() + "\n");
            }else if (message.equals("getlog")){
                System.out.println(in.readLine() + "\n");
                out.println(scan.nextLine());
                int size = Integer.parseInt(in.readLine());
                for (int i=0;i<size;i++)
                    System.out.println(in.readLine() + "\n");
            }
            
        }
    }

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        // TODO code application logic here
        MyLittleShopClient client = new MyLittleShopClient();
        try{
            client.connectToServer();
        }catch(IOException e){
            System.err.println("Server offline");
        }
        while(true){
            client.communicate();
        }
        
    }

}
