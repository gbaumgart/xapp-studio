package pmedia.html.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.helper.Validate;
import org.jsoup.safety.Whitelist;

public class WhiteListEx extends Whitelist 
{
	 
	public static WhiteListEx simpleText() 
	{
		 WhiteListEx result = new WhiteListEx();
		 result.addTags("b", "em", "i", "strong", "u");
		 return result;
	}
	public WhiteListEx addAttributes(String tag, ArrayList<String>keys) 
	{
        Validate.notEmpty(tag);
        Validate.notNull(keys);

        TagName tagName = TagName.valueOf(tag);
        Set<AttributeKey> attributeSet = new HashSet<AttributeKey>();
        for (String key : keys) {
            Validate.notEmpty(key);
            attributeSet.add(AttributeKey.valueOf(key));
        }
        if (attributes.containsKey(tagName)) {
            Set<AttributeKey> currentSet = attributes.get(tagName);
            currentSet.addAll(attributeSet);
        } else {
            attributes.put(tagName, attributeSet);
        }
        return this;
    }
	
	
	 public WhiteListEx addProtocols(String tag, String key, ArrayList <String> protocols) 
	 {
	        Validate.notEmpty(tag);
	        Validate.notEmpty(key);
	        Validate.notNull(protocols);

	        TagName tagName = TagName.valueOf(tag);
	        AttributeKey attrKey = AttributeKey.valueOf(key);
	        Map<AttributeKey, Set<Protocol>> attrMap;
	        Set<Protocol> protSet;

	        if (this.protocols.containsKey(tagName)) {
	            attrMap = this.protocols.get(tagName);
	        } else {
	            attrMap = new HashMap<AttributeKey, Set<Protocol>>();
	            this.protocols.put(tagName, attrMap);
	        }
	        if (attrMap.containsKey(attrKey)) {
	            protSet = attrMap.get(attrKey);
	        } else {
	            protSet = new HashSet<Protocol>();
	            attrMap.put(attrKey, protSet);
	        }
	        for (String protocol : protocols) {
	            Validate.notEmpty(protocol);
	            Protocol prot = Protocol.valueOf(protocol);
	            protSet.add(prot);
	        }
	        return this;
	    }
	public WhiteListEx addTags(ArrayList<String> tags) 
	{
        Validate.notNull(tags);
        for (String tagName : tags) {
            Validate.notEmpty(tagName);
            tagNames.add(TagName.valueOf(tagName));
        }
        return this;
    }

}
