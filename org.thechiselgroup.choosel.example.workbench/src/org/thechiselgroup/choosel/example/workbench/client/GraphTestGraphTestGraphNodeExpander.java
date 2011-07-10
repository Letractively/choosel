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
package org.thechiselgroup.choosel.example.workbench.client;

import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.visualization_component.graph.client.AbstractGraphNodeExpander;
import org.thechiselgroup.choosel.visualization_component.graph.client.GraphNodeExpander;
import org.thechiselgroup.choosel.visualization_component.graph.client.GraphNodeExpansionCallback;

public class GraphTestGraphTestGraphNodeExpander extends
        AbstractGraphNodeExpander implements GraphNodeExpander {

    @Override
    public void expand(VisualItem resourceItem,
            GraphNodeExpansionCallback expansionCallback) {

        // TODO better resource item handling
        Resource resource = resourceItem.getResources().getFirstElement();

        addResources(expansionCallback, calculateUrisToAdd(resource, "parent"),
                resource);
    }
}
