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

import cmx.types.ECMContentSourceType;

import pmedia.DataManager.Cache;
import pmedia.types.ArticleCategory;
import pmedia.types.BaseData;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.CListItemBanner;
import pmedia.types.Category;
import pmedia.types.CategoryComparator;
import pmedia.types.EventData;
import pmedia.types.LocationData;
import pmedia.types.MosetCategory;
import pmedia.utils.StringHelper;
import pmedia.utils.StringUtils;
public class JBannerTools 
{
	





	public static  ArrayList<CListItemBanner> readJBanner(String path,ECMContentSourceType  contentSourceType,String baseRef) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<CListItemBanner>result = new ArrayList<CListItemBanner>();
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
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
		    {
		    	CListItemBanner item = new CListItemBanner();
		    	//cat.setCategorySourceType(contentSourceType);
		    	Element fstElmnt = (Element) fstNode;
		    	///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		title	
		        //
		    	item.setTitle(StringUtils.getXMLFromElement(fstElmnt, "name"));
		    	item.setWebLink(StringUtils.getXMLFromElement(fstElmnt, "clickUrl"));
		    	item.setIconUrl(StringUtils.getXMLFromElement(fstElmnt, "iconUrl"));
		    	item.setParams(StringUtils.getXMLFromElement(fstElmnt, "params"));
		    	item.setIntroText(StringUtils.getXMLFromElement(fstElmnt, "desrc"));
		    	item.setType(contentSourceType);
		    	item.setPublished(StringUtils.getBooleanFromXMLAttribute(fstElmnt,"pub"));
		    	
		    	String picture = (StringUtils.getXMLFromElement(fstElmnt, "imgUrl"));
		    	if(picture!=null && picture.length() > 0)
		    	{
		    		picture = baseRef+"/images/banners/"+picture;
		    		item.setPicture(picture);
		    	}
		    	///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		refId	
		        //
		    	item.ref=String.valueOf(StringUtils.getIntegerFromXMLAttribute(fstElmnt, "bid"));
		    	try {
		    		item.refId=StringUtils.getIntegerFromXMLAttribute(fstElmnt, "bid");	
				} catch (Exception e) {
					
				}
		    	
		    	try {
		    		item.setGroupId(StringUtils.getIntegerFromXMLAttribute(fstElmnt, "cid"));	
				} catch (Exception e) {
					item.setGroupId(-1);
				}
				result.add(item);
		    }
		}
		
		return result;
	}

}
