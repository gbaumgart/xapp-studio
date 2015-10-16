<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<%@page import="pmedia.utils.CITools"%>
<%@page import="cmx.types.ConfigurableInformation"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.sun.org.apache.xpath.internal.operations.Bool"%>
<%@page import="cmx.tools.ServletTools"%>
<%@ page import="java.io.*"%>
<%@ page import="java.lang.*"%>
<%@ include file="jspIncludes/baseParameters.jsp" %>
<%@ include file="jspIncludes/requestParameters.jsp" %>
<%@ include file="jspIncludes/baseObjects.jsp" %>
<%@ include file="jspIncludes/cxappRequestParameters.jsp" %>
<%@ include file="jspIncludes/xappMetricRequestParameters.jsp" %>
<%@ include file="jspIncludes/loggingRequestParameters.jsp" %>
<!--%@ include file="includes/jsonpcheck.jsp" %-->


<%
		String systemAppName ="xapp";
		request.getSession().setAttribute("systemAppName", systemAppName);
		
		String dojoMobileTheme ="cssMobile";
		request.getSession().setAttribute("dojoMobileTheme", dojoMobileTheme);
		//String systemAppLocation="";
		
		response.setHeader("Access-Control-Allow-Origin","*");
		
		Boolean isDesktop = ServletTools.isDesktop(request, response, getServletContext());
		String isRedirected=request.getParameter("isRedirected");
		String preventSimulator=request.getParameter("noSim");
		//String inUrl = (String)request.getAttribute("javax.servlet.forward.request_uri");
		String reqUrl = request.getRequestURL().toString();
	    String queryString = request.getQueryString();   // d=789
	    if (queryString != null) {
	        reqUrl += "?"+queryString;
	    }
	    //System.out.println("is desktop : " + isDesktop);
		if(isDesktop && isRedirected==null && reqUrl!=null && preventSimulator==null){
			String _redirectToSimulator = System.getProperty("redirectToSimulator");
			if(_redirectToSimulator!=null && _redirectToSimulator.equals("true"))
			{
				String _redirectUrl = System.getProperty("WebPath") + "/iphonesimulator/index.php";
				_redirectUrl+="?isRedirected=true";
				if(!reqUrl.contains("width")){
					reqUrl+="&width=320&height=568";
				}
				_redirectUrl+="&url=" + URLEncoder.encode(reqUrl);
				response.sendRedirect(_redirectUrl);
			}
		}
		
		String _rtConfigC = (String)request.getSession().getAttribute("runTimeConfiguration");
		if(_rtConfigC!=null && _rtConfigC.equals("release")){
			useCache=false;
		}
		useCache=true;
		//System.out.println("use cache : " + useCache + "in" + _rtConfigC );
		
		Boolean hasMobileManifest = false;
		if(System.getProperty("mobileManifest")!=null && System.getProperty("mobileManifest").equals("true")){
			hasMobileManifest=true;
			useCache=false;
		}
		//<html manifest="cache.manifest">
		String mobileManifestUrl  = "./client?action=getManifest&appIdentifier="+(String)session.getAttribute("appId") + "&uuid="+(String)session.getAttribute("uuid");
		if(_rtConfigC!=null){
			mobileManifestUrl+="&runTimeConfiguration="+_rtConfigC;
		}
		
		
		String noManifest=request.getParameter("noManifest");
		
		if(noManifest!=null && noManifest.equals("true")){
			hasMobileManifest=false;
			useCache=true;			
		}
		//System.out.println("manifest url " + mobileManifestUrl);
 %>

<!-- New :  framework paths resolved in main -->
<%@ include file="includes/main.jsp" %>


<% if(hasMobileManifest){ %>
	<html manifest="<%=mobileManifestUrl%>">		
	<%}else{ %>
		<html>
	<%} %>
  <head>

    <base href="<%=basePath%>">
    
    <% if(useCache){ %>
		
	<%}else{ %>
		<meta http-equiv="expires" content="1">
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE,NO-STORE,PRIVATE">
	<%} %>
	
	<jsp:include page="jspIncludes/mobileHeaders.jsp"/>
	
	<%@ include file="includes/systemResources.jsp" %>
	
	<%@ include file="includes/appResources.jsp" %>
	
	<%@ include file="includes/pluginResources.jsp" %>
	
	<%@ include file="includes/composerResources.jsp" %>
	
	<%@ include file="includes/appBundle.jsp" %>
	
	<!-- %@ include file="includes/customTypes.jsp" %-->
	
	<%@ include file="includes/htmlTemplates.jsp" %>
	
	
	<%
		String __appId = (String)session.getAttribute("appId");
		if(__appId!=null){
			appId=__appId;			
		} 
		
		String __uuid = (String)session.getAttribute("uuid");
		if(__uuid!=null){
			uuid=__uuid;			
		}
		
		String __url = (String)session.getAttribute("url");
		if(__url!=null){
			url=__url;			
		}
		
		String __device = (String)session.getAttribute("device");
		if(__device!=null){
			device=__device;			
		}
		
		String __fbAppId = (String)session.getAttribute("fbAppId");
		Boolean hasFacebookId=false;
		if(__fbAppId!=null)
		{				
		   hasFacebookId=true;	
	    }
		
		//System.out.println("rtConfig : "  +  " : " + RTConfig);
		
		
		String __cxapp = (String)session.getAttribute("cxapp");
		String __cxappRoot = (String)session.getAttribute("cxappRoot");
		
		String __width = (String)session.getAttribute("width");
		String __height = (String)session.getAttribute("height");
		
		String _loggingLevel = (String)session.getAttribute("loggingLevel");
		String _loggingTargets = (String)session.getAttribute("loggingTargets");
		
		System.out.println("have cxapp " + __cxappRoot);
		
		String cxAppBoot = "cxapp/boot";
		
		String logger = "" + systemAppName;
		String remoteAddr = request.getRemoteAddr();
		if(request.getHeader("X-Forwarded-For")!=null)
		{
			remoteAddr = request.getHeader("X-Forwarded-For");
		}
		logger = logger+"@"+remoteAddr;
		
		
		
		response.addHeader("Expires","	0");
		response.addHeader("Pragma","no-cache");
		response.setHeader("Cache-Control", "no-cache, private,no-store, max-stale=0, must-revalidate");
		
		String _dojoVersion=(String)request.getSession().getAttribute("version");
		
		    
		//response.setHeader("Cache-Control", "no-cache, private, no-store, max-stale=0");
		
		
	 %>
	 
	
	
	
	<script>
        var applicationId ="<%=appId%>";
        var uuid = "<%=uuid%>";
        var device = "<%=device%>";
        var isMaster = true;
        window.isMaster = true;
        var isLiferay=false;
        var debug=true;
        var ctx = null;
        var sctx = null;
        var cctx = null;
        var log = null;
        var hasFacebook=false;
        var fbAppId = "<%=__fbAppId%>";
        var xappVersion = "<%=_dojoVersion%>";
        var device= "<%=device%>";
        var returnUrl= "<%=url%>";
        var FB=null;
        var isDesktop="<%=isDesktop%>";
        
        var xappLoggingConfig={
        	level:"<%=_loggingLevel%>",
        	targets:"<%=_loggingTargets%>",
        	logger:"<%=logger%>"
        }
        
        
        var forceDisplaySize=null;
        
        var rtConfig="<%=RTConfig%>";
        
        <%if(__height!=null && __width!=null ){%>
	        forceDisplaySize=
	        {	
	        	width:<%=__width%>,
	        	height:<%=__height%>
	        };
	        
		<%}%>
        var forceLanguage="en";
        
        
        
        var smdPath="lib/cxapp/quiz.json";
        var proxyHost="test.cooki.me";
        
       
        
        
    </script>

    <style>    
</style>
</head>

<jsp:include page="jspIncludes/main/mobileBodyTemplate.jsp"/>
<!-- script type="text/javascript">
    var mopub_ad_unit='agltb3B1Yi1pbmNyDQsSBFNpdGUYrqaVFAw';

    //Be sure to set these to the size of your adunit
    var mopub_ad_width=320; //optional
    var mopub_ad_height=50; //optional

    //Use custom keywords appropriate for your mobile webpage
    //var mopub_keywords= "custom keywords"; //optional
</script>
<script src="http://ads.mopub.com/js/client/mopub.js"></script-->
</html>
