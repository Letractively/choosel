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
package org.thechiselgroup.choosel.core.client.views.model;

import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;

// TODO pull up slots into this?
public interface ViewItemRenderer {

    /**
     * <p>
     * Updates the {@link ViewContentDisplay}. There is no overlap between the
     * three different {@link ViewItem} sets (added, updated, and removed
     * {@link ViewItem}s). We use a single method to enable the different
     * {@link ViewContentDisplay}s to do a single refresh of the view instead of
     * multiple operations.
     * </p>
     * <p>
     * The {@link ViewItem}s can be referenced during a session for reference
     * testing. When a {@link ViewItem} is created, it is passed in as part of
     * the added {@link ViewItem}s, when it changes, it is part of the updated
     * {@link ViewItem}s, and when it is removed, it is part of the removed
     * {@link ViewItem}s.
     * </p>
     * <p>
     * In addition to changing {@link ViewItem}s, the slot mapping of a
     * visualization could have changed in the same instance. This is reflected
     * by the changedSlots parameter.
     * </p>
     * <p>
     * <b>IMPORTANT:</b> The caller should guarantee that all {@link Slot}s can
     * always be resolved on the {@link ViewItem}s in the
     * {@link ViewItemRenderer}. If a {@link ViewItem} cannot resolve all slots
     * any more, it should be removed.
     * </p>
     * 
     * @param addedViewItems
     *            {@link ViewItem}s that have been added to the view. Is never
     *            <code>null</code>, but can be an empty set.
     * @param updatedViewItems
     *            {@link ViewItem}s which have changed (status, data, etc.) such
     *            that their representation needs to be updated. Is never
     *            <code>null</code>, but can be an empty set.
     * @param removedViewItems
     *            {@link ViewItem}s that have been removed from the view. Is
     *            never <code>null</code>, but can be an empty set.
     * @param updatedSlots
     *            {@link Slot}s for which the mappings have changed. Is never
     *            <code>null</code>, but can be an empty set.
     */
    void update(LightweightCollection<ViewItem> addedViewItems,
            LightweightCollection<ViewItem> updatedViewItems,
            LightweightCollection<ViewItem> removedViewItems,
            LightweightCollection<Slot> updatedSlots);

}