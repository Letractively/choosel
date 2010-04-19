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
package org.thechiselgroup.choosel.client.ui.messages;

import static org.thechiselgroup.choosel.client.configuration.MashupInjectionConstants.*;

import org.thechiselgroup.choosel.client.command.AsyncCommand;
import org.thechiselgroup.choosel.client.command.AsyncCommandExecutor;
import org.thechiselgroup.choosel.client.util.HasDescription;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MessageBlockingCommandExecutor implements AsyncCommandExecutor {

    private MessageManager messageManager;

    private AsyncCommandExecutor delegate;

    @Inject
    public MessageBlockingCommandExecutor(MessageManager messageManager,
	    @Named(DEFAULT) AsyncCommandExecutor delegate) {

	assert messageManager != null;
	assert delegate != null;

	this.messageManager = messageManager;
	this.delegate = delegate;
    }

    @Override
    public void execute(AsyncCommand command) {
	String description = null;

	if (command instanceof HasDescription) {
	    description = ((HasDescription) command).getDescription();
	}

	delegate.execute(new AsyncCommandMessageWrapper(
		messageManager, description, command));
    }
}