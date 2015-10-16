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
     %>
    <% if(debugCSS){%>
	    <link rel="stylesheet" type="text/css" href="lib/dijit/themes/claro/document.css"/>
	    <link rel="stylesheet" type="text/css" href="lib/dijit/themes/claro/claro.css">
	    <link rel="stylesheet" href="cssCommon/video-js.css" type="text/css" media="screen" title="Video JS">
	    <link href="cssCommon/photoswipe.css" type="text/css" rel="stylesheet"/>
	    <link href="cssCommon/styles.css" type="text/css" rel="stylesheet"/>
	    <link href="css/sheetStyles.css" type="text/css" rel="stylesheet"/>
	    <link href="cssCommon/forms.css" type="text/css" rel="stylesheet"/>
    <%}else{%>
    	<link rel="stylesheet" type="text/css" href="release/applications/xappstudio/resources/app.css">
    	
	<%}%>
    
    <!-- link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>/dojox/mobile/themes/iphone/iphone.css"></link>
    <link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>/cssMobile/themes/iphone/gallery.css"></link-->
    <link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>/dojox/mobile/themes/common/domButtons/DomButtonRedBadge.css"></link>

    <link rel="stylesheet" type="text/css" href="css/Widgets.css">
    <link rel="stylesheet" type="text/css" href="css/AppStudio.css">
    <link rel="stylesheet" type="text/css" href="css/Showcase.css">
    <link rel="stylesheet" href="lib/dojox/widget/Wizard/Wizard.css">
    
    
    
    

<style type="text/css">
/*
    #main {
    width: 100%;
    overflow-y:auto;
    border: 0;
    }
    */
    
    body {
    
    	font: 12px Helvetica,Arial,clean,sans-serif !important;
    	background-color:transparent !important;

	}
    
    span 
    {
    	-moz-user-select: none; 
    	-khtml-user-select: none;
	}
	


    /* pre-loader specific stuff to prevent unsightly flash of unstyled content */
    #loader {
    padding: 0;
    margin: 0;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: #ededed;
    z-index: 999;
    vertical-align: middle;
    }

    #loaderInner {
    padding: 5px;
    position: relative;
    left: 0;
    top: 0;
    width: 175px;
    background: #3c3;
    color: #fff;
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
    
    /*
	td{
		padding-right:15px;
		vertical-align:top;
	}
	*/
	
	/* test 1 styles */
	
	</style>
    