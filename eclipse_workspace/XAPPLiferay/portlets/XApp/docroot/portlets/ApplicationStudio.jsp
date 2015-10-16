<%@page import="pmedia.utils.SecurityUtil"%>
<%@page import="cmx.tools.TrackingUtils"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@page import="javax.portlet.RenderRequest"%>
<%@page import="com.liferay.portal.kernel.util.Validator" %>
<%@page import="com.liferay.portal.theme.PortletDisplay" %>
<%@page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@page import="com.liferay.portal.theme.ThemeDisplay" %>

<%@ page import="java.io.*" %>
<%@ page import="java.lang.*" %>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1" %>

<!-- baseParameters : path, basePath, uuid, appId, runtimeConfiguration   -->
<%@ include file="../jspIncludes/baseParameters.jsp" %>

<!-- override base parameters with url request parameters : uuid, appId, runtimeConfiguration   -->
<%@ include file="../jspIncludes/requestParametersLiferay.jsp" %>

<!-- Create ApplicationManager and Application Instance   -->
<%@ include file="../jspIncludes/baseObjects.jsp" %>

<!-- Sets dojo and xas paths basing on runtime configuration>


<!-- Look for custom app parameters (cxapp & cxappRoot) -->
<%@ include file="../jspIncludes/cxappRequestParameters.jsp" %>




<!-- Sets dojo and xas paths basing on runtime configuration  -->


<%
	
	String pathOri =PortalUtil.getCurrentURL(request);
	String pageUrl=null;
	if(pathOri!=null)
	{
		pageUrl=pathOri.substring(1,pathOri.lastIndexOf("/")+1);		
	}
	String pwd=(String)request.getSession().getAttribute("pwd");
	
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
	  // System.out.println("have fb : "  + hasFacebookId + " : " + __fbAppId);
    }
	
	
	String __cxapp = (String)session.getAttribute("cxapp");
	String __cxappRoot = (String)session.getAttribute("cxappRoot");
	
	//System.out.println("have cxapp " + __cxappRoot);
	
	//System.out.println("basePath " + basePath);
	
	String systemAppName ="xas";
	request.getSession().setAttribute("systemAppName", systemAppName);
	
	String dojoMobileTheme ="xasCSSMobile";
	request.getSession().setAttribute("dojoMobileTheme", dojoMobileTheme);
	HttpServletRequest _httpReq = PortalUtil.getOriginalServletRequest(request);
	
	ThemeDisplay _themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
	
	 if(!SecurityUtil.isValidAppAction(__uuid, __appId, request)){
	 	System.out.println("## user tried to open with wrong credentials ! ");
	 	//request.getSession().invalidate();
	 	//response.sendRedirect("http://www.xapp-studio.com");
	 }
		
	//}
	
	//System.out.println("user passwd :  " + pwd);
	TrackingUtils.sendMail("Open XApp-Studio", "Open XApp-Studio", __uuid,__appId);
	
	boolean isAdmin = false;
	
	try {
		isAdmin=SecurityUtil.isValidAdminAction(__uuid, __appId, request);
	}catch (Exception e)
	{
					
	}
	
	
//	String pw = PortalUtil.getUserPassword(session);
///	System.out.println("is admin" + isAdmin + " password : " + pw);

	
	
%>

		
	<%@ include file="../includes/main.jsp" %>

    <html>
     <head>
     <base href="<%=basePath%>">
     <title>xApp-Studio 1.3</title>
     
     
     
     <%@ include file="../includes/systemResources.jsp" %>
     <%@ include file="../includes/appResources.jsp" %>
     
      <script>
      
      var startTime = new Date();
      var applicationId ="<%=__appId%>";
      var isAdmin =<%=isAdmin%>;
      var uuid = "<%=__uuid%>";
      var isMaster = true;
      window.isMaster = true;
      var isLiferay=true;
      var debug=true;
      var sctx=null;
      var ctx=null;
      var cctx=null;
      var device=null;
      var mctx=null;
      var pwd="<%=pwd%>";
      var pageUrl="<%=pageUrl%>";
      var hasFacebook="<%=hasFacebookId%>";
      var fbAppId = "<%=__fbAppId%>";
      var device= "<%=device%>";
      var returnUrl= "<%=url%>";
      
     <%if(__cxapp!=null && __cxapp.length() > 0 && __cxappRoot!=null && __cxappRoot.length() > 0 ) { %>	        
    	  	var cxappId="<%=__cxapp%>";
     		var cxappRoot="<%=__cxappRoot%>";
    	<%}%>
     
     
     </script>
     </head>
    
    <jsp:include page="../jspIncludes/main/xasBodyTemplate.jsp"/>
      
</html>
