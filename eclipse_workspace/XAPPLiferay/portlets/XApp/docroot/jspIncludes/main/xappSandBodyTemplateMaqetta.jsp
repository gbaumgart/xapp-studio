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
 
<body class="claro">
<div id="main" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design:'screenDesign'" style="width:100%;height:<%=totalHeight%>">
</div>

<script type="text/javascript" src="<%=_dojoPath %>" djConfig="<%=_dojoConfig %>"></script>

<%if(_bootLoader !=null){ %>
	<script type="text/javascript" src="<%=_bootLoader %>"></script>
<%} %>

<script type="text/javascript">

</script>
</body>
