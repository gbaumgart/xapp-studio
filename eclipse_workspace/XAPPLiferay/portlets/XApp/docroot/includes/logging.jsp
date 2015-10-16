<%@page import="cmx.tools.LiferayContentTools"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<%@page import="jsontype.DojoPackage"%>
<%@page import="cmx.manager.JSPluginManager"%>
<%@ page import="java.io.*"%>
<%@ page import="java.lang.*"%>
<%@page import="cmx.types.Resource"%>
<%@page import="pmedia.utils.StringUtils"%>
<%@page import="flexjson.JSONDeserializer"%>
<%@page import="cmx.types.Resources"%>
<%@page import="java.io.File"%>
<%@page import="cmx.types.FolderKeys"%>
<%@page import="cmx.FolderKeyResolverUtil"%>
<%@page import="cmx.types.Application"%>
<%@page import="cmx.types.ApplicationManager"%>
<%@page import="cmx.tools.ResourceUtil"%>
<%@page import="flexjson.JSONSerializer"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%

	Boolean log = true;

	if(log)
	{
		String ___sysAppName = (String)request.getSession().getAttribute("systemAppName");
		String lgRTConfig = (String)request.getSession().getAttribute("runTimeConfiguration");
		String lgDstLang = "en";
		Locale __rLocale = request.getLocale();
		if(__rLocale!=null)
		{
			lgDstLang = __rLocale.getLanguage();
		}
		String lgAppId = (String)session.getAttribute("appId");
		String lgUuid = (String)session.getAttribute("uuid");
		
		String str = "";
		
		if(___sysAppName!=null){
			str+="Loading : "+___sysAppName + "::"+lgRTConfig + "::";
		}
		
		if(lgAppId!=null && lgUuid!=null)
		{
			User lgUser = LiferayContentTools.getLiferayUserByUUID(lgUuid);
			if(lgUser!=null)
			{
				if(lgUser.getEmailAddress()!=null)
				{
					str+=" "  + lgUser.getEmailAddress()+ "::" + lgUuid + "::" + lgAppId+"::"+lgDstLang+"::";
				}
			}
		}
		String referrer = request.getHeader("referer");
		String ipAddress = request.getRemoteAddr();
		String ua = request.getHeader("User-Agent");
		if(ipAddress!=null&&ipAddress.length()>0)
		{
			str+=ipAddress+"::";
		}
		
		if(referrer!=null && referrer.length() > 0)
		{
			str+=referrer+"::";
		}
		if(ua!=null && ua.length() > 0)
		{
			if(ua.length()>70){
				str+=ua.substring(0,69)+"";
			}
		}
		
		if(!str.contains("bot") && lgUuid!=null && !lgUuid.equals(System.getProperty("demoUser")))
		{
			Date dateNow = new Date();
			System.out.println("\n" +dateNow.toLocaleString() + "::" + str + "\n");
		}
		
	}
	
 %>
 