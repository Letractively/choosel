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
package org.thechiselgroup.choosel.client.ui.widget.protovis;

import com.google.gwt.core.client.JavaScriptObject;

// @formatter:off
/**
 * 
 * @author Bradley Blashko
 * @author Lars Grammel 
 */
public class Bar extends Mark {

    public static native Bar createBar() /*-{
        return $wnd.pv.Bar;
    }-*/;

    protected Bar() {
    }

    public final native <T extends Mark> T add(T mark) /*-{
        return this.add(mark);
    }-*/;

    public final native Bar anchor(String anchor) /*-{
        return this.anchor(anchor);
    }-*/;

    public final native Bar bottom(double bottom) /*-{
        return this.bottom(bottom);
    }-*/;

    public final native Bar bottom(DoubleFunction<?> f) /*-{
        return this.bottom(@org.thechiselgroup.choosel.client.ui.widget.protovis.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/client/ui/widget/protovis/DoubleFunction;)(this,f));
    }-*/;

    public final native Bar childIndex(int childIndex) /*-{
        return this.childIndex(childIndex);
    }-*/;

    public final native Bar cursor(String cursor) /*-{
        return this.cursor(cursor);
    }-*/;

    public final native Bar data(JavaScriptObject data) /*-{
        return this.data(data);
    }-*/;

    public final native Bar def(String name) /*-{
        return this.def(name);
    }-*/;

    // TODO Likely needs some fixing
    public final native Bar def(String name, DoubleFunction<?> f) /*-{
        return this.def(name, @org.thechiselgroup.choosel.client.ui.widget.protovis.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/client/ui/widget/protovis/DoubleFunction;)(this,f));
    }-*/;

    public final native Bar def(String name, String constant) /*-{
        return this.def(name, constant);
    }-*/;

    public final native Bar defaults(Mark mark) /*-{
        return this.defaults(mark);
    }-*/;

    public final native Bar event(String eventType, ProtovisEventHandler handler) /*-{
        return this.event(eventType, @org.thechiselgroup.choosel.client.ui.widget.protovis.Mark::registerEvent(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/client/ui/widget/protovis/ProtovisEventHandler;)(this, handler));
    }-*/;

    public final native Bar events(String events) /*-{
        return this.events(events);
    }-*/;

    public final native Bar fillStyle(StringFunction<?> f) /*-{
        return this.fillStyle(@org.thechiselgroup.choosel.client.ui.widget.protovis.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/client/ui/widget/protovis/StringFunction;)(this,f));
    }-*/;

    public final native Bar fillStyle(String colour) /*-{
        return this.fillStyle(colour);
    }-*/;

    public final native Bar height(double height) /*-{
        return this.height(height);
    }-*/;

    public final native Bar height(DoubleFunction<?> f) /*-{
        return this.height(@org.thechiselgroup.choosel.client.ui.widget.protovis.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/client/ui/widget/protovis/DoubleFunction;)(this,f));
    }-*/;

    public final native Bar index(double index) /*-{
        return this.index(index);
    }-*/;

    public final native Bar left(double left) /*-{
        return this.left(left);
    }-*/;

    public final native Bar left(DoubleFunction<?> f) /*-{
        return this.left(@org.thechiselgroup.choosel.client.ui.widget.protovis.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/client/ui/widget/protovis/DoubleFunction;)(this,f));
    }-*/;

    public final native Bar lineWidth(double lineWidth) /*-{
        return this.lineWidth(lineWidth);
    }-*/;

    public final native Bar parent(Panel panel) /*-{
        return this.parent(panel);
    }-*/;

    public final native Bar proto(Mark mark) /*-{
        return this.proto(mark);
    }-*/;

    public final native void render() /*-{
        return this.render();
    }-*/;

    public final native Bar reverse(boolean reverse) /*-{
        return this.reverse(reverse);
    }-*/;

    public final native Bar right(double right) /*-{
        return this.right(right);
    }-*/;

    public final native Bar right(DoubleFunction<?> f) /*-{
        return this.right(@org.thechiselgroup.choosel.client.ui.widget.protovis.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/client/ui/widget/protovis/DoubleFunction;)(this,f));
    }-*/;

    public final native Bar root(Panel panel) /*-{
        return this.root(panel);
    }-*/;

    public final native Bar scale(double scale) /*-{
        return this.scale(scale);
    }-*/;

    public final native Bar strokeStyle(StringFunction<?> f) /*-{
        return this.fillStyle(@org.thechiselgroup.choosel.client.ui.widget.protovis.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/client/ui/widget/protovis/StringFunction;)(this,f));
    }-*/;

    public final native Bar strokeStyle(String strokeStyle) /*-{
        return this.strokeStyle(strokeStyle);
    }-*/;

    public final native Bar title(String title) /*-{
        return this.title(title);
    }-*/;

    public final native Bar top(double top) /*-{
        return this.top(top);
    }-*/;

    public final native Bar top(DoubleFunction<?> f) /*-{
        return this.top(@org.thechiselgroup.choosel.client.ui.widget.protovis.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/client/ui/widget/protovis/DoubleFunction;)(this,f));
    }-*/;

    public final native Bar type(String type) /*-{
        return this.type(type);
    }-*/;

    public final native Bar visible(boolean visible) /*-{
        return this.visible(visible);
    }-*/;
    
    public final native Bar visible(BooleanFunction<?> f) /*-{
        return this.visible(@org.thechiselgroup.choosel.client.ui.widget.protovis.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/client/ui/widget/protovis/BooleanFunction;)(this,f));
    }-*/;

    public final native Bar width(double width) /*-{
        return this.width(width);
    }-*/;
    
    public final native Bar width(DoubleFunction<?> f) /*-{
        return this.width(@org.thechiselgroup.choosel.client.ui.widget.protovis.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/client/ui/widget/protovis/DoubleFunction;)(this,f));
    }-*/;

}
// @formatter:on