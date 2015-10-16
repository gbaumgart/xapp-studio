package pmedia.AssetTools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import pmedia.types.DownloadTask;
import pmedia.utils.StringUtils;

public class DownloadManager
{
	
	public static ArrayList<DownloadTask>downloadTasks=new ArrayList<DownloadTask>();

	public static Boolean hasTask(String src)
	{
		for(int i = 0  ; i < DownloadManager.downloadTasks.size() ; i ++)
		{
			DownloadTask task = DownloadManager.downloadTasks.get(i);
			if(task.remoteFilepath.equals(src))
			{
				return true;
			}
		}
		return false;
	}
	
	public static void downloadFileManaged(String src,String dst,Boolean overwrite)
	{
		if(src ==null)
			return;
		
		if(dst ==null)
		{
			dst = src;
		}
		
		String prefix = System.getProperty("webapp.root");
		
		File testFile = new File(prefix+dst);
		
		if(testFile.exists())
			return;
		
		prefix =  prefix + "cache/";
		testFile = new File(prefix+dst);
		
		//if(overwrite && testFile.exists()){testFile.delete();}
		
		if(testFile.exists())
			return;

		if(DownloadManager.hasTask(dst))
			return;
		
		DownloadTask task = new DownloadTask();
		task.autoStart=false;
		task.isDownloading=false;
		task.localFilepath=System.getProperty("webapp.root") + "cache/" + dst;
		task.remoteFilepath=src;
		task.totalFileBytes=0;
		DownloadManager.downloadTasks.add(task);
		
		DownloadManager.proceed();
		
	}
	
	public static Boolean isDownloading()
	{
		for(int i = 0  ; i < DownloadManager.downloadTasks.size() ; i ++)
		{
			DownloadTask task = DownloadManager.downloadTasks.get(i);
			if(task.isDownloading)
			{
				return true;
			}
		}
		return false;
	}
	public static DownloadTask getNextTask()
	{

		for(int i = 0  ; i < DownloadManager.downloadTasks.size() ; i ++)
		{
			DownloadTask task = DownloadManager.downloadTasks.get(i);
			if(!task.isDownloading)
			{
				return task;
			}
		}
		
		return null;
	}
	public static void proceed()
	{
		if(DownloadManager.isDownloading())
			return;
		
		DownloadTask next = DownloadManager.getNextTask();
		if(next!=null)
		{
			DownloadManager._downloadFileInternal(next);
		}
	}
	
	public static void _downloadFileInternal(DownloadTask task)
	{
		if(task.localFilepath==null){
			DownloadManager.downloadTasks.remove(task);
			DownloadManager.proceed();
			return;
			}
		
		if(task.remoteFilepath==null){
			DownloadManager.downloadTasks.remove(task);
			DownloadManager.proceed();
			return;
			}
		
		
		String dstDirectory =StringUtils.path (task.localFilepath);
		
		File dstFile = new File(dstDirectory);
		if(dstFile!=null)
		{
			dstFile.mkdirs();
		}
		
		System.out.println("downloading " + task.remoteFilepath  + " to : " +task.localFilepath);
		
		if(task!=null && !task.isDownloading)
		{
			task.isDownloading=true;
			java.io.BufferedInputStream in = null;
			try {
				in = new java.io.BufferedInputStream(new java.net.URL(task.remoteFilepath).openStream());
			} catch (MalformedURLException e) 
			{
				System.out.println("can not read file : " + task.remoteFilepath);
				DownloadManager.downloadTasks.remove(task);
				DownloadManager.proceed();
				return;
				
			} catch (IOException e) {
				System.out.println("can not read file : " + task.remoteFilepath);
				DownloadManager.downloadTasks.remove(task);
				DownloadManager.proceed();
				return;
			}
			java.io.FileOutputStream fos = null;
			try {
				fos = new java.io.FileOutputStream(task.localFilepath);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			java.io.BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
			byte[] data = new byte[1024];
			int x=0;
			try {
				while((x=in.read(data,0,1024))>=0)
				{
					bout.write(data,0,x);
				}
			} catch (IOException e) {

				System.out.println("can write file : " + task.localFilepath);
				DownloadManager.downloadTasks.remove(task);
				DownloadManager.proceed();
				e.printStackTrace();
				return;
			}
			try {
				bout.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {

				
				e.printStackTrace();
			}
			
			DownloadManager.downloadTasks.remove(task);
			DownloadManager.proceed();
			//Downloader.downloadFile(task.remoteFilepath,task.localFilepath);
		}
		
	}
	
	public static Boolean downloadFileInternalExt(DownloadTask task)
	{
		if(task.localFilepath==null){
			return false;
			
		}
		
		if(task.remoteFilepath==null){
			return false;
			
		}
		
		
		String dstDirectory =StringUtils.path (task.localFilepath);
		
		File dstFile = new File(dstDirectory);
		if(dstFile!=null)
		{
			dstFile.mkdirs();
		}
		
		System.out.println("downloading " + task.remoteFilepath  + " to : " +task.localFilepath);
		
		BufferedInputStream in = null;
		
		in = null;
			try {
				in = new java.io.BufferedInputStream(new java.net.URL(task.remoteFilepath).openStream());
			} catch (MalformedURLException e) 
			{
				System.out.println("can not read file : " + task.remoteFilepath);
				return false;
				
			} catch (IOException e) {
				System.out.println("can not read file : " + task.remoteFilepath);
				return false;
			}
			FileOutputStream fos = null;
			try {
				fos = new java.io.FileOutputStream(task.localFilepath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
			byte[] data = new byte[1024];
			int x=0;
			try {
				while((x=in.read(data,0,1024))>=0)
				{
					bout.write(data,0,x);
				}
			} catch (IOException e) {

				System.out.println("can write file : " + task.localFilepath);
				return false;
			}
			try {
				bout.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
			return true;
			
	
		
	}
	public  static Boolean hasInternet()
	{
		 Boolean hasInternet=false;
		   
		   try 
		   {
			   DownloadManager.checkInternetInternal();
			   hasInternet = true;
			} catch (Exception e) 
			{
				hasInternet=false;
			}
			System.setProperty("hasInternet", hasInternet ? "true" : "false");
			return hasInternet;
	}
	public  static void checkInternetInternal() throws Exception 
    {
    	URL url=new URL("http://www.ibiza-pearls.com/IEventsServer/"); 
    	URLConnection con=url.openConnection();
    	url.openStream();
    //	InetAddress address = InetAddress.getByName("www.google.com");
        /*
    	if ( address.isReachable(2000) )
        { 
        	return true;  
        	}
        else
        {
        	return false; 
        }
        */
    }
	public static void downloadFile(String src,String dst)
	{

		java.io.BufferedInputStream in = null;
		try {
			in = new java.io.BufferedInputStream(new java.net.URL(src).openStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.io.FileOutputStream fos = null;
		try {
			fos = new java.io.FileOutputStream(dst);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			e.printStackTrace();
		}
		try {
			bout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
