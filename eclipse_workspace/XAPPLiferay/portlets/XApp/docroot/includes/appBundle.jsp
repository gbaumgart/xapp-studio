<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<%@page import="java.io.Serializable"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="pmedia.Servlets.CMBaseServlet"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="pmedia.utils.JSONTools"%>
<%@page import="cmx.tools.StyleTreeFactory"%>
<%@page import="cmx.types.StyleTree"%>
<%@page import="cmx.types.ServiceSettings"%>
<%@page import="xappconnect.manager.XAppConnectManager"%>
<%@page import="pmedia.types.AppDataAll"%>
<%@page import="xappconnect.Utils.CustomTypeUtils"%>
<%@page import="xappconnect.types.CustomType"%>
<%@page import="cmx.manager.JSPluginManager"%>
<%@page import="cmx.types.Application"%>
<%@page import="cmx.types.ApplicationManager"%>
<%@page import="cmx.tools.ResourceUtil"%>
<%@page import="flexjson.JSONSerializer"%>
<%@page import="flexjson.JSON"%>
<%@page import="java.util.ArrayList"%>
<%@page import="cmx.types.Resource"%>
<%@page import="pmedia.utils.StringUtils"%>
<%@page import="flexjson.JSONDeserializer"%>
<%@page import="cmx.types.Resources"%>
<%@page import="java.io.File"%>
<%@page import="cmx.types.FolderKeys"%>
<%@page import="cmx.FolderKeyResolverUtil"%>
<%
	if((String)session.getAttribute("appId")!=null && (String)session.getAttribute("uuid")!=null)
	{
		String bappId = (String)session.getAttribute("appId");
		String buuid = (String)session.getAttribute("uuid");
		
		String bsystemAppName = (String)request.getSession().getAttribute("systemAppName");
		String brtConfig = (String)request.getSession().getAttribute("runTimeConfiguration");		
		String bctScope = (String)request.getSession().getAttribute("customTypeScope");
		
		if(bctScope==null)
		{	
			bctScope = "%XASWEB%";
		}
		String prefix = "/ctypes/";
		String platform = request.getParameter("platform");
		 
		if(platform==null){
			platform="IPHONE_NATIVE";
		}
		
		String lang = request.getParameter("lang");
		if(lang==null){
			lang="de";
		}
		String preventCache = request.getParameter("preventCache");
		preventCache="true";
		 
		if(preventCache==null){
			preventCache="true";
		}
		
		Cache cache = CMBaseServlet.getMainCache();
		String cacheKey = "appBundle" + buuid + bappId + brtConfig+platform+lang;
		boolean wasFromCache = false;
		
		Application bundleApplication = ApplicationManager.getInstance().getApplication(buuid, bappId, false);
		if(bundleApplication!=null){
		
			boolean allowCache=true;
			if(bundleApplication.flushCache==1 || preventCache.equals("true")){
				allowCache=false;
				CMBaseServlet.dropCacheMain(cacheKey);
				bundleApplication.flushCache=0;
				
			}
	
			if(allowCache && cache!=null && preventCache.equals("false")){
				Element element = cache.get(cacheKey);
				element=null;
				System.out.println("skip cache");
				
				if(element!=null){
					Serializable value = element.getValue();
					if(value!=null){
						 String appBundle  = value.toString();
						 String bhtmlString = "<script type=\"text/javascript\">\n";
							bhtmlString += "var xappAppBundle=" + appBundle+";\n";
							//System.out.println("app : "  + appBundle);
							bhtmlString+="\n</script>";
							//StringUtils.writeToFile(appBundle, "/tmp/a.json");
							out.write(bhtmlString);
							wasFromCache=true;
					}
				}
			}
			
			if(!wasFromCache){
				
				//http://www.xapp-studio.com/XApp-portlet/servlets/ImageScaleIcon?src=http%3A%2F%2Fwww.pearls-media.com%2FCMAC%2Fshared%2FSharedAssets%2FiconSets%2Fplastic%2Fdefault.png&width=70&shadow=false&icon=true
				//http://192.168.1.37:8080/XApp-portlet/servlets/ImageScaleIcon?src=http%3A%2F%2F192.168.1.37%2Fzoo254%2F%2F%2F%2Fmedia%2Fzoo%2Fapplications%2Fblog%2Fapplication.png&width=70&shadow=false&icon=true
				//http://192.168.1.37:8080/XApp-portlet/servlets/ImageScaleIcon?src=%2Fctypes%2F%2FSharedAssets%2FiconSets%2Fplastic%2Fdefault.png&width=70&shadow=false&icon=true
				
				
				
				/*
				if(bundleApplication!=null && bundleApplication.flushCache==1){
					dropCache(null);
					application.flushCache=0;
				}
				*/
				AppDataAll appData = new AppDataAll();
				
				/***
				 * CustomTypes
				 */
				appData.customTypes =  XAppConnectManager.getTypes(brtConfig, "IPHONE_NATIVE", buuid,bappId, bctScope);
				
				/***
				 * the application
				 */
				appData.app = bundleApplication;
				ApplicationManager.getInstance().updateRemoteResources(bundleApplication);
				
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
				
				
				
				
				StyleTree a = StyleTreeFactory.createStyleTree(buuid, bappId, platform,"", "", "");
				
				appData.styles=a;
				
				//String styleStr = serializer.deepSerialize(a);
				String bprefix = System.getProperty("WebPath") +"CMAC/"+buuid+"/apps/";
				
				JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
		    	JSONTools.optimizedAppBundle(serializer);
		    	
		    	String resultStr = serializer.deepSerialize(appData);
		    	
		    	resultStr = resultStr.replace("url('","url('" + bprefix);
				if(resultStr.contains( buuid + "/apps//SharedAssets/"))
				{
					resultStr = resultStr.replace(buuid + "/apps//SharedAssets" , "shared/SharedAssets");
				}
				request.getSession().setAttribute("appBundle",resultStr);
				String bhtmlString = "<script type=\"text/javascript\">\n";
				
				bhtmlString += "var xappAppBundle=" + resultStr+";\n";
				
				bhtmlString+="\n</script>";
				out.write(bhtmlString);
				
				
				
				
				if(cache!=null){
					Element element = new Element(cacheKey, resultStr);
					cache.put(element);
				}
			}
		}
		
		
	}
%>

