<%@page import="javax.portlet.RenderRequest"%>
<%@page import="com.liferay.portal.kernel.util.Validator" %>
<%@page import="com.liferay.portal.theme.PortletDisplay" %>
<%@page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@page import="com.liferay.portal.theme.ThemeDisplay" %>
<%@ page import="java.io.*" %>
<%@ page import="java.lang.*" %>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1" %>


<!-- baseParameters : path, basePath, uuid, appId, runtimeConfiguration   -->
<%@ include file="jspIncludes/baseParameters.jsp" %>

<!-- override base parameters with url request parameters : uuid, appId, runtimeConfiguration   -->
<%@ include file="jspIncludes/requestParametersLiferay.jsp" %>

<!-- Create ApplicationManager and Application Instance   -->
<%@ include file="jspIncludes/baseObjects.jsp" %>

<!-- Sets dojo and xas paths basing on runtime configuration  -->
<!-- %@ include file="jspIncludes/xasFrameworkPaths.jsp" %-->

<!-- Look for custom app parameters (cxapp & cxappRoot) -->
<%@ include file="../jspIncludes/cxappRequestParameters.jsp" %>





<%
	
	//request.getSession().setAttribute("userSignedIn", ""+true);
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
	   System.out.println("have fb : "  + hasFacebookId + " : " + __fbAppId);
    }
	
	String __cxapp = (String)session.getAttribute("cxapp");
	String __cxappRoot = (String)session.getAttribute("cxappRoot");
	
	
	String systemAppName ="xas";
	request.getSession().setAttribute("systemAppName", systemAppName);
	
	String dojoMobileTheme ="xasCSSMobile";
	request.getSession().setAttribute("dojoMobileTheme", dojoMobileTheme);
	
	
	//System.out.println("have cxapp " + __cxappRoot);
	
	/*<!-- jsp:include page="jspIncludes/XAS_JS_Includes.jsp"/>
	        <jsp:include page="jspIncludes/XAS_CSS_Includes.jsp"/-->
	*/
%>


		<%@ include file="includes/main.jsp" %>

        <html debug="true">
	        <head>
	        <base href="<%=basePath%>">
	        <title>xApp-Studio 1.3 </title>
	        
			<jsp:include page="includes/systemResources.jsp"/>
			
			<%@ include file="../includes/appResources.jsp" %>
			
			
			<!-- script type="text/javascript" charset="utf-8" src="http://getfirebug.com/firebug-lite-debug.js">
			{
			    overrideConsole: true,
			    startInNewWindow: false,
			    startOpened: true,
			    enableTrace: true
			}
			</script-->
			
	         <script>
	        var startTime = new Date();
	        var applicationId ="<%=__appId%>";
	        var uuid = "<%=__uuid%>";
	        var isMaster = true;
	        window.isMaster = true;
	        var isLiferay=true;
	        var debug=true;
	        var device=null;
	        var sctx=null;
	        var ctx=null;
	        var cctx=null;
	        var mctx=null;
	        var pwd="<%=pwd%>";
	        var hasFacebook="<%=hasFacebookId%>";
	        var fbAppId = "<%=__fbAppId%>";
	        var returnUrl= "<%=url%>";

			<%if(__cxapp!=null && __cxapp.length() > 0 && __cxappRoot!=null && __cxappRoot.length() > 0 ) { %>	        
		        	var cxappId="<%=__cxapp%>";
	        		var cxappRoot="<%=__cxappRoot%>";
	       	<%}%>
	        </script>
	        </head>
        <jsp:include page="jspIncludes/main/xasBodyTemplate.jsp"/>
        </html>
