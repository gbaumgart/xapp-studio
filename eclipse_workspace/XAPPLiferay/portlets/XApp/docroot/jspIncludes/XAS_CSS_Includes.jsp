	<!--link rel="stylesheet" href="lib/dijit/themes/claro/document.css"/-->
	<!--link rel="stylesheet" href="lib/dijit/themes/claro/claro.css"/-->
	<!--link rel="stylesheet" href="lib/dijit/tests/css/dijitTests.css"/-->

    <!-- link rel="stylesheet" type="text/css" href="themes/claro/document.css"/>
    <link rel="stylesheet" type="text/css" href="themes/claro/claro.css"-->
    <%
    
    String RTConfig = (String)request.getSession().getAttribute("runTimeConfiguration"); 
    Boolean debugCSS = true;
	if(RTConfig!=null)
	{
		if(RTConfig.equals("release") || RTConfig.equals("release-debug"))
		{
			//debugCSS=false;
		}
	}
	String _xappBaseDirectory = (String)request.getSession().getAttribute("xappBaseDirectory");
	
	String _path = request.getContextPath();
	String _basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";
	if(!_basePath.contains("8080"))
	{
		_basePath=_basePath.replace(":80","");
	}
	_xappBaseDirectory = _basePath+ _xappBaseDirectory;
	
     %>
     
     
     
    <% if(debugCSS){%>
	    <link rel="stylesheet" type="text/css" href="xasthemes/claro/document.css"/>
	    <link rel="stylesheet" type="text/css" href="xasthemes/claro/claro.css">
	    <link rel="stylesheet" href="cssCommon/video-js.css" type="text/css" media="screen" title="Video JS">
	    <link href="cssCommon/photoswipe.css" type="text/css" rel="stylesheet"/>
	    <link href="cssCommon/styles.css" type="text/css" rel="stylesheet"/>
	    <link href="css/sheetStyles.css" type="text/css" rel="stylesheet"/>
	    <link href="lib/cssCommon/forms.css" type="text/css" rel="stylesheet"/>
	    <link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>xasCSSMobile/themes/iphone/gallery.css"></link>
    <%}else{%>
    	<link rel="stylesheet" type="text/css" href="release/applications/xappstudio/resources/app.css">
    	
	<%}%>
    
    <!-- link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>/dojox/mobile/themes/iphone/iphone.css"></link>
    <link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>/cssMobile/themes/iphone/gallery.css"></link-->
    <link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>/dojox/mobile/themes/common/domButtons/DomButtonRedBadge.css"></link>

	<link rel="stylesheet" type="text/css" href="css/xasCommons.css">
	<link rel="stylesheet" type="text/css" href="css/xasdijitOverride.css">
	<link rel="stylesheet" type="text/css" href="css/Widgets.css">
    <link rel="stylesheet" type="text/css" href="css/AppStudio.css">

    <link rel="stylesheet" href="lib/dojox/widget/Wizard/Wizard.css">
    <link rel="stylesheet" href="lib/dojox/editor/plugins/resources/css/TextColor.css">
    
    <link rel="stylesheet" href="lib/dojo/resources/dnd.css">
    <!-- link rel="stylesheet" href="css/dndDefault.css"-->
    
	<!-- link rel="stylesheet" href="css/DND.css" type="text/css" /-->
    <link rel="stylesheet" href="lib/xas/dnd/resources/CMGridContainer.css" type="text/css" />
    
    <link rel="stylesheet" href="lib/dojox/layout/resources/ExpandoPane.css">
     <link rel="stylesheet" href="lib/dojox/layout/resources/ToggleSplitter.css">
     
     <!-- link rel="stylesheet" type="text/css" href="<%=_basePath %>css/dijitOverride.css"-->
     <link rel="stylesheet" type="text/css" href="<%=_basePath %>css/xasFonts.css">
     <link rel="stylesheet" type="text/css" href="<%=_basePath %>lib/cssCommon/xappwebfix.css"></link>
     
     
    
    <!-- link href="lib/cssMobile/tablet.css" rel="stylesheet"/>
    <link href="lib/cssMobile/listBase.css" rel="stylesheet"/>
    <link href="lib/cssMobile/commons.css" rel="stylesheet"/>
    <link href="lib/cssMobile/forms.css" rel="stylesheet"/>
    <link href="cssCommon/jquery.rating.css" type="text/css" rel="stylesheet"/-->
    <!--link rel="stylesheet" type="text/css" href="css/base.css">
    <link rel="stylesheet" type="text/css" href="css/aui.css"-->


<style type="text/css">
/*
    #main {
    width: 100%;
    overflow-y:auto;
    border: 0;
    }
    */
    
    body {
    
    	font: 13px Lato,Helvetica,Arial,clean,sans-serif !important;
    	
	}
    
    /*
    span 
    {
    	-moz-user-select: none; 
    	-khtml-user-select: none;
	}
	*/
	
	html,body
	{ 
		width:100% !important; 
		height:100% !important; 
		padding:0 !important; 
		overflow:visible !important; 
	}
	
	


    /* pre-loader specific stuff to prevent unsightly flash of unstyled content */
    .simulator {
    background-image: url("images/simulator.png");
    background-repeat: no-repeat;
    }

/*
.portlet-layout {
    border-collapse: collapse;
    border-spacing: 0;
    clear: both;
    display: table;
    table-layout: auto;
    width: 100%;
}
.portlet-column {
    display: table-cell;
    vertical-align: top;
}
.portlet-column-content {
    padding: 5px;
}
.portlet-column-content-only, .ltr .portlet-column-content-first, .rtl .portlet-column-content-last {
    padding-left: 0;
}
.portlet-column-content-only, .ltr .portlet-column-content-last, .rtl .portlet-column-content-first {
    padding-right: 0;
}
.portlet-column-content.empty {
    padding: 50px;
}
#main-content.dragging .portlet-column {
    border: 3px double #828F95;
    height: 100px;
    min-height: 100px;
}
.ie6 div.portlet-layout, .ie7 div.portlet-layout {
    height: 1%;
}
.ie6 div.portlet-column, .ie7 div.portlet-column {
    float: left;
    overflow: hidden;
    width: 100%;
}
.ie6 div.aui-column-last, .ie7 div.aui-column-last {
    margin-right: -1px;
}
.ie6 .portlet-column-content, .ie7 .portlet-column-content {
}
*/
    </style>
    <style type="text/css">
    
	
	.rotator{
		background-color:#fff;
		border:solid 1px #e5e5e5;
		width:384px;
		height:90px;
		overflow:hidden;
	}
	.pane{
		background-color:#fff;
		width:320px;
		height:480px;
		overflow:hidden;
	}
	
	/*
	td{
		padding-right:15px;
		vertical-align:top;
	}
	*/
	
	/* test 1 styles */
	.test1 .dojoxRotatorSelected{
		background-color:#e5e5e5;
		font-weight:bold;
	}
	
	/* test 2 styles */
	.test2 .rotatorContainer{
		width:384px;
		height:110px;
		position:relative;
	}
	.test2 .rotator{
		background-color:#e5e5e5;
		border:solid 1px #ccc;
		position:relative;
		top:20px;
		left:0;
		z-index:10;
	}
	.test2 .pane{
		background-color:#e5e5e5;
	}
	
	.test2 .tabs{
		position:absolute;
		top:40;
		left:10;
		z-index:20;
	}
	.test2 .tabs ul{
		margin:0;
		padding:0;
	}
	.test2 .tabs li{
		float:left;
		list-style:none;
		margin:0 5px 0 0;
		padding:0;
	}
	.test2 .tabs li a{
		background-color:#fff;
		border-top-left-radius:5px;
		border-top-right-radius:5px;
		-moz-border-radius-topleft:5px;
		-moz-border-radius-topright:5px;
		border:solid 1px #ccc;
		border-bottom:0;
		color:#666;
		display:block;
		font-size:10px;
		height:20px;
		padding:0 4px;
		text-decoration:none;
	}
	.test2 .tabs li.dojoxRotatorSelected a{
		background-color:#e5e5e5;
		color:#000;
	}
	
	.test2 .dots{
		position:absolute;
		right:5px;
		top:25px;
		z-index:20;
	}
	.test2 .dots ul{
		margin:0;
		padding:0;
	}
	.test2 .dots li{
		float:left;
		list-style:none;
		margin:0;
		padding:0;
	}
	.test2 .dots a span{
		display:none;
	}
	.test2 .dots .dojoxRotatorNumber a{
		display:block;
		width:10px;
		height:10px;
		background:url(images/rotator_dots.png) no-repeat 0 0;
	}
	.test2 .dots .dojoxRotatorSelected a{
		background:url(images/rotator_dots.png) no-repeat 0 -10px;
	}
	
	.test2 .pager1{
		background:url(images/rotator_bg.gif) no-repeat 0 0;
		bottom:5px;
		padding:3px 0 0 12px;
		position:absolute;
		left:5px;
		z-index:20;
	}
	.test2 .pager1 ul{
		height:16px;
		margin:0;
		padding:0;
		width:97px;
	}
	.test2 .pager1 li{
		float:left;
		line-height:12px;
		list-style:none;
		margin:0;
		padding:0;
	}
	.test2 .pager1 a{
		color:#fff;
		font-size:10px;
		text-decoration:none;
	}
	.test2 .pager1 li.dojoxRotatorIcon a{
		display:block;
		width:14px;
		height:14px;
	}
	.test2 .pager1 li.dojoxRotatorPrev a{
		background:url(images/rotator_icons.gif) no-repeat 0 0;
	}
	.test2 .pager1 li.dojoxRotatorPrev a span,
	.test2 .pager1 li.dojoxRotatorNext a span{
		display:none;
	}
	.test2 .pager1 li.dojoxRotatorNext a{
		background:url(images/rotator_icons.gif) no-repeat -14px 0;
	}
	.test2 .pager1 li.dojoxRotatorNumber a{
		color:#ccc;
		padding:0 4px;
	}
	.test2 .pager1 li.dojoxRotatorSelected a{
		color:#fff;
		font-weight:bold;
	}
	
	.test2 .pager2{
		background:url(images/rotator_bg.gif) no-repeat 0 0;
		bottom:13px;
		padding:3px 0 0 12px;
		position:absolute;
		left:120px;
		z-index:20;
	}
	.test2 .pager2 ul{
		height:16px;
		margin:0;
		padding:0;
		width:97px;
	}
	.test2 .pager2 li{
		float:left;
		list-style:none;
		margin:0;
		padding:0;
	}
	.test2 .pager2 li.dojoxRotatorIcon a{
		display:block;
		width:14px;
		height:14px;
	}
	.test2 .pager2 li.dojoxRotatorPrev a{
		background:url(images/rotator_icons.gif) no-repeat 0 0;
	}
	.test2 .pager2 li.dojoxRotatorPlay a{
		background:url(images/rotator_icons.gif) no-repeat -28px 0;
	}
	.test2 .pager2 li.dojoxRotatorPause a{
		background:url(images/rotator_icons.gif) no-repeat -42px 0;
	}
	.test2 .pager2 li.dojoxRotatorNext a{
		background:url(images/rotator_icons.gif) no-repeat -14px 0;
	}
	.test2 .pager2 li.dojoxRotatorInfo{
		color:#e5e5e5;
		font-size:10px;
		line-height:12px;
		padding:0 4px 0 7px;
		width:32px;
	}
	.test2 .pager2 li a span{
		display:none;
	}
	</style>
    