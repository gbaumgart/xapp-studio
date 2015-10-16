package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.MediaUtils;
import pmedia.types.ArticleData;
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
import pmedia.utils.ApplicationContentTools;
import pmedia.utils.CListItemTools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import cmx.cache.DataSourceCache;
import cmx.data.CContentFactory;
import cmx.manager.GTrackerManager;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;
import cmx.types.ECMBannerSourceType;
import cmx.types.ECMContentSourceType;
import flexjson.JSONSerializer;


public class WordpressServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
    protected void doContent(PrintWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
    }
  
    public void doGetArticleCategories(Category cat,ArrayList<Category>cListAll, int categoryIndex,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
    {

    	
    	ArrayList<Category>sub = CategoryTools.getCategoriesByParentIndex(cListAll, categoryIndex);
    	ArrayList cItems = CListItemTools.toListItems((ArrayList)sub,"pmedia.types.ArticleCategory",ECMContentSourceType.WordpressCategory,getDs(),StringUtils.getLocaleFromLang(lang));
    	
    	CList list = new CList();
    	list.setItems(cItems);
    	list.setTitle(cat.getTitle());
    	list.setRefId(categoryIndex);
    	
    	JSONSerializer serializer = new JSONSerializer();
    	
    	String jsonres = serializer.deepSerialize(list);
    	sendOutput(request,response,jsonres);
    	
    }
    public void doGetArticleList(Category cat, ArrayList articles, int categoryIndex,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
    {
    	ArrayList cItems = CListItemTools.toListItems(articles,null,""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.WordpressPost));
    	
    	CList list = new CList();
    	list.setItems(cItems);
    	list.setTitle(cat.getTitle());
    	list.setRefId(categoryIndex);
    	
    	list.baseRef=getDs().getUrl();
    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.WordpressCategory);
    	
    	GTrackerManager.trackXAPPEvent(GTrackerManager.openArticle,"wordpressCat");
    	
    	if(dataSource!=null){
    		CListItemTools.updateDatasource(cItems, dataSource);
    	}
    	
    	if(cItems !=null && cItems.size()>0){
			CListItem _item = (CListItem) cItems.get(0);
			if(_item.creationDate!=null)
			{
				Collections.sort(cItems, new CListDateComp());
			}
			list.setItems(cItems);
		}
    	
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
    		return;
    	}
    	
    	if(ds==null)
    	{
			return;
		}
    	DataSourceCache dsc=ServerCache.getDSC(appManager, application, ds);
    	ArrayList cats =dsc.getByType(ECMContentSourceType.WordpressCategory);
		
		if(cats!=null && cats.size() > 0)
		{
			Category cat = CategoryTools.getCatByIndex(cats, refId);
			// is location category menu !
			if( CategoryTools.hasSubCategories(cats, refId) && cat!=null)
			{
				doGetArticleCategories(cat,cats, refId,request, response);
				return;
			}else
			{
				
				ArrayList articlesAll =dsc.getByType(ECMContentSourceType.WordpressPost);
				if(articlesAll!=null && articlesAll.size() > 0 )
				{
					
					ArrayList<ArticleData >categoryArticles = ArticleTools.getArticlesByRefGroup(articlesAll, cat.refId);
			    	
			    	if(categoryArticles==null )
			    	{
			    		categoryArticles = ArticleTools.getArticlesByRefGroupStr(articlesAll, cat.title);
			    	}
			    	
			    	if(categoryArticles!=null && categoryArticles.size() > 0 )
					{
						doGetArticleList(cat,categoryArticles, refId,request, response);
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
    	
    	
		if(ds.getVersion().equals("1.5")){
			sections =application.getDataListByType(appManager,ECMContentSourceType.JoomlaSection, ds);
			
		}

		@SuppressWarnings("rawtypes")
		ArrayList catAll =application.getDataListByType(appManager,ECMContentSourceType.JoomlaCategory, ds);
		ArrayList<Category>cats = CategoryTools.getCategoriesByParentIndex(catAll, refId);
		if(cats!=null && cats.size() > 0)
		{
			Category cat = CategoryTools.getCatByIndex(sections, refId);
			doGetArticleCategories(cat,cats, refId,request,  response);
		}
    }
    public void doGetContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	
    	if(application==null)
    	{
    		return;
    	}
    	
    	if(ds==null)
		{
			return;
		}
		
    	DataSourceCache dsc=ServerCache.getDSC(appManager, application, ds);
    	ArrayList articles =dsc.getByType(ECMContentSourceType.WordpressPost);
    	ArticleData article = ArticleTools.getArticleByRefId(articles, refId);
    	if(article==null)
    	{
      		return404Page();
      		return;
      	}
    	
    	GTrackerManager.trackXAPPEvent(GTrackerManager.openArticle,"wordpressItem");
    	
      	
      	article.getPictures();
      	CContent content = CContentFactory.fromArticleData(article, uuid, appIdentifier, getDs().getUid(),ECMContentSourceType.WordpressPost, null, Constants.USERAGENT_IPHONE, lang, 120);
      	JSONSerializer serializer = new JSONSerializer();
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
    	String jsonres = serializer.deepSerialize(content);
    	sendOutput(request, response,jsonres);
    	
    }
    public void doGetDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	getCMObjects(request, response, true);
    	
    	if(application==null)
    	{
    		return;
    	}
    	
    	if(ds==null)
		{
			return;
		}
		
    	DataSourceCache dsc=ServerCache.getDSC(appManager, application, ds);
    	ArrayList articles =dsc.getByType(ECMContentSourceType.WordpressPost);
		
    	ArticleData article = ArticleTools.getArticleByRefId(articles, refId);
      	if(article==null){
      		System.out.println("couldnt find article : " + refId);
      		return;
      	}
      	
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
		
    	sendOutput(request, response,jsonres);
    	
    }
   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String action= request.getParameter("action");
    	
    	getCMObjects(request, response, false);
    	if(!preventCache){
	    	String cachedResponse = getCachedResponse(null);
	    	if(cachedResponse!=null){
	    		sendCachedOutput(request, response, cachedResponse);
	    		return;
	    	}
    	}
    	
    	if(application==null)
    	{
    		return404Page();
    		return;
    	}
    	
    	if(action.equals("list"))
    	{
    		doGetList(request, response);
    	}
    	if(action.equals("sectionList"))
    	{
    		doGetSectionList(request, response);
    	}
    	if(action.equals("detail"))
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
