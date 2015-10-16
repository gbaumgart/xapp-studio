package cmm.action;

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

import cmm.types.Bundle;
import cmm.types.BundleManager;
import cmm.types.CMMSubscription;
import cmm.types.CMMSubscriptionStatus;
import cmm.types.CMMUser;
import cmm.types.SubscriptionManager;
import cmm.utils.CMMUserTools;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class BundleManagerAction 
{

	public String smd() 
	{
        return Action.SUCCESS;
    }
	
	
	public BundleManager getBundleManager()
	{
		return BundleManager.getInstance();
	}
	
	private static final long serialVersionUID = 1L;
	/***
	 * 
	 * @return ArrayList<CMMSubscription>
	 */
	/*
	@SMDMethod
	public ArrayList<CMMSubscription> getAvaiableBundles(String userId) 
	{
		
		User liferayUser =null;

		try {
			liferayUser = UserLocalServiceUtil.getUserByUuid(userId);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(liferayUser!=null)
		{
			CMMUser user = CMMUserTools.fromLiferayUser(liferayUser); 
			return getBundleManager().getAvaiableBundles(user);
		}
		
		return null;
		
	}
	*/
	
	@SMDMethod
	public ArrayList<Bundle> getBundles(String userId,String appId) 
	{
		User liferayUser =null;
		try {
			liferayUser = UserLocalServiceUtil.getUserByUuid(userId);
		} catch (PortalException e) {
			
		} catch (SystemException e) 
		{
			
		}
		if(liferayUser!=null)
		{
			CMMUser user = CMMUserTools.fromLiferayUser(liferayUser); 
			return getBundleManager().getBundles(user,appId);
		}
		
		return null;
		
	}
	public HttpSession getSession()
	{
		return ((HttpServletRequest) ActionContext.getContext().get( StrutsStatics.HTTP_REQUEST)).getSession();
	}
	
	public HttpServletRequest getRequest()
	{
		return ((HttpServletRequest) ActionContext.getContext().get( StrutsStatics.HTTP_REQUEST));
	}
}
