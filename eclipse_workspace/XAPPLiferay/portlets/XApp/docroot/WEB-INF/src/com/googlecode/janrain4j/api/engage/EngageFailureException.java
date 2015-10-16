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
package com.googlecode.janrain4j.api.engage;

/**
 * <code>EngageFailureException</code> is thrown when any unknown error occurs 
 * while communicating with the Janrain Engage API.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 */
@SuppressWarnings("serial")
public class EngageFailureException extends Exception {

    private String jsonResponse = null;
    
    public EngageFailureException(String message, String jsonResponse) {
        super(message);
        this.jsonResponse = jsonResponse;
    }
    
    public EngageFailureException(String message, String jsonResponse, Throwable cause) {
        super(message, cause);
        this.jsonResponse = jsonResponse;
    }
    
    /**
     * Returns the JSON response if available, otherwise <code>null</code>.
     */
    public String getJsonResponse() {
        return jsonResponse;
    }
}
