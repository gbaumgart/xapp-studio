package pmedia.html.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import cmx.types.ECMContentSourceType;
import pmedia.html.factory.WhiteListDataFactory;
import pmedia.html.factory.WhiteListExFactory;
import pmedia.html.types.WhiteListData;
import pmedia.html.types.WhiteListEx;

public class HTMLConverter 
{
	/**
	 * 
	 * @param input
	 * @param uuid
	 * @param applicationId
	 * @param dsUID
	 * @param cType
	 * @param whiteList
	 * @param platform
	 * @param maxSize
	 * @return
	 */
	public static String convert(String input,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			ECMContentSourceType cType,
			WhiteListData whiteList,
			String platform,
			int maxSize)
	{		
		
		String result  = "";
		WhiteListEx wEx = WhiteListExFactory.createWhiteListEx(whiteList);
		
		
		//Whitelist clean = Whitelist.relaxed().addTags("blockquote", "cite", "code", "p", "q", "s","img","src","strike","br","a","strong");
    	//Whitelist clean = new Whitelist();//relaxed().addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong","tt");
    	//clean.addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong");
    	//clean.addProtocols("a", "href","ftp","http","https","mtpp");
		//Cleaner cleaner = new Cleaner(wEx);
		//Document doc = Jsoup.parse(input);
		
		//result = cleaner.clean(doc).body().html();
		
		result = Jsoup.clean(input, "", wEx);
		//System.out.println(result);
		return result;
		
	}
	
}
