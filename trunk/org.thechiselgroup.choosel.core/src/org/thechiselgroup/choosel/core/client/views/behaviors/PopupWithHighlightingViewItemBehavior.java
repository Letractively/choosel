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
package org.thechiselgroup.choosel.core.client.views.behaviors;

import java.util.Map;

import org.thechiselgroup.choosel.core.client.fx.Opacity;
import org.thechiselgroup.choosel.core.client.resources.ui.DetailsWidgetHelper;
import org.thechiselgroup.choosel.core.client.ui.popup.Popup;
import org.thechiselgroup.choosel.core.client.ui.popup.PopupManagerFactory;
import org.thechiselgroup.choosel.core.client.ui.popup.PopupOpacityChangedEvent;
import org.thechiselgroup.choosel.core.client.ui.popup.PopupOpacityChangedEventHandler;
import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.core.client.views.model.HoverModel;
import org.thechiselgroup.choosel.core.client.views.model.ViewItem;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

public class PopupWithHighlightingViewItemBehavior extends
        PopupViewItemBehavior {

    /**
     * Maps view item ids to popup highlighting managers.
     */
    private Map<String, HighlightingManager> highlightingManagers = CollectionFactory
            .createStringMap();

    private HoverModel hoverModel;

    public PopupWithHighlightingViewItemBehavior(
            DetailsWidgetHelper detailsWidgetHelper,
            PopupManagerFactory popupManagerFactory, HoverModel hoverModel) {

        super(detailsWidgetHelper, popupManagerFactory);

        this.hoverModel = hoverModel;
    }

    @Override
    public void onViewItemCreated(ViewItem viewItem) {
        super.onViewItemCreated(viewItem);

        assert !highlightingManagers.containsKey(viewItem.getViewItemID());

        final HighlightingManager highlightingManager = new HighlightingManager(
                hoverModel, viewItem.getResources());

        Popup popup = getPopupManager(viewItem).getPopup();

        popup.addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent e) {
                highlightingManager.setHighlighting(true);
            }
        }, MouseOverEvent.getType());
        popup.addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                highlightingManager.setHighlighting(false);
            }
        }, MouseOutEvent.getType());
        popup.addHandler(new PopupOpacityChangedEventHandler() {
            @Override
            public void onOpacityChangeStarted(PopupOpacityChangedEvent event) {
                if (event.getOpacity() == Opacity.TRANSPARENT) {
                    highlightingManager.setHighlighting(false);
                }
            }
        }, PopupOpacityChangedEvent.TYPE);

        highlightingManagers.put(viewItem.getViewItemID(), highlightingManager);
    }

    @Override
    public void onViewItemRemoved(ViewItem viewItem) {
        super.onViewItemRemoved(viewItem);

        assert highlightingManagers.containsKey(viewItem.getViewItemID());

        HighlightingManager manager = highlightingManagers.remove(viewItem
                .getViewItemID());
        manager.dispose();
    }
}