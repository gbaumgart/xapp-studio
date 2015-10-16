

package cmx.action;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.json.annotations.SMDMethod;

import cmx.tools.ApplicationTreeFactory;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.CMError;
import cmx.types.ContentTree;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class ApplicationTreeAction
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
	
	@SMDMethod
	public ArrayList<Application> getApplications(String uuid,String _flags)
	{
		long flags = 0;
		try {
			flags = Long.parseLong(_flags);
		} catch (Exception e) {
			flags=0;
		}
		//tree = ApplicationTreeFactory.createApplicationTree(getAppManager(), flags);
		ApplicationManager appManager = ApplicationManager.getInstance();
		
		ArrayList<Application>result = appManager.getUserApplications(uuid);
		return result;
	}
	
	@SMDMethod
	public ContentTree getApplicationsAsTree(String uuid,String _flags)
	{
		long flags = 0;
		try {
			flags = Long.parseLong(_flags);
		} catch (Exception e) {
			flags=0;
		}
		ContentTree tree = ApplicationTreeFactory.createApplicationTree(uuid,ApplicationManager.getInstance(), flags);
		return tree;
	}
	
	

	public ApplicationManager getApplicationManager(String identifier)
	{
		return  ApplicationManager.getInstance();
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
