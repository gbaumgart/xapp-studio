<%@page import="cmx.types.Application"%>
<%@page import="cmx.types.ApplicationManager"%>
<%@page import="com.liferay.portal.kernel.struts.LastPath"%>
<%@page import="pmedia.utils.StringUtils"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="com.liferay.util.portlet.PortletRequestUtil" %>

<%
	//http://mc007ibi.dyndns.org:8080/web/xapp-studio/home?p_p_state=maximized&p_p_mode=view&saveLastPath=0&_58_struts_action=%2Flogin%2Flogin&p_p_id=58&p_p_lifecycle=0&_58_redirect=%2Fweb%2Fxapp-studio%2Fmy-apps
    HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(request);
    //HttpServletRequest httpSession = PortalUtil.getS
    ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
    if(themeDisplay==null)
	{
		HttpSession _session = httpReq.getSession();
		themeDisplay = (ThemeDisplay)_session.getAttribute("themeDisplay");
		Object o= _session.getAttribute("themeDisplay");
		if(themeDisplay!=null)
		{
			//System.out.println("got theme display from session----------");
		}
	}
    
    String _uuid =httpReq.getParameter("uuid");
	String _appId = httpReq.getParameter("appId");
    String _url = httpReq.getParameter("url");
    String _runTimeConfiguration=httpReq.getParameter("runTimeConfiguration");
    
    User user = null;
	if(themeDisplay!=null)
	{
    	user=themeDisplay.getUser();
    	Boolean isSignedIn =false ;
		if(user!=null)
		{
			isSignedIn =themeDisplay.isSignedIn();
    		httpReq.getSession().setAttribute("userSignedIn", ""+isSignedIn);
    		httpReq.getSession().setAttribute("userUUID", ""+user.getUuid());
    		if(_uuid==null){
    		{
    			_uuid=user.getUuid();
    			//_uuid="Guest";
    		}
    		
    		if(_appId==null){
    		{
    			String appIdSession=(String)request.getSession().getAttribute("appId");
    			if(appIdSession!=null)
    			{
    				_appId = appIdSession;
    			}
    		}
    		
    		ApplicationManager _appManager = ApplicationManager.getInstance();
		    Application _app = _appManager.getApplication(_uuid,_appId, false);
		    if(_app==null)
		    {
	    		_app = _appManager.getApplication(_uuid, _appId + request.getSession().getId(), false);
	    		if(_app==null)
	    		{
	    			/*ArrayList<Application>userApps= _appManager.getUserApplications(_uuid);
	    			if(userApps.size()==1)
	    			{
	    				Application firstApp = userApps.get(0);
	    				_appId=firstApp.getApplicationIdentifier();
		    			session.setAttribute("appId", _appId);
	    				
	    			}else if(userApps.size()==0)
	    			{
		    			
	    			}
	    			*/
	    			
	    			
	    		}else
	    		{
	    				_appId=_app.getApplicationIdentifier();
	    				session.setAttribute("appId", _appId);
	    		}
		    }
    	}
    	
    	}
   }
   }
    	
    
    //System.out.println("user signed in : " + user.getUuid());
    //request.getSession().setAttribute("userSignedIn", isSignedIn);
	
    
    
    if(themeDisplay==null)
    {
    	
    	//String redirectUrl = StringUtils.encode(httpReq.getRequestURI() +"?appId="+_appId + "&uuid="+_uuid);
    	String redirectUrl = StringUtils.encode("/XApp-portlet/xas.jsp" +"?appId="+_appId + "&uuid="+_uuid);
    	String newUrl = "/home?p_p_state=maximized&p_p_mode=view&saveLastPath=0&_58_struts_action=%2Flogin%2Flogin&p_p_id=58&p_p_lifecycle=0&_58_redirect="+redirectUrl;
    	//System.out.println("c url " + newUrl);
    	//LastPath lastPath = new LastPath(newUrl,null); 
    	//request.getSession().setAttribute( WebKeys.LAST_PATH, lastPath );
    	//response.sendRedirect(newUrl);
    	
    	
    	//http://mc007ibi.dyndns.org:8080/web/xapp-studio/home?p_p_state=maximized&p_p_mode=view&saveLastPath=0&_58_struts_action=%2Flogin%2Flogin&p_p_id=58&p_p_lifecycle=0&_58_redirect=%2Fweb%2Fxapp-studio%2Fmy-apps
    	//%2Fweb%2Fxapp-studio%2Fmy-apps
    	//%2Fweb%2Fxapp-studio%2Fapp%3FappId%3Dwebapp%26uuid%3D11166763-e89c-44ba-aba7-4e9f4fdf97a9
    	//%2FXApp-portlet%2Fxas.jsp%3FappId%3Dwebapp%26uuid%3D11166763-e89c-44ba-aba7-4e9f4fdf97a9
    	//%2FXApp-portlet%2Fxas.jsp%3FappId%3Dwebapp%26uuid%3D11166763-e89c-44ba-aba7-4e9f4fdf97a9
    }
    
    if(_appId!=null && _appId.contains("/"))
    {
    	_url=_appId.substring(_appId.indexOf("/"),_appId.length());
    	_appId=_appId.substring(0,_appId.indexOf("/"));    
    	
    }
    if(_appId!=null){
    	request.getSession().setAttribute("appId", _appId);
    }
    if(_url!=null){
    	request.getSession().setAttribute("url", _appId);
    }
    
    if(_uuid!=null){
    	request.getSession().setAttribute("uuid", _uuid);
    }
    
    if(_runTimeConfiguration!=null){
    	request.getSession().setAttribute("runTimeConfiguration", _runTimeConfiguration);
    }	     
	
%>
