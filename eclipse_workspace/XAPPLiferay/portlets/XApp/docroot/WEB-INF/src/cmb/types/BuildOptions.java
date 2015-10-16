package cmb.types;

public class BuildOptions 
{
	/***
	 * System Content 
	 */
	
	public String version=null;
	public String platform=null;
	public boolean includeSystemCSS=false;
	public boolean includeSystemJS=false;
	public boolean includeSystemMedia=false;
	public boolean includeSystemPlugins=false;
	
	public boolean includeApp=false;
	public boolean includeAppCSS=false;
	public boolean includeAppJS=false;
	public boolean includeAppContent=false;
	public boolean includeAppMedia=false;
	
	
	public String loggingTargets;
	public String loggingLevel;
	public String loggingIdentifier;
	public int transportType;
	
	public String rpcProxy = null;
	public String rpcProxyDomain = null;
	public String rpcProxyDomainPort = null;
	public String rpcProxyPrefix = null;
	public String dojoBaseUrl=null;
	
	public boolean chacheBust=false;
	
	public boolean includeCoreFiles=false;
	
	public int type=0;
	public String dataHost;
	public String runTimeConfiguration;
	public boolean resolveResources=true;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDataHost() {
		return dataHost;
	}
	public void setDataHost(String dataHost) {
		this.dataHost = dataHost;
	}
	public String getRunTimeConfiguration() {
		return runTimeConfiguration;
	}
	public void setRunTimeConfiguration(String runTimeConfiguration) {
		this.runTimeConfiguration = runTimeConfiguration;
	}
	public boolean isResolveResources() {
		return resolveResources;
	}
	public void setResolveResources(boolean resolveResources) {
		this.resolveResources = resolveResources;
	}
	public int getTransportType() {
		return transportType;
	}
	public void setTransportType(int transportType) {
		this.transportType = transportType;
	}
	public String getLoggingTargets() {
		return loggingTargets;
	}
	public void setLoggingTargets(String loggingTargets) {
		this.loggingTargets = loggingTargets;
	}
	public String getLoggingLevel() {
		return loggingLevel;
	}
	public void setLoggingLevel(String loggingLevel) {
		this.loggingLevel = loggingLevel;
	}
	public String getLoggingIdentifier() {
		return loggingIdentifier;
	}
	public void setLoggingIdentifier(String loggingIdentifier) {
		this.loggingIdentifier = loggingIdentifier;
	}
	public String getRpcProxy() {
		return rpcProxy;
	}
	public void setRpcProxy(String rpcProxy) {
		this.rpcProxy = rpcProxy;
	}
	public String getRpcProxyDomain() {
		return rpcProxyDomain;
	}
	public void setRpcProxyDomain(String rpcProxyDomain) {
		this.rpcProxyDomain = rpcProxyDomain;
	}
	public String getRpcProxyDomainPort() {
		return rpcProxyDomainPort;
	}
	public void setRpcProxyDomainPort(String rpcProxyDomainPort) {
		this.rpcProxyDomainPort = rpcProxyDomainPort;
	}
	public String getRpcProxyPrefix() {
		return rpcProxyPrefix;
	}
	public void setRpcProxyPrefix(String rpcProxyPrefix) {
		this.rpcProxyPrefix = rpcProxyPrefix;
	}
	public String getDojoBaseUrl() {
		return dojoBaseUrl;
	}
	public void setDojoBaseUrl(String dojoBaseUrl) {
		this.dojoBaseUrl = dojoBaseUrl;
	}
	public boolean isChacheBust() {
		return chacheBust;
	}
	public void setChacheBust(boolean chacheBust) {
		this.chacheBust = chacheBust;
	}
	public boolean isIncludeCoreFiles() {
		return includeCoreFiles;
	}
	public void setIncludeCoreFiles(boolean includeCoreFiles) {
		this.includeCoreFiles = includeCoreFiles;
	}
	public boolean isIncludeSystemCSS() {
		return includeSystemCSS;
	}
	public void setIncludeSystemCSS(boolean includeSystemCSS) {
		this.includeSystemCSS = includeSystemCSS;
	}
	public boolean isIncludeSystemJS() {
		return includeSystemJS;
	}
	public void setIncludeSystemJS(boolean includeSystemJS) {
		this.includeSystemJS = includeSystemJS;
	}
	public boolean isIncludeSystemMedia() {
		return includeSystemMedia;
	}
	public void setIncludeSystemMedia(boolean includeSystemMedia) {
		this.includeSystemMedia = includeSystemMedia;
	}
	public boolean isIncludeSystemPlugins() {
		return includeSystemPlugins;
	}
	public void setIncludeSystemPlugins(boolean includeSystemPlugins) {
		this.includeSystemPlugins = includeSystemPlugins;
	}
	public boolean isIncludeApp() {
		return includeApp;
	}
	public void setIncludeApp(boolean includeApp) {
		this.includeApp = includeApp;
	}
	public boolean isIncludeAppCSS() {
		return includeAppCSS;
	}
	public void setIncludeAppCSS(boolean includeAppCSS) {
		this.includeAppCSS = includeAppCSS;
	}
	public boolean isIncludeAppJS() {
		return includeAppJS;
	}
	public void setIncludeAppJS(boolean includeAppJS) {
		this.includeAppJS = includeAppJS;
	}
	public boolean isIncludeAppContent() {
		return includeAppContent;
	}
	public void setIncludeAppContent(boolean includeAppContent) {
		this.includeAppContent = includeAppContent;
	}
	public boolean isIncludeAppMedia() {
		return includeAppMedia;
	}
	public void setIncludeAppMedia(boolean includeAppMedia) {
		this.includeAppMedia = includeAppMedia;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}
