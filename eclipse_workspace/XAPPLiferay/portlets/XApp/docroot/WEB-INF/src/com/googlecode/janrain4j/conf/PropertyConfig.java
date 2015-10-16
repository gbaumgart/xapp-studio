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

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Marcel Overdijk
 * @since 1.0
 */
class PropertyConfig extends Config {

    public static final String JANRAIN4J_PROPERTIES = "janrain4j.properties";
    public static final String KEY_PREFIX = "janrain4j.";
    
    public static final String API_KEY_KEY = KEY_PREFIX + "api.key";
    public static final String APPLICATION_ID_KEY = KEY_PREFIX + "application.id";
    public static final String APPLICATION_DOMAIN_KEY = KEY_PREFIX + "application.domain";
    public static final String TOKEN_URL_KEY = KEY_PREFIX + "token.url";
    public static final String LANGUAGE_PREFERENCE_KEY = KEY_PREFIX + "language.preference";
    public static final String PROXY_HOST_KEY = KEY_PREFIX + "proxy.host";
    public static final String PROXY_PORT_KEY = KEY_PREFIX + "proxy.port";
    public static final String PROXY_USERNAME_KEY = KEY_PREFIX + "proxy.username";
    public static final String PROXY_PASSWORD_KEY = KEY_PREFIX + "proxy.password";
    public static final String CONNECT_TIMEOUT_KEY = KEY_PREFIX + "connect.timeout";
    public static final String READ_TIMEOUT_KEY = KEY_PREFIX + "read.timeout";
    public static final String SET_STATUS_PROVIDER_NAMES_KEY = KEY_PREFIX + "set.status.provider.names";
    public static final String ACTIVITY_PROVIDER_NAMES_KEY = KEY_PREFIX + "activity.provider.names";
    
    private Log log = LogFactory.getLog(this.getClass());
    
    public PropertyConfig() {
        this(JANRAIN4J_PROPERTIES);
    }
    
    public PropertyConfig(String location) {
        log.info("Loading janrain4j properties file from: " + location + "..." + "  : " +getClass().getResource("/"));
        
        loadProperties(getClass().getResource("/" + location));
        
    }
    
    public PropertyConfig(ServletContext servletContenxt, String location) {
        log.info("Loading janrain4j properties file from: " + location + "...");
        try {
            log.debug("Trying to load janrain4j properties using Springframework classes...");
            Class<?> systemPropertyUtils = Class.forName("org.springframework.util.SystemPropertyUtils");
            Method resolvePlaceholders = systemPropertyUtils.getMethod("resolvePlaceholders", String.class);
            String resolvedLocation = (String) resolvePlaceholders.invoke(null, location);
            log.debug("Resolved location: " + resolvedLocation);
            
            Class<?> resourceUtils = Class.forName("org.springframework.util.ResourceUtils");
            Method isUrl = resourceUtils.getMethod("isUrl", String.class);
            
            if (!(Boolean) isUrl.invoke(null, resolvedLocation)) {
                Class<?> webUtils = Class.forName("org.springframework.web.util.WebUtils");
                Method getRealPath = webUtils.getMethod("getRealPath", ServletContext.class, String.class);
                resolvedLocation = (String) getRealPath.invoke(null, servletContenxt, resolvedLocation);
                log.debug("Resolved real path: " + resolvedLocation);
            }
            
            Method getURL = resourceUtils.getMethod("getURL", String.class);
            URL resource = (URL) getURL.invoke(null, resolvedLocation);
            log.debug("Resource url: " + resource.toString());
            
            loadProperties(resource);
        }
        catch (Exception e) {
           log.debug("Unable to load janrain4j properties file using Springframework classes, falling back to normal properties loading", e);
           loadProperties(getClass().getResource("/" + location));
        }
    }
    
    private void loadProperties(URL resource) {
        Properties properties = loadSystemProperties();
        try {
            if (resource == null) {
                log.warn("Unable to find janrain4j properties file");
            }
            else {
                properties.load(resource.openStream());
                log.info("Successfully loaded janrain4j properties file");    
            }
        }
        catch (IOException e) {
            log.error("Unable to load janrain4j properties file", e);
        }
        
        if (properties.containsKey(API_KEY_KEY)) {
            this.apiKey(properties.getProperty(API_KEY_KEY));
        }
        
        if (properties.containsKey(APPLICATION_ID_KEY)) {
            this.applicationID(properties.getProperty(APPLICATION_ID_KEY));
        }
        
        if (properties.containsKey(APPLICATION_DOMAIN_KEY)) {
            this.applicationDomain(properties.getProperty(APPLICATION_DOMAIN_KEY));
        }
        
        if (properties.containsKey(TOKEN_URL_KEY)) {
            this.tokenUrl(properties.getProperty(TOKEN_URL_KEY));
        }
        
        if (properties.containsKey(LANGUAGE_PREFERENCE_KEY)) {
            this.languagePreference(properties.getProperty(LANGUAGE_PREFERENCE_KEY));
        }
        
        if (properties.containsKey(PROXY_HOST_KEY)) {
            this.proxyHost(properties.getProperty(PROXY_HOST_KEY));
        }
        
        if (properties.containsKey(PROXY_PORT_KEY)) {
            this.proxyPort(parseInt(properties.getProperty(PROXY_PORT_KEY)));
        }
        
        if (properties.containsKey(PROXY_USERNAME_KEY)) {
            this.proxyUsername(properties.getProperty(PROXY_USERNAME_KEY));
        }
        
        if (properties.containsKey(PROXY_PASSWORD_KEY)) {
            this.proxyPassword(properties.getProperty(PROXY_PASSWORD_KEY));
        }
        
        if (properties.containsKey(CONNECT_TIMEOUT_KEY)) {
            this.connectTimeout(parseInt(properties.getProperty(CONNECT_TIMEOUT_KEY)));
        }
        
        if (properties.containsKey(READ_TIMEOUT_KEY)) {
            this.readTimeout(parseInt(properties.getProperty(READ_TIMEOUT_KEY)));
        }
        
        if (properties.containsKey(SET_STATUS_PROVIDER_NAMES_KEY)) {
            StringTokenizer st = new StringTokenizer(properties.getProperty(SET_STATUS_PROVIDER_NAMES_KEY), ",;|");
            List<String> providerNames = new ArrayList<String>();
            while (st.hasMoreTokens()) {
                providerNames.add(st.nextToken().trim());
            }
            this.setStatusProviderNames(providerNames);
        }
        
        if (properties.containsKey(ACTIVITY_PROVIDER_NAMES_KEY)) {
            StringTokenizer st = new StringTokenizer(properties.getProperty(ACTIVITY_PROVIDER_NAMES_KEY), ",;|");
            List<String> providerNames = new ArrayList<String>();
            while (st.hasMoreTokens()) {
                providerNames.add(st.nextToken().trim());
            }
            this.activityProviderNames(providerNames);
        }
    }
    
    private Properties loadSystemProperties() {
        Properties properties;
        try {
            properties = (Properties) System.getProperties().clone();
        }
        catch (SecurityException e) {
            log.info("Unable to retrieve system properties", e);
            properties = new Properties();
        }
        return properties;
    }
    
    private int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }
}
