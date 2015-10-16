<%@page import="net.sourceforge.wurfl.core.MarkUp"%>
<%@page import="net.sourceforge.wurfl.core.Device"%>
<%@page import="net.sourceforge.wurfl.core.WURFLManager"%>
<%@page import="net.sourceforge.wurfl.core.WURFLHolder"%>
<%@page import="pmedia.utils.StringUtils"%>
<%@page import="cmx.types.ApplicationManager"%>
<%@page import="cmx.types.Application"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>

<%



	/*
	Object sName = getServletContext().getAttribute(WURFLHolder.class.getName());
	WURFLHolder wurfl = (WURFLHolder) getServletContext().getAttribute(WURFLHolder.class.getName());
	*/
	//WURFLHolder
	/*
	WURFLManager manager = wurfl.getWURFLManager();
	if(manager!=null){
		System.out.println("sname : ");
		Device wdevice = manager.getDeviceForRequest(request);
		if(wdevice!=null)
		{
			MarkUp markUp = wdevice.getMarkUp();
			
			Boolean isWireLess = wdevice.getCapabilityAsBool("is_wireless_device");
			System.out.println(" is wireless device " + isWireLess);
			
		}
	}
	*/
	String flushConfig=request.getParameter("flushConfig");
	
	if(flushConfig!=null)
	{
		StringUtils.loadProperties();
	}
	
	ApplicationManager appManager=ApplicationManager.getInstance();
	Application app = appManager.getApplication((String)request.getSession().getAttribute("uuid"),(String)request.getSession().getAttribute("appId"),"Debug");
	if(app!=null){
		//System.out.println("loading app : " +(String)request.getSession().getAttribute("appId"));
		
		String flush=request.getParameter("flush");
		flush="asdf";
		if(flush!=null)
		{
			ApplicationManager _appManager=ApplicationManager.getInstance();
			//System.out.println("flushing app");
			Application _app = appManager.getApplication((String)request.getSession().getAttribute("uuid"), (String)request.getSession().getAttribute("appId"), false);
			if(_app!=null)
			{
				_appManager.getApplications().remove(_app);
			}
		
		}
	} 
%>
