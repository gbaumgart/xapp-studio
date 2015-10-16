package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmedia.DataJSONTransformer.ArticleDataTranslationTransformer;
import pmedia.DataJSONTransformer.BaseDataTransformer;
import pmedia.DataManager.Cache;
import pmedia.DataManager.DomainCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.MediaUtils;
import pmedia.types.ArticleData;
import pmedia.types.CFeedItem;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.Category;
import pmedia.types.Domain;
import pmedia.types.MediaItemBase;
import pmedia.types.PMDataTypes;
import pmedia.types.PictureTransformOptions;
import pmedia.utils.CListItemTools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import cmx.manager.GTrackerManager;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import flexjson.JSONSerializer;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.model.atom.Person;
import com.google.gdata.util.ServiceException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.service.AssetCategoryLocalService;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetCategoryServiceUtil;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;


public class RSSServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
    
    //private static final String BLOG_URL = &quot;http://javabeans.asia/rss.xml&quot;;
    //private static final String CONTENT_TYPE = &quot;application/json&quot;;
    private FeedFetcherCache feedInfoCache = null;
    private FeedFetcher feedFetcher = null;
    private SyndFeed theFeed=null;
    
    private String errorMessage=null;
    
    protected void doContent(PrintWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
    }
    private void initFetcher(){
    	if(feedInfoCache==null){
    		feedInfoCache = HashMapFeedInfoCache.getInstance();
    	}
    	if(feedFetcher==null)
    		feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
    	
    }
    private SyndFeed feedFetcher(String url) 
    {
        SyndFeed feed = null;
        try {
            feed = feedFetcher.retrieveFeed(new URL(url));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        } catch (FetcherException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) 
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feed;
    }
    
    public CList doYoutube(String ref)
    {
    	
    	
    	CList list = new CList();
    	URL metafeedUrl = null;//new URL(ref);
    	ref = ref.replace("?alt=rss", "");
    	ref = ref.replace("/base/", "/api/");
    	//http://gdata.youtube.com/feeds/base/users/mc007ibi/uploads?alt=rss
    	ref = ref.replace("&v=2&orderby=published&client=ytapi-youtube-profile", "");
    	try {
			metafeedUrl = new URL(ref);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}
    	
    	YouTubeService myService = new YouTubeService(ref);
    	//http://gdata.youtube.com/feeds/api/users/mc007ibi/uploads&v=2&orderby=published&client=ytapi-youtube-profile
    	
    	//http://gdata.youtube.com/feeds/api/users/mc007ibi/uploads//correct
    	//http://gdata.youtube.com/feeds/api/users/mc007ibi/uploads
        //System.out.println("Getting favorite video entries...\n");
        
        ArrayList<CListItem>result = new ArrayList<CListItem>();
        
        VideoFeed resultFeed = null; 
        
        try {
			resultFeed = myService.getFeed(metafeedUrl, VideoFeed.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        
        List<VideoEntry> entries = resultFeed.getEntries();
        for(int i=0; i<entries.size(); i++) 
        {
         
          VideoEntry entry = entries.get(i);
          CFeedItem item = new CFeedItem();
          
          List<com.google.gdata.data.Person> authors = entry.getAuthors();
          if(authors!=null && authors.size()>0){
        	  com.google.gdata.data.Person  p = authors.get(0);
        	  if(p.getName()!=null){
        		  item.setOwnerRefStr(p.getName());
        	  }
          }
      	  
          if(entry.getTitle()!=null)
          {
        	  item.setTitle(entry.getTitle().getPlainText());
          }else{
        	  continue;
          }
          //System.out.println("\t" + entry.getTitle().getPlainText());
          int count = 1;
          
          YouTubeMediaGroup mediaGroup = entry.getMediaGroup();
          if(mediaGroup==null){
        	  continue;
          }
          
          ArrayList<String> listT = new ArrayList<String>();
          MediaThumbnail mediaThumbnailF =null;
          int width=0;
          for(MediaThumbnail mediaThumbnail : mediaGroup.getThumbnails()) 
          {
              //System.out.println("Thumbnail URL: " + mediaThumbnail.getUrl());
              //System.out.println("\t\tThumbnail Time Index: " +
              //mediaThumbnail.getTime());
              //System.out.println();
        	  if(mediaThumbnail.getWidth() > width){
        		  mediaThumbnailF = mediaThumbnail;
        		  width = mediaThumbnail.getWidth();
        	  }
        	  listT.add(mediaThumbnail.getUrl());
        	  if(item.getIconUrl()==null)
        	  {
        		 /// item.setIconUrl(mediaThumbnail.getUrl());
        	  }
              //entry.get
              //break;
          }
          
          if(item.getIconUrl()==null && mediaThumbnailF!=null)
    	  {
    		  item.setIconUrl(mediaThumbnailF.getUrl());
    	  }
          
          for(YouTubeMediaContent mediaContent : mediaGroup.getYouTubeContents()) 
          {
        	  /*
              System.out.println("\t\tMedia Location: "+ mediaContent.getUrl());
              System.out.println("\t\tMedia Type: "+ mediaContent.getType());
              System.out.println("\t\tDuration: " + mediaContent.getDuration());
              System.out.println();
              */
        	  
        	  //item.setDateString(mediaContent.getDuration());
        	 
        	  if(item.getDateString() ==null)
        	  {
        		  	
        		  	int _secs = mediaContent.getDuration();
        		   // int _minutes = (_secs / 60);
        	       // int _seconds = ((int)_secs % 60);
        	        
        	        
        	        
        	        /***
        	         * 
        	         */
        	        int hours = (int) Math.floor(_secs / 3600);
        	        int mins = (int) Math.floor((_secs - hours*3600) / 60);
        	        int s = _secs - (hours*3600 + mins*60);

        	        String mins2 = (mins<10 ? "0"  + mins : "" + mins);
        	        String s2 = (s<10 ? "0"  + s :"" + s); 

        	        String time = (hours>0? hours + ":":"") + mins2 + ":" +s2;
        	        item.setDateString(time);
        	        
        	        //System.out.println("time " + time);
        	        
        	        
        	        /*
        	        $hours = floor($seconds / 3600);
        	        $mins = floor(($seconds - $hours*3600) / 60);
        	        $s = $seconds - ($hours*3600 + $mins*60);

        	        $mins = ($mins<10?"0".$mins:"".$mins);
        	        $s = ($s<10?"0".$s:"".$s); 

        	        $time = ($hours>0?$hours.":":"").$mins.":".$s;
        	        */
        	        
        	  }
            }

          item.dataClass = "pmedia.types.CFeedItem";
          item.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.ExternalLink);
          item.setWebLink(entry.getMediaGroup().getPlayer().getUrl());
          item.ref=java.net.URLDecoder.decode(entry.getMediaGroup().getPlayer().getUrl());
          
          if(item.getLocRefStr()==null && entry.getPublished()!=null)
    	  {
        	  Date p = new Date(entry.getPublished().getValue());
        	  try {
        		  item.setLocRefStr(DateFormat.getTimeInstance(DateFormat.MEDIUM).format(p));
			} catch (Exception e) {
				// TODO: handle exception
			}
        	  
    	  }

          result.add(item);
          
          //System.out.println(count + ") " + entry.getTitle().getPlainText() + ": "  + entry.getMediaGroup().getPlayer().getUrl() + " : " + entry.getMediaGroup().getThumbnails().get(0).getUrl());
        }
        
        if(result.size()>0)
        {
    		list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.RSS);
	    	list.setItems(result);
	    	return list;
        }
        
    	return null;
    }
    
    public CList doPicassa(String ref)
    {
    	return null;
    }
    public CList doGData(String ref)
    {
    	return null;
    }
    public void doGetList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	/*
    	ServiceContext sctx = new ServiceContext();
    	
    	sctx.setScopeGroupId(49104);
    	sctx.setSignedIn(true);
    	long uud = 10195;
    	sctx.setCompanyId(10153);
    	sctx.setUserId(uud);
    	
    	
    	AssetCategory cat = null;
    	java.util.Map<java.util.Locale, java.lang.String> titleMap = new java.util.HashMap<java.util.Locale, java.lang.String>();
    	java.util.Map<java.util.Locale, java.lang.String> descMap = new java.util.HashMap<java.util.Locale, java.lang.String>();
    	Locale loc = LocaleUtil.getDefault();
    	titleMap.put(LocaleUtil.getDefault(), "cattitlewfcsd");
    	descMap.put(LocaleUtil.getDefault(), "cattitlewfcsd");
    	//AssetCategoryLocalService.
		try 
		{
			//cat = AssetCategoryLocalServiceUtil.addCategory(0, titleMap, descMap, 49117, null, sctx);
			cat = AssetCategoryLocalServiceUtil.addCategory(uud, 0, titleMap, descMap, 49117, null, sctx);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		*/
		//AssetCategoryLocalServiceUtil serviceUtil = AssetCategoryLocalServiceUtil.get
    	
    	
    	
    	if(ref==null)
    	{
    		return404Page();
    		return;
    	}
    	
    	//GTrackerManager.trackXAPPEvent(GTrackerManager.openFeed,ref);
    	String oriRef = "" + ref;
    	
    	//ref = ref.substring(ref.indexOf("http"));
    	ref=java.net.URLDecoder.decode(ref);
    	CList list =null;
    	
    	if(ref.contains("youtube"))
    	{
    		list = doYoutube(ref);
    		if(list!=null){
    			list.refIdString=oriRef;
    			//http://gdata.youtube.com/feeds/base/users/mc007ibi/uploads?alt=rss&v=2&orderby=published&client=ytapi-youtube-profile
        		list.setBaseRef(ref);
    		}
    	}
    	
    	if(list==null)
    	{
	    	if(ref!=null && ref.length() >0)
	    	{
	    		initFetcher();
	    		theFeed = feedFetcher(ref);
	    	}
    	}
    	
    	
    	if(list==null)
    	{
	    	if(theFeed!=null)
	    	{
		    	list = new CList();
		    	list.setItems(CListItemTools.toListItems(theFeed, "pmedia.types.CFeedItem"));
		    	list.setTitle(theFeed.getTitle());
		    	list.setBaseRef(ref);
		    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.RSS);
		    	list.refIdString=ref;
		    	
		    	CFeedItem listItem =null;
		    	if(list.items.size()>0){
		    		
		    		listItem =(CFeedItem) list.items.get(0);
		    		if(listItem.mediaType!=null && listItem.mediaType.equals("image")){
		    			list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GooglePicassaAlbum);
		    			//list.type=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GooglePicassaAlbum);
		    		}
		    		
		    	}
		    	
	    	}else{
	    		list = new CList();
	    		CFeedItem item = new CFeedItem();
	        	item.setTitle("Error : Invalid Feed");
	        	
	    		item.dataClass = "pmedia.types.CFeedItem";
				item.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.ExternalLink);
		    	list.setItems(new ArrayList<CFeedItem>());
		    	list.getItems().add(item);
		    	list.setBaseRef(ref);
		    	
		    	list.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.RSS);
		    	list.refIdString=ref;
	    	}
    	}
    
    	if(list!=null)
    	{
	    	JSONSerializer serializer = new JSONSerializer();
	    	String jsonres = serializer.deepSerialize(list);
	    	serializer.include("items");
	    	sendOutput(request, response,jsonres);
    	}
    	
    }
    
    
    public void doGetDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	getCMObjects(request, response, true);
    	
    	/*
    	if(application==null)
    	{
    		return;
    	}*/
    	
    	
		DataSourceBase ds = application.getDataSource(dataSource);
		if(ds==null)
		{
			return;
		}
		Domain domain = Cache.getDomain(application.applicationIdentifier);
		
		ArrayList articles =application.getDataListByType(appManager,ECMContentSourceType.JoomlaArticle, ds);
		
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
		sendOutput(request, response,jsonres);
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
    	if(action.equals("list"))
    	{
    		doGetList(request, response);
    	}

    	if(action.equals("detail"))
    	{
    		doGetDetails(request, response);
    	}
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
