<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1" %>
<%@ page import="java.io.*" %>
<%@ page import="java.lang.*" %>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@page import="javax.portlet.RenderRequest"%>
<%@page import="com.liferay.portal.kernel.util.Validator" %>
<%@page import="com.liferay.portal.theme.PortletDisplay" %>
<%@page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@page import="com.liferay.portal.theme.ThemeDisplay" %>


<%@ include file="../jspIncludes/baseParameters.jsp" %>

<%@ include file="../jspIncludes/qxManagerRequestParametersLiferay.jsp" %>

<%@ include file="../jspIncludes/qxManagerRequestParameters.jsp" %>

<%@ include file="../jspIncludes/baseObjects.jsp" %>


<%
	//include file="../jspIncludes/qxappManagerFrameworkPaths.jsp"	
	String pathOri =PortalUtil.getCurrentURL(request);
	String pageUrl=null;
	if(pathOri!=null)
	{
		pageUrl=pathOri.substring(1,pathOri.lastIndexOf("/")+1);		
	}
	String pwd="";
	
	String __uuid=(String)request.getSession().getAttribute("uuid");
	String __appId = (String)request.getSession().getAttribute("appId");
	
	String qxappTemplate = (String)request.getSession().getAttribute("appTemplate");
	
	if(qxappTemplate==null)
	{
		qxappTemplate="events";
	}
	
	
	String __url = (String)session.getAttribute("url");
	if(__url!=null){
		url=__url;			
	}
	
	String __fbAppId = (String)session.getAttribute("fbAppId");
	Boolean hasFacebookId=false;
	if(__fbAppId!=null)
	{				
	   hasFacebookId=true;	
    }
	
	String forceLanguage = null;
	Locale localR = request.getLocale();
	if(localR!=null)
	{
		forceLanguage = localR.getLanguage();
	}
	
	String systemAppName ="xappconnect";
	request.getSession().setAttribute("systemAppName", systemAppName);
	
	String dojoMobileTheme =null;
	request.getSession().setAttribute("dojoMobileTheme", dojoMobileTheme);
	
%>
	
	<%@ include file="../includes/main.jsp" %>

<html>

	        <head>
	        <base href="<%=basePath%>">
	        <title>XApp Connect-Manager v1.3</title>
	        
	        <%@ include file="../includes/systemResources.jsp" %>
	        
	         <script>
	        var startTime = new Date();
	        var applicationId ="<%=__appId%>";
	        var uuid = "<%=__uuid%>";
	        var isMaster = true;
	        window.isMaster = true;
	        var isLiferay=true;
	        var debug=true;
	        var sctx=null;
	        var ctx=null;
	        var device=null;
	        var mctx=null;
	        var pwd="<%=pwd%>";
	        var pageUrl="<%=pageUrl%>";
	        var hasFacebook="<%=hasFacebookId%>";
	        var fbAppId = "<%=__fbAppId%>";
	        var device= "<%=device%>";
	        var returnUrl= "<%=url%>";
	        var appTemplate="<%=qxappTemplate%>";
	        var forceLanguage="<%=forceLanguage%>";
	        var isSignedIn = <%=httpReq.getSession().getAttribute("userSignedIn")%>
	        
	        
	        </script>
	        </head>
        <jsp:include page="../jspIncludes/main/xappConnectBodyTemplate.jsp"/>
        </html>
