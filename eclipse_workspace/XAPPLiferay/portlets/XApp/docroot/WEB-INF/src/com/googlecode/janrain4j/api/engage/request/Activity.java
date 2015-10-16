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
package com.googlecode.janrain4j.api.engage.request;

import java.util.Iterator;
import java.util.List;

import com.googlecode.janrain4j.api.engage.EngageService;
import com.googlecode.janrain4j.json.JSONException;
import com.googlecode.janrain4j.json.JSONStringer;

/**
 * Represents an activity update to be posted to the user's activity stream.  
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see EngageService#activity(String, Activity)
 * @see EngageService#activity(String, Activity, String)
 */
public class Activity {

    private String url = null;
    private String action = null;
    private String userGeneratedContent = null;
    private String title = null;
    private String description = null;
    private List<ActionLink> actionLinks = null;
    private List<MediaItem> media = null;
    private List<Property> properties = null;
    
    /**
     * Create a new <code>Activity</code>.
     * 
     * @param url The url.
     * @param action The action.
     */
    public Activity(String url, String action) {
        this.url = url;
        this.action = action;
    }
    
    public String toJSON() throws JSONException {
        JSONStringer json = new JSONStringer();
        json.object();
        json.key("url").value(url);
        json.key("action").value(action);
        if (userGeneratedContent != null && userGeneratedContent.length() > 0) {
            json.key("user_generated_content").value(userGeneratedContent);
        }
        if (title != null && title.length() > 0) {
            json.key("title").value(title);
        }
        if (description != null && description.length() > 0) {
            json.key("description").value(description);
        }
        
        if (actionLinks != null && actionLinks.size() > 0) {
            json.key("action_links");
            json.array();
            for (Iterator<ActionLink> iterator = actionLinks.iterator(); iterator.hasNext();) {
                iterator.next().writeJSON(json);
            }
            json.endArray();
        }
        
        if (media != null && media.size() > 0) {
            json.key("media");
            json.array();
            for (Iterator<MediaItem> iterator = media.iterator(); iterator.hasNext();) {
                iterator.next().writeJSON(json);
            }
            json.endArray();
        }
        
        if (properties != null && properties.size() > 0) {
            json.key("properties");
            json.object();
            for (Iterator<Property> iterator = properties.iterator(); iterator.hasNext();) {
                iterator.next().writeJSON(json);
            }
            json.endObject();
        }
        
        json.endObject();
        return json.toString();
    }
    
    /**
     * Returns the URL of the resource being mentioned in the activity update.
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Sets the URL of the resource being mentioned in the activity update.
     * 
     * @param url The url.
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * Returns the string describing what the user did.
     */
    public String getAction() {
        return action;
    }
    
    /**
     * Sets the string describing what the user did, written in the third person. 
     * Examples:
     * <ul>
     *   <li>"wrote a restaurant review"</li>
     *   <li>"posted a comment"</li>
     *   <li>"took a quiz"</li>
     * </ul>
     * 
     * @param action The string describing what the user did.
     */
    public void setAction(String action) {
        this.action = action;
    }
    
    /**
     * Returns the string containing the user-supplied content.
     */
    public String getUserGeneratedContent() {
        return userGeneratedContent;
    }
    
    /**
     * Sets the string containing the user-supplied content, such as a comment 
     * or the first paragraph of an article that the user wrote. Note that some 
     * providers (Twitter in particular) may truncate this value.
     * 
     * @param userGeneratedContent The string containing the user-supplied content.
     */
    public void setUserGeneratedContent(String userGeneratedContent) {
        this.userGeneratedContent = userGeneratedContent;
    }
    
    /**
     * Returns the title of the resource being mentioned in the activity update.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title of the resource being mentioned in the activity update.
     * 
     * @param title The title of the resource being mentioned in the activity update.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Returns the description of the resource mentioned in the activity update.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description of the resource mentioned in the activity update.
     * 
     * @param description The description of the resource mentioned in the activity update.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Returns the action links a user can use to take action on.
     */
    public List<ActionLink> getActionLinks() {
        return actionLinks;
    }
    
    /**
     * Sets the action links a user can use to take action on.
     * 
     * @param actionLinks The action links.
     */
    public void setActionLinks(List<ActionLink> actionLinks) {
        this.actionLinks = actionLinks;
    }
    
    /**
     * Returns the media attachments.
     */
    public List<MediaItem> getMedia() {
        return media;
    }
    
    /**
     * Sets the media attachments.
     * 
     * @param media The media attachments.
     */
    public void setMedia(List<MediaItem> media) {
        this.media = media;
    }
    
    /**
     * Returns the properties of the activity update.
     */
    public List<Property> getProperties() {
        return properties;
    }
    
    /**
     * Sets the properties of the activity update
     * @param properties
     */
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
