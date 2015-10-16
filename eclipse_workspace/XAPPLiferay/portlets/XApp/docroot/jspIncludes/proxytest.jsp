<%@page import="cmx.server.FileServer"%>
<%@page import="net.sf.j2ep.ProxyFilter"%>
<%@page import="net.sf.j2ep.servers.ServerStatusChecker"%>
<%@page import="net.sf.j2ep.rules.DirectoryRule"%>
<%@page import="net.sf.j2ep.servers.BaseServer"%>
<%@ page import="java.io.*"%>
<%@ page import="java.lang.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%

/*
	BaseServer baseServer = new BaseServer();
	baseServer.setDomainName("192.168.1.37:8888");
	baseServer.setPath("/dojo");
	baseServer.setIsRewriting("true");
	
	DirectoryRule rule = new DirectoryRule();
	
	rule.setDirectory("dojo");
	baseServer.setRule(rule);
	
	ProxyFilter.getServerChain().addServer(baseServer);
	*/
	//ServerStatusChecker statusChecker = new ServerStatusChecker(listener, 1);
	
	BaseServer s = ProxyFilter.getServer("192.168.1.37:3131", "/cloud", "/cloud");
	if(s!=null){
		//System.out.println("got s for news");
	
	}else
	{
	
		FileServer baseServer = new FileServer();
		baseServer.setDomainName("192.168.1.37:3131");
		baseServer.setPath("/PMaster/xasweb/");
		baseServer.setLocalPath("/PMaster/xasweb/");
		baseServer.setIsRewriting("true");
		DirectoryRule rule = new DirectoryRule();
		rule.setDirectory("/xasweb");
		
		baseServer.setRule(rule);
		ProxyFilter.getServerChain().addServer(baseServer);		
	}
	
	/*
	BaseServer s = ProxyFilter.getServer("192.168.1.37:8888", "", "ibiza");
	if(s!=null){
		System.out.println("got s for news");
	
	}else
	{
		BaseServer baseServer = new BaseServer();
		baseServer.setDomainName("192.168.1.37:8888");
		baseServer.setPath("/dojo");
		baseServer.setIsRewriting("true");
		DirectoryRule rule = new DirectoryRule();
		rule.setDirectory("ibiza");
		baseServer.setRule(rule);
		ProxyFilter.getServerChain().addServer(baseServer);		
	}
	*/
	
	

 %>
 