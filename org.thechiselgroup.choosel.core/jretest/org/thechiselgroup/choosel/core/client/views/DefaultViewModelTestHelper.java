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
package org.thechiselgroup.choosel.core.client.views;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.thechiselgroup.choosel.core.client.test.ResourcesTestHelper.emptyLightweightCollection;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.thechiselgroup.choosel.core.client.resources.DefaultResourceSet;
import org.thechiselgroup.choosel.core.client.resources.DefaultResourceSetFactory;
import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.resources.ResourceByUriTypeCategorizer;
import org.thechiselgroup.choosel.core.client.resources.ResourceCategorizerToMultiCategorizerAdapter;
import org.thechiselgroup.choosel.core.client.resources.ResourceGrouping;
import org.thechiselgroup.choosel.core.client.resources.ResourceSet;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetChangedEventHandler;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.util.collections.NullIterator;
import org.thechiselgroup.choosel.core.client.views.slots.DefaultSlotMappingInitializer;
import org.thechiselgroup.choosel.core.client.views.slots.Slot;
import org.thechiselgroup.choosel.core.client.views.slots.SlotMappingConfiguration;
import org.thechiselgroup.choosel.core.client.views.slots.SlotMappingInitializer;

import com.google.gwt.event.shared.HandlerRegistration;

public final class DefaultViewModelTestHelper {

    public static LightweightCollection<ViewItem> captureAddedViewItems(
            ViewContentDisplay contentDisplay) {

        return captureAddedViewItems(contentDisplay, 1).get(0);
    }

    public static List<LightweightCollection<ViewItem>> captureAddedViewItems(
            ViewContentDisplay contentDisplay, int wantedNumberOfInvocation) {

        ArgumentCaptor<LightweightCollection> captor = ArgumentCaptor
                .forClass(LightweightCollection.class);
        verify(contentDisplay, times(wantedNumberOfInvocation)).update(
                captor.capture(), emptyLightweightCollection(ViewItem.class),
                emptyLightweightCollection(ViewItem.class),
                emptyLightweightCollection(Slot.class));

        return cast(captor.getAllValues());
    }

    public static List<ViewItem> captureAddedViewItemsAsList(
            ViewContentDisplay contentDisplay) {

        return captureAddedViewItems(contentDisplay).toList();
    }

    public static LightweightCollection<ViewItem> captureUpdatedViewItems(
            ViewContentDisplay contentDisplay) {

        ArgumentCaptor<LightweightCollection> captor = ArgumentCaptor
                .forClass(LightweightCollection.class);
        verify(contentDisplay, times(1)).update(
                emptyLightweightCollection(ViewItem.class), captor.capture(),
                emptyLightweightCollection(ViewItem.class),
                emptyLightweightCollection(Slot.class));

        return captor.getValue();
    }

    /**
     * convert to LightWeightCollection<ViewItem>
     */
    private static List<LightweightCollection<ViewItem>> cast(
            List<LightweightCollection> allValues) {

        List<LightweightCollection<ViewItem>> result = new ArrayList<LightweightCollection<ViewItem>>();
        for (LightweightCollection<ViewItem> lightweightCollection : allValues) {
            result.add(lightweightCollection);
        }
        return result;
    }

    public static DefaultViewModel createTestViewModel(
            ResourceSet containedResources, ResourceSet highlightedResources,
            ResourceSet selectedResources, Slot... slots) {

        ViewContentDisplay contentDisplay = mock(ViewContentDisplay.class);

        return createTestViewModel(containedResources, highlightedResources,
                selectedResources, contentDisplay, slots);
    }

    public static DefaultViewModel createTestViewModel(
            ResourceSet containedResources, ResourceSet highlightedResources,
            ResourceSet selectedResources, ViewContentDisplay contentDisplay,
            Slot... slots) {

        DefaultResourceSetFactory resourceSetFactory = new DefaultResourceSetFactory();
        SlotMappingConfiguration resourceSetToValueResolver = spy(new SlotMappingConfiguration());

        // TODO change once relevant tests are migrated
        SlotMappingInitializer slotMappingInitializer = spy(new DefaultSlotMappingInitializer());

        when(contentDisplay.getSlots()).thenReturn(slots);
        when(contentDisplay.isReady()).thenReturn(true);

        ResourceGrouping resourceGrouping = new ResourceGrouping(
                new ResourceCategorizerToMultiCategorizerAdapter(
                        new ResourceByUriTypeCategorizer()),
                new DefaultResourceSetFactory());

        resourceGrouping.setResourceSet(containedResources);

        DefaultViewModel underTest = spy(new DefaultViewModel(contentDisplay,
                resourceSetToValueResolver, selectedResources,
                highlightedResources, slotMappingInitializer,
                mock(ViewItemBehavior.class), resourceGrouping));

        // deactivate slot initialization
        underTest.setConfigured(true);

        return underTest;
    }

    public static DefaultViewModel createTestViewModel(Slot... slots) {
        ResourceSet containedResources = new DefaultResourceSet();
        ResourceSet highlightedResources = new DefaultResourceSet();
        ResourceSet selectedResources = new DefaultResourceSet();

        return createTestViewModel(containedResources, highlightedResources,
                selectedResources, slots);
    }

    public static void stubHandlerRegistration(ResourceSet mockedResources,
            HandlerRegistration handlerRegistrationToReturn) {

        when(mockedResources.iterator()).thenReturn(
                NullIterator.<Resource> nullIterator());

        when(
                mockedResources
                        .addEventHandler(any(ResourceSetChangedEventHandler.class)))
                .thenReturn(handlerRegistrationToReturn);
    }

    private DefaultViewModelTestHelper() {

    }

}
