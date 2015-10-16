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
import com.googlecode.janrain4j.api.engage.response.poco.Contact;
import com.googlecode.janrain4j.json.JSONArray;
import com.googlecode.janrain4j.json.JSONException;
import com.googlecode.janrain4j.json.JSONObject;

/**
 * The <code>ContactsResponse</code> contains a list of contacts for the 
 * indentifier in the <a href="http://portablecontacts.net/">Portable Contacts</a> format.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see EngageService#getContacts(String)
 */
@SuppressWarnings("serial")
public class ContactsResponse extends AbstractEngageResponse {

    private List<Contact> contacts = null;
    
    public ContactsResponse(String json) throws EngageFailureException {
        super(json);
        JSONObject rsp = getResponseAsJSONObject();
        JSONObject rspResponse = rsp.optJSONObject("response");
        if (rspResponse != null) {
            JSONArray rspEntry = rspResponse.optJSONArray("entry");
            if (rspEntry != null) {
                contacts = new ArrayList<Contact>();
                for (int i = 0; i < rspEntry.length(); i++) {
                    try {
                        contacts.add(Contact.fromJSON(rspEntry.getJSONObject(i)));
                    }
                    catch (JSONException e) {
                        throw new EngageFailureException("Unexpected JSON error", json, e);
                    }
                }
            }
        }
    }
    
    /**
     * Returns all list of contacts for the indentifier.
     * 
     * @return The list of contacts or <code>null</code> if not found in response.
     */
    public List<Contact> getContacts() {
        return contacts;
    }
}
