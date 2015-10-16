package pmedia.Servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmedia.types.JSConstants;
import pmedia.utils.StringUtils;

public class JSSesssionStoreServlet extends HttpServlet
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -4531648356488406831L;
	private ServletContext context;
  
  public void init(ServletConfig paramServletConfig)
    throws ServletException
  {
    super.init(paramServletConfig);
    this.context = paramServletConfig.getServletContext();
  }

  public void doGet(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws ServletException, IOException
  {
    doPost(paramHttpServletRequest, paramHttpServletResponse);
  }

  public HttpServletRequest inRequest=null;
  
  public void doPost(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws ServletException, IOException
  {
	this.inRequest = paramHttpServletRequest;
	if(paramHttpServletRequest==null)
	{
		//System.out.println("\n\n\n got js call but servlet invalid !!!!!");
		return;
	}
	
	paramHttpServletRequest.getSession().setAttribute("baseUrl",StringUtils.getBaseUrl(paramHttpServletRequest));
	System.setProperty("baseUrl", StringUtils.getBaseUrl(paramHttpServletRequest));
	System.setProperty("contextName", paramHttpServletRequest.getContextPath());
	
//	System.out.println("got js call "  + inRequest.getRequestURL().toString());
	
	String action = paramHttpServletRequest.getParameter("action");
	String event = paramHttpServletRequest.getParameter("event");
	String type = paramHttpServletRequest.getParameter("type");
	String object = paramHttpServletRequest.getParameter("object");
	String value = paramHttpServletRequest.getParameter("value");
	System.out.println("got js call "  + inRequest.getRequestURL().toString() + " action : " + action  + "  event  : " + event  + " type " + type + " object = " + object  + " value = " + value);
	
	if(action !=null && action.equals(JSConstants.LOG))
	{
		System.out.println("JSLOG \t" + value );
	}
	if(action !=null && action.equals(JSConstants.SESSION_STORE))
	{
		if(event!=null && event.equals(JSConstants.ELOCTAB_CHANGED))
		{
			if(type!=null)
			{
				paramHttpServletRequest.getSession().setAttribute(JSConstants.VAR_LAST_LOC_TAB_TYPE,type);
			}
		}
		//just save it !
		if(event==null && object !=null && value !=null)
		{
			paramHttpServletRequest.getSession().setAttribute(object, value);
			//System.out.println(paramHttpServletRequest.getSession().getId());
		}
		
	}
	paramHttpServletResponse.getWriter().write("<p>nada</p>");
  }
}