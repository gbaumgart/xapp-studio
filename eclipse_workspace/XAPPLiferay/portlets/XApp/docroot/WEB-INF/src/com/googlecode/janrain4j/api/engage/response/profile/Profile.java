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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.janrain4j.api.engage.response.UserDataResponse;
import com.googlecode.janrain4j.json.JSONException;
import com.googlecode.janrain4j.json.JSONObject;

/**
 * A dictionary of fields forming the user's profile. This data may have been 
 * obtained through SREG, HCard, but is represented in the standard Portable 
 * Contacts schema.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see UserDataResponse
 * @see <a href="http://rpxnow.com/docs#profile_data">Profile data documentation</a>
 */
@SuppressWarnings("serial")
public class Profile implements Serializable {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    
    private String identifier = null;
    private String providerName = null;
    private String primaryKey = null;
    private String displayName = null;
    private String preferredUsername = null;
    private Name name = null;
    private String gender = null;
    private Date birthday = null;
    private String utcOffset = null;
    private String email = null;
    private String verifiedEmail = null;
    private String url = null;
    private String phoneNumber = null;
    private String photo = null;
    private Address address = null;
    
    protected Profile() {
    }
    
    public static Profile fromJSON(JSONObject json) throws JSONException {
        Profile profile = null;
        if (json != null) {
            profile = new Profile();
            profile.setIdentifier(json.getString("identifier"));
            profile.setProviderName(json.getString("providerName"));
            profile.setPrimaryKey(json.optString("primaryKey", null));
            profile.setDisplayName(json.optString("displayName", null));
            profile.setPreferredUsername(json.optString("preferredUsername", null));
            profile.setName(Name.fromJSON(json.optJSONObject("name")));
            profile.setGender(json.optString("gender", null));
            profile.setBirthday(parseDate(json.optString("birthday", null)));
            profile.setUtcOffset(json.optString("utcOffset", null));
            profile.setEmail(json.optString("email", null));
            profile.setEmail(json.optString("email", null));
            profile.setVerifiedEmail(json.optString("verifiedEmail", null));
            profile.setUrl(json.optString("url", null));
            profile.setPhoneNumber(json.optString("phoneNumber", null));
            profile.setPhoto(json.optString("photo", null));
            profile.setAddress(Address.fromJSON(json.optJSONObject("address")));
        }
        return profile;
    }
    
    private static Date parseDate(String date) {
        if (date != null && date.length() > 0) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_PATTERN);
            try {
                return dateFormatter.parse(date);
            }
            catch (ParseException ignore) {
            }
        }
        return null;
    }
    
    /**
     * Returns the user's OpenID URL. Use this value to sign the user in to 
     * your website. This field is always present.
     * 
     * @return The user's OpenID URL.
     */
    public String getIdentifier() {
        return identifier;
    }
    
    void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    /**
     * Returns the human-readable name of the authentication provider that was 
     * used for this authentication. For well-known providers, Janrain Engage 
     * sends values such as "Google", "Facebook", and "MySpace"; "Other" is 
     * sent for other providers. New provider names are added over time.
     * 
     * @return The human-readable name of the authentication provider.
     */
    public String getProviderName() {
        return providerName;
    }
    
    void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    
    /**
     * Returns the primary key of the user in your database. Only present if 
     * you are using the mapping API.
     * 
     * @return The primary key or <code>null</code> if not found in response.
     * @see <a href="http://rpxnow.com/docs#mappings">Mappings</a>
     */
    public String getPrimaryKey() {
        return primaryKey;
    }
    
    void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    /**
     * Returns the name of the user, suitable for display to end-users.
     * 
     * @return The display name or <code>null</code> if not found in response.
     */
    public String getDisplayName() {
        return displayName;
    }
    
    void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Returns the preferred username of the user on sites that ask for a 
     * username.
     * 
     * @return The preferred username or <code>null</code> if not found in response.
     */
    public String getPreferredUsername() {
        return preferredUsername;
    }
    
    void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }
    
    /**
     * Returns the name of the user.
     * 
     * @return The name or <code>null</code> if not found in response.
     * @see <a href="http://rpxnow.com/docs#profile_name">Profile data documentation</a>
     */
    public Name getName() {
        return name;
    }
    
    void setName(Name name) {
        this.name = name;
    }
    
    /**
     * Returns the gender of the user. Canonical values are 'male', and 
     * 'female', but may be any value.
     * 
     * @return The gender or <code>null</code> if not found in response.
     */
    public String getGender() {
        return gender;
    }
    
    /**
     * Returns true if the gender of the user is male. 
     */
    public boolean isMale() {
        return MALE.equalsIgnoreCase(gender);
    }
    
    /**
     * Returns true if the gender of the user is female. 
     */
    public boolean isFemale() {
        return FEMALE.equalsIgnoreCase(gender);
    }
    
    void setGender(String gender) {
        this.gender = gender;
    }
    
    /**
     * Returns the date of birth of the user. Year field may be 0000 if 
     * unavailable.
     * 
     * @return The birthday or <code>null</code> if not found in response.
     */
    public Date getBirthday() {
        return birthday;
    }
    
    void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    
    /**
     * Returns the offset from UTC of the user's current time zone, as of the 
     * time this response was returned. The value MUST conform to the offset 
     * portion of xs:dateTime, e.g. -08:00. Note that this value MAY change 
     * over time due to daylight saving time, and is thus meant to signify only 
     * the current value of the user's timezone offset.
     * 
     * @return The UTC offset or <code>null</code> if not found in response.
     */
    public String getUtcOffset() {
        return utcOffset;
    }
    
    void setUtcOffset(String utcOffset) {
        this.utcOffset = utcOffset;
    }
    
    /**
     * Returns the email address at which the user may be reached.
     * 
     * @return The email or <code>null</code> if not found in response.
     */
    public String getEmail() {
        return email;
    }
    
    void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Returns the email address at which the person may be reached.
     * 
     * @return The verified email or <code>null</code> if not found in response.
     */
    public String getVerifiedEmail() {
        return verifiedEmail;
    }
    
    void setVerifiedEmail(String verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }
    
    /**
     * Returns the URL of a webpage relating to this person.
     * 
     * @return The URL or <code>null</code> if not found in response.
     */
    public String getUrl() {
        return url;
    }
    
    void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * Returns the phone number at which the user may be reached.
     * 
     * @return The phone number or <code>null</code> if not found in response.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Returns the URL to a photo (GIF/JPG/PNG) of the user.
     * 
     * @return The URL or <code>null</code> if not found in response.
     */
    public String getPhoto() {
        return photo;
    }
    
    void setPhoto(String photo) {
        this.photo = photo;
    }
    
    /**
     * Returns the address of the user.
     * 
     * @return The address or <code>null</code> if not found in response.
     * @see <a href="http://rpxnow.com/docs#profile_address">Profile data documentation</a>
     */
    public Address getAddress() {
        return address;
    }
    
    void setAddress(Address address) {
        this.address = address;
    }
}
