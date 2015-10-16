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


	String ___appId = (String)session.getAttribute("appId");
	String ___uuid = (String)session.getAttribute("uuid");
	String ___didAddResources = (String)request.getAttribute("didAddResources");
	if(___didAddResources!=null && ___didAddResources.equals("true")){
		//System.out.println("did add resources !");
	}
	
	//System.out.println("including app resources : " + request.getAttribute("javax.servlet.forward.request_uri"));
	//System.out.println("request id "  + System.identityHashCode(request));
	//System.out.println("including app resources : ctx uri " + request.getRequestURI());
	//System.out.println("including app resources : ctx url" + 			request.getRequestURL() + request.getQueryString());
	

	if(___uuid!=null && ___appId!=null)
	{
		//request.setAttribute("didAddResources", "true");
		//System.out.println("include appresources");
		Application xuapp = ApplicationManager.getInstance().getApplication(___uuid,___appId, false);
		
		if(xuapp!=null)
		{
	
	
			String appLocalPath = ApplicationManager.getInstance().getAppDataPath(___uuid, ___appId,null);
			
			String _rtConfig = (String)request.getSession().getAttribute("runTimeConfiguration");
			
			String appResourcesPath = appLocalPath + "resources-" + _rtConfig + ".json";
			
			File _resourceFile =  new File(appResourcesPath);
			if(_resourceFile.exists())
			{
				//System.out.println("user app : "  + ___appId + " root + " );
				
				Resources resources = ResourceUtil.fromPath(appResourcesPath);
				
				if(resources!=null){
					//resolve resources
					resources = ResourceUtil.resolve(resources, ___uuid,___appId, null);
					
					ResourceUtil.registerProxies(resources, ___uuid,___appId,null, "PROXY");
					
					//write out as script tag
					JSONSerializer appResD = new JSONSerializer();
					appResD.prettyPrint(true);
					String _appResourcesStr = appResD.deepSerialize(resources);
					
					//System.out.println("app resource : " + _appResourcesStr);
					
					
					request.getSession().setAttribute("appResources",resources);
					
					out.write("<script type=\"text/javascript\">\n");
					
					out.write("xappResources=");
					
					out.write(_appResourcesStr +"\n");
					
					out.write("</script>\n");
					
					out.write(ResourceUtil.toHTML(resources, ___appId, ___uuid, null,"head",_rtConfig));
					out.write(ResourceUtil.toHTML(resources, ___appId, ___uuid, null,"CXAPP",_rtConfig));
				}
			}
		}
	}
%>