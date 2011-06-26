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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.thechiselgroup.choosel.core.client.views.model.DefaultViewModelTestHelper.stubHandlerRegistration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gwt.event.shared.HandlerRegistration;

/* 
 * TODO we don't count how often event handler get registered. Every added event
 * handler should also get removed.
 */
public class DefaultViewModelDisposeTest {

    private DefaultViewModel underTest;

    @Mock
    private HandlerRegistration containedResourcesHandlerRegistration;

    @Mock
    private HandlerRegistration highlightedResourcesHandlerRegistration;

    @Mock
    private HandlerRegistration selectedResourcesHandlerRegistration;

    private DefaultViewModelTestHelper helper;

    @Test
    public void disposeContainedResourcesEventHandler() {
        verify(containedResourcesHandlerRegistration, times(1)).removeHandler();
    }

    @Test
    public void disposeContentDisplay() {
        verify(helper.getViewContentDisplay(), times(1)).dispose();
    }

    @Test
    public void disposeHighlightedResourcesEventHandler() {
        verify(highlightedResourcesHandlerRegistration, times(1))
                .removeHandler();
    }

    @Test
    public void disposeSelectedResourcesEventHandler() {
        verify(selectedResourcesHandlerRegistration, times(1)).removeHandler();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        helper = new DefaultViewModelTestHelper();
        helper.mockContainedResources();
        helper.mockHighlightedResources();
        helper.mockSelectedResources();
        helper.setUseDefaultFactories(true);

        stubHandlerRegistration(helper.getContainedResources(),
                containedResourcesHandlerRegistration);
        stubHandlerRegistration(helper.getSelectedResources(),
                selectedResourcesHandlerRegistration);
        stubHandlerRegistration(helper.getHighlightedResources(),
                highlightedResourcesHandlerRegistration);

        underTest = helper.createTestViewModel();

        underTest.dispose();
    }

}