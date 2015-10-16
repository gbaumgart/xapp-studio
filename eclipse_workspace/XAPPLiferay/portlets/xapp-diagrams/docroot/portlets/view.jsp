<%@page import="com.liferay.portal.kernel.struts.LastPath"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="com.liferay.util.portlet.PortletRequestUtil" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />

<%@ include file="requestParametersLiferay.jsp" %>

<%

	String path = request.getContextPath();
	path="/diagrams-web/backend";
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//basePath+="backend/";
	HttpServletRequest httpReq2 = PortalUtil.getOriginalServletRequest(request);
	String _isSignedIn = (String)httpReq2.getSession().getAttribute("userSignedIn");
	String openId = (String)httpReq2.getSession().getAttribute("openid");
	if(openId==null){
		openId = "public";
	}
	System.out.println("basePath: " + basePath + " and openid " + openId + " for " + session.getId());
	System.out.println("userSignedIn "  + _isSignedIn);
	
	String dstLang = "en_us";
	Locale rLocale = request.getLocale();
	if(rLocale!=null)
	{
		dstLang = rLocale.getLanguage();
	}
	
	System.out.println("dst language in : " + dstLang );
	
	if(!dstLang.equals("en_us") && !dstLang.equals("es") && !dstLang.equals("ru") && !dstLang.equals("de"))
	{
		dstLang = "en_us";
	}
	
	System.out.println("dst language : " + dstLang );
	
 %>
 
<html>
<head>


<base href="<%=basePath%>">

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">
<link rel="stylesheet" type="text/css" href="/diagrams-web/backend/ext-2.0.2/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css" href="/diagrams-web/backend/ext-2.0.2/resources/css/xtheme-gray.css">

<script type="text/javascript">if(!Repository) var Repository = {}; Repository.currentUser='<%=openId%>';</script>


<script type="text/javascript" src="/diagrams-web/backend/repository2/prototype.js"></script>

<script type="text/javascript" src="/diagrams-web/backend/ext-2.0.2/adapter/ext/ext-base.js"></script>


<script type="text/javascript" src="/diagrams-web/backend/ext-2.0.2/ext-all-debug.js"></script>

<script type="text/javascript" src="/diagrams-web/backend/repository2/ext_templates.js"></script>



<script type="text/javascript" src="/diagrams-web/backend/repository2/core/clazz.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/core/helper.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/config.js"></script>

<script type="text/javascript" src="/diagrams-web/backend/repository2/core/extExtention.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/core/eventHandler.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/core/dataCache.js"></script>

<script type="text/javascript" src="/diagrams-web/backend/repository2/core/plugin.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/core/viewPlugin.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/core/contextFreePlugin.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/core/contextPlugin.js"></script>


<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/context/accessInfo.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/context/edit.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/context/export.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/context/modelRangeSelection.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/context/rating.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/context/tagInfo.js"></script>

<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/contextFree/accessFilter.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/contextFree/busyIndicator.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/contextFree/friendFilter.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/contextFree/languageSupport.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/contextFree/newModelPlugin.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/contextFree/ratingFilter.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/contextFree/sortingSupport.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/contextFree/tagFilter.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/contextFree/typeFilter.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/contextFree/updateButton.js"></script>

<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/view/fullView.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/view/iconView.js"></script>
<script type="text/javascript" src="/diagrams-web/backend/repository2/plugins/view/tableView.js"></script>


<script type="text/javascript" src="/diagrams-web/backend/repository2/core/repository.js"></script>


<!--script type="text/javascript" src="/backend/repository2/repository2.js"></script-->

<script src="/diagrams-web/backend/i18n/translation_<%=dstLang %>.js" type="text/javascript" ></script>


<link rel="stylesheet" type="text/css" href="/diagrams-web/backend/css/openid.css">
<link rel="stylesheet" type="text/css" href="/diagrams-web/backend/css/repository.css">
<link rel="stylesheet" type="text/css" href="/diagrams-web/backend/css/model_properties.css">
<!--[if IE]>
<style type="text/css">
@import url(/diagrams-web/backend/css/repository_ie_fixes.css);
</style>
<![endif]-->
<title>My Diagrams</title>
</head>
<body >

<div id="root" class="xapp-diagram-manager-root" style="height:800px;position: relative;"></div>
</body>
</html>