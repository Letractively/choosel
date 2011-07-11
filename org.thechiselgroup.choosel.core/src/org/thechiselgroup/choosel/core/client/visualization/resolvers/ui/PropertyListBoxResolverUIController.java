/*******************************************************************************
 * Copyright (C) 2011 Lars Grammel 
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
package org.thechiselgroup.choosel.core.client.visualization.resolvers.ui;

import java.util.ArrayList;
import java.util.List;

import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.ui.widget.listbox.ExtendedListBox;
import org.thechiselgroup.choosel.core.client.ui.widget.listbox.ListBoxControl;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.util.transform.NullTransformer;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.ManagedVisualItemValueResolver;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.ManagedSlotMapping;
import org.thechiselgroup.choosel.core.client.visualization.resolvers.PropertyDependantViewItemValueResolver;
import org.thechiselgroup.choosel.core.client.visualization.resolvers.managed.PropertyDependantVisualItemValueResolverFactory;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;

public abstract class PropertyListBoxResolverUIController implements
        ViewItemValueResolverUIController {

    /**
     * This method will return the properties that it would be ok to select from
     * the view items. All of the resources must contain that same property
     */
    private static List<String> getSharedPropertiesFromViewItems(
            LightweightCollection<VisualItem> viewItems) {
        List<String> properties = new ArrayList<String>();

        // intialize properties to be the ones in the first resource
        VisualItem firstItem = viewItems.getFirstElement();
        Resource firstResource = firstItem.getResources().iterator().next();
        properties.addAll(firstResource.getProperties().keySet());

        // only keep properties that are shared by all of the resource
        for (VisualItem viewItem : viewItems) {
            for (Resource resource : viewItem.getResources()) {
                properties.retainAll(resource.getProperties().keySet());
            }
        }
        return properties;
    }

    private final PropertyDependantVisualItemValueResolverFactory resolverFactory;

    private ManagedSlotMapping uiModel;

    private List<String> properties;

    private ListBoxControl<String> selector;

    /**
     * This change handler will automatically synchronize the resolvers changes
     * with the property that is selected in the UI
     */
    private ChangeHandler propertySelectChangeHandler = new ChangeHandler() {

        // this change represents that the resolver has changed without the
        // factory changing
        @Override
        public void onChange(ChangeEvent event) {
            String selectedProperty = selector.getSelectedValue();
            uiModel.setResolver(resolverFactory.create(selectedProperty));
        }
    };

    public PropertyListBoxResolverUIController(
            PropertyDependantVisualItemValueResolverFactory resolverFactory,
            ManagedSlotMapping uiModel,
            LightweightCollection<VisualItem> viewItems) {
        this(resolverFactory, uiModel, viewItems,
                getSharedPropertiesFromViewItems(viewItems).get(0));
    }

    public PropertyListBoxResolverUIController(
            PropertyDependantVisualItemValueResolverFactory resolverFactory,
            ManagedSlotMapping uiModel,
            LightweightCollection<VisualItem> viewItems, String property) {
        this.uiModel = uiModel;
        this.resolverFactory = resolverFactory;

        selector = new ListBoxControl<String>(new ExtendedListBox(false),
                new NullTransformer<String>());
        selector.setChangeHandler(propertySelectChangeHandler);

        setProperties(getSharedPropertiesFromViewItems(viewItems));
        selector.setSelectedValue(property);
    }

    /**
     * @return A Widget representation of this UI, as created by the controller.
     */
    @Override
    public Widget asWidget() {

        // if properties is not set, then this UI does not make sense
        // the resolver should be unapplicable, and this should never get called
        PropertyDependantViewItemValueResolver resolver = getCurrentResolverFromUIModel();
        assert properties.contains(resolver.getProperty());

        selector.setValues(properties);
        selector.setSelectedValue(resolver.getProperty());
        return selector.asWidget();
    }

    private PropertyDependantViewItemValueResolver getCurrentResolverFromUIModel() {
        ManagedVisualItemValueResolver currentResolver = uiModel
                .getCurrentResolver();

        // XXX cannot cast because it is actually the Decorator that we are
        // getting, and the delegate is the thing that is PropertyDependant
        assert currentResolver instanceof PropertyDependantViewItemValueResolver;
        return (PropertyDependantViewItemValueResolver) currentResolver;
    }

    /**
     * this method will set the properties of both this UI Controller and the
     * properties of the Selector Widget
     */
    private void setProperties(List<String> properties) {
        assert properties != null;
        this.properties = properties;
        this.selector.setValues(properties);
    }

    /**
     * This method assumes that it will be possible to find some way of creating
     * a valid resolver given the new work items. If it isn't, the factory
     * should say that it is not applicable, and so we should never get here
     */
    @Override
    public void update(LightweightCollection<VisualItem> viewItems) {
        setProperties(getSharedPropertiesFromViewItems(viewItems));

        // the new view items can not be resolved by the current resolver, and
        // the property field should be set to something that is valid
        if (!properties.contains(getCurrentResolverFromUIModel().getProperty())) {
            uiModel.setResolver(resolverFactory.create(properties.get(0)));
            selector.setSelectedValue(properties.get(0));
        }
    }
}