

package cmx.action;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.json.annotations.SMDMethod;
import org.xml.sax.SAXException;

import pmedia.utils.BitUtils;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.SecurityUtil;
import cmx.tools.JoomlaContentTreeFactory;
import cmx.tools.GoogleTreeFactory;
import cmx.tools.LiferayContentTools;
import cmx.tools.LiferayContentTreeFactory;
import cmx.tools.WordpressTreeFactory;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.CINames;
import cmx.types.CMError;
import cmx.types.ConfigurableInformation;
import cmx.types.ContentTree;
import cmx.types.ECMContentSourceType;
import cmx.types.Page;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

//import com.pm.Catalog;
//import com.pm.DBContextHolder;
//import com.pm.DBSource;


//@Namespace("/db")
//@Result(name="success",location="/dummy.jsp")
//@Result(location="${redirectURL}", type="redirect")

public class ContentTreeAction
   extends ActionSupport implements ServletRequestAware,ServletResponseAware,SessionAware
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2071708732299385367L;
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	
	public CMError error;
	
	private String lang;
	private String type;
	private String applicationIdentifier;
	private ApplicationManager appManager=null;
	public Application application=null;
	
	@SMDMethod
	public ContentTree getBannerCategories(String uuid,String appIdentfier,String elementId)
	{
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		ContentTree tree = null;
		if(application!=null){
			
			Page element = application.getElementById(Integer.parseInt(elementId));
			if(element!=null)
			{
				
				ConfigurableInformation contentSourceCI = element.getItemByChainAndName(0,"Content Source");
				if(contentSourceCI!=null)
				{
					if(contentSourceCI.dataSource!=null)
					{
					
						try {
							ECMContentSourceType  sType = ECMContentSourceTypeTools.FromString(contentSourceCI.value);
							switch (sType) {
								case JoomlaSection:
								case JoomlaCategory:
								{
									tree = JoomlaContentTreeFactory.createContentTree(appManager,application, contentSourceCI.dataSource);
									break;
								}
								case JoomlaBannerCategory:
								{
									tree = JoomlaContentTreeFactory.createMostetCategoryTree(appManager,application, contentSourceCI.dataSource);
									break;
								}
							}
							
							
							
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							
							e.printStackTrace();
						}
					}
				}
			}
		}
		return tree;
	}
	
	@SMDMethod
	public ContentTree getBlueFrameForms(String uuid,String appIdentfier,String elementId)
	{
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		ContentTree tree = null;
		if(application!=null){
			
			Page element = application.getElementById(Integer.parseInt(elementId));
			if(element!=null)
			{
			
				ConfigurableInformation contentSourceCI = element.getItemByChainAndName(0,"Content Source");
				if(contentSourceCI!=null)
				{
					if(contentSourceCI.dataSource!=null)
					{
					
						try {
							ECMContentSourceType  sType = ECMContentSourceTypeTools.FromString(contentSourceCI.value);
							switch (sType) {
								case JoomlaSection:
								case JoomlaCategory:
								{
									tree = JoomlaContentTreeFactory.createContentTree(appManager,application, contentSourceCI.dataSource);
									break;
								}
								case JoomlaBannerCategory:
								{
									tree = JoomlaContentTreeFactory.createMostetCategoryTree(appManager,application, contentSourceCI.dataSource);
									break;
								}
							}
							
							
							
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							
							e.printStackTrace();
						}
					}
				}
			}
		}
		return tree;
	}
	@SMDMethod
	public ContentTree getCategoriesByCIName(String uuid,String appIdentfier,String elementId,String ciName)
	{
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		ContentTree tree = null;
		if(application!=null){
			
			Page element = application.getElementById(Integer.parseInt(elementId));
			if(element!=null)
			{
			
				ConfigurableInformation contentSourceCI = element.getItemByChainAndName(0,ciName);
				if(contentSourceCI==null)
				{
					contentSourceCI = element.getItemByChainAndName(0,CINames.BannerSource);
				}
				if(contentSourceCI!=null)
				{
					if(contentSourceCI.dataSource!=null)
					{
					
						try {
							ECMContentSourceType  sType = ECMContentSourceTypeTools.FromString(contentSourceCI.value);
							switch (sType) {
								case JoomlaSection:
								case JoomlaCategory:
								{
									tree = JoomlaContentTreeFactory.createContentTree(appManager,application, contentSourceCI.dataSource);
									break;
								}
								case MosetTreeCategory:
								{
									tree = JoomlaContentTreeFactory.createMostetCategoryTree(appManager,application, contentSourceCI.dataSource);
									break;
								}
								
								case JoomlaBannerCategory:
								{
									tree = JoomlaContentTreeFactory.createJoomlaBannerCategoryTree(appManager,application, contentSourceCI.dataSource);
									break;
								}
							}
							
							
							
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							
							e.printStackTrace();
						}
					}
				}
			}
		}
		return tree;
	}

	@SMDMethod
	public ContentTree getGoogleTreeFiltered(String uuid,String appIdentfier,String _flags,Boolean includeEmpty)
	{
		long flags = 0;
		try {
			flags = Long.parseLong(_flags);
		} catch (Exception e) {
			flags=0;
		}
		
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		ContentTree tree = null;
		try {
			tree = GoogleTreeFactory.createDataSourceTree(appManager,application, flags);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return tree;
	}
	@SMDMethod
	public ContentTree getContentTreeFull(String uuid,String appIdentfier,int flags,Boolean includeEmpty)
	{
		
		
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		ContentTree tree = null;
		try {
			tree = JoomlaContentTreeFactory.createDataSourceTreeFull(appManager,application, true);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return tree;
	}
	@SMDMethod
	public ContentTree getContentJoomlaTreeFiltered(String uuid,String appIdentfier,String _flags,Boolean includeEmpty)
	{


		long flags = 0;
		try {
			flags = Long.parseLong(_flags);
		} catch (Exception e) {
			flags=0;
		}
		/*
		java.util.List<com.liferay.portlet.journal.model.JournalArticle> articles=null;
		
		try {
			articles=JournalArticleLocalServiceUtil.getArticles();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		*/
		//ArrayList arts = LiferayContentTools.getArticlesByUserId(uuid);
		
		//Boolean hasContent = BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaSection));
		//Boolean hasJEvents = BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsCategory));
		//Boolean hasBanners= BitUtils.hasFlag(flags, ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaBannerCategory));
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		
		ContentTree tree = null;
		try {
			tree = JoomlaContentTreeFactory.createDataSourceTree(appManager,application, flags);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return tree;
	}
	
	@SMDMethod
	public ContentTree getContentWordpressTreeFiltered(String uuid,String appIdentfier,String _flags,Boolean includeEmpty)
	{
		
		long flags = 0;
		try {
			flags = Long.parseLong(_flags);
		} catch (Exception e) {
			flags=0;
		}
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		ContentTree tree = null;
		try {
			tree = WordpressTreeFactory.createDataSourceTree(appManager,application, true);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return tree;
	}
	
	@SMDMethod
	public ContentTree getContentLiferayTreeFiltered(String uuid,String appIdentfier,String _flags,Boolean includeEmpty)
	{
		long flags = 0;
		try {
			flags = Long.parseLong(_flags);
		} catch (Exception e) {
			flags=0;
		}
		
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		if(application!=null){
			LiferayContentTools.prepareUser(uuid, appIdentfier);
		}
		ContentTree tree = null;
		try {
			tree = LiferayContentTreeFactory.createDataSourceTree(appManager,application, flags);
		} catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return tree;
	}

	public ContentTree getContentTree(String uuid,String appIdentfier,String currentNode,int flags)
	{
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		ContentTree tree = null;
		try {
			tree = JoomlaContentTreeFactory.createDataSourceTree(appManager,application, flags);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return tree;
	}
	public HttpServletRequest getRequest()
	{
		return ((HttpServletRequest) ActionContext.getContext().get( StrutsStatics.HTTP_REQUEST));
	}
	@SMDMethod
	public ContentTree getDatasources(String uuid,String appIdentfier,Boolean deep)
	{
		if(!SecurityUtil.isValidAppAction(uuid, appIdentfier, getRequest()))
		{
			return null;
		}
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		ContentTree tree = null;
		try {
			tree = JoomlaContentTreeFactory.createDataSourceTreeFull(appManager,application, deep);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return tree;
	}
	@SMDMethod
	public ContentTree getArticlesByDataSource(String uuid,String appIdentfier,String dataSource)
	{
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		ContentTree tree = null;
		if(application!=null)
		{
			if(dataSource!=null)
			{
				try {
					tree = JoomlaContentTreeFactory.createContentTree(appManager,application,dataSource);
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
			}
		}
		return tree;
	}
	@SMDMethod
	public ContentTree getArticles(String uuid,String appIdentfier,String elementId)
	{
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		ContentTree tree = null;
		if(application!=null){
			
			Page element = application.getElementById(Integer.parseInt(elementId));
			if(element!=null)
			{
				ConfigurableInformation contentSourceCI = element.getItemByChainAndName(0,CINames.CONTENT_SOURCE);
				if(contentSourceCI!=null)
				{
					if(contentSourceCI.dataSource!=null)
					{
					
						try {
							ECMContentSourceType  sType = ECMContentSourceTypeTools.FromString(contentSourceCI.value);
							switch (sType) {
								case JoomlaArticle:
								{
									tree = JoomlaContentTreeFactory.createContentTree(appManager,application, contentSourceCI.dataSource);
									break;
								}
							}
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							
							e.printStackTrace();
						}
					}
				}
			}
		}
		return tree;
	}
	@SMDMethod
	public ContentTree getCategories(String uuid,String appIdentfier,String elementId)
	{
		appManager  = getApplicationManager(appIdentfier);
		application= appManager.getApplication(uuid,appIdentfier,"Debug");
		ContentTree tree = null;
		if(application!=null){
			
			Page element = application.getElementById(Integer.parseInt(elementId));
			if(element!=null)
			{
			
				ConfigurableInformation contentSourceCI = element.getItemByChainAndName(0,CINames.CONTENT_SOURCE);
				if(contentSourceCI==null)
				{
					contentSourceCI = element.getItemByChainAndName(0,CINames.BannerSource);
				}
				if(contentSourceCI!=null)
				{
					if(contentSourceCI.dataSource!=null)
					{
					
						try {
							ECMContentSourceType  sType = ECMContentSourceTypeTools.FromString(contentSourceCI.value);
							switch (sType) {
								case JoomlaSection:
								case JoomlaCategory:
								{
									tree = JoomlaContentTreeFactory.createContentTree(appManager,application, contentSourceCI.dataSource);
									break;
								}
								case MosetTreeCategory:
								{
									tree = JoomlaContentTreeFactory.createMostetCategoryTree(appManager,application, contentSourceCI.dataSource);
									break;
								}
								
								case JoomlaBannerCategory:
								{
									tree = JoomlaContentTreeFactory.createJoomlaBannerCategoryTree(appManager,application, contentSourceCI.dataSource);
									break;
								}
							}
							
							
							
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							
							e.printStackTrace();
						}
					}
				}
			}
		}
		return tree;
	}
	public ApplicationManager getApplicationManager(String identifier)
	{
		appManager = ApplicationManager.getInstance();
		if(appManager==null)
		{
			appManager = ApplicationManager.getInstance();
			//getSession().setAttribute("AppManager", appManager);
			//String uuid = UUID.randomUUID().toString();
			//System.out.println("app user id " + uuid);
			
		}
		return appManager;
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

	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		setSession(arg0);
		
	}
}
