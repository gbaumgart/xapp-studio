package cmx.types;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import jsontype.ComposerPackages;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.xml.sax.SAXException;

import com.liferay.portal.kernel.management.jmx.GetAttributesAction;
import com.liferay.portal.kernel.util.DocumentConversionUtil;

import pmedia.DataUtils.ApplicationConfigurationTools;
import pmedia.types.ApplicationConfiguration;
import pmedia.types.ApplicationMetaData;
import pmedia.types.ApplicationMetaDataKeys;
import pmedia.utils.CITools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import cmq.manager.ApplicationTemplateManager;
import cmq.types.ApplicationTemplate;
import cmx.action.StyleManagerAction;
import cmx.tools.CIFactory;
import cmx.tools.Crypto;
import cmx.tools.DBConnectionChecker;
import cmx.tools.LiferayContentTools;
import cmx.tools.LiferayDataSourceUtil;
import cmx.tools.ResourceUtil;
import cmx.tools.StyleTreeFactory;
import cmx.tools.TrackingUtils;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ApplicationManager {
	
	public void updateRemoteResources(Application jspapp){
		if(jspapp!=null){
			
			ArrayList<DataSourceBase>datasources = jspapp.getDataSources();
			ArrayList<XCDatasourceInfo>xcDataSourceInfos = new ArrayList<XCDatasourceInfo>();
			
			for(int j = 0  ; j  <  datasources.size() ; j++)	
			{
				DataSourceBase ds = datasources.get(j);
				Boolean ok = ds.getType().equals("JoomlaMySQL") || ds.getType().equals("XAppConnect") || ds.getType().equals("JoomlaXML") || ds.getType().equals("WordpressXML");
				if(!ok){
					continue;
				}
				
				if(ds.getType().equals("XAppConnect")){
					XCDataSource xds = (XCDataSource)ds;
					String siteUrl = ds.getUrl();
					String rpcUrl = xds.getRPCUrl();
					
					if(siteUrl!=null && siteUrl.length()>0){
						
						if(rpcUrl!=null && rpcUrl.length()>0){
							
							/***
							 * First is to determine that the host has JSONP support
							 */
							Boolean hasJSONP =true;// hasJSONP(xds);
							XCDatasourceInfo info = null;
							if(hasJSONP){
								info = new XCDatasourceInfo();
								info.url=siteUrl;
								info.hasJSONP=true;
								
							}
							if(info!=null){
								/***
								 * Second is to get the host's registered Composer resources
								 */
								ComposerPackages cPackages=null;// getComposerPackages(xds);
								if(cPackages!=null){
									info.composerPackages=cPackages;
								}
								xcDataSourceInfos.add(info);
							}
						}
					}	
				}
			}
			if(xcDataSourceInfos!=null)
			{
				jspapp.setXcDataSourceInfos(xcDataSourceInfos);
			}
		}
	}
	public static Boolean hasJSONP(XCDataSource ds)
	{
		URL serverURL = null;
		String siteUrl = ds.getUrl();
		String rpcUrl = ds.getRPCUrl();

		try {
			serverURL = new URL(siteUrl + "/" + rpcUrl);
		} catch (MalformedURLException e) {
		}
		
		GetMethod get = new GetMethod(siteUrl + "/" + rpcUrl);

		HttpClient client = new HttpClient();
		int status =-1;
		try {
			 status = client.executeMethod(get);
		} catch (HttpException e) {
			System.out.println("error getting jsonp data : " + siteUrl);
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error getting jsonp data : " + siteUrl);
			//e.printStackTrace();
		}
		String response = null;
		try {
			response = get.getResponseBodyAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error getting jsonp data : " + siteUrl);
			//e.printStackTrace();
		}
		if(response!=null){
			
			if(response.indexOf("customTypeQuery")!=-1){
				return true;
			}
		}
		//System.out.println(response);
		return false;
	}
	
	public static ComposerPackages getComposerPackages(XCDataSource ds)
	{
		ComposerPackages packages=null;
		URL serverURL = null;
		String siteUrl = ds.getUrl();
		String rpcUrl = ds.getRPCUrl();

		try {
			serverURL = new URL(siteUrl + "/" + rpcUrl);
		} catch (MalformedURLException e) {
		}
		
		String url = siteUrl + "/" + rpcUrl;
		if(url.indexOf("index.php?")!=-1){
			url+="&service=xapp_get_plugin_infos";	
		}else{
			url+="?service=xapp_get_plugin_infos";
		}
		
		//System.out.println("asking for composer packages : " + url);
		GetMethod get = new GetMethod(url);
		
		HttpClient client = new HttpClient();
		int status =-1;
		try {
			 status = client.executeMethod(get);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String response = null;
		try {
			response = get.getResponseBodyAsString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(response!=null){
			
			if(response.indexOf("items")!=-1){
				
				response=response.replace("%SITEURL%",siteUrl+"/");
				JSONDeserializer derializerSC = new JSONDeserializer<ComposerPackages>();
				try {
	    			packages= (ComposerPackages)derializerSC.deserialize(response);
				} catch (Exception e) {
					/*System.out.println(e.getMessage());*/
					/*e.printStackTrace();*/
					return null;
				}
			}
			
		}
		
		return packages;
	}
	
	public void getRemoteResources(Application app)
	{
		if(app ==null)
		{
			return;
		}
	}
	public void registerPluginParameters(Application app,Resources pResources)
	{
		if(app ==null || app.getMetaData()==null)
		{
			
			return;
		}
		
		ApplicationMetaData appMeta = app.getMetaData();
		
		for(int i = 0 ; i<pResources.getItems().size(); i++ )
		{
			Resource res =pResources.getItems().get(i);
			if(res.enabled && res.type!=null && res.type.equals("PLUGIN"))
			{
				if(res.getParameters()!=null && res.getParameters().size() > 0)
				{

					for(int j = 0 ; j < res.getParameters().size() ; j++)
					{
						ConfigurableInformation param = res.getParameters().get(j);
						ConfigurableInformation paramMeta = CITools.getById(appMeta.getProperties(),param.id );
						if(paramMeta==null)
						{
							paramMeta = CIFactory.AppMetaDataCI( CITools.CIFromInteger(param.type) , param.id, null, 
									param.title);
							paramMeta.setParentId(app.getApplicationIdentifier());
							appMeta.getProperties().add(paramMeta);
							
						}
					}
				}
			}
		}
		saveApplication(app);
	}
	public static String findAppIcon(String uuid,String appId,String platform)
	{
		String result = null;
		
		ApplicationManager appManager = getInstance();
		Application app = appManager.getApplication(uuid, appId, false);
		if(app==null){
			return result;
		}
		
		ApplicationMetaData appMeta = app.getMetaData();
		if(appMeta==null){
			return result;
		}
		
		
		String icon = null;
		
		ConfigurableInformation iconMeta = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_ICON_0);
		if(iconMeta!=null){
			if(iconMeta.getValue()!=null && iconMeta.getValue().length()>0 ){
				icon=iconMeta.getValue();
			}
		}
		if(icon==null){
			iconMeta = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_1);
			if(iconMeta.getValue()!=null && iconMeta.getValue().length()>0 )
			{
				icon=iconMeta.getValue();
			}
		}
		
		if(icon==null){
			iconMeta = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_0);
			if(iconMeta.getValue()!=null && iconMeta.getValue().length()>0 )
			{
				icon=iconMeta.getValue();
				
			}
		}

		result = appManager.toWebPath(uuid, appId, icon);
		return result;
	}
	
	ArrayList<Application> applications=new ArrayList<Application>();
	
	public ArrayList<Application> getApplications() {
		return applications;
	}

	public void setApplications(ArrayList<Application> applications) {
		this.applications = applications;
	}
	private static ApplicationManager sInstance = new ApplicationManager();
	
	public static ApplicationManager getInstance(){
		//ServerCache sc = ServerCache.getInstance();
		return ApplicationManager.sInstance;
	};
	
	public Application getById(String uuid,String appId){
		
		for (int i  = 0  ;  i < applications.size() ;  i ++){
			Application app = applications.get(i);
			if(app.getUserIdentifier().equals(uuid) && app.getApplicationIdentifier().equals(appId))
			{
				String appPath=getUserAppPath(uuid, appId)+"/appInfo.json";
				File appFile = new File(appPath);
				if(!appFile.exists()){
					
					applications.remove(app);
					return null;
				}
				
				return app;
			}
		}
		
		return null;
	}
	
	
	public void createUser(String uuid,Boolean isLRUser)
	{
		if(hasUser(uuid)){
			return;
		}
		
		String userPath = getStoreRootPath() + "/" + uuid;
		File userPathFileObject = new File(userPath);
	
		//create the user base path
		if(!userPathFileObject.exists()){
			userPathFileObject.mkdirs();
		}
		String subpath = userPath+"/apps";
		File subPFile = new File(subpath);
		subPFile.mkdirs();
	}
	
	public Boolean containsApp(ArrayList<Application>apps,String identifier)
	{
		Boolean result = false;
	
		if(apps !=null){
			 for (int i=0; i<apps.size(); i++) 
			 {
				 Application app = apps.get(i);
				 if(app.applicationIdentifier.equals(identifier)){
					 return true;
				 }
			 }
		}
		 
		return result;
		 
	}
	public void copyDemoApp(String uuid,String appId){
		
		String userPath = getStoreRootPath() + "/" + uuid + "/apps/" + appId;
		File userPathFileObject = new File(userPath);
		
		
		String adminPath = getStoreRootPath() + "/" + System.getProperty("adminUser") + "/apps/" + appId;
		File adminPathFileObject = new File(adminPath);
		
		if(userPathFileObject.exists()){
			return;
		}
		
		if(!adminPathFileObject.exists()){
			return;
		}
		
		try {
			FileUtils.copyDirectory(adminPathFileObject, userPathFileObject, new FileFilter() {
				
				  public boolean accept(File pathname) {
			       if(pathname.getAbsolutePath().contains("svn"))
			       {
			    	   return false;
			       }
			        return true;
			    }
			}, true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
	}
	public void addDemoApps(String uuid){
		if(uuid!=null && !uuid.equals(System.getProperty("demoUser")))
		{
			copyDemoApp(uuid, System.getProperty("demoAppDark"));
			copyDemoApp(uuid, System.getProperty("demoAppWhite"));
		}
	}
	public ArrayList<Application>getUserApplications(String uuid)
	{
		
		if(!hasUser(uuid))
			return null;
		
		ArrayList<Application>apps = new ArrayList<Application>();
		String userPath = getStoreRootPath() + "/" + uuid + "/apps/";
		File userPathFileObject = new File(userPath);
		
		if(uuid!=null && !uuid.equals(System.getProperty("demoUser")))
		{
			System.out.println("\t loading applications : from " + userPathFileObject + " for user : " + uuid);
		}
		
		addDemoApps(uuid);
		
		if(userPathFileObject.exists() && userPathFileObject.isDirectory())
		{
			 File[] files = userPathFileObject.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
				 	if(_file.isDirectory())
				 	{
				 		String absDir = _file.getAbsolutePath();
				 		String appFile = absDir + "/appInfo.json";
				 		File appFileObject = new File(appFile);
				 		if(appFileObject.exists())
				 		{
				 			Application app = loadApplication(appFile);
				 			if(app!=null)
				 			{
				 				app.setUserIdentifier(uuid);
				 				String newAppID = absDir.substring(absDir.lastIndexOf("/")+1,absDir.length());
				 				if(!newAppID.equals(app.applicationIdentifier)){
				 					System.out.println("updating to new app id " + newAppID + " old appId : " + app.getApplicationIdentifier());
				 					app.setApplicationIdentifier(newAppID);
				 				}
				 				app.setTitle(app.getTitle());				 				
				 				app.setIconUrl(app.getIconUrl());
				 				if(!containsApp(apps, app.applicationIdentifier))
				 				{
				 					apps.add(app);
				 				}
				 			}
				 		}
				 	}
			 }
		}else
		{
			if(uuid!=null && !uuid.equals(System.getProperty("demoUser")))
			{
				System.out.println("\t loading applications : failed for path " + userPathFileObject + " and user : " + uuid);
			}
		}
		
		ArrayList<Application>appsOut = new ArrayList<Application>();
		for (int i=0; i<apps.size(); i++) 
		{			
			Application iApp = apps.get(i);
			Application sApp = new Application();
			sApp.setApplicationIdentifier(iApp.getApplicationIdentifier());
			sApp.setUserIdentifier(iApp.getUserIdentifier());
			sApp.setMetaData(iApp.getMetaData());
			appsOut.add(sApp);
		}
		
		return appsOut;
		
	}
	public ArrayList<Application>getUserApplicationsFull(String uuid)
	{
		
		if(!hasUser(uuid))
			return null;
		
		ArrayList<Application>apps = new ArrayList<Application>();
		String userPath = getStoreRootPath() + "/" + uuid + "/apps/";
		File userPathFileObject = new File(userPath);
		
		if(uuid!=null && !uuid.equals(System.getProperty("demoUser")))
		{
			System.out.println("\t loading applications : from " + userPathFileObject + " for user : " + uuid);
		}
		
		addDemoApps(uuid);
		
		if(userPathFileObject.exists() && userPathFileObject.isDirectory())
		{
			 File[] files = userPathFileObject.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
				 	if(_file.isDirectory())
				 	{
				 		String absDir = _file.getAbsolutePath();
				 		String appFile = absDir + "/appInfo.json";
				 		File appFileObject = new File(appFile);
				 		if(appFileObject.exists())
				 		{
				 			Application app = loadApplication(appFile);
				 			if(app!=null)
				 			{
				 				app.setUserIdentifier(uuid);
				 				String newAppID = absDir.substring(absDir.lastIndexOf("/")+1,absDir.length());
				 				if(!newAppID.equals(app.applicationIdentifier)){
				 					System.out.println("updating to new app id " + newAppID + " old appId : " + app.getApplicationIdentifier());
				 					app.setApplicationIdentifier(newAppID);
				 				}
				 				app.setTitle(app.getTitle());				 				
				 				app.setIconUrl(app.getIconUrl());
				 				if(!containsApp(apps, app.applicationIdentifier))
				 				{
				 					apps.add(app);
				 				}
				 			}
				 		}
				 	}
			 }
		}else
		{
			if(uuid!=null && !uuid.equals(System.getProperty("demoUser")))
			{
				System.out.println("\t loading applications : failed for path " + userPathFileObject + " and user : " + uuid);
			}
		}
		
		/*
		ArrayList<Application>appsOut = new ArrayList<Application>();
		for (int i=0; i<apps.size(); i++) 
		{			
			Application iApp = apps.get(i);
			Application sApp = new Application();
			sApp.setApplicationIdentifier(iApp.getApplicationIdentifier());
			sApp.setUserIdentifier(iApp.getUserIdentifier());
			sApp.setMetaData(iApp.getMetaData());
			appsOut.add(sApp);
		}*/
		
		return apps;
		
	}
	public Boolean hasUser(String uuid)
	{
		Boolean result=false;
		String userPath = getStoreRootPath() + "/" + uuid;
		File userPathFileObject = new File(userPath);
		if(userPathFileObject.exists())
		{
			return true;
		}
		
		return result;
		
	}
	public ApplicationConfiguration loadAppSettings(Application app){
		
		if(app.getAppSettings()!=null)
		{
			return app.getAppSettings();
		}
		
		ApplicationConfiguration config=null;
		String appSettingsPath = null;
		appSettingsPath = getUserAppPath(app.getUserIdentifier(),app.getApplicationIdentifier()) + "appSettings.xml";
		if(appSettingsPath!=null && appSettingsPath.length() > 0)
		{
			ArrayList<ApplicationConfiguration>configs = null;
			try {
				 try {
					 File appSettingsFile = new File(appSettingsPath);
					 if(!appSettingsFile.exists())
					 {
						 return null;
					 }
					configs = ApplicationConfigurationTools.readAppConfigFromFile(appSettingsPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				//app.setAppSettings(config);
			}
			
		}
		return config;
	}

	public void purgeApplication(Application app,String servletContextPath)
	{
		ArrayList<Page> rootPages= app.getPageByType("view");
		ArrayList<ConfigurableInformation>dsCIs=CITools.getAllDataSourceCI(rootPages);
		int maxp = dsCIs.size();
		ArrayList<DataSourceBase>dataSources=app.getDataSources();
		

		/*
		for(int i = 0  ; i  <  maxp ; i++)	
		{
			ConfigurableInformation  dsci = dsCIs.get(i);
			if(dsci.dataSource!=null)
			{
				DataSourceBase sqlDataSource = app.getDataSource(dsci.dataSource);
				if(sqlDataSource!=null)
				{
					if(!dataSources.contains(sqlDataSource)){
						dataSources.add(sqlDataSource);
					}
				}
			}
		}
		*/
		
		maxp = dataSources.size();
		if(dataSources.size() > 0)
		{
			
			for(int j = 0  ; j  <  maxp ; j++)	
			{
				String savepath = getUserAppPath(app.getUserIdentifier(), app.getApplicationIdentifier());
				
				DataSourceBase ds = dataSources.get(j);
				
				if(ds.getType().equals("Liferay"))
				{
					savepath+="/datasources/liferay/";
					LiferayDataSourceUtil.dumpArticles(app.getUserIdentifier(), app.getApplicationIdentifier(),pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE , savepath, "Articles");
					LiferayDataSourceUtil.dumpArticleCategories(app.getUserIdentifier(), app.getApplicationIdentifier(),pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE, savepath, "Articles");
					
					LiferayDataSourceUtil.dumpArticles(app.getUserIdentifier(), app.getApplicationIdentifier(),pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE , savepath, "Locations");
					LiferayDataSourceUtil.dumpArticleCategories(app.getUserIdentifier(), app.getApplicationIdentifier(),pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE, savepath, "Locations");
					//continue;
				}
				
				
				//savepath+="/datasources/" + ds.getHost()+ "/" + ds.getDatabase() + "/";
				
				/*
				File dbPath = new File(savepath);
				if(!dbPath.exists())
				{
					dbPath.mkdirs();
				}
				
				String key = "asd28071977";
	            Crypto encrypter = new Crypto(key);
				
				String passD = encrypter.decrypt(ds.getPassword());
				String userD = encrypter.decrypt(ds.getUser());
				
				DBConnectionError f = null;
				try {
					//
					f = DBConnectionChecker.downloadJDB(savepath, app.getUserIdentifier() ,app.getApplicationIdentifier(),ds.getHost(), "3306", ds.getDatabase(),ds.getPrefix(), userD, passD,ds.getVersion());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("db download failed : " + e.getLocalizedMessage());
				}
				*/
				
			}
			
		}
		
		
	}
	
	public void createNativeClientContent(Application app,String servletContextPath)
	{
		
	}
	
	public void mergeSharedAssets(Application app,String dstFolder)
	{
		
		String appPath = getUserAppPath(app.getUserIdentifier(), app.getApplicationIdentifier());
		String assetPath=System.getProperty("AppStoreRoot") + "shared";
		//System.out.println("loading app " + appPath);
		String data = null;
		try 
		{
			data = StringUtils.readFileAsString(appPath+"/appInfo.json");
		} catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
		
		if(data==null){
			return;
		}
		
		
		String cssData2 = StyleTreeFactory.getMergedCSS(app.getUserIdentifier(), app.getApplicationIdentifier(), app.getPlatform());
		
		//now walk over files :
		
		//FileUtils.iterateFiles(directory, extensions, recursive)
		Iterator iter =  null;
		
		String dstPath = getUserBasePathPublicLocal(app.getUserIdentifier()) + "/Archive/" + app.getApplicationIdentifier() +"/";
		//http://mc007ibi.dyndns.org:8080/XApp-portlet/client?action=getArchive&uuid=11166763-e89c-44ba-aba7-4e9f4fdf97a9&appIdentifier=gaaa51a&version=3&platform=IPHONE4_NATIVE
		
		StyleTree tree =StyleTreeFactory.createStyleTree(app.getUserIdentifier(), app.getApplicationIdentifier(), app.getPlatform(), null, null, null);
		
		iter = FileUtils.iterateFiles(new File(assetPath) ,TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		while(iter.hasNext()) {
		    File file = (File) iter.next();
		    String relativePath = file.getAbsolutePath().replace(assetPath, "");
		    if(file.isDirectory())
		    {
		    	continue;
		    }
		    
		    //appInfo info file 
		    if(data.contains(relativePath))
		    {
		    	System.out.println("ll :  " + relativePath);
		    	//StringUtils.path(relativePath)
		    	
		    	String newPath = "/" +app.getApplicationIdentifier()  + "/" + relativePath;
		    	data = data.replace(relativePath, newPath);
		    	
		    	File dstPathO = new File(  getUserBasePathPublicLocal(app.getUserIdentifier()) + "/Archive/" + app.getApplicationIdentifier()+ "/" + StringUtils.path(relativePath));
		    	if(!dstPathO.exists()){
		    		dstPathO.mkdirs();
		    		
		    	}
		    	
		    	File dstFileO = new File( getUserBasePathPublicLocal(app.getUserIdentifier()) + "/Archive/" + app.getApplicationIdentifier() +"/"+ relativePath);
		    	try {
					FileUtils.copyFile(file, dstFileO);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    
		    if(tree!=null)
		    {
				for (int i = 0 ; i<tree.getItems().size() ; i++ )
				{
					StyleTreeItem item = (StyleTreeItem) tree.getItems().get(i);
					if(item.getContent() !=null)
					{
						if(item.getContent() !=null && item.getContent().contains(relativePath))
					    {
							String newPath = "/" +app.getApplicationIdentifier()  + relativePath;
					    	String cssDataNew = item.getContent().replace(relativePath, newPath);
							item.setContent(cssDataNew);
					    }
					}
				}
		    }
		    
		    
		    if(cssData2 !=null && cssData2.contains(relativePath))
		    {
		    	//System.out.println("ll :  " + relativePath);
		    	//StringUtils.path(relativePath)
		    	//String newPath = "/" +app.getApplicationIdentifier()  + "/" + relativePath;
		    	//cssData2 = cssData2.replace(relativePath, newPath);
		    	File dstPathO = new File(  getUserBasePathPublicLocal(app.getUserIdentifier()) + "/Archive/" + app.getApplicationIdentifier()+ "/" + StringUtils.path(relativePath));
		    	if(!dstPathO.exists()){
		    		dstPathO.mkdirs();
		    	}
		    	
		    	File dstFileO = new File( getUserBasePathPublicLocal(app.getUserIdentifier()) + "/Archive/" + app.getApplicationIdentifier() +"/"+ relativePath);
		    	try {
					FileUtils.copyFile(file, dstFileO);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    
		}
		
		//save modified app :
		try {
			StringUtils.writeToFile(data, dstPath+"appInfo.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// the modified css 

		if(tree!=null)
	    {
			for (int i = 0 ; i<tree.getItems().size() ; i++ )
			{
				StyleTreeItem item = (StyleTreeItem) tree.getItems().get(i);
				if(item.getContent() !=null)
				{
					String cssFilePath = dstPath +  item.getPath(); 
					try {
						StringUtils.writeToFile(item.getContent(), cssFilePath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	    }
		
	}
	public String prepareApplicationForCompressing(Application app)
	{
		
		/**
		 * Prepare destination folder : 
		 */
		String dstPath = getUserBasePathPublicLocal(app.getUserIdentifier()) + "/Archive/" + app.getApplicationIdentifier();
		File dstPathO = new File(dstPath);
		if(dstPathO.exists())
		{
			try {
				FileUtils.deleteDirectory(dstPathO);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dstPathO.mkdirs();
		
		String appPath = getUserAppPath(app.getUserIdentifier(), app.getApplicationIdentifier());

		try {
			FileUtils.copyDirectory(new File(appPath), dstPathO);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//copy shared assets to destination folder 
		mergeSharedAssets(app, dstPath);
		
		return dstPath;
	}
	public String compressApplication(Application app,String servletContextPath)
	{
		
		String result = null;
		String appPath = getUserAppPath(app.getUserIdentifier(), app.getApplicationIdentifier());///Applications/MAMP/htdocs/CMAC/11166763-e89c-44ba-aba7-4e9f4fdf97a9/apps/gaaa51a/
		
		//purge items : 
		try{
			purgeApplication(app, null);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		String mergedAppPath = prepareApplicationForCompressing(app);
		
		String dstPath = getUserBasePathPublicLocal(app.getUserIdentifier()) + "Archive";///liferay/tomcat-7.0.27/webapps/XApp-portlet/11166763-e89c-44ba-aba7-4e9f4fdf97a9/Archive
		String dstFile = dstPath + app.getApplicationIdentifier()+  ".zip";///liferay/tomcat-7.0.27/webapps/XApp-portlet/11166763-e89c-44ba-aba7-4e9f4fdf97a9/Archivegaaa51a.zip
		result = "Archive" + app.getApplicationIdentifier()+  ".zip";
		System.out.println("compressing :" + mergedAppPath + " to : " + dstFile);
		
		String ctScope = null; 
		if(ctScope==null)
		{	
			ctScope = "%XASWEB%";
		}
		String prefix = "/ctypes/";
		//public static ArrayList<CustomType>getTypes(String rtConfig,String platform,String uuid,String appId,String systemAppName,String storeRoot)
		//ArrayList<CustomType> pTypes = CustomTypeUtils.getTypes(prtConfig, "IPHONE_NATIVE", puuid,pappId, psystemAppName, ctScope + prefix);
		
		String cTypeRoot= ResourceUtil.resolveConstantsAbsolute(ctScope + prefix, null, null);
		String rtConfig = "debug";
		//cTypeRoot=cTypeRoot;// + "/" + "IPHONE_NATIVE" + "/" + rtConfig+"/";
		
		File dstfileO = new File(dstFile);
		if(dstfileO.exists())
		{
			dstfileO.delete();
		}
		
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			
			ZipFile zipFile = new ZipFile(dstFile);
			zipFile.setRunInThread(false);
			
			// Folder to add
			String folderToAdd = mergedAppPath;
			
			// Initiate Zip Parameters which define various properties such
			// as compression method, etc.
			ZipParameters parameters = new ZipParameters();
			parameters.setIncludeRootFolder(false);
			
			// set compression method to store compression
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			
			// Set the compression level
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
			parameters.addExlude("appInfo.json");
			
			// Add folder to the zip file
			zipFile.addFolder(folderToAdd, parameters);
			
			ZipParameters parametersCType = new ZipParameters();
			parameters.setIncludeRootFolder(true);
			// Add ctype folder to the zip file
			zipFile.addFolder(cTypeRoot, parametersCType);
			
			
			//zipFile.removeFile(appPath+"/" + "appInfo.json");
			
			//zipFile.getFile().
			
			
		} catch (ZipException e) {
			e.printStackTrace();
			System.out.println("zip exception : " + e.getMessage());
		}

		File dstArchive =  new File(System.getProperty("AppArchiveRoot") + result);
		
		try {
			FileUtils.copyFile(dstfileO, dstArchive);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String getStoreRootPath()
	{
		return System.getProperty("AppStoreRoot");
	}
	
	public String getAppAssetsPrefixComponent(Application app)
	{
		return "/" + app.getUserIdentifier()  + "/apps/" + app.getApplicationIdentifier() + "/Assets/";
	}
	public String getDataPath(Application app,ECMContentSourceType type,DataSourceBase dataSource)
	{
		String dbPath = getUserAppPath(app.getUserIdentifier(),app.getApplicationIdentifier());
		String prefix="";
		if(dataSource.getType().equals("JoomlaMySQL")){
			prefix=dataSource.getDatabase();
		}
		if(dataSource.getType().equals("JoomlaXML")){
			prefix="joomla";
		}
		String host= dataSource.getHost();
		 try {
			if(dataSource !=null && dataSource.getHost()!=null)
			{ 
				URI uri = new URI(dataSource.getHost());
				String _host = uri.getHost();
				if(_host!=null){
					host = _host;
				}
				
			}
		} catch (URISyntaxException e) 
		{
			e.printStackTrace();
		}
		switch (type) 
		{
			
			case VMartProductItem:
			{
					dbPath+="datasources/" + host+"/" + prefix+ "/";
					dbPath+="vmProducts.xml";
					break;
			}
			case VMartCategory:
			{
					dbPath+="datasources/" + host+"/" + prefix+ "/";
					dbPath+="vmCategories.xml";
					break;
			}
			case JEventsCategory:
			{
					dbPath+="datasources/" + host+"/" + prefix + "/";
					if(dataSource.getVersion().equals("1.5") && dataSource.getType().equals("JoomlaMySQL") )
					{
						dbPath+="categories.xml";
					}else{
						dbPath+="jeventCategories.xml";
					}
					break;
			}
			case JEventsLocationItem:
			{
				
					dbPath+="datasources/" + host+"/" + prefix + "/";
					dbPath+="jeventslocations.xml";
					break;
			}
			case JEventsLocationCategory:
			{
				if(dataSource.getType().equals("JoomlaMySQL") )
				{
					dbPath+="datasources/" + host+"/" + prefix + "/";
					dbPath+="categories.xml";
				}else{
					dbPath+="datasources/" + host+"/" + prefix + "/";
					dbPath+="jeventLocationCategories.xml";
				}
				break;
			}
			case JEventsCalendarToday:
			{
					dbPath+="datasources/" + host+"/" + prefix + "/";
					dbPath+="jeventsNormal.xml";
					break;
			}
			case GoogleCalendar:
			{
				dbPath+="datasources/google/" + dataSource.getType()+"/" +dataSource.getUrl()+ "/c_"+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendar) + "_.json";
				break;
			}
			case GoogleCalendarItem:
			{
				dbPath+="datasources/google/" + dataSource.getType()+"/" + dataSource.getUrl()+ "/c_"+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendarItem) + "_.json";
				break;
			}
			
			
			case GoogleDocumentFolder:
			{
				dbPath+="datasources/google/" + dataSource.getType()+"/" +dataSource.getUrl()+ "/c_"+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentFolder) + "_.json";
				break;
			}
			case GoogleDocumentItem:
			{
				dbPath+="datasources/google/" + dataSource.getType()+"/" +dataSource.getUrl()+ "/c_"+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentItem) + "_.json";
				break;
			}
			
			case GooglePicassaItem:
			{
					dbPath+="datasources/google/" + dataSource.getType()+"/" + dataSource.getUrl() + "/";
					dbPath+="photos.xml";
					break;
			}
			case BreezingForm:
			{
					dbPath+="datasources/" + host+"/" + prefix+ "/";
					dbPath+="breezingforms.xml";
					break;
			}
			case GooglePicassaAlbum:
			{
					dbPath+="datasources/google/" + dataSource.getType() +"/" + dataSource.getUrl() + "/";
					dbPath+="albums.xml";
					break;
			}
			case JoomlaSection:
			{
					dbPath+="datasources/" + host+"/" + prefix + "/";
					dbPath+="sections.xml";
					break;
			}
			case JoomlaArticle:
			{
					dbPath+="datasources/" + host+"/" + prefix + "/";
					dbPath+="articles.xml";
					break;
			}
			case JoomlaCategory:
			{
					dbPath+="datasources/" + host+"/" + prefix + "/";
					dbPath+="categories.xml";
					break;
			}
			case MosetTreeCategory:
			{
				dbPath+="datasources/" + host+"/" + prefix + "/";
				dbPath+="mosetCategories.xml";
				break;
			}
			case MosetTreeItem:
			{
				dbPath+="datasources/" + host+"/" + prefix + "/";
				dbPath+="mosetLinks.xml";
				break;
			}
			case JoomlaBannerCategory:
			{
				dbPath+="datasources/" + host+"/" + prefix + "/";
				dbPath+="categories.xml";
				break;
			}
			
			case JoomlaBannerItem:
			{
				dbPath+="datasources/" + host+"/" + prefix + "/";
				dbPath+="banners.xml";
				break;
			}
			
			case WordpressCategory:
			{
				if(dataSource.getHost()!=null){
					
				}
				
				String dbName=dataSource.getDatabase();
				if(dbName==null){
					dbName="wordpress";
				}
				dbPath+="datasources/" +  StringUtils.getDomain(dataSource.getHost()) +"/" + dbName + "/";
				dbPath+="wpCategories.xml";
				break;
			}
			
			case WordpressPost:
			{
				String dbName=dataSource.getDatabase();
				if(dbName==null){
					dbName="wordpress";
				}
				dbPath+="datasources/" + StringUtils.getDomain(dataSource.getHost())+"/" + dbName + "/";
				dbPath+="wpPosts.xml";
				break;
			}
			
			case WordpressPage:
			{
				dbPath+="datasources/" + dataSource.getHost()+"/" + dataSource.getDatabase() + "/";
				dbPath+="wpPages.xml";
				break;
			}
		}
		return dbPath;
	}
	
	public String getUserRootPath(String uuid)
	{
		String path  = getStoreRootPath() + uuid + "/";
		File pathObject = new File(path);
		if(!pathObject.exists())
		{
			pathObject.mkdirs();
		}
		return path;
		
	}
	
	public String getAppDataPath(String uuid,String appIdentfier,DataSourceBase dataSource)
	{
		String path  = getUserRootPath(uuid)+"/apps/"+appIdentfier +"/";
		File pathObject = new File(path);
		if(!pathObject.exists())
		{
			pathObject.mkdirs();
		}
		return path;
		
	}
	
	public String getUserBasePathPublicLocal(String uuid)
	{
		String path  = System.getProperty("ServletPath") + uuid +"/";
		File pathObject = new File(path);
		if(!pathObject.exists())
		{
			pathObject.mkdirs();
		}
		return path;
		
	}
	public String getAppPublicPathLocal(String uuid,String appIdentfier)
	{
		String path  = System.getProperty("ServletPath") + uuid +"/apps/"+appIdentfier +"/";
		File pathObject = new File(path);
		if(!pathObject.exists())
		{
			pathObject.mkdirs();
		}
		return path;
		
	}
	
	public String toWebPath(String uuid,String appIdentfier,String fileName)
	{
		String path  = System.getProperty("WebPath") +"CMAC/" + uuid +"/apps/"+fileName;
		
		return path;
		
	}
	
	public String getAppAssetsLocal(String uuid,String appIdentfier)
	{
		String path  = getAppDataPath(uuid, appIdentfier,null) + "Assets/";
		File pathObject = new File(path);
		if(!pathObject.exists())
		{
			pathObject.mkdirs();
		}
		return path;
	}
	
	public String getAppAssetsPublicPathLocal(String uuid,String appIdentfier)
	{
		String path  = getAppPublicPathLocal(uuid, appIdentfier) + "Assets/";
		File pathObject = new File(path);
		if(!pathObject.exists())
		{
			pathObject.mkdirs();
		}
		return path;
	}
	public String getUserAppPath(String uuid,String appIdentfier)
	{
		String path  = getUserRootPath(uuid)+"/apps/"+appIdentfier +"/";
		File pathObject = new File(path);
		if(!pathObject.exists())
		{
			//pathObject.mkdirs();
		}
		return path;
		
	}
	
	public String getUserAppPath(String uuid)
	{
		String path  = getUserRootPath(uuid)+"/apps/";
		File pathObject = new File(path);
		if(!pathObject.exists())
		{
			//pathObject.mkdirs();
		}
		return path;
		
	}
	
	public void saveApplication(String uuid,String identifier,String rtConfig)
	{
		
		Application application= getApplication(uuid,identifier,rtConfig);
		if(application==null)
		{
			
		}
		//return application;
		
	}
	public void saveApplication(Application app)
	{
		if(app==null){
			return;
		}
		String appPath = getUserAppPath(app.getUserIdentifier(), app.getApplicationIdentifier());
		//System.out.println("saving app " + appPath);
		JSONSerializer serializer = new JSONSerializer();
		serializer.prettyPrint(true);
		String data = serializer.deepSerialize(app);
		
		File appPathO =new File(appPath); 
		if(! appPathO.exists())
		{
			appPathO.mkdirs();
		}
		
		File outfile = new File(appPath+"/appInfo.json");
        BufferedWriter writer = null;
        try {
			writer = new BufferedWriter(new FileWriter(outfile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			writer.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String appPath = System.getProperty("webapp.root") +;
		
		
	}
	
	public void UpgradeApplication(Application app)
	{
		if(app==null){
			return;
		}
		app.upgradeApplication(this);
		saveApplication(app);
		
		ApplicationStyleUpgrader sUpgrader = new ApplicationStyleUpgrader();
		sUpgrader.UpgradeApplication(app);
		
	}
	
	public Application loadApplication(String appPath)
	{
		Application app = null;
		//System.out.println("loading app " + appPath);
		String data = null;
		try {
			 data = StringUtils.readFileAsString(appPath);
		} catch (IOException e) {
			//System.out.println("\t	application : " + appPath + " doesnt exists");
		}
		if(data!=null && data.length() > 0)
		{
			JSONDeserializer derializerSC = new JSONDeserializer<Application>();
	    	if(data!=null && data.length() > 0)
	    	{
	    		try {
	    			app= (Application) derializerSC.deserialize(data);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
					return null;
				}
	    		
	    	}
	    	if(app!=null)
	    	{
	    		return app;
	    	}
		}
		return null;
	}
	public Application loadApplication(String uuid, String identifier)
	{
		Application app = null;
		
		String appPath = getUserAppPath(uuid, identifier);
		//System.out.println("loading app " + appPath);
		String data = null;
		try {
			 data = StringUtils.readFileAsString(appPath+"/appInfo.json");
			 
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//System.out.println("	\tapplication : " + appPath + " doesnt exists");
			
		}
		if(data!=null && data.length() > 0)
		{
			JSONDeserializer derializerSC = new JSONDeserializer<Application>();
	    	if(data!=null && data.length() > 0)
	    	{
	    		//System.out.println(searchConfigurationStr);
	    		try {
	    			app= (Application) derializerSC.deserialize(data);
	    			if(app!=null){
	    				app.applicationIdentifier=identifier;
	    				app.userIdentifier=uuid;
	    			}
	    			UpgradeApplication(app);
	    			app.setAppSettings(loadAppSettings(app));
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
	    	}
	    	if(app!=null)
	    	{
	    		/*
	    		Domain domain = Cache.getDomain(identifier);
	    		if(domain==null)
	    		{
	    			domain = new Domain();
	    			domain.title = identifier;
	    			Cache.domains.add(domain);
	    		}
	    		*/
	    		
	    		
	    		return app;
	    	}
	    	
		}
		
		//File outfile = new File(appPath+"/appInfo.json");
		
		return null;
	}
	public Application getApplication(String uuid, String identifier,String rtConfig)
	{
		return getApplication(uuid, identifier,false);
	}
	public Boolean deleteApplication(String uuid, String identifier)
	{
		
		Application app = getApplication(uuid, identifier, false);
		if(app!=null){
			String appPath = getAppDataPath(uuid, identifier, null);
			if(appPath!=null)
			{
				File appFile  = new File(appPath);
				if(appFile.exists() && appFile.isDirectory())
				{
					try {
						FileUtils.deleteDirectory(appFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//appFile.delete();
				}
			}
			this.getApplications().remove(app);
		}
		return true;
	}
	
	public Application copyApplication(String srcUUID, String srcAppId,String dstUUID,String dstAppId)
	{
		String srcPath=getUserAppPath(srcUUID, srcAppId);
		File srcAppFilePathO = new File(srcPath);
		if(!srcAppFilePathO.exists())
		{
			return null;
		}
		String dstPath = getUserAppPath(dstUUID);
		File dstAppFilePathO = new File( dstPath + dstAppId );
		if(!dstAppFilePathO.exists())
		{
			dstAppFilePathO.mkdirs();
		}
		String includes []={""};
		//org.codehaus.plexus.util.FileUtils.copyDirectory(arg0, arg1, arg2, arg3)
		

		//System.out.println(" ####### " + org.codehaus.plexus.util.FileUtils.getDefaultExcludes().toString());
		
		/**
		try {
			//org.codehaus.plexus.util.FileUtils.copyDirectory(srcAppFilePathO, dstAppFilePathO,"**",org.codehaus.plexus.util.FileUtils.getDefaultExcludes().toString());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		

		/***
		 * copy src app 
		 */
		try {
			FileUtils.copyDirectory(srcAppFilePathO, dstAppFilePathO, new FileFilter() {
				
				  public boolean accept(File pathname) {
			        // We don't want 'Sub3' folder to be imported
			        // + look at the settings to decide if some format needs to be
			        // excluded
			        String name = pathname.getName();
			        
			        /*if (!Settings.getSiemensOptionAWL() && name.endsWith(".awl"))
			            return false;
			        if (!Settings.getSiemensOptionSCL() && name.endsWith(".scl"))
			            return false;

			        return !(name.equals("Sub3") && pathname.isDirectory());
			        */
			       //System.out.println(pathname.getAbsolutePath() + "  " + pathname.getName());
			       if(pathname.getAbsolutePath().contains("svn"))
			       {
			    	   return false;
			       }
			        return true;
			    }
			}, true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/*
		try {
			FileUtils.copyDirectory(srcAppFilePathO, dstAppFilePathO, new FileFilterUtils(){
			    public boolean accept(File pathname) 
			    {
			        // We don't want 'Sub3' folder to be imported
			        // + look at the settings to decide if some format needs to be
			        // excluded
			       if(pathname.getAbsolutePath().contains("svn"))
			       {
			    		   
			       }
			    }
			}
			    
			);

		} catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
		*/
		
		
		/***
		 * transform urls
		 */
		String appPath = getUserAppPath(dstUUID, dstAppId);
		System.out.println("loading app " + appPath);
		String data = null;
		try {
			data = StringUtils.readFileAsString(appPath+"/appInfo.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(data!=null)
		{
			String stringToReplace  = ""+srcAppId +"/";
			String newString = "/" + dstAppId + "/";
			data = data.replaceAll(stringToReplace, newString);
			
			try {
				StringUtils.writeToFile(data, appPath+"/appInfo.json");
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		/***
		 * Load Application
		 */
		Application dstApp = loadApplication( appPath+"/appInfo.json");
		if(dstApp==null)
		{
			return null;
		}
		
		dstApp.setApplicationIdentifier(dstAppId);
		dstApp.setUserIdentifier(dstUUID);
		saveApplication(dstApp);
		return dstApp;
		
	}
	
	public ArrayList<Application>getUserAppByTitle(String uuid,String title)
	{
		ArrayList<Application>result = new ArrayList<Application>();
		ArrayList<Application>userApps = getUserApplications(uuid);
		int count = 0;
		for (int i = 0 ;  i < userApps.size() ; i ++)
		{
			Application app = userApps.get(i);
			ApplicationMetaData appMeta=app.getMetaData();
			if(appMeta!=null)
			{
				ConfigurableInformation appTitle = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_TITLE);
				if(appTitle!=null)
				{
					if(appTitle.getValue() !=null && appTitle.getValue().equals(title))
					{
						result.add(app);
					}
				}
			}
			
		}
		
		return result;
	}
	public String getNewTitle(String uuid,String incomingTitle)
	{
		ArrayList<Application>userApps = getUserAppByTitle(uuid, incomingTitle);
		if(userApps !=null && userApps.size()>0)
		{
			return incomingTitle + " " + userApps.size();
		}
		return "" + incomingTitle;
	}
	public Application createWizardApplication(String uuid,String title, String appTemplate,String visualTemplate,String iconSet,String sessionId,String platform)
	{
		String srcUUID = System.getProperty("adminUser");
		String srcAppId = ""+appTemplate;
		Application srcApp=getApplication(srcUUID, srcAppId, false);
		if(srcApp==null)
		{
			return null;
		}
		
		String dstAppid=getNewTitle(uuid, title).replace(" ","").toLowerCase();//+""+sessionId.toLowerCase().substring(0, 3);
		dstAppid+=sessionId.toLowerCase().substring(0,2);
		dstAppid = dstAppid.toLowerCase();
		Application dstApp=getApplication(uuid, dstAppid, false);
		if(dstApp==null)
		{
			//we copy the admin application
			dstApp = copyApplication(srcUUID, srcAppId, uuid,dstAppid);
			
			//clean data sources :
			File dataSourcePathO = new File(getUserAppPath(uuid, dstApp.getApplicationIdentifier()) +"/datasources");
			if(dataSourcePathO.exists()){
				dataSourcePathO.delete();
				try {
					FileUtils.deleteDirectory(dataSourcePathO);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//clean uploads :
			
			/*
			File dataSourcePathO = new File(getUserAppPath(uuid, dstApp.getApplicationIdentifier()) +"/datasources");
			if(dataSourcePathO.exists()){
				dataSourcePathO.delete();
				try {
					FileUtils.deleteDirectory(dataSourcePathO);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
			dstApp.setDataSources(new ArrayList<DataSourceBase>());
			DataSourceBase sqlDataSource = new SQLDataSource();
			sqlDataSource.setHost("127.0.0.1");
			sqlDataSource.setPrefix(null);
			sqlDataSource.setDatabase(null);
			sqlDataSource.setPassword(null);
			sqlDataSource.setUser(uuid);
			sqlDataSource.setType("Liferay");
			sqlDataSource.setVersion("6.1");
			sqlDataSource.setUrl("127.0.0.1");
			sqlDataSource.setUid("Liferay");
			dstApp.addDataSource(sqlDataSource);
			
			TrackingUtils.sendMail("Create Application", title, uuid,dstApp.getApplicationIdentifier());
			
			ApplicationMetaData appMeta=dstApp.getMetaData();
			if(appMeta!=null)
			{
				ConfigurableInformation appTitle = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_TITLE);
				if(appTitle!=null)
				{
					appTitle.setValue(getNewTitle(uuid, title));
				}
				
				ConfigurableInformation iconSetCI = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_ICON_SET);
				if(iconSetCI!=null)
				{
					iconSetCI.setValue(iconSet);
				}
			}
			
			if(iconSet!=null)
			{
				applyIconSet(dstApp, iconSet);
				//saveApplication(dstApp);
				//getApplications().remove(dstApp);
			}
			
			saveApplication(dstApp);
				
			String appTemplateId= appTemplate.replace("qx", "");
			ApplicationTemplate _appTemplate = ApplicationTemplateManager.getApplicationTemplate(uuid, appTemplate, System.getProperty("defaultPlatform"),appTemplateId);
			if(appTemplate!=null)
			{
				String defaultThemeId=_appTemplate.applicationThemeTemplate;
				if(visualTemplate!=null)
				{
					defaultThemeId=visualTemplate;
				}
				if(defaultThemeId!=null)
				{
					//apply the template 
					TemplateManager.applyTemplate(uuid, dstApp.getApplicationIdentifier(), _appTemplate.getApplicationTemplatePlatform() , defaultThemeId, _appTemplate.applicationTemplatePlatform, 0);
				}
			}
			
			/***
			 * Adjust Custom Fields CSS
			 */
			String customFieldsPathStr = TemplateManager.getUserStyleRootFolder(uuid, dstApp.getApplicationIdentifier(), System.getProperty("defaultPlatform")) + "CustomFields.css";
			String cssFileContent =null;
			
			try {
				cssFileContent =StringUtils.readFileAsString(customFieldsPathStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(cssFileContent!=null){
				
				String iconFolderPrefix = "/SharedAssets/iconSets/" + _appTemplate.getIconSet() +"/";
				cssFileContent = cssFileContent.replace(iconFolderPrefix,"/SharedAssets/iconSets/"+iconSet+"/" );
				try {
					StringUtils.writeToFile(cssFileContent, customFieldsPathStr);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			/***
			 * Fix App Settings
			 */
			String dstPath = getAppDataPath(uuid, dstAppid, null) + "appSettings.xml";
			String content = null;
			try {
				content = StringUtils.readFileAsString(dstPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			content = content.replace( "<appIdentifier>" + srcAppId+"</appIdentifier>", "<appIdentifier>" + dstAppid+"</appIdentifier>");
			content = content.replace( "<appIdentifier>appId</appIdentifier>", "<appIdentifier>" + dstAppid+"</appIdentifier>");
			try {
				StringUtils.writeToFile(content, dstPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//get Application Tempate
			
		}
		return dstApp;
	}
	public void applyIconSet(Application app,String iconSet)
	{
		if(app==null){
			return;
		}
		String iconFolderPrefix = "/SharedAssets/iconSets/" + iconSet + "/";
		ArrayList<Page> subPages= app.getSubPages("0");
		if(subPages!=null && subPages.size()>0)
		{
			for(int pi = 0  ; pi  <  subPages.size() ; pi++)	
			{
				Page  p = subPages.get(pi);
				if(p.qxappTabIdentifier==null){
					continue;
				}
				ConfigurableInformation ciData= p.getItemByChainAndName(0,"Icon");
				if(ciData != null)
				{
					String iconCINewValue = iconFolderPrefix + p.qxappTabIdentifier.toLowerCase() + ".png";
                    //update CI :
                    //xapp.utils.setCIValueByField(iconCi, 'value', iconCINewValue);
					ciData.setValue(iconCINewValue);
				}
			}
		}
	}
	public Application createTemplateApplication(String uuid,String title, String appTemplate,String sessionId,String platform)
	{
		String srcUUID = System.getProperty("adminUser");
		String srcAppId = ""+appTemplate;
		Application srcApp=getApplication(srcUUID, srcAppId, false);
		if(srcApp==null)
		{
			return null;
		}
		
		String dstAppid=title.replace(" ","").toLowerCase()+""+sessionId.toLowerCase().substring(0, 8);
		
		Application dstApp=getApplication(uuid, dstAppid, false);
		if(dstApp==null)
		{
			//we copy the admin application
			dstApp = copyApplication(srcUUID, srcAppId, uuid,dstAppid);
			
			//clean data sources :
			File dataSourcePathO = new File(getUserAppPath(uuid, dstApp.getApplicationIdentifier()) +"/datasources");
			if(dataSourcePathO.exists()){
				dataSourcePathO.delete();
				try {
					FileUtils.deleteDirectory(dataSourcePathO);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//clean uploads :
			
			/*
			File dataSourcePathO = new File(getUserAppPath(uuid, dstApp.getApplicationIdentifier()) +"/datasources");
			if(dataSourcePathO.exists()){
				dataSourcePathO.delete();
				try {
					FileUtils.deleteDirectory(dataSourcePathO);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
			dstApp.setDataSources(new ArrayList<DataSourceBase>());
			DataSourceBase sqlDataSource = new SQLDataSource();
			sqlDataSource.setHost("127.0.0.1");
			sqlDataSource.setPrefix(null);
			sqlDataSource.setDatabase(null);
			sqlDataSource.setPassword(null);
			sqlDataSource.setUser(uuid);
			sqlDataSource.setType("Liferay");
			sqlDataSource.setVersion("6.1");
			sqlDataSource.setUrl("127.0.0.1");
			sqlDataSource.setUid("Liferay");
			dstApp.addDataSource(sqlDataSource);
			
			TrackingUtils.sendMail("Create Application", title, uuid,dstApp.getApplicationIdentifier());
			
			ApplicationMetaData appMeta=dstApp.getMetaData();
			if(appMeta!=null)
			{
				ConfigurableInformation appTitle = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_TITLE);
				if(appTitle!=null)
				{
					appTitle.setValue(title);
				}
			}
			
			
			
			saveApplication(dstApp);
				
			String appTemplateId=appTemplate.replace("qx", "");
			ApplicationTemplate _appTemplate = ApplicationTemplateManager.getApplicationTemplate(uuid, appTemplate, System.getProperty("defaultPlatform"),appTemplateId);
			if(appTemplate!=null)
			{
				String defaultThemeId=_appTemplate.applicationThemeTemplate;
				if(defaultThemeId!=null)
				{
					//apply the template 
					TemplateManager.applyTemplate(uuid, dstApp.getApplicationIdentifier(), _appTemplate.getApplicationTemplatePlatform() , defaultThemeId, _appTemplate.applicationTemplatePlatform, 0);
				}
			}
			//get Application Tempate 
			
		}

		dstApp.setStyleVersion("2");
		return dstApp;
	}
	public Application createGuestApplication(String uuid, String appId,String sessionId)
	{
		String srcUUID = System.getProperty("adminUser");
		String srcAppId = ""+appId;
		Application srcApp=getApplication(srcUUID, srcAppId, false);
		if(srcApp==null)
		{
			return null;
		}
		
		
		Application dstApp=getApplication(uuid, appId, false);
		if(dstApp==null)
		{
			//we copy the admin application
			dstApp = copyApplication(srcUUID, srcAppId, uuid, appId+sessionId);
			
			//clean data sources :
			File dataSourcePathO = new File(getUserAppPath(uuid, dstApp.getApplicationIdentifier()) +"/datasources");
			if(dataSourcePathO.exists()){
				dataSourcePathO.delete();
				try {
					FileUtils.deleteDirectory(dataSourcePathO);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//clean uploads :
			
			/*
			File dataSourcePathO = new File(getUserAppPath(uuid, dstApp.getApplicationIdentifier()) +"/datasources");
			if(dataSourcePathO.exists()){
				dataSourcePathO.delete();
				try {
					FileUtils.deleteDirectory(dataSourcePathO);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
			dstApp.setDataSources(new ArrayList<DataSourceBase>());
			DataSourceBase sqlDataSource = new SQLDataSource();
			sqlDataSource.setHost("127.0.0.1");
			sqlDataSource.setPrefix(null);
			sqlDataSource.setDatabase(null);
			sqlDataSource.setPassword(null);
			sqlDataSource.setUser(uuid);
			sqlDataSource.setType("Liferay");
			sqlDataSource.setVersion("6.1");
			sqlDataSource.setUrl("127.0.0.1");
			sqlDataSource.setUid("Liferay");
			dstApp.addDataSource(sqlDataSource);
			
			
			saveApplication(dstApp);
				
			String appTemplateId=appId.replace("qx", "");
			ApplicationTemplate appTemplate = ApplicationTemplateManager.getApplicationTemplate(uuid, appId, System.getProperty("defaultPlatform"),appTemplateId);
			if(appTemplate!=null)
			{
				String defaultThemeId=appTemplate.applicationThemeTemplate;
				if(defaultThemeId!=null)
				{
					//apply the template 
					TemplateManager.applyTemplate(uuid, dstApp.getApplicationIdentifier(), appTemplate.getApplicationTemplatePlatform() , defaultThemeId, appTemplate.applicationTemplatePlatform, 0);
				}
				
			}
			//get Application Tempate 
			
		}
		return dstApp;
	}
	public Application createApplication(String uuid, String title)
	{
		String identifier=title.replace(" ", "");
		Application a = this.getById(uuid, identifier);
		if(a==null)
		{
			a = new Application();
			//this.getApplications().add(a);
			a.setApplicationIdentifier(identifier);
			a.setUserIdentifier(uuid);
			
			saveApplication(a);
			
			a=getApplication(uuid, identifier,"Debug");
			ApplicationMetaData appMeta = a.getMetaData();
			ConfigurableInformation cmAppTitle = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_TITLE);
			if(cmAppTitle!=null)
			{
				cmAppTitle.value=title;
			}
			
			ConfigurableInformation cmAppPrexfix= CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_SCHEMA_PREFIX);
			if(cmAppPrexfix!=null)
			{
				cmAppPrexfix.value="com.pm." + identifier;
			}
			
			saveApplication(a);
			
			String appSettingsFile = getStoreRootPath()+"templates/appSettings.xml";
	
			try {
				File  aps = new File(appSettingsFile);
				String dstPath = getAppDataPath(uuid, identifier, null) + "appSettings.xml";
				File dst = new File( dstPath );
				FileUtils.copyFile(aps,dst );
				
				String content = StringUtils.readFileAsString(dstPath);
				content = content.replace("<appIdentifier>aapId</appIdentifier>", "<appIdentifier>" + identifier+"</appIdentifier>");
				StringUtils.writeToFile(content, dstPath);
				
				
				String srcPath= getStoreRootPath()+"templates/style/";
				dstPath = getAppDataPath(uuid, identifier, null) + "style/";
				FileUtils.copyDirectory(new File(srcPath), new File(dstPath));
				
				srcPath= getStoreRootPath()+"templates/Assets/";
				dstPath = getAppDataPath(uuid, identifier, null) + "Assets/";
				FileUtils.copyDirectory(new File(srcPath), new File(dstPath));
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return a;
	}
	public Application getApplication(String uuid, String identifier, Boolean create)
	{
		
		//this.getApplications().clear();
		Application a = this.getById(uuid, identifier);
		if(a==null)
		{
			
			a=loadApplication(uuid, identifier);
			if(a!=null)
			{
				this.getApplications().add(a);
				UpgradeApplication(a);
				a.setAppSettings(loadAppSettings(a));
			}
		}
		
		if(a==null && create)
		{
			a= createApplication(uuid, identifier);
		}
		
		return a;
	}

	/**
	 * @return the applications
	 */
	
}
