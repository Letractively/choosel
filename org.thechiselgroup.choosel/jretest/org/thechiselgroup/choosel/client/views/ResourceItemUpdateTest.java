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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.client.resources.DefaultResourceSet;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.test.TestResourceSetFactory;
import org.thechiselgroup.choosel.client.ui.popup.PopupManager;

public class ResourceItemUpdateTest {

    private static final String RESOURCE_ITEM_CATEGORY = "resourceItemCategory";

    @Mock
    private HoverModel hoverModel;

    @Mock
    private ResourceItemValueResolver layer;

    @Mock
    private PopupManager popupManager;

    private ResourceSet resources;

    private ResourceItem underTest;

    private int updateCalled;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        resources = new DefaultResourceSet();
        updateCalled = 0;
        underTest = new ResourceItem(RESOURCE_ITEM_CATEGORY, resources,
                hoverModel, popupManager, layer) {
            @Override
            protected void updateContent() {
                updateCalled++;
            }
        };
    }

    @Test
    public void updateResourceItemOnCreation() {
        assertEquals(1, updateCalled);
    }

    @Test
    public void updateResourceItemWhenResourcesAreAddedToUnderlyingResourceSet() {
        updateCalled = 0;
        resources.add(TestResourceSetFactory.createResource(1));

        assertEquals(1, updateCalled);
    }

    @Test
    public void updateResourceItemWhenResourcesAreRemovedFromUnderlyingResourceSet() {
        resources.add(TestResourceSetFactory.createResource(1));
        updateCalled = 0;
        resources.remove(TestResourceSetFactory.createResource(1));

        assertEquals(1, updateCalled);
    }

}