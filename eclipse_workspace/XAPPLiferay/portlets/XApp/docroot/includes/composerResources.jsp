<%@page import="pmedia.utils.ComposerPackageUtils"%>
<%@page import="jsontype.ComposerPackage"%>
<%@page import="cmx.types.XCDatasourceInfo"%>
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

	String __cappId = (String)session.getAttribute("appId");
	String __cuuid = (String)session.getAttribute("uuid");
	
	if(__cuuid!=null && __cappId!=null)
	{
		Application capp = ApplicationManager.getInstance().getApplication(__cuuid, __cappId, false);
		
		if(capp!=null){
	
			String appLocalPath = ApplicationManager.getInstance().getAppDataPath(__cuuid, __cappId,null);
			String cRTConfig = (String)request.getSession().getAttribute("runTimeConfiguration");
			if(capp.getXcDataSourceInfos()!=null){
				ArrayList<XCDatasourceInfo>dsInfos = capp.getXcDataSourceInfos();
				for(int i= 0 ; i< dsInfos.size() ; i ++){
					XCDatasourceInfo info = dsInfos.get(i);

					if(info.getComposerPackages()!=null){
						ArrayList<Resources>resources=ComposerPackageUtils.getResourcesByRunTimeConfiguration(info.getComposerPackages(),cRTConfig, info);
						if(resources!=null){
							for(int j = 0 ; j<resources.size(); j++ )
							{
								Resources res =resources.get(j);
								res = ResourceUtil.resolve(res, __cuuid,__cappId, null);
								//String __res= ResourceUtil.toHTML(res, null, null, null,"head",cRTConfig);
								out.write(ResourceUtil.toHTML(res, null, null, null,"head",cRTConfig));
								//out.write(ResourceUtil.toHTML(res, null, null, null,"JS-HEADER-SCRIPT-TAG",cRTConfig));
							}
						}
					}
				}
			}
		}
	}
%>

<%
	
 %>


