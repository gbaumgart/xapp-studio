package cmb.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.DefaultActionSupport;
import org.apache.struts2.json.annotations.SMDMethod;

import pmedia.utils.SecurityUtil;
import cmb.types.BuildManager;
import cmb.types.BuildOptions;
import cmb.types.CMBApplicationBuild;
import cmm.types.CMMUser;
import cmm.utils.CMMUserTools;
import cmx.tools.TrackingUtils;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;


public class BuildManagerAction extends DefaultActionSupport 
{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String smd() 
	{
        return Action.SUCCESS;
    }
	
	public BuildManager getBuildManager()
	{
		return BuildManager.getInstance();
	}
	
	@SMDMethod
	public BuildOptions getBuidOptions(String uuid,String applicationId,String platform,String type)
	{
		return getBuildManager().getBuidOptions(uuid, applicationId, platform, type);
	}
	
	@SMDMethod
	public String addToBuildQueue(String uuid,String applicationId,String platform,String type,String options)
	{
		if(!SecurityUtil.isValidUser(uuid, getRequest()))
		{
			return "No such user";
		}
		Boolean allowHybridDebug = true;
		if(System.getProperty("allowHybridDebugBuilds")!=null){
			if(System.getProperty("allowHybridDebugBuilds").equals("false")){
				allowHybridDebug=false;
			}
		}
		
		if(!allowHybridDebug){
			type="release";
			if(options!=null){
				options = options.replace("debug", "release");
			}
		}
		
		System.out.println("create  build " + uuid   + " and : " + applicationId);
		TrackingUtils.sendMail("build application", "" + platform, uuid,applicationId);
		return getBuildManager().addToBuildQueue(uuid, applicationId,platform,type,options);
	}
	@SMDMethod
	public ArrayList<CMBApplicationBuild> getApplicationBuilds(String userId,String applicationId,String platform) 
	{
		if(!SecurityUtil.isValidUser(userId, getRequest()))
		{
			return null;
		}
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
			return getBuildManager().getApplicationBuilds(userId, applicationId, platform);
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
