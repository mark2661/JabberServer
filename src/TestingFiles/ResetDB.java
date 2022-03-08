package TestingFiles;

import com.bham.fsd.assignments.jabberserver.JabberDatabase;

/*
@author Mark Cartwright Jr - 1697422
 */
/*
This class is used to reset the Database for testing purposes, using the resetDatabase() method of Jabber Database.
 */

public class ResetDB {
  public static void main(String[] args) {
    JabberDatabase db = new JabberDatabase();
    db.resetDatabase();
    System.out.println("db reset");
  }
}
