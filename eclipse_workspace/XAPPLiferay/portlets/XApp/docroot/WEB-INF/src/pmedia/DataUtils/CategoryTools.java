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
import pmedia.types.Category;
import pmedia.types.CategoryComparator;
import pmedia.types.EventData;
import pmedia.types.LocationData;
import pmedia.types.MosetCategory;
import pmedia.utils.StringHelper;
import pmedia.utils.StringUtils;
public class CategoryTools 
{
	
	public static void updateLocalizedTitle(String domain,String lang,ArrayList<Category>cats)
	{
		for (int s = 0; s < cats.size() ; s++) 
		{
			Category cat = cats.get(s);
			String category=TranslationTools.translateCategory(domain,lang,cat.refId);
			if(category!=null && category.length() > 0)
			{
				cat.setLocalizedTitle(category);
				//System.out.println( " setting : "  + cat.title  + " to  " + category);
			}else
			{
				cat.setLocalizedTitle(null);
			}
		}
	}
	public static void sortByTitleTranslated(String domain,String lang,ArrayList<Category>cats)
	{
		for (int s = 0; s < cats.size() ; s++) 
		{
			Category cat = cats.get(s);
			//cat.updateTranslations(domain);
		}
		Comparator<Category> catAlpComparator = new CategoryComparator();
		CategoryComparator cc= (CategoryComparator)catAlpComparator;
		cc.lang = lang;
		if(cats!=null)
		{
			Collections.sort(cats,catAlpComparator);
		}
	}
	public static Boolean hasLocations(ArrayList<Category>srcCategories,ArrayList<LocationData>srcLocations,int index)
	{
		if(srcCategories==null)
			return false;
		
		if(srcLocations==null)
			return false;
		
		Category thisCat = getCatByIndex(srcCategories, index);
		
		
		for (int s = 0; s < srcLocations.size() ; s++) 
		{
			LocationData loc = srcLocations.get(s);
			
			if(thisCat.published && loc.cat_id==thisCat.refId)
			{
				return true;
			}
		}
		return false;
	}
	public static Boolean hasSubCategories(ArrayList<Category>srcCategories,int index)
	{
		if(srcCategories==null)
			return false;
		
		for (int s = 0; s < srcCategories.size() ; s++) 
		{
			Category c = srcCategories.get(s);
			
			if(c.refId !=index && c.groupId==index)
			{
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Category> insertSubCategoriesByIndex(ArrayList<Category>allCategories,ArrayList<Category>srcCategories,ArrayList<LocationData>srcLocations,String lang)
	{
		ArrayList<Category>result = new ArrayList<Category>();
		
		for (int s = 0; s < srcCategories.size() ; s++) 
		{
			Category c = srcCategories.get(s);
			c.intendLevel=0;
			result.add(c);
			
			if(CategoryTools.hasSubCategories(allCategories, c.refId))
			{
				ArrayList<Category>subCats =CategoryTools.getLocationCategoriesByIndex(allCategories,srcLocations,c.refId,false);
				if(subCats.size() > 0)
				{
					
					for (int s0 = 0; s0 < subCats.size() ; s0++) 
					{
						Category c0 = subCats.get(s0);
						c0.intendLevel=1;
					}
					CategoryTools.sortByTitleTranslated("ibiza",lang,subCats);
					CategoryTools.updateLocalizedTitle("ibiza",lang,subCats);
					result.addAll(subCats);
				}else
				{
					
				}
			}
		}
		
		//ArrayList<Category>result = CategoryTools.getLocationCategoriesByIndex(srcCategories,srcLocations,index,flatten);
		//dstCategories.addAll(result);
		return result;
	}
	public static void addLocationCategoriesByIndex(ArrayList<Category>srcCategories,ArrayList<Category>dstCategories,ArrayList<LocationData>srcLocations,int index,boolean flatten)
	{
		//ArrayList<Category>result = CategoryTools.getLocationCategoriesByIndex(srcCategories,srcLocations,index,flatten);
		//dstCategories.addAll(result);
		if(srcCategories==null)
			return;
		
		ArrayList<Category>result = new ArrayList<Category>();
		
		for (int s = 0; s < srcCategories.size() ; s++) 
		{
			Category c = srcCategories.get(s);
			if(c.groupId==index && c.published && hasLocations(srcCategories, srcLocations, c.refId))
			{
				
			}
		}
	}
	public static ArrayList<Category>getLocationCategoriesByIndex(ArrayList<Category>srcCategories,ArrayList<LocationData>srcLocations,int index,boolean flatten)
	{
		
		if(srcCategories==null)
			return null;
		
		ArrayList<Category>result = new ArrayList<Category>();
		
		for (int s = 0; s < srcCategories.size() ; s++) 
		{
			Category c = srcCategories.get(s);
			if(c.groupId==index && c.published /*&& hasLocations(srcCategories, srcLocations, c.refId)*/)
			{
				result.add(c);
				/*
				if(flatten && CategoryTools.hasSubCategories(srcCategories, c.refId))
				{
					CategoryTools.addLocationCategoriesByIndex(srcCategories, result, srcLocations, c.refId, flatten);
				}*/
					
			}
		}
		return result;
	}
	public static ArrayList<BaseData>getLocationCategoriesByIndex2(ArrayList<BaseData>srcCategories,ArrayList<BaseData>srcLocations,int index,boolean flatten)
	{
		
		if(srcCategories==null)
			return null;
		ArrayList<BaseData>result = new ArrayList<BaseData>();
		for (int s = 0; s < srcCategories.size() ; s++) 
		{
			Category c = (Category) srcCategories.get(s);
			if(c.groupId==index && c.published /*&& hasLocations(srcCategories, srcLocations, c.refId)*/)
			{
				result.add(c);/*
				if(flatten && CategoryTools.hasSubCategories(srcCategories, c.refId))
				{
					CategoryTools.addLocationCategoriesByIndex(srcCategories, result, srcLocations, c.refId, flatten);
				}*/
					
			}
		}
		return result;
	}
	public static ArrayList<Category>getTicketCategoryList(ArrayList<EventData> srcTickets)
	{
		
		ArrayList<Category>result = new ArrayList<Category>();
		
		for (int s = 0; s < srcTickets.size() ; s++) 
		{
			EventData e = srcTickets.get(s);
			if(e.ticketCategory !=null)
			{
				
				String cats[] = StringHelper.split(e.ticketCategory,";");
				String firstTerm = null;
				
				if(cats !=null && cats.length > 0)
				{
					firstTerm = cats[0];
				}
				
				String innerTerms[] = StringHelper.split(firstTerm, "/");
				String catTitle=null;
				if(innerTerms!=null && innerTerms.length > 1)
				{
					catTitle = innerTerms[1];
					
				}
				
				if(catTitle !=null)
				{
					catTitle = catTitle.trim(); 
				}
				
				Category c= new Category();
				if(catTitle !=null)
				{
					c.title = catTitle;
				}	
				
				if(c !=null)
				{
					Category searchCat = getCatByName(result,c.title );
				
					
					if(searchCat ==null)
					{
						result.add(c);
					}
				}
			}
			
		}
		
		return result;
	}
	
	public static String getTicketCategory(String ticketCatgory)
	{
		
		String cats[] = StringHelper.split(ticketCatgory,";");
		String firstTerm = null;
		
		if(cats !=null && cats.length > 0)
		{
			firstTerm = cats[0];
		}
		
		String innerTerms[] = StringHelper.split(firstTerm, "/");
		String catTitle=null;
		if(innerTerms!=null && innerTerms.length > 1)
		{
			catTitle = innerTerms[1];
		}

		if(catTitle !=null)
		{
			catTitle = catTitle.trim(); 
		}
		return catTitle;
	}
	
	
	public static Category getCatByIndex(ArrayList srcCategories,int index)
	{
		if(srcCategories!=null)
		{
			for (int s = 0; s < srcCategories.size() ; s++) 
			 {
			    Category c = (Category) srcCategories.get(s);
			    if(c.refId == index)
			    	return c;
			 }
		}
		return null;
	}
	
	public static ArrayList<Category> getCategoriesByParentIndex(ArrayList<Category>srcCategories,int index)
	{
		ArrayList<Category> result = new ArrayList<Category>();
		if(srcCategories!=null)
		{
			for (int s = 0; s < srcCategories.size() ; s++) 
			 {
			    Category c = srcCategories.get(s);
			    if(c.groupId == index){
			    	result.add(c);
			    	c.hasSubCategories = CategoryTools.hasSubCategories(srcCategories, c.refId);
			    }
			 }
		}
		return result;
	}
	
	public static String getCategoryFullString(ArrayList<Category>srcCategories,int index)
	{
		
		String out = "";
		
		Category c = getCatByIndex(srcCategories, index);
		if(c !=null )
		{
			out+=c.title;
			
			if(c.groupId !=0)
			{
				Category sub = getCatByIndex(srcCategories, c.groupId);
				if(sub !=null)
				{
					out = getCategoryFullString(srcCategories, sub.refId) + " :: " + out;
				}
			}
		}
		return out;
	}
	
	public static Category getCatByName(ArrayList<Category>srcCategories,String test)
	{
		if(srcCategories==null)
			return null;
		
		if(test ==null)
			return null;
		
		
		for (int s = 0; s < srcCategories.size() ; s++) 
		 {
		    Category c = srcCategories.get(s);
		    if(c !=null && c.title !=null && c.title.toLowerCase().contentEquals(test.toLowerCase()))
		    	return c;
		 }
		return null;
	}
	
	public static ArrayList<BaseData>readWPCatsFromFile(String path,String type,int contentSourceType) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<BaseData> result = new ArrayList<BaseData>();
		File file = new File(path);
		if(!file.exists()){
			return null;
		}
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
		    	Category cat = new Category();
		    	cat.setCategorySourceType(contentSourceType);
		    	Element fstElmnt = (Element) fstNode;
		    	
		    	///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		title	
		        //
		    	cat.setTitle(StringUtils.getXMLFromElement(fstElmnt, "title"));
		    	

		    	///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		refId	
		        //
		    	cat.refId=StringUtils.getIntegerFromXMLAttribute(fstElmnt, "id");
				cat.setRefId(cat.refId);
				cat.groupId= StringUtils.getIntegerFromXMLElement(fstElmnt, "parent");
				cat.description= StringUtils.getXMLFromElement(fstElmnt, "descr");
				result.add(cat);
		    }
		}
		
		return result;
	}
	
	public static ArrayList<Category>readFromRPCFile(String path,String type,int contentSourceType) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<Category> result = new ArrayList<Category>();
		
		File file = new File(path);
		if(!file.exists()){
			return result;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		
		try {
			doc = db.parse(file);	
		} catch (Exception e) {
			//e.printStackTrace();
			return result;
		}
		
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("item");
		
		for (int s = 0; s < nodeLst.getLength(); s++) 
		{
		    Node fstNode = nodeLst.item(s);
		    
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
		    {
		    	Category cat = new Category();
		    	cat.setCategorySourceType(contentSourceType);

		    	Element fstElmnt = (Element) fstNode;
		    	
		    	///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		title	
		        //
		       cat.title=StringUtils.getXMLFromElement(fstElmnt, "title");
		        
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        cat.groupId = StringUtils.getIntegerFromXMLElement(fstElmnt, "groupId");
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		id	
		        //
		        cat.refId = StringUtils.getIntegerFromXMLElement(fstElmnt, "refId");
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //


		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        cat.access = StringUtils.getIntegerFromXMLElement(fstElmnt, "access");
		        
		        String enabled = fstElmnt.getAttribute("pub");
		        if(enabled!=null && enabled.length() > 0)
		        {
		        	cat.published = enabled.equals("1") ? true : false;
		        }
		        
		        String pic = StringUtils.getXMLFromElement(fstElmnt, "picture");
		        if(pic!=null){
		        	cat.iconUrl = pic;
		        }
		        
		      
		      
		        result.add(cat);
		        
		        cat.description = StringUtils.getXMLFromElement(fstElmnt, "descr");
		        cat.introText = StringUtils.getXMLFromElement(fstElmnt, "introText");
		        if((cat.description==null || cat.description.length()==0) && cat.introText !=null)
		        {
		        	cat.description=cat.introText;
		        }
		        cat.sourceType=""+contentSourceType;
		    }
		}
		
		return result;
	}
	public static ArrayList<Category>readFromFile(String path,String type,int contentSourceType) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<Category> result = new ArrayList<Category>();
		
		File file = new File(path);
		if(!file.exists()){
			return result;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		
		try {
			doc = db.parse(file);	
		} catch (Exception e) {
			//e.printStackTrace();
			return result;
		}
		
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("item");
		
		for (int s = 0; s < nodeLst.getLength(); s++) 
		{
		    Node fstNode = nodeLst.item(s);
		    
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
		    {
		    	Category cat = new Category();
		    	cat.setCategorySourceType(contentSourceType);

		    	Element fstElmnt = (Element) fstNode;
		    	
		    	///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		title	
		        //
		       cat.title=StringUtils.getXMLFromElement(fstElmnt, "title");
		        
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        cat.groupId  = StringUtils.getIntegerFromXMLElement(fstElmnt, "parent");
		        cat.groupId = cat.groupId;
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		id	
		        //
		        cat.refId = StringUtils.getIntegerFromXMLElement(fstElmnt, "id");
		        //cat.refId = StringUtils.getIntegerFromXMLElement(fstElmnt, "id");
		        
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        NodeList sElmntLst = fstElmnt.getElementsByTagName("section");
		        if(sElmntLst==null){
		        	sElmntLst = fstElmnt.getElementsByTagName("extension");
		        }
		        if( sElmntLst != null)
		        {
		        	Element sElmnt = (Element) sElmntLst.item(0);
		        	if(sElmnt !=null)
		        	{
		        		NodeList t = sElmnt.getChildNodes();
		        		if(t !=null && t.item(0) !=null )
		        		{
		        			cat.sourceType = ((Node)t.item(0)).getNodeValue();
		        			
		        			if(cat.type!=null && cat.sourceType.length() > 0)
		        			{
		        				int _parent = -1;
		        				try {
		        					_parent = Integer.parseInt(cat.sourceType);
		        					if(cat.groupId == 0)
		        					{
		        						cat.groupId=_parent;
		        						cat.groupId=cat.groupId;
		        						
		        					}
		        					
		        					if(type.equals("com_content"))
		        					{
		        						if(_parent!=-1)
		        						{
		        							cat.sourceType="com_content";
		        						}
		        					}
		        					
								} catch (Exception e) {
									// TODO: handle exception
								}
		        				
		        			}
		        			
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        cat.access = StringUtils.getIntegerFromXMLElement(fstElmnt, "access");
		        
		        String enabled = fstElmnt.getAttribute("pub");
		        if(enabled!=null && enabled.length() > 0)
		        {
		        	cat.published = enabled.equals("true") ? true : false;
		        }
		        
		        String pic = StringUtils.getXMLFromElement(fstElmnt, "picture");
		        if(pic!=null){
		        	cat.iconUrl = pic;
		        }
		        
		        if(cat.type.equals("com_banner")){
		        	
		        	//System.out.println("std pic : " + cat.type);
		        }
		        if(type !=null && type.equalsIgnoreCase(cat.sourceType))
		        {
		        	result.add(cat);
		        }
		        
		        if(type ==null)
		        {
		        	result.add(cat);
		        }
		        cat.description = StringUtils.getXMLFromElement(fstElmnt, "descr");
		        cat.introText = StringUtils.getXMLFromElement(fstElmnt, "introText");
		        if((cat.description==null || cat.description.length()==0) && cat.introText !=null)
		        {
		        	cat.description=cat.introText;
		        }
		        cat.sourceType=""+contentSourceType;
		    }
		}
		
		return result;
	}
	
	public static ArrayList<Category>readMosetCategoriesFromFile(String path,String type,int contentSourceType) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<Category> result = new ArrayList<Category>();
		
		File file = new File(path);
		if(!file.exists()){
			return result;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		
		
		
		try {
			doc = db.parse(file);
		} catch (Exception e) {
			//e.printStackTrace();
			return result;
		}
		doc.getDocumentElement().normalize();
		//System.out.println("Root element " + doc.getDocumentElement().getNodeName());
		
		NodeList nodeLst = doc.getElementsByTagName("item");
		
		for (int s = 0; s < nodeLst.getLength(); s++) 
		{
		    Node fstNode = nodeLst.item(s);
		    
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
		    {
		    	MosetCategory cat = new MosetCategory();
		    	cat.setCategorySourceType(contentSourceType);
		    	Element fstElmnt = (Element) fstNode;
		    	///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		title	
		        //
		        NodeList titleElmntLst = fstElmnt.getElementsByTagName("title");
		        if( titleElmntLst != null)
		        {
		        	Element titleElmnt = (Element) titleElmntLst.item(0);
		        	if(titleElmnt !=null)
		        	{
		        		NodeList title = titleElmnt.getChildNodes();
		        		if(title !=null && title.item(0) !=null )
		        		{
		        			cat.title = ((Node)title.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        NodeList pElmntLst = fstElmnt.getElementsByTagName("parent");
		        if( pElmntLst != null)
		        {
		        	Element pElmnt = (Element) pElmntLst.item(0);
		        	if(pElmnt !=null)
		        	{
		        		NodeList title = pElmnt.getChildNodes();
		        		if(title !=null && title.item(0) !=null )
		        		{
		        			cat.groupId = Integer.parseInt(((Node)title.item(0)).getNodeValue());
		        			cat.groupId=cat.groupId;
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        NodeList iElmntLst = fstElmnt.getElementsByTagName("id");
		        if( iElmntLst != null)
		        {
		        	Element iElmnt = (Element) iElmntLst.item(0);
		        	if(iElmnt !=null)
		        	{
		        		NodeList i = iElmnt.getChildNodes();
		        		if(i !=null && i.item(0) !=null )
		        		{
		        			cat.refId =Integer.parseInt(((Node)i.item(0)).getNodeValue());
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        NodeList sElmntLst = fstElmnt.getElementsByTagName("extension");
		        if( sElmntLst != null)
		        {
		        	Element sElmnt = (Element) sElmntLst.item(0);
		        	if(sElmnt !=null)
		        	{
		        		NodeList t = sElmnt.getChildNodes();
		        		if(t !=null && t.item(0) !=null )
		        		{
		        			cat.sourceType = ((Node)t.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        NodeList cElmntLst = fstElmnt.getElementsByTagName("color");
		        if( cElmntLst != null)
		        {
		        	Element cElmnt = (Element) cElmntLst.item(0);
		        	if(cElmnt !=null)
		        	{
		        		NodeList c = cElmnt.getChildNodes();
		        		if(c !=null && c.item(0) !=null )
		        		{
		        			cat.color = ((Node)c.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        NodeList aElmntLst = fstElmnt.getElementsByTagName("access");
		        if( aElmntLst != null)
		        {
		        	Element aElmnt = (Element) aElmntLst.item(0);
		        	if(aElmnt !=null)
		        	{
		        		NodeList a = aElmnt.getChildNodes();
		        		if(a !=null && a.item(0) !=null )
		        		{
		        			cat.access = Integer.parseInt(((Node)a.item(0)).getNodeValue());
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        
		        String enabled = fstElmnt.getAttribute("pub");
		        cat.picture =StringUtils.getXMLFromElement(fstElmnt, "picture");
		        if(cat.picture!=null)
		        {
		        	cat.iconUrl="components/com_mtree/img/cats/o/"+cat.picture;
		        }
		        
		        if(enabled!=null && enabled.length() > 0)
		        {
		        	cat.published= enabled.equals("1") ? true : false;
		        }
		        
		        if(type !=null && type.equalsIgnoreCase(cat.sourceType))
		        {
		        	result.add(cat);
		        }
		        
		        if(type ==null)
		        {
		        	result.add(cat);
		        }
		        cat.description = StringUtils.getXMLFromElement(fstElmnt, "descr");
		        cat.setType(ECMContentSourceType.MosetTreeCategory);
		        
		    }
		}
		
		return result;
	}
	public static ArrayList<Category>readFromFile17(String path,String type,int contentSourceType) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<Category> result = new ArrayList<Category>();
		
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
		
		//System.out.println("Root element " + doc.getDocumentElement().getNodeName());
		
		NodeList nodeLst = doc.getElementsByTagName("item");
		
		for (int s = 0; s < nodeLst.getLength(); s++) 
		{
		    Node fstNode = nodeLst.item(s);
		    
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
		    {
		    	ArticleCategory cat = new ArticleCategory();
		    	cat.sourceType=null;
		    	
		    	cat.setCategorySourceType(contentSourceType);
		    	
		    	Element fstElmnt = (Element) fstNode;
		    	
		    	///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		title	
		        //
		        NodeList titleElmntLst = fstElmnt.getElementsByTagName("title");
		        if( titleElmntLst != null)
		        {
		        	Element titleElmnt = (Element) titleElmntLst.item(0);
		        	if(titleElmnt !=null)
		        	{
		        		NodeList title = titleElmnt.getChildNodes();
		        		if(title !=null && title.item(0) !=null )
		        		{
		        			cat.title = ((Node)title.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        NodeList pElmntLst = fstElmnt.getElementsByTagName("parent");
		        if( pElmntLst != null)
		        {
		        	Element pElmnt = (Element) pElmntLst.item(0);
		        	if(pElmnt !=null)
		        	{
		        		NodeList title = pElmnt.getChildNodes();
		        		if(title !=null && title.item(0) !=null )
		        		{
		        			cat.groupId = Integer.parseInt(((Node)title.item(0)).getNodeValue());
		        			cat.groupId=cat.groupId;
		        			
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        NodeList iElmntLst = fstElmnt.getElementsByTagName("id");
		        if( iElmntLst != null)
		        {
		        	Element iElmnt = (Element) iElmntLst.item(0);
		        	if(iElmnt !=null)
		        	{
		        		NodeList i = iElmnt.getChildNodes();
		        		if(i !=null && i.item(0) !=null )
		        		{
		        			cat.refId = Integer.parseInt(((Node)i.item(0)).getNodeValue());
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        NodeList sElmntLst = fstElmnt.getElementsByTagName("extension");
		        if( sElmntLst != null)
		        {
		        	Element sElmnt = (Element) sElmntLst.item(0);
		        	if(sElmnt !=null)
		        	{
		        		NodeList t = sElmnt.getChildNodes();
		        		if(t !=null && t.item(0) !=null )
		        		{
		        			cat.sourceType = ((Node)t.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        NodeList cElmntLst = fstElmnt.getElementsByTagName("color");
		        if( cElmntLst != null)
		        {
		        	Element cElmnt = (Element) cElmntLst.item(0);
		        	if(cElmnt !=null)
		        	{
		        		NodeList c = cElmnt.getChildNodes();
		        		if(c !=null && c.item(0) !=null )
		        		{
		        			cat.color = ((Node)c.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        NodeList aElmntLst2 = fstElmnt.getElementsByTagName("picture");
		        if( aElmntLst2 != null)
		        {
		        	Element aElmnt = (Element) aElmntLst2.item(0);
		        	if(aElmnt !=null)
		        	{
		        		NodeList a = aElmnt.getChildNodes();
		        		if(a !=null && a.item(0) !=null )
		        		{
		        			cat.iconUrl =((Node)a.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        
//////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		parent	
		        //
		        NodeList aElmntLst = fstElmnt.getElementsByTagName("access");
		        if( aElmntLst != null)
		        {
		        	Element aElmnt = (Element) aElmntLst.item(0);
		        	if(aElmnt !=null)
		        	{
		        		NodeList a = aElmnt.getChildNodes();
		        		if(a !=null && a.item(0) !=null )
		        		{
		        			cat.access = Integer.parseInt(((Node)a.item(0)).getNodeValue());
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        
		        String enabled = fstElmnt.getAttribute("pub");
		        if(enabled!=null && enabled.length() > 0)
		        {
		        	cat.published = enabled.equals("true") ? true : false;
		        	 //Integer.parseInt(((Node)title.item(0)).getNodeValue());
		        	int pubInt = 0;
		        	try {
						pubInt=Integer.parseInt(enabled);
						if(pubInt==1){
							cat.published=true;
						}else{
							cat.published=false;
						}
					} catch (Exception e) {
						
					}
		        	
		        }
		        
		        String pic = StringUtils.getXMLFromElement(fstElmnt, "picture");
		        if(pic!=null){
		        	StringUtils.getXMLFromElement(fstElmnt, "picture");
		        }
		        
		        
		        if(type ==null)
		        {
		        	result.add(cat);
		        }else if(type !=null && cat.sourceType!=null)
		        {
		        	if(type.equalsIgnoreCase(cat.sourceType) || cat.sourceType.equalsIgnoreCase("system"))
		        	{
		        		result.add(cat);
		        	}
		        }
		        
		        
		        
		        
		        cat.description = StringUtils.getXMLFromElement(fstElmnt, "descr");
		    }
		}
		
		return result;
	}

	public static Boolean isValidCat(int cat_index,ArrayList<LocationData> locCats)
	{
	
		if(LocationArrayTools.isBlackListed(cat_index))
		{
			return false;
		}
	
		if(cat_index !=0)
		{
			Category c = getCatByIndex(Cache.categories,cat_index);
			if(c !=null && c.groupId !=0)
			{
				isValidCat(c.groupId,locCats);
			}
		}
		return true;
	}
}
