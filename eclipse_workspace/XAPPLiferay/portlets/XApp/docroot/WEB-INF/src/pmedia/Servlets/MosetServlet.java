package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import cmx.manager.GTrackerManager;
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
import pmedia.types.BaseComparator;
import pmedia.types.BaseData;
import pmedia.types.CContent;
import pmedia.types.CList;
import pmedia.types.Category;
import pmedia.types.CategoryComparator;
import pmedia.types.Constants;
import pmedia.types.EventData;
import pmedia.types.LocationComparator;
import pmedia.types.LocationData;
import pmedia.types.MappingData;
import pmedia.types.MosetCategory;
import pmedia.types.MosetItem;
import pmedia.types.PMDataTypes;
import pmedia.types.Rating;
import pmedia.types.TranslationData;
import pmedia.utils.CListItemTools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import flexjson.JSONSerializer;


public class MosetServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
    private static final String __HUMAN_ID = "ID";
    @SuppressWarnings("unchecked")
	public void doGetContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	
    	getCMObjects(request, response, true);
    	GTrackerManager.trackXAPPEvent(GTrackerManager.openArticle,"Moset");
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
    	ArrayList<LocationData> lList = dsc.getByType(ECMContentSourceType.MosetTreeItem);
      	LocationData event = (LocationData)BaseDataArrayTools.getByIndex(lList, refId);
      	if(event==null)
      	{
      		
      		return404Page();
      	}
      	CContent content = dsc.getAsCContent(ECMContentSourceType.MosetTreeItem,""+refId, uuid, appIdentifier, dataSource, lang, Constants.USERAGENT_IPHONE);
      	JSONSerializer serializer = new JSONSerializer();
      	serializer.exclude("listItem");
      	serializer.exclude("asListItem");
      	serializer.exclude("treeUid");
      	String serialized  = serializer.deepSerialize(content);
      	serialized = serialized.replaceAll("[\"][a-zA-Z0-9_]*[\"]:null[ ]*[,]?", "");
    	sendOutput(request, response,serialized);
    	
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
    	ArrayList<BaseData> cListAll =  getDataSourceCache().getByType(ECMContentSourceType.MosetTreeCategory);
    	
    	ArrayList<BaseData> locList =  getDataSourceCache().getByType(ECMContentSourceType.MosetTreeItem);
    

    	EventSearch lastEventSearch = (EventSearch)request.getSession().getAttribute("lastLocationCategorySearch");
		if(lastEventSearch==null)
    	{
    		lastEventSearch = new EventSearch();
    		request.getSession().setAttribute("lastLocationCategorySearch",lastEventSearch);
    	}
		
		//public static ArrayList<BaseData>getLocationCategoriesByIndex2(ArrayList<BaseData>srcCategories,ArrayList<BaseData>srcLocations,int index,boolean flatten)
		ArrayList<BaseData> locCats = CategoryTools.getLocationCategoriesByIndex2(cListAll,locList, categoryIndex,flatten);
		
		
		Category p = CategoryTools.getCatByIndex(((ArrayList<BaseData>)cListAll), categoryIndex);
		//Category p =(Category) BaseDataArrayTools.getByIndex(cListAll, categoryIndex);
		

		ArrayList cItems = CListItemTools.toListItems((ArrayList)locCats,"pmedia.types.Category",ECMContentSourceType.MosetTreeCategory,getDs(),StringUtils.getLocaleFromLang(lang));
	
		Comparator<BaseData> catAlpComparator = new BaseComparator();
		Collections.sort(locCats, catAlpComparator);
		
		/*
		Comparator<LocationData> locAlpComparator = new LocationComparator();
		
		Collections.sort(locCats, locAlpComparator);
		*/
    	CList list = new CList();
    	cItems = CListItemTools.setBaseRef(cItems, getDs());
    	list.setItems(cItems);
    	list.items=cItems;
    	list.setRefId(categoryIndex);
    	list.baseRef=getDs().getUrl();
    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MosetTreeCategory);
    	
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
    	int cIndex = 0;
    	cIndex = refId;	

    	Boolean flatten = false; 
		String flattenStr = request.getParameter("flatten");
    	try {
			flatten=Boolean.parseBoolean(flattenStr);
		} catch (Exception e) {
			flatten=false;
		}
    	
    	if(cIndex==-1)
    	{
    		if(getDs().getVersion().equals("1.5")){
    			cIndex=0;
    		}else{
    			cIndex=1;
    		}
    	}
    
    	ArrayList<Category> cListAll =  getDataSourceCache().getByType(ECMContentSourceType.MosetTreeCategory);
		if(cListAll!=null && cListAll.size() > 0)
		{
			if( CategoryTools.hasSubCategories(cListAll, cIndex) )
			{
				if(cIndex == 0 || !flatten)
				{
					doGetLocationCategories(request, response, cIndex);
					return;
				}
				
			}
		}
		ArrayList<Category> locListAll =  getDataSourceCache().getByType(ECMContentSourceType.MosetTreeItem);
    	ArrayList<LocationData> last = BaseDataArrayTools.getByGroupIndex(locListAll, cIndex);
    	if(last!=null && last.size()>0)
    	{
    		for(int i = 0 ;  i < last.size() ; i++)
    		{
    			LocationData loc = last.get(i);
    			loc.populateStatusVariables();
    		}
    	}
    	if(last==null){
    		return404Page();
    		return;
    	}
    	
    	Comparator<LocationData> catAlpComparator = new LocationComparator();
		Collections.sort(last, catAlpComparator);
		
    	
    	//ArrayList<Rating> ratings=  ServerCache.getInstance().getDC("ibiza").get(DomainCache.RATINGS);
    	//LocationArrayTools.updateRating(last, ratings);
    	
    	Category locCat = CategoryTools.getCatByIndex(cListAll, cIndex);
    	if(locCat!=null && locCat.getPictures() !=null && locCat.getPictures().size() > 0)
    	{
    		//LocationArrayTools.updateCategoryIcon(last, locCat);
    	}
    	Category p = CategoryTools.getCatByIndex(cListAll, cIndex);
		ArrayList cItems = CListItemTools.toListItems((ArrayList)last,"pmedia.types.LocationData",ECMContentSourceType.MosetTreeItem,getDs(),StringUtils.getLocaleFromLang(lang));
    	CList list = new CList();
    	cItems = CListItemTools.setBaseRef(cItems, getDs());
    	list.setItems(cItems);
    	list.items=cItems;
    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MosetTreeCategory);
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
