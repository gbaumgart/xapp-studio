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
 * <code>Property</code> with a text value.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Property
 */
public class TextProperty extends AbstractProperty {

    private String text = null;
    
    /**
     * Create a new <code>StringProperty</code>.
     * 
     * @param key The key.
     * @param text The text.
     */
    public TextProperty(String key, String text) {
        this.key = key;
        this.text = text;
    }
    
    public void writeJSON(JSONWriter writer) throws JSONException {
        writer.key(key).value(text);
    }
    
    /**
     * Returns the text value. 
     */
    public String getText() {
        return text;
    }
    
    /**
     * Sets the text value.
     * 
     * @param text The text.
     */
    public void setValue(String text) {
        this.text = text;
    }
}
