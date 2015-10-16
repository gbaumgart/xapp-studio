package pmedia.Indexing;


import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.types.LocationData;
import pmedia.types.SearchConfiguration;
import pmedia.types.SearchResult;
import pmedia.types.SearchableSourceConfiguration;
import pmedia.types.SectionedSearchResults;
import pmedia.types.TranslationData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class DomainSearch 
{

	private String domain;
	private String lang;
	private String path;
	private String query;
	private int hitsPerPage=10;
	private Directory directory = null;
	private IndexReader reader=null;
	
	
	
	public SectionedSearchResults searchWithConfiguration(SearchConfiguration config,String query,String platform) throws CorruptIndexException, IOException
	{
		SectionedSearchResults results = new SectionedSearchResults();
		if(directory==null)
		{
			initIndexer();
		}
		
		for(int i = 0 ; i < config.getSearchSourceConfigurations().size() ; i++)
		{
			SearchableSourceConfiguration sConfig = config.getSearchSourceConfigurations().get(i);
			if(sConfig.getSearchFields().size() > 0 )
			{
				ArrayList<SearchResult>sourceResults = DomainSearchUtils.searchSource(sConfig, query, getDirectory(),platform);
				if(sourceResults.size()>0)
				{
					results.getSearchResults().put(sConfig.getSourceType(), sourceResults);
				}
			}
		}
		return results;
	}
	private void initIndexer()
	{
		
		String indexDirectoryStr = path + "index/" + lang;
	    File indexDirectory = new File(indexDirectoryStr);
	    if(!indexDirectory.exists())
	    {
	    	indexDirectory.mkdirs();
	    }
	    
	    try {
			directory = new SimpleFSDirectory(indexDirectory);
		} catch (IOException e) 
		{
			e.printStackTrace();
			return;
		}
	}
	
	
	public DomainSearch(String path,String domain,String language,String query)
	{
		
		this.domain=domain;
		this.lang=language;
		this.path=path;
		this.query=query;
		
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @return the directory
	 */
	public Directory getDirectory() {
		return directory;
	}

	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	/**
	 * @return the hitsPerPage
	 */
	public int getHitsPerPage() {
		return hitsPerPage;
	}

	/**
	 * @param hitsPerPage the hitsPerPage to set
	 */
	public void setHitsPerPage(int hitsPerPage) {
		this.hitsPerPage = hitsPerPage;
	}

}
