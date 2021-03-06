package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmedia.DataJSONTransformer.BaseDataTransformer;
import pmedia.DataJSONTransformer.EventDataTransformer;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.BaseDataArrayTools;
import pmedia.DataUtils.EventDataArrayTools;
import pmedia.DataUtils.LocationArrayTools;
import pmedia.DataUtils.LocationPropertyTools;
import pmedia.DataUtils.MediaUtils;
import pmedia.SearchBeans.EventSearch;
import pmedia.html.factory.WhiteListDataFactory;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.CContent;
import pmedia.types.CEventItem;
import pmedia.types.CGroupedList;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.Category;
import pmedia.types.Constants;
import pmedia.types.EventData;
import pmedia.types.LocationData;
import pmedia.types.MediaItemBase;
import pmedia.types.PMDataTypes;
import pmedia.types.SectionedEvents;
import pmedia.utils.CEventListItemTools;
import pmedia.utils.CListItemTools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import pmedia.utils.TimeUtils;

import cmx.cache.DataSourceCache;
import cmx.data.CContentFactory;
import cmx.data.CContentStorageUtils;
import cmx.manager.GTrackerManager;
import cmx.types.ApplicationManager;
import cmx.types.ECMContentSourceType;

import flexjson.JSONSerializer;

public class EventsServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
    private static final String __HUMAN_ID = "ID";
    public void doGetContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	getCMObjects(request, response, true);
    	GTrackerManager.trackXAPPEvent(GTrackerManager.openArticle,"JEvents");
    	if(application==null)
    	{
    		return404Page();
    	}
    	DataSourceCache dsc=null;
    	try {
    		dsc = ServerCache.getDSC(appManager, application, ds);	
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	ArrayList events=dsc.getByType(ECMContentSourceType.JEventsItem);
      	EventData event = (EventData)BaseDataArrayTools.getByIndex(events, refId);
      	if(event==null){
      		
      		return404Page();
      	}
      	CContent content = dsc.getAsCContent(ECMContentSourceType.JEventsItem,""+refId, uuid, appIdentifier, dataSource, lang, Constants.USERAGENT_IPHONE);
      	JSONSerializer serializer = new JSONSerializer();
      	serializer.exclude("listItem");
      	serializer.exclude("asListItem");
      	serializer.exclude("treeUid");
      	String serialized  = serializer.deepSerialize(content);
      	sendOutput(request,response,serialized);
    	
    }

    public void doGetList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	EventSearch lastEventSearch = (EventSearch)request.getSession().getAttribute("lastEventSearch");
    	if(lastEventSearch==null)
    	{
    		lastEventSearch = new EventSearch();
    		request.getSession().setAttribute("lastEventSearch",lastEventSearch);
    	}

    	lastEventSearch.setShowPast(false);

    	String startDateStr = request.getParameter("startDate");
    	Date startDate = new Date();
    	if(startDateStr!=null && startDateStr.length() > 0)
    	{
    		startDate=TimeUtils.fromDojoSpinWheel(startDateStr);
    		request.getSession().setAttribute("lastStartDate",startDate);
    	}
    	
    	int catId = 0;
    	String catIdStr = request.getParameter("cat");
    	if(catIdStr!=null)
    	{
    		try {
				catId = Integer.parseInt(catIdStr);
			} catch (Exception e) {
				catId = 0;
			}
    	}
    	
    	if(dataSourceCache==null){
    		return404Page();
    		return;
    	}
    	
    	
    	ArrayList<EventData> allEvents = dataSourceCache.getByType(ECMContentSourceType.JEventsCalendarToday);
    	ArrayList<EventData>events = lastEventSearch.searchEventsAll(allEvents,"", false, "ibiza", "false", null, null,startDate,catId);
    	
    	
    	//String savepath = appManager.getUserAppPath(uuid, application.getApplicationIdentifier());
		//savepath+="/datasources/" + ds.getHost() + "/" + ds.getDatabase() + "/";
		
		/*
		//update groupStr
		ArrayList<Category>cats = dataSourceCache.getByType(ECMContentSourceType.JEventsCategory);
	  	if(cats!=null && cats.size() > 0)
	  	{
	  		EventDataArrayTools.updateCategoryTitle(lang, events, cats);
	  	}
	  	
	  	ArrayList<LocationData>locs = dataSourceCache.getByType(ECMContentSourceType.JEventsLocationItem);
	  	if(cats!=null && cats.size() > 0)
	  	{
	  		EventDataArrayTools.updateLocationData(events, locs);
	  	}
	  	*/
		
		/*
		CContentStorageUtils.saveAsCContentData(
					events, 
					savepath, 
					"json", 
					uuid, 
					application.getApplicationIdentifier(), 
					ds.getUid(), 
					ECMContentSourceType.JEventsItem, 
					WhiteListDataFactory.mobileBase(application.getApplicationIdentifier()), 
							pmedia.types.Constants.USERAGENT_IPHONE_NATIVE, lang);

		*/
    	
    	Vector currentPage = lastEventSearch.getNextPage();
    	EventDataArrayTools.sort(currentPage);
    	
    	@SuppressWarnings("unchecked")
		ArrayList cevents =CListItemTools.toListItems((Vector<BaseData>)currentPage,null, ECMContentSourceType.JEventsItem,getDs(),StringUtils.getLocaleFromLang(lang));
    	CListItemTools.setBaseRef(cevents, getDs());
    	CGroupedList groupedList = CEventListItemTools.createGoupedListByStartDate(
    			cevents,
    			appManager, 
    			application, 
    			getDs(), 
    			StringUtils.getLocaleFromLang(lang),4);
    	
    	groupedList.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCalendarToday);
    	groupedList.baseRef=getDs().getUrl();
    	groupedList.refId=-1;
    	
    	//ArrayList<Category> cListAll =  ServerCache.getInstance().getDC("ibiza").get(DomainCache.CATS_LOCATIONS);
    	
    	//EventDataArrayTools.updateLocalizedCategoryTitle("ibiza", lang, events, cListAll);
    	
    	ArrayList<SectionedEvents>outListSectioned = lastEventSearch.buildSectionedEvents(currentPage,StringUtils.getLocaleFromLang(lang));
    	
    	JSONSerializer serializer = new JSONSerializer();
    	//serializer.transform(new EventDataTransformer(true,pmedia.types.Constants.USERAGENT_TABLET,null,"iconU r l",lang), "events.iconUrl");
    	
    	serializer.include("items");
    	serializer.include("items.items");
    	/*
    	serializer.include("events");
    	serializer.include("events.localizedCategoryTitle");
    	serializer.exclude("events.description");
    	*/
    	
    	String out =  serializer.deepSerialize(groupedList);
    	//System.out.println(out);
    	//serializerS.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET,null,"iconU r l",lang), "events");
    	//serializer.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET,null,"iconU r l",lang), "iconUrl");
    	//serializer.exclude("events.s t a t i c M a p U r l");
    	//serializer.exclude("events.descriptionNoPictures");
    	sendOutput(request,response,out);
    	
    }
    
    public void doGetDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String lang=(String)request.getSession().getAttribute("lang");
    	if(lang==null)
    	{
    		lang = "en";
    	}
    	String evtID = request.getParameter("id");
    	
    	
    	
    	EventSearch lastEventSearch = (EventSearch)request.getSession().getAttribute("lastEventSearch");
    	ArrayList<EventData> elist = ServerCache.getInstance().getDC("ibiza").get(DomainCache.EVENTS_FINAL);
    	
    	EventData e  = EventDataArrayTools.getByUID(elist, evtID);
    	if(e==null)
    		return;
    	
    	if(e.locRefId==0)
    	{
    		if(e.loc!=null)
    		{
    			e.locRefId = e.loc.location_id;
    		}else{
	    		ArrayList<LocationData> lList = ServerCache.getInstance().getDC("ibiza").get(DomainCache.LOCATIONS3);
	    		if(e.location!=null && e.location.length() > 0)
	    		{
	    			LocationData loc = LocationArrayTools.getLocationByName(lList, e.location);
	    			if(loc!=null)
	    			{
	    				e.locRefId=loc.location_id;
	    			}
	    		}
    		}
    	}
    	
    
    	
    	JSONSerializer serializer = new JSONSerializer();
    	serializer.transform(new EventDataTransformer(true,pmedia.types.Constants.USERAGENT_TABLET,e,"iconUrl",lang), "iconUrl");
    	serializer.transform(new BaseDataTransformer(true,pmedia.types.Constants.USERAGENT_TABLET,e,"descriptionPicture"), "descriptionPicture");
    	if(e.getGalleryFiles()!=null && e.getGalleryFiles().size() > 0)
    	{
    		serializer.include("galleryFiles");
    		serializer.include("galleryThumbnailFiles");
    		serializer.include("galleryTitles");
    	}
    	
    	// it has a location, create a static map be default : 
    	if(e.loc!=null)
    	{
    		e.setMapUrl(LocationPropertyTools.getMapUrl(e.loc, request.getSession()));
    		//System.out.println(e.getMapUrl() + "\n "  + request.getSession().getId());
    		
    	}else
    	{
    		//e.setS t a t i c M a p U r l(null);
    		serializer.exclude("mapUrl");
    	}
    	//serializer.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET,e,"s t a t i c M a p U r l",lang), "s t a t i c M a p U r l");
    	
    	String jsonres = serializer.serialize(e);
    	sendOutput(request,response,jsonres);
    	
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	getCMObjects(request, response, false);
    	if(!preventCache){
	    	String cachedResponse = getCachedResponse(null);
	    	if(cachedResponse!=null){
	    		sendCachedOutput(request, response, cachedResponse);
	    		return;
	    	}
    	}
    	
    	String action= request.getParameter("action");
    	if(action.equals("list"))
    	{
    		doGetList(request, response);
    	}
    	if(action.equals("content"))
    	{
    		doGetContent(request, response);
    	}
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
