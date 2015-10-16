package cmx.db;

public class DBDownloadTask {
	
	public String host; 
	public String port;
	public String dbName;
	public String prefix;
	public String user; 
	public String pswd;
	public String xqueryFile;
    public Object delegate;
    public Boolean isDownloading;
    public Boolean  autoStart;
    public Boolean  overwrite;
    public String name;
    public String path;
    
    public static DBDownloadTask createTask(
    		String path,
    		String host, 
			String port,
			String dbName, 
			String prefix,
			String user, 
			String pswd,
			String name,
			String xqueryFile,
			Object delegate)
    {
    	
    	DBDownloadTask task =  new DBDownloadTask();
    	task.path=path;
    	task.host = host;
    	task.port = port;
    	task.dbName = dbName;
    	task.prefix = prefix;
    	task.name = name;
    	task.user = user;
    	task.pswd = pswd;
    	task.xqueryFile = xqueryFile;
    	task.delegate = delegate;
    	task.isDownloading = false;
    	task.autoStart= false;
    	task.overwrite= false;
    	
    	return task;
    	
    }
    
    
}
