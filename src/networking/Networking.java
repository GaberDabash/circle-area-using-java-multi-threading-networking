
package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Abo Muhammad
 */
public class Networking extends Application {
    // Text area for displaying contents
    private TextArea ta = new TextArea();
    
    // Number a client
    private int clientNo = 0;
    ArrayList<Client> clients = new ArrayList<Client>();    

      
    
    @Override
    public void start(Stage primaryStage) {
        
        
       // Create a scene and place it in the stage
        Button addClient = new Button("add a client");
        Button cancel = new Button("cancel");
        VBox vBox = new VBox();
        vBox.setSpacing(100);
        vBox.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(80);
        hBox.getChildren().addAll(addClient,cancel);
        vBox.getChildren().addAll(ta,hBox);
        vBox.fillWidthProperty();
        
        
        
        
        Scene scene = new Scene(vBox, 500, 400);
        
        primaryStage.setTitle("Calculation Server ");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //closing the server and clients
        cancel.setOnAction(e->{
            primaryStage.close();
            
            for (Client client : clients) {
                        client.primaryStage.close();
                    }
            
        });
        
        
        // add new client
        
        
        addClient.setOnAction(e->{
            try{
                Client client = new Client();
                clients.add(client);
                client.display();
                
            }catch(Exception ex){
                System.out.println(ex);
            }
        });
        
        new Thread(
                ()->{
                    try{
                        ServerSocket serverSocket = new ServerSocket(8000);
                        ta.appendText("MultiThread Calculation Server started at "+new Date() + '\n');
                        for(;;){
                            
                            Socket socket =  serverSocket.accept();
                            clientNo++;
                            Platform.runLater(
                                    ()->{
                                        // Display the client number
                                        ta.appendText("Starting thread for client " + clientNo +
                                            " at " + new Date() + '\n');
                                        // Find the client's host name, and IP address
                                        InetAddress inetAddress = socket.getInetAddress();
                                        ta.appendText("Client " + clientNo + "'s host name is "+
                                                inetAddress.getHostName() + "\n");
                                        
                                        
                                    });
                            // Create and start a new thread for the connection
                              new Thread(new HandleClient(socket)).start();
                        }
                        
                    } catch (IOException ex) {
                        System.out.println(ex);
            }
                    
                }).start();
        
    }

    // Define the thread class for handling new connection
 class HandleClient implements Runnable {
     private Socket socket; // A connected socket
          /** Construct a thread */
        public HandleClient(Socket socket) {
            
            this.socket = socket;
            
        }
        public void run(){
            try {
                DataInputStream InputStreamFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream OutputStreamToClient = new DataOutputStream(socket.getOutputStream());
                // listen always to client
                for(;;){
                    double radius = InputStreamFromClient.readDouble();
                    // compute area
                    double area= radius*radius*Math.PI;
                           
                    OutputStreamToClient.writeDouble(area);
                    
                    
                    
                    Platform.runLater(()->{
                     ta.appendText("Radius recieved is "+radius+"\nArea calculated is "+area+"\n");
                     
                     for (Client client : clients) {
                        client.ta.appendText("area just calculated is "+area+"\n");
                    }
                        
                        
                    });
                }
                
            } catch (IOException ex) {
                System.out.println(ex);
         }
        }}
      
        
        
        
        
       public static void main(String[] args) {
        launch(args);
            }

        
}
