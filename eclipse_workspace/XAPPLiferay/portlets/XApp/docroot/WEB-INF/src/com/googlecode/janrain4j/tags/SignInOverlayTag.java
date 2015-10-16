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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;


/**
 * Tag to include the sign-in overlay.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see SignInLinkTag
 */
public class SignInOverlayTag extends AbstractBaseTag {

    private String defaultProvider = null;
    private String flags = null;
    private String languagePreference = null;
    
    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        
        StringWriter sw = new StringWriter();
        sw.append("<script type=\"text/javascript\">");
        sw.append("    var rpxJsHost = ((\"https:\" == document.location.protocol) ? \"https://\" : \"http://static.\");");
        sw.append("    document.write(unescape(\"%3Cscript src='\" + rpxJsHost + \"rpxnow.com/js/lib/rpx.js' type='text/javascript'%3E%3C/script%3E\"));");
        sw.append("</script>");
        sw.append("<script type=\"text/javascript\">\n");
        sw.append("    RPXNOW.overlay = true;\n");
        
        if (getDefaultProvider() != null && getDefaultProvider().length() > 0) {
            sw.append("    RPXNOW.default_provider = '").append(getDefaultProvider()).append("';\n");
        }
        
        if (getFlags() != null && getFlags().length() > 0) {
            sw.append("    RPXNOW.flags = '").append(getFlags()).append("';\n");
        }
        
        if (getLanguagePreference() != null && getLanguagePreference().length() > 0) {
            sw.append("    RPXNOW.language_preference = '").append(getLanguagePreference()).append("';\n");
        }
        
        JspFragment body = getJspBody();
        if (body != null) {
            body.invoke(sw);
        }
        
        sw.append("</script>");
        
        out.println(sw.toString());
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
}
