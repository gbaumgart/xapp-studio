package cmx.tools;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pmedia.DataManager.ServerCache;
import pmedia.types.CContent;
import pmedia.types.CList;
import pmedia.types.CListItem;
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

public class GoogleTreeFactory  extends ContentTreeFactoryBase
{	

	
	public static ArrayList<ContentTreeItem>addTreeItemsByParentDocFolders(ArrayList<CContent>calendars,ContentTree tree,DataSourceBase dataSource)
	{

		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		for (int s = 0; s < calendars.size() ; s++) 
		{
    		CContent album = calendars.get(s);
    		
	    	ContentTreeItem cItem = new ContentTreeItem();
	    	cItem.name = album.getTitle();
	    	cItem.contentType = "" + ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentFolder);
	    	cItem.dataSourceUID=dataSource.uid;
	    	String uuid  = UUID.randomUUID().toString();
	    	cItem.id = uuid;
	    	cItem.uid = album.getRefId();
	    	cItem.setUidStr(album.refIdStr);
	    	result.add(cItem);
	    	tree.items.add(cItem);
	    	cItem.type = "leaf";
	    	
	    	Reference ref = new Reference();
	    	ref._reference="" + album.getRefIdStr();
	    	//ref._reference="" + cItem.id;
		}
		
		return result;
		
	}
	public static ContentTree createDocFolderTree(ApplicationManager appManager, Application app, String dataSourceUid,long flags) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		tree.id="1";
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList<CContent> calendars=dsc.getByType(ECMContentSourceType.GoogleDocumentFolder);
		if(calendars!=null )
		{
  		   //addTreeItemsByParent(CList albums,ArrayList<CListItem>photos,ContentTree tree,DataSourceBase dataSource)
		   //addTreeItemsByParentCalendars(ArrayList<CContent>calendars,ContentTree tree,DataSourceBase dataSource)
		   GoogleTreeFactory.addTreeItemsByParentDocFolders(calendars,tree,dataSource);
		}
		
		return tree;
	}
	public static ArrayList<ContentTreeItem>addDocFolders(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree,long flags)
	{
		if(dataSource==null)
		{
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = GoogleTreeFactory.createDocFolderTree(appManager, app, dataSource.uid,flags);
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
    	rootItem.name = "Document Folders"; 
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
			if(item.contentType !=null && item.contentType.equals(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentFolder)))
			{
				Reference ref = new Reference();
		    	ref._reference="" + item.id;
		    	rootItem.addChild(ref);
		    	item.type = "leaf";
		    	item.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentFolder);
			}
		}
		return tree.items;
	}

	
	/***
	 * Calendars
	 * @param calendars
	 * @param tree
	 * @param dataSource
	 * @return
	 */
	
	public static ArrayList<ContentTreeItem>addTreeItemsByParentCalendars(ArrayList<CContent>calendars,ContentTree tree,DataSourceBase dataSource)
	{

		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		for (int s = 0; s < calendars.size() ; s++) 
		{
    		CContent album = calendars.get(s);
    		
	    	ContentTreeItem cItem = new ContentTreeItem();
	    	cItem.name = album.getTitle();
	    	cItem.contentType = "" + ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendar);
	    	cItem.dataSourceUID=dataSource.uid;
	    	String uuid  = UUID.randomUUID().toString();
	    	cItem.id = uuid;
	    	cItem.uid = album.getRefId();
	    	cItem.setUidStr(album.refIdStr);
	    	result.add(cItem);
	    	tree.items.add(cItem);
	    	cItem.type = "leaf";
	    	
	    	Reference ref = new Reference();
	    	ref._reference="" + album.getRefIdStr();
	    	//ref._reference="" + cItem.id;
		}
		
		return result;
		
	}
	public static ContentTree createCalendarTree(ApplicationManager appManager, Application app, String dataSourceUid,long flags) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		tree.id="1";
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList<CContent> calendars=dsc.getByType(ECMContentSourceType.GoogleCalendar);
		if(calendars!=null )
		{
  		   //addTreeItemsByParent(CList albums,ArrayList<CListItem>photos,ContentTree tree,DataSourceBase dataSource)
		   //addTreeItemsByParentCalendars(ArrayList<CContent>calendars,ContentTree tree,DataSourceBase dataSource)
		   GoogleTreeFactory.addTreeItemsByParentCalendars(calendars,tree,dataSource);
		}
		
		return tree;
	}
	public static ArrayList<ContentTreeItem>addCalendars(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree,long flags)
	{
		if(dataSource==null){
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = GoogleTreeFactory.createCalendarTree(appManager, app, dataSource.uid,flags);
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
    	rootItem.name = "Calendars"; 
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
			if(item.contentType !=null && item.contentType.equals(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendar)))
			{
				Reference ref = new Reference();
		    	ref._reference="" + item.id;
		    	rootItem.addChild(ref);
		    	item.type = "leaf";
		    	item.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendar);
			}
		}
		return tree.items;
	}

	
	public static ArrayList<ContentTreeItem>addTreeItemsByParent(CList albums,ArrayList<CListItem>photos,ContentTree tree,DataSourceBase dataSource)
	{
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		
    	for (int s = 0; s < albums.getItems().size() ; s++) 
		{
    		CListItem album = albums.getItems().get(s);
    		
	    	ArrayList<CListItem> albumEntries = CListItemTools.getByGroupId(photos,album.getRef());
	    	if(albumEntries!=null && albumEntries.size()==0){
	    		continue;
	    	}
	    	
	    	ContentTreeItem cItem = new ContentTreeItem();
	    	cItem.name = album.getTitle();
	    	cItem.contentType = "" + ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GooglePicassaAlbum);
	    	cItem.dataSourceUID=dataSource.uid;
	    	String uuid  = UUID.randomUUID().toString();
	    	cItem.id = uuid;
	    	cItem.uid = album.getRefId();
	    	cItem.setUidStr(album.ref);
	    	result.add(cItem);
	    	tree.items.add(cItem);
	    	cItem.type = "leaf";
	    	if(albumEntries!=null && albumEntries.size() > 0)
	    	{
	    		for (int ai = 0; ai < albumEntries.size() ; ai++) 
				{
	    			CListItem article = albumEntries.get(ai);
	    			
		    		
	    			ContentTreeItem articleItem = new ContentTreeItem();
	    			articleItem.name = article.title;
	    			//articleItem.id = ""+(article.article_id + 50000);
	    			String uuid3 = UUID.randomUUID().toString();
	    			
	    			
	    			//articleItem.id = article.getTreeUid();
	    			articleItem.id = uuid3;
	    			articleItem.uidStr = article.getRef();
	    			articleItem.label = article.title;
	    			articleItem.contentType = ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GooglePicassaItem);
	    			articleItem.dataSourceUID=dataSource.uid;
			    	result.add(articleItem);
			    	tree.items.add(articleItem);
			    	//c.hasSubCategories = CategoryTools.hasSubCategories(cats, c.index);
			    	articleItem.type = "item";
			    	articleItem.setChildren(null);
			    	articleItem.children = new ArrayList<Reference>();
			    	
			    	Reference articleRef = new Reference();
    		    	articleRef._reference="" + articleItem.id;
    		    	cItem.addChild(articleRef);
				}
	    	}
	    	Reference ref = new Reference();
	    	ref._reference="" + album.getRef();
	    	//ref._reference="" + cItem.id;
		}
		
    
		return result;
	}
	
	public static ContentTree createAlbumTree(ApplicationManager appManager, Application app, String dataSourceUid,long flags) throws ParserConfigurationException, SAXException, IOException, ParseException
	{		
		ContentTree tree = new ContentTree();
		tree.id="1";
		DataSourceBase dataSource = app.getDataSource(dataSourceUid);
		
		DataSourceCache dsc=ServerCache.getDSC(appManager, app, dataSource);
		ArrayList<CList> albums=dsc.getByType(ECMContentSourceType.GooglePicassaAlbum);
		ArrayList<CListItem> entries=dsc.getByType(ECMContentSourceType.GooglePicassaItem);
		if(albums!=null && entries!=null)
		{
			CList cAlbums = albums.get(0);
			if(cAlbums!=null)
			{
				//addTreeItemsByParent(CList albums,ArrayList<CListItem>photos,ContentTree tree,DataSourceBase dataSource)
				
				GoogleTreeFactory.addTreeItemsByParent(cAlbums,entries,tree,dataSource);
			}
		}
		
		return tree;
	}
	public static ArrayList<ContentTreeItem>addAlbums(ApplicationManager appManager, Application app,DataSourceBase dataSource,ContentTreeItem dataSourceItem,ContentTree destinationTree,long flags)
	{
		if(dataSource==null){
			return null;
		}
		ContentTree tree = null ; 
		try {
			tree = GoogleTreeFactory.createAlbumTree(appManager, app, dataSource.uid,flags);
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
    	rootItem.name = "Albums"; 
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
			if(item.contentType !=null && item.contentType.equals(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GooglePicassaAlbum)))
			{
				Reference ref = new Reference();
		    	ref._reference="" + item.id;
		    	rootItem.addChild(ref);
		    	//result.add(item);
		    	item.type = "leaf";
		    	item.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GooglePicassaAlbum);
			}
		}
		return tree.items;
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
    	//result.add(rootItem);
    	tree.items.add(rootItem);
    	rootItem.type = "top";
    	rootItem.contentType = ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown);
		
		for(int i = 0  ; i  <  datasources.size() ; i++)	
		{
			DataSourceBase ds = datasources.get(i);
			String dsType = ds.getType();
			Boolean isValid = dsType.equals("Youtube") || dsType.equals("Picassa") || dsType.equals("Calendar") || dsType.equals("Documents");
			if(!isValid)
				continue;
			
			ContentTreeItem item = new ContentTreeItem();
	    	item.name = GoogleTreeFactory.getDataSourceLabel(ds); 
	    	item.id = ""+ds.getUid();
	    	item.dataSourceUID=ds.uid;
	    	item.contentType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.DataSourceItem);
	    	/*
	    	item.children = new ArrayList<Reference>();
	    	Reference ref0 = new Reference();
	    	ref0._reference="" + "asdasd";
	    	item.addChild(ref0);
	    	*/
	       	Reference ref = new Reference();
	    	ref._reference="" + item.id;
	    	rootItem.addChild(ref);
	    	result.add(item);
	    	item.type = ds.getType();
	    	
	    	if(dsType.equals("Picassa") && BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GooglePicassaAlbum)))
			{
	    		ArrayList<ContentTreeItem>albumItems = GoogleTreeFactory.addAlbums(appManager, app, ds, item, tree, flags);
	    		if(albumItems!=null)
	    		{
	    			tree.items.addAll(albumItems);
	    		}
	    	}
	    	
	    	if(dsType.equals("Calendar") && BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendar)))
			{
	    		ArrayList<ContentTreeItem>albumItems = GoogleTreeFactory.addCalendars(appManager, app, ds, item, tree, flags);
	    		if(albumItems!=null)
	    		{
	    			tree.items.addAll(albumItems);
	    		}
	    	}
	    	
	    	Boolean docs = BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentFolder));
	    	if(dsType.equals("Documents") && BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentFolder)))
			{
	    		ArrayList<ContentTreeItem>albumItems = GoogleTreeFactory.addDocFolders(appManager, app, ds, item, tree, flags);
	    		if(albumItems!=null)
	    		{
	    			tree.items.addAll(albumItems);
	    		}
	    	}

		}
		//tree.setItems(result);
		
		return tree;
	}
	
}
