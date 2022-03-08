package TestingFiles;

import com.bham.fsd.assignments.jabberserver.JabberMessage;
/*

This class is used to test is jabberserver and clientConnection can simultaneously handle multiple clients
make sure startServer.java is running before running this file


 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TestClient2 {
  public static void main(String[] args) {
    try(Socket clientSocket = new Socket("localhost", 44444)){
      ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream  ois = new ObjectInputStream(clientSocket.getInputStream());

      while(true) {
        System.out.println("Enter a command:");
        Scanner       input     = new Scanner(System.in);
        String        userInput = input.nextLine();
        JabberMessage jb        = new JabberMessage(userInput);

        oos.writeObject(jb);
        oos.flush();
        if(userInput.equals("signout")){break;}
        JabberMessage reply       = (JabberMessage) ois.readObject();
        String        replyString = reply.getMessage();
        System.out.println(replyString);
      }

    } catch(IOException | ClassNotFoundException e){
      e.printStackTrace();
    }
  }
}
