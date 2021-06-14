package com.sprint.delink.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.sprint.delink.model.Model;

public class Controller {
	private Model model;
	
	public Controller() {
		model = new Model();
	}
	
	public void runApp(String filenames) {
		String []filenamesArr = filenames.trim().split("\\n");
		List<String> queries = new ArrayList<>();
		String param = "";
		
		for (int ctr = 0; ctr < filenamesArr.length; ctr++) {
			param += "'" + filenamesArr[ctr] + "',";
		}
		
		param = param.substring(0, param.length() - 1);
		
		if (model.getEdfErrorCount(param) == 0) {
			JOptionPane.showMessageDialog(null, "Filename/s not found.");
		
		} else {
			queries = model.getQueryToWrite(param);
			
			File file = new File(System.getProperty("user.home") + "\\Documents\\delink_reprocess.txt");
			
			try {
				FileWriter writer = new FileWriter(file);
				
				for (int ctr = queries.size() - 1; ctr >= 0; ctr--) {
					writer.write(queries.get(ctr));
					writer.write("\n");
				}
				
				writer.close();
				
			} catch (IOException e) {
				System.out.println("IO error: " + e.getMessage());
				
			}
			
		}
		
	}
}	
