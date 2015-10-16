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
 * Image media item to be posted to the user's activity stream.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Activity
 * @see <a href="http://developers.facebook.com/docs/guides/attachments">Media object format and rules</a>
 */
public class ImageMediaItem implements MediaItem {

    public final String TYPE = "image";
    
    private String src = null;
    private String href = null;
    
    /**
     * Create a new <code>ImageMediaItem</code>.
     * 
     * @param src The src.
     * @param href The href.
     */
    public ImageMediaItem(String src, String href) {
        this.src = src;
        this.href = href;
    }
    
    public void writeJSON(JSONWriter writer) throws JSONException {
        writer.object();
        writer.key("type").value(TYPE);
        writer.key("src").value(src);
        writer.key("href").value(href);
        writer.endObject();
    }
    
    /**
     * Returns the src of the image attachment.
     */
    public String getSrc() {
        return src;
    }
    
    /**
     * Sets the src of the image attachment.
     * 
     * @param src The src.
     */
    public void setSrc(String src) {
        this.src = src;
    }
    
    /**
     * Returns the href of the image attachment.
     */
    public String getHref() {
        return href;
    }
    
    /**
     * Sets the href of the image attachment.
     * 
     * @param href The href.
     */
    public void setHref(String href) {
        this.href = href;
    }
}
