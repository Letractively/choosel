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
package org.thechiselgroup.choosel.core.client.views.slots;

import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;

public class SlotMappingResolutionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // TODO slot, viewitem, cause
    public SlotMappingResolutionException(Slot slot, String groupID,
            LightweightCollection<Resource> resources, Exception cause) {

        super("slot resolution failed for view item " + groupID + " "
                + resources + " and slot" + slot, cause);
    }

}