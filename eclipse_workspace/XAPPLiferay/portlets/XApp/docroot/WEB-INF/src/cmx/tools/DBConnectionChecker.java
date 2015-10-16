package cmx.tools;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xbill.DNS.Address;

import cmx.db.DBDownloadTask;
import cmx.db.DBDownloader;
import cmx.types.DBConnectionError;
import cmx.types.XCDataSource;

import com.ddtek.xquery.xqj.DDXQDataSource;
import com.ddtek.xquery.xqj.DDXQJDBCConnection;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
//The Client sessions package
//The Base package for representing JSON-RPC 2.0 messages

//The JSON Smart package for JSON encoding/decoding (optional)
//import net.minidev.json.*;


	public class DBConnectionChecker {

	public static Boolean hasJSONP(XCDataSource ds)
	{
		// The JSON-RPC 2.0 server URL
		URL serverURL = null;
		String siteUrl = ds.getUrl();
		String rpcUrl = ds.getRPCUrl();

		try {
			serverURL = new URL(siteUrl + "/" + rpcUrl);
		} catch (MalformedURLException e) {
			// handle exception...
		}
		
		GetMethod get = new GetMethod(siteUrl + "/" + rpcUrl);
		// add the payload to the post object
		HttpClient client = new HttpClient();
		int status =-1;
		try {
			 status = client.executeMethod(get);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String response = null;
		try {
			response = get.getResponseBodyAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(response!=null){
			
			if(response.indexOf("customTypeQuery")!=-1){
				return true;
			}
		}
		//System.out.println(response);
		
		// Create new JSON-RPC 2.0 client session
		 //JSONRPC2Session mySession = new JSONRPC2Session(serverURL);
		
		/*Clien client = Client.create();
		WebResource webResource = 
		client.resource("http://twittervision.com/user/current_status/bdarfler.json");
		String response = webResource.get(String.class);*/
		 
		return false;
	}
	
	public static DBConnectionError checkGoogleDocumentService(String service,String channel, String user, String pswd)
	{
		DBConnectionError res = new DBConnectionError();
		
		CalendarService myService = new CalendarService("demo-CalendarFeedDemo-1");
	    
		if (user !=null &&user.length()>0 && pswd!= null && pswd.length()> 0) 
		{
		      try {
		        myService.setUserCredentials(user, pswd);
		      } catch (AuthenticationException e) 
		      {
		    	  
		    	   e.printStackTrace();
		    	  	res.type=-1;
					res.msg="failed";
					res.msgInternal = "Illegal username/password combination.";
					return res;
		      }
		
		}
		
	        URL metafeedUrl = null;
	        try {
	        	
	        		String userFeedName=new String(channel);
	        		if(!userFeedName.contains("@gmail.com"))
	        		{
	        			userFeedName+="@gmail.com";
	        		}
	        		metafeedUrl = new URL("https://www.google.com/calendar/feeds/"+userFeedName+"/allcalendars/full");
	        } catch (MalformedURLException e) 
	        {
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;
			}
	        
	        //System.out.println("Getting favorite video entries...\n");
	        CalendarFeed userFeed = null;
	        try {
				userFeed = myService.getFeed(metafeedUrl, CalendarFeed.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				
				e.printStackTrace();
				return res;
			}
		return res;

	}
	
	public static DBConnectionError checkGoogleCalendarService(String service,String channel, String user, String pswd)
	{
		DBConnectionError res = new DBConnectionError();
		
		CalendarService myService = new CalendarService("demo-CalendarFeedDemo-1");
	    
		if (user !=null &&user.length()>0 && pswd!= null && pswd.length()> 0) 
		{
		      try {
		        myService.setUserCredentials(user, pswd);
		      } catch (AuthenticationException e) 
		      {
		    	  
		    	   e.printStackTrace();
		    	  	res.type=-1;
					res.msg="failed";
					res.msgInternal = "Illegal username/password combination.";
					return res;
		      }
		
		}
		
	        URL metafeedUrl = null;
	        try {
	        	
	        		String userFeedName=new String(channel);
	        		if(!userFeedName.contains("@gmail.com"))
	        		{
	        			userFeedName+="@gmail.com";
	        		}
	        		metafeedUrl = new URL("https://www.google.com/calendar/feeds/"+userFeedName+"/allcalendars/full");
	        } catch (MalformedURLException e) 
	        {
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;
			}
	        
	        //System.out.println("Getting favorite video entries...\n");
	        CalendarFeed userFeed = null;
	        try {
				userFeed = myService.getFeed(metafeedUrl, CalendarFeed.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				
				e.printStackTrace();
				return res;
			}
		return res;

	}
	public static DBConnectionError checkGooglePicassaService(String service,String channel, String user, String pswd)
	{
		DBConnectionError res = new DBConnectionError();
		
		PicasawebService myService = new PicasawebService(service+channel);
	    
		if (user !=null &&user.length()>0 && pswd!= null && pswd.length()> 0) {
		      try {
		        myService.setUserCredentials(user, pswd);
		      } catch (AuthenticationException e) 
		      {
		    	  
		    	   e.printStackTrace();
		    	  	res.type=-1;
					res.msg="failed";
					res.msgInternal = "Illegal username/password combination.";
					return res;
		      }
		    }
		
	        URL metafeedUrl = null;
	        try {
	        		metafeedUrl = new URL("https://picasaweb.google.com/data/feed/api/user/"+channel);
	      
	        } catch (MalformedURLException e) 
	        {
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;
			}
	        
	        //System.out.println("Getting favorite video entries...\n");
	        UserFeed userFeed = null;
	        
	        try {
				userFeed = myService.getFeed(metafeedUrl, UserFeed.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				
				e.printStackTrace();
				return res;
			}
		return res;

	}
	public static DBConnectionError checkGoogleYoutubeService(String service,String channel, String user, String pswd)
	{
		DBConnectionError res = new DBConnectionError();
		
		  YouTubeService myService = new YouTubeService(service+channel);
		  URL metafeedUrl = null;
	        
	        try {
	        		metafeedUrl = new URL("http://gdata.youtube.com/feeds/api/users/" +channel + "/uploads");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;
			}
	        VideoFeed resultFeed = null;
	        try {
				resultFeed = myService.getFeed(metafeedUrl, VideoFeed.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				
				e.printStackTrace();
				return res;
			}
		return res;
	}
	
	

	private static DDXQJDBCConnection createConnection(String name, String url, String user, String pswd, boolean forest, String esc, String sqlProfile) {
		DDXQJDBCConnection c = new DDXQJDBCConnection(name);
		c.setUrl(url);
		
		if (user.length() > 0) c.setUser(user);
		if (pswd.length() > 0) c.setPassword(pswd);
		c.setSqlXmlForest(forest);
		c.setSqlXmlIdentifierEscaping(esc);
		if (sqlProfile.length() > 0) c.setSqlProfile(sqlProfile);
		return c;
	}
	
	public static void xqDownload(
			String path,
			String uuid,
    		String applicationIdentifier,
			String host, 
			String port,
			String dbName, 
			String prefix,
			String user, 
			String pswd,
			String name,
			String version)
	{
		
		
		String xqueryFilePathBase = System.getProperty("webapp.root") + "XQBase/joomla/" + version +"/" ;
		DBDownloader dbDownloader = DBDownloader.getInstance();
		
		String xqArticles = xqueryFilePathBase + name + ".xquery"; 
		
		File xqFile = new File(xqArticles);
		if(!xqFile.exists())
		{
			return;
		}
		
		
		DBDownloadTask task = DBDownloadTask.createTask(path,host, port, dbName, prefix, user, pswd,name, xqArticles, dbDownloader);
		task.overwrite= true;
		if(DBDownloader.hasTask(task)){
			DBDownloader.downloadTasks.remove(task);
		}
		DBDownloader.addDownloadTask(task);
		
	}
	public static String getIPAdress(String host) throws UnknownHostException
	{
		URL url = null;
		try {
			url = new URL(host);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String result =null;
		InetAddress addr =null;
		
		addr = Address.getByName(url.getHost());
		addr.getHostName();
		if(addr!=null){
			result = new String (addr.getHostAddress());
		}
		return result;
		
	}
	public static String getHostName(String host) throws UnknownHostException
	{
		
		URL url = null;
		try {
			if(host!=null){
			url = new URL(host);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String result =null;
		InetAddress addr =null;
		
		if(url!=null && url.getHost()!=null)
		{
			addr = Address.getByName(url.getHost());
			addr.getHostName();
			if(addr!=null){
				result = new String (addr.getHostName());
			}
		}else{
			return host;
		}
		return result;
		
	}
	
	public static DBConnectionError downloadJDBTable(
			String path,
			String uuid,
    		String applicationIdentifier,
			String host, 
			String port,
			String dbName, 
			String prefix,
			String user, 
			String pswd,
			String version,
			String table)
	{
		DBConnectionError res = new DBConnectionError();

		//DBConnectionChecker.xqDownload(path,uuid,applicationIdentifier,host, port, dbName, prefix, user, pswd, "articles",version);
		
		String xqueryFilePathBase = System.getProperty("webapp.root") + "XQBase/joomla/" + version +"/" ;
		DBDownloader dbDownloader = DBDownloader.getInstance();
		
		String xqArticles = xqueryFilePathBase + table + ".xquery"; 
		
		File xqFile = new File(xqArticles);
		if(!xqFile.exists())
		{
			res.type = -1;
			res.msgInternal="No Such Table";
			System.out.println("couldnt find xq file");
			return res;
		}
		DBDownloadTask task = DBDownloadTask.createTask(path,host, port, dbName, prefix, user, pswd,table, xqArticles, dbDownloader);
		task.overwrite= true;
		
		if(DBDownloader.hasTask(task)){
			DBDownloader.downloadTasks.remove(task);
		}
		res=DBDownloader.downloadInternalNow(task);
		
		
		return res;
		
	}
	
	public static DBConnectionError downloadJDB(
			String path,
			String uuid,
    		String applicationIdentifier,
			String host, 
			String port,
			String dbName, 
			String prefix,
			String user, 
			String pswd,
			String version)
	{
		DBConnectionError res = new DBConnectionError();
		
		DBConnectionError connectionStatus=DBConnectionChecker.checkConnection(host, port, dbName, user, pswd);
		if(connectionStatus.type!=0)
		{
			System.out.println("invalid connection : " + connectionStatus.msgInternal);
			res.type=-1;
			res.msgInternal = connectionStatus.msgInternal;
			return res;
		}

		
		
		DBConnectionChecker.xqDownload(path,uuid,applicationIdentifier,host, port, dbName, prefix, user, pswd, "articles",version);
		DBConnectionChecker.xqDownload(path,uuid,applicationIdentifier,host, port, dbName, prefix, user, pswd, "categories",version);
		DBConnectionChecker.xqDownload(path,uuid,applicationIdentifier,host, port, dbName, prefix, user, pswd, "sections",version);
		DBConnectionChecker.xqDownload(path,uuid,applicationIdentifier,host, port, dbName, prefix, user, pswd, "jeventsNormal",version);
		DBConnectionChecker.xqDownload(path,uuid,applicationIdentifier,host, port, dbName, prefix, user, pswd, "mosetCategories",version);
		DBConnectionChecker.xqDownload(path,uuid,applicationIdentifier,host, port, dbName, prefix, user, pswd, "mosetLinks",version);
		DBConnectionChecker.xqDownload(path,uuid,applicationIdentifier,host, port, dbName, prefix, user, pswd, "banners",version);
		DBConnectionChecker.xqDownload(path,uuid,applicationIdentifier,host, port, dbName, prefix, user, pswd, "jeventCategories",version);
		DBConnectionChecker.xqDownload(path,uuid,applicationIdentifier,host, port, dbName, prefix, user, pswd, "bforms",version);
		DBConnectionChecker.xqDownload(path,uuid,applicationIdentifier,host, port, dbName, prefix, user, pswd, "breezingforms",version);
		
		
		
		//String xqueryFilePathBase = System.getProperty("webapp.root") + "XQBase/" ;
		//DBDownloader dbDownloader = DBDownloader.getInstance();
		
		//String xqArticles = xqueryFilePathBase + "articles.xquery"; 
		
		/*
		DBDownloadTask task = DBDownloadTask.createTask(host, port, dbName, prefix, user, pswd,"articles", xqArticles, dbDownloader);
		task.overwrite= true;
		if(DBDownloader.hasTask(task)){
			DBDownloader.downloadTasks.remove(task);
		}
		DBDownloader.addDownloadTask(task);
		*/
		//System.out.println("downloading db " + xqArticles);
		
		return res;
		
	}
	public static DBConnectionError checkConnection(String host, String port,String dbName, String user, String pswd)
	{
		String xqUrl = "jdbc:xquery:mysql://";
		String connectionUrl = xqUrl + host + ":" + port + ";DatabaseName=" + dbName;
		
		DDXQJDBCConnection[] jdbcConnection = new DDXQJDBCConnection[1];
		jdbcConnection[0] = createConnection("mysql_localhost_3306", connectionUrl, user, pswd, true, "full", "");
		
		
		
		DDXQDataSource dataSource = new DDXQDataSource();
		XQConnection         xqconnection = null;

		try {
			dataSource.setLoginTimeout(3);
		} catch (XQException e1) {
			e1.printStackTrace();
		}
		
		dataSource.setDdxqJdbcConnection(jdbcConnection);
		
		
		
		dataSource.setOptions("serialize=indent=yes");

		DBConnectionError res = new DBConnectionError();
		res.identifier = host + dbName;
		try {
			xqconnection = dataSource.getConnection();
		} catch (XQException e) {
			
			res.msg="failed";
			res.msgInternal = e.getLocalizedMessage();
			res.msgInternal=res.msgInternal.replace("[DataDirect][XQuery][MySQL JDBC Driver]", "");
			
			res.type=-1;
			e.printStackTrace();
			
		}
		
		if(res.type==0){
			try {
				xqconnection.close();
			} catch (XQException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		
		return res;
	}
	
}
