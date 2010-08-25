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
package org.thechiselgroup.choosel.client.ui.widget.chart;

import org.thechiselgroup.choosel.client.ui.widget.chart.protovis.ProtovisFunctionString;
import org.thechiselgroup.choosel.client.views.chart.ChartItem;

public class BenchmarkingProtovisFunctionString implements
        ProtovisFunctionString {

    private ProtovisFunctionString delegate;

    public BenchmarkingProtovisFunctionString(ProtovisFunctionString delegate) {
        this.delegate = delegate;
    }

    @Override
    public String f(ChartItem value, int i) {
        long startTime = System.currentTimeMillis();
        try {
            return delegate.f(value, i);
        } finally {
            System.err.println(delegate.toString() + " took "
                    + (System.currentTimeMillis() - startTime) + " ms -- "
                    + System.currentTimeMillis());
        }
    }

}