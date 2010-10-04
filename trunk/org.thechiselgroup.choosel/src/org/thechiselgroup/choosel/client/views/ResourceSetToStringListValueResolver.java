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

import java.util.ArrayList;
import java.util.List;

import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.resources.ResourceCategorizer;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.util.CollectionUtils;

public class ResourceSetToStringListValueResolver extends
        AbstractResourceSetToValueResolver {

    public ResourceSetToStringListValueResolver(String slotID,
            DefaultResourceToValueResolverFactory factory,
            ResourceCategorizer categorizer) {
        super(slotID, factory, categorizer);
    }

    @Override
    public Object resolve(ResourceSet resources, String category) {
        if (resources.isEmpty()) {
            return "";
        }

        if (resources.size() == 1) {
            return "" + resolve(resources.getFirstResource());
        }

        String result = "{ ";

        List<String> values = new ArrayList<String>();
        for (Resource resource : resources) {
            values.add((String) resolve(resource));
        }
        result += CollectionUtils.deliminateIterableStringCollection(values,
                ", ");
        result += " }";

        return result;
    }
}