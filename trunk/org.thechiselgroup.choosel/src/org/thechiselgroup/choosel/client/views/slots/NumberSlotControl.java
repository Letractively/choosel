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
package org.thechiselgroup.choosel.client.views.slots;

import java.util.Arrays;
import java.util.List;

import org.thechiselgroup.choosel.client.ui.widget.listbox.ExtendedListBox;
import org.thechiselgroup.choosel.client.ui.widget.listbox.ListBoxControl;
import org.thechiselgroup.choosel.client.util.ConversionException;
import org.thechiselgroup.choosel.client.util.Converter;
import org.thechiselgroup.choosel.client.util.NullConverter;
import org.thechiselgroup.choosel.client.util.math.AverageCalculation;
import org.thechiselgroup.choosel.client.util.math.Calculation;
import org.thechiselgroup.choosel.client.util.math.CountCalculation;
import org.thechiselgroup.choosel.client.util.math.MaxCalculation;
import org.thechiselgroup.choosel.client.util.math.MinCalculation;
import org.thechiselgroup.choosel.client.util.math.SumCalculation;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NumberSlotControl extends SlotControl {

    private VerticalPanel panel;

    private ListBoxControl<String> propertySelector;

    private ListBoxControl<Calculation> calculationSelector;

    private ChangeHandler changeHandler;

    private final SlotMappingConfiguration slotMappingConfiguration;

    public NumberSlotControl(Slot slot,
            final SlotMappingConfiguration slotMappingConfiguration) {

        super(slot);

        this.slotMappingConfiguration = slotMappingConfiguration;

        changeHandler = new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                String propertyName = propertySelector.getSelectedValue();
                Calculation calculation = calculationSelector
                        .getSelectedValue();

                slotMappingConfiguration.setMapping(getSlot(),
                        new CalculationResourceSetToValueResolver(propertyName,
                                calculation));
            }
        };

        Calculation[] calculations = new Calculation[] { new SumCalculation(),
                new CountCalculation(), new AverageCalculation(),
                new MinCalculation(), new MaxCalculation() };

        calculationSelector = new ListBoxControl<Calculation>(
                new ExtendedListBox(false),
                new Converter<Calculation, String>() {
                    @Override
                    public String convert(Calculation value)
                            throws ConversionException {
                        return value.getDescription();
                    }
                });
        calculationSelector.setValues(Arrays.asList(calculations));
        calculationSelector.setSelectedValue(calculations[0]);
        calculationSelector.setChangeHandler(changeHandler);

        propertySelector = new ListBoxControl<String>(
                new ExtendedListBox(false), new NullConverter<String>());
        propertySelector.setChangeHandler(changeHandler);

        panel = new VerticalPanel();
        panel.add(calculationSelector.asWidget());
        panel.add(propertySelector.asWidget());
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void updateOptions(List<String> properties) {
        propertySelector.setValues(properties);

        if (propertySelector.getSelectedValue() == null) {
            ResourceSetToValueResolver resolver = slotMappingConfiguration
                    .getResolver(getSlot());

            if (resolver instanceof CalculationResourceSetToValueResolver) {
                String property = ((CalculationResourceSetToValueResolver) resolver)
                        .getProperty();
                propertySelector.setSelectedValue(property);
            }
        }
    }
}