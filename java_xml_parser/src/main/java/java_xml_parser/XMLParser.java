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
    private static final Logger methodLogger = LoggerFactory.getLogger("MethodLogger");

    private static Map<String, Object> elementMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            String methodName = "main";
            methodLogger.info("Entering method: {}", methodName);

            // Create a DocumentBuilder to parse XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Load the XML file from the specified path
            File xmlFile = new File("C://Users//suyog.kedar//Desktop//All Files//Validator//DirectCreditMsgSpec.xml");
            Document document = builder.parse(xmlFile);

            // Normalize the document to handle white spaces and formatting
            document.getDocumentElement().normalize();
            Element rootElement = document.getDocumentElement();

            // Process all child elements of the root element
            processChildElements(rootElement);

            // Print the resulting elementMap
//            for (Map.Entry<String, Object> entry : elementMap.entrySet()) {
//                logger.info("{} : {}", entry.getKey(), entry.getValue());
//            }

        } catch (Exception e) {
            logger.error("XML parsing error: " + e.getMessage(), e);
        }
    }

    // Recursively process XML elements
    private static void processChildElements(Element parentElement) {
        String methodName = "processChildElements";
        methodLogger.info("Entering method: {}", methodName);

        NodeList childNodes = parentElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                processElement((Element) node, "");
            }
            for (Map.Entry<String, Object> entry : elementMap.entrySet()) {
                logger.info("{} : {}", entry.getKey(), entry.getValue());
            }
            
            
        }

        methodLogger.info("Exiting method: {}", methodName);
    }

    // Recursively process XML elements
    private static void processElement(Element element, String parentPath) {
        String methodName = "processElement";
        methodLogger.info("Entering method: {}", methodName);

        NodeList childNodes = element.getChildNodes();
        String currentPath = parentPath.isEmpty() ? element.getNodeName() : parentPath + "~" + element.getNodeName();

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

        methodLogger.info("Exiting method: {}", methodName);
    }
}
