package java_xml_parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XMLParser {
	private static final Logger logger = LoggerFactory.getLogger(XMLParser.class);

	private static Map<String, Object> elementMap = new HashMap<>();
	private static Map<String, Object> indexMap = new HashMap<>();

	public static void main(String[] args) {
		try {


			// Create a DocumentBuilder to parse XML
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Load the XML file from the specified path
				File xmlFile = new File("C://Users//suyog.kedar//Desktop//All Files//Validator//DirectCreditMsgSpec.xml");
			//	File xmlFile = new File("C://Users//suyog.kedar//Desktop//All Files//DirectCreditMsgSpec_1.xml");

			Document document = builder.parse(xmlFile);

			// Normalize the document to handle white spaces and formatting
			document.getDocumentElement().normalize();
			Element rootElement = document.getDocumentElement();

			// Process all child elements of the root element
			processChildElements(rootElement);

			System.out.println("Done");


		} catch (Exception e) {
			logger.error("XML parsing error: " + e.getMessage(), e);
		}
	}

	// Recursively process XML elements
	private static void processChildElements(Element parentElement) {

		NodeList childNodes = parentElement.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				processElement((Element) node, "");
			}
			if (i % 2 == 1) {
				for (Map.Entry<String, Object> entry : elementMap.entrySet()) {
					logger.info("{} : {}", entry.getKey(), entry.getValue());
				}
			}

		}

	}

	// Recursively process XML elements
	private static void processElement(Element element, String parentPath) {

		NodeList childNodes = element.getChildNodes();
		String currentPath = parentPath.isEmpty() ? element.getNodeName() : parentPath + "~" + element.getNodeName();
		boolean flag = true;
		int a = 1;
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				processElement((Element) node, currentPath);
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				String textContent = node.getTextContent().trim();
				if (!textContent.isEmpty()) {
					String fullPath = currentPath;
					int indexOfTilde = fullPath.indexOf("~");

					if (indexOfTilde != -1) {
						fullPath = fullPath.substring(indexOfTilde + 1);
					}
					
					elementMap.put(fullPath, textContent);

				}
			}

		}

	}

}