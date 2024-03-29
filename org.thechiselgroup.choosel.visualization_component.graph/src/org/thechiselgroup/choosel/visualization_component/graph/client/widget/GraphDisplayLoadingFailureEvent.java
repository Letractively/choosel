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
package org.thechiselgroup.choosel.visualization_component.graph.client.widget;

import com.google.gwt.event.shared.GwtEvent;

public class GraphDisplayLoadingFailureEvent extends
        GwtEvent<GraphDisplayLoadingFailureEventHandler> {

    public static final Type<GraphDisplayLoadingFailureEventHandler> TYPE = new Type<GraphDisplayLoadingFailureEventHandler>();

    private GraphDisplay graphDisplay;

    private final Exception exception;

    public GraphDisplayLoadingFailureEvent(GraphDisplay graphDisplay,
            Exception exception) {
        this.graphDisplay = graphDisplay;
        this.exception = exception;
    }

    @Override
    protected void dispatch(GraphDisplayLoadingFailureEventHandler handler) {
        handler.onLoadingFailure(this);
    }

    @Override
    public Type<GraphDisplayLoadingFailureEventHandler> getAssociatedType() {
        return TYPE;
    }

    public Exception getException() {
        return exception;
    }

    public GraphDisplay getGraphDisplay() {
        return graphDisplay;
    }

}