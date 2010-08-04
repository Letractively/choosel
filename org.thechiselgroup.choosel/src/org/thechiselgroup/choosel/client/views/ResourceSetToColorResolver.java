/*******************************************************************************
 * Copyright 2009, 2010 Lars Grammel 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.  
 *******************************************************************************/
package org.thechiselgroup.choosel.client.views;

import java.util.HashMap;
import java.util.Map;

import org.thechiselgroup.choosel.client.resolver.ResourceSetToValueResolver;
import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.resources.ResourceCategorizer;
import org.thechiselgroup.choosel.client.resources.ResourceSet;

public class ResourceSetToColorResolver implements ResourceSetToValueResolver {

    private static final String[] COLORS = new String[] { "#6495ed", "#b22222",
            "#A9C0B1" };

    private ResourceCategorizer categorizer;

    private Map<String, String> resourceTypeToColor = new HashMap<String, String>();

    public ResourceSetToColorResolver(ResourceCategorizer categorizer) {
        this.categorizer = categorizer;
    }

    @Override
    public Object resolve(ResourceSet resources, String category) {
        // TODO what if resource.isEmpty?
        if (resources.isEmpty()) {
            return COLORS[0]; // TODO we need something better
        }

        Resource resource = resources.getFirstResource();
        String resourceType = categorizer.getCategory(resource);

        if (!resourceTypeToColor.containsKey(resourceType)) {
            resourceTypeToColor.put(resourceType,
                    COLORS[resourceTypeToColor.size()]);
        }

        return resourceTypeToColor.get(resourceType);
    }
}