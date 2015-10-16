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

	String __appId = (String)session.getAttribute("appId");
	String __uuid = (String)session.getAttribute("uuid");
	
	if(__uuid!=null && __appId!=null)
	{
		Application app = ApplicationManager.getInstance().getApplication(__uuid, __appId, false);
		
		if(app!=null){
	
			String appLocalPath = ApplicationManager.getInstance().getAppDataPath(__uuid, __appId,null);
			
			String rtConfig = (String)request.getSession().getAttribute("runTimeConfiguration");
			
			String appResourcesPath = appLocalPath + "resources-" + rtConfig + ".json";
			
			File resourceFile =  new File(appResourcesPath);
			if(resourceFile.exists())
			{
				//System.out.println("user app : "  + __appId + " root + " );
				
				Resources resources = ResourceUtil.fromPath(appResourcesPath);
				
				//resolve resources
				resources = ResourceUtil.resolve(resources, __uuid,__appId, __appId);
				/*
				out.write(ResourceUtil.toHTML(resources, null, null, null,"head"));
				
				out.write(ResourceUtil.toHTML(resources, null, null, null,"JS-HEADER-SCRIPT-TAG"));
				*/
				/*ResourceUtil.registerProxies(resources, null, null, _systemAppName, "PROXY");*/
				
				request.getSession().setAttribute("systemResources",resources);
			}
		}
		
	}
%>

<%
	
 %>


