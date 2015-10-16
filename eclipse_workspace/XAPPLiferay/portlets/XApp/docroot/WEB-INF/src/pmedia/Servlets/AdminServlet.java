package pmedia.Servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pmedia.AssetTools.AssetTools;
import pmedia.AssetTools.DownloadManager;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ApplicationConfigurationTools;
import pmedia.DataUtils.ArticleTools;
import pmedia.Indexing.DomainIndexer;
import pmedia.types.ApplicationConfiguration;
import pmedia.types.ArticleData;
import pmedia.types.LocationData;
import pmedia.types.PMPlatformRenderConfiguration;
import cmx.types.Application;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class AdminServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
    public void downloadArticleContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	ArrayList<ArticleData> lList = ServerCache.getInstance().getDC("ibiza").get(DomainCache.JARTICLES);
    	if(lList == null)
    		return;
    	
    	/**
    	 * The icon configuration : 
    	 */
    	PMPlatformRenderConfiguration renderConfig = null;
    	String renderConfigString=request.getParameter("rc");
    	JSONDeserializer derializerSC = new JSONDeserializer<PMPlatformRenderConfiguration>();

    	//{"class": "pmedia.types.PMPlatformRenderConfiguration","listItemHeight": "70","listItemIconHeight": "60","platform": "IPAD"}
    	if(renderConfigString!=null && renderConfigString.length() > 0)
    	{
    		//System.out.println(searchConfigurationStr);
    		try {
    			renderConfig= (PMPlatformRenderConfiguration) derializerSC.deserialize(renderConfigString);	
			} catch (Exception e) {
				renderConfig = new PMPlatformRenderConfiguration();
				System.out.println(e.getMessage());
			}
    	}else
    	{
    		renderConfig = new PMPlatformRenderConfiguration();
    	}
    	/**
    	 * 	Location pictures files pure 
    	 */

    	for(int i  = 0 ; i < lList.size() ; i++)
    	{
    		ArticleData loc = lList.get(i);
    		ArrayList<String>files  = loc.getPictures();
    		if(files==null)
    		{
    			files = ArticleTools.getPicturesFromJArticleTranslation("ibiza", loc, "en");
    		}
    		if(files !=null && files.size() > 0)
    		{
    			for(int j  = 0 ; j < loc.getPictures().size() ; j++)
    	    	{
    				String _file = loc.getPictures().get(j);
    				if(j==0)
    				{
    					String iconRemoteFile = AssetTools.getIconRemoteUrl(_file, renderConfig);
    					String iconLocalFile = AssetTools.makeIconPath(_file,renderConfig.platform);
    					DownloadManager.downloadFileManaged(iconRemoteFile,iconLocalFile, false);
    					//System.out.println("icon = " + iconLocalFile);
    				}
    				DownloadManager.downloadFileManaged(System.getProperty("ibiza.locationFiles") + _file,_file, false);
    	    	}
    			//Downloader.downloadFileManaged(files., dst, overwrite)
    		}
    	}
    }
    
    
    public void downloadLocationContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	ArrayList<LocationData> lList = ServerCache.getInstance().getDC("ibiza").get(DomainCache.LOCATIONS3);
    	if(lList == null)
    		return;
    	
    	/**
    	 * The icon configuration : 
    	 */
    	PMPlatformRenderConfiguration renderConfig = null;
    	String renderConfigString=request.getParameter("rc");
    	JSONDeserializer derializerSC = new JSONDeserializer<PMPlatformRenderConfiguration>();

    	//{"class": "pmedia.types.PMPlatformRenderConfiguration","listItemHeight": "70","listItemIconHeight": "60","platform": "IPAD"}
    	if(renderConfigString!=null && renderConfigString.length() > 0)
    	{
    		//System.out.println(searchConfigurationStr);
    		try {
    			renderConfig= (PMPlatformRenderConfiguration) derializerSC.deserialize(renderConfigString);	
			} catch (Exception e) {
				renderConfig = new PMPlatformRenderConfiguration();
				System.out.println(e.getMessage());
			}
    	}else
    	{
    		renderConfig = new PMPlatformRenderConfiguration();
    	}
    	/**
    	 * 	Location pictures files pure 
    	 */

    	for(int i  = 0 ; i < lList.size() ; i++)
    	{
    		LocationData loc = lList.get(i);
    		ArrayList<String>files  = loc.getPictures();
    		if(files !=null && files.size() > 0)
    		{
    			for(int j  = 0 ; j < loc.getPictures().size() ; j++)
    	    	{
    				String _file = loc.getPictures().get(j);
    				if(j==0)
    				{
    					String iconRemoteFile = AssetTools.getIconRemoteUrl(_file, renderConfig);
    					String iconLocalFile = AssetTools.makeIconPath(_file,renderConfig.platform);
    					DownloadManager.downloadFileManaged(iconRemoteFile,iconLocalFile, false);
    					//System.out.println("icon = " + localFile);
    				}
    				DownloadManager.downloadFileManaged(System.getProperty("ibiza.locationFiles") + _file,_file, false);
    	    	}
    			//Downloader.downloadFileManaged(files., dst, overwrite)
    		}
    	}
    }
    
    public void createLuceneIndexes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	String domain=(String)request.getParameter("domain");
    	if(domain==null){domain = "ibiza";}
    	
    	String lang=(String)request.getParameter("lang");
    	if(lang==null){lang = "en";	}
    	
    	//String indexDirectoryStr = path + "index/" + lang;
    	String path = System.getProperty("webapp.root") + "db/" + domain + "/";
    	
    	DomainIndexer indexer = new DomainIndexer(path, domain, lang);
    	indexer.initIndexer();
    	
    	indexer.indexAll();
    	indexer.finish();
    	//indexTools.createIndex(request.getParameter("lang"));
    }
    public void getAppConfig(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    	
    	getCMObjects(request, response, true);
/*
    	WURFLHolder wurfl = (WURFLHolder) getServletContext().getAttribute(WURFLHolder.class.getName());
		
		WURFLManager manager = wurfl.getWURFLManager();

		Device device = manager.getDeviceForRequest(request);
*/		
    	String dataSource = request.getParameter("dataSource");
    	
    	//String dataRef= request.getParameter("ref");
    	

    	ApplicationConfiguration config=null;
    	String appSettingsPath = null;
    	
    	Application app = appManager.getApplication(uuid,appIdentifier,"Debug");
    	if(app!=null)
    	{
    		if(app.getAppSettings()!=null)
    		{
    			config = app.getAppSettings();
    		}
    		
    		appSettingsPath = appManager.getUserAppPath(uuid, appIdentifier) + "appSettings.xml";
    		if(appSettingsPath!=null && appSettingsPath.length() > 0)
    		{
    			ArrayList<ApplicationConfiguration>configs = null;
    			try {
					 configs = ApplicationConfigurationTools.readAppConfigFromFile(appSettingsPath);
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(configs!=null && configs.size()>0){
					config = configs.get(0);
					app.setAppSettings(config);
				}
    			
    		}
    		
    	}
		
    	
    	if(config!=null)
		{
			
			JSONSerializer serializer = new JSONSerializer();
	    	String jsonres = serializer.serialize(config);
	    	response.getWriter().write(jsonres);
		}
		
		
    	/*
    	String domain=(String)request.getParameter("domain");
    	if(domain==null){domain = "ibiza";}
    	
    	
    	String appIdentifier=(String)request.getParameter("identifier");
    	if(appIdentifier==null){appIdentifier= "imDefault";}
    	
    	
    	*/
    	
		//ApplicationConfiguration config = ApplicationConfigurationTools.getAppConfigByIdentifier(cList, appIdentifier);
		
		/*
    	ArrayList<ApplicationConfiguration> cList = ServerCache.getInstance().getDC("ibiza").get(DomainCache.APPCONFIGS);
    	if(cList!=null && cList.size() > 0)
    	{
    		ApplicationConfiguration config = ApplicationConfigurationTools.getAppConfigByIdentifier(cList, appIdentifier);
    		if(config!=null)
    		{
    			
    			JSONSerializer serializer = new JSONSerializer();
    	    	String jsonres = serializer.serialize(config);
    	    	response.getWriter().write(jsonres);
    		}
    	}
    	*/
    	
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String action= request.getParameter("action");
    	String type= request.getParameter("type");
    	
    	if(action.equals("appConfig"))
    	{
    		getAppConfig(request, response);
    	}
    	
    	if(action.equals("download"))
    	{
    		if(type!=null && type.equals("locations"))
    		{
    			downloadLocationContent(request, response);
    		}

    		if(type!=null && type.equals("articles"))
    		{
    			downloadArticleContent(request, response);
    		}
    	}
    	
    	if(action.equals("index"))
    	{
    		createLuceneIndexes(request, response);
    	}

    	
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
