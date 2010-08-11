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

import static com.google.gwt.query.client.GQuery.$;

import java.util.ArrayList;
import java.util.List;

import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.ui.popup.PopupManager;
import org.thechiselgroup.choosel.client.ui.widget.timeline.TimeLineEvent;
import org.thechiselgroup.choosel.client.views.DragEnabler;
import org.thechiselgroup.choosel.client.views.DragEnablerFactory;
import org.thechiselgroup.choosel.client.views.HoverModel;
import org.thechiselgroup.choosel.client.views.IconResourceItem;
import org.thechiselgroup.choosel.client.views.ResourceItemValueResolver;
import org.thechiselgroup.choosel.client.views.SlotResolver;

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Event;

public class TimeLineItem extends IconResourceItem {

    // TODO move, combine with listview
    private static final String CSS_HIGHLIGHT_CLASS = "hover";

    private static final String CSS_SELECTED_CLASS = "selected";

    private DragEnablerFactory dragEnablerFactory;

    private List<String> elementIDs = new ArrayList<String>();

    private final Function mouseClickListener = new Function() {
        @Override
        public boolean f(Event e) {
            onMouseClick(e);
            return true;
        }
    };

    private final Function mouseOutListener = new Function() {
        @Override
        public boolean f(Event e) {
            onMouseOut(e);
            return true;
        }
    };

    private final Function mouseOverListener = new Function() {
        @Override
        public boolean f(Event e) {
            onMouseOver(e);
            return true;
        }
    };

    private TimeLineEvent timeLineEvent;

    private final TimeLineViewContentDisplay view;

    public TimeLineItem(String category, ResourceSet resources,
            TimeLineViewContentDisplay view, PopupManager popupManager,
            HoverModel hoverModel, ResourceItemValueResolver layerModel,
            DragEnablerFactory dragEnablerFactory) {

        super(category, resources, hoverModel, popupManager, layerModel);

        this.view = view;
        this.dragEnablerFactory = dragEnablerFactory;

        String date = (String) getResourceValue(SlotResolver.DATE_SLOT);

        timeLineEvent = TimeLineEvent.create(date, null, "", this);

    }

    private void addCssClass(String cssClass) {
        for (String elementID : elementIDs) {
            if (elementID.startsWith("label")) {
                // TODO get index of element for similar highlighting on
                // overview band
                getGElement(elementID).addClass(cssClass);
            }
        }
    }

    private void addToElementIDs(String elementID) {
        if (!elementIDs.contains(elementID)) {
            elementIDs.add(elementID);
        }
    }

    private String getColor() {
        switch (getStatus()) {
        case PARTIALLY_HIGHLIGHTED_SELECTED:
        case HIGHLIGHTED_SELECTED:
        case PARTIALLY_HIGHLIGHTED:
        case HIGHLIGHTED:
            return getHighlightColor();
        case DEFAULT:
            return getDefaultColor();
        case SELECTED:
            return getSelectedColor();
        }

        throw new RuntimeException("this point should never be reached");
    }

    private GQuery getGElement(String elementID) {
        return $("#" + elementID);
    }

    public TimeLineEvent getTimeLineEvent() {
        return timeLineEvent;
    }

    private void onMouseClick(Event e) {
        view.getCallback().switchSelection(getResourceSet());
    }

    public void onMouseOut(Event e) {
        popupManager.onMouseOut(e.getClientX(), e.getClientY());
        getHighlightingManager().setHighlighting(false);
    }

    public void onMouseOver(Event e) {
        popupManager.onMouseOver(e.getClientX(), e.getClientY());
        getHighlightingManager().setHighlighting(true);
    }

    public void onPainted(String labelElementID, String iconElementID) {
        assert labelElementID != null;
        assert iconElementID != null;

        replaceIconImageWithDiv(iconElementID);

        /*
         * every time the event is repainted, we need to hook up our listeners
         * again
         */
        registerListeners(labelElementID);
        registerListeners(iconElementID);

        addToElementIDs(labelElementID);
        addToElementIDs(iconElementID);
    }

    private void registerListeners(String elementID) {
        GQuery element = getGElement(elementID);

        element.mouseover(mouseOverListener);
        element.mouseout(mouseOutListener);
        element.click(mouseClickListener);

        final DragEnabler enabler = dragEnablerFactory.createDragEnabler(this);
        element.mouseup(new Function() {
            @Override
            public boolean f(Event e) {
                enabler.forwardMouseUp(e);
                return false;
            }
        });
        element.mouseout(new Function() {
            @Override
            public boolean f(Event e) {
                enabler.forwardMouseOut(e);
                return false;
            }
        });
        element.mousemove(new Function() {
            @Override
            public boolean f(Event e) {
                enabler.forwardMouseMove(e);
                return false;
            }
        });
        element.mousedown(new Function() {
            @Override
            public boolean f(Event e) {
                enabler.forwardMouseDownWithTargetElementPosition(e);
                return false;
            }
        });
    }

    private void removeCssClass(String cssClass) {
        for (String elementID : elementIDs) {
            if (elementID.startsWith("label")) {
                getGElement(elementID).removeClass(cssClass);
            }
        }
    }

    private void replaceIconImageWithDiv(String iconElementID) {
        GQuery gElement = getGElement(iconElementID);
        gElement.children("img").remove();
        GQuery children = gElement.children("div");
        if (children.length() == 0) {
            String label = (String) getResourceValue(SlotResolver.LABEL_SLOT);
            gElement.append("<div class='" + CSS_RESOURCE_ITEM_ICON + "'>"
                    + label + "</div>");

            GQuery div = gElement.children("div");
            div.css("background-color", getColor());
            div.css("border-color", calculateBorderColor(getColor()));
        }
    }

    private void setIconColor(String color) {
        for (String elementID : elementIDs) {
            if (elementID.startsWith("icon")) {
                GQuery div = getGElement(elementID).children("div");
                div.css("background-color", color);
                div.css("border-color", calculateBorderColor(color));
            }
        }
    }

    private void setLabelStyle(Status status) {
        switch (status) {
        case PARTIALLY_HIGHLIGHTED_SELECTED:
        case HIGHLIGHTED_SELECTED: {
            addCssClass(CSS_SELECTED_CLASS);
            addCssClass(CSS_HIGHLIGHT_CLASS);
        }
            break;
        case PARTIALLY_HIGHLIGHTED:
        case HIGHLIGHTED: {
            removeCssClass(CSS_SELECTED_CLASS);
            addCssClass(CSS_HIGHLIGHT_CLASS);
        }
            break;
        case DEFAULT: {
            removeCssClass(CSS_SELECTED_CLASS);
            removeCssClass(CSS_HIGHLIGHT_CLASS);
        }
            break;
        case SELECTED: {
            removeCssClass(CSS_HIGHLIGHT_CLASS);
            addCssClass(CSS_SELECTED_CLASS);
        }
            break;
        }
    }

    @Override
    protected void setStatusStyling(Status status) {
        setIconColor(getColor());
        setLabelStyle(status);
    }

}
