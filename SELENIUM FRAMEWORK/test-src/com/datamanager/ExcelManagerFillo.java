package com.datamanager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.Utf8Appendable.NotUtf8Exception;
import org.testng.Assert;

import com.utilities.UtilityMethods;

import Exception.FilloException;
import Fillo.Connection;
import Fillo.Fillo;
import Fillo.Recordset;

/**
 * This class implements IExcelManager
 */
public class ExcelManagerFillo implements IExcelManager {
	Fillo fillo;
	Connection con;
	String filepath;
	Recordset recordset;
	private Logger log = Logger.getLogger("ExcelManager");
	
	/**
	 * Initiating the Class members using parameterized constructor. 
	 * @param filepath -  Pass Excel file location 
	 */
	
	public ExcelManagerFillo(String filepath) {
		this.filepath=filepath;
		fillo=new Fillo();
		try {
			con= fillo.getConnection(filepath);
		    }
		   catch (FilloException e) {
			   log.error("File " + filepath + "doesn't exist...Please check the filepath \n"+UtilityMethods.getStackTrace());
		    Assert.fail("File " + filepath + "doesn't exist...Please check the filepath \n"+e.getCause());
		}
	}
	
/**
 * Purpose - To get entire ColumnData from excel sheet
 * @param sheetName - Pass the name of  excel sheet
 * @param columnIndex- Pass the column Index  which we want to retrieve
 * @return - returns the specified columnData as List<String>.
 * 
 */
	
	@Override
	public List<String> getExcelColumnData(String sheetName, int columnIndex) {
		List<String> columnData=new ArrayList<String>();
		try {
			String query="select * from " + sheetName ;
			recordset=con.executeQuery(query);
			 if(columnIndex<0||columnIndex>=getColumnCount(sheetName) ){
				 log.error("Column index '"+columnIndex+"' is not valid...Please check column index \n"+UtilityMethods.getStackTrace());
				Assert.fail("Column index '"+columnIndex+"' is not valid...Please check column index");
			   }
					while(recordset.next()){
						columnData.add(recordset.getField(columnIndex).value());
					}
		       } catch (FilloException e) {
        	log.error("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n"+UtilityMethods.getStackTrace());
		    Assert.fail("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name" );
            }
	finally {
		if(recordset==null)	
               con.close();
	      else {
	    	  recordset.close(); 
	    	  con.close();
	         }
	 }
		  return columnData;   
	}
		
/**
 * Purpose - To get entire ColumnData from excel sheet
 * @param sheetName- Pass the name of  excel sheet
 * @param columnName- Pass the column name which we want to retrieve
 * @return - returns the specified columnData as List<String>.
 * 
 */
	@Override
	public List<String>  getExcelColumnData(String sheetName, String columnName) {
		List<String> columnData=new ArrayList<String>();
		try {
			String strQuery=" Select "+ columnName +" from "+ sheetName;
			recordset = con.executeQuery(strQuery);
			if((columnName==""||columnName==null)||!recordset.getFieldNames().contains(columnName)){
		    	log.error("Column name provided as '"+ columnName +"' does not exist in the sheet \n"+UtilityMethods.getStackTrace());
		    	Assert.fail("Column name provided as '"+ columnName +"' does not exist in the sheet \n");
		    }
			while(recordset.next()) {
				columnData.add(recordset.getField(columnName));
			}
		} catch (FilloException e) {
			log.error("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n" +UtilityMethods.getStackTrace());
	 		Assert.fail("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n"+e.getCause() );
		}
	finally {
		if(recordset==null)	
            con.close();
	      else {
	    	  recordset.close(); 
	    	  con.close();
	         }
		}
		return columnData;
	}
	/**
	 * Purpose - To get cell data from Excel sheet by passing row and column positions
	 * @param sheetName  - Pass name of the sheet
	 * @param columnPosition- position of column[index starts from 0]
	 * @param rowPosition- position of row[index starts from zero]
	 * @return - value of the specified cell as String
	 */

@Override
public String getExcelCellData(String sheetName, int columnIndex,
		int rowIndex) {
	
	String cellValue=null;
	try {
		String query="select * from " + sheetName ;
		recordset=con.executeQuery(query);
		int rowCount=getRowCount(sheetName);
	    if((columnIndex<0||columnIndex>=getColumnCount(sheetName)) ||(rowIndex<0||rowIndex>=rowCount) ){
	    	log.error("Index is not valid...Please check row and column Index \n"+UtilityMethods.getStackTrace());
			Assert.fail("Index is not valid...Please check row and column Index \n");
	    }
    	  for(int row=0;row<rowCount;row++) 
	    			 if(recordset.next()&& row==rowIndex) {
	    					cellValue=recordset.getField(columnIndex).value();
	    					break;
	    			 }
	     }catch (FilloException e) {
	    	 log.error("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n" +UtilityMethods.getStackTrace());
	 		Assert.fail("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n" + e.getCause());
	}
	finally {
		if(recordset==null)	
            con.close();
	      else {
	    	  recordset.close(); 
	    	  con.close();
	         }
	}
	return cellValue;
}
/**
 * Purpose - To get cell data from Excel sheet by passing column name and row position
 * @param sheetName- name of the sheet
 * @param columnName- name of the column you want to retrieve
 * @param rowPosition- position of row
 * @return - value of specified cell as String
 */

@Override
public String getExcelCellData(String sheetName, String columnName,
		int rowIndex) {
String cellValue="";
	try {
		String query="select * from " + sheetName ;
		recordset=con.executeQuery(query);
		int rowCount=getRowCount(sheetName);
		if((columnName==""||columnName==null)||!recordset.getFieldNames().contains(columnName)){
	    	log.error("Column name provided as '"+ columnName +"' does not exist in the sheet \n"+UtilityMethods.getStackTrace());
	    	Assert.fail("Column name provided as '"+ columnName +"' does not exist in the sheet \n");
	    }
	    if(rowIndex<0||rowIndex>=rowCount){
	    	log.error("Row index '"+rowIndex+"' is not valid...Please check row index \n"+UtilityMethods.getStackTrace());
	    	Assert.fail("Row index '"+rowIndex+"' is not valid...Please check row index");
	    }
	  for(int row=0;row<rowCount;row++)
		   if(recordset.next()&&row==rowIndex) {
				 cellValue= recordset.getField(columnName);
				 break;
	           }
	} catch (FilloException e) {
    	log.error("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n" +UtilityMethods.getStackTrace());
		Assert.fail("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n" + e.getMessage());
	}
	finally{
		if(recordset==null)	
            con.close();
	      else {
	    	  recordset.close(); 
	    	  con.close();
	         }
	}
	return cellValue;
}

/**
 * Purpose - To get sheetData
 * @param sheetName- pass sheet name
 * @return - returns sheet data as two dimensional array as two dimensional String array
 */

@Override
public String[][] getExcelSheetData(String sheetName) {
	String[][] sheetData=null;
	try {
	String query="select * from " + sheetName ;
	recordset=con.executeQuery(query);
	int rowCount=getRowCount(sheetName);
	int colCount=getColumnCount(sheetName);
    sheetData=new String[rowCount][colCount];
	for(int cols=0;cols<colCount;cols++){	
		for(int row=0;row<rowCount;row++)
			if(recordset.next()){
				if(row==0)
					recordset.moveFirst();
				sheetData[row][cols]=recordset.getField(cols).value();
			}
		recordset.moveFirst();
	}			
	  }
    catch (FilloException e) {
    	log.error("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n" +UtilityMethods.getStackTrace());
		Assert.fail("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n" + e.getMessage());
	}
	finally{
		if(recordset==null)	
            con.close();
	      else {
	    	  recordset.close(); 
	    	  con.close();
	         }
	}
	return sheetData;
}
/**
 * Purpose - To add new record to the existing excel file
 * @param sheetName - Pass Excel sheet name
 * @param arrayOfDataForRow - Pass values for new record in the form of an array
 */

@Override
public void addExcelRowData(String sheetName, String[] rowData) {
	try {
		recordset=con.executeQuery("select * from " + sheetName);
		if(rowData.length<=getColumnCount(sheetName)){
		String strQuery="INSERT INTO " + sheetName + "("+  getQueryStringOfColumnnames(recordset.getFieldNames())  +")"+ " VALUES("+  getQueryStringOfValues(rowData)    +")";
		if(con.executeUpdate(strQuery)==0)
			log.info("The Insertion is sucessfully Completed");
		else
		     log.info("Unable to insert row in"+sheetName);
		}else
		{
			log.error("Index outof range...Please check array size \n"+UtilityMethods.getStackTrace());
			Assert.fail("Index out of range...Please check array size \n");
		}
	} catch (FilloException e) {
		log.error("Unable to insert row in '"+sheetName+"' Please check the sheet name \n" +UtilityMethods.getStackTrace());
		Assert.fail("Unable to insert row in '"+sheetName+"' Please check the sheet name \n" + e.getMessage());
	}
	catch(NullPointerException e){
		log.error("String[] rowData is null or empty...Please provide proper values for an array \n"+UtilityMethods.getStackTrace());
		Assert.fail("String[] rowData is null or empty...Please provide proper values for an array \n"+e.getCause());
	}
	finally {
		if(recordset==null)	
            con.close();
	      else {
	    	  recordset.close(); 
	    	  con.close();
	         }
		}
}
/**
 * purpose - To form a query String for column names
 * @param colname- list of column names
 */
 private String getQueryStringOfColumnnames(List<String> colname) {
	 String colNames="";
     int size= colname.size();
		for(int i=0;i<size;i++)
			if(i<size-1)
				colNames=colNames+ colname.get(i)+",";
			else
				colNames=colNames+ colname.get(i);
	 return colNames;
 }

/**
 * Purpose - To form a query string for values
 * @param rowData-array of values we add to the excel sheet
 */

private String getQueryStringOfValues(String[] rowData) {
	String values="";
	int size=rowData.length;
	for(int i=0;i<size;i++)
		if(i<size-1)
	values=values+ "'" +rowData[i] +"'" +",";
		else 
	values=values+ "'" +rowData[i] +"'" ;
	return values;
}

/**
 * Purpose - To get row count
 * @param query- name of the sheet
 * @return- row count as integer
 */
@Override
public int getRowCount(String sheetName)
{
	int rowCount=0;
	try {
		String query="select * from "+ sheetName;
		recordset=con.executeQuery(query);
		rowCount=recordset.getCount();
	} catch (FilloException e) {
		log.error("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n"+ UtilityMethods.getStackTrace());
		Assert.fail("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n"+ e.getMessage());
	}
	return rowCount;
}
/**
 * Purpose - To get column count
 * @param query- name of the sheet
 * @return - column count as integer
 */
@Override
public int getColumnCount(String sheetName)
{
	int columnCount=0;
	try {
		String query="select * from "+ sheetName;
		recordset=con.executeQuery(query);
		columnCount=recordset.getFieldNames().size();
		
	} catch (FilloException e) {
		log.error("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n"+ UtilityMethods.getStackTrace());
		Assert.fail("Provided sheetName '"+sheetName+"' does not exist...Please check sheet name \n"+ e.getMessage());
	}
	return columnCount;
}
}

