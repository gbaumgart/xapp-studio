package pmedia.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import pmedia.types.ApplicationConfiguration;
import pmedia.types.PictureTransformOptions;

import cmx.types.Application;

public class ApplicationContentTools 
{
	
	public static String generateCacheManifest(String uuid, String appId,String platform,String rtConfig)
	{
		String result = "";
		
		String templatePath  = System.getProperty("ServletPath") + "cache.manifest.template";
		String template=null;
		try {
			template = StringUtils.readFileAsString(templatePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(template==null){
			return null;
		}
		
		template=template.replace("%uuid%", uuid);
		if(appId!=null){
			template=template.replace("%appId%", appId);
		}
		
		String dojoFilePath = System.getProperty("xasWebRoot") +"/"  + "xapp" +  "/dojo/dojo.js";
		String dojoVersion = "";
		String cssVersion = "";
		File dojoFile=new File(dojoFilePath);
		if(rtConfig==null){
			rtConfig="release";
		}
		if(rtConfig.equals("release")){
			if(dojoFile.exists()){
				long time = dojoFile.lastModified();
				dojoVersion="?time="+time;
				cssVersion="&time="+time;
			}
		}
		
		if(rtConfig.equals("debug")){
			if(dojoFile.exists()){
				long date = new Date().getTime();
				dojoVersion="?time="+date;
				cssVersion="&time="+date;
			}
		}
		template=template.replace("%dojoVersion%", dojoVersion);
		template=template.replace("%cssVersion%", cssVersion);
		return template;
	}
	
	
	public static Whitelist relaxed() {
        return new Whitelist()
                .addTags(
                        "a", "b", "blockquote", "br", "caption", "cite", "code", "col",
                        "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6",
                        "i", "img", "li", "ol", "p", "pre", "q", "small", "strike", "strong",
                        "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u",
                        "ul")

                .addAttributes("a", "href", "title")
                .addAttributes("blockquote", "cite")
                .addAttributes("col", "span", "width")
                .addAttributes("colgroup", "span", "width")
                .addAttributes("img", "align", "alt", "height", "src", "title", "width","height")
                .addAttributes("ol", "start", "type")
                .addAttributes("q", "cite")
                .addAttributes("table", "summary", "width")
                .addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width")
                .addAttributes(
                        "th", "abbr", "axis", "colspan", "rowspan", "scope",
                        "width")
                .addAttributes("ul", "type")
                .addProtocols("a", "href", "ftp", "http", "https", "mailto","tt")
                .addProtocols("blockquote", "cite", "http", "https","tt")
                .addProtocols("q", "cite", "http", "https","tt")
                ;
    }
	public static String relaxedHTML(String input)
    {
    	if(input==null || input.length()==0)
			return null;
		Whitelist clean = relaxed();
		Cleaner cleaner = new Cleaner(clean);
		Document doc = Jsoup.parse(input);
		return cleaner.clean(doc).body().html();
    }
	
	public static String generateImageResizeUrl(String input,ApplicationConfiguration appSettings,PictureTransformOptions pictureOptions){
		
		String result;
		String imageProcessorUrl = null;
		if(appSettings.getImageProcessorUrl() !=null)
		{
			imageProcessorUrl = appSettings.getImageProcessorUrl();
		}
		String siteUrl=null;
		if(appSettings !=null && appSettings.getWww() !=null)
		{
			siteUrl = appSettings.getWww();
		}
		
		if(imageProcessorUrl==null)
		{
			imageProcessorUrl=System.getProperty("imageProcessorUrl");
		}
		if(!input.contains("http")){
    		result=imageProcessorUrl + "servlets/ImageScaleIcon?src=" + siteUrl  + input;
    	}else{
    		result=imageProcessorUrl + "servlets/ImageScaleIcon?src=" + input;
    	}
		
		if(pictureOptions.addShadow){
			result+="&shadow=true";
		}
		if(pictureOptions.resizeHeight!=null){
			result+="&height="+pictureOptions.resizeHeight;
		}
		
		if(pictureOptions.resizeWidth!=null){
			result+="&width="+pictureOptions.resizeWidth;
		}
		
		
		return result;
	}
	
	public static String transformPictures(Application app,String input,PictureTransformOptions pictureOptions)
	{
		
		Document doc = Jsoup.parse(input);
		//Elements pngs = doc.select("img[src~=.(png|jpg)]");
		Elements pngs = doc.select("img");
		String imageProcessorUrl = null;
		if(app.getAppSettings()!=null && app.getAppSettings().getImageProcessorUrl() !=null)
		{
			imageProcessorUrl = app.getAppSettings().getImageProcessorUrl();
		}
		String siteUrl=null;
		if(app.getAppSettings()!=null && app.getAppSettings().getWww() !=null)
		{
			siteUrl = app.getAppSettings().getWww();
		}
		
		for (Element img : pngs)
		{
		        String url = img.attr("src");
		        String width = img.attr("width");
		        String height= img.attr("height");
		        String newWidth = null;
		        if(url!=null){
		        	
		        	url = generateImageResizeUrl(url, app.getAppSettings(), pictureOptions);
		        	img.attr("src", url);
		        }
		        
		        //System.out.println("img : url" + url +  " w:   " + width + " h: " + height);
		        Element parent = img.parent();
		        if(parent!=null){
		        	Elements innerNodes = parent.getElementsByTag("img");
		        	
		      
		        	if(innerNodes !=null && innerNodes.size()>0){
		        		//if(innerNodes.get(0) == img){
		        			newWidth ="100%";
		        		//}
		        	}else{
		        		
		        	}
		        	
		        }

		        if(newWidth==null){
		        	img.removeAttr("width");
		        }else{
		        	img.attr("width",newWidth);
		        }
		        img.removeAttr("height");
		}
		return doc.body().html();
	}
	public static String transformHTMLText(Application app,String input,PictureTransformOptions pictureOptions)
	{
		if(app==null)
			return null;
		
		if(input==null)
			return null;
		
		String mobileNormalized = relaxedHTML(input);
		String picturesTransformed = ApplicationContentTools.transformPictures(app, mobileNormalized,pictureOptions);
		
		return picturesTransformed;
	}

}
