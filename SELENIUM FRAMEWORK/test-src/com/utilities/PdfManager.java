package com.utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.util.PDFTextStripper;

import com.testng.Assert;

/**
 * Purpose - Read the text from the PDF document, verifies whether given phrase
 * exists in the pdf file or not, and also write the content of the PDF file &
 * save it to local file system
 */
public class PdfManager {
	PDFParser parser;
	private Logger log = Logger.getLogger("ReadPdfText");

	public PdfManager() {}

	/**
	 * Purpose- To retrieve text from Current URL which displays PDF file
	 * 
	 * @return - entire text of the PDF file. If there is any problem in
	 *         accessing URL/File, then it returns null
	 */
	public String getPdfTextFromURL(URL Url) {
		try {
			// create buffer reader object
			BufferedInputStream fileToParse = new BufferedInputStream(Url.openStream());
			parser = new PDFParser(fileToParse);
			return getTextFromPDF();
		}catch (FileNotFoundException e) {
			log.error("Current URL : "+Url+" does not contain PDF document \n" + e.getMessage());
			Assert.fail("Current URL : "+Url+" does not contain PDF document" + e.getMessage());
			return null;
		} catch (MalformedURLException e) {
			log.error("URL is invalid "+Url + e.getMessage());
			Assert.fail("Exception occurred while retreiving text from PDF file\n");
			return null;
		} catch (IOException e) {
			log.error("Exception occurred while accessing current URL\n" + e.getMessage());
			Assert.fail("Exception occurred while accessing current URL\n");
			return null;
		} catch (Exception e) {
			log.error("Exception occurred while retreiving text from PDF file\n" + e.getMessage());
			Assert.fail("Exception occurred while retreiving text from PDF file\n");
			return null;
		}
	}

	/**
	 * Purpose- To retrieve text from PDF file
	 * 
	 * @param - PDF file path location
	 * @return - entire text of the PDF file. If there is any problem in
	 *         accessing File, then it returns null
	 */
	public String getPdfTextFromFile(String pdfFilePath) {
		try {
			File file = new File(pdfFilePath);

			if (file.exists()) {
				parser = new PDFParser(new FileInputStream(file));
				return getTextFromPDF();
			} else {
				log.error("File -'" + pdfFilePath + "' does not exist in Data Folder, Please Re-check given file");
				Assert.fail("File -'" + pdfFilePath + "' does not exist in Data Folder, Please Re-check given file");
				return null;
			}
		} catch (FileNotFoundException e) {
				log.error("Given filepath - " + pdfFilePath + "does not exist\n" + e.getMessage());
				Assert.fail("Given filepath - " + pdfFilePath + "does not exist" + e.getMessage());
				return null;
		} catch (NullPointerException e) {
			log.error("Given filepath - " + pdfFilePath + "may not exist\n" + e.getMessage());
			Assert.fail("Given filepath - " + pdfFilePath + "may not exist" + e.getMessage());
			return null;
		} catch (IOException e) {
			log.error("Unable to open PDF Parser." + e.getMessage());
			Assert.fail("Unable to open PDF Parser." + e.getMessage());
			return null;
		} catch (Exception e) {
			log.error("Exception occurred while accessing file - " + pdfFilePath + "\n" + e.getMessage());
			Assert.fail("Exception occurred while accessing file - " + pdfFilePath + e.getMessage());
			return null;
		}
	}

	/**
	 * Purpose- To parse text from PDF file
	 * 
	 * @return - If PDF file contains text, then it will return entire text of
	 *         the PDF file, else it returns null
	 */
	private String getTextFromPDF() {
		try {
			parser.parse();

			// save pdf text into string variable
			String parsedText = new PDFTextStripper().getText(parser.getPDDocument());
			log.info(parsedText);

			// close PDFParser object
			parser.getPDDocument().close();

			return parsedText;
		} catch (Exception e) {
			log.error("An exception occured in parsing the PDF Document.\n" + e.getMessage());
			Assert.fail("An exception occured in parsing the PDF Document." + e.getMessage());
			return null;
		}
	}

	/* *//**
	 * Purpose- To check if the given text exists in the PDF file or not
	 *
	 * @param - pdfText - entire text of the PDF file
	 * @param - verifyText - input text to verify
	 */
	public boolean verifyText(String actualText, String inputText) {
		boolean verifyResults = actualText.contains(inputText);
		if (verifyResults) {
			log.info("PDF document contains given text");
			return true;
		}
		else {
			log.info("PDF document doesn't contains given text");
			return false;
			//Assert.fail("PDF document doesn't contains given text");
		}
	}
	
	/**
	 * Purpose- To write the content of PDF file to the file and save it to the
	 * given local file path
	 * 
	 * @param - pdfText - entire text of the PDF file
	 * @param - fileName - file name along with file extension & path (Ex: C:\\txtdoc1.doc, C:\\textfile1.txt)
	 */
	public void writeTexttoFile(String pdfText, String fileName) {
		try {
			PrintWriter pw = new PrintWriter(fileName);
			pw.print(pdfText);
			pw.close();
		} 
		catch (FileNotFoundException e) {
			log.error("Given File/Location does not exist."+fileName+"\n" + e.getMessage());
			Assert.fail("Given File/Location does not exist."+fileName + e.getMessage());
		}
		catch (Exception e) {
			log.error("An exception occured while writing the pdf text to file. \n" + e.getMessage());
			Assert.fail("An exception occured while writing the pdf text to file." + e.getMessage());
		}
	}
}