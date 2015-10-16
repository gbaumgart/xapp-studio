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
	
     %>
     


	
     
    <% if(debugCSS){%>
	    <link rel="stylesheet" type="text/css" href="themes/claro/document.css"/>
	    <link rel="stylesheet" type="text/css" href="themes/claro/claro.css">
	    <link href="lib/cssCommon/forms.css" type="text/css" rel="stylesheet"/>
	    
	    <link rel="stylesheet" type="text/css" href="css/QXWidgets.css">
	    <link rel="stylesheet" type="text/css" href="css/QXAppStudio.css">
	    <link rel="stylesheet" type="text/css" href="css/QXAppManager.css">
	    <link rel="stylesheet" type="text/css" href="cssCommon/qSheetStyles.css">
	    <link rel="stylesheet" type="text/css" href="<%=_basePath %>css/dijitOverride.css">
	    <link rel="stylesheet" type="text/css" href="css/xasCommons.css">
	    
    <%}else{%>
    	<link rel="stylesheet" type="text/css" href="qxam/resources/app.css">
	<%}%>
    
    
    
    
    <link rel="stylesheet" href="lib/dojox/widget/Wizard/Wizard.css">
    
    <link rel="stylesheet" type="text/css" href="css/Fonts.css">

<style type="text/css">
    body {
    	/*font: 12px Helvetica,Arial,clean,sans-serif !important;*/
    	outline: 0 !important;
    	
	}
    .claro .dijitSplitContainer-child, .claro .dijitBorderContainer-child {
    	border: 0px solid #B5BCC7 !important;
	}
    
    a:hover, a:active, a:focus {
    	outline: 0 !important;
	}
	div:hover, div:active, div:focus 
	{
    	outline: 0 !important;
	}
	span:hover, span:active, span:focus 
	{
    	outline: 0 !important;
	}
    span 
    {
    	-moz-user-select: none; 
    	-khtml-user-select: none;
	}
	
	html,body
	{ 
		/*
		width:100% !important; 
		height:100% !important; 
		padding:0 !important; 
		overflow:visible !important; */
		
	}
	
	/* pre-loader specific stuff to prevent unsightly flash of unstyled content */
    .simulator {
    background-image: url("images/simulator.png");
    background-repeat: no-repeat;
    }

</style>
    
	
    