package pmedia.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import pmedia.types.ArticleData;
import pmedia.types.LocationData;
import pmedia.types.MappingData;
import pmedia.types.PMDataTypes;
import pmedia.types.PMResourceTypes;
import pmedia.types.ResourceData;

public class ResourceTools 
{

	public static void copyDirectory(File sourceLocation , File targetLocation)
		    throws IOException 
		    {

			 if (sourceLocation.isDirectory()) {
		            if (!targetLocation.exists()) {
		                targetLocation.mkdir();
		            }
	
		            String[] children = sourceLocation.list();
		            for (int i=0; i<children.length; i++) {
		                copyDirectory(new File(sourceLocation, children[i]),
		                        new File(targetLocation, children[i]));
		            }
		        } else {
	
		        	//System.out.print("copying " + sourceLocation.getAbsolutePath() + " to : " + targetLocation.getAbsolutePath());
		            InputStream in = new FileInputStream(sourceLocation);
		            OutputStream out = new FileOutputStream(targetLocation);
	
		            // Copy the bits from instream to outstream
		            byte[] buf = new byte[1024];
		            int len;
		            while ((len = in.read(buf)) > 0) {
		                out.write(buf, 0, len);
		            }
		            in.close();
		            out.close();
		        }

		    
	}

	
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
	public static ArticleData getArticleByTypeAndIndex(ArrayList<ArticleData>articles,int index,PMDataTypes type)
	{
		 for (int s = 0; s < articles.size(); s++)
		 {
			 ArticleData article = articles.get(s);
			 if(article.translationType == type && article.refId==index)
				 return article;
			 
		 }
		 return null;
		 
	}
	public static PMDataTypes getMappingType(String src)
	{
		
		if(src.equals("helpLocation"))
		{
			return PMDataTypes.DITT_JLOCATION;
		}
		
		if(src.equals("article"))
		{
			return PMDataTypes.DITT_ARTICLE;
		}
		if(src.equals("jArticle"))
		{
			return PMDataTypes.DITT_JARTICLE;
		}
		
		return PMDataTypes.DITT_UNKNOWN;
		
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
	public static ArrayList<ArticleData>readITArticlesFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<ArticleData > result = new ArrayList<ArticleData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("art");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {

		    	LocationData l = new LocationData();
		    	ArticleData article = new ArticleData();
		    	Element fstElmnt = (Element) fstNode;
		    	article.region = Integer.parseInt(fstElmnt.getAttribute("r"));
		    	article.type = Integer.parseInt(fstElmnt.getAttribute("type"));
		    	article.refId = Integer.parseInt(fstElmnt.getAttribute("id"));
		    	article.isBeach = Boolean.getBoolean(fstElmnt.getAttribute("beach"));
		    	article.pub= Boolean.getBoolean(fstElmnt.getAttribute("pub"));
		    	article.video = fstElmnt.getAttribute("v");
		    	article.title= fstElmnt.getAttribute("title");
		    	article.translationType = PMDataTypes.DITT_ARTICLE;
		    	
		        result.add(article);
		    }
		 }
		return result;
	}
	
	public static ArrayList<ArticleData>readJArticlesFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<ArticleData > result = new ArrayList<ArticleData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("art");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	ArticleData article = new ArticleData();
		    	Element fstElmnt = (Element) fstNode;
		    	article.acc= Integer.parseInt(fstElmnt.getAttribute("ac"));
		    	article.refId = Integer.parseInt(fstElmnt.getAttribute("id"));
		    	article.pub= Boolean.getBoolean(fstElmnt.getAttribute("published"));
		    	article.translationType = PMDataTypes.DITT_JARTICLE;
		    	article.title = StringUtils.getXMLFromElement(fstElmnt,"title");
		    	article.www = StringUtils.getXMLFromElement(fstElmnt,"www");
		        result.add(article);
		    }
		 }
		return result;
	}
}
