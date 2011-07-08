/*******************************************************************************
 * Copyright (C) 2011 Lars Grammel 
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
package org.thechiselgroup.choosel.core.client.views.resolvers;

import java.util.List;

import org.thechiselgroup.choosel.core.client.resources.DataType;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.views.model.Slot;
import org.thechiselgroup.choosel.core.client.views.model.ViewItem;

public class FirstResourcePropertyResolverFactory extends
        PropertyDependantViewItemValueResolverFactory {

    private final DataType dataType;

    private final String resolverID;

    public FirstResourcePropertyResolverFactory(DataType dataType,
            String resolverID) {
        this.dataType = dataType;
        this.resolverID = resolverID;
    }

    @Override
    public boolean canCreateApplicableResolver(Slot slot,
            LightweightCollection<ViewItem> viewItems) {

        if (!slot.getDataType().equals(dataType)) {
            return false;
        }

        // for now, all resolvers should be applicable when there are no view
        // items
        if (viewItems.isEmpty()) {
            return true;
        }

        return !getSharedProperties(viewItems).isEmpty();
    }

    @Override
    public ManagedViewItemValueResolver create(
            LightweightCollection<ViewItem> viewItems) {
        List<String> properties = getSharedProperties(viewItems);
        assert !properties.isEmpty();

        return create(properties.get(0));

    }

    @Override
    public ManagedViewItemValueResolver create(String property) {
        return new PropertyDependantManagedViewItemValueResolverDecorator(
                resolverID, new FirstResourcePropertyResolver(property,
                        dataType));
    }

    @Override
    public String getId() {
        return resolverID;
    }

    // TODO Perhaps a better value for this
    @Override
    public String getLabel() {
        return "Property Selector";
    }

}