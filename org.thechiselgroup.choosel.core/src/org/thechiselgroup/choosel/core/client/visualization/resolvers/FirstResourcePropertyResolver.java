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
package org.thechiselgroup.choosel.core.client.visualization.resolvers;

import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.resources.ResourceSet;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetUtils;
import org.thechiselgroup.choosel.core.client.util.DataType;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemValueResolverContext;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem.Subset;

public class FirstResourcePropertyResolver extends SubsetViewItemValueResolver
        implements PropertyDependantViewItemValueResolver {

    protected final String property;

    protected final DataType dataType;

    public FirstResourcePropertyResolver(String property, DataType dataType) {
        this(property, dataType, Subset.ALL);
    }

    public FirstResourcePropertyResolver(String property, DataType dataType,
            Subset subset) {

        super(subset);

        assert property != null;
        assert dataType != null;

        this.dataType = dataType;
        this.property = property;
    }

    // TODO test
    @Override
    public boolean canResolve(VisualItem viewItem,
            VisualItemValueResolverContext context) {

        assert viewItem != null;
        assert context != null;

        ResourceSet resources = viewItem.getResources();

        if (resources.isEmpty()) {
            return false;
        }

        Resource resource = resources.getFirstElement();

        // XXX need to check for property type
        return resource.containsProperty(property)
                && resource.getValue(property) != null;
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public Object resolve(VisualItem viewItem,
            VisualItemValueResolverContext context, Subset subset) {

        assert canResolve(viewItem, context);

        // TODO what if viewItem could be resolved, but not for subset?
        ResourceSet resources = viewItem.getResources(subset);
        return resources.getFirstElement().getValue(property);
    }

    @Override
    public String toString() {
        return property + " (first item)";
    }

}