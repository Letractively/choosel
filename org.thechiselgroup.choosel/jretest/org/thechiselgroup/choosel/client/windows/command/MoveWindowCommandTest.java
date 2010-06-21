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
package org.thechiselgroup.choosel.client.windows.command;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.client.test.MockitoGWTBridge;
import org.thechiselgroup.choosel.client.windows.MoveWindowCommand;
import org.thechiselgroup.choosel.client.windows.WindowPanel;

public class MoveWindowCommandTest {

    private MoveWindowCommand command;

    @Mock
    private WindowPanel windowPanel;

    private int sourceX;

    private int sourceY;

    private int targetX;

    private int targetY;

    @Before
    public void setUp() throws Exception {
        MockitoGWTBridge.setUp();
        MockitoAnnotations.initMocks(this);

        sourceX = 10;
        sourceY = 15;
        targetX = 20;
        targetY = 25;

        command = new MoveWindowCommand(windowPanel, sourceX, sourceY, targetX,
                targetY);
    }

    @Test
    public void setWindowLocationOnExecute() {
        command.execute();
        verify(windowPanel, times(1)).animateMoveToLocation(eq(targetX),
                eq(targetY));
    }

    @Test
    public void setWindowLocationOnUndo() {
        command.undo();
        verify(windowPanel, times(1)).animateMoveToLocation(eq(sourceX),
                eq(sourceY));
    }

    @After
    public void tearDown() {
        MockitoGWTBridge.tearDown();
    }

}
