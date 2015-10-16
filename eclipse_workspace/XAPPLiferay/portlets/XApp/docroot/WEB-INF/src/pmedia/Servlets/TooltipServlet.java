package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.xml.sax.SAXException;

import pmedia.DataJSONTransformer.ArticleDataTranslationTransformer;
import pmedia.DataJSONTransformer.BaseDataTransformer;
import pmedia.DataManager.Cache;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.MediaUtils;
import pmedia.types.ArticleData;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.Category;
import pmedia.types.Domain;
import pmedia.types.MediaItemBase;
import pmedia.types.PMDataTypes;
import pmedia.types.PictureTransformOptions;
import pmedia.types.ToolTipInfo;
import pmedia.utils.ApplicationContentTools;
import pmedia.utils.CListItemTools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.ToolTipFactory;
import cmx.cache.DataSourceCache;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;
import cmx.types.ECMBannerSourceType;
import cmx.types.ECMContentSourceType;
import flexjson.JSONSerializer;


public class TooltipServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
    protected void doContent(PrintWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
    }

    public void doGetDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String ttkey = request.getParameter("ttkey");
		if(ttkey==null)
		{			
			return404Page();
			return;
		}
		ToolTipInfo info = ToolTipFactory.getToolTip(ttkey, lang);

		JSONSerializer serializer = new JSONSerializer();
    	
    	String jsonres = serializer.deepSerialize(info);
    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	response.getWriter().write(jsonres);
    	
    	
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	getCMObjects(request, response, false);
    	if(!preventCache){
	    	String cachedResponse = getCachedResponse(null);
	    	if(cachedResponse!=null){
	    		sendCachedOutput(request, response, cachedResponse);
	    		return;
	    	}
    	}
    	String action= request.getParameter("action");
    	if(action.equals("get"))
    	{
    		doGetDetails(request, response);
    	}
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
