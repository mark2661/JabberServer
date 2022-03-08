package com.bham.fsd.assignments.jabberserver;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


/*
@author Mark Cartwright Jr - 1697422
 */

public class ClientConnection implements Runnable{

  private Socket clientSocket;
  private JabberDatabase db;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private boolean terminateMainLoop = false;
  private String  username;
  private int     user_id;

  public ClientConnection(Socket clientSocket, JabberDatabase db){
    this.clientSocket  = clientSocket;
    this.db = db;
    new Thread(this).start();
  }

  @Override
  public void run() {
    try {
      this.ois = new ObjectInputStream(clientSocket.getInputStream());
      this.oos = new ObjectOutputStream(clientSocket.getOutputStream());
      while (!terminateMainLoop) { //use a boolean to control the loop -> value is set to true when userSignOut() is called, and the thread will be terminated.
        try {
          JabberMessage request = (JabberMessage) ois.readObject();
          processRequest(request); // this method will decode the clients request.
        }
        catch (EOFException e){
          /*
          ois.readObject() was constantly throwing EOFExceptions in between waiting for client request. This was causing the ClientConnection to crash
          after the first iteration of the main while loop in run(). To solve this the program keeps catching the exception and ignoring it until the
          input stream returns a JabberMessage Object (since the try-catch is inside the main while loop),
          At which point the request will be processed as normal.
           */
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
      }
    }catch (IOException e){
      e.printStackTrace();
    }
  }
  /*
  This method determines what service the client has requested and passes control over to the relevant helper function
   */
  private void processRequest(JabberMessage request){
    String command = extractCommandFromUserRequest(request);

    switch(command){
      case "signin":
        userSignIn(request);
        break;

      case "register":
        registerNewUser(request);
        break;

      case "signout":
        userSignOut();
        break;

      case "timeline":
        getUserTimeLine();
        break;

      case "users":
        getUsersToFollow();
        break;

      case "post":
        postUserJab(request);
        break;

      case "like":
        addLikeToJabPost(request);
        break;

      case "follow":
        addNewFollow(request);
        break;
      default:
      /* By default the server will simply return from this method and ignore the request if the command is not valid.
         This prevents the server crashing in the event of an invalid command.
       */
    }
  }

  /*
  Below are helper methods that process each of the request defined by the protocol.
   */

  /*
  This method determines if username provided at login is correct. It does this by looking for a corresponding jabberID in the
  Database. The client is notified if the login attempt has succeeded or failed.
   */
  private void userSignIn(JabberMessage request) {
    JabberMessage reply;
    final String LOGIN_SUCCESSFUL = "signedin";
    final String LOGIN_UNSUCCESSFUL = "unknown-user";
    String candidate_username = extractUsernameFromUserRequest(request);
    if(candidate_username == null){return;} // if for some reason the extractUsernameFromUserRequest() returns null. This method will return to prevent the server crashing.

    if (isValidUsername(candidate_username)) {
      this.user_id = db.getUserID(candidate_username); //store userId and username as instance variables for further use.
      this.username = candidate_username;
      reply = new JabberMessage(LOGIN_SUCCESSFUL);
    }

    else {
      reply = new JabberMessage(LOGIN_UNSUCCESSFUL);
    }

    sendServerReplyMessage(reply);
  }

  private void registerNewUser(JabberMessage request){
    String candidate_username = extractUsernameFromUserRequest(request);;
    if(candidate_username == null){return;} // if for some reason the extractUsernameFromUserRequest() returns null. This method will return to prevent the server crashing.

    if(!isValidUsername(candidate_username)){
      String user_email = generateUserEmailAddress(candidate_username);
      db.addUser(candidate_username,user_email);
      userSignIn(new JabberMessage(String.format("signin %s",candidate_username)));
    }
    // if the client tries to register an already registered username, this method will return and the server will ignore the request.
  }

  private void userSignOut(){
    try{
      this.oos.close(); //Release Resources before closing socket.
      this.ois.close();
      this.clientSocket.close();
      this.terminateMainLoop = true; // Terminate main loop in run() method causing the thread to terminate.
    }catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void getUserTimeLine(){
    ArrayList<ArrayList<String>> userTimeline = db.getTimelineOfUserEx(this.user_id);
    final String TIMELINE_REPLY = "timeline";
    sendServerReplyMessage(new JabberMessage(TIMELINE_REPLY,userTimeline));
  }

  private void addLikeToJabPost(JabberMessage request){
    final String ADD_LIKE_TO_JAB_REPLY = "posted";
    int jabIdToLike = extractJabIDFromUserRequest(request);
    db.addLike(this.user_id,jabIdToLike);
    sendServerReplyMessage(new JabberMessage(ADD_LIKE_TO_JAB_REPLY));
  }

  private void getUsersToFollow(){
    final String GET_USERS_TO_FOLLOW_REPLY = "users";
    ArrayList<ArrayList<String>> usersToFollow = db.getUsersNotFollowed(this.user_id);
    sendServerReplyMessage(new JabberMessage(GET_USERS_TO_FOLLOW_REPLY, usersToFollow));
  }

  private void postUserJab(JabberMessage request){
    final String POST_USER_JAB_REPLY = "posted";
    String userJabText = extractUserJabTextFromUserRequest(request);
    db.addJab(this.username, userJabText);
    sendServerReplyMessage(new JabberMessage(POST_USER_JAB_REPLY));
  }

  private void addNewFollow(JabberMessage request){
    final String ADD_NEW_FOLLOW_REPLY = "posted";
    String userToFollow = extractUsernameFromUserRequest(request);
    db.addFollower(this.user_id, userToFollow);
    sendServerReplyMessage(new JabberMessage(ADD_NEW_FOLLOW_REPLY));
  }

  /*
  Below are further helper methods which have been used in the main helper methods to simplify the code and improve readability.
   */

  private String extractUserJabTextFromUserRequest(JabberMessage request){
    return request.getMessage().replaceFirst("post ","");
  }

  private String extractUsernameFromUserRequest(JabberMessage request){
    return request.getMessage().split(" ")[1];
  }

  private String extractCommandFromUserRequest(JabberMessage request){
    return request.getMessage().split(" ")[0];
  }

  private int extractJabIDFromUserRequest(JabberMessage request){
    return Integer.parseInt(request.getMessage().split(" ")[1]);
  }

  private boolean isValidUsername(String username){
    return db.getUserID(username) != -1;
  }

  private String generateUserEmailAddress(String username){
    return String.format("%s@email.com",username);
  }

  private void sendServerReplyMessage(JabberMessage replyMessage){
    try {
      this.oos.writeObject(replyMessage);
      this.oos.flush();
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }
}
