package pmedia.Indexing;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import pmedia.types.PMDataTypes;
import pmedia.types.PMSearchFieldTypes;
import pmedia.types.PMSearchSourceType;
import pmedia.types.SearchResult;
import pmedia.types.SearchableSourceConfiguration;
import pmedia.utils.StringUtils;


public class DomainSearchUtils 
{
	public static String[] toFields(SearchableSourceConfiguration config)
	{
		if(config==null)
			return new String[0];
		
		String result [] = new String[config.getSearchFields().size()];
		
		for(int i = 0 ; i < config.getSearchFields().size() ; i++)
		{
			PMSearchFieldTypes type = config.getSearchFields().get(i);
			
			if(type == PMSearchFieldTypes.PM_SFT_TITLE)
			{
				result[i]="title";
			}
			if(type == PMSearchFieldTypes.PM_SFT_DESCRIPTION)
			{
				result[i]="description";
			}
			if(type == PMSearchFieldTypes.PM_SFT_DESCRIPTION_EXTRA)
			{
				result[i]="descriptionExtra";
			}
			
			if(type == PMSearchFieldTypes.PM_SFT_CATEGORY)
			{
				result[i]="category";
			}
		}
		return result;
	}
	public static PMDataTypes searchSourceTypeToDataType(PMSearchSourceType srcType)
	{
		if(srcType==PMSearchSourceType.PMSST_LOCATIONS)
		{
			return PMDataTypes.DITT_JLOCATION;
		}
		return PMDataTypes.DITT_UNKNOWN;
	}

	public static Boolean hasResult(ArrayList<SearchResult>testArray,SearchResult testItem)
	{
		if(testArray.size()>0)
		{
			for(int i = 0 ; i<testArray.size() ; i++)
			{
				SearchResult srcItem = testArray.get(i);
				if(srcItem.id==testItem.id /*&&srcItem.type==testItem.type*/ )
				{
					return true;
				}
			}
		}
		
		return false;
	}
	public static ArrayList<SearchResult>mergeFieldResults(ArrayList<SearchResult>dst,ArrayList<SearchResult>src)
	{
		if(src.size()>0)
		{
			for(int i = 0 ; i<src.size() ; i++)
			{
				SearchResult resultToAdd = src.get(i);
				if(!DomainSearchUtils.hasResult(dst, resultToAdd))
				{
					dst.add(resultToAdd);
				}
				
			}
		}
		return dst;
	}
	/*
	private static final Analyzer[] analyzers = new Analyzer[]{
        new WhitespaceAnalyzer(Version.LUCENE_34),
        new SimpleAnalyzer(Version.LUCENE_34),
        new StopAnalyzer(Version.LUCENE_34),
        new StandardAnalyzer(Version.LUCENE_34),
        new SnowballAnalyz("English", StopAnalyzer.ENGLISH_STOP_WORDS),
    };
	*/
	public static ArrayList<SearchResult> searchByField(SearchableSourceConfiguration config,String query,Directory directory,PMSearchFieldTypes fieldType,String platform) throws CorruptIndexException, IOException
	{
		
		ArrayList<SearchResult>results = new ArrayList<SearchResult>();
		
		BooleanQuery q = new BooleanQuery();
		
		// always add type constraint 
		q.add(new TermQuery(new Term(""+PMSearchFieldTypes.PM_SFT_TYPE,""+ config.getSourceType())),BooleanClause.Occur.MUST);
		//now the actual field
		
		if(fieldType==PMSearchFieldTypes.PM_SFT_TITLE && config.getSourceType()==PMSearchSourceType.PMSST_LOCATIONS)
		{
			//System.out.println("title search");
		}
		
		query=query.trim();
		
		if(query.contains(" "))
		{
			query=query.replace(" ", ",");
		}
		
		float minimumSimilarity = 0.5f;
		int prefixLength = 0;//query.length()/3;
		int maxExpansions = Integer.MAX_VALUE;
		if(!query.contains("*"))
		{
			query+="*";
		}
		
		if(fieldType!=PMSearchFieldTypes.PM_SFT_TYPE)
		{
			if(query.length()<3)
			{
				q.add(new PrefixQuery(new Term(""+fieldType,query)),BooleanClause.Occur.MUST);
				
			}else
			{
				String splittedTerm[] = StringUtils.toArray(query,",");
				if(splittedTerm.length==1)
				{
					//q.add(new TermQuery(new Term(""+fieldType,query)),BooleanClause.Occur.MUST);
					//FuzzyQuery(term, minimumSimilarity, 0, Integer.MAX_VALUE)}.
					//q.add(new FuzzyQuery(new Term(""+fieldType,query)),BooleanClause.Occur.MUST);
					q.add(new FuzzyQuery(new Term(""+fieldType,query), minimumSimilarity, prefixLength, maxExpansions), BooleanClause.Occur.MUST);
					
				}
				
				if(splittedTerm.length>1)
				{
					for(int ti = 0 ; ti<splittedTerm.length ; ti++)
					{
						if(ti==0)
						{
							//q.add(new TermQuery(new Term(""+fieldType,splittedTerm[ti])),BooleanClause.Occur.MUST);
							q.add(new FuzzyQuery(new Term(""+fieldType,splittedTerm[ti]), minimumSimilarity, prefixLength, maxExpansions), BooleanClause.Occur.MUST);
							
						}else
						{
							//q.add(new TermQuery(new Term(""+fieldType,splittedTerm[ti])),BooleanClause.Occur.MUST);
							q.add(new FuzzyQuery(new Term(""+fieldType,splittedTerm[ti]), minimumSimilarity, prefixLength, maxExpansions), BooleanClause.Occur.MUST);
						}
						
					}
				}
			}
		}
	
		
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		IndexSearcher searcher = new IndexSearcher(directory, true);
	
		
		if(config.getHighlightTerm() && fieldType==PMSearchFieldTypes.PM_SFT_DESCRIPTION || fieldType==PMSearchFieldTypes.PM_SFT_DESCRIPTION_EXTRA)
		{
			QueryScorer scorer = new QueryScorer(q, "" + fieldType);
			
			SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class=" + config.getHighlightFragmentClass() +">" , "</span>"); 
		    Highlighter highlighter = new Highlighter(formatter,scorer);
		    TopDocs hits2 = searcher.search(q, config.getHitsPerPage());
		    highlighter.setTextFragmenter(new SimpleFragmenter(config.getHighlightFragmentLength()));
		    
		    for (int i = 0; i < hits2.scoreDocs.length; i++) {
		        Document doc = searcher.doc(hits2.scoreDocs[i].doc);
	
		        String storedField = doc.get("" + fieldType);
		        
		        TokenStream stream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), hits2.scoreDocs[i].doc, ""+fieldType, doc, analyzer);
		        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
	
		        highlighter.setTextFragmenter(fragmenter);
		        String fragment=null;
				try {
					fragment = highlighter.getBestFragment(stream, storedField);
				} catch (InvalidTokenOffsetsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				SearchResult res = IndexTools.convertToSearchResultSmall(doc,platform);
			    res.matchingField=fieldType;
			    if(fragment!=null && fragment.length() > 0)
			    {			    	
			    	if(fieldType==PMSearchFieldTypes.PM_SFT_DESCRIPTION)
					{
			    		res.description=fragment;
					}
			    	if(fieldType==PMSearchFieldTypes.PM_SFT_DESCRIPTION_EXTRA)
					{
			    		res.descriptionExtra=fragment;
					}
			    }
			    results.add( res );
		        
			  //  System.out.println(fragment);
		      }
		}		
		
		
		//IndexSearcher searcher = new IndexSearcher(directory, true);
		//TopScoreDocCollector collector = TopScoreDocCollector.create(config.getHitsPerPage(), true);

		if(!config.getHighlightTerm() && fieldType!=PMSearchFieldTypes.PM_SFT_DESCRIPTION || fieldType!=PMSearchFieldTypes.PM_SFT_DESCRIPTION_EXTRA)
		{
			TopFieldCollector collector = TopFieldCollector.create(Sort.RELEVANCE,config.getHitsPerPage(), true,true,false,false);
			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			for(int i=0;i<hits.length;++i) {
			    int docId = hits[i].doc;
			    Document d = searcher.doc(docId);
			  //  System.out.println((i + 1) + ". " + d.get("" + PMSearchFieldTypes.PM_SFT_TITLE) + " id : " + d.get("id") + d.get(""+PMSearchFieldTypes.PM_SFT_SUBTYPE) );
			    SearchResult res = IndexTools.convertToSearchResultSmall(d,platform);
			    res.matchingField=fieldType;
			    results.add( res );
			}
		}
		return results;
	}
	
	public static ArrayList<SearchResult> searchSource(SearchableSourceConfiguration config,String query,Directory directory,String platform) throws CorruptIndexException, IOException
	{
		ArrayList<SearchResult>results = new ArrayList<SearchResult>();
		BooleanQuery q = new BooleanQuery();
		//Query nameOrDescQuery = Query.mergeBooleanQueries((BooleanQuery[]) new Query[] { q, q});
		
		//
		
		for(int i = 0 ; i < config.getSearchFields().size() ; i++)
		{
			PMSearchFieldTypes fType = config.getSearchFields().get(i);
			ArrayList<SearchResult>fieldResults = searchByField(config, query, directory, fType,platform);
			if(fieldResults!=null && fieldResults.size() > 0)
			{
				results = DomainSearchUtils.mergeFieldResults(results, fieldResults);
			}
		}
		
		/*
		q.add(new TermQuery(new Term(""+PMSearchFieldTypes.PM_SFT_TYPE,""+ config.getSourceType())),BooleanClause.Occur.MUST);
		
		// now field by field
		if(config.getSearchFields().contains(PMSearchFieldTypes.PM_SFT_TITLE))
		{
			q.add(new TermQuery(new Term(""+PMSearchFieldTypes.PM_SFT_TITLE,query)),BooleanClause.Occur.MUST);
		}
		
		//MultiFieldQueryParser.parse(Version.LUCENE_CURRENT, queries, fields, analyzer)

		IndexSearcher searcher = new IndexSearcher(directory, true);
		//TopScoreDocCollector collector = TopScoreDocCollector.create(config.getHitsPerPage(), true);
		TopFieldCollector collector = TopFieldCollector.create(Sort.RELEVANCE,config.getHitsPerPage(), true,true,false,false);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		*/
		/*
		QueryScorer scorer = new QueryScorer(q, "title");
	    Highlighter highlighter = new Highlighter(scorer);
	    TopDocs hits2 = searcher.search(q, 10);
	    
	    for (int i = 0; i < hits2.scoreDocs.length; i++) {
	        Document doc = searcher.doc(hits2.scoreDocs[i].doc);
	        String storedField = doc.get("title");

	        TokenStream stream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), hits2.scoreDocs[i].doc, "title", doc, analyzer);
	        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);

	        highlighter.setTextFragmenter(fragmenter);

	        String fragment=null;
			try {
				fragment = highlighter.getBestFragment(stream, storedField);
			} catch (InvalidTokenOffsetsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        //System.out.println(fragment);
	      }
	    */
		
		//System.out.println("\n Found " + results.size());
		/*
		for(int i=0;i<hits.length;++i) {
		    int docId = hits[i].doc;
		    Document d = searcher.doc(docId);
		    List fl = d.getFields();
		    System.out.println((i + 1) + ". " + d.get("" + PMSearchFieldTypes.PM_SFT_TITLE));
		    SearchResult res = indexTools.convertToSearchResultSmall(d);
		    results.add( res );
		}
		*/
		return results;
	}
}
