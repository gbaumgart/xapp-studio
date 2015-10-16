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
package com.googlecode.janrain4j.api.engage.response.accesscredentials;

import java.io.Serializable;

import com.googlecode.janrain4j.api.engage.response.UserDataResponse;
import com.googlecode.janrain4j.json.JSONException;
import com.googlecode.janrain4j.json.JSONObject;

/**
 * The user's authorization credentials.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see UserDataResponse
 */
@SuppressWarnings("serial")
public class AccessCredentials implements Serializable {

    public static final String TYPE_OAUTH = "OAuth";
    public static final String TYPE_FACEBOOK = "Facebook";
    public static final String TYPE_WINDOWS_LIVE = "WindowsLive";
    
    private String type = null;
    
    // OAuth access credentials
    private String oauthToken = null;
    private String oauthTokenSecret = null;
    
    // Facebook access credentials
    private String uid = null;
    private String accessToken = null;
    private Long expires = null;
    
    // Windows Live credentials
    private String eact = null;
    
    private AccessCredentials() {
    }
    
    public static AccessCredentials fromJSON(JSONObject json) throws JSONException {
        AccessCredentials accessCredentials = null;
        if (json != null) {
            accessCredentials = new AccessCredentials();
            accessCredentials.setType(json.optString("type"));
            accessCredentials.setOauthToken(json.optString("oauthToken"));
            accessCredentials.setOauthTokenSecret(json.optString("oauthTokenSecret"));
            accessCredentials.setUid(json.optString("uid"));
            accessCredentials.setAccessToken(json.optString("accessToken"));
            accessCredentials.setExpires(json.optLong("expires"));
            accessCredentials.setEact(json.optString("eact"));
        }
        return accessCredentials;
    }
    
    /**
     * Returns true if the signed in user is authenticated using OAuth. 
     */
    public boolean isOauth() {
        return TYPE_OAUTH.equalsIgnoreCase(type);
    }
    
    /**
     * Returns true if the signed in user is authenticated using Facebook.
     */
    public boolean isFacebook() {
        return TYPE_FACEBOOK.equalsIgnoreCase(type);
    }
    
    /**
     * Returns true if the signed in user is authenticated using Windows Live.
     */
    public boolean isWindowsLive() {
        return TYPE_WINDOWS_LIVE.equalsIgnoreCase(type);
    }
    
    /**
     * Returns the authentication type.
     * 
     * @return The authentication type or <code>null</code> if not found in response.
     */
    public String getType() {
        return type;
    }
    
    void setType(String type) {
        this.type = type;
    }
    
    /**
     * Returns the OAuth token (only available with OAuth authentication).
     * 
     * @return The OAuth token or <code>null</code> if not found in response.
     */
    public String getOauthToken() {
        return oauthToken;
    }
    
    void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }
    
    /**
     * Returns the OAuth token secret (only available with OAuth authentication).
     * 
     * @return The OAuth token secret or <code>null</code> if not found in response.
     */
    public String getOauthTokenSecret() {
        return oauthTokenSecret;
    }
    
    void setOauthTokenSecret(String oauthTokenSecret) {
        this.oauthTokenSecret = oauthTokenSecret;
    }
    
    /**
     * Returns the UID (only available with Facebook authentication).
     * 
     * @return The UID or <code>null</code> if not found in response.
     */
    public String getUid() {
        return uid;
    }
    
    void setUid(String uid) {
        this.uid = uid;
    }
    
    /**
     * Returns the access token (only available with Facebook authentication).
     * 
     * @return The access token or <code>null</code> if not found in response.
     */
    public String getAccessToken() {
        return accessToken;
    }
    
    void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    /**
     * Returns the expires Unix timestamp (only available with Facebook authentication).
     * 
     * @return The expires Unix timestamp or <code>null</code> if not found in response.
     */
    public Long getExpires() {
        return expires;
    }
    
    void setExpires(Long expires) {
        this.expires = expires;
    }
    
    /**
     * Returns the eact token (only available with Windows Live authentication).
     * 
     * @return The eact token or <code>null</code> if not found in response.
     */
    public String getEact() {
        return eact;
    }
    
    void setEact(String eact) {
        this.eact = eact;
    }
}
