package com.sprint.delink.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	public List<String> getQueryToWrite(String parameters) {
		List<String> queries = new ArrayList<>();
		String seqNo = "";
		String recordId = "";
		int count = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn =
					DriverManager.getConnection("", "", "");
			
			PreparedStatement statement = conn.prepareStatement(
					"SELECT HDR.FILE_NAME,DET.unique_sequence_number,DET.record_id,DET.MEID_HEX,DET.MEID_DECIMAL,DET.IMEI_DECIMAL,TOTAL_DEVICE_COUNT,DEVICE_COUNT " + 
					"FROM EDF.EDF_HEADER_STG_INT HDR,edf.edf_details_stg_int DET " + 
					"WHERE HDR.unique_sequence_number = DET.unique_sequence_number " + 
					"AND HDR.FILE_NAME IN (" + parameters + ") and DET.error_description is not null "
			);
			
			ResultSet rs = statement.executeQuery();
			
			while (rs.next()) {
				seqNo += rs.getString(2) + ",";
				recordId += rs.getString(3) + ",";
				
				queries.add(
						"UPDATE EDF.EDF_HEADER_STG_INT\n" + 
						"SET TOTAL_DEVICE_COUNT = " + rs.getString(7) + ", DEVICE_COUNT = " + rs.getString(8) + ",\n" + 
						"STATUS = 'NEW', ERROR_DESCRIPTION = NULL\n" + 
						"WHERE FILE_NAME = '" + rs.getString(1) + "'\n" + 
						"AND UNIQUE_SEQUENCE_NUMBER = '" + rs.getString(2)  + "'\n"	+
						"-- 1 row\n"
				);
			}
			
			seqNo = seqNo.substring(0, seqNo.length() - 1);
			recordId = recordId.substring(0, recordId.length() - 1);
			
			statement = conn.prepareStatement(
					"SELECT COUNT(1) FROM EDF.EDF_DETAILS_STG_INT " + 
					"WHERE UNIQUE_SEQUENCE_NUMBER IN (SELECT UNIQUE_SEQUENCE_NUMBER " + 
					"FROM EDF.EDF_HEADER_STG_INT " + 
					"WHERE FILE_NAME IN ( " + parameters + ") " + 
					"and unique_sequence_number IN (" + seqNo + ")) "		
			);
			
			rs = statement.executeQuery();
			
			while (rs.next()) {
				count = rs.getInt(1);
			}
			
			queries.add(
					"UPDATE EDF.EDF_DETAILS_STG_INT\n" + 
					"SET PRL = NULL,\n" + 
					"AKEY = NULL,\n" + 
					"AKEY_CHECKSUM = NULL, " + 
					"PUC1 = NULL,\n" + 
					"PUC2 = NULL,\n" + 
					"PIN1 = NULL,\n" + 
					"PIN2 = NULL,\n" + 
					"KI = NULL,\n" + 
					"ACC_UICC = NULL,\n" + 
					"ADMIN1 = NULL,\n" + 
					"SF_EQUIPMENT_ID = NULL,\n" + 
					"IMSI_UICC = NULL,\n" + 
					"STATUS = 'NEW',\n" + 
					"ERROR_DESCRIPTION = NULL\n" + 
					"WHERE UNIQUE_SEQUENCE_NUMBER IN (SELECT UNIQUE_SEQUENCE_NUMBER\n" + 
					"FROM EDF.EDF_HEADER_STG_INT\n" + 
					"WHERE FILE_NAME IN (" + parameters + ")\n" + 
					"and unique_sequence_number IN (" + seqNo + "))\n" +
					"-- " + count + " rows\n"
			);
			
			conn.close();
			
		} catch (ClassNotFoundException e) {
			System.out.println("Driver error: " + e.getMessage());
			
		} catch (SQLException e) {
			System.out.println("SQL error: " + e.getMessage());
			
		}
		
		return queries;
	}
}
