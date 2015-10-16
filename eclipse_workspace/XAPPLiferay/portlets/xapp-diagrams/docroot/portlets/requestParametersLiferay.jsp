<%@page import="org.b3mn.poem.Identity"%>
<%@page import="com.liferay.portal.kernel.struts.LastPath"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="com.liferay.util.portlet.PortletRequestUtil" %>

<%
    HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(request);
    ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
    if(themeDisplay==null)
	{
		HttpSession _session = httpReq.getSession();
		themeDisplay = (ThemeDisplay)_session.getAttribute("themeDisplay");
		Object o= _session.getAttribute("themeDisplay");
		if(themeDisplay!=null)
		{
			System.out.println("got theme display from session----------");
		}
	}
    
    String _runTimeConfiguration=httpReq.getParameter("runTimeConfiguration");
    User user = null;
    Boolean isSignedIn =false ;
    String userEMail = null;
    if(themeDisplay!=null)
	{
    	user=themeDisplay.getUser();
    	
		if(user!=null)
		{
		    userEMail=user.getEmailAddress();
			isSignedIn =themeDisplay.isSignedIn();
    		httpReq.getSession().setAttribute("userSignedIn", ""+isSignedIn);
    		httpReq.getSession().setAttribute("userUUID", ""+user.getUuid());
    		String pass =user.getPassword();
    		session.setAttribute("pwd", user.getPassword());
    	}
   }
   
   if(themeDisplay!=null && isSignedIn && user!=null)
   {
   
   		//Identity subject = Identity.ensureSubject(userEMail);
   		org.b3mn.poem.business.User ouser = null;
		//ouser = new org.b3mn.poem.business.User(subject);
		Identity subject2 = null ; 
		try{
			subject2 = Identity.instance(userEMail);
	    }catch(Exception e){
	    	
	    }
	    
	    String userUniqueId = userEMail;

		// identify user
		Identity subj = Identity.instance(userUniqueId);
	
		// create new user, if the user does not exist
		if (subj == null) {
			// Create new user with open id attributes
			org.b3mn.poem.business.User.CreateNewUser(userUniqueId);
		}
		
		//get user
		org.b3mn.poem.business.User user2 = new org.b3mn.poem.business.User(userUniqueId);
		user2.login(request, response);
	    
	    
		if(ouser!=null && subject2!=null)
		{
			//System.out.println("have no ouser");
		}
		
		if(user2!=null)
		{
				
			UUID authToken = UUID.randomUUID();
     		user2.addAuthenticationAttributes(pageContext.getServletContext(), httpReq, response, authToken);
			System.out.println("have ouser");
		}
		   		   		   	   		
   }
    if(themeDisplay==null)
    {
    /*
    	String redirectUrl = StringUtils.encode("/XApp-portlet/xas.jsp" +"?appId="+_appId + "&uuid="+_uuid);
    	String newUrl = "/home?p_p_state=maximized&p_p_mode=view&saveLastPath=0&_58_struts_action=%2Flogin%2Flogin&p_p_id=58&p_p_lifecycle=0&_58_redirect="+redirectUrl;
    	System.out.println("c url " + newUrl);
    	*/
   }
    
    /*
    if(_uuid!=null){
    	request.getSession().setAttribute("uuid", _uuid);
    }
    
    if(_runTimeConfiguration!=null){
    	request.getSession().setAttribute("runTimeConfiguration", _runTimeConfiguration);
    }	
    */     
	
%>
