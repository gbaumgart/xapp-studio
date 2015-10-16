package cmb.types;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.jcraft.jsch.JSchException;

import pmedia.types.ApplicationMetaData;
import pmedia.types.ApplicationMetaDataKeys;
import pmedia.types.PlatformGroup;
import pmedia.utils.CITools;
import pmedia.utils.StringUtils;
import pmedia.utils.XFileUtils;
import xappconnect.Utils.CustomTypeUtils;
import cmb.utils.HybridIndexBuilder;
import cmx.tools.ResourceUtil;
import cmx.tools.StyleTreeFactory;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ConfigurableInformation;
import cmx.types.ServiceSettings;
import cmx.types.StyleTree;
import cmx.types.StyleTreeItem;
import cmx.types.TemplateInfo;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class BuildManager 
{
	public static Boolean adjustCSSFileToApp(String uuid,
			String appIdentfier,
			String platform, 
			String templateTitle,
			String templatePlatform,
			int mode,
			String filePath)
	{
		
		String cssFileContent =null;
		try {
			cssFileContent =StringUtils.readFileAsString(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}
		String oriPathPrefix = ""+appIdentfier+"";
		/***
		 * The destination path should start with this : /ibizamedia5/Assets/
		 */
		String newPathPrefix = appIdentfier;
		
		cssFileContent=cssFileContent.replace("url('/" + oriPathPrefix, "url('/" +newPathPrefix);
		
		try {
			StringUtils.writeToFile(cssFileContent, filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	public static String getUserStyleRootFolder(String uuid,
			String appIdentfier,
			String platform)
	{
		ApplicationManager appMgr = ApplicationManager.getInstance();
		String userPath = appMgr.getStoreRootPath() + uuid + "/apps/"+appIdentfier+"/style/" + platform+ "/";
		
		return userPath;
	}
	
	public static void mergeSharedAssets(Application app,String dstFolder,String platform,String type,BuildOptions options)
	{
		ApplicationManager appMan = ApplicationManager.getInstance(); 
		
		String appPath = appMan.getUserAppPath(app.getUserIdentifier(), app.getApplicationIdentifier());
		String assetPath=System.getProperty("AppStoreRoot") + "shared";
		//System.out.println("loading app " + appPath);
		String data = null;
		try 
		{
			data = StringUtils.readFileAsString(appPath+"/appInfo.json");
		} catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
		
		if(data==null){
			return;
		}
		
		
		String cssData2 = StyleTreeFactory.getMergedCSS(app.getUserIdentifier(), app.getApplicationIdentifier(), app.getPlatform());

		Iterator iter =  null;
		
		String dstPath =  dstFolder+"/"; //appMan.getUserBasePathPublicLocal(app.getUserIdentifier()) + "/Archive/" + app.getApplicationIdentifier() +"/";
		
		StyleTree tree =StyleTreeFactory.createStyleTree(app.getUserIdentifier(), app.getApplicationIdentifier(), app.getPlatform(), null, null, null);
		
		iter = FileUtils.iterateFiles(new File(assetPath) ,TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		
		while(iter.hasNext()) {
			
		    File file = (File) iter.next();
		    String relativePath = file.getAbsolutePath().replace(assetPath, "");
		    if(file.isDirectory())
		    {
		    	continue;
		    }
		    
		    /**
		     * Adjust app info 
		     */
		    if(data.contains(relativePath) && options.includeAppMedia && options.includeApp)
		    {
		    	System.out.println("ll :  " + relativePath);
		    	//StringUtils.path(relativePath)
		    	String newPath = relativePath;
		    	
		    	data = data.replace(relativePath, newPath);
		    	//data = data.replace("//", "./");
		    	data = data.replace("//" +  app.getApplicationIdentifier() + "/", "/" +app.getApplicationIdentifier()+"/");
		    	
		    	data = data.replace("/" +  app.getApplicationIdentifier() + "/", "./");
		    	data = data.replace("/" +  app.getApplicationIdentifier() + "/", "./");
		    	data = data.replace("\"/SharedAssets/", "\"./SharedAssets/");
		    	//data = data.replace("\"./Assets/", "\"../../Assets/");
		    	
		    	File dstPathO = new File( dstPath+  "/" + StringUtils.path(relativePath));
		    	if(!dstPathO.exists()){
		    		dstPathO.mkdirs();
		    	}
		    	
		    	File dstFileO = new File( dstPath +"/"+ relativePath);
		    
					XFileUtils.copyFile(file, dstFileO);
				
		    }
		    		    
		    
		    if(tree!=null && options.includeAppMedia)
		    {
		    	
				for (int i = 0 ; i<tree.getItems().size() ; i++ )
				{
					StyleTreeItem item = (StyleTreeItem) tree.getItems().get(i);
					if(item.getContent() !=null)
					{
						if(item.getContent() !=null && item.getContent().contains(relativePath))
					    {
							String newPath = "/"  + relativePath;
					    	String cssDataNew = item.getContent().replace(relativePath, newPath);
							item.setContent(cssDataNew);
					    }
					}
				}
		    }
		    
		    
		    if(cssData2 !=null && cssData2.contains(relativePath))
		    {
		    	File dstPathO = new File(  dstPath + "/" + StringUtils.path(relativePath));
		    	if(!dstPathO.exists()){
		    		dstPathO.mkdirs();
		    	}
		    	
		    	File dstFileO = new File( dstPath +"/"+ relativePath);
		    	
					
		    	XFileUtils.copyFile(file, dstFileO);
				
		    }
		    
		}
		
		//save modified app :
		if(options.includeAppMedia && options.includeApp)
		{
			try {
				StringUtils.writeToFile(data, dstPath+"appInfo.json");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// the modified css 
		if(tree!=null && options.includeAppCSS)
	    {
			for (int i = 0 ; i<tree.getItems().size() ; i++ )
			{
				StyleTreeItem item = (StyleTreeItem) tree.getItems().get(i);
				if(item.getContent() !=null)
				{
					String cssFilePath = dstPath +  item.getPath(); 
					if(options.includeAppMedia){
						String strToReplace = "/"+app.getApplicationIdentifier() +"/";
						String newContent = item.getContent().replace(strToReplace,"/");
						newContent = newContent.replace("//","/");
						newContent = newContent.replace("http:/","http://");
						
						newContent = newContent.replace("'/Assets/","'../../Assets/");
						newContent = newContent.replace("'/SharedAssets/","'./SharedAssets/");
						
						item.setContent(newContent);
					}
					try {
						StringUtils.writeToFile(item.getContent(), cssFilePath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			JSONSerializer serializer = new JSONSerializer();
			serializer.prettyPrint(true);
			try {
				StringUtils.writeToFile( serializer.deepSerialize(tree) , dstPath+"styleTree.json");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    }
	}

	public void buildSystemResources(String dstPath,String uuid,String applicationId,String platform,String type,BuildOptions options)
	{
		System.out.println("creating resource package for " + uuid + " appId : " + applicationId);
		
		
		/***
		 * xapp JS
		 */
		if(options.includeSystemJS){
			
			//String xappCorePath = Fold
			if(options.runTimeConfiguration.equals("release")||options.runTimeConfiguration.equals("debug"))
			{
				String xappPath=ResourceUtil.resolveConstantsAbsolute("%XASWEB%/xappHybrid", uuid, applicationId);
				//System.out.println("adding xapp" + xappPath);
				
				//dstPath+="/xapp";
				File dstPathObject=new File(dstPath + "/xapp");
				if(dstPathObject.exists())
				{
					dstPathObject.mkdirs();
				}
				
				File srcPath=new File(xappPath);
				if(srcPath.exists())
				{
					try {
						FileUtils.copyDirectory(srcPath, dstPathObject, new FileFilter() 
						{
							  public boolean accept(File pathname) {
						        String name = pathname.getName();
						       if(pathname.getAbsolutePath().contains("svn") || pathname.getAbsolutePath().contains("uncompressed"))
						       {
						    	   return false;
						       }
						       return true;
						    }
						}, true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		
		/***
		 * Service Settings : 
		 */
		ServiceSettings settings = new ServiceSettings();
		settings.setAjaxplorerRootUrl(System.getProperty("AjaxplorerRootUrl"));
		settings.setFeatherEditorApiKey(System.getProperty("FeatherEditorApiKey"));
		settings.setWebPath(System.getProperty("WebPath"));
		settings.setImageScaleServlet(System.getProperty("imageProcessorUrl"));
		JSONSerializer serializer = new JSONSerializer();
		serializer.prettyPrint(true);
		String settingsStr = serializer.deepSerialize(settings);
		String settingsOutPath = dstPath+"/serviceSettings.json";
		try 
		{
			StringUtils.writeToFile(settingsStr, settingsOutPath);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
		
	}
	public void buildAppResources(String dstPath,String uuid,String applicationId,String platform,String type,BuildOptions options)
	{
		System.out.println("creating resource package for " + uuid + " appId : " + applicationId);
		String prefix = System.getProperty("WebPath") +"CMAC/"+uuid+"/apps/";
		
		if(options.includeAppMedia || options.includeAppCSS)
		{
		
			Application app = ApplicationManager.getInstance().getApplication(uuid, applicationId, false);
			if(app!=null)
			{
				mergeSharedAssets(app,dstPath,platform,type,options);
			}
			prefix="";
		}
		
		/*
		if(options.includeAppCSS)
		{
			String dstPlatform = "IPHONE_NATIVE";
			StyleTree a = StyleTreeFactory.createStyleTree(uuid, applicationId, dstPlatform,"", "", "");
			JSONSerializer serializer = new JSONSerializer();
			serializer.prettyPrint(true);
			
			String result = serializer.deepSerialize(a);
			result = result.replace("url('","url('" + prefix);
			if(result.contains( uuid + "/apps//SharedAssets/"))
			{
				result = result.replace(uuid + "/apps//SharedAssets" , "shared/SharedAssets");
			}
			try {
				StringUtils.writeToFile(result, dstPath + "/styleTree.json");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
			
	}
	public void buildMediaResources(String dstPath,String uuid,String applicationId,String platform,String type,BuildOptions options)
	{
		System.out.println("creating resource package for " + uuid + " appId : " + applicationId);
	}
	
	public void buildResources(String dstPath,String uuid,String applicationId,String platform,String type,BuildOptions options)
	{
		System.out.println("creating resource package for " + uuid + " appId : " + applicationId);
		
		//system resources
		buildSystemResources(dstPath, uuid, applicationId, platform, type, options);

		//app resources
		buildAppResources(dstPath, uuid, applicationId, platform, type, options);
	}
	
	
	private ArrayList<SSHCommandPipe>buildPipes = new ArrayList<SSHCommandPipe>();

	public String createAndroidHybridMetaZipFile(String uuid,String applicationId,String platform,String type,BuildOptions options)
	{
		
		System.out.println("creating meta package for " + uuid + " appId : " + applicationId);
		String result = System.getProperty("LOCAL_BUILD_TMP");
		result+="meta/"+uuid+"/"+applicationId+"/";
		
		ApplicationManager appManager=ApplicationManager.getInstance();
		Application app = appManager.getApplication(uuid, applicationId, false);
		
		String appPath = appManager.getAppDataPath(uuid, applicationId, null);
		
		String appPathTmp = result+"cmapp/"+applicationId;
		
		
		
		 //Create TMP Directory
		 

		File buildTmpDir = new File(result);
		if(buildTmpDir.exists()){
			buildTmpDir.delete();
		}
		buildTmpDir.mkdirs();
		
		if(buildTmpDir.exists()){
			buildTmpDir.delete();
		}
		buildTmpDir.mkdirs();
		buildTmpDir=new File(appPathTmp);
		
		try {
			FileUtils.copyDirectory(new File(appPath), new File(appPathTmp));
		} catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		buildResources(appPathTmp, uuid, applicationId, platform, type, options);
		
		if(app!=null)
		{
			ApplicationMetaData appMeta = app.getMetaData();
			
			ConfigurableInformation gAppleIcon0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_0);
			ConfigurableInformation gAppleIcon1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_1);
			
			ConfigurableInformation splash0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_0);
			ConfigurableInformation splash1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_1);
			ConfigurableInformation splash2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_2);
			
			
			String localIcon0Path =  appManager.getUserAppPath(uuid) + gAppleIcon0.getValue();
			String localIcon1Path =  appManager.getUserAppPath(uuid) + gAppleIcon1.getValue();
			
			String splash0Path =  appManager.getUserAppPath(uuid) + splash0.getValue();
			String splash1Path =  appManager.getUserAppPath(uuid) + splash1.getValue();
			String splash2Path =  appManager.getUserAppPath(uuid) + splash2.getValue();
		
		
				
				
			/**
			 * copy icons into place 
			 */
				XFileUtils.copyFile(new File(localIcon1Path), new File(result+"xappolixIcon.png"));
				XFileUtils.copyFile(new File(localIcon1Path), new File(result+"xappolixIcon@2x.png"));
				XFileUtils.copyFile(new File(splash1Path), new File(result+"Default.png"));
				XFileUtils.copyFile(new File(splash1Path), new File(result+"Default@2x.png"));
				XFileUtils.copyFile(new File(splash2Path), new File(result+"Default-568h@2x.png"));
			
			
			
			/***
			 * Index file
			 */
			String indexHTML = HybridIndexBuilder.createIndexFile(uuid, applicationId, "HYBRID_ANDROID", options.runTimeConfiguration, options);
			if(indexHTML!=null){
				String indexFilePath = appPathTmp+"/index.html";
				File indexFile = new File(indexFilePath);
				if(indexFile.exists())
				{
					indexFile.delete();
				}
				try {
					StringUtils.writeToFile(indexHTML, indexFilePath);
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			
			String zipFilePath = result + uuid+"-" + applicationId + "-"   + "meta.zip";
			File zip=new File(zipFilePath);
			if(zip.exists()){
				zip.delete();
			}
			try {
				// Initiate ZipFile object with the path/name of the zip file.
				ZipFile zipFile = new ZipFile(zipFilePath);
				zipFile.setRunInThread(false);
				
				// Folder to add
				String folderToAdd = result;
				
				
				// Initiate Zip Parameters which define various properties such
				// as compression method, etc.
				ZipParameters parameters = new ZipParameters();
				parameters.setIncludeRootFolder(false);
				
				// set compression method to store compression
				parameters.setCompressionMethod(Zip4jConstants.COMP_STORE);
				
				// Set the compression level
				parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
				
				
				// Add folder to the zip file
				zipFile.addFolder(folderToAdd, parameters);
					
				// Add the app folder to the zip file
				//zipFile.addFolder(appPath, parameters);
				
				
				//zipFile.removeFile(appPath+"/" + "appInfo.json");
				
			} catch (ZipException e) {
				e.printStackTrace();
				System.out.println("zip exception : " + e.getMessage());
			}
			
			String zipFileRemote = System.getProperty("LOCAL_BUILD_TMP") + uuid+"" + applicationId + ""   + "meta.zip";
			copyToBuildHost(zipFilePath,zipFileRemote);
			
			File zipFile = new File(zipFilePath);
			zipFile.delete();
			System.out.println("creating meta package");
		}else{
			System.out.println("have no app");
		}
		
		return result;
		
	}

	
	public AndroidHybridSSHBuildPipe createAndroidHybridIOSPipe(String uuid,String applicationId,String platform,String type,String options)
	{
		
		//create buildOptions
		BuildOptions boptions = null;
		JSONDeserializer<BuildOptions>dser = new JSONDeserializer<BuildOptions>();
		try {
			boptions=dser.deserialize(options);	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if(boptions==null)
		{
			return null;
		}
		
		
		/***
		 * Prepare Object
		 */
		AndroidHybridSSHBuildPipe pipe  = new AndroidHybridSSHBuildPipe();
		pipe.setDelegate(this);
		
		pipe.setUuid(uuid);
		pipe.setApplicationId(applicationId);
		pipe.setType(type);
		pipe.setPlatform(platform);
		
		pipe.setBuildStatusProgress(0.0f);
		pipe.setBuildStatus("Building");
		pipe.setBuildStatusMessage("Creating XCode Project");
		
		String metaZip = createIOSHybridMetaZipFile(uuid, applicationId,platform,type,boptions);
		System.out.println("created meta package : " + metaZip);
		
		
		Boolean build = true ;
		ApplicationManager appManager=ApplicationManager.getInstance();
		Application app = appManager.getApplication(uuid, applicationId, false);
		ApplicationMetaData appMeta = app.getMetaData();
		
		ConfigurableInformation title = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_TITLE);
		ConfigurableInformation bundleId= CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.ANDROID_PACKAGE);
		
		
		
		pipe.setApplicationTitle(title.getValue());
		
		pipe.setRtConfig(type);
		
		
		String bundleIdStr = bundleId.getValue();
		if(bundleIdStr==null || bundleIdStr.length()==0){
			bundleIdStr = "com." + title.getValue().trim().toLowerCase().replace(" ", "");
		}
		pipe.setBundleId(bundleIdStr);
		
		
		String user = System.getProperty("HYBRID_BUILD_USER");
		String password = System.getProperty("HYBRID_BUILD_PASSWORD");
		String host = System.getProperty("HYBRID_BUILD_HOST");
		String port = System.getProperty("HYBRID_BUILD_HOST_PORT");
		try {
			pipe.initSession(user, password, host, Integer.parseInt(port));
		} catch (JSchException e) {
			e.printStackTrace();
		}
				
		try {
			build = pipe.excecute("CreateUserApp.sh");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		}
		
		String zipFileRemote = System.getProperty("LOCAL_BUILD_TMP") + uuid+"" + applicationId + "-"  + PlatformGroup.HYBRID_ANDROID +"-"+boptions.runTimeConfiguration + ".zip";
		//String zipFileLocal = System.getProperty("LOCAL_BUILD_STORE_DIRECTORY") + uuid+"" + applicationId + ""   + ".zip";
		String zipFileLocal = System.getProperty("AppArchiveRoot") + uuid+"" + applicationId + "-" + PlatformGroup.HYBRID_ANDROID +"-"+boptions.runTimeConfiguration   + ".zip";
		File l=new File(zipFileLocal);
		if(l.exists()){
			l.delete();
		}
		downloadFromHyrbidBuildHost(zipFileRemote,zipFileLocal);
		System.out.println("download compiled app from " + zipFileRemote + " to " + zipFileLocal);
		
		return pipe;
	}
	
	
	public String addToHybridBuildQueue(String uuid,String applicationId,String platform,String type,String options)
	{
		String result = null;
		SSHCommandPipe pipe = (SSHCommandPipe) getPipe(uuid, applicationId, platform, type);
		if(pipe!=null){
			buildPipes.remove(pipe);
			pipe=null;
		}
		
		BuildOptions boptions = null;
		JSONDeserializer<BuildOptions>dser = new JSONDeserializer<BuildOptions>();
		try {
			boptions=dser.deserialize(options);	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if(boptions==null)
		{
			return null;
		}
		
		if(pipe==null)
		{
			if(platform!=null && platform.equals(pmedia.types.PlatformGroup.HYBRID_IOS))
			{
				pipe = createHybridIOSPipe(uuid, applicationId, platform, type,options);
				if(pipe!=null)
				{
					buildPipes.add(pipe);
					result =""+System.getProperty("WebPath") + "CMACArchives/";
					//result+=uuid+"" + applicationId + ""   + ".zip";
					result+=uuid+"" + applicationId + "-"  + PlatformGroup.HYBRID_IOS +"-" + boptions.runTimeConfiguration  + ".zip";
					return result;
				}
			}
			
			if(platform!=null && platform.equals(pmedia.types.PlatformGroup.HYBRID_ANDROID))
			{
				pipe = createAndroidHybridIOSPipe(uuid, applicationId, platform, type,options);
				if(pipe!=null)
				{
					buildPipes.add(pipe);
					result =""+System.getProperty("WebPath") + "CMACArchives/";
					result+=uuid+"" + applicationId + "-"  + PlatformGroup.HYBRID_ANDROID +"-" + boptions.runTimeConfiguration  + ".zip";
					return result;
				}
			}
			
		}
		
		
		
		return null;
	}
	public String addToBuildQueue(String uuid,String applicationId,String platform,String type,String options)
	{
		if(platform!=null && platform.equals(pmedia.types.PlatformGroup.HYBRID_IOS)||
				platform!=null && platform.equals(pmedia.types.PlatformGroup.HYBRID_ANDROID))
		{
			return addToHybridBuildQueue(uuid, applicationId, platform, type, options);
		}
		
		String result = null;
		SSHCommandPipe pipe = getPipe(uuid, applicationId, platform, type);
		if(pipe!=null){
			buildPipes.remove(pipe);
			pipe=null;
		}
		
		
		
		if(pipe==null)
		{
			pipe = createPipe(uuid, applicationId, platform, type);
			
			buildPipes.add(pipe);
			result =""+System.getProperty("WebPath") + "CMACArchives/";
			result+=uuid+"" + applicationId + ""   + ".zip";
			
		}
		return result;
	}
	
	public BuildOptions getBuidOptions(String uuid,String applicationId,String platform,String type)
	{
		
		return null;
	}
	

	
	public HybridSSHBuildPipe createHybridIOSPipe(String uuid,String applicationId,String platform,String type,String options)
	{
		
		//create buildOptions
		BuildOptions boptions = null;
		JSONDeserializer<BuildOptions>dser = new JSONDeserializer<BuildOptions>();
		try {
			boptions=dser.deserialize(options);	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if(boptions==null)
		{
			return null;
		}
		
		
		/***
		 * Prepare Object
		 */
		HybridSSHBuildPipe pipe  = new HybridSSHBuildPipe();
		pipe.setDelegate(this);
		
		pipe.setUuid(uuid);
		pipe.setApplicationId(applicationId);
		pipe.setType(type);
		pipe.setPlatform(platform);
		
		pipe.setBuildStatusProgress(0.0f);
		pipe.setBuildStatus("Building");
		pipe.setBuildStatusMessage("Creating XCode Project");
		
		String metaZip = createIOSHybridMetaZipFile(uuid, applicationId,platform,type, boptions);
		System.out.println("created meta package : " + metaZip);
		
		
		Boolean build = true ;
		if(!build){
			return null;
		}
		
		
		ApplicationManager appManager=ApplicationManager.getInstance();
		Application app = appManager.getApplication(uuid, applicationId, false);
		ApplicationMetaData appMeta = app.getMetaData();
		
		ConfigurableInformation title = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_TITLE);
		ConfigurableInformation bundleId= CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_BUNDLE_ID);
		
		pipe.setApplicationTitle(title.getValue());
		
		pipe.setRtConfig(type);
		
		
		String bundleIdStr = bundleId.getValue();
		if(bundleIdStr==null){
			bundleIdStr = "pm.cmios";
		}
		pipe.setBundleId(bundleIdStr);
		
		
		String user = System.getProperty("HYBRID_BUILD_USER");
		String password = System.getProperty("HYBRID_BUILD_PASSWORD");
		String host = System.getProperty("HYBRID_BUILD_HOST");
		String port = System.getProperty("HYBRID_BUILD_HOST_PORT");
		try {
			pipe.initSession(user, password, host, Integer.parseInt(port));
		} catch (JSchException e) {
			e.printStackTrace();
		}
				
		try {
			build = pipe.excecute("CreateUserApp.sh");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		}
		
		//String zipFileRemote = System.getProperty("LOCAL_BUILD_TMP") + uuid+"" + applicationId + ""   + ".zip";
		//String zipFileLocal = System.getProperty("LOCAL_BUILD_STORE_DIRECTORY") + uuid+"" + applicationId + ""   + ".zip";
		//String zipFileLocal = System.getProperty("AppArchiveRoot") + uuid+"" + applicationId + ""   + ".zip";
		
		String zipFileRemote = System.getProperty("LOCAL_BUILD_TMP") + uuid+"" + applicationId + "-"  + PlatformGroup.HYBRID_IOS +"-"+boptions.runTimeConfiguration + ".zip";
		//String zipFileLocal = System.getProperty("LOCAL_BUILD_STORE_DIRECTORY") + uuid+"" + applicationId + ""   + ".zip";
		String zipFileLocal = System.getProperty("AppArchiveRoot") + uuid+"" + applicationId + "-" + PlatformGroup.HYBRID_IOS +"-"+boptions.runTimeConfiguration   + ".zip";

		File l=new File(zipFileLocal);
		if(l.exists()){
			l.delete();
		}
		downloadFromHyrbidBuildHost(zipFileRemote,zipFileLocal);
		System.out.println("download compiled app from " + zipFileRemote + " to " + zipFileLocal);
		
		return pipe;
	}
	public SSHCommandPipe getPipe(String uuid,String applicationId,String platform,String type)
	{
		
		for (int i=0; i<buildPipes.size(); i++) 
		 {
			SSHCommandPipe pipe = buildPipes.get(i);
			 if( pipe!=null && pipe.getApplicationId().equals(applicationId) && 
					 pipe.getUuid().equals(uuid) &&
					 pipe.getPlatform().equals(platform) && 
					 pipe.getType().equals(type))
			 {
				 return pipe;
			 }	
		 }
		 
		 return null;
	}
	
	public Boolean copyToBuildHost(String localFile,String remoteFile)
	{
		Boolean result  = true;
		SSHCommandPipe pipe=new SSHCommandPipe();
		
		String user = System.getProperty("OSX_BUILD_USER");
		String password = System.getProperty("OSX_BUILD_PASSWORD");
		String host = System.getProperty("OSX_BUILD_HOST");
		String port = System.getProperty("OSX_BUILD_HOST_PORT");
		
		try {
			pipe.initSession(user, password, host, Integer.parseInt(port));
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result = pipe.copyToBuildHost(localFile, remoteFile);
		
		pipe.channel.disconnect();
		pipe.getSession().disconnect();
		
		
		
		return result;
	
	}
	
	public Boolean copyToBuildHostHybrid(String localFile,String remoteFile)
	{
		Boolean result  = true;
		SSHCommandPipe pipe=new SSHCommandPipe();
		
		String user = System.getProperty("HYBRID_BUILD_USER");
		String password = System.getProperty("HYBRID_BUILD_PASSWORD");
		String host = System.getProperty("HYBRID_BUILD_HOST");
		String port = System.getProperty("HYBRID_BUILD_HOST_PORT");
		
		try {
			pipe.initSession(user, password, host, Integer.parseInt(port));
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result = pipe.copyToBuildHost(localFile, remoteFile);
		
		pipe.channel.disconnect();
		pipe.getSession().disconnect();
		
		
		
		return result;
	
	}
	
	public Boolean downloadFromBuildHost(String remoteFile,String localFile)
	{
		Boolean result  = true;
		SSHCommandPipe pipe=new SSHCommandPipe();
		
		String user = System.getProperty("OSX_BUILD_USER");
		String password = System.getProperty("OSX_BUILD_PASSWORD");
		String host = System.getProperty("OSX_BUILD_HOST");
		String port = System.getProperty("OSX_BUILD_HOST_PORT");
		try {
			pipe.initSession(user, password, host, Integer.parseInt(port));
		} catch (JSchException e) {
			e.printStackTrace();
		}
		result = pipe.downloadFromHost(remoteFile,localFile);
		pipe.channel.disconnect();
		pipe.getSession().disconnect();
		return result;
	
	}
	
	public Boolean downloadFromHyrbidBuildHost(String remoteFile,String localFile)
	{
		Boolean result  = true;
		SSHCommandPipe pipe=new SSHCommandPipe();
		
		String user = System.getProperty("HYBRID_BUILD_USER");
		String password = System.getProperty("HYBRID_BUILD_PASSWORD");
		String host = System.getProperty("HYBRID_BUILD_HOST");
		String port = System.getProperty("HYBRID_BUILD_HOST_PORT");
		try {
			pipe.initSession(user, password, host, Integer.parseInt(port));
		} catch (JSchException e) {
			e.printStackTrace();
		}
		result = pipe.downloadFromHost(remoteFile,localFile);
		pipe.channel.disconnect();
		pipe.getSession().disconnect();
		return result;
	
	}
		
	public String createIOSHybridMetaZipFile(String uuid,String applicationId,String platform,String type,BuildOptions options)
	{
		
		System.out.println("creating meta package for " + uuid + " appId : " + applicationId);
		String result = System.getProperty("LOCAL_BUILD_TMP");
		result+="meta/"+uuid+"/"+applicationId+"/";
		
		ApplicationManager appManager=ApplicationManager.getInstance();
		Application app = appManager.getApplication(uuid, applicationId, false);
		
		String appPath = appManager.getAppDataPath(uuid, applicationId, null);
		
		String appPathTmp = result+"cmapp/"+applicationId;
		
		
		
		 //Create TMP Directory
		 

		File buildTmpDir = new File(result);
		if(buildTmpDir.exists()){
			buildTmpDir.delete();
		}
		buildTmpDir.mkdirs();
		
		if(buildTmpDir.exists()){
			buildTmpDir.delete();
		}
		buildTmpDir.mkdirs();
		buildTmpDir=new File(appPathTmp);
		
		try {
			FileUtils.copyDirectory(new File(appPath), new File(appPathTmp));
		} catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		buildResources(appPathTmp, uuid, applicationId, platform, type, options);
		
		if(app!=null)
		{
			ApplicationMetaData appMeta = app.getMetaData();
			
			ConfigurableInformation gAppleIcon0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_0);
			ConfigurableInformation gAppleIcon1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_1);
			ConfigurableInformation gAppleIcon2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_2);
			
			
			ConfigurableInformation splash0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_0);
			ConfigurableInformation splash1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_1);
			ConfigurableInformation splash2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_2);
			ConfigurableInformation splash3 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_3);
			ConfigurableInformation splash4 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_4);
			
			
			
			String localIcon0Path =  appManager.getUserAppPath(uuid) + gAppleIcon0.getValue();
			String localIcon1Path =  appManager.getUserAppPath(uuid) + gAppleIcon1.getValue();
			String localIcon2Path =  appManager.getUserAppPath(uuid) + gAppleIcon2.getValue();
			
			
			String splash0Path =  appManager.getUserAppPath(uuid) + splash0.getValue();
			String splash1Path =  appManager.getUserAppPath(uuid) + splash1.getValue();
			String splash2Path =  appManager.getUserAppPath(uuid) + splash2.getValue();
			String splash3Path =  appManager.getUserAppPath(uuid) + splash3.getValue();
			String splash4Path =  appManager.getUserAppPath(uuid) + splash4.getValue();
			
		
		
			ConfigurableInformation ipadsplash0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_0);
			ConfigurableInformation ipadsplash1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_1);
			ConfigurableInformation ipadsplash2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_2);
			ConfigurableInformation ipadsplash3 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_3);
			String ipadsplash0Path =  appManager.getUserAppPath(uuid) + ipadsplash0.getValue();
			String ipadsplash1Path =  appManager.getUserAppPath(uuid) + ipadsplash1.getValue();
			String ipadsplash2Path =  appManager.getUserAppPath(uuid) + ipadsplash2.getValue();
			String ipadsplash3Path =  appManager.getUserAppPath(uuid) + ipadsplash3.getValue();
			
			
				
				
			/**
			 * copy icons into place 
			 */
				XFileUtils.copyFile(new File(localIcon0Path), new File(result+"xappolixIcon.png"));
				XFileUtils.copyFile(new File(localIcon1Path), new File(result+"xappolixIcon@2x.png"));
				XFileUtils.copyFile(new File(localIcon2Path), new File(result+"xappolixIcon2@2x.png"));
			
			/**
			 * copy ipad icons into place 
			 */
			ConfigurableInformation gAppleIPADIcon0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_ICON_0);
			String localIPADIcon0Path =  appManager.getUserAppPath(uuid) + gAppleIPADIcon0.getValue();
				
			XFileUtils.copyFile(new File(localIPADIcon0Path), new File(result+"xappolixIconIPAD.png"));
			
			
			ConfigurableInformation gAppleIPADIcon1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_ICON_1);
			String localIPADIcon1Path =  appManager.getUserAppPath(uuid) + gAppleIPADIcon1.getValue();
				XFileUtils.copyFile(new File(localIPADIcon1Path), new File(result+"xappolixIconIPAD@2x.png"));
			ConfigurableInformation gAppleIPADIcon2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_ICON_2);
			String localIPADIcon2Path =  appManager.getUserAppPath(uuid) + gAppleIPADIcon2.getValue();
			
			XFileUtils.copyFile(new File(localIPADIcon2Path), new File(result+"xappolixIcon2IPAD.png"));
			ConfigurableInformation gAppleIPADIcon3 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_ICON_3);
			String localIPADIcon3Path =  appManager.getUserAppPath(uuid) + gAppleIPADIcon3.getValue();
				XFileUtils.copyFile(new File(localIPADIcon3Path), new File(result+"xappolixIconIPAD2@2x.png"));
			
			
			
			
			//splash
				XFileUtils.copyFile(new File(splash0Path), new File(result+"Default.png"));
				XFileUtils.copyFile(new File(splash1Path), new File(result+"Default@2x.png"));
				XFileUtils.copyFile(new File(splash1Path), new File(result+"Default-568h.png"));
				XFileUtils.copyFile(new File(splash2Path), new File(result+"Default-568h@2x.png"));
				XFileUtils.copyFile(new File(splash3Path), new File(result+"Default@2x.png"));
				XFileUtils.copyFile(new File(splash4Path), new File(result+"Default-568h@2x.png"));
				
				
			
			//splash ipad
				XFileUtils.copyFile(new File(ipadsplash0Path), new File(result+"Default-Landscape~ipad.png"));
				XFileUtils.copyFile(new File(ipadsplash1Path), new File(result+"Default-Landscape@2x~ipad.png"));
				XFileUtils.copyFile(new File(ipadsplash2Path), new File(result+"Default-Landscape2~ipad.png"));
				XFileUtils.copyFile(new File(ipadsplash3Path), new File(result+"Default-Landscape2@2x~ipad.png"));
				
				//now delete those !
				
			
			/***
			 * Index file
			 */
			String indexHTML = HybridIndexBuilder.createIndexFile(uuid, applicationId, platform, options.runTimeConfiguration, options);
			if(indexHTML!=null){
				String indexFilePath = appPathTmp+"/index.html";
				File indexFile = new File(indexFilePath);
				if(indexFile.exists())
				{
					indexFile.delete();
				}
				try {
					StringUtils.writeToFile(indexHTML, indexFilePath);
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			stripResources(app, appPathTmp, platform, null, null);
			
			String zipFilePath = result + uuid+"-" + applicationId + "-"   + "meta.zip";
			File zip=new File(zipFilePath);
			if(zip.exists()){
				zip.delete();
			}
			try {
				// Initiate ZipFile object with the path/name of the zip file.
				ZipFile zipFile = new ZipFile(zipFilePath);
				zipFile.setRunInThread(false);
				
				// Folder to add
				String folderToAdd = result;
				
				
				// Initiate Zip Parameters which define various properties such
				// as compression method, etc.
				ZipParameters parameters = new ZipParameters();
				parameters.setIncludeRootFolder(false);
				
				// set compression method to store compression
				parameters.setCompressionMethod(Zip4jConstants.COMP_STORE);
				
				// Set the compression level
				parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
				
				
				// Add folder to the zip file
				zipFile.addFolder(folderToAdd, parameters);
					
				// Add the app folder to the zip file
				//zipFile.addFolder(appPath, parameters);
				
				
				//zipFile.removeFile(appPath+"/" + "appInfo.json");
				
			} catch (ZipException e) {
				e.printStackTrace();
				System.out.println("zip exception : " + e.getMessage());
			}
			
			String zipFileRemote = System.getProperty("LOCAL_BUILD_TMP") + uuid+"" + applicationId + ""   + "meta.zip";
			copyToBuildHostHybrid(zipFilePath,zipFileRemote);
			
			File zipFile = new File(zipFilePath);
			zipFile.delete();
			System.out.println("creating meta package");
		}else{
			System.out.println("have no app");
		}
		
		return result;
		
	}
	public static boolean isIOSMetaFile(Application app,String file){
		
		ApplicationMetaData appMeta = app.getMetaData();
		
		if(file==null){
			return false;
		}
		
		ConfigurableInformation gAppleIcon0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_0);
		if(gAppleIcon0 !=null &&  gAppleIcon0.getValue() !=null && file.contains(gAppleIcon0.getValue())){
			return true;
		}
		
		ConfigurableInformation gAppleIcon1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_1);
		if(gAppleIcon1 !=null && gAppleIcon1.getValue() !=null &&  file.contains(gAppleIcon1.getValue())){
			return true;
		}
		ConfigurableInformation gAppleIcon2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_2);
		if(gAppleIcon2 !=null &&  gAppleIcon2.getValue() !=null &&  file.contains(gAppleIcon2.getValue())){
			return true;
		}
		
		
		ConfigurableInformation splash0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_0);
		if(splash0 !=null &&  splash0.getValue() !=null &&  file.contains(splash0.getValue())){
			return true;
		}
		ConfigurableInformation splash1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_1);
		if(splash1 !=null &&  splash1.getValue() !=null &&  file.contains(splash1.getValue())){
			return true;
		}
		
		ConfigurableInformation splash2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_2);
		if(splash2 !=null && splash2.getValue() !=null &&  file.contains(splash2.getValue())){
			return true;
		}
		
		ConfigurableInformation splash3 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_3);
		if(splash3 !=null &&  splash3.getValue() !=null &&  file.contains(splash3.getValue())){
			return true;
		}
		
		/*
		ConfigurableInformation splash4 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_4);
		if(file.contains(splash4.getValue())){
			return true;
		}
		*/
		
		ConfigurableInformation ipadsplash0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_0);
		if(ipadsplash0 !=null && ipadsplash0.getValue() !=null && file.contains(ipadsplash0.getValue())){
			return true;
		}
		
		ConfigurableInformation ipadsplash1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_1);
		if(ipadsplash1 !=null && ipadsplash1.getValue() !=null && file.contains(ipadsplash1.getValue())){
			return true;
		}
		ConfigurableInformation ipadsplash2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_2);
		if(ipadsplash2 !=null && ipadsplash2.getValue() !=null &&  file.contains(ipadsplash2.getValue())){
			return true;
		}
		ConfigurableInformation ipadsplash3 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_3);
		if(ipadsplash3 !=null && ipadsplash3.getValue() !=null &&  file.contains(ipadsplash3.getValue())){
			return true;
		}
		ConfigurableInformation gAppleIPADIcon0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_ICON_0);
		if(gAppleIPADIcon0 !=null && gAppleIPADIcon0.getValue() !=null &&  file.contains(gAppleIPADIcon0.getValue())){
			return true;
		}
		ConfigurableInformation gAppleIPADIcon1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_ICON_1);
		if(gAppleIPADIcon1 !=null &&  gAppleIPADIcon1.getValue() !=null && file.contains(gAppleIPADIcon1.getValue())){
			return true;
		}
		ConfigurableInformation gAppleIPADIcon2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_ICON_2);
		if(gAppleIPADIcon2 !=null &&  gAppleIPADIcon2.getValue() !=null && file.contains(gAppleIPADIcon2.getValue())){
			return true;
		}
		ConfigurableInformation gAppleIPADIcon3 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_ICON_3);
		if(gAppleIPADIcon3 !=null &&   gAppleIPADIcon3.getValue() !=null && file.contains(gAppleIPADIcon3.getValue())){
			return true;
		}
		
		
		return false;
	}
	public static void stripResources(Application app,String dstFolder,String platform,String type,BuildOptions options)
	{

		ApplicationManager appMan = ApplicationManager.getInstance(); 
		
		String appPath = appMan.getUserAppPath(app.getUserIdentifier(), app.getApplicationIdentifier());
		//String assetPath=System.getProperty("AppStoreRoot") + "shared";
		//System.out.println("loading app " + appPath);
		String data = null;
		try 
		{
			data = StringUtils.readFileAsString(appPath+"/appInfo.json");
		} catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
		
		if(data==null){
			return;
		}

		Iterator iter =  null;
		
		Boolean isAndroid = true;
		if(platform!=null && platform.equals(pmedia.types.PlatformGroup.HYBRID_IOS))
		{
			isAndroid=false;
		}
		
		String dstPath =  dstFolder+"/"; //appMan.getUserBasePathPublicLocal(app.getUserIdentifier()) + "/Archive/" + app.getApplicationIdentifier() +"/";
		
		StyleTree tree =StyleTreeFactory.createStyleTree(app.getUserIdentifier(), app.getApplicationIdentifier(), app.getPlatform(), null, null, null);
		
		iter = FileUtils.iterateFiles(new File(dstFolder) ,TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		
		while(iter.hasNext()) {
			
		    File file = (File) iter.next();
		    String absPath = file.getAbsolutePath().toLowerCase();
		    
		    System.out.println("iterate over file : " + absPath);
		    if(data.contains(absPath))
		    {		    	
		    	System.out.println("\t app info contains : " + absPath);
		    }
		    
		    if(file.isDirectory())
		    {
		    	continue;
		    }
		    if(absPath.contains(".css")||absPath.contains(".json")){
		    	continue;
		    }
		    if(absPath.contains(".xml")){
		    	continue;
		    }
		    
		    if(!isAndroid && absPath.contains("appsplash")){
		    	file.delete();
		    	continue;
		    }
		    if(isIOSMetaFile(app,absPath)){
		    	file.delete();
		    	continue;
		    }
		    //delete unused pictures
		    if(absPath.contains(".png")||absPath.contains(".jpg")){
		    	String fileName = StringUtils.filenameComplete(absPath);
		    	if(fileName!=null){
		    		if(data.contains(fileName)){
		    			
		    		}else{
		    			file.delete();
		    			continue;
		    		}
		    	}
		    }
		    
		    
		}
	}
	public String createIOSMetaZipFile(String uuid,String applicationId){
		
		System.out.println("creating meta package for " + uuid + " appId : " + applicationId);
		String result = System.getProperty("LOCAL_BUILD_TMP");
		result+="meta/"+uuid+"/"+applicationId+"/";
		
		ApplicationManager appManager=ApplicationManager.getInstance();
		Application app = appManager.getApplication(uuid, applicationId, false);
		
		String appPath = appManager.getAppDataPath(uuid, applicationId, null);
		
		String appPathTmp = result+"cmapp/"+applicationId;
		
		 //Create TMP Directory
		 

		File buildTmpDir = new File(result);
		if(buildTmpDir.exists()){
			buildTmpDir.delete();
		}
		buildTmpDir.mkdirs();
		
		if(buildTmpDir.exists()){
			buildTmpDir.delete();
		}
		buildTmpDir.mkdirs();
		buildTmpDir=new File(appPathTmp);
		
		try {
			FileUtils.copyDirectory(new File(appPath), new File(appPathTmp));
		} catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		if(app!=null)
		{
			ApplicationMetaData appMeta = app.getMetaData();
			
			ConfigurableInformation gAppleIcon0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_0);
			ConfigurableInformation gAppleIcon1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_1);
			
			ConfigurableInformation splash0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_0);
			ConfigurableInformation splash1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_1);
			ConfigurableInformation splash2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_2);
			
			
			String localIcon0Path =  appManager.getUserAppPath(uuid) + gAppleIcon0.getValue();
			String localIcon1Path =  appManager.getUserAppPath(uuid) + gAppleIcon1.getValue();
			
			String splash0Path =  appManager.getUserAppPath(uuid) + splash0.getValue();
			String splash1Path =  appManager.getUserAppPath(uuid) + splash1.getValue();
			String splash2Path =  appManager.getUserAppPath(uuid) + splash2.getValue();
		
			
				
			XFileUtils.copyFile(new File(localIcon1Path), new File(result+"xappolixIcon.png"));
			XFileUtils.copyFile(new File(localIcon1Path), new File(result+"xappolixIcon@2x.png"));
			
			XFileUtils.copyFile(new File(splash1Path), new File(result+"Default.png"));
			XFileUtils.copyFile(new File(splash1Path), new File(result+"Default@2x.png"));
			File splas2  = new File(splash2Path);
			if(splas2.exists()){
				XFileUtils.copyFile(new File(splash2Path), new File(result+"Default-568h@2x.png"));
			}
				
			
			
			
			
			String zipFilePath = result + uuid+"-" + applicationId + "-"   + "meta.zip";
			File zip=new File(zipFilePath);
			if(zip.exists()){
				zip.delete();
			}
			try {
				// Initiate ZipFile object with the path/name of the zip file.
				ZipFile zipFile = new ZipFile(zipFilePath);
				zipFile.setRunInThread(false);
				
				// Folder to add
				String folderToAdd = result;
				
				
				// Initiate Zip Parameters which define various properties such
				// as compression method, etc.
				ZipParameters parameters = new ZipParameters();
				parameters.setIncludeRootFolder(false);
				
				// set compression method to store compression
				parameters.setCompressionMethod(Zip4jConstants.COMP_STORE);
				
				// Set the compression level
				parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
				
				String ctScope=null;
				if(ctScope==null)
				{	
					ctScope = "%XASWEB%";
				}
				String prefix = "/ctypes/";
				//public static ArrayList<CustomType>getTypes(String rtConfig,String platform,String uuid,String appId,String systemAppName,String storeRoot)
				
				//String customTypeFolder =String dstFilePath =storeRoot + "/" + platform + "/" + rtConfig + "/"; 
				
				
				// Add folder to the zip file
				zipFile.addFolder(folderToAdd, parameters);
					
				// Add the app folder to the zip file
				//zipFile.addFolder(appPath, parameters);
				
				
				//zipFile.removeFile(appPath+"/" + "appInfo.json");
				
			} catch (ZipException e) {
				e.printStackTrace();
				System.out.println("zip exception : " + e.getMessage());
			}
			
			String zipFileRemote = System.getProperty("LOCAL_BUILD_TMP") + uuid+"" + applicationId + ""   + "meta.zip";
			copyToBuildHost(zipFilePath,zipFileRemote);
			
			File zipFile = new File(zipFilePath);
			zipFile.delete();
			System.out.println("creating meta package");
		}else{
			System.out.println("have no app");
		}
		
		return result;
		
	}
	
	public void onPipeMessage(SSHCommandPipe pipe,String message)
	{
		
		System.out.print("Build Message : " + message);
		if(message.contains("CMIOSBUILD_FINISH"))
		{
			System.out.print("!!!! ---- Build Finish : " );
		}
		
	}
	public SSHBuildPipe createPipe(String uuid,String applicationId,String platform,String type)
	{
		
		/***
		 * Prepare Object
		 */
		SSHBuildPipe pipe  = new SSHBuildPipe();
		pipe.setDelegate(this);
		
		pipe.setUuid(uuid);
		pipe.setApplicationId(applicationId);
		pipe.setType(type);
		pipe.setPlatform(platform);
		
		pipe.setBuildStatusProgress(0.0f);
		pipe.setBuildStatus("Building");
		pipe.setBuildStatusMessage("Creating XCode Project");
		
		String metaZip = createIOSMetaZipFile(uuid, applicationId);
		System.out.println("created meta package : " + metaZip);
		
		Boolean build = true ;
		ApplicationManager appManager=ApplicationManager.getInstance();
		Application app = appManager.getApplication(uuid, applicationId, false);
		ApplicationMetaData appMeta = app.getMetaData();
		
		ConfigurableInformation title = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_TITLE);
		ConfigurableInformation bundleId= CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_BUNDLE_ID);
		
		pipe.setApplicationTitle(title.getValue());
		
		String bundleIdStr = bundleId.getValue();
		if(bundleIdStr==null){
			bundleIdStr = "pm.cmios";
		}
		pipe.setBundleId(bundleIdStr);
		
		
		String user = System.getProperty("OSX_BUILD_USER");
		String password = System.getProperty("OSX_BUILD_PASSWORD");
		String host = System.getProperty("OSX_BUILD_HOST");
		String port = System.getProperty("OSX_BUILD_HOST_PORT");
		try {
			pipe.initSession(user, password, host, Integer.parseInt(port));
		} catch (JSchException e) {
			e.printStackTrace();
		}
		//pipe.channel.disconnect();
		//pipe.getSession().disconnect();
				
		try {
			build = pipe.excecute("CreateUserApp.sh");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		}
		
		String zipFileRemote = System.getProperty("LOCAL_BUILD_TMP") + uuid+"" + applicationId + ""   + ".zip";
		//String zipFileLocal = System.getProperty("LOCAL_BUILD_STORE_DIRECTORY") + uuid+"" + applicationId + ""   + ".zip";
		String zipFileLocal = System.getProperty("AppArchiveRoot") + uuid+"" + applicationId + ""   + ".zip";
		File l=new File(zipFileLocal);
		if(l.exists()){
			l.delete();
		}
		downloadFromBuildHost(zipFileRemote,zipFileLocal);
		System.out.println("download compiled app from " + zipFileRemote + " to " + zipFileLocal);
		
		return pipe;
	}
	
	
	
	
	private Boolean hasApplicationBuild(String uuid,String applicationId,String platform,String type)
	{
		String path = getApplicationBuildPath(uuid, applicationId, platform, type);
		
		path+=uuid+applicationId+".zip";
		if(path!=null)
		{			
			
			File userPathFileObject = new File(path);
			if(userPathFileObject.exists())
			{
				return true;
			}
			/*
			File userPathFileObject = new File(path);
			if(userPathFileObject.exists() && userPathFileObject.isDirectory())
			{
				 File[] files = userPathFileObject.listFiles();
				 for (int i=0; i<files.length; i++) 
				 {
					 	File _file = files[i];
					 	if(!_file.isDirectory())
					 	{
					 		String extension = StringUtils.extension(_file.getAbsolutePath());
					 		if(extension!=null && extension.equals("zip"))
					 		{
					 			
					 		}
					 	}
				 }
			
			}
			*/
		}
		
		return false;
		
	}
	private String getApplicationBuildPath(String uuid,String applicationId,String platform,String type)
	{
		ApplicationManager appManager=ApplicationManager.getInstance();
		Application app = appManager.getApplication(uuid, applicationId, false);
		String result = null;
		if(app!=null)
		{
			result = System.getProperty("LOCAL_BUILD_TMP");  //appManager.getUserAppPath(uuid, applicationId); Sys
			
			/*result+="builds/";
			result+=platform+"/";
			result+=type;
			*/
			File dir=new File(result);
			if(!dir.exists())
			{
				dir.mkdirs();
			}
			
			return result;
		}
		
		return null;
	}
	public ArrayList <CMBApplicationBuild> getApplicationBuilds(String uuid,String applicationId,String platform)
	{
		
		if(platform==null){
			platform="iPhone";
		}
		
		ArrayList<CMBApplicationBuild>_applicationBuilds = new ArrayList<CMBApplicationBuild>(); 
		ApplicationManager appManager=ApplicationManager.getInstance();
		Application app = appManager.getApplication(uuid, applicationId, false);
		if(app!=null)
		{
			String applicationBuildDirectory = getApplicationBuildPath(uuid, applicationId, platform, "Debug");
			if(applicationBuildDirectory!=null)
			{
				//appManager.getUserApplications(uuid)
				Boolean hasBuild = hasApplicationBuild(uuid, applicationId, platform, "Debug");
				hasBuild=false;
				CMBApplicationBuild build = new CMBApplicationBuild();
				build.setApplicationId(applicationId);
				build.setPlatform(platform);
				build.setType("Debug(Testing Only)");
				build.setUuid(uuid);
				
			//	if(!uuid.equals(System.getProperty("adminUser")))				{
					if(hasBuild)
					{
						build.setBuildStatus("Builded");
						build.setBuildStatusProgress(1);
						build.setBuildStatusMessage(System.getProperty("WebPath") + "CMACArchives/" +uuid+"" + applicationId + ""   + ".zip");
						_applicationBuilds.add(build);
						return _applicationBuilds;
					}
			//	}
				
				SSHCommandPipe pipe= getPipe(uuid, applicationId, platform, "Debug");
				if(pipe!=null){
					
					build.setBuildStatus(pipe.getBuildStatus());
					build.setBuildStatusMessage(pipe.getBuildStatusMessage());
					build.setBuildStatusProgress(pipe.getBuildStatusProgress());
				}
				
				_applicationBuilds.add(build);
			}
		}
		
		return _applicationBuilds;
	}
	/*
	public CMBApplicationBuilds getTaskStatus(String uuid,String applicationId,String platform)
	{
		//ArrayList<CMMSubscription>_userSubscriptions = LiferayTools.getUserSubscriptions(uuid);
		//System.out.println("user subscriptions : " + _userSubscriptions.size());
		
		
		/*
		String salesDate = "2012-03-17 10:50:20"; 
				
		DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
        Date dSalesDate = null; 
        
        try {
			dSalesDate = format.parse(salesDate);
		} catch (ParseException e) {
			
		}
        
        System.out.println("sales date   : "  + dSalesDate.toString());
        */
		/*
		if(subscriptionsInPorgress==null){
			subscriptionsInPorgress = new ArrayList<CMMSubscriptionStatus>();
		}
		
		
		for(int  i = 0 ; i < subscriptionsInPorgress.size() ; i ++)
		{
			CMMSubscriptionStatus status = subscriptionsInPorgress.get(i);
			if(status.getUuid().equals(uuid) && status.getReferenceNumber().equals(referenceNumber)){
				return status;
			}
		}
		
		return null;
	}
	*/
	/*
	
	public void addToPurchaseQueue(String uuid,String referenceNumber)
	{
		if(subscriptionsInPorgress==null)
		{
			subscriptionsInPorgress=new ArrayList<CMMSubscriptionStatus>();
		}
		
		CMMSubscriptionStatus status = getSubscriptionStatus(uuid, referenceNumber);
		if(status!=null){
			System.out.println("#### Subscription Purchase already in progress");
			subscriptionsInPorgress.remove(status);
			status=null;
		}
		
		status = new CMMSubscriptionStatus();
		status.setUuid(uuid);
		status.setReferenceNumber(referenceNumber);
		status.setCheckoutStatus("REQUESTED");
		subscriptionsInPorgress.add(status);
		
	}
	

*/
	



	private static BuildManager sInstance = new BuildManager();
	
	
	
	public static BuildManager getInstance() {
		return sInstance;
	}

	public static void setInstance(BuildManager sInstance) 
	{
		BuildManager.sInstance = sInstance;
	}


	public ArrayList<SSHCommandPipe> getBuildPipes() {
		return buildPipes;
	}


	public void setBuildPipes(ArrayList<SSHCommandPipe> buildPipes) 
	{
		this.buildPipes = buildPipes;
	};
	
	
	
	/*public Application getById(String uuid,String appId){
		
		for (int i  = 0  ;  i < applications.size() ;  i ++){
			Application app = applications.get(i);
			if(app.getUserIdentifier().equals(uuid) && app.getApplicationIdentifier().equals(appId)){
				return app;
			}
		}
		
		return null;
	}
	
	*/
	


	/**
	 * @return the applications
	 */
	
}
