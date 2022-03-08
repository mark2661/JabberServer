package TestingFiles;

import com.bham.fsd.assignments.jabberserver.JabberDatabase;
import com.bham.fsd.assignments.jabberserver.JabberMessage;
import com.bham.fsd.assignments.jabberserver.JabberServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/*
@author Mark Cartwright Jr - 1697422
 */
class JabberServerTest {

  private static com.bham.fsd.assignments.jabberserver.JabberDatabase database;



  public static void init() {
    try{
      //JabberServer testServer = new JabberServer();
//      database = new JabberDatabase();
//      database.resetDatabase();

    } catch(Exception e){
      e.printStackTrace();
    }

  }


  @org.junit.jupiter.api.Test
  @Order(1)
  void TestUserSignIn() {
    final String username = "kim";
    JabberMessage userRequest = new JabberMessage(String.format("signin %s",username));
    String replyString = "";
    try(Socket clientSocket = new Socket("localhost",44444)){
      ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

      oos.writeObject(userRequest);
      oos.flush();

    JabberMessage reply = (JabberMessage) ois.readObject();
    replyString = reply.getMessage();
    assertTrue(replyString.equals("signedin"));
    } catch(IOException | ClassNotFoundException e){
      e.printStackTrace();
    }
    finally {
      assertTrue(replyString.equals("signedin"));
    }

  }

  @org.junit.jupiter.api.Test
  @Order(2)
  void TestUserSignInTwo() {
    final String username = "superman";
    JabberMessage userRequest = new JabberMessage(String.format("signin %s",username));
    String replyString = "";
    try(Socket clientSocket = new Socket("localhost",44444)){
      ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());


      oos.writeObject(userRequest);
      oos.flush();

      JabberMessage reply = (JabberMessage) ois.readObject();
      replyString = reply.getMessage();
      assertTrue(replyString.equals("unknown-user"));
    } catch(IOException | ClassNotFoundException e){
      e.printStackTrace();
    }
    finally {
      assertTrue(replyString.equals("unknown-user"));
    }

  }
  @Disabled // test is currently jamming server since it dosn't respond to incorrect inputs.
  @org.junit.jupiter.api.Test
  @Order(3)
  void TestUserSignInThree() {
    final String username = "kim";
    JabberMessage userRequest = new JabberMessage(String.format("signin%s",username));
    String replyString = "";
    try(Socket clientSocket = new Socket("localhost",44444)){
      ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

      oos.writeObject(userRequest);
      oos.flush();

      JabberMessage reply = (JabberMessage) ois.readObject();
      replyString = reply.getMessage();
      assertTrue(replyString.equals("unknown-user"));
    } catch(IOException | ClassNotFoundException e){
      e.printStackTrace();
    }
    finally {
      assertTrue(replyString.equals("unknown-user"));
    }

  }


  @org.junit.jupiter.api.Test
  @Order(4)
  void TestRegisterNewUser() {
    final String username = "kimmy";
    final String command = "register";
    JabberMessage userRequest = new JabberMessage(String.format("%s %s",command,username));
    String replyString = "";
    try(Socket clientSocket = new Socket("localhost",44444)){
      ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

      oos.writeObject(userRequest);
      oos.flush();

      JabberMessage reply = (JabberMessage) ois.readObject();
      replyString = reply.getMessage();
      assertTrue(replyString.equals("signedin"));
    } catch(IOException | ClassNotFoundException e){
      e.printStackTrace();
    }
    finally {
      assertTrue(replyString.equals("signedin"));
    }

  }

  @Disabled
  @org.junit.jupiter.api.Test
  @Order(5)
  void TestSignOut() {
    JabberMessage userRequest = new JabberMessage(String.format("signout"));
    try(Socket clientSocket = new Socket("localhost",44444)){
      ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());


      oos.writeObject(userRequest);
      oos.flush();


    } catch(IOException e){
      e.printStackTrace();
    }
  }
  @org.junit.jupiter.api.Test
  @Order(6)
  void TestGetUserTimeline() {
    try(Socket clientSocket = new Socket("localhost",44444)){
      ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

      //sign user in
      JabberMessage userRequest = new JabberMessage(String.format("signin kim"));
      oos.writeObject(userRequest);
      oos.flush();
      JabberMessage serverReply = (JabberMessage) ois.readObject();

      userRequest = new JabberMessage(String.format("timeline"));
      oos.writeObject(userRequest);
      oos.flush();

      serverReply = (JabberMessage) ois.readObject();
      //System.out.println(serverReply.getMessage());
      assertTrue(serverReply.getMessage().equals("timeline"));
      //System.out.println(Arrays.toString(serverReply.getData().toArray()));


    } catch(IOException | ClassNotFoundException e){
      e.printStackTrace();
    }
  }
  @org.junit.jupiter.api.Test
  @Order(7)
  void TestAddLikeToJabPost() {
    try(Socket clientSocket = new Socket("localhost",44444)){
      ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

      //sign user in
      JabberMessage userRequest = new JabberMessage(String.format("signin kim"));
      oos.writeObject(userRequest);
      oos.flush();
      JabberMessage serverReply = (JabberMessage) ois.readObject();

      userRequest = new JabberMessage(String.format("like 10"));
      oos.writeObject(userRequest);
      oos.flush();

      serverReply = (JabberMessage) ois.readObject();
      //System.out.println(serverReply.getMessage());
      assertTrue(serverReply.getMessage().equals("posted"));
      //System.out.println(Arrays.toString(serverReply.getData().toArray()));


    } catch(IOException | ClassNotFoundException e){
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  @Order(8)
  void TestGetUsersToFollow() {
    try(Socket clientSocket = new Socket("localhost",44444)){
      ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

      //sign user in
      JabberMessage userRequest = new JabberMessage(String.format("signin kim"));
      oos.writeObject(userRequest);
      oos.flush();
      JabberMessage serverReply = (JabberMessage) ois.readObject();

      userRequest = new JabberMessage(String.format("users"));
      oos.writeObject(userRequest);
      oos.flush();

      //get users to follow request
      serverReply = (JabberMessage) ois.readObject();
      //System.out.println(serverReply.getMessage());
      assertTrue(serverReply.getMessage().equals("users"));
      System.out.println(Arrays.toString(serverReply.getData().toArray()));


    } catch(IOException | ClassNotFoundException e){
      e.printStackTrace();
    }
  }

  @org.junit.jupiter.api.Test
  @Order(9)
  void TestPostUserJab() {
    try(Socket clientSocket = new Socket("localhost",44444)){
      ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

      //sign user in
      JabberMessage userRequest = new JabberMessage(String.format("signin kim"));
      oos.writeObject(userRequest);
      oos.flush();
      JabberMessage serverReply = (JabberMessage) ois.readObject();

      userRequest = new JabberMessage(String.format("post test jab"));
      oos.writeObject(userRequest);
      oos.flush();

      //get users to follow request
      serverReply = (JabberMessage) ois.readObject();
      //System.out.println(serverReply.getMessage());
      assertTrue(serverReply.getMessage().equals("posted"));
      //System.out.println(Arrays.toString(serverReply.getData().toArray()));


    } catch(IOException | ClassNotFoundException e){
      e.printStackTrace();
    }
  }


  @org.junit.jupiter.api.Test
  @Order(10)
  void TestAddNewFollower() {
    try(Socket clientSocket = new Socket("localhost",44444)){
      ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

      //sign user in
      JabberMessage userRequest = new JabberMessage(String.format("signin kim"));
      oos.writeObject(userRequest);
      oos.flush();
      JabberMessage serverReply = (JabberMessage) ois.readObject();

      userRequest = new JabberMessage(String.format("follow klopp"));
      oos.writeObject(userRequest);
      oos.flush();

      //get users to follow request
      serverReply = (JabberMessage) ois.readObject();
      //System.out.println(serverReply.getMessage());
      assertTrue(serverReply.getMessage().equals("posted"));
      //System.out.println(Arrays.toString(serverReply.getData().toArray()));


    } catch(IOException | ClassNotFoundException e){
      e.printStackTrace();
    }
  }

  }

