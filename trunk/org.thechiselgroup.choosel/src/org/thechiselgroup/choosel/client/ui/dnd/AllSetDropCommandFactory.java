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
package org.thechiselgroup.choosel.client.ui.dnd;

import org.thechiselgroup.choosel.client.command.UndoableCommand;
import org.thechiselgroup.choosel.client.resources.command.AddResourcesToViewCommand;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatar;
import org.thechiselgroup.choosel.client.views.View;
import org.thechiselgroup.choosel.client.views.ViewAccessor;

public class AllSetDropCommandFactory implements
        ResourceSetAvatarDropCommandFactory {

    private ResourceSetAvatar targetDragAvatar;

    private ViewAccessor viewAccessor;

    public AllSetDropCommandFactory(ResourceSetAvatar targetDragAvatar,
            ViewAccessor viewAccessor) {

        assert targetDragAvatar != null;
        assert viewAccessor != null;

        this.viewAccessor = viewAccessor;
        this.targetDragAvatar = targetDragAvatar;
    }

    @Override
    public boolean canDrop(ResourceSetAvatar dragAvatar) {
        assert dragAvatar != null;
        return !(isSameFromView(dragAvatar) || targetContainsAllResources(dragAvatar));
    }

    @Override
    public UndoableCommand createCommand(ResourceSetAvatar dragAvatar) {
        assert dragAvatar != null;

        return new AddResourcesToViewCommand(getTargetView(), dragAvatar
                .getResourceSet());
    }

    private View getTargetView() {
        return viewAccessor.findView(targetDragAvatar);
    }

    private boolean isSameFromView(ResourceSetAvatar dragAvatar) {
        return getTargetView() == viewAccessor.findView(dragAvatar);
    }

    private boolean targetContainsAllResources(ResourceSetAvatar dragAvatar) {
        return getTargetView().containsResources(dragAvatar.getResourceSet());
    }

}