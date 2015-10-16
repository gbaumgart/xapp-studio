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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;

import com.googlecode.janrain4j.conf.Config;
import com.googlecode.janrain4j.util.URLEncoderUtils;

/**
 * @author Marcel Overdijk
 * @since 1.0
 */
class HttpClientImpl implements HttpClient {

    public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    
    private Config config = null;
    
    public HttpClientImpl(Config config) {
        this.config = config;
    }
    
    public HttpResponse post(String url, Map<String, String> parameters) throws HttpFailureException {
        try {
            HttpURLConnection connection = getConnection(url);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.connect();
            
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(URLEncoderUtils.encodeParameters(parameters));
            writer.close();
            
            int repsonseCode = connection.getResponseCode();
            String content = toString(connection.getInputStream());
            
            return new HttpResponseImpl(repsonseCode, content);
        }
        catch (IOException e) {
            throw new HttpFailureException("Unexpected IO error", e);
        }
    }
    
    private HttpURLConnection getConnection(String url) throws IOException {
        
        HttpURLConnection connection = null;
        
        if (config.getProxyHost() != null && config.getProxyHost().length() > 0) {
            if (config.getProxyUsername() != null && config.getProxyUsername().length() > 0) {
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        if (getRequestorType().equals(RequestorType.PROXY)) {
                            return new PasswordAuthentication(config.getProxyUsername(), config.getProxyPassword().toCharArray());
                        }
                        else {
                            return null;
                        }
                    }
                });
            }
            Proxy proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(config.getProxyHost(), config.getProxyPort()));
            connection = (HttpURLConnection) new URL(url).openConnection(proxy);
        }
        else {
            connection = (HttpURLConnection) new URL(url).openConnection();
        }
        
        if (config.getConnectTimeout() > -1) {
            connection.setConnectTimeout(config.getConnectTimeout());
        }
        
        if (config.getReadTimeout() > -1) {
            connection.setReadTimeout(config.getReadTimeout());
        }
        
        return connection;
    }
    
    /**
     * Get the contents of an <code>InputStream</code> as a String
     * using the default character encoding of the platform.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * 
     * @param input The <code>InputStream</code> to read from.
     * @return The requested String
     * @throws NullPointerException If the input is null.
     * @throws IOException If an IO error occurs.
     */
    private String toString(InputStream input) throws IOException {
        StringBuilder contents = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(input);
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        int n = 0;
        while (-1 != (n = reader.read(buffer))) {
            contents.append(buffer, 0, n);
        }
        return contents.toString();
    }
}
