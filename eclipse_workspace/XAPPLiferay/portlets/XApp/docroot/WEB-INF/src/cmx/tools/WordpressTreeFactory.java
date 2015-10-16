package cmx.tools;

import java.io.IOException;
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
import pmedia.types.Category;
import pmedia.types.Domain;
import pmedia.utils.ECMContentSourceTypeTools;
import cmx.cache.DataSourceCache;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ContentTree;
import cmx.types.ContentTreeItem;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import cmx.types.Reference;
import cmx.types.XCDataSource;

public class WordpressTreeFactory  extends ContentTreeFactoryBase
{	
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
	public static ArrayList<ContentTreeItem>addTreeItemsByParent(ArrayList<Category> cats,ArrayList<ArticleData>posts,int parent,ContentTree tree,DataSourceBase dataSource)
	{
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		
   	
    	for (int s = 0; s < cats.size() ; s++) 
		{
    		Category c = cats.get(s);
	    	ArrayList<ArticleData >categoryArticles = ArticleTools.getArticlesByRefGroup(posts, c.refId);
	    	if(categoryArticles==null )
	    	{
	    		categoryArticles = ArticleTools.getArticlesByRefGroupStr(posts, c.title);
	    	}

	    	if(categoryArticles!=null && categoryArticles.size()==0)
	    	{
	    		continue;
	    	}
	    	
	    	ContentTreeItem cItem = new ContentTreeItem();
	    	cItem.name = c.title;
	    	cItem.contentType = "" + ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.WordpressCategory);
	    	cItem.dataSourceUID=dataSource.uid;
	    	cItem.id = c.getTreeUid();
	    	cItem.uid = c.refId;
	    	result.add(cItem);
	    	tree.items.add(cItem);
	    	cItem.type = "leaf";
	    
	    	/***
	    	 * add articles
	    	 */
	    											
	    	
	    	if(categoryArticles!=null && categoryArticles.size() > 0)
	    	{
	    		for (int ai = 0; ai < categoryArticles.size() ; ai++) 
				{
	    			ArticleData article = categoryArticles.get(ai);
		    		
	    			ContentTreeItem articleItem = new ContentTreeItem();
	    			articleItem.name = article.title;
	    			articleItem.id = article.getTreeUid() + "::" +cItem.getId();
	    			articleItem.uid = article.refId;
	    			articleItem.label = article.title;
	    			articleItem.contentType = ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.WordpressPost);
	    			articleItem.dataSourceUID=dataSource.uid;
			    	result.add(articleItem);
			    	tree.items.add(articleItem);

			    	articleItem.type = "item";
			    	articleItem.setChildren(null);
			    	articleItem.children = new ArrayList<Reference>();
			    	
			    	Reference articleRef = new Reference();
    		    	articleRef._reference="" + articleItem.id;
    		    	cItem.addChild(articleRef);
				}
	    	}
	    	/***
	    	 * add cat to section 
	    	 */
	    	Reference ref = new Reference();
	    	ref._reference="" + c.refId;
	    	ref._reference="" + cItem.id;
		}
		
		return result;
	}
	public static ContentTree createContentTree(ApplicationManager appManager, Application app, String dataSourceUid) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		tree.id="1";
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList cats =dsc.getByType(ECMContentSourceType.WordpressCategory);
		ArrayList posts =dsc.getByType(ECMContentSourceType.WordpressPost);
		if(cats==null)
		{
			return null;
		}
		if(cats!=null && posts!=null)
		{
			WordpressTreeFactory.addTreeItemsByParent(cats,posts, 1, tree,dataSource);
		}
		
		return tree;
	}
	public static ArrayList<ContentTreeItem>addArticles(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree)
	{
		if(dataSource==null){
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = WordpressTreeFactory.createContentTree(appManager, app, dataSource.uid);
		} catch (ParserConfigurationException e) 
		{
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
    	rootItem.name = "Categories"; 
    	String uuid = UUID.randomUUID().toString();
    	rootItem.id = uuid;
    	//rootItem.id = "55e969d6-c588-4268-a8b7-45240999d1cc";
    	rootItem.children = new ArrayList<Reference>();
    	destinationTree.items.add(rootItem);
    	rootItem.type = "leaf";
    	rootItem.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown);
    	
    	Reference articleRef = new Reference();
    	articleRef._reference="" + rootItem.id;
    	dataSourceItem.addChild(articleRef);
    	
		for(int i = 0  ; i  <  tree.items.size() ; i++)	
		{
			
			ContentTreeItem item = tree.items.get(i);
//			String cType = ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.WordpressCategory);
			if(item.contentType !=null && item.contentType.equals(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.WordpressCategory)))
			{
				Reference ref = new Reference();
		    	ref._reference="" + item.id;
		    	rootItem.addChild(ref);
		    	//result.add(item);
		    	item.type = "leaf";
		    	//item.contentType="section";
			}
		}
		
		
		
		return tree.items;
	}
	public static ContentTree createDataSourceTree(ApplicationManager appManager, Application app, Boolean deep) throws ParserConfigurationException, SAXException, IOException, ParseException
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
    	//result.add(rootItem);
    	tree.items.add(rootItem);
    	rootItem.type = "top";
    	rootItem.contentType =""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown);
		
		for(int i = 0  ; i  <  datasources.size() ; i++)	
		{
			DataSourceBase ds = datasources.get(i);
			String dsType = ds.getType();
			
			Boolean isValid = dsType.equals("WordpressMySQL") || ds.getType().equals("XAppConnect") || dsType.equals("WordpressXML");
			if(!isValid)
			{
				continue;
			}
			
			if(ds instanceof XCDataSource){
				
				XCDataSource xcds = (XCDataSource)ds;
    			if(xcds.getIdentifier() !=null && !xcds.getIdentifier().equals("XCWordpress")){
        			continue;
    			}
    				
    		}
			
			
			
			
			
			
			
			ContentTreeItem item = new ContentTreeItem();
	    	item.name = WordpressTreeFactory.getDataSourceLabel(ds); 
	    	item.id = ""+ds.getUid();
	    	item.dataSourceUID=ds.uid;
	    	item.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.DataSourceItem);

	    	Reference ref = new Reference();
	    	ref._reference="" + item.id;
	    	rootItem.addChild(ref);
	    	result.add(item);
	    	item.type = ds.getType();
	    	
	    	/***
	    	 * XApp-Connect 
	    	 */
	    	if(item.type.equals("XAppConnect"))
    		{
    			XCDataSource xcds = (XCDataSource)ds;
    			if(xcds!=null){
    				item.xcGroup = xcds.getIdentifier();
    			}
    			item.children = new ArrayList<Reference>();
    			continue;
    		}
	    	
	    	if(deep)
	    	{
    			ArrayList<ContentTreeItem>articleItems= WordpressTreeFactory.addArticles(appManager, app, ds, item,tree);
	    		if(articleItems!=null)
	    		{
	    			tree.items.addAll(articleItems);
	    		}
	    		
	    	}
		}
		//tree.setItems(result);
		
		return tree;
	}
	public static ContentTree createMostetCategoryTree(ApplicationManager appManager, Application app, String dataSourceUid) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		String dbPath = appManager.getUserAppPath(app.getUserIdentifier(), app.getApplicationIdentifier());
		if(dataSource!=null)
		{
			dbPath+="datasources/" + dataSource.getHost()+"/" + dataSource.getDatabase() + "/";
			dbPath+="mosetCategories.xml";
		}
		
		Domain domain = Cache.getDomain(app.applicationIdentifier);
		
		ArrayList<Category> cats = ServerCache.getInstance().getDC(app.applicationIdentifier).mosetCategories;
		if(cats==null){
			
			cats = CategoryTools.readMosetCategoriesFromFile(dbPath, "com_mtree",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MosetTreeCategory));
			if(cats!=null && cats.size() > 0){
				ServerCache.getInstance().getDC(app.applicationIdentifier).mosetCategories=cats;
			}
		}
      	
		if(cats!=null)
		{
			JoomlaContentTreeFactory.addTreeItemsByParent(cats,0,0,tree);
		}
		
		return tree;
	}
	
	public static ContentTree createJoomlaBannerCategoryTree(ApplicationManager appManager, Application app, String dataSourceUid) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		Domain domain = Cache.getDomain(app.applicationIdentifier);
		ArrayList cats =app.getDataListByType(appManager,ECMContentSourceType.JoomlaBannerCategory, dataSource);
		if(cats!=null)
		{
			JoomlaContentTreeFactory.addTreeItemsByParent(cats,0,0,tree);
		}
		
		return tree;
	}
}
