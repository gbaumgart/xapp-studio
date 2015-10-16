package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmedia.DataJSONTransformer.ArticleDataTranslationTransformer;
import pmedia.DataJSONTransformer.BaseDataTransformer;
import pmedia.DataManager.Cache;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.MediaUtils;
import pmedia.html.factory.WhiteListDataFactory;
import pmedia.html.types.ClientFilter;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.CContent;
import pmedia.types.CList;
import pmedia.types.CListDateComp;
import pmedia.types.CListItem;
import pmedia.types.Category;
import pmedia.types.Constants;
import pmedia.types.Domain;
import pmedia.types.MediaItemBase;
import pmedia.types.PMDataTypes;
import pmedia.types.PictureTransformOptions;
import pmedia.utils.CListItemTools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import cmx.cache.DataSourceCache;
import cmx.data.CContentFactory;
import cmx.data.CContentStorageUtils;
import cmx.manager.GTrackerManager;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import flexjson.JSONSerializer;


public class ArticleServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
    public void doGetContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	getCMObjects(request, response, true);
    	
    	GTrackerManager.trackXAPPEvent(GTrackerManager.openArticle,"Joomla");
    	if(application==null)
    	{
    		return404Page();
    	}
    	ArrayList articles = getDataSourceCache().getByType(ECMContentSourceType.JoomlaArticle);
		
		
    	String articleType = request.getParameter("type");
    	String cacheLookupType = DomainCache.JARTICLES;
    	PMDataTypes cType = PMDataTypes.DITT_ARTICLE;
    	
		
		if(articleType!=null)
		{
			if(articleType.equals("9"))
			{
				cacheLookupType=DomainCache.JARTICLES;
				cType = PMDataTypes.DITT_JARTICLE; 
			}
		}
      	ArticleData article = ArticleTools.getArticleByTypeAndIndex(articles, refId, cType);
      	if(article==null){
      		
      		return404Page();
      		return;
      	}
      	article.getPictures();
      	
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
      	
      	/*
      	public static void setCustomFields(
    			CContent content,
    			BaseData data,
    			String uuid, 
    			String applicationId , 
    			String  dsUID, 
    			ECMContentSourceType cType,
    			String platform,
    			String lang)
      	*/
      	
      	if(content!=null){
      		CContentFactory.setCustomFields(content, (BaseData)article, uuid, appIdentifier, getDs().getUid(), ECMContentSourceType.JoomlaArticle, Constants.USERAGENT_IPHONE, lang);
      	}

      	SerializerUtils.removeDefaultItems(serializer);
      	String jsonres = serializer.deepSerialize(content);
    	response.setHeader("attachment", "article.json");
    	sendOutput(request, response,jsonres);
    	
    }
    protected void doContent(PrintWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
    }
  
    public void doGetArticleCategories(Category cat,ArrayList<Category>cListAll, int categoryIndex,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		//CategoryTools.sortByTitleTranslated("ibiza",(String)session.getAttribute("lang"),locCats);
		//CategoryTools.updateLocalizedTitle("ibiza",(String)session.getAttribute("lang"),locCats);
		
    	ArrayList<Category>sub = CategoryTools.getCategoriesByParentIndex(cListAll, categoryIndex);
    	ArrayList cItems = CListItemTools.toListItems((ArrayList)sub,"pmedia.types.ArticleCategory",ECMContentSourceType.JoomlaCategory,getDs(),StringUtils.getLocaleFromLang(lang));
    	
    	CList list = new CList();
    	cItems = CListItemTools.setBaseRef(cItems, getDs());
    	list.baseRef=getDs().getUrl();
    	list.refId=refId;
    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory);
    	list.setItems(cItems);
    	list.setTitle(cat.getTitle());
    	list.setRefId(categoryIndex);
    	
    	JSONSerializer serializer = new JSONSerializer();
    	String jsonres = serializer.deepSerialize(list);
    	sendOutput(request, response,jsonres);
    }
    public void doGetArticleList(Category cat, ArrayList articles, int categoryIndex,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
    {
    	
    	ArrayList cItems = CListItemTools.toListItems(articles,null,ECMContentSourceType.JoomlaArticle,getDs(),StringUtils.getLocaleFromLang(lang));
    	CList list = new CList();
    	list.setItems(cItems);
    	list.setTitle(cat.getTitle());
    	list.setRefId(categoryIndex);
    	cItems = CListItemTools.setBaseRef(cItems, getDs());

    	//DisqusHelper.updateArticleCommentCount(cItems, uuid, appIdentifier, ds.getUid(), "joomla254","TxnZoqL0PFzlkF8E4dZN26YcvikV24Ri2hCsHSiFKGbJR2DV6qsrxgIGOdD7om3X");
    	
    	list.baseRef=getDs().getUrl();
    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory);
    	if(cItems !=null && cItems.size()>0){
			CListItem _item = (CListItem) cItems.get(0);
			if(_item.creationDate!=null)
			{
				Collections.sort(cItems, new CListDateComp());
			}
			list.setItems(cItems);
		}
    	
    	/*
    	String rawDescription = cat.getDescription();
		
		String newRawDescription = ClientFilter.filter(rawDescription, uuid, appIdentifier, getDs().getUid(),ds.getUrl(),ECMContentSourceType.JoomlaCategory, null, null, null, 80);
    	list.setIntroText(newRawDescription);
    	*/
    	
    	/**
    	 * 
    	 */
    	//list.setIconUrl(iconUrl)
    	
    	
    	JSONSerializer serializer = new JSONSerializer();
    	String jsonres = serializer.deepSerialize(list);
    	serializer.include("items");
    	
    	sendOutput(request, response,jsonres);
    	
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void doGetList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	getCMObjects(request, response, true);
    	if(application==null)
    	{
    		return404Page();
    		return;
    	}
    	
    	if(ds==null)
    	{
    		return404Page();
    		return;
    	}
    	

    	DataSourceCache dsc=null;
    	try {
    		dsc = ServerCache.getDSC(appManager, application, ds);	
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
    	
		ArrayList sections = null;
    	Boolean is15=true;
    	
    	if(ds.getVersion() !=null && !ds.getVersion().equals("1.5"))
    	{
    		is15=false;
    	}
    	
    	if(ds.getVersion()==null)
    	{
    		return404Page();
    		return;
    	}
    	
    	if(is15)
    	{
			sections =application.getDataListByType(appManager,ECMContentSourceType.JoomlaSection, ds);
		
			/*
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
			*/
		}
		
    	ArrayList cats =dsc.getByType(ECMContentSourceType.JoomlaCategory);
    	if(cats !=null)
    	{
    		
    		/*
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
    	
    		*/
    		/*
    		String savepath = appManager.getUserAppPath(uuid, application.getApplicationIdentifier());
    		savepath+="/datasources/" + ds.getHost() + "/" + ds.getDatabase() + "/";
    		CContentStorageUtils.saveAsCContentData(
					cats, 
					savepath, 
					"json", 
					uuid, 
					application.getApplicationIdentifier(), 
					ds.getUid(), 
					ECMContentSourceType.JoomlaCategory, 
					WhiteListDataFactory.mobileBase(application.getApplicationIdentifier()), 
							pmedia.types.Constants.USERAGENT_IPHONE_NATIVE, lang);
    		*/
    	}
    	ArrayList articles =dsc.getByType(ECMContentSourceType.JoomlaArticle);
		if(cats!=null && cats.size() > 0)
		{
			Category cat = CategoryTools.getCatByIndex(cats, refId);
			// is location category menu !
			if( CategoryTools.hasSubCategories(cats, refId) && cat!=null)
			{
				doGetArticleCategories(cat,cats, refId, request, response);
				return;
			}else{
				
				
				if(articles!=null && articles.size() > 0 )
				{
					ArrayList<ArticleData >categoryArticles = ArticleTools.getArticlesByTypeAndParent(articles, refId, PMDataTypes.DITT_JARTICLE);
					if(categoryArticles!=null && categoryArticles.size() > 0 )
					{
						doGetArticleList(cat,categoryArticles, refId, request, response);
						
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
					    	sendOutput( request, response,jsonres);
						}else{
							return404Page();
						}
					}
				}
			}
		}
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void doGetSectionList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	getCMObjects(request, response, true);
    	if(application==null)
    	{
    		return;
    	}
    	
    	DataSourceBase ds = application.getDataSource(dataSource);
		@SuppressWarnings("unused")
		Domain domain = Cache.getDomain(application.applicationIdentifier);
		if(ds==null){
			return;
		}
    	@SuppressWarnings("unused")
		ArrayList sections = null;
		
    	Boolean is15=ds.getVersion().equals("1.5");
    	
    	
		if(ds.getVersion() !=null && ds.getVersion().equals("1.5"))
		{
			sections =application.getDataListByType(appManager,ECMContentSourceType.JoomlaSection, ds);
		}

		DataSourceCache dsc=null;
    	try {
    		dsc = ServerCache.getDSC(appManager, application, ds);	
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		@SuppressWarnings("rawtypes")
		ArrayList catAll = dsc.getByType(ECMContentSourceType.JoomlaCategory); //application.getDataListByType(appManager,ECMContentSourceType.JoomlaCategory, ds);
		//ArrayList cats =dsc.getByType(ECMContentSourceType.JoomlaCategory);
    	/*
		if(catAll !=null)
    	{
    		
    		CContentStorageUtils.saveAsCContentData(
    				appManager,
    				application,
    				dsc,
    				ds,
					catAll, 
					"json", 
					uuid, 
					application.getApplicationIdentifier(), 
					ds.getUid(), 
					ECMContentSourceType.JoomlaCategory, 
					WhiteListDataFactory.mobileBase(application.getApplicationIdentifier()), 
							pmedia.types.Constants.USERAGENT_IPHONE_NATIVE, lang);
    	}
    	*/
		ArrayList<Category>cats = CategoryTools.getCategoriesByParentIndex(catAll, refId);
		if(cats!=null && cats.size() > 0)
		{
			Category cat = CategoryTools.getCatByIndex(sections, refId);
			if(cat!=null){
				doGetArticleCategories(cat,cats, refId,request, response);
			}
		}
    }
    
    public void doGetDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	getCMObjects(request, response, true);
    	
    	if(application==null)
    	{
    		return;
    	}
    	
    	
		DataSourceBase ds = application.getDataSource(dataSource);
		if(ds==null)
		{
			return;
		}
		
		
		DataSourceCache dsc=ServerCache.getDSC(appManager, application, ds);
		dsc.jArticles=null;
		ArrayList articles = dsc.getByType(ECMContentSourceType.JoomlaArticle);
		
		
		
    	String articleType = request.getParameter("type");
    	String cacheLookupType = DomainCache.JARTICLES;
    	PMDataTypes cType = PMDataTypes.DITT_ARTICLE;
		
		if(articleType!=null)
		{
			if(articleType.equals("9"))
			{
				cacheLookupType=DomainCache.JARTICLES;
				cType = PMDataTypes.DITT_JARTICLE; 
			}
		}
      	ArticleData article = ArticleTools.getArticleByTypeAndIndex(articles, refId, cType);
      	
      	
      	if(article==null){
      		System.out.println("couldnt find article : " + refId);
      		return;
      	}
      	
      	//String oriText = article.getDescription();
      	
      	
      	/*
      	String replacment  = HTMLConverter.convert(
      			oriText, 
      			uuid , 
      			application.getApplicationIdentifier(), 
      			ds.getUid(), 
      			ECMContentSourceType.JoomlaArticle, 
      			WhiteListDataFactory.mobileBase(application.getApplicationIdentifier()), 
      			pmedia.types.Constants.USERAGENT_IPHONE_NATIVE, 0);
      	*/
      	
      	
      	ECMContentSourceType contentType = ECMContentSourceType.JoomlaArticle;
			//flush cache : 
		//dsc.jArticles=null;
      	String savepath = appManager.getUserAppPath(uuid, application.getApplicationIdentifier());
		savepath+="/datasources/" + ds.getHost() + "/" + ds.getDatabase() + "/";
		CContentStorageUtils.saveAsCContentData(
					articles, 
					savepath, 
					"json", 
					uuid, 
					application.getApplicationIdentifier(), 
					ds.getUid(), 
					contentType, 
					WhiteListDataFactory.mobileBase(application.getApplicationIdentifier()), 
							pmedia.types.Constants.USERAGENT_IPHONE_NATIVE, lang);
		
		
      	/**
		 * add annotation information : 
		 */
      	
      	//init variables before JSon serialize
      	
		article.getPictures();
      	article.getDescriptionPicture();
      	

      	
    	JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
    	
		serializer.include("annotations");
		serializer.include("pictures");
		
    	//serializer.transform(new LocationDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET,loc,"descriptionPicture"), "descriptionPicture");
    	//serializer.transform(new ArticleDataTranslationTransformer(true,pmedia.types.Constants.USERAGENT_TABLET,lang,article,"description"), "description");
		PictureTransformOptions pictureOptions = getPictureOptionsFromSession(request.getSession());
		
		BaseDataTransformer baseTransformer = new BaseDataTransformer(
    			true,
    			pmedia.types.Constants.USERAGENT_TABLET
    			,lang,
    			article,
    			"description",
    			application,
    			request.getSession(),
    			pictureOptions);

		serializer.transform(baseTransformer, "description");
    	
    	
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
    	
    	
    	serializer.transform(new ArticleDataTranslationTransformer(true,pmedia.types.Constants.USERAGENT_TABLET,lang,article,"descriptionNoPictures"), "descriptionNoPictures");
    	//serializer.transform(new ArticleDataTranslationTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET,lang,article,"descriptionPicture"), "descriptionPicture");
    	serializer.transform(new BaseDataTransformer(true,userAgent,article,"descriptionPicture"), "descriptionPicture");
    	serializer.transform(new BaseDataTransformer(true,userAgent,article,"pictures"), "pictures");
    	
    	
    	//serializer.transform(new LocationDataTranslationTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET,lang,loc,"descriptionExtra"), "descriptionExtra");
    	
    	String jsonres = serializer.serialize(article);
    	//String descrRaw = article.description;
    	//String descriptionMobileNormalized = ApplicationContentTools.transformHTMLText(application, descrRaw);
    	
    	//System.out.println(descriptionMobileNormalized);
    	

    	/*
    	Whitelist clean = Whitelist.relaxedNoPictures().addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong");
    	clean = clean.addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong");
    	clean = clean.addProtocols("a", "href","ftp","http","https","mtpp");*/
		
    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	response.setHeader("attachment", "article.json");
    	response.getWriter().write(jsonres);
    	cacheResponse(null, jsonres);
    	
    }
   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	getCMObjects(request, response, true);
    	
    	if(!preventCache){
	    	String cachedResponse = getCachedResponse(null);
	    	if(cachedResponse!=null){
	    		sendCachedOutput(request, response, cachedResponse);
	    		return;
	    	}
    	}
    	
    	
    	String action= request.getParameter("action");
    	if(action !=null && action.equals("list"))
    	{
    		doGetList(request, response);
    	}else if(action.equals("sectionList"))
    	{
    		doGetSectionList(request, response);
    	}else if(action.equals("content"))
    	{
    		doGetContent(request, response);
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
