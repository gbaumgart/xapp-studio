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

	//System.out.println("Loading QXAM Application at \n\t : " + _basePath +"\n\t DojoPath : " + _dojoPath + "\n\t BootLoader : " + _bootLoader);

	String forcePlatform=null;
	String simWidth="330px";
	String mainHeight="545px";
	String totalHeight="740px";
	if(forcePlatform!=null)
	{
		simWidth="1024px";
		mainHeight="768px";
		totalHeight="740px";
	}

 %>
 
<body id='root' class="claro" role="main">

		<div id="loader"><div class="spinner"></div><div id="loaderInner" style="direction:ltr;">Loading Applications ... </div></div>
		<div id="applicationsRoot" data-dojo-type="dijit.layout.ContentPane" data-dojo-props="design:'screenDesign'" splitter="false" style="width:95%;height:<%="auto"%>;overflow:auto;">
		
		 </div>
	
	    <div id="widgetCache"></div>
        <div id="optional_bottom_div"></div>
        <div id="dialog1" data-dojo-type="dijit.Dialog" style="display:none;" data-dojo-props="title:'Floating Modal Dialog from href',style: 'width: 400px;',refreshOnShow:true">
        <label id="errMsg">ErrorMessage</label>
        <div id="rootStandBy" data-dojo-type="dojox.widget.Standby" data-dojo-props="target:'root'" style="background-color: black;"></div>
        </div>

<script type="text/javascript" src="<%=_dojoPath %>" djConfig="<%=_dojoConfig %>"></script>

<%if(_bootLoader !=null){ %>
	<script type="text/javascript" src="<%=_bootLoader %>"></script>
<%} %>

<script type="text/javascript">

</script>
</body>
