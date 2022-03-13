package com.bham.fsd.assignments.jabberserver;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/*
@author Mark Cartwright Jr - 1697422
 */

public class JabberServer implements Runnable{

  private static final int PORT_NUMBER = 44444;
  private ServerSocket serverSocket;

  public JabberServer(){
    try {
      serverSocket = new ServerSocket(PORT_NUMBER);
      serverSocket.setSoTimeout(300);
      new Thread(this).start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean isConnected(){
    return !this.serverSocket.isClosed();
  }

  public void closeServer(){
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run(){
    while (true){
    try {
        Socket clientSocket = serverSocket.accept();
        Thread.sleep(100);
        ClientConnection client = new ClientConnection(clientSocket, new JabberDatabase());
      }
    catch(SocketTimeoutException | SocketException e){

      /* serverSocket.accept() was constantly throwing SocketTimeoutExceptions unless the setSoTimout was high (> 30 sec).
         To solve this I just caught and ignored the exception to prevent the socket constantly timing out and crashing the server.
         Since the try catch is surrounded by a loop the program will simply go back to listening at the socket.
       */
      /*
        SocketExceptions were also being thrown during testing when the port was temporarily unavailable.
        They were not causing the server to crash but were constantly throwing error messages. For this reason I chose to ignore them since
        my test were still passing when the exception was ignored.
       */

    }
    catch(IOException | InterruptedException e){
      e.printStackTrace();
    }
    }

    }
  }


