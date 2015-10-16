package cmx.action;
import com.liferay.portal.kernel.events.Action;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
public class CMXLoginPre extends Action{
	 
		public void run(HttpServletRequest req, HttpServletResponse res) 
		{
	        //System.out.println("## My custom login pre action" + req.getSession().getId());
	    }
}
