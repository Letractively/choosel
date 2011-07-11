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
package org.thechiselgroup.choosel.core.client.visualization.resolvers;

import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollections;
import org.thechiselgroup.choosel.core.client.visualization.model.Slot;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem.Subset;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemValueResolver;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemValueResolverContext;

public class SubsetDelegatingValueResolver implements VisualItemValueResolver {

    private Slot slot;

    private LightweightCollection<Slot> slots;

    private Subset subset;

    public SubsetDelegatingValueResolver(Slot slot, Subset subset) {
        this.slot = slot;
        this.subset = subset;

        this.slots = LightweightCollections.toCollection(slot);
    }

    @Override
    public boolean canResolve(VisualItem viewItem,
            VisualItemValueResolverContext context) {

        if (!context.isConfigured(slot)) {
            return false;
        }

        assert context.getResolver(slot) != null;

        return context.getResolver(slot).canResolve(viewItem, context);
    }

    @Override
    public LightweightCollection<Slot> getTargetSlots() {
        return slots;
    }

    @Override
    public Object resolve(VisualItem viewItem,
            VisualItemValueResolverContext context) {

        VisualItemValueResolver delegate = context.getResolver(slot);

        if (delegate instanceof SubsetViewItemValueResolver) {
            return ((SubsetViewItemValueResolver) delegate).resolve(viewItem,
                    context, subset);
        }

        return delegate.resolve(viewItem, context);
    }
}