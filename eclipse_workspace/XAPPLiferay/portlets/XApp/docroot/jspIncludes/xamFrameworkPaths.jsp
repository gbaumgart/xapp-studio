<%@ page import="java.io.*"%>
<%@ page import="java.lang.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%

	
	
	String xappBaseDirectory="lib/";
	String dojoDirectory="dojo/";
	String dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";

	//deprecated : "modulePaths:{'pm' : '../../lib/pm'}"
	String dojoConfig="parseOnLoad:false,baseUrl:'lib',tlmSiblingOfDojo: 0,isDebug:0,mblAlwaysHideAddressBar:false,async:true,mblThemeFiles:['@theme',['cssMobile','deviceMain']]";
	String bootLoader=xappBaseDirectory+"pm/mobile/MobileLoader.js";
	
		//new bootloader	
	bootLoader=xappBaseDirectory+"applications/xappmanager/run.js";
	
	String RTConfig = (String)request.getSession().getAttribute("runTimeConfiguration");
	//RTConfig="release";
	//Adjust paths with run-time-configuration 
	if(RTConfig!=null)
	{
		if(RTConfig.equals("release"))
		{
			bootLoader=null;
			xappBaseDirectory="xam/";
			dojoConfig="async: 1, tlmSiblingOfDojo: 0, baseUrl:'xam', deps:['applications/xappmanager/run'],locale: 'en-us',extraLocale: ['de-de,fr-fr,es-es']";
			dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";
		}
		if(RTConfig.equals("releaseDebug"))
		{
			bootLoader=null;
			xappBaseDirectory="xam/";
			//release doesn't need a boot loader since all is baking into one layer!
			//dojoConfig="parseOnLoad:false,baseUrl:'release',tlmSiblingOfDojo: 0,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['cssTablet','gallery']],modulePaths:{'pm' : '../../lib/pm'}";
			dojoConfig="async: 1, tlmSiblingOfDojo: 0, baseUrl:'xam', deps:['applications/xappmanager/run'],locale: 'en-us',extraLocale: ['de-de,fr-fr,es-es']";
			dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js.uncompressed.js";
		}
	}
	request.getSession().setAttribute("dojoPath", dojoPath);
	request.getSession().setAttribute("dojoConfig", dojoConfig);
	request.getSession().setAttribute("bootLoader", bootLoader);
	request.getSession().setAttribute("xappBaseDirectory", xappBaseDirectory);
	
	
 %>
 