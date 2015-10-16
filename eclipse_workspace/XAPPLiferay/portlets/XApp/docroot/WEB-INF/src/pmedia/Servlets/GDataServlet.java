package pmedia.Servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.struts2.json.annotations.SMDMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.xml.sax.SAXException;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.liferay.portal.model.User;

import cmx.manager.GTrackerManager;
import cmx.tools.Crypto;
import cmx.tools.GDocumentList;
import cmx.tools.GDocumentListException;
import cmx.tools.LiferayContentTools;
import cmx.tools.StyleTreeFactory;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DBConnectionError;
import cmx.types.SQLDataSource;
import cmx.types.StyleTree;

import pmedia.DataJSONTransformer.ArticleDataTranslationTransformer;
import pmedia.DataJSONTransformer.BaseDataTransformer;
import pmedia.DataManager.Cache;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;

import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.MediaUtils;
import pmedia.SearchBeans.EventSearch;

import pmedia.types.ArticleData;
import pmedia.types.Category;
import pmedia.types.Domain;
import pmedia.types.LocationData;
import pmedia.types.MediaItemBase;
import pmedia.types.PMDataTypes;
import pmedia.utils.StringUtils;
import flexjson.JSONSerializer;


public class GDataServlet extends CMBaseServlet
{
    private static final long serialVersionUID = 1L;
    protected void doContent(PrintWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
    	
    }
    String getContentType(String fileName) {
        String extension[] = { // File Extensions
            "txt", //0 - plain text
            "htm", //1 - hypertext
            "jpg", //2 - JPEG image
            "png", //2 - JPEG image
            "gif", //3 - gif image
            "pdf", //4 - adobe pdf
            "doc", //5 - Microsoft Word
            "docx",
        }; // you can add more
        String mimeType[] = { // mime types
            "text/plain", //0 - plain text
            "text/html", //1 - hypertext
            "image/jpg", //2 - image
            "image/jpg", //2 - image
            "image/gif", //3 - image
            "application/pdf", //4 - Adobe pdf
            "application/msword", //5 - Microsoft Word
            "application/msword", //5 - Microsoft Word
        }, // you can add more
                contentType = "text/html";    // default type
        // dot + file extension
        int dotPosition = fileName.lastIndexOf('.');
        // get file extension
        String fileExtension =
                fileName.substring(dotPosition + 1);
        // match mime type to extension
        for (int index = 0; index < mimeType.length; index++) {
            if (fileExtension.equalsIgnoreCase( extension[index])) {
                contentType = mimeType[index];
                break;
            }
        }
        return contentType;
    }

    
    public void getFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    		//mc007ibi.dyndns.org:8080/CMLiferay-portlet/client?action=getArchive&uuid=11166763-e89c-44ba-aba7-4e9f4fdf97a9&appIdentifier=ibizamedia5
    			
	    	String type = request.getParameter("type");
			if(type==null)
			{
				type="spreadsheet";
			}
			
    		String servletPath = System.getProperty("ServletPath");
			if(application==null){
				return;
			}
			
			if(getDs()==null){
				return;
			}
			
			if(ref==null){
				return;
			}

			
			GTrackerManager.trackXAPPEvent(GTrackerManager.openGDoc,uuid+"::"+appIdentifier);
			
			String key = "asd28071977";
	        Crypto encrypter = new Crypto(key);
			String userD = encrypter.decrypt(ds.getUser());
			String passD = encrypter.decrypt(ds.getPassword());

			String DEFAULT_HOST = "docs.google.com";
			GDocumentList documentList =null;
			
			try {
				documentList =new GDocumentList("any",DEFAULT_HOST);
			} catch (GDocumentListException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			DBConnectionError res = new DBConnectionError();
			if(!userD.contains("@gmail.com"))
			{
				userD+="@gmail.com";
			}
			
			byte[] docIdB = Base64.decodeBase64(ref.getBytes());
			if(docIdB==null){
				return;
			}
			String docId = new String (docIdB);
			
			try {
				documentList.login(userD, passD);
			} catch (AuthenticationException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			} catch (GDocumentListException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
			String format="pdf";
			
			String remoteFile =System.getProperty("WebPathOuter")+"CMACArchives/" + DigestUtils.md5Hex( docId);
			String localFile =System.getProperty("AppArchiveRoot")+DigestUtils.md5Hex( docId) +".pdf";
			if(type.equals("spreadsheet")){
				try {
					documentList.downloadSpreadsheet(docId, localFile, format);
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (GDocumentListException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			}else if(type.equals("document")){
				try {
					documentList.downloadDocument(docId.replace("document:",""), localFile, format);
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (GDocumentListException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			}else if(type.equals("presentation")){
				try {
					documentList.downloadPresentation(docId.replace("presentation:",""), localFile, format);
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (GDocumentListException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			}
			
			//response.sendRedirect(remoteFile);
			
			
					/*
			//application  = appManager.getApplication(appIdentifier,uuid);
			String fileName = appManager.compressApplication(application,servletPath);
			System.out.println("compressing application : " + servletPath);
			String baseUrl  = StringUtils.getBaseUrl(request);
			baseUrl+="/" +application.getUserIdentifier()+"/" + fileName;
			String archiveRedirectionUrl = baseUrl;
			String filePathLocation = servletPath+uuid + "/"+fileName;
			

			//response.sendRedirect(archiveRedirectionUrl);
			response.sendRedirect(System.getProperty("WebPath")+"CMACArchives/"+fileName);
			*/
			String contentType = getContentType(localFile);
			System.out.println("sending " + localFile + " as " + contentType);
			File file = new File(localFile);
	        response.setContentType(contentType);
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + "a.pdf" + "\"");
	        //response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
	        //response.setHeader("Pragma", "no-cache"); // HTTP 1.0
	        //response.setDateHeader("Expires", -1); // Proxies.
	        
	        
	        
	        int length = (int) file.length();
	        response.setContentLength(length);
	        
	        //System.out.println("redirection url : " + archiveRedirectionUrl + " with length : " + length);
	        
	        byte[] bytes = new byte[length];
	        FileInputStream fin = new FileInputStream(file);
	        fin.read(bytes);
	        ServletOutputStream os = response.getOutputStream();
	        os.write(bytes);
	        os.flush();
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	getCMObjects(request, response, true);
    	
    	String action= request.getParameter("action");
    	if(action.equals("get"))
    	{
    		getFile(request, response);
    	}
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
