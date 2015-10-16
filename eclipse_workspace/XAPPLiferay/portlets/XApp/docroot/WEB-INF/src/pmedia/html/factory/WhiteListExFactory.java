package pmedia.html.factory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.jsoup.safety.Whitelist.AttributeKey;
import org.jsoup.safety.Whitelist.Protocol;
import org.jsoup.safety.Whitelist.TagName;

import pmedia.html.types.WhiteListData;
import pmedia.html.types.WhiteListEx;


public class WhiteListExFactory {

	public static WhiteListEx createWhiteListEx(WhiteListData data){
		
		WhiteListEx result = WhiteListEx.simpleText();
		
		//add the attributes : 
		if(data.getAttributes()!=null){
			for (Map.Entry<String, ArrayList<String>> entry : data.getAttributes().entrySet()) 
			{
				result = result.addAttributes(entry.getKey(), entry.getValue());
			}
		}
		//add the tags : 
		if(data.getTagNames()!=null)
		{
			result  = result.addTags(data.getTagNames());
		}
		
		//add protocols
		if(data.getProtocols()!=null)
		{
			for (Map.Entry<String, Map<String, ArrayList<String>>> tagEntry : data.getProtocols().entrySet()) 
			{
				for (Map.Entry<String, ArrayList<String>> keyEntry : tagEntry.getValue().entrySet()) 
				{
					result.addProtocols(tagEntry.getKey(), keyEntry.getKey(), keyEntry.getValue());
				}
			}
		}
		
		//System.out.println("Attributes : \n\n\n");
		for (Map.Entry<TagName, Set<AttributeKey>> entry : result.attributes.entrySet()) 
		{
			//result = result.addAttributes(entry.getKey(), entry.getValue());
			//System.out.println("'TagName : " + entry.getKey());
			//System.out.println("'Attribute Key: " + entry.getValue().toString());
			
		}
		//System.out.println("Tags : \n\n\n");
		for (TagName tag : result.tagNames) 
		{
			//result = result.addAttributes(entry.getKey(), entry.getValue());
			//System.out.println("'TagName : " + tag.toString());
			//System.out.println("'Attribute Key: " + entry.getValue().toString());
			
		}
	//	System.out.println("\n\n\n");
		
		for (Map.Entry<TagName, Map<AttributeKey, Set<Protocol>>> tagEntry : result.protocols.entrySet()) 
		{
			for (Map.Entry<AttributeKey, Set<Protocol>> keyEntry : tagEntry.getValue().entrySet()) 
			{
				//System.out.println("'TagEntry: " + tagEntry.getKey());
				//System.out.println("'Key Entry: " + keyEntry.getKey());
				//System.out.println("'Key Entry Value : " + keyEntry.getValue().toString());
				//result.addProtocols(tagEntry.getKey(), keyEntry.getKey(), keyEntry.getValue());
			}
		}
		
	//	System.out.println("\n\n\n");
		
		
		
		
		return result;
	}
}
