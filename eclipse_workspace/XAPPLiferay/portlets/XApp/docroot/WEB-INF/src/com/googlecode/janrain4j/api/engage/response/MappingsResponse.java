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
package com.googlecode.janrain4j.api.engage.response;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.janrain4j.api.engage.EngageFailureException;
import com.googlecode.janrain4j.api.engage.EngageService;
import com.googlecode.janrain4j.json.JSONArray;
import com.googlecode.janrain4j.json.JSONException;
import com.googlecode.janrain4j.json.JSONObject;

/**
 * The <code>MappingsResponse</code> contains all stored mappings for a particular primary key.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see EngageService#mappings(String)
 */
@SuppressWarnings("serial")
public class MappingsResponse extends AbstractEngageResponse {

    private List<String> mappings = null;
    
    public MappingsResponse(String json) throws EngageFailureException {
        super(json);
        JSONObject rsp = getResponseAsJSONObject();
        JSONArray rspIdentifiers = rsp.optJSONArray("identifiers");
        if (rspIdentifiers != null) {
            mappings = new ArrayList<String>();
            for (int i = 0; i < rspIdentifiers.length(); i++) {
                try {
                    mappings.add(rspIdentifiers.getString(i));
                }
                catch (JSONException e) {
                    throw new EngageFailureException("Unexpected JSON error", json, e);
                }
            }
        }
    }
    
    /**
     * Returns all stored mappings for the primary key.
     * 
     * @return The stored mappings or <code>null</code> if not found in response.
     */
    public List<String> getMappings() {
        return mappings;
    }
}
