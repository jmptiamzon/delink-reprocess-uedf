package com.sprint.delink.controller;

import com.sprint.delink.model.Model;

public class Controller {
	private Model model;
	
	public Controller() {
		model = new Model();
	}
	
	public void runApp(String filenames) {
		String []filenamesArr = filenames.trim().split("\\n");
		String param = "";
		
		for (int ctr = 0; ctr < filenamesArr.length; ctr++) {
			param += "'" + filenamesArr[ctr] + "',";
		}
		
		param = param.substring(0, param.length() - 1);
		
		System.out.println(model.getEdfErrorCount(param));
		
	}
}	
