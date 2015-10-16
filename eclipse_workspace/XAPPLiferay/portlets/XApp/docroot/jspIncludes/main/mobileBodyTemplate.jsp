<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<!-- %@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%-->
<% 
	String _dojoPath=(String)request.getSession().getAttribute("dojoPath");
	String _bootLoader=(String)request.getSession().getAttribute("bootLoader");
	String _dojoConfig=(String)request.getSession().getAttribute("dojoConfig");
	
	String _path = request.getContextPath();
	String _basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";
	
	/*
	response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST,GET");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
    */
    
    
	
	//System.out.println("Loading Mobile Application at \n\t : " + _basePath +"\n\t DojoPath : " + _dojoPath + "\n\t BootLoader : " + _bootLoader);
 %>
<body class="mobileBody">
<div id="root" data-dojo-attach-point='root' style="padding: 0px;overflow:hidden !important;visibility:visible;">
</div>
<div id="progDiv" style="z-index:99999"></div>
<div id="fb-root"></div>
<div id="cacheDiv"></div>

</div-->

<div id="loadingWrapper" class="loadingWrapper">
	<div class="loading">
	    <div class="outer"></div>
	    <div class="inner"></div>
	</div>
</div>

	<div id="loadDiv" style="position:absolute;left:0px;top:0px;width:100%;height:100%;z-index:999;">
		<span></span>
	</div-->

		
		
		<div id="calPicker" data-dojo-type="dojox.mobile.Opener" style="z-index:2000">
	 		<h1 dojoType="dojox.mobile.Heading" label="Date Picker">
				<div dojoType="dojox.mobile.ToolBarButton" label="Done" class="mblColorBlue" style="position:absolute;width:45px;right:0;"
					onclick="dijit.registry.byId('calPicker').hide(true)"></div>
				<div dojoType="dojox.mobile.ToolBarButton" label="Cancel" class="mblColorBlue" style="position:absolute;width:45px;left:0;"
					onclick="dijit.registry.byId('calPicker').hide(false)"></div>
			</h1>
			<div id="spin1" dojoType="dojox.mobile.SpinWheelDatePicker"></div>
		</div>
		
		
	<div id="sharePicker" data-dojo-type="dojox.mobile.Opener" data-dojo-props="onShow:onSharePickerOpen">
		<h1 dojoType="dojox.mobile.Heading" label="Share" class="header1" style="width:100%">
			<div dojoType="dojox.mobile.ToolBarButton" label="Done" class="mblColorBlue" style="position:absolute;width:45px;right:0;"
				onclick="dijit.registry.byId('sharePicker').hide(true)"></div>
			<div dojoType="dojox.mobile.ToolBarButton" label="Cancel" class="mblColorBlue" style="position:absolute;width:45px;left:0;"
				onclick="dijit.registry.byId('sharePicker').hide(true)"></div>
		</h1>
		
		<div id="sharePickerScrollView" dojoType="dojox.mobile.ScrollableView" selected="true" height="auto">
			<ul id="sharePickerList" dojoType="dojox.mobile.RoundRectList" select="single" data-dojo-props="">
				
			</ul>
		</div>
	</div>
	
	
	
	
	<div id="sectionPicker" data-dojo-type="dojox.mobile.Opener" data-dojo-props="">
		
		<div dojoType="dojox.mobile.ScrollableView" selected="true" height="auto" id="sectionPickerRoot">
			<ul id="sectionPickerList" dojoType="dojox.mobile.RoundRectList" select="single" data-dojo-props="">
			</ul>
		</div>
	</div>
	
	<div id="dlg_dummy_radio" style="z-index:100"
	     data-dojo-type="dojox.mobile.SimpleDialog"
	     data-dojo-mixins="dojox.mobile._ContentPaneMixin"
	     data-dojo-props=''></div>
	     
	<div id="homeScreenRoot"></div>
	

<script type="text/javascript" src="<%=_dojoPath %>" djConfig="<%=_dojoConfig %>"></script>

<%if(_bootLoader !=null){ %>
	<script type="text/javascript" src="<%=_bootLoader %>"></script>
<%} %>

</body>	