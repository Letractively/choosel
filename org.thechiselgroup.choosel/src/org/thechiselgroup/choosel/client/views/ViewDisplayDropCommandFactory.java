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

import org.thechiselgroup.choosel.client.command.UndoableCommand;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.UnmodifiableResourceSet;
import org.thechiselgroup.choosel.client.resources.command.AddResourceSetToViewCommand;
import org.thechiselgroup.choosel.client.resources.command.AddResourcesToViewCommand;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatar;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatarType;
import org.thechiselgroup.choosel.client.ui.dnd.ResourceSetAvatarDropCommandFactory;

import com.google.gwt.user.client.ui.Widget;

public class ViewDisplayDropCommandFactory implements
        ResourceSetAvatarDropCommandFactory {

    private final Widget dropTarget;

    private final ViewAccessor viewAccessor;

    public ViewDisplayDropCommandFactory(Widget dropTarget,
            ViewAccessor viewAccessor) {

        assert viewAccessor != null;
        assert dropTarget != null;

        this.viewAccessor = viewAccessor;
        this.dropTarget = dropTarget;
    }

    @Override
    public boolean canDrop(ResourceSetAvatar dragAvatar) {
        assert dragAvatar != null;
        return !isFromSameView(dragAvatar) && !isAlreadyContained(dragAvatar);
    }

    @Override
    public UndoableCommand createCommand(ResourceSetAvatar dragAvatar) {
        assert dragAvatar != null;

        ResourceSet addedResources = dragAvatar.getResourceSet();

        assert addedResources != null;

        if (!addedResources.hasLabel()
                && dragAvatar.getType() == ResourceSetAvatarType.SET) {

            return new AddResourcesToViewCommand(getTargetView(),
                    addedResources);
        }

        if (dragAvatar.getType() != ResourceSetAvatarType.SET) {
            addedResources = new UnmodifiableResourceSet(addedResources);
        }

        return new AddResourceSetToViewCommand(getTargetView(), addedResources);
    }

    private View getTargetView() {
        return viewAccessor.findView(dropTarget);
    }

    private boolean isAlreadyContained(ResourceSetAvatar dragAvatar) {
        ResourceSet resources = dragAvatar.getResourceSet();

        if (!resources.hasLabel()
                && dragAvatar.getType() == ResourceSetAvatarType.SET) {
            return getTargetView().containsResources(resources);
        }

        return getTargetView().containsResourceSet(resources);
    }

    private boolean isFromSameView(ResourceSetAvatar dragAvatar) {
        return getTargetView() == viewAccessor.findView(dragAvatar);
    }
}