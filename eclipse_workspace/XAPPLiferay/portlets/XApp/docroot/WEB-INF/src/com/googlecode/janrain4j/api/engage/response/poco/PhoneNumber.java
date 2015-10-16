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
 * Phone number for the <code>Contact</code>.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Contact
 */
@SuppressWarnings("serial")
public class PhoneNumber implements Serializable {

    public static final String TYPE_WORK = "work";
    public static final String TYPE_HOME = "home";
    public static final String TYPE_MOBILE = "mobile";
    public static final String TYPE_FAX = "fax";
    public static final String TYPE_PAGER = "pager";
    public static final String TYPE_OTHER = "other";
    
    protected String value = null;
    protected String type = null;
    protected boolean primary = false;
    
    protected PhoneNumber() {
    }
    
    public static PhoneNumber fromJSON(JSONObject json) {
        PhoneNumber phoneNumber = null;
        if (json != null) {
            phoneNumber = new PhoneNumber();
            phoneNumber.setValue(json.optString("value"));
            phoneNumber.setType(json.optString("type"));
            phoneNumber.setPrimary(json.optBoolean("primary", false));
        }
        return phoneNumber;
    }
    
    /**
     * Returns the phone number.
     * 
     * @return The phone number or <code>null</code> if not found in response.
     */
    public String getValue() {
        return value;
    }
    
    void setValue(String value) {
        this.value = value;
    }
    
    /**
     * Returns the type of the phone number.
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
     * Returns true if the type is mobile. 
     */
    public boolean isMobile() {
        return TYPE_MOBILE.equalsIgnoreCase(type);
    }
    
    /**
     * Returns true if the type is fax. 
     */
    public boolean isFax() {
        return TYPE_FAX.equalsIgnoreCase(type);
    }
    
    /**
     * Returns true if the type is pager. 
     */
    public boolean isPager() {
        return TYPE_PAGER.equalsIgnoreCase(type);
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
     * Returns true if the phone number is the primary or preferred phone number.
     */
    public boolean isPrimary() {
        return primary;
    }
    
    void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
