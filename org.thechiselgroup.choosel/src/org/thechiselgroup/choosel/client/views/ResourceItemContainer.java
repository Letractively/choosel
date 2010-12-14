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

import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.util.collections.LightweightCollection;

/**
 * Provides read access to <code>ResourceItem</code>s.
 * 
 * @author Lars Grammel
 * 
 * @see ResourceItem
 */
public interface ResourceItemContainer {

    /**
     * Tests if the resource item with the given id exists in this container.
     */
    boolean containsResourceItem(String groupId);

    /**
     * Returns the resource item with the given group ID.
     */
    ResourceItem getResourceItemByGroupID(String groupId);

    /**
     * Returns all resource items in this container.
     */
    LightweightCollection<ResourceItem> getResourceItems();

    /**
     * Returns the resource items that contain at least one of the given
     * resources.
     */
    LightweightCollection<ResourceItem> getResourceItems(
            Iterable<Resource> resources);

}