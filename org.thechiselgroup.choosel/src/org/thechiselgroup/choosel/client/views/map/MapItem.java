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

import org.thechiselgroup.choosel.client.views.DragEnabler;
import org.thechiselgroup.choosel.client.views.DragEnablerFactory;
import org.thechiselgroup.choosel.client.views.IconResourceItem;
import org.thechiselgroup.choosel.client.views.DefaultResourceItem.Status;
import org.thechiselgroup.choosel.client.views.ResourceItem;
import org.thechiselgroup.choosel.client.views.SlotResolver;
import org.thechiselgroup.choosel.client.views.ViewContentDisplayCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;

public class MapItem extends IconResourceItem {

    private class MarkerEventHandler implements ClickHandler, MouseOutHandler,
            MouseOverHandler {

        @Override
        public void onClick(ClickEvent event) {
            callback.switchSelection(resourceItem.getResourceSet());
        }

        @Override
        public void onMouseOut(MouseOutEvent event) {
            resourceItem.getPopupManager().onMouseOut(event.getClientX(),
                    event.getClientY());
            resourceItem.getHighlightingManager().setHighlighting(false);
        }

        @Override
        public void onMouseOver(MouseOverEvent event) {
            resourceItem.getPopupManager().onMouseOver(event.getClientX(),
                    event.getClientY());
            resourceItem.getHighlightingManager().setHighlighting(true);
        }
    }

    private final ViewContentDisplayCallback callback;

    private DragEnablerFactory dragEnablerFactory;

    private MarkerEventHandler eventHandler;

    private LabelOverlay overlay;

    public MapItem(ResourceItem resourceItem, LatLng point,
            ViewContentDisplayCallback callback,
            DragEnablerFactory dragEnablerFactory) {

        super(resourceItem);

        this.callback = callback;
        this.dragEnablerFactory = dragEnablerFactory;

        String label = (String) getResourceValue(SlotResolver.LABEL_SLOT);
        this.overlay = new LabelOverlay(point, Point.newInstance(-10, -10),
                label, CSS_RESOURCE_ITEM_ICON); // -10 = - (width /2)
        this.eventHandler = new MarkerEventHandler();

        initEventHandlers();
    }

    public LabelOverlay getOverlay() {
        return overlay;
    }

    private void initEventHandlers() {
        overlay.addMouseOverHandler(eventHandler);
        overlay.addMouseOutHandler(eventHandler);
        overlay.addClickHandler(eventHandler);

        final DragEnabler enabler = dragEnablerFactory
                .createDragEnabler(resourceItem);

        overlay.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                enabler.forwardMouseUp(event.getNativeEvent());
            }
        });
        overlay.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                enabler.forwardMouseOut(event.getNativeEvent());
            }
        });
        overlay.addMouseMoveHandler(new MouseMoveHandler() {
            @Override
            public void onMouseMove(MouseMoveEvent event) {
                enabler.forwardMouseMove(event.getNativeEvent());
            }
        });
        overlay.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                enabler.forwardMouseDownWithTargetElementPosition(event
                        .getNativeEvent());
                event.stopPropagation(); // to prevent standard map drag
            }
        });
    }

    public void setDefaultStyle() {
        overlay.setBackgroundColor(getDefaultColor());
        overlay.setBorderColor(calculateBorderColor(getDefaultColor()));
        overlay.setZIndex(Z_INDEX_DEFAULT);
    }

    private void setHighlightedStyle() {
        overlay.setBackgroundColor(getHighlightColor());
        overlay.setBorderColor(calculateBorderColor(getHighlightColor()));
        overlay.setZIndex(Z_INDEX_HIGHLIGHTED);
    }

    private void setSelectedStyle() {
        overlay.setBackgroundColor(getSelectedColor());
        overlay.setBorderColor(calculateBorderColor(getSelectedColor()));
        overlay.setZIndex(Z_INDEX_SELECTED);
    }

    protected void setStatusStyling(Status status) {
        switch (status) {
        case PARTIALLY_HIGHLIGHTED:
        case PARTIALLY_HIGHLIGHTED_SELECTED:
        case HIGHLIGHTED_SELECTED:
        case HIGHLIGHTED: {
            setHighlightedStyle();
        }
            break;
        case DEFAULT: {
            setDefaultStyle();
        }
            break;
        case SELECTED: {
            setSelectedStyle();
        }
            break;
        }
    }
}