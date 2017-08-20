package org.openshift;
import java.sql.*;

import java.util.Random;

public class InsultGenerator {
	
	public String generateInsult() {
		String vowels = "AEIOU";
		String article = "an";
		String theInsult = "";
		String log = "";
		try {
			
			String host = System.getenv("POSTGRESQL_SERVICE_HOST") == null ? "localhost:5432" :  System.getenv("POSTGRESQL_SERVICE_HOST");
			String databasaName = System.getenv("POSTGRESQL_DATABASE") == null ? "insult" : System.getenv("POSTGRESQL_DATABASE"); 
			String username = System.getenv("POSTGRESQL_USER") == null ? "insult" : System.getenv("POSTGRESQL_USER");
			String password = System.getenv("PGPASSWORD") == null ? "insult" : System.getenv("PGPASSWORD");
					
			String databaseURL = "jdbc:postgresql://" + host + "/" + databasaName;
			
			log = databaseURL + " : " + username + " : " + password;
			Connection connection = DriverManager.getConnection(databaseURL, username, password);
			if (connection != null) {
				String SQL = "select a.string AS first, b.string AS second, c.string AS noun from short_adjective a , long_adjective b, noun c ORDER BY random() limit 1";
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(SQL);
				while (rs.next()) {
					if (vowels.indexOf(rs.getString("first").charAt(0)) == -1) {
						article = "a";
					}
					theInsult = String.format("Thou art %s %s %s %s!", article, rs.getString("first"),
							rs.getString("second"), rs.getString("noun"));
				}
				rs.close();
				connection.close();
			}
		}catch(Exception e) {
			return "Database connection problem!\n" + e.getMessage() + "\n" + log;
		}
		return theInsult;
	}

}
