package cmx.data;

import java.io.IOException;
import java.util.ArrayList;

import com.liferay.portal.kernel.json.JSONDeserializer;

import cmx.cache.DataSourceCache;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import flexjson.JSONSerializer;

import pmedia.html.types.WhiteListData;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.CContent;
import pmedia.types.CList;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;

public class CContentStorageUtils 
{
	public static void saveAsCContentData(
			ApplicationManager appManager,
			Application app,
			DataSourceCache dsc,
			DataSourceBase ds,
			ArrayList data,
			String format,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			ECMContentSourceType cType,
			WhiteListData whiteList,
			String platform,
			String language)
	{
		
		String savepath = appManager.getUserAppPath(uuid, app.getApplicationIdentifier());
		savepath+="/datasources/" + ds.getHost() + "/" + ds.getDatabase() + "/";
		
		saveAsCContentData(data, savepath, format, uuid, applicationId, dsUID, cType, null, platform, language);
		
	}
	public static void saveAsCContentData(
			ArrayList articles,
			String path,
			String format,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			ECMContentSourceType cType,
			WhiteListData whiteList,
			String platform,
			String language)
	{
		CContent fcontent=null;
		ArrayList<CContent>cArticles = new ArrayList<CContent>();
		for(int i = 0 ; i < articles.size() ; i ++)
		{
			BaseData data = (BaseData) articles.get(i);
			CContent content = CContentFactory.fromArticleData(data, uuid, applicationId, dsUID, cType, whiteList, platform,language, 0);
			if(content!=null)
			{
				CContentFactory.setCustomFields(content, data, uuid, applicationId, dsUID, cType, platform, language);
				cArticles.add(content);
				fcontent = content;
			}
		}
		
		String dstPath= "c_" + ECMContentSourceTypeTools.TypeToInteger(cType) + "_." + format;
		JSONSerializer serializer = new JSONSerializer();
		serializer.exclude("items.asListItem");

		CContentList list = new CContentList();
		list.setItems(cArticles);
		String serialized = serializer.deepSerialize(list);
		
		/*serializer.exclude("asListItem");
		serializer.exclude("items.didEventSearch");
		serializer.exclude("items.didPictureSearch");
		serializer.exclude("items.didStatusTest");
		serializer.exclude("items.didMapping");
		serializer.exclude("items.didMapping");
		serializer.exclude("items.class");
		*/
		serialized = serialized.replaceAll("[\"][a-zA-Z0-9_]*[\"]:null[ ]*[,]?", "");
		//serialized = serialized.replaceAll("[\"][a-zA-Z0-9_]*[\"]:null[ ]*[,]?", "");
		//JsonNullRegEx = "[\"][a-zA-Z0-9_]*[\"]:null[ ]*[,]?";
		
		
		try {
			StringUtils.writeToFile(serialized, path + dstPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveAsCCList(
			ArrayList articles,
			String path,
			String format,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			ECMContentSourceType cType,
			WhiteListData whiteList,
			String platform,
			String language)
	{
		String dstPath= "c_" + ECMContentSourceTypeTools.TypeToInteger(cType) + "_." + format;
		JSONSerializer serializer = new JSONSerializer();
		CList list = new CList();
		list.setItems(articles);
		String serialized = serializer.deepSerialize(list);
		serializer.exclude("asListItem");
		try {
			StringUtils.writeToFile(serialized, path + dstPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
