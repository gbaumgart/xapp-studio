package cmx.tools;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

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
import cmx.cache.DataSourceCache;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ContentTree;
import cmx.types.ContentTreeItem;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import cmx.types.Reference;

public class LiferayContentTreeFactory  extends ContentTreeFactoryBase
{	
	
	public static ArrayList<ContentTreeItem>addTreeItemsRecursive(
			String uuid,
			String appId,
			List<AssetCategory>cats,
			ArrayList<JournalArticle>articles,
			int parent,
			ContentTree tree,
			DataSourceBase dataSource,
			ECMContentSourceType catType,
			ECMContentSourceType contentType,
			ArrayList<ContentTreeItem>dstArray,
			ContentTreeItem parentItem,
			long flags,
			String type)
			{
	
		

		ArrayList<ContentTreeItem> result = dstArray;
		
		if(result==null){
			result = new ArrayList<ContentTreeItem>();
		}
		
    	//List<AssetCategory>catItems = LiferayContentTools.getCategoriesByType(uuid, appId, parent, type,null);
		//List<AssetCategory>catItems = LiferayContentTools.getCategoriesByParentIndex(srcCategories, index)
		List<AssetCategory>catItems=null;
		
		try {
			catItems= AssetCategoryLocalServiceUtil.getChildCategories(parent);
		} catch (SystemException e1) {
			// TODO Auto-generated catch block
			return dstArray;
		}
		if(catItems==null){
			return dstArray;
		}
		
    	for (int s = 0; s < catItems.size() ; s++) 
		{
    		AssetCategory c = catItems.get(s);
    		int cP = (int)c.getParentCategoryId();
    		if(cP!=parent){
    			continue;
    		}
    		boolean pub=true;
		    if(pub)
		    {
		    	
		    	Category bc = LiferayContentTools.toBaseData(c);
		    	if(bc.getTitle().equals("subsub"))
		    	{
		    		System.out.println("subsub");
		    	}
		    	/***
		    	 * Add the category
		    	 */
		    	ContentTreeItem item = ContentTreeFactoryBase.createTreeItemWithDataEx(
		    			bc, 
		    			null, 
		    			""+ECMContentSourceTypeTools.TypeToInteger(catType), 
		    			dataSource.getUid(),
		    			parentItem,
		    			tree,
		    			result);

		    	//4294967296  cats   | all flags	6442450944	
		    	Boolean hasArticles = BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(contentType));
		    	if(hasArticles){
			    	ArrayList<JournalArticle >categoryArticles = null;
			    	
			    	if(articles !=null && articles.size() > 0){
				    	categoryArticles=LiferayContentTools.getCategoryArticles(uuid,appId,bc.refId);
			    	}
			    	
			    	/***
			    	 * add articles
			    	 */
			    	if(categoryArticles!=null && categoryArticles.size() > 0)
			    	{
			    		for (int ai = 0; ai < categoryArticles.size() ; ai++) 
						{
			    			JournalArticle article = categoryArticles.get(ai);
			    			boolean apub=true;
			    			if(apub)
			    			{
			    				
			    				ContentTreeItem articleItem =  ContentTreeFactoryBase.createTreeItemWithDataEx(
			    				    LiferayContentTools.toBaseData(uuid,appId,articles,article,bc.refId), 
					    			null, 
					    			""+ECMContentSourceTypeTools.TypeToInteger(contentType), 
					    			dataSource.getUid(),
					    			item,tree,result);
			    				
			    			}
						}
			    	}
		    	}
		    	
		    	
		    	
	
		    	
		    	/**
		    	 * Recursion for categories
		    	 */
		    	List<AssetCategory>subCats = null; 
		    	try {
					subCats = AssetCategoryLocalServiceUtil.getChildCategories(bc.refId);
				} catch (SystemException e) 
				{
					e.printStackTrace();
				}
				if(subCats!=null && subCats.size()>0)
				{
					bc.hasSubCategories=true;
				}
				
				if(subCats==null){
					return result;
				}
		    	//bc.hasSubCategories = LiferayContentTools.hasSubCategories(cats, bc.refId);
		    	
		    	if(bc.hasSubCategories)
		    	{
		    		//System.out.println("\t Recursion"  + c.title);
		    		
		    		/***
		    		 * 
		    		 */
		    		List<AssetCategory>subItems = subCats;//LiferayContentTools.getCategoriesByParentIndex(cats,bc.refId);
		    		for (int j = 0; j < subItems.size() ; j++) 
		    		{
		    		    
		    			AssetCategory subItem = subItems.get(j);
		    			
	    		    	Reference ref = new Reference();
	    		    //	ref._reference="" + UUID.randomUUID().toString();
	    		    //	item.addChild(ref);
	    		    	/*
	    		    	LiferayContentTreeFactory.addTreeItemsRecursive(
			    				uuid,
			    				appId,
			    				cats,
			    				articles,
			    				(int)subItem.getCategoryId(),
			    				tree,
			    				dataSource,
			    				""+ECMContentSourceType.StaticWebContentCategory,
			    				result,
			    				item,
			    				-1,
			    				type);
	    		    	*/
	    		    	
	    		    	//ref._reference="" + subItem.getTreeUid();
	    		    	
		    		    
		    		    
		    		}
		    		
		    		LiferayContentTreeFactory.addTreeItemsRecursive(
		    				uuid,
		    				appId,
		    				cats,
		    				articles,
		    				bc.refId,
		    				tree,
		    				dataSource,
		    				catType,
		    				contentType,
		    				result,
		    				item,
		    				-1,
		    				type);
		    		//JoomlaContentTreeFactoryNew.addTreeItemsByParent(cats,articles,c.refId, tree,dataSource,""+ECMContentSourceType.JoomlaCategory);
		    		
		    	}
		    }
		}

		return result;
		
	}
	
	/***
	 * 
	 * @param appManager
	 * @param app
	 * @param dataSource
	 * @param dataSourceItem
	 * @param destinationTree
	 * @return
	 */
	public static ArrayList<ContentTreeItem>addAllFiltered(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree,long flags)
	{
		if(dataSource==null){
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = LiferayContentTreeFactory.createContentTreeArticles(appManager, app, dataSource.uid,flags);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tree==null)
		{
			return null;
		}

		///add it to items : 
		ContentTreeItem rootItem = createDummyTreeItem("Articles", dataSourceItem, destinationTree, null);
		rootItem.type="parent";
		//item.contentType.equals(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.StaticWebContent)) ||
		for(int i = 0  ; i  <  tree.items.size() ; i++)	
		{
			ContentTreeItem item = tree.items.get(i);
			if(item.contentType !=null && 
					item.contentType.equals(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.StaticWebContentCategory)))
			{
				Reference ref = new Reference();
		    	ref._reference="" + item.id;
		    	rootItem.addChild(ref);
		    	item.type = "leaf";
			}
		}
		return tree.items;
	}
	
	public static ContentTree createContentTreeArticles(ApplicationManager appManager, Application app, String dataSourceUid,long flags) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		LiferayContentTools.prepareUser(app.getUserIdentifier(), app.getApplicationIdentifier());
		
		ArrayList articles = LiferayContentTools.getArticlesByUserId(app.getUserIdentifier(), -1);
		
		List<AssetCategory>cats = LiferayContentTools.getCategoriesByType(app.getUserIdentifier(),app.getApplicationIdentifier(),0,"Articles",null);
		
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		ContentTree tree2 = new ContentTree();
		tree2.id="1";
		
		AssetCategory cat=LiferayContentTools.getDefaultCategory(app.getUserIdentifier(), app.getApplicationIdentifier(), 0, "Articles");
		if(cat!=null){
		
			LiferayContentTreeFactory.addTreeItemsRecursive(app.getUserIdentifier(),app.getApplicationIdentifier(),cats, articles,(int)cat.getCategoryId(), tree2, dataSource,ECMContentSourceType.StaticWebContentCategory,ECMContentSourceType.StaticWebContent,null,null,flags,"Articles");
		}
		return tree2;
	}
	
	public static ContentTree createContentTreeVenues(ApplicationManager appManager, Application app, String dataSourceUid,long flags) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		LiferayContentTools.prepareUser(app.getUserIdentifier(), app.getApplicationIdentifier());
		
		ArrayList articles = LiferayContentTools.getArticlesByUserId(app.getUserIdentifier(), -1);
		
		List<AssetCategory>cats = LiferayContentTools.getCategoriesByType(app.getUserIdentifier(),app.getApplicationIdentifier(),0,"Locations",null);
		
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		ContentTree tree2 = new ContentTree();
		tree2.id="1";
		
		AssetCategory cat=LiferayContentTools.getDefaultCategory(app.getUserIdentifier(), app.getApplicationIdentifier(), 0, "Locations");
		if(cat!=null){
			LiferayContentTreeFactory.addTreeItemsRecursive(app.getUserIdentifier(),app.getApplicationIdentifier(),cats, articles,(int)cat.getCategoryId(), tree2, dataSource,ECMContentSourceType.StaticWebContentVenueCategory,ECMContentSourceType.StaticWebContentVenue,null,null,flags,"Locations");
		}
		return tree2;
	}

	/***
	 * 
	 * @param appManager
	 * @param app
	 * @param dataSource
	 * @param dataSourceItem
	 * @param destinationTree
	 * @return
	 */
	public static ArrayList<ContentTreeItem>addArticles(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree,long flags)
	{
		if(dataSource==null){
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = LiferayContentTreeFactory.createContentTreeArticles(appManager, app, dataSource.uid,flags);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(tree==null)
		{
			return null;
		}

		
		/***
		 * Articles
		 */
		///add it to items : 
		ContentTreeItem rootItem = createDummyTreeItem("Articles", dataSourceItem, destinationTree, null);
		rootItem.type="parent";
		AssetCategory cat=LiferayContentTools.getDefaultCategory(app.getUserIdentifier(), app.getApplicationIdentifier(), 0, "Articles");
		rootItem.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.StaticWebContentCategory);
		rootItem.dataSourceUID="Liferay";
		if(cat!=null){
			for(int i = 0  ; i  <  tree.items.size() ; i++)	
			{
				rootItem.uid=(int)cat.getCategoryId();
				//item.contentType.equals(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.StaticWebContent))
				ContentTreeItem item = tree.items.get(i);
				if(item.contentType !=null && item.contentType.equals(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.StaticWebContentCategory)))
				{
					AssetCategory scat=null;
			    	try {
						scat =AssetCategoryLocalServiceUtil.getAssetCategory(item.getUid());
					} catch (PortalException e) {
						e.printStackTrace();
					} catch (SystemException e) {

						e.printStackTrace();
					}

			    	if(cat!=null && scat!=null && scat.getParentCategoryId() == cat.getCategoryId())
			    	{
						Reference ref = new Reference();
				    	ref._reference="" + item.id;
				    	rootItem.addChild(ref);
				    	item.type = "leaf";
			    	}
			    				    	
			    	
			    	//break;
				}
			}
		}
		
		/***
		 * Locations
		 */
		//
		
		ContentTree treeLocs = null ; 
		try {
			treeLocs = LiferayContentTreeFactory.createContentTreeVenues(appManager, app, dataSource.uid,flags);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ContentTreeItem rootItemLocs = createDummyTreeItem("Locations", dataSourceItem, destinationTree, null);
		
		rootItemLocs.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.StaticWebContentVenueCategory);
		
		rootItemLocs.dataSourceUID="Liferay";
		
		rootItemLocs.type="parent";
		
		AssetCategory catLoc=LiferayContentTools.getDefaultCategory(app.getUserIdentifier(), app.getApplicationIdentifier(), 0, "Locations");
		
		if(cat!=null){
			
			rootItemLocs.uid=(int)catLoc.getCategoryId();
			//rootItem.id=UUID.randomUUID().toString();
			
			for(int i = 0  ; i  <  treeLocs.items.size() ; i++)	
			{
				ContentTreeItem item = treeLocs.items.get(i);
				if(item.contentType !=null && item.contentType.equals(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.StaticWebContentVenueCategory)))
				{
					
					
					AssetCategory scat=null;
			    	try {
						scat =AssetCategoryLocalServiceUtil.getAssetCategory(item.getUid());
					} catch (PortalException e) {
						e.printStackTrace();
					} catch (SystemException e) {

						e.printStackTrace();
					}

			    	if(catLoc!=null && scat!=null && scat.getParentCategoryId() == catLoc.getCategoryId())
			    	{
						Reference ref = new Reference();
				    	ref._reference="" + item.id;
				    	rootItemLocs.addChild(ref);
				    	item.type = "leaf";
			    	}
			    	
					/*
					if(i==0){
						Reference ref = new Reference();
				    	ref._reference="" + item.id;
				    	rootItemLocs.addChild(ref);
				    	item.type = "leaf";
					}
					*/
				}
				
				
				
				tree.items.add(item);
			}
		}
		
		return tree.items;
	}
	
	public static String getDataSourceLabel(DataSourceBase ds)
	{
		String result="";
		
		String type = ds.getType();
		
		if(type.equals("Liferay"))
		{
			result = "Content"; 				
		}
		if(type.equals("JoomlaMySQL"))
		{
			result = ds.getHost() + " ( Joomla - MySQL"  + " :: " + ds.getDatabase()  +")"; 				
		}
		if(type.equals("WordpressMySQL"))
		{
			result = ds.getHost() + " ( Wordpress - MySQL"  + " :: " + ds.getDatabase()  +")"; 				
		}
		if(type.equals("WordpressXML")){
			String host = ds.getHost();
			try {
				host= DBConnectionChecker.getHostName(host);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			result = host + " ( Wordpress - XML)"; 
		}
		
		if(type.equals("Youtube") || type.equals("Picassa") || type.equals("Calendar"))
		{
			result = ds.getType() + " - " + ds.getUrl();
		}
		
		return result;
	}
	
	public static ContentTree createDataSourceTree(ApplicationManager appManager, Application app, long flags) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		tree.id="1";
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		tree.setItems(result);
		
		ArrayList<DataSourceBase>datasources = app.getDataSources();
		
		ContentTreeItem rootItem = new ContentTreeItem();
    	rootItem.name = "Data Sources"; 
    	rootItem.label= "Data Sources";
    	rootItem.id = "9898989";
    	rootItem.children = new ArrayList<Reference>();
    	tree.items.add(rootItem);
    	rootItem.type = "top";
    	rootItem.contentType = ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown);
		
    	Boolean hasCats = BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.StaticWebContentCategory));
    	Boolean hasContent = BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.StaticWebContent));
		for(int i = 0  ; i  <  datasources.size() ; i++)	
		{
			DataSourceBase ds = datasources.get(i);
			if(!ds.getType().equals("Liferay"))
			{
				continue;
			}
			ContentTreeItem item = new ContentTreeItem();
			

			item.name  = LiferayContentTreeFactory.getDataSourceLabel(ds);
	    	item.id = ""+ds.getUid();
	    	item.dataSourceUID=ds.uid;
	    	item.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown);
	    	
	    	
	    	Reference ref = new Reference();
	    	ref._reference="" + item.id;
	    	rootItem.addChild(ref);
	    	result.add(item);
	    	item.type = ds.getType();
			
	    	if(hasContent || hasCats)
	    	{
	    		ArrayList<ContentTreeItem>articleItems= LiferayContentTreeFactory.addArticles(appManager, app, ds, item,tree,flags);
	    		if(articleItems!=null)
	    		{
	    			tree.items.addAll(articleItems);
	    		}
	    	}
		}
		//tree.setItems(result);
		
		return tree;
	}

}
