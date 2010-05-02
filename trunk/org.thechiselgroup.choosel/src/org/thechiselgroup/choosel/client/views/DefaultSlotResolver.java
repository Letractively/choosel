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

import java.util.List;

import org.thechiselgroup.choosel.client.resolver.FixedValuePropertyValueResolver;
import org.thechiselgroup.choosel.client.resolver.PropertyValueResolver;
import org.thechiselgroup.choosel.client.resolver.SimplePropertyValueResolver;

public abstract class DefaultSlotResolver implements SlotResolver {

    private static final String[] COLORS = new String[] { "#6495ed", "#b22222" };

    @Override
    public PropertyValueResolver createColorSlotResolver(String category,
	    List<Layer> layers) {

	String color = COLORS[layers.size()];
	return new FixedValuePropertyValueResolver(color);
    }

    @Override
    public PropertyValueResolver createDateSlotResolver(String type) {
	return new SimplePropertyValueResolver("date");
    }

    @Override
    public PropertyValueResolver createGraphLabelSlotResolver(String category) {
	return createDescriptionSlotResolver(category);
    }

    @Override
    public PropertyValueResolver createGraphNodeBackgroundColorResolver(
	    String category) {

	return new FixedValuePropertyValueResolver("#DAE5F3");
    }

    @Override
    public PropertyValueResolver createGraphNodeBorderColorResolver(
	    String category) {

	return new FixedValuePropertyValueResolver("#AFC6E5");
    }

    @Override
    public PropertyValueResolver createLabelSlotResolver(String category) {
	return new FixedValuePropertyValueResolver("");
    }

    @Override
    public PropertyValueResolver createLocationSlotResolver(String category) {
	return new SimplePropertyValueResolver("location");
    }
}
