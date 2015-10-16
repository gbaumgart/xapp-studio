package pmedia.DataManager;
import java.util.HashMap;

import cmx.cache.DataSourceCache;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;
import net.sf.ehcache.*;
import net.sf.ehcache.Cache;
public class ServerCache 
{

	public HashMap<String, DomainCache>dc = new HashMap<String, DomainCache>();
	
	public HashMap<String, DataSourceCache>dsc = new HashMap<String, DataSourceCache>();
	
	public static CacheManager cacheManager;
	public static Cache cache;
	
	public static DataSourceCache getDSC(ApplicationManager appManager,Application app,DataSourceBase ds)
	{
		if(ds==null){
			return null;
		}
		ServerCache sc = ServerCache.getInstance();
		
		
		DataSourceCache obj = sc.dsc.get(ds.uid);
		if(obj ==null)
		{
			obj = new DataSourceCache(appManager,app,ds);
			sc.dsc.put(ds.uid, obj);
		}
		return obj;
	}
	
	public DataSourceCache getDSC(String dsUID)
	{
		return dsc.get(dsUID);
	}
	
	public DomainCache getDC(String domain)
	{
		DomainCache obj = dc.get(domain);
		if(obj ==null)
		{
			obj = new DomainCache(domain);
			dc.put(domain, obj);
		}
		return obj;
	}
	
	public static ServerCache instance = null;
	
	public void initCache(String configPath){
		
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
	public static ServerCache getInstance()
	{
		
		if(instance ==null)
		{
			instance = new ServerCache();
		}
		
		if(ServerCache.cacheManager==null){
			instance.initCache(System.getProperty("ServletPath"));
		}
		return instance;
	}
}
