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
package org.thechiselgroup.choosel.client.views.graph;

import org.thechiselgroup.choosel.client.domain.ncbo.NcboUriHelper;
import org.thechiselgroup.choosel.client.error_handling.ErrorHandler;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class BioMixerNodeMenuEntryRegistry extends NodeMenuEntryRegistry {

    @Inject
    public BioMixerNodeMenuEntryRegistry(
	    @Named("mapping") NeighbourhoodServiceAsync mappingNeighbourhoodService,
	    @Named("concept") NeighbourhoodServiceAsync conceptNeighbourhoodService,
	    ErrorHandler errorHandler) {

	putNodeMenuEntry(NcboUriHelper.NCBO_CONCEPT, "Concepts",
		new ConceptConceptNeighbourhoodExpander(
			conceptNeighbourhoodService, errorHandler));
	putNodeMenuEntry(NcboUriHelper.NCBO_CONCEPT, "Mappings",
		new ConceptMappingNeighbourhoodExpander(
			mappingNeighbourhoodService, errorHandler));
	putNodeMenuEntry(NcboUriHelper.NCBO_MAPPING, "Concepts",
		new MappingExpander());
    }

}
