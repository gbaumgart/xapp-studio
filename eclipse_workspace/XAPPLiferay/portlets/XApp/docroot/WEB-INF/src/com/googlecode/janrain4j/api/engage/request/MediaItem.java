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
 * Rich media item to be posted to the user's activity stream.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Activity
 * @see <a href="http://developers.facebook.com/docs/guides/attachments">Media object format and rules</a>
 */
public interface MediaItem {

    public void writeJSON(JSONWriter writer) throws JSONException;
}
