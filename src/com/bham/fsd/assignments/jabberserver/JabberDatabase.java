package com.bham.fsd.assignments.jabberserver;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author iankenny
 * The JabberDatabase class. Formerly known as 'JabberServer'.
 */
public class JabberDatabase {
	
//	private static String dbcommand = "jdbc:postgresql://127.0.0.1:5432/postgres";
//	private static String db = "postgres";
//	private static String pw = "password"; //Password has been changed.
//
//	private Connection conn;
//
//	/**
//	 * Returns the current database connection.
//	 * @return the database connection.
//	 */
//	public Connection getConnection() {
//		return conn;
//	}

	private static String dbcommand = "jdbc:postgresql://127.0.0.1:5432/postgres";
	private static String db = "postgres";
	private static String pw = "password"; //Remove when finished

	private static Connection conn;

	public static Connection getConnection() {
		return conn;
	}

	/**
	 * This method returns a list of the userids of users following the user with the userid in the parameter.
	 * @param userid the userid of the user.
	 * @return a list of the userids of users following the user.
	 */
//	public ArrayList<String> getFollowerUserIDs(int userid) {
//
//		ArrayList<String> ret = new ArrayList<String>();
//
//		try {
//
//			PreparedStatement stmt = conn.prepareStatement("select userida from follows where useridb = ?");
//
//			stmt.setInt(1, userid);
//
//			ResultSet rs = stmt.executeQuery();
//
//			while (rs.next()) {
//				ret.add(rs.getObject("userida").toString());
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return ret;
//	}
	public ArrayList<String> getFollowerUserIDs(int userid) {
		ArrayList<String> followers = new ArrayList<>();

		try{
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM follows WHERE useridb = ?");
			stmt.setInt(1,userid);

			ResultSet result = stmt.executeQuery();

			while(result.next()){
				followers.add(result.getString("userida"));
			}

			result.close();
			stmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		return followers;
	}

	/**
	 * This method returns a list of the userids of users being followed by the user in the parameter.
	 * @param userid The userid of the user.
	 * @return a list of the userids of users being followed by the user.
	 */
//	public ArrayList<String> getFollowingUserIDs(int userid) {
//
//		ArrayList<String> ret = new ArrayList<String>();
//
//		try {
//
//			PreparedStatement stmt = conn.prepareStatement("select useridb from follows where userida = ?");
//
//			stmt.setInt(1, userid);
//
//			ResultSet rs = stmt.executeQuery();
//
//			while (rs.next()) {
//				ret.add(rs.getObject("useridb").toString());
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return ret;
//	}

	public ArrayList<String> getFollowingUserIDs(int userid) {
		ArrayList<String> following = new ArrayList<>();

		try{
			PreparedStatement stmt = conn.prepareStatement("SELECT useridb FROM follows WHERE userida = ?");
			stmt.setInt(1,userid);

			ResultSet result = stmt.executeQuery();

			while(result.next()){
				following.add(result.getString("useridb"));
			}

			result.close();
			stmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		return following;
	}
	
	/**
	 * This method returns a list of pairs of users who follow each other. Each pair is only listed once.
	 * @return a list of 'mutual' follows, i.e. users who follow each other.
	 */
//	public ArrayList<ArrayList<String>> getMutualFollowUserIDs() {
//
//		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
//
//		try {
//
//			PreparedStatement stmt = conn.prepareStatement("select f1.userida, f1.useridb from follows f1 inner join follows f2 on f1.userida = f2.useridb and f1.useridb = f2.userida");
//
//			ResultSet rs = stmt.executeQuery();
//
//			while (rs.next()) {
//				ArrayList<String> r = new ArrayList<String>();
//				r.add(rs.getObject("userida").toString());
//				r.add(rs.getObject("useridb").toString());
//				ret.add(r);
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return removeDuplicates(ret);
//	}

	public ArrayList<ArrayList<String>> getMutualFollowUserIDs() {
		ArrayList<ArrayList<String>> mutualFollowersSet = new ArrayList<>();
		ArrayList<String> mutualFollowerPair;

		try{
			PreparedStatement stmt = conn.prepareStatement("SELECT a.userida As UserA,b.userida AS userB " +
																										 "FROM follows AS a JOIN follows AS b " +
																										 "ON a.userida = b.useridb AND a.useridb = b.userida AND a.userida < b.userida");

			ResultSet result = stmt.executeQuery();

			while(result.next()){
				mutualFollowerPair = new ArrayList<String>();
				mutualFollowerPair.add(result.getString("userA"));
				mutualFollowerPair.add(result.getString("userB"));
				mutualFollowersSet.add(mutualFollowerPair);
			}
			result.close();
			stmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		return mutualFollowersSet;
	}

	/**
	 * This method returns a list of the usernames of users not followed by the user with the userid in the parameter.
	 * @param userid the userid of the user.
	 * @return a list of the usernames of users not followed by the user.
	 */
	public ArrayList<ArrayList<String>> getUsersNotFollowed(int userid) {
		
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();

		try {
			
			PreparedStatement stmt = conn.prepareStatement("select username from jabberuser where userid not in (select useridb from follows where userida = ?)");
			
			stmt.setInt(1, userid);
			
			ResultSet rs = stmt.executeQuery();
	
			while (rs.next()) {
				ArrayList<String> r = new ArrayList<String>();
				r.add(rs.getObject("username").toString());
				ret.add(r);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	/**
	 * This method returns list of [username, jabtext] pairs of jabs liked by the user.
	 * @param userid the userid of the user.
	 * @return a list of [username, jabtext] pairs of the jabs liked by the user.
	 */
//	public ArrayList<ArrayList<String>> getLikesOfUser(int userid) {
//
//		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
//
//		try {
//
//			PreparedStatement stmt = conn.prepareStatement("select username, jabtext from jab natural join jabberuser where jabid in (select jabid from likes where userid = ?)");
//
//			stmt.setInt(1, userid);
//
//			ResultSet rs = stmt.executeQuery();
//
//			while (rs.next()) {
//				ArrayList<String> r = new ArrayList<String>();
//				r.add(rs.getObject("username").toString());
//				r.add(rs.getObject("jabtext").toString());
//				ret.add(r);
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return ret;
//	}
	public ArrayList<ArrayList<String>> getLikesOfUser(int userid) {
		ArrayList<ArrayList<String>> likedMessages = new ArrayList<>();
		ArrayList<String> message;

		try{
			PreparedStatement stmt = conn.prepareStatement("SELECT username,jabtext FROM jabberuser NATURAL JOIN jab " +
																										 "WHERE jabid IN (SELECT jabid FROM likes WHERE userid = ?)");
			stmt.setInt(1,userid);

			ResultSet result = stmt.executeQuery();

			while(result.next()){
				message = new ArrayList<String>();
				message.add(result.getString("username"));
				message.add(result.getString("jabtext"));
				likedMessages.add(message);
			}
			result.close();
			stmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		return likedMessages;
	}

	/**
	 * This method returns the timeline of the user as [username, jabtext] pairs.
	 * @param username the username of the user.
	 * @return the timeline of the user.
	 */
	@Deprecated
	public ArrayList<ArrayList<String>> getTimelineOfUser(String username) {
	
		int userid = this.getUserID(username);
		
		if (userid >= 0) {
			return getTimelineOfUser(userid);
		}
		
		return null;
	}

	/**
	 * This method returns the timeline of the user. Each jab in the returned list has the following
	 * data: [username, jabtext, jabid, number-of-likes].
	 * @param username the username of the user.
	 * @return the timeline of the user.
	 */
	public ArrayList<ArrayList<String>> getTimelineOfUserEx(String username) {
		
		int userid = this.getUserID(username);
		
		if (userid >= 0) {
			return getTimelineOfUserEx(userid);
		}
		
		return null;
	}

	/**
	 * This method returns the timeline of the user as [username, jabtext] pairs.
	 * @param userid the userid of the user.
	 * @return the timeline of the user.
	 */
	@Deprecated
	public ArrayList<ArrayList<String>> getTimelineOfUser(int userid) {

		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();

		try {
			
			PreparedStatement stmt = conn.prepareStatement("select username, jabtext from jab natural join jabberuser where userid in (select useridb from follows where userida = ?)");
		
			stmt.setInt(1, userid);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				ArrayList<String> r = new ArrayList<String>();
				r.add(rs.getObject("username").toString()); 
				r.add(rs.getObject("jabtext").toString()); 
				ret.add(r);
			}
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
						
		return ret;
	}

	/**
	 * This method returns the timeline of the user. Each jab in the returned list has the following
	 * data: [username, jabtext, jabid, number-of-likes].
	 * @param userid the userid of the user.
	 * @return the timeline of the user.
	 */
	public ArrayList<ArrayList<String>> getTimelineOfUserEx(int userid) {

		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();

		try {
			
			PreparedStatement stmt = conn.prepareStatement("select username, jabtext, jabid, (select count(jabid) from likes A where A.jabid = B.jabid group by A.jabid) as jabcount from jab B natural join jabberuser where userid in (select useridb from follows where userida = ?)");
		
			stmt.setInt(1, userid);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				ArrayList<String> r = new ArrayList<String>();
				r.add(rs.getObject("username").toString()); 
				r.add(rs.getObject("jabtext").toString()); 
				r.add(rs.getObject("jabid").toString()); 
				if (rs.getObject("jabcount") != null) {
					r.add(rs.getObject("jabcount").toString()); 
				}
				else r.add("0"); 
				ret.add(r);
			}
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
						
		return ret;
	}

	/**
	 * This method adds a jab from the user username.
	 * @param username the username of the user.
	 * @param jabtext the text of the jab.
	 */
//	public void addJab(String username, String jabtext) {
//
//		int userid = getUserID(username);
//
//		int jabid = getNextJabID();
//
//		try {
//
//			PreparedStatement stmt = conn.prepareStatement("insert into jab (values (?,?,?));");
//
//			stmt.setInt(1,  jabid);
//			stmt.setInt(2,  userid);
//			stmt.setString(3,  jabtext);
//
//			stmt.executeUpdate();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	public void addJab(String username, String jabtext) {
		int newJabID = getNewJabID();
		try{
			PreparedStatement stmt = conn.prepareStatement("SELECT userid FROM jabberuser WHERE username = ?");
			stmt.setString(1,username);

			ResultSet result = stmt.executeQuery();
			result.next();
			int userID = result.getInt("userid");

			stmt = conn.prepareStatement("INSERT INTO jab VALUES(?,?,?)");

			stmt.setInt(1,newJabID);
			stmt.setInt(2,userID);
			stmt.setString(3,jabtext);

			stmt.executeUpdate();
			result.close();
			stmt.close();

		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method adds a new user to the platform.
	 * @param username the username of the new user.
	 * @param emailadd the email address of the new user.
	 */
//	public void addUser(String username, String emailadd) {
//
//		int newid = getNextUserID();
//
//		try {
//
//			PreparedStatement stmt = conn.prepareStatement("insert into jabberuser (values(?,?,?))");
//
//			stmt.setInt(1, newid);
//			stmt.setString(2, username);
//			stmt.setString(3, emailadd);
//
//			stmt.executeUpdate();
//
//			addFollower(newid, newid);
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	public void addUser(String username, String emailadd) {
		int newUserID = getNewUserID();
		try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO jabberuser VALUES(?,?,?)")) {
			stmt.setInt(1, newUserID);
			stmt.setString(2, username);
			stmt.setString(3, emailadd);

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method adds a new follows relationship between users: the user userida follows the user useridb.
	 * @param userida the user who is doing the following.
	 * @param useridb the user being followed.
	 */
//	public void addFollower(int userida, int useridb) {
//
//		try {
//
//			PreparedStatement stmt = conn.prepareStatement("insert into follows (values(?,?))");
//
//			stmt.setInt(1, userida);
//			stmt.setInt(2, useridb);
//
//			stmt.executeUpdate();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	public void addFollower(int userida, int useridb) {

		try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO follows VALUES(?,?)")){
			stmt.setInt(1,userida);
			stmt.setInt(2,useridb);

			stmt.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * This method adds a new follows relationship between users: userida follows user username.
	 * @param userida the user whow is doing the following.
	 * @param username the username of the user being followed.
	 */
	public void addFollower(int userida, String username) {
		
		int useridb = this.getUserID(username);
		
		if (useridb < 0) {
			return;
		}
		
		try {

			PreparedStatement stmt = conn.prepareStatement("insert into follows (values(?,?))");
			
			stmt.setInt(1, userida);
			stmt.setInt(2, useridb);
			
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method adds a new like: the user with user id userid likes the jab with jab id jabid.
	 * @param userid the user.
	 * @param jabid the jab.
	 */
//	public void addLike(int userid, int jabid) {
//		try {
//
//			PreparedStatement stmt = conn.prepareStatement("insert into likes (values(?,?))");
//
//			stmt.setInt(1, userid);
//			stmt.setInt(2, jabid);
//
//			stmt.executeUpdate();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	public void addLike(int userid, int jabid) {
		try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO likes VALUES(?,?)")){
			stmt.setInt(1,userid);
			stmt.setInt(2,jabid);

			stmt.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method returns a list of users with the most followers.
	 * @return a list of the users with the most followers.
	 */
//	public ArrayList<String> getUsersWithMostFollowers() {
//
//		ArrayList<String> ret = new ArrayList<String>();
//
//		String query = "select useridb from follows group by useridb having count (useridb) >= all (select count(useridb) from follows group by useridb order by count(useridb));";
//
//		try {
//
//			PreparedStatement pstmt = conn.prepareStatement(query);
//
//			ResultSet rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				ret.add(rs.getObject("useridb").toString());
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return ret;
//	}
	public ArrayList<String> getUsersWithMostFollowers() {
		ArrayList<String> mostFollowedUsers = new ArrayList<>();

		try{
			PreparedStatement stmt = conn.prepareStatement("SELECT useridb FROM (SELECT useridb,COUNT(useridb) FROM follows GROUP BY useridb) AS a " +
																										 "WHERE a.count IN (SELECT MAX(count) FROM " +
																										 "(SELECT COUNT(useridb) FROM follows GROUP BY useridb ORDER BY COUNT(useridb)) AS maxFollows);");


			ResultSet result = stmt.executeQuery();

			while(result.next()){
				mostFollowedUsers.add(result.getString("useridb"));
			}

			result.close();
			stmt.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		return mostFollowedUsers;

	}
	
	private int getNextJabID() {

		int maxid = -1;
		
		try {
			
			PreparedStatement stmt = conn.prepareStatement("select max(jabid) from jab");
		
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {			
				maxid = rs.getInt(1); // only one result.
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (maxid < 0) {
			return maxid;
		}
		
		return maxid + 1;
	}

	/**
	 * A convenience method to get the userid of the user with user name username.
	 * @param username the username of the user.
	 * @return the userid of the user.
	 */
	public int getUserID(String username) {

		int ret = -1;

		try {

			PreparedStatement stmt = conn.prepareStatement("select userid from jabberuser where username = ?");
			stmt.setString(1, username);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ret = rs.getInt(1); // only one result anyway.
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ret;

	}
	private int getNewUserID(){
		String sqlQuery = "SELECT MAX(userid) FROM jabberuser";
		int newID = -1;

		try(Statement stmt = conn.createStatement()){
			ResultSet result = stmt.executeQuery(sqlQuery);

			while(result.next()){
				newID = result.getInt("max");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		if(newID < 0){
			return newID;
		}
		return newID + 1;
	}


//	private int getNextUserID() {
//
//		String query = "select max(userid) from jabberuser";
//
//		int maxid = -1;
//
//		try (PreparedStatement stmt = conn.prepareStatement(query)) {
//			ResultSet rs = stmt.executeQuery();
//			while (rs.next()) {
//				maxid = rs.getInt(1);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		if (maxid < 0) {
//			return maxid;
//		}
//
//		return maxid + 1;
//	}

	private int getNewJabID(){
		String sqlQuery = "SELECT MAX(jabid) FROM jab";
		int newID = -1;

		try(Statement stmt = conn.createStatement()){
			ResultSet result = stmt.executeQuery(sqlQuery);

			while(result.next()){
				newID = result.getInt("max");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		if(newID < 0){
			return newID;
		}
		return newID + 1;
	}
	
	private ArrayList<ArrayList<String>> removeDuplicates(ArrayList<ArrayList<String>> list) {
		
		for (ArrayList<String> l: list) {	
			Collections.sort(l);
		}
		
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		
		ret.add(list.get(0));
		
		for (ArrayList<String> l: list) {	
			
			if (!ret.contains(l)) {
				ret.add(l);
			}
		}
		
		return ret;
	}

	/**
	 * Default constructor. Connects to the database.
	 */
	public JabberDatabase() {
		connectToDatabase();
		//resetDatabase();
	}
	
	/**
	 * Connects to the database. No need to call this as it is called by the default constuctor.
	 */
	public void connectToDatabase() {

		try {
			conn = DriverManager.getConnection(dbcommand,db,pw);

		}catch(Exception e) {		
			e.printStackTrace();
		}
	}

	/*
	 * Utility method to print an ArrayList of ArrayList<String>s to the console.
	 */
	private static void print2(ArrayList<ArrayList<String>> list) {
		
		for (ArrayList<String> s: list) {
			print1(s);
			System.out.println();
		}
	}
		
	/*
	 * Utility method to print an ArrayList to the console.
	 */
	private static void print1(ArrayList<String> list) {
		
		for (String s: list) {
			System.out.print(s + " ");
		}
	}

	/**
	 * Resets the database to the origi nal data set. This method requires that the files
	 * jabberdef.sql and jabberdata.sql are in the working folder of the JabberDatabase project
	 * (e.g. the root directory of the project). You don't have to use this method. It exists 
	 * in case you need to sort out the database if it has been messed up.
	 */
	public void resetDatabase() {
		
		dropTables();
		
		ArrayList<String> defs = loadSQL("jabberdef");
	
		ArrayList<String> data =  loadSQL("jabberdata");
		
		executeSQLUpdates(defs);
		executeSQLUpdates(data);
	}
	
	private void executeSQLUpdates(ArrayList<String> commands) {
	
		for (String query: commands) {
			
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private ArrayList<String> loadSQL(String sqlfile) {
		
		/*
		 * This method is to be used only by the resetDatabase() code. 
		 * Do not use it yourself to load your own SQL.
		 */
		
		ArrayList<String> commands = new ArrayList<String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(sqlfile + ".sql"));
			
			String command = "";
			
			String line = "";
			
			while ((line = reader.readLine())!= null) {
				
				if (line.contains(";")) {
					command += line;
					command = command.trim();
					commands.add(command);
					command = "";
				}
				
				else {
					line = line.trim();
					command += line + " ";
				}
			}
			
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return commands;
		
	}

	private void dropTables() {
		
		String[] commands = {
				"drop table jabberuser cascade;",
				"drop table jab cascade;",
				"drop table follows cascade;",
				"drop table likes cascade;"};
		
		for (String query: commands) {
			
			try (PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
