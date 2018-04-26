
package networking;

import java.io.*;
 import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
 import javafx.application.Application;
 import javafx.geometry.Insets;
 import javafx.geometry.Pos;
 import javafx.scene.Scene;
 import javafx.scene.control.Label;
 import javafx.scene.control.ScrollPane;
 import javafx.scene.control.TextArea;
 import javafx.scene.control.TextField;
 import javafx.scene.layout.BorderPane;
 import javafx.stage.Stage;

 public  class Client  {
     
  
 // IO streams
     
 DataOutputStream toServer = null;
 DataInputStream fromServer = null;
 TextField tf = new TextField();
 TextArea ta = new TextArea();
 Stage primaryStage = new Stage();
 void display(){


 
     
 // Panel p to hold the label and text field
 BorderPane paneForTextField = new BorderPane();
 paneForTextField.setPadding(new Insets(5, 5, 5, 5));
 paneForTextField.setStyle("-fx-border-color: green");
 paneForTextField.setLeft(new Label("Enter a radius: "));

 
 tf.setAlignment(Pos.BOTTOM_RIGHT);
 paneForTextField.setCenter(tf);

 BorderPane mainPane = new BorderPane();
 // Text area to display contents
 
 mainPane.setCenter(new ScrollPane(ta));
 mainPane.setTop(paneForTextField);

 // Create a scene and place it in the stage
 Scene scene = new Scene(mainPane, 450, 200);
 primaryStage.setTitle("Client"); // Set the stage title
 primaryStage.setScene(scene); // Place the scene in the stage
 primaryStage.show(); // Display the stage

 

    try {
        
          // Create a socket to connect to the server
         Socket socket = new Socket("localhost", 8000);
           
              

             // Create an input stream to receive data from the server
        fromServer = new DataInputStream(socket.getInputStream());

        // Create an output stream to send data to the server
        toServer = new DataOutputStream(socket.getOutputStream());
        
      
                 }
        catch (IOException ex) {
             ta.appendText(ex.toString() + '\n');
                                        }
    //performing an operation
    tf.setOnAction(e -> {
 try {

 double radius = Double.parseDouble(tf.getText().trim());

 // Send the radius to the server
 toServer.writeDouble(radius);
 toServer.flush();

 // Get area from the server
 double area = fromServer.readDouble();
 

 // Display to the text area
 ta.appendText("Radius is " + radius + "\n");
 ta.appendText("Area received from the server is "+ area + '\n');
   
 
 //sending to other client
 
 
        }
        catch (IOException ex) {
            System.err.println(ex); }
 });
    
 }
 

 }
 
 

        
 
