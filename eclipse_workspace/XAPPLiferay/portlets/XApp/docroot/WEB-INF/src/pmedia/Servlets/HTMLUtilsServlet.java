package pmedia.Servlets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pmedia.types.CListItem;
import pmedia.utils.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cmx.tools.GDocumentListException;

import com.google.gdata.data.MediaContent;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.util.ServiceException;
import com.liferay.portal.kernel.util.DocumentConversionUtil;

import net.moraleboost.streamscraper.ScrapeException;
import net.moraleboost.streamscraper.Stream;
import net.moraleboost.streamscraper.Scraper;
import net.moraleboost.streamscraper.scraper.IceCastScraper;
import net.moraleboost.streamscraper.scraper.ShoutCastScraper;

public class HTMLUtilsServlet extends CMBaseServlet
{
	public void downloadFile(URL url, String filepath) throws IOException,
	    MalformedURLException, ServiceException, GDocumentListException {
	  if (url == null || filepath == null) {
	    throw new GDocumentListException("null passed in for required parameters");
	  }
	
	  
	  InputStream inStream = null;
	  FileOutputStream outStream = null;
	  
	  try {
	    inStream = url.openStream();
	    outStream = new FileOutputStream(filepath);
	
	    int c;
	    while ((c = inStream.read()) != -1) {
	      outStream.write(c);
	    }
	  } finally {
	    if (inStream != null) {
	      inStream.close();
	    }
	    if (outStream != null) {
	      outStream.flush();
	      outStream.close();
	    }
	  }
	}
	public void doConvert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		getCMObjects(request, response, true);
		InputStream is=null;

		// if we were getting data from a file, we might use:
		
		if(ref!=null){
			ref=java.net.URLDecoder.decode(ref);	
		}
		
		
		URL  url = new URL(ref);
		String fileExt = "doc";
		
		if(ref.contains(".xls")){
			fileExt="xls";
		}else if(ref.contains(".ppt")){
			fileExt="ppt";
		}
		else if(ref.contains(".pptx")){
				fileExt="pptx";
		}else if(ref.contains(".docx")){
			fileExt="docx";
		}if(ref.contains(".pdf")){
			fileExt="pdf";
		}
		
		File file  =null;
		if(url!=null ){
			is = url.openStream();
			if(!fileExt.contains("pdf")){
				
				try {
					file = DocumentConversionUtil.convert(fileExt + UUID.randomUUID().toString().substring(0, 5), is, fileExt, "pdf");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				
				String localFile =System.getProperty("AppArchiveRoot")+DigestUtils.md5Hex(ref) +".pdf";
				try {
					downloadFile(url, localFile);
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GDocumentListException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				file = new File(localFile);
			}
		}
		
		URLConnection urlConn = null;
	    ServletOutputStream stream = null;
	    BufferedInputStream buf = null;
	    
		
		if(file!=null){
			System.out.println("converting : " + ref + "::success");
			
			 try {
			      stream = response.getOutputStream();
			      response.setContentType("application/pdf");

			      response.addHeader("Content-Disposition", "attachment; filename="
			          + file.getName() + "" + UUID.randomUUID().toString().substring(0, 5) + ".pdf");
			      
			      response.setContentLength((int) file.length());
			      FileInputStream input = new FileInputStream(file);
			      buf = new BufferedInputStream(input);
			      int readBytes = 0;

			      while ((readBytes = buf.read()) != -1)
			        stream.write(readBytes);
			    } catch (IOException ioe) {
			      throw new ServletException(ioe.getMessage());
			    } finally {
			      if (stream != null)
			        stream.close();
			      if (buf != null)
			        buf.close();
			    }
		}
		
		
	}
	
	/**
	 * 
	 */
	public String getMetaTag(Document document, String attr) 
	{
	    Elements elements = document.select("meta[name=" + attr + "]");
	    for (Element element : elements) {
	        final String s = element.attr("content");
	        if (s != null) return s;
	    }
	    elements = document.select("meta[property=" + attr + "]");
	    for (Element element : elements) {
	        final String s = element.attr("content");
	        if (s != null) return s;
	    }
	    return null;
	}
	private static final long serialVersionUID = 1L;
	public void doGetStreamMeta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	  
		getCMObjects(request, response, true);
		if(ref==null)
		{
			return404Page();
			return;
		}
		if(!ref.contains("http://"))
		{
			ref = "http://" + ref; 
		}	
		ref=java.net.URLEncoder.encode(ref);
		Scraper scraper = new IceCastScraper();
        List<Stream> streams = null;
        if(ref==null || ref.length()==0)
        {
        	return404Page();
        }
        try {
			streams = scraper.scrape(new URI(ref));
		} catch (ScrapeException e) {
			
			//e.printStackTrace();
			return404Page();
			return;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return404Page();
			return;
		}
     
        if(streams !=null && streams.size()==0)
       {
    	   scraper = new ShoutCastScraper();
           try {
   				streams = scraper.scrape(new URI(ref));
   			} catch (ScrapeException e) 
   			{
	   			e.printStackTrace();
	   			return404Page();
	   			return;
	   		} catch (URISyntaxException e) {
	   			e.printStackTrace();
	   			return404Page();
	   			return;
	   		}
           
           String title = null;
           for (Stream stream: streams) {
               System.out.println("Song Title: " + stream.getCurrentSong());
               title = stream.getCurrentSong();
               System.out.println("URI: " + stream.getUri());
           }
           
           if(title!=null)
           {
    	    	String output = "{res:" + "\"" +  title + "\"}";
    	    	sendOutput(request, response,output);
    	    	return;
           }
       }
       
        
        String output = "{res:" + "\"" +  "No Title" + "\"}";
    	sendOutput(request, response,output);
	}
	public void doGetPageTitle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	  
		getCMObjects(request, response, true);
		if(ref==null)
		{
			return404Page();
			return;
		}
		if(!ref.contains("http://"))
		{
			ref = "http://" + ref; 
		}	
		ref=java.net.URLDecoder.decode(ref);
		Document doc = null;
		String title = "No Title";
		try {
			doc = Jsoup.connect(ref).get();
		} catch (IOException e) 
		{
			//e.printStackTrace();
			//return result;
			String fileName = null;
			try {
				fileName = StringUtils.filename(ref) + "." + StringUtils.extension(ref);
			} catch (Exception e2) {

			}
			
			if(fileName!=null && fileName.length()>0){
				title=fileName;
			}
			
			if(title.indexOf("%2")!=-1){
				title = title.substring(title.lastIndexOf("%2")+2,title.length());
				
			}
			String output = "{res:" + "\"" +  title + "\"}";
	    	sendOutput(request, response,output);
			return;
		}
	    
	    if(doc!=null){
	    	title=getMetaTag(doc, "title");
	    }
	    if(title==null){
	    	Elements finallink1 = doc.select("title");
	    	if(finallink1!=null){
	    		title=finallink1.text();
	    	}
	    }
	    if(title!=null)
	    {
	    	String output = "{res:" + "\"" +  title + "\"}";
	    	sendOutput(request, response,output);
	    }
	}
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String action= request.getParameter("action");
    	
    	if(action.equals("getPageTitle"))
    	{
    		doGetPageTitle(request, response);
    	}
    	
    	if(action.equals("getStreamMeta"))
    	{
    		doGetStreamMeta(request, response);
    	}
    	
    	if(action.equals("convert"))
    	{
    		doConvert(request, response);
    	}

    	
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
