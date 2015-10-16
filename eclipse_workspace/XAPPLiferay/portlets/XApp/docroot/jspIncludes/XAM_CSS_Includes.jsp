<%
    
    String RTConfig = (String)request.getSession().getAttribute("runTimeConfiguration"); 
    Boolean debugCSS = true;
	if(RTConfig!=null)
	{
		if(RTConfig.equals("release") || RTConfig.equals("releaseDebug"))
		{
			//debugCSS=false;
		}
	}
	String _path = request.getContextPath();
	String _basePathCSS = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";//<%=_basePathCSS
%>
    
    <% if(debugCSS){%>
	    <link rel="stylesheet" type="text/css" href="lib/dijit/themes/claro/document.css"/>
	    <link rel="stylesheet" type="text/css" href="lib/dijit/themes/claro/claro.css">
	    <link rel="stylesheet" href="cssCommon/video-js.css" type="text/css" media="screen" title="Video JS">
	    <link href="cssCommon/photoswipe.css" type="text/css" rel="stylesheet"/>
	
	    <link href="cssCommon/styles.css" type="text/css" rel="stylesheet"/>
	    <link href="cssCommon/qsheetStyles.css" type="text/css" rel="stylesheet"/>
    <%}else{%>
    	<link rel="stylesheet" type="text/css" href="release/applications/xappmanager/resources/app.css">
    	
	<%}%>
    
    <link rel="stylesheet" type="text/css" href="css/Widgets.css">
    <link rel="stylesheet" type="text/css" href="css/AppStudio.css">
    

<style type="text/css">
    
    /*span 
    {
    	-moz-user-select: none; 
    	-khtml-user-select: none;
	}
	*/


	body {
    	font: 12px Myriad,Helvetica,Tahoma,Arial,clean,sans-serif !important;
	}

	@IMPORT url("css/Widgets.css");
	@IMPORT url("css/AppStudio.css");

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
    </style>
    
    