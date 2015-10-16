package cmx.cache;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import cmx.data.CContentFactory;
import cmx.data.CContentList;
import cmx.data.CContentStorageUtils;
import cmx.tools.DataFix;
import cmx.tools.JEventsContentTreeFactory;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;

import pmedia.DataUtils.ApplicationConfigurationTools;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.BaseDataArrayTools;
import pmedia.DataUtils.BreezingFormTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.EventDataArrayTools;
import pmedia.DataUtils.JBannerTools;
import pmedia.DataUtils.LocationArrayTools;
import pmedia.DataUtils.MediaUtils;
import pmedia.DataUtils.MenuDataTools;
import pmedia.DataUtils.RatingTools;
import pmedia.DataUtils.ResourceTools;
import pmedia.DataUtils.TranslationTools;
import pmedia.DataUtils.VMartTools;
import pmedia.html.factory.WhiteListDataFactory;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.CContent;
import pmedia.types.CEventComparator;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.CListItemBanner;
import pmedia.types.Category;
import pmedia.types.CategoryComparator;
import pmedia.types.Constants;
import pmedia.types.EventComparator;
import pmedia.types.EventData;
import pmedia.types.LocationComparator;
import pmedia.types.LocationData;
import pmedia.types.MappingData;
import pmedia.types.MediaItemBase;
import pmedia.types.MenueData;
import pmedia.types.MosetItem;
import pmedia.types.ProductData;
import pmedia.types.Rating;
import pmedia.types.TranslationData;
import pmedia.types.ResourceData;

import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.EventFilterTools;
import pmedia.utils.StringUtils;

import pmedia.utils.SettingsUtil;
import provider.disqus.DisqusContainerReponse;

public class DataSourceCache
{

	public Application application;
	public ApplicationManager applicationManager;
	public DataSourceBase sqlSource;
	public void updateCCList(ECMContentSourceType cType,
			String savePath,
			String format,
			String uuid,
			String appId,
			String dsUID,
			String lang,
			String platform)
	{
		ArrayList objects = getByType(cType);
		if(objects!=null && objects.size() > 0)
		{
			CContentStorageUtils.saveAsCCList(
					objects, 
					savePath, 
					format, 
					uuid, 
					appId, 
					sqlSource.getUid(), 
					cType, 
					WhiteListDataFactory.mobileBase(appId), 
							platform, lang);
			
		}
	}
	
	
	public CContent getAsCContent(
			ECMContentSourceType cType,
			String refId,
			String uuid,
			String appId,
			String dsUID,
			String lang,
			String platform)
	{
		
		int refIdInt = -1;
		try {
			refIdInt=Integer.parseInt(refId);
		} catch (Exception e) {
		}
		
		if(refIdInt==-1)
			return null;
		
			
		ArrayList objects = getByType(cType);
		if(objects==null){
			return null;
		}
		
		
		BaseData data = BaseDataArrayTools.getByIndex(objects, refIdInt);
		if(!data.published){
			return null;
		}
		String translationText= data.getDescription();
      	if(translationText!=null && translationText.length() >0)
    	{
    		ArrayList<MediaItemBase> pictureItems= MediaUtils.getPictureItemsFromText(translationText);
    		
    		if(pictureItems!=null && pictureItems.size() > 0)
    		{
    			if(data.getPictureItems()==null){
    				data.setPictureItems(pictureItems);
    			}else{
    				//data.getPictureItems().addAll(pictureItems);
    				MediaUtils.addPictureItems(pictureItems, data.getPictureItems(),sqlSource.getUrl());
    			}
    		}
    	}
      	
      	
      	CContent content = CContentFactory.fromArticleData(data, uuid, appId, dsUID, cType, WhiteListDataFactory.mobileBase(appId), platform,lang, 0);
		if(content!=null)
		{
			CContentFactory.setCustomFields(content, data, uuid, appId, dsUID, cType, platform, lang);
		}
      	/*
      	CContentStorageUtils.saveAsCContentData(
					objects, 
					savePath, 
					format, 
					uuid, 
					appId, 
					sqlSource.getUid(), 
					cType, 
					WhiteListDataFactory.mobileBase(appId), 
							platform, lang);
			
		}*/
		return content;
	}
	
	public void updateCContent(ECMContentSourceType cType,
			String savePath,
			String format,
			String uuid,
			String appId,
			String dsUID,
			String lang,
			String platform)
	{
		
		if(cType==ECMContentSourceType.JEventsCalendarToday)
		{
			
		}
		if(cType==ECMContentSourceType.BreezingForm)
		{
			return;
		}
		
		ArrayList objects = getByType(cType);
		
		      	
		if(objects!=null && objects.size() > 0)
		{
			
			for(int i = 0 ; i < objects.size() ; i++)
			{
				BaseData data = (BaseData)objects.get(i);
				if(!data.published){
					continue;
				}
				String translationText= data.getDescription();
		      	if(translationText!=null && translationText.length() >0)
		    	{
		    		ArrayList<MediaItemBase> pictureItems= MediaUtils.getPictureItemsFromText(translationText);
		    		
		    		if(pictureItems!=null && pictureItems.size() > 0)
		    		{
		    			if(data.getPictureItems()==null){
		    				data.setPictureItems(pictureItems);
		    			}else{
		    				//data.getPictureItems().addAll(pictureItems);
		    				MediaUtils.addPictureItems(pictureItems, data.getPictureItems(),sqlSource.getUrl());
		    			}
		    		}
		    	}
		      	if(data.getDescription()!=null && data.getDescription().length() > 0)
		      	{
		      		data.setDescription(data.getDescription().replaceAll("\\p{Cntrl}", ""));
		      		data.setDescription(data.getDescription().replaceAll("\n", ""));
		      	}
			}

			CContentStorageUtils.saveAsCContentData(
					objects, 
					savePath, 
					format, 
					uuid, 
					appId, 
					sqlSource.getUid(), 
					cType, 
					WhiteListDataFactory.mobileBase(appId), 
							platform, lang);
			
		}
	}
	
	public void purgeAll()
	{
		
	}
	public BaseData getObjectByTypeAndRef(ECMContentSourceType type,int ref)
	{
		ArrayList objects = getByType(type);
		if(objects==null || objects.size() ==0 )
		{
			return null;
		}

		BaseData result = BaseDataArrayTools.getByIndex(objects, ref);
		
		return result;
	}
	
	public static ArrayList<CListItem>listFromXML(String path)
	{
		ArrayList<CListItem> result = null;
		XStream xstream = new XStream(new StaxDriver());
		
		String xml = null;
		
		try {
			xml = StringUtils.readFileAsString(path);
		} catch (IOException e) {
			
			//e.printStackTrace();
			return null;
		}
		if(xml!=null && xml.length() > 0)
		{
			result = (ArrayList<CListItem>)xstream.fromXML(xml);
		}
		return result;
		
	}
	public static CList fromXML(String path)
	{
		
		CList result = null;
		XStream xstream = new XStream(new StaxDriver());
		
		String xml = null;
		
		try {
			xml = StringUtils.readFileAsString(path);
		} catch (IOException e) {
			
			//e.printStackTrace();
			return null;
		}
		if(xml!=null && xml.length() > 0)
		{
			result = (CList) xstream.fromXML(xml);
		}
		return result;
		
	}

	public DataSourceCache(ApplicationManager _appManager,Application app,DataSourceBase ds)
	{
		this.application = app;
		this.applicationManager = _appManager;
		this.sqlSource = ds;
		
	}
	
	public static void appendDSUID(ArrayList dst,String dsUID)
	{
		
	}
	
	public static DataSourceCache getInstance(String uid)
	{
		return ServerCache.getInstance().getDSC(uid);
	}

	public DataSource dataSource = null;
	
	public static String toCacheType(ECMContentSourceType type)
	{
		String result = null;
		
		switch (type) 
		{
			case JEventsCategory:
			{
				return DataSourceCache.CATS_EVENTS;
			}
			case VMartCategory:
			{
				return DataSourceCache.VMCATEGORIES;
			}
			case VMartProductItem:
			{
				return DataSourceCache.VMPRODUCTS;
			}
			case JEventsLocationCategory:
			{
				return DataSourceCache.CATS_LOCATIONS;
			}
			case JEventsLocationItem:
			{
				return DataSourceCache.LOCATIONS3;
			}
			
			case JEventsItem:
			case JEventsCalendarToday:
			case JEventsCalendarWeek:
			{
				return DataSourceCache.EVENTS_FINAL;
			}
			case BreezingForm:
			{
				return DataSourceCache.JBREEZING_FORM;
			}
			case MosetTreeItemList:
			case MosetTreeItem:
			{
				return DataSourceCache.MOSET_LINKS;
			}
			case MosetTreeCategory:
			{
				return DataSourceCache.CATS_MOSET;
			}
			case JoomlaBannerItem:
			{
				return DataSourceCache.JBANNERS;
			}
			case JoomlaBannerCategory:
			{
				return DataSourceCache.JBANNER_CATEGORY;
			}
			case JoomlaSection:
			{
				return DataSourceCache.JSECTION;
			}
			case JoomlaCategory:
			{
				return DataSourceCache.JCATEGORY;
			}
			case JoomlaArticle:
			{
				return DataSourceCache.JARTICLES;
			}
			case WordpressCategory:
			{
				return DataSourceCache.WPCATEGORIES;
			}
			case WordpressPage:
			{
				return DataSourceCache.WPPAGES;
			}
			case WordpressPost:
			{
				return DataSourceCache.WPPOSTS;
			}
			
			case WordpressUser:
			{
				return DataSourceCache.WPUSERS;
			}
			case WordpressTag:
			{
				return DataSourceCache.WPTAGS;
			}
			
			case GooglePicassaAlbum:
			{
				return DataSourceCache.GPICASSA_ALBUMS;
			}
			case GooglePicassaItem:
			{
				return DataSourceCache.GPICASSA_PHOTOS;
			}
			case GoogleYoutubeChannel:
			{
				return DataSourceCache.GYOUTUBE_CHANNEL;
			}
			
			case GoogleCalendar:
			{
				return DataSourceCache.GCALENDARS;
			}
			case GoogleCalendarItem:
			{
				return DataSourceCache.GCALENDAR_ITEMS;
			}
			
			case GoogleDocumentFolder:
			{
				return DataSourceCache.GDOC_FOLDERS;
			}
			case GoogleDocumentItem:
			{
				return DataSourceCache.GDOC_ITEMS;
			}
			
		}
		return result;
	}
	
	public void setByType(ECMContentSourceType type,ArrayList list)
	{
			switch (type) 
			{
				case JoomlaArticle:
				{
					jArticles=list;
					break;
				}
				
				case JoomlaBannerCategory:
				{
					joomlaBannerCategories=list;
					break;
				}
				case JoomlaBannerItem:
				{
					joomlaBanners=list;
					break;
				}
				
				case GooglePicassaAlbum:
				{
					gPicassaAlbums=list;
					break;
				}
				
				case GooglePicassaItem:
				{
					gPicassaPhotos=list;
					break;
				}
				
				case WordpressCategory:
				{
					wpCategories=list;
					break;
				}
				case GoogleCalendar:
				{
					gCalendars=list;
					break;
				}
				case GoogleCalendarItem:
				{
					gCalendarEvents=list;
					break;
				}
				
				case GoogleDocumentFolder:
				{
					gDocumentFolders=list;
					break;
				}
				case GoogleDocumentItem:
				{
					gDocumentItems=list;
					break;
				}
				case WordpressPost:
				{
					wpPosts=list;
					break;
				}
				
				case JoomlaCategory:
				{
					articleCategories=list;
					break;
				}
				case JoomlaSection:
				{
					articleSections=list;
					break;
				}
				
				case VMartCategory:
				{
					vmCategories=list;
					break;
				}
				case VMartProductList:
				case VMartProductItem:
				{
					vmProducts=list;
					break;
				}
				
				case MosetTreeItemList:
				case MosetTreeItem:
				{
					mosetItems=list;
					break;
				}
				case MosetTreeCategory:
				{
					mosetCategories=list;
					break;
				}
				case JEventsLocationItem:
				case JEventsLocationItemList:
				{
					locations=list;
					break;
				}
				case JEventsLocationCategory:
				{
					locationCategories=list;
					break;
				}
				case JEventsCategory:
				{
					eventCategories=list;
					break;
				}
				
				case JEventsCalendarToday:
				case JEventsCalendarWeek:
				case JEventsItem:
				{
					eventsFinal=list;
					dbEvents=list;
					break;
				}
				
				
			}
		
	}
			
	public ArrayList getByType(ECMContentSourceType type)
	{
		String internalType = toCacheType(type);
		ArrayList result = null;
		if(internalType!=null){
			try {
				result = get(internalType);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
		
	}


	//public static final String
	// own implementation :

	public static final String EVENTS_DB = "DBEvents";
	
	public static final String MENU = "MENU";
	public static final String RESOURCES = "RESOURCES";

	public static final String EVENTS_FB = "FBEvents";
	public static final String EVENTS_FB_ALL = "FBEventsAll";
	public static final String EVENTS_FB_TRUSTED = "FBEventsTrusted";
	public static final String EVENTS_FB_CLEANED = "FBEventsCleaned";

	public static final String EVENTS_FINAL= "EventsFinal";
	public static final String EVENTS_TICKETS_ALL = "TicketEventsAll";
	public static final String EVENTS_TICKETS_TRUSTED = "TicketEventsTrusted";
	public static final String CATS_EVENTS = "EventCats";
	public static final String CATS_LOCATIONS = "LocationCats";
	
	public static final String CATS_MOSET = "MOSET_CATS";
	public static final String MOSET_LINKS = "MOSET_LINKS";
	
	//public static final String LOCATIONS = "Locations";
	//public static final String LOCATIONS2 = "locations2";
	public static final String LOCATIONS3 = "locations3";
	public static final String RATINGS = "ratings";
	public static final String COMMENTS = "comments";
	public static final String THREADS = "threads";
	
	public static final String JARTICLES = "JARTICLES";
	public static final String JSECTION = "JSECTION";
	public static final String JCATEGORY = "JCATEGORY";
	
	public static final String VMCATEGORIES = "VMCATEGORIES";
	public static final String VMPRODUCTS = "VMPRODUCTS";
	
	
	public static final String WPCATEGORIES = "WPCATEGORIES";
	public static final String WPPAGES = "WPPAGES";
	public static final String WPPOSTS= "WPPOSTS";
	public static final String WPTAGS= "WPTAGS";
	public static final String WPUSERS= "WPUSERS";
	public static final String WPCOMMENTS= "WPCOMMENTS";
	
	public static final String GYOUTUBE_CHANNEL= "GYOUTUBE_CHANNEL";
	
	public static final String GPICASSA_ALBUMS= "GPICASSA_ALBUMS";
	public static final String GPICASSA_PHOTOS= "GPICASSA_PHOTOS";
	
	public static final String GCALENDARS= "GCALENDARS";
	public static final String GCALENDAR_ITEMS= "GCALENDAR_ITEMS";
	
	public static final String GDOC_FOLDERS= "GDOC_FOLDERS";
	public static final String GDOC_ITEMS= "GDOC_ITEMS";
	
	
	public static final String JBANNER_CATEGORY = "JBANNER_CATEGORY";
	public static final String JBANNERS= "JBANNERS";
	
	public static final String JBREEZING_FORM = "JBREEZING_FORM";
	
	
	public static final String ARTICLES = "articles";
	public static final String TRANSLATIONS= "translations";
	public static final String MAPPINGS = "mappings";
	
	public static final String ANNOTATIONS= "annotations";
	public static final String REGIONS= "regions";
	
	public static final String DTHREADS= "DTHREADS";
	public static final String APPCONFIGS= "appConfigs";
	
	
	public String domain = "ibiza";

	////////////////////////////////////////////////////////
	//
	//		status variables
	//
	public Boolean ccIsDirty = true;


	////////////////////////////////////////////////////////
	//
	//		Client Cache
	//
	public HashMap<Integer, HashMap<Integer,ArrayList<EventData>>>ccMD=null;
	public ArrayList<DisqusContainerReponse>disqusThreads = new ArrayList<DisqusContainerReponse>();
	
	////////////////////////////////////////////////////////
	//
	//		Stored Events
	//
	public ArrayList<EventData> dbEvents = null;


	public ArrayList<EventData> fbEvents = null;
	public ArrayList<EventData> fbEventsAll = null;
	public ArrayList<EventData> eventsFinal = null;


	public ArrayList<EventData> fbEventsTrusted = null;

	public ArrayList<EventData> fbEventsCleaned = null;

	public HashMap<String,String> trustedFBUsers = null;
	public ArrayList<String>blackListFBUsers = null;
	public ArrayList<String>blackListFBEvents = null;
	public HashMap<String, ArrayList<EventData>>fbUserEvents=null;


	public ArrayList<EventData> ticketsAll = null;
	public ArrayList<EventData> ticketsTrusted = null;

	public HashMap<String,String> trustedTicketLocations = null;
	public HashMap<String, String>blackListTicketLocations=null;
	public HashMap<String, String>blackListTicketCategories=null;

	////////////////////////////////////////////////////////
	//
	//		Extra Elements : Locations , Categories
	//
	public ArrayList<LocationData> locations = null;
	public ArrayList<Category> eventCategories = null;
	public ArrayList<Category> locationCategories = null;
	public ArrayList<Category> articleCategories = null;
	public ArrayList<Category> articleSections= null;
	
	
	public ArrayList<Category> vmCategories= null;
	public ArrayList<ProductData> vmProducts= null;

	
	
	
	
	public ArrayList<BaseData> wpCategories= null;
	public ArrayList<BaseData> wpPages= null;
	public ArrayList<BaseData> wpPosts= null;
	public ArrayList<BaseData> wpUsers= null;
	public ArrayList<BaseData> wpTags= null;
	public ArrayList<BaseData> wpComments= null;
	
	public ArrayList<CList> gYoutubeChannels= null;
	public ArrayList<CList> gPicassaAlbums= null;
	public ArrayList<CContent> gCalendars= null;
	
	public ArrayList<CContent> gDocumentFolders= null;
	public ArrayList<CContent> gDocumentItems= null;
	
	
	public ArrayList<CContent> gCalendarEvents= null;
	public ArrayList<CListItem> gPicassaPhotos= null;
	
	public ArrayList<Category> mosetCategories = null;
	public ArrayList<MosetItem> mosetItems = null;
	
	public ArrayList<CList> jBreezingForms = null;
	
	
	public ArrayList<Category> joomlaBannerCategories = null;
	public ArrayList<CListItemBanner> joomlaBanners= null;
	public ArrayList<Category> fpssBannerCategories = null;
	
	public ArrayList<Category> regions = null;

	public HashMap<String, ArrayList<EventData>>ticketLocationEvents=null;
	
	public ArrayList<ArticleData> articles = null;
	public ArrayList<ArticleData> jArticles = null;
	public ArrayList<TranslationData> translations = null;
	public ArrayList<MappingData> mappings= null;
	public ArrayList<MenueData> menues= null;
	
	public ArrayList<ResourceData> resources= null;
	public ArrayList<Rating> ratings= null;
	
	
	public ArrayList<pmedia.types.ApplicationConfiguration> appConfigs= null;
	
	public ArrayList get(String objectName) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		
		if(objectName.equals(VMCATEGORIES))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.VMartCategory, sqlSource);
			//vmCategories=null;
			if(vmCategories ==null)
			{
				vmCategories =  CategoryTools.readFromFile(dataFile,null,ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.VMartCategory));
				Comparator<Category> catAlpComparator = new CategoryComparator();
				Collections.sort(vmCategories, catAlpComparator);
				return vmCategories;
			}
			return vmCategories;
		}
		
		if(objectName.equals(VMPRODUCTS))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.VMartProductItem, sqlSource);
			//vmProducts=null;
			if(vmProducts==null)
			{
				vmProducts=  VMartTools.readProductsFromFile(sqlSource,dataFile);
				return vmProducts;
			}
			return vmProducts;
		}
		
		if(objectName.equals(CATS_EVENTS))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.JEventsCategory, sqlSource);
			//eventCategories=null;
			
			if(eventCategories ==null)
			{
				if(sqlSource.getType().equals("JoomlaXML"))
				{
					eventCategories= CategoryTools.readFromRPCFile(dataFile, null,ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCategory));
				}else{
					if(sqlSource.getVersion().equals("1.7")){
						eventCategories =  CategoryTools.readFromFile(dataFile,null,ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCategory));
					}else{
						eventCategories =  CategoryTools.readFromFile(dataFile,"com_jevents",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCategory));
					}
				}
				
				Comparator<Category> catAlpComparator = new CategoryComparator();
				Collections.sort(eventCategories, catAlpComparator);
				return eventCategories;
			}
			return eventCategories;
		}
		if(objectName.equals(LOCATIONS3))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.JEventsLocationItem, sqlSource);
			//locations=null;
			if(locations ==null)
			{
				locations = LocationArrayTools.readFromFile(dataFile);
				locations = LocationArrayTools.updateLocationCategories(locations,get(CATS_LOCATIONS));
				Comparator<LocationData> locAlpComparator = new LocationComparator();
				if(locations!=null)
				{
					Collections.sort(locations,locAlpComparator);
				}
			}
			return locations;
		}
		if(objectName.equals(CATS_LOCATIONS))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.JEventsLocationCategory, sqlSource);
			//locationCategories=null;
			if(locationCategories ==null)
			{
				if(sqlSource.getType().equals("JoomlaXML"))
				{
					locationCategories= CategoryTools.readFromRPCFile(dataFile, null,ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationCategory));
				}else{
					locationCategories = CategoryTools.readFromFile(dataFile,"com_jevlocations2",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationCategory));
				}
				
				Comparator<Category> catAlpComparator = new CategoryComparator();
				Collections.sort(locationCategories, catAlpComparator);

				for(int dIndex= 0 ; dIndex <  locationCategories.size() ; dIndex++ )
				{
					Category c = locationCategories.get(dIndex);
					c.hasSubCategories = CategoryTools.hasSubCategories(locationCategories, c.refId);
				}
				return locationCategories;
			}
			return locationCategories;
		}
		if(objectName.equals(EVENTS_FINAL))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.JEventsCalendarToday, sqlSource);
			//dbEvents=null;
			if(dbEvents==null)
			{
				//dbEvents = EventDataArrayTools.readLocalEventsFromFile2(dataFile);
				ArrayList<EventData>rawEvents = EventDataArrayTools.readLocalEventsFromFile2(dataFile,sqlSource.getUrl());
				String path = StringUtils.path(dataFile);
				//String outFile = dbPath+="datasources/" + dataSource.getHost()+"/" + dataSource.getDatabase() + "/";
				DataFix.fixEvents(rawEvents, path + "/jeventsNormalized.xml");
				rawEvents.clear();
				rawEvents = EventDataArrayTools.readLocalEventsFromFile2(path + "/jeventsNormalized.xml",sqlSource.getUrl());
				
				//ArrayList<EventData>tmpR = EventDataArrayTools.sortByDate(rawEvents);
				ArrayList<EventData>tmpR = new ArrayList<EventData>();
				
				Comparator<EventData> evComparator = new EventComparator();
				if(rawEvents!=null)
				{
					Collections.sort(rawEvents,evComparator);
				}
				tmpR.addAll(rawEvents);
				ArrayList<Category>cats = getByType(ECMContentSourceType.JEventsCategory);
			  	if(cats!=null && cats.size() > 0)
			  	{
			  		EventDataArrayTools.updateCategoryTitle("en", tmpR, cats);
			  	}
			  	
			  	ArrayList<LocationData>locs = getByType(ECMContentSourceType.JEventsLocationItem);
			  	if(cats!=null && cats.size() > 0)
			  	{
			  		EventDataArrayTools.updateLocationData(tmpR, locs);
			  	}
			  	
			  	dbEvents = new ArrayList<EventData>();
			  	dbEvents.addAll(tmpR);
			  	tmpR.clear();
			  	
			  	
			  	
			  	
			  	
				
				//dbEvents = EventDataArrayTools.updateCategoryData(dbEvents, get(CATS_EVENTS));
				//dbEvents = EventDataArrayTools.updateLocationData(dbEvents, get(LOCATIONS3));
				/*
				eventsFinal=null;
				if(eventsFinal==null)
				{
					ArrayList<EventData> fb = get(DomainCache.EVENTS_FB_TRUSTED);
					ArrayList<EventData> db = get(DomainCache.EVENTS_DB);
					//ArrayList<EventData> tickets = get(DomainCache.EVENTS_TICKETS_TRUSTED);
					ArrayList<EventData> tickets = get(DomainCache.EVENTS_TICKETS_ALL);
		
					if(eventsFinal ==null)
						eventsFinal = new ArrayList<EventData>();
		
					eventsFinal = EventFilterTools.combine(db,fb);
					eventsFinal = EventFilterTools.combine(eventsFinal, tickets);
					
					ArrayList<EventData>tmpR = EventDataArrayTools.sortByDate(eventsFinal);
				  	eventsFinal.clear();
				  	eventsFinal.addAll(tmpR);
				  	tmpR.clear();
				}
				return eventsFinal;
				*/
			}
			return dbEvents;
		}
		
		if(objectName.equals(CATS_MOSET))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.MosetTreeCategory, sqlSource);
			//mosetCategories=null;
			if(sqlSource.getType().equals("JoomlaXML"))
			{
				mosetCategories= CategoryTools.readFromRPCFile(dataFile, null,ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MosetTreeCategory));
			}else
			{
				if(mosetCategories==null)
				{
					mosetCategories=CategoryTools.readMosetCategoriesFromFile(dataFile, "com_mtree",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MosetTreeCategory));
				}
			}
			
			Comparator<Category> catAlpComparator = new CategoryComparator();
			Collections.sort(mosetCategories, catAlpComparator);

			for(int dIndex= 0 ; dIndex <  mosetCategories.size() ; dIndex++ )
			{
				Category c = mosetCategories.get(dIndex);
				c.hasSubCategories = CategoryTools.hasSubCategories(mosetCategories, c.refId);
			}
			return mosetCategories;
		}
		
		if(objectName.equals(MOSET_LINKS))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.MosetTreeItem, sqlSource);
			if(mosetItems==null)
			{
				mosetItems= LocationArrayTools.readMosetItemsFromFile(dataFile,sqlSource.getUrl());
			}
			return mosetItems;
		}
		
		if(objectName.equals(JBREEZING_FORM))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.BreezingForm, sqlSource);
			if(jBreezingForms==null)
			{
				CList forms =  BreezingFormTools.readBreezingForms(dataFile, "",0,sqlSource.getUrl());
				if(forms!=null)
				{
					jBreezingForms = new ArrayList<CList>();
					jBreezingForms.add(forms);
				}
			}
			return jBreezingForms;
		}
		if(objectName.equals(GCALENDARS))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.GoogleCalendar, sqlSource);
			gCalendars=null;
			if(gCalendars==null)
			{
				if(! new File(dataFile).exists())
				{
					return null;
				}
			
				String data = StringUtils.readFileAsString(dataFile);
				
				if(data==null){
					return null;
				}
			
				CContentList result=null;
				JSONDeserializer derializerSC = new JSONDeserializer<CContentList>();
		    	if(data!=null && data.length() > 0)
		    	{
		    			try {
		    				result= (CContentList) derializerSC.deserialize(data);	
						} catch (Exception e) {
							// TODO: handle exception
						}
		    	}
		    	if(result!=null){
		    		gCalendars=result.getItems();
		    	}
			}
			return gCalendars;
		}
		if(objectName.equals(GCALENDAR_ITEMS))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.GoogleCalendarItem, sqlSource);
			gCalendarEvents=null;
			if(gCalendarEvents==null)
			{
			
				String data = StringUtils.readFileAsString(dataFile);
			
				CContentList result=null;
				JSONDeserializer derializerSC = new JSONDeserializer<CContentList>();
		    	if(data!=null && data.length() > 0)
		    	{
		    			try {
		    				result= (CContentList) derializerSC.deserialize(data);	
						} catch (Exception e) {
							// TODO: handle exception
						}
		    	}
		    	if(result!=null){
		    		Comparator<CContent> evComparator = new CEventComparator();
		    		gCalendarEvents=result.getItems();
					Collections.sort(gCalendarEvents,evComparator);
		    	}
			}
			return gCalendarEvents;
		}
		if(objectName.equals(GDOC_FOLDERS))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.GoogleDocumentFolder, sqlSource);
			gDocumentFolders=null;
			if(gDocumentFolders==null)
			{
			
				String data = StringUtils.readFileAsString(dataFile);
			
				CContentList result=null;
				JSONDeserializer derializerSC = new JSONDeserializer<CContentList>();
		    	if(data!=null && data.length() > 0)
		    	{
		    			try {
		    				result= (CContentList) derializerSC.deserialize(data);	
						} catch (Exception e) {
							// TODO: handle exception
						}
		    	}
		    	if(result!=null){
		    		gDocumentFolders=result.getItems();
		    	}
			}
			return gDocumentFolders;
		}
		
		if(objectName.equals(GDOC_ITEMS))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.GoogleDocumentItem, sqlSource);
			gDocumentItems=null;
			if(gDocumentItems==null)
			{
			
				String data = StringUtils.readFileAsString(dataFile);
			
				CContentList result=null;
				JSONDeserializer derializerSC = new JSONDeserializer<CContentList>();
		    	if(data!=null && data.length() > 0)
		    	{
		    			try {
		    				result= (CContentList) derializerSC.deserialize(data);	
						} catch (Exception e) {
							// TODO: handle exception
						}
		    	}
		    	if(result!=null){
		    		gDocumentItems=result.getItems();
		    	}
			}
			return gDocumentItems;
		}
		
		
		if(objectName.equals(GPICASSA_ALBUMS))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.GooglePicassaAlbum, sqlSource);
			gPicassaAlbums=null;
			if(gPicassaAlbums==null)
			{
				CList albums =  DataSourceCache.fromXML(dataFile);
				if(albums!=null){
					gPicassaAlbums = new ArrayList<CList>();
					gPicassaAlbums.add(albums);
				}
				//gPicassaAlbums=CategoryTools.readWPCatsFromFile(dataFile, "com_content",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.WordpressCategory));
			}
			return gPicassaAlbums;
		}
		if(objectName.equals(GPICASSA_PHOTOS))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.GooglePicassaItem, sqlSource);
			if(gPicassaPhotos==null)
			{
				gPicassaPhotos= DataSourceCache.listFromXML(dataFile);
				//gPicassaPhotos.add(albums);
				//gPicassaAlbums=CategoryTools.readWPCatsFromFile(dataFile, "com_content",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.WordpressCategory));
			}
			return gPicassaPhotos;
		}
		if(objectName.equals(WPCATEGORIES))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.WordpressCategory, sqlSource);
			if(wpCategories==null)
			{
				wpCategories=CategoryTools.readWPCatsFromFile(dataFile, "com_content",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.WordpressCategory));
			}
			return wpCategories;
		}
		if(objectName.equals(WPPOSTS))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.WordpressPost, sqlSource);
			//wpPosts=null;
			if(wpPosts==null)
			{
				wpPosts = ArticleTools.readWPostsFromFile(dataFile);
			}
			return wpPosts;
		}
		
		if(objectName.equals(JCATEGORY))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.JoomlaCategory, sqlSource);
			//articleCategories=null;
			
			if(articleCategories==null)
			{
				
				if(sqlSource.getType().equals("JoomlaXML"))
				{
					articleCategories= CategoryTools.readFromRPCFile(dataFile, null,ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory));
				}else
				{
					if(sqlSource.getVersion().equals("1.7")){
						articleCategories= CategoryTools.readFromFile17(dataFile, "com_content",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory));
					}else{
						articleCategories=CategoryTools.readFromFile(dataFile, "com_content",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory));
					}
				}
			}
			return articleCategories;
		}
		
		if(objectName.equals(JSECTION))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.JoomlaSection, sqlSource);
			//articleSections=null;
			
			if(articleSections==null)
			{
				if(sqlSource.getType().equals("JoomlaXML")){
					articleSections= CategoryTools.readFromRPCFile(dataFile, null,ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaSection));
				}else
				{
					if(sqlSource.getVersion().equals("1.7"))
					{
						articleSections= CategoryTools.readFromFile17(dataFile, "com_content",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaSection));
					}else{
						articleSections=CategoryTools.readFromFile(dataFile, "com_content",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaSection));
					}
				}
			}
			return articleSections;
		}
		
		if(objectName.equals(JBANNER_CATEGORY))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.JoomlaBannerCategory, sqlSource);
			if(joomlaBannerCategories==null)
			{
				if(sqlSource.getVersion().equals("1.7")){
					joomlaBannerCategories= CategoryTools.readFromFile17(dataFile, "com_banner",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaBannerCategory));
				}else{
					joomlaBannerCategories=CategoryTools.readFromFile(dataFile, "com_banner",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaBannerCategory));
				}
			}
			return joomlaBannerCategories;
		}
		
		if(objectName.equals(JBANNERS))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.JoomlaBannerItem, sqlSource);
			//joomlaBanners=null;
			if(joomlaBanners==null)
			{
				joomlaBanners=JBannerTools.readJBanner(dataFile, ECMContentSourceType.JoomlaBannerItem,sqlSource.getUrl());
			}
			return joomlaBanners;
		}

		
		if(objectName.equals(JARTICLES))
		{
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.JoomlaArticle, sqlSource);
			//jArticles=null;
			if(jArticles==null)
			{
				jArticles = ArticleTools.readJArticlesFromFile(dataFile);
			}
			return jArticles;
		}
		
		/***
		 * 
		 */
		// point to domain directory !
		String pathSuffix = SettingsUtil.getProperty("webapp.root");
		if( domain !=null && domain.length() > 0)
		{
			pathSuffix = pathSuffix + "db/" + domain + "/";
		}


		/////////////////////////////////////////////////////////////////////////////////
		//
		//	Final
		//
		if(objectName.equals(EVENTS_FINAL))
		{
			eventsFinal=null;
			if(eventsFinal==null)
			{
				ArrayList<EventData> fb = get(DomainCache.EVENTS_FB_TRUSTED);
				ArrayList<EventData> db = get(DomainCache.EVENTS_DB);
				//ArrayList<EventData> tickets = get(DomainCache.EVENTS_TICKETS_TRUSTED);
				ArrayList<EventData> tickets = get(DomainCache.EVENTS_TICKETS_ALL);
	
				if(eventsFinal ==null)
					eventsFinal = new ArrayList<EventData>();
	
				eventsFinal = EventFilterTools.combine(db,fb);
				eventsFinal = EventFilterTools.combine(eventsFinal, tickets);
				
				ArrayList<EventData>tmpR = EventDataArrayTools.sortByDate(eventsFinal);
			  	eventsFinal.clear();
			  	eventsFinal.addAll(tmpR);
			  	tmpR.clear();
			}
			return eventsFinal;

		}
		/////////////////////////////////////////////////////////////////////////////////
		//
		//	Tickets
		//
		if(objectName.equals(EVENTS_TICKETS_ALL))
		{
			String ticketsPath =  pathSuffix + "tickets" + ".xml";
			if(ticketsAll == null)
			{
				ticketsAll = new ArrayList<EventData>();

				ArrayList<String> tickets = SettingsUtil.getTicketFiles(domain);

				ArrayList<EventData> ticketsTmp = new ArrayList<EventData>();


				for (int fi = 0; fi < tickets.size() ; fi++)
			   		{
					    String f = tickets.get(fi);
					    ticketsTmp.addAll(EventDataArrayTools.readLocalTicketsFromFileInternalFormat( f ));
			   		}

					for (int s = 0; s < ticketsTmp.size() ; s++)
			   		{
					    EventData e = ticketsTmp.get(s);
					    if	(	e !=null && e.location !=null )
					    {
					    	ticketsAll.add(e);
					    }
			   		}
					ticketsAll = EventDataArrayTools.updateLocation(ticketsAll, get(LOCATIONS3) ,domain,dataSource);
			}
			return ticketsAll;
		}

		if(objectName.equals(EVENTS_TICKETS_TRUSTED))
		{

			if(ticketsTrusted == null)
			{
				ticketsAll = get(EVENTS_TICKETS_ALL);
				ticketsTrusted = new ArrayList<EventData>();

				if(trustedTicketLocations !=null)
				{
					trustedTicketLocations.clear();

				}
				
				for (int s = 0; s < ticketsAll.size() ; s++)
		   		{
				    EventData e = ticketsAll.get(s);
				    if	(	e !=null && e.loc !=null )
					{
					}
				    if	(	e !=null &&
				    		e.loc !=null
				    	)
				    {
				    	ticketsTrusted.add(e);
				    }
		   		}
			}
			return ticketsTrusted;
		}

		/////////////////////////////////////////////////////////////////////////////////
		//
		//	Facebook
		//


		if(objectName.equals(EVENTS_FB_ALL))
		{
			String fbEventsPath =  pathSuffix + "fbevents" + ".xml";
			if(fbEventsAll == null)
			{
				fbEventsAll = EventDataArrayTools.readFBEventsFromFile(fbEventsPath);
			}
			return fbEventsAll;
		}


		if(objectName.equals(EVENTS_FB_CLEANED))
		{

			if(fbEventsCleaned == null)
			{
				if(fbUserEvents!=null)
					fbUserEvents.clear();

				fbEventsAll = get(EVENTS_FB_ALL);
				fbEventsCleaned = new ArrayList<EventData>();
				//fbEventsCleaned = EventDataArrayTools.updateLocation(fbEventsAll,get(LOCATIONS),domain,dataSource);
			}
			return fbEventsCleaned;
		}


		if(objectName.equals(EVENTS_FB_TRUSTED))
		{

			if(fbEventsTrusted == null)
			{

				if(fbUserEvents!=null)fbUserEvents.clear();

				fbEventsCleaned = get(EVENTS_FB_CLEANED);

				if(blackListFBUsers!=null)blackListFBUsers.clear();
				if(blackListFBEvents!=null)blackListFBEvents.clear();

				fbEventsTrusted = new ArrayList<EventData>();

				// extra information

			
				for (int s = 0; s < fbEventsCleaned.size() ; s++)
		   		{
				    EventData e = fbEventsCleaned.get(s);
				    e.eventSourceType = "Facebook";
				    if	(	e !=null &&  e.loc !=null
				    	)
				    {
				    	if( e.creator_id !=null && trustedFBUsers.containsKey( e.creator_id ))
				    	{
					  		fbEventsTrusted.add(e);
				    	}
				    }
		   		}
			}
			return fbEventsTrusted;
		}

		if(objectName.equals(JARTICLES))
		{
			
			String dataFile = applicationManager.getDataPath(application, ECMContentSourceType.JoomlaArticle, sqlSource);

			if(jArticles==null)
			{
				
				String locFile =  pathSuffix + "jarticles" + ".xml";
				System.out.println("loading " + locFile);
				jArticles = ArticleTools.readJArticlesFromFile(dataFile);
			}
			return jArticles;
		}
		//
		//		translations
		//
		if(objectName.equals(TRANSLATIONS))
		{
			
			if(translations ==null)
			{
				translations= new ArrayList<TranslationData>();
				

	
				// IT article translations
				for(int i  = 0 ; i < 6 ; i++)
				{
					String locFile =  pathSuffix + "rArticlesl" + getLangCodeIT(i) + ".xml";
					ArrayList<TranslationData> tmpTranslations = TranslationTools.readITArticleTranslationFromFile(locFile);
					translations.addAll(tmpTranslations);
				}
				
				// Location Category Translation 
				for(int i  = 1 ; i < 7 ; i++)
				{
					String locFile =  pathSuffix + "categoriesl" + i + ".xml";
					ArrayList<TranslationData> tmpTranslations = TranslationTools.readLocationCategoryTranslationsFromFile(locFile);
					translations.addAll(tmpTranslations);
				}
				
				// Location Category Translation 
				for(int i  = 1 ; i < 7 ; i++)
				{
					String locFile =  pathSuffix + "ecategoriesl" + i + ".xml";
					ArrayList<TranslationData> tmpTranslations = TranslationTools.readEventCategoryTranslationsFromFile(locFile);
					translations.addAll(tmpTranslations);
				}
				
				// Joomla Article Translation 
				for(int i  = 1 ; i < 6 ; i++)
				{
					String locFile =  pathSuffix + "jarticlesl" + i + ".xml";
					ArrayList<TranslationData> tmpTranslations = TranslationTools.readJArticleTranslationsFromFile(locFile);
					translations.addAll(tmpTranslations);
					System.out.println("loading " + locFile);
				}
				
				// Joomla Location Translation 
				for(int i  = 1 ; i < 7 ; i++)
				{
					String locFile =  pathSuffix + "jlocationsl" + i + ".xml";
					ArrayList<TranslationData> tmpTranslations = TranslationTools.readJLocationTranslationsFromFile(locFile);
					translations.addAll(tmpTranslations);
				}
				// Joomla Events  
				for(int i  = 1 ; i < 6 ; i++)
				{
					String locFile =  pathSuffix + "jeventsl" + i + ".xml";
					ArrayList<TranslationData> tmpTranslations = TranslationTools.readJLocationTranslationsFromFile(locFile);
					translations.addAll(tmpTranslations);
				}
				
				// Joomla Menu Strings  
				for(int i  = 1 ; i < 6 ; i++)
				{
					String locFile =  pathSuffix + "jstringsl" + i + ".xml";
					ArrayList<TranslationData> tmpTranslations = TranslationTools.readJStringsTranslationsFromFile(locFile);
					translations.addAll(tmpTranslations);
				}
			}
			
			return translations;
		}
		
		//
		//		translations
		//
		if(objectName.equals(MAPPINGS))
		{
			
			if(mappings==null)
			{
				mappings= new ArrayList<MappingData>();
				String locFile =  pathSuffix + "mappings" + ".xml";
				mappings = ArticleTools.readMappingsFromFile(locFile);
			}
			return mappings;
		}
		
		if(objectName.equals(LOCATIONS3))
		{
			if(locations ==null)
			{
				String locFile =  pathSuffix + "locations3" + ".xml";
				locations = LocationArrayTools.readFromFile(locFile);
				locations = LocationArrayTools.updateLocationCategories(locations,get(CATS_LOCATIONS));
				Comparator<LocationData> locAlpComparator = new LocationComparator();
				if(locations!=null)
				{
					Collections.sort(locations,locAlpComparator);
				}
			}
			return locations;
		}

		//
		//		Location Categories
		//
		if(objectName.equals(CATS_LOCATIONS))
		{
			if(locationCategories ==null)
			{
				String catFile =  pathSuffix + "categories" + ".xml";
				locationCategories = CategoryTools.readFromFile(catFile,"com_jevlocations2",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationCategory));
				Comparator<Category> catAlpComparator = new CategoryComparator();
				Collections.sort(locationCategories, catAlpComparator);
				
				for(int dIndex= 0 ; dIndex <  locationCategories.size() ; dIndex++ )
				{
					Category c = locationCategories.get(dIndex);
					c.hasSubCategories = CategoryTools.hasSubCategories(locationCategories, c.id);
				}
				return locationCategories;
			}
			return locationCategories;
		}
		
		//
		//		Location Categories
		//
		if(objectName.equals(REGIONS))
		{
			if(regions ==null)
			{
				String catFile =  pathSuffix + "categories" + ".xml";
				regions = CategoryTools.readFromFile(catFile,"com_jevlocations",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationCategory));
				Comparator<Category> catAlpComparator = new CategoryComparator();
				Collections.sort(regions, catAlpComparator);
				return regions;
			}
			return regions;
		}

		//
		//		Event Categories
		//
		if(objectName.equals(CATS_EVENTS))
		{
			if(eventCategories ==null)
			{

				String catFile =  pathSuffix + "categories" + ".xml";
				eventCategories =  CategoryTools.readFromFile(catFile,"com_jevents",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCategory));
				Comparator<Category> catAlpComparator = new CategoryComparator();
				Collections.sort(eventCategories, catAlpComparator);
				return eventCategories;
			}
			return eventCategories;
		}

		if(objectName.equals(MENU))
		{
			if(eventCategories ==null)
			{
				String catFile =  pathSuffix + "jstrings" + ".xml";

				menues =  MenuDataTools.readMenuData(catFile);
				return menues;
			}
			return menues;
		}
		
		if(objectName.equals(RESOURCES))
		{
			if(resources ==null)
			{
				String catFile =  pathSuffix + "resources" + ".xml";

				resources =  ResourceTools.readResourcesFromFile(catFile);
				return resources;
			}
			return resources;
		}

		
		if(objectName.equals(APPCONFIGS))
		{
			if(appConfigs==null)
			{
				String catFile =  pathSuffix + "appSettings" + ".xml";
				appConfigs=  ApplicationConfigurationTools.readAppConfigFromFile(catFile);
				return appConfigs;
			}
			return appConfigs;
		}
	
		if(objectName.equals(RATINGS))
		{
			if(ratings==null)
			{
				String catFile =  pathSuffix + "ratings" + ".xml";
				ratings=  RatingTools.readRatingsFromFile(catFile);
				return ratings;
			}
			return ratings;
		}
		//
		//		Events DB
		//
		if(objectName.equals(EVENTS_DB))
		{
			if(dbEvents ==null)
			{
				String localEvents=  pathSuffix + "events2" + ".xml";
				dbEvents = EventDataArrayTools.readLocalEventsFromFile2(localEvents,sqlSource.getUrl());
				dbEvents = EventDataArrayTools.updateCategoryData(dbEvents, get(CATS_EVENTS));
				dbEvents = EventDataArrayTools.updateLocationData(dbEvents, get(LOCATIONS3));
			}
			return dbEvents;
		}
		return null;
	}
	public String getLangCodeIT(int index)
	{
		switch (index) 
		{
			case 0:
				return "ca";
			case 1:
				return "de";
			case 2:
				return "en";
			case 3:
				return "es";
			case 4:
				return "fr";
			case 5:
				return "it";
		}
		return "";
	}
	
	public String getLangCodeJoomla(int index)
	{
		switch (index) 
		{
			case 0:
				return "ca";
			case 1:
				return "de";
			case 2:
				return "en";
			case 3:
				return "es";
			case 4:
				return "fr";
			case 5:
				return "it";
		}
		return "";
	}
	
	public void cacheEventSource(String userID, EventData event)
	{

		if(fbUserEvents ==null)
			fbUserEvents = new HashMap<String, ArrayList<EventData>>();

		if(ticketLocationEvents ==null)
			ticketLocationEvents = new HashMap<String, ArrayList<EventData>>();

		if(event.eventSourceType.equals("Facebook"))
        {
			//get the list
			ArrayList<EventData> eList = fbUserEvents.get(userID);
			if(eList ==null)
				eList = new ArrayList<EventData>();

			if(!eList.contains(event))
			{
				eList.add(event);
				fbUserEvents.put(userID, eList);
			}
        }

		if(event.eventSourceType.equals("Tickets"))
        {
			//get the list
			ArrayList<EventData> eList = ticketLocationEvents.get(userID);
			if(eList ==null)
			{
				eList = new ArrayList<EventData>();

			}
			if(!eList.contains(event))
			{
				eList.add(event);
				ticketLocationEvents.put(userID, eList);
			}
        }
	}
	
	public ArrayList<EventData>getRange( Date s, Date e )
	{
		if(this.ccIsDirty)
			return null;

		if(this.ccMD == null)
			return null;

		return null;
	}

	////////////////////////////////////////////////////////
	//
	//		Extra Elements : Locations , Categories
	//
	public int insert(EventData e)
	{

		Date current = new Date();
		int cyear = current.getYear();
		int syear = e.start_time.getYear();
		int sday = e.start_time.getDate();

		Date eDate = e.start_time;

		// year check
		if(cyear!=syear){ return -1; }

		if(e.start_time.before(current))
			return 0;

		int smonth = e.start_time.getMonth();

		if(ccMD ==null)
		{
			ccMD = new HashMap<Integer, HashMap<Integer,ArrayList<EventData>>>() ;
		}

		// get the month map
		HashMap<Integer,ArrayList<EventData>> ccm = ccMD.get(smonth);
		if(ccm ==null)
			ccm = new HashMap<Integer, ArrayList<EventData>>();






		// get the day list :
		ArrayList<EventData>dlist = ccm.get(sday);
		if(dlist ==null)
		{
			dlist = new  ArrayList<EventData>();
		}


		//insert the event
		dlist.add(e);
		ccm.put(sday, dlist );


		ccMD.put(smonth, ccm);

		return 0;
	}
	
}
