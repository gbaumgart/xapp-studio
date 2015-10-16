package pmedia.DataUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import pmedia.types.MediaItemBase;
import pmedia.types.MediaType;
import pmedia.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Element;


/**
 * This class is used to manipulate strings
 */
public class MediaUtils
{
	static void updateMediaItemsForDevice(ArrayList<MediaItemBase>pictures, String platform)
	{
		
	}
	

	public static Boolean hasPictureItem(ArrayList<MediaItemBase>src,pmedia.types.MediaItemBase item)
	{
		Boolean result = false;
		for (int i = 0; i < src.size(); i++) 
		{
			MediaItemBase srcItem=src.get(i);
			if(srcItem.getFullSizeLocation() !=null && item.getFullSizeLocation()!=null)
			{
				if(srcItem.getFullSizeLocation().equals(item.getFullSizeLocation()))
				{
					return true;
				}else if( item.getFullSizeLocation().contains(srcItem.getFullSizeLocation()))
				{
					return true;
				}
			}
			
		}
		
		return result;
		
	}

	public static ArrayList<MediaItemBase>addPictureItems(ArrayList<MediaItemBase>src,ArrayList<MediaItemBase>dst,String baseRef)
	{
		for (int i = 0; i < src.size(); i++) 
		{
			MediaItemBase item = src.get(i);
			if(!item.getFullSizeLocation().startsWith("http") && baseRef!=null)
			{
				item.setFullSizeLocation(baseRef+"/"+item.getFullSizeLocation());
			}
			if(!hasPictureItem(dst, src.get(i)))
			{
				dst.add(item);
			}else{
				
			}
		}
		return dst;
	}
	public static ArrayList<MediaItemBase>getPictureItemsFromText(String text){
		
		ArrayList<pmedia.types.MediaItemBase>result = new ArrayList<MediaItemBase>();
		
		if(text!=null && text.length() > 0)
		{
			Document doc = Jsoup.parse(text);
			Elements pngs = doc.select("img");
			for (Element img : pngs)
			{
			        String url = img.attr("src");
			        if(url!=null && url.length() > 0)
			        {
			        	MediaItemBase itemToAdd = new MediaItemBase();
			        	itemToAdd.setFullSizeLocation(url);
			        	
			        	String alt = img.attr("alt");
			        	if(alt!=null && alt.length() > 0)
			        	{
			        		itemToAdd.setTitle(alt);
			        	}
			        	
			        	String width = img.attr("width");
			        	if(width!=null && width.length() > 0)
			        	{
			        		itemToAdd.setWidth(width);
			        	}
			        	
			        	String height= img.attr("height");
			        	if(height!=null && height.length() > 0)
			        	{
			        		itemToAdd.setHeight(alt);
			        	}
			        	itemToAdd.setType(MediaType.MT_PICTURE);
			        	result.add(itemToAdd);
			        }
			    }
		}
		return result;
	}
}
