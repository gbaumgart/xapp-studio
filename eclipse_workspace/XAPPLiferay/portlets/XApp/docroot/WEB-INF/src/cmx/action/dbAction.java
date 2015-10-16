

package cmx.action;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.json.annotations.SMDMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pmedia.DataManager.ServerCache;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.utils.ECMContentSourceTypeTools;
import cmx.cache.DataSourceCache;
import cmx.manager.GTrackerManager;
import cmx.tools.Crypto;
import cmx.tools.DBConnectionChecker;
import cmx.tools.GDataDownloader;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DBConnectionError;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import cmx.types.SQLDataSource;
import cmx.types.XCDataSource;
import cmx.types.XMLDataSource;

import com.opensymphony.xwork2.Action;

//import com.pm.Catalog;
//import com.pm.DBContextHolder;
//import com.pm.DBSource;


//@Namespace("/db")
//@Result(name="success",location="/dummy.jsp")
//@Result(location="${redirectURL}", type="redirect")

public class dbAction
   extends CMBaseAction implements ServletRequestAware,ServletResponseAware
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9097347805333543588L;


	private String cmdType;
	private String lang;
	private String type;
	private String dbName="joomla_7";
	
	private String user="joomla_7";
	private String prefix="jos_";
	private String password="kSg2a9LX1_";
	private String host="178.63.34.114";
	
	private DBConnectionError e = new DBConnectionError();
	
	
	private ApplicationManager appManager=null;
	public Application application=null;
	
	@SMDMethod
    public CList detectFeeds(
    		String url) 
	{
		CList result = new CList();
		result.setItems(new ArrayList<CListItem>());
		
		
		if(!url.contains("http://"))
		{
			url = "http://" + url; 
		}	
		  
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) 
		{
			e.printStackTrace();
			return result;
		}
	    
		Elements imports = doc.select("link[href]");
		for (Element link : imports) 
		{
            //print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
			if(link.attr("type")!=null && link.attr("type").equals("application/rss+xml"))
			{
				CListItem item = new CListItem();
				item.setWebLink(link.attr("abs:href"));
				item.setTitle(link.attr("title"));
				result.getItems().add(item);
			}
        }
		
		if(result.getItems().size()==0)
		{
			CListItem item = new CListItem();
			item.setWebLink(url);
			result.getItems().add(item);
		}
		
		return result;
	}

	@SMDMethod
    public DBConnectionError checkGoogleService(
    		String service,
    		String channel, 
    		String user, 
    		String password) 
	{
		DBConnectionError f = new DBConnectionError();
		if(service.equals("Youtube")){
			f = DBConnectionChecker.checkGoogleYoutubeService(service,channel, user, password);
		}
		if(service.equals("Picassa")){
			f = DBConnectionChecker.checkGooglePicassaService(service,channel, user, password);
		}
		if(service.equals("Calendar")){
			f = DBConnectionChecker.checkGoogleCalendarService(service,channel, user, password);
		}
		if(service.equals("Documents"))
		{
			f = DBConnectionChecker.checkGoogleDocumentService(service,channel, user, password);
		}
        return f;
    }
	
	@SMDMethod
    public DBConnectionError checkConnection(
    		String host, 
    		String port, 
    		String dbName, 
    		String user, 
    		String password) 
	{
		DBConnectionError f = DBConnectionChecker.checkConnection(host, port, dbName, user, password);
        return f;
    }
	
	public ApplicationManager getApplicationManager(String identifier)
	{
		appManager = ApplicationManager.getInstance();
		

		return appManager;
	}
	
	@SMDMethod
    public void deleteDataSource(
    		String uuid,
    		String applicationIdentifier,
    		String dsUUID) 
	{
		appManager  = getApplicationManager(null);
		Application app = appManager.getApplication(uuid,applicationIdentifier,"Debug");
		if(app!=null){
			app.flushCache=1;
		}
		DataSourceBase sqlDataSource = app.getDataSource(dsUUID);
		if(sqlDataSource!=null)
		{
			app.getDataSources().remove(sqlDataSource);
		}
		appManager.saveApplication(app);
	}
	@SMDMethod
    public DataSourceBase getDataSource(
    		String uuid,
    		String applicationIdentifier,
    		String dataSourceUID) 
	{
		appManager  = getApplicationManager(null);
		Application app = appManager.getApplication(uuid,applicationIdentifier,"Debug");
		DBConnectionError f = new DBConnectionError();
		DataSourceBase ds = app.getDataSource(dataSourceUID);
		
		return ds;
	}
	@SMDMethod
    public DBConnectionError updateDataSourceEx(
    		String uuid,
    		String applicationIdentifier,
    		String host, 
    		String port, 
    		String dbName,
    		String prefix,
    		String user, 
    		String password,
    		String version,
    		String url,
    		String type,
    		String dataSourceUID) 
	{
		
		appManager  = getApplicationManager(null);
		Application app = appManager.getApplication(uuid,applicationIdentifier,"Debug");
		DBConnectionError f = new DBConnectionError();//DBConnectionChecker.checkConnection(host, port, dbName, user, password);
		DataSourceBase ds = app.getDataSource(dataSourceUID);
		
		if(app!=null){
			app.flushCache=1;
		}
		if(ds!=null)
		{
			if(host!=null && host.length()>0)
			{
				ds.setHost(host);
			}
			
			if(port!=null && port.length()>0)
			{
				ds.setPort(port);
			}
			if(dbName!=null && dbName.length()>0)
			{
				ds.setDatabase(dbName);
			}
			
			
			String key = "asd28071977";
	        Crypto encrypter = new Crypto(key);
			if(user!=null && user.length()>0)
			{
				String userE = encrypter.encrypt(user);
				ds.setUser(userE);
			}
			if(password!=null && password.length()>0)
			{
				String passE = encrypter.encrypt(password);
				ds.setPassword(passE);
			}
			
			if(version!=null && version.length()>0)
			{
				ds.setVersion(version);
			}
			
			if(url!=null && url.length()>0)
			{
				ds.setUrl(url);
			}
			
			if(type!=null && type.length()>0)
			{
				ds.setType(type);
			}
			
			if(prefix!=null && prefix.length()>0)
			{
				ds.setPrefix(prefix);
			}
		}
		
		if(app!=null){
			appManager.saveApplication(app);
			}
		
		
		return f;
		
	}
	
	@SMDMethod
    public DBConnectionError createXCDataSourceEx(
    		
    		String uuid,
    		String applicationIdentifier,
    		String user, 
    		String password,
    		String url,
    		String identifier,
    		String authClass,
    		String rpcUrl) 
	{
		
		
		GTrackerManager.trackXASEvent(GTrackerManager.addDS,type);
		
		appManager  = getApplicationManager(null);
		Application app = appManager.getApplication(uuid,applicationIdentifier,"Debug");
		
		if(app!=null){
			app.flushCache=1;
		}
		DBConnectionError f = new DBConnectionError();//DBConnectionChecker.checkConnection(host, port, dbName, user, password);
		
		String key = "asd28071977";
        Crypto encrypter = new Crypto(key);
		String userE = encrypter.encrypt(user);
		String passE = encrypter.encrypt(password);
		
		XCDataSource dataSource = null;
		
		if(dataSource!=null)
		{
			f.type=-1;
			f.msgInternal="DataSource Already Exists";
			return f;
		}
		
			
		dataSource = new XCDataSource();
		
		//dataSource.setHost(host);
		//dataSource.setPrefix(prefix);
		//dataSource.setDatabase(dbName);
		dataSource.setPassword(password);
		dataSource.setUser(user);
		dataSource.setType("XAppConnect");
		dataSource.setUrl(url);
		dataSource.setId(UUID.randomUUID().toString());
		dataSource.setIdentifier(identifier);
		dataSource.setAuthClass(authClass);
		dataSource.setRPCUrl(rpcUrl);
		app.addDataSource(dataSource);
		if(!dataSource.getUser().equals(userE))
		{
			dataSource.setUser(userE);
		}
		
		if(!dataSource.getPassword().equals(passE))
		{
			dataSource.setPassword(passE);
		}
		
		f.setUid(dataSource.getUid());
		f.setDataSource(dataSource);
		appManager.saveApplication(app);	
        return f;
    }
		
	@SMDMethod
    public DBConnectionError createDataSourceEx(
    		
    		String uuid,
    		String applicationIdentifier,
    		String host, 
    		String port, 
    		String dbName,
    		String prefix,
    		String user, 
    		String password,
    		String version,
    		String url,
    		String type,
    		Boolean testConnection) 
	{
		
		
		GTrackerManager.trackXASEvent(GTrackerManager.addDS,type);
		
		appManager  = getApplicationManager(null);
		if(application!=null){
			application.flushCache=1;
		}
		Application app = appManager.getApplication(uuid,applicationIdentifier,"Debug");
		
		DBConnectionError f = new DBConnectionError();//DBConnectionChecker.checkConnection(host, port, dbName, user, password);
		if(testConnection && type!=null)
		{
			if(type.equals("JoomlaMySQL")){
				f = DBConnectionChecker.checkConnection(host, port, dbName, user, password);				
			}
			if(type.equals("WordpressXML"))
			{
				//f = DBConnectionChecker.checkWPRPCConnection(host, user, password);				
			}
			
			if(type.equals("JoomlaXML"))
			{
				//f = DBConnectionChecker.checkJoomlaRPCConnection(host, user, password);				
			}
			
			if(f.type!=0)
			{
				return f;
			}
		}
		
		String key = "asd28071977";
        Crypto encrypter = new Crypto(key);
		String userE = encrypter.encrypt(user);
		String passE = encrypter.encrypt(password);
		
		DataSourceBase sqlDataSource = null;
		if(type.equals("JoomlaMySQL")){
			sqlDataSource = app.getDataSource(host,dbName);
		}
		if(type.equals("JoomlaXML")){
			sqlDataSource = app.getDataSourceByHost(host);
		}
		if(type.equals("WordpressXML")){
			sqlDataSource = app.getDataSourceByHost(host);
		}
		if(type.equals("WordpressMySQL")){
			sqlDataSource = app.getDataSource(host,dbName);
		}
		if(type.equals("XAppConnect")){
			sqlDataSource = app.getDataSource(host,dbName);
		}
		
		if(sqlDataSource!=null)
		{
			f.type=-1;
			f.msgInternal="DataSource Already Exists";
			return f;
		}
		
		if(type.equals("JoomlaMySQL") || type.equals("WordpressMySQL"))
		{
			sqlDataSource = new SQLDataSource();				
		}
		if(type.equals("WordpressXML")){
			sqlDataSource = new XMLDataSource();
		}
		
		if(type.equals("JoomlaXML"))
		{
			sqlDataSource = new XMLDataSource();
		}
		if(type.equals("XAppConnect"))
		{
			sqlDataSource = new XCDataSource();
		}
		
		if(type.equals("Youtube") || type.equals("Picassa") || type.equals("Calendar") || type.equals("Documents"))
		{
			sqlDataSource = new XMLDataSource();
		}

		if(sqlDataSource==null){
			return f;
		}
		
		if(url.contains("mc007ibi"))
		{
			//host = "192.168.1.37";
		}
		
		sqlDataSource.setHost(host);
		sqlDataSource.setPrefix(prefix);
		sqlDataSource.setDatabase(dbName);
		sqlDataSource.setPassword(password);
		sqlDataSource.setUser(user);
		sqlDataSource.setPrefix(prefix);
		sqlDataSource.setType(type);
		sqlDataSource.setVersion(version);
		sqlDataSource.setUrl(url);
		sqlDataSource.setPort(port);
		sqlDataSource.setId(UUID.randomUUID().toString());
		app.addDataSource(sqlDataSource);
		
		
		if(!sqlDataSource.getUser().equals(userE))
		{
			sqlDataSource.setUser(userE);
		}
		
		if(!sqlDataSource.getPassword().equals(passE))
		{
			sqlDataSource.setPassword(passE);
		}
		
		f.setUid(sqlDataSource.getUid());
		f.setDataSource(sqlDataSource);
		appManager.saveApplication(app);

		DataSourceCache dsc=ServerCache.getDSC(appManager, app, sqlDataSource);
	
		if(sqlDataSource.getType().equals("JoomlaMySQL") || sqlDataSource.getType().equals("JoomlaXML"))
		{
			createDataSourceCContentJoomla(uuid, applicationIdentifier, sqlDataSource.getUid());
		}
		
		if(sqlDataSource.getType().equals("WordpressMySQL"))
		{
			createDataSourceCContentWordpress(uuid, applicationIdentifier, sqlDataSource.getUid());
		}
		
		if(type.equals("Youtube") || type.equals("Picassa") || type.equals("Calendar") || type.equals("Documents"))
		{
			downloadGDataWithDataSourceUID(uuid, applicationIdentifier, type, sqlDataSource.getUid());
		}
		
        //bean.setPrice(quantity * 10);
        return f;
    }
	@SMDMethod
    public DBConnectionError createDataSource(
    		
    		String uuid,
    		String applicationIdentifier,
    		String host, 
    		String port, 
    		String dbName,
    		String prefix,
    		String user, 
    		String password,
    		String version,
    		String url) 
	{
		
		appManager  = getApplicationManager(null);
		Application app = appManager.getApplication(uuid,applicationIdentifier,"Debug");
		
		DBConnectionError f = DBConnectionChecker.checkConnection(host, port, dbName, user, password);
		if(f.type==0)
		{
			String key = "asd28071977";
            Crypto encrypter = new Crypto(key);
			String userE = encrypter.encrypt(user);
			String passE = encrypter.encrypt(password);
			
			DataSourceBase sqlDataSource = app.getDataSource(host,dbName);
			if(sqlDataSource==null)
			{
				sqlDataSource = new SQLDataSource();
				sqlDataSource.setHost(host);
				sqlDataSource.setPrefix(prefix);
				sqlDataSource.setDatabase(dbName);
				sqlDataSource.setPassword(password);
				sqlDataSource.setUser(user);
				sqlDataSource.setPrefix(prefix);
				sqlDataSource.setType("JoomlaMySQL");
				sqlDataSource.setVersion(version);
				sqlDataSource.setUrl(url);
				app.addDataSource(sqlDataSource);
			}
			
			if(!sqlDataSource.getUser().equals(userE))
			{
				sqlDataSource.setUser(userE);
			}
			
			if(!sqlDataSource.getPassword().equals(passE))
			{
				sqlDataSource.setPassword(passE);
			}
			
			f.setUid(sqlDataSource.getUid());
			f.setDataSource(sqlDataSource);
		}
        //bean.setPrice(quantity * 10);
        return f;
    }
	
	@SMDMethod
    public DBConnectionError downloadJoomlaDatabase(
    		String uuid,
    		String applicationIdentifier,
    		String host, 
    		String port, 
    		String dbName,
    		String dbPrefix, 
    		String user, 
    		String password,
    		String version) 
	{
		DBConnectionError f = null;
		appManager  = getApplicationManager(null);
		application= appManager.getApplication(uuid,applicationIdentifier,"Debug");
		
		String savepath = appManager.getUserAppPath(uuid, applicationIdentifier);
		savepath+="/datasources/" + host + "/" + dbName + "/";
		File dbPath = new File(savepath);
		if(!dbPath.exists())
		{
			dbPath.mkdirs();
			
		}
		
		try {
			f = DBConnectionChecker.downloadJDB(savepath,uuid,applicationIdentifier,host, port, dbName,dbPrefix, user, password,version);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//d = new Date();
		
        //bean.setPrice(quantity * 10);
        return f;
    }
	
	@SMDMethod
    public DBConnectionError downloadJoomlaDatabaseTable(
    		String uuid,
    		String applicationIdentifier,
    		String host, 
    		String port, 
    		String dbName,
    		String dbPrefix, 
    		String user, 
    		String password,
    		String version,
    		String table) 
	{
		DBConnectionError f = null;
		appManager  = getApplicationManager(null);
		application= appManager.getApplication(uuid,applicationIdentifier,"Debug");
		
		String savepath = appManager.getUserAppPath(uuid, applicationIdentifier);
		savepath+="/datasources/" + host + "/" + dbName + "/";
		File dbPath = new File(savepath);
		System.out.println("Start downloadling JTable");
		
		if(!dbPath.exists())
		{
			dbPath.mkdirs();
			
		}
		f = DBConnectionChecker.downloadJDBTable(savepath, uuid, applicationIdentifier, host, port, dbName, dbPrefix, user, password, version, table);
		f.identifier=table;
		
        return f;
    }
	
	public void createDataSourceCContentJoomla(
			String uuid,
    		String applicationIdentifier,
    		String dataSourceUID)
	{

		appManager  = getApplicationManager(null);
		application= appManager.getApplication(uuid,applicationIdentifier,"Debug");
		if(application==null){
			return;
			
			
		}
		
		
		
		DataSourceBase ds = application.getDataSource(dataSourceUID);
		if(ds==null)
		{
			return;
		}
		
		DataSourceCache dsc = ServerCache.getDSC(appManager, application, ds);
		if(dsc==null){
			return;
		}
		
		String prefix = "";
		if(ds.getType().equals("JoomlaMySQL")){
			prefix="joomla";
			SQLDataSource sqlds = (SQLDataSource)ds;
			prefix=sqlds.getDatabase();
		}else
		{
			XMLDataSource sqlds = (XMLDataSource)ds;
			prefix="joomla";
		}
		
		String prefixHost ="";
		if(ds.getType().equals("JoomlaXML")){
			try {
				URI uri = new URI(ds.getHost());
				prefixHost = uri.getHost();
			} catch (URISyntaxException e) 
			{
				e.printStackTrace();
			}
		}else
		{
			prefixHost=ds.getHost();
		}
		
		String savepath = appManager.getUserAppPath(uuid, applicationIdentifier);
		savepath+="/datasources/" + prefixHost + "/" + prefix + "/";
		File dbPath = new File(savepath);
		if(!dbPath.exists())
		{
			dbPath.mkdirs();
		}
		
		/***
		 *  	dst.addDownloadTask("articles");
                dst.addDownloadTask("categories");
                dst.addDownloadTask("sections");
                dst.addDownloadTask("jeventsNormal");
                dst.addDownloadTask("jeventCategories");
                dst.addDownloadTask("jeventslocations");
                dst.addDownloadTask("jeventLocationCategories");
                dst.addDownloadTask("mosetCategories");
                dst.addDownloadTask("mosetLinks");
                dst.addDownloadTask("banners");
                dst.addDownloadTask("breezingforms");
                dst.addDownloadTask("vmCategories");
                dst.addDownloadTask("vmProducts");
		 */
		dsc.updateCContent(ECMContentSourceType.JoomlaArticle, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		dsc.updateCContent(ECMContentSourceType.JoomlaSection, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		dsc.updateCContent(ECMContentSourceType.JoomlaCategory, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		dsc.updateCContent(ECMContentSourceType.JEventsLocationCategory, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		dsc.updateCContent(ECMContentSourceType.JEventsLocationItem, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		dsc.updateCContent(ECMContentSourceType.JEventsCalendarToday, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		dsc.updateCContent(ECMContentSourceType.VMartCategory, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		dsc.updateCContent(ECMContentSourceType.MosetTreeCategory, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		dsc.updateCContent(ECMContentSourceType.MosetTreeItem, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		//dsc.updateCContent(ECMContentSourceType.VMartCategory, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		//dsc.updateCContent(ECMContentSourceType.VMartProductItem, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		dsc.updateCContent(ECMContentSourceType.BreezingForm, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		
	}
	
	public void createDataSourceCContentWordpress(
			String uuid,
    		String applicationIdentifier,
    		String dataSourceUID)
	{

		appManager  = getApplicationManager(null);
		application= appManager.getApplication(uuid,applicationIdentifier,"Debug");
		if(application==null){
			return;
		}
		SQLDataSource ds = (SQLDataSource)application.getDataSource(dataSourceUID);
		if(ds==null)
		{
			return;
		}
		
		DataSourceCache dsc = ServerCache.getDSC(appManager, application, ds);
		if(dsc==null){
			return;
		}
		
		String savepath = appManager.getUserAppPath(uuid, applicationIdentifier);
		savepath+="/datasources/" + ds.getHost() + "/" + ds.getDatabase() + "/";
		File dbPath = new File(savepath);
		if(!dbPath.exists())
		{
			dbPath.mkdirs();
		}
		dsc.updateCContent(ECMContentSourceType.WordpressCategory, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		dsc.updateCContent(ECMContentSourceType.WordpressPost, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
	}
	
	public void flushServerCache(
			String uuid,
    		String applicationIdentifier,
    		String dataSourceUID)
	{
		appManager  = getApplicationManager(null);

		application= appManager.getApplication(uuid,applicationIdentifier,"Debug");
		if(application==null)
		{
			return;
		}
		SQLDataSource ds = (SQLDataSource)application.getDataSource(dataSourceUID);
		if(ds==null){
			return;
		}
		
		DataSourceCache dsc=ServerCache.getDSC(appManager, application, ds);
		if(dsc!=null)
		{
			dsc.joomlaBannerCategories=null;
			dsc.jArticles=null;
			dsc.articleCategories=null;
			dsc.articles=null;
			dsc.jBreezingForms=null;
			dsc.articleSections=null;
			dsc.locationCategories=null;
			dsc.eventsFinal=null;
			dsc.locations=null;
			
			
		}
		
	}
	@SMDMethod
    public DBConnectionError downloadJoomlaDatabaseTableWithDataSourceUID(
    		String uuid,
    		String applicationIdentifier,
    		String table,
    		String dataSourceUID) 
	{
		DBConnectionError f = null;
		appManager  = getApplicationManager(null);
		application= appManager.getApplication(uuid,applicationIdentifier,"Debug");
		
		
		SQLDataSource ds = (SQLDataSource)application.getDataSource(dataSourceUID);
		
		String savepath = appManager.getUserAppPath(uuid, applicationIdentifier);
		savepath+="/datasources/" + ds.getHost() + "/" + ds.getDatabase() + "/";
		File dbPath = new File(savepath);
		if(!dbPath.exists())
		{
			dbPath.mkdirs();
			
		}
	
		String key = "asd28071977";
        Crypto encrypter = new Crypto(key);
		String userD = encrypter.decrypt(ds.getUser());
		String passD = encrypter.decrypt(ds.getPassword());
		f = DBConnectionChecker.downloadJDBTable(savepath, uuid, applicationIdentifier, ds.getHost(), "3306", ds.getDatabase(), ds.getPrefix(), userD, passD, ds.getVersion(), table);
		
		// store in new CContent Format
		ECMContentSourceType cType = ECMContentSourceTypeTools.FromTableString(table);

		f.identifier=table;
		DataSourceCache dsc = ServerCache.getDSC(appManager, application, ds);
		if( dsc !=null && f.type==0 && cType!=ECMContentSourceType.Unknown )
		{
			if(table.equals("jeventsNormal")){
				System.out.println("jeventsNormal");
			}
			//flush
			dsc.setByType(cType, null);
			//create ccontent files
			dsc.updateCContent(cType, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		}
		
        return f;
    }
	
	@SMDMethod
    public DBConnectionError downloadGDataWithDataSourceUID(
    		String uuid,
    		String applicationIdentifier,
    		String service,
    		String dataSourceUID
    		) 
	{
		DBConnectionError f = null;
		appManager  = getApplicationManager(null);
		application= appManager.getApplication(uuid,applicationIdentifier,"Debug");
		
		XMLDataSource ds = (XMLDataSource)application.getDataSource(dataSourceUID);
		
		String key = "asd28071977";
        Crypto encrypter = new Crypto(key);
		String userD = encrypter.decrypt(ds.getUser());
		String passD = encrypter.decrypt(ds.getPassword());
		
		String savepath = appManager.getUserAppPath(uuid, applicationIdentifier);
		savepath+="/datasources/google/"+service+"/" + ds.getUrl() + "/";
		File dbPath = new File(savepath);
		if(!dbPath.exists())
		{
			dbPath.mkdirs();
		}
		if(service.equals("Youtube"))
		{
			f = GDataDownloader.downloadGDataYoutube(savepath, uuid, applicationIdentifier, service, ds.getUrl(), userD, passD);
		}
		
		if(service.equals("Picassa")){
			f = GDataDownloader.downloadGDataPicassa(savepath, uuid, applicationIdentifier, service, ds.getUrl(), userD, passD);
		}
		if(service.equals("Calendar"))
		{
			f = GDataDownloader.downloadGDataCalendar(savepath, uuid, applicationIdentifier, service, ds.getUrl(), userD, passD,ds.getUid());
		}
		if(service.equals("Documents")){
			
			f = GDataDownloader.downloadGDataDocumentFolder(savepath, uuid, applicationIdentifier, service, ds.getUrl(), userD, passD,ds.getUid());
		}
		f.identifier="items";
		// store in new CContent Format
		ECMContentSourceType cType = ECMContentSourceTypeTools.FromTableString(service);

		DataSourceCache dsc = ServerCache.getDSC(appManager, application, ds);
		if( dsc !=null )
		{
			if(service!=null)
			{
				if(service.equals("Calendar")){
					dsc.setByType(ECMContentSourceType.GoogleCalendar, null);
					dsc.setByType(ECMContentSourceType.GoogleCalendarItem, null);
				}
				if(service.equals("Documents"))
				{
					dsc.setByType(ECMContentSourceType.GoogleDocumentFolder, null);
					dsc.setByType(ECMContentSourceType.GoogleDocumentItem, null);
				}
				
				if(service.equals("Picassa"))
				{
					dsc.setByType(ECMContentSourceType.GooglePicassaAlbum, null);
					dsc.setByType(ECMContentSourceType.GooglePicassaItem, null);
				}
			}
		}
		if( dsc !=null && f.type==0 && cType!=ECMContentSourceType.Unknown )
		{
			//flush
			dsc.setByType(cType, null);
			//dsc.updateCCList(cType, savepath, "json", uuid, applicationIdentifier, ds.getUid(), lang, pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE);
		}
        return f;
    }
	@SMDMethod
    public DBConnectionError downloadGData(
    		String uuid,
    		String applicationIdentifier,
    		String service, 
    		String channel,
    		String user,
    		String password,
    		String dsUID) 
	{
		DBConnectionError f = null;
		appManager  = getApplicationManager(null);
		application= appManager.getApplication(uuid,applicationIdentifier,"Debug");
		
		String savepath = appManager.getUserAppPath(uuid, applicationIdentifier);
		savepath+="/datasources/google/"+service+"/" + channel + "/";
		File dbPath = new File(savepath);
		if(!dbPath.exists())
		{
			dbPath.mkdirs();
		}
		if(service.equals("Youtube"))
		{
			f = GDataDownloader.downloadGDataYoutube(savepath, uuid, applicationIdentifier, service, channel, user, password);
		}
		
		if(service.equals("Picassa")){
			f = GDataDownloader.downloadGDataPicassa(savepath, uuid, applicationIdentifier, service, channel, user, password);
		}
		if(service.equals("Calendar")){
			f = GDataDownloader.downloadGDataCalendar(savepath, uuid, applicationIdentifier, service, channel, user, password,dsUID);
		}
		if(service.equals("Documents"))
		{
			f = GDataDownloader.downloadGDataDocumentFolder(savepath, uuid, applicationIdentifier, service, channel, user, password,dsUID);
		}
		f.identifier="items";
        return f;
    }
	
	public String smd() {
        return Action.SUCCESS;
    }
	public String execute2()
   {
	   
		Date d = new Date();
		System.out.println("db connection test : start " + d.getSeconds() );
		e = DBConnectionChecker.checkConnection(host, "3306", dbName, user, password);
		d = new Date();
		System.out.println("db connection test : " + e.msg + "\n internal reason: " + e.msgInternal + d.getSeconds());

	   return "success";
	
	  
   }


	/**
	 * @return the cmdType
	 */
	public String getCmdType() {
		return cmdType;
	}


	/**
	 * @param cmdType the cmdType to set
	 */
	public void setCmdType(String cmdType) {
		this.cmdType = cmdType;
	}


	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}


	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}


	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}


	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}


	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}


	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}


	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}


	/**
	 * @return the e
	 */
	public DBConnectionError getE() {
		return e;
	}


	/**
	 * @param e the e to set
	 */
	public void setE(DBConnectionError e) {
		this.e = e;
	}


	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}


	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	
	

}
