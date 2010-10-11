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
package org.thechiselgroup.chooselexample.client;

import org.thechiselgroup.choosel.client.resolver.ResourceSetToValueResolver;
import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ResourceSetFactory;
import org.thechiselgroup.choosel.client.resources.ui.DetailsWidgetHelper;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatar;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatarType;
import org.thechiselgroup.choosel.client.ui.CSS;
import org.thechiselgroup.choosel.client.ui.dnd.ResourceSetAvatarDragController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ChooselExampleDetailsWidgetHelper extends DetailsWidgetHelper {

	@Inject
	public ChooselExampleDetailsWidgetHelper(
			ResourceSetFactory resourceSetFactory,
			ResourceSetAvatarFactory dragAvatarFactory,
			ResourceSetAvatarDragController dragController) {
		super(resourceSetFactory, dragAvatarFactory, dragController);
	}

	@Override
	public Widget createDetailsWidget(ResourceSet resources,
			ResourceSetToValueResolver resolver) {

		VerticalPanel verticalPanel = GWT.create(VerticalPanel.class);

		for (Resource resource : resources) {
			// FIXME use generic way to put in custom widgets
			if (resource.getUri().startsWith("tsunami")) {
				ResourceSetAvatar avatar = new ResourceSetAvatar("Tsunami",
						"avatar-resourceSet", resources,
						ResourceSetAvatarType.SET);
				avatar.setEnabled(true);
				dragController.setDraggable(avatar, true);
				verticalPanel.add(avatar);

				String date = resource.getValue("date").toString();
				String evaluation = resource.getValue("evaluation").toString();
				if (evaluation.length() > 250) {
					int indexOfSpace = evaluation.indexOf(' ', 249);
					if (indexOfSpace != -1) {
						evaluation = evaluation.substring(0, indexOfSpace)
								+ "...";
					}
				}
				evaluation = evaluation.replaceAll("<br>", "");

				HTML html = GWT.create(HTML.class);
				CSS.setWidth(html, 280);
				html.setHTML("<b>" + date + "</b><br/>" + evaluation);
				verticalPanel.add(html);
			} else if (resource.getUri().startsWith("earthquake")) {
				ResourceSetAvatar avatar = new ResourceSetAvatar("Earthquake",
						"avatar-resourceSet", resources,
						ResourceSetAvatarType.SET);
				avatar.setEnabled(true);
				dragController.setDraggable(avatar, true);
				verticalPanel.add(avatar);

				String description = resource.getValue("description")
						.toString();
				String details = resource.getValue("details").toString();
				HTML html = GWT.create(HTML.class);
				CSS.setWidth(html, 280);
				html.setHTML("<b>" + description + "</b><br/>" + details);
				verticalPanel.add(html);
			} else {
				verticalPanel.add(avatarFactory.createAvatar(resources));

				String value = "";
				HTML html = GWT.create(HTML.class);
				html.setHTML(value);
				verticalPanel.add(html);
			}
		}

		return verticalPanel;
	}
}