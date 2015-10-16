package cmq.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.json.annotations.SMDMethod;

import pmedia.utils.StringUtils;

import cmq.manager.ApplicationTemplateManager;
import cmq.types.ApplicationTemplate;
import cmx.action.CMBaseAction;
import cmx.tools.StyleTreeFactory;
import cmx.types.ApplicationManager;
import cmx.types.ContentManager;
import cmx.types.DBConnectionError;
import cmx.types.Page;
import cmx.types.StyleTree;
import cmx.types.TemplateInfo;
import cmx.types.TemplateManager;
import flexjson.JSONSerializer;

public class ApplicationTemplateManagerAction extends CMBaseAction
{
	/**
	 * 
	 */
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	
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
		//System.out.println("template change : " + fullUri + " at : " + referrer);
		e.msg=referrer;
		return e;
		//return StyleTreeFactory.createStyleTree(uuid, appIdentfier, platform, pageId, dataSourceUID, refId);
	}
	
	@SMDMethod
	public ArrayList<ApplicationTemplate> getApplicationTemplates(String lang)
	{
		return ApplicationTemplateManager.getApplicationTemplates(lang);
	}
	@SMDMethod
	public ApplicationTemplate getApplicationTemplate(String uuid,
			String appIdentfier,
			String platform,
			String applicationTemplateIdentifier)
	{
		ApplicationTemplate template = ApplicationTemplateManager.getApplicationTemplate(uuid, appIdentfier, platform,applicationTemplateIdentifier);
		return template;
		
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
