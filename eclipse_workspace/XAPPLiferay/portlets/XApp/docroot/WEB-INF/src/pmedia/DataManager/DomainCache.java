package pmedia.DataManager;

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

import cmx.types.ECMContentSourceType;

import pmedia.DataUtils.ApplicationConfigurationTools;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.EventDataArrayTools;
import pmedia.DataUtils.LocationArrayTools;
import pmedia.DataUtils.MenuDataTools;
import pmedia.DataUtils.RatingTools;
import pmedia.DataUtils.ResourceTools;
import pmedia.DataUtils.TranslationTools;
import pmedia.types.ArticleData;
import pmedia.types.Category;
import pmedia.types.CategoryComparator;
import pmedia.types.EventData;
import pmedia.types.LocationComparator;
import pmedia.types.LocationData;
import pmedia.types.MappingData;
import pmedia.types.MenueData;
import pmedia.types.Rating;
import pmedia.types.TranslationData;
import pmedia.types.ResourceData;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.EventFilterTools;

import pmedia.utils.SettingsUtil;

public class DomainCache
{

	//public static DomainCache instance = null;
	public static DomainCache getInstance(String domain)
	{
		return ServerCache.getInstance().getDC(domain);
	}

	public DataSource dataSource = null;


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
	
	//public static final String LOCATIONS = "Locations";
	//public static final String LOCATIONS2 = "locations2";
	public static final String LOCATIONS3 = "locations3";
	public static final String RATINGS = "ratings";
	public static final String COMMENTS = "comments";
	public static final String THREADS = "threads";
	
	public static final String JARTICLES = "JARTICLES";
	
	public static final String ARTICLES = "articles";
	public static final String TRANSLATIONS= "translations";
	public static final String MAPPINGS = "mappings";
	
	public static final String ANNOTATIONS= "annotations";
	public static final String REGIONS= "regions";
	
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
	
	public ArrayList<Category> mosetCategories = null;
	
	public ArrayList<Category> joomlaBannerCategories = null;
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
	
	public ArrayList<EventData>getRange( Date s, Date e )
	{
		if(this.ccIsDirty)
			return null;

		if(this.ccMD == null)
			return null;

		//System.arraycopy(src, srcPos, dest, destPos, length)


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

		//store day map
		ccMD.put(smonth, ccm);

		ArrayList<EventData> lop = ccm.get(23);


		// get day map for the above map
		//HashMap<Integer, EventData > ccd = ccm.get(sday);
		//if(ccd ==null) ccd = new HashMap<Integer, EventData>();



		int shour = e.start_time.getHours();



		return 0;
	}

	public ArrayList get(String objectName)
	{

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
				try
				{
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

				} catch (ParserConfigurationException e)
				{
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
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
				//ticketsAll = EventDataArrayTools.updateLocation(ticketsAll, get(LOCATIONS) ,domain,dataSource);
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
				    //	HashMap<Integer, Integer> categoryMapping = DBTools.getCategoryMappings(dataSource);
				    //	e = EventDataArrayTools.updateTicketCategory(e, trustedTicketLocations ,get(CATS_EVENTS),categoryMapping );
				   // 	cacheEventSource(Integer.toString(e.loc.location_id), e);
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
				try {

					fbEventsAll = EventDataArrayTools.readFBEventsFromFile(fbEventsPath);

				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
					return fbEventsAll;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
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
				    		/*ArrayList<EventData> cList =null;
		  			  		if(fbUserEvents !=null)
		  			  			cList=fbUserEvents.get(e.creator_id);*/
				    		//HashMap<Integer, Integer> categoryMapping = DBTools.getCategoryMappings(dataSource);
				    		//e= EventDataArrayTools.updateFBCategory(e, trustedFBUsers,get(CATS_EVENTS), categoryMapping);
				    		//e.trustedFBUser = true;
		  			  		//cacheEventSource(e.creator_id, e);
		  			  		fbEventsTrusted.add(e);

				    	}
				    }
		   		}
			}
			return fbEventsTrusted;
		}
		//
		//		Location
		//
		/*
		if(objectName.equals(LOCATIONS))
		{
			if(locations ==null)
			{
				String locFile =  pathSuffix + "locations2" + ".xml";
				//DBTools.updateFromDB("xquery", "locations",domain);

				try {
					locations = LocationArrayTools.readFromFile(locFile);
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

				locations = LocationArrayTools.updateLocationCategories(locations,get(CATS_LOCATIONS));
				Comparator<LocationData> locAlpComparator = new LocationComparator();
				if(locations!=null)
				{
					Collections.sort(locations,locAlpComparator);
				}
			}
			return locations;
		}
		*/
		//
		//		articles
		//
		if(objectName.equals(ARTICLES))
		{
			if(articles ==null)
			{
				String locFile =  pathSuffix + "rArticles" + ".xml";
				try {
					articles = ArticleTools.readITArticlesFromFile(locFile);
				} catch (ParserConfigurationException e) 
				{
					e.printStackTrace();
				} catch (SAXException e) 
				{
					e.printStackTrace();
				} catch (IOException e) 
				{
					e.printStackTrace();
				} catch (ParseException e) 
				{
					e.printStackTrace();
				}
			}
			return articles;
		}
		//
		//		articles
		//
		if(objectName.equals(JARTICLES))
		{
			
			if(articles==null)
			{
				articles=get(ARTICLES);
			}
			
			if(jArticles==null)
			{
				
				String locFile =  pathSuffix + "jarticles" + ".xml";
				try 
				{
					System.out.println("loading " + locFile);
					jArticles = ArticleTools.readJArticlesFromFile(locFile);
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
				
				articles.addAll(jArticles);
			}
			
			return articles;
		}
		//
		//		translations
		//
		if(objectName.equals(TRANSLATIONS))
		{
			
			if(translations ==null)
			{
				translations= new ArrayList<TranslationData>();
				
				try {
					
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
				try {
					mappings = ArticleTools.readMappingsFromFile(locFile);
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
			}
			return mappings;
		}
		
		if(objectName.equals(LOCATIONS3))
		{
			if(locations ==null)
			{
				String locFile =  pathSuffix + "locations3" + ".xml";
				//DBTools.updateFromDB("xquery", "locations",domain);

				try {
					locations = LocationArrayTools.readFromFile(locFile);
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

				try {
					//DBTools.updateFromDB("xquery", "categories",domain);
					locationCategories = CategoryTools.readFromFile(catFile,"com_jevlocations2",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationCategory));
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Comparator<Category> catAlpComparator = new CategoryComparator();
				Collections.sort(locationCategories, catAlpComparator);
				
				for(int dIndex= 0 ; dIndex <  locationCategories.size() ; dIndex++ )
				{
					Category c = locationCategories.get(dIndex);
					c.hasSubCategories = CategoryTools.hasSubCategories(locationCategories, c.id);
					//System.out.println("has subs : "+ c.title + " : " + c.hasSubCategories );
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

				try {
					//DBTools.updateFromDB("xquery", "categories",domain);
					regions = CategoryTools.readFromFile(catFile,"com_jevlocations",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationCategory));
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

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

				//DBTools.updateFromDB("xquery", "categories",domain);
				String catFile =  pathSuffix + "categories" + ".xml";
				try {
					//DBTools.updateFromDB("xquery", "categories",domain);
					eventCategories =  CategoryTools.readFromFile(catFile,"com_jevents",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCategory));
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
				try {
					//DBTools.updateFromDB("xquery", "categories",domain);
					menues =  MenuDataTools.readMenuData(catFile);
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
				return menues;
			}

			return menues;
		}
		
		if(objectName.equals(RESOURCES))
		{
			if(resources ==null)
			{
				String catFile =  pathSuffix + "resources" + ".xml";
				try {
					//DBTools.updateFromDB("xquery", "categories",domain);
					resources =  ResourceTools.readResourcesFromFile(catFile);
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
				return resources;
			}
			return resources;
		}

		if(objectName.equals(APPCONFIGS))
		{
			if(appConfigs==null)
			{
				String catFile =  pathSuffix + "appSettings" + ".xml";
				try {
					//DBTools.updateFromDB("xquery", "categories",domain);
					appConfigs=  ApplicationConfigurationTools.readAppConfigFromFile(catFile);
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
				return appConfigs;
			}
			return appConfigs;
		}
	
		if(objectName.equals(RATINGS))
		{
			if(ratings==null)
			{
				String catFile =  pathSuffix + "ratings" + ".xml";
				try {
					//DBTools.updateFromDB("xquery", "categories",domain);
					ratings=  RatingTools.readRatingsFromFile(catFile);
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

			 	try {
					dbEvents = EventDataArrayTools.readLocalEventsFromFile2(localEvents,"");
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
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
	public DomainCache(String name)
	{
		domain = name;
	}
}
