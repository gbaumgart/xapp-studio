package cmx.action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pmedia.Servlets.SessionCreateAction;
import cmx.types.ScriptManager;

import com.liferay.portal.kernel.events.Action;
public class CMXLogoutPost extends Action{
	 
		public void run(HttpServletRequest req, HttpServletResponse res) 
		{
	        //System.out.println("## My custom logout action" + req.getSession().getId());
	        //isSignedIn =themeDisplay.isSignedIn();
    		//httpReq.getSession().setAttribute("userUUID", ""+user.getUuid());
	        req.getSession().setAttribute("userSignedIn", false);
	        
	        HttpSession _s = SessionCreateAction.find(req.getSession().getId());
			if(_s!=null)
			{
				String uuid = (String)_s.getAttribute("uuid");
				if(uuid==null){
					uuid = (String)req.getSession().getAttribute("uuid"); 
				}
				_s.setAttribute("userSignedIn", false);
				ScriptManager.onLogout(_s.getId());
			}
	        
	    }
}
