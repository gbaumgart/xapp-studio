package cmx.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.json.annotations.SMDMethod;

import pmedia.utils.StringUtils;

import cmx.tools.StyleTreeFactory;
import cmx.types.ApplicationManager;
import cmx.types.ContentManager;
import cmx.types.DBConnectionError;
import cmx.types.Page;
import cmx.types.ShowcaseInfo;
import cmx.types.ShowcaseManager;
import cmx.types.StyleTree;
import cmx.types.TemplateInfo;
import cmx.types.TemplateManager;
import flexjson.JSONSerializer;

public class ShowcaseManagerAction extends CMBaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9097347805333543588L;

	
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
	public StyleTree getThemeStyles(
			String platform,String template)
	{
		return StyleTreeFactory.createThemeStyleTree(platform, template);
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
	public DBConnectionError applyTemplate(
			String uuid,
			String appIdentfier,
			String platform, 
			String templateTitle,
			String templatePlatform,
			int mode)
	{
		getCMObjects(uuid, appIdentfier);
		DBConnectionError e= new DBConnectionError();
		e =TemplateManager.applyTemplate(uuid, appIdentfier, platform, templateTitle, templatePlatform, mode);
		
		String fullUri =getRequest().getRequestURI();
		String referrer = getRequest().getHeader("referer");
		System.out.println("template change : " + fullUri + " at : " + referrer);
		e.msg=referrer;
		return e;
		//return StyleTreeFactory.createStyleTree(uuid, appIdentfier, platform, pageId, dataSourceUID, refId);
	}
	
	
	@SMDMethod
	public ShowcaseInfo getShowcase(String showcaseId,
			String uuid,
			String appIdentfier,
			String platform,
			String lang)
	{
		ShowcaseInfo showcase = ShowcaseManager.getShowcase(showcaseId,uuid,appIdentfier,platform,lang);
		return showcase;
		
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
