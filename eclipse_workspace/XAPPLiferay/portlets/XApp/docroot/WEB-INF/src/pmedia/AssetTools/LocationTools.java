package pmedia.AssetTools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import pmedia.types.LocationData;
import pmedia.utils.SettingsUtil;
import pmedia.utils.StringHelper;
import pmedia.utils.StringUtils;





public class LocationTools 
{
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
	public static void saveToGallery(LocationData data)
	{
		
		ArrayList<String> files = data.getPictures();
		
		if(data.pictures==null)
			return;
		
		int startIndex=0;
		if(files.size() > 1)
			startIndex = 1;
		for(int i = startIndex ; i < data.getPictures().size() ; i++  )
		{
			String fileName = data.getPictures().get(i);
			String srcPath=SettingsUtil.getProperty("galleryPathLocal");
			String folderName = StringUtils.replace(data.title," ","");
			srcPath = srcPath + folderName + "Gallery";
			File dstFolder = new File(srcPath);
			if(!dstFolder.exists())
			{
				dstFolder.mkdirs();
			}
			
			
			String srcPathdoNotShow = srcPath + "/DoNotShow";
			File dstFolderDoNotShow = new File(srcPathdoNotShow);
			if(!dstFolderDoNotShow.exists())
			{
				dstFolderDoNotShow.mkdirs();
			}
			
			String srcPathFeatured= srcPath + "/Featured";
			File dstFolderFeatrued= new File(srcPathFeatured);
			if(!dstFolderFeatrued.exists())
			{
				dstFolderFeatrued.mkdirs();
			}
			
			String dstFileName = srcPath+"/" + StringHelper.getName(fileName);
			File dstFile = new File(dstFileName);
			//System.out.println("file doesnt exists : " + dstFileName);
			String srcFileName=SettingsUtil.getProperty("ibiza.locationFiles") + fileName;
			if(!dstFile.exists())
			{
				downloadFile(srcFileName, dstFileName);
			}
		}
	}
}
