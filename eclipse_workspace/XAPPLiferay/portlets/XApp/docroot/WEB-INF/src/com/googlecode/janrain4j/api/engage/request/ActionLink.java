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

import com.googlecode.janrain4j.json.JSONException;
import com.googlecode.janrain4j.json.JSONWriter;

/**
 * An action link is a link a user can use to take action on an activity 
 * update on the provider.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Activity
 */
public class ActionLink {

    private String text = null;
    private String href = null;
    
    /**
     * Create a new <code>ActionLink</code>.
     * 
     * @param text The text.
     * @param href The href.
     */
    public ActionLink(String text, String href) {
        this.text = text;
        this.href = href;
    }
    
    public void writeJSON(JSONWriter writer) throws JSONException {
        writer.object();
        writer.key("text").value(text);
        writer.key("href").value(href);
        writer.endObject();
    }
    
    /**
     * Returns the text of the action link.
     */
    public String getText() {
        return text;
    }
    
    /**
     * Sets the text of the action link.
     * 
     * @param text The text.
     */
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * Returns the href of the action link.
     */
    public String getHref() {
        return href;
    }
    
    /**
     * Sets the href of the action link.
     * 
     * @param href The href.
     */
    public void setHref(String href) {
        this.href = href;
    }
}
