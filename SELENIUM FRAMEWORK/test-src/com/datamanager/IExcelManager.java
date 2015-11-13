package com.datamanager;

import java.util.List;
/**
 * This interface has to be implemented by ExcelManager and ExcelManagerFillo
 */

public interface IExcelManager {
	/**
	 * Purpose - This method returns the entire column data based on the columIndex
	 * @param sheetName - Name of the excel sheet
	 * @param columnIndex - Pass the column Index we want to retrieve
	 * @return - ColumnData as List<String>
	 */
    public List<String> getExcelColumnData(String sheetName, int columnIndex);
    
    /**
	 * Purpose - This method returns the entire column data based on the column name
	 * @param sheetName - Name of the excel sheet
	 * @param columnName - Pass the columnName we want to retrieve
	 * @return - ColumnData as List<String>
	 */
    
	public List<String> getExcelColumnData(String sheetName, String columnName);
	/**
	 * Purpose - To get cell data from Excel sheet by passing row and column positions
	 * @param sheetName  - Name of the excel sheet
	 * @param columnIndex- Index of column
	 * @param rowIndex- Index of row
	 * @return - value of the specified cell as String
	 */

	
	public String getExcelCellData(String sheetName, int columnIndex, int rowIndex);
	/**
	 * Purpose - To get cell data from Excel sheet by passing Column name and row index
	 * @param sheetName  - Name of the Excel sheet
	 * @param columnName- Name of the column
	 * @param rowIndex- Index of row
	 * @return - value of the specified cell as String
	 */

	public String getExcelCellData(String sheetName, String columnName, int rowIndex);
	/**
	 * Purpose - To get sheetData
	 * @param sheetName- Name of the Excel sheet
	 * @return - returns sheet data as two dimensional String array 
	 */
	public String[][] getExcelSheetData(String sheetName);
	/**
	 * Purpose - To get row count 
	 * @param sheetName - Name of the sheet
	 * @return- returns row count as integer
	 */
    public int getRowCount(String sheetName);
    /**
     * Purpose - To get Column count
     * @param sheetName - Name of the sheet
     * @return -returns column count as integer
     */
    public int getColumnCount(String SheetName);
    /**
     * Purpose - To add a row to the existing Excel sheet
     * @param sheetName - Name off the Excel sheet
     * @param rowData - Pass the data we want to insert in to the row
     */
	public void addExcelRowData(String sheetName, String[] rowData);
}
