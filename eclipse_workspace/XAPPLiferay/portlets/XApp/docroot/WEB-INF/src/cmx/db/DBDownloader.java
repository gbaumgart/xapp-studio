package cmx.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQConstants;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQItemType;
import javax.xml.xquery.XQSequence;
import javax.xml.xquery.XQStaticContext;

import pmedia.utils.StringUtils;
import cmx.XQuery.XqueryProcessor;
import cmx.tools.DBConnectionChecker;
import cmx.types.DBConnectionError;

import com.ddtek.xquery.xqj.DDXQDataSource;
import com.ddtek.xquery.xqj.DDXQJDBCConnection;

	
	public class DBDownloader {
		
		
    public static DBDownloader instance=null;
    
    
    public static ArrayList<DBDownloadTask>downloadTasks=new ArrayList<DBDownloadTask>();

    public static int convert(String xqueryFile, String connectionURL,String targetFileName,String dbUser,String dbPwd,String lang) throws Exception
	{
		XQConnection         xqconnection = null;
		XQExpression xqExpr       = null;
		File outfile = new File(targetFileName);
		FileOutputStream    outWriter    = new FileOutputStream(outfile) ;
			DDXQDataSource dataSource = new DDXQDataSource();

			DDXQJDBCConnection[] jdbcConnection = new DDXQJDBCConnection[1];
			jdbcConnection[0] = createConnection("mysql_localhost_3306", connectionURL, dbUser, dbPwd, true, "full", "");

			dataSource.setDdxqJdbcConnection(jdbcConnection);

			dataSource.setOptions("serialize=indent=yes");

			xqconnection = dataSource.getConnection();

			XQStaticContext context = xqconnection.getStaticContext();
			context.setBindingMode(XQConstants.BINDING_MODE_DEFERRED);
			//context.
			xqconnection.setStaticContext(context);

			//xqExpr = xqconnection.prepareExpression(new FileReader(xqueryFile));

			xqExpr = xqconnection.createExpression();

			if(lang!=null)
			{
//				xqExpr.bindString(new QName("lang"), lang, xqconnection.createAtomicType(XQItemType.XQBASETYPE_STRING));
				xqExpr.bindString(new QName("lang"), lang, xqconnection.createAtomicType(XQItemType.XQBASETYPE_STRING));

			}
			//System.out.println("xquery file : " + xqueryFile );
			FileReader xqFile = new FileReader(xqueryFile);
			XQSequence xqSequence = xqExpr.executeQuery(xqFile);//
			xqSequence.writeSequenceToResult(new StreamResult(outWriter));

			outWriter.close();
			outWriter.flush();
			



			return 1;

	}
    
    public static  DBConnectionError downloadInternalNow(DBDownloadTask task)
	{

    	DBConnectionError res = new DBConnectionError();
		

    	String dbRootPath = System.getProperty("dbDownloadPath");
    	String dbDstPath = dbRootPath + "datasources/" + task.host +"/" + task.dbName;
    	
    	String dstFilePath= task.path + task.name + ".xml";

    	// mk dirs
    	String dstDirectory =StringUtils.path (dbDstPath);
		File dbPath = new File(dbDstPath);
		if(dbPath!=null)
		{
			dbPath.mkdirs();
		}
		
		File dstFile = new File(dstFilePath);
		if(dstFile!=null && dstFile.exists() && task.overwrite)
		{
			dstFile.delete();
		}
		
		
		String xqUrl = "jdbc:xquery:mysql://";
		String connectionUrl = xqUrl + task.host+ ":" + task.port + ";DatabaseName=" + task.dbName;
		String connectionName = task.host + task.dbName;
		task.isDownloading=true;
		
		try {
			XqueryProcessor.convertEx(connectionName, task.xqueryFile,connectionUrl, dstFilePath, task.user, task.pswd,null,task.prefix);
		} catch (Exception e) {
			e.printStackTrace();
			res.type=-1;
			res.msgInternal = e.getLocalizedMessage();
		}

		System.out.println("downloading db to " + dstFilePath);
		DBDownloader.downloadTasks.remove(task);
		return res;
	}
    
    public static void downloadInternal(DBDownloadTask task)
	{
    	String dbRootPath = System.getProperty("dbDownloadPath");
    	String dbDstPath = dbRootPath + "datasources/" + task.host +"/" + task.dbName;
    	
    	String dstFilePath= task.path + task.name + ".xml";

    	// mk dirs
    	String dstDirectory =StringUtils.path (dbDstPath);
		File dbPath = new File(dbDstPath);
		if(dbPath!=null)
		{
			dbPath.mkdirs();
		}
		
		File dstFile = new File(dstFilePath);
		if(dstFile!=null && dstFile.exists() && task.overwrite)
		{
			dstFile.delete();
		}
		
		
		String xqUrl = "jdbc:xquery:mysql://";
		String connectionUrl = xqUrl + task.host+ ":" + task.port + ";DatabaseName=" + task.dbName;
		String connectionName = task.host + task.dbName;
		task.isDownloading=true;
		
		try {
			XqueryProcessor.convertEx(connectionName, task.xqueryFile,connectionUrl, dstFilePath, task.user, task.pswd,null,task.prefix);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("downloading db to " + dstFilePath);
		
		DBDownloader.downloadTasks.remove(task);
		
    	
	}
    public static void proceed()
	{
		if(DBDownloader.isDownloading())
			return;
		
		DBDownloadTask next = DBDownloader.getNextTask();
		if(next!=null)
		{
			DBDownloader.downloadInternal(next);
		}
	}
    public static void addDownloadTask(DBDownloadTask task)
	{
    	if(!DBDownloader.hasTask(task)){
    		
    		DBDownloader.downloadTasks.add(task);
    	}
    	
    	DBDownloader.proceed();
	}
    
    public static Boolean isDownloading()
	{
		for(int i = 0  ; i < DBDownloader.downloadTasks.size() ; i ++)
		{
			DBDownloadTask task = DBDownloader.downloadTasks.get(i);
			if(task.isDownloading)
			{
				return true;
			}
		}
		return false;
	}
    public static DBDownloadTask getNextTask()
	{

		for(int i = 0  ; i < DBDownloader.downloadTasks.size() ; i ++)
		{
			DBDownloadTask task = DBDownloader.downloadTasks.get(i);
			if(!task.isDownloading)
			{
				return task;
			}
		}
		
		return null;
	}

    public static Boolean hasTask(DBDownloadTask task)
	{
		return DBDownloader.downloadTasks.indexOf(task) !=-1;
	}
    
    public static DBDownloader getInstance()
    {
    	if(DBDownloader.instance ==null)
    	{
    		DBDownloader.instance = new DBDownloader();
    	}
    	
    	return DBDownloader.instance;
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
	
	public static DBConnectionError downloadJDB(
			String host, 
			String port,
			String dbName, 
			String prefix,
			String user, 
			String pswd)
	{
		DBConnectionError res = new DBConnectionError();
		
		DBConnectionError connectionStatus=DBDownloader.checkConnection(host, port, dbName, user, pswd);
		if(connectionStatus.type!=0)
		{
			System.out.println("invalid connection : " + connectionStatus.msgInternal);
			res.type=-1;
			res.msgInternal = connectionStatus.msgInternal;
			return res;
		}
		String dbRootPath = System.getProperty("dbDownloadPath");
		
		String xqueryFilePathBase = System.getProperty("webapp.root") + "/XQBase/" ;
		
		
		System.out.println("downloading db " + dbRootPath);
		
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
