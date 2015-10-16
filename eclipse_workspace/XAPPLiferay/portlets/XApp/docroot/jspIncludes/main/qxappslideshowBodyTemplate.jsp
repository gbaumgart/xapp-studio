<%@page import="pmedia.Servlets.SessionCreateAction"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<% 

	String _dojoPath=(String)request.getSession().getAttribute("dojoPath");
	String _bootLoader=(String)request.getSession().getAttribute("bootLoader");
	String _dojoConfig=(String)request.getSession().getAttribute("dojoConfig");
	
	String _path = request.getContextPath();
	String _basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";

	//System.out.println("Loading QXAppSlideShow Application at \n\t : " + _basePath +"\n\t DojoPath : " + _dojoPath + "\n\t BootLoader : " + _bootLoader);
	

     HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(request);
     
    
    //System.ount.println("TimeZone : " + TimeZone.getDefault().toString());
    //System.ount.println("TimeZone : " + TimeZone.getDefault().getDisplayName());
    
    
    
    /*
    HttpSessionContext sc=httpReq.getSession().getSessionContext();
	HttpSession session2=sc.getSession("55248C09BB2DFE5DE11F1E81BF830A7A");
	
	if(session2!=null)
	{
		System.out.println("got session----------");
	}
    */
    //HttpServletRequest httpSession = PortalUtil.getS
    ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
    if(themeDisplay !=null)
    {
    	HttpSession _s = SessionCreateAction.find(httpReq.getSession().getId());
    	if(_s!=null)
    	{
    		//System.out.println("got theme display from session----------" + httpReq.getSession().getId());
    		_s.setAttribute("themeDisplay", themeDisplay);
    	}       
       httpReq.getSession().setAttribute("themeDisplay", themeDisplay);
    }
			
 %>
 
<body>
			<div id="loaderParent">
			 <div id="loader"><div class="spinner"></div><div id="loaderInner" style="direction:ltr;">Loading ... </div></div>
			 </div>
              <div id="qxappslideshowMain" class="qxappslideshowMain" style="">
			      <div id="qxappslideshowParent" class="qxappslideshowParent"  style="padding-left:0px;">
			      </div>
		      </div>
      <div id="widgetCache"></div>

<script type="text/javascript" src="<%=_dojoPath %>" djConfig="<%=_dojoConfig %>"></script>

<%if(_bootLoader !=null){ %>
	<script type="text/javascript" src="<%=_bootLoader %>"></script>
<%} %>

<script type="text/javascript">

</script>
</body>
