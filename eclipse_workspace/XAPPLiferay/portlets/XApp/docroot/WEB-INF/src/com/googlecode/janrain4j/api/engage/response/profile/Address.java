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
package com.googlecode.janrain4j.api.engage.response.profile;

import java.io.Serializable;

import com.googlecode.janrain4j.json.JSONException;
import com.googlecode.janrain4j.json.JSONObject;

/**
 * A dictionary of address parts.
 * 
 * @author Marcel Overdijk
 * @see <a href="http://rpxnow.com/docs#profile_address">Profile data documentation</a>
 * @since 1.0
 * @see Profile
 */
@SuppressWarnings("serial")
public class Address implements Serializable {

    private String formatted = null;
    private String streetAddress = null;
    private String locality = null;
    private String region = null;
    private String postalCode = null;
    private String country = null;
    
    protected Address() {
    }
    
    public static Address fromJSON(JSONObject json) throws JSONException {
        Address address = null;
        if (json != null) {
            address = new Address();
            address.setFormatted(json.optString("formatted", null));
            address.setStreetAddress(json.optString("streetAddress", null));
            address.setLocality(json.optString("locality", null));
            address.setRegion(json.optString("region", null));
            address.setPostalCode(json.optString("postalCode", null));
            address.setCountry(json.optString("country", null));
        }
        return address;
    }
    
    /**
     * Returns the full mailing address, formatted for display or use with a 
     * mailing label.
     * 
     * @return The formatted address or <code>null</code> if not found in response.
     */
    public String getFormatted() {
        return formatted;
    }
    
    void setFormatted(String formatted) {
        this.formatted = formatted;
    }
    
    /**
     * Returns the full street address, which may include house number, street 
     * name, PO BOX, and multi-line extended street address information.
     * 
     * @return The street address or <code>null</code> if not found in response.
     */
    public String getStreetAddress() {
        return streetAddress;
    }
    
    void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    
    /**
     * Returns the city or locality.
     * 
     * @return The city or locality or <code>null</code> if not found in response.
     */
    public String getLocality() {
        return locality;
    }
    
    void setLocality(String locality) {
        this.locality = locality;
    }
    
    /**
     * Returns the state or region.
     * 
     * @return The state or region or <code>null</code> if not found in response.
     */
    public String getRegion() {
        return region;
    }
    
    void setRegion(String region) {
        this.region = region;
    }
    
    /**
     * Returns the postal code or zipcode.
     * 
     * @return The postal code or zipcode or <code>null</code> if not found in response.
     */
    public String getPostalCode() {
        return postalCode;
    }
    
    void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    /**
     * Returns the country name.
     * 
     * @return The country name or <code>null</code> if not found in response.
     */
    public String getCountry() {
        return country;
    }
    
    void setCountry(String country) {
        this.country = country;
    }
}
