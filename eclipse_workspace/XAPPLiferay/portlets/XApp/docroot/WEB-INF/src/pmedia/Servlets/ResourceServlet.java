package pmedia.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmedia.DataJSONTransformer.EventDataTransformer;
import pmedia.DataJSONTransformer.ResourceDataTransformer;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.EventDataArrayTools;
import pmedia.DataUtils.LocationPropertyTools;
import pmedia.DataUtils.ResourceTools;
import pmedia.SearchBeans.EventSearch;

import pmedia.types.EventData;
import pmedia.types.ResourceData;
import flexjson.JSONSerializer;


public class ResourceServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private static final String __HUMAN_ID = "ID";
    protected void doContent(PrintWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {

    	/*
        writer.println("<h1 class='pageheader'>System Settings</h1><div id='content'>");
        //Cursor cursor = getContentResolver().query(Settings.System.CONTENT_URI, null, null, null, null);
        String[] cols = cursor.getColumnNames();
        int i = 0;

        for (String col : cols)
        {
            if (i == 0)
            {
                cols[i] = __HUMAN_ID;
            }
            else
            {
                // Make first letter uppercase
                cols[i] = col.substring(0, 1).toUpperCase() + col.substring(1);
            }

            i++;
        }

       // HTMLHelper.formatTable(cols, cursor, writer);
       // writer.println("</div>");
        */
    }
    public void doGetList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String lang=(String)request.getSession().getAttribute("lang");
    	if(lang==null)
    	{
    		lang = "en";
    	}
    	String typeStr=request.getParameter("type");
    	if(typeStr==null)
    	{
    		typeStr="-1";
    	}
    	int type = Integer.parseInt(typeStr);
    	
    	ArrayList<ResourceData> list = ServerCache.getInstance().getDC("ibiza").get(DomainCache.RESOURCES);
    	JSONSerializer serializer = new JSONSerializer();
    	ArrayList<ResourceData> listOut = ResourceTools.getByType(list, type);
    	
    	for (ResourceData resourceData : listOut) 
    	{
			resourceData.getPictures();
		}
    	
    	serializer.include("pictures");
    	serializer.transform(new ResourceDataTransformer(true,pmedia.types.Constants.USERAGENT_TABLET,null,"iconU r l",lang), "iconUrl");
    	serializer.transform(new ResourceDataTransformer(true,pmedia.types.Constants.USERAGENT_TABLET,null,"pictures",lang), "pictures");
    	serializer.exclude("description");
    	

    	//serializer.exclude("s t a t i c M a p U r l");
    	
    	//serializer.exclude("descriptionNoPictures");
    	String jsonres = serializer.serialize(listOut);
    	//response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	response.getWriter().write(jsonres);
    }
    
    public void doGetDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String lang=(String)request.getSession().getAttribute("lang");
    	if(lang==null)
    	{
    		lang = "en";
    	}
    	String evtID = request.getParameter("id");
    	EventSearch lastEventSearch = (EventSearch)request.getSession().getAttribute("lastEventSearch");
    	ArrayList<EventData> elist = ServerCache.getInstance().getDC("ibiza").get(DomainCache.EVENTS_FINAL);
    	
    	EventData e  = EventDataArrayTools.getByUID(elist, evtID);
    	if(e==null)
    		return;
    	
    	
    	JSONSerializer serializer = new JSONSerializer();
    	serializer.transform(new EventDataTransformer(true,pmedia.types.Constants.USERAGENT_TABLET,e,"iconUrl",lang), "iconUrl");
    	if(e.getGalleryFiles()!=null && e.getGalleryFiles().size() > 0)
    	{
    		serializer.include("galleryFiles");
    		serializer.include("galleryThumbnailFiles");
    		serializer.include("galleryTitles");
    	}
    	
    	// it has a location, create a static map be default : 
    	if(e.loc!=null)
    	{
    		e.setMapUrl(LocationPropertyTools.getMapUrl(e.loc, request.getSession()));
    		//System.out.println(e.getMapUrl() + "\n "  + request.getSession().getId());
    		
    	}else
    	{
    		//e.setS t a t i c M a p U r l(null);
    		serializer.exclude("mapUrl");
    	}
    	//serializer.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET,e,"s t a t i c M a p U r l",lang), "s t a t i c M a p U r l");
    	
    	String jsonres = serializer.serialize(e);
    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	response.getWriter().write(jsonres);
    	
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String action= request.getParameter("action");
    	if(action.equals("list"))
    	{
    		doGetList(request, response);
    	}
    	if(action.equals("getDetail"))
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
