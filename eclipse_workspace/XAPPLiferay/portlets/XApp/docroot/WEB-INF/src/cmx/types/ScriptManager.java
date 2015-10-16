package cmx.types;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.json.annotations.SMDMethod;

import cmm.utils.LiferayTools;
import cmx.FolderKeyResolverUtil;
import cmx.manager.GroovyScriptManager;
import cmx.manager.JSPluginManager;
import cmx.tools.LiferayContentTools;
import cmx.tools.ResourceUtil;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import pmedia.types.CList;
import pmedia.types.CListIconItem;
import pmedia.utils.ResourceTools;
import pmedia.utils.SecurityUtil;
import pmedia.utils.StringUtils;

public class ScriptManager 
{
	public static boolean haveShutdownHook=false;
	
	public static void onLogout(String sessionId)
	{
		System.out.println("## SM : On user logout " + sessionId);
		ScriptSession session  = getSession(sessionId);
		if(session !=null  && session.thread!=null)
		{			
			session.thread.thread.stop();
			session.thread.process.destroy();
			currentPort--;
			sessions.remove(session);
		}
	}
	
	public static String createCustomApp(String uuid,String appIdentfier,String platform,String cxAppRoot)
	{
		
		
		String srcFolder =System.getProperty("cxapp.template");
		File srcObject = new File(srcFolder);
		if(!srcObject.exists())
		{
			srcObject.mkdirs();
		}

		String resDebugPath=System.getProperty("AppStoreRoot")+"templates/resources-debug.json";
		String resReleasePath=System.getProperty("AppStoreRoot")+"templates/resources-release.json";
		
		try {
			FileUtils.copyFile(new File(resDebugPath), new File(FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + "/resources-debug.json"));
			FileUtils.copyFile(new File(resReleasePath), new File(FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + "/resources-release.json"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/***
		 * now activate the actual resources : 
		 */
		Resources resDebug = ResourceUtil.fromPath(FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + "/resources-debug.json");
		if(resDebug!=null)
		{
			Resource res=ResourceUtil.getResourceByType(resDebug, "CXAPP");
			res.enabled=true;
			res=ResourceUtil.getResourceByType(resDebug, "CSS");
			ResourceUtil.save(resDebug, FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + "/resources-debug.json");
		}
		
		Resources resRelease = ResourceUtil.fromPath(FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + "/resources-release.json");
		if(resRelease!=null)
		{
			Resource res=ResourceUtil.getResourceByType(resDebug, "CXAPP");
			res.enabled=true;
			res=ResourceUtil.getResourceByType(resRelease, "CSS");
			ResourceUtil.save(resDebug, FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + "/resources-release.json");
		}
		
		
		String dstFolder = FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + FolderKeyResolverUtil.resolveKey(FolderKeys.CM_CXAPP);
		
		File dstObject = new File(dstFolder);
		if(!dstObject.exists())
		{
			dstObject.mkdirs();
		}

		try {
			FileUtils.copyDirectory(srcObject, dstObject, new FileFilter() 
			{
				  public boolean accept(File pathname) 
				  {
					if(pathname.getAbsolutePath().contains("svn")){
			    		  return false;
			         }
					  return true;
			    }
			}, true);
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}

		return "OK";
	}
	
	public static void killUserSession(String workspace,String uuid)
	{
		ArrayList<String>processes = cleanup();
		if(processes==null){
			return;
		}
		
		
		for(int i = 0 ; i < processes.size() ;i ++){
			
			String process  = processes.get(i);
			if(process.contains(uuid)){
				if(process.contains("c9") || process.contains("node")){
					System.out.println("have running instance : " + process);
					String[] args = process.split(" ");
					if(args!=null )
					{
						System.out.println(" killing" + args[0]);
						try {
							Runtime.getRuntime().exec("kill "+args[0]);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	public static ArrayList cleanup(){
		
		
		ArrayList<String> result = new ArrayList<String>();
		
		InputStream in = null;
		InputStream buffer = null;
		Reader reader = null;
		try {
			Process p = Runtime.getRuntime().exec("ps -ax");
			in = p.getInputStream();
			buffer = new BufferedInputStream(in);
			reader = new InputStreamReader(buffer);
			char[] charr = new char[1024];
			StringBuffer strbuff = new StringBuffer();
			while(true) {
				int r = reader.read(charr);
				if(r<=0) {
					break;
				}
				strbuff.append(charr, 0, r);
			}
			String[] args = strbuff.toString().split("\n");
			if(args!=null )
			{
				//System.out.println(" have pid : " + pid);
				for(int i = 0 ; i < args.length ; i++){
					result.add(args[i]);
				}
			}
			
			
			//return strbuff.toString();
			//System.out.print("\n \n :::: " + strbuff.toString());
			
		}catch(IOException e) {
			//todo - error handling
			e.printStackTrace();
		} finally {
			if(reader!=null) {
				try {
					reader.close();
				} catch(IOException e) {
					//failsafe
				}
			}
			if(buffer!=null) {
				try {
					buffer.close();
				} catch(IOException e) {
					//failsafe
				}
			}
			if(in!=null) {
				try {
					in.close();
				} catch(IOException e) {
					//failsafe
				}
			}
		}
		
		
		/*
		Runtime run = Runtime.getRuntime();
		Process process =null; 
		try {
			process = run.exec("ps -aux");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		try {
			while ((line=buf.readLine())!=null) 
			{
				System.out.println(line);
				
				if(line!=null && line.contains("PID"))
				{
					
				}
				if(line.contains("Process terminated with"))
				{
					
				}
			}
			} catch (IOException e) 
			{
				e.printStackTrace();
			} 
		
		
		*/
		return result;
	}
	public static void setup(){
		
		if (!ScriptManager.haveShutdownHook)
		{
			ScriptManager.haveShutdownHook=true;
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				   @Override
				   public void run() 
				   {
					   for (int i=0; i<ScriptManager.sessions.size(); i++) 
						{
							ScriptSession session = ScriptManager.sessions.get(i);
							if(session.thread!=null)
							{
								if(session.thread.thread!=null)
								{
									session.thread.thread.stop();
								}
								
								if(session.thread.process!=null){
									session.thread.process.destroy();
								}
							}
						}
					   
				   }
			
				  });
				  System.out.println("Shut Down Hook Attached.");
			
		}
	}
	public void attachShutDownHook()
	{
		  Runtime.getRuntime().addShutdownHook(new Thread() {
		   @Override
		   public void run() {
		    System.out.println("Inside Add Shutdown Hook");
		   }
	
		  });
		  System.out.println("Shut Down Hook Attached.");
	
	}
	public static Cloud9Thread setupCloud9Thread(String workspace,String uuid, ScriptSession session)
	{
		
		
		
		ScriptManager.setup();
		Cloud9Thread c9 = new Cloud9Thread(workspace);
		Thread thread = new Thread(c9);
		c9.thread=thread;
		c9.uuid=uuid;
		currentPort++;
		c9.port = currentPort;
		thread.start();
		return c9;
	}
	public static Process setupCloud9Process(String workspace,ScriptSession session)
	{
		
		
		
		String cmdRoot = System.getProperty("cloud9Root");
		Process pr = null;
		try
		{
			String node= "/usr/local/bin/node";
			currentPort++;
			Runtime run = Runtime.getRuntime() ;
			String fullCMD=  node + " " + cmdRoot + " server.js" + " -d " + " -p " + currentPort + " -w " + workspace + " &";
			System.out.println("full scrupt " + fullCMD);
			pr = run.exec(fullCMD);
			/*
			 * 			//String cmd[] = {src+""," " + height + " ",scaledFileName};
			 * 			//String fullCMD=  cmdRoot + "bin/cloud9.sh" + " -p " + currentPort + " -w " + workspace;  
			try 
			{
				pr.waitFor() ;
			} catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			*/
			session.buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			try {
				while ((line=session.buf.readLine())!=null) 
				{
					System.out.println(line);
				}
				} catch (IOException e) 
				{
					e.printStackTrace();
				} 
			
		}catch (IOException e)
		{
	          System.out.println("exception happened - here's what I know: ");
	          e.printStackTrace();
	//          System.exit(-1);
		}
		return pr;
	}
	public static Boolean hasAppPlugins(String uuid,String appIdentfier)
	{
		String userAppPlugins = ResourceUtil.resolveConstantsAbsolute("%XAPP%/plugins/", uuid, appIdentfier);
		if(userAppPlugins!=null)
		{
			File userAppPluginPathObject = new File(userAppPlugins);
			if(userAppPluginPathObject.exists() && userAppPluginPathObject.isDirectory())
			{
				return true;
			}
		}
		return false;
	}
	public static Boolean hasUserPlugins(String uuid,String appIdentfier)
	{
		String userAppPlugins = ResourceUtil.resolveConstantsAbsolute("%XUSER%/plugins/", uuid, appIdentfier);
		if(userAppPlugins!=null)
		{
			File userAppPluginPathObject = new File(userAppPlugins);
			if(userAppPluginPathObject.exists() && userAppPluginPathObject.isDirectory())
			{
				return true;
			}
		}
		return false;
	}
	public static Boolean hasPlugins(String uuid,String appIdentfier,String runtimeConfig,String systemApp)
	{
		
		return false;
	}
	
	public static ScriptSession createSession(HttpServletRequest req,String uuid,String appIdentfier,String platform,String cxAppRoot)
	{
		ScriptSession session= ScriptManager.getSession(uuid, appIdentfier, platform, cxAppRoot);
		
		if(session !=null  && session.thread!=null)
		{			
			
			session.thread.thread.stop();
			session.thread.process.destroy();
			currentPort--;
			
			
			//session.process.destroy();
			//session.thread.stop();
			//session.thread.
			sessions.remove(session);
			session=null;
		}
		
		
		killUserSession(null, uuid);
		
		
		Application app = ApplicationManager.getInstance().getApplication(uuid, appIdentfier, false);

		if(cxAppRoot==null)
		{
			cxAppRoot ="";
		}
		
		if(app==null){
			return null;
		}

		if(session==null)
		{
			/***
			 * Find the workspace local path 
			 */
			
			String workspace = null;
			
			//assume user root folder
			String userRootFolder = ApplicationManager.getInstance().getStoreRootPath();
			if(cxAppRoot==null)
			{
				return null;
			}
			
			String rootPath = ResourceUtil.resolveConstantsAbsolute(cxAppRoot, uuid, appIdentfier);
			System.out.println("#####incoming " + rootPath);
			
			File testPath = new File(rootPath); 
			
			
			/*
			if(cxAppRoot.length()==0){
				
				if(hasAppPlugins(uuid, appIdentfier))
				{
					workspace=rootPath+"plugins";
				}else if(hasUserPlugins(uuid, appIdentfier))
				{
					workspace=rootPath+"plugins/";
				}else{
					return null;
				}
			}*/
			
			if(workspace==null && testPath.exists() && testPath.isDirectory())
			{
				workspace="" + rootPath;
			}
			
			
			
			//assume user app dir :
			/*
			if(workspace==null)
			{
				String userAppDir=ApplicationManager.getInstance().getStoreRootPath();
				String testPathStr = userAppDir + "/" + cxAppRoot;
				testPath=new File(testPathStr);
				testPathStr = testPath.getAbsolutePath();
				testPathStr = testPathStr.replace("CMAC/CMAC/", "CMAC/");
				testPath=new File(testPathStr);
				System.out.println("####### try " + testPath);
				if(testPath.exists() && testPath.isDirectory())
				{
					workspace=userAppDir + uuid + "/apps/" + appIdentfier + "/";
				}else{
					workspace=null;
				}
			}
			*/
			
			// try lib dir , only if its admin !
			/*
			if(workspace==null)
			{
				if( uuid.equals(System.getProperty("adminUser")))
				{
					String servletPath = System.getProperty("ServletPath");
					testPath=new File(servletPath+ "/" + cxAppRoot);
					
					System.out.println("####### try " + testPath);
					if(testPath.exists() && testPath.isDirectory())
					{
						workspace=servletPath + "/" + cxAppRoot;
					}
					
					if(workspace==null)
					{
						//try xasweb doc root
						testPath = new File(System.getProperty("xasWebRoot") + "lib/" + cxAppRoot);
						if(testPath.exists() && testPath.isDirectory())
						{
							workspace=System.getProperty("xasWebRoot") + "lib/" + cxAppRoot;
						}
						
					}
				}
			}
			*/
			System.out.println("#######open script editor at : " + workspace);
			
			//we have a valid workspace
			if(workspace==null)
			{
				return null;
			}
			
			//create the final Session
			session = new ScriptSession();
			session.uuid = uuid;
			session.appId = appIdentfier;
			session.cxAppFolder = cxAppRoot;
			session.workspace = workspace;
			
			// now spawn a new process
			session.thread = setupCloud9Thread(workspace,uuid, session);
			session.sessionKey = req.getSession().getId();
			session.sessionID = req.getSession().getId();

			
			String referrer = req.getHeader("referer");
			String ipAddress = req.getRemoteAddr();
			String ref = (String)req.getSession().getAttribute("clientIP");
			if(ref!=null && ref.length() > 0){
				ipAddress=ref;
			}
			System.out.println("ref:" +referrer + " from " + ipAddress + " and session id : " + session.sessionKey + " and ref " + ref);
			String hash=referrer+"|"+ipAddress;
			
			String cSettings = workspace+"/.settings";
			File cSettingsFile = new File(cSettings);
			if(cSettingsFile.exists())
			{
				cSettingsFile.delete();
			}
			
			
			String file = workspace+"/.sessionkey";
			File f = new File(file);
			if(f.exists()){
				f.delete();
			}
			try {
				StringUtils.writeToFile(hash, file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(session.thread !=null)
			{
				//session.thread.run();
				session.referer=hash;
				session.status=1;
				sessions.add(session);
				session.editorUrl =  "http://" + System.getProperty("cloud9Host") + ":" + currentPort;
				return session;
			}
			
			
			/*
			session.process = setupCloud9Process(workspace,session);
			if(session.process !=null)
			{
				session.status=1;
				sessions.add(session);
				return session;
			}
			*/
			
		}
		
		return null;
	}
	
	
	public static ScriptSession getSession(String uuid,String appIdentfier,String platform,String cxAppRoot)
	{
		for (int i=0; i<ScriptManager.sessions.size(); i++) 
		{
			ScriptSession session = ScriptManager.sessions.get(i);
			
			if(session.appId.equals(appIdentfier) && session.uuid.equals(uuid))
			{
				return session;
			}
		}
		return null;
	}
	
	public static ScriptSession getSession(String sessionId)
	{
		for (int i=0; i<ScriptManager.sessions.size(); i++) 
		{
			ScriptSession session = ScriptManager.sessions.get(i);
			
			if(session.sessionID.equals(sessionId))
			{
				return session;
			}
		}
		return null;
	}
	
	
	public static ArrayList<ScriptSession>sessions = new ArrayList<ScriptSession>();
	public static int currentPort=3134;
	
	public static ArrayList<ScriptSession> getSessions() {
		return sessions;
	}

	public static void setSessions(ArrayList<ScriptSession> sessions) {
		ScriptManager.sessions = sessions;
	}

	public static int getCurrentPort() {
		return currentPort;
	}

	public static void setCurrentPort(int currentPort) {
		ScriptManager.currentPort = currentPort;
	}
	
	public static String createCustomPlugin(String uuid,String appIdentfier,String identifier,String root)
	{
		/*
		String srcFolder =System.getProperty("cxapp.template");
		File srcObject = new File(srcFolder);
		if(!srcObject.exists())
		{
			srcObject.mkdirs();
		}

		String resDebugPath=System.getProperty("AppStoreRoot")+"templates/resources-debug.json";
		String resReleasePath=System.getProperty("AppStoreRoot")+"templates/resources-release.json";
		
		try {
			FileUtils.copyFile(new File(resDebugPath), new File(FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + "/resources-debug.json"));
			FileUtils.copyFile(new File(resReleasePath), new File(FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + "/resources-release.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}


		Resources resDebug = ResourceUtil.fromPath(FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + "/resources-debug.json");
		if(resDebug!=null)
		{
			Resource res=ResourceUtil.getResourceByType(resDebug, "CXAPP");
			res.enabled=true;
			res=ResourceUtil.getResourceByType(resDebug, "CSS");
			ResourceUtil.save(resDebug, FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + "/resources-debug.json");
		}
		
		Resources resRelease = ResourceUtil.fromPath(FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + "/resources-release.json");
		if(resRelease!=null)
		{
			Resource res=ResourceUtil.getResourceByType(resDebug, "CXAPP");
			res.enabled=true;
			res=ResourceUtil.getResourceByType(resRelease, "CSS");
			ResourceUtil.save(resDebug, FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + "/resources-release.json");
		}
		
		
		String dstFolder = FolderKeyResolverUtil.toLocalPath(cxAppRoot,uuid,appIdentfier) + FolderKeyResolverUtil.resolveKey(FolderKeys.CM_CXAPP);
		
		File dstObject = new File(dstFolder);
		if(!dstObject.exists())
		{
			dstObject.mkdirs();
		}

		try {
			FileUtils.copyDirectory(srcObject, dstObject, new FileFilter() 
			{
				  public boolean accept(File pathname) 
				  {
					if(pathname.getAbsolutePath().contains("svn")){
			    		  return false;
			         }
					  return true;
			    }
			}, true);
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		*/
		
		//return "OK";
		return GroovyScriptManager.runScript("newCustomPlugin.groovy", uuid, appIdentfier,identifier,root,null,null,null,null,null);
	}
}
