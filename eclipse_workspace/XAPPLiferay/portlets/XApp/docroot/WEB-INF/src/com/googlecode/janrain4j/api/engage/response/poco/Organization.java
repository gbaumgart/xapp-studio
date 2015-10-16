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

import com.googlecode.janrain4j.json.JSONObject;

/**
 * Current or past organizational affiliation of the <code>Contact</code>.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Contact
 */
@SuppressWarnings("serial")
public class Organization implements Serializable {

    public static final String TYPE_JOB = "job";
    public static final String TYPE_SCHOOL = "school";
    
    private String name = null;
    private String department = null;
    private String title = null;
    private String type = null;
    private String startDate = null;
    private String endDate = null;
    private String location = null;
    private String description = null;
    private boolean primary = false;
    
    protected Organization() {
    }
    
    public static Organization fromJSON(JSONObject json) {
        Organization organization = null;
        if (json != null) {
            organization = new Organization();
            organization.setName(json.optString("name"));
            organization.setDepartment(json.optString("department"));
            organization.setTitle(json.optString("title"));
            organization.setType(json.optString("type"));
            organization.setStartDate(json.optString("startDate"));
            organization.setEndDate(json.optString("endDate"));
            organization.setLocation(json.optString("location"));
            organization.setDescription(json.optString("description"));
            organization.setPrimary(json.optBoolean("primary", false));
        }
        return organization;
    }
    
    /**
     * Returns the name of the organization (e.g. company, school, or other organization).
     * 
     * @return The name or <code>null</code> if not found in response.
     */
    public String getName() {
        return name;
    }
    
    void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the department within this organization.
     * 
     * @return The department or <code>null</code> if not found in response.
     */
    public String getDepartment() {
        return department;
    }
    
    void setDepartment(String department) {
        this.department = department;
    }
    
    /**
     * Returns the job title or role within this organization.
     * 
     * @return The job title or <code>null</code> if not found in response.
     */
    public String getTitle() {
        return title;
    }
    
    void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Returns the type of this instance.
     * 
     * @return The type or <code>null</code> if not found in response.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns true if the type of this instance is job. 
     */
    public boolean isJob() {
        return TYPE_JOB.equalsIgnoreCase(type);
    }
    
    /**
     * Returns true if the type of this instance is school. 
     */
    public boolean isSchool() {
        return TYPE_SCHOOL.equalsIgnoreCase(type);
    }
    
    void setType(String type) {
        this.type = type;
    }
    
    /**
     * Returns the date the <code>Contact</code> joined this organization.
     * 
     * @return The start date or <code>null</code> if not found in response.
     */
    public String getStartDate() {
        return startDate;
    }
    
    void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    /**
     * Returns the date the <code>Contact</code> left this organization or the role specified by title within this organization.
     * 
     * @return The end date or <code>null</code> if not found in response.
     */
    public String getEndDate() {
        return endDate;
    }
    
    void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    /**
     * Returns the physical location of this organization. This may be a complete address, or an abbreviated location like "San Francisco".
     * 
     * @return The physical location or <code>null</code> if not found in response.
     */
    public String getLocation() {
        return location;
    }
    
    void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * Returns the textual description of the role the <code>Contact</code> played in this organization
     * 
     * @return The description or <code>null</code> if not found in response.
     */
    public String getDescription() {
        return description;
    }
    
    void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Returns true if this instance is the primary or prefered value for this field.
     */
    public boolean isPrimary() {
        return primary;
    }
    
    void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
