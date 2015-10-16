package cmx.action;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.DefaultActionSupport;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.json.annotations.SMDMethod;

import pmedia.Servlets.HttpSessionCollector;
import pmedia.Servlets.SessionCreateAction;
import pmedia.types.ApplicationMetaData;
import pmedia.types.ApplicationMetaDataKeys;
import pmedia.utils.CITools;
import pmedia.utils.SecurityUtil;
import pmedia.utils.StringUtils;

import cmm.types.CMMUser;
import cmm.utils.LiferayTools;
import cmx.tools.LiferayContentTools;
import cmx.tools.TrackingUtils;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ConfigurableInformation;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @Struts 2 Portlet action controller
 *
 * @author Jeff Robinson
 *
 */
public class ApplicationManagerAction extends DefaultActionSupport implements ServletResponseAware,SessionAware
{
    /**
	 * 
	 */
	
	private String applicationIdentifier;
	private ApplicationManager appManager=null;
	public Application application=null;
	
	public String smd() 
	{
        return Action.SUCCESS;
    }
	
	public ApplicationManager getApplicationManager()
	{
		return ApplicationManager.getInstance();
	}
	
	private static final long serialVersionUID = 1L;
	String name = null;
	String uuid = null;
	String action = null;

	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@SMDMethod
	public String deleteApp(String _uuid,String _appId)
	{
		if(!SecurityUtil.isValidAppAction(_uuid, _appId, getRequest()))
		{
			return "No such user";
		}
		
		if(_uuid !=null && _appId!=null)
		{
			application = getApplicationManager().getApplication(_uuid, _appId,false);
			System.out.println("Deleting Application");
			if(application!=null){
				getApplicationManager().deleteApplication(_uuid,_appId);
			}
		}
		return ActionSupport.SUCCESS;
	}
	@SMDMethod
	public ArrayList<Application> getApplications(String _uuid,String platform)
	{
		if(!SecurityUtil.isValidUser(_uuid, getRequest())){
			System.out.println("getApplications :: is not a valid user : " +_uuid);
			return null;
		}else{
			
		}
		return ApplicationManager.getInstance().getUserApplications(_uuid);
	}
	

	 
	
	@SMDMethod
	public CMMUser ready(String sessionIn)
	{
		if(sessionIn==null){
			return null;
		}
		System.out.println("ins ready : " +sessionIn);
		HttpSession _s = SessionCreateAction.find(sessionIn);
		if(_s==null)
		{			
			_s = HttpSessionCollector.find(sessionIn);
		}
		if(_s!=null)
		{
			
			Boolean isSignedIn =false;
			if(_s.getAttribute("userSignedIn") !=null)
			{
				try {
					isSignedIn = (Boolean) _s.getAttribute("userSignedIn");	
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			String userUUID = (String) _s.getAttribute("userUUID");
			if(isSignedIn && userUUID!=null)
			{
				System.out.println("xxxxx  ready : is signed in");
			}
			
			
			
			
			//ThemeDisplay themeDisplay = (ThemeDisplay)_s.getAttribute(WebKeys.THEME_DISPLAY);
			ThemeDisplay themeDisplay = (ThemeDisplay)_s.getAttribute("themeDisplay");
		    if(themeDisplay!=null)
			{
				/*HttpSession _session = httpReq.getSession();
				themeDisplay = (ThemeDisplay)_session.getAttribute("themeDisplay");
				Object o= _session.getAttribute("themeDisplay");
				*/
				if(themeDisplay!=null)
				{
					System.out.println("ready : got session ----------xxxxx");
					if(themeDisplay.isSignedIn())
					{
						CMMUser user = new CMMUser();
						user.setUuid( themeDisplay.getUser().getUuid());
						return user;
						
					}
				}
			}
		}
		return null;
	}
	
	@SMDMethod
	public Application createWizardApplication(String uuid, String title , String appTemplate,String visualTemplate,String iconSet,String platform)
	{
		//public Application createWizardApplication(String uuid,String title, String appTemplate,String visualTemplate,String iconSet,String sessionId,String platform)
		return ApplicationManager.getInstance().createWizardApplication(uuid, title, appTemplate,visualTemplate,iconSet, getSession().getId(),platform);
	}
	@SMDMethod
	public Application createGuestApplication(String uuid, String title , String appTemplate, String platform)
	{
		ApplicationManager.getInstance().createTemplateApplication(uuid, title, appTemplate, getSession().getId(),platform);
		return null;
	}
	@SMDMethod
	public String createApp(String _uuid,String  _name, String platform)
	{

		if(!SecurityUtil.isValidUser(_uuid, getRequest())){
			return "No such user";
		}
		if(_uuid !=null && _name !=null)
		{
			String title=""+_name;
			_name=_name.toLowerCase();
			_name=StringUtils.trim(_name, true, false);
			application = getApplicationManager().getApplication(_uuid,_name,false);
			System.out.println("Creating Application : " + title + " type : " + platform);
			if(application==null)
			{
				application = getApplicationManager().getApplication(_uuid,_name, true);
				TrackingUtils.sendMail("Create Application", _name, _uuid,application.getApplicationIdentifier());
				if(application!=null)
				{
					ApplicationMetaData appMeta=application.getMetaData();
					if(appMeta!=null)
					{
						ConfigurableInformation appTitle = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_TITLE);
						if(appTitle!=null)
						{
							appTitle.setValue(title);
						}
					}
					application.setPlatform(platform);
					getApplicationManager().saveApplication(application);
				}
			}
		}
		if(application==null)
		{
			return "Failed";
		}
		return null;
	}
	
	public void validate() {
      if ((name==null) || (name.length() == 0))
        addFieldError("name", "Name is required!");
	  }
		
    public void setName(String name){		    
      this.name = name;
    }

    public String getName(){		    
      return name;
    }
    
    public HttpSession getSession()
	{
		return ((HttpServletRequest) ActionContext.getContext().get( StrutsStatics.HTTP_REQUEST)).getSession();
	}
	
	public HttpServletRequest getRequest()
	{
		return ((HttpServletRequest) ActionContext.getContext().get( StrutsStatics.HTTP_REQUEST));
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		
	}

}
