package pmedia.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class XFileUtils {
	
	public static int copyFile(File source, File destination){
		
		int result = 0;
		if(source !=null && destination!=null){
			System.out.println("#	xcopy " + source.getAbsolutePath() + " to  : " +destination.getAbsolutePath());
			try{
				
				FileUtils.copyFile(source, destination);	
			}catch(Exception e){
				
			}
		}else{
			System.out.println("#	xcopy failed : invalid parameters" );
		}
		return result;
	}
public static int safeDelete(String source){
		
		int result = 0;
		if(source !=null){
			try{
				File file = new File(source);
				if(file!=null && file.exists()){
					file.delete();
				}
			}catch(Exception e){
				
			}
		}else{
			System.out.println("#	xdelete failed : invalid parameters" );
		}
		return result;
	}
}
