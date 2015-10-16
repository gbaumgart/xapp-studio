<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<% 

	String _dojoPath=(String)request.getSession().getAttribute("dojoPath");
	String _bootLoader=(String)request.getSession().getAttribute("bootLoader");
	String _dojoConfig=(String)request.getSession().getAttribute("dojoConfig");
	
	String _path = request.getContextPath();
	String _basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";

	//System.out.println("Loading XAppShowCase Application at \n\t : " + _basePath +"\n\t DojoPath : " + _dojoPath + "\n\t BootLoader : " + _bootLoader);
 %>
 
<body class="claro">
        <div id="main" class="xappshowMain" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="liveSplitters:true,design:'sidebar'" style="width:100% !important;height: 730px">
          <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region:'left', splitter:false" class="showcaseParent"  style="padding-left:0px;width: 620px;height:600px;" id="showcaseParent">
	        </div>
	        
	        <div style="padding-left:0px;width: 370px;height:710px;" id="simulatorFrame" class="simulatorFrame">
	        	<div id="appSelectorRoot" data-dojo-attach-point='appSelectorRoot' style="visibility:visible;float:left;width:320px;height:1px;margin-left:25px !important;"></div>
		        <div style="padding-left:0px;width: 320px;height:480px;margin-left:25px;margin-top:110px !important;" id="simulatorParent"></div>
		        <div id="actionRoot" data-dojo-attach-point='actionRoot' style="visibility:visible;float:left;width:320px;height:30px;margin-left:25px !important;"></div>
		    	
		    </div>
		    
      </div>
      
      	<!-- span class='st_sharethis_large' displayText='ShareThis'></span>
		<span class='st_facebook_large' displayText='Facebook'></span>
		<span class='st_twitter_large' displayText='Tweet'></span>
		<span class='st_linkedin_large' displayText='LinkedIn'></span>
		<span class='st_fblike_large' displayText='Facebook Like'></span>
		<span class='st_fbrec_large' displayText='Facebook Recommend'></span>
		<span class='st_plusone_large' displayText='Google +1'></span>
		<span class='st_email_large' displayText='Email'></span-->

        <div id="widgetCache"></div>

<script type="text/javascript" src="<%=_dojoPath %>" djConfig="<%=_dojoConfig %>"></script>

<%if(_bootLoader !=null){ %>
	<script type="text/javascript" src="<%=_bootLoader %>"></script>
<%} %>

<script type="text/javascript">

</script>
</body>
