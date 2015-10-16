package pmedia.types;

public class DownloadTask {
	
	public String localFilepath;
    public String remoteFilepath;
	
    public long totalFileBytes;
	
    public Boolean isDownloading;
    public Boolean  autoStart;
    public int type;
    
    public Object delegate;
    
}
