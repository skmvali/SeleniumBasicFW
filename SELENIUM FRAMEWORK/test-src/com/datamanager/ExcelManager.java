package com.datamanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


//import com.utilities.UtilityMethods;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sun.mail.imap.Utility;
import com.testng.Assert;
import com.utilities.UtilityMethods;

public class ExcelManager implements IExcelManager{
	XSSFWorkbook workbook;
	XSSFSheet Sheet;
	XSSFRow row;
	XSSFCell cell;
	HSSFWorkbook hworkbook;
	HSSFSheet hSheet;
	HSSFRow hrow;
	HSSFCell hcell;

	FileInputStream fis;
	FileOutputStream  fos;
	int iIndex;
	ConfigManager app;
	private Logger log = Logger.getLogger("ExcelManager");
     String sFilePath;

	/**
	 * Purpose- Constructor to pass Excel file name
	 * 
	 * @param sFileName
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws NullPointerException
	 */
	public ExcelManager(String sFilePath) {
		try {
			this.sFilePath=sFilePath;
			File file = new File(sFilePath);
			System.setProperty("ExcelFilePath", sFilePath);
			if (file.exists()) {
				if (isXLSX()) {
					fis = new FileInputStream(sFilePath);
					workbook = new XSSFWorkbook(fis);
					fis.close();
					
				} else {
					fis = new FileInputStream(sFilePath);
					hworkbook = new HSSFWorkbook(fis);
					fis.close();
				}
		} 
			else {
				log.error("File -'"
						+ sFilePath
						+ "' does not exist in Data Folder, Please Re-check given file"+UtilityMethods.getStackTrace());
				Assert.fail("File -'"
						+ sFilePath
						+ "' does not exist in Data Folder, Please Re-check given file");
			}

		} catch (FileNotFoundException e) {
			log.error("File with Specified name - "+sFilePath+" does not exist \n" +UtilityMethods.getStackTrace());
			Assert.fail("File with Specified name - "+sFilePath+" does not exist \n" + 
		                   "Exception is - "+e.getCause());
		}catch(IOException e){
			log.error("Exception occurred while accessing the file - " + sFilePath +"\n"+UtilityMethods.getStackTrace());
			Assert.fail("Exception occurred while accessing the file - " + sFilePath +"\n" 
		                     +e.getCause());
		}catch(NullPointerException e){
			log.error("Please check the filepath provided - "+sFilePath+"\n"+UtilityMethods.getStackTrace());
			Assert.fail("Please check whether provided filepath is not null \n"+e.getCause());
		}
		
		
	}

	/**
	 * Purpose- To check if the sheet with given name exists or not
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @return - if sheet with specified name exists it returns true else it
	 *         returns false
	 */
	private boolean isSheetExist(String sheetName) {
	if(sheetName!=null)	
		if (isXLSX())
			iIndex = workbook.getSheetIndex(sheetName);
		else
			iIndex = hworkbook.getSheetIndex(sheetName);
	else{ 
		log.error("Sheet name "+sheetName+" is provided as null...Please check sheet name"+UtilityMethods.getStackTrace());
		Assert.fail("Sheet name "+sheetName+" is provided as null...Please check sheet name");
	}
	if (iIndex == -1)
	{
		log.error("sheet with name '"+sheetName+"' does not exist in the "+sFilePath+"\n"+UtilityMethods.getStackTrace());
		Assert.fail("sheet with name '"+sheetName+"' does not exist in the "+sFilePath);
		return false;
	} else
		return true;
	}

	/**
	 * Purpose- To get the row count of specified sheet
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @return- Returns value of row count as integer
	 */
	@Override
	public int getRowCount(String sheetName) {
		int number = 0;
			if (isSheetExist(sheetName)) {
				if (isXLSX()) {
					Sheet = workbook.getSheetAt(iIndex);
					number = Sheet.getLastRowNum() + 1;
				} else {
					hSheet = hworkbook.getSheetAt(iIndex);
					number = hSheet.getLastRowNum() + 1;
				}
			}
		return number;
	}

	/**
	 * Purpose- To get column count of specified sheet
	 * 
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @return- Returns value of column count
	 * @throws Exception
	 */
	@Override
	public int getColumnCount(String sheetName) {
		int count = 0;
			if (isSheetExist(sheetName)) {
				if (isXLSX()) {
					Sheet = workbook.getSheet(sheetName);
					row = Sheet.getRow(0);
					if (row == null)
						return -1;
					count = row.getLastCellNum();
				} else {
					hSheet = hworkbook.getSheet(sheetName);
					hrow = hSheet.getRow(0);
					if (hrow == null)
						return -1;
					count = hrow.getLastCellNum();
				}
			}
		return count;
	}

	/**
	 * Purpose- Returns the value from Excel based on Sheet name, column name,
	 * row Index
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @param colName
	 *            - Column Name should be provided
	 * @param rowNum
	 *            - Row value should be provided
	 * @return-returns value as a string
	 */
	private String getCellDataXLSX(String sheetName, String colName, int rowNum) {
			if (isSheetExist(sheetName)) {
				if (rowNum <= 0) {
					log.error("Row number should be greater than 0 \n"+UtilityMethods.getStackTrace());
					Assert.fail("Row number should be greater than 0");
					return "";
				}
				int col_Num = -1;
				Sheet = workbook.getSheetAt(iIndex);
				row = Sheet.getRow(0);
				for (int i = 0; i < row.getLastCellNum(); i++) {
					if (row.getCell(i).getStringCellValue().trim()
							.equals(colName.trim()))
						col_Num = i;
				}
				if (col_Num == -1) {
					log.error("Column with specified name '"+colName+"' is not being displayed \n"+UtilityMethods.getStackTrace());
					Assert.fail("Column with specified name '"+colName+"' is not being displayed");
					return "";
				}

				row = Sheet.getRow(rowNum - 1);
				if (row == null)
					return "";
				cell = row.getCell(col_Num);

				if (cell == null)
					return "";
				if (cell.getCellType() == Cell.CELL_TYPE_STRING)
					return cell.getStringCellValue();
				else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC
						|| cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					String cellText = String
							.valueOf(cell.getNumericCellValue());
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// format in form of D/M/YY
						double d = cell.getNumericCellValue();
						Calendar cal = Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						int Year = cal.get(Calendar.YEAR);
						int Day = cal.get(Calendar.DAY_OF_MONTH);
						int Month = cal.get(Calendar.MONTH) + 1;
						cellText = Day + "/" + Month + "/"
								+ (String.valueOf(Year)).substring(2);
					}
					return cellText;
				} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
					return "";
				else
					return String.valueOf(cell.getBooleanCellValue());
			}
			return "";
		
	}

	/**
	 * Purpose- Returns the value from Excel based on Sheet name, column number,
	 * row number
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @param colNum
	 *            - Column number should be provided
	 * @param rowNum
	 *            - Row number should be provided
	 * @return - returns value as a string
	 */
	private String getCellDataXLSX(String sheetName, int colNum, int rowNum) {
		String cellData = null;
			if (isSheetExist(sheetName)) {
				if (rowNum <= 0) {
					log.error("Row number should be greater than 0 \n"+UtilityMethods.getStackTrace());
					Assert.fail("Row number should be greater than 0");
				}
				if(colNum<0||colNum>=getColumnCount(sheetName)){
					log.error("Column index '"+colNum+"' out of range...Please check column Index \n"+UtilityMethods.getStackTrace());
					Assert.fail("Column index '"+colNum+"' out of range...Please check column Index");
				}
				Sheet = workbook.getSheetAt(iIndex);
				row = Sheet.getRow(rowNum - 1);
				if (row == null) {
					return cellData;
				}
				cell = row.getCell(colNum);
				if (cell == null) {
					return cellData;
				}
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					return cell.getStringCellValue();
				} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC
						|| cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					String cellText = String
							.valueOf(cell.getNumericCellValue());
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// format in form of D/M/YY
						double d = cell.getNumericCellValue();
						Calendar cal = Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						int Year = cal.get(Calendar.YEAR);
						int Day = cal.get(Calendar.DAY_OF_MONTH);
						int Month = cal.get(Calendar.MONTH) + 1;
						cellText = Day + "/" + Month + "/"
								+ (String.valueOf(Year)).substring(2);
					}
					return cellText;
				} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					return cellData;
				} else {
					cellData = String.valueOf(cell.getBooleanCellValue());
				}
			}
		
		return cellData;
	}

	/**
	 * Purpose- Returns the value from Excel based on Sheet name, column name,
	 * row value
	 * 
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @param colName
	 *            - Column Name should be provided
	 * @param rowNum
	 *            - Row value should be provided
	 * @return
	 */
	private String getCellDataXLS(String sheetName, String colName, int rowNum) {
	
			if (isSheetExist(sheetName)) {
				if (rowNum <= 0) {
					log.error("Row number should be greater than 0 \n"+UtilityMethods.getStackTrace());
					Assert.fail("Row number should be greater than 0");
					return "";
				}
				int col_Num = -1;
				hSheet = hworkbook.getSheetAt(iIndex);
				hrow = hSheet.getRow(0);
				for (int i = 0; i < hrow.getLastCellNum(); i++) {
					if (hrow.getCell(i).getStringCellValue().trim()
							.equals(colName.trim()))
						col_Num = i;
				}
				if (col_Num == -1) {
					log.error("Column with specified name '"+colName+"' is not being displayed"+UtilityMethods.getStackTrace());
					Assert.fail("Column with specified name '"+colName+"' is not being displayed");
					return "";
				}

				hrow = hSheet.getRow(rowNum - 1);
				if (hrow == null)
					return "";
				hcell = hrow.getCell(col_Num);

				if (hcell == null)
					return "";
				if (hcell.getCellType() == Cell.CELL_TYPE_STRING)
					return hcell.getStringCellValue();
				else if (hcell.getCellType() == Cell.CELL_TYPE_NUMERIC
						|| hcell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					String cellText = String.valueOf(hcell
							.getNumericCellValue());
					if (HSSFDateUtil.isCellDateFormatted(hcell)) {
						// format in form of D/M/YY
						double d = hcell.getNumericCellValue();
						Calendar cal = Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						int Year = cal.get(Calendar.YEAR);
						int Day = cal.get(Calendar.DAY_OF_MONTH);
						int Month = cal.get(Calendar.MONTH) + 1;
						cellText = Day + "/" + Month + "/"
								+ (String.valueOf(Year)).substring(2);
					}
					return cellText;
				} else if (hcell.getCellType() == Cell.CELL_TYPE_BLANK)
					return "";
				else
					return String.valueOf(hcell.getBooleanCellValue());
			}
			return "";
	
	}

	/**
	 * Purpose- Returns the value from Excel based on Sheet name, column number,
	 * row number
	 * 
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @param colNum
	 *            - Column number should be provided
	 * @param rowNum
	 *            - Row number should be provided
	 * @return - returns value as a string
	 */
	private String getCellDataXLS(String sheetName, int colNum, int rowNum) {
		String cellData = null;
			if (isSheetExist(sheetName)) {
				if (rowNum <= 0) {
					log.error("Row number should be greater than 0 \n"+UtilityMethods.getStackTrace());
					Assert.fail("Row number should be greater than 0");
				}
				if(colNum<0||colNum>=getColumnCount(sheetName)){
					log.error("Column index '"+colNum+"' out of range...Please check column Index \n"+UtilityMethods.getStackTrace());
					Assert.fail("Column index '"+colNum+"' out of range...Please check column Index");
				}
				hSheet = hworkbook.getSheetAt(iIndex);
				hrow = hSheet.getRow(rowNum - 1);

				if (hrow == null) {
					return cellData;
				}
				hcell = hrow.getCell(colNum);
				if (hcell == null) {
					return cellData;
				}
				if (hcell.getCellType() == Cell.CELL_TYPE_STRING) {
					return hcell.getStringCellValue();
				} else if (hcell.getCellType() == Cell.CELL_TYPE_NUMERIC
						|| hcell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					String cellText = String.valueOf(hcell
							.getNumericCellValue());
					if (HSSFDateUtil.isCellDateFormatted(hcell)) {
						// format in form of D/M/YY
						double d = hcell.getNumericCellValue();
						Calendar cal = Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						int Year = cal.get(Calendar.YEAR);
						int Day = cal.get(Calendar.DAY_OF_MONTH);
						int Month = cal.get(Calendar.MONTH) + 1;
						cellText = Day + "/" + Month + "/"
								+ (String.valueOf(Year)).substring(2);
					}
					return cellText;
				} else if (hcell.getCellType() == Cell.CELL_TYPE_BLANK) {
					return cellData;
				} else {
					cellData = String.valueOf(hcell.getBooleanCellValue());
				}
			}
		return cellData;
	}

	/**
	 * Purpose - To get the cell data from Excel spreadsheet based on column number and row number(it may be excelfile with extension .xlsx or.xsl)
	 * @param sheetName - Name of the work sheet in the Excel
	 * @param colNum - column number in the sheet
	 * @param rowNum - Row number in the sheet
	 * @return - String (value of the cell based on excel format) 
	 */
	@Override
	public String getExcelCellData(String sheetName, int colNum, int rowNum) {
		if (isXLSX()) {
			return getCellDataXLSX(sheetName, colNum, rowNum);
		} else {
			return getCellDataXLS(sheetName, colNum, rowNum);
		}
	}

	/**
	 * Purpose - To get the cell data from Excel spreadsheet based on column name and row number(it may be excelfile with extension .xlsx or.xsl)
	 * @param sheetName - Name of the work sheet in the Excel
	 * @param colName - column name in the Sheet
	 * @param rowNum - Row number in the sheet
	 * @return - String (value of the cell based on excel format) 
	 */
	@Override
	public String getExcelCellData(String sheetName, String colName, int rowNum) {
		if (isXLSX()) {
			return getCellDataXLSX(sheetName, colName, rowNum);
		} else {
			return getCellDataXLS(sheetName, colName, rowNum);
		}
	}

	/**
	 * Purpose - To find given spreadsheet extension is xlsx
	 * @return - boolean(if the the given spreadsheet extension is .xlsx returns true else return false) 
	 */
	private static boolean isXLSX() {
		return System.getProperty("ExcelFilePath").endsWith(".xlsx");
	}

	/**
	 * Purpose - This method will return all the data present in the sheet(rows and
	 * columns) and stored in 2D String array
	 */
	@Override
	public String[][] getExcelSheetData(String sheetName){
		String[][] sheetData = null;
			if (isSheetExist(sheetName)) {
				int rowCount = getRowCount(sheetName);
				int colCount = getColumnCount(sheetName);
				sheetData = new String[rowCount - 1][colCount];
				for (int i = 2; i <= rowCount; i++)
					for (int j = 0; j < colCount; j++)
						sheetData[i - 2][j] = getExcelCellData(sheetName, j, i);
			}
		return sheetData;
	}
/**
 * Purpose - Returns the entire ColumnData based on the sheetName,columnIndex
 * @param sheetName - Name of the ExcelSheet
 * @param columnIndex - pass the columnIndex we want to retrieve
 * @return - ColumnData as List<String>
 */
	@Override
	public List<String> getExcelColumnData(String sheetName, int columnIndex) {
	     List<String> cellValue=new ArrayList<String>();
			if(isSheetExist(sheetName)) {
				if(columnIndex<0||columnIndex>=getColumnCount(sheetName)){
					log.error("Column Index '"+columnIndex+"' is invalid...Please check column index \n"+UtilityMethods.getStackTrace());
					Assert.fail("Column Index '"+columnIndex+"' is invalid...Please check column index");
				}
				Sheet = workbook.getSheetAt(iIndex);
				for(int i=2;i<=getRowCount(sheetName);i++)
					cellValue.add(getExcelCellData(sheetName,columnIndex,i));
			}
		return cellValue;
	}

	/**
	 * Purpose - Returns the entire ColumnData based on the sheetName,columnName
	 * @param sheetName - Name of the ExcelSheet
	 * @param columnIndex - pass the column Name we want to retrieve
	 * @return - ColumnData as List<String>
	 */
	@Override
	public List<String> getExcelColumnData(String sheetName, String columnName) {
		 List<String> cellValue=new ArrayList<String>();
				if(isSheetExist(sheetName)) {
					Sheet = workbook.getSheetAt(iIndex);
					for(int i=2;i<=getRowCount(sheetName);i++)
						cellValue.add(getExcelCellData(sheetName,columnName,i));
				}
			return cellValue;
	}
	/**
	 * Purpose - to append a row to the Existing Excel File
	 * @param sheetName - Name of the sheet
	 * @param rowData - pass  the values for a row as an array
	 * @param file - pass the Excel File
	 * @throws  
	 */
	@Override
	public void addExcelRowData(String sheetName, String[] rowData)  {
		
		try {
			 fos=new FileOutputStream(sFilePath);
			 int rowCount=getRowCount(sheetName);
			 int columnCount=getColumnCount(sheetName);
				if(isSheetExist(sheetName)) 
					if(isXLSX()) {
					Sheet = workbook.getSheetAt(iIndex);
					row=Sheet.createRow(rowCount);
					if(rowData.length<=columnCount)
					for(int i=0;i<columnCount;i++){
							cell=row.createCell(i);
							 cell.setCellValue(rowData[i]);
					}
					else{
						log.error("Array Index is out of Range...Please check array size \n"+UtilityMethods.getStackTrace());
						Assert.fail("Array Index is out of Range...Please check array size");
					}
	              workbook.write(fos);
	              fos.close();
	              log.info("Data successfully inserted in to the row");
					} else {
						hSheet = hworkbook.getSheetAt(iIndex);
						hrow=hSheet.createRow(rowCount);
						if(rowData.length==columnCount)
							for(int i=0;i<columnCount;i++){	
								cell=row.createCell(i);
								 cell.setCellValue("");
							}
							else{
								log.error("Array Index is out of Range...Please check array size \n"+UtilityMethods.getStackTrace());
								Assert.fail("Array Index is out of Range...Please check array size");
							}
					 hworkbook.write(fos);
		              fos.close();
		              log.info("Data successfully inserted in to the row");
					}
		 }catch(FileNotFoundException e) {
			 log.error("File with Specified name - "+sFilePath+" does not exist \n" +UtilityMethods.getStackTrace());
			 Assert.fail("File with Specified name - "+sFilePath+" does not exist \n" + 
	                   "Exception is - "+e.getCause());
			 
		 } catch (IOException e) {
			 log.error("Exception occurred while accessing the file - " + sFilePath +"\n"+UtilityMethods.getStackTrace());
			 Assert.fail("Exception occurred while accessing the file - " + sFilePath +"\n" 
                     +e.getCause());
			
		}
			

	}	
	
}
