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
package com.googlecode.janrain4j.tags;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import com.googlecode.janrain4j.util.URLEncoderUtils;

/**
 * Tag to embed the sign-in widget.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 */
public class SignInEmbeddedTag extends AbstractBaseTag {

    private String applicationDomain = "";
    private String tokenUrl = null;
    private String defaultProvider = null;
    private String flags = null;
    private String languagePreference = null;
    private int width = 400;
    private int height = 240;
    
    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        
        Map<String, String> params = new HashMap<String, String>();
        
        params.put("token_url", getTokenUrl());
        
        if (getDefaultProvider() != null && getDefaultProvider().length() > 0) {
            params.put("default_provider", getDefaultProvider());
        }
        
        if (getFlags() != null && getFlags().length() > 0) {
            params.put("flags", getFlags());
        }
        
        //if (getLanguagePreference() != null && getLanguagePreference().length() > 0) {

        params.put("language_preference", "es");
        //}
        
        String encodedParams = URLEncoderUtils.encodeParameters(params);
        
        StringWriter sw = new StringWriter();
        sw.append("<iframe ");
        sw.append("src=\"").append(getApplicationDomain()).append("openid/embed?").append(encodedParams).append("\" ");
        sw.append("scrolling=\"no\" frameBorder=\"no\" allowtransparency=\"true\" ");
        sw.append("style=\"width: ").append(Integer.toString(width)).append("px; height: ").append(Integer.toString(height)).append("px;\"");
        sw.append("></iframe>");
        
        out.print(sw.toString());
    }
    
    public String getApplicationDomain() {
        return System.getProperty("janrain.applicationDomain");
    }
    
    public void setApplication(String applicationDomain) {
        this.applicationDomain = applicationDomain;
    }
    
    public String getTokenUrl() {
    	return System.getProperty("janrain.tokenUrl");
    }
    
    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }
    
    public String getDefaultProvider() {
        return defaultProvider;
    }
    
    public void setDefaultProvider(String defaultProvider) {
        this.defaultProvider = defaultProvider;
    }
    
    public String getFlags() {
        return flags;
    }
    
    public void setFlags(String flags) {
        this.flags = flags;
    }
    
    public String getLanguagePreference() {
        return languagePreference != null ? languagePreference : getConfig().getLanguagePreference();
    }
    
    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
}
