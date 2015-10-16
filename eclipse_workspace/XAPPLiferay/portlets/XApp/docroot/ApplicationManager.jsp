<%@page import="pmedia.utils.StringUtils"%>
<%@page import="com.liferay.portal.theme.PortletDisplay"%>
<%@page import="com.liferay.portal.model.LayoutTypePortlet"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.service.persistence.PortletUtil"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="javax.portlet.RenderRequest"%>
<%@page import="com.liferay.portal.kernel.util.Validator" %>




<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*"%>
<%@ page import="java.lang.*"%>

<!-- baseParameters : path, basePath, uuid, appId, runtimeConfiguration   -->
<%@ include file="jspIncludes/baseParameters.jsp" %>

<!-- override base parameters with url request parameters : uuid, appId, runtimeConfiguration   -->
<%@ include file="jspIncludes/requestParameters.jsp" %>

<!-- Create ApplicationManager and Application Instance   -->
<%@ include file="jspIncludes/baseObjects.jsp" %>

<!-- Sets dojo and xas paths basing on runtime configuration  -->
<%@ include file="jspIncludes/xamFrameworkPaths.jsp" %>

<!-- Sets dojo and xas paths basing on runtime configuration  -->

<%@ page import="java.io.*" %>
<%@ page import="java.lang.*" %>
<%
	//String pageUrl=request.getPathInfo();
	 
	 
	System.out.println("Loading Application Manager - JSP" + basePath + "oriParth : ");
	request.getSession().setAttribute("userSignedIn", ""+true);
	
	
	 String pwd = "";
	 Boolean constructCMX = true;
	 	 
%>

<html>
        <head>
        
        
        	<base href="<%=basePath%>">
        	
        	<jsp:include page="jspIncludes/XAS_JS_Includes.jsp"/>
        	<jsp:include page="jspIncludes/XAM_CSS_Includes.jsp"/>
	        <!-- Sets dojo and xas paths basing on runtime configuration  -->
			
			<script type="text/javascript">
				var startTime = new Date();
		        var applicationId ="<%=appId%>";
		        var uuid = "<%=uuid%>";
		        var isMaster = true;
		        window.isMaster = true;
		        var isLiferay=true;
		        var debug=true;
		        var sctx=null;
		        var mctx=null;
		        var ctx=null;
		        var pageUrl=null;
		        var pwd="<%=pwd%>";
			</script>
		</head>
		        
        <jsp:include page="jspIncludes/main/xamBodyTemplate.jsp"/>
        
</html>
		
