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
package org.thechiselgroup.choosel.client.views.list;

import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatar;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatarType;
import org.thechiselgroup.choosel.client.ui.dnd.ResourceSetAvatarDragController;
import org.thechiselgroup.choosel.client.ui.popup.DefaultPopupManager;
import org.thechiselgroup.choosel.client.ui.popup.PopupManager;
import org.thechiselgroup.choosel.client.views.HoverModel;
import org.thechiselgroup.choosel.client.views.ResourceItem;
import org.thechiselgroup.choosel.client.views.ResourceItemValueResolver;
import org.thechiselgroup.choosel.client.views.SlotResolver;
import org.thechiselgroup.choosel.client.views.list.ListViewContentDisplay.Display;

public class ListItem extends ResourceItem {

    public class ListItemLabel extends ResourceSetAvatar {

        public ListItemLabel(String text,
                ResourceSetAvatarDragController dragController,
                ResourceSet resources) {

            super(text, "avatar-resourceSet", resources,
                    ResourceSetAvatarType.SET);

            removeStyleName(CSS_CLASS);
            setEnabled(true);
            dragController.setDraggable(this, true);
        }

        public ListItem getListItem() {
            return ListItem.this;
        }

        @Override
        public void setEnabled(boolean dragEnabled) {
            super.setEnabled(dragEnabled);

            removeStyleName(CSS_AVATAR_DISABLED);
            removeStyleName("avatar-resourceSet");
        }

    }

    private static final String CSS_GRAYED_OUT = "listItemGrayedOut";

    private static final String CSS_HIGHLIGHTED = "listItemHover";

    private static final String CSS_LIST = "listItem";

    private static final String CSS_SELECTED = "listItemSelected";

    private final Display display;

    private ResourceSetAvatarDragController dragController;

    private ListItemLabel label;

    public ListItem(String category, ResourceSet resources,
            HoverModel hoverModel, PopupManager popupManager,
            ListViewContentDisplay.Display display,
            ResourceItemValueResolver layerModel,
            ResourceSetAvatarDragController dragController) {

        super(category, resources, hoverModel, popupManager, layerModel);

        this.display = display;
        this.dragController = dragController;
    }

    public ListItemLabel getLabel() {
        return label;
    }

    public void init() {
        this.label = new ListItemLabel("", dragController, getResourceSet());
        this.label.addStyleName(CSS_LIST);

        DefaultPopupManager.linkManagerToSource(popupManager, getLabel());

        updateContent();
    }

    @Override
    protected void setStatusStyling(Status status) {
        switch (status) {
        case HIGHLIGHTED_SELECTED: {
            display.removeStyleName(this, CSS_GRAYED_OUT);
            display.addStyleName(this, CSS_SELECTED);
            display.addStyleName(this, CSS_HIGHLIGHTED);
        }
            break;
        case HIGHLIGHTED: {
            display.removeStyleName(this, CSS_SELECTED);
            display.removeStyleName(this, CSS_GRAYED_OUT);
            display.addStyleName(this, CSS_HIGHLIGHTED);
        }
            break;
        case DEFAULT: {
            display.removeStyleName(this, CSS_SELECTED);
            display.removeStyleName(this, CSS_GRAYED_OUT);
            display.removeStyleName(this, CSS_HIGHLIGHTED);
        }
            break;
        case GRAYED_OUT: {
            display.removeStyleName(this, CSS_SELECTED);
            display.removeStyleName(this, CSS_HIGHLIGHTED);
            display.addStyleName(this, CSS_GRAYED_OUT);
        }
            break;
        case SELECTED: {
            display.removeStyleName(this, CSS_GRAYED_OUT);
            display.removeStyleName(this, CSS_HIGHLIGHTED);
            display.addStyleName(this, CSS_SELECTED);
        }
            break;
        }
    }

    @Override
    protected void updateContent() {
        if (label == null) {
            return;
        }

        String description = (String) getResourceValue(SlotResolver.DESCRIPTION_SLOT);
        this.label.setText(description);
    }

}