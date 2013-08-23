package com.encima.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.drools.builder.KnowledgeBuilder;

public class DBTools {

	public static Connection dbConnect(String user, String pwd, String db) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String connString = "jdbc:mysql://localhost:3306/" + db;
			Connection conn = DriverManager.getConnection(connString, user, pwd);
			return conn;
		}catch(Exception e) {
			return null;
		}
	}
	
	public static ResultSet execute(Connection conn, String query) {
		Statement stmt;
		ResultSet rs;
		try {
			stmt = conn.createStatement();
			System.out.println("***Executing: " + query);
			Boolean res = stmt.execute(query);
			if(res)
				rs = stmt.getResultSet();
			else
				return null; 
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getLocationDescription(int locID) {
		Connection cnx = dbConnect("root", "root", "gsn");
		ResultSet rs = execute(cnx, String.format("SELECT * FROM location WHERE id=%d;", locID));
		String descrip = null;
		try {
			while(rs.next()) {
				descrip = rs.getString("description");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return descrip;
	}
	
	public static void close(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Error closing connection");
			e.printStackTrace();
		}
	}
	
	public static int getID(Connection conn, String table, String field, String max) {
		String query = "SELECT %s(%s) AS %s FROM %s";
		ResultSet rs = execute(conn, String.format(query, max, field, field, table));
		try {
			rs.first();
			return rs.getInt(field) + 1;
		} catch (SQLException e) {
			System.out.println(e);
			return 1;
		}
	}
	
   public static String readFile(String path) {
	   //reading the file line by line allows more control on the content
    	StringBuilder contentBuilder = new StringBuilder();
    	try {
    	    BufferedReader in = new BufferedReader(new FileReader(path));
    	    String str;
    	    while ((str = in.readLine()) != null) {
//    	    	System.out.println(str);
    	        contentBuilder.append(str + " ");
    	    }
    	    in.close();
    	} catch (IOException e) {
    	}
    	return contentBuilder.toString();
    }
   
   public static void addDBRulesFromFile(String db, String user, String pwd, String path) {
		Connection conn = dbConnect(user, pwd, db);
		String rules = readFile(path);
		int id = getID(conn, "rules", "id", "MAX");
		String insert = "INSERT INTO rules VALUE(%d, '%s');";
		execute(conn, String.format(insert, id, rules));
		close(conn);
	}
   
   public static void loadDBRules(String db, String user, String pwd, int id, KnowledgeBuilder kbuilder) {
	   String query = "SELECT * FROM rules";
	   //Only select particular rules if the user has specified
	   if(id > 0) {
		   query = "SELECT * FROM rules WHERE id = " + id + ";";
	   }
	   Connection conn = dbConnect(user, pwd, db);
	   ResultSet rs = execute(conn, query);
	   try {
			while(rs.next()) {
				String rule = rs.getString("rule");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	   close(conn);
   }
	
}
