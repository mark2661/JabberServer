package com.bham.fsd.assignments.jabberserver;

import com.bham.fsd.assignments.jabberserver.JabberServer;



/*
@author Mark Cartwright Jr - 1697422
 */
/*
The main method of this class can be used to start a server thread.
Note*
The jabber server constructor starts a thread. So the server can also be started by simply creating a new
JabberServer object.

 */

public class StartServer {
  public static void main(String[] args) {
    JabberServer js = new JabberServer();
    if(js.isConnected()){System.out.println("connected");}
  }
}
