<%@page import="cmx.types.Application"%>
<%@page import="cmx.types.ApplicationManager"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>
<%@ page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil" %>
<% 
	String _dojoPath=(String)request.getSession().getAttribute("dojoPath");
	String _bootLoader=(String)request.getSession().getAttribute("bootLoader");
	String _dojoConfig=(String)request.getSession().getAttribute("dojoConfig");
	
	String xappId=(String)request.getSession().getAttribute("appId");
	String xappUUID=(String)request.getSession().getAttribute("uuid");
	
	ApplicationManager appMan = ApplicationManager.getInstance();
	Application app = appMan.getApplication(xappUUID,xappId, false);
	if(app!=null)
	{
		//app.setIsQXApp(1);
	}
	
	String _path = request.getContextPath();
	String _basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";

	//System.out.println("Loading XApp-Connect at \n\t : " + _basePath +"\n\t DojoPath : " + _dojoPath + "\n\t BootLoader : " + _bootLoader);

	String forcePlatform=null;
	String simWidth="330px";
	String mainHeight="650px";
	String totalHeight="750px";
	if(forcePlatform!=null)
	{
		simWidth="1024px";
		mainHeight="768px";
		totalHeight="740px";
	}

 %>
 
<body class="claro" role="main">

<div id="main" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design:'screenDesign'" style="width:100%;height:<%=totalHeight%>">

	<div id="mainTop" data-dojo-type="dijit.layout.BorderContainer"  region="center" data-dojo-props="design:'screenDesign'" style="width:100%;height: <%=mainHeight %> !important;overflow:visible;">
		<script type="dojo/method">
		this._splitterClass = "dojox.layout.ToggleSplitter";
	</script>
	
	<div class="centerTabs" data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region:'center', tabStrip:true" id="centerTabs" style="padding-left:0px;eight: <%=mainHeight %> !important;" splitter="true">
	
	<div class="centerTabBorderContainer" id="centerTabBorderContainer" data-dojo-type="dijit.layout.BorderContainer"  region="center"  title="Common"  label="Common" data-dojo-props="design:'screenDesign'" style="width:100%;height: <%=mainHeight %> !important;">
		<script type="dojo/method">
						this._splitterClass = "dojox.layout.ToggleSplitter";
				</script>
		
		<div class="topMainTabs" data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region:'center', tabStrip:true" id="topTabs" style="padding-left:0px;min-width:400px;width:55%;height:550px" splitter="true">
		</div>
		
		<div data-dojo-type="dijit.layout.AccordionContainer" data-dojo-props="region:'left', splitter:true, minSize:20" style="padding-left:0px;width: 320px;" id="dataBrowserParent2" tabIndex="-1" splitter="true" toggleSplitterCollapsedSize="20px" region="left" toggleSplitterState="full">
		</div>
  	</div>
	  
	  <div id="scriptRoot" style="overflow:hidden !important;" dojoType="dijit.layout.ContentPane" title="Scripts">
		<!-- iframe id="scriptFrame" src="http://192.168.1.37:3131/" width="100%" height="100%" scrolling="no" frameborder="0"></iframe-->
	</div>
	
	<div id="filesRoot" style="overflow:hidden !important;" dojoType="dijit.layout.ContentPane" title="File Manager">
	
		</div>
	
	 </div>
	
	
	<div class="bottomTab"  dojoType="dijit.layout.TabContainer" attachParent="true" tabPosition="bottom" tabStrip="true" id="bottomTabs" splitter="true" region="bottom" height="250px" toggleSplitterFullSize="250px" toggleSplitterCollapsedSize="30px" toggleSplitterState="full">
				<div id="bottomTabLog" class="bottomTabLog" dojoType="dijit.layout.ContentPane" title="Log" style="padding:0;background-color: transparent;">
	   			
	</div>
	<div class="bottomTabHelp" dojoType="dijit.layout.ContentPane" title="Help" style="background-color: transparent;">
	<span style="color:white">Help Panel : Soon</span>
				</div>
	</div>
	
	 
	    </div>
	</div>
      
      
      
      
      
      
      
      
     <div id="widgetCache"></div>
     <div id="optional_bottom_div"></div>
     <div id="scriptStandBy" data-dojo-type="dojox.widget.Standby" data-dojo-props="target:'scriptRoot'" style="background-color: black;"></div>
     <div id="filesStandBy" data-dojo-type="dojox.widget.Standby" data-dojo-props="target:'filesRoot'" style="background-color: black;"></div>

        <div id="dialog1" data-dojo-type="dijit.Dialog" style="display:none;"
        data-dojo-props="title:'Floating Modal Dialog from href',style: 'width: 400px;',refreshOnShow:true">
        <label id="errMsg">ErrorMessage</label>
        </div>
        
        
        
        
        
        

<script type="text/javascript" src="<%=_dojoPath %>" djConfig="<%=_dojoConfig %>"></script>

<%if(_bootLoader !=null){ %>
	<script type="text/javascript" src="<%=_bootLoader %>"></script>
<%} %>

<script type="text/javascript">

</script>
</body>
