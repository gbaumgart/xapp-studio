<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1" %>
<%
	String path = request.getContextPath();
	path="/diagrams-web/backend";
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//basePath+="backend/";
	System.out.println("basePath: " + basePath);
	
 %>
 
<html>
<head>


<base href="<%=basePath%>">

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">
<link rel="stylesheet" type="text/css" href="/diagrams-web/backend/ext-2.0.2/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css" href="/diagrams-web/backend/ext-2.0.2/resources/css/xtheme-gray.css">

<script type="text/javascript">if(!Repository) var Repository = {}; Repository.currentUser='public';</script>


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

<script src="/diagrams-web/backend/i18n/translation_en_us.js" type="text/javascript" ></script>


<link rel="stylesheet" type="text/css" href="/diagrams-web/backend/css/openid.css">
<link rel="stylesheet" type="text/css" href="/diagrams-web/backend/css/repository.css">
<link rel="stylesheet" type="text/css" href="/diagrams-web/backend/css/model_properties.css">
<!--[if IE]>
<style type="text/css">
@import url(/diagrams-web/backend/css/repository_ie_fixes.css);
</style>
<![endif]-->
<title>ORYX - Man - Debug.JSP</title>
</head>
<body>
<div id="root" style="width:100%;height:800px;position: absolute;"></div>
</body>
</html>