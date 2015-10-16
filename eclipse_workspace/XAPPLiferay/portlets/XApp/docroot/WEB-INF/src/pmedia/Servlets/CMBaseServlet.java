package pmedia.Servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import pmedia.DataManager.ServerCache;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.PagingInfo;
import pmedia.types.PictureTransformOptions;
import pmedia.utils.ECMContentSourceTypeTools;

import cmx.cache.DataSourceCache;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class CMBaseServlet extends javax.servlet.http.HttpServlet 
{
	
	
	public Application application;
	public ApplicationManager appManager;
	public String appIdentifier;
	public String uuid;
	public String lang;
	public String userAgent;
	public String dataSource;
	public String rtConfig;
	public String scope;
	public DataSourceBase ds;
	public DataSourceCache dataSourceCache;
	public ECMContentSourceType cType;
	
	public PagingInfo pagingInfo;
	
	public String hardwarePlatform;
	public Boolean isHybrid=false;
	
	
	public DataSourceBase getDs() {	return ds;	}
	HttpServletRequest _request; 
	HttpServletResponse _response;
	public void setDs(DataSourceBase ds) {this.ds = ds;	}

	public int refId;
	public String ref;
	public Boolean preventCache=false;
	public String callback;
	
	public void cacheResponse(String key, String value)
	{		
		if(key==null){
			
			if(_request !=null &&_request.getRequestURI()!=null){
				key = _request.getQueryString();
			}
		}
		
		if(lang!=null){
			key+="&lang="+lang;
		}
		
		Cache cache = getCache();
		if(cache!=null){
			Element element = new Element(key, value);
			cache.put(element);
		}
	}
	public static void dropCacheMain(String key)
	{		
		Cache cache = getMainCache();
		if(cache!=null){
			Element element = cache.get(key);
			if(element!=null){
				System.out.println("drop cache for key : " + key);
				cache.remove(key);
			}
		}
	}
	
	public String dropCache(String key)
	{		
		if(key==null){
			
			if(_request !=null &&_request.getRequestURI()!=null){
				key = _request.getQueryString();
			}
		}
		
		if(lang!=null){
			key+="&lang="+lang;
		}
		
		Cache cache = getCache();
		if(cache!=null){
			Element element = cache.get(key);
			if(element!=null){
				System.out.println("drop cache for key : " + key);
				cache.remove(key);
			}
		}
		
		return null;
	}
	public String getCachedResponse(String key)
	{		
		if(key==null){
			
			if(_request !=null &&_request.getRequestURI()!=null){
				key = _request.getQueryString();
			}
		}
		
		if(lang!=null){
			key+="&lang="+lang;
		}
		
		Cache cache = getCache();
		if(cache!=null){
			Element element = cache.get(key);
			if(element!=null){
				Serializable value = element.getValue();
				if(value!=null){
					System.out.print("have cached response : " + key);
					return value.toString();
				}
			}
		}
		
		return null;
	}

	public static Cache getMainCache(){
		
		if(ServerCache.cacheManager!=null){
			Cache cache = ServerCache.cacheManager.getCache("servletCache");
			if(cache!=null){
				return cache;
			}
		}
		String	configPath=System.getProperty("ServletPath");
		try {
			configPath +="config/normal.xml";
			if(ServerCache.cacheManager==null){
				ServerCache.cacheManager = new CacheManager(configPath);
			}
			if(ServerCache.cache==null){
				ServerCache.cache=ServerCache.cacheManager.getCache("firstcache");
			}	
		} catch (Exception e) {
			
		}
		
		if(ServerCache.cache!=null){
			return ServerCache.cache;
		}
		return null;
	}

	public Cache getCache(){
		
		if(ServerCache.cacheManager!=null){
			Cache cache = ServerCache.cacheManager.getCache("servletCache");
			if(cache!=null){
				return cache;
			}
		}
		
		
		
		String	configPath=System.getProperty("ServletPath");
			
		try {
			configPath +="config/normal.xml";
			if(ServerCache.cacheManager==null){
				ServerCache.cacheManager = new CacheManager(configPath);
			}
			if(ServerCache.cache==null){
				ServerCache.cache=ServerCache.cacheManager.getCache("firstcache");
			}	
		} catch (Exception e) {
			
		}
		
		if(ServerCache.cache!=null){
			return ServerCache.cache;
		}
		
		System.out.println("have no cache");
		
		return null;
		/*if(cManager==null){
			cManager = CacheManager.create();
		}
		*/
		//CacheManager manager = CacheManager.getInstance();
		
		//Cache cache = manager.getCache("");
	}
	
	public void setExludes(JSONSerializer serializer)
	{
		
	}
	public void sendOutput(HttpServletRequest request , HttpServletResponse response,String serialized)
	{
		//response.setHeader("Content-Type", "text/text; charset=UTF-8");
		if(callback!=null)
		{
			serialized = callback + "(" + serialized + ")"; 
		}
		
		
		OutputStream out = null;
		String encodings = request.getHeader("Accept-Encoding");
	    if (encodings != null && encodings.indexOf("gzip") != -1) {

	    	// Go with GZIP
		      response.setHeader("Content-Encoding", "gzip");
		      try {
				out = new GZIPOutputStream(response.getOutputStream());
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
	    }else if (encodings != null && encodings.indexOf("compress") != -1) 
	    {
	      // Go with ZIP
	      response.setHeader("Content-Encoding", "x-compress");
	      try {
			out = new ZipOutputStream(response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		      try {
				((ZipOutputStream)out).putNextEntry(new ZipEntry("dummy name"));
			} catch (IOException e) {
				e.printStackTrace();
			}
	    
	    }
	    else {
	      // No compression
	      try {
			out = response.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	    response.setHeader("Vary", "Accept-Encoding");
	    
	    byte[] stringByte = serialized.getBytes();
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(serialized.length());
	    
	    if(serialized!=null){
		    try {
				out.write(serialized.getBytes(Charset.forName("UTF-8")));
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();//e.printStackTrace();
				return404Page();
			}
	    }else{
	    	returnEmptyList();
	    }
	    /*
	    if(serialized!=null){
		    try {
				out.write(serialized.getBytes(Charset.forName("UTF-8")));
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();//e.printStackTrace();
				return404Page();
			}
	    }else{
	    	return404Page();
	    	
	    }
	    */
	    try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    cacheResponse(null,serialized);
	}
	
	public void sendCachedOutput(HttpServletRequest request , HttpServletResponse response,String serialized)
	{
		if(callback!=null)
		{
			serialized = callback + "(" + serialized + ");"; 
		}
		
		
		OutputStream out = null;
		String encodings = request.getHeader("Accept-Encoding");
	    if (encodings != null && encodings.indexOf("gzip") != -1) {

	    	// Go with GZIP
		      response.setHeader("Content-Encoding", "gzip");
		      try {
				out = new GZIPOutputStream(response.getOutputStream());
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
	    }else if (encodings != null && encodings.indexOf("compress") != -1) 
	    {
	      // Go with ZIP
	      response.setHeader("Content-Encoding", "x-compress");
	      try {
			out = new ZipOutputStream(response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		      try {
				((ZipOutputStream)out).putNextEntry(new ZipEntry("dummy name"));
			} catch (IOException e) {
				e.printStackTrace();
			}
	    
	    }
	    else {
	      // No compression
	      try {
			out = response.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	    response.setHeader("Vary", "Accept-Encoding");
	    
	    byte[] stringByte = serialized.getBytes();
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(serialized.length());
	    
	    if(serialized!=null){
		    try {
				out.write(serialized.getBytes(Charset.forName("UTF-8")));
			} catch (IOException e) 
			{
				e.printStackTrace();//e.printStackTrace();
				return404Page();
			}
	    }else{
	    	returnEmptyList();
	    }
	    try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void return404Page()
	{
		try {
			_response.getWriter().write("Error");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void returnEmptyList()
	{
		JSONSerializer serializer = new JSONSerializer();
		CList list = new CList();
		list.setItems(new ArrayList<CListItem>());
		list.sourceType = ""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown);
		list.setTitle("No Data");
		String jsonres = serializer.deepSerialize(list);
    	if(_request!=null && _response!=null)
    	{
    		sendOutput(_request, _response,jsonres);	
    	}else{
    		System.out.println("returnEmptyList : had no request & response object"); 
    	}
	}
	
	public PictureTransformOptions getPictureOptionsFromSession(HttpSession session)
	{
		PictureTransformOptions result = PictureTransformOptions.defaultOptions();
		String screenWidth = (String)session.getAttribute("VAR_SCREENWIDTH");
		if(screenWidth!=null)
		{
			result.resizeWidth = screenWidth;
		}
		
		return result;

	}
	
	public void getCMObjects(HttpServletRequest request, HttpServletResponse response,Boolean storeInSession) throws ServletException, IOException
    {
		_response = response;
		_request= request;
		/***
		 * Determine parameters from request parameters first, then session !
		 */
		
		
		/***
		 * Application Identfier
		 */
		appIdentifier = request.getParameter("appIdentifier");
		if(appIdentifier==null)
		{
			appIdentifier=(String)request.getSession().getAttribute("appIdentifier");
			
			if(appIdentifier!=null && appIdentifier.length() > 0 )
			{
				if(storeInSession){
					request.getSession().setAttribute("appIdentifier", appIdentifier);
				}
			}
		}
		
		isHybrid=request.getParameter("hybrid")!=null;
		
		
		if(rtConfig==null)
		
		
		rtConfig = request.getParameter("rtConfig");
		if(rtConfig==null)
		{
			rtConfig=System.getProperty("defaultRuntimeConfiguration");
		}
		
		String rtConfig2 = request.getParameter("runTimeConfiguration");
		if(rtConfig2!=null)
		{
			rtConfig=""+rtConfig2;
		}
		
		scope = request.getParameter("scope");
		
		/*****
		 * Applications Unique User Id
		 */
		uuid = request.getParameter("uuid");
		try {
		
			if(uuid!=null){
				uuid=uuid.substring(0, 36);
			}
		} catch (Exception e) {
			uuid="";
		}
		
		
		
		String _pC = request.getParameter("preventCache");
		if(_pC!=null){
			preventCache=true;
		}
		preventCache=true;
		if(uuid==null)
		{
			uuid=(String)request.getSession().getAttribute("uuid");
			
			if(uuid!=null && uuid.length() > 0 )
			{
				if(storeInSession){
					request.getSession().setAttribute("uuid", uuid);
				}
			}
		}
		
		/***
		 * Content Language
		 */
		String pagingInfoStr = request.getParameter("paging");
		if(pagingInfoStr!=null && pagingInfoStr.length() > 0 )
		{
			JSONDeserializer< PagingInfo>dser = new JSONDeserializer<PagingInfo>();
			PagingInfo _pagingInfo = null;
			try {
				_pagingInfo = dser.deserialize(pagingInfoStr);	
			} catch (Exception e) 
			{
				//pagingInfo=new PagingInfo();
			}
			if(_pagingInfo!=null)
			{
				pagingInfo = _pagingInfo;
			}
		}
		
		
		/***
		 * Content Language
		 */
		lang = request.getParameter("lang");
		if(lang==null)
		{
			lang=(String)request.getSession().getAttribute("lang");
			
			if(lang!=null && lang.length() > 0 )
			{
				if(storeInSession){
					request.getSession().setAttribute("lang", lang);
				}
			}
		}
		
		String dstLang = "en";
		Locale rLocale = request.getLocale();
		if(rLocale!=null && lang==null)
		{
			dstLang = rLocale.getLanguage();
			lang = dstLang;
		}
		
		if(lang==null)
		{
			lang="en";
		}
		
		/***
		 * Content Language
		 */
		hardwarePlatform= request.getParameter("hardwarePlatform");
		if(hardwarePlatform==null)
		{
			hardwarePlatform=(String)request.getSession().getAttribute("hardwarePlatform");
			
			if(hardwarePlatform!=null && hardwarePlatform.length() > 0 )
			{
				if(storeInSession){
					request.getSession().setAttribute("hardwarePlatform", hardwarePlatform);
				}
			}
		}
		
		/***
		 * JSONP callback
		 */
		callback= request.getParameter("callback");
		
		/***
		 * User Agent
		 */
		userAgent=(String)request.getSession().getAttribute("UserAgent");
    	if(userAgent==null)
    	{
    		userAgent= "PC";
    	}
    	
    	String platform=(String)request.getParameter("platform");
    	if(platform!=null)
    	{
    		userAgent= platform;
    	}
		
		
		/***
		 * Ci's DataSource UUID
		 */
		dataSource = request.getParameter("dataSource");
		if(dataSource==null)
		{
			dataSource=(String)request.getSession().getAttribute("dataSource");
			
			if(dataSource!=null && dataSource.length() > 0 )
			{
				if(storeInSession){
					request.getSession().setAttribute("dataSource", dataSource);
				}
			}
		}
		
		
		/***
		 * Ci's DataSource Reference UUID
		 */
		ref = request.getParameter("ref");
		if(ref==null)
		{
			ref=(String)request.getSession().getAttribute("ref");
			
			if(ref!=null && ref.length() > 0 )
			{
				if(storeInSession){
					request.getSession().setAttribute("ref", ref);
				}
			}
		}
		
		
		
		/***
		 * Application Manager Object
		 */
		appManager = ApplicationManager.getInstance();
		
			
    	
		/***
		 * Application it self : 
		 */
		if(uuid !=null || appIdentifier!=null   )
    	{
			application = appManager.getApplication(uuid,appIdentifier,false);
    	}
		
		
		/***
		 * Data specific paramenters
		 */
		String refIdStr=(String)request.getParameter("refId");
    	if(refIdStr!=null)
    	{
    		try {
				refId=Integer.parseInt(refIdStr);
			} catch (Exception e) {
				refId=-1;
			}
    	}
    	
    	
    	/***
		 * Data specific paramenters
		 */
		String cTypeStr=(String)request.getParameter("cType");
    	if(cTypeStr!=null)
    	{
    		try {
				cType= ECMContentSourceTypeTools.FromString(cTypeStr);
			} catch (Exception e) {
				cType=ECMContentSourceType.Unknown;
			}
    	}
    	
    	
    	
    	if(application!=null  && dataSource!=null && dataSource.length() > 0)
    	{
    		ds = application.getDataSource(dataSource);
    	}
		
    	if(ds!=null && application!=null && appManager!=null)
    	{
    		dataSourceCache = ServerCache.getDSC(appManager, application, ds);
    	}
		
		
    }

	/**
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}

	/**
	 * @param ref the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}

	public DataSourceCache getDataSourceCache() {
		return dataSourceCache;
	}

	public void setDataSourceCache(DataSourceCache dataSourceCache) {
		this.dataSourceCache = dataSourceCache;
	}
	public int getRefId() {
		return refId;
	}
	public void setRefId(int refId) {
		this.refId = refId;
	}

	public String getHardwarePlatform() {
		return hardwarePlatform;
	}

	public void setHardwarePlatform(String hardwarePlatform) {
		this.hardwarePlatform = hardwarePlatform;
	}
	public Boolean getIsHybrid() {
		return isHybrid;
	}
	public void setIsHybrid(Boolean isHybrid) {
		this.isHybrid = isHybrid;
	}
}
