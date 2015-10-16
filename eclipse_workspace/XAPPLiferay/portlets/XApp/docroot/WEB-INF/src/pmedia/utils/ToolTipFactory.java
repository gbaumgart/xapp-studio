package pmedia.utils;

import java.io.File;
import java.io.IOException;

import pmedia.types.ToolTipInfo;

public class ToolTipFactory 
{
	public static ToolTipInfo getToolTip(String key,String lang)
	{
		if(lang==null){
			lang="en";
		}
		ToolTipInfo result = new ToolTipInfo();
		
		String toolTipPath=System.getProperty("ToolTipStoreRoot")+"/"+ lang +"/"+key.toLowerCase()+".html";
		File f = new File(toolTipPath);
		if(!f.exists()){
			lang="en";
			toolTipPath=System.getProperty("ToolTipStoreRoot")+"/"+ lang +"/"+key.toLowerCase()+".html";
		}
		
		if(f.exists()){
			try {
				result.setContent(StringUtils.readFileAsString(toolTipPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.print("could not resolve Tooltip : " + key +" lang : " + lang + " at " + toolTipPath + "\n");
		}
		
		
		return result;
	}
}
