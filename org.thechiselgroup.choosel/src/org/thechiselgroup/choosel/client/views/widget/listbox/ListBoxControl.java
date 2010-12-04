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
package org.thechiselgroup.choosel.client.views.widget.listbox;

import java.util.List;

import org.thechiselgroup.choosel.client.ui.WidgetAdaptable;
import org.thechiselgroup.choosel.client.util.Converter;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class ListBoxControl<T> implements WidgetAdaptable {

    private ListBoxPresenter presenter;

    private ChangeHandler changeHandler;

    private HandlerRegistration changeHandlerRegistration;

    private List<T> values;

    private final Converter<T, String> formatter;

    // TODO refactor: use handler registration / deregistration for
    // changeHandler
    public ListBoxControl(ListBoxPresenter presenter,
            Converter<T, String> formatter) {

        assert formatter != null;
        assert presenter != null;

        this.formatter = formatter;
        this.presenter = presenter;

        this.presenter.setVisibleItemCount(1);

    }

    @Override
    public Widget asWidget() {
        return presenter.asWidget();
    }

    /**
     * @return selected value or <code>null</code>, if no value is selected.
     */
    public T getSelectedValue() {
        int selectedIndex = presenter.getSelectedIndex();

        if (selectedIndex == -1 || values == null) {
            return null;
        }

        return values.get(selectedIndex);
    }

    // TODO allow for multiple change handlers
    public void setChangeHandler(ChangeHandler changeHandler) {
        assert changeHandler != null;

        this.changeHandler = changeHandler;
        changeHandlerRegistration = presenter.addChangeHandler(changeHandler);
    }

    public void setSelectedValue(T t) {
        // XXX what if value is not part of values?
        presenter.setSelectedIndex(values.indexOf(t));
    }

    public void setValues(List<T> values) {
        assert values != null;

        T selectedValue = getSelectedValue();

        if (changeHandlerRegistration != null) {
            changeHandlerRegistration.removeHandler();
            changeHandlerRegistration = null;
        }

        this.values = values;
        presenter.clear();
        for (T value : values) {
            presenter.addItem(formatter.convert(value));
        }
        setSelectedValue(selectedValue);

        if (changeHandler != null) {
            changeHandlerRegistration = presenter
                    .addChangeHandler(changeHandler);
        }
    }

}