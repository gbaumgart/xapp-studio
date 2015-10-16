package pmedia.DataUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pmedia.types.ArticleData;
import pmedia.types.Category;
import pmedia.types.LocationData;
import pmedia.types.LocationSearchResult;
import pmedia.types.LocationSearchResultComparator;
import pmedia.types.LocationSearchResultComparatorWC;
import pmedia.types.MappingData;
import pmedia.types.MediaItemBase;
import pmedia.types.MediaType;
import pmedia.types.MosetItem;
import pmedia.types.PMDataTypes;
import pmedia.types.Rating;
import pmedia.types.TranslationData;
import pmedia.utils.SettingsUtil;
import pmedia.utils.StringHelper;
import pmedia.utils.StringUtils;
import pmedia.DataManager.Cache;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;





public class LocationArrayTools
{

	public static int CONST_BLACKLIST_CATS[]= {};
	
	 public static void updateLocationMappedArticle(LocationData loc,String lang,String domain)
	 {
	    	
    	ArticleData article = null;
    	ArrayList<MappingData> mList = ServerCache.getInstance().getDC(domain).get(DomainCache.MAPPINGS);
    	if(loc !=null && mList!=null)
    	{
    		MappingData mapping = LocationArrayTools.getLocationMapping(loc.location_id,mList);
    		if(mapping!=null)
    		{
    			ArrayList<ArticleData> articles = ServerCache.getInstance().getDC(domain).get(DomainCache.ARTICLES);
    			if(articles!=null && articles.size() > 0 )
    			{
    				
    				article = ArticleTools.getArticleByTypeAndIndex(articles,mapping.dstID,PMDataTypes.DITT_ARTICLE);
    				if(article !=null)
    				{
    					loc.mappedArticle = article;
    				} 
    			}
    		} 						
    	}
    	loc.didMapping=true;
	}
	 
	 public static void updateRating(ArrayList<LocationData>srcLocations,ArrayList<Rating>ratings)
	{
		 if(srcLocations==null){
			 return;
		 }
		for (int ls = 0; ls < srcLocations.size() ; ls++)
		{
   		    LocationData l = srcLocations.get(ls);
   		   // if(l.isDidRatingSearch())
   		    //	continue;
   		    Rating r = RatingTools.getByContextId(ratings, l.location_id);
   		    if(r!=null){
   		    	l.setRating(r);
   		    }
   		    l.setDidRatingSearch(true);
		}
	}
	
	public static void updateCategoryIcon(ArrayList<LocationData>srcLocations,Category category)
	{
		if(category.getIconUrl()!=null && category.getIconUrl().length() > 0)
		{
			for (int ls = 0; ls < srcLocations.size() ; ls++)
			{
	   		    LocationData l = srcLocations.get(ls);
	   		    l.categoryIcon = category.getIconUrl();
			}
		}
		
		
	}
	
	public static MappingData getLocationMapping(int locID,ArrayList<MappingData>mList)
	{
		for( int i = 0 ; i < mList.size() ; i ++)
		{
			MappingData data = mList.get(i);
			if(data.srcID == locID && data.srcType == PMDataTypes.DITT_JLOCATION)
				return data;
		}
		
		return null;
		
	}

	public static void makeLTest()
	{

		String searchTerm = "Le Redlight ( 75 ) Club";
		LocationData a1 = new LocationData();a1.title="Redlighz - Brasil Tropical Club";
		LocationData a2 = new LocationData();a2.title="MoonLight - Brasil Tropical Club";

		ArrayList<LocationData> locs = new ArrayList<LocationData>();
		locs.add(a2);
		locs.add(a1);

		//LocationData out = findBest(searchTerm, locs);
		//System.out.println("asdfa");

	}


	public static LocationData findBest(String searchTerm,ArrayList<LocationData>srcLocations,String domain )
	{

		searchTerm = searchTerm.replace("-", " ");
		String blackList = SettingsUtil.getProperty(domain + ".locationBlackList").toLowerCase();
		ArrayList<String> searchTermStrings= StringHelper.split2(searchTerm.toLowerCase(), " ",2,blackList);

		ArrayList<LocationSearchResult> sResults = new ArrayList<LocationSearchResult>();

		//iterator through locations
		for (int ls = 0; ls < srcLocations.size() ; ls++)
		{
   		    LocationData l = srcLocations.get(ls);

   		    ArrayList<String> searchTermLocationStrings= StringHelper.split2(l.title.toLowerCase(), " ",2,blackList);
   		    int wordcount = 0;
   		    LocationSearchResult sResult = new LocationSearchResult();

   		    for (int i = 0; i < searchTermLocationStrings.size() ; i++)
   		    {

   		    	String testTermLoc = searchTermLocationStrings.get(i);
   		    	for (int j = 0; j < searchTermStrings.size() ; j++)
   	   		    {
   		    		String testTerm = searchTermStrings.get(j);
   		    		int distance = StringHelper.computeLevenshteinDistance(testTermLoc,testTerm );
   		    		if(distance <= 2 )
   		    		{
   		    			wordcount++;
   		    		}

   		    		if(wordcount >0)
   		    		{
   		    			sResult.charMatchCount = testTermLoc.length() - testTerm.length();
   		    		}

   	   		    }
   		    }

   		    if(wordcount > 0)
   		    {
   		    	sResult.item = l;
   		    	sResult.wordCount = wordcount;
   		    	sResults.add(sResult);
   		    }
   		    //System.out.println("asdfa");
		}

		Comparator<LocationSearchResult> locComparator = new LocationSearchResultComparator();
		Collections.sort(sResults,locComparator);

		//Comparator<LocationSearchResult> locComparator2 = new LocationSearchResultComparatorWC();
		//Collections.sort(sResults,locComparator2);


		if(sResults.size() > 0)
		{
			return sResults.get(0).item;
		}

		//System.out.println("asdfa");
		return null;

	}



	public static ArrayList<LocationData>filterByCity(ArrayList<LocationData>srcLocations,String city,ArrayList<Category>regions)
	{
		if(srcLocations==null)
			return null;
		
		if(city==null)
			return srcLocations;
		
			
		ArrayList<LocationData>result = new ArrayList<LocationData>();
		Category c = CategoryTools.getCatByName(regions,city);
		if(c==null)
			return srcLocations;
		
		System.out.println("c : " + city +  "  - " + c.title + " index :  " + c.refId);
		if(srcLocations !=null)
		{
			for (int s = 0; s < srcLocations.size() ; s++)
			 {
			    LocationData l = srcLocations.get(s);
			    if(l.city_cat == c.refId&& l.city_cat!=-1)
			    {
			    	result.add(l);
			    }else
			    {
			    }
			 }
		}
		return result;
	}
	public static ArrayList<LocationData>getLocationsByGroupId(ArrayList<LocationData>list,int index)
	{
		ArrayList<LocationData> result = new ArrayList<LocationData>();
		if(list !=null)
		{
			for (int s = 0; s < list.size() ; s++)
			 {
			    LocationData l = list.get(s);
			    if(l !=null && l.getGroupId()==index)
			    {
			    	return result;
			    }
			 }
		}
		return result;
	}
	public static ArrayList<LocationData>updateLocationCategories(ArrayList<LocationData>srcLocations,ArrayList<Category>srcCategories)
	{
		if(srcLocations !=null)
		{
			for (int s = 0; s < srcLocations.size() ; s++)
			 {
			    LocationData l = srcLocations.get(s);
			    if(l !=null)
			    {
			    	//LocationData l = LocationArrayTools.getLocationByIndex(srcLocations, e.loc_id);

			    	Category c = CategoryTools.getCatByIndex(srcCategories, l.cat_id);
			    	if(c !=null)
			    	{
			    		l.category = c;
			    		l.categoryFullString = CategoryTools.getCategoryFullString(srcCategories, l.cat_id);

			    	}
			    }
			 }
		}
		return srcLocations;
	}

	public static ArrayList<LocationData>updateLocationLogo(ArrayList<LocationData>srcLocations)
	{
		for (int s = 0; s < srcLocations.size() ; s++)
		 {
		    LocationData l = srcLocations.get(s);
		    if(l !=null)
		    {

		    	if(l.description != null && l.description.length() > 0 && l.logoLocalPath ==null )
		    	{
		    		//RegEx reg = /<img .*src=["|\']([^"|\']+)/i;

		    		Pattern p=null;
		    		Matcher m= null;
		    		String word0= null;
		    		String word1= null;

		    		p= Pattern.compile(".*<img[^>]*src=\"([^\"]*)",Pattern.CASE_INSENSITIVE);
		    		m= p.matcher(l.description);
		    		while (m.find())
		    		{
			    		word0=m.group(1);
			    		l.logoLocalPath = word0;
			    		//System.out.println(word0.toString());
		    		}

		    	}
		    }
		 }
		return srcLocations;
	}

	public static Boolean isBlackListed(int cat_index)
	{

		for(int i  = 0 ; i < 4 ; i++)
		{
			if(CONST_BLACKLIST_CATS[i] == cat_index)
			{
				return true;
			}
		}
		return false;
	}
	public static Boolean isValidLocation(int cat_index,ArrayList<LocationData> locCats)
	{
		Category c = CategoryTools.getCatByIndex(Cache.categories, cat_index);
		if(c != null)
		{
			return CategoryTools.isValidCat(cat_index,locCats);
		}
		return true;
	}

	public static ArrayList<LocationData>filterLocationListByBlackList(ArrayList<LocationData>srcLocations)
	{

		ArrayList<LocationData> result = new ArrayList<LocationData>();
		for (int s = 0; s < srcLocations.size() ; s++)
		 {
		    LocationData l = srcLocations.get(s);
		    if(l!=null && isValidLocation(l.cat_id,srcLocations))
		    {
		    	result.add(l);
		    }
		 }
		return result;
	}

	public static String[]getLocationTitleList(ArrayList<LocationData>srcLocations)
	{
		String result[] = new String[srcLocations.size()];
		for (int s = 0; s < srcLocations.size() ; s++)
		 {
		    LocationData l = srcLocations.get(s);
		    if(l!=null)
		    {
		    	result[s] = l.title;
		    }
		 }
		return result;
	}

	public static LocationData getLocationByIndex(ArrayList<LocationData>srcLocations,int index)
	{
		for (int s = 0; s < srcLocations.size() ; s++)
		 {
		    LocationData l = srcLocations.get(s);
		    if(l.location_id == index)
		    	return l;
		 }
		return null;
	}

	public static LocationData getLocationByName(ArrayList<LocationData>srcLocations,String name)
	{


		for (int s = 0; s < srcLocations.size() ; s++)
		{

		    LocationData l = srcLocations.get(s);
		    String titleTerms[] = StringHelper.split(l.title, " ");

		    if(l.title.trim().equalsIgnoreCase(name))
		    	return l;

		    /*
		    for (int li = 0; li < titleTerms.length ; li++)
    		{
				String locationTestTerm  = titleTerms[li];
				if(locationTestTerm.trim().equalsIgnoreCase(name))
				{
					//if(l.title.trim().equalsIgnoreCase(name))
			    	return l;
				}
    		}
		    */
		}
		return null;
	}

	public static ArrayList<MosetItem>readMosetItemsFromFile(String path,String baseRef) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<MosetItem > result = new ArrayList<MosetItem>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		try {
			doc = db.parse(file);	
		} catch (Exception e) {
			return result;
		}
		doc.getDocumentElement().normalize();
		//System.out.println("Root element " + doc.getDocumentElement().getNodeName());
		NodeList nodeLst = doc.getElementsByTagName("location_item");

		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	MosetItem l = new MosetItem();
		    	Element fstElmnt = (Element) fstNode;

		    	 
		    	///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		title
		        //
		        
		        l.title=StringUtils.getXMLFromElement(fstElmnt, "title");
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		www
		        //
		        l.www=StringUtils.getXMLFromElement(fstElmnt, "www");
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		phone
		        //
		        l.phone = fstElmnt.getAttribute("p");

		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		long
		        //
		        l.longtitude = fstElmnt.getAttribute("ln");


		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		lat
		        //
		        l.latitude = fstElmnt.getAttribute("lat");
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		description
		        //
		        l.description=StringUtils.getXMLFromElement(fstElmnt, "description");
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		cat idx
		        //
		        l.cat_id = StringUtils.getIntegerFromXMLAttribute(fstElmnt, "cID");
		        
		        l.groupId=l.cat_id;
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		city
		        //
		        l.city=StringUtils.getXMLFromElement(fstElmnt, "city");
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		id
		        //
		        l.location_id = StringUtils.getIntegerFromXMLAttribute(fstElmnt, "id");
		        l.refId=l.location_id;
				try {
					if(fstElmnt.getAttribute("r")!=null)
					{
						l.city_cat = Integer.parseInt(fstElmnt.getAttribute("r"));
						
					}
				} catch (Exception e) {
					l.city_cat = -1;
				}
				
				int pub = 1;
				try {
					if(fstElmnt.getAttribute("pub")!=null)
					{
						pub = Integer.parseInt(fstElmnt.getAttribute("pub"));
					}
				} catch (Exception e) {
					
				}
				
				l.setPublished( pub == 1  ? true : false);
				
				String picture = fstElmnt.getAttribute("picture");
				if(picture!=null)
				{
					String prefix  = "";
					if(baseRef!=null){
						prefix=baseRef;
					}
					String iconUrl = baseRef +  "/index.php?option=com_mtree&task=att_download&link_id=" + l.refId + "&cf_id=23";
					l.setPicture(iconUrl);
					l.setIconUrl(iconUrl);
					l.setPictureItems(new ArrayList<MediaItemBase>());
					MediaItemBase picItem = new MediaItemBase();
					picItem.setType(MediaType.MT_PICTURE);
					picItem.setFullSizeLocation(iconUrl);
					l.getPictureItems().add(picItem);
					//if(l.getPictureItems())
				}

				///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		street
		        //

				l.street=StringUtils.getXMLFromElement(fstElmnt, "street");
				

				/////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		street
		        //
		        l.pcode =  fstElmnt.getAttribute("pc");
		        result.add(l);
		    }
		 }
		return result;
	}
	
	public static ArrayList<LocationData>readFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<LocationData > result = new ArrayList<LocationData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		
		try {
			doc = db.parse(file);	
		} catch (Exception e) {
			return result;
		}
		
		doc.getDocumentElement().normalize();

		//System.out.println("Root element " + doc.getDocumentElement().getNodeName());

		NodeList nodeLst = doc.getElementsByTagName("location_item");

		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {

		    	LocationData l = new LocationData();

		    	Element fstElmnt = (Element) fstNode;

		    	 
		    	///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		title
		        //
		        NodeList titleElmntLst = fstElmnt.getElementsByTagName("title");
		        l.title=StringUtils.getXMLFromElement(fstElmnt, "title");
		        //fstElmnt.hasAttribute("region");
		        //String region = fstElmnt.getAttribute("region");
		        //LocationArrayTools.System.out.println("title : " + l.title  +  "  r :  " + fstElmnt.getAttribute("region") + " has att : " +fstElmnt.hasAttribute("region") + " r: " +fstElmnt.hasAttribute("r") );
		        
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		www
		        //
		        l.www=StringUtils.getXMLFromElement(fstElmnt, "www");
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		phone
		        //
		        l.phone = fstElmnt.getAttribute("p");

		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		long
		        //
		        l.longtitude = fstElmnt.getAttribute("ln");


		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		lat
		        //
		        l.latitude = fstElmnt.getAttribute("lat");
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		description
		        //
		        l.description=StringUtils.getXMLFromElement(fstElmnt, "description");
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		cat idx
		        //
		        l.cat_id = StringUtils.getIntegerFromXMLAttribute(fstElmnt, "cID");
		        
		        l.groupId=l.cat_id;
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		city
		        //
		        l.city=StringUtils.getXMLFromElement(fstElmnt, "city");
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		id
		        //
		        l.location_id = StringUtils.getIntegerFromXMLAttribute(fstElmnt, "id");
		        l.refId=l.location_id;
				try {
					if(fstElmnt.getAttribute("r")!=null)
					{
						l.city_cat = Integer.parseInt(fstElmnt.getAttribute("r"));
						
					}
				} catch (Exception e) {
					l.city_cat = -1;
				}
				//System.out.println("loc city cat : " + l.title + "  : " + l.city_cat + " in was : " + fstElmnt.getAttribute("r"));
		        /*if( idElmntLst != null)
		        {
		        	Element idElmnt = (Element) idElmntLst.item(0);
		        	if(idElmnt !=null)
		        	{
		        		NodeList id = idElmnt.getChildNodes();
		        		if(id!=null && id.item(0) !=null )
		        		{
		        			l.location_id = Integer.parseInt(((Node)id.item(0)).getNodeValue());
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }*/
		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		street
		        //
		        NodeList streetElmntLst = fstElmnt.getElementsByTagName("street");
		        if( streetElmntLst != null)
		        {
		        	Element streetElmnt = (Element) streetElmntLst.item(0);
		        	if(streetElmnt !=null)
		        	{
		        		NodeList street= streetElmnt.getChildNodes();
		        		if(street!=null && street.item(0) !=null )
		        		{
		        			l.street= ((Node)street.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
				/////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		street
		        //
		        l.pcode =  fstElmnt.getAttribute("pc");
		        
		        int pub=1;
		    	try {
		    		pub = Integer.parseInt(fstElmnt.getAttribute("pub"));
				} catch (Exception e2) 
				{
					
				}
		    	
		    	l.setPublished(pub==1 ? true : false);
		    	
		    	
		    	NodeList vElmntLst = fstElmnt.getElementsByTagName("video0");
		        if( vElmntLst != null)
		        {
		        	
		        	Element vElmnt = (Element) vElmntLst.item(0);
		        	if(vElmnt !=null)
		        	{
		        		NodeList v = vElmnt.getChildNodes();
		        		if(v!=null && v.item(0) !=null )
		        		{
		        			l.setVideo(((Node)v.item(0)).getNodeValue());
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		       // l.setVideo(StringUtils.getXMLFromElement(fstElmnt, "video0"));
		        
		        result.add(l);
		    }
		 }
		return result;
	}
}
