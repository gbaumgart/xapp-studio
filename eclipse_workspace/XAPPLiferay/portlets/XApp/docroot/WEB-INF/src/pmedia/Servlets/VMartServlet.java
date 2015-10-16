package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
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
import pmedia.DataUtils.VMartTools;
import pmedia.html.factory.WhiteListDataFactory;
import pmedia.html.tools.HTMLConverter;
import pmedia.html.types.ClientFilter;
import pmedia.types.ArticleData;
import pmedia.types.CContent;
import pmedia.types.CList;
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
import cmx.data.CContentStorageUtils;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;
import cmx.types.ECMBannerSourceType;
import cmx.types.ECMContentSourceType;
import flexjson.JSONSerializer;


public class VMartServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;

    public void doGetContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	getCMObjects(request, response, true);
    	
    	if(application==null)
    	{
    		return404Page();
    	}
    	ArrayList articles = getDataSourceCache().getByType(ECMContentSourceType.VMartProductItem);
		
    	ArticleData article = VMartTools.getArticleByRefId(articles, refId);
      	if(article==null)
      	{
      		
      		return404Page();
      	}
      	article.getPictures();
      	
      	CContent content = CContentFactory.fromArticleData(article, uuid, appIdentifier, getDs().getUid(),ECMContentSourceType.VMartProductItem, null, Constants.USERAGENT_IPHONE_NATIVE, lang, 120);

      	ECMContentSourceType contentType = ECMContentSourceType.VMartProductItem;
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
    	
    	String jsonres = serializer.deepSerialize(content);
    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	response.setHeader("attachment", "article.json");
    	response.getWriter().write(jsonres);
    	
    }
    protected void doContent(PrintWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
    }
  
    public void doGetArticleCategories(Category cat,ArrayList<Category>cListAll, int categoryIndex,HttpServletResponse response) throws ServletException, IOException
    {

    	ArrayList<Category>sub = CategoryTools.getCategoriesByParentIndex(cListAll, categoryIndex);
    	ArrayList cItems = CListItemTools.toListItems((ArrayList)sub,"pmedia.types.VMartCategory",ECMContentSourceType.VMartCategory,getDs(),StringUtils.getLocaleFromLang(lang));
    	
    	CList list = new CList();
    	cItems = CListItemTools.setBaseRef(cItems, getDs());
    	list.setItems(cItems);
    	list.setTitle(cat.getTitle());
    	list.setRefId(categoryIndex);
    	
    	JSONSerializer serializer = new JSONSerializer();
    	String jsonres = serializer.deepSerialize(list);
    	
    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	response.getWriter().write(jsonres);
    	
    }
    public void doGetArticleList(Category cat, ArrayList articles, int categoryIndex,HttpServletResponse response) throws ServletException, IOException
    {
    	ArrayList cItems = CListItemTools.toListItems(articles,null,ECMContentSourceType.VMartProductItem,getDs(),StringUtils.getLocaleFromLang(lang));
    	CList list = new CList();
    	list.setItems(cItems);
    	list.setTitle(cat.getTitle());
    	list.setRefId(categoryIndex);
    	cItems = CListItemTools.setBaseRef(cItems, getDs());
    	JSONSerializer serializer = new JSONSerializer();
    	String jsonres = serializer.deepSerialize(list);
    	serializer.include("items");
    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	response.getWriter().write(jsonres);
    	
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void doGetList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
			// TODO: handle exception
		}

    	
    	
		ArrayList cats =dsc.getByType(ECMContentSourceType.VMartCategory);
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
					ECMContentSourceType.VMartCategory, 
					WhiteListDataFactory.mobileBase(application.getApplicationIdentifier()), 
							pmedia.types.Constants.USERAGENT_IPHONE_NATIVE, lang);
    	}
    	if(refId==-1){
    		refId=0;
    	}
    	ArrayList articles =dsc.getByType(ECMContentSourceType.VMartProductItem);
		if(cats!=null && cats.size() > 0)
		{
			Category cat = CategoryTools.getCatByIndex(cats, refId);
			// is location category menu !
			if( CategoryTools.hasSubCategories(cats, refId))
			{
				doGetArticleCategories(cat,cats, refId, response);
				return;
			}else{
				
				
				if(articles!=null && articles.size() > 0 )
				{
					ArrayList<ArticleData >categoryArticles = VMartTools.getArticlesByParent(articles, refId);
					if(categoryArticles!=null && categoryArticles.size() > 0 )
					{
						doGetArticleList(cat,categoryArticles, refId, response);
						
					}else{
						CList list = new CList();
				    	list.setBaseRef(getDs().getUrl());
				    	list.setTitle(cat.getTitle());
				    	list.setRefId(refId);
				    	list.setItems(new ArrayList<CListItem>());
				    	
						String rawDescription = cat.getDescription();
						
						String newRawDescription = ClientFilter.filter(rawDescription, uuid, appIdentifier, getDs().getUid(),ds.getUrl(),ECMContentSourceType.JoomlaCategory,""  + cat.getRefId() , null, null, null, 80);
				    	list.setIntroText(newRawDescription);
				    	
				    	JSONSerializer serializer = new JSONSerializer();
				    	String jsonres = serializer.deepSerialize(list);
				    	response.getWriter().write(jsonres);
					}
				}
			}
		}
    }

   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String action= request.getParameter("action");
    	if(action !=null && action.equals("list"))
    	{
    		doGetList(request, response);
    	}
    	else if(action.equals("content"))
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
    }

}
