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
package com.googlecode.janrain4j.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class that provides convenient methods on top of {@link java.net.URLEncoder}.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 */
public class URLEncoderUtils {

    /**
     * Encodes the given value to UTF-8.
     * 
     * @param value The value to encode.
     * @return The encoded value.
     * @throws UnsupportedEncodingException If any error occurs while encoding the value.
     * @see #encodeParameter(String, String)
     * @see #encodeParamters(Map)
     */
    public static String encode(String value) throws UnsupportedEncodingException {
        if (value != null && value.length() > 0) {
            return URLEncoder.encode(value, "UTF-8");
        }
        else {
            return "";
        }
    }
    
    /**
     * Encodes to given key to:
     * <p>
     * <code><encoded key>=<encoded value></code>
     * </p>
     * 
     * @param key The key to encode.
     * @param value The value to encode
     * @return The encoded parameter.
     * @throws UnsupportedEncodingException If any error occurs while encoding the parameter.
     * @see #encode(String)
     * @see #encodeParamters(Map)
     */
    public static String encodeParameter(String key, String value) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        sb.append(encode(key));
        sb.append("=");
        sb.append(encode(value));
        return sb.toString();
    }
    
    /**
     * Encodes to given parameters to:
     * <p>
     * <code><encoded key1>=<encoded value1>&<encoded key2>=<encoded value2>&...</code>
     * </p>
     * 
     * @param parameters The parameters to encode.
     * @return The encoded parameters.
     * @throws UnsupportedEncodingException If any error occurs while encoding the parameters.
     * @see #encode(String)
     * @see #encodeParameter(String, String)
     */
    public static String encodeParameters(Map<String, String> parameters) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        for (Iterator<Map.Entry<String, String>> it = parameters.entrySet().iterator(); it.hasNext();) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
            sb.append(encodeParameter(e.getKey(), e.getValue()));
        }
        return sb.toString();
    }
}
