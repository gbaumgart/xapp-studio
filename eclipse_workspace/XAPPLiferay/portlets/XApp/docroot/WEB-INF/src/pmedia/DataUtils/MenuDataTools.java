package pmedia.DataUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pmedia.DataManager.Cache;
import pmedia.types.Category;
import pmedia.types.CategoryComparator;
import pmedia.types.EventData;
import pmedia.types.LocationData;
import pmedia.types.MenueData;
import pmedia.types.PMDataTypes;
import pmedia.types.TranslationData;
public class MenuDataTools 
{
	
	
	public static ArrayList<MenueData>readMenuData(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<MenueData > result = new ArrayList<MenueData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("eMenu");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	MenueData menue = new MenueData();
		    	Element fstElmnt = (Element) fstNode;
		    	
		    	menue.id = Integer.parseInt(fstElmnt.getAttribute("id"));
		    	menue.title = fstElmnt.getAttribute("t");
		    	menue.typeString = fstElmnt.getAttribute("m");
		    	menue.pub = Boolean.parseBoolean(fstElmnt.getAttribute("pub"));
		    	result.add(menue);
		    }
		 }
		return result;
	}
	
}
