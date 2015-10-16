package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmedia.types.AppDataAll;
import pmedia.utils.ApplicationContentTools;
import pmedia.utils.JSONTools;
import pmedia.utils.StringUtils;
import xappconnect.manager.XAppConnectManager;
import xappconnect.types.CustomType;
import cmx.manager.GTrackerManager;
import cmx.tools.DBConnectionChecker;
import cmx.tools.LiferayContentTools;
import cmx.tools.StyleTreeFactory;
import cmx.types.Application;
import cmx.types.DataSourceBase;
import cmx.types.ServiceSettings;
import cmx.types.StyleTree;
import cmx.types.XCDataSource;
import cmx.types.XCDatasourceInfo;

import com.liferay.portal.model.User;

import flexjson.JSONSerializer;
import cmx.types.HTMLTemplateManager;
import cmx.types.HTMLTemplate;



public class AppClientServlet extends CMBaseServlet
{
	
	public void updateApp(Application jspapp){
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
							
							Boolean hasJSONP = DBConnectionChecker.hasJSONP(xds);
							if(hasJSONP){
								XCDatasourceInfo info = new XCDatasourceInfo();
								info.url=siteUrl;
								info.hasJSONP=true;
								xcDataSourceInfos.add(info);
							}
						}
						
						/*
						String mobileAppUrl= "";
						mobileAppUrl = "<br/><a href=\"" + siteUrl + "\">" + siteUrl +"</a>";
						*/
					}	
				}
			}
			
			
			if(xcDataSourceInfos!=null)
			{
				//System.out.println("###### include custom types : " + pTypes.size());
				jspapp.setXcDataSourceInfos(xcDataSourceInfos);
			}
		}
	}
	public void getCacheManifest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		getCMObjects(request, response, false);
		String manifest = ApplicationContentTools.generateCacheManifest(uuid, appIdentifier, null,rtConfig);
		if(manifest==null)
		{
			return;
		}
		String contentType="text/cache-manifest";
    	contentType += ";charset=UTF-8";
    	response.setContentType(contentType);
    	response.setHeader("Content-Disposition", "attachment; filename=\"" + "cache.manifest" + "\"");
    	response.getWriter().write(manifest);
    }
	
	/***
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getAppBundle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		getCMObjects(request, response, false);
		
		if(application!=null && application.flushCache==1){
			dropCache(null);
			application.flushCache=0;
		}
		if(preventCache){
			dropCache(null);
			if(application!=null){
				application.flushCache=0;
			}
		}else {
	    	String cachedResponse = getCachedResponse(null);
	    	if(cachedResponse!=null){
	    		sendCachedOutput(request, response, cachedResponse);
	    		//System.out.println("is cached");
	    		return;
	    	}
    	}
		
		
		if(scope==null){
			scope="%XASWEB%/ctypes";
		}
		
		AppDataAll appData = new AppDataAll();
		
		
		/***
		 * CustomTypes
		 */
		appData.customTypes =  XAppConnectManager.getTypes(rtConfig, "IPHONE_NATIVE", uuid,appIdentifier, scope);
		
		/***
		 * the application
		 */
		appData.app = application;
		appManager.updateRemoteResources(application);
		
		
		//updateApp(application);
		
		
		
		/***
		 * serviceSettings
		 */
		ServiceSettings settings = new ServiceSettings();
		settings.setAjaxplorerRootUrl(System.getProperty("AjaxplorerRootUrl"));
		settings.setFeatherEditorApiKey(System.getProperty("FeatherEditorApiKey"));
		settings.setWebPath(System.getProperty("WebPath"));
		settings.setImageScaleServlet(System.getProperty("imageProcessorUrl"));
		
		appData.serviceSettings=settings;
		
		
		
		
		/***
		 * StyleTree
		 */
		
		String uuid = request.getParameter("uuid");
		String appIdentfier = request.getParameter("appIdentifier");
		String platform = request.getParameter("platform");
		 
		if(platform==null){
			platform="IPHONE_NATIVE";
		}
		
		StyleTree a = StyleTreeFactory.createStyleTree(uuid, appIdentfier, platform,"", "", "");
		
		appData.styles=a;
		
		
		/***
		 * HTML templates
		 */
		if(getIsHybrid()){
			
			ArrayList< HTMLTemplate>_htmlTemplates=HTMLTemplateManager.getTemplates(uuid, appIdentfier, null);
			if(_htmlTemplates!=null && _htmlTemplates.size()>0){					
				
					JSONSerializer appResD = new JSONSerializer();
					//appResD.prettyPrint(true);
					String _htmlTemplatesStr = appResD.deepSerialize(_htmlTemplates);
					appData.htmlTemplates=_htmlTemplatesStr; 
				
			}
		}
		
		
		//String styleStr = serializer.deepSerialize(a);
		String prefix = System.getProperty("WebPath") +"CMAC/"+uuid+"/apps/";
		
		JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
    	JSONTools.optimizedAppBundle(serializer);
    	
    	String resultStr = serializer.deepSerialize(appData);
    	
    	resultStr = resultStr.replace("url('","url('" + prefix);
		if(resultStr.contains( uuid + "/apps//SharedAssets/"))
		{
			resultStr = resultStr.replace(uuid + "/apps//SharedAssets" , "shared/SharedAssets");
		}
		
		if(resultStr!=null){
			sendOutput(request, response,resultStr);
			return;
		}
	}
	public void getCTypes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		getCMObjects(request, response, false);
		
		if(scope==null){
			scope="%XASWEB%/ctypes";
		}
		
		ArrayList<CustomType> result =  XAppConnectManager.getTypes(rtConfig, "IPHONE_NATIVE", uuid,appIdentifier, scope);
		
		if(result!=null && result.size()>0){
			JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
			JSONTools.optimizedCustomTypes(serializer);
			String resultStr = serializer.deepSerialize(result);
			
			if(resultStr!=null){
				sendOutput(request, response,resultStr);
				return;
			}
		}
		sendOutput(request, response,"");
		/*
		String manifest = ApplicationContentTools.generateCacheManifest(uuid, appIdentifier, null);
		if(manifest==null)
		{
			return;
		}
		String contentType="text/cache-manifest";
    	contentType += ";charset=UTF-8";
    	response.setContentType(contentType);
    	response.setHeader("Content-Disposition", "attachment; filename=\"" + "cache.manifest" + "\"");
    	response.getWriter().write(manifest);
    	*/
    }
	
    private static final long serialVersionUID = 1L;
    protected void doContent(PrintWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
    }
    String getContentType(String fileName) {
        String extension[] = { // File Extensions
            "txt", //0 - plain text
            "htm", //1 - hypertext
            "jpg", //2 - JPEG image
            "png", //2 - JPEG image
            "gif", //3 - gif image
            "pdf", //4 - adobe pdf
            "doc", //5 - Microsoft Word
            "docx",
        }; // you can add more
        String mimeType[] = { // mime types
            "text/plain", //0 - plain text
            "text/html", //1 - hypertext
            "image/jpg", //2 - image
            "image/jpg", //2 - image
            "image/gif", //3 - image
            "application/pdf", //4 - Adobe pdf
            "application/msword", //5 - Microsoft Word
            "application/msword", //5 - Microsoft Word
        }, // you can add more
                contentType = "text/html";    // default type
        // dot + file extension
        int dotPosition = fileName.lastIndexOf('.');
        // get file extension
        String fileExtension =
                fileName.substring(dotPosition + 1);
        // match mime type to extension
        for (int index = 0; index < mimeType.length; index++) {
            if (fileExtension.equalsIgnoreCase( extension[index])) {
                contentType = mimeType[index];
                break;
            }
        }
        return contentType;
    }

    public void getStyles(
    		HttpServletRequest request, HttpServletResponse response)
	{
    	
    	try {
			getCMObjects(request, response, false);
		} catch (ServletException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	String uuid = request.getParameter("uuid");
		String appIdentfier = request.getParameter("appIdentifier");
		String platform = request.getParameter("platform");
		 
		
		StyleTree a = StyleTreeFactory.createStyleTree(uuid, appIdentfier, platform,"", "", "");
		JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
		String result = serializer.deepSerialize(a);
		String prefix = System.getProperty("WebPath") +"CMAC/"+uuid+"/apps/";
		result = result.replace("url('","url('" + prefix);
		/*String suffix=System.getProperty("WebPath") +"CMAC/"+uuid+"/apps//SharedAssets/";
		prefix = System.getProperty("WebPath") +"CMAC/shared/SharedAssets/";
		result = result.replace("url('"+suffix,"url('" + prefix);
		result = result.replace("');", "') !important;");
		*/
		if(result.contains( uuid + "/apps//SharedAssets/"))
		{
			result = result.replace(uuid + "/apps//SharedAssets" , "shared/SharedAssets");
		}
		//sendOutput(result);
		if(callback==null)
		{
			//sendOutput(request, response,serializer.deepSerialize(result));
			
			response.setHeader("Content-Type", "application/json; charset=UTF-8");
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			sendOutput(request,response,result);
		}
	}
    public void isValidC9Session(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String login=(String)request.getParameter("user");
    	if(login==null)
    	{
    		return;
    	}
    	
    	String password=(String)request.getParameter("pwd");
    	if(password==null)
    	{
    		return;
    	}

    	User user = LiferayContentTools.authUser(login, password, request);
    	if(user==null){
    		response.getWriter().write("NoSuchUser");
    	}else{
    		response.getWriter().write("OK");
    	}
    	
    }
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String login=(String)request.getParameter("user");
    	if(login==null)
    	{
    		return;
    	}
    	
    	String password=(String)request.getParameter("pwd");
    	if(password==null)
    	{
    		return;
    	}

    	User user = LiferayContentTools.authUser(login, password, request);
    	if(user==null){
    		response.getWriter().write("NoSuchUser");
    	}else{
    		response.getWriter().write("OK");
    	}
    	
    }
    public void getApplications(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String login=(String)request.getParameter("user");
    	if(login==null)
    	{
    		return;
    	}
    	
    	String password=(String)request.getParameter("pwd");
    	if(password==null)
    	{
    		return;
    	}
    	
    	String version=(String)request.getParameter("version");
    	if(version==null)
    	{
    		return;
    	}
    	
    	User user = LiferayContentTools.authUser(login, password, request);
    	if(user==null){
    		response.getWriter().write("No Such User");
    		return;
    	}
    	//Application demoApp = appManager.getApplication(System.getProperty("demoUser"), "Demo", false);
    	
    	ArrayList<Application>apps = appManager.getUserApplications(user.getUuid());
    	if(apps!=null)
    	{
    		/*
    		if(demoApp!=null && !appManager.containsApp(apps, demoApp.applicationIdentifier))
    		{
    			apps.add(demoApp);
    		}
    		*/
    		JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
			response.setHeader("Content-Type", "text/text; charset=UTF-8");
			response.getWriter().write(serializer.deepSerialize(apps));
    	}
    	apps.clear();
    	
    }
    public void getArchive(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    		//mc007ibi.dyndns.org:8080/CMLiferay-portlet/client?action=getArchive&uuid=11166763-e89c-44ba-aba7-4e9f4fdf97a9&appIdentifier=ibizamedia5
			String servletPath = System.getProperty("ServletPath");
			if(application==null){
				return;
			}
			//application  = appManager.getApplication(appIdentifier,uuid);
			String fileName = appManager.compressApplication(application,servletPath);
			System.out.println("compressing application : " + servletPath);
			String baseUrl  = StringUtils.getBaseUrl(request);
			baseUrl+="/" +application.getUserIdentifier()+"/" + fileName;
			String archiveRedirectionUrl = baseUrl;
			String filePathLocation = servletPath+uuid + "/"+fileName;
			String contentType = getContentType(fileName);

			//response.sendRedirect(archiveRedirectionUrl);
			response.sendRedirect(System.getProperty("WebPath")+"CMACArchives/"+fileName+"?time=" + new Date().getTime());
			
			
			/*
			File file = new File(filePathLocation);
	        response.setContentType(contentType);
	        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + UUID.randomUUID().toString() + ".zip");
	        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
	        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
	        //response.setDateHeader("Expires", -1); // Proxies.
	        
	        
	        
	        int length = (int) file.length();
	        response.setContentLength(length);
	        
	        System.out.println("redirection url : " + archiveRedirectionUrl + " with length : " + length);
	        
	        byte[] bytes = new byte[length];
	 
	        FileInputStream fin = new FileInputStream(file);
	 
	        fin.read(bytes);
	 
	        ServletOutputStream os = response.getOutputStream();
	        os.write(bytes);
	        os.flush();
	        */
			 
			
    }
    public void getApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
   	
/*
    	WURFLHolder wurfl = (WURFLHolder) getServletContext().getAttribute(WURFLHolder.class.getName());
		
		WURFLManager manager = wurfl.getWURFLManager();

		Device device = manager.getDeviceForRequest(request);
		
		*/
    	//String dataSource = request.getParameter("dataSource");
    	//String dataRef= request.getParameter("ref");
    	



		/*
    	SQLDataSource ds = app.getDataSource(dataSource);
		String dbPath = appManager.getUserAppPath(app.getUserIdentifier(), app.getApplicationIdentifier());
		if(dataSource!=null)
		{
			dbPath+=ds.getHost()+"/" + ds.getDatabase() + "/";
			dbPath+="categories.xml";
		}
		*/
		if(application!=null)
		{
			
			GTrackerManager.trackXAPPEvent(GTrackerManager.openGDoc,uuid+"::"+appIdentifier+"::"+ref);
						JSONSerializer serializer = new JSONSerializer();
			//sendOutput(serializer.deepSerialize(application));
			if(callback==null){
				
				sendOutput(request, response,serializer.deepSerialize(application));
				/*
				response.setHeader("Content-Type", "text/text; charset=UTF-8");
				
				try {
					response.getWriter().write(serializer.deepSerialize(application));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
			}else{
				sendOutput(request, response,serializer.deepSerialize(application));
			}
		}
    }
    public void getServiceSettings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	ServiceSettings settings = new ServiceSettings();
		settings.setAjaxplorerRootUrl(System.getProperty("AjaxplorerRootUrl"));
		settings.setFeatherEditorApiKey(System.getProperty("FeatherEditorApiKey"));
		settings.setWebPath(System.getProperty("WebPath"));
		settings.setImageScaleServlet(System.getProperty("imageProcessorUrl"));
		JSONSerializer serializer = new JSONSerializer();
		if(callback==null){
			response.setHeader("Content-Type", "text/text; charset=UTF-8");
			
			try {
				response.getWriter().write(serializer.deepSerialize(settings));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			sendOutput(request,response,serializer.deepSerialize(settings));
		}
	}
    public void purgeApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	if(application!=null){
    		appManager.purgeApplication(application, null);
    	}
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	getCMObjects(request, response, true);
    	
    	String action= request.getParameter("action");
    	
    	System.out.println((String) request.getAttribute("javax.servlet.forward.request_uri"));
    	
    	
    	if(action==null){
    		return404Page();
    		return;
    	}
    	if(action.equals("getApplication"))
    	{
    		getApplication(request, response);
    	
    	}
    	
    	if(action.equals("getAppBundle"))
    	{
    		getAppBundle(request, response);
    	}
    	
    	if(action.equals("login"))
    	{
    		login(request, response);
    		
    		
    	}
    	if(action.equals("getApplications"))
    	{
    		getApplications(request, response);
    	}
    	
    	if(action.equals("getCustomTypes"))
    	{
    		getCTypes(request, response);
    	}
    	
    	if(action.equals("getServiceSettings"))
    	{
    		getServiceSettings(request, response);
    	}
    	if(action.equals("getArchive"))
    	{
    		getArchive(request, response);
    	}
    	
    	if(action.equals("purge"))
    	{
    		purgeApplication(request, response);
    	}
    	
    	if(action.equals("getStyles"))
    	{
    		getStyles(request, response);
    	}
    	
    	if(action.equals("getManifest"))
    	{
    		getCacheManifest(request, response);
    	}
    	
    	if(action.equals("isValidC9Session"))
    	{
    		isValidC9Session(request, response);
    	}
    	
    	
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
