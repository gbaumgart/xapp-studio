package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.model.User;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

import pmedia.DataJSONTransformer.ArticleDataTranslationTransformer;
import pmedia.DataJSONTransformer.BaseDataTransformer;
import pmedia.DataManager.Cache;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.MediaUtils;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.CContent;
import pmedia.types.CList;
import pmedia.types.Category;
import pmedia.types.Domain;
import pmedia.types.MediaItemBase;
import pmedia.types.PMDataTypes;
import pmedia.types.PictureTransformOptions;
import pmedia.utils.ApplicationContentTools;
import pmedia.utils.CListItemTools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import cmm.utils.LiferayTools;
import cmx.tools.LiferayContentTools;
import cmx.tools.LiferayDataSourceUtil;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import flexjson.JSONSerializer;


public class ContentServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
    protected void doContent(PrintWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
    	
    }
    public CList doGetArticleList(int categoryIndex,
    		ECMContentSourceType catType,
			ECMContentSourceType contentType,
			HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
    {
    	

    	

		/***
		 * The current cat
		 */
		AssetCategory lcat = null;
		try {
			lcat = AssetCategoryLocalServiceUtil.getAssetCategory((long)categoryIndex);
		} catch (PortalException e) {
			returnEmptyList();
			return null;
		} catch (SystemException e) {
			returnEmptyList();
			return null;
		}
		if(lcat==null){
			
			returnEmptyList();
			return null;
		}
		
    	CList list = new CList();
    	//cItems = CListItemTools.setBaseRef(cItems, getDs());
    	list.baseRef=getDs().getUrl();
    	list.refId=refId;
    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(catType);
    	list.setTitle(lcat.getTitle(LocaleUtil.getDefault()));
    	//list.setItems(new ArrayList());
    	ArrayList dst =new ArrayList<CContent>();
    	list.setRefId(categoryIndex);
    	
    	
    	ArrayList<JournalArticle >categoryArticles = categoryArticles=LiferayContentTools.getCategoryArticles(uuid,appIdentifier,categoryIndex);
    	User lUser = LiferayContentTools.getLiferayUserByUUID(uuid);
    	/***
    	 * add articles
    	 */
    	if(categoryArticles!=null && categoryArticles.size() > 0)
    	{
    		for (int ai = 0; ai < categoryArticles.size() ; ai++) 
			{
    			JournalArticle article = categoryArticles.get(ai);
    			
    			JournalArticle __article = null ; 
    			long _id2 = article.getId();
    			try {
					__article=JournalArticleLocalServiceUtil.getArticle(_id2);
				} catch (PortalException e) {
					continue;
				} catch (SystemException e) {
					continue;
				}
    			//53603
    			
    			CContent contentFull =  LiferayContentTools.toCContent(uuid, appIdentifier, __article, categoryIndex,contentType,userAgent);
    			BaseData content = LiferayContentTools.toBaseData(__article,categoryIndex);
    			if(content!=null)
    			{
    				//content.setRefId((int)article.getPrimaryKey());
    				content.setRefId((int)_id2);
    				content.setIconUrl(contentFull.getIconUrl());
    				content.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(contentType);
    				if(lUser !=null)
    				{
    					content.setOwnerRefStr(lUser.getScreenName());
    				}
    				dst.add(content);
    				//content.setDescription(null);
    				if(contentType==ECMContentSourceType.StaticWebContentVenue)
    				{
    					//setCustomFieldsWithLocation(content, article, ApplicationManager.getInstance(), app, platform, "en");
    					//LiferayContentTools.setCustomFields(content, __article, uuid, appIdentifier, "Liferay", contentType, userAgent, "en");
    				}
    			}
			}
    	}
    	
    	list.setItems(CListItemTools.toListItems(dst,null,contentType,getDs(),StringUtils.getLocaleFromLang(lang)));
    	JSONSerializer serializer = new JSONSerializer();
    	String jsonres = serializer.deepSerialize(list);
    	serializer.exclude("items.asListItem");
    	sendOutput(request,response,jsonres);
    	return list;
    }

    public CList doGetArticleCategories(int categoryIndex,
    		ECMContentSourceType catType,
			ECMContentSourceType contentType,
			HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
    {
		//CategoryTools.sortByTitleTranslated("ibiza",(String)session.getAttribute("lang"),locCats);
		//CategoryTools.updateLocalizedTitle("ibiza",(String)session.getAttribute("lang"),locCats);
		
    	
    	List<AssetCategory>catItems=null;
		
		try {
			catItems= AssetCategoryLocalServiceUtil.getChildCategories(categoryIndex);
		} catch (SystemException e1) {
			// TODO Auto-generated catch block
			//return dstArray;
		}
		
		/***
		 * The current cat
		 */
		AssetCategory lcat = null;
		try {
			lcat = AssetCategoryLocalServiceUtil.getAssetCategory((long)categoryIndex);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<CContent>ccats = new ArrayList<CContent>();
		
		for (int s = 0; s < catItems.size() ; s++) 
		{
    		AssetCategory c = catItems.get(s);
    		CContent _ccat = LiferayContentTools.toCContent(c);

    		ArrayList<JournalArticle >categoryArticles = categoryArticles=LiferayContentTools.getCategoryArticles(uuid,appIdentifier,c.getCategoryId());
    		if(categoryArticles==null || categoryArticles.size()==0){
    			continue;
    		}
    		_ccat.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(catType);
    		ccats.add(_ccat);
		}
    	
		//ArrayList cItems = CListItemTools.toListItems((ArrayList)sub,"pmedia.types.ArticleCategory",ECMContentSourceType.JoomlaCategory,getDs());
    	
    	CList list = new CList();
    	//cItems = CListItemTools.setBaseRef(cItems, getDs());
    	list.baseRef=getDs().getUrl();
    	list.refId=refId;
    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(catType);
    	list.setItems(ccats);
    	list.setTitle(lcat.getTitle());
    	list.setRefId(categoryIndex);
    	
    	JSONSerializer serializer = new JSONSerializer();
    	String jsonres = serializer.deepSerialize(list);
    	serializer.exclude("items.asListItem");
    	sendOutput(request,response,jsonres);
    	return list;
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
	
    public void doGetList(HttpServletRequest request, HttpServletResponse response,
    		ECMContentSourceType catType,
			ECMContentSourceType contentType
			) throws ServletException, IOException
    {
    	getCMObjects(request, response, true);
    	if(application==null)
    	{
    		return;
    	}
    	
    	String type = "Articles";
    	if(contentType==ECMContentSourceType.StaticWebContentVenue){
    		type="Locations";
    	}
    	
    	List<AssetCategory>cats = LiferayContentTools.getCategoriesByType(uuid,appIdentifier,refId,type,null);
    	if(cats == null){
    		return404Page();
    	}
    	if(LiferayContentTools.hasSubCategories(cats, refId))
    	{
    		doGetArticleCategories(refId,catType,contentType,request, response);
    	}else{
    		doGetArticleList(refId, catType, contentType, request, response);
    	}
    }
    
    public void doGetDetails(HttpServletRequest request, HttpServletResponse response,
    		ECMContentSourceType catType,
			ECMContentSourceType contentType
			) throws ServletException, IOException
    {
    	
    	getCMObjects(request, response, true);
    	
    	if(application==null)
    	{
    		return;
    	}
    	JournalArticle  larticle= null ;
    	try {
			larticle = JournalArticleLocalServiceUtil.getArticle(refId);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(larticle==null)
    	{
    		return;
    	}

    	CContent content = LiferayContentTools.toCContent(uuid, appIdentifier, larticle, 0,cType,pmedia.types.Constants.MOBILE_WEB_APP);
    	content.setRefId((int) larticle.getId());
    	LiferayContentTools.setCustomFields(content, larticle, uuid, appIdentifier, "Liferay", cType,pmedia.types.PlatformGroup.IPHONE,lang);
    	JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
      	
    	String translationText= content.getDescription();
    	
    	String _cType = request.getParameter("cType");
    	if(_cType!=null){
    		
    	}else{
    		_cType="" + ECMContentSourceTypeTools.TypeToInteger(contentType);
    	}
    	
    	content.sourceType=""+_cType;
    	String jsonres = serializer.deepSerialize(content);
    	sendOutput(request,response,jsonres);
    	/*
    	ArticleData article = new ArticleData();
    	
    	article.setDescription(plainContent);
    	article.setTitle(larticle.getTitle(LocaleUtil.getDefault()));
    	//article.setRefId(refId)
		article.getAnnotations();
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
    		doGetList(request, response,ECMContentSourceType.StaticWebContentCategory,ECMContentSourceType.StaticWebContent);
    	}
    	

    	if(action.equals("detail"))
    	{
    		doGetDetails(request, response,ECMContentSourceType.StaticWebContentCategory,
    				ECMContentSourceType.StaticWebContent);

    	}
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
