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

/**
 * Bi-directionally asserted relationship type that was established between the 
 * user and the <code>Contact</code> by the Service Provider.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Contact
 */
@SuppressWarnings("serial")
public class Relationship implements Serializable {

    // Friendship
    public static final String CONTACT = "contact";
    public static final String ACQUAINTANCE = "acquaintance";
    public static final String FRIEND = "friend";
    
    // Physical
    public static final String MET = "met";
    
    // Professional
    public static final String CO_WORKER = "co-worker";
    public static final String COLLEAGUE = "colleague";
    
    // Geographical
    public static final String CO_RESIDENT = "co-resident";
    public static final String NEIGHBOR = "neighbor";
    
    // Family
    public static final String CHILD = "child";
    public static final String PARENT = "parent";
    public static final String SIBLING = "sibling";
    public static final String SPOUSE = "spouse";
    public static final String KIN = "kin";
    
    // Romantic
    public static final String MUSE = "muse";
    public static final String CRUSH = "crush";
    public static final String DATE = "date";
    public static final String SWEETHEART = "sweetheart";
    
    // Identity
    public static final String ME = "me";
    
    private String value = null;
    
    protected Relationship() {
    }
    
    /**
     * Returns the value of this instance.
     */
    public String getValue() {
        return value;
    }
    
    void setValue(String value) {
        this.value = value;
    }
    
    /**
     * Returns true if the relation type of this instance is contact. 
     */
    public boolean isContact() {
        return CONTACT.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is acquaintance. 
     */
    public boolean isAcquaintance() {
        return ACQUAINTANCE.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is friend. 
     */
    public boolean isFriend() {
        return FRIEND.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is met. 
     */
    public boolean isMet() {
        return MET.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is co-worker. 
     */
    public boolean isCoWorker() {
        return CO_WORKER.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is colleague. 
     */
    public boolean isColleague() {
        return COLLEAGUE.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is co-resident. 
     */
    public boolean isCoResident() {
        return CO_RESIDENT.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is neighbor. 
     */
    public boolean isNeighbor() {
        return NEIGHBOR.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is child. 
     */
    public boolean isChild() {
        return CHILD.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is parent. 
     */
    public boolean isParent() {
        return PARENT.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is sibling. 
     */
    public boolean isSibling() {
        return SIBLING.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is spouse. 
     */
    public boolean isSpouse() {
        return SPOUSE.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is kin. 
     */
    public boolean isKin() {
        return KIN.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is muse. 
     */
    public boolean isMuse() {
        return MUSE.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is crush. 
     */
    public boolean isCrush() {
        return CRUSH.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is date. 
     */
    public boolean isDate() {
        return DATE.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is sweetheart. 
     */
    public boolean isSweetheart() {
        return SWEETHEART.equalsIgnoreCase(value);
    }
    
    /**
     * Returns true if the relation type of this instance is me. 
     */
    public boolean isMe() {
        return ME.equalsIgnoreCase(value);
    }
    
    public String toString() {
        return value;
    }
}
