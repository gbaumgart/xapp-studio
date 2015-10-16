package cmx.tools;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pmedia.DataManager.Cache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.Category;
import pmedia.types.Domain;
import pmedia.types.PMDataTypes;
import pmedia.utils.BitUtils;
import pmedia.utils.CListItemTools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import cmx.cache.DataSourceCache;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ContentTree;
import cmx.types.ContentTreeItem;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import cmx.types.Reference;

public class ContentTreeFactoryBase 
{	
	public static Category getTopLevelItem(ArrayList<Category>cats,int rootIndex)
	{
		Category result = null;
		for (int j = 0; j < cats.size() ; j++) 
		{
			Category  c = cats.get(j);
			if(c.published)
			{
				if(c.refId==rootIndex)
				{
					return c;
				}
				Boolean hasSubs = CategoryTools.hasSubCategories(cats, c.refId);
				if(hasSubs)
				{
					
				}
			}
		}
		return result;
	}
	public static Category getTopLevelItem(ArrayList<Category>cats)
	{
		
		Category result = null;
		for (int j = 0; j < cats.size() ; j++) 
		{
			Category  c = cats.get(j);
			if( c.published)
			{
				if(c.groupId==-1){
					return c;
				}
				Boolean hasSubs = CategoryTools.hasSubCategories(cats, c.refId);
				if(hasSubs)
				{
					
				}
			}
		}
		return result;
	}
	public static Boolean isTopLevelItem(ArrayList<Category>topLevelItems,int catIndex)
	{
			for (int j = 0; j < topLevelItems.size() ; j++) 
    		{
				Category c= topLevelItems.get(j);
				if( c.published)
				{
					if(c.refId == catIndex){
						return true;
					}
				}
    		}
		return false;
	}
	
	public static ArrayList<Category>getTopLevelItems(ArrayList<Category>cats,int rootIndex)
	{
		ArrayList<Category>result = new ArrayList<Category>();
		Category topLevelItem = getTopLevelItem(cats,rootIndex);
		if(topLevelItem!=null)
		{
			for (int j = 0; j < cats.size() ; j++) 
    		{
				Category c= cats.get(j);
				
				if(c.published)
				{
					if(c.groupId==topLevelItem.refId)
					{
						result.add(c); 
					}
				}
    		}
		}else{
			for (int j = 0; j < cats.size() ; j++) 
    		{
				Category c= cats.get(j);
				
				if(c.published)
				{
					if(c.groupId==rootIndex)
					{
						result.add(c); 
					}
				}
    		}
		}
		
		return result;
	}
	public static ArrayList<Category>getTopLevelItems(ArrayList<Category>cats)
	{
		ArrayList<Category>result = new ArrayList<Category>();
		
		Category topLevelItem = getTopLevelItem(cats);
		if(topLevelItem!=null)
		{
			for (int j = 0; j < cats.size() ; j++) 
    		{
				Category c= cats.get(j);
				
				if(c.published)
				{
					if(c.groupId==topLevelItem.refId)
					{
						result.add(c); 
					}
				}
    		}
		}
		
		return result;
	}
	
	public static void addToTreeItemFiltered(ArrayList<ContentTreeItem>items,ContentTreeItem dstTreeItem,String contentType1,String contentType2,ArrayList<Category>topLevelItems)
	{
		for(int i = 0  ; i  <  items.size() ; i++)	
		{
			ContentTreeItem item = items.get(i);
			
			if(contentType1!=null && contentType2==null)
			{
				if( item.contentType !=null && item.contentType.equals(contentType1) )
				{
					if(topLevelItems!=null && topLevelItems.size() > 0)
					{
						Integer catIndex = item.uid;
						
						if(!isTopLevelItem(topLevelItems, catIndex))
						{	
							continue;
						}
					}
					
					Reference ref = new Reference();
			    	ref._reference="" + item.id;
			    	dstTreeItem.addChild(ref);
			    	item.type = "leaf";
				}
			}else if(contentType1!=null && contentType2!=null)
			{
				if( item.contentType !=null && item.contentType.equals(contentType1) )
				{
					
					if(topLevelItems!=null && topLevelItems.size() > 0)
					{
						Integer catIndex = item.uid;
						
						if(!isTopLevelItem(topLevelItems, catIndex))
						{	
							continue;
						}
					}
					
					Reference ref = new Reference();
			    	ref._reference="" + item.id;
			    	dstTreeItem.addChild(ref);
			    	item.type = "leaf";
				}
			}
		}
		
	}
	
	public static void addToTreeItemFiltered(ArrayList<ContentTreeItem>items,ContentTreeItem dstTreeItem,String contentType1,String contentType2)
	{
		for(int i = 0  ; i  <  items.size() ; i++)	
		{
			ContentTreeItem item = items.get(i);
			
			if(contentType1!=null && contentType2==null)
			{
				if( item.contentType !=null && item.contentType.equals(contentType1) )
				{
					Reference ref = new Reference();
			    	ref._reference="" + item.id;
			    	dstTreeItem.addChild(ref);
			    	item.type = "leaf";
				}
			}else if(contentType1!=null && contentType2!=null)
			{
				if( item.contentType !=null && item.contentType.equals(contentType1) )
				{
					Reference ref = new Reference();
			    	ref._reference="" + item.id;
			    	dstTreeItem.addChild(ref);
			    	item.type = "leaf";
				}
			}
		}
		
	}
	/***
	 * 
	 * @param data
	 * @param section
	 * @param contentType
	 * @param dataSourceUID
	 * @param parentItem
	 * @param tree
	 * @return
	 */
	public static ContentTreeItem createTreeItemWithDataEx(
			BaseData data,
			String section,
			String contentType,
			String dataSourceUID,
			ContentTreeItem parentItem,
			ContentTree tree,
			ArrayList<ContentTreeItem> dstArray
			)
	{
		
		
		ContentTreeItem result = createTreeItemWithDataEx(
				data,
				section,
				contentType, 
				dataSourceUID
				);
		
		if(parentItem!=null){
			Reference itemRef = new Reference();
	    	itemRef._reference="" + result.id;
	    	parentItem.addChild(itemRef);
	    	
		}
		
		if(tree!=null){
			tree.items.add(result);
		}
	
		if(dstArray!=null){
			dstArray.add(result);
		}
		return result;
	}
	/***
	 * 
	 * @param data
	 * @param section
	 * @param sourceType
	 * @param dataSourceUID
	 * @param parentItem
	 * @param tree
	 * @return
	 */
	public static ContentTreeItem createDummyTreeItem(
			String title,
			ContentTreeItem parentItem,
			ContentTree tree,
			ArrayList<ContentTreeItem> dstArray
			)
	{
		ContentTreeItem result = createTreeItemEx(
				title, 
				UUID.randomUUID().toString(), 
				""+-1, 
				null,
				""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown), 
				false, 
				null,
				parentItem,
				tree,
				dstArray);

		return result;
	}
	public static ContentTreeItem createDummyTreeItemEx(
			String title,
			ECMContentSourceType contentType,
			ContentTreeItem parentItem,
			ContentTree tree,
			ArrayList<ContentTreeItem> dstArray
			)
	{
		ContentTreeItem result = createTreeItemEx(
				title, 
				UUID.randomUUID().toString(), 
				""+-1, 
				null,
				""+ECMContentSourceTypeTools.TypeToInteger(contentType), 
				false, 
				null,
				parentItem,
				tree,
				dstArray);

		return result;
	}
	
	/***
	 * 
	 * @param data
	 * @param section
	 * @param contentType
	 * @param dataSourceUID
	 * @return
	 */
	public static ContentTreeItem createTreeItemWithDataEx(
			BaseData data,
			String section,
			String contentType,
			String dataSourceUID
			)
	{
		
		int uid = 0;
		if(data instanceof Category){
			uid = ((Category) data).refId;
		}else if (data instanceof ArticleData){
			uid = data.refId;
		}
		
		ContentTreeItem result = createTreeItemEx(
				data.getTitle(), 
				data.getTreeUid(), 
				"" + uid, 
				section,
				contentType, 
				false, 
				dataSourceUID,
				null,
				null,
				null);

		return result;
		
	}
	/**
	 * 
	 * @param label
	 * @param id
	 * @param uid
	 * @param section
	 * @param contentType
	 * @param top
	 * @param dataSourceUID
	 * @return
	 */
	public static ContentTreeItem createTreeItemEx(
			String label,
			String id,
			String uid,
			String section,
			String contentType,
			Boolean top,
			String dataSourceUID,
			ContentTreeItem parentItem,
			ContentTree tree,
			ArrayList<ContentTreeItem> dstArray)
	{
		ContentTreeItem result = new ContentTreeItem();
		result.children = new ArrayList<Reference>();
		result.name = label;
		
		
		
		int _uid =0;
		try {
			_uid=Integer.parseInt(uid);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		result.uid=_uid;
		
		if(id!=null){
			result.id=id;
		}else{
			result.id = UUID.randomUUID().toString();
		}
		if(contentType!=null)
		{
			result.contentType=contentType;
		}else{
			result.contentType = "" + ECMContentSourceType.Unknown;
		}
		
		if(top){
			result.type="top";
		}else{
			result.type="leaf";
		}
		result.section=section;
		result.dataSourceUID=dataSourceUID;
		
		if(parentItem!=null){
			Reference itemRef = new Reference();
	    	itemRef._reference="" + result.id;
	    	parentItem.addChild(itemRef);
	    	
		}
		
		if(tree!=null){
			if(!tree.items.contains(result))
			{
				tree.items.add(result);
			}
		}
	
		if(dstArray!=null){
			if(!dstArray.contains(result))
			{
				dstArray.add(result);
			}
		}
		
		return result;
	}
	
	public static String getDataSourceLabel(DataSourceBase ds)
	{
		String result="";
		
		String type = ds.getType();
		
		if(type.equals("JoomlaMySQL"))
		{
			result = ds.getHost() + "("+ds.getDatabase()  +")"; 				
		}
		
		if(type.equals("XAppConnect"))
		{
			result = ds.getUrl() + " (XConnect)"; 			
			
			String host= ds.getUrl();
			 try {
				URI uri = new URI(ds.getUrl());
				if(uri.getHost() !=null)
				{
					host = uri.getHost();	
				}
			} catch (URISyntaxException e) 
			{
				e.printStackTrace();
			}
			result = host + " (XConnect)";
		}
		
		if(type.equals("JoomlaXML"))
		{
			String host= ds.getHost();
			 try {
				URI uri = new URI(ds.getHost());
				if(uri.getHost() !=null)
				{
					host = uri.getHost();	
				}
			} catch (URISyntaxException e) 
			{
				e.printStackTrace();
			}
			 
			result = host + "(XML-RPC)"; 				
		}
		if(type.equals("WordpressMySQL"))
		{
			result = ds.getHost() + "(MySQL)"; 				
		}
		if(type.equals("Liferay"))
		{
			result = "Content"; 				
		}
		if(type.equals("WordpressXML")){
			String host = ds.getHost();
			try {
				host= DBConnectionChecker.getHostName(host);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			result = host + " (XML)"; 
		}

		if(type.equals("Youtube") || type.equals("Picassa") || type.equals("Calendar") || type.equals("Documents"))
		{
			result = ds.getType() + " - " + ds.getUrl();
		}
		
		result=result.replace("www.", "");
		
		return result;
	}
	

}
