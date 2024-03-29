/* This file is part of the cvc webservices.
 *  Copyright (c) 2020 by the Board of Trustees of the University of Iowa
 *  Licensed under the Apache License, Version 2.0 (the "License"); you
 *  may not use this file except in compliance with the License.  You
 *  may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package edu.uiowa.cvc;

import java.util.List;

public class Argument
{
    public String category;
    public String type;
    public boolean alternate = false;
    public String prefix;
    public List<String> allowedValues;
    public String description;
    public String defaultValue;
    public float min;
    public float max;

    @Override
    public String toString()
    {
        return "Argument{" +
                "category='" + category + '\'' +
                "type='" + type + '\'' +
                ", alternate='" + alternate + '\'' +
                ", prefix='" + prefix + '\'' +
                ", allowedValues=" + allowedValues +
                ", description='" + description + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", min=" + min +
                ", max=" + max +
                '}' + "\n";
    }
}
