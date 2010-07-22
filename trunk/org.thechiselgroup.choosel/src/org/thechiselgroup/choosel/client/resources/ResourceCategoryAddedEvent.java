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
package org.thechiselgroup.choosel.client.resources;

import com.google.gwt.event.shared.GwtEvent;

public class ResourceCategoryAddedEvent extends
        GwtEvent<ResourceCategoryAddedEventHandler> {

    public static final GwtEvent.Type<ResourceCategoryAddedEventHandler> TYPE = new GwtEvent.Type<ResourceCategoryAddedEventHandler>();

    private String category;

    private final ResourceSet resourceSet;

    public ResourceCategoryAddedEvent(String category, ResourceSet resourceSet) {
        assert category != null;
        assert resourceSet != null;

        this.category = category;
        this.resourceSet = resourceSet;
    }

    @Override
    protected void dispatch(ResourceCategoryAddedEventHandler handler) {
        handler.onResourceCategoryAdded(this);
    }

    @Override
    public GwtEvent.Type<ResourceCategoryAddedEventHandler> getAssociatedType() {
        return TYPE;
    }

    public String getCategory() {
        return category;
    }

    public ResourceSet getResourceSet() {
        return resourceSet;
    }

}