package utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class XMLConfig
{
	private static final String HTTP_APACHE_ORG_XML_FEATURES_NONVALIDATING_LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
	private static final String HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
	private static final String HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
	private static final String HTTP_APACHE_ORG_XML_FEATURES_DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";
	private static final Logger LOGGER = LoggerFactory.getLogger(XMLConfig.class);

	/**
	 * It will read the xml configuration from file.
	 *
	 * @param root     : tag name from which you want to start
	 * @param tagName  : the exact tag name that you want to get
	 * @param filePath : optional (if you want to use other xml file)] e.g
	 *                 "src\test\resources\other.xml"
	 * @return value of requested tag
	 * @throws URISyntaxException
	 * @author: Kunal Kaviraj
	 */
	public static String getConfig(String root, String tagName, String... filePath)
	{
		Document doc = null;
		ClassLoader CLDR = Config.class.getClassLoader();
		String filePathFinal = filePath.length == 0 ? "config/config.xml" : filePath[0];
		filePathFinal = filePathFinal.replace("./src/main/resources/", "");
		InputStream inputstream = CLDR.getResourceAsStream(filePathFinal);
		String value = null;
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setAttribute(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
			dbf.setFeature(HTTP_APACHE_ORG_XML_FEATURES_DISALLOW_DOCTYPE_DECL, true);
			dbf.setFeature(HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_GENERAL_ENTITIES, false);
			dbf.setFeature(HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_PARAMETER_ENTITIES, false);
			// Disable external DTDs as well
			dbf.setFeature(HTTP_APACHE_ORG_XML_FEATURES_NONVALIDATING_LOAD_EXTERNAL_DTD, false);
			DocumentBuilder db = dbf.newDocumentBuilder();
			LOGGER.info("Parsing xml file : {}", filePathFinal);
			doc = db.parse(inputstream);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName(root);
			for (int itr = 0; itr < nodeList.getLength(); itr++)
			{
				Node node = nodeList.item(itr);
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) node;
					value = eElement.getElementsByTagName(tagName).item(0).getTextContent();
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred while parsing or reading file : {}", e.getMessage());
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @param rootName, tagName, filePath
	 * @return Value
	 * @MethodName getXmlConfigurator
	 * @author Kunal Kaviraj
	 */
	public static String getXmlConfigurator(String rootName, String tagName, String... filePath)
	{
		XMLConfig xmlConfig = new XMLConfig();
		String fileLocation = filePath.length == 0 ? xmlConfig.getFile("config/config.xml") : filePath[0];
		String value = null;
		try
		{
			File file = new File(fileLocation);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// an instance of builder to parse the specified xml file
			dbf.setAttribute(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
			dbf.setFeature(HTTP_APACHE_ORG_XML_FEATURES_DISALLOW_DOCTYPE_DECL, true);
			dbf.setFeature(HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_GENERAL_ENTITIES, false);
			dbf.setFeature(HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_PARAMETER_ENTITIES, false);
			// Disable external DTDs as well
			dbf.setFeature(HTTP_APACHE_ORG_XML_FEATURES_NONVALIDATING_LOAD_EXTERNAL_DTD, false);

			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName(rootName);
			// nodeList is not iterable, so we are using for loop
			for (int itr = 0; itr < nodeList.getLength(); itr++)
			{
				Node node = nodeList.item(itr);
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) node;
					value = eElement.getTextContent().trim().toString();
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred while parsing or reading file : {}", e.getMessage());
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @param rootName
	 * @param tagName
	 * @param filePath
	 * @return value
	 * @author Kunal Kaviraj
	 */
	public static String getReqFileConfigurator(String rootName, String tagName, String... filePath)
	{
		XMLConfig xmlConfig = new XMLConfig();
		String fileLocation = filePath.length == 0 ? xmlConfig.getFile("config/TestData.xml") : filePath[0];
		String value = null;
		try
		{
			File file = new File(fileLocation);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setAttribute(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
			dbf.setFeature(HTTP_APACHE_ORG_XML_FEATURES_DISALLOW_DOCTYPE_DECL, true);
			dbf.setFeature(HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_GENERAL_ENTITIES, false);
			dbf.setFeature(HTTP_XML_ORG_SAX_FEATURES_EXTERNAL_PARAMETER_ENTITIES, false);
			// Disable external DTDs as well
			dbf.setFeature(HTTP_APACHE_ORG_XML_FEATURES_NONVALIDATING_LOAD_EXTERNAL_DTD, false);
			// an instance of builder to parse the specified xml file
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName(rootName);

			// nodeList is not iterable, so we are using for loop
			for (int itr = 0; itr < nodeList.getLength(); itr++)
			{
				Node node = nodeList.item(itr);
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) node;
					value = eElement.getElementsByTagName(tagName).item(0).getTextContent();
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred while parsing or reading file : {}", e.getMessage());
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * It will return the file path for any file within main/resources f
	 *
	 * @param fileName
	 * @return
	 */
	private String getFile(String fileName)
	{
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		String filePath = null;
		try
		{
			filePath = resource.toURI().getPath();
		}
		catch (URISyntaxException e)
		{
			LOGGER.error("Error while getting file name : ", e.getMessage());
			e.printStackTrace();
		}
		return filePath;
	}
}