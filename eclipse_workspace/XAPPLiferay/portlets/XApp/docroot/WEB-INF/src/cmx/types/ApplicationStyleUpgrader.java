package cmx.types;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.xml.sax.SAXException;

import com.liferay.portal.kernel.management.jmx.GetAttributesAction;
import com.liferay.portal.kernel.util.DocumentConversionUtil;

import pmedia.DataUtils.ApplicationConfigurationTools;
import pmedia.types.ApplicationConfiguration;
import pmedia.types.ApplicationMetaData;
import pmedia.types.ApplicationMetaDataKeys;
import pmedia.utils.CITools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import cmq.manager.ApplicationTemplateManager;
import cmq.types.ApplicationTemplate;
import cmx.action.StyleManagerAction;
import cmx.tools.CIFactory;
import cmx.tools.Crypto;
import cmx.tools.DBConnectionChecker;
import cmx.tools.LiferayContentTools;
import cmx.tools.LiferayDataSourceUtil;
import cmx.tools.ResourceUtil;
import cmx.tools.StyleTreeFactory;
import cmx.tools.TrackingUtils;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
public class  ApplicationStyleUpgrader extends ApplicationManager{
	
	public void copyToApplicationStyles(String uuid,String appId, String root,String template,String platform)
	{
		String dstFolder  = TemplateManager.getUserStyleRootFolder(uuid, appId, platform);
		
		dstFolder=dstFolder+ "/"+template;
		
		File dstFolderO = new File(dstFolder);
		if(dstFolderO.exists()){
			return;
		}
		
		String srcFolder=root + "/" +template;
		File srcFolderO = new File(srcFolder);
		if(!srcFolderO.exists()){
			return;
		}
		
		try {
			FileUtils.copyDirectory(srcFolderO, dstFolderO, new FileFilter() 
			{
				  public boolean accept(File pathname) 
				  {
				       if(pathname.getAbsolutePath().contains("svn"))
				       {
				    	   return false;
				       }
			        return true;
			    }
			}, true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void upgradeDefaultApplicationStylesheet(Application app, String srcFilePath,String dstFilePath,String version,Boolean force)
	{
		String defaultContent = null;
		File srcFile =new File(srcFilePath);
		if(!srcFile.exists()){
			return;
		}
		File dstFile  =  new  File(dstFilePath);
		if(!dstFile.exists()){
			return;
		}
		
		
		try {
			defaultContent = StringUtils.readFileAsString(srcFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		if(defaultContent==null){
			return;
		}
		
		/***
		 * Now check the version of the source file
		 */
		if(!defaultContent.contains(version)){
			return;
		}
		
		/***
		 * User Template File
		 */
		String defaultUserContent = null;
		try {
			defaultUserContent = StringUtils.readFileAsString(dstFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		if(defaultUserContent==null){
			return;
		}
		/***
		 * Now check the version
		 */
		if(defaultUserContent.contains(version)){
			return;
		}
		
		/***
		 * Final copy
		 */
		try {
			FileUtils.copyFile(new File(srcFilePath), new File(dstFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void upgradeDefaultApplicationStyle(Application app, String uuid,String appId, String root,String version,String platform)
	{
		String dstFolder  = TemplateManager.getUserStyleRootFolder(uuid, appId, platform);
		
		dstFolder=dstFolder+ "/";

		/***
		 * Source Template File
		 */
		String defaultFile = root + "/Default/Default.css";
		String defaultUserFile = dstFolder + "/Default/Default.css";
		upgradeDefaultApplicationStylesheet(app, defaultFile, defaultUserFile, version,false);
		
		
		defaultFile = root + "/ListItems.css";
		defaultUserFile = dstFolder + "/ListItems.css";
		upgradeDefaultApplicationStylesheet(app, defaultFile, defaultUserFile, version,false);
		
		defaultFile = root + "/Content.css";
		defaultUserFile = dstFolder + "/Content.css";
		upgradeDefaultApplicationStylesheet(app, defaultFile, defaultUserFile, version,false);
		
		defaultFile = root + "/CustomFields.css";
		defaultUserFile = dstFolder + "/CustomFields.css";
		upgradeDefaultApplicationStylesheet(app, defaultFile, defaultUserFile, version,false);
		
		defaultFile = root + "/Views.css";
		defaultUserFile = dstFolder + "/Views.css";
		upgradeDefaultApplicationStylesheet(app, defaultFile, defaultUserFile, version,false);
		
	}
	public void upgradeApplicationStyles(Application app, String uuid,String appId, String root,String version,String template,String platform)
	{
		String dstFolder  = TemplateManager.getUserStyleRootFolder(uuid, appId, platform);
		
		dstFolder=dstFolder+ "/";

		/***
		 * Source Template File
		 */
		String defaultFile = root + template+ "/ListItems.css";
		String defaultUserFile = dstFolder +  template + "/ListItems.css";
		upgradeDefaultApplicationStylesheet(app, defaultFile, defaultUserFile, version,false);
		
		defaultFile = root + template+ "/Content.css";
		defaultUserFile = dstFolder +  template + "/Content.css";
		
		defaultFile = root + template+ "/CustomFields.css";
		defaultUserFile = dstFolder +  template + "/CustomFields.css";
		upgradeDefaultApplicationStylesheet(app, defaultFile, defaultUserFile, version,false);
		
		
		defaultFile = root + template+ "/Views.css";
		defaultUserFile = dstFolder +  template + "/Views.css";
		upgradeDefaultApplicationStylesheet(app, defaultFile, defaultUserFile, version,false);
		
		
		
		
	}
	public void UpgradeApplication(Application app)
	{
		//app.upgradeApplication(this);
		//saveApplication(app);
		
		String appId = app.getApplicationIdentifier();
		String uuid = app.getUserIdentifier();
		
		String templateSource =null;
		String templateDst=null;
		
		String templateName=null;
		if(appId.contains("qxgeneral") || appId.contains("qxevents")){
			return;
		}
		if(appId.contains("nativedarktemplate") || appId.contains("nativeeleganttemplate")){
			return;
		}
		if(appId.contains("general")){
			templateName = "Elegant";
		}
		if(appId.contains("events")){
			templateName = "Dark";
		}
		
		if(templateName==null){
			return;
		}
		templateSource = TemplateManager.getTemplateStyleRootFolder(templateName, "IPHONE_NATIVE");
		copyToApplicationStyles(uuid, appId, templateSource, "Videos", "IPHONE_NATIVE");
		copyToApplicationStyles(uuid, appId, templateSource, "Blog", "IPHONE_NATIVE");
		copyToApplicationStyles(uuid, appId, templateSource, "Framed", "IPHONE_NATIVE");
		
		if(!uuid.equals(System.getProperty("adminUser")))
		{
			upgradeDefaultApplicationStyle(app,uuid, appId, templateSource, "VERSION4", "IPHONE_NATIVE");
			upgradeApplicationStyles(app,uuid, appId, templateSource, "VERSION4", "Videos","IPHONE_NATIVE");
			upgradeApplicationStyles(app,uuid, appId, templateSource, "VERSION4", "Blog","IPHONE_NATIVE");
			upgradeApplicationStyles(app,uuid, appId, templateSource, "VERSION4", "Framed","IPHONE_NATIVE");
			upgradeApplicationStyles(app,uuid, appId, templateSource, "VERSION4", "Compact","IPHONE_NATIVE");
		}
	}
	
	
}
