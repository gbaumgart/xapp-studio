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
package com.googlecode.janrain4j.api.engage.response.poco;

import java.io.Serializable;

import com.googlecode.janrain4j.json.JSONObject;

/**
 * Online account held by the <code>Contact</code>.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Contact
 */
@SuppressWarnings("serial")
public class Account implements Serializable {

    private String domain = null;
    private String username = null;
    private String userid = null;
    private boolean primary = false;
    
    protected Account() {
    }
    
    public static Account fromJSON(JSONObject json) {
        Account account = null;
        if (json != null) {
            account = new Account();
            account.setDomain(json.optString("domain"));
            account.setUsername(json.optString("username"));
            account.setUserid(json.optString("userid"));
            account.setPrimary(json.optBoolean("primary", false));
        }
        return account;
    }
    
    /**
     * Returns the top-most authoritative domain for this account, e.g. "twitter.com".
     * 
     * @return The domain or <code>null</code> if not found in response.
     */
    public String getDomain() {
        return domain;
    }
    
    void setDomain(String domain) {
        this.domain = domain;
    }
    
    /**
     * Returns the alphanumeric user name, usually chosen by the user, e.g. "jsmarr".
     * 
     * @return The username or <code>null</code> if not found in response.
     */
    public String getUsername() {
        return username;
    }
    
    void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Returns the user ID number, usually chosen automatically, and usually 
     * numeric but sometimes alphanumeric, e.g. "12345" or "1Z425A".
     * 
     * @return The userid or <code>null</code> if not found in response.
     */
    public String getUserid() {
        return userid;
    }
    
    void setUserid(String userid) {
        this.userid = userid;
    }
    
    /**
     * Returns true if this instance is the primary or prefered value for this field.
     */
    public boolean isPrimary() {
        return primary;
    }
    
    void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
