package cmx.tools;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pmedia.DataManager.Cache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.BaseDataArrayTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.LocationArrayTools;
import pmedia.types.ArticleData;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.Category;
import pmedia.types.Domain;
import pmedia.types.LocationData;
import pmedia.types.PMDataTypes;
import pmedia.utils.BitUtils;
import pmedia.utils.CListItemTools;
import pmedia.utils.ECMContentSourceTypeTools;
import cmx.cache.DataSourceCache;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ContentTree;
import cmx.types.ContentTreeItem;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import cmx.types.Reference;

public class JEventsContentTreeFactory extends ContentTreeFactoryBase
{	
	
	
	
	public static ArrayList<ContentTreeItem>addTreeItemsRecursive(
			ArrayList<Category> cats,
			ArrayList<LocationData>locations,
			int parent,
			ContentTree tree,
			DataSourceBase dataSource,
			String contentType,
			ArrayList<ContentTreeItem>dstArray,
			ContentTreeItem parentItem,
			long flags)
	{
		ArrayList<ContentTreeItem> result = dstArray;
		
		if(result==null){
			result = new ArrayList<ContentTreeItem>();
		}
		
		
		
    	ArrayList<Category>catItems = CategoryTools.getCategoriesByParentIndex(cats, parent);
    	
    	for (int s = 0; s < catItems.size() ; s++) 
		{
		    Category c = catItems.get(s);
		    if(c.published)
		    {
		    	
		    	/***
		    	 * Add the category
		    	 */
		    	ContentTreeItem item = ContentTreeFactoryBase.createTreeItemWithDataEx(
		    			c, 
		    			null, 
		    			""+contentType, 
		    			dataSource.getUid(),
		    			parentItem,
		    			tree,
		    			result);

		    	
		    	ArrayList<LocationData >categoryArticles = BaseDataArrayTools.getByGroupIndex(locations, c.refId);
		    	if(categoryArticles!=null && categoryArticles.size()==0){
		    		//continue;
		    	}
		    	
		    	/***
		    	 * add articles
		    	 */
		    	if(categoryArticles!=null && categoryArticles.size() > 0)
		    	{
		    		/*
		    		for (int ai = 0; ai < categoryArticles.size() ; ai++) 
					{
		    			ArticleData article = categoryArticles.get(ai);
		    			if(article.published){
		    				
		    				ContentTreeItem articleItem =  ContentTreeFactoryBase.createTreeItemWithDataEx(
				    			article, 
				    			null, 
				    			""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaArticle), 
				    			dataSource.getUid(),
				    			item,tree,result);
		    				
		    			}
					}
		    		
		    		*/
		    	}
		    	
		    	/**
		    	 * Recursion for categories
		    	 */
		    	c.hasSubCategories = CategoryTools.hasSubCategories(cats, c.refId);

		    	if(c.hasSubCategories)
		    	{
		    		ArrayList<Category>subItems = CategoryTools.getCategoriesByParentIndex(cats,c.refId);
		    		for (int j = 0; j < subItems.size() ; j++) 
		    		{
		    		    Category subItem = subItems.get(j);
		    		    if(subItem.published)
		    		    {
		    		    	Reference ref = new Reference();
		    		    	ref._reference="" + subItem.getTreeUid();
		    		    	item.addChild(ref);
		    		    }
		    		}
		    		
		    		addTreeItemsRecursive(
		    				cats,
		    				locations,
		    				c.refId,
		    				tree,
		    				dataSource,
		    				""+contentType,
		    				result,
		    				item,
		    				-1);
		    		
		    	}
		    }
		}

		return result;
	}
	
	public static ArrayList<ContentTreeItem>addJEventsCategories(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree,long flags)
	{
		if(dataSource==null){
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = JEventsContentTreeFactory.createJEventsCategoryTree(appManager, app, dataSource.uid,flags);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tree==null){
			return null;
		}
	
		///add it to items : 
		ContentTreeItem rootItem = createDummyTreeItemEx("JEvents Categories",ECMContentSourceType.JEventsCategory,dataSourceItem, tree, null);
		rootItem.type="JEvents";		
		rootItem.uid=-1;
		rootItem.dataSourceUID=dataSource.getUid();
		
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList<Category> cats = dsc.getByType(ECMContentSourceType.JEventsCategory);
		if(cats!=null && cats.size() > 0)
		{
			ArrayList<Category> topLevelItems = null;
			if(dataSource.getVersion().equals("1.5")){
				topLevelItems=getTopLevelItems(cats,0);	
			}else{
				topLevelItems=getTopLevelItems(cats,1);
			}
			
			ContentTreeFactoryBase.addToTreeItemFiltered(tree.items, rootItem, ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCategory), null,topLevelItems);
			
			for(int i = 0  ; i  <  tree.items.size() ; i++)	
			{
				//ContentTreeItem item = tree.items.get(i);
	
			}
		}
		return tree.items;
	}
	
	
	public static ArrayList<ContentTreeItem>addJEventLocationCategories(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree,long flags)
	{
		if(dataSource==null){
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = JEventsContentTreeFactory.createJEventsLocationCategoryTree(appManager, app, dataSource.uid,flags);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tree==null){
			return null;
		}
	
		///add it to items : 
		ContentTreeItem rootItem = createDummyTreeItemEx("JEvents Locations",ECMContentSourceType.JEventsLocationCategory, dataSourceItem, tree, null);
		rootItem.type="JEvents";
		rootItem.uid=-1;
		rootItem.dataSourceUID=dataSource.getUid();
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList<Category> cats = dsc.getByType(ECMContentSourceType.JEventsLocationCategory);
		if(cats!=null && cats.size() > 0)
		{
			ArrayList<Category> topLevelItems = getTopLevelItems(cats,0);
			ContentTreeFactoryBase.addToTreeItemFiltered(tree.items, rootItem, ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationCategory), null,topLevelItems);
			
			for(int i = 0  ; i  <  tree.items.size() ; i++)	
			{
				//ContentTreeItem item = tree.items.get(i);
	
			}
		}
		return tree.items;
	}
	
	public static ContentTree createJEventsLocationCategoryTree(ApplicationManager appManager, Application app, String dataSourceUid,long flags) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList cats = dsc.getByType(ECMContentSourceType.JEventsLocationCategory);
		ArrayList locations = dsc.getByType(ECMContentSourceType.JEventsLocationItem);
		if(cats!=null)
		{
			addTreeItemsRecursive(
					cats, 
					locations, 
					0, 
					tree, 
					dataSource,
					""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationCategory), 
					null, 
					null,
					-1);
			
		}
		
		
		return tree;
	}
	
	public static ContentTree createJEventsCategoryTree(ApplicationManager appManager, Application app, String dataSourceUid,long flags) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		
		ContentTree tree = new ContentTree();
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList cats = dsc.getByType(ECMContentSourceType.JEventsCategory);
		if(cats!=null)
		{
			if(dataSource.getVersion().equals("1.5")){
			addTreeItemsRecursive(
					cats, 
					null, 
					0, 
					tree, 
					dataSource,
					""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCategory), 
					null, 
					null,
					-1);
			}else{
				addTreeItemsRecursive(
						cats, 
						null, 
						1, 
						tree, 
						dataSource,
						""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCategory), 
						null, 
						null,
						-1);	
			}
			
		}
		return tree;
	}
	

}
