package pmedia.DataUtils;

import java.io.File;
import java.io.IOException;

import java.text.DateFormat;
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

import cmx.types.ECMContentSourceType;
import cmx.types.Reference;

import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.LocationData;
import pmedia.types.MappingData;
import pmedia.types.PMDataTypes;
import pmedia.types.TranslationData;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import java.util.Date;
public class ArticleTools 
{


	//System.out.println("transforming location description :  " + location);
	
	public static ArrayList<String>getPicturesFromJArticleTranslation(String domain,ArticleData article,String lang)
	{
		if(article!=null)
		{
			TranslationData translation =null;
		
			ArrayList<TranslationData> translations = ServerCache.getInstance().getDC("ibiza").get(DomainCache.TRANSLATIONS);
			if(translations!=null && translations.size() > 0 )
			{
				translation = TranslationTools.getTranslationByTypeAndIndex(translations,article.refId,PMDataTypes.DITT_JARTICLE,lang);
				if(translation!=null)
				{
						if(translation.descr!=null && translation.descr.length() > 0)
						{
							
							if(article.getPictures()!=null)
							{
								article.getPictures().clear();
								article.setPictures(null);
							}
							
							article.setDescription(translation.descr);
							return article.getPictures();
						}
					}
				}
			}
		return null;
	}
	public static String getIconUrlFromJArticleTranslation(String domain,ArticleData article,String lang)
	{
		if(article!=null)
		{
			TranslationData translation =null;
		
			ArrayList<TranslationData> translations = ServerCache.getInstance().getDC("ibiza").get(DomainCache.TRANSLATIONS);
			if(translations!=null && translations.size() > 0 )
			{
				translation = TranslationTools.getTranslationByTypeAndIndex(translations,article.refId,PMDataTypes.DITT_JARTICLE,lang);
				if(translation!=null)
				{
						if(translation.descr!=null && translation.descr.length() > 0)
						{
							
							if(article.getPictures()!=null)
							{
								article.getPictures().clear();
								article.setPictures(null);
							}
							
							article.setDescription(translation.descr);
							article.getPictures();
							article.setIconUrl(null);
							
							return article.getIconUrl();
						}
					}
				}
			}
		
		return null;
	}
	public static ArticleData getArticleByTypeAndTitle(ArrayList<ArticleData>articles,String title,PMDataTypes type)
	{
		 for (int s = 0; s < articles.size(); s++)
		 {
			 ArticleData article = articles.get(s);
			 if(article.translationType == type && article.title.equalsIgnoreCase(title) )
				 return article;
			 
		 }
		 return null;
		 
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
	public static ArticleData getArticleByRefId(ArrayList<ArticleData>articles,int index)
	{
		 for (int s = 0; s < articles.size(); s++)
		 {
			 ArticleData article = articles.get(s);
			 if(article.refId==index)
				 return article;
		 }
		 return null;
	}
	
	public static ArrayList<ArticleData >getArticlesByRefGroup(ArrayList<ArticleData>articles,int refGroup)
	{
		if(articles==null)
		{
			return null;
		}
		ArrayList<ArticleData > result = new ArrayList<ArticleData>();
		 for (int s = 0; s < articles.size(); s++)
		 {
			 ArticleData article = articles.get(s);
			 if(article.getRefGroups()!=null)
			 {
				 for (int gi = 0; gi < article.getRefGroups().size() ; gi++) 
				 {
					 Reference gid = article.getRefGroups().get(gi);
					 Integer __ref = -1;
					 try {
						 __ref =Integer.parseInt(gid._reference);	
					} catch (Exception e) {
						
					}
					 
					 if(__ref !=-1 && __ref==refGroup){
						 result.add(article);
					 }
					 
					 /*
					 if(gid._reference.equals(""+refGroup))
					 {
						result.add(article);
					 }
					 */
				 }
			 }
		 }
		 if(result.size()==0)
		 {
			 return null;
		 }
		 return result;
	}
	
	public static ArrayList<ArticleData >getArticlesByRefGroupStr(ArrayList<ArticleData>articles,String refGroup)
	{
		ArrayList<ArticleData > result = new ArrayList<ArticleData>();
		 for (int s = 0; s < articles.size(); s++)
		 {
			 ArticleData article = articles.get(s);
			 if(article.getRefGroupsStr()!=null)
			 {
				 for (int gi = 0; gi < article.getRefGroupsStr().size() ; gi++) 
				 {
					 String gid = article.getRefGroupsStr().get(gi);
					 if(gid.equals(refGroup))
					 {
						result.add(article);
					 }
				 }
			 }
		 }
		 if(result.size()==0)
		 {
			 return null;
		 }
		 return result;
	}
	public static ArrayList<ArticleData >getArticlesByTypeAndParent(ArrayList<ArticleData>articles,int parent,PMDataTypes type)
	{
		ArrayList<ArticleData > result = new ArrayList<ArticleData>();
		 for (int s = 0; s < articles.size(); s++)
		 {
			 ArticleData article = articles.get(s);
			 if(article.translationType == type && article.groupId==parent)
			 {
				result.add(article);
			 }
			 
		 }
		 return result;
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
	
	public static ArrayList<MappingData>readMappingsFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<MappingData> result = new ArrayList<MappingData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("item");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	MappingData article = new MappingData();
		    	Element fstElmnt = (Element) fstNode;
		    	
		    	article.dstID = Integer.parseInt(fstElmnt.getAttribute("dstID"));
		    	article.srcID = Integer.parseInt(fstElmnt.getAttribute("srcID"));
		    	String srcType = fstElmnt.getAttribute("srcType");
		    	String dstType = fstElmnt.getAttribute("dstType");
		    	article.srcType = getMappingType(srcType);
		    	article.dstType= getMappingType(dstType);
		        result.add(article);
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
	
	public static ArrayList<BaseData>readWPostsFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<BaseData > result = new ArrayList<BaseData>();
		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		try {
			doc = db.parse(file);			
		} catch (Exception e) {
			return null;
		}
		
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("art");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	ArticleData article = new ArticleData();
		    	Element fstElmnt = (Element) fstNode;
		    	
		    	
		    	
		    	//article.setO(StringUtils.getIntegerFromXMLAttribute(fstElmnt, "id"));
		    	//article.acc= Integer.parseInt(fstElmnt.getAttribute("ac"));
		    	article.refId = -1;
		    	try {
		    		article.refId =Integer.parseInt(fstElmnt.getAttribute("id"));
		    		
				} catch (Exception e) {
					
				}
				try {
					article.setRefId(StringUtils.getIntegerFromXMLAttribute(fstElmnt, "id"));
				} catch (Exception e) 
				{
					
				}
		    	
		    	String pub = fstElmnt.getAttribute("pub");
		    	if(pub!=null && pub.equals("open"))
		    	{
		    		article.pub=true;
		    	}
	
		    	//article.cat_id = Integer.parseInt(fstElmnt.getAttribute("cid"));
		    	//article.translationType = PMDataTypes.DITT_JARTICLE;
		    	article.title = StringUtils.getXMLFromElement(fstElmnt,"title");
		    	
		    	String createdStr = fstElmnt.getAttribute("created");
		    	if(createdStr!=null)
		    	{
		    		DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		    		try {
		    			article.created= df.parse(createdStr);
					} catch (Exception e) {
						
					}
        			//String date = new java.text.SimpleDateFormat("yyyy-MM-ddTHH:mm:ss").format(new java.util.Date (endTime*1000));
		    	}
		    	article.introText = StringUtils.getXMLFromElement(fstElmnt,"fullText");
		    	article.description = StringUtils.getXMLFromElement(fstElmnt,"introtext");
		    	
		    	NodeList groupsLst  = fstElmnt.getElementsByTagName("categories");
		    	if(groupsLst!=null){
			    	
		    		Node cNode = groupsLst.item(0);
			    	
			    	if (cNode!=null && cNode.getNodeType() == Node.ELEMENT_NODE)
				    {
				    	Element cElmnt = (Element) cNode;
				    	NodeList gList = cElmnt.getElementsByTagName("category");
				    	for (int sc = 0; sc < gList.getLength(); sc++)
						{
				    		Element gE = (Element) gList.item(sc);
					    	int _gId = StringUtils.getIntegerFromXMLAttribute(gE, "id");
					    	if(_gId!=-1)
					    	{
					    		if(article.getRefGroups()==null){
					    			article.setRefGroups(new ArrayList<Reference>());
					    		}
					    		article.getRefGroups().add(new Reference(""+_gId));
					    	}
					    	
					    	Reference r;
					    	
					    	
					    	String _gIdStr = gE.getAttribute("idStr");
					    	if(_gIdStr!=null)
					    	{
					    		if(article.getRefGroupsStr()==null){
					    			article.setRefGroupsStr(new ArrayList<String>());
					    		}
					    		article.getRefGroupsStr().add(_gIdStr);
					    	}
					    }
					}
		    	}
		    	
		    	article.setSourceType(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.WordpressPost));
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
		Document doc = null;
		try {
			doc = db.parse(file);			
		} catch (Exception e) {
			return null;
		}
		
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("art");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	ArticleData article = new ArticleData();
		    	Element fstElmnt = (Element) fstNode;
		    	try {
		    		article.acc= Integer.parseInt(fstElmnt.getAttribute("ac"));	
				} catch (Exception e) {
					// TODO: handle exception
				}
		    	
		    	if(fstElmnt.getAttribute("id")!=null)
		    	{
		    		try 
		    		{
		    			article.refId = Integer.parseInt(fstElmnt.getAttribute("id"));
					} catch (Exception e) 
					{
					
					}
		    		
		    	}
		    	article.title = StringUtils.getXMLFromElement(fstElmnt,"title");
		    	/*
		    	if(article.title.equals("Second Blog Post")){
		    		//System.out.println("as");
		    	}
		    	
		    	*/
		    	article.pub= Boolean.getBoolean(fstElmnt.getAttribute("pub"));
		    	
		    	String pubStr = fstElmnt.getAttribute("published");
		    	if(pubStr!=null){
		    		int pubInt = 0; 
		    		try {
		    			pubInt=Integer.parseInt(pubStr);
		    			if(pubInt>0){
		    				article.published=true;
		    			}else{
		    				article.published=false;
		    			}
					} catch (Exception e) {
						// TODO: handle exception
					}
		    	}
		    	
		    	article.ownerRefId=-1;
		    	try {
		    		article.ownerRefId = Integer.parseInt(fstElmnt.getAttribute("owner"));	
				} catch (Exception e) {
					
				}
		    	
		    	article.ownerRefStr=null;
		    	try {
		    		article.ownerRefStr= fstElmnt.getAttribute("ownerName");	
				} catch (Exception e) {
					
				}
		    	if(article.ownerRefStr!=null && article.ownerRefStr.length()==0){
		    		article.ownerRefStr=null;
		    	}
		    	
		    	
		    	
		    	//Joomla 1.7 :
		    	
		    	int pubInt =0;
		    	String pub = fstElmnt.getAttribute("pub");
		    	if(pub!=null && pub.length() > 0){
		    		//System.out.println( " pub " + pub + " in " +article.title);
			    	try {
			    		pubInt = Integer.parseInt(fstElmnt.getAttribute("pub"));
					} catch (Exception e) {
						pubInt=0;
					}
					if(pubInt==1){
						article.published=true;
					}else{
						article.published=false;
					}
					
					article.pub=article.published;
		    	}
				
				int groupId = -1;
				try {
					groupId = Integer.parseInt(fstElmnt.getAttribute("cid"));
					article.setGroupId(groupId);
				} catch (Exception e) {
					// TODO: handle exception
				}
		    	//article.setGroupId(Integer.parseInt(fstElmnt.getAttribute("cid")));
		    	
		    	article.translationType = PMDataTypes.DITT_JARTICLE;
		    	
		    	
		    	String createdStr = fstElmnt.getAttribute("created");
		    	if(createdStr!=null)
		    	{
		    		DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        			//String date = new java.text.SimpleDateFormat("yyyy-MM-ddTHH:mm:ss").format(new java.util.Date (endTime*1000));
        			try {
        				article.created= df.parse(createdStr);	
					} catch (Exception e) {
						
					}
        			
        			if(article.created==null){
        				//2011-01-01 00:00:01
        				df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            			//String date = new java.text.SimpleDateFormat("yyyy-MM-ddTHH:mm:ss").format(new java.util.Date (endTime*1000));
            			try {
            				article.created= df.parse(createdStr);	
    					} catch (Exception e) {
    						
    					}
        			}
		    	}
		    	
		    	if(article.created!=null){
		    	//	System.out.println(" t : " + article.title + " pub " + article.published + " article : pub " + article.pub);
		    	}
		    	
		    	article.introText = StringUtils.getXMLFromElement(fstElmnt,"fullText");
		    	article.description = StringUtils.getXMLFromElement(fstElmnt,"introtext");
		        result.add(article);
		    }
		 }
		return result;
	}
}
