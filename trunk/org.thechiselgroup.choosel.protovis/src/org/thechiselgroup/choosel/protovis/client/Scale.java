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
package org.thechiselgroup.choosel.protovis.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Wrapper for
 * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Scale.html">pv.Scale</a></code>
 * .
 * 
 * @author Bradley Blashko
 * @author Lars Grammel
 */
// @formatter:off
public class Scale extends JavaScriptObject {

    public final static native LinearScale linear() /*-{
        return $wnd.pv.Scale.linear();
    }-*/;

    public final static native OrdinalScale ordinal() /*-{
        return $wnd.pv.Scale.ordinal();
    }-*/;

    public final static native LinearScale linear(double from, double to) /*-{
        return $wnd.pv.Scale.linear(from, to);
    }-*/;

    protected Scale() {
    }
    
    /**
     * Scales are functions.
     */
    public final native double f(double value) /*-{
        return this(value);
    }-*/;
    
}
// @formatter:on
