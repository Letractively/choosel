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
package org.thechiselgroup.choosel.client.ui.popup;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.client.test.DndTestHelpers;
import org.thechiselgroup.choosel.client.test.MockitoGWTBridge;
import org.thechiselgroup.choosel.client.ui.WidgetFactory;
import org.thechiselgroup.choosel.client.ui.popup.DefaultPopupManager;
import org.thechiselgroup.choosel.client.ui.popup.PopupClosingEvent;
import org.thechiselgroup.choosel.client.ui.popup.PopupClosingHandler;

public class PopupManagerTest {

    @Mock
    private WidgetFactory widgetFactory;

    private DefaultPopupManager underTest;

    @Mock
    private PopupClosingHandler closingHandler;

    @Test
    public void fireCloseEventWhenClosingPopup() {
	underTest.addPopupClosingHandler(closingHandler);
	underTest.hidePopup();

	verify(closingHandler, times(1)).onPopupClosing(
		(PopupClosingEvent) isNotNull());
    }

    @Before
    public void setUp() throws Exception {
	MockitoGWTBridge bridge = MockitoGWTBridge.setUp();
	MockitoAnnotations.initMocks(this);
	DndTestHelpers.mockDragClientBundle(bridge);

	underTest = new DefaultPopupManager(widgetFactory) {
	    @Override
	    protected void setPopupTransparency(int newTransparency) {
	    };
	};
    }

    @After
    public void tearDown() {
	MockitoGWTBridge.tearDown();
    }
}
