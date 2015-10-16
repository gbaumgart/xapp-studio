<%@ page import="java.io.*"%>
<%@ page import="java.lang.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%

	String xappBaseDirectory="lib/";
	String dojoDirectory="dojo/";
	String dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";

	//deprecated : "modulePaths:{'pm' : '../../lib/pm'}"
	
	String dstLang = "en";
	Locale rLocale = request.getLocale();
	if(rLocale!=null)
	{
		dstLang = rLocale.getLanguage();
	}
	
	if(!dstLang.equals("en") && !dstLang.equals("es") && !dstLang.equals("de") &&  !dstLang.equals("fr"))
	{	
		dstLang = "en";
	}
	
	String dojoConfig="parseOnLoad:false,baseUrl:'lib',tlmSiblingOfDojo: 0,isDebug:0,async:true,locale: '" + dstLang + "',extraLocale: ['de-de,fr-fr,es-es']";
	
	//String dojoConfig="parseOnLoad:false,baseUrl:'lib',tlmSiblingOfDojo: 0,isDebug:0,mblAlwaysHideAddressBar:true,async:true";
	String bootLoader=xappBaseDirectory+"qxappbilling/run.js";
	String RTConfig = (String)request.getSession().getAttribute("runTimeConfiguration");
	//RTConfig="release";
	
	
	//Adjust paths with run-time-configuration 
	if(RTConfig!=null)
	{
		if(RTConfig.equals("release"))
		{
			bootLoader=null;
			xappBaseDirectory="qxappbilling/";
		
			//release doesn't need a boot loader since all is baking into one layer!
			//dojoConfig="parseOnLoad:false,baseUrl:'release',tlmSiblingOfDojo: 0,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['cssTablet','gallery']],modulePaths:{'pm' : '../../lib/pm'}";
			dojoConfig="async: 1, tlmSiblingOfDojo: 0, baseUrl:'qxappbilling', deps:['qxappbilling/run'],locale: '" + dstLang +"',extraLocale: ['de-de,fr-fr,es-es']";
			dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";
		}
		if(RTConfig.equals("releaseDebug"))
		{
			bootLoader=null;
			xappBaseDirectory="qxappbilling/";
			//release doesn't need a boot loader since all is baking into one layer!
			//dojoConfig="parseOnLoad:false,baseUrl:'release',tlmSiblingOfDojo: 0,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['cssTablet','gallery']],modulePaths:{'pm' : '../../lib/pm'}";
			dojoConfig="async: 1, tlmSiblingOfDojo: 0, baseUrl:'qxappbilling', deps:['qxappbilling/run'],locale: '" + dstLang +"',extraLocale: ['de-de,fr-fr,es-es']";
			dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js.uncompressed.js";
		}
	}
	request.getSession().setAttribute("dojoPath", dojoPath);
	request.getSession().setAttribute("dojoConfig", dojoConfig);
	request.getSession().setAttribute("bootLoader", bootLoader);
	request.getSession().setAttribute("xappBaseDirectory", xappBaseDirectory);
	
 %>
 