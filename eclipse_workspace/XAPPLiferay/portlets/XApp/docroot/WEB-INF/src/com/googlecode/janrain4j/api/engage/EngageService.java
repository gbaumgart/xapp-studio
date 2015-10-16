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
package com.googlecode.janrain4j.api.engage;

import java.util.Date;
import java.util.List;

import com.googlecode.janrain4j.api.engage.request.Activity;
import com.googlecode.janrain4j.api.engage.response.AllMappingsResponse;
import com.googlecode.janrain4j.api.engage.response.AnalyticsResponse;
import com.googlecode.janrain4j.api.engage.response.ContactsResponse;
import com.googlecode.janrain4j.api.engage.response.MappingsResponse;
import com.googlecode.janrain4j.api.engage.response.UserDataResponse;


/**
 * The <code>EngageService</code> provides access to the Janrain Engage API.
 * <p>
 * Note that methods might throw a {@link ErrorResponeException} in case your 
 * plan (Basic, Plus or Pro) is not sufficient for making the particular 
 * Janrain Engage API call. 
 * 
 * @author Marcel Overdijk
 * @see <a href="http://rpxnow.com/docs">Janrain Engage Documentation</a>
 * @since 1.0
 */
public interface EngageService {
    
    /**
     * Get information about the user currently signing in to your web application.
     * 
     * @param token The token parameter received at your "token_url".
     * @return The user data Janrain Engage knows about the user signing into your website.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_auth_info">Janrain Engage API Documentation: auth_info</a>
     * @since 1.0
     */
    public UserDataResponse authInfo(String token) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Get information about the user currently signing in to your web application.
     * 
     * @param token The token parameter received at your "token_url".
     * @param extended 'true' or 'false'(default). Return the extended Simple Registration and HCard data in addition to the normalized Portable Contacts format.
     * @return The user data with Janrain Engage knows about the user signing into your website.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_auth_info">Janrain Engage API Documentation: auth_info</a>
     * @since 1.0
     */
    public UserDataResponse authInfo(String token, boolean extended) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Retrieve a list of contacts for an identifier in the <a href="http://portablecontacts.net/">Portable Contacts</a> format.
     * 
     * @param identifier The identifier returned from a previous {@link #authInfo(String)} API call.
     * @return The <a href="http://portablecontacts.net/">Portable Contacts</a> data representing the address book contents.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_get_contacts">Janrain Engage API Documentation: get_contacts</a>
     * @since 1.0
     */
    public ContactsResponse getContacts(String identifier) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Obtain an up-to-date copy of a user's profile as previously returned by 
     * an {@link #authInfo(String)} API call.
     * 
     * @param identifier The identifier returned from a previous {@link #authInfo(String)} API call.
     * @return The user data Janrain Engage knows about the user.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs/get_user_data">Janrain Engage API Documentation: get_user_data</a>
     * @since 1.0
     */
    public UserDataResponse getUserData(String identifier) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Obtain an up-to-date copy of a user's profile as previously returned by 
     * an {@link #authInfo(String)} API call.
     * 
     * @param identifier The identifier returned from a previous {@link #authInfo(String)} API call.
     * @param extended 'true' or 'false'(default). Return the extended Simple Registration and HCard data in addition to the normalized Portable Contacts format.
     * @return The user data Janrain Engage knows about the user.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs/get_user_data">Janrain Engage API Documentation: get_user_data</a>
     * @since 1.0
     */
    public UserDataResponse getUserData(String identifier, boolean extended) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Set the status message for the account corresponding to an identifier.
     * 
     * @param identifier The identifier returned from the {@link #authInfo(String)} API call.
     * @param status The status message to set. No length restriction on the status is imposed by Janrain Engage, but some providers may truncate it implicitly (e.g., Twitter).
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_set_status">Janrain Engage API Documentation: set_status</a>
     * @since 1.0
     */
    public void setStatus(String identifier, String status) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Set the status message for the account corresponding to an identifier.
     * 
     * @param identifier The identifier returned from the {@link #authInfo(String)} API call.
     * @param status The status message to set. No length restriction on the status is imposed by Janrain Engage, but some providers may truncate it implicitly (e.g., Twitter).
     * @param location This is a string containing location data associated with the content being published. The string is latitude, followed by longitude, for example "37.4220 -122.0843". Valid values for latitude are -90.0 to +90.0, with North being positive. Valid values for longitude are -180.0 to +180.0 with East being positive.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_set_status">Janrain Engage API Documentation: set_status</a>
     * @since 1.0
     */
    public void setStatus(String identifier, String status, String location) throws EngageFailureException, ErrorResponeException;

    /**
     * Returns true if the provider name supports the {@link #setStatus(String, String)} API call.
     * 
     * @param providerName The provider name
     * @return True if supported.
     * @since 1.1
     */
    public boolean supportsSetStatus(String providerName);
    
    /**
     * Map an OpenID identifier to a primary key. Future logins by this owner 
     * of this OpenID will return the mapped primaryKey in the {@link #authInfo(String)} 
     * API response, which you may use to sign the user in.
     * <p>
     * The map call is usually made right after a call to {@link #authInfo(String)}, 
     * when you already know who the user is because they are signed in to 
     * your website with their legacy (or existing) account.
     * </p>
     * 
     * @param identifier The identifier returned from the {@link #authInfo(String)} API call.
     * @param primaryKey The primary key from your users table, as a string.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_map">Janrain Engage API Documentation: map</a>
     * @since 1.0
     */
    public void map(String identifier, String primaryKey) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Map an OpenID identifier to a primary key. Future logins by this owner 
     * of this OpenID will return the mapped primaryKey in the {@link #authInfo(String)} 
     * API response, which you may use to sign the user in.
     * <p>
     * The map call is usually made right after a call to {@link #authInfo(String)}, 
     * when you already know who the user is because they are signed in to 
     * your website with their legacy (or existing) account.
     * </p>
     * 
     * @param identifier The identifier returned from the {@link #authInfo(String)} API call.
     * @param primaryKey The primary key from your users table, as a string.
     * @param overwrite 'true'(default) or 'false'. If 'false', only create the mapping if one does not already exist for the specified identifier.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_map">Janrain Engage API Documentation: map</a>
     * @since 1.0
     */
    public void map(String identifier, String primaryKey, boolean overwrite) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Remove (unmap) all OpenID identifiers from a primary key.
     * 
     * @param primaryKey The primary key from your users table, as a string.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_unmap">Janrain Engage API Documentation: unmap</a>
     * @since 1.0
     */
    public void unmap(String primaryKey) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Remove (unmap) all OpenID identifiers from a primary key, and optionally 
     * unlink your application from the user's account with the provider.
     * 
     * @param primaryKey The primary key from your users table, as a string.
     * @param unlink 'true' or 'false'(default). If 'true', unlink your application from the user's account with the provider.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_unmap">Janrain Engage API Documentation: unmap</a>
     * @since 1.0
     */
    public void unmap(String primaryKey, boolean unlink) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Remove (unmap) an OpenID identifier from a primary key.
     * 
     * @param identifier The identifier returned from the auth_info API call.
     * @param primaryKey The primary key from your users table, as a string.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_unmap">Janrain Engage API Documentation: unmap</a>
     * @since 1.0
     */
    public void unmap(String identifier, String primaryKey) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Remove (unmap) an OpenID identifier from a primary key, and optionally 
     * unlink your application from the user's account with the provider.
     * 
     * @param identifier The identifier returned from the auth_info API call.
     * @param primaryKey The primary key from your users table, as a string.
     * @param unlink 'true' or 'false'(default). If 'true', unlink your application from the user's account with the provider.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_unmap">Janrain Engage API Documentation: unmap</a>
     * @since 1.0
     */
    public void unmap(String identifier, String primaryKey, boolean unlink) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Get all stored mappings for a particular primary key.
     * 
     * @param primaryKey The primary key from your users table, as a string.
     * @return All stored mappings for a particular primary key.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_mappings">Janrain Engage API Documentation: mappings</a>
     * @since 1.0
     */
    public MappingsResponse mappings(String primaryKey) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Get all stored mappings for the application.
     * 
     * @return All stored mappings for the application.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_all_mappings">Janrain Engage API Documentation: all_mappings</a>
     * @since 1.0
     */
    public AllMappingsResponse allMappings() throws EngageFailureException, ErrorResponeException;
    
    /**
     * Post an activity update to the user's activity stream.
     * 
     * @param identifier The identifier returned from the {@link #authInfo(String)} API call.
     * @param activity The activity structure.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_activity">Janrain Engage API Documentation: activity</a>
     * @since 1.0
     */
    public void activity(String identifier, Activity activity) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Post an activity update to the user's activity stream.
     * 
     * @param identifier The identifier returned from the {@link #authInfo(String)} API call.
     * @param activity The activity structure as JSON <code>String</code>.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_activity">Janrain Engage API Documentation: activity</a>
     * @since 1.0
     */
    public void activity(String identifier, String activity) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Post an activity update to the user's activity stream.
     * 
     * @param identifier The identifier returned from the {@link #authInfo(String)} API call.
     * @param activity The activity structure.
     * @param location This is a string containing location data associated with the content being published. The string is latitude, followed by longitude, for example "37.4220 -122.0843". Valid values for latitude are -90.0 to +90.0, with North being positive. Valid values for longitude are -180.0 to +180.0 with East being positive.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_activity">Janrain Engage API Documentation: activity</a>
     * @since 1.0
     */
    public void activity(String identifier, Activity activity, String location) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Post an activity update to the user's activity stream.
     * 
     * @param identifier The identifier returned from the {@link #authInfo(String)} API call.
     * @param activity The activity structure as JSON <code>String</code>.
     * @param location This is a string containing location data associated with the content being published. The string is latitude, followed by longitude, for example "37.4220 -122.0843". Valid values for latitude are -90.0 to +90.0, with North being positive. Valid values for longitude are -180.0 to +180.0 with East being positive.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_activity">Janrain Engage API Documentation: activity</a>
     * @since 1.0
     */
    public void activity(String identifier, String activity, String location) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Returns true if the provider name supports the {@link #activity(String, Activity)} API call.
     * 
     * @param providerName The provider name
     * @return True if supported.
     * @since 1.1
     */
    public boolean supportsActivity(String providerName);
    
    /**
     * Get statistics for your application.
     * 
     * @param start The start date.
     * @param end The end date.
     * @return A URL to access the analytics file.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_analytics">Janrain Engage API Documentation: analytics</a>
     * @since 1.0
     */
    public AnalyticsResponse analytics(Date start, Date end) throws EngageFailureException, ErrorResponeException;
    
    /**
     * Set the providers that will be displayed in the <a href="http://rpxnow.com/docs#sign-in_interface">Sign-in</a> Interface.
     * 
     * @param providers A list of provider names.
     * @throws EngageFailureException If any unknown error occurs while communicating with the Janrain Engage API.
     * @throws ErrorResponeException If the Janrain Engage API returns an error response.
     * @see <a href="http://rpxnow.com/docs#api_set_auth_providers">Janrain Engage API Documentation: set_auth_providers</a>
     * @since 1.0
     */
    public void setAuthProviders(List<String> providers) throws EngageFailureException, ErrorResponeException;
}
