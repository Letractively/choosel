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
package org.thechiselgroup.choosel.client.views.map;

import org.thechiselgroup.choosel.client.configuration.MashupInjectionConstants;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ui.DetailsWidgetHelper;
import org.thechiselgroup.choosel.client.ui.popup.PopupManagerFactory;
import org.thechiselgroup.choosel.client.views.DragEnablerFactory;
import org.thechiselgroup.choosel.client.views.ViewContentDisplay;
import org.thechiselgroup.choosel.client.views.ViewContentDisplayFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MapViewContentDisplayFactory implements ViewContentDisplayFactory {

    private final PopupManagerFactory popupManagerFactory;

    private final DetailsWidgetHelper detailsWidgetHelper;

    private final ResourceSet hoverModel;

    private final DragEnablerFactory dragEnablerFactory;

    @Inject
    public MapViewContentDisplayFactory(
	    PopupManagerFactory popupManagerFactory,
	    DetailsWidgetHelper detailsWidgetHelper,
	    @Named(MashupInjectionConstants.HOVER_MODEL) ResourceSet hoverModel,
	    DragEnablerFactory dragEnablerFactory) {

	this.popupManagerFactory = popupManagerFactory;
	this.detailsWidgetHelper = detailsWidgetHelper;
	this.hoverModel = hoverModel;
	this.dragEnablerFactory = dragEnablerFactory;
    }

    @Override
    public ViewContentDisplay createViewContentDisplay() {
	return new MapViewContentDisplay(popupManagerFactory,
		detailsWidgetHelper, hoverModel, dragEnablerFactory);
    }

}