package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.xml.sax.SAXException;

import pmedia.DataJSONTransformer.ArticleDataTranslationTransformer;
import pmedia.DataJSONTransformer.BaseDataTransformer;
import pmedia.DataManager.Cache;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.BaseDataArrayTools;
import pmedia.DataUtils.CEventDataArrayTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.EventDataArrayTools;
import pmedia.DataUtils.MediaUtils;
import pmedia.SearchBeans.CEventSearch;
import pmedia.SearchBeans.EventSearch;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.CContent;
import pmedia.types.CEventComparator;
import pmedia.types.CGroupedList;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.Category;
import pmedia.types.Domain;
import pmedia.types.EventComparator;
import pmedia.types.EventData;
import pmedia.types.LocationEventsComparator;
import pmedia.types.MediaItemBase;
import pmedia.types.PMDataTypes;
import pmedia.types.PictureTransformOptions;
import pmedia.utils.ApplicationContentTools;
import pmedia.utils.CContentTools;
import pmedia.utils.CEventListItemTools;
import pmedia.utils.CListItemTools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import pmedia.utils.TimeUtils;
import cmx.cache.DataSourceCache;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;
import cmx.types.ECMBannerSourceType;
import cmx.types.ECMContentSourceType;
import flexjson.JSONSerializer;


public class GoogleServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
    protected void doContent(PrintWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
    }
    
    public void doGetDocument(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	getCMObjects(request, response, true);
    	if(application==null)
    	{
    		return;
    	}
		if(ds==null){
			return;
		}
	
		DataSourceCache dsc=ServerCache.getDSC(appManager, application, ds);
		ArrayList<CList> albums=dsc.getByType(ECMContentSourceType.GooglePicassaAlbum);
		ArrayList<CListItem> entries=dsc.getByType(ECMContentSourceType.GooglePicassaItem);
		if(albums!=null && entries!=null)
		{
			CList cAlbums = albums.get(0);
			if(cAlbums!=null)
			{
				CListItem album = CListItemTools.getByRef(cAlbums.getItems(), ref);
				if(album!=null)
				{
					CList result= new CList();
					result.setTitle(album.getTitle());
					result.setRefIdString(ref);
					ArrayList< CListItem>items = CListItemTools.getByGroupId(entries,result.getRefIdString());
					
					result.setType(ECMContentSourceType.GooglePicassaAlbum);
					result.baseRef="https://picasaweb.google.com/";
			    	result.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GooglePicassaAlbum);
					result.setItems(items);
					JSONSerializer serializer = new JSONSerializer();
			    	String jsonres = serializer.deepSerialize(result);
			    	serializer.include("items");
			    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
			    	sendOutput(request,response,jsonres);
				}

			}
			
		}
    }
    
    public void doGetFolder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	getCMObjects(request, response, true);
    	if(application==null)
    	{
    		return;
    	}
		if(ds==null){
			return;
		}
	
		DataSourceCache dsc=ServerCache.getDSC(appManager, application, getDs());
		ArrayList<CContent> folders=dsc.getByType(ECMContentSourceType.GoogleDocumentFolder);
		ArrayList<CContent> docs=dsc.getByType(ECMContentSourceType.GoogleDocumentItem);
		if(docs==null){
			return404Page();
		}
		
		if(docs.size()==0)
		{
			return404Page();
		}
		ArrayList<CListItem>items = CListItemTools.toCListItems(docs);
		items = CListItemTools.getByGroupId(items, ref);
	
		ArrayList<CListItem>folderItems =   CListItemTools.toCListItems(folders);
		CListItem folderItem = CListItemTools.getByRefIdStr(folderItems, ref);
		
		
		CList result= new CList();
		
		if(folderItem!=null)
		{
			result.setTitle(folderItem.getTitle());
		}
		result.setRefIdString(ref);
		
		result.setType(ECMContentSourceType.GoogleDocumentFolder);
		result.baseRef="https://picasaweb.google.com/";
    	result.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentFolder);
    	for (int s = 0; s < items.size() ; s++) 
		{
    		//CContent c = data.get(s);
			CListItem listItem = items.get(s);
			listItem.setSourceType(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.ExternalLink));
			if(listItem.getWebLink()==null){
				continue;
			}
			//https://docs.google.com/viewer?url=http://mc007ibi.dyndns.org:8080/XApp-portlet/gdata?action%3Dget%26appIdentifier%3Dmyeventsappbf%26uuid%3D11166763-e89c-44ba-aba7-4e9f4fdf97a9%26dataSource%3D63b4835f-5a82-408e-b10d-b1c567593a0f%26ref%3Dc3ByZWFkc2hlZXQ6MEFxTXZya1VxYUVXS2RGRkdiMFZLUlVSdVNVcDJiak5MUm1OclJYTnpiVUU%3D%26type%3Dspreadsheet
			//http://docs.google.com/viewer?embedded=true&url=http%3A%2F%2Fdocs.google.com%2Fviewer%3Fembedded%3Dtrue%26url%3Dhttp%3A%2F%2Fmc007ibi.dyndns.org%3A8080%2FXApp-portlet%2Fgdata%3Faction%3Dget%26appIdentifier%3Dmyeventsappbf%26uuid%3D11166763-e89c-44ba-aba7-4e9f4fdf97a9%26dataSource%3D63b4835f-5a82-408e-b10d-b1c567593a0f%26ref%3Dc3ByZWFkc2hlZXQ6MEFxTXZya1VxYUVXS2RGRkdiMFZLUlVSdVNVcDJiak5MUm1OclJYTnpiVUU%3D%26type%3Dspreadsheet
			//String newUrl  ="http://docs.google.com/viewer?url="  + java.net.URLDecoder.decode(listItem.getWebLink());
			/****
			 *      NSString *downloadUrl = [NSString stringWithFormat:@"%@/gdata?action=get&appIdentifier=%@&uuid=%@&dataSource=%@&ref=%@&type=%@",cmRoot,_appInfo.applicationIdentifier,_appInfo.userIdentifier,content.dataSourceUID,content.refIdStr,content.locRefStr];
			 */
			
			String newUrl ="http://docs.google.com/viewer?embedded=true&url=";
			newUrl+= System.getProperty("ServletWebPathOuter") + "gdata?action=get&appIdentifier=" + appIdentifier+"&uuid=" +uuid+"&dataSource="+dataSource +"&ref=" + listItem.getRefIdStr()+"&type=" + listItem.locRefStr;
			System.out.println("doc url " + newUrl);
			listItem.setRef(newUrl);   
			listItem.setGroupIdStr(listItem.locRefStr);
			listItem.setLocRefStr(listItem.locRefStr);
			if(listItem.creationDate!=null){
				DateFormat df = new SimpleDateFormat("dd MMM");
				listItem.setDateString(df.format(listItem.creationDate));
			}
		}
    	
    	
		result.setItems(items);
		JSONSerializer serializer = new JSONSerializer();
    	String jsonres = serializer.deepSerialize(result);
    	serializer.include("items");
    	sendOutput(request,response,jsonres);
    }
    
    public void doGetEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	getCMObjects(request, response, true);
    	if(application==null)
    	{
    		return;
    	}
		if(ds==null){
			return;
		}
	
		DataSourceCache dsc=ServerCache.getDSC(appManager, application, ds);
		ArrayList eventsAll =dsc.getByType(ECMContentSourceType.GoogleCalendarItem);
		if(eventsAll==null){
			return404Page();
			return;
		}
		
		CContent content = CContentTools.getByRefIdStr(eventsAll, ref);
		if(content==null){
			return404Page();
			return;
		}
		JSONSerializer serializer = new JSONSerializer();
		String jsonres = serializer.deepSerialize(content);
		content.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendarItem);
		content.setRefIdStr(ref);
    	sendOutput(request, response,jsonres);
		/**
		 * 
		 * article.getPictures();
      	
      	CContent content = CContentFactory.fromArticleData(article, uuid, appIdentifier, getDs().getUid(),ECMContentSourceType.JoomlaArticle, null, Constants.USERAGENT_IPHONE, lang, 120);
      	JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
      	String translationText= article.getDescription();
      	if(translationText!=null && translationText.length() >0)
    	{
    		ArrayList<MediaItemBase> pictureItems= MediaUtils.getPictureItemsFromText(translationText);
    		if(pictureItems!=null && pictureItems.size() > 0)
    		{
    			article.setPictureItems(pictureItems);
    			serializer.include("pictureItems");
    		}
    	}
		 */
    }
    
    public void doGetCalendar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	getCMObjects(request, response, true);
    	if(application==null)
    	{
    		return;
    	}
		if(ds==null){
			return;
		}
		
		CEventSearch lastEventSearch = (CEventSearch)request.getSession().getAttribute("lastEventSearchC");
    	if(lastEventSearch==null)
    	{
    		lastEventSearch = new CEventSearch();
    		request.getSession().setAttribute("lastEventSearchC",lastEventSearch);
    	}

    	lastEventSearch.setShowPast(false);

    	String startDateStr = request.getParameter("startDate");
    	Date startDate = new Date();
    	
 	   
    	Calendar startCDate = Calendar.getInstance();
 	   	
 	   
 	   startCDate.setTime(startDate);
 	   startCDate.set(Calendar.HOUR, 0);
 	   startCDate.set(Calendar.MINUTE, 0);
 	   startDate = startCDate.getTime();
 	   
    	if(startDateStr!=null && startDateStr.length() > 0)
    	{
    		startDate=TimeUtils.fromDojoSpinWheel(startDateStr);
    		request.getSession().setAttribute("lastStartDate",startDate);
    	}
    		
		
    	DataSourceCache dsc=ServerCache.getDSC(appManager, application, ds);
		ArrayList eventsAll =dsc.getByType(ECMContentSourceType.GoogleCalendarItem);
		
		ArrayList<CContent>events = lastEventSearch.searchEventsAll(eventsAll,"", false, "ibiza", "false", null, null,startDate,ref);
		Vector currentPage = lastEventSearch.getNextPage();
		CEventDataArrayTools.sort(currentPage);
		
		  
		//ArrayList cevents =CListItemTools.toListItems(events,null, ECMContentSourceType.JEventsItem,getDs());
		ArrayList<CContent>cvec = new ArrayList<CContent>();
		if(currentPage!=null){
			cvec.addAll(currentPage);
		}
		CEventComparator fbUserSumComp = new CEventComparator();
		Collections.sort(cvec, fbUserSumComp);
		
		ArrayList cevents =CEventListItemTools.toCEventItems(cvec,StringUtils.getLocaleFromLang(lang));//,null, ECMContentSourceType.JEventsItem,getDs());
    	//CListItemTools.setBaseRef(cevents, getDs());
		CGroupedList groupedList = CEventListItemTools.createGoupedListByStartDate(
				cevents,
    			appManager, 
    			application, 
    			getDs(), 
    			StringUtils.getLocaleFromLang(lang),4);
    	
		JSONSerializer serializer = new JSONSerializer();
		
		
		
		groupedList.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendar);
		groupedList.setRefIdString(ref);
		
		
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
    	//response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	sendOutput(request,response,out);
		
    }
    
    public void doGetAlbum(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	getCMObjects(request, response, true);
    	if(application==null)
    	{
    		return;
    	}
		if(ds==null){
			return;
		}
	
		DataSourceCache dsc=ServerCache.getDSC(appManager, application, ds);
		ArrayList<CList> albums=dsc.getByType(ECMContentSourceType.GooglePicassaAlbum);
		ArrayList<CListItem> entries=dsc.getByType(ECMContentSourceType.GooglePicassaItem);
		if(albums!=null && entries!=null)
		{
			CList cAlbums = albums.get(0);
			if(cAlbums!=null)
			{
				CListItem album = CListItemTools.getByRef(cAlbums.getItems(), ref);
				if(album!=null)
				{
					CList result= new CList();
					result.setTitle(album.getTitle());
					result.setRefIdString(ref);
					ArrayList< CListItem>items = CListItemTools.getByGroupId(entries,result.getRefIdString());
					
					items = CListItemTools.updateSourceType(items, ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GooglePicassaItem));
					result.setType(ECMContentSourceType.GooglePicassaAlbum);
					result.baseRef="https://picasaweb.google.com/";
			    	result.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GooglePicassaAlbum);
			    	
					result.setItems(items);
					
					JSONSerializer serializer = new JSONSerializer();
			    	String jsonres = serializer.deepSerialize(result);
			    	serializer.include("items");
			    	sendOutput(request, response,jsonres);
					//result.setItems(items)
				}
				/*
				JSONSerializer serializer = new JSONSerializer();
		    	String jsonres = serializer.deepSerialize(list);
		    	serializer.include("items");
		    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
		    	response.getWriter().write(jsonres);
		    	*/
			}
			
		}
		
		
		
		
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
    	if(action.equals("calendar"))
    	{
    		doGetCalendar(request, response);
    	}
    	
    	
    	if(action.equals("event"))
    	{
    		doGetEvent(request, response);
    	}
    	
    	if(action.equals("folder"))
    	{
    		doGetFolder(request, response);
    	}
    	
    	if(action.equals("document"))
    	{
    		doGetDocument(request, response);
    	}
    	
    	if(action.equals("album"))
    	{
    		doGetAlbum(request, response);
    	}
    	/*
    	if(action.equals("detail"))
    	{
    		doGetDetails(request, response);
    	}*/
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
