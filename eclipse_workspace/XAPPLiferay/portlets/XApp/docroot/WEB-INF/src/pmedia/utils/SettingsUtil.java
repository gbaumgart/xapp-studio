
package pmedia.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class SettingsUtil 
{
	

	public static ArrayList<String>getTicketFiles(String domain)
	{
		ArrayList<String>result = new ArrayList<String>();
		Properties dbProperties=null;
		String PROPERTIES_RESOURCE = "application.properties";

		String t = System.getProperty("webapp.root");
		t +=PROPERTIES_RESOURCE;
	  	File propFile = new File(t);
	  	try {
		  FileInputStream fis = new FileInputStream(propFile);
		  dbProperties = new Properties();
		  dbProperties.load(fis);
		  if(fis!=null)
		  {
			  fis.close();
		  }
		}
	  	catch (Exception e) 
	  	{
			e.printStackTrace();
		}
	  	
	  	/**
	  	 * parse properties
	  	 */
	  	
	  	String pathSuffix = SettingsUtil.getProperty("webapp.root");
		if( domain !=null && domain.length() > 0)
		{
			pathSuffix = pathSuffix + "db/" + domain + "/";
		}
		
	  	if(dbProperties == null)
	  		return null;
	  	
	  	String searchString = domain + ".tickets";
	  	Enumeration names = dbProperties.keys();
        while (names.hasMoreElements()) 
        {
            String key = (String)names.nextElement();
            String value = 	(String) dbProperties.get(key);
            String file = "";
            if(key.contains(searchString))
            {
            	String splitted[] = StringHelper.split(key, ".");
            	//String[] splitted = key.split(".");
            	if(splitted!=null && splitted.length > 1)
            	{
            		file = pathSuffix +  splitted[1] + ".xml"; 
            		result.add(file);
            		//System.out.println(file);
            	}
            	
            	
            }
        }
	  	//dbProperties.elements()
	  	return result;
		
	}
	
	public static String getProperty(String name)
	{
		Properties dbProperties;
		String PROPERTIES_RESOURCE = "config/application.properties";

		String t = System.getProperty("webapp.root");
		t +=PROPERTIES_RESOURCE;
	  	File propFile = new File(t);
	  	try {
		  FileInputStream fis = new FileInputStream(propFile);
		  dbProperties = new Properties();
		  dbProperties.load(fis);
		  if(fis!=null)
		  {
			  fis.close();
		  }
		}
	  	catch (Exception e) {
			e.printStackTrace();
		}
	  	return System.getProperty(name);
	}
    
}
