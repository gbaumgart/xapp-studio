package cmx.action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pmedia.Servlets.SessionCreateAction;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
public class CMXLoginPost extends Action{
	 
		public void run(HttpServletRequest req, HttpServletResponse res) 
		{
	        //System.out.println("## My custom login action" + req.getSession().getId());
	        //isSignedIn =themeDisplay.isSignedIn();
    		//httpReq.getSession().setAttribute("userUUID", ""+user.getUuid());
	        req.getSession().setAttribute("userSignedIn", true);
	        HttpSession _s = SessionCreateAction.find(req.getSession().getId());
			if(_s!=null)
			{
				_s.setAttribute("userSignedIn", true);
				ThemeDisplay themeDisplay = (ThemeDisplay)req.getAttribute(WebKeys.THEME_DISPLAY);
				//System.out.println("## My custom : have theme ");
			}
	        
	        HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(req);
	        ThemeDisplay themeDisplay = (ThemeDisplay)httpReq.getAttribute(WebKeys.THEME_DISPLAY);
	        if(themeDisplay!=null)
	    	{
	        	//System.out.println("## My custom : have theme " + themeDisplay.isSignedIn());
	        	req.getSession().setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);
	    	}
	        
	        /*
	        try {
				res.sendRedirect("/web/xapp-studio/home");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
	    }
}
