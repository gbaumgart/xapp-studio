package pmedia.DataJSONTransformer;

import java.util.ArrayList;

import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.TranslationTools;
import pmedia.types.Category;
import pmedia.types.LocationData;
import pmedia.types.PMDataTypes;
import pmedia.types.TranslationData;
import flexjson.transformer.AbstractTransformer;

public class CategoryDataTranslationTransformer extends AbstractTransformer 
{

	private Boolean useRemoteServer=false;
	private String language="en";
	private String field="description";
	private String platform=pmedia.types.Constants.USERAGENT_TABLET;
	private Category category=null;
	
	public CategoryDataTranslationTransformer(Boolean useRemoteServer,String platform,String language,Category cat,String field)
	{
		this.useRemoteServer = useRemoteServer;
		this.platform = platform;
		this.language = language;
		this.category = cat;
		this.field = field;
	}

	public void transform(Object object) 
	{
		
		String input=(String)object;
		/*
		TranslationData translation =null;
		//System.out.println("transforming location description :  " + location);
		if(location!=null && location.mappedArticle!=null)
		{
			ArrayList<TranslationData> translations = ServerCache.getInstance().getDC("ibiza").get(DomainCache.TRANSLATIONS);
			if(translations!=null && translations.size() > 0 )
			{
				translation = TranslationTools.getTranslationByTypeAndIndex(translations,location.mappedArticle.article_id,PMDataTypes.DITT_ARTICLE,language);
				if(translation!=null)
				{
					
					if(field.equals("description"))
					{
						if(translation.descr!=null && translation.descr.length() > 0)
						{
							input=translation.descr;
						}
					}
					if(field.equals("descriptionExtra"))
					{
						if(translation.observ!=null && translation.observ.length() > 0)
							input=translation.observ;
					}
				}						
			}
		}

		if(field.equals("description"))
		{
			if(location!=null && location.mappedArticle==null)
			{
				if(location.getDescriptionNoPictures()!=null && location.getDescriptionNoPictures().length()  > 0 )
				{
					input = location.getDescriptionNoPictures();
				}else
				{
					input = "No Data";
				}
			}
		}
		*/
		getContext().writeQuoted(input);
	}
}
