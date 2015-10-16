package cmx.action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.CMError;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class CMBaseAction extends ActionSupport implements ServletRequestAware,ServletResponseAware
{
	private static final long serialVersionUID = 1L;

	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	private CMError error=new CMError();
	
	private String uuid;
	private String lang;
	private String type;
	
	public String applicationIdentifier;
	public ApplicationManager appManager=null;
	public Application application=null;
	
	
	public void getCMObjects(String uuid,String identifier)
	{
		appManager = getApplicationManager();
		application = getApplication(uuid,identifier);
	}
	
	public ApplicationManager getApplicationManager()
	{
		appManager = ApplicationManager.getInstance();
		return appManager;
	}

	public Application getApplication(String uuid,String identifier)
	{
		appManager  = getApplicationManager();
		application= appManager.getApplication(uuid,identifier,false);
		return application;
		
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



	public HttpSession getSession()
	{
		return ((HttpServletRequest) ActionContext.getContext().get( StrutsStatics.HTTP_REQUEST)).getSession();
	}
	
	public HttpServletRequest getRequest()
	{
		return ((HttpServletRequest) ActionContext.getContext().get( StrutsStatics.HTTP_REQUEST));
	}
	public HttpServletResponse getResponse()
	{
		return ((HttpServletResponse) ActionContext.getContext().get( StrutsStatics.HTTP_RESPONSE));
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

	
}
