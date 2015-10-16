package pmedia.html.factory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import pmedia.html.types.WhiteListData;

public class WhiteListDataFactory {

	/***
	 * Returns Whitelist allow minimal HTML elements
	 * @param applicationNameSpace
	 * @return
	 */
	public static WhiteListData iphoneNativeBase(String applicationNameSpace)
	{
	
		WhiteListData result = new WhiteListData();
		
		/***
		 * TagNames
		 */
		ArrayList<String>tagNames = new ArrayList<String>();
		tagNames.add("a");
		tagNames.add("b");
		tagNames.add("blockquote");
		tagNames.add("caption");
		tagNames.add("h1");
		tagNames.add("h2");
		tagNames.add("h3");
		tagNames.add("h4");
		tagNames.add("h5");
		tagNames.add("li");
		tagNames.add("ol");
		tagNames.add("p");
		tagNames.add("br");
		tagNames.add("table");
		tagNames.add("td");
		tagNames.add("tr");
		tagNames.add("tbody");
		tagNames.add("img");
		tagNames.add("style");
		tagNames.add("span");
		
		result.setTagNames(tagNames);
		
		/***********************************************
		 * Attributes per tag
		 */
		Map<String,ArrayList<String>> attributes = new Hashtable<String, ArrayList<String>>();

		/***
		 * Link Attribute
		 */
		ArrayList <String> aAtts = new ArrayList<String>();
		aAtts.add("href");
		attributes.put("a", aAtts);
		
		/***
		 * Span Attribute
		 */
		ArrayList <String> spanAtts = new ArrayList<String>();
		spanAtts.add("style");
		spanAtts.add("class");
		spanAtts.add("onclick");
		attributes.put("span", spanAtts);
		
		
		/***
		 * Link Attribute
		 */
		ArrayList <String> imgAtts = new ArrayList<String>();
		imgAtts.add("align");
		imgAtts.add("alt");
		imgAtts.add("src");
		imgAtts.add("height");
		imgAtts.add("title");
		imgAtts.add("width");
		attributes.put("img", imgAtts);
		
		result.setAttributes(attributes);
		
		/***********************************************
		 * Protocols per tag and attribute
		 */
		ArrayList <String> baseProtocols = new ArrayList<String>();
		baseProtocols.add("http");
		baseProtocols.add("https");
		baseProtocols.add("ftp");
		baseProtocols.add(applicationNameSpace);
		
		Map<String, Map<String, ArrayList<String>>> protocols = new Hashtable<String, Map<String,ArrayList<String>>>();

		//img
		Map<String, ArrayList<String>>imgSrc = new Hashtable<String, ArrayList<String>>();
		
		ArrayList <String> imgSrcProtocols = new ArrayList<String>();
		imgSrcProtocols.addAll(baseProtocols);
		
		imgSrc.put("src", imgSrcProtocols);
		protocols.put("img", imgSrc);
		
		//img
		Map<String, ArrayList<String>>linkSrc = new Hashtable<String, ArrayList<String>>();
		ArrayList <String> linkSrcProtocols = new ArrayList<String>();
		
		linkSrcProtocols.addAll(baseProtocols);
		linkSrcProtocols.add("mailto");
		linkSrc.put("href", linkSrcProtocols);
		protocols.put("a", linkSrc);
		
		result.setProtocols(protocols);

		
		
		return result;
	}
	/***
	 * Returns Whitelist allow minimal HTML elements
	 * @param applicationNameSpace
	 * @return
	 */
	public static WhiteListData mobileBase(String applicationNameSpace)
	{
	
		WhiteListData result = new WhiteListData();
		
		/***
		 * TagNames
		 */
		ArrayList<String>tagNames = new ArrayList<String>();
		tagNames.add("a");
		tagNames.add("b");
		tagNames.add("blockquote");
		tagNames.add("caption");
		tagNames.add("h1");
		tagNames.add("h2");
		tagNames.add("h3");
		tagNames.add("h4");
		tagNames.add("h5");
		tagNames.add("li");
		tagNames.add("ol");
		tagNames.add("p");
		tagNames.add("br");
		tagNames.add("table");
		tagNames.add("td");
		tagNames.add("tr");
		tagNames.add("tbody");
		tagNames.add("img");
		tagNames.add("style");
		tagNames.add("span");
		
		result.setTagNames(tagNames);
		
		/***********************************************
		 * Attributes per tag
		 */
		Map<String,ArrayList<String>> attributes = new Hashtable<String, ArrayList<String>>();

		/***
		 * Link Attribute
		 */
		ArrayList <String> aAtts = new ArrayList<String>();
		aAtts.add("href");
		attributes.put("a", aAtts);
		
		/***
		 * Span Attribute
		 */
		ArrayList <String> spanAtts = new ArrayList<String>();
		spanAtts.add("style");
		spanAtts.add("class");
		spanAtts.add("onclick");
		attributes.put("span", spanAtts);
		
		
		/***
		 * Link Attribute
		 */
		ArrayList <String> imgAtts = new ArrayList<String>();
		imgAtts.add("align");
		imgAtts.add("alt");
		imgAtts.add("src");
		imgAtts.add("height");
		imgAtts.add("title");
		imgAtts.add("width");
		attributes.put("img", imgAtts);
		
		result.setAttributes(attributes);
		
		/***********************************************
		 * Protocols per tag and attribute
		 */
		ArrayList <String> baseProtocols = new ArrayList<String>();
		baseProtocols.add("http");
		baseProtocols.add("https");
		baseProtocols.add("ftp");
		baseProtocols.add(applicationNameSpace);
		
		Map<String, Map<String, ArrayList<String>>> protocols = new Hashtable<String, Map<String,ArrayList<String>>>();

		//img
		Map<String, ArrayList<String>>imgSrc = new Hashtable<String, ArrayList<String>>();
		
		ArrayList <String> imgSrcProtocols = new ArrayList<String>();
		imgSrcProtocols.addAll(baseProtocols);
		
		imgSrc.put("src", imgSrcProtocols);
		protocols.put("img", imgSrc);
		
		//img
		Map<String, ArrayList<String>>linkSrc = new Hashtable<String, ArrayList<String>>();
		ArrayList <String> linkSrcProtocols = new ArrayList<String>();
		
		linkSrcProtocols.addAll(baseProtocols);
		linkSrcProtocols.add("mailto");
		linkSrc.put("href", linkSrcProtocols);
		protocols.put("a", linkSrc);
		
		result.setProtocols(protocols);

		
		
		return result;
	}
}
