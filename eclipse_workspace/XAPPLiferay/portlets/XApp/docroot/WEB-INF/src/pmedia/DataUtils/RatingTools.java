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

import pmedia.types.Rating;
import pmedia.utils.StringUtils;


public class RatingTools 
{

	
	public static Rating getByContextId(ArrayList<Rating>ratings,int id)
	{
		 for (int s = 0; s < ratings.size(); s++)
		 {
			 Rating rating= ratings.get(s);
			 if(rating.getContextId() == id)
				 return rating;
		 }
		 return null;
	}
	
	
	public static ArrayList<Rating>readRatingsFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<Rating> result = new ArrayList<Rating>();
		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		try {
			doc = db.parse(file);	
		} catch (Exception e) {
			return result;
		}
		
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("item");
	    
		for (int s = 0; s < nodeLst.getLength(); s++) 
		{
			Node fstNode = nodeLst.item(s);
			Element element= (Element) fstNode;
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	Rating rating= new Rating();
		    	rating.setContext(StringUtils.getXMLFromElement(element, "context"));
		    	rating.setContextId(StringUtils.getFromXMLElementInteger(element, "context_id"));
		    	
		    	rating.setPscoreTotal(StringUtils.getFromXMLElement(element, "pscore_total"));
		    	rating.setPscoreCount(StringUtils.getFromXMLElementInteger(element, "pscore_count"));
		    	rating.setPscore(StringUtils.getFromXMLElement(element, "pscore"));
		    	result.add(rating);
		    	//System.out.println("got name " + appName);
		    }
		}
		return result;
	}
}
