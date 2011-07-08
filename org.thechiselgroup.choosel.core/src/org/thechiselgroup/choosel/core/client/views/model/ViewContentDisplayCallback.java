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

import org.thechiselgroup.choosel.core.client.views.resolvers.ViewItemValueResolver;

// TODO inline interface into ViewContentDisplay
// TODO we need to expose a filtered, more limited version of the ViewItemContainer
// --> create new superinterface?
public interface ViewContentDisplayCallback extends ViewItemContainer {

    // TODO is this part of another interface?
    ViewItemValueResolver getResolver(Slot slot);

    String getSlotResolverDescription(Slot slot);

}