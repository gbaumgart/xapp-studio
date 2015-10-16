package cmx.action;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.json.annotations.SMDMethod;

import pmedia.AssetTools.DownloadManager;
import pmedia.types.DownloadTask;
import pmedia.utils.StringUtils;

import cmx.manager.ResourceManager;
import cmx.tools.StyleTreeFactory;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ContentManager;
import cmx.types.DBConnectionError;
import cmx.types.Page;
import cmx.types.Resource;
import cmx.types.ResourceTree;
import cmx.types.Resources;
import cmx.types.StyleTree;
import flexjson.JSONSerializer;

public class ResourceManagerAction extends CMBaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9097347805333543588L;

	@SMDMethod
	public Resources removeResource(
			String uuid,
			String appIdentfier,
			String platform,
			String path,
			String newResource)
	{
		getCMObjects(uuid, appIdentfier);
		if(application!=null){
			application.flushCache=1;
		}
		
		return ResourceManager.removeResource(uuid, appIdentfier, platform, path,newResource);
	}
	
	@SMDMethod
	public Resources updateResource(
			String uuid,
			String appIdentfier,
			String platform,
			String path,
			String oldResource,
			String newResource)
	{
		getCMObjects(uuid, appIdentfier);
		if(application!=null){
			application.flushCache=1;
		}
		
		return ResourceManager.updateResource(uuid, appIdentfier, platform, path,oldResource,newResource);
	}
	
	@SMDMethod
	public Resources getResource(
			String uuid,
			String appIdentfier,
			String platform,
			String path)
	{
		getCMObjects(uuid, appIdentfier);
		
		Resources result = ResourceManager.getResources(uuid, appIdentfier, platform, path);
		
		if(result.items!=null && result.items.size()==0){
			Resource res = new Resource();
			res.type = "CSS";
			res.url = "THIS IS A DUMMY-RESOURCE";
			res.enabled=false;
			result.items.add(res);
			
		}
		
		return result;
	}
	
	@SMDMethod
	public Resources addResource(
			String uuid,
			String appIdentfier,
			String platform,
			String path,
			String newResource)
	{
		getCMObjects(uuid, appIdentfier);
		if(application!=null){
			application.flushCache=1;
		}
		
		return ResourceManager.addResource(uuid, appIdentfier, platform, path,newResource);
	}
	
	@SMDMethod
	public ResourceTree getResourceTree(
			String uuid,
			String appIdentfier,
			String platform,
			String appName)
	{
		getCMObjects(uuid, appIdentfier);
		
		return ResourceManager.getResourceTree(uuid, appIdentfier, platform, appName);
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
