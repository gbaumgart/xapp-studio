package pmedia.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbill.DNS.Address;




/**
 * This class is used to manipulate strings
 */
public class StringUtils
{
	
    //~ Static fields/initializers ---------------------------------------------
	public static String deparameterize(String uri) 
	{
	      int i = uri.lastIndexOf('?');
	      if (i == -1) {
	          return uri;
	      }

	      
	      String[] params = uri.substring(i + 1).split("&");
	      for (int j = 0; j < params.length; j++) {
	          String p = params[j];
	          int k = p.indexOf('=');
	          if (k == -1) {
	              break;
	          }
	          String name = p.substring(0, k);
	          String value = p.substring(k + 1);
	      }

	      return uri.substring(0, i);
	  }
	public static String sStringToHMACMD5(String s, String keyString)
    {
        String sEncodedString = null;
        try
        {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(key);

            byte[] bytes = mac.doFinal(s.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();

            for (int i=0; i<bytes.length; i++) {
                String hex = Integer.toHexString(0xFF &  bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            sEncodedString = hash.toString();
        }
        catch (UnsupportedEncodingException e) {
        	
        }
        catch(InvalidKeyException e){
        	
        }
        catch (NoSuchAlgorithmException e) {
        	
        }
        return sEncodedString ;
    }


    private static final String PROPERTY_START_TOKEN = "${";
    private static final String PROPERTY_END_TOKEN = "}";
    public static void writeToFile(String content,String fileName) throws IOException  
    {
    	if(content==null){
    		System.out.println("tried to save null content to " + fileName);
    		return;
    	}
    	
    	if(content.length()==0){
    		System.out.println("tried to save empty content to " + fileName);
    		return;
    	}
    	if(content.equals("null")){
    		System.out.println("tried to save empty content to " + fileName);
    		return;
    	}
        //log("Writing to file named " + fFileName + ". Encoding: " + fEncoding);
        Writer out = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
        try {
          out.write(content);
        }
        finally {
          out.close();
        }

    }
    public static String readFileAsString(String filePath) throws java.io.IOException {
    	int len;
        char[] chr = new char[4096];
        final StringBuffer buffer = new StringBuffer();
        final FileReader reader = new FileReader(filePath);
        try {
            while ((len = reader.read(chr)) > 0) {
                buffer.append(chr, 0, len);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }
    public static String encode(String input) {
        StringBuilder resultStr = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (isUnsafe(ch)) {
                resultStr.append('%');
                resultStr.append(toHex(ch / 16));
                resultStr.append(toHex(ch % 16));
            } else {
                resultStr.append(ch);
            }
        }
        return resultStr.toString();
    }

    private static char toHex(int ch) {
        return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
    }

    private static boolean isUnsafe(char ch) {
        if (ch > 128 || ch < 0)
            return true;
        return " %$&+,/:;=?@<>#%".indexOf(ch) >= 0;
    }

    
    public static String extension(String fullPath)
	{
    	
    	if(fullPath.lastIndexOf(".") >0)
    	{
		    int dot = fullPath.lastIndexOf(".");
		    return fullPath.substring(dot + 1);
    	}else
    	{
    		return "";
    	}
	}

	  public static String filename(String fullPath)
	  {
		 if(fullPath!=null && fullPath.length() > 0){
			 
			 
			 if(fullPath.contains(".") && fullPath.contains("/"))
			 {
				 int dot = fullPath.lastIndexOf(".");
				 int sep = fullPath.lastIndexOf("/");
				 //System.out.println("testing " + fullPath + " dot : " + dot + " sep : " + sep + " l : " + fullPath.length());
				 
				 if(dot >0 && sep >0 && dot < fullPath.length() && (sep+1) < fullPath.length())
				 {
					 try {
						 return fullPath.substring(sep + 1, dot);
					} catch (Exception e) 
					{
						System.out.println("testing " + fullPath);
						e.printStackTrace();
						//System.out.println("testing " + fullPath);
					}
				    
				 }else
				 {
					 return "";
				 }
			 }else{
				 return fullPath;
			 }
			 
		 }else
		 {
			 return "";
		 }
		 return "";
	  }
	  
	  public static String filenameComplete(String fullPath)
	  {
		  String ext = extension(fullPath);
		  String result =  filename(fullPath);
		  if(result==null){
			  result = "";
		  }
		  if(ext.length() > 0)
		  {
			  result +="." +ext; 
		  }
		  return result;
		  //return filename(fullPath) + "." + extension(fullPath);
	  }

    public static String removeHTML2(String input)
    {
    	if(input==null || input.length()==0)
			return null;
    	
		Whitelist clean = Whitelist.simpleText();
    	Cleaner cleaner = new Cleaner(clean);
		Document doc = Jsoup.parse(input);
		//return doc.body().html();
		String result = cleaner.clean(doc).body().html();
		result = result.replaceAll("(?s)\\[.*?\\]", "");
		result = result.replaceAll("(?s)\\{.*?\\}", "");
		return result;
    }
    
    public static Locale getLocaleFromLang(String lang)
    {
    	if(lang.equals("en"))
    	{
    		return Locale.ENGLISH;
    	}
    	if(lang.equals("de"))
    	{
    		return Locale.GERMANY;
    	}
    	if(lang.equals("es"))
    	{
    		return new Locale("es", "ES");
    	}
    	if(lang.equals("fr"))
    	{
    		return Locale.FRANCE;
    	}
    	if(lang.equals("it"))
    	{
    		return Locale.ITALY;
    	}
    	return Locale.ENGLISH;
    }
    
    public static String removeSpecialCharacters(String input)
    {
    	if(input==null || input.length()==0)
			return null;
    	
    	String pattern = "[^a-z]";
        
        return  input.replaceAll(pattern, " ");
    	
    }
    public static String getFirstPicture(String input){


    	String result=null;
    		
    		if(input!=null && input.length() > 0)
    		{
    			Document doc = Jsoup.parse(input);
    			Elements pngs = doc.select("img");
    			for (org.jsoup.nodes.Element img : pngs)
    			{
    			        String url = img.attr("src");
    			        if(url.length() > 0 )
    			        {
    			        	result = url;
    			        	break;
    			        }
    			}
    		}
    		return result;
    }
    public static String convertLinks(String input)
    {
    	if(input==null || input.length()==0)
			return null;
    	
		//Whitelist clean = Whitelist.relaxedNoPictures().addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong");
    	//Whitelist clean = new Whitelist();//relaxed().addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong","tt");
    	//clean.addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong");
    	//clean.addProtocols("a", "href","ftp","http","https","mtpp");
		//Cleaner cleaner = new Cleaner(clean);
		Document doc = Jsoup.parse(input);
		Elements links = doc.select("a");
		for (org.jsoup.nodes.Element link : links)
		{
		        String url = link.attr("href");
		        {
		        	url = url.replace("http://","tt://");
		        	org.jsoup.nodes.Element newLink= link.before("<div class=\"articleLink\" " +
		        			"onClick=\"ctx.getUrlHandler().openInternalUrl(\'" + url + "\',null);\" >"  +link.html() + "</div>");
		        	//newLink.attr("onClick", "openInternalUrl(\'" + url + "\',null)");
		        	//link.after("</div>");
		        	
		        	//link.remove();
		        	/*
		        	url = url.replace("http://","tt://");
		        	link.attr("onClick", "openInternalUrl(\'" + url + "\',null)");
		        	link.attr("href","#");
		        	*/
		        	//link.removeAttr("href");
		        }
		 }
		links.remove();
		//doc.select("a").attr("rel", "nofollow");
		//String linkBefore=
		//doc.select("a").attr("onClick", "nothing");
		return doc.body().html();
		//return cleaner.clean(doc).body().html();
    }
    public static String removePictures(String input)
    {
    	if(input==null || input.length()==0)
			return null;
    	
		Whitelist clean = Whitelist.relaxedNoPictures().addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong");
		
    	//Whitelist clean = new Whitelist();//relaxed().addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong","tt");
    	//clean.addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong");
    	//clean.addProtocols("a", "href","ftp","http","https","mtpp");
		Cleaner cleaner = new Cleaner(clean);
		Document doc = Jsoup.parse(input);
		return cleaner.clean(doc).body().html();
    }
    public static String htmlMobileCleanup(String input)
    {
    	if(input==null || input.length()==0)
			return null;
    	
		Whitelist clean = Whitelist.relaxed().addTags("blockquote", "cite", "code", "p", "q", "s","img","src","strike","br","a","strong");
    	//Whitelist clean = new Whitelist();//relaxed().addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong","tt");
    	//clean.addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong");
    	//clean.addProtocols("a", "href","ftp","http","https","mtpp");
		Cleaner cleaner = new Cleaner(clean);
		Document doc = Jsoup.parse(input);
		return cleaner.clean(doc).body().html();
    }
    public static boolean getBoolFromString(String value)
    {
    	boolean result = false;
    	if(value!=null){
    		try {
				result = Boolean.parseBoolean(value);
				return result;
			} catch (Exception e) {

			}
    		
    		int valueInt = 0;
    		try {
    			valueInt = Integer.parseInt(value);
    			return valueInt>1 ? true :false;
			} catch (Exception e) {
				// TODO: handle exception
			}
    		
    	}
    	
    	return result;
    }
    public static String getXMLFromElement(Element element,String identifier)
    {
    	NodeList hostElmntLst = element.getElementsByTagName(identifier);
        if( hostElmntLst != null)
        {
        	Element hostElmnt = (Element) hostElmntLst.item(0);
        	if(hostElmnt !=null)
        	{
        		NodeList host = hostElmnt.getChildNodes();
        		if(host !=null && host.item(0) !=null )
        		{
        			return ((Node)host.item(0)).getNodeValue();
        			//System.out.println("host  : " + e.host);
	        	}
        	}
        }
        return null;
    }
    
    public static Boolean setXMLFromElement(Element element,String identifier,String content)
    {
    	NodeList hostElmntLst = element.getElementsByTagName(identifier);
        if( hostElmntLst != null)
        {
        	Element hostElmnt = (Element) hostElmntLst.item(0);
        	if(hostElmnt !=null)
        	{
        		NodeList host = hostElmnt.getChildNodes();
        		if(host !=null && host.item(0) !=null )
        		{
        			((Node)host.item(0)).setNodeValue(content);
        			return true;
	        	}
        	}
        }
        return false;
    }
    
    public static float getFromXMLElement(Element element,String identifier)
    {
    	NodeList hostElmntLst = element.getElementsByTagName(identifier);
        if( hostElmntLst != null)
        {
        	Element hostElmnt = (Element) hostElmntLst.item(0);
        	if(hostElmnt !=null)
        	{
        		NodeList host = hostElmnt.getChildNodes();
        		if(host !=null && host.item(0) !=null )
        		{
        			String e = ((Node)host.item(0)).getNodeValue();
        			float res=-1;
        			try {
        				res = Float.valueOf(e);
					} catch (Exception e2) {
					}
        			return res; 
        			//System.out.println("host  : " + e.host);
	        	}
        	}
        }
        return -1;
    }
    
    public static int getFromXMLElementInteger(Element element,String identifier)
    {
    	NodeList hostElmntLst = element.getElementsByTagName(identifier);
        if( hostElmntLst != null)
        {
        	Element hostElmnt = (Element) hostElmntLst.item(0);
        	if(hostElmnt !=null)
        	{
        		NodeList host = hostElmnt.getChildNodes();
        		if(host !=null && host.item(0) !=null )
        		{
        			String e = ((Node)host.item(0)).getNodeValue();
        			int res=-1;
        			try {
        				res = Integer.valueOf(e);
					} catch (Exception e2) {
					}
        			return res; 
        			//System.out.println("host  : " + e.host);
	        	}
        	}
        }
        return -1;
    }
    
    
    public static Integer getIntegerFromXMLAttribute(Element element,String identifier)
    {
    	int _refId=-1;
    	try {
    		_refId = Integer.parseInt(element.getAttribute(identifier));
		} catch (Exception e) 
		{
			_refId=-1;
		}
		return _refId;
    }
    public static Integer getIntegerFromXMLElement(Element element,String identifier)
    {
    	NodeList hostElmntLst = element.getElementsByTagName(identifier);
        if( hostElmntLst != null)
        {
        	Element hostElmnt = (Element) hostElmntLst.item(0);
        	if(hostElmnt !=null)
        	{
        		NodeList host = hostElmnt.getChildNodes();
        		if(host !=null && host.item(0) !=null )
        		{
        			String res = ((Node)host.item(0)).getNodeValue();
        			if(res!=null)
        			{
        				int resI = -1;
        				try {
							resI = Integer.parseInt(res);
							return resI;
						} catch (Exception e) {
							
							return -1;
						}
        			}
	        	}
        	}
        }
        return -1;
    }
    public static Boolean getBooleanFromXMLAttribute(Element element,String identifier)
    {
    	
		String res = element.getAttribute(identifier);
		if(res!=null)
		{
			Boolean resI = false;
			try {
				resI = Boolean.parseBoolean(res);
				return resI;
			} catch (Exception e) 
			{
				
				return false;
			}
		}
        return false;
    }
    public static Boolean getBooleanFromXMLElement(Element element,String identifier)
    {
    	NodeList hostElmntLst = element.getElementsByTagName(identifier);
        if( hostElmntLst != null)
        {
        	Element hostElmnt = (Element) hostElmntLst.item(0);
        	if(hostElmnt !=null)
        	{
        		NodeList host = hostElmnt.getChildNodes();
        		if(host !=null && host.item(0) !=null )
        		{
        			String res = ((Node)host.item(0)).getNodeValue();
        			if(res!=null)
        			{
        				Boolean resI = false;
        				try {
							resI = Boolean.parseBoolean(res);
							return resI;
						} catch (Exception e) 
						{
							
							return false;
						}
        			}
	        	}
        	}
        }
        return false;
    }
    public static String path(String fullPath) 
    {
    	if(fullPath==null)
    		return "";
        int sep = fullPath.lastIndexOf("/");
        return fullPath.substring(0, sep);
      }
    public static String getDomain(String url)
    {
    	String result = null;
    	try {
    		URI uri = new URI(url);
    		result = uri.getHost();
    		if(result!=null)
    		{
    			return result;
    		}
    		} catch (URISyntaxException e) 
    		{
    			result=null;
    		}
    	
    	//no domain 
    	if(result==null)
    	{
    		URL _url = null;
    		try {
    			_url = new URL(url);
    		} catch (MalformedURLException e) 
    		{
    			// TODO Auto-generated catch block
    			//e.printStackTrace();
    			return url;
    		}
    		InetAddress addr =null;
    		
    		try {
				addr = Address.getByName(_url.getHost());
			} catch (UnknownHostException e) {
				e.printStackTrace();
				
			}
    		addr.getHostName();
    		if(addr!=null){
    			result = new String (addr.getHostAddress());
    		}
    	}
    	
    	return result;
    }
    public static String getBaseUrl( HttpServletRequest request ) 
    {
        if ( ( request.getServerPort() == 80 ) ||
             ( request.getServerPort() == 443 ) )
          return request.getScheme() + "://" +
                 request.getServerName() +
                 request.getContextPath();
        else
          return request.getScheme() + "://" +
                 request.getServerName() + ":" + request.getServerPort() +
                 request.getContextPath();
      }
    public static String getRelativeUrl(
    	    HttpServletRequest request ) {

    	    String baseUrl = null;

    	    if ( ( request.getServerPort() == 80 ) ||
    	         ( request.getServerPort() == 443 ) )
    	      baseUrl =
    	        request.getScheme() + "://" +
    	        request.getServerName() +
    	        request.getContextPath();
    	    else
    	      baseUrl =
    	        request.getScheme() + "://" +
    	        request.getServerName() + ":" + request.getServerPort() +
    	        request.getContextPath();

    	    StringBuffer buf = request.getRequestURL();

    	    if ( request.getQueryString() != null ) {
    	      buf.append( "?" );
    	      buf.append( request.getQueryString() );
    	    }

    	    return buf.substring( baseUrl.length() );
    	  }

    /**
     * Returns the file specified by <tt>path</tt> as returned by
     * <tt>ServletContext.getRealPath()</tt>.
     */
    public static File getRealFile(
      HttpServletRequest request,
      String path ) {

      return
        new File( request.getSession().getServletContext().getRealPath( path ) );
    }
    //~ Methods ----------------------------------------------------------------

    public static String removeHTML( String original )
    {
    	return Jsoup.parse(original).text();
    }
    
    public static String capitalize( String original )
    {
        if ( StringUtils.nullOrEmpty( original ) )
        {
            return original;
        }

        return original.substring( 0, 1 ).toUpperCase(  ) + original.substring( 1 );
    }

    /**
     * Performs a string replace
     */
    public static String replace( String original,
                                  String find,
                                  String substitute )
    {
        String result = original;
        int    location = original.indexOf( find );

        while ( location >= 0 )
        {
            result   = result.substring( 0, location ) + substitute + result.substring( location + find.length(  ) );
            location = result.indexOf( find, location + find.length(  ) );
        }

        return result;
    }

    /**
     * Returns <code>true</code> if the string is null or zero length
     */
    public static boolean nullOrEmpty( String value )
    {
        return ( value != null )
               ? ( value.length(  ) == 0 )
               : true;
    }

    /**
         * Returns <code>true</code> if the string "str" is contained in string "in".
     * null does not contain null and an empty string does not contain an empty string.
     * no string contains an empty string
     */
    public static boolean containsString( String in,
                                          String str )
    {
        return ( ( ( str == null ) || ( in == null ) || ( in.length(  ) == 0 ) || ( str.length(  ) == 0 ) )
                 ? false
                 : ( in.indexOf( str ) >= 0 ) );
    }

    /**
     * Returns the strings separated by the separator string.  If the initial
     * string is empty then the separator is not used.
     */
    public static String concat( String str1,
                                 String str2,
                                 char   concatenator )
    {
        if ( nullOrEmpty( str1 ) )
        {
            return str2;
        }
        else if ( nullOrEmpty( str2 ) )
        {
            return str1;
        }
        else
        {
            return ( str1 + concatenator + str2 );
        }
    }

    /**
     * Strips spaces from a string, including internal spaces.
     *
     * @param str string to strip
     * @return string with no spaces
     */
    public static String trim( String str )
    {
        return trim( str, true, false );
    }

    /**
     * Strips spaces from a string, including internal spaces.
     *
     * @param str string to strip
     * @return string with no spaces
     */
    public static String trim( String  str,
                               boolean trimSpaces,
                               boolean trimNonAlpha )
    {
        if ( ( str == null ) || str.equals( "" ) )
        {
            return "";
        }

        int          length = str.length(  );
        StringBuffer sb = new StringBuffer( length );

        for ( int i = 0; i < length; i++ )
        {
            char c = str.charAt( i );

            if ( ( trimSpaces && Character.isSpaceChar( c ) ) || ( trimNonAlpha && !Character.isLetterOrDigit( c ) ) )
            {
                continue;
            }

            sb.append( c );
        }

        String key = sb.toString(  );

        return key.trim(  );
    }

    public static boolean getBoolean( String  value,
                                      boolean defaultValue )
    {
        try
        {
            return Boolean.valueOf( value ).booleanValue(  );
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    public static int getInt( String value,
                              int    defaultValue )
    {
        try
        {
            return Integer.valueOf( value ).intValue(  );
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    public static float getFloat( String value,
                                  float  defaultValue )
    {
        try
        {
            return Float.valueOf( value ).floatValue(  );
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    public static double getDouble( String value,
                                    double defaultValue )
    {
        try
        {
            return Double.valueOf( value ).doubleValue(  );
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    private static boolean replaceProperty( Properties   properties,
                                            String       value,
                                            StringBuffer buffer,
                                            int          startTokenIndex,
                                            String       startToken,
                                            String       propertyEndToken )
    {
        int endTokenIndex = value.indexOf( propertyEndToken );
        if ( endTokenIndex < 0 )
        {
            return false;
        }

        String propertyName = value.substring( startTokenIndex + startToken.length(  ), endTokenIndex );
        String propertyValue = properties.getProperty( propertyName );
        if ( propertyValue == null )
        {
            propertyValue = "";
        }

        buffer.replace( startTokenIndex, endTokenIndex + 1, propertyValue );
        return true;
    }

    public static String resolveProperties( Properties properties,
                                            String     value,
                                            String     startToken,
                                            String     endToken )
    {
        if ( nullOrEmpty( value ) || nullOrEmpty( startToken ) || nullOrEmpty( endToken ) )
        {
            return value;
        }

        StringBuffer buffer = new StringBuffer( value );
        while ( true )
        {
            int startTokenIndex = value.indexOf( startToken );
            if ( startTokenIndex < 0 )
            {
                break;
            }

            if ( !replaceProperty( properties, value, buffer, startTokenIndex, startToken, endToken ) )
            {
                break;
            }

            value = buffer.toString(  );
        }

        return buffer.toString(  );
    }

    public static String resolveProperties( String value )
    {
        return resolveProperties( System.getProperties(  ), value );
    }

    public static String resolveProperties( Properties properties,
                                            String     value )
    {
        return resolveProperties( properties, value, PROPERTY_START_TOKEN, PROPERTY_END_TOKEN );
    }

    public static void resolveProperties( Properties props )
    {
        Enumeration keys = props.keys(  );
        if ( keys == null )
        {
            return;
        }

        while ( keys.hasMoreElements(  ) )
        {
            String key = ( String ) keys.nextElement(  );
            String value = props.getProperty( key );
            props.setProperty( key, StringUtils.resolveProperties( props, value ) );
        }
    }

    public static void loadProperties()
    {
    	String PROPERTIES_RESOURCE = "/config/server.application.properties";
 	   	String t = System.getProperty("webapp.root");
 	   	t +=PROPERTIES_RESOURCE;

 	   	File propFile = new File(t);
 	   	if(!propFile.exists()){
	 	   	PROPERTIES_RESOURCE = "/config/application.properties.codigonexo";
	 	   	t = System.getProperty("webapp.root");
	 	   	t +=PROPERTIES_RESOURCE;
	 	   	propFile = new File(t);	
 	   	}
 	   	
 	   	
 	   if(!propFile.exists()){
	 	   	PROPERTIES_RESOURCE = "/config/application.properties";
	 	   	t = System.getProperty("webapp.root");
	 	   	t +=PROPERTIES_RESOURCE;
	 	   	propFile = new File(t);	
	   	}
 	   
 	   System.out.println("## loading configuration from " + propFile.getAbsolutePath());
 	   	
 	   	Properties props = null;
        try {
            FileInputStream fis = new FileInputStream(propFile);
            props= new Properties();
            props.load(fis);

            Enumeration keys = props.keys(  );
            if ( keys == null )
            {
                return;
            }

            while ( keys.hasMoreElements(  ) )
            {
                String key = ( String ) keys.nextElement(  );
                String value = props.getProperty( key );
                //System.out.println( "key : " + key + " value : " +value);
                System.setProperty(key,value);

                //props.setProperty( key, StringUtils.resolveProperties( props, value ) );
            }

            //System.setProperties(propertyList);

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }




    }

    public static String[] toArray(String source, String delimiter)
    {
        List results = new ArrayList<String>();
        if (source != null) {
	        StringTokenizer tokenizer = new StringTokenizer(source, delimiter);
	        while (tokenizer.hasMoreTokens()) {
	            String token = tokenizer.nextToken();
	            results.add(token.trim());
	        }
        }

        return (String[])results.toArray(new String[0]);
    }


}
