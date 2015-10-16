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
 * URL of a photo of the <code>Contact</code>.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Contact
 */
@SuppressWarnings("serial")
public class Photo implements Serializable {

    public static final String TYPE_WORK = "work";
    public static final String TYPE_HOME = "home";
    public static final String TYPE_OTHER = "other";
    
    protected String value = null;
    protected String type = null;
    protected boolean primary = false;
    
    protected Photo() {
    }
    
    public static Photo fromJSON(JSONObject json) {
        Photo photo = null;
        if (json != null) {
            photo = new Photo();
            photo.setValue(json.optString("value"));
            photo.setType(json.optString("type"));
            photo.setPrimary(json.optBoolean("primary", false));
        }
        return photo;
    }
    
    
    /**
     * Returns the URL of the photo.
     * 
     * @return The URL of the photo or <code>null</code> if not found in response.
     */
    public String getValue() {
        return value;
    }
    
    void setValue(String value) {
        this.value = value;
    }
    
    /**
     * Returns the type of the photo.
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
     * Returns true if the photo is the primary or preferred photo.
     */
    public boolean isPrimary() {
        return primary;
    }
    
    void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
