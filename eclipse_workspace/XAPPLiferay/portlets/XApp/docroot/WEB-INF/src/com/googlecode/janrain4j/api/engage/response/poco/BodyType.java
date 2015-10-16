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
 * Body characteristics of the <code>Contact</code>.
 * 
 * @author Marcel Overdijk
 * @since 1.1
 * @see Contact
 */
@SuppressWarnings("serial")
public class BodyType implements Serializable {

    private String build = null;
    private String eyeColor = null;
    private String hairColor = null;
    private Double height = null;
    private Double weight = null;
    
    protected BodyType() {
    }
    
    public static BodyType fromJSON(JSONObject json) {
        BodyType bodyType = null;
        if (json != null) {
            bodyType = new BodyType();
            bodyType.setBuild(json.optString("build"));
            bodyType.setEyeColor(json.optString("eyeColor"));
            bodyType.setHairColor(json.optString("hairColor"));
            bodyType.setHeight(json.optDouble("height"));
            bodyType.setWeight(json.optDouble("weight"));
        }
        return bodyType;
    }
    
    /**
     * Returns the build.
     * 
     * @return The build or <code>null</code> if not found in response.
     */
    public String getBuild() {
        return build;
    }
    
    void setBuild(String build) {
        this.build = build;
    }
    
    /**
     * Returns the eye color.
     * 
     * @return The eye color or <code>null</code> if not found in response.
     */
    public String getEyeColor() {
        return eyeColor;
    }
    
    void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }
    
    /**
     * Returns the hair color.
     * 
     * @return The hair color or <code>null</code> if not found in response.
     */
    public String getHairColor() {
        return hairColor;
    }
    
    void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }
    
    /**
     * Returns the height.
     * 
     * @return The height or <code>null</code> if not found in response.
     */
    public Double getHeight() {
        return height;
    }
    
    void setHeight(Double height) {
        this.height = height;
    }
    
    /**
     * Returns the weight.
     * 
     * @return The weight or <code>null</code> if not found in response.
     */
    public Double getWeight() {
        return weight;
    }
    
    void setWeight(Double weight) {
        this.weight = weight;
    }
}
