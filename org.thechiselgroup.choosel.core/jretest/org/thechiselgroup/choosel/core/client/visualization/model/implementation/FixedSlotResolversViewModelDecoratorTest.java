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
package org.thechiselgroup.choosel.core.client.visualization.model.implementation;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.thechiselgroup.choosel.core.shared.test.matchers.collections.CollectionMatchers.equalsArray;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.core.client.util.DataType;
import org.thechiselgroup.choosel.core.client.visualization.model.Slot;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemValueResolver;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualizationModel;

public class FixedSlotResolversViewModelDecoratorTest {

    private FixedSlotResolversVisualizationModelDecorator underTest;

    private Slot slot1;

    private Slot slot2;

    @Mock
    private VisualizationModel delegate;

    @Mock
    private VisualItemValueResolver resolver;

    @Test
    public void getSlotsExcludesFixedSlots() {
        Map<Slot, VisualItemValueResolver> fixedSlotResolvers = new HashMap<Slot, VisualItemValueResolver>();
        fixedSlotResolvers.put(slot1, resolver);

        when(delegate.getSlots()).thenReturn(new Slot[] { slot1, slot2 });

        underTest = new FixedSlotResolversVisualizationModelDecorator(delegate,
                fixedSlotResolvers);

        assertThat(underTest.getSlots(), equalsArray(slot2));
    }

    @Test
    public void setFixedSlotResolver() {
        Map<Slot, VisualItemValueResolver> fixedSlotResolvers = new HashMap<Slot, VisualItemValueResolver>();
        fixedSlotResolvers.put(slot1, resolver);

        when(delegate.getSlots()).thenReturn(new Slot[] { slot1 });

        underTest = new FixedSlotResolversVisualizationModelDecorator(delegate,
                fixedSlotResolvers);

        verify(delegate, times(1)).setResolver(slot1, resolver);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        slot1 = new Slot("s1", "", DataType.NUMBER);
        slot2 = new Slot("s2", "", DataType.NUMBER);
    }

}