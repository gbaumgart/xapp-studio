package com.googlecode.janrain4j.samples.servlets.basic;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pmedia.Servlets.SessionCreateAction;

import cmx.tools.TrackingUtils;

import com.googlecode.janrain4j.api.engage.EngageFailureException;
import com.googlecode.janrain4j.api.engage.EngageService;
import com.googlecode.janrain4j.api.engage.EngageServiceFactory;
import com.googlecode.janrain4j.api.engage.ErrorResponeException;
import com.googlecode.janrain4j.api.engage.response.UserDataResponse;
import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

@SuppressWarnings("serial")
public class TokenServlet extends HttpServlet {

	public static final String FACEBOOK_USER_ID = "FACEBOOK_USER_ID";
	public static final String FACEBOOK_USER_EMAIL_ADDRESS = "rpxCode";
	
	
	
	
	protected void addUser(
			HttpSession session, long companyId, UserDataResponse jsonObject)
		throws Exception {

		long creatorUserId = 0;
		boolean autoPassword = true;
		String password1 = StringPool.BLANK;
		String password2 = StringPool.BLANK;
		boolean autoScreenName = true;
		String screenName = StringPool.BLANK;
		String emailAddress = jsonObject.getProfile().getEmail();
		//long facebookId = jsonObject.getLong("id");
		String openId = StringPool.BLANK;
		Locale locale = LocaleUtil.getDefault();
		String firstName = jsonObject.getProfile().getName().getGivenName();
		String middleName = jsonObject.getProfile().getName().getMiddleName();
		String lastName = jsonObject.getProfile().getName().getFamilyName();
		
		
		
		
		long primKey =  0;
		
		try {
			primKey = Long.parseLong(jsonObject.getProfile().getPrimaryKey());	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		int prefixId = 0;
		int suffixId = 0;
		String male = jsonObject.getProfile().getGender();
		int birthdayMonth = Calendar.JANUARY;
		int birthdayDay = 1;
		int birthdayYear = 1970;
		String jobTitle = StringPool.BLANK;
		long[] groupIds = null;
		long[] organizationIds = null;
		long[] roleIds = null;
		long[] userGroupIds = null;
		boolean sendEmail = true;

		ServiceContext serviceContext = new ServiceContext();

		User user = UserLocalServiceUtil.addUser(
			creatorUserId, companyId, autoPassword, password1, password2,
			autoScreenName, screenName, emailAddress, primKey, openId,
			locale, firstName, middleName, lastName, prefixId, suffixId, true,
			birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds,
			organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);

		UserLocalServiceUtil.updateLastLogin(
			user.getUserId(), user.getLoginIP());

		UserLocalServiceUtil.updatePasswordReset(user.getUserId(), false);

		UserLocalServiceUtil.updateEmailAddressVerified(user.getUserId(), true);

		session.setAttribute(FACEBOOK_USER_EMAIL_ADDRESS, emailAddress);
		
		
		TrackingUtils.sendMail("New User", user.getScreenName() , user.getUserUuid(),null);
		
		
	}

	protected void setFacebookCredentials(
			HttpSession session,HttpServletRequest req, HttpServletResponse resp, long companyId, UserDataResponse userDataResponse)
		throws Exception {

		/*
		JSONObject jsonObject = FacebookConnectUtil.getGraphResources(
			companyId, "/me", token,
			"id,email,first_name,last_name,gender");

		if ((jsonObject == null) ||
			(jsonObject.getJSONObject("error") != null)) {

			return;
		}
		*/

		/*
		if (FacebookConnectUtil.isVerifiedAccountRequired(companyId) &&
			!jsonObject.getBoolean("verified")) {

			return;
		}
		*/

		User user = null;

	//	long facebookId = jsonObject.getLong("id");

		long primKey =  0;
		try {
			primKey = Long.parseLong(userDataResponse.getProfile().getPrimaryKey());	
		} catch (Exception e) {
			// TODO: handle exception
		}

		
		if (primKey > 0) {
			session.setAttribute(
				FACEBOOK_USER_ID, String.valueOf(primKey));

			try {
				user = UserLocalServiceUtil.getUserByFacebookId(
					companyId, primKey);
			}
			catch (NoSuchUserException nsue) {
			}
		}
		

		String emailAddress = userDataResponse.getProfile().getEmail();

		if ((user == null) && Validator.isNotNull(emailAddress)) 
		{
			session.setAttribute("rpxCode", emailAddress);
						try {
				user = UserLocalServiceUtil.getUserByEmailAddress(
					companyId, emailAddress);
			}
			catch (NoSuchUserException nsue) {
			}
		}

		if (user != null) {
			updateUser(user, userDataResponse);
		}
		else {
			addUser(session, companyId, userDataResponse);
		}
		//org.apache.catalina.session.StandardSessionFacade@55ac84ed
		System.out.println("sss - token servlet: " + session.getId() + " email addr " + emailAddress);
		if(user!=null){
			//session.setAttribute(WebKeys.COMPANY_, companyId);
			
			session.setAttribute(WebKeys.USER_ID, user.getUserId() );
			session.setAttribute(WebKeys.USER_PASSWORD, user.getPassword());
			//session.setAttribute(FACEBOOK_USER_EMAIL_ADDRESS, emailAddress);
			session.setAttribute("rpxCode", emailAddress);
			String sessionId= req.getParameter("sessionId");
			if(sessionId!=null){
				HttpSession _s = SessionCreateAction.find(sessionId);
				if(_s!=null){
					_s.setAttribute("rpxCode", emailAddress);
					System.out.println("found session by id");
				}
			}
		}
		
	}

	protected void updateUser(User user, UserDataResponse userDataResponse)
		throws Exception {

		String emailAddress = userDataResponse.getProfile().getEmail();
		String firstName = userDataResponse.getProfile().getName().getGivenName();
		String lastName = userDataResponse.getProfile().getName().getFamilyName();
		//boolean male = Validator.equals(jsonObject.getString("gender"), "male");

		if (emailAddress.equals(user.getEmailAddress()) &&
			firstName.equals(user.getFirstName()) &&
			lastName.equals(user.getLastName())) {

			return;
		}

		Contact contact = user.getContact();

		Calendar birthdayCal = CalendarFactoryUtil.getCalendar();

		birthdayCal.setTime(contact.getBirthday());

		int birthdayMonth = birthdayCal.get(Calendar.MONTH);
		int birthdayDay = birthdayCal.get(Calendar.DAY_OF_MONTH);
		int birthdayYear = birthdayCal.get(Calendar.YEAR);

		long[] groupIds = null;
		long[] organizationIds = null;
		long[] roleIds = null;
		List<UserGroupRole> userGroupRoles = null;
		long[] userGroupIds = null;
		
		long primKey =  0;
		
		try {
			primKey = Long.parseLong(userDataResponse.getProfile().getPrimaryKey());	
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		ServiceContext serviceContext = new ServiceContext();

		if (!emailAddress.equalsIgnoreCase(user.getEmailAddress())) {
			UserLocalServiceUtil.updateEmailAddress(
				user.getUserId(), StringPool.BLANK, emailAddress, emailAddress);
		}

		UserLocalServiceUtil.updateEmailAddressVerified(user.getUserId(), true);

		UserLocalServiceUtil.updateUser(
			user.getUserId(), StringPool.BLANK, StringPool.BLANK,
			StringPool.BLANK, false, user.getReminderQueryQuestion(),
			user.getReminderQueryAnswer(), user.getScreenName(), emailAddress,
			primKey, user.getOpenId(), user.getLanguageId(),
			user.getTimeZoneId(), user.getGreeting(), user.getComments(),
			firstName, user.getMiddleName(), lastName, contact.getPrefixId(),
			contact.getSuffixId(), true, birthdayMonth, birthdayDay,
			birthdayYear, contact.getSmsSn(), contact.getAimSn(),
			contact.getFacebookSn(), contact.getIcqSn(), contact.getJabberSn(),
			contact.getMsnSn(), contact.getMySpaceSn(), contact.getSkypeSn(),
			contact.getTwitterSn(), contact.getYmSn(), contact.getJobTitle(),
			groupIds, organizationIds, roleIds, userGroupRoles, userGroupIds,
			serviceContext);
	}
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        
        String token = req.getParameter("token");
        
        String sessionId= req.getParameter("sessionId");
        
        String addr = req.getRemoteAddr(); // 123.123.123.123

        // Get client's hostname
        String host = req.getRemoteHost(); // hostname.com
        
        System.out.println("token :: " + token + " ip :: " + addr + " domain :: " + host + " :: sessionId " + sessionId);
        
        EngageService engageService = EngageServiceFactory.getEngageService();
        
        try {
            UserDataResponse userDataResponse = engageService.authInfo(token, true);
            HttpSession session = req.getSession();
            session.setAttribute("userData", userDataResponse);
            
            
            //resp.sendRedirect("/portlets/user_data.jsp");
            try {
				setFacebookCredentials(session,req,resp,Long.parseLong(System.getProperty("companyId")) , userDataResponse);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            String redirect = ParamUtil.getString(req, "redirect");
            if(redirect!=null){
            	resp.sendRedirect("/web/xapp-studio/login");
            }
            
        }
        catch (EngageFailureException e) {
            req.setAttribute("error", e);
            getServletContext().getRequestDispatcher("/error.jsp").forward(req, resp);
        }
        catch (ErrorResponeException e) {
            req.setAttribute("error", e);
            getServletContext().getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
