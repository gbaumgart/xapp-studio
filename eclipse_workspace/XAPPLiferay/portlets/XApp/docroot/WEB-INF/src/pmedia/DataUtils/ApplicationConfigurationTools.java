package pmedia.DataUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.types.ArticleData;
import pmedia.types.Category;
import pmedia.types.LocationData;
import pmedia.types.MappingData;
import pmedia.types.PMDataTypes;
import pmedia.types.PMResourceTypes;
import pmedia.types.ResourceData;
import pmedia.types.TranslationData;
import pmedia.types.ApplicationConfiguration;
import pmedia.utils.StringUtils;


public class ApplicationConfigurationTools 
{

	
	public static ApplicationConfiguration getAppConfigByIdentifier(ArrayList<ApplicationConfiguration>configs,String identifier)
	{
		 for (int s = 0; s < configs.size(); s++)
		 {
			 ApplicationConfiguration config= configs.get(s);
			 if(config.getAppIdentifier().equals(identifier))
				 return config;
			 
		 }
		 return null;
		 
	}
	
	
	public static ArrayList<ApplicationConfiguration>readAppConfigFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<ApplicationConfiguration> result = new ArrayList<ApplicationConfiguration>();
		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("baseConfig");
	    Node fstNode = nodeLst.item(0);
	    Element element= (Element) fstNode;
	    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
	    {
	    	ApplicationConfiguration config = new ApplicationConfiguration();
	    	config.setAppName(StringUtils.getXMLFromElement(element, "appName"));
	    	config.setServletContextName(StringUtils.getXMLFromElement(element, "servletContextName"));
	    	config.setAppIdentifier(StringUtils.getXMLFromElement(element, "appIdentifier"));
	    	config.setBannerDelay(StringUtils.getIntegerFromXMLElement(element, "bannerDelay"));

	    	config.setImageProcessorUrl(StringUtils.getXMLFromElement(element, "imageProcessorUrl"));
	    	config.setImageProcessorUrlLocal(StringUtils.getXMLFromElement(element, "imageProcessorUrlLocal"));

	    	config.setJoomlaImagesPath(StringUtils.getXMLFromElement(element, "joomlaImagesPath"));
	    	
	    	config.setWww(StringUtils.getXMLFromElement(element, "www"));
	    	config.setCmMaster(StringUtils.getXMLFromElement(element, "cmMaster"));
	    	config.setCmMasterDevelopment(StringUtils.getXMLFromElement(element, "cmMasterDevelopment"));
	    	
	    	config.setDbName(StringUtils.getXMLFromElement(element, "dbName"));
	    	
	    	String lat = StringUtils.getXMLFromElement(element, "defaultLat");
	    	String lon= StringUtils.getXMLFromElement(element, "defaultLon");
	    	try{
		    	config.setDefaultLat(Double.valueOf(lat));
		    	config.setDefaultLon(Double.valueOf(lon));
	    	}catch (Exception e) {
 
			}
	    	
	    	
	    	//config.setHasBanners(StringUtils.getBooleanFromXMLElement(element, "hasBanners"));
	    	config.setHasBanners(StringUtils.getIntegerFromXMLElement(element, "hasBanners") == 1 ? true : false );
	    	result.add(config);
	    	//System.out.println("got name " + appName);
	    }
		return result;
	}
}
