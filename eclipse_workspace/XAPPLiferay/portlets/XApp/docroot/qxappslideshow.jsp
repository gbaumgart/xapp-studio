<%@page import="javax.portlet.RenderRequest"%>
<%@page import="com.liferay.portal.kernel.util.Validator" %>
<%@page import="com.liferay.portal.theme.PortletDisplay" %>
<%@page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@page import="com.liferay.portal.theme.ThemeDisplay" %>

<!-- baseParameters : path, basePath, uuid, appId, runtimeConfiguration   -->
<%@ include file="jspIncludes/baseParameters.jsp" %>

<!-- override base parameters with url request parameters : uuid, appId, runtimeConfiguration   -->
<%@ include file="jspIncludes/requestParameters.jsp" %>

<!-- Create ApplicationManager and Application Instance   -->
<%@ include file="jspIncludes/baseObjects.jsp" %>

<!-- Sets dojo and xas paths basing on runtime configuration  -->
<%@ include file="jspIncludes/qxappslideshowFrameworkPaths.jsp" %>


<%@ page import="java.io.*" %>
<%@ page import="java.lang.*" %>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1" %>

<!-- Sets dojo and xas paths basing on runtime configuration  -->


<%
	
	//request.getSession().setAttribute("userSignedIn", ""+true);
	String pwd="";
	String __uuid=(String)request.getSession().getAttribute("uuid");
	String __appId = (String)request.getSession().getAttribute("appId");
	
	String __url = (String)session.getAttribute("url");
	if(__url!=null){
		url=__url;			
	}
	String __fbAppId = (String)session.getAttribute("fbAppId");
	Boolean hasFacebookId=false;
	if(__fbAppId!=null)
	{				
	   hasFacebookId=true;	
	   System.out.println("have fb : "  + hasFacebookId + " : " + __fbAppId);
    }
    
    String __device = (String)session.getAttribute("device");
	if(__device!=null){
		device=__device;			
	}
%>
        <html>
	        <head>
	        <base href="<%=basePath%>">
	        <title>XApp Show</title>
	        <jsp:include page="jspIncludes/QXAPP_SLIDE_SHOW_JS_Includes.jsp"/>
	        <jsp:include page="jspIncludes/QXAPP_SLIDE_SHOW_CSS_Includes.jsp"/>
	        
	         <script>
	        var startTime = new Date();
	        var applicationId ="<%=__appId%>";
	        var uuid = "<%=__uuid%>";
	        var isMaster = true;
	        window.isMaster = true;
	        var isLiferay=true;
	        var debug=true;
	        var sctx=null;
	        var device= "<%=device%>";
	        var ctx=null;
	        var mctx=null;
	        var pwd="<%=pwd%>";
	        var hasFacebook="<%=hasFacebookId%>";
	        var fbAppId = "<%=__fbAppId%>";
	        var returnUrl= "<%=url%>";
	        var showcaseId = "QXAppHome";
	        </script>
	        </head>
        <jsp:include page="jspIncludes/main/qxappslideshowBodyTemplate.jsp"/>
        </html>
