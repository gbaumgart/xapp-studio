package cmx.action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pmedia.Servlets.SessionCreateAction;

import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AutoLogin;
import com.liferay.portal.security.auth.AutoLoginException;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;


public class CMXRPXAutoLogin implements AutoLogin 
{
	public static final String FACEBOOK_USER_ID = "FACEBOOK_USER_ID";
	public static final String FACEBOOK_USER_EMAIL_ADDRESS = "rpxCode";
	
	public String[] login(
			HttpServletRequest request, HttpServletResponse response) {

			String[] credentials = null;
			

			try {
				long companyId = PortalUtil.getCompanyId(request);

				HttpSession session = request.getSession();

				//Object a  = session.getAttribute("rpxCode");
				String emailAddress = (String) session.getAttribute("rpxCode");

				//System.out.println("sss - auto login servlet: " + request.getSession().getId() + " email addr " + emailAddress);
				User user = null;

				if (Validator.isNotNull(emailAddress)) 
				{
					//session.removeAttribute(FACEBOOK_USER_EMAIL_ADDRESS);

					try {
						user = UserLocalServiceUtil.getUserByEmailAddress(
							companyId, emailAddress);
					}
					catch (NoSuchUserException nsue) {
					}
				}
				else {
					long facebookId = GetterUtil.getLong(
						(String)session.getAttribute(FACEBOOK_USER_ID));

					if (facebookId > 0) {
						try {
							user = UserLocalServiceUtil.getUserByFacebookId(
								companyId, facebookId);
						}
						catch (NoSuchUserException nsue) {
							return credentials;
						}
					}
					else {
						return credentials;
					}
				}
				HttpSession s = SessionCreateAction.find(session.getId());
				
				if(session!=null){
					//System.out.println("sss2 - auto login servlet: " + s.getId());
					if(user!=null)
					{
						session.setAttribute("userUUID", ""+user.getUuid());
						session.setAttribute("userSignedIn",true);
					}
				}
				
				ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
				if(themeDisplay!=null)
				{
					//System.out.println("sss2 - auto login servlet: have theme ");
				}
				credentials = new String[3];

				credentials[0] = String.valueOf(user.getUserId());
				credentials[1] = user.getPassword();
				credentials[2] = Boolean.FALSE.toString();
			}
			catch (Exception e) {
				_log.error(e, e);
			}

			return credentials;
		}

		private static Log _log = LogFactoryUtil.getLog(CMXRPXAutoLogin.class);

		@Override
		public String[] handleException(HttpServletRequest arg0,
				HttpServletResponse arg1, Exception arg2)
				throws AutoLoginException {
			// TODO Auto-generated method stub
			return null;
		}
}
