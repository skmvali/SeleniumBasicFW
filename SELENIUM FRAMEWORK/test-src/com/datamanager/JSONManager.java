package com.datamanager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//import com.testng.CustomAssert;
import com.testng.Assert;
import com.utilities.UtilityMethods;



public class JSONManager
{
	File jFile;
	JSONParser parser = new JSONParser();
	private static Logger log = Logger.getLogger("Verify");
	
	
	public void readJsonContent(String pnode, String key, String filePath){
		try {
			jFile = new File(filePath);
			JSONObject root_Object = (JSONObject) parser.parse(new FileReader(jFile));
			
			JSONObject parent = (JSONObject) root_Object.get(pnode);
			System.out.println(parent.get(key));
		}
		catch (IOException | ParseException e) {
			log.error("Exception while reading key - "+key+" from JSON file - "+ filePath+e.getMessage()+UtilityMethods.getStackTrace());
			/*Custom*/Assert.fail("Exception while reading key - "+key+" from JSON file - "+ filePath+e.getMessage()+UtilityMethods.getStackTrace());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void writeJsonContent(String pnode, String key, String value, String filePath){
		try 
		{
			jFile = new File(filePath);
			JSONObject root_Object = (JSONObject) parser.parse(new FileReader(jFile));
		
			JSONObject parent = (JSONObject) root_Object.get(pnode);
			parent.put(key, value);
			updateFile(root_Object, filePath);
		}
		catch (IOException | ParseException e) {
			log.error("Exception while writing key - "+key+" to JSON file - "+filePath+e.getMessage()+UtilityMethods.getStackTrace());
			/*Custom*/Assert.fail("Exception while writing key - "+key+" to JSON file - "+filePath+e.getMessage()+UtilityMethods.getStackTrace());
		}
	}
	
	public void removeJsonContent(String pnode, String key, String filePath){
		try {
			jFile = new File(filePath);
			JSONObject root_Object = (JSONObject) parser.parse(new FileReader(jFile));
		
			JSONObject parent = (JSONObject) root_Object.get(pnode);
			parent.remove(key);
			updateFile(root_Object, filePath);
		}
		catch (IOException | ParseException e) {
			log.error("Exception while removing key - "+key+" to JSON file - "+filePath+e.getMessage()+UtilityMethods.getStackTrace());
			/*Custom*/Assert.fail("Exception while removing key - "+key+" to JSON file - "+filePath+e.getMessage()+UtilityMethods.getStackTrace());
		}
	}
	
	public void updateFile(JSONObject root_Object, String filePath){
		FileWriter file;
		try {
			file = new FileWriter(filePath);
			file.write(root_Object.toJSONString());
			file.flush();
			file.close();
		}
		catch (IOException e) {
			log.error("Exception accessing JSON file - "+filePath+e.getMessage()+UtilityMethods.getStackTrace());
			/*Custom*/Assert.fail("Exception accessing JSON file - "+filePath+e.getMessage()+UtilityMethods.getStackTrace());
		}
	}
}
