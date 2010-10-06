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
import java.util.Set;

import org.thechiselgroup.choosel.client.persistence.Memento;
import org.thechiselgroup.choosel.client.ui.widget.timeline.TimeLineEvent;
import org.thechiselgroup.choosel.client.ui.widget.timeline.TimeLineWidget;
import org.thechiselgroup.choosel.client.views.AbstractViewContentDisplay;
import org.thechiselgroup.choosel.client.views.DragEnablerFactory;
import org.thechiselgroup.choosel.client.views.ResourceItem;
import org.thechiselgroup.choosel.client.views.SlotResolver;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class TimeLineViewContentDisplay extends AbstractViewContentDisplay {

    private static final String MEMENTO_DATE = "date";

    private static final String MEMENTO_ZOOM_PREFIX = "zoom-band-";

    private DragEnablerFactory dragEnablerFactory;

    private TimeLineWidget timelineWidget;

    @Inject
    public TimeLineViewContentDisplay(DragEnablerFactory dragEnablerFactory) {
        this.dragEnablerFactory = dragEnablerFactory;
    }

    private void addEventsToTimeline(
            Set<? extends ResourceItem> addedResourceItems) {

        timelineWidget.addEvents(getTimeLineEvents(addedResourceItems));
    }

    @Override
    public void checkResize() {
        timelineWidget.layout();
    }

    private void createTimeLineItems(
            Set<? extends ResourceItem> addedResourceItems) {

        for (ResourceItem resourceItem : addedResourceItems) {
            resourceItem.setDisplayObject(new TimeLineItem(resourceItem, this,
                    dragEnablerFactory));
        }
    }

    @Override
    public Widget createWidget() {
        timelineWidget = new TimeLineWidget();

        timelineWidget.setHeight("100%");
        timelineWidget.setWidth("100%");

        return timelineWidget;
    }

    public final String getEventElementID(int bandIndex, String elementType,
            TimeLineEvent event) {
        return timelineWidget.getEventElementID(bandIndex, elementType, event);
    }

    @Override
    public String[] getSlotIDs() {
        return new String[] { SlotResolver.DESCRIPTION_SLOT,
                SlotResolver.COLOR_SLOT, SlotResolver.DATE_SLOT };
    }

    private TimeLineEvent[] getTimeLineEvents(
            Set<? extends ResourceItem> resourceItems) {

        TimeLineEvent[] events = new TimeLineEvent[resourceItems.size()];
        int counter = 0;
        for (ResourceItem item : resourceItems) {
            TimeLineItem timelineItem = (TimeLineItem) item.getDisplayObject();
            events[counter++] = timelineItem.getTimeLineEvent();
        }
        return events;
    }

    public TimeLineWidget getTimeLineWidget() {
        return timelineWidget;
    }

    private void removeEventsFromTimeline(
            Set<? extends ResourceItem> removedResourceItems) {
        timelineWidget.removeEvents(getTimeLineEvents(removedResourceItems));
    }

    @Override
    public void restore(Memento state) {
        timelineWidget.setZoomIndex(0,
                (Integer) state.getValue(MEMENTO_ZOOM_PREFIX + 0));
        timelineWidget.setZoomIndex(1,
                (Integer) state.getValue(MEMENTO_ZOOM_PREFIX + 1));

        // set date *AFTER* zoom restored
        Date date = (Date) state.getValue(MEMENTO_DATE);
        timelineWidget.setCenterVisibleDate(date);
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
    public void update(Set<ResourceItem> addedResourceItems,
            Set<ResourceItem> updatedResourceItems,
            Set<ResourceItem> removedResourceItems) {

        if (!addedResourceItems.isEmpty()) {
            createTimeLineItems(addedResourceItems);
            addEventsToTimeline(addedResourceItems);
            updateStatusStyling(addedResourceItems);
        }

        if (!updatedResourceItems.isEmpty()) {
            updateStatusStyling(updatedResourceItems);
        }

        if (!removedResourceItems.isEmpty()) {
            removeEventsFromTimeline(removedResourceItems);
        }
    }

    private void updateStatusStyling(Set<? extends ResourceItem> resourceItems) {
        for (ResourceItem resourceItem : resourceItems) {
            ((TimeLineItem) resourceItem.getDisplayObject())
                    .setStatusStyling(resourceItem.getStatus());
        }
    }
}