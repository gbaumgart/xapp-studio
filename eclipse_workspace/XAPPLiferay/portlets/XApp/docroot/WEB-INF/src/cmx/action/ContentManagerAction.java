package cmx.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.json.annotations.SMDMethod;

import pmedia.AssetTools.DownloadManager;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.CIOverride;
import pmedia.types.Category;
import pmedia.types.DownloadTask;
import pmedia.utils.CITools;
import pmedia.utils.StringUtils;
import cmx.tools.LiferayContentTools;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.CIType;
import cmx.types.CMError;
import cmx.types.ConfigurableInformation;
import cmx.types.ContentManager;
import cmx.types.DataSourceBase;
import cmx.types.Page;
import cmx.types.ServiceSettings;

import com.liferay.portlet.asset.service.AssetCategoryServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import flexjson.JSONSerializer;

public class ContentManagerAction
   extends ActionSupport implements ServletRequestAware,ServletResponseAware,SessionAware
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9097347805333543588L;


	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	
	public CMError error;
	
	private String uuid;
	private String lang;
	private String type;
	private String archiveType;
	private String applicationIdentifier;
	private ApplicationManager appManager=null;
	public Application application=null;
	
	@SMDMethod
	public static ArticleData createArticle(String uuid,String appIdentfier,String title,String content,int catId)
	{
		return LiferayContentTools.toBaseData(uuid,appIdentfier,null,LiferayContentTools.createArticle(uuid, appIdentfier, title, content, catId),catId);
	}
	
	@SMDMethod
	public static ArticleData createLocation(String uuid,String appIdentfier,String title,String content,int catId,String street,String phone,String pcode,String city,String web,String lat,String lon,String image)
	{
		return LiferayContentTools.toBaseData(uuid,appIdentfier,null,LiferayContentTools.createLocation(uuid, appIdentfier, title, content, catId,street,phone,pcode,city,web,lat,lon,image),catId);
	}
	
	@SMDMethod
	public Category createCategory(String uuid,String appIdentfier,String title, int parent, String type)
	{
		//System.out.println("create category");
		return LiferayContentTools.createCategorySafe(uuid, appIdentfier, title, parent, type);
	}
	
	@SMDMethod
	public Category updateCategory(String uuid,String appIdentfier,String title, String refId)
	{
		//System.out.println("create category");
		return LiferayContentTools.updateCategorySafe(uuid, appIdentfier, title, refId);
		
	}
	
	@SMDMethod
	public void deleteCategory(String uuid,String appIdentfier,int catId)
	{
		//System.out.println("create category");
		LiferayContentTools.deleteCategorySafe(uuid, appIdentfier, catId);
	}
	
	@SMDMethod
	public void deleteLocation(String uuid,String appIdentfier,int refId)
	{
		//System.out.println("create category");
		LiferayContentTools.deleteLocationSafe(uuid, appIdentfier, refId);
	}
	
	@SMDMethod
	public void deleteArticle(String uuid,String appIdentfier,int refId)
	{
		//System.out.println("create category");
		LiferayContentTools.deleteArticleSafe(uuid, appIdentfier, refId);
	}
	
	@SMDMethod
	public void updateCategory(String uuid,String appIdentfier,String title, int parent,int catId)
	{
		LiferayContentTools.updateCategorySafe(uuid, appIdentfier, title, parent, catId);
		//System.out.println("create category");
	}
	
	public ApplicationManager getApplicationManager(String identifier)
	{
		appManager = ApplicationManager.getInstance();
		return appManager;
	}

	public Application getApplication(String identifier,String uuid)
	{
		appManager  = getApplicationManager(identifier);
		application= appManager.getApplication(uuid,identifier,"Debug");
		return application;
		
	}

	@SMDMethod
	public String saveLocation(String appIdentfier,String uuid,String refId,String content,String newTitle,int catId,String street,String phone,String pcode,String city,String web,String lat,String lon,String image)
	{
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		if(application!=null)
		{
			ContentManager.getInstance();
			
			String result = ContentManager.saveLocation(appIdentfier, uuid, refId, content,newTitle,catId,street,phone,pcode,city,web,lat,lon,image);
			if(result !=null)
			{
				return result;
			}
			
		
		}
		
		//AssetCategoryServiceUtil.addCategory(parentCategoryId, titleMap, descriptionMap, vocabularyId, categoryProperties, serviceContext)
		
		return null;
	}
	
	@SMDMethod
	public String saveArticle(String appIdentfier,String uuid,String refId,String content,String structureField,String newTitle)
	{
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		if(application!=null)
		{
			ContentManager.getInstance();
			
			String result = ContentManager.saveJournalArticle(appIdentfier, uuid, refId, content, structureField,newTitle);
			if(result !=null)
			{
				return result;
			}
			
		
		}
		
		//AssetCategoryServiceUtil.addCategory(parentCategoryId, titleMap, descriptionMap, vocabularyId, categoryProperties, serviceContext)
		
		return null;
	}
	
	
	
	public String smd() {
        return Action.SUCCESS;
    }
	

	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}


	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	public ApplicationManager getAppManager() {
		return appManager;
	}

	/**
	 * @param appManager the appManager to set
	 */
	public void setAppManager(ApplicationManager appManager) {
		this.appManager = appManager;
	}

	public HttpSession getSession()
	{
		return ((HttpServletRequest) ActionContext.getContext().get( StrutsStatics.HTTP_REQUEST)).getSession();
	}
	
	public HttpServletRequest getRequest()
	{
		return ((HttpServletRequest) ActionContext.getContext().get( StrutsStatics.HTTP_REQUEST));
	}
	



	/**
	 * @return the error
	 */
	public CMError getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(CMError error) {
		this.error = error;
	}

	
	public void setServletResponse(HttpServletResponse arg0) {
		
		servletResponse = arg0;
		
	}

	public void setServletRequest(HttpServletRequest arg0) {
			
		servletRequest = arg0;
		
	}

	/**
	 * @return the servletRequest
	 */
	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	/**
	 * @return the servletResponse
	 */
	public HttpServletResponse getServletResponse() {
		return servletResponse;
	}




	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the archiveType
	 */
	public String getArchiveType() {
		return archiveType;
	}

	/**
	 * @param archiveType the archiveType to set
	 */
	public void setArchiveType(String archiveType) {
		this.archiveType = archiveType;
	}
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		setSession(arg0);
	}
}
