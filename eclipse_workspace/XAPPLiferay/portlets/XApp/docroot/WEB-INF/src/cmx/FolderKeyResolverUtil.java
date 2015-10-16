package cmx;

import java.io.File;
import java.util.HashMap;

import cmx.types.ApplicationManager;
import cmx.types.FolderKeys;

public class FolderKeyResolverUtil 
{
	public static  HashMap<String, String>getKeyMapAbsolute(String appId,String uuid)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		
		result.put(FolderKeys.CM_DOC_ROOT, FolderKeyResolverUtil.toLocalPath(FolderKeys.CM_DOC_ROOT, uuid, appId));
		
		result.put(FolderKeys.CM_LIB_ROOT, FolderKeyResolverUtil.toLocalPath(FolderKeys.CM_LIB_ROOT, uuid, appId));
		
		result.put(FolderKeys.CM_APP_ROOT, FolderKeyResolverUtil.toLocalPath(FolderKeys.CM_APP_ROOT, uuid, appId));
		
		result.put(FolderKeys.CM_USER_ROOT, FolderKeyResolverUtil.toLocalPath(FolderKeys.CM_USER_ROOT, uuid, appId));
		
		result.put(FolderKeys.CM_CSS_COMMON, FolderKeyResolverUtil.toLocalPath(FolderKeys.CM_CSS_COMMON, uuid, appId));
		
		result.put(FolderKeys.CM_CSS_MOBILE_COMMON, FolderKeyResolverUtil.toLocalPath(FolderKeys.CM_CSS_MOBILE_COMMON, uuid, appId));
		
		result.put(FolderKeys.CM_DIST, FolderKeyResolverUtil.toLocalPath(FolderKeys.CM_DIST, uuid, appId));
		
		result.put(FolderKeys.CM_XASWEB, FolderKeyResolverUtil.toLocalPath(FolderKeys.CM_XASWEB, uuid, appId));
		
		result.put(FolderKeys.CM_RPC_PROXY_DOMAIN, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_RPC_PROXY_DOMAIN, uuid, appId));
		
		result.put(FolderKeys.CM_RPC_PROXY_PATH, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_RPC_PROXY_PATH, uuid, appId));
		
		result.put(FolderKeys.CM_WEB_SERVER, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_WEB_SERVER, uuid, appId));
		
		

		
		
		return result;
	}
	public static  HashMap<String, String>getKeyMap(String appId,String uuid)
	{
		
		HashMap<String, String> result = new HashMap<String, String>();
		
		result.put(FolderKeys.CM_DOC_ROOT, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_DOC_ROOT, uuid, appId));
		
		result.put(FolderKeys.CM_LIB_ROOT, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_LIB_ROOT, uuid, appId));
		
		result.put(FolderKeys.CM_APP_ROOT, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_APP_ROOT, uuid, appId));
		
		result.put(FolderKeys.CM_USER_ROOT, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_USER_ROOT, uuid, appId));
		
		result.put(FolderKeys.CM_CSS_COMMON, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_CSS_COMMON, uuid, appId));
		
		result.put(FolderKeys.CM_CSS_MOBILE_COMMON, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_CSS_MOBILE_COMMON, uuid, appId));
		
		result.put(FolderKeys.CM_DIST, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_DIST, uuid, appId));
		
		result.put(FolderKeys.CM_XASWEB, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_XASWEB, uuid, appId));
		
		result.put(FolderKeys.CM_RPC_PROXY_DOMAIN, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_RPC_PROXY_DOMAIN, uuid, appId));
		
		result.put(FolderKeys.CM_RPC_PROXY_PATH, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_RPC_PROXY_PATH, uuid, appId));
		
		result.put(FolderKeys.CM_WEB_SERVER, FolderKeyResolverUtil.resolveKey(FolderKeys.CM_WEB_SERVER, uuid, appId));
		
		return result;
	}
	public static String getAbsolutePrefix(String key)
	{
		if(key.equals(FolderKeys.CM_XASWEB))
		{
			return System.getProperty("xasWebRoot");
		}
		
		if(key.equals(FolderKeys.CM_LIB_ROOT))
		{
			return getAbsolutePrefix(FolderKeys.CM_XASWEB) + "lib/";//getAbsolutePrefix(FolderKeys.CM_DOC_ROOT) + "";
		}
		
		if(key.equals(FolderKeys.CM_DOC_ROOT))
		{
			return System.getProperty("ServletPath");
		}
		return null;
	}
	public static String toLocalPath(String key)
	{
		String keyResolved = resolveKey(key);
		
		return null;
	}
	
	public static String toLocalPath(String key,String uuid,String appId)
	{
		
		//String keyResolved = resolveKey(key);
		
		if(key.equals(FolderKeys.CM_LIB_ROOT))
		{
			return getAbsolutePrefix(key);
		}
		
		if(key.equals(FolderKeys.CM_USER_ROOT))
		{
			return ApplicationManager.getInstance().getUserRootPath(uuid) + "apps/";
		}
		
		if(key.equals(FolderKeys.CM_APP_ROOT))
		{
			return ApplicationManager.getInstance().getUserAppPath(uuid,appId);
		}
		
		if(key.equals(FolderKeys.CM_CXAPP))
		{
			
			/***
			 * Try app path :
			 */
			String testPath = ApplicationManager.getInstance().getUserAppPath(uuid,appId);
			File testDir = null;
			if(testPath!=null)
			{
				testPath+=resolveKey(FolderKeys.CM_CXAPP);
				testDir=new File(testPath);
				if(testDir.exists() && testDir.isDirectory())
				{
					return testPath;
				}
			}
		}
	
		if(key.equals(FolderKeys.CM_XASWEB))
		{
			return System.getProperty("xasWebRoot");
		}
		
		return null;
	}
	
	public static String resolveKey(String key)
	{
		if(key.equals(FolderKeys.CM_LIB_ROOT))
		{
			return "lib";
		}
		
		if(key.equals(FolderKeys.CM_DOC_ROOT))
		{
			return "XApp-portlet";
		}

		if(key.equals(FolderKeys.CM_USER_ROOT))
		{
			return "CMAC";
		}
		
		if(key.equals(FolderKeys.CM_APP_ROOT))
		{
			return "apps";
		}
		
		if(key.equals(FolderKeys.CM_CXAPP))
		{
			return "cxapp";
		}
		if(key.equals(FolderKeys.CM_CSS_COMMON))
		{
			return "cssCommon";
		}
		
		if(key.equals(FolderKeys.CM_CSS_MOBILE_COMMON))
		{
			return "cssMobile";
		}
		
		if(key.equals(FolderKeys.CM_XASWEB))
		{
			return "xasweb";
		}
		
		
		if(key.equals(FolderKeys.CM_RPC_PROXY_PATH))
		{
			return System.getProperty("rpcProxyPath");
		}
		
		if(key.equals(FolderKeys.CM_RPC_PROXY_DOMAIN))
		{
			return System.getProperty("rpcProxyDomain");
		}
		
		if(key.equals(FolderKeys.CM_WEB_SERVER))
		{
			return System.getProperty("CM_WEB_SERVER");
		}
		
		return null;
	}
	
	public static String resolveKey(String key,String uuid,String appId)
	{
		if(key.equals(FolderKeys.CM_LIB_ROOT))
		{
			return "lib";
		}
		
		if(key.equals(FolderKeys.CM_DOC_ROOT))
		{
			return "XApp-portlet";
		}

		if(key.equals(FolderKeys.CM_USER_ROOT))
		{
			return "CMAC";
		}
		if(key.equals(FolderKeys.CM_APP_ROOT))
		{
			//System.out.println("uuid=" + uuid + " appid : " + appId);
			return "/" +resolveKey(FolderKeys.CM_USER_ROOT) +"/" + uuid + "/apps/" + appId;
		}
		
		if(key.equals(FolderKeys.CM_CXAPP))
		{
			return "cxapp";
		}
		
		if(key.equals(FolderKeys.CM_CSS_COMMON))
		{
			return "cssCommon";
		}
		
		if(key.equals(FolderKeys.CM_CSS_MOBILE_COMMON))
		{
			return "cssMobile";
		}
		
		if(key.equals(FolderKeys.CM_SYS_APP))
		{
			return appId;
		}
		
		if(key.equals(FolderKeys.CM_DIST))
		{
			return appId;
		}

		if(key.equals(FolderKeys.CM_XASWEB))
		{
			
			/*
			String hardcoded = System.getProperty("xaswebRootUrl");
			if(hardcoded!=null && hardcoded.length()>0){
				return hardcoded;
			}
			*/
			
			return "/XApp-portlet/xasweb";
		}
		
		if(key.equals(FolderKeys.CM_RPC_PROXY_PATH))
		{
			return System.getProperty("rpcProxyPath");
		}
		
		if(key.equals(FolderKeys.CM_RPC_PROXY_DOMAIN))
		{
			return System.getProperty("rpcProxyDomain");
		}
		
		if(key.equals(FolderKeys.CM_WEB_SERVER))
		{
			return System.getProperty("CM_WEB_SERVER");
		}

		
		return null;
	}
}
