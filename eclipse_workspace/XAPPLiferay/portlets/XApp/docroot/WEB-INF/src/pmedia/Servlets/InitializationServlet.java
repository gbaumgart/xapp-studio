
package pmedia.Servlets;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.CacheManager;

import pmedia.DataManager.Cache;
import pmedia.DataManager.ServerCache;
import pmedia.types.Domain;
import pmedia.utils.StringUtils;
public class InitializationServlet extends HttpServlet
{
	
	private static final long serialVersionUID = 1L;
	private static InitializationServlet thisServlet=null;
   	
	

	public void initCache(String configPath){
	
		if(configPath==null){
			configPath=System.getProperty("ServletPath");
		}
	
		try {
			configPath +="config/normal.xml";
			if(ServerCache.cacheManager==null){
				ServerCache.cacheManager = new CacheManager(configPath);
			}
			if(ServerCache.cache==null){
				ServerCache.cache=ServerCache.cacheManager.getCache("firstcache");
			}	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
	}

	public void init() throws ServletException 
	{
		super.init();
		
		initCache(null);
		
		//System.setProperty("JWEBSOCKET_HOME", "/opt/jWebSocket/");
		
		String env= System.getenv("JWEBSOCKET_HOME");
		ServletContext context = super.getServletContext();
		String webRoot = context.getRealPath("/");
		
		//invalidate sessions
		SessionCreateAction.mySessions = new HashMap<String, HttpSession>();
		HttpSessionCollector.sessions=new HashMap<String, HttpSession>();
		
		this.thisServlet = this;
		System.out.println( "Start init servlet at : " + webRoot + " on path : " + "  env : " + env);
		
		if (webRoot != null)
		{
			System.setProperty("webapp.root",webRoot);
		}
		StringUtils.loadProperties();
		Enumeration names = super.getInitParameterNames();
		while (names.hasMoreElements()) 
		{
			String name = (String)names.nextElement();
			String value = super.getInitParameter(name);
			if (value != null) {
				System.setProperty(name, StringUtils.resolveProperties(value));
			}
		}
	}

	public void destroy()
	{

		super.destroy();

	}

}
