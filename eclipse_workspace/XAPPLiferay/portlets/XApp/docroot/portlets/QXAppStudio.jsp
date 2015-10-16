<%@page import="cmx.tools.TrackingUtils"%>
<%@page import="pmedia.utils.SecurityUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1" %>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@page import="javax.portlet.RenderRequest"%>
<%@page import="com.liferay.portal.kernel.util.Validator" %>
<%@page import="com.liferay.portal.theme.PortletDisplay" %>
<%@page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@page import="com.liferay.portal.theme.ThemeDisplay" %>
<%@ page import="java.io.*" %>
<%@ page import="java.lang.*" %>


<%@ include file="../jspIncludes/baseParameters.jsp" %>
<%@ include file="../jspIncludes/requestParametersLiferay.jsp" %>
<%@ include file="../jspIncludes/qxRequestParameters.jsp" %>
<%@ include file="../jspIncludes/baseObjects.jsp" %>
<%@ include file="../jspIncludes/cxappRequestParameters.jsp" %>

<%
	
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
	
	TrackingUtils.sendMail("Open Quick-XApp-Studio", "Open XApp-Studio", __uuid,__appId);
	
	
	//System.out.println("Loading Application Studio - JSP with " + basePath + " for : " + __appId + " and uuid : " + __uuid + " appTemplate + " + qxappTemplate);
	
	//app = appManager.getApplication((String)request.getSession().getAttribute("uuid"),(String)request.getSession().getAttribute("appId"),"Debug");
	if(app!=null){
		HttpServletRequest httpReqOri = PortalUtil.getOriginalServletRequest(request);
		String flush=httpReqOri.getParameter("flush");
		if(flush!=null){
			ApplicationManager _appManager=ApplicationManager.getInstance();
			Application _app = appManager.getApplication((String)request.getSession().getAttribute("uuid"), (String)request.getSession().getAttribute("appId"), false);
			if(_app!=null)
			{
				_appManager.getApplications().remove(_app);
			}
		}
	} 

	
	String forceLanguage = null;
	Locale localR = request.getLocale();
	if(localR!=null)
	{
		forceLanguage = localR.getLanguage();
	}

	if(!basePath.contains("8080"))
	{
		basePath=basePath.replace(":80","");
	}


 	if(!SecurityUtil.isValidAppAction(__uuid, __appId, request)){
	 	System.out.println("## user tried to open with wrong credentials ! ");
	 	//request.getSession().invalidate();
	 	//response.sendRedirect("http://www.xapp-studio.com");
	 }

	String __cxapp = (String)session.getAttribute("cxapp");
	String __cxappRoot = (String)session.getAttribute("cxappRoot");
	
	String systemAppName ="qxapp";
	request.getSession().setAttribute("systemAppName", systemAppName);
	
	String dojoMobileTheme ="xasCSSMobile";
	request.getSession().setAttribute("dojoMobileTheme", dojoMobileTheme);
	
	
	response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    
    response.setDateHeader("Expires", System.currentTimeMillis() + 604800000L);
    
%>


		<%@ include file="../includes/main.jsp" %>
		
        <html>
	        <head>
	        <base href="<%=basePath%>">
	        
	        <title>Quick-XApp-Studio v1.5 </title>
	        
	       	<%@ include file="../includes/systemResources.jsp" %>
     		
     		<%@ include file="../includes/appResources.jsp" %>
     		
     		<!-- Pull in QXApp-Core Plugin Resources  -->
     		<%	
     			session.setAttribute("systemAppName","qxapp");
     		 %>
     		<%@ include file="../includes/pluginResources.jsp" %>
     		
     		<!-- Pull in XApp-Mobile Plugin Resources  -->
     		<%	
     			session.setAttribute("systemAppName","xapp");
     		 %>
     		<%@ include file="../includes/pluginResources.jsp" %>
     		
     		<%@ include file="../includes/htmlTemplates.jsp" %>
     		
     		<!-- Flip Back to QXApp  -->
     		<%	
     			session.setAttribute("systemAppName","qxapp");
     		 %>    		
     		
     		
     		
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
	        var cctx=null;
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
	        var rtConfig="<%=RTConfig%>";
	        
	       
	        </script>
	        </head>
        <jsp:include page="../jspIncludes/main/qxappBodyTemplate.jsp"/>
        </html>
