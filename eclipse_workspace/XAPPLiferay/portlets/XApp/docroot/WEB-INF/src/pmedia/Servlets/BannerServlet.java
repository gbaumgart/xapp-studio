package pmedia.Servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmedia.DataManager.ServerCache;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.utils.CListItemTools;
import pmedia.utils.ECMContentSourceTypeTools;
import cmx.cache.DataSourceCache;
import cmx.types.ECMContentSourceType;
import flexjson.JSONSerializer;


public class BannerServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
  
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void doGetJoomlaBannerList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	getCMObjects(request, response, true);
    	if(application==null)
    	{
    		return404Page();
    	}
    	
    	if(ds==null)
    	{
    		return404Page();
    	}

    	DataSourceCache dsc=null;
    	try {
    		dsc = ServerCache.getDSC(appManager, application, ds);	
		} catch (Exception e) {
			e.printStackTrace();
			return404Page();
			return;
		}
    	
    	ArrayList bannerCatsAll = dsc.getByType(ECMContentSourceType.JoomlaBannerCategory);
    	CList list = new CList();
    	list.setBaseRef(getDs().getUrl());
    	list.setRefId(refId);
    	list.setItems(new ArrayList<CListItem>());
    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaBannerCategory);
    	JSONSerializer serializer = new JSONSerializer();
    	if(bannerCatsAll!=null && bannerCatsAll.size()>0)
    	{
    		
    		ArrayList allBanners = dsc.getByType(ECMContentSourceType.JoomlaBannerItem);
    		if(allBanners!=null && allBanners.size() > 0)
    		{
    			ArrayList catBanners = CListItemTools.getByGroupId(allBanners, refId); //BaseDataArrayTools.getByGroupIndex(allBanners, refId);
    			if(catBanners!=null)
    			{
    				list.setItems(catBanners);
    			}
    		}
    	}
    	String jsonres = serializer.deepSerialize(list);
    	sendOutput(request, response,jsonres);
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void doGetList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	
    	getCMObjects(request, response, true);
    	if(cType==ECMContentSourceType.JoomlaBannerCategory)
    	{
    		doGetJoomlaBannerList(request, response);
    	}
    	/*
    	if(application==null)
    	{
    		return404Page();
    	}
    	
    	if(ds==null)
    	{
    		return404Page();
    	}
    	

    	DataSourceCache dsc=null;
    	try {
    		dsc = ServerCache.getDSC(appManager, application, ds);	
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
    	
		ArrayList sections = null;
    	Boolean is15=ds.getVersion().equals("1.5");
    	if(is15)
    	{
			sections =application.getDataListByType(appManager,ECMContentSourceType.JoomlaSection, ds);
			if(sections!=null){
				CContentStorageUtils.saveAsCContentData(
	    				appManager,
	    				application,
	    				dsc,
	    				ds,
						sections, 
						"json", 
						uuid, 
						application.getApplicationIdentifier(), 
						ds.getUid(), 
						ECMContentSourceType.JoomlaSection, 
						WhiteListDataFactory.mobileBase(application.getApplicationIdentifier()), 
								pmedia.types.Constants.USERAGENT_IPHONE_NATIVE, lang);
				
			}
		}
		
    	ArrayList cats =dsc.getByType(ECMContentSourceType.JoomlaCategory);
    	if(cats !=null)
    	{
    		
    		CContentStorageUtils.saveAsCContentData(
    				appManager,
    				application,
    				dsc,
    				ds,
					cats, 
					"json", 
					uuid, 
					application.getApplicationIdentifier(), 
					ds.getUid(), 
					ECMContentSourceType.JoomlaCategory, 
					WhiteListDataFactory.mobileBase(application.getApplicationIdentifier()), 
							pmedia.types.Constants.USERAGENT_IPHONE_NATIVE, lang);
    		
    		
    	}
    	ArrayList articles =dsc.getByType(ECMContentSourceType.JoomlaArticle);
		if(cats!=null && cats.size() > 0)
		{
			Category cat = CategoryTools.getCatByIndex(cats, refId);
			// is location category menu !
			if( CategoryTools.hasSubCategories(cats, refId) && cat!=null)
			{
				doGetArticleCategories(cat,cats, refId, response);
				return;
			}else{
				
				
				if(articles!=null && articles.size() > 0 )
				{
					ArrayList<ArticleData >categoryArticles = ArticleTools.getArticlesByTypeAndParent(articles, refId, PMDataTypes.DITT_JARTICLE);
					if(categoryArticles!=null && categoryArticles.size() > 0 )
					{
						doGetArticleList(cat,categoryArticles, refId, response);
						
					}else{
						
						if(cat!=null)
						{
							CList list = new CList();
					    	list.setBaseRef(getDs().getUrl());
					    	list.setTitle(cat.getTitle());
					    	list.setRefId(refId);
					    	list.setItems(new ArrayList<CListItem>());
					    	
							String rawDescription = cat.getDescription();
							
							String newRawDescription = ClientFilter.filter(rawDescription, uuid, appIdentifier, getDs().getUid(),ds.getUrl(),ECMContentSourceType.JoomlaCategory,""  + cat.getRefId() , null, null, null, 80);
					    	list.setIntroText(newRawDescription);
					    	
					    	list.baseRef=getDs().getUrl();
					    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory);
					    	
					    	JSONSerializer serializer = new JSONSerializer();
					    	String jsonres = serializer.deepSerialize(list);
					    	response.getWriter().write(jsonres);
						}else{
							return404Page();
						}
					}
				}
			}
		}
		*/
    }
    
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String action= request.getParameter("action");
    	if(action !=null && action.equals("list"))
    	{
    		doGetList(request, response);
    	
    	}else{
    		return404Page();
    	}
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
