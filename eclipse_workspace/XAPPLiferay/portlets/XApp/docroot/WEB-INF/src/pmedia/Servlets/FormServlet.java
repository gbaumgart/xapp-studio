package pmedia.Servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pmedia.DataUtils.BreezingFormTools;
import pmedia.types.CList;
import pmedia.types.CListFormItem;
import pmedia.types.CListItem;
import cmx.types.ECMContentSourceType;
import flexjson.JSONSerializer;


public class FormServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
   
    public void doGetForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	getCMObjects(request, response, true);
    	if(application==null)
    	{
    		return404Page();
    	}
    	ArrayList forms = getDataSourceCache().getByType(ECMContentSourceType.BreezingForm);
    	CListItem formItem=null;
    	
		if(forms!=null)
		{
			CList formsItem = (CList) forms.get(0);
			if(formsItem!=null)
			{				
				formItem=formsItem.getItemByRefId(refId);
			}
		}
		
		if(formItem==null){
			return404Page(); 
			return;
		}
		
      	JSONSerializer serializer = new JSONSerializer();
      	
      	BreezingFormTools.addSubmitButton((CListFormItem) formItem);

    	String jsonres = serializer.deepSerialize(formItem);
    	response.setHeader("Content-Type", "text/text; charset=UTF-8");
    	response.setHeader("attachment", "article.json");
    	response.getWriter().write(jsonres);
    	
    }
    
    
   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String action= request.getParameter("action");
    	if(action !=null && action.equals("get"))
    	{
    		doGetForm(request, response);
    	}else{
    		return404Page();
    	}
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
