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


public class SubscriptionManagerAction 
{

	public String smd() 
	{
        return Action.SUCCESS;
    }
	
	
	public SubscriptionManager getSubscriptionManager()
	{
		return SubscriptionManager.getInstance();
	}
	
	private static final long serialVersionUID = 1L;

	@SMDMethod
	public String publish(String uuid,String appID , String referenceNumber)
	{
		return getSubscriptionManager().publish(uuid, appID, referenceNumber);
	}
	@SMDMethod
	public void addToPurchaseQueue(String uuid,String appId, String referenceNumber)
	{
		getSubscriptionManager().addToPurchaseQueue(uuid,appId,referenceNumber);
	}
	@SMDMethod
	public CMMSubscriptionStatus getSubscriptionStatus(String uuid,String appId,String referenceNumber)
	{
		return getSubscriptionManager().getSubscriptionStatus(uuid, appId, referenceNumber);
	}
	/***
	 * 
	 * @return ArrayList<CMMSubscription>
	 */
	@SMDMethod
	public ArrayList<CMMSubscription> getAvaiableSubscriptions(String userId) 
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
			return getSubscriptionManager().getAvaiableSubscriptions(user);
		}
		
		return null;
		
	}
	
	@SMDMethod
	public ArrayList<CMMSubscription> getAvaiableAppSubscriptions(String userId,String appId) 
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
			return getSubscriptionManager().getAvaiableAppSubscriptions(user,appId);
		}
		
		return null;
		
	}
	
	@SMDMethod
	public ArrayList<CMMSubscription> getAddons(String userId,String appId) 
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
			return getSubscriptionManager().getAddons(user,appId);
		}
		
		return null;
		
	}
	

	@SMDMethod
	public String createApp(String _uuid,String  _name)
	{
		/*
		if(uuid !=null && name !=null)
		{
			/*application = h.getApplication(_uuid,_name,false);
			System.out.println("Creating Application");
			if(application==null){
				appManager.getApplication(_uuid,_name, true);
			}
			
		}
		*/
		return ActionSupport.SUCCESS;
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
