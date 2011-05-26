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
package org.thechiselgroup.choosel.core.client.views.model;

import org.thechiselgroup.choosel.core.client.resources.ResourceSet;

/**
 * this class defines default mappings between slots that are in the view and
 * Resolvers that are used to resolve that slot.
 * 
 * It takes in a slotMappingConfiguration and adds the mappings to that
 * configuration
 */
public interface SlotMappingInitializer {

    void initializeMappings(ResourceSet resources,
            ViewContentDisplay contentDisplay,
            SlotMappingConfiguration slotMappingConfiguration);

}