package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		try{
			Connection connection = getConnection(); 			
			PreparedStatement stmt = connection.prepareStatement("select keyword, response from t1");
			//return all results containing a particular phrase 
			ResultSet rs = stmt.executeQuery(); 
			while(rs.next()){
				if(rs.getString(1).toLowerCase().equals(text.toLowerCase())){
					return rs.getString(2); 
				}
			}
			rs.close(); 
			stmt.close(); 
			connection.close(); 
		} catch(Exception e){
			System.out.println(e); 
		}
		return null;
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
