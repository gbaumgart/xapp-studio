package pmedia.wizard;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import javazoom.upload.UploadFile;
import javazoom.upload.UploadListener;
import javazoom.upload.UploadParameters;
import org.apache.commons.io.FileUtils;
import cmx.types.ApplicationManager;

/**
 * UploadListener implementation sample.
 */
public class WizardUploadListener implements UploadListener, Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String uuid;
	public String appId;
	public String pageId;
	public String ciId;
	
	
	public static boolean DEBUG = false;
	  private File _tmpFile = null;
	  private boolean _uploaded = false;
	  private boolean _cancelled = false;
	  private int _size = -1;
	  private int _totalSize = -1;
	  private transient PrintStream ps = System.out;
	  public HttpSession theSession = null;
	  private static final String FACEBOOK_USER_CLIENT = "facebook.user.client";
  
  public WizardUploadListener()
  {
    trace("MyUploadListener Constructor called");
  }

  public void setTracefile(String file) throws IOException
  {
    FileOutputStream fos = new FileOutputStream(file);
    ps = new PrintStream(fos,true);
    trace("setTracefile called");
    System.out.println("file: " + file);
    
    
  }

  /**
   * Callback when file upload is starting.
   * @param file
   */
  public void fileUploadStarted(File tmpfile, int contentlength, String contenttype)
  {
    trace("fileUploadStarted called : "+tmpfile+","+contentlength+","+contenttype);
    _tmpFile = tmpfile;
    _totalSize = contentlength;
    _uploaded = false;
    _cancelled = false;
    _size = 0;
   // System.out.println("started: ");
    
  }

  public ApplicationManager getApplicationManager(String identifier)
	{
		ApplicationManager appManager = ApplicationManager.getInstance();
		if(appManager==null)
		{
			appManager = new ApplicationManager();
			this.theSession.setAttribute("AppManager", appManager);
			String uuid = UUID.randomUUID().toString();
			
			//System.out.println("app user id " + uuid);
		}
		return appManager;
	}
  /**
   * Callback when file upload is completed.
   * @param up
   * @param file
   */
  public void fileUploaded(UploadParameters up, UploadFile file)
  {
	  System.out.println("fileUploaded called : "+file.getFileName()+","+up.getAltFilename()+","+up.getStoreinfo());
    
    
    ApplicationManager appMan = getApplicationManager(appId);
    if(appMan!=null){
    	
    	//Application application= appMan.getApplication(uuid,appId,false);
    	
    	//String localWebAppPathAssets = appMan.getAppAssetsPublicPathLocal(uuid, appId);
    	String localAppPathAssets = appMan.getAppAssetsLocal(uuid, appId);
    	
    	File path = new File(localAppPathAssets + "/QXAppUploads");
    	if(!path.exists()){
    		path.mkdirs();
    	}
    	String fileName = file.getFileName();
    	if(fileName!=null){
    		fileName = fileName.replace(" ", "_");
    	}
    	
    	File srcFileUpload = new File(up.getStoreinfo() + file.getFileName());
    	//File dstFile1 = new File( localWebAppPathAssets + file.getFileName());
    	File dstFile2 = new File( localAppPathAssets + "/QXAppUploads/" + fileName);
    	try {
			//FileUtils.copyFile(srcFileUpload, dstFile1);
			FileUtils.copyFile(srcFileUpload, dstFile2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		System.out.println("fileUploaded called : "+ localAppPathAssets);
    	
    	//ContentTree tree = null;
		_uploaded = true;
    }
    
    //System.out.println("fileUploaded called : "+file.getFileName()+","+up.getAltFilename()+","+up.getStoreinfo());
    
    
    
    
    /*
    try {
		//uploadToFlicker(up, file);
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    //file.reset();
  */
  }
  

  
  
  
  /**
   * Callback when data chunk is loaded.
   * @param read
   */
  public void dataRead(int read)
  {
    trace("dataRead called : "+read);
    _size = _size + read;
    
  }

  /**
   * Return upload state.
   * @return
   */
  public boolean isFileUploaded()
  {
    trace("isFileUploaded called : "+_uploaded);
    System.out.println("file: uploaded ");
    return _uploaded;
    
  }

  /**
   * Return upload state.
   * @return
   */
  public boolean isFileUploadCancelled()
  {
    trace("isFileUploadCancelled called : "+_cancelled);
    reset();
    return _cancelled;
  }

  /**
   * Returns length in KB for current uploading.
   * @return
   */
  public int getUploadedKBLength()
  {
    trace("getUploadedKBLength called : "+_size);
    System.out.println("file: " + "getUploadedKBLength called : "+_size );
    int kblength = Math.round(_size*1.0f/1024.0f);
    return kblength;
  }

  /**
   * Returns length in bytes for current uploading.
   * @return
   */
  public int getUploadedLength()
  {
    trace("getUploadedLength called : "+_size);
    return _size;
  }

  /**
   * Returns ratio for current uploading.
   * @return
   */
  public int getUploadedRatio()
  {
    trace("getUploadedRatio called : "+_size+"/"+_totalSize);
    if (_totalSize > 0)
    {
      return (int) Math.round(_size*100.0f/_totalSize*1.0f);
    }
    else return -1;
  }

  /**
   * Returns length in bytes for upload file.
   * @return
   */
  public int getTotalLength()
  {
    trace("getTotalLength called : "+_totalSize);
    return _totalSize;
  }

  /**
   * Cancel Upload
   */
  public void cancel()
  {
    trace("cancel called ");
    _cancelled = true;
    
  }

  /**
   * Resets UploadStatus
   */
  public void reset()
  {
    trace("reset called : "+_totalSize);
    cleanLastupload();
    _tmpFile = null;
    _totalSize = -1;
    _uploaded = false;
    _cancelled = false;
    _size = -1;
  }

  /**
   * Deletes last uploaded file.
   */
  public void cleanLastupload()
  {
     if (_tmpFile != null)
     {
       String tmpFilename = _tmpFile.getName();
       _tmpFile.delete();
       trace("cleanLastupload called : "+tmpFilename);
     }
  }

  /**
   * Sends traces to PrintStream
   * @param msg
   */
  private void trace(String msg)
  {
    if (DEBUG == true)
    {
      Date d = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
      if (ps != null)
      {
        ps.println(sdf.format(d)+" MyUploadListener1.0.4 : "+msg);
      }
      else
      {
        System.out.println(sdf.format(d)+" MyUploadListener1.0.4 : "+msg);
      }
    }
  }

/**
 * @return the uuid
 */
public String getUuid() {
	return uuid;
}

/**
 * @param uuid the uuid to set
 */
public void setUuid(String uuid) {
	this.uuid = uuid;
}

/**
 * @return the appId
 */
public String getAppId() {
	return appId;
}

/**
 * @param appId the appId to set
 */
public void setAppId(String appId) {
	this.appId = appId;
}

/**
 * @return the pageId
 */
public String getPageId() {
	return pageId;
}

/**
 * @param pageId the pageId to set
 */
public void setPageId(String pageId) {
	this.pageId = pageId;
}

/**
 * @return the ciId
 */
public String getCiId() {
	return ciId;
}

/**
 * @param ciId the ciId to set
 */
public void setCiId(String ciId) {
	this.ciId = ciId;
}


}