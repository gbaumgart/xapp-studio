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

	//System.out.println("system app"  + systemAppName);
	
	
	String _systemAppName = (String)request.getSession().getAttribute("systemAppName");
	
	//build the resource config path :
	
	String sysAppLocalPath = FolderKeyResolverUtil.getAbsolutePrefix(FolderKeys.CM_LIB_ROOT) + _systemAppName +"/";
	
	
	String rtConfig = (String)request.getSession().getAttribute("runTimeConfiguration");
	
	String sysAppResourcesPath = sysAppLocalPath + "resources-" + rtConfig + ".json";
	
	File resourceFile =  new File(sysAppResourcesPath);
	if(resourceFile.exists())
	{
		//System.out.println("system app : "  + _systemAppName + " root + " + sysAppLocalPath);
		
		Resources resources = ResourceUtil.fromPath(sysAppResourcesPath);
		
		//resolve resources
		resources = ResourceUtil.resolve(resources, null, null, _systemAppName);
		
		out.write(ResourceUtil.toHTML(resources, null, null, null,"head",rtConfig));
		
		out.write(ResourceUtil.toHTML(resources, null, null, null,"JS-HEADER-SCRIPT-TAG",rtConfig));
		
		ResourceUtil.registerProxies(resources, null, null, _systemAppName, "PROXY");
		
		ResourceUtil.registerFileProxies(resources, null, null, _systemAppName, "FILE_PROXY");
		
		
		request.getSession().setAttribute("systemResources",resources);
	}
	
	String htmlString = "<script type=\"text/javascript\">\n";
	htmlString += "var liferayContentScopeId=\"" +  System.getProperty("scopeId") +"\";\n";
	htmlString+="\n</script>";
	out.write(htmlString);
	
	String cssKeysString = "<script type=\"text/javascript\">\n";
	
	String cssKeysData = StringUtils.readFileAsString(System.getProperty("xasWebRoot") + "/" + "cssKeys.txt");
	if(cssKeysData!=null)
	{
		cssKeysData  = cssKeysData.replace("\n",";");
	}
	
	cssKeysString += "var xappCSSKeys=\"" +  cssKeysData +"\";\n";
	cssKeysString+="\n</script>";
	out.write(cssKeysString);
	
%>

<%
	
 %>


