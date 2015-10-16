package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmedia.DataUtils.TranslationTools;
import flexjson.JSONSerializer;


public class TranslationServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private static final String __HUMAN_ID = "ID";
    public void doGetList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String lang = request.getParameter("lang");
    	
    	HashMap<String, String>tMenuMap = TranslationTools.generateMenuTranslationTable(lang, "ibiza");
    	JSONSerializer serializerT = new JSONSerializer();
    	String jsonresT = serializerT.serialize(tMenuMap);
    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	response.getWriter().write(jsonresT);
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String action= request.getParameter("action");
    	if(action.equals("list"))
    	{
    		doGetList(request, response);
    	}
    	
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
