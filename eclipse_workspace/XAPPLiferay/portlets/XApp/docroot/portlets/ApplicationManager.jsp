<%@page import="pmedia.utils.SecurityUtil"%>
<%@page import="cmx.tools.LiferayContentTools"%>
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
<%@ include file="../jspIncludes/baseParameters.jsp" %>

<%
	ThemeDisplay themeDisplayReq = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
	if(themeDisplayReq !=null)
	{
			HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(request);
			HttpSession _session = httpReq.getSession();
			_session.setAttribute("themeDisplay",themeDisplayReq);
			System.out.println("store in session----------");
	}
 %>

<!-- override base parameters with url request parameters : uuid, appId, runtimeConfiguration   -->
<%@ include file="../jspIncludes/requestParameters.jsp" %>
<!-- Create ApplicationManager and Application Instance   -->
<%@ include file="../jspIncludes/baseObjects.jsp" %>

<!-- Sets dojo and xas paths basing on runtime configuration  -->
<%@ include file="../jspIncludes/xamFrameworkPaths.jsp" %>

<!-- Sets dojo and xas paths basing on runtime configuration  -->

<%@ page import="java.io.*" %>
<%@ page import="java.lang.*" %>
<%
	//String pageUrl=request.getPathInfo();
	 
	 
	HttpServletRequest oriReq = PortalUtil.getOriginalServletRequest(request);
	String pathOri =PortalUtil.getCurrentURL(request);
	String pageUrl=null;
	if(pathOri!=null)
	{
		pageUrl=pathOri.substring(1,pathOri.lastIndexOf("/")+1);		
	}
	System.out.println("Loading Application Manager - JSP" + basePath + "oriParth : " + pageUrl);
	
	
	 User __user= PortalUtil.getUser(request);
	 //String userName =user.getFirstName();
	 if(__user!=null)
	 {
	 	uuid = __user.getUuid();
	 }
	 
	 String localUserPath = null;
	 String pwd =""; 
	 Boolean constructCMX = true;
	 
	 User user = LiferayContentTools.getLiferayUserByUUID(uuid);
	 
	 if(constructCMX && user!=null)
 	 {
 	 	pwd =user.getPassword();
 		appManager = ApplicationManager.getInstance();
		System.out.println("user :  " + uuid + " pwd " + pwd);
		if(!appManager.hasUser(uuid))
		{
			System.out.println("user has no profile path");
			appManager.createUser(uuid,true);
		}
	 }	 
	 
	 if(!SecurityUtil.isValidAppAction(uuid, appId, request))
	{
		System.out.println("## user tried to open with wrong credentials ! ");
	}
	 
	 if( uuid !=null && uuid.equals(System.getProperty("adminUser"))){
		//List<User>users  = LiferayContentTools.getLiferayUsers();
		//System.out.println("Liferay Users : " + users.size());
	}
%>

<html>
        <head>
        
        
        	<base href="<%=basePath%>">
        	
        	<jsp:include page="../jspIncludes/XAS_JS_Includes.jsp"/>
        	<jsp:include page="../jspIncludes/XAM_CSS_Includes.jsp"/>
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
		        var pageUrl="<%=pageUrl%>";
		        var pwd="<%=pwd%>";
			</script>
		</head>
		        
        <jsp:include page="../jspIncludes/main/xamBodyTemplate.jsp"/>
        
</html>
		
