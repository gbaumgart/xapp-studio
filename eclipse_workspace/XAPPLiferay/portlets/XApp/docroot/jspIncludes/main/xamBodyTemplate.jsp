<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<% 
	String _dojoPath=(String)request.getSession().getAttribute("dojoPath");
	String _bootLoader=(String)request.getSession().getAttribute("bootLoader");
	String _dojoConfig=(String)request.getSession().getAttribute("dojoConfig");
	
	String _path = request.getContextPath();
	String _basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";
	

	//System.out.println("Loading XAS Application at \n\t : " + _basePath +"\n\t DojoPath : " + _dojoPath + "\n\t BootLoader : " + _bootLoader);
 %>
 
<body class="claro">

		<div id="loader"><div id="loaderInner" style="direction:ltr;">Loading Application Center ... </div></div>

        <div id="main" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="liveSplitters:true" style="width:auto;height: 600px;margin: 2%">
	    	<!-- Main Tab Container  -->    
	        <div data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region:'center', tabStrip:false" id="mainTabs">
	            
	            <!-- Application Tab   -->
	            <div id="appTab" data-dojo-type="dijit.layout.ContentPane" data-dojo-props='title:"Application Center"' class="applicationManagerTab">

					<!-- Application Tab Container - START  -->
		            <div id="appTabMain" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="liveSplitters:true,design:'sidebar'">
		            
		            	<!-- Application Tab - Left Side  -->
		            	<div data-dojo-type="dijit.layout.AccordionContainer" data-dojo-props="region:'left', splitter:true" id="applicationsRoot" tabIndex="0" style="width:200px">
		            		
				        </div>
		               
		               <div data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region:'center'" id="applicationsSettingsRoot">
		               </div>
		               
	        		</div><!-- Application Tab Container - END  -->
        		</div><!-- Application Tab - END  -->
        	</div><!-- Main Tab Container - END  -->
        	
        	<div data-dojo-type="dijit.layout.TabContainer" id="bottomTabs" data-dojo-props="tabPosition:'bottom', selectedchild:'btab1', region:'bottom',splitter:true, tabStrip:true" style="height:80px;">
							<div id="btab1" data-dojo-type="dijit.layout.ContentPane" data-dojo-props='title:"Actions", style:" padding:5px;"' style="height:80px;">
							<div class="btn00"  data-dojo-type="dijit.form.Button" href="#" onclick="mctx.getWorkflowManager().createNewApplication();return false;" >Create New App</div>
							</div><!-- end:info btab1 -->
			</div>
        	
        </div>
        

		<div id="widgetCache"></div>

        <div id="dialog1" data-dojo-type="dijit.Dialog" style="display:none;"
        data-dojo-props="title:'Floating Modal Dialog from href',style: 'width: 400px;',refreshOnShow:true">
        <label id="errMsg">ErrorMessage</label>
        </div>

<script type="text/javascript" src="<%=_dojoPath %>" djConfig="<%=_dojoConfig %>"></script>

<%if(_bootLoader !=null){ %>
	<script type="text/javascript" src="<%=_bootLoader %>"></script>
<%} %>

</body>
