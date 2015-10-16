package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import pmedia.AssetTools.DownloadManager;
import pmedia.DataJSONTransformer.ArticleDataTranslationTransformer;
import pmedia.DataJSONTransformer.LocationDataTransformer;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;

import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.Indexing.DomainIndexer;
import pmedia.Indexing.DomainSearch;
import pmedia.Indexing.IndexTools;
import pmedia.SearchBeans.EventSearch;

import pmedia.types.ArticleData;
import pmedia.types.Category;
import pmedia.types.LocationData;
import pmedia.types.PMDataTypes;
import pmedia.types.PMPlatformRenderConfiguration;
import pmedia.types.PMSearchFieldTypes;
import pmedia.types.SearchConfiguration;
import pmedia.types.SearchResult;
import pmedia.types.SectionedSearchResults;
import pmedia.utils.StringUtils;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class SearchServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    public void createLuceneIndexes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	String domain=(String)request.getSession().getAttribute("domain");
    	if(domain==null){domain = "ibiza";}
    	
    	String lang=(String)request.getSession().getAttribute("lang");
    	if(lang==null){lang = "en";	}
    	
    	//String indexDirectoryStr = path + "index/" + lang;
    	String path = System.getProperty("webapp.root") + "db/" + domain + "/";
    	
    	DomainIndexer indexer = new DomainIndexer(path, domain, lang);
    	indexer.initIndexer();
    	indexer.indexAll();
    	//indexTools.createIndex(request.getParameter("lang"));
    	
    }
    public void searchManually(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	/*
    	String domain=(String)request.getParameter("domain");
    	if(domain==null){domain = "ibiza";}
    	
    	String lang=(String)request.getParameter("lang");
    	if(lang==null){lang = "en";	}
    	
    	String query=(String)request.getParameter("q");
    	if(query==null){query= "ibiza";	}
    	
    	String type=(String)request.getParameter("type");
    	if(type==null){type= "" + PMDataTypes.DITT_JLOCATION;	}
    	
    	String path = System.getProperty("webapp.root") + "db/" + domain + "/";
    	DomainSearch search = new DomainSearch(path, domain, lang, query);
    	
    	ArrayList<SearchResult> results = null ;
    	
    	results = search.search();
    	System.out.println("Found " + results.size() + " hits.");
    	
    	if(results==null)
    		return;
    		
    	JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
    	String jsonres = serializer.serialize(results);
    	//response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	
    	response.getWriter().write(jsonres);
    	
    	*/
    }
    public void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String domain=(String)request.getParameter("domain");
    	if(domain==null){domain = "ibiza";}
    	
    	String lang=(String)request.getParameter("lang");
    	if(lang==null){lang = "en";	}
    	
    	String query=(String)request.getParameter("q");
    	if(query==null){query= "ibiza";	}
    	
    	String platform=(String)request.getSession().getAttribute("UserAgent");
    	if(platform==null){platform= "PC";	}
    	
    	String searchConfigurationStr=(String)request.getParameter("sc");
    	JSONDeserializer derializerSC = new JSONDeserializer<SearchConfiguration>();
    	
    	
    	SearchConfiguration searchConfiguration= null; 
    	//searchConfigurationStr=  ""
    	//derializerSC.use("searchSources.values", PMSearchFieldTypes.class);

    	if(searchConfigurationStr!=null && searchConfigurationStr.length() > 0)
    	{
    		//System.out.println(searchConfigurationStr);
    		try {
    			searchConfiguration= (SearchConfiguration) derializerSC.deserialize(searchConfigurationStr);	
			} catch (Exception e) {
				searchConfiguration = SearchConfiguration.allSourcesDefault();
				System.out.println(e.getMessage());
			}
    	}else
    	{
    		searchConfiguration = SearchConfiguration.allSourcesDefault();
    	}
    	
    	
    	String path = System.getProperty("webapp.root") + "db/" + domain + "/";
    	DomainSearch search = new DomainSearch(path, domain, lang, query);
    	
    	SectionedSearchResults results = null ;
    	
    	results = search.searchWithConfiguration(searchConfiguration,query,platform);
    	IndexTools.updateSearchResultIcons(results, platform, domain, lang);
    	
    	
    	//System.out.println("Found " + results.getSearchResults().size() + " hits.");
    	
    	if(results==null)
    		return;
    		
    	JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
    	String jsonres = serializer.deepSerialize(results);
    	/*
    	PMPlatformRenderConfiguration iPhone = new PMPlatformRenderConfiguration();
    	PMPlatformRenderConfiguration tablet = new PMPlatformRenderConfiguration();
    	ArrayList<PMPlatformRenderConfiguration>list = new ArrayList<PMPlatformRenderConfiguration>();
    	list.add(tablet);
    	list.add(iPhone);
    	*/
    	//SearchConfiguration sc = SearchConfiguration.allSourcesDefault();
    	//JSONSerializer serializerSC = new JSONSerializer();
    	///String jsonresSC = serializer.deepSerialize(list);
    	//System.out.println(" sc json res : \n" + jsonresSC);
    	
    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	
    	response.getWriter().write(jsonres);
    	
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String action= request.getParameter("action");
    	if(action==null){action= "search";}
    	String type= request.getParameter("type");
    	if(type==null){type= "automatic";}
    	if(action.equals("search"))
    	{
    		if(type.equals("automatic"))
    			search(request, response);
    		
    		if(type.equals("manually"))
    			searchManually(request, response);
    		
    	}
    	
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
