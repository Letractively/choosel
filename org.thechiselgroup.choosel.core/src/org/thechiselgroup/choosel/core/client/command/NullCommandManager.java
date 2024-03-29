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
package org.thechiselgroup.choosel.core.client.command;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;

public class NullCommandManager implements CommandManager {

    @Override
    public <H extends CommandManagerEventHandler> HandlerRegistration addHandler(
            Type<H> type, H handler) {
        return null;
    }

    @Override
    public boolean canRedo() {
        return false;
    }

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public void execute(UndoableCommand command) {
    }

    @Override
    public UndoableCommand getRedoCommand() {
        return null;
    }

    @Override
    public UndoableCommand getUndoCommand() {
        return null;
    }

    @Override
    public void redo() {
    }

    @Override
    public void undo() {
    }

}