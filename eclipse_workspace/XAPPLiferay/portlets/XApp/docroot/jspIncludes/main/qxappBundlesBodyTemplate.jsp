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
	Application app = appMan.getApplication(xappId, xappUUID, false);
	if(app!=null)
	{
		//app.setIsQXApp(1);
	}
	
	String _path = request.getContextPath();
	String _basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";

	//System.out.println("Loading QXApp Billing Application at \n\t : " + _basePath +"\n\t DojoPath : " + _dojoPath + "\n\t BootLoader : " + _bootLoader);

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

		<div id="loaderBilling"><div class="spinner"></div><div id="loaderInnerBilling" style="direction:ltr;">Loading Prices ... </div></div>
		
		<div id="nativeRoot" data-dojo-type="dijit.layout.ContentPane" data-dojo-props="design:'screenDesign'" splitter="false" style="width:100%;height:<%="auto"%>;overflow:auto;">
		
		 </div>
		
		<div id="billingRoot" data-dojo-type="dijit.layout.ContentPane" data-dojo-props="design:'screenDesign'" splitter="false" style="width:100%;height:<%="auto"%>;overflow:auto;">
		
		 </div>
	
	    <div id="widgetCache"></div>
        <div id="optional_bottom_div"></div>
        <div id="dialog1" data-dojo-type="dijit.Dialog" style="display:none;" data-dojo-props="title:'Floating Modal Dialog from href',style: 'width: 400px;',refreshOnShow:true">
        <label id="errMsg">ErrorMessage</label>
        </div>

<script type="text/javascript" src="<%=_dojoPath %>" djConfig="<%=_dojoConfig %>"></script>

<%if(_bootLoader !=null){ %>
	<script type="text/javascript" src="<%=_bootLoader %>"></script>
<%} %>

<script type="text/javascript">

</script>
</body>
