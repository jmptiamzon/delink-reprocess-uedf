package com.sprint.delink.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Model {
	
	public int getEdfErrorCount(String parameters) {
		int count = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = 
					DriverManager.getConnection("", "", "");
			// AND STATUS='ERROR'")
			PreparedStatement statement = conn.prepareStatement("SELECT COUNT(1) FROM edf.edf_inbound_history WHERE FILENAME in ("+ parameters + ")");
			//statement.setString(1, parameters);
			
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				count = rs.getInt(1);
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println("Driver error: " + e.getMessage());
			
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
			
		}

		return count;
	}
}
