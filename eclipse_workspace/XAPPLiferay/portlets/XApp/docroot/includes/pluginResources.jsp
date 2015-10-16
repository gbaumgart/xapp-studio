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
		ArrayList<Resources> pResources = JSPluginManager.getPluginResources(System.getProperty("xappPluginRoot"), prtConfig, psystemAppName);
		
		/***
			User App Plugins : 
		*/
		String userAppPlugins = ResourceUtil.resolveConstantsAbsolute("%XAPP%/plugins/", puuid, pappId);
		if(userAppPlugins!=null){
		
			File userAppPluginPathObject = new File(userAppPlugins);
			if(userAppPluginPathObject.exists() && userAppPluginPathObject.isDirectory()){
				//System.out.println("user app plugins : " + userAppPlugins);
				ArrayList<Resources> userAppPluginResources = JSPluginManager.getPluginResources(userAppPlugins, prtConfig, psystemAppName);
				if(userAppPluginResources!=null && userAppPluginResources.size() > 0)
				{
					//System.out.println("have user app plugins : " + userAppPluginResources.size());
					if(pResources==null){
						pResources=new ArrayList<Resources>();
					}
					pResources.addAll(userAppPluginResources);
				}
				
			}
		}
		
		
		if(pResources!=null)
		{
			//System.out.println("###### include plugins : " + pResources.size());
			request.getSession().setAttribute("pluginResources", pResources);
			
			Application pApp = ApplicationManager.getInstance().getApplication(puuid,pappId, false);
			
			ArrayList<Resource> allPlugins = new ArrayList<Resource>();
			for(int i = 0 ; i<pResources.size(); i++ )
			{
				Resources res =pResources.get(i);
				
				String resUUID = null;
				String resAppId = null;
				
				
				res = ResourceUtil.resolve(res, puuid,null, null);
				
				out.write(ResourceUtil.toHTML(res, null, null, null,"head",prtConfig));
				out.write(ResourceUtil.toHTML(res, null, null, null,"JS-HEADER-SCRIPT-TAG",prtConfig));
				
				ResourceUtil.registerProxies(res, null, null, psystemAppName, "PROXY");
				allPlugins.addAll(res.getItems());
				
				if(pApp!=null){
					ApplicationManager.getInstance().registerPluginParameters(pApp, res);
									
				}
				
			}
			
			JSONSerializer appResD = new JSONSerializer();

			appResD.prettyPrint(true);
			String _appResourcesStr = appResD.deepSerialize(allPlugins);
			out.write("<script type=\"text/javascript\">\n");
			out.write(psystemAppName +"PluginResources=");
			out.write(_appResourcesStr +"\n");
			out.write("</script>\n");
			//System.out.println("all plugins : \n" + _appResourcesStr);
			
		}
	}
%>

