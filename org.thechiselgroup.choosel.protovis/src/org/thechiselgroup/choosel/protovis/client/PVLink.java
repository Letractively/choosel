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
 * 
 * @author Lars Grammel
 */
public class PVLink extends JavaScriptObject {

    public static native PVLink create(int source, int target, double value) /*-{
        return {
        'source': source,
        'target': target,
        'value': value
        };
    }-*/;

    public static PVLink create(Link link) {
        return create(link.getSource(), link.getTarget(), link.getValue());
    }

    protected PVLink() {
    }

    public final native double linkValue() /*-{
        return this.linkValue;
    }-*/;

    public final native PVLink linkValue(double linkValue) /*-{
        return this.linkValue(linkValue);
    }-*/;

    public final native int source() /*-{
        return this.source;
    }-*/;

    public final native PVNode sourceNode() /*-{
        return this.sourceNode;
    }-*/;

    public final native int target() /*-{
        return this.target;
    }-*/;

    public final native PVNode targetNode() /*-{
        return this.targetNode;
    }-*/;

}