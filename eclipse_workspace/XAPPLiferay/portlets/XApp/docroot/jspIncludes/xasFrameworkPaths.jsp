<%@ page import="java.io.*"%>
<%@ page import="java.lang.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%

	
	
	String xappBaseDirectory="lib/";
	String dojoDirectory="dojo/";
	String dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";

	//deprecated : "modulePaths:{'pm' : '../../lib/pm'}"
	
	String packages = "packages: [ { name: 'cxapp', location:'../../CMAC/11166763-e89c-44ba-aba7-4e9f4fdf97a9/apps/myeventsapp12f/cxapp'} ]";
	
	//String dojoConfig="parseOnLoad:false,baseUrl:'lib',tlmSiblingOfDojo: 0,isDebug:0,mblAlwaysHideAddressBar:false,async:true,mblThemeFiles:['@theme',['xasCSSMobile','gallery']],has:{'dojo-undef-api': true}," + packages ;
	String dojoConfig="parseOnLoad:false,baseUrl:'lib',tlmSiblingOfDojo: 0,isDebug:0,mblAlwaysHideAddressBar:false,async:true,mblThemeFiles:['@theme',['xasCSSMobile','gallery']],has:{'dojo-undef-api': true}";
	String bootLoader=xappBaseDirectory+"xas/run.js";
	String RTConfig = (String)request.getSession().getAttribute("runTimeConfiguration");
	
	//Adjust paths with run-time-configuration 
	if(RTConfig!=null)
	{
			
		//release doesn't need a boot loader since all is baking into one layer!
		if(RTConfig.equals("release"))
		{
			bootLoader=null;
			xappBaseDirectory="xas/";
			//dojoConfig="parseOnLoad:false,baseUrl:'release',tlmSiblingOfDojo: 0,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['cssTablet','gallery']],modulePaths:{'pm' : '../../lib/pm'}";
			dojoConfig="async: 1, tlmSiblingOfDojo: 0, baseUrl:'xas', deps:['applications/xappstudio/run'],mblThemeFiles:['@theme',['xasCSSMobile','gallery']],locale: 'en-us',extraLocale: ['de-de,fr-fr,es-es']";
			dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";
		}
		if(RTConfig.equals("releaseDebug"))
		{
			bootLoader=null;
			xappBaseDirectory="xas/";
			//dojoConfig="parseOnLoad:false,baseUrl:'release',tlmSiblingOfDojo: 0,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['cssTablet','gallery']],modulePaths:{'pm' : '../../lib/pm'}";
			dojoConfig="async: 1, tlmSiblingOfDojo: 0, baseUrl:'xas', deps:['applications/xappstudio/run'],mblThemeFiles:['@theme',['xasCSSMobile','gallery']],locale: 'en-us',extraLocale: ['de-de,fr-fr,es-es']";
			dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js.uncompressed.js";
		}
	}
	request.getSession().setAttribute("dojoPath", dojoPath);
	request.getSession().setAttribute("dojoConfig", dojoConfig);
	request.getSession().setAttribute("bootLoader", bootLoader);
	request.getSession().setAttribute("xappBaseDirectory", xappBaseDirectory);
	
 %>
 