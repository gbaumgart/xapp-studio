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


import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import com.sun.org.apache.xpath.internal.operations.Bool;


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
	
	
}
