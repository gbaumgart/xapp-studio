<%@ page import="java.io.*"%>
<%@ page import="java.lang.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%


    String _path = request.getContextPath();
	String _basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";
	if(!_basePath.contains("8080"))
	{
		_basePath=_basePath.replace(":80","");
	}
	
	//_basePath+="XApp-portlet/";
	//System.out.println("lib base:: " + _basePath);
	
	String xappBaseDirectory="lib/";
	String dojoDirectory="dojo/";
	String dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";
	
	String dstLang = "en";
	Locale rLocale = request.getLocale();
	if(rLocale!=null)
	{
		dstLang = rLocale.getLanguage();
	}
	
	
	String dojoConfig="parseOnLoad:false,baseUrl:'lib',tlmSiblingOfDojo: 0,isDebug:0,mblAlwaysHideAddressBar:false,async:true,mblThemeFiles:['@theme',['xasCSSMobile','gallery']],locale: '" +dstLang +"',extraLocale: ['de-de,fr-fr,es-es']";
	String bootLoader=xappBaseDirectory+"qxapp/run.js";
	String RTConfig = (String)request.getSession().getAttribute("runTimeConfiguration");
	//RTConfig="release";
	
	
	
	//Adjust paths with run-time-configuration 
	if(RTConfig!=null)
	{
		if(RTConfig.equals("release"))
		{
			bootLoader=null;
			xappBaseDirectory="qxapp/";
		
			//release doesn't need a boot loader since all is baking into one layer!
			//dojoConfig="parseOnLoad:false,baseUrl:'release',tlmSiblingOfDojo: 0,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['cssTablet','gallery']],modulePaths:{'pm' : '../../lib/pm'}";
			dojoConfig="async: 1, tlmSiblingOfDojo: 0, baseUrl:'" + "qxapp', deps:['qxapp/run'],mblThemeFiles:['@theme',['xasCSSMobile','gallery']],locale: 'en-us',extraLocale: ['de-de,fr-fr,es-es']";
			dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js";
		}
		if(RTConfig.equals("releaseDebug"))
		{
			bootLoader=null;
			xappBaseDirectory="qxapp/";
			//release doesn't need a boot loader since all is baking into one layer!
			//dojoConfig="parseOnLoad:false,baseUrl:'release',tlmSiblingOfDojo: 0,mblAlwaysHideAddressBar:true,async:true,mblThemeFiles:['@theme',['cssTablet','gallery']],modulePaths:{'pm' : '../../lib/pm'}";
			dojoConfig="async: 1, tlmSiblingOfDojo: 0, baseUrl:'" +_basePath +"qxapp', deps:['qxapp/run'],mblThemeFiles:['@theme',['xasCSSMobile','gallery']],locale: 'en-us',extraLocale: ['de-de,fr-fr,es-es']";
			dojoPath=xappBaseDirectory+dojoDirectory+"dojo.js.uncompressed.js";
		}
	}
	request.getSession().setAttribute("dojoPath",  _basePath + dojoPath);
	request.getSession().setAttribute("dojoConfig", dojoConfig);
	request.getSession().setAttribute("bootLoader", bootLoader);
	request.getSession().setAttribute("xappBaseDirectory", xappBaseDirectory);
	
 %>
 