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

import org.thechiselgroup.choosel.protovis.client.functions.PVDoubleFunctionWithoutThis;
import org.thechiselgroup.choosel.protovis.client.functions.PVFunctionWithoutThis;
import org.thechiselgroup.choosel.protovis.client.util.JsArrayGeneric;
import org.thechiselgroup.choosel.protovis.client.util.JsUtils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsDate;

/**
 * Wrapper for
 * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Scale.linear.html">pv.Scale.linear</a></code>
 * .
 * 
 * @author Bradley Blashko
 * @author Lars Grammel
 */
public class PVLinearScale extends PVScale {

    protected PVLinearScale() {
    }

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Scale.quantitative.html#domain">pv.Scale.quantitative.domain()</a></code>
     * .
     */
    public final native PVLinearScale domain(double min, double max) /*-{
        return this.domain(min, max);
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Scale.quantitative.html#domain">pv.Scale.quantitative.domain()</a></code>
     * .
     */
    public final native <S> PVLinearScale domain(JsArrayGeneric<S> array,
            PVDoubleFunctionWithoutThis<S> f) /*-{
        return this.domain(array, @org.thechiselgroup.choosel.protovis.client.functions.JsFunctionUtils::toJavaScriptFunction(Lorg/thechiselgroup/choosel/protovis/client/functions/PVDoubleFunctionWithoutThis;)(f));
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Scale.quantitative.html#domain">pv.Scale.quantitative.domain()</a></code>
     * .
     */
    public final native <S> PVLinearScale domain(JsArrayGeneric<S> array,
            PVDoubleFunctionWithoutThis<S> min,
            PVDoubleFunctionWithoutThis<S> max) /*-{
        return this.domain(array, 
        @org.thechiselgroup.choosel.protovis.client.functions.JsFunctionUtils::toJavaScriptFunction(Lorg/thechiselgroup/choosel/protovis/client/functions/PVDoubleFunctionWithoutThis;)(min), 
        @org.thechiselgroup.choosel.protovis.client.functions.JsFunctionUtils::toJavaScriptFunction(Lorg/thechiselgroup/choosel/protovis/client/functions/PVDoubleFunctionWithoutThis;)(max));
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Scale.quantitative.html#domain">pv.Scale.quantitative.domain()</a></code>
     * .
     */
    public final native <S> PVLinearScale domain(JsArrayGeneric<S> array,
            PVFunctionWithoutThis<S, JsDate> f) /*-{
        return this.domain(array, @org.thechiselgroup.choosel.protovis.client.functions.JsFunctionUtils::toJavaScriptFunction(Lorg/thechiselgroup/choosel/protovis/client/functions/PVFunctionWithoutThis;)(f));
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Scale.quantitative.html#domain">pv.Scale.quantitative.domain()</a></code>
     * .
     */
    public final <S> PVLinearScale domain(S[] array,
            PVDoubleFunctionWithoutThis<S> f) {
        return this.domain(JsUtils.toJsArrayGeneric(array), f);
    }

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Scale.quantitative.html#domain">pv.Scale.quantitative.domain()</a></code>
     * .
     */
    public final <S> PVLinearScale domain(S[] array,
            PVDoubleFunctionWithoutThis<S> min,
            PVDoubleFunctionWithoutThis<S> max) {

        return this.domain(JsUtils.toJsArrayGeneric(array), min, max);
    }

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Scale.quantitative.html#domain">pv.Scale.quantitative.domain()</a></code>
     * .
     */
    public final <S> PVLinearScale domain(S[] array,
            PVFunctionWithoutThis<S, JsDate> f) {
        return this.domain(JsUtils.toJsArrayGeneric(array), f);
    }

    public final native PVLinearScale nice() /*-{
        return this.nice();
    }-*/;

    public final native PVLinearScale range(double min, double max) /*-{
        return this.range(min, max);
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Scale.quantitative.html#tickFormat">tickFormat()</a></code>
     * .
     */
    public final native JavaScriptObject tickFormat() /*-{
        return this.tickFormat;
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Scale.quantitative.html#tickFormat">tickFormat()</a></code>
     * .
     */
    public final native String tickFormat(int tick) /*-{
        return this.tickFormat(tick);
    }-*/;

    public final native JavaScriptObject ticks() /*-{
        return this.ticks();
    }-*/;

    public final native JavaScriptObject ticks(int ticks) /*-{
        return this.ticks(ticks);
    }-*/;

}