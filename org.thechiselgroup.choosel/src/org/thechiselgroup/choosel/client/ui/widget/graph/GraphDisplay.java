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
package org.thechiselgroup.choosel.client.ui.widget.graph;

import java.util.Collection;

import org.thechiselgroup.choosel.client.geometry.Point;
import org.thechiselgroup.choosel.client.ui.WidgetAdaptable;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;

public interface GraphDisplay extends WidgetAdaptable {

    String ARC_COLOR = "normalLineColor";

    String ARC_STYLE = "normalLineStyle";

    String ARC_STYLE_DASHED = "dashed";

    String ARC_STYLE_SOLID = "solid";

    // FIXME these a flexviz specific, use generic ones & translate in
    // graphwidget

    String NODE_BACKGROUND_COLOR = "normalBackgroundColor";

    String NODE_BORDER_COLOR = "normalBorderColor";

    String NODE_FONT_COLOR = "normalColor";

    String NODE_FONT_WEIGHT = "normalFontWeight";

    String NODE_FONT_WEIGHT_BOLD = "bold";

    String NODE_FONT_WEIGHT_NORMAL = "normal";

    void addArc(Arc arc);

    <T extends EventHandler> HandlerRegistration addEventHandler(Type<T> type,
            T handler);

    HandlerRegistration addGraphWidgetReadyHandler(
            GraphWidgetReadyHandler handler);

    void addNode(Node node);

    void addNodeMenuItemHandler(String menuLabel,
            NodeMenuItemClickedHandler handler, String nodeClass);

    void animateMoveTo(Node node, Point targetLocation);

    boolean containsArc(String arcId);

    boolean containsNode(String nodeId);

    Arc getArc(String arcId);

    Point getLocation(Node node);

    Node getNode(String nodeId);

    void layOut();

    void layOutNodes(Collection<Node> nodes);

    void removeArc(Arc arc);

    void removeNode(Node node);

    void setArcStyle(Arc arc, String styleProperty, String styleValue);

    void setLocation(Node node, Point location);

    void setNodeStyle(Node node, String styleProperty, String styleValue);

}