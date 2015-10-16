package cmb.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jsontype.DojoPackage;

import pmedia.types.ApplicationMetaData;
import pmedia.types.ApplicationMetaDataKeys;
import pmedia.utils.CITools;
import pmedia.utils.StringUtils;
import xappconnect.Utils.CustomTypeUtils;
import xappconnect.types.CustomType;
import cmb.types.BuildOptions;
import cmx.FolderKeyResolverUtil;
import cmx.manager.JSPluginManager;
import cmx.tools.ResourceUtil;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ConfigurableInformation;
import cmx.types.FolderKeys;
import cmx.types.Resource;
import cmx.types.Resources;
import flexjson.JSONSerializer;

public class HybridIndexBuilder 
{
	
	public static String getBootLoader(String systemAppName,String theme,String lang,String uuid,String appId,String platform,String type,BuildOptions options)
	{
		return null;
	}
	public static String getDojoPath(String systemAppName,String theme,String lang,String uuid,String appId,String platform,String type,BuildOptions options)
	{
		return null;
	}
	public static String createDojoConfig(String systemAppName,String theme,String lang,String uuid,String appId,String platform,String type,BuildOptions options)
	{
		
		HashMap<String, String> dynamicTokens=new HashMap<String, String>();
		if(options.runTimeConfiguration.equals("debug"))
		{
			dynamicTokens=getDynamicTokensDebug(uuid,appId,options.runTimeConfiguration,options);
		}else if(options.runTimeConfiguration.equals("release"))
		{
			dynamicTokens=getDynamicTokensRelease(uuid,appId,options.runTimeConfiguration,options);
		}
		
		String dojoConfig="";
		Boolean hasCXAPP=false;
		String packagePaths=",packages:[{";
		if(uuid!=null && appId!=null)
		{
			Application uapp = ApplicationManager.getInstance().getApplication(uuid,appId, false);
			if(uapp!=null)
			{
				String appLocalPath = ApplicationManager.getInstance().getAppDataPath(uuid, appId,null);
				String appResourcesPath = appLocalPath + "resources-" + options.runTimeConfiguration + ".json";
				File _resourceFile =  new File(appResourcesPath);
				if(_resourceFile.exists())
				{
					Resources resources = ResourceUtil.fromPath(appResourcesPath);
					if(resources!=null){
						resources = ResourceUtil.resolveEx(resources, uuid,appId, null,dynamicTokens);
						Resource cxappResource = ResourceUtil.getResourceByType(resources, "CXAPP");
						if(cxappResource !=null && cxappResource.enabled)
						{
							
							packagePaths+="name:"		+"'"	+ cxappResource.name+"',";
							packagePaths+="location:"	+"'"	+ cxappResource.url +"'";
							hasCXAPP=true;
						}
					}
				}
			}
		}
		
		ArrayList<DojoPackage> dPackages = JSPluginManager.getDojoPluginPackages(System.getProperty("xappPluginRoot"), options.runTimeConfiguration, systemAppName);
		if(dPackages!=null && dPackages.size()>0)
		{
			//now iterate over the packages
			for(int i = 0 ; i<dPackages.size(); i++ ){
				
				DojoPackage pkg =dPackages.get(i);
				if(pkg.enabled && pkg.location!=null && pkg.name !=null)
				{
					String pLoc=ResourceUtil.resolveConstantsEx(pkg.location, uuid, uuid,dynamicTokens);
					if(i==0 && hasCXAPP)
					{
						packagePaths+=",";									
					}
					if(i>0)
					{
						packagePaths+=",";
					}
					packagePaths+="name:"		+"'"	+ pkg.name+"',";
					packagePaths+="location:"	+"'"	+ pLoc+"'";
				}
			}	
		}
		//close packagePaths
		packagePaths+="}]";
		
		String cacheBust = "";
		if(options.runTimeConfiguration.equals("debug"))
		{
			options.chacheBust=true;
		}
		if(options.chacheBust)
		{
			cacheBust="cacheBust:true,";	
		}else
		{
			cacheBust="";
		}
		String dstLang="en";
		//cacheBust:true,
		
		
		/***
		 * Determine Dojo's baseUrl
		 */
		String baseUrl = "";
		if(options.runTimeConfiguration.equals("debug"))
		{
			if(!options.includeSystemJS)
			{
				baseUrl=ResourceUtil.resolveConstantsEx("%XASWEB%/lib/", uuid, appId, dynamicTokens);
			}else{
				baseUrl=ResourceUtil.resolveConstantsEx("lib/", uuid, appId, dynamicTokens);
			}
			
			
		}
		if(options.runTimeConfiguration.equals("release"))
		{
			if(!options.includeSystemJS)
			{
				baseUrl=ResourceUtil.resolveConstantsEx("%XASWEB%/" + systemAppName+"/", uuid, appId, dynamicTokens);
			}else{
				baseUrl=ResourceUtil.resolveConstantsEx("" + systemAppName+"/", uuid, appId, dynamicTokens);
			}
		}
		
		/***
		 * Debug
		 */
		if(theme!=null)
		{
			dojoConfig=cacheBust+ "parseOnLoad:false,baseUrl:'" + baseUrl+ "',tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:false,async:true,mblThemeFiles:['@theme',['"  + theme + "','gallery']],has:{'dojo-undef-api': true,'dojo-firebug': false},locale:'" +lang +"'";
			if(packagePaths!=null && packagePaths.length()>0){
				dojoConfig+=packagePaths;
			}
		}else{
			dojoConfig=cacheBust+ "parseOnLoad:false,baseUrl:'" +baseUrl+"',tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:false,async:true,has:{'dojo-undef-api': true,'dojo-firebug': false},locale:'" +lang +"'";	
		}
		
		
		/***
		 * Release 
		 */
		if(options.runTimeConfiguration.equals("release"))
		{
			dojoConfig=cacheBust+ "parseOnLoad:false,baseUrl:'" + baseUrl+ "',deps:['" +systemAppName+"/run'],tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:false,async:true,mblThemeFiles:['@theme',['"  + theme + "','gallery']],has:{'dojo-undef-api': true,'dojo-firebug': false},locale:'" +lang +"'";
			if(packagePaths!=null && packagePaths.length()>0)
			{
				dojoConfig+=packagePaths;
			}
		}
		
		return dojoConfig;
	}
	public static HashMap<String, String> getDynamicTokensRelease(String uuid,String appId,String rtConfig,BuildOptions options)
	{
		HashMap<String, String> result=new HashMap<String, String>();
		
		String xaswebResolved = System.getProperty("ServletWebPath")+FolderKeyResolverUtil.resolveKey(FolderKeys.CM_XASWEB);
		result.put(FolderKeys.CM_XASWEB, xaswebResolved);
		
		String xappResolved = System.getProperty("ServletWebPath")+FolderKeyResolverUtil.resolveKey(FolderKeys.CM_APP_ROOT,uuid,appId);
		result.put(FolderKeys.CM_APP_ROOT, xappResolved);
		
		return result;
	}
	
	public static HashMap<String, String> getDynamicTokensDebug(String uuid,String appId,String rtConfig,BuildOptions options)
	{
		HashMap<String, String> result=new HashMap<String, String>();
		
		String prefix=System.getProperty("ServletWebPath");
		String xaswebResolved = prefix+FolderKeyResolverUtil.resolveKey(FolderKeys.CM_XASWEB);
		
		result.put(FolderKeys.CM_XASWEB, xaswebResolved);
		
		String xappResolved = prefix+FolderKeyResolverUtil.resolveKey(FolderKeys.CM_APP_ROOT,uuid,appId);
		result.put(FolderKeys.CM_APP_ROOT, xappResolved);
		
		return result;
	}
	public static String createIndexFile(String uuid,String applicationId,String platform,String type,BuildOptions options)
	{
		String templatePath =  System.getProperty("HYBRID_BUILD_ROOT")+"templates/index-"+options.runTimeConfiguration+".html";
		String html=null ; 
		try {
			html = StringUtils.readFileAsString(templatePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String _systemAppName = "xapp";
		
		options.dataHost = System.getProperty("appCMMaster");
		
		//build the resource config path :
		
		String sysAppLocalPath = FolderKeyResolverUtil.getAbsolutePrefix(FolderKeys.CM_LIB_ROOT) + _systemAppName +"/";
		
		ApplicationManager appManager = ApplicationManager.getInstance();
	    Application app=appManager.getApplication(uuid, applicationId, false);
		
		String rtConfig = options.runTimeConfiguration;
		
		String sysAppResourcesPath = sysAppLocalPath + "resources-" + rtConfig + ".json";
		
		HashMap<String, String> dynamicTokens=new HashMap<String, String>();
		
		if(options.runTimeConfiguration.equals("debug"))
		{
			dynamicTokens=getDynamicTokensDebug(uuid,applicationId,rtConfig,options);
		}else if(options.runTimeConfiguration.equals("release"))
		{
			dynamicTokens=getDynamicTokensRelease(uuid,applicationId,rtConfig,options);
		}
		
		
		File resourceFile =  new File(sysAppResourcesPath);
		if(resourceFile.exists())
		{
			//System.out.println("system app : "  + _systemAppName + " root + " + sysAppLocalPath);
			Resources resources = ResourceUtil.fromPath(sysAppResourcesPath);
			
			
			
			//resolve resources
			resources = ResourceUtil.resolveEx(resources, null, null, _systemAppName,dynamicTokens);
			
			String headResources = null;
			
			/***
			 * Head CSS Resources
			 */
			if(!options.includeSystemCSS)
			{
				headResources = ResourceUtil.toHTML(resources, null, null, null,"head",rtConfig);
			}else
			{
				if(options.runTimeConfiguration.equals("release") || options.runTimeConfiguration.equals("debug"))
				{
					Resources cssResources = ResourceUtil.fromPath(sysAppResourcesPath);
					HashMap<String, String> dynaTokes=new HashMap<String, String>();
					String xaswebResolved = "./";
					dynaTokes.put(FolderKeys.CM_XASWEB, xaswebResolved);
					cssResources = ResourceUtil.resolveEx(cssResources, null, null, _systemAppName,dynaTokes);
					headResources = ResourceUtil.toHTML(cssResources, null, null, null,"head",rtConfig);
				}
			}
			
			String jsHeadResources = ResourceUtil.toHTML(resources, null, null, null,"JS-HEADER-SCRIPT-TAG",rtConfig);
			if(headResources!=null){
				html = html.replace("%HEAD_0_INCLUDES%", headResources);
			}else{
				html = html.replace("%HEAD_0_INCLUDES%", "");
			}
			html = html.replace("%HEAD_JS_INCLUDES%", jsHeadResources);
						
			/***
			 * 	Common variables 
			 */
			String javaScriptVars = "<script type=\"text/javascript\">\n";
			
			
			//javaScriptVars += "\tvar dataHostUrl=\"" + options.dataHost +"\";\n";
			javaScriptVars += "\tvar dataHostUrl=\"" + options.dataHost +"\";\n";
			
			
			javaScriptVars += "\tvar applicationId=\"" + applicationId +"\";\n";
			javaScriptVars += "\tvar uuid=\"" + uuid +"\";\n";
			javaScriptVars += "\tvar rtConfig=\"" + options.runTimeConfiguration +"\";\n";

			javaScriptVars+="\t\n</script>";
			
			html = html.replace("%HEAD_JS_VARIABLE_INCLUDES%", javaScriptVars);
			
			
			String cssKeysString = "<script type=\"text/javascript\">\n";
			
			String cssKeysData=null;
			try {
				cssKeysData = StringUtils.readFileAsString(System.getProperty("xasWebRoot") + "/" + "cssKeys.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(cssKeysData!=null)
			{
				cssKeysData  = cssKeysData.replace("\n",";");
			}
			
			cssKeysString += "var xappCSSKeys=\"" +  cssKeysData +"\";\n";
			cssKeysString+="\n</script>";
			
			html = html.replace("%CSS_KEYS%", cssKeysString);
			
		}
			
			
		/***
		 * App Resources : 
		 */
		String appLocalPath = ApplicationManager.getInstance().getAppDataPath(uuid, applicationId,null);
		String appResourcesPath = appLocalPath + "resources-" + rtConfig + ".json";
		File _resourceFile =  new File(appResourcesPath);
		String appResStrFinal="";
		String cxAppStrFinal="";
		if(_resourceFile.exists())
		{
			Resources appResources = ResourceUtil.fromPath(appResourcesPath);
			
			if(appResources!=null){
				appResources = ResourceUtil.resolveEx(appResources, uuid,applicationId, null,dynamicTokens);
								
				//write out as script tag
				JSONSerializer appResD = new JSONSerializer();
				appResD.prettyPrint(true);
				String _appResourcesStr = appResD.deepSerialize(appResources);
				
				//System.out.println("app resource : " + _appResourcesStr);
				
				appResStrFinal = "<script type=\"text/javascript\">\n";
				
				appResStrFinal+="xappResources=";
				
				appResStrFinal+=_appResourcesStr +"\n";
				
				appResStrFinal+="</script>\n";
				appResStrFinal+=ResourceUtil.toHTML(appResources, null, null, null,"head",rtConfig);
				
				cxAppStrFinal=ResourceUtil.toHTML(appResources, applicationId, uuid, null,"CXAPP",rtConfig);
			}
		}
		
		
		html = html.replace("%HEAD_APP_RESOURCES%", appResStrFinal);
		html = html.replace("%HEAD_CXAPP%", cxAppStrFinal);
		/*
		String weinre = "<script src=\"" + System.getProperty("weinreTarget")  + applicationId +  "\"" +"></script>";
		html = html.replace("%HEAD_DEBUGGER%", weinre);
		*/
		
		html = html.replace("%HEAD_DEBUGGER%", "");
		
		
		/***
		 * Splash 
		 */
		String splash = "http://%WWW_SERVER%/%XAPP%/Assets/backgrounds/meta/Default.png";
		if(options.includeAppMedia)
		{
			splash = "Assets/backgrounds/meta/Default.png";
		}
		ApplicationMetaData appMeta = app.getMetaData();
		String launchScreen5 = "";
		if(appMeta!=null){
			ConfigurableInformation cLaunchScreen = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_2);
			if(cLaunchScreen!=null){
				if(cLaunchScreen.getValue()!=null && cLaunchScreen.getValue().length()>0 ){
					launchScreen5=cLaunchScreen.getValue();
					String launchScreenAbsPath = ResourceUtil.resolveConstantsAbsolute("%XUSER%/"+launchScreen5, uuid, applicationId);
					File launchScreenFile = new File(launchScreenAbsPath);
					
						if(launchScreenFile.exists() ){
							if(!options.includeAppMedia){
								splash="http://%WWW_SERVER%/CMAC/" + uuid  +"/apps/"+launchScreen5;
							}else{
								splash="" + launchScreen5;
								splash = splash.replace("/" + applicationId + "/", "");
							}
						}
					}
				}
		}
		if(!options.includeAppMedia){
			splash = ResourceUtil.resolveConstants(splash,uuid,applicationId);
		}
		html = html.replace("%SPLASH_URL%", splash);
		
		/***
		 * Build-Options
		 */
		if(options !=null)
		{
			JSONSerializer appResD = new JSONSerializer();
			options.platform=platform;
			appResD.prettyPrint(true);
			String _appResourcesStr = appResD.deepSerialize(options);
			
			
			appResStrFinal = "<script type=\"text/javascript\">\n";
			appResStrFinal+="var xappBuildOptions=";
			appResStrFinal+=_appResourcesStr +"\n";
			appResStrFinal+="</script>\n";
			
			html = html.replace("%XAPP_BUILD_OPTIONS%", appResStrFinal);
		}
		
		
		/***
		 * Custom Types
		 */
		String ctScope=null;
		if(ctScope==null)
		{	
			ctScope = "%XASWEB%";
		}
		
		/*
		String prefix = "/ctypes/";
		//public static ArrayList<CustomType>getTypes(String rtConfig,String platform,String uuid,String appId,String systemAppName,String storeRoot)
		
		ArrayList<CustomType> pTypes = CustomTypeUtils.getTypes(options.runTimeConfiguration, "IPHONE_NATIVE", uuid,applicationId, "xapp", ctScope + prefix);
		
		if(pTypes!=null)
		{
			//System.out.println("###### include custom types : " + pTypes.size());
			JSONSerializer typesSer = new JSONSerializer();
			typesSer.prettyPrint(false);
			String _appResourcesStr = typesSer.deepSerialize(pTypes);
			String cTypesFinal = "<script type=\"text/javascript\">\n";
			cTypesFinal+="xappConnectTypes=";
			cTypesFinal+=_appResourcesStr +"\n";
			cTypesFinal+="</script>\n";
			html = html.replace("%XAPP_CUSTOM_TYPES%", cTypesFinal);
		}
		*/
		
		System.out.println("hybrid platform : "+platform) ;
		if(platform.equals("HYBRID_IOS")){
			html = html.replace("%CORDOVA_FILE%", "cordova.js");
		}else if(platform.equals("HYBRID_ANDROID")){
			html = html.replace("%CORDOVA_FILE%", "cordova.js");
		}
		/***
		 * Dojo RunTime and Dojo Config
		 * 
	     */
		String dojoConfig = createDojoConfig(_systemAppName, "cssMobile", "en", uuid, applicationId, platform, type, options);
		String dojoPath = ""; 
		if(options.runTimeConfiguration.equals("debug"))
		{
			if(!options.includeSystemJS)
			{
				dojoPath=ResourceUtil.resolveConstantsEx("%XASWEB%/lib/dojo/dojo.js", null, null, dynamicTokens);
			}else{
				dojoPath = ResourceUtil.resolveConstantsEx(_systemAppName+ "/dojo/dojo.js", null, null, dynamicTokens);
			}
		}
		if(options.runTimeConfiguration.equals("release"))
		{
			if(!options.includeSystemJS)
			{
				dojoPath = ResourceUtil.resolveConstantsEx("%XASWEB%/" + _systemAppName+ "/dojo/dojo.js", null, null, dynamicTokens);
			}else{
				dojoPath = ResourceUtil.resolveConstantsEx(_systemAppName+ "/dojo/dojo.js", null, null, dynamicTokens);
			}
		}
		String dojoStrFinal = "<script type=\"text/javascript\" src=";
		dojoStrFinal +="\"" + dojoPath + "\"" +" djConfig=\"" + dojoConfig + "\"></script>"; 
		html = html.replace("%DOJO_PATH_AND_CONFIG%", dojoStrFinal);
		
		/***
		 * Dojo Boot Loader
		 */
	    
		String dojoBootLoader ="";
		if(options.runTimeConfiguration.equals("debug"))
		{
			
			if(!options.includeSystemJS)
			{
				dojoBootLoader=ResourceUtil.resolveConstantsEx("%XASWEB%/lib/"+_systemAppName + "/run.js" , null, null, dynamicTokens);
			}else{
				dojoBootLoader=ResourceUtil.resolveConstantsEx(_systemAppName + "/run.js" , null, null, dynamicTokens);				
			}
		}
		if(options.runTimeConfiguration.equals("release"))
		{
			if(!options.includeSystemJS)
			{
				dojoBootLoader =ResourceUtil.resolveConstantsEx("%XASWEB%/"+_systemAppName + "/" + _systemAppName + "/run.js" , null, null, dynamicTokens);
			}else{
				dojoBootLoader =ResourceUtil.resolveConstantsEx(_systemAppName + "/" + _systemAppName + "/run.js" , null, null, dynamicTokens);
			}
		}
		
		
		String dojoBootStrFinal = "<script type=\"text/javascript\" src=";
		dojoBootStrFinal+="\"" + dojoBootLoader + "\"></script>"; 
		html = html.replace("%DOJO_BOOT_LOADER%", dojoBootStrFinal);
		
		//appResStrFinal+="</script>\n";
		
		
		return html;
	}
	
}
