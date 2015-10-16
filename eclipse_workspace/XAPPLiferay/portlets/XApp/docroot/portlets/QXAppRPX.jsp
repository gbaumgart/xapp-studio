<%@page import="pmedia.Servlets.SessionCreateAction"%>
<%@page import="java.util.Locale"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@page import="com.liferay.portal.kernel.util.HttpUtil"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.kernel.facebook.FacebookConnectUtil"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="janrain" uri="/WEB-INF/tld/janrain4j.tld" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />
<%

	boolean showFacebookConnectIcon=true;
	
	ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
	HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(request);
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
    		httpReq.getSession().setAttribute("themeDisplay",themeDisplay);
    		
    		HttpSession _session = SessionCreateAction.find(session.getId());
    		if(_session!=null){
	    		_session.setAttribute("userSignedIn", ""+isSignedIn);
	    		_session.setAttribute("userUUID", ""+user.getUuid());
	    		_session.setAttribute("themeDisplay",themeDisplay);
	    		
	    		//System.out.println("qxapprpx " + _session.getId());
    		}
    		
    	}
    }
    String dstLang = "en";
	Locale rLocale = request.getLocale();
	if(rLocale!=null)
	{
		dstLang = rLocale.getLanguage();
	}
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<style type="text/css">
	.janrainContent{
			padding-bottom: 15px !important;
			padding-top: 15px !important;
			padding-left: 20px !important;
		}
		/*
		.janrainEngageEmbed DIV
		{
			padding-bottom: 15px !important;
			padding-top: 15px !important;
			padding-left: 20px !important;
		}
		*/
</style>
<script type="text/javascript">

(function() {
    if (typeof window.janrain !== 'object') window.janrain = {};
    if (typeof window.janrain.settings !== 'object') window.janrain.settings = {};
    
    janrain.settings.tokenUrl = '<%=System.getProperty("janrain.tokenUrl")+"?sessionId="+session.getId()%>';
    janrain.settings.language = '<%=dstLang%>';
    janrain.settings.redirectTo = '/web/xapp-studio/home';
    
    function isReady() { janrain.ready = true; };
    if (document.addEventListener) {
      document.addEventListener("DOMContentLoaded", isReady, false);
    } else {
      window.attachEvent('onload', isReady);
    }

    var e = document.createElement('script');
    e.type = 'text/javascript';
    e.id = 'janrainAuthWidget';

    if (document.location.protocol === 'https:') {
      e.src = 'https://rpxnow.com/js/lib/ibiza-pearls/engage.js';
    } else {
      e.src = 'http://widget-cdn.rpxnow.com/js/lib/ibiza-pearls/engage.js';
    }

    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(e, s);
})();
      /*
      (function() {
       if (typeof window.janrain !== 'object') window.janrain = {};
       window.janrain.settings = {};
 
       janrain.settings.tokenUrl = '<%=System.getProperty("janrain.tokenUrl")%>';
       
       function isReady() { janrain.ready = true; };
       if (document.addEventListener) {
         document.addEventListener("DOMContentLoaded", isReady, false);
       } else {
         window.attachEvent('onload', isReady);
       }
 
       var e = document.createElement('script');
       e.type = 'text/javascript';
       e.id = 'janrainAuthWidget';
 
       if (document.location.protocol === 'https:') {
         e.src = 'https://rpxnow.com/js/lib/widget-examples-default/engage.js';
       } else {
         e.src = 'http://widget-cdn.rpxnow.com/js/lib/widget-examples-default/engage.js?language_preference=es';
       }
 
       var s = document.getElementsByTagName('script')[0];
       s.parentNode.insertBefore(e, s);
      })();
      
      
      */
    </script>

</head>

	<%
	 //String facebookAuthRedirectURL = FacebookConnectUtil.getRedirectURL(themeDisplay.getCompanyId());
	String rpxCode =(String) session.getAttribute("rpxCode");
	//System.out.println("rpx code " + rpxCode);
	//System.out.println("sss QRPX-JSP: " + session.getId());
	if(rpxCode!=null && rpxCode.length()>0)
	{	
		response.sendRedirect("/web/xapp-studio/home");
	}
	
	/*
	LoginUti.login(
			request, response, login, password, rememberMe, authType);
	*/
	//themeDisplay.setSignedIn(true);

/*
	//facebookAuthRedirectURL = HttpUtil.addParameter(facebookAuthRedirectURL, "redirect", HttpUtil.encodeURL(loginRedirectURL.toString()));

	String facebookAuthURL = FacebookConnectUtil.getAuthURL(themeDisplay.getCompanyId());

	facebookAuthURL = HttpUtil.addParameter(facebookAuthURL, "client_id", FacebookConnectUtil.getAppId(themeDisplay.getCompanyId()));
	facebookAuthURL = HttpUtil.addParameter(facebookAuthURL, "redirect_uri", facebookAuthRedirectURL);
	facebookAuthURL = HttpUtil.addParameter(facebookAuthURL, "scope", "email");

	String taglibOpenFacebookConnectLoginWindow = "javascript:var facebookConnectLoginWindow = window.open('" + facebookAuthURL.toString() + "', 'facebook', 'align=center,directories=no,height=560,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no,width=1000'); void(''); facebookConnectLoginWindow.focus();";
	*/
	 %>
    
    <body lang="es">
    
      <!--  janrain:signInEmbedded /-->
      <div id="janrainEngageEmbed" class="janrainEngageEmbed"></div>
      
    </body>
</html>
