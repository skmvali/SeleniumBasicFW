package com.datamanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.IllegalNameException;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.testng.Assert;
import com.utilities.UtilityMethods;

public class XMLManager {
	SAXBuilder builder = new SAXBuilder();
	File xFile;
	Document xmldoc;
	Element rootNode;
	private Logger log = Logger.getLogger("XMLManager");

	/**
	 * Purpose- Constructor to pass Xml file path
	 * 
	 * @param sFileName
	 */
	public XMLManager(String xmlFilePath) {
		this.xFile = new File(xmlFilePath);
	}

	/**
	 * Purpose - To get value of the child of first node in the given Xpath
	 * 
	 * @param xmlFilePath
	 *            - Xml file location
	 * @param path
	 *            - Xpath Expression
	 * @return String - Value of the first child
	 */

	public String readXmlContent(String path) {
		String value = null;
		try {
			xmldoc = builder.build(xFile);
			rootNode = xmldoc.getRootElement();

			String[] array = path.split("/");
			int last_element = array.length;

			Element s = rootNode;
			for (int i = 1; i < last_element; i++)
				s = s.getChild(array[i]);
			value = s.getText();
		} catch (JDOMException | IOException e) {
			log.error("Exception while reading XML content from file - "
					+ xFile + e.getMessage()
					+ UtilityMethods.getStackTrace());
			Assert.fail("Exception while reading XML content from file - "
					+ xFile + e.getMessage());
		}catch (NullPointerException e) {
			log.error("Unable to get Text using  "+ path +" from file"
					+ xFile + e.getMessage());
			Assert.fail("Unable to get Text using  "+ path +" from file"
					+ xFile + e.getMessage());
		}
		return value;
	}

	/**
	 * Purpose - To add Content to the Xml File
	 * 
	 * @param keypath
	 *            - Xpath Expression
	 * @param value
	 *            - Value for the node
	 * @param xmlFilePath
	 *            - Xml file location
	 */
	public void addXmlContent(String keyPath, String value) {
		try {
			xmldoc = builder.build(xFile);
			rootNode = xmldoc.getRootElement();

			String[] array = keyPath.split("/");
			int last_element = array.length;

			Element new_node = new Element(array[(last_element - 1)])
					.setText(value);

			Element s = rootNode;
			for (int i = 1; i < last_element - 1; i++) {
				s = s.getChild(array[i]);
			}
			s.addContent(new_node);
			updateFile(xmldoc, xFile);
		} catch (JDOMException | IOException e) {
			log.error("Exception while writing XML content to file - "
					+ xFile + e.getMessage()
					+ UtilityMethods.getStackTrace());
			Assert.fail("Exception while writing XML content to file - "
					+ xFile + e.getMessage()
					+ UtilityMethods.getStackTrace());
		}catch (NullPointerException e) {
			log.error("Unable to get Text using  "+ keyPath +" from file"
					+ xFile + e.getMessage()
					+ UtilityMethods.getStackTrace());
			Assert.fail("Unable to get Text using  "+ keyPath +" from file"
					+ xFile + e.getMessage());
		}catch (IllegalNameException e) {
			log.error(keyPath +" will accept only alphanumeric and blackslash to write XML content from file"
					+ xFile + e.getMessage());
			Assert.fail(keyPath +" will accept alphanumeric and blackslash to write XML content from file"
					+ xFile + e.getMessage());
		}
	}

	/**
	 * Purpose - To remove Content from Xml File
	 * 
	 * @param keypath
	 *            - Xpath Expression
	 * @param xmlFilePath
	 *            - Xml file location
	 */
	public void removeXmlContent(String keyPath) {
		try {
			// xFile = new File(xmlFilePath);
			xmldoc = builder.build(xFile);
			rootNode = xmldoc.getRootElement();
			String[] array = keyPath.split("/");
			int last_element = array.length;
			Element s = rootNode;
			for (int i = 1; i < last_element - 1; i++) {
				s = s.getChild(array[i]);
			}
			s.removeChild(array[(last_element - 1)]);
			updateFile(xmldoc, xFile);
		} catch (JDOMException | IOException e) {
			log.error("Exception while removing XML content from file - "
					+ xFile + e.getMessage()
					+ UtilityMethods.getStackTrace());
			Assert.fail("Exception while removing XML content from file - "
					+ xFile + e.getMessage());
		}catch (NullPointerException e) {
			log.error("Unable to remove Text using  "+ keyPath +" from file"
					+ xFile + e.getMessage()
					+ UtilityMethods.getStackTrace());
			Assert.fail("Unable to remove Text using  "+ keyPath +" from file"
					+ xFile + e.getMessage());
		}
	}

	/**
	 * Purpose - To Update existing node value
	 * 
	 * @param keypath
	 *            - Xpath Expression
	 * @param newvalue
	 *            - new Value for the node
	 * @param xmlFilePath
	 *            - Xml file location
	 */
	public void updateXmlContent(String keyPath, String newvalue) {
		try {
			xmldoc = builder.build(xFile);
			rootNode = xmldoc.getRootElement();

			String[] array = keyPath.split("/");
			int last_element = array.length;

			Element s = rootNode;
			for (int i = 1; i < last_element; i++) {
				s = s.getChild(array[i]);
			}
			s.setText(newvalue);
			updateFile(xmldoc, xFile);
		} catch (JDOMException | IOException e) {
			log.error("XML  file - " + xFile
					+ " not found or no permissions to update the file"
					+ e.getMessage() + UtilityMethods.getStackTrace());
			Assert.fail("XML  file - " + xFile
					+ " not found or no permissions to update the file"
					+ e.getMessage());
		}catch (NullPointerException e) {
			log.error("Unable to update Text using  "+ keyPath +" from file"
					+ e.getMessage()
					+ UtilityMethods.getStackTrace());
			Assert.fail("Unable to update Text using  "+ keyPath +" from file"
					+ e.getMessage());
		}
	}

	/**
	 * Purpose - This method will update the xml file
	 * 
	 * @param xmlDoc
	 * @param xFile - Xml file path
	 */
	public void updateFile(Document xmlDoc, File xFile) {
		try {
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(xmlDoc, new FileWriter(xFile));
		} catch (IOException e) {
			log.error("XML  file - " + xFile
					+ " not found or no permissions to update the file"
					+ e.getMessage() + UtilityMethods.getStackTrace());
			Assert.fail("XML  file - " + xFile
					+ " not found or no permissions to update the file"
					+ e.getMessage());
		}
	}

	/**
	 * Purpose - This method will return elements of the given xpath
	 * 
	 * @param xpath - Xpath Expression
	 * @return - children for the matched Xpath
	 */
	public List<Element> getNodes(String xpath) {
		
		List<Element> nodes=null;
		try {
			xmldoc = builder.build(xFile);
			Element s = xmldoc.getRootElement();
			Element child = null;

			String[] array = xpath.split("/");
			int length = array.length;
			for (int i = 1; i < length - 1; i++) {
				child = s.getChild(array[i]);
			}

			nodes=child.getChildren(array[length - 1]);
		} catch (JDOMException | IOException e) {
			log.error("XML  file - " + xFile
					+ " not found or no permissions to read the file"
					+ e.getMessage() + UtilityMethods.getStackTrace());
			Assert.fail("XML  file - " + xFile
					+ " not found or no permissions to read the file"
					+ e.getMessage() + UtilityMethods.getStackTrace());
		}catch (NullPointerException e) {
			log.error("XML  file - " + xFile
					+ " not found or no permissions to read the file"
					+ e.getMessage());
			Assert.fail("XML  file - " + xFile
					+ " not found or no permissions to read the file"
					+ e.getMessage());
		}
		return nodes;
	}

	/**
	 * Purpose - This method will read values of children for the given element and returns as a list
	 * 
	 * @param Element - element that has children
	 * @return - List of Children values 
	 */
	public List<String> getChildrenText(Element element) {
		List<String> children = new ArrayList<String>();
		try {
			for (Element child : element.getChildren()) {
				children.add(child.getText());
			}
			
		} catch (NullPointerException e) {
			log.error("Exception Occur while reading children values for the element :" + element
					+ UtilityMethods.getStackTrace());
			Assert.fail("Exception Occur while reading children values for the element :"+ element);
		}
		return children;
	}

	/**
	 * Purpose - This method will identify all the matching nodes for the given xpath expression and read values of children for the matched nodes 
	 * 
	 * @param xpath - Xpath expression for of the Children node
	 * @return Array with values of children's of the node for the matched Xpath
	 */

	public String[][] getChildrenData(String xpath) {
		String[][] array = null;
		try {
			List<Element> nodes = getNodes(xpath);
			int nodeCount = nodes.size();
			int childrenCount = nodes.get(0).getChildren().size();
			array = new String[nodeCount][childrenCount];
			int i = 0;
			for (Element ele : nodes) {

				List<String> test = getChildrenText(ele);
				for (int j = 0; j < test.size(); j++) {
					array[i][j] = test.get(j);
				}
				i++;
			}
		} catch (Exception e) {
			log.error("Unable to get Children Text using  " + xpath + " "
					+ UtilityMethods.getStackTrace());
			Assert.fail("Unable to get Children Text using  " + xpath);
		}
		return array;
	}
}