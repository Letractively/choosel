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

import org.thechiselgroup.choosel.protovis.client.functions.PVDoubleFunction;
import org.thechiselgroup.choosel.protovis.client.functions.PVStringFunction;
import org.thechiselgroup.choosel.protovis.client.functions.PVStringFunctionDoubleArg;
import org.thechiselgroup.choosel.protovis.client.functions.PVStringFunctionIntArg;

/**
 * Wrapper for
 * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Rule.html">pv.Rule</a></code>
 * .
 * 
 * @author Bradley Blashko
 * @author Lars Grammel
 */
public class PVRule extends PVAbstractMark<PVRule> {

    protected PVRule() {
    }

    public final native PVRule height(double height) /*-{
        return this.height(height);
    }-*/;

    public final native PVRule height(PVDoubleFunction<PVRule, ?> f) /*-{
        return this.height(@org.thechiselgroup.choosel.protovis.client.functions.JsFunctionUtils::toJavaScriptFunction(Lorg/thechiselgroup/choosel/protovis/client/functions/PVDoubleFunction;)(f));
    }-*/;

    public final native PVRule lineWidth(double lineWidth) /*-{
        return this.lineWidth(lineWidth);
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Rule.html#strokeStyle">strokeStyle()</a></code>
     * .
     */
    public final native PVRule strokeStyle(String strokeStyle) /*-{
        return this.strokeStyle(strokeStyle);
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Rule.html#strokeStyle">strokeStyle()</a></code>
     * .
     */
    public final native PVRule strokeStyle(PVStringFunction<PVRule, ?> f) /*-{
        return this.strokeStyle(@org.thechiselgroup.choosel.protovis.client.functions.JsFunctionUtils::toJavaScriptFunction(Lorg/thechiselgroup/choosel/protovis/client/functions/PVStringFunction;)(f));
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Rule.html#strokeStyle">strokeStyle()</a></code>
     * .
     */
    public final native PVRule strokeStyle(PVStringFunctionIntArg<PVRule> f) /*-{
        return this.strokeStyle(@org.thechiselgroup.choosel.protovis.client.functions.JsFunctionUtils::toJavaScriptFunction(Lorg/thechiselgroup/choosel/protovis/client/functions/PVStringFunctionIntArg;)(f));
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Rule.html#strokeStyle">strokeStyle()</a></code>
     * .
     */
    public final native PVRule strokeStyle(PVStringFunctionDoubleArg<PVRule> f) /*-{
        return this.strokeStyle(@org.thechiselgroup.choosel.protovis.client.functions.JsFunctionUtils::toJavaScriptFunction(Lorg/thechiselgroup/choosel/protovis/client/functions/PVStringFunctionDoubleArg;)(f));
    }-*/;

    public final native PVRule width(double width) /*-{
        return this.width(width);
    }-*/;

    public final native PVRule width(PVDoubleFunction<PVRule, ?> f) /*-{
        return this.width(@org.thechiselgroup.choosel.protovis.client.functions.JsFunctionUtils::toJavaScriptFunction(Lorg/thechiselgroup/choosel/protovis/client/functions/PVDoubleFunction;)(f));
    }-*/;

}