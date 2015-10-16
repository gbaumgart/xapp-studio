<%@page import="pmedia.utils.UAgentInfo"%>
<%@page import="net.sourceforge.wurfl.core.Device"%>
<%@page import="java.io.File"%>
<%@page import="cmx.tools.ResourceUtil"%>
<%@page import="cmx.tools.ServletTools"%>
<%@page import="pmedia.utils.CITools"%>
<%@page import="pmedia.types.ApplicationMetaDataKeys"%>
<%@page import="cmx.types.ConfigurableInformation"%>
<%@page import="pmedia.types.ApplicationMetaData"%>
<%@page import="cmx.types.ApplicationManager"%>
<%@page import="cmx.types.Application"%>
<!-- %@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%-->

<%

	String  muuid =request.getParameter("uuid");
    String  mappId = request.getParameter("appId");
    
    if(mappId==null){
    	String __appId = (String)session.getAttribute("appId");
		if(__appId!=null)
		{
			mappId=__appId;			
		} 
	}
	
	if(muuid!=null)
	{
		muuid=muuid.substring(0, 36);		
	}
	if(muuid==null){	
    	String __uuid = (String)session.getAttribute("uuid");
		if(__uuid!=null){
			muuid=__uuid;			
		}
    }
    
    String fbAppId = null;
    String fbAppSecret = null;
    String launchScreen5 = "";
    
    String splash = "http://%WWW_SERVER%/%XAPP%/Assets/backgrounds/meta/Default.png";
    
    ApplicationManager appManager = ApplicationManager.getInstance();
    Application app=appManager.getApplication(muuid, mappId, false);
    String iconUrl = "images/MobileShow_App_114.png";
    String title = null;
    
    String deviceWidth="320";
    String deviceHeight="480";
    Device wurflDevice = ServletTools.getWurflDevice(request, response, getServletContext());
    if(wurflDevice!=null){
    	String _w =  wurflDevice.getCapability("resolution_width");
    	String _h =  wurflDevice.getCapability("resolution_height");
    	
    	//System.out.println("wurfl device with : " + _w);
    	
    	
    }
    
    String wDevice="iPhone OS";
    String wDeviceIn = ServletTools.getDevice(request, response, getServletContext());
    if(wDeviceIn!=null && wDeviceIn.length()>0){
    	wDevice = wDeviceIn;
    }
    
    if(wDevice!=null && wDevice.length()>0){
    	System.out.println("device " + wDevice);
    }
    
    
    if(app!=null){
        iconUrl=ApplicationManager.findAppIcon(muuid, mappId, null);
        
        
        ApplicationMetaData appMeta = app.getMetaData();
		if(appMeta!=null){
			String icon = null;
			
			ConfigurableInformation cfbAppId = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.FB_SHARE_APP_ID);
			if(cfbAppId!=null){
				if(cfbAppId.getValue()!=null && cfbAppId.getValue().length()>0 ){
					fbAppId=cfbAppId.getValue();
					session.setAttribute("fbAppId", fbAppId);
				}
			}
			ConfigurableInformation cfbAppSec = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.FB_SHARE_APP_SECRET);
			if(cfbAppSec!=null){
				if(cfbAppSec.getValue()!=null && cfbAppSec.getValue().length()>0 ){
					fbAppSecret=cfbAppSec.getValue();
					session.setAttribute("fbAppSecret", fbAppSecret);
				}
			}
			
			/***
			*/
			ConfigurableInformation cLaunchScreen = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_2);
			if(cLaunchScreen!=null){
				if(cLaunchScreen.getValue()!=null && cLaunchScreen.getValue().length()>0 ){
					
					launchScreen5=cLaunchScreen.getValue();
					
					String launchScreenAbsPath = ResourceUtil.resolveConstantsAbsolute("%XUSER%/"+launchScreen5, muuid, mappId);
					File launchScreenFile = new File(launchScreenAbsPath);
					if(wDevice!=null && wDevice.length()>0){
							if(launchScreenFile.exists()){
								if(wDevice.equals("iPhone OS") || wDevice.equals("Android")){
									splash="http://%WWW_SERVER%/CMAC/" + muuid  +"/apps/"+launchScreen5;
								}
							}
					}
					//System.out.println("launch screen : " + launchScreen5 + " at " + launchScreenAbsPath);
					//session.setAttribute("fbAppSecret", fbAppSecret);
				}
			}
			
			UAgentInfo uainfo = new UAgentInfo(request);
			if(uainfo.detectIpad()){
				//System.out.println("seems ipad!");
				ConfigurableInformation cLaunchScreenIPad = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.TABLET_LAUNCH_2);
				if(cLaunchScreenIPad!=null){
					if(cLaunchScreenIPad.getValue()!=null && cLaunchScreenIPad.getValue().length()>0 ){
						
						launchScreen5=cLaunchScreenIPad.getValue();
						String launchScreenAbsPath = ResourceUtil.resolveConstantsAbsolute("%XUSER%/"+launchScreen5, muuid, mappId);
						File launchScreenFile = new File(launchScreenAbsPath);
						if(launchScreenFile.exists()){
							if(wDevice.equals("iPhone OS") || wDevice.equals("Android")){
								splash="http://%WWW_SERVER%/CMAC/" + muuid  +"/apps/"+launchScreen5;
							}
						}
					}
				}
			}
			
			
			
			ConfigurableInformation titleCI = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_TITLE);
			if(titleCI !=null){
				if(titleCI.getValue()!=null && titleCI.getValue().length()>0 ){
					title=titleCI.getValue();
					//session.setAttribute("fbAppSecret", fbAppSecret);
				}
			}
		}
		
		
		
		if(launchScreen5!=null)
		{
			launchScreen5 = ApplicationManager.getInstance().toWebPath(muuid, mappId, launchScreen5);				
		}        
		
		
		splash = ResourceUtil.resolveConstants(splash,muuid,mappId);
		
		//System.out.println("splash screen : " + splash);
		session.setAttribute("splashScreen", splash);
		
		
		//System.out.println("launch5 : " + launchScreen5);//width=device-width,
    }
    
    
 %>
 
     <style>
     	#loadDiv{
     		background-image:url('<%=splash%>');
     		background-size:100% 100%;
     		width:100% !important;
     	}
     </style>
 
 	<script type="text/javascript">
	 	var appIcon="<%=iconUrl%> ";
 	</script>

<%if(title!=null){
 %>
 	<title><%=title %></title>
 <%} %>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="viewport" id="viewPortMeta" content="initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no"/>
<!-- meta name="apple-mobile-web-app-status-bar-style" content="black"/-->

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<link rel="apple-touch-icon" href="<%=iconUrl%> "/>
<link rel="apple-touch-icon" sizes="114x114" href="<%=iconUrl%>"/>
<link rel="apple-touch-startup-image" href="<%=launchScreen5%>" sizes="640x1096">
<link rel="shortcut icon" href="<%=iconUrl%>"/>
