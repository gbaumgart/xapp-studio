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
 * Mp3 media item to be posted to the user's activity stream.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Activity
 * @see <a href="http://developers.facebook.com/docs/guides/attachments">Media object format and rules</a>
 */
public class Mp3MediaItem implements MediaItem {

    public final String TYPE = "mp3";
    
    private String src = null;
    private String title = null;
    private String artist = null;
    private String album = null;
    
    /**
     * Create a new <code>Mp3MediaItem</code>.
     * 
     * @param src The src.
     */
    public Mp3MediaItem(String src) {
        this.src = src;
    }
    
    public void writeJSON(JSONWriter writer) throws JSONException {
        writer.object();
        writer.key("type").value(TYPE);
        writer.key("src").value(src);
        if (title != null && title.length() > 0) {
            writer.key("title").value(title);
        }
        if (artist != null && artist.length() > 0) {
            writer.key("artist").value(artist);
        }
        if (album != null && album.length() > 0) {
            writer.key("album").value(album);
        }
        writer.endObject();
    }
    
    /**
     * Returns the src of the mp3 attachment.
     */
    public String getSrc() {
        return src;
    }
    
    /**
     * Sets the src of the mp3 attachment.
     * 
     * @param src The src.
     */
    public void setSrc(String src) {
        this.src = src;
    }
    
    /**
     * Returns the title of the mp3 attachment.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title of the mp3 attachment.
     * 
     * @param title The title.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Returns the artist of the mp3 attachment.
     */
    public String getArtist() {
        return artist;
    }
    
    /**
     * Sets the artist of the mp3 attachment.
     * 
     * @param artist The artist.
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }
    
    /**
     * Returns the album of the mp3 attachment.
     */
    public String getAlbum() {
        return album;
    }
    
    /**
     * Sets the album of the mp3 attachment.
     * 
     * @param album The album.
     */
    public void setAlbum(String album) {
        this.album = album;
    }
}
