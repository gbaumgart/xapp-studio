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
		String pappId = (String)session.getAttribute("appId");
		String puuid = (String)session.getAttribute("uuid");
		
		String psystemAppName = (String)request.getSession().getAttribute("systemAppName");
		String prtConfig = (String)request.getSession().getAttribute("runTimeConfiguration");		
		String ctScope = (String)request.getSession().getAttribute("customTypeScope");
		
		if(ctScope==null)
		{	
			ctScope = "%XASWEB%";
		}
		String prefix = "/ctypes/";
		//public static ArrayList<CustomType>getTypes(String rtConfig,String platform,String uuid,String appId,String systemAppName,String storeRoot)
		//, ctScope + prefix
		ArrayList<CustomType> pTypes = CustomTypeUtils.getTypesAll(prtConfig, "IPHONE_NATIVE", puuid,pappId, psystemAppName);
		
		if(pTypes!=null)
		{
			//System.out.println("###### include custom types : " + pTypes.size());
			request.getSession().setAttribute("customTypes", pTypes);
			
			JSONSerializer typesSer = new JSONSerializer();

			//typesSer.prettyPrint(true);
			String _appResourcesStr = typesSer.deepSerialize(pTypes);
			
			out.write("<script type=\"text/javascript\">\n");
			out.write("xappConnectTypes=");
			out.write(_appResourcesStr +"\n");
			out.write("</script>\n");
			
		}
	}
%>

