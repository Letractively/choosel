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
package org.thechiselgroup.choosel.client.workspace.command;

import static org.mockito.Mockito.verify;
import static org.thechiselgroup.choosel.client.test.Matchers2.isNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.client.services.NullAsyncCallback;
import org.thechiselgroup.choosel.client.workspace.WorkspacePersistenceManager;
import org.thechiselgroup.choosel.client.workspace.WorkspacePreview;
import org.thechiselgroup.choosel.client.workspace.command.LoadWorkspaceDialogCommand.DetailsDisplay;
import org.thechiselgroup.choosel.client.workspace.command.LoadWorkspaceDialogCommand.LoadWorkspacePreviewsCallback;

public class LoadWorkspaceDialogCommandTest {

    @Mock
    private WorkspacePersistenceManager persistenceManager;

    @Mock
    private DetailsDisplay detailsDisplay;

    private LoadWorkspaceDialogCommand presenter;

    @Test
    public void callGetAllWorkspacesOnExecute() {
        presenter.execute(new NullAsyncCallback<Void>());

        verify(persistenceManager).loadWorkspacePreviews(
                isNotNull(LoadWorkspacePreviewsCallback.class));
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new LoadWorkspaceDialogCommand(detailsDisplay,
                persistenceManager);
    }

    @Test
    public void showDetailsDisplayOnSuccess() {
        LoadWorkspacePreviewsCallback callback = new LoadWorkspacePreviewsCallback(
                detailsDisplay, new NullAsyncCallback<Void>());

        List<WorkspacePreview> workspaces = new ArrayList<WorkspacePreview>();
        callback.onSuccess(workspaces);

        verify(detailsDisplay).show(workspaces);
    }
}
