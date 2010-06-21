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
package org.thechiselgroup.choosel.client.resources.ui;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.thechiselgroup.choosel.client.test.ResourcesTestHelper.createResources;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.client.resources.DefaultResourceSet;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ResourceSetContainer;
import org.thechiselgroup.choosel.client.resources.UnmodifiableResourceSet;
import org.thechiselgroup.choosel.client.test.DndTestHelpers;
import org.thechiselgroup.choosel.client.test.MockitoGWTBridge;
import org.thechiselgroup.choosel.client.ui.dnd.ResourceSetAvatarDragController;
import org.thechiselgroup.choosel.client.util.Disposable;

import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class HighlightingResourceSetAvatarFactoryTest {

    @Mock
    private ResourceSetAvatar avatar;

    @Mock
    private ResourceSetAvatarFactory delegate;

    @Mock
    private HandlerRegistration handlerRegistration;

    @Mock
    private ResourceSet hoverModel;

    private DefaultResourceSet resources;

    private ResourceSetContainer setHoverModel;

    private HighlightingResourceSetAvatarFactory underTest;

    @Mock
    private ResourceSetAvatarDragController dragController;

    @Test
    public void addDisposeHook() {
        underTest.createAvatar(createResources(1));

        ArgumentCaptor<Disposable> argument = ArgumentCaptor
                .forClass(Disposable.class);
        verify(avatar, times(1)).addDisposable(argument.capture());

        argument.getValue().dispose();

        verify(dragController, times(1)).removeDragHandler(
                any(DragHandler.class));
        verify(handlerRegistration, times(2)).removeHandler();
    }

    @Test
    public void highlightIfUnmodifiableWrapperGetsHighlighted() {
        DefaultResourceSet wrappedSet = createResources(1);
        UnmodifiableResourceSet unmodifiableWrapper = new UnmodifiableResourceSet(
                wrappedSet);

        when(avatar.getResourceSet()).thenReturn(wrappedSet);

        underTest.createAvatar(wrappedSet);

        setHoverModel.setResourceSet(unmodifiableWrapper);

        verify(avatar, times(1)).setHover(true);
    }

    @Test
    public void highlightUnmodifiableWrapperIfOtherUnmodifiableWrapperGetsHighlighted() {
        DefaultResourceSet wrappedSet = createResources(1);
        UnmodifiableResourceSet unmodifiableWrapper1 = new UnmodifiableResourceSet(
                wrappedSet);
        UnmodifiableResourceSet unmodifiableWrapper2 = new UnmodifiableResourceSet(
                wrappedSet);

        when(avatar.getResourceSet()).thenReturn(unmodifiableWrapper1);

        underTest.createAvatar(unmodifiableWrapper1);

        setHoverModel.setResourceSet(unmodifiableWrapper2);

        verify(avatar, times(1)).setHover(true);
    }

    @Test
    public void highlightUnmodifiableWrappersIfWrappedSetGetsHighlighted() {
        DefaultResourceSet wrappedSet = createResources(1);
        UnmodifiableResourceSet unmodifiableWrapper = new UnmodifiableResourceSet(
                wrappedSet);

        when(avatar.getResourceSet()).thenReturn(unmodifiableWrapper);

        underTest.createAvatar(unmodifiableWrapper);

        setHoverModel.setResourceSet(wrappedSet);

        verify(avatar, times(1)).setHover(true);
    }

    @Test
    public void hoverClearedAtDragStart() {
        underTest.createAvatar(resources);

        ArgumentCaptor<DragHandler> argument = ArgumentCaptor
                .forClass(DragHandler.class);
        verify(dragController, times(1)).addDragHandler(argument.capture());

        DragHandler dragHandler = argument.getValue();

        dragHandler.onDragStart(mock(DragStartEvent.class));

        verify(hoverModel, times(1)).removeAll(eq(resources));
        verify(setHoverModel, times(1)).setResourceSet((ResourceSet) isNull());
    }

    @Test
    public void mouseOutRemovesResourcesFromHover() {
        underTest.createAvatar(resources);

        when(avatar.getResourceSet()).thenReturn(resources);

        ArgumentCaptor<MouseOutHandler> captor = ArgumentCaptor
                .forClass(MouseOutHandler.class);
        verify(avatar, times(1)).addMouseOutHandler(captor.capture());

        MouseOutHandler mouseOutHandler = captor.getValue();

        mouseOutHandler.onMouseOut(mock(MouseOutEvent.class));

        verify(hoverModel, times(1)).removeAll(eq(resources));
        verify(setHoverModel, times(1)).setResourceSet((ResourceSet) isNull());
    }

    @Test
    public void mouseOverAddsResourcesToHover() {
        underTest.createAvatar(resources);

        when(avatar.getResourceSet()).thenReturn(resources);

        ArgumentCaptor<MouseOverHandler> captor = ArgumentCaptor
                .forClass(MouseOverHandler.class);
        verify(avatar, times(1)).addMouseOverHandler(captor.capture());

        MouseOverHandler mouseOverHandler = captor.getValue();

        mouseOverHandler.onMouseOver(mock(MouseOverEvent.class));

        verify(hoverModel, times(1)).addAll(eq(resources));
        verify(setHoverModel, times(1)).setResourceSet(eq(resources));
    }

    @Test
    public void setDragAvatarHoverOnResourceSetContainerEvent() {
        underTest.createAvatar(resources);
        setHoverModel.setResourceSet(resources);

        verify(avatar, times(1)).setHover(true);
    }

    @Test
    public void setDragAvatarHoverOnResourceSetContainerEventToNull() {
        underTest.createAvatar(resources);

        setHoverModel.setResourceSet(resources);
        setHoverModel.setResourceSet(null);

        verify(avatar, times(1)).setHover(false);
    }

    @Before
    public void setUp() throws Exception {
        MockitoGWTBridge bridge = MockitoGWTBridge.setUp();
        MockitoAnnotations.initMocks(this);
        DndTestHelpers.mockDragClientBundle(bridge);

        resources = spy(createResources(1));
        setHoverModel = spy(new ResourceSetContainer());

        underTest = new HighlightingResourceSetAvatarFactory(delegate,
                hoverModel, setHoverModel, dragController);

        when(delegate.createAvatar(any(ResourceSet.class))).thenReturn(avatar);

        when(avatar.getResourceSet()).thenReturn(resources);

        when(avatar.addMouseOutHandler(any(MouseOutHandler.class))).thenReturn(
                handlerRegistration);
        when(avatar.addMouseOverHandler(any(MouseOverHandler.class)))
                .thenReturn(handlerRegistration);
    }

    @After
    public void tearDown() {
        MockitoGWTBridge.tearDown();
    }
}
