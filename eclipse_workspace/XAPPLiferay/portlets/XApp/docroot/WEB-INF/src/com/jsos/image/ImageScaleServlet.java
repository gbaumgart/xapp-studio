package com.jsos.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import pmedia.Servlets.SerializerUtils;
import sun.security.provider.MD5;

import com.oreilly.servlet.ServletUtils;

public class ImageScaleServlet extends HttpServlet
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String CPR = "(c) Coldbeans info@servletsuite.com";
  private static final String VER = "1.3";
  private static final String DIR = "dir";
  private static final String HEIGHT = "height";
  private static final String WIDTH = "width";
  private static final String EXPIRES = "expires";
  private static final String CACHE = "cache";
  private ServletContext context;
  private static final String CACHED_FILE = "cj2009iss_";

  public void init(ServletConfig paramServletConfig)
    throws ServletException
  {
    super.init(paramServletConfig);
    this.context = paramServletConfig.getServletContext();
    //System.out.println("ImageScaleServlet (c) Coldbeans info@servletsuite.com 1.3");
  }

  public void doGet(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws ServletException, IOException
  {
    doPost(paramHttpServletRequest, paramHttpServletResponse);
  }

  public void downloadFile(String src,String dst)
  {

		java.io.BufferedInputStream in = null;
		try {
			in = new java.io.BufferedInputStream(new java.net.URL(src).openStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return;
		}
		java.io.FileOutputStream fos = null;
		try {
			fos = new java.io.FileOutputStream(dst);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return;
		}
		if(in==null)
			return;
		java.io.BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
		byte[] data = new byte[1024];
		int x=0;
		try {
			while((x=in.read(data,0,1024))>=0)
			{
				bout.write(data,0,x);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return;
		}
		try {
			bout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return;
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return;
		}
	}
  
  
  
  public void resizeToHeight(String src,String dst,int height)
  {
	  
	  String scriptPath = this.getServletContext().getRealPath("") + "/utils/resizeToHeight.sh";
	  
	  String downloadFolder= getInitParameter("downloads");
	  String scaledFileName = downloadFolder + filename(src) + height + "." + extension(src);
	  //System.out.println("scaled path" + scaledFileName);
	  File dstF = new File(dst);
	  if(dstF.exists())
		  return;
	  try
		{
			Runtime run = Runtime.getRuntime() ;
			String fullCMD=  scriptPath + " " + src + " " + height + " " +dst;
			//System.out.println("full scrupt " + fullCMD);
			String cmd[] = {src+""," " + height + " ",scaledFileName};
			//Process pr = run.exec(cmd,null,new File(inRequest.getRealPath("") + "/utils/"));
			//Process pr = run.exec(scriptPath,cmd);
			Process pr = run.exec(fullCMD);
			//Process pr2 = run.exec
			try {
				pr.waitFor() ;
			} catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			try {
			while ((line=buf.readLine())!=null) 
			{
				System.out.println(line);
			}
			} catch (IOException e) 
			{
				e.printStackTrace();
			} 
			
		}catch (IOException e)
		{
          System.out.println("exception happened - here's what I know: ");
          e.printStackTrace();
//          System.exit(-1);
		}
  }
  public void resizeToHeightThumbnail(String src,String dst,int height)
  {
	  String scriptPath = this.getServletContext().getRealPath("") + "/utils/resizetoHeight.sh";
	  
	  String downloadFolder= getInitParameter("downloads");
	  String scaledFileName = downloadFolder + filename(src) + height + "." + extension(src);
	  File dstF = new File(dst);
	  if(dstF.exists())
		  return;
	  try
		{
			Runtime run = Runtime.getRuntime() ;
			String fullCMD=  scriptPath + " " + src + " " + height + " " +dst;
			String cmd[] = {src+""," " + height + " ",scaledFileName};

			File f = new File(scriptPath);
			if(f.exists()){
				f.setExecutable(true);
			}
			
			Process pr = run.exec(fullCMD);
			try {
				pr.waitFor() ;
			} catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			try {
			while ((line=buf.readLine())!=null) 
			{
				System.out.println(line);
			}
			} catch (IOException e) 
			{
				e.printStackTrace();
			} 
			
		}catch (IOException e)
		{
          System.out.println("exception happened - here's what I know: ");
          e.printStackTrace();
		}
  }
  
  public void makeIcon(String src,String dst,int height)
  {
	  String scriptPath = this.getServletContext().getRealPath("") + "/utils/thumb.sh";
	  
	  String downloadFolder= getInitParameter("downloads");
	  String scaledFileName = downloadFolder + filename(src) + height + "." + extension(src);
	  File dstF = new File(dst);
	  if(dstF.exists())
		  return;
	  try
		{
			Runtime run = Runtime.getRuntime() ;
			String fullCMD=  scriptPath + " " + src + " " + height + " " +dst;
			String cmd[] = {src+""," " + height + " ",scaledFileName};
			
			File f = new File(scriptPath);
			if(f.exists()){
				f.setExecutable(true);
			}
			
			//System.out.println("full scrupt " + fullCMD);
			Process pr = run.exec(fullCMD);
			try {
				pr.waitFor() ;
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
				return;
			}
			
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			try {
			while ((line=buf.readLine())!=null) 
			{
				//System.out.println(line);
			}
			} catch (IOException e) 
			{
				e.printStackTrace();
				return;
			} 
			
		}catch (IOException e)
		{
          System.out.println("exception happened - here's what I know: ");
          e.printStackTrace();
		}
  }
  public void resizeToWidth(String src,String dst,int width)
  {
	  String scriptPath = this.getServletContext().getRealPath("") + "/utils/resizetoWidth.sh";
	  
	  String downloadFolder= getInitParameter("downloads");
	  String scaledFileName = downloadFolder + filename(src) + width + "." + extension(src);
	  File dstF = new File(dst);
	  if(dstF.exists())
		  return;
	  try
		{
			Runtime run = Runtime.getRuntime() ;
			String fullCMD=  scriptPath + " " + src + " " + width + " " +dst;
			//System.out.println("full scrupt " + fullCMD);
			String cmd[] = {src+""," " + width + " ",scaledFileName};
			File f = new File(scriptPath);
			if(f.exists()){
				f.setExecutable(true);
			}
			Process pr = run.exec(fullCMD);
			try {
				pr.waitFor() ;
			} catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			try {
			while ((line=buf.readLine())!=null) 
			{
				System.out.println(line);
			}
			} catch (IOException e) 
			{
				e.printStackTrace();
			} 
			
		}catch (IOException e)
		{
          System.out.println("exception happened - here's what I know: ");
          e.printStackTrace();
		}
  }
  public void addShadow(String src,String dst)
  {
	  String scriptPath = this.getServletContext().getRealPath("") + "/utils/iconShadowSmall.sh";
	  String downloadFolder= getInitParameter("downloads");
	  String shadowed = filename(src) + "_shadowed" + "." + extension(src);
	  File dstF = new File(dst);
	  if(dstF.exists())
		  return;
	  
	  //System.out.println("scaled path" + scaledFileName);
	  
	  try
		{
			Runtime run = Runtime.getRuntime() ;
			String fullCMD=  scriptPath + " " + src + " " +  " " + dst;
		//	System.out.println("full scrupt " + fullCMD);
			//String cmd[] = {src+""," " + height + " ",scaledFileName};
			//Process pr = run.exec(cmd,null,new File(inRequest.getRealPath("") + "/utils/"));
			//Process pr = run.exec(scriptPath,cmd);
			Process pr = run.exec(fullCMD);
			//Process pr2 = run.exec
			try {
				pr.waitFor() ;
			} catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			try {
			while ((line=buf.readLine())!=null) 
			{
				System.out.println(line);
			}
			} catch (IOException e) 
			{
				e.printStackTrace();
			} 
			
		}catch (IOException e)
		{
          System.out.println("exception happened - here's what I know: ");
          e.printStackTrace();
//          System.exit(-1);
		}
  }
  public HttpServletRequest inRequest=null;
  
  public String getLocalFileName(String srcName , String h,String w, String shadow,Boolean icon)
  {
	  
	  int _h = 100;
	  int _w = 100;
	  boolean _shadow = false;
	  boolean resizeToHeight = true; 
	  
	  if(h!=null){_h = Integer.parseInt(h);  }
	  if(w!=null){_w = Integer.parseInt(w); resizeToHeight=false;}
	  if(shadow!=null){ _shadow= Boolean.parseBoolean(shadow);}

	  String dirBase =inRequest.getRealPath("");
	  //String fileBase = filename(srcName);
	  String fileBase  = DigestUtils.md5Hex( srcName );
	  if(resizeToHeight)
	  {
		  fileBase +="_" + _h + "h";
	  }else
	  {
		  fileBase +="_" + _w + "w";
	  }
	  
	  if(icon)
	  {
		  fileBase +="icon";
	  }
	  
	  
	  String newExtension = extension(srcName);
	  if(_shadow)
	  {
		  fileBase+="_shadowed";
		  newExtension = "png";
	  }
	  fileBase+="." +newExtension;
	  return fileBase;
  }
  
  public void processFile(String srcName,String h,String w, String shadow,Boolean icon)
  {
	  int _h = 100;
	  int _w = 100;
	  boolean _shadow = false;
	  boolean resizeToHeight = true; 
	  
	  if(h!=null){_h = Integer.parseInt(h);  }
	  if(w!=null){_w = Integer.parseInt(w); resizeToHeight=false;}
	  if(shadow!=null){ _shadow= Boolean.parseBoolean(shadow);}

	  String downloadFolder= getInitParameter("downloads");
	  String resizeDst = null;
	  String shadowDst = null;
	  
	  //String dirBase =inRequest.getRealPath("");
	  String fileBase = filename(srcName);
	  String newExtension = extension(srcName);
	  String size = h !=null ? h : w!=null ? w : "200";  
	  
	  if(resizeToHeight)
	  {
		  fileBase +="_" + _h + "h";
	  }else
	  {
		  fileBase +="_" + _w + "w";
	  }
	  
	  if(icon)
	  {
		  fileBase +="icon";
	  }
	  
	  if(icon){
		
		  resizeDst = downloadFolder + "/"  + fileBase + "." + newExtension;
		  makeIcon(srcName, resizeDst, Integer.parseInt(size));
		   
	  }else{
		  
		  if(resizeToHeight)
		  {
			   resizeDst = downloadFolder + "/"  + fileBase + "." + newExtension;
			   resizeToHeightThumbnail(srcName, resizeDst, _h);
		  }else
		  {
			  resizeDst = downloadFolder + "/"  + fileBase + "." + newExtension;
			  resizeToWidth(srcName, resizeDst, _w);
		  }
	  }

	  if(_shadow)
	  {
		  newExtension="png";
		  shadowDst = downloadFolder + "/"  + fileBase + "_shadowed" + "." + newExtension;
		  addShadow(resizeDst, shadowDst);
	  }
	  fileBase+="." +newExtension;
	  String dstFile = downloadFolder + "/"  + fileBase;

  }
  public void doPost(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws ServletException, IOException
  {
	inRequest = paramHttpServletRequest;
	ImageTypeSpecifier a=null;
    String str1 = getInitParameter("dir");
    String str2 = getInitParameter("width");
    String str3 = getInitParameter("height");
    String str4 = getInitParameter("expires");
    String str5 = getInitParameter("cache");
    String downloadFolder= getInitParameter("downloads");
    //String str6 = paramHttpServletRequest.getS
    //String str6 = paramHttpServletRequest.getQueryString();
    String str6 = paramHttpServletRequest.getParameter("src");
    String height = paramHttpServletRequest.getParameter("height");
    String width = paramHttpServletRequest.getParameter("width");
    String shadow = paramHttpServletRequest.getParameter("shadow");
    
    String iconStr = paramHttpServletRequest.getParameter("icon");
    Boolean icon = true;
    try {
		icon = Boolean.parseBoolean(iconStr);
	} catch (Exception e) {
		icon=true;
	}
    
    
    //String shadow = paramHttpServletRequest.getParameter("shadow");
    //System.out.println("local file name " + getLocalFileName(str6, height, width, shadow,icon));
    String newExtension = extension(str6);
    if(shadow!=null && shadow.equals("true"))
    {
    	newExtension="png";
    }
    //String fileName =getLocalFileName(str6, height, width, shadow);
    String fileNameAbs = downloadFolder +  getLocalFileName(str6, height, width, shadow,icon);
    //String fileNameOriCached = downloadFolder  + filename(str6) + "." + extension(str6);
    String fileNameOriCached = downloadFolder  + DigestUtils.md5Hex( str6)  + "." + extension(str6);
    
    //download first if needed 
    File localCopy  = new File(fileNameOriCached);
    if(!localCopy.exists())
    {
    	downloadFile(str6, fileNameOriCached);
    	//System.out.println("downloading file : " + str6 + "to  : " + fileNameOriCached );
    }
//    Right ho, it seems to work ! Events, Maps and really nice pictures of Ibiza : http://apps.facebook.com/ibizamedia/facebookApp.jsp?loc=102&tab=TLTGALLERY. Or open on Android, Blackberry or any other modern device : http://www.ibiza-pearls.com:8082/Pmaster/content/demos/mobileGallery/facebookApp.jsp?loc=102&tab=TLTGALLERY. http://www.facebook.com/pages/Ibiza-Media/194704527258549?sk=app_18430612163445
    File localCopyProcessed  = new File(fileNameAbs);
    Boolean process = true; //!localCopyProcessed.exists() 
    if(process)
    {
    	processFile(fileNameOriCached, height, width, shadow,icon);
    	//downloadFile(str6, fileNameOriCached);
    }
    
    if (str6 == null)
      throw new ServletException("ImageScale: could not get image");
    if (str6.length() == 0)
      throw new ServletException("ImageScale: could not get image");
    int i = getInteger(str2);
    int j = getInteger(str3);
    if (i < 0)
      throw new ServletException("ImageScale: invalid width");
    if (j < 0)
      throw new ServletException("ImageScale: invalid height");
    BufferedImage localBufferedImage1 = null;
    int k = i;
    int m = j;
    String str7 = getFile(str6, str1, str5, k, m);

    //String localName = str5 + filename(str6) + "." + extension(str6);
    //File cacheFile = new File(localName);
    File cacheFile = new File(fileNameAbs);
    if(cacheFile.exists())
    {
    	//dumpFileFromCache2(str5, str7, str4, paramHttpServletResponse);
    	dumpFileFromCache2(fileNameAbs,paramHttpServletRequest,paramHttpServletResponse);
        return;
    }
    System.gc();
    
    //String dstFileName = downloadFolder +"/" + StringHelper.getName(str7);
    //resizeToHeight(dstFileName, dstFileName, 80);
    
    
    try
    {
      if (!str7.startsWith("http://"))
        localBufferedImage1 = ImageIO.read(lookupFile(str7));
      else
        localBufferedImage1 = ImageIO.read(new URL(str7).openStream());
    }catch (Exception localException1)
    {
      localBufferedImage1 = null;
    }
    if (localBufferedImage1 == null)
    {
    	//throw new ServletException("ImageHeight: could not get image " + str7);
    	return;
    }
    int n = localBufferedImage1.getWidth();
    int i1 = localBufferedImage1.getHeight();
    double d = n / i1;
    if ((i == 0) && (j == 0))
    {
      i = n;
      j = i1;
    }
    if (i == 0)
      i = (int)(j * d);
    else if (j == 0)
    {
    	//j= -1;
      j = (int)(i / d);
    }
    Image scaledPic = null;
    
    if(i1 > n)
    {
    	scaledPic = localBufferedImage1.getScaledInstance(-1, i , Image.SCALE_SMOOTH);
    }else
    {
    	scaledPic = localBufferedImage1.getScaledInstance(i, -1, Image.SCALE_SMOOTH);
    }
    int nx= scaledPic.getWidth(null);
    int ny= scaledPic.getHeight(null);

    BufferedImage localBufferedImage2 = new BufferedImage(nx, ny, 1);
    //BufferedImage = scaledPic = picture.getScaledInstance(new_width, -1, Image.SCALE_FAST);
    Graphics2D localGraphics2D = localBufferedImage2.createGraphics();
    localGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    BlockingImageObserver localBlockingImageObserver = new BlockingImageObserver();
    if (!localGraphics2D.drawImage(localBufferedImage1, 0, 0, nx, ny, localBlockingImageObserver))
      localBlockingImageObserver.block();
    if (str4 != null)
    {
      long l = -1L;
      try
      {
        l = Long.parseLong(str4);
      }
      catch (Exception localException2)
      {
        l = -1L;
      }
      if (l > 0L)
      {
        //localObject = new Date();
       // paramHttpServletResponse.setDateHeader("Expires", ((Date)localObject).getTime() + l * 1000L);
        paramHttpServletResponse.setHeader("Cache-Control", "max-age=" + l);
      }
    }
    paramHttpServletResponse.setContentType("image/jpeg");
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(localBufferedImage2, "jpeg", localByteArrayOutputStream);
    localByteArrayOutputStream.flush();
    byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
    paramHttpServletResponse.setContentLength(arrayOfByte.length);
    Object localObject = paramHttpServletResponse.getOutputStream();
    
    ((OutputStream)localObject).write(arrayOfByte);
    if (str5 != null)
    {
      String str8 = getCachedFile(str6, str1, k, m);
      String str9 = str5;
      if ((!str9.endsWith("/")) && (!str9.endsWith("\\")))
        str9 = str9 + "/";
      str9 = str9 + str8;
      File localFile = lookupFile(str9);
      try
      {
        FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
        localFileOutputStream.write(arrayOfByte);
        localFileOutputStream.close();
      }
      catch (Exception localException3)
      {
      }
      //System.out.println("output file : " + str9);
    }
    ((OutputStream)localObject).close();
  }

  private File lookupFile(String paramString)
  {
    File localFile = new File(paramString);
    return localFile.isAbsolute() ? localFile : new File(this.context.getRealPath("/"), paramString);
  }

	public String extension(String fullPath)
	{
	    int dot = fullPath.lastIndexOf(".");
	    return fullPath.substring(dot + 1);
	}

	  public String filename(String fullPath)
	  {
		  //http://www.ibiza-pearls.com/files/site/locations/SalsaEulalia/SalsaEulalia.jpg
	    int dot = fullPath.lastIndexOf(".");
	    int sep = fullPath.lastIndexOf("/");
	    int length = fullPath.length();
	    
	    String result = null ;
	    
	    try {
	    	result =fullPath.substring(sep + 1, dot);	
		} catch (Exception e) {
			result = DigestUtils.md5Hex( fullPath );
		}
	    return result;
	  }

	  public String path(String fullPath)
	  {
	    int sep = fullPath.lastIndexOf("/");
	    return fullPath.substring(0, sep);
	  }
  private String getCachedFile(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    String str = "";
    if (paramString1.startsWith("http:"))
    {
      str = str + paramString1;
    }
    else
    {
      if ((paramString2 != null) && (paramString2.length() > 0))
      {
        str = str + paramString2;
        if ((!str.endsWith("/")) && (!str.endsWith("\\")))
          str = str + "/";
      }
      str = str + paramString1;
    }
    /*
    str = str + "x" + paramInt1;
    str = str + "x" + paramInt2;
    */
    String result = filename(str) + "." + extension(str);
    return result;
  }

  private String getFile(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2)
  {
    if (paramString3 != null)
    {
      String str1 = getCachedFile(paramString1, paramString2, paramInt1, paramInt2);
      String str2 = paramString3;
      if ((!str2.endsWith("/")) && (!str2.endsWith("\\")))
        str2 = str2 + "/";
      str2 = str2 + str1;
      File localFile = lookupFile(str2);
      if ((localFile != null) && (localFile.isFile()) && (localFile.canRead()))
        return str1;
    }
    if (paramString1.startsWith("http:"))
      return paramString1;
    if (paramString2 == null)
      return paramString1;
    if (paramString2.length() == 0)
      return paramString1;
    String str1 = paramString2;
    if ((!str1.endsWith("/")) && (!str1.endsWith("\\")))
      str1 = str1 + "/";
    return str1 + paramString1;
  }

  private int getInteger(String paramString)
  {
    int i = 0;
    if (paramString == null)
      return i;
    try
    {
      i = Integer.parseInt(paramString);
    }
    catch (Exception localException)
    {
      i = -1;
    }
    return i;
  }
  
  private void dumpFileFromCache2(String fileName,HttpServletRequest request,HttpServletResponse res)
  throws IOException
{
  
	  
	 // res.setHeader("Cache-Control", "max-age=" + 0);
	//  res.setContentType("image/jpeg");
	  File localFile = lookupFile(fileName);
	  
	  //System.out.println("sending : " + fileName);
	  SerializerUtils.sendGZiped(localFile, request, res);
	  
	  /*
	  res.setContentLength((int)localFile.length());
	  Object localObject = res.getOutputStream();
	  Util.dumpFile(localFile, (OutputStream)localObject);
	  ((OutputStream)localObject).close();
	  */
	  /*
	  File localFile = lookupFile(fileName);
	  
	  OutputStream out = null;

	    // Select the appropriate content encoding based on the
	    // client's Accept-Encoding header. Choose GZIP if the header 
	    // includes "gzip". Choose ZIP if the header includes "compress". 
	    // Choose no compression otherwise.
	    String encodings = request.getHeader("Accept-Encoding");
	    if (encodings != null && encodings.indexOf("gzip") != -1) {
	      // Go with GZIP
	      res.setHeader("Content-Encoding", "gzip");
	      out = new GZIPOutputStream(res.getOutputStream());
	    }
	    else if (encodings != null && encodings.indexOf("compress") != -1) {
	      // Go with ZIP
	      res.setHeader("Content-Encoding", "x-compress");
	      out = new ZipOutputStream(res.getOutputStream());
	      ((ZipOutputStream)out).putNextEntry(new ZipEntry("dummy name"));
	    }
	    else {
	      // No compression
	      out = res.getOutputStream();
	    }
	    res.setHeader("Vary", "Accept-Encoding");

	    // Get the file to view
	    String file = request.getPathTranslated();

	    // No file, nothing to view
	    if (file == null) {
	      res.sendError(res.SC_FORBIDDEN);
	      return;
	    }

	    // Get and set the type of the file
	    String contentType = getServletContext().getMimeType(file);
	    res.setContentType(contentType);

	    // Return the file
	    try {
	      ServletUtils.returnFile(localFile, out);
	    }
	    catch (FileNotFoundException e) { 
	      res.sendError(res.SC_NOT_FOUND);
	      return;
	    }
	    catch (IOException e) {
	      getServletContext().log(e, "Problem sending file");
	      res.sendError(res.SC_INTERNAL_SERVER_ERROR,
	                    ServletUtils.getStackTraceAsString(e));
	    }

	    // Write the compression trailer and close the output stream
	    out.close();
	    */
  
}

  private void dumpFileFromCache(String paramString1, String paramString2, String paramString3, HttpServletResponse paramHttpServletResponse)
    throws IOException
  {
    if (paramString3 != null)
    {
      long l = -1L;
      try
      {
        l = Long.parseLong(paramString3);
      }
      catch (Exception localException)
      {
        l = -1L;
      }
      if (l > 0L)
      {
        //localObject = new Date();
        //paramHttpServletResponse.setDateHeader("Expires", ((Date)localObject).getTime() + l * 1000L);
        paramHttpServletResponse.setHeader("Cache-Control", "max-age=315360000");
      }
    }
    paramHttpServletResponse.setContentType("image/jpeg");
    String str = paramString1;
    if ((!str.endsWith("/")) && (!str.endsWith("\\")))
      str = str + "/";
    str = str + paramString2;
    File localFile = lookupFile(str);
    paramHttpServletResponse.setContentLength((int)localFile.length());
    Object localObject = paramHttpServletResponse.getOutputStream();
    Util.dumpFile(localFile, (OutputStream)localObject);
    ((OutputStream)localObject).close();
  }
  
}