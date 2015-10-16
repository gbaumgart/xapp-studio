package cmx.types;

import cmx.tools.LiferayContentTools;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

public class ContentManager {
	
	/***
	 * 
	 */
	private static ContentManager sInstance = new ContentManager();
	
	
	public static ContentManager getInstance()
	{
		//ServerCache sc = ServerCache.getInstance();
		return ContentManager.sInstance;
	};
	
	public static String saveLocation(String appIdentfier,String uuid,String refId,String content,String newTitle,int catId,String street,String phone,String pcode,String city,String web,String lat,String lon,String image)
	{
		JournalArticle larticle=null;
		try {
			larticle = JournalArticleLocalServiceUtil.getArticle(Long.parseLong(refId));
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		JournalArticle larticle = LiferayContentTools.getArticleByUserIdAndArticleId(uuid,""+refId);
    	if(larticle==null)
    	{
    		larticle=LiferayContentTools.getArticleByUserIdAndResourceId(uuid, Long.parseLong(refId));
    	}
    	*/
    	if(larticle==null){
    		return null;
    	}
    
    	System.out.println("updating :"+larticle.getArticleId());
    	String contentFull=larticle.getContent();
    	
    	if(lat.length()==0){
    		lat = LiferayContentTools.getLiferayArticleContentByStructureElement(larticle, "Lat");
    	}
    	
    	if(lon.length()==0)
    	{
    		lon = LiferayContentTools.getLiferayArticleContentByStructureElement(larticle, "Lon");
    	}
    	
    	String innerContent ="<root available-locales=\"en_US\" default-locale=\"en_US\">    <dynamic-element instance-id=\"axvcN9kp\" name=\"Content\" type=\"text_area\" index-type=\"text\">        " +
    			"<dynamic-content><![CDATA[" + content +"]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"435vhIKf\" name=\"City\" type=\"text\" index-type=\"\">        " +
    				"<dynamic-content><![CDATA["+city+"]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"O2GNKBrZ\" name=\"Featured\" type=\"boolean\" index-type=\"\">      " +
    				"<dynamic-content><![CDATA[true]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"pFOkdsf3\" name=\"Icon\" type=\"image\" index-type=\"\">        " +
    				"<dynamic-content><![CDATA[" + image+ "]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"eeL4RGlF\" name=\"Web\" type=\"text\" index-type=\"\">       " +
    				"<dynamic-content><![CDATA[" + web  + "]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"57H0Hz7I\" name=\"Lon\" type=\"text\" index-type=\"\">       " +
    				" <dynamic-content><![CDATA[" +lon + "]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"CmuxJ2UM\" name=\"Lat\" type=\"text\" index-type=\"\">      " +
    				"<dynamic-content><![CDATA[" + lat+ "]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"U8sM71DN\" name=\"PostCode\" type=\"text\" index-type=\"text\">        " +
    				"<dynamic-content><![CDATA[" + pcode +"]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"v5wlSSQ3\" name=\"Phone\" type=\"text\" index-type=\"keyword\">        " +
    				"<dynamic-content><![CDATA[" + phone +"]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"gY1LgDke\" name=\"Street\" type=\"text\" index-type=\"text\">        " +
    				"<dynamic-content><![CDATA[" +street +"]]></dynamic-content>    </dynamic-element>" +
    			"</root>";
    	
    	
    	//String fullContent = LiferayContentTools.setLiferayArticleContentByStructureElement(larticle, structureField, content);
    	/*
    	if(fullContent!=null)
    	{
    		
    	}
    	*/
    	larticle.setContent(innerContent);
    	if(newTitle!=null && newTitle.length() > 0)
    	{
    		larticle.setTitle(newTitle);
    	}
    	/*
    	Map<Locale, String> titleMap = larticle.getTitleMap();
		Map<Locale, String> descriptionMap = larticle.getDescriptionMap();

		String tempOldUrlTitle = larticle.getUrlTitle();
		titleMap.put(defaultLocale, title);
		descriptionMap.put(defaultLocale, description);
		*/
    	JournalArticle updatedArticle=null;
		try {
			updatedArticle = JournalArticleLocalServiceUtil.updateJournalArticle(larticle);
		} catch (SystemException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	if(updatedArticle!=null)
    	{
    		System.out.println("liferay article updated : " + updatedArticle.getArticleId());
    		return updatedArticle.getArticleId();
    	}
    	return null;
	}
	public static String saveJournalArticle(String appIdentfier,String uuid,String refId,String content,String structureField,String newTitle)
	{
		//JournalArticle larticle = LiferayContentTools.getArticleByUserIdAndArticleId(uuid,""+refId);
		JournalArticle  larticle= null ;
    	try {
			larticle = JournalArticleLocalServiceUtil.getArticle(Long.parseLong(refId));
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(larticle==null)
    	{
    		return null;
    	}
    	System.out.println("updating :"+larticle.getArticleId());
    	String fullContent = LiferayContentTools.setLiferayArticleContentByStructureElement(larticle, structureField, content);
    	if(fullContent!=null)
    	{
    		
    	}
    	
    	larticle.setContent(fullContent);
    	if(newTitle!=null && newTitle.length() > 0)
    	{
    		larticle.setTitle(newTitle);
    	}
    	/*
    	Map<Locale, String> titleMap = larticle.getTitleMap();
		Map<Locale, String> descriptionMap = larticle.getDescriptionMap();

		String tempOldUrlTitle = larticle.getUrlTitle();
		titleMap.put(defaultLocale, title);
		descriptionMap.put(defaultLocale, description);
		*/

    	JournalArticle updatedArticle=null;
		try {
			updatedArticle = JournalArticleLocalServiceUtil.updateJournalArticle(larticle);
		} catch (SystemException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	if(updatedArticle!=null)
    	{
    		System.out.println("liferay article updated : " + updatedArticle.getArticleId());
    		return updatedArticle.getArticleId();
    	}
    	return null;
	}
	
	
	/**
	 * @return the applications
	 */
	
}
