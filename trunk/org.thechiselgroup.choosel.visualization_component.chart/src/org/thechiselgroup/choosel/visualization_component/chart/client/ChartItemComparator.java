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
package org.thechiselgroup.choosel.visualization_component.chart.client;

import java.util.Comparator;

import org.thechiselgroup.choosel.core.client.views.slots.Slot;

public class ChartItemComparator implements Comparator<ChartItem> {

    // for test access
    public static int compare(double value1, double value2) {
        if (value1 > value2) {
            return 1;
        }

        if (value1 < value2) {
            return -1;
        }

        return 0;
    }

    private Slot slot;

    public ChartItemComparator(Slot slot) {
        assert slot != null;
        this.slot = slot;
    }

    @Override
    public int compare(ChartItem o1, ChartItem o2) {
        assert o1 != null;
        assert o2 != null;

        double v1 = o1.getSlotValueAsDouble(slot);
        double v2 = o2.getSlotValueAsDouble(slot);
        return compare(v1, v2);
    }

}