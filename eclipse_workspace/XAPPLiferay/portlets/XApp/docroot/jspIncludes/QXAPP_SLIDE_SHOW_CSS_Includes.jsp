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
	    
    <%}else{%>
    	<link rel="stylesheet" type="text/css" href="release/applications/xappstudio/resources/app.css">
    	
	<%}%>
    
	<link rel="stylesheet" type="text/css" href="css/QXAppSlideShow.css">
    
<style type="text/css">
    body {
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

    </style>
    <style type="text/css">
	</style>
    