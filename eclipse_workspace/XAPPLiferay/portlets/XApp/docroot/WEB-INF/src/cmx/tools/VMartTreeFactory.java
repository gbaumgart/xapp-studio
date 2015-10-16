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

public class VMartTreeFactory extends JoomlaContentTreeFactory
{	
	public static ArrayList<ContentTreeItem>addVMartTree(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree)
	{
		if(dataSource==null)
		{
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = VMartTreeFactory.createVMartCategoryTree(appManager, app, dataSource.uid);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		} catch (ParseException e) 
		{
			e.printStackTrace();
		}

		
		if(tree==null)
		{
			return null;
		}
	
		
		ContentTreeItem rootItem = createDummyTreeItemEx("Virtue Mart Categories",ECMContentSourceType.VMartCategory,dataSourceItem, tree, null);
		rootItem.type="VirtueMart";		
		rootItem.uid=-1;
		rootItem.dataSourceUID=dataSource.getUid();
		
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList<Category> cats = dsc.getByType(ECMContentSourceType.VMartCategory);
		if(cats!=null && cats.size() > 0)
		{
			ArrayList<Category> topLevelItems = topLevelItems=getTopLevelItems(cats,0);	

			ContentTreeFactoryBase.addToTreeItemFiltered(tree.items, rootItem, ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.VMartCategory), null,topLevelItems);
		}
		return tree.items;
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
	
	public static ContentTree createVMartCategoryTree(ApplicationManager appManager, Application app, String dataSourceUid) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList<Category> cats = dsc.getByType(ECMContentSourceType.VMartCategory);
		
		if(cats!=null && cats.size() > 0)
		{
			addTreeItemsRecursive(
					cats, 
					0, 
					tree, 
					dataSource,
					""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.VMartCategory), 
					null, 
					null,
					-1);	
		}else{
			return null;
		}
		
		return tree;
	}
}
