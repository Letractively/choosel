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
package org.thechiselgroup.choosel.client.command.ui;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.client.command.CommandManager;
import org.thechiselgroup.choosel.client.command.DefaultCommandManager;
import org.thechiselgroup.choosel.client.resolver.ResourceSetToValueResolver;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.test.MockitoGWTBridge;
import org.thechiselgroup.choosel.client.test.TestUndoableCommandWithDescription;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public class UndoCommandManagerPresenterTest {

    private static final String COMMAND_DESCRIPTION = "command";

    @Mock
    private TestUndoableCommandWithDescription command;

    private CommandManager commandManager;

    private UndoCommandManagerPresenter underTest;

    @Mock
    private CommandManagerPresenterDisplay undoDisplay;

    @Mock
    private ResourceSetToValueResolver resolver;

    @Mock
    private HasClickHandlers undoClickHandlers;

    // TODO tests for command manager with initial state
    @Test
    public void buttonsDisabledInitialyForEmptyCommandManager() {
        verify(undoDisplay, times(1)).setButtonEnabled(false);
    }

    @Test
    public void callsUndoOnClick() {
        commandManager.addExecutedCommand(command);

        ArgumentCaptor<ClickHandler> argument = ArgumentCaptor
                .forClass(ClickHandler.class);
        verify(undoClickHandlers, times(1)).addClickHandler(argument.capture());

        ClickHandler handler = argument.getValue();

        handler.onClick(mock(ClickEvent.class));

        verify(commandManager, times(1)).undo();
    }

    @Test
    public void disableOnClear() {
        commandManager.addExecutedCommand(command);
        commandManager.addExecutedCommand(command);
        commandManager.undo();

        verify(undoDisplay, times(1)).setButtonEnabled(false);

        commandManager.clear();

        verify(undoDisplay, times(2)).setButtonEnabled(false);
    }

    @Test
    public void disableUndoButtonOnEventIfNotUndoable() {
        commandManager.addExecutedCommand(command);
        commandManager.undo();

        verify(undoDisplay, times(2)).setButtonEnabled(false);
    }

    @Test
    public void enableUndoButtonOnEventIfUndoable() {
        commandManager.addExecutedCommand(command);

        verify(undoDisplay).setButtonEnabled(true);
    }

    @Test
    public void initRegistersClickHandler() {
        verify(undoClickHandlers, times(1)).addClickHandler(
                any(ClickHandler.class));
    }

    @Test
    public void setUndoButtonCommandDescriptionOnEventIfNotUndoable() {
        commandManager.addExecutedCommand(command);
        commandManager.undo();

        verify(undoDisplay, times(2)).setCommandDescription("");
    }

    @Test
    public void setUndoButtonDescriptionOnEventIfUndoable() {
        commandManager.addExecutedCommand(command);

        verify(undoDisplay).setCommandDescription(COMMAND_DESCRIPTION);
    }

    @Before
    public void setUp() throws Exception {
        MockitoGWTBridge.setUp();
        MockitoAnnotations.initMocks(this);

        commandManager = spy(new DefaultCommandManager());
        underTest = new UndoCommandManagerPresenter(commandManager, undoDisplay);

        when(resolver.resolve(any(ResourceSet.class), any(String.class)))
                .thenReturn("");
        when(undoDisplay.getClickHandlers()).thenReturn(undoClickHandlers);
        when(command.getDescription()).thenReturn(COMMAND_DESCRIPTION);

        underTest.init();
    }

    @After
    public void tearDown() {
        MockitoGWTBridge.tearDown();
    }

}
