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
package com.googlecode.janrain4j.http;

import java.util.Map;

/**
 * The <code>HttpClient</code> executes http requests.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 */
public interface HttpClient {

    /**
     * Executes a http post request.
     * 
     * @param url The url.
     * @param parameters The http parameters.
     * @return The http response.
     * @throws HttpFailureException If any unknown error occurs while executing the http request.
     */
    public HttpResponse post(String url, Map<String, String> parameters) throws HttpFailureException;
}
