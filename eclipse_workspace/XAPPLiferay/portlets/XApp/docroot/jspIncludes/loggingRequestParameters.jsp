<%@page import="cmx.tools.AppFeatureConfigUtil"%>
<%@page import="cmx.FolderKeyResolverUtil"%>
<%@page import="cmx.types.FolderKeys"%>
<%
	
	
		String loggingLevel=request.getParameter("loggingLevel");
		if(loggingLevel!=null)
	    {
	    	request.getSession().setAttribute("loggingLevel", loggingLevel);
	    }else{
	    	request.getSession().setAttribute("loggingLevel", "ERROR");
	    }
	    
	    String loggingTargets=request.getParameter("loggingTargets");
		if(loggingLevel!=null)
	    {
	    	request.getSession().setAttribute("loggingTargets", loggingTargets);
	    }else{
	    	request.getSession().setAttribute("loggingTargets", "");
	    }
	    
	    String _logger=request.getParameter("logger");
		if(_logger!=null)
	    {
	    	request.getSession().setAttribute("logger", _logger);
	    }else{
	    	//request.getSession().setAttribute("logger", "");
	    }
	    
%>
