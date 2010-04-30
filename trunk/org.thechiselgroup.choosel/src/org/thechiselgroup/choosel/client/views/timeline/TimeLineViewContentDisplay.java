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
package org.thechiselgroup.choosel.client.views.timeline;

import java.util.Date;
import java.util.List;

import org.thechiselgroup.choosel.client.configuration.ChooselInjectionConstants;
import org.thechiselgroup.choosel.client.persistence.Memento;
import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ui.DetailsWidgetHelper;
import org.thechiselgroup.choosel.client.ui.popup.PopupManager;
import org.thechiselgroup.choosel.client.ui.popup.PopupManagerFactory;
import org.thechiselgroup.choosel.client.ui.widget.timeline.TimeLineWidget;
import org.thechiselgroup.choosel.client.views.AbstractViewContentDisplay;
import org.thechiselgroup.choosel.client.views.DragEnablerFactory;
import org.thechiselgroup.choosel.client.views.Layer;
import org.thechiselgroup.choosel.client.views.ResourceItem;
import org.thechiselgroup.choosel.client.views.SlotResolver;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TimeLineViewContentDisplay extends AbstractViewContentDisplay {

    private static final String MEMENTO_ZOOM_PREFIX = "zoom-band-";

    private static final String MEMENTO_DATE = "date";

    private TimeLineWidget timelineWidget;

    private DragEnablerFactory dragEnablerFactory;

    @Inject
    public TimeLineViewContentDisplay(
	    PopupManagerFactory popupManagerFactory,
	    DetailsWidgetHelper detailsWidgetHelper,
	    SlotResolver slotResolver,
	    @Named(ChooselInjectionConstants.HOVER_MODEL) ResourceSet hoverModel,
	    DragEnablerFactory dragEnablerFactory) {

	super(popupManagerFactory, detailsWidgetHelper, hoverModel,
		slotResolver);

	this.dragEnablerFactory = dragEnablerFactory;
    }

    @Override
    public ResourceItem createResourceItem(Layer layer, Resource i) {
	PopupManager popupManager = createPopupManager(layer, i);

	TimeLineItem timeLineItem = new TimeLineItem(i, this, popupManager,
		hoverModel, layer, dragEnablerFactory);

	timelineWidget.addEvent(timeLineItem.getTimeLineEvent());

	return timeLineItem;
    }

    @Override
    public String[] getSlotIDs() {
	return new String[] { SlotResolver.DESCRIPTION_SLOT,
		SlotResolver.LABEL_SLOT, SlotResolver.COLOR_SLOT,
		SlotResolver.DATE_SLOT };
    }

    @Override
    public void initLayer(Layer layerModel, List<Layer> layers) {
	// TODO should happen automatically based on available slots...
	layerModel.putResolver(SlotResolver.DESCRIPTION_SLOT, getSlotResolver()
		.createDescriptionSlotResolver(layerModel.getCategory()));
	layerModel.putResolver(SlotResolver.COLOR_SLOT, getSlotResolver()
		.createColorSlotResolver(layerModel.getCategory(), layers));
	layerModel.putResolver(SlotResolver.LABEL_SLOT, getSlotResolver()
		.createLabelSlotResolver(layerModel.getCategory()));
	layerModel.putResolver(SlotResolver.DATE_SLOT, getSlotResolver()
		.createDateSlotResolver(layerModel.getCategory()));
    }

    @Override
    public void checkResize() {
	timelineWidget.layout();
    }

    @Override
    public Widget createWidget() {
	timelineWidget = new TimeLineWidget();

	timelineWidget.setHeight("100%");
	timelineWidget.setWidth("100%");

	return timelineWidget;
    }

    @Override
    public void removeResourceItem(ResourceItem resourceItem) {
	timelineWidget.removeEvent(((TimeLineItem) resourceItem)
		.getTimeLineEvent());
    }

    public TimeLineWidget getTimeLineWidget() {
	return timelineWidget;
    }

    @Override
    public Memento save() {
	Memento state = new Memento();
	state.setValue(MEMENTO_DATE, timelineWidget.getCenterVisibleDate());
	state.setValue(MEMENTO_ZOOM_PREFIX + 0, timelineWidget.getZoomIndex(0));
	state.setValue(MEMENTO_ZOOM_PREFIX + 1, timelineWidget.getZoomIndex(1));
	return state;
    }

    @Override
    public void restore(Memento state) {
	timelineWidget.setZoomIndex(0, (Integer) state
		.getValue(MEMENTO_ZOOM_PREFIX + 0));
	timelineWidget.setZoomIndex(1, (Integer) state
		.getValue(MEMENTO_ZOOM_PREFIX + 1));

	// set date *AFTER* zoom restored
	Date date = (Date) state.getValue(MEMENTO_DATE);
	timelineWidget.setCenterVisibleDate(date);
    }
}