package pmedia.Servlets;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.hsqldb.lib.StringInputStream;
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
import pmedia.html.factory.WhiteListDataFactory;
import pmedia.html.tools.HTMLConverter;
import pmedia.html.types.ClientFilter;
import pmedia.types.ArticleData;
import pmedia.types.CContent;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.Category;
import pmedia.types.Constants;
import pmedia.types.Domain;
import pmedia.types.MediaItemBase;
import pmedia.types.PMDataTypes;
import pmedia.types.PictureTransformOptions;
import pmedia.utils.ApplicationContentTools;
import pmedia.utils.CListItemTools;
import pmedia.utils.CSSUtils;
import pmedia.utils.ECMContentSourceTypeTools;
import cmx.cache.DataSourceCache;
import cmx.data.CContentFactory;
import cmx.data.CContentStorageUtils;
import cmx.servlets.FileServlet.Range;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;
import cmx.types.ECMBannerSourceType;
import cmx.types.ECMContentSourceType;
import cmx.types.StyleTree;
import flexjson.JSONSerializer;


public class CSSServlet extends CMBaseServlet
{
	 public class Range {
	        long start;
	        long end;
	        long length;
	        long total;

	        /**
	         * Construct a byte range.
	         * @param start Start of the byte range.
	         * @param end End of the byte range.
	         * @param total Total length of the byte source.
	         */
	        public Range(long start, long end, long total) {
	            this.start = start;
	            this.end = end;
	            this.length = end - start + 1;
	            this.total = total;
	        }

	    }
	 private static final int DEFAULT_BUFFER_SIZE = 10240; // ..bytes = 10KB.
	    
	 private static final long DEFAULT_EXPIRE_TIME = 604800000L; // ..ms = 1 week.
	 
	 private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";
    private static final long serialVersionUID = 1L;
    private static boolean accepts(String acceptHeader, String toAccept) {
        String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
        Arrays.sort(acceptValues);
        return Arrays.binarySearch(acceptValues, toAccept) > -1
            || Arrays.binarySearch(acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1
            || Arrays.binarySearch(acceptValues, "*/*") > -1;
    }
   
    private static void copy(StringInputStream input, OutputStream output, long start, long length)
            throws IOException
        {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int read;

            //if (input.() == length) {
                // Write full range.
                while ((read = input.read(buffer)) > 0) {
                    output.write(buffer, 0, read);
                }
            
        	/*} else {
                // Write partial range.
                input.seek(start);
                long toRead = length;

                while ((read = input.read(buffer)) > 0) {
                    if ((toRead -= read) > 0) {
                        output.write(buffer, 0, read);
                    } else {
                        output.write(buffer, 0, (int) toRead + read);
                        break;
                    }
                }
            }
            */
        
        }
    
    
    public String doGetCSSAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    
    	getCMObjects(request, response, true);
    	ArrayList files = new ArrayList<String>();
    	if(userAgent==null){
			userAgent=pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE;
		}
		
		if(userAgent.equals(pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE))
		{
			userAgent=pmedia.types.Constants.USERAGENT_IPHONE_NATIVE;
		}
		if(userAgent.equals(pmedia.types.Constants.MOBILE_WEB_APP) || userAgent.equals(pmedia.types.Constants.MOBILE_WEB_APP_TABLET))
		{
			userAgent=pmedia.types.Constants.USERAGENT_IPHONE_NATIVE;
		}
		userAgent=pmedia.types.Constants.USERAGENT_IPHONE_NATIVE;
		
		String transformedFile = transformCSS(uuid, appIdentifier, userAgent,"Default/Default.css", null);
		files.add(transformedFile);
		
		transformedFile = CSSUtils.transformCSS(uuid, appIdentifier, userAgent,"ListItems.css", null);
		files.add(transformedFile);
		
		transformedFile = CSSUtils.transformCSS(uuid, appIdentifier, userAgent,"CustomFields.css", null);
		files.add(transformedFile);
		
		transformedFile = CSSUtils.transformCSS(uuid, appIdentifier, userAgent,"Content.css", null);
		files.add(transformedFile);
		
		transformedFile = CSSUtils.transformCSS(uuid, appIdentifier, userAgent,"LaunchItems.css", null);
		files.add(transformedFile);
		
		transformedFile = CSSUtils.transformCSS(uuid, appIdentifier, userAgent,"Views.css", null);
		files.add(transformedFile);
		
		CSSUtils.addCustomStylesTree(uuid, appIdentifier, userAgent, files);
		
		String transformedCSS=CSSUtils.optimizeCSS(files);
		
		String contentType="text/css";
    	contentType += ";charset=UTF-8";
    	response.setContentType(contentType);
    	response.setDateHeader("Last-Modified", new Date().getTime());
    	response.setHeader("Content-Disposition", "attachment; filename=\"" + "userCSS.css" + "\"");
    	if(transformedCSS!=null)
    	{
    		response.getWriter().write(transformedCSS);
    	}
		
    	return transformedCSS;
		
    	//action=get
    	/*
    	appIdentifier=myeventsapp1d0
    			file=Views.css
    			platform=IPHONE_NATIVE
    			time=1369560173320
    			transformResource=true
    			uuid=11166763-e89c-44ba-aba7-4e9f4fdf97a9*/
    	
    }
    
    public String transformCSS(String uuid,String appId,String platform,String file,String classSuffix){

    	
    	ApplicationManager appManager = ApplicationManager.getInstance(); 
    	if(file==null)
		{
			return null;
		}

    	if(platform==null){
    		platform=pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE;
		}
		
		if(platform.equals(pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE))
		{
			platform=pmedia.types.Constants.USERAGENT_IPHONE_NATIVE;
		}
		if(platform.equals(pmedia.types.Constants.MOBILE_WEB_APP) || platform.equals(pmedia.types.Constants.MOBILE_WEB_APP_TABLET))
		{
			platform=pmedia.types.Constants.USERAGENT_IPHONE_NATIVE;
		}
		
		platform=pmedia.types.Constants.USERAGENT_IPHONE_NATIVE;
    	
    	String cssBasePath = appManager.getUserAppPath(uuid, appId) + "style/" + userAgent+"/";

    		
    	File cssFile = new File(cssBasePath + file);
    	if(!cssFile.exists()){
    		System.out.println("no such css file : " + cssBasePath + file);
    	}
    	String transformedCSS=CSSUtils.adjustCSS(cssBasePath+file, appIdentifier, uuid, userAgent,classSuffix);
    	if(file.contains("List") || file.contains("Custom") )
    	{
    		if(transformedCSS!=null){
	    		transformedCSS = transformedCSS.replace("width: 220px;", "");
	    		transformedCSS = transformedCSS.replace("width: 210px;", "");
    		}
    	}
    	return transformedCSS;
    }
    
    public void doGetCSS(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	getCMObjects(request, response, true);
    	
    	if(application==null)
    	{
    		return;
    	}
    	
    	if(userAgent==null)
    	{
    		return;
    	}
    	String file = request.getParameter("file");
		if(file==null)
		{
			return;
		}
		
		String classSuffix= request.getParameter("classSuffix");
		if(classSuffix==null){
			classSuffix="";
		}
		
		if(userAgent==null){
			userAgent=pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE;
		}
		
		if(userAgent.equals(pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE))
		{
			userAgent=pmedia.types.Constants.USERAGENT_IPHONE_NATIVE;
		}
		if(userAgent.equals(pmedia.types.Constants.MOBILE_WEB_APP) || userAgent.equals(pmedia.types.Constants.MOBILE_WEB_APP_TABLET))
		{
			userAgent=pmedia.types.Constants.USERAGENT_IPHONE_NATIVE;
		}
		
		userAgent=pmedia.types.Constants.USERAGENT_IPHONE_NATIVE;
    	
    	String cssBasePath = appManager.getUserAppPath(uuid, appIdentifier) + "style/" + userAgent+"/";

    	
    	File cssFile = new File(cssBasePath + file);
    	if(!cssFile.exists()){
    		System.out.println("no such css file : " + cssBasePath + file);
    	}

    	String transformedCSS=CSSUtils.adjustCSS(cssBasePath+file, appIdentifier, uuid, userAgent,classSuffix);

    	String contentType="text/css";
    	contentType += ";charset=UTF-8";
    	response.setContentType(contentType);
    	response.setDateHeader("Last-Modified", new Date().getTime());
    	if(file.contains("List") || file.contains("Custom") )
    	{
    		if(transformedCSS!=null){
	    		transformedCSS = transformedCSS.replace("width: 220px;", "");
	    		transformedCSS = transformedCSS.replace("width: 210px;", "");
    		}
    	}
    	response.setHeader("Content-Disposition", "attachment; filename=\"" + file + "\"");
    	if(transformedCSS!=null)
    	{
    		response.getWriter().write(transformedCSS);
    	}

    	//String allCSS = doGetCSSAll(request, response);
    	
    	//System.out.println("all CSS : \n" + allCSS);
    	
    	/*
    	
    	boolean acceptsGzip = true;
        String disposition = "inline";
        long length = transformedCSS.length();
        long lastModified = cssFile.lastModified();
        String eTag = file + "_" + length + "_" + lastModified;
        
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("ETag", eTag);
        response.setDateHeader("Last-Modified", lastModified);
        String acceptEncoding = request.getHeader("Accept-Encoding");
        acceptsGzip = acceptEncoding != null && accepts(acceptEncoding, "gzip");
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setHeader("Content-Disposition", disposition + ";filename=\"" + file + "\"");
        response.setHeader("Accept-Ranges", "bytes");
        //contentType += ";charset=UTF-8";
    	
    	
        
        
        //Range r = full;
        
        //response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
        OutputStream output = null;
        RandomAccessFile input = null;
        if (transformedCSS!=null) 
        {
        	 try {
        	      //input = new RandomAccessFile(cssFile, "r");
                  output = response.getOutputStream();
			      // Open streams.
                  
	            if (acceptsGzip) {
	                // The browser accepts GZIP, so GZIP the content.
	                response.setHeader("Content-Encoding", "gzip");
	                Range full = new Range(0, length - 1, length);
	                Range r = full;
	                response.setHeader("ETag", eTag);
	                response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
	              
	                output = new GZIPOutputStream(output, DEFAULT_BUFFER_SIZE);
	                StringInputStream buf = new StringInputStream(transformedCSS);
	                copy(buf, output, 0, transformedCSS.length());
	                
	            } else {
	                // Content length is not directly predictable in case of GZIP.
	                // So only add it if there is no means of GZIP, else browser will hang.
	                //response.setHeader("Content-Length", String.valueOf(r.length));
	            	response.getWriter().write(transformedCSS);
	            }
        	 } catch (Exception e) {
 				// TODO: handle exception
 			} finally {
 	            // Gently close streams.
 	            close(output);
 	            //close(input);
 	        }
             

            // Copy full range.
            
        }
        */
    }
   
    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException ignore) {
                // Ignore IOException. If you want to handle this anyway, it might be useful to know
                // that this will generally only be thrown when the client aborted the request.
            }
        }
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String action= request.getParameter("action");
    	if(action !=null && action.equals("get"))
    	{
    		doGetCSS(request, response);
    	}else if(action !=null && action.equals("getAll"))
    	{
    		doGetCSSAll(request, response);
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
