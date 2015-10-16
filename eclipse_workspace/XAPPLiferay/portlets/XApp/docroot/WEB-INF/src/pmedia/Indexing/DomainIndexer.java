package pmedia.Indexing;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
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
import pmedia.types.ArticleData;
import pmedia.types.EventData;
import pmedia.types.LocationData;
import pmedia.types.PMDataTypes;
import pmedia.types.TranslationData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class DomainIndexer 
{

	private String domain;
	private String lang;
	private String path;
	private Directory directory = null;
	private IndexWriter writer=null;
	
	
	public void indexAll() throws CorruptIndexException, IOException
	{
		/*
		if(writer.isLocked(directory))
			writer.close();
		*/
		indexLocations();
		indexEvents();
		indexJArticles();
		//writer.close();
		
	}
	public void indexEvents() throws CorruptIndexException, IOException
	{
		
		ArrayList<EventData> events= ServerCache.getInstance().getDC(domain).get(DomainCache.EVENTS_FINAL);
		if(events==null)
			return;
		
		for(int i = 0 ; i < events.size() ; i++)
		{
			EventData event = events.get(i);
			
			Document indexedDoc = IndexTools.createEventDocument(event, lang, domain);
			writer.addDocument(indexedDoc);
		}
	}
	public void indexJArticles() throws CorruptIndexException, IOException
	{
		
		ArrayList<ArticleData> articles= ServerCache.getInstance().getDC(domain).get(DomainCache.JARTICLES);
		if(articles==null)
			return;
		
		for(int i = 0 ; i < articles.size() ; i++)
		{
			ArticleData data = articles.get(i);
			if(data.translationType!=PMDataTypes.DITT_JARTICLE)
				continue;
			
			Document indexedDoc = IndexTools.createJArticleDocument(data, lang, domain);
			writer.addDocument(indexedDoc);
		}
	}
	public void finish() throws CorruptIndexException, IOException
	{
		if(writer!=null)
		{
			writer.close();
		}
	}
	public void indexLocations() throws CorruptIndexException, IOException
	{
		
		ArrayList<LocationData> locations= ServerCache.getInstance().getDC(domain).get(DomainCache.LOCATIONS3);
		if(locations==null)
			return;
		
		for(int i = 0 ; i < locations.size() ; i++)
		{
			LocationData loc = locations.get(i);
			Document indexedDoc = IndexTools.createLocationDocument(loc, lang, domain);
			writer.addDocument(indexedDoc);
		}
	}
	
	public void initIndexer()
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
		
	}
	
	public DomainIndexer(String path,String domain,String language)
	{
		
		this.domain=domain;
		this.lang=language;
		this.path=path;
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

}
