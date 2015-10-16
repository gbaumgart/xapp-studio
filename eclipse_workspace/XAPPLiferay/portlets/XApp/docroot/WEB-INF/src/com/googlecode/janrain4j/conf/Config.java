/* Copyright 2010 Marcel Overdijk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.janrain4j.conf;

import java.util.Arrays;
import java.util.List;

/**
 * User-configurable properties. In most cases it is not needed to create a 
 * <code>Config</code> manually. To create a <code>Config</code> 
 * programatically, the recommended way is to statically import 
 * {@link Config.Builder}.* and invoke the static build method followed by the 
 * desired instance mutators:
 * <blockquote>
 * <pre>
 * import static com.googlecode.janrain4j.conf.Config.Builder.*;
 * 
 * ...
 * 
 * // specify API key
 * Config config = build().apiKey(apiKey);
 * 
 * // specify API key and proxy
 * Config config = build().apiKey(apiKey).proxy(proxyHost, proxyPort);
 * 
 * // overview of all configurable properties
 * Config config = build()
 *         .apiKey(apiKey)
 *         .applicationID(applicationID)
 *         .applicationDomain(applicationDomain)
 *         .tokenUrl(tokenUrl)
 *         .languagePreference(languagePreference)
 *         .proxyHost(proxyHost)
 *         .proxyPort(proxyHost)
 *         .proxyUsername(proxyUsername)
 *         .proxyPassword(proxyPassword)
 *         .connectTimeout(connectTimeout)
 *         .readTimeout(readTimeout)
 *         .setStatusProviderNames(setStatusProviderNames)
 *         .activityProviderNames(activityProviderNames);
 * </pre>
 * </blockquote>
 * 
 * @author Marcel Overdijk
 * @since 1.0
 */
public class Config {

    public static List<String> DEFAULT_SET_STATUS_PROVIDER_NAMES = Arrays.asList("Facebook", "LinkedIn", "Twitter", "MySpace", "Google", "Yahoo!");
    public static List<String> DEFAULT_ACTIVITY_PROVIDER_NAMES = Arrays.asList("Facebook", "LinkedIn", "Twitter", "MySpace", "Yahoo!");
    
    private String apiKey = System.getProperty("janrain.apiKey");
    private String applicationID = System.getProperty("janrain.applicationID");
    private String applicationDomain = "https://ibiza-pearls.rpxnow.com/";
    private String tokenUrl = System.getProperty("janrain.tokenUrl ");
    private String languagePreference = null;
    private String proxyHost = null;
    private int proxyPort = -1;
    private String proxyUsername = null;
    private String proxyPassword = null;
    private int connectTimeout = -1;
    private int readTimeout = -1;
    private List<String> setStatusProviderNames = DEFAULT_SET_STATUS_PROVIDER_NAMES;
    private List<String> activityProviderNames = DEFAULT_ACTIVITY_PROVIDER_NAMES;
    
    Config() {
    }
    
    /**
     * Returns the Janrain API key.
     */
    public String getApiKey() {
        return apiKey;
    }
    
    /**
     * Returns the Janrain application ID.
     */
    public String getApplicationID() {
        return applicationID;
    }
    
    /**
     * Returns the Janrain application domain.
     */
    public String getApplicationDomain() {
    	return System.getProperty("janrain.applicationDomain");
    }
    
    /**
     * Returns the token url.
     */
    public String getTokenUrl() {
        return System.getProperty("janrain.tokenUrl");
    }
    
    /**
     * Returns the language preference.
     */
    public String getLanguagePreference() {
        return languagePreference;
    }
    
    /**
     * Returns the proxy host.
     */
    public String getProxyHost() {
        return proxyHost;
    }
    
    /**
     * Returns the proxy port.
     */
    public int getProxyPort() {
        return proxyPort;
    }
    
    /**
     * Returns the proxy username.
     */
    public String getProxyUsername() {
        return proxyUsername;
    }
    
    /**
     * Returns the proxy password.
     */
    public String getProxyPassword() {
        return proxyPassword;
    }
    
    /**
     * Returns the connect timeout.
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }
    
    /**
     * Returns the read timeout.
     */
    public int getReadTimeout() {
        return readTimeout;
    }
    
    /**
     * Returns the provider names which support the <code>set_status</code> API call.
     */
    public List<String> getSetStatusProviderNames() {
        return setStatusProviderNames;
    }
    
    /**
     * Returns the provider names which support the <code>activity</code> API call.
     */
    public List<String> getActivityProviderNames() {
        return activityProviderNames;
    }
    
    /**
     * Sets the Janrain API key.
     * 
     * @param apiKey Your Janrain API key.
     * @return <code>this</code> (for chaining)
     */
    public Config apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }
    
    /**
     * Sets the Janrain application ID.
     * 
     * @param applicationID The application ID.
     * @return <code>this</code> (for chaining)
     */
    public Config applicationID(String applicationID) {
        this.applicationID = applicationID;
        return this;
    }
    
    /**
     * Sets the Janrain application domain.
     * 
     * @param applicationDomain The application domain.
     * @return <code>this</code> (for chaining)
     */
    public Config applicationDomain(String applicationDomain) {
        this.applicationDomain = applicationDomain;
        return this;
    }
    
    /**
     * Sets the token url.
     * 
     * @param tokenUrl The token url.
     * @return <code>this</code> (for chaining)
     */
    public Config tokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
        return this;
    }
    
    /**
     * Sets the language preference.
     * 
     * @param languagePreference The language preference.
     * @return <code>this</code> (for chaining)
     */
    public Config languagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
        return this;
    }
    
    /**
     * Sets the proxy host.
     * 
     * @param host The proxy host.
     * @return <code>this</code> (for chaining)
     */
    public Config proxyHost(String host) {
        this.proxyHost = host;
        return this;
    }
    
    /**
     * Sets the proxy port.
     * 
     * @param port The proxy port.
     * @return <code>this</code> (for chaining)
     */
    public Config proxyPort(int port) {
        this.proxyPort = port;
        return this;
    }
    
    /**
     * Sets the proxy username.
     * 
     * @param username The proxy username.
     * @return <code>this</code> (for chaining)
     */
    public Config proxyUsername(String username) {
        this.proxyUsername = username;
        return this;
    }
    
    /**
     * Sets the proxy password.
     * 
     * @param password The proxy password.
     * @return <code>this</code> (for chaining)
     */
    public Config proxyPassword(String password) {
        this.proxyPassword = password;
        return this;
    }
    
    /**
     * Set the connect timeout, in milliseconds, to be used when opening the 
     * communications link to the resource.
     * <p>
     * 0 implies that the option is disabled (i.e., timeout of infinity).
     * </p>
     * 
     * @param timeout The connect timeout value in milliseconds.
     * @return <code>this</code> (for chaining)
     */
    public Config connectTimeout(int timeout) {
        this.connectTimeout = timeout;
        return this;
    }
    
    /**
     * Sets the read timeout, in milliseconds, to be used when reading from the 
     * input steam.
     * <p>
     * 0 implies that the option is disabled (i.e., timeout of infinity).
     * </p>
     * 
     * @param timeout The read timeout value in milliseconds.
     * @return <code>this</code> (for chaining)
     */
    public Config readTimeout(int timeout) {
        this.readTimeout = timeout;
        return this;
    }
    
    /**
     * Sets the provider names which support the <code>set_status</code> API call.
     * 
     * @param providerNames The provider names.
     * @return <code>this</code> (for chaining)
     * @since 1.1
     */
    public Config setStatusProviderNames(List<String> providerNames) {
        this.setStatusProviderNames = providerNames;
        return this;
    }
    
    /**
     * Sets the provider names which support the <code>activity</code> API call.
     * 
     * @param providerNames The provider names.
     * @return <code>this</code> (for chaining)
     * @since 1.1
     */
    public Config activityProviderNames(List<String> providerNames) {
        this.activityProviderNames = providerNames;
        return this;
    }
    
    /**
     * Contains static creation methods for <code>Config</code>. 
     */
    public static class Builder {

        private Builder() {
        }
        
        /**
         * Create a <code>Config</code> with default values.
         * 
         * @return The newly created <code>Config</code> instance.
         */
        public static Config build() {
            return new Config();
        }
    }
}
