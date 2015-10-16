package pmedia.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;

import cmx.tools.LiferayContentTools;
import cmx.types.ApplicationManager;

public class SecurityUtil 
{
	public static boolean isValidUser(String uuid,HttpServletRequest request)
	{
		Boolean result=true;
		Boolean userCheck = Boolean.parseBoolean(System.getProperty("checkUser"));
		if(userCheck)
		{
			User user = LiferayContentTools.isValidUser(uuid, request);
			if(user==null){
				return false;
			}else{
				return true;
			}
		}
		

		return result;
	}
	public static boolean isValidAdminAction(String uuid,String appId,HttpServletRequest request)
	{
		Boolean isValidUser = isValidUser(uuid, request);
		
		User user = LiferayContentTools.isValidUser(uuid, request);

		if(user!=null){
			//check admin roles
			List<Role> userRoles = null;
			try {
				userRoles = user.getRoles();
			} catch (SystemException e) {
			}
			for(Role r : userRoles){
				
				if("Administrator".equalsIgnoreCase(r.getName())){
					return true;
				}
			}
		}
		
		if(!System.getProperty("adminUser").equals(uuid)){
			System.out.println("## sec : not a valid admin action " + "|| uuid=" + uuid + " || "  + request.getRequestURI());
			return false;
		}
		if(isValidUser){
			if(ApplicationManager.getInstance().getApplication(uuid, appId, false)==null){
					System.out.println("## sec : not a valid admin action, app = null " + "|| uuid=" + uuid + " || "  + request.getRequestURI());
				return false;
			}
		}
		
		return isValidUser;
	}
	public static boolean isValidAppAction(String uuid,String appId,HttpServletRequest request)
	{
		/*
		if(isValidAdminAction(uuid, appId, request)){
			return true;
		}*/
		
		Boolean isValidUser = isValidUser(uuid, request);
		if(isValidUser){
			if(ApplicationManager.getInstance().getApplication(uuid, appId, false)==null){
				isValidUser= false;
			}
		}
		if(!isValidUser){
			System.out.println("## sec : not a valid app action " + "|| uuid=" + uuid + " || "  + request.getRequestURI());
			request.getSession().invalidate();
		}
		return isValidUser;
	}
}
