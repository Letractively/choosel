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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.thechiselgroup.choosel.core.client.test.HamcrestResourceMatchers.containsExactly;
import static org.thechiselgroup.choosel.core.client.views.model.ViewItemValueResolverTestUtils.createSlots;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.core.client.resources.DataType;
import org.thechiselgroup.choosel.core.client.resources.ResourceGrouping;
import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightList;
import org.thechiselgroup.choosel.core.client.views.resolvers.ManagedViewItemValueResolver;
import org.thechiselgroup.choosel.core.client.views.resolvers.SlotMappingUIModel;
import org.thechiselgroup.choosel.core.client.views.resolvers.ViewItemValueResolver;
import org.thechiselgroup.choosel.core.client.views.resolvers.ViewItemValueResolverFactory;
import org.thechiselgroup.choosel.core.client.views.resolvers.ViewItemValueResolverFactoryProvider;

/**
 * 
 * @author Lars Grammel
 * @author Patrick Gorman
 */

public class SlotMappingConfigurationUIModelTest {

    private static final String RESOLVER_ID_1 = "resolver-id-1";

    private static final String RESOLVER_ID_2 = "resolver-id-2";

    public static Matcher<LightweightCollection<SlotMappingUIModel>> uiModelsContainSlots(
            final Slot... slots) {

        return new TypeSafeMatcher<LightweightCollection<SlotMappingUIModel>>() {
            @Override
            public void describeTo(Description description) {
                for (Slot slot : slots) {
                    description.appendValue(slot);
                }
            }

            @Override
            public boolean matchesSafely(
                    LightweightCollection<SlotMappingUIModel> uiModels) {
                if (uiModels.size() != slots.length) {
                    return false;
                }

                for (Slot slot : slots) {
                    boolean found = false;
                    for (SlotMappingUIModel uiModel : uiModels) {
                        if (uiModel.getSlot().equals(slot)) {
                            found = true;
                        }
                    }

                    if (!found) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private SlotMappingConfigurationUIModel underTest;

    @Mock
    private ViewItemValueResolverFactoryProvider resolverProvider;

    @Mock
    private SlotMappingInitializer slotMappingInitializer;

    @Mock
    private ViewModel viewModel;

    private Slot[] slots;

    @Mock
    private ViewItemValueResolverFactory factory1;

    @Mock
    private ManagedViewItemValueResolver resolver1;

    @Mock
    private ManagedViewItemValueResolver resolver2;

    private DefaultViewItemResolutionErrorModel errorModel;

    @Mock
    private ViewItemValueResolverFactory factory2;

    private SlotMappingChangedEvent captureSlotMappingChangedEvent(
            SlotMappingChangedHandler handler) {
        ArgumentCaptor<SlotMappingChangedEvent> captor = ArgumentCaptor
                .forClass(SlotMappingChangedEvent.class);
        verify(handler, times(1)).onSlotMappingChanged(captor.capture());
        return captor.getValue();
    }

    private SlotMappingChangedHandler captureSlotMappingChangedHandler() {
        ArgumentCaptor<SlotMappingChangedHandler> captor = ArgumentCaptor
                .forClass(SlotMappingChangedHandler.class);
        verify(viewModel, times(1)).addHandler(captor.capture());
        return captor.getValue();
    }

    private ViewItemContainerChangeEventHandler captureViewItemContainerChangeEventHandler() {
        ArgumentCaptor<ViewItemContainerChangeEventHandler> captor = ArgumentCaptor
                .forClass(ViewItemContainerChangeEventHandler.class);
        verify(viewModel, times(1)).addHandler(captor.capture());
        return captor.getValue();
    }

    @Test
    public void configurationUIModelContainsUIModelForEachSlotInViewModel() {
        setUpSlots(DataType.TEXT, DataType.NUMBER);

        underTest = new SlotMappingConfigurationUIModel(resolverProvider,
                slotMappingInitializer, viewModel, errorModel);
        LightweightCollection<SlotMappingUIModel> uiModels = underTest
                .getSlotMappingUIModels();

        assertThat(uiModels, uiModelsContainSlots(slots));
    }

    @Test
    public void configurationUIModelContainsUIModelForOneSlotInViewModel() {
        setUpSlots(DataType.TEXT);

        underTest = new SlotMappingConfigurationUIModel(resolverProvider,
                slotMappingInitializer, viewModel, errorModel);
        LightweightList<SlotMappingUIModel> uiModels = underTest
                .getSlotMappingUIModels();

        assertThat(uiModels, uiModelsContainSlots(slots));
    }

    @Test
    public void errorInErrorModelResolverInViewModelNotApplicable() {
        setUpSlots(DataType.TEXT);
        when(viewModel.getResolver(slots[0])).thenReturn(resolver1);

        ViewItem viewItem = mock(ViewItem.class);
        when(viewItem.getViewItemID()).thenReturn("a");
        errorModel.reportError(slots[0], viewItem);

        underTest = new SlotMappingConfigurationUIModel(resolverProvider,
                slotMappingInitializer, viewModel, errorModel);

        assertThat(underTest.getSlotsWithInvalidResolvers(),
                containsExactly(slots[0]));
    }

    @Test
    public void getResolverFromViewModelContext() {
        setUpSlots(DataType.TEXT);
        when(viewModel.getResolver(slots[0])).thenReturn(resolver1);

        underTest = new SlotMappingConfigurationUIModel(resolverProvider,
                slotMappingInitializer, viewModel, errorModel);

        assertEquals(resolver1, underTest.getCurrentResolver(slots[0]));
    }

    @SuppressWarnings("unchecked")
    public void mockFactory(ViewItemValueResolverFactory factory, String id,
            ManagedViewItemValueResolver resolver) {
        when(factory.getId()).thenReturn(id);
        when(
                factory.canCreateApplicableResolver(any(Slot.class),
                        any(LightweightList.class))).thenReturn(true);

        when(factory.create(any(LightweightList.class))).thenReturn(resolver);
    }

    private void mockResolversAndFactories() {
        mockFactory(factory1, RESOLVER_ID_1, resolver1);
        mockFactory(factory2, RESOLVER_ID_2, resolver2);
        when(resolver1.getResolverId()).thenReturn(RESOLVER_ID_1);
        when(
                resolver1.canResolve(any(ViewItem.class),
                        any(ViewItemValueResolverContext.class))).thenReturn(
                true);
        when(resolver2.getResolverId()).thenReturn(RESOLVER_ID_2);
        when(
                resolver2.canResolve(any(ViewItem.class),
                        any(ViewItemValueResolverContext.class))).thenReturn(
                true);
    }

    @Test
    public void nonAllowableResolverInViewModelNotApplicable() {
        setUpSlots(DataType.TEXT);
        when(viewModel.getResolver(slots[0])).thenReturn(resolver2);

        underTest = new SlotMappingConfigurationUIModel(resolverProvider,
                slotMappingInitializer, viewModel, errorModel);

        assertEquals(1, underTest.getSlotsWithInvalidResolvers().size());
        assertEquals(slots[0], underTest.getSlotsWithInvalidResolvers().get(0));
    }

    @Test
    public void nonManagedResolverInViewModelNotApplicable() {
        setUpSlots(DataType.TEXT);
        ViewItemValueResolver unManagedResolver = mock(ViewItemValueResolver.class);
        when(viewModel.getResolver(slots[0])).thenReturn(unManagedResolver);

        underTest = new SlotMappingConfigurationUIModel(resolverProvider,
                slotMappingInitializer, viewModel, errorModel);

        assertThat(underTest.getSlotsWithInvalidResolvers(),
                containsExactly(slots[0]));
    }

    @Test
    public void resolversInitializedWhenViewItemsAdded() {
        setUpSlots(DataType.TEXT);

        Map<Slot, ViewItemValueResolver> initialSlotMapping = new HashMap<Slot, ViewItemValueResolver>();
        initialSlotMapping.put(slots[0], resolver1);
        SlotMappingInitializer initializer = new TestSlotMappingInitializer(
                initialSlotMapping);

        underTest = new SlotMappingConfigurationUIModel(resolverProvider,
                initializer, viewModel, errorModel);

        ViewItemContainerChangeEventHandler handler = captureViewItemContainerChangeEventHandler();

        ResourceGrouping grouping = mock(ResourceGrouping.class);
        when(viewModel.getViewItems()).thenReturn(
                CollectionFactory.<ViewItem> createLightweightList());
        when(viewModel.getResourceGrouping()).thenReturn(grouping);

        LightweightList<Slot> badSlots = CollectionFactory
                .createLightweightList();
        badSlots.add(slots[0]);
        when(viewModel.getUnconfiguredSlots()).thenReturn(badSlots);
        // XXX right now underTest does not care what the event is, but it may
        // in the future, feel free to implement the event in this test in the
        // future
        handler.onViewItemContainerChanged(null);

        // verify that we set the viewModel
        verify(viewModel, times(1)).setResolver(slots[0], resolver1);
    }

    @Test
    public void resolversNotSetBeforeViewItemsAdded() {
        setUpSlots(DataType.TEXT);

        Map<Slot, ViewItemValueResolver> initialSlotMapping = new HashMap<Slot, ViewItemValueResolver>();
        initialSlotMapping.put(slots[0], resolver1);
        SlotMappingInitializer initializer = new TestSlotMappingInitializer(
                initialSlotMapping);

        underTest = new SlotMappingConfigurationUIModel(resolverProvider,
                initializer, viewModel, errorModel);

        assertThat(underTest.getSlotsWithInvalidResolvers(),
                containsExactly(slots[0]));
    }

    @Test
    public void setResolverOnconfigurationUIModelUpdatesViewModel() {
        setUpSlots(DataType.TEXT);

        when(viewModel.getResolver(slots[0])).thenReturn(resolver1);

        underTest = new SlotMappingConfigurationUIModel(resolverProvider,
                slotMappingInitializer, viewModel, errorModel);

        underTest.setCurrentResolver(slots[0], resolver2);
        verify(viewModel, times(1)).setResolver(slots[0], resolver2);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockResolversAndFactories();

        setUpResolverProvider(factory1);

        errorModel = new DefaultViewItemResolutionErrorModel();
    }

    public void setUpResolverProvider(ViewItemValueResolverFactory... factories) {

        LightweightList<ViewItemValueResolverFactory> factoryList = CollectionFactory
                .createLightweightList();

        for (ViewItemValueResolverFactory factory : factories) {
            factoryList.add(factory);
        }

        when(resolverProvider.getResolverFactories()).thenReturn(factoryList);
    }

    private void setUpSlots(DataType... dataTypes) {
        slots = createSlots(dataTypes);
        when(viewModel.getSlots()).thenReturn(slots);
    }

    @Test
    public void viewModelFiredSlotMappingChangedEventFiresEventOnUnderTest() {
        setUpSlots(DataType.TEXT);

        when(viewModel.getResolver(slots[0])).thenReturn(resolver1);
        setUpResolverProvider(factory1, factory2);
        underTest = new SlotMappingConfigurationUIModel(resolverProvider,
                slotMappingInitializer, viewModel, errorModel);

        SlotMappingUIModel uiModel = underTest.getSlotMappingUIModel(slots[0]);
        SlotMappingChangedHandler uiModelHandler = mock(SlotMappingChangedHandler.class);
        uiModel.addSlotMappingEventHandler(uiModelHandler);

        SlotMappingChangedHandler handler = captureSlotMappingChangedHandler();
        SlotMappingChangedEvent event = new SlotMappingChangedEvent(slots[0],
                resolver1, resolver2);
        handler.onSlotMappingChanged(event);

        SlotMappingChangedEvent resultingEvent = captureSlotMappingChangedEvent(uiModelHandler);
        assertEquals(resultingEvent.getCurrentResolver(), resolver2);
        assertEquals(resultingEvent.getSlot(), slots[0]);
    }

    @Test
    public void viewModelResolverChangesAreReflectedInUIModelThroughContext() {
        setUpSlots(DataType.TEXT);

        when(viewModel.getResolver(slots[0])).thenReturn(resolver1);

        setUpResolverProvider(factory1, factory2);
        underTest = new SlotMappingConfigurationUIModel(resolverProvider,
                slotMappingInitializer, viewModel, errorModel);

        when(viewModel.getResolver(slots[0])).thenReturn(resolver2);
        assertEquals(resolver2, underTest.getCurrentResolver(slots[0]));
        assertThat(underTest.getSlotsWithInvalidResolvers(),
                containsExactly(CollectionFactory
                        .<Slot> createLightweightList()));
    }

    @Test
    public void viewModelResolverChangesToInvalidStateReflectedInUIModel() {
        setUpSlots(DataType.TEXT);

        when(viewModel.getResolver(slots[0])).thenReturn(resolver1);

        underTest = new SlotMappingConfigurationUIModel(resolverProvider,
                slotMappingInitializer, viewModel, errorModel);

        when(viewModel.getResolver(slots[0])).thenReturn(resolver2);
        assertThat(underTest.getSlotsWithInvalidResolvers(),
                containsExactly(slots));
    }
}