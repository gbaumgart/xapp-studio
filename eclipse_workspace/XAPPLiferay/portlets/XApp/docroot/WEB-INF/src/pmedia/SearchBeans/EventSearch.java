package pmedia.SearchBeans;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pmedia.DataManager.Cache;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.EventDataArrayTools;
import pmedia.DataUtils.LocationArrayTools;
import pmedia.types.Category;
import pmedia.types.EventData;
import pmedia.types.FBUserEventsComparator;
import pmedia.types.LocationData;
import pmedia.types.LocationEventsComparator;
import pmedia.types.SectionedEvents;
import pmedia.utils.EventDateComparator;
import pmedia.utils.SettingsUtil;
import pmedia.utils.StringHelper;
import pmedia.utils.StringUtils;
import pmedia.utils.TimeUtils;
import java.util.Collections;

public class EventSearch
{

   /** The number of hits to be returned per page */
   protected int m_hitsPerPage = 40;
   /** The current page number */
   private int m_currentPageNumber = 0;

   private boolean showPast = true;
   private int maxDiff = 4;
   public int getMaxDiff() {
	return maxDiff;
}

public void setMaxDiff(int maxDiff) {
	this.maxDiff = maxDiff;
}

public boolean isShowPast() {
	return showPast;
}

public void setShowPast(boolean showPast)
{
	this.showPast = showPast;
}

/** A vector of product that match the search criteria */
   protected Vector m_searchResults = null;
   protected ArrayList<ArrayList<EventData>> searchResultsTimeTree = null;



public ArrayList<ArrayList<EventData>> getSearchResultsTimeTree() {
	return searchResultsTimeTree;
}

public void setSearchResultsTimeTree(
		ArrayList<ArrayList<EventData>> searchResultsTimeTree) {
	this.searchResultsTimeTree = searchResultsTimeTree;
}

/** The sql that is used to search for the products */
   protected String m_searchString = null;

   public String m_recurs = null;

   public Vector m_FilterColumns = null;

   public Cache cache = new Cache();
   public HttpSession   session    = null;



   public void sortByFBUserEventsSum()
   {

	   if(m_searchResults == null)
		   return;

	   if(cache == null)
		   return;

	   FBUserEventsComparator fbUserSumComp = new FBUserEventsComparator(cache);
	   if(getSearchResults() !=null)
		   Collections.sort(getSearchResults(), fbUserSumComp);



   }

   public void sortByLocationEventsSum()
   {

	   if(m_searchResults == null)
		   return;

	   if(cache == null)
		   return;


	   LocationEventsComparator fbUserSumComp = new LocationEventsComparator(cache);
	   Collections.sort(getSearchResults(), fbUserSumComp);

   }

   
   public ArrayList<EventData>GetLastTicketResults(String domain,String locationCheck,DataSource ds)
   {

	  	int locTest = Integer.parseInt(locationCheck);

  		String pathSuffix = SettingsUtil.getProperty("webapp.root");
  		String picstore =  SettingsUtil.getProperty("picstore");

  		if ( domain !=null && domain.length() > 0)
  		{
  			pathSuffix = pathSuffix + "db/" + domain + "/";
  		}


  		String ticketsPath =  pathSuffix + "tickets" + ".xml";
  		ArrayList<EventData> elist = null;
  		try {
			elist = EventDataArrayTools.readLocalTicketsFromFileInternalFormat(ticketsPath);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		Vector<EventData> results = new Vector<EventData>();
  	    //ArrayList<EventData> finalFBList = new ArrayList<EventData>();

  	    for (int s = 0; s < elist.size() ; s++)
  	    {
  	    	EventData e = elist.get(s);
		    results.add(e);
  	    }

  	    setSearchResults(results);

  	    return elist;

   }

   public ArrayList<LocationData>searchLocationsByCategory(int category,String searchTerm,Boolean inPlaceSearch,String domain,String locationCheck,DataSource ds,ArrayList<LocationData>currentLocations,Boolean flatten)
   {
	  if(inPlaceSearch)
	  {
	  }else
	  {
   	  }
	  String pathSuffix = SettingsUtil.getProperty("webapp.root");
	  if( domain !=null && domain.length() > 0)
	  {
		pathSuffix = pathSuffix + "db/" + domain + "/";
	  }

	  ArrayList<LocationData> lList = null;
   	  if(inPlaceSearch)
   	  {
   		  lList = currentLocations;
   	  }else
   	  {
   		   lList = ServerCache.getInstance().getDC(domain).get(DomainCache.LOCATIONS3);
   		   //lList = EventDataArrayTools.readLocalEventsFromFile(localEvents);
   	  }

	  ArrayList<Category> catListLocations =  ServerCache.getInstance().getDC(domain).get(DomainCache.CATS_LOCATIONS);
	  
	  ArrayList<Category> subLocationCategories = CategoryTools.getLocationCategoriesByIndex(catListLocations, currentLocations, category, flatten);
	  
	  
	  Vector<LocationData> results = new Vector<LocationData>();
  	  ArrayList<LocationData>out = new ArrayList<LocationData>();
  	  for (int s = 0; s < lList.size() ; s++)
  	  {
  		  LocationData l = lList.get(s);
  		  if(l !=null)
		  {
  			  if(!flatten){
	  			  if(l.groupId == category)
				  {
		  			  	results.add(l);
		  			  	out.add(l);
				  }
  			  }else{
  				
  				if(subLocationCategories.size() > 0)
  				{
	  				for (int ci = 0; ci < subLocationCategories.size() ; ci++) 
	  				{
	  					Category c = subLocationCategories.get(ci);
	  					if(l.groupId == c.id)
		  				{
		  		  			  	results.add(l);
		  		  			  	out.add(l);
		  				}
	  				}
  				}else
  				{
  					
  					if(l.groupId == category)
  					{
  						results.add(l);
  		  			  	out.add(l);
  					}
  				}
  			  }
		  }
  	   }

  	  setSearchResults(results);
  	  return out;
   }
   
   public ArrayList<LocationData>searchLocations(String searchTerm,Boolean inPlaceSearch,String domain,String locationCheck,DataSource ds,ArrayList<LocationData>currentLocations)
   {
	  if(inPlaceSearch)
	  {
	  }else
	  {

   	  }
	  String pathSuffix = SettingsUtil.getProperty("webapp.root");
	  if( domain !=null && domain.length() > 0)
	  {
		pathSuffix = pathSuffix + "db/" + domain + "/";
	  }

	  ArrayList<LocationData> lList = null;
   	  if(inPlaceSearch)
   	  {
   		  lList = currentLocations;
   	  }else
   	  {
   		   lList = ServerCache.getInstance().getDC(domain).get(DomainCache.LOCATIONS3);
   		   //lList = EventDataArrayTools.readLocalEventsFromFile(localEvents);
   	  }

   	  ArrayList<Category> catListLocations =  ServerCache.getInstance().getDC(domain).get(DomainCache.CATS_LOCATIONS);
	  Vector<LocationData> results = new Vector<LocationData>();
  	  ArrayList<LocationData>out = new ArrayList<LocationData>();
  	  for (int s = 0; s < lList.size() ; s++)
  	  {
  		  LocationData l = lList.get(s);
  		  if(l !=null)
		  {
  			  String searchTerms[] = StringHelper.split(searchTerm, " ");
  			  if(StringHelper.containsAnyIgnoreCase(l.title,searchTerms))
  			  {
  			  	results.add(l);
  			  	out.add(l);
  			  }
		  }
  	   }

  	  setSearchResults(results);
  	  return out;
   }

   public ArrayList<SectionedEvents>buildSectionedEvents(Vector<EventData>src,Locale locale)
   {
	   if(src==null)
		   return null;
	   ArrayList<SectionedEvents>result = new ArrayList<SectionedEvents>();
	   
	   EventData currentElement = src.get(0);
	   Date currentDate = currentElement.start_time;
	   
	   SectionedEvents dst  = new SectionedEvents();
	   dst.getEvents().add(currentElement);
	   
	   String dformat= new SimpleDateFormat("dd. EEEE MMM",locale).format(currentElement.start_time);
	   dst.setSectionText(dformat);
		
	   Calendar startCDate = Calendar.getInstance();
	   Calendar eCDate = Calendar.getInstance();
	   
	   startCDate.setTime(currentDate);
	   startCDate.set(Calendar.HOUR, 0);
	   startCDate.set(Calendar.MINUTE, 0);
	   currentDate = startCDate.getTime();
	   
	   
	   int  dDay = startCDate.get(Calendar.DAY_OF_YEAR); 

	   for(int i = 1 ; i < src.size() ; i++)
	   {
		   EventData nextElement = src.get(i);
		
		   eCDate.setTime(nextElement.start_time);
		   eCDate.set(Calendar.HOUR, 1);
		   eCDate.set(Calendar.MINUTE, 1);
		   int  cdDay = eCDate.get(Calendar.DAY_OF_YEAR); 
		   if(maxDiff!=0)
		   {
			   if(result.size() >=maxDiff )
			   {
				   break;
			   }
		   }
		   if(cdDay!=dDay)
		   {
			   dDay=cdDay;
			   currentElement = nextElement;
			   currentDate = eCDate.getTime();
			   dst = new SectionedEvents();
			   dformat= new SimpleDateFormat("dd. EEEE MMM",locale).format(currentElement.start_time);
			   dst.setSectionText(dformat);
			   result.add(dst);
		   }
		   dst.getEvents().add(nextElement);
	   }
	   return result;
	   
   }
   public ArrayList<ArrayList<EventData>>buildTimeTree(Vector<EventData>src)
   {
	   if(src ==null || src.size() == 0 )
		   return null;

	   ArrayList<ArrayList<EventData>>result = new ArrayList<ArrayList<EventData>>();

	   EventData currentElement = src.get(0);
	   Date currentDate = currentElement.start_time;
	   ArrayList<EventData>dst = new ArrayList<EventData>();
	   dst.add(currentElement);
	   result.add(dst);
	   
	   Calendar startCDate = Calendar.getInstance();
	   Calendar eCDate = Calendar.getInstance();
	   
	   startCDate.setTime(currentDate);
	   startCDate.set(Calendar.HOUR, 0);
	   startCDate.set(Calendar.MINUTE, 0);
	   currentDate = startCDate.getTime();
	   
	   int  dDay = startCDate.get(Calendar.DAY_OF_YEAR); 

	   for(int i = 1 ; i < src.size() ; i++)
	   {
		   EventData nextElement = src.get(i);
		
		   eCDate.setTime(nextElement.start_time);
		   eCDate.set(Calendar.HOUR, 1);
		   eCDate.set(Calendar.MINUTE, 1);
		   int  cdDay = eCDate.get(Calendar.DAY_OF_YEAR); 
		   if(maxDiff!=0)
		   {
			   if(result.size() >=maxDiff )
			   {
				   break;
			   }
		   }
		   if(cdDay!=dDay)
		   {
			   dDay=cdDay;
			   currentElement = nextElement;
			   currentDate = eCDate.getTime();
			   dst = new ArrayList<EventData>();
			   result.add(dst);
			   
		   }
		   dst.add(nextElement);
	   }
	   return result;
   }

   public void printIdList(ArrayList<EventData>in)
   {
	   System.out.println("\n\n\n ----------------------");
	   for(int i = 0 ; i < in.size() ; i++)
	   {
		   EventData nextElement = in.get(i);
		   System.out.println("e : " + nextElement.uid + " " + nextElement.start_time);
	   }
   }
   
   public void printIdListArray(Object in[])
   {
	   System.out.println("\n\n\n ----------------------");
	   for(int i = 0 ; i < in.length ; i++)
	   {
		   EventData nextElement = (EventData)in[i];
		   System.out.println("e : " + nextElement.uid + " " + nextElement.start_time);
	   }
   }
   /**
    * 
    * @param allEvents
    * @param searchTerm
    * @param inPlaceSearch
    * @param domain
    * @param locationCheck
    * @param ds
    * @param currentEvents
    * @param startDate
    * @param catId
    * @return
    */
   public ArrayList<EventData>searchEventsAll(ArrayList<EventData>allEvents,
		   String searchTerm,
		   Boolean inPlaceSearch,
		   String domain,
		   String locationCheck,
		   DataSource ds,
		   ArrayList<EventData>currentEvents,
		   Date startDate,
		   int catId)
   {
	  if(inPlaceSearch)
	  {
	  }else
	  {
   	  }

	  ArrayList<EventData> elist = null;
  	  if(inPlaceSearch)
	  {
  		  elist = currentEvents;
	  }else
	  {
		  elist = allEvents;
	  }

  	  //ArrayList<LocationData> lList = ServerCache.getInstance().getDC(domain).get(DomainCache.LOCATIONS3);
  	  Vector<EventData> results = new Vector<EventData>();
  	  ArrayList<EventData>out = new ArrayList<EventData>();
  	  if(elist!=null)
  	  {
	  	  for (int s = 0; s < elist.size() ; s++)
	  	  {
	  		  EventData e = elist.get(s);
	  		  if(e !=null && e.published)
			  {
	  			  String searchTerms[] = StringHelper.split(searchTerm, " ");
	  			  if(s>845){
					  //System.out.println("test event : "  + e.title + " diff : ");
				  }
	  			  if(e.title!=null && StringHelper.containsAnyIgnoreCase(e.title,searchTerms) && e.start_time!=null)
	  			  {
	  				  
	  				  
	  				long diff  = TimeUtils.daysBetween(startDate,e.start_time);
	  				if(s>849){
	  					  //System.out.println("test event : "  + e.title + " diff : " + diff);
	  				  }
	  				if(!showPast)
	  				{
	  					if(diff >= 0 )
	  					{
	  						if(catId==0 || catId==1 || catId==-1)
	  						{
	  							
				  			  	results.add(e);
				  			  	out.add(e);
				  			  	
	  						}else if(e.groupId==catId)
	  						{
	  							results.add(e);
				  			  	out.add(e);
	  						}
	  					}
	  				}else
	  				{
	  				  	results.add(e);
		  			  	out.add(e);
	  				}
	  			  }
			  }
	  	  }
  	  }
  	   EventDataArrayTools.sort(results);
  	   setSearchResults(results);
  	   return out;
   }
   public ArrayList<EventData>searchEventsAll(String searchTerm,Boolean inPlaceSearch,String domain,String locationCheck,DataSource ds,ArrayList<EventData>currentEvents,Date startDate,int catId)
   {
	  if(inPlaceSearch)
	  {
	  }else
	  {
   	  }
	  String pathSuffix = SettingsUtil.getProperty("webapp.root");
	  if( domain !=null && domain.length() > 0)
	  {
		pathSuffix = pathSuffix + "db/" + domain + "/";
	  }

	  ArrayList<EventData> elist = null;
  	  if(inPlaceSearch)
	  {
  		  elist = currentEvents;
	  }else
	  {
		  elist = ServerCache.getInstance().getDC(domain).get(DomainCache.EVENTS_FINAL); // EventDataArrayTools.readLocalEventsFromFile2(localEvents);
	  }

  	  ArrayList<LocationData> lList = ServerCache.getInstance().getDC(domain).get(DomainCache.LOCATIONS3);
  	  Vector<EventData> results = new Vector<EventData>();
  	  ArrayList<EventData>out = new ArrayList<EventData>();
  	  if(elist!=null)
  	  {
	  	  for (int s = 0; s < elist.size() ; s++)
	  	  {
	  		  EventData e = elist.get(s);
	  		  if(e !=null)
			  {

	  			  if(StringUtils.containsString(e.title.toLowerCase(), "boat"))
	  			  {
	  				  continue;
	  			  }
	  			  
	  			  if(StringUtils.containsString(e.title.toLowerCase(), "zoo"))
	  			  {
	  				  continue;
	  			  }
	  			  if(e.loc==null)
	  			  {
	  				  e.loc = LocationArrayTools.getLocationByIndex(lList,e.locRefId);
	  			  }

	  			  String searchTerms[] = StringHelper.split(searchTerm, " ");
	  			  if(StringHelper.containsAnyIgnoreCase(e.title,searchTerms) && e.start_time!=null && e.end_time!=null)
	  			  {
	  				long diff  = TimeUtils.daysBetween(startDate,e.start_time);
	  				
	  				//System.out.println("test event : "  + e.title + " diff : " + diff);
	  				if(!showPast)
	  				{
	  					if(diff >= 0 )
	  					{
	  						if(catId==0){
				  			  	results.add(e);
				  			  	out.add(e);
	  						}else if(e.groupId==catId)
	  						{
	  							results.add(e);
				  			  	out.add(e);
	  						}
	  					}
	  				}else
	  				{
	  				  	results.add(e);
		  			  	out.add(e);
	  				}
	  			  }
			  }
	  	  }
  	  }
  	  
  	   //printIdList(elist);
  	   //Collections.sort(results,eTimeComparator);
	   //Collections.sort(out,eTimeComparator);
	   //printIdList(elist);

  	   //ArrayList<EventData>tmpR = sortByDate(results);
  	   //results.clear();
  	   //results.addAll(tmpR);
  	   EventDataArrayTools.sort(results);
  	   setSearchResults(results);
  	   //ArrayList<ArrayList<EventData>>timeTree=buildTimeTree(results);
  	   //setSearchResultsTimeTree(timeTree);
  	   return out;
   }
   public ArrayList<EventData>searchEvents(String searchTerm,Boolean inPlaceSearch,String domain,String locationCheck,DataSource ds,ArrayList<EventData>currentEvents)
   {
	  if(inPlaceSearch)
	  {
	  }else
	  {
   	  }
	  String pathSuffix = SettingsUtil.getProperty("webapp.root");
	  
	  if(domain==null)
		  domain = "ibiza";
	  
	  if( domain !=null && domain.length() > 0)
	  {
		pathSuffix = pathSuffix + "db/" + domain + "/";
	  }
	  

	  ArrayList<EventData> elist = null;
  	  if(inPlaceSearch)
	  {
  		  elist = currentEvents;
	  }else
	  {
   		   //DBTools.updateFromDB("xquery", "events",domain);
		  elist = ServerCache.getInstance().getDC(domain).get(DomainCache.EVENTS_DB); // EventDataArrayTools.readLocalEventsFromFile2(localEvents);
	  }

  	  ArrayList<LocationData> lList = null;
  	  lList = ServerCache.getInstance().getDC(domain).get(DomainCache.LOCATIONS3);

  	  Vector<EventData> results = new Vector<EventData>();
  	  ArrayList<EventData>out = new ArrayList<EventData>();

  	  if(elist!=null)
  	  {
	  	  for (int s = 0; s < elist.size() ; s++)
	  	  {
	  		  EventData e = elist.get(s);
	  		  if(e !=null)
			  {

	  			  if(e.title.trim().equals("Circo Loco"))
	  			  {
						//System.out.println("asdfsdf");
	  			  }
	  			  //e.loc = LocationArrayTools.getLocationByName(lList, "DC10");
	  			  if(e.loc==null)
	  			  {
	  				  e.loc = LocationArrayTools.getLocationByIndex(lList,e.locRefId);
	  			  }

	  			  String searchTerms[] = StringHelper.split(searchTerm, " ");
	  			  if(StringHelper.containsAnyIgnoreCase(e.title,searchTerms) && e.start_time!=null && e.end_time!=null)
	  			  {
	  				long diff  = TimeUtils.daysBetween(new Date(),e.start_time);
	  				//System.out.println("test event : "  + e.summary + " diff : " + diff);
	  				if(!showPast)
	  				{
	  					if(diff > 0)
	  					{
			  			  	results.add(e);
			  			  	out.add(e);
	  					}
	  				}else
	  				{
	  				  	results.add(e);
		  			  	out.add(e);
	  				}
	  			  }
			  }
	  	  }
  	  }

  	   Comparator<EventData> eTimeComparator = new EventDateComparator();
	   Collections.sort(results,eTimeComparator);
  	  setSearchResults(results);
  	  ArrayList<ArrayList<EventData>>timeTree=buildTimeTree(results);
  	  setSearchResultsTimeTree(timeTree);
  	  return out;
   }
   public int getCurrentPageNumber() 
   {
	return m_currentPageNumber;
   }

   public void setCurrentPageNumber(int pageNumber)
   {
		m_currentPageNumber = pageNumber;
   }
   public EventSearch()
   {

   }


   public void setFullSearchWithIn( String keyword )
   {
	   /*
	   Iterator<Picture> it = m_searchResults.iterator();

	   String currentPicIds = " AND ( ";

	   for (int i=0 ; i<m_searchResults.size() ; i++ ){


		   currentPicIds +="id_picture=" + it.next().getID_Picture();
		   if ( !(i==m_searchResults.size()-1)){
			   currentPicIds +=" OR ";
		   }

	   }

	   currentPicIds +=")";

	   m_searchString = new String(
	  "SELECT id_picture FROM Picture WHERE " +
	  "username " + "LIKE '%"+ DataMiddle.escapeSingleQuote( keyword ) + "%'" +
	  " OR "+
	  "title " + "LIKE '%"+ DataMiddle.escapeSingleQuote( keyword ) + "%'" +
	  " OR "+
	  "description " + "LIKE '%"+ DataMiddle.escapeSingleQuote( keyword ) + "%'"
 	   );

	   if (m_searchResults.size() >0 )
			  m_searchString += currentPicIds;

	   */
	   //System.out.println(m_searchString);

   }

   public ArrayList ckeyList = null;


   /*
   private void loopThru(NodeList nodeList, String parent)
   {
		boolean hasChild;

		for (int i=0;i<nodeList.length;i++){
			Node node = nodeList.item(i);

			m_recurs +=" OR keyword_id='" +String.valueOf( node.getID() ) +"'" ;
			ckeyList.add( String.valueOf( node.getID() )  );

			if(node.childNodes.length>0){
				hasChild=true;
			}else{
				hasChild=false;
			}
			if(hasChild){

				String out = ("<li "+ " id=abt >");
			}else{
			}

			if(hasChild){
				loopThru(node.childNodes,parent + "_" + i);
			}
		}
   }
   */


   public void setKeywordSearchWithFilter( String keyword_id,Vector FilterColumns )
   {
	   /*
	   KeywordMenu tmpMenu = new KeywordMenu();
	   tmpMenu.buildTopMenu(keyword_id);
	   ckeyList = new ArrayList();
	   ckeyList.add( keyword_id );

	   loopThru( tmpMenu.tview.nodes,"0" );

	   String instring = "(";

	   for (int i = 0 ; i < ckeyList.size() ; i++ ){

		   instring += String.valueOf(ckeyList.get(i));

		   if (!(i==(ckeyList.size()-1)))
			   instring += ",";
	   }

	  instring +=")";

	  String filterColumns = "";

	  for (int i = 0 ; i < FilterColumns.size() ; i++ ){

		  filterColumns += String.valueOf(FilterColumns.get(i));

		   if (!(i==(FilterColumns.size()-1)))
			   filterColumns += ",";
	   }

	  m_searchString  =
		  new String ("SELECT DISTINCT b.id_picture," + filterColumns  +  " FROM Picture b LEFT JOIN picture_keywords bs"+
			" ON (b.id_picture = bs.id_picture) WHERE bs.id_keyword in " + instring);

	  //System.out.println( m_searchString );

	  */

   }

   public void setKeywordSearch( String keyword_id,String UserName )
   {

       /*
	   KeywordMenu tmpMenu = new KeywordMenu();
	   tmpMenu.buildTopMenu(keyword_id);
	   ckeyList = new ArrayList();
	   ckeyList.add( keyword_id );

	   loopThru( tmpMenu.tview.nodes,"0" );

	   String instring = "(";

	   for (int i = 0 ; i < ckeyList.size() ; i++ ){

		   instring += String.valueOf(ckeyList.get(i));

		   if (!(i==(ckeyList.size()-1)))
			   instring += ",";
	   }

	  instring +=")";

	  m_searchString  =
		  new String ("SELECT DISTINCT b.id_picture, username FROM Picture b LEFT JOIN picture_keywords bs"+
			" ON (b.id_picture = bs.id_picture) WHERE bs.id_keyword in " + instring);
	  //System.out.println( m_searchString );
	  */
   }


   public void setHitsPerPage( int hits )   {      m_hitsPerPage = hits;   }
   public int getHitsPerPage()   {      return m_hitsPerPage;   }
   public int getPageCount()
   {
      int ret = 0;

      if( m_searchResults != null )
      {
         int resultsNum = m_searchResults.size();

         if( resultsNum > 0 )
         {
            ret = ( resultsNum / m_hitsPerPage );
         }

         if( ( resultsNum % m_hitsPerPage ) > 0 )
         {
            ret++;
         }
      }

      return ret;
   }
   public boolean hasNextPage()
   {
      return ( m_currentPageNumber < getPageCount() );
   }
   public boolean hasPreviousPage()
   {
      return ( m_currentPageNumber > 1 );
   }
   public void setSearchResults( Vector results )
   {
      //m_hitsPerPage       = 8;

      m_currentPageNumber = 0;
      m_searchResults     = null;
      m_searchString      = null;
      m_searchResults     = results;
   }
   public Vector getSearchResults()   {      return m_searchResults;   }
   public String getSearchString()
   {
      return m_searchString;
   }
   public void SetPage(String page){

	   m_currentPageNumber  = Integer.parseInt(page) - 1 ;
	   getNextPage();

   }
  public Vector getNextPage()
   {
      int    beginIndex;
      int    endIndex;
      int    size;
      Vector page = null;

      if( m_searchResults != null )
      {
         size = m_searchResults.size();

         m_currentPageNumber++;

         beginIndex = ( ( m_currentPageNumber - 1 ) * m_hitsPerPage );
         endIndex   = ( ( m_currentPageNumber * m_hitsPerPage ) - 1 );

         if( endIndex >= size )
         {
            endIndex = size - 1;
         }

         if( beginIndex < 0 )
         {
            beginIndex = 0;
         }

         if( beginIndex <= endIndex )
         {
            page = new Vector();

            for( int i = beginIndex; i <= endIndex; i++ )
            {
               page.addElement( m_searchResults.elementAt( i ) );
            }
         }
      }

      if( m_currentPageNumber > getPageCount() )
      {
         m_currentPageNumber = getPageCount();
      }

      return page;
   }

  public Vector getPreviousPage()
   {
      int    beginIndex;
      int    endIndex;
      int    size;
      Vector page = null;

      if( m_searchResults != null )
      {
         size = m_searchResults.size();

         m_currentPageNumber--;

         beginIndex = ( ( m_currentPageNumber - 1 ) * m_hitsPerPage );
         endIndex   = ( ( m_currentPageNumber * m_hitsPerPage ) - 1 );

         if( endIndex >= size )
         {
            endIndex = size - 1;
         }

         if( beginIndex < 0 )
         {
            beginIndex = 0;
         }

         if( beginIndex <= endIndex )
         {
            page = new Vector();

            for( int i = beginIndex; i <= endIndex; i++ )
            {
               page.addElement( m_searchResults.elementAt( i ) );
            }
         }
      }

      if( m_currentPageNumber < 1 )
      {
         m_currentPageNumber = 1;
      }


      return page;
   }
  public Vector getCurrentSearchResults()
  {
		return m_searchResults;
  }

  public void addSearchResults(Vector input)
  {
	  if(input == null )
		  return;

	  for( int i = 0; i< input.size() ; i++ )
      {
         this.m_searchResults.add(input.elementAt( i ) );
      }
  }

  public void setCurrentSearchResults(Vector results)
	{
		m_searchResults.clear();
		m_searchResults = results;
	}

}
