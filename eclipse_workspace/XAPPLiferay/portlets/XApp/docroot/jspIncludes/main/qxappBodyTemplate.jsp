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
		app.setIsQXApp(1);
	}
	
	String _path = request.getContextPath();
	String _basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";

	//System.out.println("Loading XAS Application at \n\t : " + _basePath +"\n\t DojoPath : " + _dojoPath + "\n\t BootLoader : " + _bootLoader);
	//String forcePlatform="MOBILE_WEB_APP_TABLET";
	String forcePlatform=null;
	String simWidth="330px";
	String mainHeight="545px";
	String totalHeight="740px";
	if(forcePlatform!=null){
		simWidth="1024px";
		mainHeight="768px";
		totalHeight="740px";
	}
	
	/*
	Locale locale = (Locale)request.getSession().getAttribute("org.apache.struts.action.LOCALE");
  	String language = locale.getLanguage();
  	String country = locale.getCountry();
	String _labelContent=LanguageUtil.get(pageContext,"Content");
	*/
	//System.out.println("asd" + _labelContent + " lang " + country);
	
 %>
 
<body class="claro" role="main">

		<div id="loader"><div class="spinner"></div><div id="loaderInner" style="direction:ltr;">Loading Quick-XApp-Studio ... </div></div>
		<div id="main" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design:'screenDesign'" splitter="false" style="width:80%;height:<%=totalHeight%>">
		
			<div data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region:'center', tabStrip:true" id="mainTabs" splitter="false">
	        
	        	<!-- 	main tab -->
				<div class="mainTab" id="mainTab" data-dojo-props="title:'<%=LanguageUtil.get(pageContext,"Content")%>'" data-dojo-type="dijit/layout/BorderContainer" style="width:100%;height:<%=totalHeight%>;height:<%=totalHeight%>">
				
					<!-- div class="" role="banner" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='id:"border1-top", region:"top", style:"background-color: #b39b86;height: 50px;", splitter:false'>
					</div-->
					
					<div class="simulatorBackground" id="simulatorParent" style:"width:<%=simWidth%>;">
					</div>
					
					<div id="myStackContainer" data-dojo-type="dijit/layout/StackContainer" data-dojo-props='id:"ctMainStackPanel", region:"center", style:""'>
					
						<div role="main" id="stackView0" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props='region:"center", style:"padding: 0px;splitter:false"' splitter="false">
						
							<div class="panelBackground ctNavPanel" role="main" id="ctNavPanel" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='region:"left", style:"padding:0px;"' splitter="false">
							
							</div>
							
							<div class="panelContainer ctMainPanel" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='id:"ctMainPanel", region:"center", style:""'>
							</div>
							
						</div>
					</div>
					
					<!-- div class="" role="contentinfo" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='id:"border1-bottom", region:"bottom", style:"background-color: #b39b86; height: 50px;", splitter:true'>
					</div-->
				</div>
				<!--Main Tab End  -->
				
				<!-- 	design tab -->
				<div class="mainTab" id="designTab" data-dojo-props="title:'<%=LanguageUtil.get(pageContext,"Design")%>'" data-dojo-type="dijit/layout/BorderContainer" style="width:100%;height:<%=totalHeight%>">
				
					<div id="simulatorParentDesign" style:"width:<%=simWidth%>">
					</div>


					<div role="main" id="dtStackView0" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props='region:"center", style:"padding: 0px;"'>
						
							<!-- div id="dtTabContainer" data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region:'center', tabStrip:true" splitter="false">
							
							<div id="simulatorParentDesign" role="navigation" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='region:"right", style:"width:<%=simWidth%>;",
							
							
							
								<div id="dtVisualPanel" class="ctMainPanel" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='region:"center",title:"Visuals", style:"height:auto;overflow:visible;"'></div>
								<div id="dtHomePanel" class="panelBorder panelContainer ctMainPanel" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='region:"center",title:"Home Screen", style:""'>Layout Options</div>
								<div id="dtColorsPanel" class="panelBorder panelContainer ctMainPanel" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='region:"center",title:"Colors", style:""'></div>
							</div-->
					</div>
					
				</div>
					
					<!-- div id="dtBottom" class="" role="contentinfo" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='region:"bottom", style:"background-color: #b39b86; height: 50px;", splitter:true'>
					</div-->
					
				<!--Design Tab End  -->
				
				<!-- 	publish info tab -->
				<div class="mainTab" id="settingsTab" data-dojo-props="title:'<%=LanguageUtil.get(pageContext,"Settings")%>'" data-dojo-type="dijit/layout/BorderContainer" style="width:100%;height:<%=totalHeight%>;height:<%=totalHeight%>">
				
					<div role="main" id="stStackView0" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props='region:"center", style:"padding: 0px;"'></div>
					
						<!-- div id="ptMainStackPanel" data-dojo-type="dijit/layout/StackContainer" data-dojo-props='region:"center", style:""'>
							<div id="ptTabContainer" data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region:'center', tabStrip:true" splitter="false">
								<div id="ptGeneralPanel" class="ctMainPanel" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='region:"center",title:"General", style:"height:auto;overflow:visible;"'></div>
							</div>
						</div-->
				</div>
			<div id="filesRoot" style="overflow:hidden !important;" dojoType="dijit.layout.ContentPane" data-dojo-props="title:'<%=LanguageUtil.get(pageContext,"Files")%>'">
			
       		</div>
				
				<!-- 	design tab -->
				<!--  div class="mainTab" id="integrationTab" data-dojo-props="title:'Integrations'" data-dojo-type="dijit/layout/BorderContainer" style="width:100%;height:<%=totalHeight%>;height:<%=totalHeight%>">
							
						<div id="itMainStackPanel" data-dojo-type="dijit/layout/StackContainer" data-dojo-props='region:"center", style:""'>
							<div id="itTabContainer" data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region:'center', tabStrip:true,tabPosition:'left-h'" splitter="false">
								<div id="itJoomla" class="ctMainPanel" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='region:"center",title:"Joomla", style:"height:auto;overflow:visible;"'></div>
								<div id="itWordpress" class="panelBorder panelContainer ctMainPanel" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='region:"center",title:"Wordpress", style:""'>Layout Options</div>
								<div id="itPicassa" class="panelBorder panelContainer ctMainPanel" data-dojo-type="dijit/layout/ContentPane" data-dojo-props='region:"center",title:"Picassa", style:""'>Brand & App Colors</div>
							</div>
					</div>
					
					</div-->
					
	        </div>
        </div>
	
		
		<!-- div id="main" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design:'screenDesign'" style="width:100%;height:<%=totalHeight%>">
		
		

		
		<div id="mainTop" data-dojo-type="dijit.layout.BorderContainer"  region="center" data-dojo-props="design:'screenDesign'" style="width:100%;height: <%=mainHeight %> !important;">
		
		<div data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region:'center', tabStrip:true" id="topTabs" style="padding-left:0px;" splitter="true">
        </div>
        
		<div data-dojo-type="dijit.layout.LayoutContainer" data-dojo-props="region:'left', splitter:true, minSize:20"
        	style="padding-left:0px;width: <%=simWidth%>;" id="simulatorParent" tabIndex="100" attachParent="true" splitter="true">
        </div>
	        
			<div data-dojo-type="dijit.layout.AccordionContainer" data-dojo-props="region:'left', splitter:true, minSize:20" style="padding-left:0px;width: 320px;" id="dataBrowserParent2" tabIndex="-1" splitter="true" toggleSplitterCollapsedSize="20px" region="left" toggleSplitterState="full">
	            
	            <div id="dataBrowserParent" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props='design:"sidebar",title:"Data & Navigation"'>
	
	            	<div id="dataSourceMenu" data-dojo-type="dijit.MenuBar" data-dojo-props="region:'top'">
		                
		                <div  data-dojo-type="dijit.PopupMenuBarItem" data-dojo-props='iconClass:"dijitEditorIcon dijitEditorIconCopy"'>
		                    <span>Register</span>
		                    <div id="dataSourceMenuNew" data-dojo-type="dijit.Menu" ></div>
                        </div>
		                
		                <div  data-dojo-type="dijit.PopupMenuBarItem" data-dojo-props='iconClass:"dijitEditorIcon dijitEditorIconCopy"'>
		                    <span>Templates</span>
                            <div id="dataSourceMenuTemplates" data-dojo-type="dijit.Menu" ></div>
		                </div>
		                
	            	</div>

	            </div>
	        
	        </div-->
        
        
        




        <div id="widgetCache" style="maxHeight:0px;"></div>
        <div id="lightbox"></div>
        <div id="optional_bottom_div"></div>
        <div id="dialog1" data-dojo-type="dijit.Dialog" style="display:none;" data-dojo-props="title:'Error :',style: 'width: 400px;',refreshOnShow:true">
        <label id="errMsg">ErrorMessage</label>
        </div>

<script type="text/javascript" src="<%=_dojoPath %>" djConfig="<%=_dojoConfig %>"></script>

<%if(_bootLoader !=null){ %>
	<script type="text/javascript" src="<%=_bootLoader %>"></script>
<%} %>

<script type="text/javascript">

</script>
</body>
