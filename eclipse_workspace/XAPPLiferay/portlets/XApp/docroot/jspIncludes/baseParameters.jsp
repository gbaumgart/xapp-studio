<%@page import="pmedia.types.Constants"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	Boolean useCache =false;
	String uuid = "11166763-e89c-44ba-aba7-4e9f4fdf97a9";//System.getProperty("guestUser");


    String runTimeConfiguration=System.getProperty("defaultRuntimeConfiguration");
    String device=Constants.USERAGENT_IPHONE_NATIVE;
    String url="";
    String appTemplate=System.getProperty("defaultAppTemplate");
    String appId = "qxevents";
    
    if(appTemplate!=null)
    {
    	appId = "qx"+appTemplate.toLowerCase();
    }else{
    	appTemplate="Events";
    }
    
    String _appIn =(String)request.getSession().getAttribute("appId");
    if(_appIn!=null && _appIn.length()>0){
    	appId=_appIn;
    }
    
    if(request.getSession()!=null)
    {     
    
    	     request.getSession().setAttribute("uuid", uuid);
    	     request.getSession().setAttribute("appId", appId);
    	     request.getSession().setAttribute("runTimeConfiguration", runTimeConfiguration);
    	     request.getSession().setAttribute("device", device);
    	     request.getSession().setAttribute("url", url);
    	     request.getSession().setAttribute("appTemplate", appTemplate);
    	     
    }
%>