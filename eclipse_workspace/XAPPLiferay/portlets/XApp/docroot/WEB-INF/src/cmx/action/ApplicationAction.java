package cmx.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.json.annotations.SMDMethod;
import org.xbill.DNS.APLRecord;

import pmedia.AssetTools.DownloadManager;
import pmedia.types.BaseData;
import pmedia.types.CIOverride;
import pmedia.types.CList;
import pmedia.types.DownloadTask;
import pmedia.utils.CITools;
import pmedia.utils.SecurityUtil;
import pmedia.utils.StringUtils;
import cmx.tools.AppFeatureConfigUtil;
import cmx.tools.LiferayContentTools;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.CIType;
import cmx.types.CMError;
import cmx.types.ConfigurableInformation;
import cmx.types.DataSourceBase;
import cmx.types.Page;
import cmx.types.ServiceSettings;
import cmx.types.XASFeature;

import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

//import com.pm.Catalog;
//import com.pm.DBContextHolder;
//import com.pm.DBSource;


//@Namespace("/db")
//@Result(name="success",location="/dummy.jsp")
//@Result(location="${redirectURL}", type="redirect")

public class ApplicationAction
   extends ActionSupport implements ServletRequestAware,ServletResponseAware,SessionAware
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9097347805333543588L;


	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	
	public CMError error;
	
	private String uuid;
	private String lang;
	private String type;
	private String archiveType;
	private String applicationIdentifier;
	private ApplicationManager appManager=null;
	public Application application=null;
	public String archiveRedirectionUrl;
	
	//http://192.168.1.37:8082/CMDataX/getArchive.action?&uuid=ffa3c5bc-6414-47cf-b728-731b14337baf&applicationIdentifier=ibizamedia
	
	
	public String getExternalUrl()
	{
		
		return archiveRedirectionUrl;
		//return "http://192.168.1.37:8082/CMDataX/appLogo.jpg";
	}
	
	public String purgeApplication()
	{
		//buzz : 
		//192.168.1.37:8082/CMDataX/purgeApplication.action?&uuid=20e7f630-2d4d-11e1-a223-0800200c9a66&applicationIdentifier=buzz
		if(applicationIdentifier!=null && uuid!=null)
		{
			ApplicationManager appManager = ApplicationManager.getInstance();
			if(appManager !=null){
				Application app  = getApplication(applicationIdentifier,uuid);
				if(app!=null)
				{
					System.out.println("purging application");
					String servletPath = getSession().getServletContext().getRealPath("");
					//archiveRedirectionUrl = appManager.compressApplication(app,servletPath);
					appManager.purgeApplication(app, servletPath);
				}
			}
		}
		return "successPurge";
	}
	
	public String getArchive(String rtConfig)
	{
		

		if(applicationIdentifier!=null && uuid!=null){

			ApplicationManager appManager = ApplicationManager.getInstance();
			if(appManager !=null){
				Application app  = getApplication(applicationIdentifier,uuid);
				if(app!=null)
				{
					
					//String servletPath = getSession().getServletContext().getRealPath("");
					String servletPath = System.getProperty("ServletPath");
					String fileName = appManager.compressApplication(app,servletPath);
					
					System.out.println("compressing application : " + servletPath);
					String baseUrl  = StringUtils.getBaseUrl(this.getRequest());
					baseUrl+="/" +app.getUserIdentifier()+"/" + fileName;
					archiveRedirectionUrl = baseUrl;
					
				}
			}
		}
		return "archive";
	}
	
	
	
	public ApplicationManager getApplicationManager(String identifier)
	{
		appManager = ApplicationManager.getInstance();
		return appManager;
	}

	public Application getApplication(String identifier,String uuid)
	{
		if(!SecurityUtil.isValidAppAction(uuid, identifier, getRequest()))
		{
			return null;
		}
		appManager  = getApplicationManager(identifier);
		application= appManager.getApplication(uuid,identifier,"Debug");
		return application;
		
	}
	@SMDMethod
	public ArrayList<XASFeature>getXASFeatures(String uuid,String identifier)
	{
		if(!SecurityUtil.isValidAppAction(uuid, identifier, getRequest()))
		{
			return null;
		}
		appManager  = ApplicationManager.getInstance();
		application= appManager.getApplication(uuid,identifier,"Debug");
		ArrayList<XASFeature>result = AppFeatureConfigUtil.getFeatures();
		
		return result;
	}
	@SMDMethod
	public Application loadApplication(String uuid,String identifier)
	{
		if(!SecurityUtil.isValidAppAction(uuid, identifier, getRequest()))
		{
			return null;
		}
		User liferayUser = LiferayContentTools.isValidUser(uuid, getRequest());
		if(liferayUser==null)
		{
			//return null;
		}
		//System.out.println("loading application : " + identifier + " uuid " +uuid);
		appManager  = ApplicationManager.getInstance();
		application= appManager.getApplication(uuid,identifier,"Debug");
		return application;
	}
	@SMDMethod
	public ServiceSettings getServiceSettings()
	{
		//System.out.println("loading application : " + identifier + " uuid " +uuid);
		ServiceSettings settings = new ServiceSettings();
		settings.setAjaxplorerRootUrl(System.getProperty("AjaxplorerRootUrl"));
		settings.setFeatherEditorApiKey(System.getProperty("FeatherEditorApiKey"));
		settings.setWebPath(System.getProperty("WebPath"));
		settings.setImageScaleServlet(System.getProperty("imageProcessorUrl"));
		//(ServiceSettings)System.getProperty("ServiceSettings");
		
		return settings;
		
	}
	public void copyToAppWebRoot(ApplicationManager appMan,Application app,String path,String dstPathPrefix)
	{
		String localUserPathBasePublic =appMan.getUserBasePathPublicLocal(app.getUserIdentifier())+"apps/" + app.getApplicationIdentifier() +"/";
    	
		File dstFilePath = new File(localUserPathBasePublic+dstPathPrefix);
		if(!dstFilePath.exists()){
			dstFilePath.mkdirs();
		}
    	File srcFile = new File( path);
    	File dstFile = new File(localUserPathBasePublic+dstPathPrefix + StringUtils.filename(path) + "." + StringUtils.extension(path));
    	try {
			FileUtils.copyFile(srcFile, dstFile);
			//FileUtils.copyFile(srcFileUpload, dstFile2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ConfigurableInformation checkForAssetModification(ApplicationManager appMan,Application app, ConfigurableInformation oriCi,ConfigurableInformation newCi)
	{
		if(newCi==null)
			return null;
		
		if(newCi.type ==  CITools.CIToInteger(CIType.ICON) || 
				newCi.type ==  CITools.CIToInteger(CIType.IMAGE) ||
				newCi.type ==  CITools.CIToInteger(CIType.LOGO))
		{
			String dstBasePath = appMan.getUserRootPath(app.getUserIdentifier()) +"/apps/";
			String ciNewValue = newCi.value;
			String ciOldValue = oriCi.getValue();
			
			//System.out.print("old ci " + ciOldValue + " :: " + ciNewValue);
			if(!ciNewValue.contains("http"))
			{
				return newCi;
			}
			
			
			/***
			 * Case of a prev. value exists : 
			 * 1. download the item
			 * 2. extract file name of the old item.
			 * 3. overwrite the old item. 
			 */
			
				// prepare task
				DownloadTask task=new DownloadTask();
				task.remoteFilepath = newCi.value;
				
				String oriPath = null;
				
				//String pathComponent=null;
				String suffix=null;
				
				String fileNameNew = StringUtils.filenameComplete(ciNewValue);
				String fileNameOld = null;
				if(ciOldValue!=null)
					fileNameOld = StringUtils.filenameComplete(ciOldValue);
				
				String fileName=null;

				
				if(ciOldValue!=null && ciOldValue.length() > 0)
				{
					// we are updating an existing picture
					oriPath = StringUtils.path(appMan.getUserRootPath(app.getUserIdentifier()) +"/apps/" + ciOldValue);
					fileName=fileNameOld;
					
				}else{
					
					//we are having a new picture
					
					oriPath = appMan.getAppAssetsLocal(app.getUserIdentifier() , app.getApplicationIdentifier());
					if(newCi.type ==  CITools.CIToInteger(CIType.ICON))
					{ 
						suffix =  "icons";
					}
					if(newCi.type ==  CITools.CIToInteger(CIType.IMAGE))
					{
						suffix="images";
						
					}
					oriPath+=suffix;
					fileName = fileNameNew;
				}
				
				String dstLocalPath=oriPath+  "/" + fileName;
				File oriFile = new File(dstLocalPath);
				if(oriFile.exists()){
					oriFile.delete();
				}
				task.localFilepath =dstLocalPath;
				DownloadManager.downloadFileInternalExt(task);
				System.out.println("download " +  newCi.value + " to " + task.localFilepath);
				//now 
				
				if(ciOldValue!=null && ciOldValue.length() > 0 ){
					newCi.value = oriCi.value;
				}else
				{
					newCi.value = app.getApplicationIdentifier() + "/Assets/" + suffix+ "/" + fileName;
				}
			}
			
			
			String localUserPathBasePublic =appMan.getUserBasePathPublicLocal(app.getUserIdentifier())+"apps/";
			String localAppPathAssets = appMan.getAppAssetsLocal(app.getUserIdentifier(), app.getApplicationIdentifier());
	    	
	    	
			String relativeFileName = appMan.getUserRootPath(app.getUserIdentifier()) +"/apps/" + newCi.getValue();
	    	//String ciValue = updatedItem.getValue();
	    	File srcFileUpload = new File( appMan.getUserRootPath(app.getUserIdentifier()) +"/apps/" + newCi.getValue());
	    	
	    	
	    	File dstFile1 = new File( localUserPathBasePublic + newCi.getValue());
	    	
	    	
	    	//System.out.println("setting new ci value to : " + newCi.value);
	    	//File dstFile2 = new File( localAppPathAssets + file.getFileName());
	    	
	    	/*
	    	try {
				FileUtils.copyFile(srcFileUpload, dstFile1);
				//FileUtils.copyFile(srcFileUpload, dstFile2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	*/
			
		
		return newCi;
	}
	
	public String findIcon(ApplicationManager appMan,Application app, ConfigurableInformation oriCi,ConfigurableInformation newCi)
	{
		if(newCi==null)
			return null;
		
		if(newCi.type ==  CITools.CIToInteger(CIType.ICON) || 
				newCi.type ==  CITools.CIToInteger(CIType.IMAGE) ||
				newCi.type ==  CITools.CIToInteger(CIType.LOGO))
		{
			String dstBasePath = appMan.getUserRootPath(app.getUserIdentifier()) +"/apps/";
			String ciNewValue = newCi.value;
			String ciOldValue = oriCi.getValue();
			
			if(ciNewValue.contains("http"))
			{
				// prepare task
				DownloadTask task=new DownloadTask();
				task.remoteFilepath = newCi.value;
				
				String oriPath = null;
				
				String pathComponent=null;
				String suffix=null;
				
				String fileNameNew = StringUtils.filenameComplete(ciNewValue);
				String fileNameOld = null;
				if(ciOldValue!=null)
					fileNameOld = StringUtils.filenameComplete(ciOldValue);
				
				String fileName=null;

				
				if(ciOldValue!=null && ciOldValue.length() > 0)
				{
					// we are updating an existing picture
					oriPath = StringUtils.path(appMan.getUserRootPath(app.getUserIdentifier()) +"/apps/" + ciOldValue);
					fileName=fileNameOld;
					
				}else{
					
					//we are having a new picture
					
					oriPath = appMan.getAppAssetsLocal(app.getUserIdentifier() , app.getApplicationIdentifier());
					if(newCi.type ==  CITools.CIToInteger(CIType.ICON))
					{ 
						suffix =  "icons";
					}
					if(newCi.type ==  CITools.CIToInteger(CIType.IMAGE))
					{
						suffix="images";
						
					}
					oriPath+=suffix;
					fileName = fileNameNew;
				}
				
				String dstLocalPath=oriPath+  "/" + fileName;
				File oriFile = new File(dstLocalPath);
				if(oriFile.exists()){
					oriFile.delete();
				}
				
				task.localFilepath =dstLocalPath;
				
				DownloadManager.downloadFileInternalExt(task);
				
				//now 
				
				if(ciOldValue!=null && ciOldValue.length() > 0 ){
					newCi.value = oriCi.value;
				}else
				{
					newCi.value = app.getApplicationIdentifier() + "/Assets/" + suffix+ "/" + fileName;
				}
			}
			
			
			String localUserPathBasePublic =appMan.getUserBasePathPublicLocal(app.getUserIdentifier())+"apps/";
			String localAppPathAssets = appMan.getAppAssetsLocal(app.getUserIdentifier(), app.getApplicationIdentifier());
	    	
	    	
			String relativeFileName = appMan.getUserRootPath(app.getUserIdentifier()) +"/apps/" + newCi.getValue();
	    	//String ciValue = updatedItem.getValue();
	    	File srcFileUpload = new File( appMan.getUserRootPath(app.getUserIdentifier()) +"/apps/" + newCi.getValue());
	    	
	    	
	    	File dstFile1 = new File( localUserPathBasePublic + newCi.getValue());
	    	System.out.println("setting new ci value to : " + newCi.value);
	    	//File dstFile2 = new File( localAppPathAssets + file.getFileName());
	    	
	    	try {
				FileUtils.copyFile(srcFileUpload, dstFile1);
				//FileUtils.copyFile(srcFileUpload, dstFile2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
			
		}
		return "";
	}
	
	@SMDMethod
	public ConfigurableInformation createCI(String appIdentfier,String uuid,String itemJSON) throws java.lang.Exception
	{
		User liferayUser = LiferayContentTools.isValidUser(uuid, getRequest());
		if(liferayUser==null)
		{
			return null;
		}
		appManager  = getApplicationManager(appIdentfier);
		if(uuid==null)
		{
			throw new java.lang.Exception("no uuid");
		}
		application= appManager.getApplication(uuid,appIdentfier,false);
		
		ConfigurableInformation newItem=null;	
		if(application!=null){
			
			newItem  = application.createCI(itemJSON);
			appManager.saveApplication(application);
		}
		return newItem;
		
	}
	
	@SMDMethod
	public CIOverride updateCIOverride(String appIdentfier,String uuid,String itemJSON) throws java.lang.Exception
	{
		User liferayUser = LiferayContentTools.isValidUser(uuid, getRequest());
		if(liferayUser==null)
		{
			return null;
		}
		
		appManager  = getApplicationManager(appIdentfier);
		if(uuid==null)
		{
			throw new java.lang.Exception("no uuid");
		}

		application= appManager.getApplication(uuid,appIdentfier,false);
		CIOverride newItem=null;	
		if(application!=null)
		{
			newItem  = application.updateCIOverride(itemJSON);
			appManager.saveApplication(application);
		}
		return newItem;
		
	}
	@SMDMethod
	public CIOverride createCIOverride(String appIdentfier,String uuid,String itemJSON) throws java.lang.Exception
	{
		User liferayUser = LiferayContentTools.isValidUser(uuid, getRequest());
		if(liferayUser==null)
		{
			return null;
		}
		appManager  = getApplicationManager(appIdentfier);
		if(uuid==null)
		{
			throw new java.lang.Exception("no uuid");
		}
		application= appManager.getApplication(uuid,appIdentfier,false);
		
		CIOverride newItem=null;	
		if(application!=null){
			
			newItem  = application.createCIOverride(itemJSON);
			appManager.saveApplication(application);
		}
		return newItem;
		
	}
	@SMDMethod
	public CIOverride deleteCIOverride(String appIdentfier,String uuid,String itemJSON) throws java.lang.Exception
	{
		User liferayUser = LiferayContentTools.isValidUser(uuid, getRequest());
		if(liferayUser==null)
		{
			return null;
		}
		appManager  = getApplicationManager(appIdentfier);
		if(uuid==null)
		{
			throw new java.lang.Exception("no uuid");
		}
		application= appManager.getApplication(uuid,appIdentfier,false);
		
		CIOverride newItem=null;	
		if(application!=null){
			
			application.deleteCIOverride(itemJSON);
			appManager.saveApplication(application);
		}
		return newItem;
		
	}
	public ConfigurableInformation getItemFromJSON(String itemJSON)
	{
		
		JSONSerializer serializer = new JSONSerializer();
		ConfigurableInformation item= null;
    	JSONDeserializer derializerSC = new JSONDeserializer<ConfigurableInformation>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		//System.out.println(searchConfigurationStr);
    		try {
    			item= (ConfigurableInformation) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
	
			}
    	}
    	return item;
	}
	
	public Page getPageFromJSON(String itemJSON)
	{
		
		JSONSerializer serializer = new JSONSerializer();
		Page item= null;
    	JSONDeserializer derializerSC = new JSONDeserializer<Page>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		//System.out.println(searchConfigurationStr);
    		try {
    			item= (Page) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
	
			}
    	}
    	return item;
	}
	
	@SMDMethod
	public void pageChanged(String appIdentfier,String uuid,String itemJSON) throws java.lang.Exception
	{
		if(!SecurityUtil.isValidAppAction(uuid, appIdentfier, getRequest()))
		{
			return ;
		}
		appManager  = getApplicationManager(appIdentfier);
		if(uuid==null)
		{
			throw new java.lang.Exception("no uuid");
		}

		application= appManager.getApplication(uuid,appIdentfier,false);

		ConfigurableInformation newCi = getItemFromJSON(itemJSON);
		if(newCi !=null && newCi.getStoreDestination()!=null && newCi.getStoreDestination().length() > 0)
		{
			if(newCi.getStoreDestination().equals("metaDataStore"))
			{
				appIdentfier=newCi.getParentId();
				
				//ConfigurableInformation oriCi = application.getItem(itemJSON);
				
				application= appManager.getApplication(uuid,appIdentfier,false);
				if(application!=null){
					application.flushCache=1;
				}
				ConfigurableInformation oriCi = CITools.getById(application.getMetaData().getProperties(), newCi.getName());
				if(newCi!=null && oriCi!=null)
				{				
					newCi =checkForAssetModification(appManager, application, oriCi,newCi);
				}
				
				ConfigurableInformation updatedItem = application.metaDataChanged(newCi);
				appManager.saveApplication(application);
				return;
			}
		}
		
		if(application!=null){
			application.flushCache=1;
			ConfigurableInformation updatedItem=null;
			
			
			
			    ConfigurableInformation oriCi = application.getItem(itemJSON);
				if(newCi!=null && oriCi!=null)
				{				
					newCi =checkForAssetModification(appManager, application, oriCi,newCi);
				}
				updatedItem  = application.itemChanged(newCi);
			appManager.saveApplication(application);
		}		
	}
	
	@SMDMethod
	public void itemChanged(String appIdentfier,String uuid,String itemJSON) throws java.lang.Exception
	{
		if(!SecurityUtil.isValidAppAction(uuid, appIdentfier, getRequest()))
		{
			return;
		}
		appManager  = getApplicationManager(appIdentfier);
		if(uuid==null)
		{
			throw new java.lang.Exception("no uuid");
		}

		application= appManager.getApplication(uuid,appIdentfier,false);
		if(application!=null){
			application.flushCache=1;
		}

		ConfigurableInformation newCi = getItemFromJSON(itemJSON);
		if(newCi !=null && newCi.getStoreDestination()!=null && newCi.getStoreDestination().length() > 0)
		{
			if(newCi.getStoreDestination().equals("metaDataStore"))
			{
				//appIdentfier=newCi.getParentId();
				
				//ConfigurableInformation oriCi = application.getItem(itemJSON);
				
				//application= appManager.getApplication(uuid,appIdentfier,false);
				if(application==null){
					return;
				}
				ConfigurableInformation oriCi = CITools.getById(application.getMetaData().getProperties(), newCi.getName());
				if(newCi!=null && oriCi!=null)
				{				
					newCi =checkForAssetModification(appManager, application, oriCi,newCi);
				}
				
				ConfigurableInformation updatedItem = application.metaDataChanged(newCi);
				appManager.saveApplication(application);
				return;
			}
		}
		
		if(application!=null){
			ConfigurableInformation updatedItem=null;
			
			
			
			    ConfigurableInformation oriCi = application.getItem(itemJSON);
				if(newCi!=null && oriCi!=null)
				{				
					newCi =checkForAssetModification(appManager, application, oriCi,newCi);
				}
				updatedItem  = application.itemChanged(newCi);
			appManager.saveApplication(application);
		}		
	}
	public String resolveRemotePath(Application app,ConfigurableInformation dsCI,BaseData data,String fileName)
	{
		String result = null;
		
		DataSourceBase dataSource = application.getDataSource(dsCI.dataSource);
		if(dataSource!=null){
			
			switch (data.type) 
			{
				case MosetTreeCategory:
				{
					result = dataSource.getUrl();
					result +="components/com_mtree/img/cats/o/" + fileName;
					break;
				}
			}
		}
		
		if(result!=null)
		{
			String basePath = StringUtils.getBaseUrl(getRequest());

			DownloadTask task=new DownloadTask();
			task.remoteFilepath = result;
			
			
			ApplicationManager appManager = getApplicationManager(app.getApplicationIdentifier());
			String dstPath =  appManager.getAppAssetsLocal(app.getUserIdentifier(), app.getApplicationIdentifier())  + "tmp/";
			
			//String dstLocalPath=oriPath+  "/" + dstFilename;
			File dstPathF = new File(dstPath);
			if(!dstPathF.exists()){
				dstPathF.mkdirs();
			}
			task.localFilepath =dstPath + StringUtils.filename(result) + "." + StringUtils.extension(result);
			
			DownloadManager.downloadFileInternalExt(task); 
			copyToAppWebRoot(appManager, app, task.localFilepath,"Assets/tmp/");
			
			
			result = basePath+appManager.getAppAssetsPrefixComponent(app)  +"tmp/"  + StringUtils.filename(result) + "." + StringUtils.extension(result);
			
			/*
			String imageServletUrl = basePath + "/servlets/ImageScaleIcon?";
			imageServletUrl+="appId="+app.getApplicationIdentifier();
			imageServletUrl+="&uuid="+app.getUserIdentifier();
			imageServletUrl+="&src="+result;
			
			System.out.println(" image servlet url  : " + imageServletUrl);
			
			
			BufferedInputStream in = null;
			try {
				in = new java.io.BufferedInputStream(new java.net.URL(imageServletUrl).openStream());
			} catch (MalformedURLException e) {

				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			result = basePath + "/downloads/"+fileName;
			*/
			
			
			
			//request.getSession().setAttribute("baseUrl",StringUtils.getBaseUrl(request));
	    	//System.setProperty("baseUrl", StringUtils.getBaseUrl(request));
		}
	
		return result;
	}
	@SMDMethod
	public String findIconFromContentSelection(String appIdentfier,String uuid,String itemJSON)
	{
		
		String result =  null;
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		
		if(application!=null){

			ConfigurableInformation dsCi = application.getItemFromJSON(itemJSON);
			BaseData data=application.getDataByContentSourceCI(appManager,dsCi);
			if(data !=null)
			{
				ArrayList<String> pics = data.getPictures();
				if(pics!=null && pics.size() > 0 )
				{
					result = resolveRemotePath(application, dsCi, data, pics.get(0));
					if(result!=null){
						//result = result.replace("//", "/");
					}
				}
			}
			System.out.println("found data:   "  + "\n : ");
			
		}
		return result;
	}
	
	@SMDMethod
	public Page createNewPage(String appIdentfier,String uuid,String itemJSON)
	{
		
		if(!SecurityUtil.isValidAppAction(uuid, appIdentfier, getRequest()))
		{
			return null;
		}
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		Page newPage = null;
		if(application!=null){
			application.flushCache=1;
			newPage = application.createNewPage(itemJSON);
			if(newPage!=null){
				
				JSONSerializer serializer = new JSONSerializer();
				String t = serializer.deepSerialize(newPage);
				System.out.println("page added :   "  + "\n : " + t);
				appManager.saveApplication(application);
				return newPage;
			}
		}
		return null;
	}
	
	@SMDMethod
	public Page createNewPageEx(String appIdentfier,String uuid,String itemJSON,String newTitle,String dataRef,int contentSourceType,String dataSourceUID)
	{
		if(!SecurityUtil.isValidAppAction(uuid, appIdentfier, getRequest()))
		{
			return null;
		}
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		Page newPage = null;
		if(application!=null){
			application.flushCache=1;
			newPage = application.createNewPageEx(itemJSON,newTitle,dataRef,contentSourceType,dataSourceUID);
			if(newPage!=null){
				
				JSONSerializer serializer = new JSONSerializer();
				String t = serializer.deepSerialize(newPage);
				System.out.println("page added :   "  + "\n : " + t);
				appManager.saveApplication(application);
				return newPage;
			}
		}
		return null;
	}
	
	@SMDMethod
	public Page itemDeleted(String appIdentfier,String uuid,String itemJSON)
	{
		

		if(!SecurityUtil.isValidAppAction(uuid, appIdentfier, getRequest()))
		{
			return null;
		}
		
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		if(application!=null){
			application.flushCache=1;
			application.itemDeleted(itemJSON);
			appManager.saveApplication(application);
		}
		
		return null;
	}
	
	
	
	public String smd() {
        return Action.SUCCESS;
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the applicationIdentifier
	 */
	public String getApplicationIdentifier() {
		return applicationIdentifier;
	}

	/**
	 * @param applicationIdentifier the applicationIdentifier to set
	 */
	public void setApplicationIdentifier(String applicationIdentifier) {
		this.applicationIdentifier = applicationIdentifier;
	}

	/**
	 * @return the appManager
	 */
	public ApplicationManager getAppManager() {
		return appManager;
	}

	/**
	 * @param appManager the appManager to set
	 */
	public void setAppManager(ApplicationManager appManager) {
		this.appManager = appManager;
	}

	public HttpSession getSession()
	{
		return ((HttpServletRequest) ActionContext.getContext().get( StrutsStatics.HTTP_REQUEST)).getSession();
	}
	
	public HttpServletRequest getRequest()
	{
		return ((HttpServletRequest) ActionContext.getContext().get( StrutsStatics.HTTP_REQUEST));
	}
	



	/**
	 * @return the error
	 */
	public CMError getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(CMError error) {
		this.error = error;
	}

	
	public void setServletResponse(HttpServletResponse arg0) {
		
		servletResponse = arg0;
		
	}

	public void setServletRequest(HttpServletRequest arg0) {
			
		servletRequest = arg0;
		
	}

	/**
	 * @return the servletRequest
	 */
	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	/**
	 * @return the servletResponse
	 */
	public HttpServletResponse getServletResponse() {
		return servletResponse;
	}




	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the archiveType
	 */
	public String getArchiveType() {
		return archiveType;
	}

	/**
	 * @param archiveType the archiveType to set
	 */
	public void setArchiveType(String archiveType) {
		this.archiveType = archiveType;
	}
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		//setSession(arg0);
	}
}
