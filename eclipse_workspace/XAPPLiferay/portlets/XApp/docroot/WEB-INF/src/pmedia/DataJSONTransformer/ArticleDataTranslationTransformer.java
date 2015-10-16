package pmedia.DataJSONTransformer;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import pmedia.AssetTools.AssetTools;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.TranslationTools;
import pmedia.types.ArticleData;
import pmedia.types.LocationData;
import pmedia.types.PMDataTypes;
import pmedia.types.TranslationData;
import pmedia.utils.StringUtils;
import flexjson.transformer.AbstractTransformer;

public class ArticleDataTranslationTransformer extends AbstractTransformer 
{

	private Boolean useRemoteServer=false;
	private String language="en";
	private String field="description";
	private String platform=pmedia.types.Constants.USERAGENT_TABLET;
	private ArticleData article=null;
	public ArticleDataTranslationTransformer(Boolean useRemoteServer,String platform,String language,ArticleData article,String field)
	{
		this.useRemoteServer = useRemoteServer;
		this.platform = platform;
		this.language = language;
		this.article  = article;
		this.field = field;
		//System.out.println("init transformer: ");
	}

	public void transform(Object object) 
	{
		
		String input=(String)object;
		TranslationData translation =null;
		//System.out.println("transforming location description :  " + location);
		
		//System.out.println("transforming article " + article.article_id + " with lang " + language);
		
		
		if(article!=null)
		{
			ArrayList<TranslationData> translations = ServerCache.getInstance().getDC("ibiza").get(DomainCache.TRANSLATIONS);
			if(translations!=null && translations.size() > 0 )
			{
				translation = TranslationTools.getTranslationByTypeAndIndex(translations,article.refId,PMDataTypes.DITT_JARTICLE,language);
				if(translation==null)
				{
					System.out.println("couldnt find article translation : " + article.refId);
				}
				if(translation!=null)
				{
					if(field.equals("description"))
					{
						if(translation.descr!=null && translation.descr.length() > 0)
						{
							input=translation.descr;
							
							if(article.getPictures()!=null)
							{
								article.getPictures().clear();
								article.setPictures(null);
							}
							article.setDescription(translation.descr);
							article.getPictures();
						}
					}
					
					if(field.equals("descriptionNoPictures"))
					{
						if(translation.descr!=null && translation.descr.length() > 0)
						{
							article.setDescriptionNoPictures(null);
							String descrRaw = translation.descr.replace("tt://", "http://");
							//article.setDescription(translation.descr);
							//article.setDescription(descrRaw);
							//String convertedLinks=StringUtils.convertLinks(descrRaw);
							Whitelist clean = Whitelist.relaxedNoPictures().addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong");
					    	clean = clean.addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong");
					    	clean = clean.addProtocols("a", "href","ftp","http","https","mtpp");
					    	//clean.("a", "rel", "nofollow")
							
							Cleaner cleaner = new Cleaner(clean);
							Document doc = Jsoup.parse(descrRaw);
							String noPics  = cleaner.clean(doc).body().html();
							
							String convertedLinks=StringUtils.convertLinks(noPics);
							//input =StringUtils.removePictures(noPics);
							//input=article.getDescriptionNoPictures();
							//input = StringUtils.removePictures(convertedLinks);
							input = convertedLinks;
							article.setDescriptionNoPictures(convertedLinks);
							//System.out.println("\n\n" + input);
							
						}
					}else{
						
						
					}
					// large description picture : 
					if(field.equals("descriptionPicture"))
					{
						if(article!=null && article.getPictures()!=null && article.getPictures().size()  > 0 )
						{
							//input = System.getProperty("imageProcessorUrl") + "/servlets/ImageScaleIcon?src=" + System.getProperty("ibiza.locationFiles")  +  article.getPictures().get(0) + "&height=180";
							//input+="&shadow=true";
							//System.out.println("loc description picture : " + input + location.location_id);
							input = AssetTools.resolvePath(article.getPictures().get(0), true, null);
							
							getContext().writeQuoted(input);
							return;
						}else
						{
							System.out.println("couldnt find article pictures: " + article.refId + " with lang " + language);
						}
						
					}
					
				}						
			}
		}
		getContext().writeQuoted(input);
	}
}
