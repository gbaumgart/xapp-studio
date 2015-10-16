<%@page import="javax.portlet.RenderRequest"%>
<%@page import="com.liferay.portal.kernel.util.Validator" %>
<%@page import="com.liferay.portal.theme.PortletDisplay" %>
<%@page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@page import="com.liferay.portal.theme.ThemeDisplay" %>

<!-- baseParameters : path, basePath, uuid, appId, runtimeConfiguration   -->
<%@ include file="jspIncludes/baseParameters.jsp" %>

<!-- override base parameters with url request parameters : uuid, appId, runtimeConfiguration   -->
<%@ include file="jspIncludes/requestParameters.jsp" %>

<!-- override base parameters with url request parameters : appTemplate   -->
<%@ include file="jspIncludes/qxRequestParameters.jsp" %>


<!-- Create ApplicationManager and Application Instance   -->
<%@ include file="jspIncludes/baseObjects.jsp" %>

<!-- Sets dojo and xas paths basing on runtime configuration  -->
<%@ include file="jspIncludes/qxappFrameworkPaths.jsp" %>


<%@ page import="java.io.*" %>
<%@ page import="java.lang.*" %>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1" %>

<!-- Sets dojo and xas paths basing on runtime configuration  -->

<%
	
	//request.getSession().setAttribute("userSignedIn", ""+true);
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
	   System.out.println("have fb : "  + hasFacebookId + " : " + __fbAppId);
    }
    
    
    ApplicationManager _appManager = ApplicationManager.getInstance();
    Application _app = _appManager.getApplication(__uuid, __appId, false);
    if(_app==null)
    {
    	//we're guest user
    	//if(__uuid.equals(System.getProperty("guestUser"))){
    		//find the guest session application     		    		
    		_app = _appManager.getApplication(__uuid, __appId + request.getSession().getId(), false);
    		if(_app==null)
    		{
    		
    			//ok, create one :
    			_app = _appManager.createGuestApplication(__uuid, __appId, request.getSession().getId());
    			
    			if(_app!=null)
    			{
    				__appId=_app.getApplicationIdentifier();
    				session.setAttribute("appId", __appId);
    			}
    		}else{
    				__appId=_app.getApplicationIdentifier();
    				session.setAttribute("appId", __appId);
    		}
    	//}    		
    }
    
	
%>
        <html>
	        <head>
	        <base href="<%=basePath%>">
	        <title>Content Mobilizer 0.8</title>
	        
	        <jsp:include page="jspIncludes/XAS_JS_Includes.jsp"/>
	        <jsp:include page="jspIncludes/QXAPP_CSS_Includes.jsp"/>
	        <script>
	        
	        
	        	window.mapObject=new Object();
	        	window.mapObject.initMapInternal=function(e){};
	        	
	        	
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
		        var mctx=null;
		        var pwd="<%=pwd%>";
		        var hasFacebook="<%=hasFacebookId%>";
		        var fbAppId = "<%=__fbAppId%>";
		        var returnUrl= "<%=url%>";
		        
		        var appTemplate="<%=qxappTemplate%>";
	        
	        </script>
	        </head>
        <jsp:include page="jspIncludes/main/qxappBodyTemplate.jsp"/>
        </html>
