package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cmx.cache.DataSourceCache;
import cmx.data.CContentFactory;
import cmx.data.CContentStorageUtils;
import cmx.types.ECMContentSourceType;

import pmedia.DataJSONTransformer.BaseDataTransformer;
import pmedia.DataJSONTransformer.LocationDataTransformer;
import pmedia.DataJSONTransformer.LocationDataTranslationTransformer;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.BaseDataArrayTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.LocationArrayTools;
import pmedia.DataUtils.LocationPropertyTools;
import pmedia.DataUtils.TranslationTools;
import pmedia.SearchBeans.EventSearch;
import pmedia.html.factory.WhiteListDataFactory;
import pmedia.types.ArticleData;
import pmedia.types.CContent;
import pmedia.types.CList;
import pmedia.types.Category;
import pmedia.types.Constants;
import pmedia.types.EventData;
import pmedia.types.LocationData;
import pmedia.types.MappingData;
import pmedia.types.PMDataTypes;
import pmedia.types.Rating;
import pmedia.types.TranslationData;
import pmedia.utils.CListItemTools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import flexjson.JSONSerializer;


public class LocationsServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
    private static final String __HUMAN_ID = "ID";
    @SuppressWarnings("unchecked")
	public void doGetContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	
    	getCMObjects(request, response, true);
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
    	
    	//ArrayList events=dsc.getByType(ECMContentSourceType.JEventsItem);
    	ArrayList<LocationData> lList = dsc.getByType(ECMContentSourceType.JEventsLocationItem);
      	LocationData event = (LocationData)BaseDataArrayTools.getByIndex(lList, refId);
      	if(event==null){
      		
      		return404Page();
      	}
      	CContent content = dsc.getAsCContent(ECMContentSourceType.JEventsLocationItem,""+refId, uuid, appIdentifier, dataSource, lang, Constants.USERAGENT_IPHONE);
      	JSONSerializer serializer = new JSONSerializer();
      	serializer.exclude("listItem");
      	serializer.exclude("asListItem");
      	serializer.exclude("treeUid");
      	String serialized  = serializer.deepSerialize(content);
      	serialized = serialized.replaceAll("[\"][a-zA-Z0-9_]*[\"]:null[ ]*[,]?", "");
    	sendOutput(request,response,serialized);
    	
    }
    public void updateLocationArticle(LocationData loc,String lang)
    {
    	
    	ArticleData article = null;
    	TranslationData translation =null;  
    	ArrayList<MappingData> mList = ServerCache.getInstance().getDC("ibiza").get(DomainCache.MAPPINGS);
    	if(loc !=null && mList!=null)
    	{
    		MappingData mapping = LocationArrayTools.getLocationMapping(loc.location_id,mList);
    		if(mapping!=null)
    		{
    			
    			ArrayList<ArticleData> articles = ServerCache.getInstance().getDC("ibiza").get(DomainCache.ARTICLES);
    			if(articles!=null && articles.size() > 0 )
    			{
    				
    				article = ArticleTools.getArticleByTypeAndIndex(articles,mapping.dstID,PMDataTypes.DITT_ARTICLE);
    				if(article !=null)
    				{
    					loc.mappedArticle = article;
    				} 
    			}
    		} 						
    	}
    	loc.didMapping=true;
    }
    
    public void doGetLocationCategories(HttpServletRequest request, HttpServletResponse response,int categoryIndex) throws ServletException, IOException
    {
    	//System.out.println("open loc cat : " + ind)
    	Boolean flatten=false;
    	
    	String flattenStr = request.getParameter("flatten");
    	try {
			flatten=Boolean.parseBoolean(flattenStr);
		} catch (Exception e) {
			flatten=false;
		}
    			
    	HttpSession session = request.getSession();
    	//ArrayList<Category> cListAll =  ServerCache.getInstance().getDC("ibiza").get(DomainCache.CATS_LOCATIONS);
    	ArrayList<Category> cListAll =  getDataSourceCache().getByType(ECMContentSourceType.JEventsLocationCategory);
    	
    	/*
    	if(cListAll!=null)
    	{
	    	CContentStorageUtils.saveAsCContentData(
					appManager,
					application,
					dataSourceCache,
					ds,
					cListAll, 
					"json", 
					uuid, 
					application.getApplicationIdentifier(), 
					ds.getUid(), 
					ECMContentSourceType.JEventsLocationCategory, 
					WhiteListDataFactory.mobileBase(application.getApplicationIdentifier()), 
							pmedia.types.Constants.USERAGENT_IPHONE_NATIVE, lang);
    	}
    	
    	*/
    	ArrayList<LocationData> locList =  getDataSourceCache().getByType(ECMContentSourceType.JEventsLocationItem);
    	
//		ArrayList<LocationData> locList =  ServerCache.getInstance().getDC("ibiza").get(DomainCache.LOCATIONS3);
		EventSearch lastEventSearch = (EventSearch)request.getSession().getAttribute("lastLocationCategorySearch");
		if(lastEventSearch==null)
    	{
    		lastEventSearch = new EventSearch();
    		request.getSession().setAttribute("lastLocationCategorySearch",lastEventSearch);
    	}
		ArrayList<Category> locCats = CategoryTools.getLocationCategoriesByIndex(cListAll,locList, categoryIndex,flatten);
		
		//Category p = CategoryTools.getCatByIndex(cListAll, 87);
		//CategoryTools.sortByTitleTranslated("ibiza",(String)session.getAttribute("lang"),locCats);
		//CategoryTools.updateLocalizedTitle("ibiza",(String)session.getAttribute("lang"),locCats);

		Category p = CategoryTools.getCatByIndex(cListAll, categoryIndex);
		

		ArrayList cItems = CListItemTools.toListItems((ArrayList)locCats,"pmedia.types.Category",ECMContentSourceType.JEventsLocationCategory,getDs(),StringUtils.getLocaleFromLang(lang));
    	CList list = new CList();
    	cItems = CListItemTools.setBaseRef(cItems, getDs());
    	list.setItems(cItems);
    	list.items=cItems;
    	list.setRefId(categoryIndex);
    	list.baseRef=getDs().getUrl();
    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationCategory);
    	list.refId=refId;
    	
    	if(p!=null){
    		list.setTitle(p.getTitle());
    	}else{
    		list.setTitle("Locations");
    	}
    	
		if(flatten){
			//ArrayList<Category> flattened =CategoryTools.insertSubCategoriesByIndex(cListAll,locCats,locList,(String)session.getAttribute("lang"));
			//locCats = flattened;
		}

		JSONSerializer serializer = new JSONSerializer();
		serializer.include("items");
    	String jsonres = serializer.deepSerialize(list);
    	sendOutput(request, response,jsonres);
    }
    public void doGetList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String cityFilter= request.getParameter("cityFilter");
    	int cIndex = 0;
    	cIndex = refId;	

    	Boolean flatten = false; 
		String flattenStr = request.getParameter("flatten");
    	try {
			flatten=Boolean.parseBoolean(flattenStr);
		} catch (Exception e) {
			flatten=false;
		}
    	
    	if(getDataSourceCache()==null){
    		return404Page();
    		return;
    	}
    	ArrayList<Category> cListAll =  getDataSourceCache().getByType(ECMContentSourceType.JEventsLocationCategory);
    	
    	if(cIndex==-1)
    	{
    		if(getDs().getVersion().equals("1.5"))
    		{
    			cIndex=0;
    			
    			Category rootCat = (Category) BaseDataArrayTools.getByIndex(cListAll, 0);
    			if(rootCat==null)
    			{
    				/*
    				rootCat=new Category();
    				rootCat.refId=0;
    				rootCat.title="ROOT";
    				rootCat.groupId=0;
    				rootCat.published=true;
    				cListAll.add(rootCat);
    				*/
    			}
    			
    			
    		}else{
    			cIndex=1;
    		}
    	}
    
    	
		if(cListAll!=null && cListAll.size() > 0)
		{
			if( CategoryTools.hasSubCategories(cListAll, cIndex) )
			{
				if(cIndex == 0 || !flatten){
					doGetLocationCategories(request, response, cIndex);
					return;
				}
				
			}
		}
		ArrayList<Category> locListAll =  getDataSourceCache().getByType(ECMContentSourceType.JEventsLocationItem);
		if(locListAll==null || locListAll.size()==0){
			returnEmptyList();
			return;
		}
		
		/*
		EventSearch lastEventSearch = (EventSearch)request.getSession().getAttribute("lastLocationSearch");
    	if(lastEventSearch==null)
    	{
    		lastEventSearch = new EventSearch();
    		request.getSession().setAttribute("lastLocationSearch",lastEventSearch);
    	}
    	*/
    	
    	//ArrayList<LocationData> last = lastEventSearch.searchLocationsByCategory(cIndex,"",false,"ibiza","false",null,null,flatten);
    	ArrayList<LocationData> last = BaseDataArrayTools.getByGroupIndex(locListAll, cIndex);
    	//ArrayList<LocationData> last = lastEventSearch.searchLocationsByCategory(cIndex,"",false,"ibiza","false",null,null,flatten);
    	if(last==null || last.size()==0)
    	{
			returnEmptyList();
			return;
		}
    	if(last!=null && last.size()>0)
    	{
    		for(int i = 0 ;  i < last.size() ; i++)
    		{
    			LocationData loc = last.get(i);
    			loc.populateStatusVariables();
    		}
    	}

    	ArrayList<Rating> ratings=  ServerCache.getInstance().getDC("ibiza").get(DomainCache.RATINGS);
    	LocationArrayTools.updateRating(last, ratings);
    	
    	Category locCat = CategoryTools.getCatByIndex(cListAll, cIndex);
    	if(locCat!=null && locCat.getPictures() !=null && locCat.getPictures().size() > 0)
    	{
    		//LocationArrayTools.updateCategoryIcon(last, locCat);
    	}
    	Category p = CategoryTools.getCatByIndex(cListAll, cIndex);
		ArrayList cItems = CListItemTools.toListItems((ArrayList)last,"pmedia.types.LocationData",ECMContentSourceType.JEventsLocationItem,getDs(),StringUtils.getLocaleFromLang(lang));
    	CList list = new CList();
    	if(getDs()==null)
    	{
    		return404Page();
    		return;
    	}
    	cItems = CListItemTools.setBaseRef(cItems, getDs());
    	list.setItems(cItems);
    	list.items=cItems;
    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationCategory);
    	list.setRefId(cIndex);
    	if(p!=null){
    		list.setTitle(p.getTitle());
    	}else{
    		list.setTitle("Locations");
    	}
    	
		if(flatten){
			//ArrayList<Category> flattened =CategoryTools.insertSubCategoriesByIndex(cListAll,locCats,locList,(String)session.getAttribute("lang"));
			//locCats = flattened;
		}

		JSONSerializer serializer = new JSONSerializer();
		serializer.include("items");
    	String jsonres = serializer.deepSerialize(list);
    	sendOutput(request, response,jsonres);

    	//System.out.println("ds: " + ds.getUid() + " ref : " + refId);
    	
		/*
    	JSONSerializer serializer = new JSONSerializer().transform(new LocationDataTransformer(true,pmedia.types.Constants.USERAGENT_TABLET,null,"iconUrl",request.getSession()), "iconUrl");
    	
    	//serializer.transform(new LocationDataTransformer(true,pmedia.types.Constants.USERAGENT_TABLET,null,"categoryIcon",request.getSession()), "categoryIcon");
    	
    	serializer.exclude("description");
    	serializer.exclude("didStatusTest");
    	
    	
    	serializer.include("rating");
    	
    	serializer.exclude("country");
    	serializer.exclude("video");
    	serializer.exclude("descriptionExtra");
    	
    	serializer.exclude("descriptionPicture");
    	serializer.exclude("staticMapUrl");
    	serializer.exclude("mapUrl");
    	serializer.exclude("published");
    	serializer.exclude("didMapping");
    	serializer.exclude("didEventSearch");
    	serializer.exclude("didPictureSearch");
    	serializer.exclude("access");
    	
    	
    	serializer.exclude("descriptionNoPictures");
    	serializer.exclude("www");
    	serializer.exclude("htmlText");
    	serializer.exclude("street");
    	//serializer.exclude("latitude");
    	//serializer.exclude("longtitude");
    	serializer.exclude("mappedArticle");
    	serializer.exclude("staticMap1");
    	serializer.exclude("logoLocalPath");
    	serializer.exclude("logoRemotePath");
    	serializer.exclude("stdPictureLink");
    	serializer.exclude("trusted");
    	serializer.exclude("pcode");
    	serializer.exclude("phone");
    	serializer.exclude("geozoom");
    	serializer.exclude("uid");
    	serializer.exclude("class");
    	serializer.exclude("ErrorReport");
    	serializer.exclude("sourceType");
    	serializer.exclude("location");
    	serializer.exclude("city_cat");
    	serializer.exclude("forcedEventCategory");
    	serializer.exclude("forcedEventCatgory");

    	if(cityFilter!=null && cityFilter.length() > 0)
    	{
    		ArrayList<Category> regions = ServerCache.getInstance().getDC("ibiza").get(DomainCache.REGIONS);
    		ArrayList<LocationData>filteredByCity=LocationArrayTools.filterByCity(last, cityFilter, regions);
    		if(filteredByCity!=null && filteredByCity.size() > 0)
    		{
    			last = filteredByCity;
    		}
    	}
    	
    	String jsonres = serializer.serialize(last);
    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	response.getWriter().write(jsonres);
    	*/
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
