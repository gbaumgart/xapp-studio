<%@page import="cmx.tools.AppFeatureConfigUtil"%>
<%@page import="cmx.FolderKeyResolverUtil"%>
<%@page import="cmx.types.FolderKeys"%>
<%
	
	
		String width=request.getParameter("width");
		if(width!=null)
	    {
	    	request.getSession().setAttribute("width", width);
	    }else{
	    	request.getSession().setAttribute("width", null);
	    }
	    String height =request.getParameter("height");
	    if(height!=null)
	    {
	    	request.getSession().setAttribute("height", height);
	    }else{
	    	request.getSession().setAttribute("height", null);
	    }
	    	
%>
