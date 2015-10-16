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
import xappconnect.Utils.CustomTypeUtils;
import xappconnect.types.CustomType;
import cmx.types.ContentTree;
import cmx.types.ContentTreeItem;
import cmx.types.CustomTypeTree;
import cmx.types.ECMContentSourceType;
import cmx.types.Reference;

public class CustomTypeTreeFactory 
{	
	public static ArrayList<ContentTreeItem>addTreeItemsRecursive(
			ArrayList<CustomType>items,
			String parent,
			ContentTree tree,
			ArrayList<ContentTreeItem>dstArray,
			ContentTreeItem parentItem,
			long flags)
	{
		
		ArrayList<ContentTreeItem> result = dstArray;
		
		ArrayList<CustomType>childItems = null ;//getTopLevelItems(items,parent);
		
		if(parent==null){
			 childItems = getTopLevelItems(items);
		}else if(parent.length() > 0){
			childItems= getChildren(items, parent);
		}
		
		for (int s = 0; s < childItems.size() ; s++) 
		{
		    CustomType c = childItems.get(s);
		    ContentTreeItem cTreeItem = null;
		    if(c.getEnabled())
		    {
		    	cTreeItem = createTreeItemWithDataEx(c, parentItem, tree, result);
		    }
		    
		    String lookup = "";
		    
		    //if(c.getFolder()){
		    	
		    lookup = c.getName().replace(" ", "");
		    //}
		    ArrayList<CustomType>subs = getChildren(items, lookup);
		    if(subs!=null && subs.size() > 0)
		    {		    	
		    	addTreeItemsRecursive(
						items, 
						lookup, 
						tree, 
						null, 
						cTreeItem,
						-1);
		    }
		    
		}

		
		return result;
		
		/*
    	c.hasSubCategories = CategoryTools.hasSubCategories(cats, c.refId);
    	if(c.title.equals("Joomla!"))
    	{
	    	//System.out.println("---|||| Joomla"  + c.title);
	    }
    	if(c.hasSubCategories)
    	{
    		
    		ArrayList<Category>subItems = CategoryTools.getCategoriesByParentIndex(cats,c.refId);
    		for (int j = 0; j < subItems.size() ; j++) 
    		{
    		    Category subItem = subItems.get(j);
    		    if(subItem.title.equals("Joomla!")){
    		    	//System.out.println("---"  + subItem.title);
    		    }
    		    if(subItem.published)
    		    {
    		    	
    		    	Reference ref = new Reference();
    		    	ref._reference="" + subItem.getTreeUid();
    		    	item.addChild(ref);
    		    }
    		    
    		}
    	
    		addTreeItemsRecursive(
    				cats,
    				articles,
    				c.refId,
    				tree,
    				dataSource,
    				""+ECMContentSourceType.JoomlaCategory,
    				result,
    				item,
    				-1);
    	}
    }
    */
	}

	public static ContentTree createTypeTree(ArrayList<CustomType> types)
	{		
		ContentTree tree = new ContentTree();
		tree.id="1";
		
		if(types!=null)
		{
			addTreeItemsRecursive(
					types, 
					null, 
					tree, 
					null, 
					null,
					-1);
			
		}
		
		return tree;
	}

	public static ArrayList<ContentTreeItem>addCustomTypes(ArrayList<CustomType> types,ContentTreeItem parentItem,ContentTree destinationTree)
	{
		ContentTree tree = null ; 
		tree = createTypeTree(types);
		if(tree==null){
			return null;
		}
		
		ArrayList<CustomType> topLevelItems = getTopLevelItems(types);
		addToTreeItemFiltered(tree.items, parentItem,topLevelItems);
		
		/*
		ContentTreeItem rootItem = ContentTreeFactoryBase.createDummyTreeItem("Articles", dataSourceItem, tree, null);
		
		if(dataSource.getVersion().equals("1.7"))
		{
			DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
			ArrayList<Category> cats = dsc.getByType(ECMContentSourceType.JoomlaCategory);
			ArrayList<Category> topLevelItems = getTopLevelItems(cats,1);
			ContentTreeFactoryBase.addToTreeItemFiltered(tree.items, rootItem, ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory), null,topLevelItems);
		}else if(dataSource.getVersion().equals("1.5")){
				ContentTreeFactoryBase.addToTreeItemFiltered(tree.items, rootItem, ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaSection), null);			
		}
		*/
		return tree.items;
	}
	public static CustomTypeTree getTypesTree(String rtConfig,String platform,String uuid,String appId,String scope,String flags)
	{
		CustomTypeTree tree = new CustomTypeTree();
		tree.id="1";
	
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		tree.setItems(result);
		
		if(rtConfig==null){
			rtConfig="debug";
		}
		
		

		ContentTreeItem rootItem = new ContentTreeItem();
		
		rootItem.children = new ArrayList<Reference>();
		result.add(rootItem);
		
		rootItem.contentType = ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown);
		
		rootItem.type = "top";
		rootItem.name = "Custom Types"; 
		rootItem.id = "9898989";
		
		ArrayList<CustomType>types = CustomTypeUtils.getTypes(rtConfig, platform, uuid, appId, null, scope);
		if(types!=null && types.size() > 0)
		{
			ArrayList<ContentTreeItem> treeItems = addCustomTypes(types, rootItem, tree);
			
			if(treeItems!=null && treeItems.size()>0)
			{
				tree.items.addAll(treeItems);
			}
			
		}
		return tree;
	}
	
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
	public static Boolean isTopLevelItem(ArrayList<CustomType>topLevelItems,String id)
	{
			for (int j = 0; j < topLevelItems.size() ; j++) 
    		{
				CustomType c= topLevelItems.get(j);
				if(c.getEnabled())
				{
					if(c.getName().replace(" ", "") .equals(id))
					{
						return true;
					}
				}
    		}
		return false;
	}
	
	
	public static ArrayList<CustomType>getChildren(ArrayList<CustomType>types,String rootSchema)
	{
		ArrayList<CustomType>result = new ArrayList<CustomType>();
		
		for (int j = 0; j < types.size() ; j++) 
		{
			CustomType ct= types.get(j);
			
			if(ct.getEnabled())
			{
				if(ct.getParent()!=null && ct.getParent().length() > 0 && ct.getParent().equals(rootSchema))
				{
					result.add(ct); 
				}else{
					
				}
			}
		}
		
		return result;
	}
	public static ArrayList<CustomType>getTopLevelItems(ArrayList<CustomType>types)
	{
		ArrayList<CustomType>result = new ArrayList<CustomType>();
		
		for (int j = 0; j < types.size() ; j++) 
		{
			CustomType ct= types.get(j);
			
			if(ct.getEnabled())
			{
				if(ct.getParent()!=null && ct.getParent().length() > 0)
				{
					//result.add(c); 
				}else{
					result.add(ct);
				}
			}
		}
		
		//Category topLevelItem = getTopLevelItem(cats,rootIndex);
		
		/*if(topLevelItem!=null)
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
		*/
		
		return result;
	}
	public static ArrayList<Category>getTopLevelItems2(ArrayList<Category>cats)
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
	
	public static void addToTreeItemFiltered(ArrayList<ContentTreeItem>items,ContentTreeItem dstTreeItem,ArrayList<CustomType>topLevelItems)
	{
		for(int i = 0  ; i  <  items.size() ; i++)	
		{
			ContentTreeItem item = items.get(i);
			
			if(topLevelItems!=null && topLevelItems.size() > 0)
			{
				
				if(!isTopLevelItem(topLevelItems, item.id))
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
	 * @param sourceType
	 * @param dataSourceUID
	 * @param parentItem
	 * @param tree
	 * @return
	 */
	public static ContentTreeItem createTreeItemWithDataEx(
			CustomType data,
			ContentTreeItem parentItem,
			ContentTree tree,
			ArrayList<ContentTreeItem> dstArray
			)
	{
		
		
		ContentTreeItem result = createTreeItemWithType(data);
		
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
	 * @param contentType
	 * @param dataSourceUID
	 * @param parentItem
	 * @param tree
	 * @return
	 */
	/*
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
	*/
	/*
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
	*/
	
	/***
	 * 
	 * @param data
	 * @param section
	 * @param sourceType
	 * @param dataSourceUID
	 * @return
	 */
	public static ContentTreeItem createTreeItemWithType(
			CustomType data)
	{
		
		String uid = data.getUrlSchema();
		
		/***
		 * String label,
			String id,
			String uid,
			Boolean top,
			ContentTreeItem parentItem,
			ContentTree tree,
			ArrayList<ContentTreeItem> dstArray
		 */
		
		String id=data.getUrlSchema();
		if(id.length()==0){
			id = data.getName().replace(" ", "");
		}
		
		ContentTreeItem result = createTreeItemEx(
				data.getName(), 
				id, 
				"" + uid, 
				false, 
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
	 * @param sourceType
	 * @param top
	 * @param dataSourceUID
	 * @return
	 */
	public static ContentTreeItem createTreeItemEx(
			String label,
			String id,
			String uid,
			Boolean top,
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
		

		
		if(top){
			result.type="top";
		}else{
			result.type="leaf";
		}
		
		
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
	
	

	
	
}
