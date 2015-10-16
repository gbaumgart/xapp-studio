<%@page import="cmx.types.XCDatasourceInfo"%>
<%@page import="cmx.tools.DBConnectionChecker"%>
<%@page import="cmx.types.XCDataSource"%>
<%@page import="cmx.types.DataSourceBase"%>
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
		ApplicationManager am = ApplicationManager.getInstance();
		Application jspapp = am.getApplication(puuid, pappId, false);
		
		/*
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
							System.out.println(siteUrl + rpcUrl +  " :  has jsonp : " + hasJSONP);
							
							if(hasJSONP){
								XCDatasourceInfo info = new XCDatasourceInfo();
								info.url=siteUrl;
								info.hasJSONP=true;
								xcDataSourceInfos.add(info);
							}
						}
						

					}	
				}
			}
			
			
			if(xcDataSourceInfos!=null)
			{
				//System.out.println("###### include custom types : " + pTypes.size());
				request.getSession().setAttribute("xcDataSourceInfos", xcDataSourceInfos);
				
				JSONSerializer typesSer = new JSONSerializer();

				//typesSer.prettyPrint(true);
				String _appResourcesStr = typesSer.deepSerialize(xcDataSourceInfos);
				out.write("<script type=\"text/javascript\">\n");
				out.write("xcDataSourceInfos=");
				out.write(_appResourcesStr +"\n");
				out.write("</script>\n");
			}
			
		}
		
		/*
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
		*/
	}
%>

