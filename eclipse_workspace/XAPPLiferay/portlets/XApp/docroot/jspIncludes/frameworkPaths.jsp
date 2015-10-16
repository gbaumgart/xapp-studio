<%@ page import="java.io.*"%>
<%@ page import="java.lang.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%

	
	
	String xappBaseDirectory="lib/";
	String dojoDirectory="dojo/";
	String dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";

	//deprecated : "modulePaths:{'pm' : '../../lib/pm'}"
	
	
	String dojoConfig="parseOnLoad:false,baseUrl:'lib',tlmSiblingOfDojo: 0,isDebug:0,useCustomLogger:false,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['cssMobile','gallery']]";
	String bootLoader=xappBaseDirectory+"xapp/run.js";
	String RTConfig = (String)request.getSession().getAttribute("runTimeConfiguration"); 
	
	//Adjust paths with run-time-configuration 
	if(RTConfig!=null)
	{
		if(RTConfig.equals("release"))
		{
			//release doesn't need a boot loader since all is baking into one layer!
			bootLoader=null;
			xappBaseDirectory="release/";
			//dojoConfig="parseOnLoad:false,baseUrl:'release',tlmSiblingOfDojo: 0,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['cssTablet','gallery']],modulePaths:{'pm' : '../../lib/pm'}";
			//mblThemeFiles:['@theme',['cssMobile','gallery']]
			dojoConfig="async: 1, tlmSiblingOfDojo: 0, baseUrl:'release', deps:['xapp/run']";
			dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";
		}
		
		if(RTConfig.equals("releaseDebug"))
		{
			//release doesn't need a boot loader since all is baking into one layer!
			bootLoader=null;
			xappBaseDirectory="release/";
			//dojoConfig="parseOnLoad:false,baseUrl:'release',tlmSiblingOfDojo: 0,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['cssTablet','gallery']],modulePaths:{'pm' : '../../lib/pm'}";
			dojoConfig="async: 1, tlmSiblingOfDojo: 0, baseUrl:'release', deps:['xapp/run'],mblThemeFiles:['@theme',['cssMobile','gallery']]";
			dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js.uncompressed.js";
		}
		
	}
	
	
	
	
	request.getSession().setAttribute("dojoPath", dojoPath);
	request.getSession().setAttribute("dojoConfig", dojoConfig);
	request.getSession().setAttribute("bootLoader", bootLoader);
	request.getSession().setAttribute("xappBaseDirectory", xappBaseDirectory);
	
	//System.out.println("setting BaseDirectory to : " +basePath+xappBaseDirectory );
	
	
	
	
 %>
 