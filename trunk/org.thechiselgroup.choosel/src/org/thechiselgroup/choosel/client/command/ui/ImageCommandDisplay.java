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
package org.thechiselgroup.choosel.client.command.ui;

import static org.thechiselgroup.choosel.client.ui.IconURLBuilder.getIconUrl;

import org.thechiselgroup.choosel.client.ui.ImageButton;
import org.thechiselgroup.choosel.client.ui.WidgetAdaptable;
import org.thechiselgroup.choosel.client.ui.IconURLBuilder.IconType;

import com.google.gwt.user.client.ui.Widget;

// TODO factory + inline
public class ImageCommandDisplay extends ImageButton implements WidgetAdaptable {

    public ImageCommandDisplay(String name) {
        super(getIconUrl(name, IconType.NORMAL), getIconUrl(name,
                IconType.HIGHLIGHTED), getIconUrl(name, IconType.DISABLED));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

}