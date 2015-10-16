package cmx.tools;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lesscss.LessCompiler;
import org.lesscss.LessException;

import net.sf.j2ep.ProxyFilter;
import net.sf.j2ep.rules.DirectoryRule;
import net.sf.j2ep.servers.BaseServer;
import pmedia.utils.StringUtils;
import cmx.FolderKeyResolverUtil;
import cmx.server.FileServer;
import cmx.types.FolderKeys;
import cmx.types.Resource;
import cmx.types.ResourceInclude;
import cmx.types.Resources;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class ResourceUtil 
{
	
	
	public static Resource findResource(Resources resources,String url,String path,String type)
	{
		
		for(int i= 0 ; i < resources.getItems().size() ; i ++)
		{
			Resource r = resources.getItems().get(i);
			if(r.url!=null && r.type!=null && r.path!=null)
			{
				if(r.url.equals(url) && r.type.equals(type) && r.path.equals(path)){
					return r;
				}
			}
		}
		return null;
	}
	
	
	public static Resources addResource(String path,Resource newResource)
	{
		
		return null;
	}
	
	public static String resolveConstantsAbsolute(String path,String uuid ,String appId)
	{
			HashMap<String, String> tokens = FolderKeyResolverUtil.getKeyMapAbsolute(appId, uuid);
			
			String template = "" + path;
			
			String patternString = "%(" + org.apache.commons.lang.StringUtils.join(tokens.keySet(), "|") + ")%";
			
			Pattern pattern = Pattern.compile(patternString);
			
			Matcher matcher = pattern.matcher(template);

			StringBuffer sb = new StringBuffer();
			
			String start=null;
			
			
			while(matcher.find()) 
			{
				try 
				{
					String match = tokens.get(matcher.group(1));
					if(start==null){
						start = match;
					}
					
					if(start.equals(match))
					{
						matcher.appendReplacement(sb, start);
					}else{
						matcher.appendReplacement(sb, start + (new File(start).toURI().relativize(new File(match).toURI()).getPath()));
					}
				} catch (Exception e) 
				{
				}
			}
			matcher.appendTail(sb);
			String result = sb.toString();
			
			result.replace("///", "/");
			result.replace("//", "/");
			return result;
	}

	
	public static String registerFileProxies(Resources resources,String appId,String uuid,String systemAppName,String type)
	{
		if(resources==null)
		{
			return "";
		}

		for(int i= 0 ; i < resources.getItems().size() ; i ++)
		{
			Resource r = resources.getItems().get(i);
			if(r.url!=null && r.type!=null && r.type.length() > 0)
			{
				
				if(!r.enabled){
					continue;
				}
				
				if(type.equals("FILE_PROXY"))
				{
					if(r.type.equals("FILE_PROXY"))
					{
						if(r.path!=null)
						{
							//System.out.println("register proxy : " + r.url + " at : " + r.path);
							
							URI uri = null;
							String path = "";
							String domain = null;
							
							String url= r.url;
							if(!url.contains("http://")){
								url = "http://" + r.url;
							}
							try {
								uri = new URI(url);
							} catch (URISyntaxException e) 
							{
								e.printStackTrace();
								continue;
							}

							
							if(uri!=null){
								domain = uri.getHost();
								path=uri.getPath();
							}
							
							FileServer a=null;

							
							BaseServer s = null;
							try{
								s= ProxyFilter.getFileServer( r.name , r.name,"");
							}catch(Exception e){
								
							}
							if(s!=null)
							{
								//System.out.println("registered server @ " + r.mountPoint);
							}else
							{
								FileServer baseServer = new FileServer();
								baseServer.setPath(path);
								baseServer.setIsRewriting("true");
								
								
								
								if(uri.getPort()!=0){
									domain=domain+":"+uri.getPort();
								}
								baseServer.setDomainName(r.name);
								baseServer.setPath(path);
								
								DirectoryRule rule = new DirectoryRule();
								rule.setDirectory(r.path);
								
								baseServer.setRule(rule);
								net.sf.j2ep.ServerChain schain =net.sf.j2ep.ProxyFilter.getServerChain();
								if(schain!=null){
									schain.addServer(baseServer);
								}else{
									System.out.println("have no server chain");
								}
								
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public static String registerProxies(Resources resources,String appId,String uuid,String systemAppName,String type)
	{
		try {
			
		if(resources==null)
		{
			return "";
		}

		for(int i= 0 ; i < resources.getItems().size() ; i ++)
		{
			Resource r = resources.getItems().get(i);
			if(r.url!=null && r.type!=null && r.type.length() > 0)
			{
				if(!r.enabled){
					continue;
				}
				
				if(type.equals("PROXY"))
				{
					if(r.type.equals("PROXY"))
					{
						if(r.path!=null)
						{
							//System.out.println("register proxy : " + r.url + " at : " + r.path);
							
							URI uri = null;
							String path = "";
							String domain = null;
							
							String url= resolveConstantsAbsolute(r.url, null, null);
							if(!url.contains("http://")){
								url = "http://" + r.url;
							}
							try {
								uri = new URI(url);
							} catch (URISyntaxException e) 
							{
								e.printStackTrace();
								continue;
							}
							
							if(uri!=null){
								domain = uri.getHost();
								path=uri.getPath();
							}

							BaseServer s = ProxyFilter.getServer( domain , "", path);
							if(s!=null)
							{
								//System.out.println("registered server @ " + r.mountPoint);
							}else
							{
								BaseServer baseServer = new BaseServer();
								baseServer.setIsRewriting("true");
								
								
								
								if(uri.getPort()!=0){
									domain=domain+":"+uri.getPort();
								}
								baseServer.setDomainName(domain);
								baseServer.setPath(path);
								
								DirectoryRule rule = new DirectoryRule();
								rule.setDirectory(r.path);
								
								baseServer.setRule(rule);
								ProxyFilter.getServerChain().addServer(baseServer);		
							}
						}
					}
				}
			}
		}
		} catch (Exception e) {
		
		}
		
		return null;
	}
	
	public static Resource compileLessResource(Resource resource,String appId,String uuid,String rtConfig){
		
		String fileResolved = resolveConstantsAbsolute(resource.urlOri, uuid, appId);
		File f = new File(fileResolved);
		if(!f.exists() || f.isDirectory())
		{
			return resource;
		}
		System.out.println("compiling less resource : " + fileResolved);
		String lessContent= "";
		String lessContentFinal= "";
		
		try {
			lessContent = StringUtils.readFileAsString(fileResolved);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			return resource;
		}
		
		
		String localDir = StringUtils.path(fileResolved);
		String localFileName = StringUtils.filenameComplete(fileResolved);
		
		localFileName=localFileName.replace(".less", ".css");
		LessCompiler lessCompiler = new LessCompiler();
		
		String functionsPath  = "%XASWEB%/lib/cssMobile/themes/iphone/functions.less";
		functionsPath = resolveConstantsAbsolute(functionsPath, uuid, appId);
		String functionsContent="";
		try {
			functionsContent = StringUtils.readFileAsString(functionsPath);
			
		} catch (IOException e1) {
			
		}
		
		//lessContent = functionsContent + lessContent;
		
		String variablesPath  = "%XASWEB%/lib/cssMobile/themes/iphone/variables.less";
		variablesPath= resolveConstantsAbsolute(variablesPath, uuid, appId);
		String variablesContent="";
		try {
			variablesContent= StringUtils.readFileAsString(variablesPath);
			
		} catch (IOException e1) {
			
		}
		variablesContent=variablesContent.replace("@import \"functions\";","");
		//lessContent = variablesContent + lessContent;
		
		lessContentFinal+=functionsContent;
		lessContentFinal+=variablesContent;
		lessContentFinal+=lessContent;
		
		
		File tmpFileIn  = new File(localDir+"/"+"lesstemp.less");
		if(tmpFileIn.exists()){
			tmpFileIn.delete();
		}
		try {
			StringUtils.writeToFile(lessContentFinal, localDir+"/"+"lesstemp.less");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File tmpFile = new File(localDir+"/"+"lesstemp.less");
		
		
		try {
			lessCompiler.compile(tmpFile, new File(localDir+"/" + localFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			tmpFile.delete();
			e.printStackTrace();
			return resource;
		} catch (LessException e) {
			// TODO Auto-generated catch block
			tmpFile.delete();
			e.printStackTrace();
			return resource;
		}
		
		tmpFile.delete();
		resource.url =resource.url.replace(".less", ".css");
		
		return resource;
		// Compile LESS input string to CSS output string
		//String css = lessCompiler.compile("@color: #4D926F; #header { color: @color; }");
		

		// Or compile LESS input file to CSS output file
		//lessCompiler.compile(new File("main.less"), new File("main.css"));
	}
	public static String toHTML(Resources resources,String appId,String uuid,String systemAppName,String type,String rtConfig)
	{
			if(resources==null)
			{
				return "";
			}
			
			StringBuffer sb = new StringBuffer();
			for(int i= 0 ; i < resources.getItems().size() ; i ++)
			{
				Resource r = resources.getItems().get(i);
				if(r.url!=null && r.type!=null && r.type.length() > 0)
				{
					if(!r.enabled){
						continue;
					}
				
					if(type.equals("CXAPP"))
					{
						if(r.type.equals("CXAPP"))
						{
							String cxAppBootResolved = resolveConstantsAbsolute(r.urlOri, uuid, appId);
							File f = new File(cxAppBootResolved);
							if(!f.exists() || !f.isDirectory())
							{
								continue;
							}
							
							String cxAppBoot="cxapp/boot";
							
							if(rtConfig!=null && rtConfig.equals("release"))
							{
								String appBootPathResolved = resolveConstantsAbsolute(r.urlOri+"/boot-release.js", uuid, appId);
								if(appBootPathResolved!=null)
								{
									f = new File(appBootPathResolved);
									
									if(f.exists())
									{
										cxAppBoot="cxapp/boot-release";
									}else{
										//continue;
										
										/***
										 * Try from debug
										 */
										String appBootPathResolvedDebug = resolveConstantsAbsolute(r.urlOri+"/boot.js", uuid, appId);
										if(appBootPathResolvedDebug!=null)
										{
											f = new File(appBootPathResolvedDebug);
											if(f.exists())
											{
												cxAppBoot="cxapp/boot";
											}else{
												continue;
											}
										}
										
									}
								}
							}
							String htmlString = "<script type=\"text/javascript\">\n";
								htmlString += "var cxAppId=\"" + r.name+"\";\n";
								htmlString +="var cxAppRoot=\"" + r.url+"\";\n";
								htmlString +="var cxAppBoot=\"" + cxAppBoot +"\";\n";
								htmlString+="\n</script>";
							sb.append(htmlString);
						
						}
					}
					
					
					if(type.equals("head"))
					{
						if(r.type.equals("CSS") && r.isEnabled())
						{
							String urlSuffix="";

							
							if(r.url!=null && r.url.contains(".less")){
								System.out.println("have less");
								r = compileLessResource(r, appId, uuid, rtConfig);
							}
							
							//update manifest caching
							if(r.addFileTime){
								String resolvedLocal = resolveConstantsAbsolute(r.urlOri, uuid, appId);
								if(resolvedLocal!=null){
									File fileLocal = new File(resolvedLocal);
									if(fileLocal.exists()){
										long time = fileLocal.lastModified();
										urlSuffix+="?time="+time;
									}
								}
							}
							String htmlString = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + r.url  + urlSuffix +"\" />\n";
							sb.append(htmlString);
						}
						
						if(r.type.equals("JS-HEADER-INCLUDE") && r.isEnabled())
						{
							String htmlString  = "<script type=\"text/javascript\" charset=\"utf-8\" src=\"" + r.url +"\"></script>\n";
							sb.append(htmlString);
						}
					}
					
					if(type.equals("JS-HEADER-SCRIPT-TAG"))
					{
						if(r.type.equals("JS-HEADER-SCRIPT-TAG"))
						{
							String pathResolved = resolveConstantsAbsolute(r.urlOri, uuid, appId);
							if(pathResolved!=null)
							{
								File f = new File(pathResolved);
								
								if(f.exists())
								{
									String htmlString = "<script type=\"text/javascript\">\n";
									String data = null;
									
									try {
										data = StringUtils.readFileAsString(pathResolved);
									} catch (IOException e) {
										e.printStackTrace();
									}
									
									if(data!=null)
									{
										htmlString+=data;
									}
									htmlString+="\n</script>";
									sb.append(htmlString);
								}
							}
						}
					}
					
				}
			}
			return sb.toString();
		}
	public static String resolveConstantsEx(String path,String uuid ,String appId,HashMap<String, String> dynamicTokens)
	{
		
		HashMap<String, String> tokens = FolderKeyResolverUtil.getKeyMap(appId, uuid);
		
		/***
		 * Merge them now
		 */
		tokens.putAll(dynamicTokens);

		//replace in tokens with dynamic tokens  
		for (String x : dynamicTokens.keySet()) 
		{
			if(tokens.get(x) !=null)
			{
				tokens.put(x, dynamicTokens.get(x));
			}
		}
		/***
		 * Resolve values
		 */
		tokens = resolveValues(tokens, appId, uuid);
		
		String template = "" + path;
		String patternString = "%(" + org.apache.commons.lang.StringUtils.join(tokens.keySet(), "|") + ")%";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(template);

		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
		    matcher.appendReplacement(sb, tokens.get(matcher.group(1)));
		}
		matcher.appendTail(sb);
		
		return sb.toString();
	}
	
	public static HashMap<String, String> resolveValues(HashMap<String, String> tokens,String appId,String uuid)
		{
			HashMap<String, String> constants = FolderKeyResolverUtil.getKeyMap(appId, uuid);
			
			for (String x : tokens.keySet()) 
			{
				String template = "" + tokens.get(x);
				String patternString = "%(" + org.apache.commons.lang.StringUtils.join(constants.keySet(), "|") + ")%";
				Pattern pattern = Pattern.compile(patternString);
				Matcher matcher = pattern.matcher(template);

				StringBuffer sb = new StringBuffer();
				while(matcher.find()) {
				    matcher.appendReplacement(sb, tokens.get(matcher.group(1)));
				}
				matcher.appendTail(sb);
				String newValue=sb.toString();
				tokens.put(x, newValue);
			}
			return tokens;
		}
	
	public static String resolveConstants(String path,String uuid ,String appId)
	{
			
			HashMap<String, String> tokens = FolderKeyResolverUtil.getKeyMap(appId, uuid);
			
			String template = "" + path;
			String patternString = "%(" + org.apache.commons.lang.StringUtils.join(tokens.keySet(), "|") + ")%";
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(template);

			StringBuffer sb = new StringBuffer();
			while(matcher.find()) 
			{
			    matcher.appendReplacement(sb, tokens.get(matcher.group(1)));
			}
			matcher.appendTail(sb);
			return sb.toString();
	}
	
	public static boolean hasResource(Resources resources,Resource resource)
	{
		
		return false;
	}
	
	public static Resources merge(Resources resources)
	{
		return null;
	}
	
	public static Resources resolveIncludes(Resources resources,String appId,String uuid,String systemAppName)
	{
		if(resources==null)
		{
			resources = new Resources();
		}
		
		for(int i= 0 ; i < resources.getIncludes().size() ; i ++)
		{
			ResourceInclude include = resources.getIncludes().get(i);
			if(include.path!=null)
			{
				String pathResolved = resolveConstantsAbsolute(include.path, uuid, appId);
				
				//System.out.println("resource include full path : " + pathResolved);

				File resourceFile =  new File(pathResolved);
				if(resourceFile.exists())
				{
					Resources includedResources= ResourceUtil.fromPath(pathResolved);
					if(includedResources!=null)
					{
						resources.merge(includedResources);
						
						if(includedResources.getIncludes()!=null && includedResources.getIncludes().size() > 0)
						{
							resources=resolveIncludes(includedResources, appId, uuid, systemAppName);
						}
					}
				}
			}
		}
		return resources;
	}
	public static ArrayList<Resource> getResourcesByType(Resources resources,String type)
	{
			if(resources==null){
				return null;
			}
			
			ArrayList<Resource>result=new ArrayList<Resource>();
			
			for(int i= 0 ; i < resources.getItems().size() ; i ++)
			{
				Resource r = resources.getItems().get(i);
				if(r.type !=null && r.type.equals(type))
				{
					result.add(r);
				}
			}
			
			if(result.size()>0){
				return result;
			}
			
			
			return null;
	}
	public static Resource getResourceByType(Resources resources,String type)
	{
			if(resources==null){
				return null;
			}
			
			for(int i= 0 ; i < resources.getItems().size() ; i ++)
			{
				Resource r = resources.getItems().get(i);
				if(r.type !=null && r.type.equals(type))
				{
					return r;
				}
			}
			return null;
	}
	
	public static Resources resolveEx(Resources resources,String uuid,String appId,String systemAppName,HashMap<String, String> dynamicTokens)
	{
			if(resources==null){
				return resources;
			}
			
			Resources includedResources = resolveIncludes(resources, appId, uuid, systemAppName);
			if(includedResources !=null && includedResources.getItems() !=null && includedResources.getItems().size()>0)
			{
				resources.merge(includedResources);
			}
			
			for(int i= 0 ; i < resources.getItems().size() ; i ++)
			{
				Resource r = resources.getItems().get(i);
				if(r.url!=null)
				{
					r.urlOri=""+ r.url;
					r.url = resolveConstantsEx(r.url, uuid, appId,dynamicTokens);
					r.url=r.url.replace("//", "/");
					r.url=r.url.replace("http:/", "http://");
				}
				
				if(r.type !=null && r.type.equals("CXAPP"))
				{
					if(r.url!=null)
					{
						if(r.url.startsWith(uuid))
						{
							r.url="../../"+r.url;
							
							//r.path = resolveConstants(r.path, uuid, appId);
						}
					}
				}
				
				if(r.type !=null && r.type.equals("PROXY"))
				{
					if(r.path!=null)
					{
						r.path = resolveConstants(r.path, uuid, appId);
						//System.out.println("new path for " + r.path + " is : " + r.path);
					}
					
					if(r.mountPoint!=null)
					{
						r.mountPoint = resolveConstants(r.mountPoint, uuid, appId);
						//System.out.println("new mount point for " + r.mountPoint + " is : " + r.mountPoint);
					}
				}
			}
			return resources;
	}
	public static Resources resolve(Resources resources,String uuid,String appId,String systemAppName)
	{
			if(resources==null){
				return resources;
			}
			
			Resources includedResources = resolveIncludes(resources, appId, uuid, systemAppName);
			if(includedResources !=null && includedResources.getItems() !=null && includedResources.getItems().size()>0)
			{
				resources.merge(includedResources);
			}
			
			HashMap<String, String> dynamicTokens = new HashMap<String, String>();
			
			if(resources.distDir!=null)
			{
				dynamicTokens.put(FolderKeys.CM_DIST, resources.distDir);
			}
			
			for(int i= 0 ; i < resources.getItems().size() ; i ++)
			{
				Resource r = resources.getItems().get(i);
				if(r.url!=null)
				{
					r.urlOri=""+ r.url;
					r.url = resolveConstantsEx(r.url, uuid, appId,dynamicTokens);
				//	System.out.println("new url for " + r.url + " is : " + r.urlOri);
				}
				
				if(r.type !=null && r.type.equals("CXAPP"))
				{
					if(r.url!=null && uuid!=null)
					{
						if(r.url.startsWith(uuid))
						{
							r.url="../../"+r.url;
							//r.path = resolveConstants(r.path, uuid, appId);
						}
					}
				}
				
				if(r.type !=null && r.type.equals("PROXY"))
				{
					if(r.path!=null)
					{
						r.path = resolveConstants(r.path, uuid, appId);
						//System.out.println("new path for " + r.path + " is : " + r.path);
					}
					
					if(r.mountPoint!=null)
					{
						r.mountPoint = resolveConstants(r.mountPoint, uuid, appId);
						//System.out.println("new mount point for " + r.mountPoint + " is : " + r.mountPoint);
					}
				}
			}
			return resources;
		}
	public static void saveEx(Resources res , String path,Boolean restoreUrl)
	{
		if(res==null)
		{
			return;
		}
		
		if(restoreUrl)
		{
			JSONSerializer serializer = new JSONSerializer();
			
			String ser = serializer.deepSerialize(res);
			if(ser!=null)
			{
				JSONDeserializer dserializer = new JSONDeserializer<Resources>();
				Resources resources = (Resources)dserializer.deserialize(ser);
				if(resources!=null)
				{
					for(int i= 0 ; i < resources.getItems().size() ; i ++)
					{
						Resource r = resources.getItems().get(i);
						r.url=r.urlOri;
						
					}
					save(resources, path);
				}
			}
		}else{
			save(res, path);
		}
	}
	
	public static void save(Resources res , String path)
	{
		if(res==null)
		{
			return;
		}
		
		JSONSerializer serializer = new JSONSerializer();
		try 
		{
		
			serializer.prettyPrint(true);
			StringUtils.writeToFile(serializer.deepSerialize(res), path);
		
		} catch (IOException e1) 
		{
			
		}
	}
	
	public static Resources fromPath(String path)
	{
			JSONDeserializer dserializer = new JSONDeserializer<Resources>();
			Resources resources = null;
			
			String data = null;
			try {
				data = StringUtils.readFileAsString(path);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				
			}
			if(data==null){
				return null;
			}
			
			try 
			{
	   			resources = (Resources)dserializer.deserialize(data);
			} catch (Exception e) {
				
			}
			return resources;
		
	}
		
}

