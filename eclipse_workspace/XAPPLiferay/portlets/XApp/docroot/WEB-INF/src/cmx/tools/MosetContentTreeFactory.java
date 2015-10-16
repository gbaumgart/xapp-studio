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
import pmedia.DataUtils.CategoryTools;
import pmedia.types.ArticleData;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.Category;
import pmedia.types.Domain;
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

public class MosetContentTreeFactory extends JoomlaContentTreeFactory
{	
	public static ArrayList<ContentTreeItem>addMosetTree(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree)
	{
		if(dataSource==null){
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = MosetContentTreeFactory.createMostetCategoryTree(appManager, app, dataSource.uid);
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
		ContentTreeItem rootItem = new ContentTreeItem();
    	rootItem.name = "Moset Tree Categories"; 
    	String uuid = UUID.randomUUID().toString();
    	rootItem.id = uuid;
    	rootItem.children = new ArrayList<Reference>();
    	destinationTree.items.add(rootItem);
    	rootItem.type = "leaf";
    	rootItem.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MosetTreeCategory);
    	
    	Reference articleRef = new Reference();
    	articleRef._reference="" + rootItem.id;
    	dataSourceItem.addChild(articleRef);
    	
    	
    	String dbPath = appManager.getUserAppPath(app.getUserIdentifier(), app.getApplicationIdentifier());
		if(dataSource!=null)
		{
			dbPath+="datasources/" + dataSource.getHost()+"/" + dataSource.getDatabase() + "/";
			dbPath+="mosetCategories.xml";
		}
		
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList<Category> cats = dsc.getByType(ECMContentSourceType.MosetTreeCategory);
		
    	
    	ArrayList<Category> topLevelItems = getTopLevelItems(cats);
		for(int i = 0  ; i  <  tree.items.size() ; i++)	
		{
			ContentTreeItem item = tree.items.get(i);
			item.contentType = "" + ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MosetTreeCategory);
			item.dataSourceUID = dataSource.uid;
			if(item.contentType !=null && item.contentType.equals(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MosetTreeCategory)))
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
		    	rootItem.addChild(ref);
		    	item.type = "leaf";
			}
		}
		return tree.items;
	}
	
	public static ContentTree createMosetTree(ApplicationManager appManager, Application app, String dataSourceUid) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		tree.id="1";
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList sections = null;
		if(dataSource.getVersion().equals("1.5")){
			sections = dsc.getByType(ECMContentSourceType.JoomlaSection);
		}
		ArrayList cats =dsc.getByType(ECMContentSourceType.JoomlaCategory);
		ArrayList articles =dsc.getByType(ECMContentSourceType.JoomlaArticle);
		if(articles==null){
			return null;
		}
		if(cats!=null)
		{
			if(dataSource.getVersion().equals("1.7")){
				MosetContentTreeFactory.addTreeItemsByParent(cats, 1, tree);
			}else
			{
				MosetContentTreeFactory.addTreeItemsByParent(cats,sections,articles,0,tree,dataSource);
			}
		}
		
		return tree;
	}
	public static ArrayList<ContentTreeItem>addTreeItemsRecursive(
			ArrayList<Category> cats,
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
	public static ContentTree createMostetCategoryTree(ApplicationManager appManager, Application app, String dataSourceUid) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList<Category> cats = dsc.getByType(ECMContentSourceType.MosetTreeCategory);
		if(cats!=null)
		{
			//MosetContentTreeFactory.addTreeItemsByParent(cats,0,0,tree);
			addTreeItemsRecursive(
					cats, 
					0, 
					tree, 
					dataSource,
					""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MosetTreeCategory), 
					null, 
					null,
					-1);	
		}
		
		return tree;
	}
}
