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
 * Email address for the <code>Contact</code>.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Contact
 */
@SuppressWarnings("serial")
public class Email implements Serializable {

    public static final String TYPE_WORK = "work";
    public static final String TYPE_HOME = "home";
    public static final String TYPE_OTHER = "other";
    
    protected String value = null;
    protected String type = null;
    protected boolean primary = false;
    
    protected Email() {
    }
    
    public static Email fromJSON(JSONObject json) {
        Email email = null;
        if (json != null) {
            email = new Email();
            email.setValue(json.optString("value"));
            email.setType(json.optString("type"));
            email.setPrimary(json.optBoolean("primary", false));
        }
        return email;
    }
    
    /**
     * Returns the email address.
     * 
     * @return The email address or <code>null</code> if not found in response.
     */
    public String getValue() {
        return value;
    }
    
    void setValue(String value) {
        this.value = value;
    }
    
    /**
     * Returns the type of the email address.
     * 
     * @return The type or <code>null</code> if not found in response.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns true if the type is work. 
     */
    public boolean isWork() {
        return TYPE_WORK.equalsIgnoreCase(type);
    }
    
    /**
     * Returns true if the type is home. 
     */
    public boolean isHome() {
        return TYPE_HOME.equalsIgnoreCase(type);
    }
    
    /**
     * Returns true if the type is other. 
     */
    public boolean isOther() {
        return TYPE_OTHER.equalsIgnoreCase(type);
    }
    
    void setType(String type) {
        this.type = type;
    }
    
    /**
     * Returns true if the email address is the primary or preferred email address.
     */
    public boolean isPrimary() {
        return primary;
    }
    
    void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
