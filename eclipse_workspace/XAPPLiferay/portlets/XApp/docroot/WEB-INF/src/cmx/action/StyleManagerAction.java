package cmx.action;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.json.annotations.SMDMethod;

import pmedia.AssetTools.DownloadManager;
import pmedia.types.DownloadTask;
import pmedia.utils.StringUtils;

import cmx.tools.StyleTreeFactory;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ContentManager;
import cmx.types.DBConnectionError;
import cmx.types.Page;
import cmx.types.StyleTree;
import flexjson.JSONSerializer;

public class StyleManagerAction extends CMBaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9097347805333543588L;

	@SMDMethod
	public DBConnectionError deleteStyle(String uuid,
			String appIdentfier,
			String platform, 
			String srcRefId
			)
	{
		getCMObjects(uuid, appIdentfier);
		String userPath = appManager.getStoreRootPath() + uuid + "/apps/"+appIdentfier+"/style/IPHONE_NATIVE/";
		File styleDir = new File(userPath+srcRefId+"/");
		if(styleDir.exists()){
			try {
				FileUtils.deleteDirectory(styleDir);
			} catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}
		if(application!=null){
			application.flushCache=1;
		}
		
		StyleTreeFactory.unregisterStyle(srcRefId, uuid, appIdentfier, platform);
		
		DBConnectionError e = new DBConnectionError();
		return e;
	}
	@SMDMethod
	public DBConnectionError copyStyleTree(String uuid,
			String appIdentfier,
			String platform, 
			String srcRefId,
			String dstRefId)
	{
		getCMObjects(uuid, appIdentfier);
		DBConnectionError e = StyleTreeFactory.copyStyleTree(uuid, appIdentfier, platform, srcRefId, dstRefId);
		StyleTreeFactory.registerNewStyle(dstRefId, uuid, appIdentfier, platform);
		return e;
		
	}
	
	@SMDMethod
	public DBConnectionError copyStyle(String uuid,
			String appIdentfier,
			String srcPlatform,
			String dstPlatform,
			String srcRefId,
			String dstRefId)
	{
		getCMObjects(uuid, appIdentfier);
		DBConnectionError e = StyleTreeFactory.copyStyle(uuid, appIdentfier, srcPlatform, dstPlatform, srcRefId, dstRefId);
		
		if(application!=null){
			application.flushCache=1;
		}
		StyleTreeFactory.registerNewStyle(dstRefId, uuid, appIdentfier, srcPlatform);
		
		return e;
		
	}
	@SMDMethod
	public String downloadAssetFile(
			String uuid,
			String appIdentfier,
			String remoteFile)
	{
		if(appManager==null){
			appManager = ApplicationManager.getInstance();
		}
		DownloadTask task=new DownloadTask();
		task.remoteFilepath = remoteFile;
		
		//String remoteFileName = StringUtils.filenameComplete(remoteFile);
		String remoteFileName = StringUtils.filename(remoteFile) + "@2x." + StringUtils.extension(remoteFile);
		
		Application app = ApplicationManager.getInstance().getApplication(uuid, appIdentfier, false);
		if(app==null){
			return null;
		}

		String dstPath = appManager.getAppAssetsLocal(app.getUserIdentifier() , app.getApplicationIdentifier());
		task.localFilepath =dstPath + "/QXAppUploads/" + remoteFileName;
		DownloadManager.downloadFileInternalExt(task);
		return "/" + appIdentfier +"/Assets/QXAppUploads/" + remoteFileName;
	}
			
			
	@SMDMethod
	public void updateSheet(
			String uuid,
			String appIdentfier,
			String styleFileName, 
			String path,
			String content)
	{
		getCMObjects(uuid, appIdentfier);
		String filePathCSS = appManager.getUserAppPath(uuid, appIdentfier)+path;
		File cssFile = new File(filePathCSS);
		if(application!=null){
			application.flushCache=1;
		}
		if(!cssFile.exists())
			return;
		try {
			StringUtils.writeToFile(content, filePathCSS);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	@SMDMethod
	public StyleTree getStyles(
			String uuid,
			String appIdentfier,
			String platform, 
			String pageId,
			String dataSourceUID,
			String refId)
	{
		getCMObjects(uuid, appIdentfier);
		return StyleTreeFactory.createStyleTree(uuid, appIdentfier, platform, pageId, dataSourceUID, refId);
	}
	
	@SMDMethod
	public StyleTree getThemeStyles(
			String platform,String template)
	{
		return StyleTreeFactory.createThemeStyleTree(platform, template);
	}
	
	
	
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the applicationIdentifier
	 */
	public String getApplicationIdentifier() {
		return applicationIdentifier;
	}

	/**
	 * @param applicationIdentifier the applicationIdentifier to set
	 */
	public void setApplicationIdentifier(String applicationIdentifier) {
		this.applicationIdentifier = applicationIdentifier;
	}

	/**
	 * @return the appManager
	 */
	public ApplicationManager getAppManager() 
	{
		return appManager;
	}

	/**
	 * @param appManager the appManager to set
	 */
	public void setAppManager(ApplicationManager appManager) {
		this.appManager = appManager;
	}

	

	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		setSession(arg0);
	}
}
