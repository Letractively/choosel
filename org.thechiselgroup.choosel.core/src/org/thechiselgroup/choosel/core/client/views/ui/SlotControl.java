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
package org.thechiselgroup.choosel.core.client.views.ui;

import org.thechiselgroup.choosel.core.client.ui.WidgetAdaptable;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.views.model.Slot;
import org.thechiselgroup.choosel.core.client.views.model.VisualItem;
import org.thechiselgroup.choosel.core.client.views.resolvers.ViewItemValueResolverUIController;

public abstract class SlotControl implements WidgetAdaptable {

    private Slot slot;

    public SlotControl(Slot slot) {
        this.slot = slot;
    }

    public abstract String getCurrentResolverID();

    public Slot getSlot() {
        return slot;
    }

    public abstract void updateOptions(LightweightCollection<VisualItem> viewItems);

    public abstract void setNewUIModel(
            ViewItemValueResolverUIController resolverUI);

}