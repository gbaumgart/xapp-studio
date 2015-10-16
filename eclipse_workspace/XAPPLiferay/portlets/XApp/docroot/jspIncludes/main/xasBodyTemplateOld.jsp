<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<% 
	String _dojoPath=(String)request.getSession().getAttribute("dojoPath");
	String _bootLoader=(String)request.getSession().getAttribute("bootLoader");
	String _dojoConfig=(String)request.getSession().getAttribute("dojoConfig");
	
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
	
 %>
 
<body class="claro" role="main">

		<div id="loader"><div id="loaderInner" style="direction:ltr;">Loading XApp-Studio ... </div></div>
		
		<div id="main" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design:'screenDesign'" style="width:100%;height:<%=totalHeight%>">
		
        <!-- div data-dojo-type="dijit.layout.TabContainer" id="bottomTabs"	data-dojo-props="tabPosition:'bottom', selectedchild:'btab1', region:'bottom',splitter:true, tabStrip:true" style="height:90px;">
			<div id="btab1" data-dojo-type="dijit.layout.ContentPane" data-dojo-props='title:"Info", style:" padding:10px;"'>
			
			liveSplitters:true
			live:false
			</div>
		</div-->

		
		<div id="mainTop" data-dojo-type="dijit.layout.BorderContainer"  region="center" data-dojo-props="design:'screenDesign'" style="width:100%;height: <%=mainHeight %> !important;">
		<script type="dojo/method">
			this._splitterClass = "dojox.layout.ToggleSplitter";
		</script>
		
        <div data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region:'center', tabStrip:true" id="topTabs" style="padding-left:0px;" splitter="true">
        </div>
        
		<div data-dojo-type="dijit.layout.AccordionContainer" data-dojo-props="region:'left', splitter:true, minSize:20"
        	style="padding-left:0px;width: <%=simWidth%>;" id="simulatorParent" tabIndex="100" attachParent="true" splitter="true">
        </div>
	        
		<!-- div id="simExpander" dojoType="dojox.layout.ExpandoPane" splitter="true" title="" region="left" maxWidth="330" style="width:330px;">
	        
		</div-->
		    
		<div data-dojo-type="dijit.layout.AccordionContainer" data-dojo-props="region:'left', splitter:true, minSize:20"
	        style="padding-left:0px;width: 320px;" id="dataBrowserParent2" tabIndex="-1" splitter="true" toggleSplitterCollapsedSize="20px" region="left" toggleSplitterState="full">
	            
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
	        </div>
        
        
        
        <div dojoType="dijit.layout.TabContainer" attachParent="true" tabPosition="bottom" tabStrip="true" id="bottomTabs" splitter="true" region="bottom" toggleSplitterFullSize="180px" toggleSplitterCollapsedSize="30px" toggleSplitterState="full">
					<div dojoType="dijit.layout.ContentPane" title="Help">
					</div>
		</div>

		<!-- div dojoType="dojox.layout.ExpandoPane"
				splitter="true" 
				duration="125" 
				region="bottom" 
				title="Bottom Section" 
				id="bottomPane" 
				maxWidth="275" 
				style="height: 150px;">
				<div dojoType="dijit.layout.TabContainer" attachParent="false" tabPosition="bottom" tabStrip="true" id="bottomTabs">
					<div dojoType="dijit.layout.ContentPane" title="Help">
					</div>
				</div>
			</div-->
      
      </div>
      </div>
      
      	        <div id="widgetCache"></div>
        <div id="optional_bottom_div"></div>

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

<!-- span class='st_sharethis_large' displayText='ShareThis'></span>
		<span class='st_facebook_large' displayText='Facebook'></span>
		<span class='st_twitter_large' displayText='Tweet'></span>
		<span class='st_linkedin_large' displayText='LinkedIn'></span>
		<span class='st_fblike_large' displayText='Facebook Like'></span>
		<span class='st_fbrec_large' displayText='Facebook Recommend'></span>
		<span class='st_plusone_large' displayText='Google +1'></span>
		<span class='st_email_large' displayText='Email'></span-->
