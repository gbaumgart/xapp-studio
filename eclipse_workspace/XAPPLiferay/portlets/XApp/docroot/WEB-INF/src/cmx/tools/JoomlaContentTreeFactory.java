package cmx.tools;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.ddtek.xquery.xdbc.XMLDataSource;

import pmedia.DataManager.Cache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.types.ArticleData;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.Category;
import pmedia.types.Domain;
import pmedia.types.EventData;
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
import cmx.types.XASFeature;
import cmx.types.XCDataSource;

public class JoomlaContentTreeFactory extends ContentTreeFactoryBase
{	
	public static ContentTree createContentTree(ApplicationManager appManager, Application app, String dataSourceUid) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		tree.id="1";
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList sections = null;
		if(dataSource.getVersion().equals("1.5"))
		{
			sections = dsc.getByType(ECMContentSourceType.JoomlaSection);
		}
		ArrayList cats =dsc.getByType(ECMContentSourceType.JoomlaCategory);
		ArrayList articles =dsc.getByType(ECMContentSourceType.JoomlaArticle);
		if(articles==null){
			return null;
		}
		if(cats!=null)
		{
			
			Boolean includeSections=true;
			String version = dataSource.getVersion();
			
			/*
			if(dataSource instanceof XMLDataSource)
			{
				
			}
			*/
			
			if(sections ==null || sections.size()==0)
			{
				includeSections=false;
			}
			//dataSource.getVersion().equals("1.7")
			if(!includeSections)
			{
				JoomlaContentTreeFactory.addTreeItemsRecursive(
						cats, 
						articles, 
						1, 
						tree, 
						dataSource,
						""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory), 
						null, 
						null,
						-1);
				
				//JoomlaContentTreeFactory.addTreeItemsByParent(cats,articles, 1, tree,dataSource);
			}else
			{
				JoomlaContentTreeFactory.addTreeItemsByParent(cats,sections,articles,0,tree,dataSource);
			}
		}
		
		return tree;
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
		
		Boolean hasContent = BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaSection)) || 
	
	
		BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory)) || BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaArticle)) ;
		Boolean hasJEventsCategories = BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCategory));
		Boolean hasJEventsToday = BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCalendarToday));
		Boolean hasJEventsWeek = BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCalendarWeek));
		Boolean hasJEventsLocationCategory = BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationCategory));
		Boolean hasJEventsLocationItems = BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationItem));
		Boolean hasBanners= BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaBannerCategory));
		Boolean hasVMCategories= BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.VMartCategory));
		Boolean hasBForms= BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.BreezingForm));
		Boolean hasMosetCategory= BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MosetTreeCategory));
		
		
		for(int i = 0  ; i  <  datasources.size() ; i++)	
		{
			DataSourceBase ds = datasources.get(i);
			Boolean ok = ds.getType().equals("JoomlaMySQL") || ds.getType().equals("XAppConnect") || ds.getType().equals("JoomlaXML");
			if(!ok){
				continue;
			}
			
			DataSourceCache dsc =ServerCache.getDSC(appManager, app, ds);
			
			if(ds instanceof XCDataSource){
				
				XCDataSource xcds = (XCDataSource)ds;
    			if(xcds.getIdentifier() !=null && !xcds.getIdentifier().equals("XCJoomla")){
        			continue;
    			}
    				
    		}
			
			/***
			 * DS Header
			 */
			ContentTreeItem item = new ContentTreeItem();
			item.name  = ContentTreeFactoryBase.getDataSourceLabel(ds);
	    	item.id = ""+ds.getUid();
	    	item.dataSourceUID=ds.uid;
	    	item.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.DataSourceItem);
	    	Reference ref = new Reference();
	    	ref._reference="" + item.id;
	    	rootItem.addChild(ref);
	    	result.add(item);
	    	item.type = ds.getType();
	    
	    	if(item.type.equals("XAppConnect"))
    		{
    			XCDataSource xcds = (XCDataSource)ds;
    			if(xcds!=null){
    				item.xcGroup = xcds.getIdentifier();
    			}
    			item.children = new ArrayList<Reference>();
    			continue;
    		}
	    	
	    	if(hasContent)
	    	{
	    		ArrayList<ContentTreeItem>articleItems= JoomlaContentTreeFactory.addArticles(appManager, app, ds, item,tree);
	    		if(articleItems!=null)
	    		{
	    			tree.items.addAll(articleItems);
	    		}
	    	}
	
	    	if(hasBanners)
	    	{
	    		ArrayList bannerCats = dsc.getByType(ECMContentSourceType.JoomlaBannerCategory);
	    		if(bannerCats !=null && bannerCats.size() > 0){
			    	ArrayList<ContentTreeItem>bannerItems= JoomlaContentTreeFactory.addBannerCategories(appManager, app, ds, item,tree);
		    		if(bannerItems!=null)
		    		{
		    			tree.items.addAll(bannerItems);
		    		}
	    		}
	    	}
	    	
	    	if(hasJEventsLocationCategory)
	    	{
	    		ArrayList<Category> allLocationCats= dsc.getByType(ECMContentSourceType.JEventsLocationCategory);
	    		if(allLocationCats !=null && allLocationCats.size() > 0)
	    		{
		    		ArrayList<ContentTreeItem>jEventLocationItems= JEventsContentTreeFactory.addJEventLocationCategories(appManager, app, ds, item,tree,-1);
		    		if(jEventLocationItems!=null)
		    		{
		    			tree.items.addAll(jEventLocationItems);
		    		}
	    		}
	    	}
	    	
	    	ArrayList<EventData> allEvents = dsc.getByType(ECMContentSourceType.JEventsCalendarToday);
	    	if(hasJEventsCategories &&  allEvents!=null && allEvents.size()>0)
	    	{
	    		ArrayList<ContentTreeItem>jEventCatItems= JEventsContentTreeFactory.addJEventsCategories(appManager, app, ds,item,tree,-1);
	    		if(jEventCatItems!=null)
	    		{
	    			tree.items.addAll(jEventCatItems);
	    		}
	    	}

	    		
	    	if(hasJEventsToday &&  allEvents!=null && allEvents.size()>0)
	    	{
	    		ContentTreeItem jeventsToday = ContentTreeFactoryBase.createDummyTreeItemEx("JEvents Today",ECMContentSourceType.JEventsCalendarToday, item, tree, null);
	    		jeventsToday.type="JEvents";
	    		jeventsToday.dataSourceUID=ds.getUid();
	    		jeventsToday.setUidStr("1");
	    		
	    	}

	    	
	    	/*
	    	if(hasJEventsWeek &&  allEvents!=null && allEvents.size()>0)
	    	{
	    		ContentTreeItem jeventsWeek= ContentTreeFactoryBase.createDummyTreeItemEx("JEvents Week",ECMContentSourceType.JEventsCalendarWeek, item, tree, null);
	    		jeventsWeek.type="JEvents";
	    		jeventsWeek.dataSourceUID=ds.getUid();
	    		jeventsWeek.setUidStr("7");	
	    	}*/
	    	
	    	
	    	Boolean bf = AppFeatureConfigUtil.isEnabled(XASFeature.BreezingForms);
    		if(AppFeatureConfigUtil.isEnabled(XASFeature.BreezingForms) && hasBForms)
	    	{
    			ArrayList forms = dsc.getByType(ECMContentSourceType.BreezingForm);
    			if(forms !=null && forms.size()  > 0)
    			{
		    		ArrayList<ContentTreeItem>formItems= JoomlaContentTreeFactory.addBreezingForms(appManager, app, ds, item,tree);
		    		if(formItems!=null)
		    		{
		    			tree.items.addAll(formItems);
		    		}
    			}
	    	}
	    	
    		if(AppFeatureConfigUtil.isEnabled(XASFeature.Moset) && hasMosetCategory)
	    	{
    			ArrayList mosetCats= dsc.getByType(ECMContentSourceType.MosetTreeCategory);
    			if(mosetCats!=null && mosetCats.size() > 0)
    			{
		    		ArrayList<ContentTreeItem>mosetTreeCategoryItems= MosetContentTreeFactory.addMosetTree(appManager, app, ds, item,tree);
		    		if(mosetTreeCategoryItems!=null)
		    		{
		    			tree.items.addAll(mosetTreeCategoryItems);
		    		}
    			}
	    	}
    		
	    	
	    	if(AppFeatureConfigUtil.isEnabled(XASFeature.VMart))
	    	{
		    	if(hasVMCategories)
		    	{
			    	ArrayList<ContentTreeItem>vmartTreeCategoryItems= VMartTreeFactory.addVMartTree(appManager, app, ds, item,tree);
		    		if(vmartTreeCategoryItems!=null)
		    		{
		    			tree.items.addAll(vmartTreeCategoryItems);
		    		}
		    	}
	    	}
	    	
	    	
	    	
		}
		return tree;
	}

	public static ContentTree createDataSourceTreeFull(ApplicationManager appManager, Application app, Boolean deep) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		tree.id="1";
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		tree.setItems(result);
		
		if(app==null){
			return tree;
		}
		
		ArrayList<DataSourceBase>datasources = app.getDataSources();
		
		ContentTreeItem rootItem = new ContentTreeItem();
		
		rootItem.children = new ArrayList<Reference>();
		result.add(rootItem);
		
		rootItem.contentType = ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown);
		
		rootItem.type = "top";
		rootItem.name = "Data Sources"; 
		rootItem.id = "9898989";
		
		LiferayContentTools.prepareUser(app.getUserIdentifier(), app.getApplicationIdentifier());
		
		for(int i = 0  ; i  <  datasources.size() ; i++)	
		{
			DataSourceBase ds = datasources.get(i);
			if(!deep)
			{
				continue;
			}
			ContentTreeItem dsItem = new ContentTreeItem();
			dsItem.type = ds.getType();
			
    		if(dsItem.type.equals("Liferay"))
    		{
    			//continue;
    		}
    		
    		if(dsItem.type.equals("XAppConnect"))
    		{
    			XCDataSource xcds = (XCDataSource)ds;
    			if(xcds!=null){
    				dsItem.xcGroup = xcds.getIdentifier();
    			}
    			dsItem.children = new ArrayList<Reference>();
    		}
    		
    		DataSourceCache dsc =ServerCache.getDSC(appManager, app, ds);
    		
			dsItem.id=ds.getUid();
			dsItem.dataSourceUID=ds.getUid();
	    	String type =dsItem.type; 
	    	dsItem.name  = ContentTreeFactoryBase.getDataSourceLabel(ds);
	    	dsItem.id = ""+ds.getUid();
	    	dsItem.dataSourceUID=ds.uid;
	    	dsItem.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.DataSourceItem);
	    	Reference ref = new Reference();
	    	ref._reference="" + dsItem.id;
	    	rootItem.addChild(ref);
	    	result.add(dsItem);
	    	if(deep)
	    	{
	    		/***
	    		 * Liferay :
	    		 */
	    		
	    		if(dsItem.type.equals("Liferay"))
	    		{
	    			ArrayList<ContentTreeItem>articleItems= LiferayContentTreeFactory.addArticles(appManager, app, ds, dsItem,tree,0);
		    		if(articleItems!=null)
		    		{
		    			tree.items.addAll(articleItems);
		    		}
	    			//ContentTree lfTree=getContentLiferayTreeFiltere 
	    			//getContentLiferayTreeFiltered
	    			/*
		    		ArrayList<ContentTreeItem>articleItems= LiferayContentTreeFactory.addAllFiltered(appManager, app, ds, dsItem,tree,-1);
		    		if(articleItems!=null)
		    		{
		    			tree.items.addAll(articleItems);
		    		}*/
	    		}
	    		/***
	    		 * Joomla :
	    		 */
	    		if(dsItem.type.equals("JoomlaMySQL") || dsItem.type.equals("JoomlaXML"))	
	    		{
		    		ArrayList<ContentTreeItem>articleItems= JoomlaContentTreeFactory.addArticles(appManager, app, ds, dsItem,tree);
		    		if(articleItems!=null)
		    		{
		    			tree.items.addAll(articleItems);
		    		}
		    		
		    		if(AppFeatureConfigUtil.isEnabled(XASFeature.Banner))
			    	{
			    		ArrayList<ContentTreeItem>bannerItems= JoomlaContentTreeFactory.addBannerCategories(appManager, app, ds, dsItem,tree);
			    		if(bannerItems!=null)
			    		{
			    			tree.items.addAll(bannerItems);
			    		}
			    	}

		    		ArrayList<Category> allLocationCats= dsc.getByType(ECMContentSourceType.JEventsLocationCategory);
		    		
		    		if(allLocationCats!=null && allLocationCats.size() > 0)
		    		{
			    		ArrayList<ContentTreeItem>jEventLocationItems= JEventsContentTreeFactory.addJEventLocationCategories(appManager, app, ds, dsItem,tree,-1);
			    		if(jEventLocationItems!=null)
			    		{
			    			tree.items.addAll(jEventLocationItems);
			    		}
		    		}
		    		
		    		ArrayList<Category> allEventCats = dsc.getByType(ECMContentSourceType.JEventsCategory);
		    		if(allEventCats!=null && allEventCats.size() > 0)
		    		{
			    		ArrayList<ContentTreeItem>jEventCatItems= JEventsContentTreeFactory.addJEventsCategories(appManager, app, ds, dsItem,tree,-1);
			    		if(jEventCatItems!=null)
			    		{
			    			tree.items.addAll(jEventCatItems);
			    		}
		    		}
		    		ArrayList<EventData> allEvents = dsc.getByType(ECMContentSourceType.JEventsCalendarToday);
		    		
		    		if(allEvents!=null && allEvents.size()>0)
		    		{
			    		ContentTreeItem jeventsToday = ContentTreeFactoryBase.createDummyTreeItemEx("JEvents Today",ECMContentSourceType.JEventsCalendarToday, dsItem, tree, null);
			    		jeventsToday.type="JEvents";
			    		jeventsToday.dataSourceUID=ds.getUid();
			    		jeventsToday.setUidStr("1");
			    		/*
			    		ContentTreeItem jeventsWeek= ContentTreeFactoryBase.createDummyTreeItemEx("JEvents Week",ECMContentSourceType.JEventsCalendarWeek, dsItem, tree, null);
			    		
			    		jeventsWeek.type="JEvents";
			    		jeventsWeek.dataSourceUID=ds.getUid();
			    		jeventsWeek.setUidStr("7");*/
		    		}
		    		
		    		Boolean bf = AppFeatureConfigUtil.isEnabled(XASFeature.BreezingForms);
		    		if(AppFeatureConfigUtil.isEnabled(XASFeature.BreezingForms))
			    	{
			    		ArrayList<ContentTreeItem>formItems= JoomlaContentTreeFactory.addBreezingForms(appManager, app, ds, dsItem,tree);
			    		if(formItems!=null)
			    		{
			    			tree.items.addAll(formItems);
			    		}
			    	}


		    		if(AppFeatureConfigUtil.isEnabled(XASFeature.VMart))
			    	{
			    		ArrayList<ContentTreeItem>vmartTreeCategoryItems= VMartTreeFactory.addVMartTree(appManager, app, ds, dsItem,tree);
			    		if(vmartTreeCategoryItems!=null)
			    		{
			    			tree.items.addAll(vmartTreeCategoryItems);
			    		}
			    	}
		    		
		    		if(AppFeatureConfigUtil.isEnabled(XASFeature.Moset))
			    	{
			    		ArrayList<ContentTreeItem>mosetTreeCategoryItems= MosetContentTreeFactory.addMosetTree(appManager, app, ds, dsItem,tree);
			    		if(mosetTreeCategoryItems!=null)
			    		{
			    			tree.items.addAll(mosetTreeCategoryItems);
			    		}
			    	}
		    		
	    		}
	    		/***
	    		 * Google 
	    		 */
	    		if(type.equals("Picassa"))
	    		{
	    			int flags = BitUtils.enableFlag(ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GooglePicassaAlbum));
	    			ArrayList<ContentTreeItem>albumItems = GoogleTreeFactory.addAlbums(appManager, app, ds, dsItem, tree, flags);
		    		if(albumItems!=null)
		    		{
		    			tree.items.addAll(albumItems);
		    		}
	    		}
	    		
	    		if(type.equals("Calendar"))
	    		{
	    			int flags = BitUtils.enableFlag(ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendar));
	    			ArrayList<ContentTreeItem>calendarItems = GoogleTreeFactory.addCalendars(appManager, app, ds, dsItem, tree, flags);
		    		if(calendarItems!=null)
		    		{
		    			tree.items.addAll(calendarItems);
		    		}
	    		}
	    		
	    		if(type.equals("Documents"))
	    		{
	    			int flags = BitUtils.enableFlag(ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentFolder));
	    			ArrayList<ContentTreeItem>docFolderItems = GoogleTreeFactory.addDocFolders(appManager, app, ds, dsItem, tree, flags);
		    		if(docFolderItems!=null)
		    		{
		    			tree.items.addAll(docFolderItems);
		    		}
	    		}
	    		/**
	    		 * Wordpress 
	    		 */
	    		if(dsItem.type.equals("WordpressXML"))
	    		{
	    			System.out.println("adding wordpess xml items");
	    			ArrayList<ContentTreeItem>articleItems= WordpressTreeFactory.addArticles(appManager, app, ds, dsItem,tree);
	    			if(articleItems!=null)
		    		{
		    			tree.items.addAll(articleItems);
		    		}
	    		}
	    		
	    		if(dsItem.type.equals("WordpressMySQL"))
	    		{
	    			//System.out.println("adding wordpess mysql items");
	    			ArrayList<ContentTreeItem>articleItems= WordpressTreeFactory.addArticles(appManager, app, ds, dsItem,tree);
	    			if(articleItems!=null)
		    		{
		    			tree.items.addAll(articleItems);
		    		}
	    			
	    		}
	    	}
		}
		//tree.setItems(result);
		
		return tree;
	}

	public static ContentTree createGroupedTreeJoomla(ApplicationManager appManager, Application app, int flags,Boolean includeEmpty) throws ParserConfigurationException, SAXException, IOException, ParseException
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
    	rootItem.contentType = "group";
		
		for(int i = 0  ; i  <  datasources.size() ; i++)	
		{
			DataSourceBase ds = datasources.get(i);
			ContentTreeItem item = new ContentTreeItem();
	    	item.name = ds.getHost() + " (" + ds.getType() + " :: " + ds.getDatabase()  +")"; 
	    	item.id = ""+ds.getUid();
	    	item.dataSourceUID=ds.uid;
	    	item.contentType="group";


	       	Reference ref = new Reference();
	    	ref._reference="" + item.id;
	    	rootItem.addChild(ref);
	    	result.add(item);
	    	item.type = "Joomla";
	    	
			ArrayList<ContentTreeItem>articleItems= JoomlaContentTreeFactory.addArticles(appManager, app, ds, item,tree);
    		if(articleItems!=null)
    		{
    			tree.items.addAll(articleItems);
    		}
	    	
		}
		//tree.setItems(result);
		
		return tree;
	}
	
	public static ArrayList<ContentTreeItem>addTreeItemsByParent(ArrayList<Category> cats,ArrayList<ArticleData>articles,int parent,ContentTree tree,DataSourceBase dataSource)
	{
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		
    	ArrayList<Category>catItems = CategoryTools.getCategoriesByParentIndex(cats, parent);
    	
    	for (int s = 0; s < catItems.size() ; s++) 
		{
		    Category c = catItems.get(s);
		    if(c.published){
		    	
		    	ArrayList<ArticleData >categoryArticles = ArticleTools.getArticlesByTypeAndParent(articles, c.refId, PMDataTypes.DITT_JARTICLE);
		    	if(categoryArticles!=null && categoryArticles.size()==0){
		    		//continue;
		    	}
		    	
		    	ContentTreeItem item = new ContentTreeItem();
		    	
		    	item = ContentTreeFactoryBase.createTreeItemWithDataEx(c, null, ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory), dataSource.getUid());
		    	
		    	
		    	item.name = c.title;
		    	item.contentType = ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory);
		    	item.dataSourceUID=dataSource.uid;
		    	//String uuid2 = UUID.randomUUID().toString();
		    	//item.id = ""+c.refId;
		    	item.id = c.getTreeUid();
		    	item.uid = c.refId;
		    	result.add(item);
		    	tree.items.add(item);
		    	//c.hasSubCategories = CategoryTools.hasSubCategories(cats, c.refId);
		    	item.type = "leaf";
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
		    			
		    			
		    			//articleItem.id = ""+(article.article_id + 50000);
		    			String uuid3 = UUID.randomUUID().toString();
		    			
		    			articleItem.id = article.getTreeUid();
		    			
		    			articleItem.uid = article.refId;
		    			articleItem.label = article.title;
		    			articleItem.contentType = ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaArticle);
		    			articleItem.dataSourceUID=dataSource.uid;
				    	result.add(articleItem);
				    	tree.items.add(articleItem);
				    	//c.hasSubCategories = CategoryTools.hasSubCategories(cats, c.refId);
				    	articleItem.type = "item";
				    	articleItem.setChildren(null);
				    	articleItem.children = new ArrayList<Reference>();
				    	
				    	Reference articleRef = new Reference();
	    		    	articleRef._reference="" + articleItem.id;
	    		    	item.addChild(articleRef);
					}
		    	}
		    	/***
		    	 * add cat to section 
		    	 */
		    	/*
		    	Reference ref = new Reference();
		    	ref._reference="" + c.refId;
		    	ref._reference="" + item.id;
		    	*/
		    }
		}

		return result;
	}

	public static ArrayList<ContentTreeItem>addTreeItemsByParent(ArrayList<Category> cats,ArrayList<Category> sections,ArrayList<ArticleData>articles,int parent,ContentTree tree,DataSourceBase dataSource)
	{
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		if(cats==null){
			return null;
		}
		
		if(sections==null){
			return null;
		}
		if(articles==null)
		{
			return null;
		}
		
		for (int si = 0; si < sections.size() ; si++) 
		{
			Category section = sections.get(si);
		    if(section.published)
		    {
		    	ArrayList<Category>catItems = CategoryTools.getCategoriesByParentIndex(cats, section.refId);
		    	if(catItems!=null && catItems.size() == 0)
		    	{
		    		continue;
		    	}
		    	ContentTreeItem sectionItem = ContentTreeFactoryBase.createTreeItemWithDataEx(section, 
		    			null, 
		    			""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaSection), 
		    			dataSource.getUid(),
		    			null,
		    			tree,
		    			result);
		    	
		    	for (int s = 0; s < catItems.size() ; s++) 
				{
				    Category c = catItems.get(s);
				    if(c.published){
				    	
				    	ArrayList<ArticleData >categoryArticles = ArticleTools.getArticlesByTypeAndParent(articles, c.refId, PMDataTypes.DITT_JARTICLE);
				    	if(categoryArticles!=null && categoryArticles.size()==0){
				    		
				    		//continue;
				    	}
				    	
				    	ContentTreeItem item = ContentTreeFactoryBase.createTreeItemWithDataEx(
				    			c,
				    			""+section.refId, 
				    			""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory), 
				    			dataSource.getUid(),
				    			sectionItem,
				    			tree,
				    			result);

				    	/***
				    	 * add articles
				    	 */
				    	if(categoryArticles!=null && categoryArticles.size() > 0)
				    	{
				    		for (int ai = 0; ai < categoryArticles.size() ; ai++) 
							{
				    			ArticleData article = categoryArticles.get(ai);
					    		
				    			ContentTreeItem articleItem = ContentTreeFactoryBase.createTreeItemWithDataEx(
				    					article, 
				    					null, 
				    					""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaArticle), 
				    					dataSource.getUid(),
				    					item,
				    					tree,
				    					result);
							}
				    	}
				    }
				}
			}
		}
		return result;
	}
	public static ArrayList<ContentTreeItem>addTreeItemsByParent(ArrayList<Category> cats,ArrayList<Category> sections,int parent,ContentTree tree)
	{
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		
		for (int si = 0; si < sections.size() ; si++) 
		{
			Category section = sections.get(si);
		    if(section.published)
		    {
		    	ContentTreeItem sectionItem = new ContentTreeItem();
		    	sectionItem.name = section.title;
		    	sectionItem.id = ""+(section.refId+100000);
		    	sectionItem.uid = section.refId;
		    	result.add(sectionItem);
		    	tree.items.add(sectionItem);
		    	//sectionItem.hasSubCategories = CategoryTools.hasSubCategories(cats, c.refId);
		    	sectionItem.type = "top";
		    	/*
		    	if(parent==1)
		    	{
		    		item.type="top";
		    	}
		    	*/
		    	
		    	
		    	ArrayList<Category>catItems = CategoryTools.getCategoriesByParentIndex(cats, section.refId);
				for (int s = 0; s < catItems.size() ; s++) 
				{
				    Category c = catItems.get(s);
				    if(c.published){
				    	
				    	ContentTreeItem item = new ContentTreeItem();
				    	item.name = c.title;
				    	item.id = ""+c.refId;
				    	item.uid = c.refId;
				    	result.add(item);
				    	tree.items.add(item);
				    	c.hasSubCategories = CategoryTools.hasSubCategories(cats, c.refId);
				    	item.type = "leaf";
				    	item.contentType="category";
				    	
				    	Reference ref = new Reference();
	    		    	ref._reference="" + c.refId;
	    		    	sectionItem.addChild(ref);

				    }
				}
				
			}
		}
		return result;
	}
	public static ArrayList<ContentTreeItem>addTreeItemsByParent(ArrayList<Category> cats,int parent,ContentTree tree)
	{
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		ArrayList<Category>catItems = CategoryTools.getCategoriesByParentIndex(cats, parent);
		for (int s = 0; s < catItems.size() ; s++) 
		{
		    Category c = catItems.get(s);
		    if(c.published){
		    	
		    	ContentTreeItem item = new ContentTreeItem();
		    	item.name = c.title;
		    	item.id = ""+c.refId;
		    	item.uid = c.refId;
		    	result.add(item);
		    	tree.items.add(item);
		    	c.hasSubCategories = CategoryTools.hasSubCategories(cats, c.refId);
		    	item.type = "leaf";
		    	item.contentType="category";
		    	if(parent==1)
		    	{
		    		item.type="top";
		    	}
		    	if(c.hasSubCategories)
		    	{
		    		//item.type="branch";
		    		ArrayList<Category>subItems = CategoryTools.getCategoriesByParentIndex(cats,c.refId);
		    		for (int j = 0; j < subItems.size() ; j++) 
		    		{
		    		    Category subItem = subItems.get(j);
		    		    if(subItem.published){
		    		    	Reference ref = new Reference();
		    		    	ref._reference="" + subItem.refId;
		    		    	item.addChild(ref);
		    		    }
		    		}
		    		JoomlaContentTreeFactory.addTreeItemsByParent(cats, c.refId, tree);
		    	}
		    	//ContentTreeFactory.addSubReferences(cats, c.refId,tree,item);
		    }
		}
		return result;
	}
	
	public static ArrayList<ContentTreeItem>addBannerTreeItems(ArrayList<Category> cats,ArrayList<CListItem>items ,int parent,int rootIndex,ContentTree tree)
	{
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		for (int s = 0; s < cats.size() ; s++) 
		{
		    Category c = cats.get(s);
		    if(c.published)
		    {
		    	ContentTreeItem item = new ContentTreeItem();
		    	item.name = c.title;
		    	item.id = ""+c.getTreeUid();
		    	item.uid = c.refId;
		    	result.add(item);
		    	item.type = "leaf";
		    	
		    	item.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaBannerCategory);
		    	tree.items.add(item);
		    	ArrayList<CListItem>catItems=CListItemTools.getByGroupId(items, c.refId);
		    	for (int j = 0; j < catItems.size() ; j++) 
	    		{
	    		    CListItem catItem = catItems.get(j);
	    		    if(catItem.published)
	    		    {
	    		    	
	    		    	ContentTreeItem subItem = new ContentTreeItem();
	    		    	subItem.name = catItem.getTitle();
				    	subItem.contentType = ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaBannerItem);
				    	subItem.id = UUID.randomUUID().toString();
				    	subItem.uid = catItem.refId;
				    	
				    	tree.items.add(subItem);
				    	
				    	Reference ref = new Reference();
	    		    	ref._reference="" + subItem.id;
	    		    	item.addChild(ref);
	    		    }
	    		}
		    }
		}
		return result;
	}
	public static ArrayList<ContentTreeItem>addTreeItemsByParent(ArrayList<Category> cats,int parent,int rootIndex, ContentTree tree)
	{
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		ArrayList<Category>catItems = CategoryTools.getCategoriesByParentIndex(cats, parent);
		
		for (int s = 0; s < catItems.size() ; s++) 
		{
		    Category c = catItems.get(s);
		    if(c.published){
		    	

		    	
		    	ContentTreeItem item = new ContentTreeItem();
		    	item.name = c.title;
		    	item.id = ""+UUID.randomUUID().toString();
		    	item.uid = c.refId;
		    	result.add(item);
		    	tree.items.add(item);
		    	c.hasSubCategories = CategoryTools.hasSubCategories(cats, c.refId);
		    	item.type = "leaf";
		    	item.contentType="category";
		    	if(c.type!=null && c.type.equals("com_banner"))
		    	{
		    		item.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaBannerCategory);
		    	}
		    	
		    	if(parent==rootIndex)
		    	{
		    		item.type="top";
		    	}
		    	if(c.hasSubCategories)
		    	{
		    		//item.type="branch";
		    		ArrayList<Category>subItems = CategoryTools.getCategoriesByParentIndex(cats,c.refId);
		    		for (int j = 0; j < subItems.size() ; j++) 
		    		{
		    		    Category subItem = subItems.get(j);
		    		    if(subItem.published){
		    		    	Reference ref = new Reference();
		    		    	ref._reference="" + subItem.refId;
		    		    	item.addChild(ref);
		    		    }
		    		}
		    		JoomlaContentTreeFactory.addTreeItemsByParent(cats, c.refId,rootIndex, tree);
		    	}
		    	//ContentTreeFactory.addSubReferences(cats, c.refId,tree,item);
		    }
		}
		return result;
	}
	public static ArrayList<ContentTreeItem>addTreeItemsByParent(CList cats,int parent,int rootIndex, ContentTree tree)
	{
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		
		for (int s = 0; s < cats.items.size() ; s++) 
		{
		    CListItem c = (CListItem) cats.items.get(s);
	    	ContentTreeItem item = new ContentTreeItem();
	    	item.name = c.title;
	    	item.id = ""+UUID.randomUUID().toString();
	    	item.uid = Integer.parseInt(c.ref);
	    	result.add(item);
	    	tree.items.add(item);
	    	item.type = "leaf";
	    	item.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.BreezingForm);
	    	
		}
		return result;
	}
	public static ArrayList<ContentTreeItem>addTreeItemsByParent(CList cats,int parent,int rootIndex, ContentTree tree,String dsUID)
	{
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		
		for (int s = 0; s < cats.items.size() ; s++) 
		{
		    CListItem c = (CListItem) cats.items.get(s);
	    	ContentTreeItem item = new ContentTreeItem();
	    	item.name = c.title;
	    	item.id = ""+UUID.randomUUID().toString();
	    	item.uid = Integer.parseInt(c.ref);
	    	item.dataSourceUID=dsUID;
	    	result.add(item);
	    	tree.items.add(item);
	    	item.type = "leaf";
	    	item.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.BreezingForm);
	    	
		}
		return result;
	}
	
	
	/***
	 * addTreeItemsRecursive : traverses over Joomla 1.7+ categories
	 * @param cats
	 * @param articles
	 * @param parent
	 * @param tree
	 * @param dataSource
	 * @param contentType
	 * @param dstArray
	 * @param flags
	 * @return
	 */
	public static ArrayList<ContentTreeItem>addTreeItemsRecursive(
			ArrayList<Category> cats,
			ArrayList<ArticleData>articles,
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
		    			""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory), 
		    			dataSource.getUid(),
		    			parentItem,
		    			tree,
		    			result);

		    	
		    	ArrayList<ArticleData >categoryArticles = null;
		    	
		    	if(articles !=null && articles.size() > 0){
			    	categoryArticles=ArticleTools.getArticlesByTypeAndParent(articles, c.refId, PMDataTypes.DITT_JARTICLE);
			    	if(categoryArticles!=null && categoryArticles.size()==0)
			    	{
			    		//continue;
			    	}
		    	}
		    	
		    	/***
		    	 * add articles
		    	 */
		    	if(categoryArticles!=null && categoryArticles.size() > 0)
		    	{
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
		    	}
		    	
		    	/**
		    	 * Recursion for categories
		    	 */
		    	c.hasSubCategories = CategoryTools.hasSubCategories(cats, c.refId);
		    	if(c.title.equals("Joomla!"))
		    	{
    		    	//System.out.println("---|||| Joomla"  + c.title);
    		    }
		    	if(c.hasSubCategories)
		    	{
		    		//System.out.println("\t Recursion"  + c.title);
		    		
		    		/***
		    		 * 
		    		 */
		    		ArrayList<Category>subItems = CategoryTools.getCategoriesByParentIndex(cats,c.refId);
		    		for (int j = 0; j < subItems.size() ; j++) 
		    		{
		    		    Category subItem = subItems.get(j);
		    		    if(subItem.title.equals("Joomla!")){
		    		    	//System.out.println("---"  + subItem.title);
		    		    }
		    		    if(subItem.published)
		    		    {
		    		    	
/*
		    		    	JoomlaContentTreeFactory.addTreeItemsRecursive(
				    				cats,
				    				articles,
				    				subItem.index,
				    				tree,
				    				dataSource,
				    				""+ECMContentSourceType.JoomlaCategory,
				    				result,
				    				item,
				    				-1);
*/
		    		    	
		    		    	Reference ref = new Reference();
		    		    	ref._reference="" + subItem.getTreeUid();
		    		    	item.addChild(ref);
		    		    	
		    		    	
		    		    }
		    		    
		    		}
		    		
		    		JoomlaContentTreeFactory.addTreeItemsRecursive(
		    				cats,
		    				articles,
		    				c.refId,
		    				tree,
		    				dataSource,
		    				""+ECMContentSourceType.JoomlaCategory,
		    				result,
		    				item,
		    				-1);
		    		
		    		//JoomlaContentTreeFactoryNew.addTreeItemsByParent(cats,articles,c.refId, tree,dataSource,""+ECMContentSourceType.JoomlaCategory);
		    		
		    	}
		    }
		}

		return result;
	}
	
	public static ArrayList<ContentTreeItem>addBannerCategories(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree)
	{
		if(dataSource==null){
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = JoomlaContentTreeFactory.createJoomlaBannerCategoryTree(appManager, app, dataSource.uid);
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
    	rootItem.name = "Banners"; 
    	String uuid = UUID.randomUUID().toString();
    	rootItem.id = uuid;
    	rootItem.children = new ArrayList<Reference>();
    	destinationTree.items.add(rootItem);
    	rootItem.type = "JoomlaBanners";
    	rootItem.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown);
    	
    	Reference articleRef = new Reference();
    	articleRef._reference="" + rootItem.id;
    	dataSourceItem.addChild(articleRef);
    	
		for(int i = 0  ; i  <  tree.items.size() ; i++)	
		{
			
			ContentTreeItem item = tree.items.get(i);
			item.dataSourceUID=dataSource.getUid();
			int a = ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaBannerCategory);
			int b = -1;
			try {
				b= Integer.parseInt(item.contentType);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(a==b)
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
	
	public static void addDummyToDatasource(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree,ECMContentSourceType type,String title,String leafType)
	{

		///add it to items : 
		ContentTreeItem rootItem = new ContentTreeItem();
    	rootItem.name = title; 
    	String uuid = UUID.randomUUID().toString();
    	rootItem.id = uuid;
    	
    	destinationTree.items.add(rootItem);
    	rootItem.type = leafType;
    	rootItem.contentType=""+type;
    	
    	Reference articleRef = new Reference();
    	articleRef._reference="" + rootItem.id;
    	dataSourceItem.addChild(articleRef);
	}
	public static ArrayList<ContentTreeItem>addBreezingForms(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree)
	{
		if(dataSource==null){
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = JoomlaContentTreeFactory.createBreezingFormTree(appManager, app, dataSource.uid);
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
    	rootItem.name = "Breezing Forms"; 
    	String uuid = UUID.randomUUID().toString();
    	rootItem.id = uuid;
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
			if(item.contentType !=null && item.contentType.equals(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.BreezingForm)))
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
	public static ArrayList<ContentTreeItem>addArticles(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree)
	{
		
		if(dataSource==null){
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = JoomlaContentTreeFactory.createContentTree(appManager, app, dataSource.uid);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		if(tree==null){
			return null;
		}
		
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
		
		return tree.items;
	}
	
	
	
	public static ContentTree createMostetCategoryTree(ApplicationManager appManager, Application app, String dataSourceUid) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList<Category> cats = dsc.getByType(ECMContentSourceType.MosetTreeCategory);
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
		//ArrayList cats =app.getDataListByType(appManager,ECMContentSourceType.JoomlaBannerCategory, dataSource);
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList cats = dsc.getByType(ECMContentSourceType.JoomlaBannerCategory);
		ArrayList banners = dsc.getByType(ECMContentSourceType.JoomlaBannerItem);
		if(cats!=null)
		{
			JoomlaContentTreeFactory.addBannerTreeItems(cats,banners,0,0,tree);
		}
		
		
		return tree;
	}
	
	public static ContentTree createBreezingFormTree(ApplicationManager appManager, Application app, String dataSourceUid) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList forms = dsc.getByType(ECMContentSourceType.BreezingForm);
		if(forms!=null && forms.size() > 0)
		{
			CList formsItem = (CList) forms.get(0);
			if(formsItem!=null)
			{
				JoomlaContentTreeFactory.addTreeItemsByParent(formsItem,0,0,tree,dataSourceUid);	
			}
			
		}
		
		return tree;
	}
}
