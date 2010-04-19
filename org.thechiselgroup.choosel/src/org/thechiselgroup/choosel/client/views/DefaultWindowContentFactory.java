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
package org.thechiselgroup.choosel.client.views;

import static org.thechiselgroup.choosel.client.configuration.MashupInjectionConstants.*;

import org.thechiselgroup.choosel.client.MashupClient;
import org.thechiselgroup.choosel.client.label.CategoryLabelProvider;
import org.thechiselgroup.choosel.client.label.LabelProvider;
import org.thechiselgroup.choosel.client.resources.ResourceCategorizer;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ResourceSetFactory;
import org.thechiselgroup.choosel.client.resources.ResourceSplitter;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatarResourceSetsPresenter;
import org.thechiselgroup.choosel.client.ui.HelpWindowContent;
import org.thechiselgroup.choosel.client.ui.NoteWindowContent;
import org.thechiselgroup.choosel.client.ui.dnd.DropEnabledViewContentDisplay;
import org.thechiselgroup.choosel.client.ui.dnd.ResourceSetAvatarDropTargetManager;
import org.thechiselgroup.choosel.client.windows.WindowContent;
import org.thechiselgroup.choosel.client.windows.WindowContentFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DefaultWindowContentFactory implements WindowContentFactory {

    private ResourceSetAvatarFactory userSetsDragAvatarFactory;

    private ResourceSetAvatarFactory typesDragAvatarFactory;

    private ResourceSetAvatarFactory selectionDragAvatarFactory;

    private ResourceSet hoverModel;

    private ResourceSetFactory resourceSetFactory;

    private LabelProvider selectionModelLabelFactory;

    private ResourceCategorizer categorizer;

    private CategoryLabelProvider labelProvider;

    private ResourceSetAvatarDropTargetManager contentDropTargetManager;

    private final ResourceSetAvatarFactory allResourcesDragAvatarFactory;

    @Inject
    public DefaultWindowContentFactory(
	    @Named(AVATAR_FACTORY_SET) ResourceSetAvatarFactory userSetsDragAvatarFactory,
	    @Named(AVATAR_FACTORY_TYPE) ResourceSetAvatarFactory typesDragAvatarFactory,
	    @Named(AVATAR_FACTORY_ALL_RESOURCES) ResourceSetAvatarFactory allResourcesDragAvatarFactory,
	    @Named(AVATAR_FACTORY_SELECTION) ResourceSetAvatarFactory selectionDragAvatarFactory,
	    @Named(HOVER_MODEL) ResourceSet hoverModel,
	    ResourceSetFactory resourceSetFactory,
	    @Named(LABEL_PROVIDER_SELECTION_SET) LabelProvider selectionModelLabelFactory,
	    ResourceCategorizer categorizer,
	    CategoryLabelProvider labelProvider,
	    @Named(DROP_TARGET_MANAGER_VIEW_CONTENT) ResourceSetAvatarDropTargetManager contentDropTargetManager) {

	this.userSetsDragAvatarFactory = userSetsDragAvatarFactory;
	this.typesDragAvatarFactory = typesDragAvatarFactory;
	this.allResourcesDragAvatarFactory = allResourcesDragAvatarFactory;
	this.selectionDragAvatarFactory = selectionDragAvatarFactory;
	this.contentDropTargetManager = contentDropTargetManager;
	this.hoverModel = hoverModel;
	this.resourceSetFactory = resourceSetFactory;
	this.selectionModelLabelFactory = selectionModelLabelFactory;
	this.categorizer = categorizer;
	this.labelProvider = labelProvider;
    }

    public WindowContent createWindowContent(String contentType) {
	assert contentType != null;

	// direct stuff
	if ("ncbo-search".equals(contentType)) {
	    return MashupClient.injector.createNCBOSearchViewContent();
	} else if ("help".equals(contentType)) {
	    return new HelpWindowContent();
	} else if ("note".equals(contentType)) {
	    return new NoteWindowContent();
	}

	// otherwise: view
	ResourceSplitter resourceSplitter = new ResourceSplitter(categorizer,
		resourceSetFactory, labelProvider);

	ViewContentDisplay contentDisplay = new DropEnabledViewContentDisplay(
		createContentDisplay(contentType), contentDropTargetManager);

	DefaultView view = new DefaultView(hoverModel,
		selectionModelLabelFactory, resourceSetFactory,
		new ResourceSetAvatarResourceSetsPresenter(
			userSetsDragAvatarFactory),
		new ResourceSetAvatarResourceSetsPresenter(
			typesDragAvatarFactory),
		new ResourceSetAvatarResourceSetsPresenter(
			allResourcesDragAvatarFactory),
		new ResourceSetAvatarResourceSetsPresenter(
			selectionDragAvatarFactory), resourceSplitter,
		contentDisplay, contentType, contentType);

	return view;
    }

    // TODO replace with factory // map of factories
    private ViewContentDisplay createContentDisplay(String contentType) {
	if ("Map".equals(contentType)) {
	    return MashupClient.injector.createMap();
	} else if ("List".equals(contentType)) {
	    return MashupClient.injector.createList();
	} else if ("Timeline".equals(contentType)) {
	    return MashupClient.injector.createTimeLine();
	} else if ("Graph".equals(contentType)) {
	    return MashupClient.injector.createGraph();
	}

	throw new RuntimeException("content type " + contentType
		+ " not supported.");
    }
}