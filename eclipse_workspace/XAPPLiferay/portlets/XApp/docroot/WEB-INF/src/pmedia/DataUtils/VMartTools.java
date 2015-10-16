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

import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import cmx.types.SQLDataSource;

import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.LocationData;
import pmedia.types.MappingData;
import pmedia.types.PMDataTypes;
import pmedia.types.ProductData;
import pmedia.types.TranslationData;
import pmedia.utils.StringUtils;
import java.util.Date;

public class VMartTools extends ArticleTools 
{

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
	
	public static ArrayList<ArticleData >getArticlesByParent(ArrayList<ArticleData>articles,int parent)
	{
		ArrayList<ArticleData > result = new ArrayList<ArticleData>();
		 for (int s = 0; s < articles.size(); s++)
		 {
			 ArticleData article = articles.get(s);
			 if(article.groupId==parent)
			 {
				result.add(article);
			 }
			 
		 }
		 return result;
	}
	
	public static ArrayList<ProductData>readProductsFromFile(DataSourceBase ds,String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<ProductData > result = new ArrayList<ProductData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		try {
			doc = db.parse(file);			
		} catch (Exception e) 
		{
			return null;
		}
		
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("art");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	ProductData article = new ProductData();
		    	Element fstElmnt = (Element) fstNode;
		    	
		    	article.refId = Integer.parseInt(fstElmnt.getAttribute("id"));
		    	
		    	article.pub= Boolean.getBoolean(fstElmnt.getAttribute("pub"));
		    	
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
		    	try {
		    		pubInt = Integer.parseInt(fstElmnt.getAttribute("pub"));
				} catch (Exception e) {
					pubInt=0;
				}
				if(pubInt==1){
					article.published=true;
				}
				
				article.pub=article.published;
				String cidStr = fstElmnt.getAttribute("cid");
				if(cidStr!=null){
					try {
						article.setGroupId(Integer.parseInt(cidStr));	
					} catch (Exception e) {
						article.setGroupId(-1);
					}
				}
		    	
		    	
		    	Boolean featured=false ;
		    	try {
		    		featured  =Boolean.parseBoolean(fstElmnt.getAttribute("featured"));	
				} catch (Exception e) {
					
				}
		    	
		    	if(featured){
		    		article.setFeatured(1);
		    	}else{
		    		article.setFeatured(0);
		    	}
		    	
		    	//article.setFeatured(Integer.parseInt(fstElmnt.getAttribute("cid")));
		    	
		    	article.translationType = PMDataTypes.DITT_JARTICLE;
		    	article.title = StringUtils.getXMLFromElement(fstElmnt,"title");
		    	
		    	
		    	//article.iconUrl = StringUtils.getXMLFromElement(fstElmnt,"picture");
		    	article.pictures=new ArrayList<String>();
		    	
		    	//article.getPictures();
		    	NodeList groupsLst  = fstElmnt.getElementsByTagName("picture");
		    	if(groupsLst!=null)
		    	{
			    	
				    	for (int sc = 0; sc < groupsLst.getLength(); sc++)
						{
				    		Node pNode = groupsLst.item(s);
				  		    if (pNode!=null && pNode.getNodeType() == Node.ELEMENT_NODE)
				  		    {
				  		    	if(pNode.getChildNodes() !=null && pNode.getChildNodes().getLength() > 0)
				  		    	{
						    		String picUrl =  pNode.getChildNodes().item(0).getNodeValue();//  pElmnt.getNodeValue(); //((Node)groupsLst.item(sc)).getNodeValue();
						    		if(picUrl!=null)
						    		{
						    			article.getPictures().add( ds.getUrl() +"/" + picUrl);
						    		}
				  		    	}
				  		    }
						}
		    	}
		    	
		    	//transfer the first to icon url
		    	if(article.getPictures().size() > 0)
		    	{
		    		article.setIconUrl(article.getPictures().get(0));
		    	}
		    	
		    	article.sku = fstElmnt.getAttribute("sku");
		    	article.weight = fstElmnt.getAttribute("weight");
		    	article.price = fstElmnt.getAttribute("price");
		    	article.currency= fstElmnt.getAttribute("currencySymbol");
		    	article.sourceType = "" + ECMContentSourceType.VMartProductItem;
		    	
		    	String createdStr = fstElmnt.getAttribute("created");
		    	if(createdStr!=null)
		    	{
		    		DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        			try {
        				article.created= df.parse(createdStr);	
					} catch (Exception e) {
					}
		    	}
		    	article.introText = StringUtils.getXMLFromElement(fstElmnt,"introText");
		    	//article.description = StringUtils.getXMLFromElement(fstElmnt,"introtext");
		    	article.description=article.introText;
		        result.add(article);
		    }
		 }
		return result;
	}
}
