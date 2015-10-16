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

import com.googlecode.janrain4j.api.engage.EngageFailureException;
import com.googlecode.janrain4j.api.engage.EngageService;
import com.googlecode.janrain4j.json.JSONException;
import com.googlecode.janrain4j.json.JSONObject;

/**
 * The <code>AnalyticsResponse</code> contains statistics for your application.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see EngageService#analytics(java.util.Date, java.util.Date)
 */
@SuppressWarnings("serial")
public class AnalyticsResponse extends AbstractEngageResponse {

    private String url = null;
    
    public AnalyticsResponse(String json) throws EngageFailureException {
        super(json);
        JSONObject rsp = getResponseAsJSONObject();
        try {
            url = rsp.getString("url");
        }
        catch (JSONException e) {
            throw new EngageFailureException("Unexpected JSON error", json, e);
        }
    }
    
    /**
     * Returns the URL of the zip file containing the statistics for your application.
     */
    public String getUrl() {
        return url;
    }
}
