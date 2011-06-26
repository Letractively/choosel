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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.core.client.persistence.Memento;
import org.thechiselgroup.choosel.core.client.resources.DataType;
import org.thechiselgroup.choosel.core.client.resources.persistence.ResourceSetCollector;
import org.thechiselgroup.choosel.core.client.views.resolvers.ViewItemValueResolver;

public class SlotMappingConfigurationPersistableAdapterTest {

    private FixedSlotResolversViewModelDecorator underTest;

    private Slot slot1;

    @Mock
    private ViewModel delegate;

    @Test
    public void doesNotSaveFixedMappingsToMemento() {
        Map<Slot, ViewItemValueResolver> fixedSlotResolvers = new HashMap<Slot, ViewItemValueResolver>();
        fixedSlotResolvers.put(slot1, mock(ViewItemValueResolver.class));

        when(delegate.getSlots()).thenReturn(new Slot[] { slot1 });
        when(delegate.isConfigured(any(Slot.class))).thenReturn(true);

        underTest = new FixedSlotResolversViewModelDecorator(delegate,
                fixedSlotResolvers);

        Memento result = new SlotMappingConfigurationPersistableAdapter(
                underTest).save(mock(ResourceSetCollector.class));

        assertEquals(0, result.getChildren().size());
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        slot1 = new Slot("s1", "", DataType.NUMBER);
    }
}