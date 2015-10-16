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

import com.googlecode.janrain4j.conf.Config;

/**
 * Returns {@link HttpClient} implementations.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 */
public class HttpClientFactory {

    /**
     * Returns a <code>HttpClient</code> instance.
     * 
     * @param config The config.
     * @return A <code>HttpClient</code> instance.
     */
    public static HttpClient getHttpClient(Config config) {
        return new HttpClientImpl(config);
    }
}
