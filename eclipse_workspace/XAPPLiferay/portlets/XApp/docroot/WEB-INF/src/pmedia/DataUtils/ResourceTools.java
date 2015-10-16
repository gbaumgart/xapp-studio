package pmedia.DataUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import pmedia.types.PMResourceTypes;
import pmedia.types.ResourceData;
import pmedia.utils.StringUtils;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import java.text.ParseException;
import pmedia.types.PMPlatformRenderConfiguration;
import pmedia.types.PMResourceTypes;
import pmedia.utils.SettingsUtil;
import pmedia.utils.StringUtils;
public class ResourceTools 
{
	
	public static ArrayList<ResourceData> getByType(ArrayList<ResourceData>resources,int type)
	{
		if(resources==null)
			return null;
		ArrayList<ResourceData>result = new ArrayList<ResourceData>();
		 for (int s = 0; s < resources.size(); s++)
		 {
			 ResourceData resource= resources.get(s);
			 if((resource.type==type || type==-1) && resource.pub)
			 {
				 result.add(resource);
			 }
		 }
		 return result;
	}
	public static ArrayList<ResourceData>readResourcesFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<ResourceData> result = new ArrayList<ResourceData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("items");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	ResourceData resource = new ResourceData();
		    	Element fstElmnt = (Element) fstNode;
		    	
		    	resource.description = StringUtils.getXMLFromElement(fstElmnt, "description");
		    	resource.title= StringUtils.getXMLFromElement(fstElmnt, "title");
		    	
		    	resource.alias= StringUtils.getXMLFromElement(fstElmnt, "alias");
		    	
		    	resource.custom0= StringUtils.getXMLFromElement(fstElmnt, "c0");
		    	resource.custom1= StringUtils.getXMLFromElement(fstElmnt, "c1");
		    	
		    	
		    	resource.pub= StringUtils.getBooleanFromXMLElement(fstElmnt, "published");
		    	
		    	//resource.p= StringUtils.getIntegerFromXMLElement(fstElmnt, "type");
		    	resource.type = StringUtils.getIntegerFromXMLElement(fstElmnt, "type");
		    	resource.id = StringUtils.getIntegerFromXMLElement(fstElmnt, "id");
		    	resource.cat_id= StringUtils.getIntegerFromXMLElement(fstElmnt, "id");
		    	resource.www = StringUtils.getXMLFromElement(fstElmnt,"www");
		        result.add(resource);
		        
		    }
		 }
		return result;
	}
	
}
