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
package com.googlecode.janrain4j.api.engage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.janrain4j.api.engage.request.Activity;
import com.googlecode.janrain4j.api.engage.response.AllMappingsResponse;
import com.googlecode.janrain4j.api.engage.response.AnalyticsResponse;
import com.googlecode.janrain4j.api.engage.response.ContactsResponse;
import com.googlecode.janrain4j.api.engage.response.MappingsResponse;
import com.googlecode.janrain4j.api.engage.response.UserDataResponse;
import com.googlecode.janrain4j.conf.Config;
import com.googlecode.janrain4j.conf.ConfigHolder;
import com.googlecode.janrain4j.http.HttpClientFactory;
import com.googlecode.janrain4j.http.HttpFailureException;
import com.googlecode.janrain4j.http.HttpResponse;
import com.googlecode.janrain4j.json.JSONException;
import com.googlecode.janrain4j.json.JSONObject;

/**
 * @author Marcel Overdijk
 * @since 1.0
 */
class EngageServiceImpl implements EngageService {
    
    public static final String API_URL = "https://rpxnow.com/api/v2/";
    
    public static final String ACTIVITY_METHOD = "activity";
    public static final String ALL_MAPPINGS_METHOD = "all_mappings";
    public static final String ANALYTICS_METHOD = "analytics_access";
    public static final String AUTH_INFO_METHOD = "auth_info";
    public static final String GET_CONTACTS_METHOD = "get_contacts";
    public static final String GET_USER_DATA_METHOD = "get_user_data";
    public static final String MAP_METHOD = "map";
    public static final String MAPPINGS_METHOD = "mappings";
    public static final String SET_AUTH_PROVIDERS_METHOD = "set_auth_providers";
    public static final String SET_STATUS_METHOD = "set_status";
    public static final String UNMAP_METHOD = "unmap";
    
    public static final String ACTIVITY_PARAM = "activity";
    public static final String ALL_IDENTIFIERS_PARAM = "all_identifiers";
    public static final String API_KEY_PARAM = "apiKey";
    public static final String END_PARAM = "end";
    public static final String EXTENDED_PARAM = "extended";
    public static final String FORMAT_PARAM = "format";
    public static final String IDENTIFIER_PARAM = "identifier";
    public static final String LOCATION_PARAM = "location";
    public static final String OVERWRITE_PARAM = "overwrite";
    public static final String PRIMARY_KEY_PARAM = "primaryKey";
    public static final String PROVIDERS_PARAM = "providers";
    public static final String START_PARAM = "start";
    public static final String STATUS_PARAM = "status";
    public static final String TOKEN_PARAM = "token";
    public static final String UNLINK_PARAM = "unlink";
    
    public static final String JSON = "json";
    
    private Log log = LogFactory.getLog(this.getClass());
    
    private Config config = null;
    
    EngageServiceImpl() {
    }
    
    EngageServiceImpl(Config config) {
        this.config = config;
    }
    
    Config getConfig() {
        return config == null ? ConfigHolder.getConfig() : config;
    }
    
    public UserDataResponse authInfo(String token) throws EngageFailureException, ErrorResponeException {
        return authInfo(token, false);
    }
    
    public UserDataResponse authInfo(String token, boolean extended) throws EngageFailureException, ErrorResponeException {
        Map<String, String> params = new HashMap<String, String>();
        params.put(TOKEN_PARAM, token);
        params.put(EXTENDED_PARAM, Boolean.toString(extended));
        String jsonResponse = apiCall(AUTH_INFO_METHOD, params);
        return new UserDataResponse(jsonResponse);
    }
    
    public ContactsResponse getContacts(String identifier) throws EngageFailureException, ErrorResponeException {
        Map<String, String> params = new HashMap<String, String>();
        params.put(IDENTIFIER_PARAM, identifier);
        String jsonResponse = apiCall(GET_CONTACTS_METHOD, params);
        return new ContactsResponse(jsonResponse);
    }

    public UserDataResponse getUserData(String identifier) throws EngageFailureException, ErrorResponeException {
        return getUserData(identifier, false);
    }

    public UserDataResponse getUserData(String identifier, boolean extended) throws EngageFailureException, ErrorResponeException {
        Map<String, String> params = new HashMap<String, String>();
        params.put(IDENTIFIER_PARAM, identifier);
        params.put(EXTENDED_PARAM, Boolean.toString(extended));
        String jsonResponse = apiCall(GET_USER_DATA_METHOD, params);
        return new UserDataResponse(jsonResponse);
    }
    
    public void setStatus(String identifier, String status) throws EngageFailureException, ErrorResponeException {
        setStatus(identifier, status, null);
    }
    
    public void setStatus(String identifier, String status, String location) throws EngageFailureException, ErrorResponeException {
        Map<String, String> params = new HashMap<String, String>();
        params.put(IDENTIFIER_PARAM, identifier);
        params.put(STATUS_PARAM, status);
        if (location != null && location.length() > 0) {
            params.put(LOCATION_PARAM, location);
        }
        apiCall(SET_STATUS_METHOD, params);
    }
    
    public boolean supportsSetStatus(String providerName) {
        return getConfig().getSetStatusProviderNames().contains(providerName);
    }
    
    public void map(String identifier, String primaryKey) throws EngageFailureException, ErrorResponeException {
        map(identifier, primaryKey, true);
    }
    
    public void map(String identifier, String primaryKey, boolean overwrite) throws EngageFailureException, ErrorResponeException {
        Map<String, String> params = new HashMap<String, String>();
        params.put(IDENTIFIER_PARAM, identifier);
        params.put(PRIMARY_KEY_PARAM, primaryKey);
        params.put(OVERWRITE_PARAM, Boolean.toString(overwrite));
        apiCall(MAP_METHOD, params);
    }

    public void unmap(String primaryKey) throws EngageFailureException, ErrorResponeException {
        unmap(primaryKey, false);
    }

    public void unmap(String primaryKey, boolean unlink) throws EngageFailureException, ErrorResponeException {
        unmap(null, true, primaryKey, unlink);
    }

    public void unmap(String identifier, String primaryKey) throws EngageFailureException, ErrorResponeException {
        unmap(identifier, primaryKey, false);
    }

    public void unmap(String identifier, String primaryKey, boolean unlink) throws EngageFailureException, ErrorResponeException {
        unmap(identifier, false, primaryKey, unlink);
    }
    
    protected void unmap(String identifier, boolean allIdentifiers, String primaryKey, boolean unlink) throws EngageFailureException, ErrorResponeException {
        Map<String, String> params = new HashMap<String, String>();
        if (!allIdentifiers) {
            params.put(IDENTIFIER_PARAM, identifier);
        }
        else {
            params.put(ALL_IDENTIFIERS_PARAM, Boolean.toString(allIdentifiers));
        }
        params.put(PRIMARY_KEY_PARAM, primaryKey);
        params.put(UNLINK_PARAM, Boolean.toString(unlink));
        apiCall(UNMAP_METHOD, params);
    }
    
    public MappingsResponse mappings(String primaryKey) throws EngageFailureException, ErrorResponeException {
        Map<String, String> params = new HashMap<String, String>();
        params.put(PRIMARY_KEY_PARAM, primaryKey);
        String jsonResponse = apiCall(MAPPINGS_METHOD, params);
        return new MappingsResponse(jsonResponse);
    }
    
    public AllMappingsResponse allMappings() throws EngageFailureException, ErrorResponeException {
        Map<String, String> params = new HashMap<String, String>();
        String jsonResponse = apiCall(ALL_MAPPINGS_METHOD, params);
        return new AllMappingsResponse(jsonResponse);
    }
    
    public void activity(String identifier, Activity activity) throws EngageFailureException, ErrorResponeException {
        activity(identifier, activity, null);
    }

    public void activity(String identifier, String activity) throws EngageFailureException, ErrorResponeException {
         activity(identifier, activity, null);
    }
    
    public void activity(String identifier, Activity activity, String location) throws EngageFailureException, ErrorResponeException {
        try {
            activity(identifier, activity.toJSON(), location);
        }
        catch (JSONException e) {
            throw new EngageFailureException("Unexpected JSON error", null, e);
        }
    }
    
    public void activity(String identifier, String activity, String location) throws EngageFailureException, ErrorResponeException {
        Map<String, String> params = new HashMap<String, String>();
        params.put(IDENTIFIER_PARAM, identifier);
        params.put(ACTIVITY_PARAM, activity);
        if (location != null && location.length() > 0) {
            params.put(LOCATION_PARAM, location);
        }
        apiCall(ACTIVITY_METHOD, params);
    }
    
    public boolean supportsActivity(String providerName) {
        return getConfig().getActivityProviderNames().contains(providerName);
    }
    
    public AnalyticsResponse analytics(Date start, Date end) throws EngageFailureException, ErrorResponeException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
        Map<String, String> params = new HashMap<String, String>();
        params.put(START_PARAM, dateFormatter.format(start));
        params.put(END_PARAM, dateFormatter.format(end));
        String jsonResponse = apiCall(ANALYTICS_METHOD, params);
        return new AnalyticsResponse(jsonResponse);
    }
    
    public void setAuthProviders(List<String> providers) throws EngageFailureException, ErrorResponeException {
        StringBuffer sb = new StringBuffer();
        for (String provider : providers) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(provider);
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(PROVIDERS_PARAM, sb.toString());
        apiCall(SET_AUTH_PROVIDERS_METHOD, params);
    }
    
    String apiCall(String method, Map<String, String> partialParams) throws EngageFailureException, ErrorResponeException {
        
        Map<String, String> params = new HashMap<String, String>();
        
        if (partialParams != null) {
            params.putAll(partialParams);
        }

        params.put(FORMAT_PARAM, JSON);
        params.put(API_KEY_PARAM, getConfig().getApiKey());
        
        String url = API_URL + method;
        
        if (log.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer();
            sb.append("Janrain Engage request: ").append(method).append("\n");
            sb.append("url: ").append(url).append("\n");
            sb.append("parameters: [\n");
            for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
                String key = iterator.next();
                String value = params.get(key);
                sb.append("  ").append(key).append(": ").append(value).append("\n");
            }
            sb.append("]");
            log.debug(sb.toString());
        }
        
        String jsonResponse = null;
        
        try {
            HttpResponse response = HttpClientFactory.getHttpClient(getConfig()).post(url, params);
            jsonResponse = response.getContent();
            JSONObject rsp = new JSONObject(jsonResponse);
            
            if (log.isDebugEnabled()) {
                log.debug("Janrain Engage response:\n" + jsonResponse);
            }
            
            String stat = rsp.getString("stat");
            if (!stat.equals("ok")) {
                if (stat.equals("fail")) {
                    JSONObject err = rsp.getJSONObject("err");
                    int code = err.getInt("code");
                    String msg = err.getString("msg");
                    throw new ErrorResponeException(code, msg, jsonResponse);
                }
                else {
                    throw new EngageFailureException("Unexpected status in response: " + stat, jsonResponse);
                }
            }
            
            return jsonResponse;
        }
        catch (HttpFailureException e) {
            throw new EngageFailureException("Unexpected HTTP error", jsonResponse, e);
        }
        catch (JSONException e) {
            throw new EngageFailureException("Unexpected JSON error", jsonResponse, e);
        }
    }
}
