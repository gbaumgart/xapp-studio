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
package com.googlecode.janrain4j.api.engage.response.poco;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.janrain4j.api.engage.response.ContactsResponse;
import com.googlecode.janrain4j.api.engage.response.UserDataResponse;
import com.googlecode.janrain4j.json.JSONArray;
import com.googlecode.janrain4j.json.JSONObject;

/**
 * A dictionary containing the <a href="http://portablecontacts.net/">Portable Contacts</a> data.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see UserDataResponse
 * @see ContactsResponse
 */
@SuppressWarnings("serial")
public class Contact implements Serializable {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    
    // portable contacts
    private String id = null;
    private String displayName = null;
    private Name name = null;
    private String nickname = null;
    private Date published = null;
    private Date updated = null;
    private Date birthday = null;
    private Date anniversary = null;
    private String gender = null;
    private String note = null;
    private String preferredUsername = null;
    private String utcOffset = null;
    private boolean connected = false;
    private List<Email> emails = null;
    private List<Url> urls = null;
    private List<PhoneNumber> phoneNumbers = null;
    private List<IM> ims = null;
    private List<Photo> photos = null;
    private List<String> tags = null;
    private List<Relationship> relationships = null;
    private List<Address> addresses = null;
    private List<Organization> organizations = null;
    private List<Account> accounts = null;
    
    // open social
    private String aboutMe = null;
    private BodyType bodyType = null;
    private Address currentLocation = null;
    private String drinker = null;
    private String ethnicity = null;
    private String fashion = null;
    private String happiestWhen = null;
    private String humor = null;
    private String livingArrangement = null;
    private String lookingFor = null;
    private String profileSong = null;
    private String profileUrl = null;
    private String profileVideo = null;
    private String relationshipStatus = null;
    private String religion = null;
    private String romance = null;
    private String scaredOf = null;
    private String sexualOrientation = null;
    private String smoker = null;
    private String status = null;
    private List<String> activities = null;
    private List<String> books = null;
    private List<String> cars = null;
    private String children = null;
    private List<String> food = null;
    private List<String> heroes = null;
    private List<String> interests = null;
    private String jobInterests = null;
    private List<String> languagesSpoken = null;
    private List<String> movies = null;
    private List<String> music = null;
    private String pets = null;
    private String politicalViews = null;
    private List<String> quotes = null;
    private List<String> sports = null;
    private List<String> turnOffs = null;
    private List<String> turnOns = null;
    private List<String> tvShows = null;
    
    protected Contact() {
    }
    
    public static Contact fromJSON(JSONObject json) {
        Contact contact = null;
        if (json != null) {
            contact = new Contact();
            contact.setId(json.optString("id", null));
            contact.setDisplayName(json.optString("displayName", null));
            contact.setName(Name.fromJSON(json.optJSONObject("name")));
            contact.setNickname(json.optString("nickname", null));
            contact.setPublished(parseDateTime(json.optString("published", null)));
            contact.setUpdated(parseDateTime(json.optString("updated", null)));
            contact.setBirthday(parseDate(json.optString("birthday", null)));
            contact.setAnniversary(parseDate(json.optString("anniversary", null)));
            contact.setGender(json.optString("gender", null));
            contact.setNote(json.optString("note", null));
            contact.setPreferredUsername(json.optString("preferredUsername", null));
            contact.setUtcOffset(json.optString("utcOffset", null));
            contact.setConnected(json.optBoolean("connected"));
            
            JSONArray emailsJSONArray = json.optJSONArray("emails");
            if (emailsJSONArray != null) {
                List<Email> emails = new ArrayList<Email>();
                for (int i = 0; i < emailsJSONArray.length(); i++) {
                    emails.add(Email.fromJSON(emailsJSONArray.optJSONObject(i)));
                }
                contact.setEmails(emails);
            }
            
            JSONArray urlsJSONArray = json.optJSONArray("urls");
            if (urlsJSONArray != null) {
                List<Url> urls = new ArrayList<Url>();
                for (int i = 0; i < urlsJSONArray.length(); i++) {
                    urls.add(Url.fromJSON(urlsJSONArray.optJSONObject(i)));
                }
                contact.setUrls(urls);
            }
            
            JSONArray phoneNumbersJSONArray = json.optJSONArray("phoneNumbers");
            if (phoneNumbersJSONArray != null) {
                List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
                for (int i = 0; i < phoneNumbersJSONArray.length(); i++) {
                    phoneNumbers.add(PhoneNumber.fromJSON(phoneNumbersJSONArray.optJSONObject(i)));
                }
                contact.setPhoneNumbers(phoneNumbers);
            }
            
            JSONArray imsJSONArray = json.optJSONArray("ims");
            if (imsJSONArray != null) {
                List<IM> ims = new ArrayList<IM>();
                for (int i = 0; i < imsJSONArray.length(); i++) {
                    ims.add(IM.fromJSON(imsJSONArray.optJSONObject(i)));
                }
                contact.setIms(ims);
            }
            
            JSONArray photosJSONArray = json.optJSONArray("photos");
            if (photosJSONArray != null) {
                List<Photo> photos = new ArrayList<Photo>();
                for (int i = 0; i < photosJSONArray.length(); i++) {
                    photos.add(Photo.fromJSON(photosJSONArray.optJSONObject(i)));
                }
                contact.setPhotos(photos);
            }
            
            JSONArray tagsJSONArray = json.optJSONArray("tags");
            if (tagsJSONArray != null) {
                List<String> tags = new ArrayList<String>();
                for (int i = 0; i < tagsJSONArray.length(); i++) {
                    tags.add(tagsJSONArray.optString(i));
                }
                contact.setTags(tags);
            }
            
            JSONArray relationshipsJSONArray = json.optJSONArray("relationships");
            if (relationshipsJSONArray != null) {
                List<Relationship> relationships = new ArrayList<Relationship>();
                for (int i = 0; i < relationshipsJSONArray.length(); i++) {
                    Relationship relationship = new Relationship();
                    relationship.setValue(relationshipsJSONArray.optString(i));
                    relationships.add(relationship);
                }
                contact.setRelationships(relationships);
            }
            
            JSONArray addressesJSONArray = json.optJSONArray("addresses");
            if (addressesJSONArray != null) {
                List<Address> addresses = new ArrayList<Address>();
                for (int i = 0; i < addressesJSONArray.length(); i++) {
                    addresses.add(Address.fromJSON(addressesJSONArray.optJSONObject(i)));
                }
                contact.setAddresses(addresses);
            }
            
            JSONArray organizationsJSONArray = json.optJSONArray("organizations");
            if (organizationsJSONArray != null) {
                List<Organization> organizations = new ArrayList<Organization>();
                for (int i = 0; i < organizationsJSONArray.length(); i++) {
                    organizations.add(Organization.fromJSON(organizationsJSONArray.optJSONObject(i)));
                }
                contact.setOrganizations(organizations);
            }
            
            JSONArray accountsJSONArray = json.optJSONArray("accounts");
            if (accountsJSONArray != null) {
                List<Account> accounts = new ArrayList<Account>();
                for (int i = 0; i < accountsJSONArray.length(); i++) {
                    accounts.add(Account.fromJSON(accountsJSONArray.optJSONObject(i)));
                }
                contact.setAccounts(accounts);
            }
            
            contact.setAboutMe(json.optString("aboutMe", null));
            contact.setBodyType(BodyType.fromJSON(json.optJSONObject("bodyType")));
            contact.setCurrentLocation(Address.fromJSON(json.optJSONObject("currentLocation")));
            contact.setDrinker(json.optString("drinker", null));
            contact.setEthnicity(json.optString("ethnicity", null));
            contact.setFashion(json.optString("fashion", null));
            contact.setHappiestWhen(json.optString("happiestWhen", null));
            contact.setHumor(json.optString("humor", null));
            contact.setLivingArrangement(json.optString("livingArrangement", null));
            contact.setLookingFor(json.optString("lookingFor", null));
            contact.setProfileSong(json.optString("profileSong", null));
            contact.setProfileUrl(json.optString("profileUrl", null));
            contact.setProfileVideo(json.optString("profileVideo", null));
            contact.setRelationshipStatus(json.optString("relationshipStatus", null));
            contact.setReligion(json.optString("religion", null));
            contact.setRomance(json.optString("romance", null));
            contact.setScaredOf(json.optString("scaredOf", null));
            contact.setSexualOrientation(json.optString("sexualOrientation", null));
            contact.setSmoker(json.optString("smoker", null));
            contact.setStatus(json.optString("status", null));
            
            JSONArray activitiesJSONArray = json.optJSONArray("activities");
            if (activitiesJSONArray != null) {
                List<String> activities = new ArrayList<String>();
                for (int i = 0; i < activitiesJSONArray.length(); i++) {
                    activities.add(activitiesJSONArray.optString(i));
                }
                contact.setActivities(activities);
            }
            
            JSONArray booksJSONArray = json.optJSONArray("books");
            if (booksJSONArray != null) {
                List<String> books = new ArrayList<String>();
                for (int i = 0; i < booksJSONArray.length(); i++) {
                    books.add(booksJSONArray.optString(i));
                }
                contact.setBooks(books);
            }
            
            JSONArray carsJSONArray = json.optJSONArray("cars");
            if (carsJSONArray != null) {
                List<String> cars = new ArrayList<String>();
                for (int i = 0; i < carsJSONArray.length(); i++) {
                    cars.add(carsJSONArray.optString(i));
                }
                contact.setCars(cars);
            }
            
            contact.setChildren(json.optString("children", null));
            
            JSONArray foodJSONArray = json.optJSONArray("food");
            if (foodJSONArray != null) {
                List<String> food = new ArrayList<String>();
                for (int i = 0; i < foodJSONArray.length(); i++) {
                    food.add(foodJSONArray.optString(i));
                }
                contact.setFood(food);
            }
            
            JSONArray heroesJSONArray = json.optJSONArray("heroes");
            if (heroesJSONArray != null) {
                List<String> heroes = new ArrayList<String>();
                for (int i = 0; i < heroesJSONArray.length(); i++) {
                    heroes.add(heroesJSONArray.optString(i));
                }
                contact.setHeroes(heroes);
            }
            
            JSONArray interestsJSONArray = json.optJSONArray("interests");
            if (interestsJSONArray != null) {
                List<String> interests = new ArrayList<String>();
                for (int i = 0; i < interestsJSONArray.length(); i++) {
                    interests.add(interestsJSONArray.optString(i));
                }
                contact.setInterests(interests);
            }
            
            contact.setJobInterests(json.optString("jobInterests", null));
            
            JSONArray languagesSpokenJSONArray = json.optJSONArray("languagesSpoken");
            if (languagesSpokenJSONArray != null) {
                List<String> languagesSpoken = new ArrayList<String>();
                for (int i = 0; i < languagesSpokenJSONArray.length(); i++) {
                    languagesSpoken.add(languagesSpokenJSONArray.optString(i));
                }
                contact.setLanguagesSpoken(languagesSpoken);
            }
            
            JSONArray moviesJSONArray = json.optJSONArray("movies");
            if (moviesJSONArray != null) {
                List<String> movies = new ArrayList<String>();
                for (int i = 0; i < moviesJSONArray.length(); i++) {
                    movies.add(moviesJSONArray.optString(i));
                }
                contact.setMovies(movies);
            }
            
            JSONArray musicJSONArray = json.optJSONArray("music");
            if (musicJSONArray != null) {
                List<String> music = new ArrayList<String>();
                for (int i = 0; i < musicJSONArray.length(); i++) {
                    music.add(musicJSONArray.optString(i));
                }
                contact.setMusic(music);
            }
            
            contact.setPets(json.optString("pets", null));
            contact.setPoliticalViews(json.optString("politicalViews", null));
            
            JSONArray quotesJSONArray = json.optJSONArray("quotes");
            if (quotesJSONArray != null) {
                List<String> quotes = new ArrayList<String>();
                for (int i = 0; i < quotesJSONArray.length(); i++) {
                    quotes.add(quotesJSONArray.optString(i));
                }
                contact.setQuotes(quotes);
            }
            
            JSONArray sportsJSONArray = json.optJSONArray("sports");
            if (sportsJSONArray != null) {
                List<String> sports = new ArrayList<String>();
                for (int i = 0; i < sportsJSONArray.length(); i++) {
                    sports.add(sportsJSONArray.optString(i));
                }
                contact.setSports(sports);
            }
            
            JSONArray turnOffsJSONArray = json.optJSONArray("turnOffs");
            if (turnOffsJSONArray != null) {
                List<String> turnOffs = new ArrayList<String>();
                for (int i = 0; i < turnOffsJSONArray.length(); i++) {
                    turnOffs.add(turnOffsJSONArray.optString(i));
                }
                contact.setTurnOffs(turnOffs);
            }
            
            JSONArray turnOnsJSONArray = json.optJSONArray("turnOns");
            if (turnOnsJSONArray != null) {
                List<String> turnOns = new ArrayList<String>();
                for (int i = 0; i < turnOnsJSONArray.length(); i++) {
                    turnOns.add(turnOnsJSONArray.optString(i));
                }
                contact.setTurnOns(turnOns);
            }
            
            JSONArray tvShowsJSONArray = json.optJSONArray("tvShows");
            if (tvShowsJSONArray != null) {
                List<String> tvShows = new ArrayList<String>();
                for (int i = 0; i < tvShowsJSONArray.length(); i++) {
                    tvShows.add(tvShowsJSONArray.optString(i));
                }
                contact.setTvShows(tvShows);
            }
        }
        return contact;
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
    
    private static Date parseDateTime(String dateTime) {
        if (dateTime != null && dateTime.length() > 0) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_TIME_PATTERN);
            try {
                return dateFormatter.parse(dateTime);
            }
            catch (ParseException ignore) {
            }
        }
        return null;
    }
    
    /**
     * Returns the unique identifier for the contact.
     * 
     * @return The unique identifier or <code>null</code> if not found in response.
     */
    public String getId() {
        return id;
    }
    
    void setId(String id) {
        this.id = id;
    }
    
    /**
     * Returns the name of the contact, suitable for display to end-users.
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
     * Returns the broken-out components and fully formatted version of the 
     * contact's real name.
     * 
     * @return The name or <code>null</code> if not found in response.
     */
    public Name getName() {
        return name;
    }
    
    void setName(Name name) {
        this.name = name;
    }
    
    /**
     * Returns the casual way to address the Contact in real life, e.g. "Bob" 
     * or "Bobby" instead of "Robert".
     * 
     * @return The nickname or <code>null</code> if not found in response.
     */
    public String getNickname() {
        return nickname;
    }
    
    void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    /**
     * Returns the date the contact was first added to the user's address book 
     * or friends list (i.e. the creation date of this entry).
     * 
     * @return The published date or <code>null</code> if not found in response.
     */
    public Date getPublished() {
        return published;
    }
    
    void setPublished(Date published) {
        this.published = published;
    }
    
    /**
     * Returns the most recent date the details of the contact were updated 
     * (i.e. the modified date of this entry).
     * 
     * @return The updated date or <code>null</code> if not found in response.
     */
    public Date getUpdated() {
        return updated;
    }
    
    void setUpdated(Date updated) {
        this.updated = updated;
    }
    
    /**
     * Returns the birthday of the contact.
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
     * Returns the wedding anniversary of the contact.
     * 
     * @return The wedding anniversary or <code>null</code> if not found in response.
     */
    public Date getAnniversary() {
        return anniversary;
    }
    
    void setAnniversary(Date anniversary) {
        this.anniversary = anniversary;
    }
    
    /**
     * Returns the gender of the contact.
     * 
     * @return The gender or <code>null</code> if not found in response.
     */
    public String getGender() {
        return gender;
    }
    
    /**
     * Returns true if the gender of the contact is male. 
     */
    public boolean isMale() {
        return MALE.equalsIgnoreCase(gender);
    }
    
    /**
     * Returns true if the gender of the contact is female. 
     */
    public boolean isFemale() {
        return FEMALE.equalsIgnoreCase(gender);
    }
    
    void setGender(String gender) {
        this.gender = gender;
    }
    
    /**
     * Returns notes about this contact, with an unspecified meaning or usage.
     * 
     * @return The notes or <code>null</code> if not found in response.
     */
    public String getNote() {
        return note;
    }
    
    void setNote(String note) {
        this.note = note;
    }
    
    /**
     * Returns the preferred username of the contact on sites that ask for a 
     * username (e.g. jsmarr or daveman692).
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
     * Returns the offset from UTC of the contact's current time zone, as of the 
     * time this response was returned.
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
     * Returns true if the user and the Contact have established a 
     * bi-directionally asserted connection of some kind on the Service 
     * Provider's service.
     */
    public boolean isConnected() {
        return connected;
    }
    
    void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    /**
     * Returns the email addresses for the contact.
     * 
     * @return The email addresses or <code>null</code> if not found in response.
     */
    public List<Email> getEmails() {
        return emails;
    }
    
    void setEmails(List<Email> emails) {
        this.emails = emails;
    }
    
    /**
     * Returns the URL's of web pages relating to the contact.
     * 
     * @return The URL's or <code>null</code> if not found in response.
     */
    public List<Url> getUrls() {
        return urls;
    }
    
    void setUrls(List<Url> urls) {
        this.urls = urls;
    }
    
    /**
     * Returns the phone numbers for the contact.
     * 
     * @return The phone numbers or <code>null</code> if not found in response.
     */
    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }
    
    void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
    
    /**
     * Returns the instant messaging addresses for the contact.
     * 
     * @return The instant messaging addresses or <code>null</code> if not found in response.
     */
    public List<IM> getIms() {
        return ims;
    }
    
    void setIms(List<IM> ims) {
        this.ims = ims;
    }
    
    /**
     * Returns the URL's of photos of the contact.
     * 
     * @return The photos or <code>null</code> if not found in response.
     */
    public List<Photo> getPhotos() {
        return photos;
    }
    
    void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
    
    /**
     * Returns the user-defined categories or labels for the contact, e.g. "favorite" or "web20".
     * 
     * @return The tags or <code>null</code> if not found in response.
     */
    public List<String> getTags() {
        return tags;
    }
    
    void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    /**
     * Returns the bi-directionally asserted relationships that were established 
     * between the user and the contact by the Service Provider.
     * 
     * @return The relationships or <code>null</code> if not found in response.
     */
    public List<Relationship> getRelationships() {
        return relationships;
    }
    
    void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }
    
    /**
     * Returns the physical mailing addresses for the contact.
     * 
     * @return The physical mailing addresses or <code>null</code> if not found in response.
     */
    public List<Address> getAddresses() {
        return addresses;
    }
    
    void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
    
    /**
     * Returns the current or past organizational affiliations of the contact.
     * 
     * @return The organizations or <code>null</code> if not found in response.
     */
    public List<Organization> getOrganizations() {
        return organizations;
    }
    
    void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }
    
    /**
     * Returns the online accounts held by the contact.
     * 
     * @return The accounts or <code>null</code> if not found in response.
     */
    public List<Account> getAccounts() {
        return accounts;
    }
    
    void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
    
    /**
     * Returns the general statement about the contact.
     * 
     * @return The general statement or <code>null</code> if not found in response.
     */
    public String getAboutMe() {
        return aboutMe;
    }
    
    void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
    
    /**
     * Returns the body characteristics of the contact.
     * 
     * @return The body characteristics or <code>null</code> if not found in response.
     */
    public BodyType getBodyType() {
        return bodyType;
    }
    
    void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }
    
    /**
     * Returns the current location of the contact.
     * 
     * @return The current location or <code>null</code> if not found in response.
     */
    public Address getCurrentLocation() {
        return currentLocation;
    }
    
    void setCurrentLocation(Address currentLocation) {
        this.currentLocation = currentLocation;
    }
    
    /**
     * Returns the drinking status of the contact.
     * 
     * @return The drinking status or <code>null</code> if not found in response.
     */
    public String getDrinker() {
        return drinker;
    }
    
    void setDrinker(String drinker) {
        this.drinker = drinker;
    }
    
    /**
     * Returns the ethnicity of the contact.
     * 
     * @return The ethnicity or <code>null</code> if not found in response.
     */
    public String getEthnicity() {
        return ethnicity;
    }
    
    void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }
    
    /**
     * Returns the thoughts on fashion of the contact.
     * 
     * @return The thoughts on fashion or <code>null</code> if not found in response.
     */
    public String getFashion() {
        return fashion;
    }
    
    void setFashion(String fashion) {
        this.fashion = fashion;
    }
    
    /**
     * Returns the description of when the contact is happiest.
     * 
     * @return The description of when the contact is happiest or <code>null</code> if not found in response.
     */
    public String getHappiestWhen() {
        return happiestWhen;
    }
    
    void setHappiestWhen(String happiestWhen) {
        this.happiestWhen = happiestWhen;
    }
    
    /**
     * Returns the thoughts on humor of the contact.
     * 
     * @return The thoughts on humor or <code>null</code> if not found in response.
     */
    public String getHumor() {
        return humor;
    }
    
    void setHumor(String humor) {
        this.humor = humor;
    }
    
    /**
     * Returns the description of the living arrangement of the contact.
     * 
     * @return The description of the living arrangement or <code>null</code> if not found in response.
     */
    public String getLivingArrangement() {
        return livingArrangement;
    }
    
    void setLivingArrangement(String livingArrangement) {
        this.livingArrangement = livingArrangement;
    }
    
    /**
     * Returns the statement about who or what the contact is looking for, or 
     * what the contact is interested in meeting people for.
     * 
     * @return What the contact is looking for or <code>null</code> if not found in response.
     */
    public String getLookingFor() {
        return lookingFor;
    }
    
    void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }
    
    /**
     * Returns the profile song of the contact.
     * 
     * @return The profile song or <code>null</code> if not found in response.
     */
    public String getProfileSong() {
        return profileSong;
    }
    
    void setProfileSong(String profileSong) {
        this.profileSong = profileSong;
    }
    
    /**
     * Returns the profile url of the contact.
     * 
     * @return The profile url or <code>null</code> if not found in response.
     */
    public String getProfileUrl() {
        return profileUrl;
    }
    
    void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
    
    /**
     * Returns the profile video of the contact.
     * 
     * @return The profile video or <code>null</code> if not found in response.
     */
    public String getProfileVideo() {
        return profileVideo;
    }
    
    void setProfileVideo(String profileVideo) {
        this.profileVideo = profileVideo;
    }
    
    /**
     * Returns the relationship status of the contact.
     * 
     * @return The relationship status or <code>null</code> if not found in response.
     */
    public String getRelationshipStatus() {
        return relationshipStatus;
    }
    
    void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }
    
    /**
     * Returns the religion or religious views of the contact.
     * 
     * @return The religion or religious views or <code>null</code> if not found in response.
     */
    public String getReligion() {
        return religion;
    }
    
    void setReligion(String religion) {
        this.religion = religion;
    }
    
    /**
     * Returns the comments about romance of the contact.
     * 
     * @return The comments about romance or <code>null</code> if not found in response.
     */
    public String getRomance() {
        return romance;
    }
    
    void setRomance(String romance) {
        this.romance = romance;
    }
    
    /**
     * Returns what the contact is scared of.
     * 
     * @return What the contact is scared of or <code>null</code> if not found in response.
     */
    public String getScaredOf() {
        return scaredOf;
    }
    
    void setScaredOf(String scaredOf) {
        this.scaredOf = scaredOf;
    }
    
    /**
     * Returns the sexual orientation of the contact.
     * 
     * @return The sexual orientation or <code>null</code> if not found in response.
     */
    public String getSexualOrientation() {
        return sexualOrientation;
    }
    
    void setSexualOrientation(String sexualOrientation) {
        this.sexualOrientation = sexualOrientation;
    }
    
    /**
     * Returns the smoking status of the contact.
     * 
     * @return The smoking status or <code>null</code> if not found in response.
     */
    public String getSmoker() {
        return smoker;
    }
    
    void setSmoker(String smoker) {
        this.smoker = smoker;
    }
    
    /**
     * Returns the status, headline or shoutout of the contact.
     * 
     * @return The status, headline or shoutout or <code>null</code> if not found in response.
     */
    public String getStatus() {
        return status;
    }
    
    void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Returns the favorite activities of the contact.
     * 
     * @return The favorite activities or <code>null</code> if not found in response.
     */
    public List<String> getActivities() {
        return activities;
    }
    
    void setActivities(List<String> activities) {
        this.activities = activities;
    }
    
    /**
     * Returns the favorite books of the contact.
     * 
     * @return The favorite books or <code>null</code> if not found in response.
     */
    public List<String> getBooks() {
        return books;
    }
    
    void setBooks(List<String> books) {
        this.books = books;
    }
    
    /**
     * Returns the favorite cars of the contact.
     * 
     * @return The favorite cars or <code>null</code> if not found in response.
     */
    public List<String> getCars() {
        return cars;
    }
    
    void setCars(List<String> cars) {
        this.cars = cars;
    }

    /**
     * Returns the description of the children of the contact.
     * 
     * @return The description of the children or <code>null</code> if not found in response.
     */
    public String getChildren() {
        return children;
    }
    
    void setChildren(String children) {
        this.children = children;
    }
    
    /**
     * Returns the favorite food of the contact.
     * 
     * @return The favorite food or <code>null</code> if not found in response.
     */
    public List<String> getFood() {
        return food;
    }
    
    void setFood(List<String> food) {
        this.food = food;
    }
    
    /**
     * Returns the favorite heroes of the contact.
     * 
     * @return The favorite heroes or <code>null</code> if not found in response.
     */
    public List<String> getHeroes() {
        return heroes;
    }
    
    void setHeroes(List<String> heroes) {
        this.heroes = heroes;
    }
    
    /**
     * Returns the interests, hobbies or passions of the contact.
     * 
     * @return The interests, hobbies or passions or <code>null</code> if not found in response.
     */
    public List<String> getInterests() {
        return interests;
    }
    
    void setInterests(List<String> interests) {
        this.interests = interests;
    }
    
    /**
     * Returns the favorite jobs, or job interests and skills of the contact.
     * 
     * @return The favorite jobs, or job interests and skills or <code>null</code> if not found in response.
     */
    public String getJobInterests() {
        return jobInterests;
    }
    
    void setJobInterests(String jobInterests) {
        this.jobInterests = jobInterests;
    }
    
    /**
     * Returns the languages the contact speaks as ISO 639-1 codes.
     * 
     * @return The languages or <code>null</code> if not found in response.
     */
    public List<String> getLanguagesSpoken() {
        return languagesSpoken;
    }
    
    void setLanguagesSpoken(List<String> languagesSpoken) {
        this.languagesSpoken = languagesSpoken;
    }
    
    /**
     * Returns the favorite movies of the contact.
     * 
     * @return The favorite movies or <code>null</code> if not found in response.
     */
    public List<String> getMovies() {
        return movies;
    }
    
    void setMovies(List<String> movies) {
        this.movies = movies;
    }
    
    /**
     * Returns the favorite music of the contact.
     * 
     * @return The favorite music or <code>null</code> if not found in response.
     */
    public List<String> getMusic() {
        return music;
    }
    
    void setMusic(List<String> music) {
        this.music = music;
    }
    
    /**
     * Returns the description of the pets of the contact.
     * 
     * @return The description of the pets or <code>null</code> if not found in response.
     */
    public String getPets() {
        return pets;
    }
    
    void setPets(String pets) {
        this.pets = pets;
    }
    
    /**
     * Returns the political views of the contact.
     * 
     * @return The political views or <code>null</code> if not found in response.
     */
    public String getPoliticalViews() {
        return politicalViews;
    }
    
    void setPoliticalViews(String politicalViews) {
        this.politicalViews = politicalViews;
    }
    
    /**
     * Returns the favorite quotes of the contact.
     * 
     * @return The favorite quotes or <code>null</code> if not found in response.
     */
    public List<String> getQuotes() {
        return quotes;
    }
    
    void setQuotes(List<String> quotes) {
        this.quotes = quotes;
    }
    
    /**
     * Returns the favorite sports of the contact.
     * 
     * @return The favorite sports or <code>null</code> if not found in response.
     */
    public List<String> getSports() {
        return sports;
    }
    
    void setSports(List<String> sports) {
        this.sports = sports;
    }
    
    /**
     * Returns the turn offs of the contact.
     * 
     * @return The turn offs or <code>null</code> if not found in response.
     */
    public List<String> getTurnOffs() {
        return turnOffs;
    }
    
    void setTurnOffs(List<String> turnOffs) {
        this.turnOffs = turnOffs;
    }
    
    /**
     * Returns the turn ons of the contact.
     * 
     * @return The turn ons or <code>null</code> if not found in response.
     */
    public List<String> getTurnOns() {
        return turnOns;
    }
    
    void setTurnOns(List<String> turnOns) {
        this.turnOns = turnOns;
    }
    
    /**
     * Returns the favorite TV shows the contact.
     * 
     * @return The favorite TV shows or <code>null</code> if not found in response.
     */
    public List<String> getTvShows() {
        return tvShows;
    }
    
    void setTvShows(List<String> tvShows) {
        this.tvShows = tvShows;
    }
}
