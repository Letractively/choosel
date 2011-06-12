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
package org.thechiselgroup.choosel.example.workbench.client;

import org.thechiselgroup.choosel.core.client.command.AsyncCommandExecutor;
import org.thechiselgroup.choosel.core.client.ui.Action;
import org.thechiselgroup.choosel.dnd.client.windows.WindowContent;
import org.thechiselgroup.choosel.visualization_component.chart.client.barchart.BarChart;
import org.thechiselgroup.choosel.visualization_component.chart.client.scatterplot.ScatterPlot;
import org.thechiselgroup.choosel.visualization_component.map.client.Map;
import org.thechiselgroup.choosel.visualization_component.text.client.TextVisualization;
import org.thechiselgroup.choosel.visualization_component.timeline.client.TimeLine;
import org.thechiselgroup.choosel.workbench.client.RestrictImporterToOneDataSourceManager;
import org.thechiselgroup.choosel.workbench.client.importer.ImportDialog;
import org.thechiselgroup.choosel.workbench.client.init.WorkbenchInitializer;
import org.thechiselgroup.choosel.workbench.client.workspace.command.ConfigureSharedViewsDialogCommand;

import com.google.inject.Inject;

public class ChooselExampleWorkbench extends WorkbenchInitializer {

    @Inject
    private ConfigureSharedViewsDialogCommand configSharedViewsCommand;

    @Inject
    private AsyncCommandExecutor asyncCommandExecutor;

    @Override
    protected void afterInit() {
        WindowContent content = windowContentProducer
                .createWindowContent(WorkbenchInitializer.WINDOW_CONTENT_HELP);

        desktop.createWindow(content, 30, 120, 800, 600);
    }

    private void createImportDialog() {
        Action importAction = addDialogActionToToolbar(DATA_PANEL, "Import",
                new ImportDialog(importer, dataSources));

        new RestrictImporterToOneDataSourceManager(dataSources, importAction)
                .init();
    }

    @Override
    protected void initCustomActions() {
        // addActionToToolbar(WORKSPACE_PANEL, "Load Workspace",
        // "workspace-open",
        // new AsyncCommandToCommandAdapter(configSharedViewsCommand,
        // asyncCommandExecutor));

        if (runsInDevelopmentMode()) {
            // addCreateWindowActionToToolbar(DEVELOPER_MODE_PANEL, "Graph",
            // Graph.ID);
            // addCreateWindowActionToToolbar(DEVELOPER_MODE_PANEL,
            // "Circular Bar", TYPE_CIRCULAR_BAR);
            // addCreateWindowActionToToolbar(DEVELOPER_MODE_PANEL, "Time",
            // TYPE_TIME);
            // addCreateWindowActionToToolbar(DEVELOPER_MODE_PANEL, "Dot",
            // TYPE_DOT);
            // addCreateWindowActionToToolbar(DEVELOPER_MODE_PANEL, "Pie Chart",
            // PieChart.ID);
        }

        addCreateWindowActionToToolbar(VIEWS_PANEL, "Note", WINDOW_CONTENT_NOTE);
        addCreateWindowActionToToolbar(VIEWS_PANEL, "Text",
                TextVisualization.ID);
        addCreateWindowActionToToolbar(VIEWS_PANEL, "Map", Map.ID);
        addCreateWindowActionToToolbar(VIEWS_PANEL, "Timeline", TimeLine.ID);
        addCreateWindowActionToToolbar(VIEWS_PANEL, "Bar Chart", BarChart.ID);
        addCreateWindowActionToToolbar(VIEWS_PANEL, "Scatter Plot",
                ScatterPlot.ID);

        createImportDialog();
    }

    @Override
    protected void initWorkspacePanel() {
        initNewWorkspaceAction();
        if (runsInDevelopmentMode()) {
            initLoadWorkspaceAction();
            initSaveWorkspaceAction();
            initShareWorkspaceAction();
        }
    }
}