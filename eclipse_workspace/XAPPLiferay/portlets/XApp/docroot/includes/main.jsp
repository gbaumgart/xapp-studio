<%@page import="org.apache.commons.io.FileUtils"%>
<%@page import="jsontype.DojoPackage"%>
<%@page import="cmx.manager.JSPluginManager"%>
<%@ page import="java.io.*"%>
<%@ page import="java.lang.*"%>
<%@page import="cmx.types.Resource"%>
<%@page import="pmedia.utils.StringUtils"%>
<%@page import="flexjson.JSONDeserializer"%>
<%@page import="cmx.types.Resources"%>
<%@page import="java.io.File"%>
<%@page import="cmx.types.FolderKeys"%>
<%@page import="cmx.FolderKeyResolverUtil"%>
<%@page import="cmx.types.Application"%>
<%@page import="cmx.types.ApplicationManager"%>
<%@page import="cmx.tools.ResourceUtil"%>
<%@page import="flexjson.JSONSerializer"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
	
	String _sysAppName = (String)request.getSession().getAttribute("systemAppName");
	String _dojoMobileTheme = (String)request.getSession().getAttribute("dojoMobileTheme");
	
	String xappBaseDirectory="xasweb/lib/";
	String dojoDirectory="dojo/";
	String dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";
	
	
	String bootLoader=xappBaseDirectory+ _sysAppName + "/run.js";
	String RTConfig = (String)request.getSession().getAttribute("runTimeConfiguration");
	
	String cacheBust = (String)request.getSession().getAttribute("cacheBust");
	if(cacheBust!=null)
	{
		cacheBust="cacheBust:true,";	
	}else
	{
		cacheBust="";
	}
	
	String dstLang = "en";
	Locale rLocale = request.getLocale();
	if(rLocale!=null)
	{
		dstLang = rLocale.getLanguage();
	}
	
	if(_sysAppName!=null && _sysAppName.equals("xas")){
		dstLang="en";
	}
	
	String dojoConfig=null;
	
	String packagePaths="";
	String _path = request.getContextPath();
	String _basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";
	String _xaswebRootUrl = System.getProperty("xaswebRootUrl");
	String _xaswebBaseUrl = System.getProperty("xaswebBaseUrl");
	if(!_basePath.contains("8080"))
	{
		_basePath=_basePath.replace(":80","");
	}
	
	/***	
		App - Resources
	*/
	String ____appId = (String)session.getAttribute("appId");
	String ____uuid = (String)session.getAttribute("uuid");
	
	packagePaths=",packages:[";
	
	Boolean hasCXAPP=false;
	Application uapp = null;
	if(____uuid!=null && ____appId!=null)
	{
		uapp = ApplicationManager.getInstance().getApplication(____uuid,____appId, false);
		if(uapp!=null)
		{
			String appLocalPath = ApplicationManager.getInstance().getAppDataPath(____uuid, ____appId,null);
			String appResourcesPath = appLocalPath + "resources-" + RTConfig + ".json";
			File _resourceFile =  new File(appResourcesPath);
			if(_resourceFile.exists())
			{
				Resources resources = ResourceUtil.fromPath(appResourcesPath);
				if(resources!=null){
					resources = ResourceUtil.resolve(resources, ____uuid,____appId, null);
					//System.out.println("resolving dojo config : " + ResourceUtil.toHTML(resources, null, null, null,"CXAPP"));
					Resource cxappResource = ResourceUtil.getResourceByType(resources, "CXAPP");
					if(cxappResource !=null && cxappResource.enabled)
					{
						String cxAppBootResolved =ResourceUtil.resolveConstantsAbsolute(cxappResource.urlOri, ____uuid, ____appId);
						File f = new File(cxAppBootResolved);
						if(f.exists() && f.isDirectory())
						{
							packagePaths+="{name:"		+"'"	+ cxappResource.name+"',";
							packagePaths+="location:"	+"'"	+ cxappResource.url +"'}";
							hasCXAPP=true;
							
						}
					}
				}
			}
		}
	}
	
	
	
	ArrayList<DojoPackage> dPackages = JSPluginManager.getDojoPluginPackages(System.getProperty("xappPluginRoot"), RTConfig, _sysAppName);
	

	/***
		Find user app plugins
	*/
	String userAppPluginsPath = ResourceUtil.resolveConstantsAbsolute("%XAPP%/plugins/", ____uuid, ____appId);
	if(userAppPluginsPath!=null)
	{
	
		File userAppPluginPathObject = new File(userAppPluginsPath);
		if(userAppPluginPathObject.exists() && userAppPluginPathObject.isDirectory())
		{
			//System.out.println("user plugin package path  " +  userAppPluginsPath);
			ArrayList<DojoPackage> userAppPluginPackages = JSPluginManager.getDojoPluginPackages(userAppPluginsPath, RTConfig, _sysAppName);
			if(userAppPluginPackages !=null && userAppPluginPackages.size()>0 )
			{
				//System.out.println("have user app plugin packages : " + userAppPluginPackages.size());
				dPackages.addAll(userAppPluginPackages);
			}					
		}
	}
		
	
	if(dPackages!=null && dPackages.size()>0)
	{
		//now iterate over the packages
		for(int i = 0 ; i<dPackages.size(); i++ ){
			
			DojoPackage pkg =dPackages.get(i);
			if(pkg.enabled && pkg.location!=null && pkg.name !=null)
			{
				String pLoc=ResourceUtil.resolveConstants(pkg.location, ____uuid, ____appId);
				
				String pLocAbsolute = ResourceUtil.resolveConstantsAbsolute(pkg.location, ____uuid, ____appId);
				Boolean loadPluginRelease = false;
				String _rt = "" +  RTConfig;
				if(loadPluginRelease || RTConfig.equals("release"))
				{
				    _rt="release";									
					String pluginReleasePath = pLocAbsolute + "/" + _sysAppName +"/"+ _rt + "/";
					File releaseDir = new File(pluginReleasePath);
					if(releaseDir.exists())
					{
						pLoc+= _sysAppName  + "/" + _rt + "/";
					}					
				}
				//System.out.println("Plugin abs location " + pLocAbsolute);
				if(i==0 && hasCXAPP)
				{
					packagePaths+=",";									
				}
				if(i>0)
				{
					packagePaths+=",";
				}
				packagePaths+="{name:"		+"'"	+ pkg.name+"',";
				packagePaths+="location:"	+"'"	+ pLoc+"'}";
			}
		}	
	}
	//close packagePaths
	packagePaths+="]";
	
	//System.out.println("package paths : \n" + packagePaths);
	
	String dojoFilePath = System.getProperty("xasWebRoot") +"/lib/dojo/dojo.js"; 
	
	
	/***
	*	Build Dojo Config
	*/
	
	
	//cacheBust:true,
	if(_dojoMobileTheme!=null)
	{
		//mblForceBookmarkable: true
		dojoConfig=cacheBust+ "parseOnLoad:false,baseUrl:'xasweb/lib',tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:false,async:true,mblThemeFiles:['@theme',['"  + _dojoMobileTheme + "','gallery']],has:{'dojo-undef-api': true,'dojo-firebug': false,'clicks-prevented':false},locale:'" +dstLang +"'";
		if(packagePaths!=null && packagePaths.length()>0){
			dojoConfig+=packagePaths;
		}
	}else{
		dojoConfig=cacheBust+ "parseOnLoad:false,baseUrl:'xasweb/lib',tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:false,async:true,has:{'dojo-undef-api': true,'dojo-firebug': false,'clicks-prevented':false},locale:'" +dstLang +"'";	
	}
	 
	 
	 
	
	//Adjust paths with run-time-configuration 
	if(RTConfig!=null)
	{
		if(RTConfig.equals("release"))
		{
			//release doesn't need a boot loader since all is baking into one layer!
			bootLoader="xasweb/" + _sysAppName +"/"+_sysAppName +"/run.js";
			
			if(!hasCXAPP){
				//bootLoader=null;
			}
			 
			//bootLoader=null;
			xappBaseDirectory="xasweb/" + _sysAppName +"/";
			
			
			dojoConfig="async: 1, tlmSiblingOfDojo: 0, baseUrl:'" + _sysAppName  + "', deps:['" + _sysAppName +"/run']";
			
			if( _xaswebRootUrl !=null &&  _xaswebBaseUrl!=null && _xaswebRootUrl.length()>0 && _xaswebBaseUrl.length()>0){
				
				
				if(_dojoMobileTheme!=null){
					dojoConfig=cacheBust+  "waitSeconds: 10,parseOnLoad:false,baseUrl:'" +  _xaswebBaseUrl +_sysAppName +"',deps:['" +_sysAppName +"/run'],tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['"  + _dojoMobileTheme + "','gallery']],has:{'dojo-undef-api': true},locale: '" +dstLang +"'";
					if(packagePaths!=null && packagePaths.length()>0)
					{
						dojoConfig+=packagePaths;
					}
				}else{
					dojoConfig=cacheBust+ "waitSeconds: 10,parseOnLoad:false,baseUrl:'"+  _xaswebBaseUrl + _sysAppName + "',deps:['" +_sysAppName+ "/run'],tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:true,async:true,has:{'dojo-undef-api': true},locale: '" +dstLang +"'";	
				}
				
				//dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";
				//xaswebRootUrl=http://192.168.1.37/luiz/xasweb/
				//xaswebBaseUrl=http://192.168.1.37/luiz/xasweb/
				dojoPath=_xaswebRootUrl+ _sysAppName + '/' +  dojoDirectory+"dojo.js";
				bootLoader=_xaswebRootUrl  + _sysAppName +"/"+_sysAppName +"/run.js";
				
				
				
				
			}else{
			
				if(_dojoMobileTheme!=null){
					dojoConfig=cacheBust+  "waitSeconds: 10,parseOnLoad:false,baseUrl:'" +  "xasweb/" +_sysAppName +"',deps:['" +_sysAppName +"/run'],tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['"  + _dojoMobileTheme + "','gallery']],has:{'dojo-undef-api': true},locale: '" +dstLang +"'";
					if(packagePaths!=null && packagePaths.length()>0)
					{
						dojoConfig+=packagePaths;
					}
				}else{
					dojoConfig=cacheBust+ "waitSeconds: 10,parseOnLoad:false,baseUrl:'"+  "xasweb/" + _sysAppName + "',deps:['" +_sysAppName+ "/run'],tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:true,async:true,has:{'dojo-undef-api': true},locale: '" +dstLang +"'";	
				}
				
				dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";
				
				
				
				dojoFilePath = System.getProperty("xasWebRoot") +"/"  + _sysAppName +  "/dojo/dojo.js";
				File dojoFile=new File(dojoFilePath);
				if(dojoFile.exists()){
					
					long time = dojoFile.lastModified();
					dojoPath+="?time="+time;
					session.setAttribute("version", ""+time);
				}
			}
		}

		if(RTConfig.equals("debug"))
		{
			session.setAttribute("version", ""+ new Date().getTime());
			
			if( _xaswebRootUrl !=null &&  _xaswebBaseUrl!=null && _xaswebRootUrl.length()>0 && _xaswebBaseUrl.length()>0){
				if(_dojoMobileTheme!=null){
					
					//mblForceBookmarkable: true
				  dojoConfig=cacheBust+ "parseOnLoad:false,baseUrl:'" + _xaswebBaseUrl +"lib',tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:false,async:true,mblThemeFiles:['@theme',['"  + _dojoMobileTheme + "','gallery']],has:{'dojo-undef-api': true,'dojo-firebug': false,'clicks-prevented':false},locale:'" +dstLang +"'";
					if(packagePaths!=null && packagePaths.length()>0){
						dojoConfig+=packagePaths;
					}
//					dojoConfig=cacheBust+  "waitSeconds: 10,parseOnLoad:false,baseUrl:'" +  _xaswebBaseUrl +_sysAppName +"',deps:['" +_sysAppName +"/run'],tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['"  + _dojoMobileTheme + "','gallery']],has:{'dojo-undef-api': true},locale: '" +dstLang +"'";
					if(packagePaths!=null && packagePaths.length()>0)
					{
						//dojoConfig+=packagePaths;
					}
				}else{
					//dojoConfig=cacheBust+ "waitSeconds: 10,parseOnLoad:false,baseUrl:'"+  _xaswebBaseUrl + _sysAppName + "',deps:['" +_sysAppName+ "/run'],tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:true,async:true,has:{'dojo-undef-api': true},locale: '" +dstLang +"'";	
				}
				
				//dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";
				//xaswebRootUrl=http://192.168.1.37/luiz/xasweb/
				//xaswebBaseUrl=http://192.168.1.37/luiz/xasweb/
				dojoPath=_xaswebRootUrl+"lib/" +  dojoDirectory+"dojo.js";
				bootLoader=_xaswebRootUrl +"lib/"  + _sysAppName +"/run.js";
			}
			
		}
		
		if(RTConfig.equals("releaseDebug"))
		{
			//release doesn't need a boot loader since all is baking into one layer!
			xappBaseDirectory="xasweb/" + _sysAppName +"/";
			
			if(_dojoMobileTheme!=null){
				dojoConfig=cacheBust+ "parseOnLoad:false,baseUrl:'" +  "xasweb/" +_sysAppName +"',deps:['" +_sysAppName+ "/run'],tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['"  + _dojoMobileTheme + "','gallery']],has:{'dojo-undef-api': true},locale: '" +dstLang +"'";
			}else{
				dojoConfig=cacheBust+ "parseOnLoad:false,baseUrl:'"+  "xasweb/" + _sysAppName + "',deps:['" +_sysAppName+ "/run'],tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:true,async:true,has:{'dojo-undef-api': true},locale: '" +dstLang +"'";	
			}
			dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js.uncompressed.js";
		}
	}
	
	
	if( _xaswebRootUrl !=null &&  _xaswebBaseUrl!=null && _xaswebRootUrl.length()>0 && _xaswebBaseUrl.length()>0){
		request.getSession().setAttribute("dojoPath",  dojoPath);	
	}else{	
		request.getSession().setAttribute("dojoPath",  _basePath +""+  dojoPath);
	}
	
	request.getSession().setAttribute("dojoConfig", dojoConfig);
	
	if(bootLoader!=null)
	{
		if( _xaswebRootUrl !=null &&  _xaswebBaseUrl!=null && _xaswebRootUrl.length()>0 && _xaswebBaseUrl.length()>0){
			request.getSession().setAttribute("bootLoader",bootLoader);
		}else{
			request.getSession().setAttribute("bootLoader",_basePath +""+ bootLoader);
		}
		
		
	}
	request.getSession().setAttribute("xappBaseDirectory", xappBaseDirectory);
	
%>
<%@ include file="../includes/logging.jsp" %>
