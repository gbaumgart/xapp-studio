<%@page import="cmx.types.HTMLTemplateManager"%>
<%@page import="cmx.types.HTMLTemplate"%>
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


	String _tAppId = (String)session.getAttribute("appId");
	String _tUUID = (String)session.getAttribute("uuid");
	ArrayList<HTMLTemplate>_htmlTemplates=HTMLTemplateManager.getTemplates(_tUUID, _tAppId, null);
	
	if(_htmlTemplates!=null && _htmlTemplates.size()>0){					
		request.getSession().setAttribute("xappHTMLTemplates",_htmlTemplates);
		out.write("<script type=\"text/javascript\">\n");
		out.write("xappHTMLTemplates=");
		
		JSONSerializer appResD = new JSONSerializer();
		appResD.prettyPrint(true);
		String _htmlTemplatesStr = appResD.deepSerialize(_htmlTemplates);
		out.write(_htmlTemplatesStr +"\n");
		out.write("</script>\n");
	}

%>