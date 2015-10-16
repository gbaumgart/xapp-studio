package pmedia.Indexing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import pmedia.AssetTools.AssetTools;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.LocationArrayTools;
import pmedia.DataUtils.TranslationTools;
import pmedia.types.ArticleData;
import pmedia.types.Category;
import pmedia.types.EventData;
import pmedia.types.LocationData;
import pmedia.types.PMDataTypes;
import pmedia.types.PMSearchFieldTypes;
import pmedia.types.PMSearchSourceType;
import pmedia.types.SearchResult;
import pmedia.types.SectionedSearchResults;
import pmedia.types.TranslationData;
import pmedia.utils.StringUtils;


public class IndexTools 
{

	public static String updateSearchResultIcons(SectionedSearchResults results,String platform,String domain,String lang)
	{
		String result="";
		
		Map<PMSearchSourceType, ArrayList<SearchResult>>searchResults = results.getSearchResults();
	
		Set<Entry<PMSearchSourceType, ArrayList<SearchResult>>>set=searchResults.entrySet();
		
		for(Map.Entry<PMSearchSourceType, ArrayList<SearchResult>> entry : set)
		{			
			for(int i = 0 ; i < entry.getValue().size() ; i++ )
			{
				SearchResult res = entry.getValue().get(i);
				//System.out.println(res.iconUrl);
				/*
				if(res.iconUrl==null)
				{
					res.iconUrl=IndexTools.getSearchResultIcon(res, platform, domain);
					System.out.println(res.iconUrl);
				}else
				{
					
				}
				*/
				
				res.iconUrl=IndexTools.getSearchResultIcon(res, platform, domain,lang);
			}
		}

		
		//List<String> keyList = new ArrayList<String>;

		//Set<PMSearchSourceType> e = searchResults.keySet();
		//iterate through Hashtable keys Enumeration
		/*
		while(e.hasMoreElements())
		System.out.println(e.nextElement());
		*/
		return result;
	}
	public static String getSearchResultIcon(SearchResult result,String platform,String domain,String lang)
	{
		String icon = result.iconUrl;
		if(icon!=null)
		{
			return icon;
		}
		
		if(result.type.equals(""+PMSearchSourceType.PMSST_LOCATIONS))
		{
			//try category icon first
	    	ArrayList<Category> cListAll =  ServerCache.getInstance().getDC(domain).get(DomainCache.CATS_LOCATIONS);
	    	if(cListAll!=null)
	    	{
	    		Category cat = CategoryTools.getCatByIndex(cListAll, result.cid);
	    		if(cat!=null && cat.getIconUrl()!=null && cat.getIconUrl().length() > 0)
	    		{
	    			icon = AssetTools.resolvePath(cat.getIconUrl(), true, null) ;
	    		}
	    	}
	    	if(icon==null)
	    	{
	    		icon="images/theme/web/defaultLocation.png";
	    	}
		}
		
		if(result.type.equals(""+PMSearchSourceType.PMSST_ARTICLES))
		{
			//try category icon first
			//if(result.subType)
	    	ArrayList<ArticleData> cListAll =  ServerCache.getInstance().getDC(domain).get(DomainCache.JARTICLES);
	    	if(cListAll!=null)
	    	{
	    		//ArrayList<ArticleData> lList = ServerCache.getInstance().getDC("ibiza").get(cacheLookupType);
	          	ArticleData article = ArticleTools.getArticleByTypeAndIndex(cListAll,result.id, PMDataTypes.DITT_JARTICLE);
	    		//ArticleData article = ArticleTools.getArticleByTypeAndIndex(cListAll, result.id, type).getCatByIndex(cListAll, result.cid);
	    		if(article!=null && article.getIconUrl()!=null && article.getIconUrl().length() > 0)
	    		{
	    			icon = AssetTools.resolvePath(article.getIconUrl(), true, null) ;
	    			
	    			
	    			if(icon!=null)
	    			{
	    				icon=AssetTools.resolveIconPath(icon,platform,false, null);
	    				
	    				//result.iconUrl=ResourceTools.resolvePath(result.iconUrl,true, null);
	    			}
	    			
	    		}else
	    		{
	    			icon = ArticleTools.getIconUrlFromJArticleTranslation(domain, article, lang);
	    			//System.out.println("icon url : " + icon);
	    			if(icon!=null)
	    			{
	    				//result.iconUrl=ResourceTools.resolveIconPath(icon,platform,true, null);
	    				//result.iconUrl=ResourceTools.resolvePath(result.iconUrl,true, null);
	    				//sult.iconUrl=ResourceTools.resolvePath(icon,true, null);
	    			}
	    		}
	    	}
	    	if(icon==null)
	    	{
	    		icon="images/theme/web/defaultLocation.png";
	    	}
		}
		
		/*
		if(result.iconUrl!=null && result.iconUrl.length() > 0)
		{
			result.iconUrl=ResourceTools.resolveIconPath(result.iconUrl,platform,true, null);
			if(result.iconUrl==null)
			{
				result.iconUrl=ResourceTools.resolvePath(result.iconUrl,true, null);
			}
		}
		*/
		if(result.iconUrl!=null)
		{
			System.out.println("have icon  : " + result.iconUrl);
		}
		
		return icon;
		
	}
	public static SearchResult convertToSearchResultSmall(Document doc,String platform)
	{
		if(doc==null)
			return null;

		SearchResult result = new pmedia.types.SearchResult();
		result.title = doc.get(""+PMSearchFieldTypes.PM_SFT_TITLE);
		result.type= doc.get(""+PMSearchFieldTypes.PM_SFT_TYPE);
		result.subType= doc.get(""+PMSearchFieldTypes.PM_SFT_SUBTYPE);
		result.city= doc.get(""+PMSearchFieldTypes.PM_SFT_CITY);
		result.category= doc.get(""+PMSearchFieldTypes.PM_SFT_CATEGORY);
		result.iconUrl= doc.get(""+PMSearchFieldTypes.PM_SFT_ICON);
		
		try {
			result.id = Integer.parseInt(doc.get(""+PMSearchFieldTypes.PM_SFT_ID));	
		} catch (Exception e) {
			
		}
		
		try {
			result.cid = Integer.parseInt(doc.get(""+PMSearchFieldTypes.PM_SFT_CAT_ID));	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	
		if(result.iconUrl!=null && result.iconUrl.length() > 0)
		{
			
			if(!result.iconUrl.contains("http"))
			{
				result.iconUrl=AssetTools.resolveIconPath(result.iconUrl,platform,true, null);
				if(result.iconUrl==null)
				{
					result.iconUrl=AssetTools.resolvePath(result.iconUrl,true, null);
				}
			}
		}
		//result.iconUrl = IndexTools.getSearchResultIcon(result, platform);
		//result.iconUrl = doc.get(""+PMSearchFieldTypes.PM_SFT_ICON));
		
		
		//result.description= doc.get("title");
		return result;
	}
	
	public static Document createEventDocument(EventData data,String lang,String domain)
	{
		if(data==null)
			return null;
		 
		Document doc = new Document();
		TranslationData translation =null;
		 //doc.add(new Field("title", "ibiza", Field.Store.YES, Field.Index.ANALYZED));
		
		
		String title = data.title;
		String description = StringUtils.removeHTML(data.description);
		String descriptionExtra = null;
		String iconUrl=null;
		
		if(data.getPictures()!=null && data.getPictures().size() > 0)
		{
			iconUrl=data.getPictures().get(0);
		}
		
		if(iconUrl==null && data.getIconUrl()!=null && data.getIconUrl().length() > 0)
		{
			iconUrl = data.getIconUrl();
		}
		

		
		/**
		 * Write now indexable properties into document !
		 */
		
		if(title!=null && title.length() > 0 )
			doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_TITLE, title, Field.Store.YES, Field.Index.ANALYZED));
		
		if(description!=null && description.length() > 0 )
			doc.add(new Field("" + PMSearchFieldTypes.PM_SFT_DESCRIPTION , description, Field.Store.YES, Field.Index.ANALYZED));
		
		if(descriptionExtra!=null && descriptionExtra.length() > 0 )
			doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_DESCRIPTION_EXTRA, descriptionExtra, Field.Store.YES, Field.Index.ANALYZED));
		
		/**
		 * Write now non-indexable properties into document !
		 */
		
		if(iconUrl!=null && iconUrl.length() > 0 )
			doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_ICON, iconUrl, Field.Store.YES, Field.Index.NOT_ANALYZED));
		
		doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_ID, ""+data.uid, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_CAT_ID, ""+data.groupId, Field.Store.YES, Field.Index.NOT_ANALYZED));

		// important : write the data type out
		doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_TYPE,""+PMSearchSourceType.PMSST_EVENTS, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_SUBTYPE, ""+PMDataTypes.DITT_JEVENT, Field.Store.YES, Field.Index.NOT_ANALYZED));
		
		return doc;
	}
	public static Document createJArticleDocument(ArticleData data,String lang,String domain)
	{
		if(data==null)
			return null;
		 
		Document doc = new Document();
		TranslationData translation =null;

		String title = data.title;
		String description = StringUtils.removeHTML(data.description);
		String iconUrl=null;
		
		if(data.getPictures()!=null && data.getPictures().size() > 0)
		{
			iconUrl=data.getPictures().get(0);
		}

		ArrayList<TranslationData> translations = ServerCache.getInstance().getDC(domain).get(DomainCache.TRANSLATIONS);
		
		/**
		 * Use Joomla translation : 
		 */
		
		if(translations!=null)
		{
			// try Joomla translation
			translation = TranslationTools.getTranslationByTypeAndIndex(translations,data.refId,PMDataTypes.DITT_JARTICLE,lang);
			
			if(translation!=null && translation.descr!=null && translation.descr.length() > 0 )
			{
				description= StringUtils.removeHTML(translation.descr);
			}
			
			if(translation!=null && translation.title!=null && translation.title.length() > 0 )
			{
				title= translation.title;
			}
		}
		
		/**
		 * Write now indexable properties into document !
		 */
		
		if(title!=null && title.length() > 0 )
			doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_TITLE, title, Field.Store.YES, Field.Index.ANALYZED));
		
		if(description!=null && description.length() > 0 )
			doc.add(new Field("" + PMSearchFieldTypes.PM_SFT_DESCRIPTION , description, Field.Store.YES, Field.Index.ANALYZED));
		
		
		/**
		 * Write now non-indexable properties into document !
		 */
		if(iconUrl!=null && iconUrl.length() > 0 )
			doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_ICON, iconUrl, Field.Store.YES, Field.Index.NOT_ANALYZED));
		
		doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_ID, ""+data.refId, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_CAT_ID, ""+data.groupId, Field.Store.YES, Field.Index.NOT_ANALYZED));

		// important : write the data type out
		doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_TYPE,""+PMSearchSourceType.PMSST_ARTICLES, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_SUBTYPE, ""+PMDataTypes.DITT_JARTICLE, Field.Store.YES, Field.Index.NOT_ANALYZED));
		
		return doc;
	}
	public static Document createLocationDocument(LocationData data,String lang,String domain)
	{
		if(data==null)
			return null;
		 
		Document doc = new Document();
		TranslationData translation =null;
		if(!data.didMapping)
		{
			LocationArrayTools.updateLocationMappedArticle(data, lang, domain);
		}
		 //doc.add(new Field("title", "ibiza", Field.Store.YES, Field.Index.ANALYZED));
		
		
		String title = data.title;
		String description = StringUtils.removeHTML2(data.description);
		String descriptionExtra = null;
		String iconUrl=null;
		String categoryTitle = null;
		
		if(title.equals("Underground")){
			
			System.out.println("have");
		
		}
		
		if(data.getPictures()!=null && data.getPictures().size() > 0)
		{
			iconUrl=data.getPictures().get(0);
		}
		/**
		 * Look for a mapped article : 
		 */
		ArrayList<TranslationData> translations = ServerCache.getInstance().getDC(domain).get(DomainCache.TRANSLATIONS);
		if(data!=null && data.mappedArticle!=null)
		{
			if(translations!=null && translations.size() > 0 )
			{
				translation = TranslationTools.getTranslationByTypeAndIndex(translations,data.mappedArticle.refId,PMDataTypes.DITT_ARTICLE,lang);
				if(translation!=null)
				{
					if(translation.descr!=null && translation.descr.length() > 0)
					{
						description=StringUtils.removeHTML(translation.descr);
					}
					if(translation.observ!=null && translation.observ.length() > 0)
						descriptionExtra=StringUtils.removeHTML(translation.observ);
				}						
			}
		}
		/**
		 * If there is no mapped article, use Joomla translation : 
		 */
		
		if(data.mappedArticle==null && translations!=null)
		{
			// try Joomla translation
			translation = TranslationTools.getTranslationByTypeAndIndex(translations,data.location_id,PMDataTypes.DITT_JLOCATION,lang);
			
			if(translation!=null && translation.descr!=null && translation.descr.length() > 0 )
			{
				description= StringUtils.removeHTML(translation.descr);
			}
			
			if(translation!=null && translation.title!=null && translation.title.length() > 0 )
			{
				title= translation.title;
			}
		}
		
		/**
		 * Find location's category  		
		 */
		ArrayList<Category> cListAll =  ServerCache.getInstance().getDC(domain).get(DomainCache.CATS_LOCATIONS);
		if(cListAll!=null){
			Category cat = CategoryTools.getCatByIndex(cListAll, data.groupId);
			if(cat!=null){
				categoryTitle=TranslationTools.translateCategory(domain,lang,cat.refId);
				if(categoryTitle==null)
				{
					categoryTitle=cat.title;
					//System.out.println("found location title : " + title + " c: " + categoryTitle  );
				}
			}
		}
		/**
		 * Write now indexable properties into document !
		 */
		
		if(title!=null && title.length() > 0 )
			doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_TITLE, title, Field.Store.YES, Field.Index.ANALYZED));
		
		if(description!=null && description.length() > 0 )
			doc.add(new Field("" + PMSearchFieldTypes.PM_SFT_DESCRIPTION , description, Field.Store.YES, Field.Index.ANALYZED));
		
		if(categoryTitle!=null && categoryTitle.length() > 0 )
			doc.add(new Field("" + PMSearchFieldTypes.PM_SFT_CATEGORY , categoryTitle, Field.Store.YES, Field.Index.ANALYZED));
		
		if(descriptionExtra!=null && descriptionExtra.length() > 0 )
			doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_DESCRIPTION_EXTRA, descriptionExtra, Field.Store.YES, Field.Index.ANALYZED));
		
		if(data.city!=null && data.city.length() > 0 )
			doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_CITY, data.city, Field.Store.YES, Field.Index.ANALYZED));
		
		if(data.phone!=null && data.phone.length() > 0 )
			doc.add(new Field("phone", data.phone, Field.Store.YES, Field.Index.ANALYZED));

		if(data.pcode!=null && data.pcode.length() > 0 )
			doc.add(new Field("pcode", data.pcode, Field.Store.YES, Field.Index.ANALYZED));
		
		/**
		 * Write now non-indexable properties into document !
		 */
		if(iconUrl!=null && iconUrl.length() > 0 )
			doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_ICON, iconUrl, Field.Store.YES, Field.Index.NOT_ANALYZED));
		
		doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_ID, ""+data.location_id, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_CAT_ID, ""+data.groupId, Field.Store.YES, Field.Index.NOT_ANALYZED));

		// important : write the data type out
		doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_TYPE,""+PMSearchSourceType.PMSST_LOCATIONS, Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(""+PMSearchFieldTypes.PM_SFT_SUBTYPE, ""+PMDataTypes.DITT_JLOCATION, Field.Store.YES, Field.Index.NOT_ANALYZED));
		
		return doc;
	}
	 
	 
	public static void createIndex(String lang) throws CorruptIndexException, IOException
	{
		// 0. Specify the analyzer for tokenizing text.
	    //    The same analyzer should be used for indexing and searching
	    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
	    
	    String indexDirectoryStr = System.getProperty("webapp.root") + "index/" + lang;
	    File indexDirectory = new File(indexDirectoryStr);
	    if(!indexDirectory.exists())
	    {
	    	indexDirectory.mkdirs();
	    }
	    
	    Directory directory = null; 
	    
	    try {
			directory = new SimpleFSDirectory(indexDirectory);
		} catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
		IndexWriter writer = null ; 
		 
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_31, new StandardAnalyzer(Version.LUCENE_31));
		    
		try {
			//writer = new IndexWriter(directory, analyzer, true,IndexWriter.MaxFieldLength.UNLIMITED);
			writer = new IndexWriter(directory,indexWriterConfig );
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		IndexReader red = null ; 
		try {
			red = IndexReader.open(directory, false);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int totDocs = red.maxDoc();
		try {
			red.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*
		 Document doc = new Document();
		 
		 doc.add(new Field("title", "ibiza", Field.Store.YES, Field.Index.ANALYZED));
		 writer.addDocument(doc);
		 
		 doc = new Document();
		 doc.add(new Field("title", "space", Field.Store.YES, Field.Index.ANALYZED));
		 
		 writer.addDocument(doc);
		 
		 writer.close();
		 
		 */
		
		/*
		 String querystr = "ibiza";
		 Query q = null;
		 try {
			q = new QueryParser(Version.LUCENE_CURRENT, "title", analyzer).parse(querystr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int hitsPerPage = 10;
		IndexSearcher searcher = new IndexSearcher(directory, true);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		System.out.println("Found " + hits.length + " hits.");
		for(int i=0;i<hits.length;++i) {
		    int docId = hits[i].doc;
		    Document d = searcher.doc(docId);
		    System.out.println((i + 1) + ". " + d.get("title"));
		}
		 
		*/

		 writer.close();
	    
	    
	}
}
