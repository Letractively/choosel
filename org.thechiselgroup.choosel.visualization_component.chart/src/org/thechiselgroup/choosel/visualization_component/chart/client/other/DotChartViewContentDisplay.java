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
package org.thechiselgroup.choosel.visualization_component.chart.client.other;

import org.thechiselgroup.choosel.core.client.ui.Colors;
import org.thechiselgroup.choosel.core.client.visualization.model.Slot;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem.Subset;
import org.thechiselgroup.choosel.protovis.client.PV;
import org.thechiselgroup.choosel.protovis.client.PVAlignment;
import org.thechiselgroup.choosel.protovis.client.PVDot;
import org.thechiselgroup.choosel.protovis.client.PVEventHandler;
import org.thechiselgroup.choosel.protovis.client.PVLinearScale;
import org.thechiselgroup.choosel.protovis.client.PVMark;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsArgs;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsDoubleFunction;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsStringFunction;
import org.thechiselgroup.choosel.visualization_component.chart.client.ChartViewContentDisplay;
import org.thechiselgroup.choosel.visualization_component.chart.client.barchart.BarChart;
import org.thechiselgroup.choosel.visualization_component.chart.client.functions.TickFormatFunction;

public class DotChartViewContentDisplay extends ChartViewContentDisplay {

    private static final int BORDER_HEIGHT = 20;

    private static final int BORDER_WIDTH = 20;

    private static final String GRIDLINE_SCALE_COLOR = Colors.GRAY_1;

    private static final String AXIS_SCALE_COLOR = Colors.GRAY_2;

    private double[] dotCounts;

    protected int chartHeight;

    protected int chartWidth;

    private JsDoubleFunction dotLeft = new JsDoubleFunction() {
        @Override
        public double f(JsArgs args) {
            return calculateDotX(args.<PVMark> getThis().index());
        }
    };

    protected double maxChartItemValue;

    private JsDoubleFunction dotBottom = new JsDoubleFunction() {
        @Override
        public double f(JsArgs args) {
            return calculateDotBottom(args.<PVMark> getThis().index());
        }
    };

    private int baselineLabelBottom = -15;

    private JsStringFunction scaleStrokeStyle = new JsStringFunction() {
        @Override
        public String f(JsArgs args) {
            int value = args.getInt();
            return value == 0 ? AXIS_SCALE_COLOR : GRIDLINE_SCALE_COLOR;
        }
    };

    private PVDot regularDot;

    private String baselineLabelTextAlign = PVAlignment.CENTER;

    private JsStringFunction baselineLabelText = new JsStringFunction() {
        @Override
        public String f(JsArgs args) {
            VisualItem value = args.getObject();
            // TODO own slot
            return value.getValue(BarChart.BAR_LABEL).toString();
        }
    };

    protected PVLinearScale scale;

    @Override
    protected void beforeRender() {
        super.beforeRender();

        calculateMaximumChartItemValue();

        if (visualItemsJsArray.length() == 0) {
            return;
        }

        dotCounts = new double[visualItemsJsArray.length()];

        for (int i = 0; i < visualItemsJsArray.length(); i++) {
            dotCounts[i] = visualItemsJsArray.get(i).getValueAsDouble(
                    BarChart.BAR_LENGTH);
        }
    }

    @Override
    public void buildChart() {
        assert visualItemsJsArray.length() >= 1;

        calculateChartVariables();
        setChartParameters();

        calculateMaximumChartItemValue();
        PVLinearScale scale = PV.Scale.linear(0, maxChartItemValue).range(0,
                chartHeight);
        drawScales(scale);
        drawSelectionBox();
        drawDot();

    }

    private void calculateChartVariables() {
        chartWidth = width - BORDER_WIDTH * 2;
        chartHeight = height - BORDER_HEIGHT * 2;
    }

    private double calculateDotBottom(int index) {
        return dotCounts[index] * chartHeight / maxChartItemValue;
    }

    private int calculateDotX(int index) {
        return index * chartWidth / visualItemsJsArray.length() + chartWidth
                / (visualItemsJsArray.length() * 2);
    }

    protected void calculateMaximumChartItemValue() {
        maxChartItemValue = 0;
        for (int i = 0; i < visualItemsJsArray.length(); i++) {
            double currentItemValue = visualItemsJsArray.get(i).getValueAsDouble(
                    BarChart.BAR_LENGTH);
            if (maxChartItemValue < currentItemValue) {
                maxChartItemValue = currentItemValue;
            }
        }
    }

    private void dehighlightResources(int i) {
        // XXX outdated - forward events instead
        // chartItemsJsArray.get(i).getVisualItem().getHighlightingManager()
        // .setHighlighting(false);
    }

    private void deselectResources(int i) {
        // XXX outdated - forward events instead
        // chartItemsJsArray
        // .get(i)
        // .getView()
        // .getCallback()
        // .switchSelection(
        // chartItemsJsArray.get(i).getVisualItem().getResourceSet());
    }

    private void drawDot() {
        regularDot = getChart()
                .add(PV.Dot)
                .data(visualItemsJsArray)
                .bottom(dotBottom)
                .left(dotLeft)
                .size(Math.min(chartHeight, chartWidth)
                        / (visualItemsJsArray.length() * 2))
                // .fillStyle(new ChartItemColorFunction())
                .strokeStyle(Colors.STEELBLUE);

        regularDot.add(PV.Label).left(dotLeft)
                .textAlign(baselineLabelTextAlign).bottom(baselineLabelBottom)
                .text(baselineLabelText);

    }

    protected void drawScales(PVLinearScale scale) {
        this.scale = scale;
        getChart().add(PV.Rule).data(scale.ticks()).bottom(scale)
                .strokeStyle(scaleStrokeStyle).width(chartWidth)
                .anchor(PVAlignment.LEFT).add(PV.Label)
                .text(new TickFormatFunction(scale));

        getChart().add(PV.Rule).left(0).bottom(0).strokeStyle(AXIS_SCALE_COLOR)
                .height(chartHeight);
    }

    // @formatter:off
    public native void drawSelectionBox() /*-{
        var chart = this.@org.thechiselgroup.choosel.visualization_component.chart.client.ChartViewContentDisplay::getChart(),
        eventToggle = true,
        fade,
        s, 
        selectBoxAlpha = .42,
        selectBoxData = [{x:0, y:0, dx:0, dy:0}],
        thisChart = this;

        var selectBox = chart.add($wnd.pv.Panel)
            .data(selectBoxData)
            .events("all")
            .event("mousedown", $wnd.pv.Behavior.select())
            .event("selectstart", removeBoxes)
            .event("select", update)
            .event("selectend", addBoxes)
          .add($wnd.pv.Bar)
            .visible(function() {return s;})
            .left(function(d) {return d.x;})
            .top(function(d) {return d.y;})
            .width(function(d) {return d.dx;})
            .height(function(d) {return d.dy;})
            .fillStyle("rgba(193,217,241,"+selectBoxAlpha+")")
            .strokeStyle("rgba(0,0,0,"+selectBoxAlpha+")")
            .lineWidth(.5);

        var minusBox = selectBox.add($wnd.pv.Bar)
            .left(function(d) {return d.x + d.dx - 15;})
            .top(function(d) {return d.y + d.dy - 15;})
            .width(15)
            .height(15)
            .strokeStyle("rgba(0,0,0,"+selectBoxAlpha+")")
            .lineWidth(.5)
            .events("all")
            .event("mousedown", function(d) {
                var doReturn;
                for(var i = 0; i < thisChart.@org.thechiselgroup.choosel.visualization_component.chart.client.ChartViewContentDisplay::visualItemsJsArray.@java.util.ArrayList::size()(); i++) {
                    if(isInSelectionBox(d,i)) {
                        updateMinusPlus(d,i);
                        thisChart.@org.thechiselgroup.choosel.visualization_component.chart.client.other.DotChartViewContentDisplay::deselectResources(I)(i);
                        doReturn = true;
                    }
                }
                if(doReturn == true) {
                    removeBoxes(d);
                    return (s = null, chart);
                }
            })
            .event("mouseover", function(d) {
                for(var i = 0; i < thisChart.@org.thechiselgroup.choosel.visualization_component.chart.client.ChartViewContentDisplay::visualItemsJsArray.@java.util.ArrayList::size()(); i++) {
                    if(isInSelectionBox(d,i)) {
                        return this.fillStyle("FFFFE1");
                    }
                }
            })
            .event("mouseout", function() {return this.fillStyle("rgba(193,217,241,"+selectBoxAlpha+")");});

        var minus = minusBox.anchor("center").add($wnd.pv.Label)
            .visible(false)
            .text("-")
            .textStyle("black")
            .font("bold");

        var plusBox = selectBox.add($wnd.pv.Bar)
            .left(function(d) {return d.x + d.dx - 30;})
            .top(function(d) {return d.y + d.dy - 15;})
            .width(15)
            .height(15)
            .strokeStyle("rgba(0,0,0,.35)")
            .lineWidth(.5)
            .events("all")
            .event("mousedown", function(d) {
                var doReturn;
                for(var i = 0; i < thisChart.@org.thechiselgroup.choosel.visualization_component.chart.client.ChartViewContentDisplay::visualItemsJsArray.@java.util.ArrayList::size()(); i++) {
                    if(isInSelectionBox(d,i)) {
                        updateMinusPlus(d,i);
                        thisChart.@org.thechiselgroup.choosel.visualization_component.chart.client.other.DotChartViewContentDisplay::selectResources(I)(i);
                        doReturn = true;
                    }
                }
                if(doReturn == true) {
                    removeBoxes(d);
                    return (s = null, chart);
                }
            })
            .event("mouseover", function(d) {
                for(var i = 0; i < thisChart.@org.thechiselgroup.choosel.visualization_component.chart.client.ChartViewContentDisplay::visualItemsJsArray.@java.util.ArrayList::size()(); i++) {
                    if(isInSelectionBox(d,i)) {
                        return this.fillStyle("FFFFE1");
                    }
                }
            })
            .event("mouseout", function() {return this.fillStyle("rgba(193,217,241,"+selectBoxAlpha+")");});

        var plus = plusBox.anchor("center").add($wnd.pv.Label)
            .visible(false)
            .text("+")
            .textStyle("black")
            .font("bold");

        function addBoxes(d) {
            for(var i = 0; i < thisChart.@org.thechiselgroup.choosel.visualization_component.chart.client.ChartViewContentDisplay::visualItemsJsArray.@java.util.ArrayList::size()(); i++) {
                if(isInSelectionBox(d,i)) {
                    plusBox.visible(true);
                    plus.visible(true);
                    minusBox.visible(true);
                    minus.visible(true);
                    eventToggle = false;
                    update(d);
                    this.render();
                }
            }
        }

        function isInSelectionBox(d,i) {
            return thisChart.@org.thechiselgroup.choosel.visualization_component.chart.client.other.DotChartViewContentDisplay::isInSelectionBox(IIIII)(d.x, d.y, d.dx, d.dy, i);
        }

        function removeBoxes(d) {
            plusBox.visible(false);
            plus.visible(false);
            plus.textStyle("black");
            minusBox.visible(false);
            minus.visible(false);
            minus.textStyle("black");

            d.dx = 0;
            d.dy = 0;
            eventToggle = true;

            update(d);
        }

        function update(d) {
            s = d;
            s.x1 = d.x;
            s.x2 = d.x + d.dx;
            s.y1 = d.y;
            s.y2 = d.y + d.dy;

            for(var i = 0; i < thisChart.@org.thechiselgroup.choosel.visualization_component.chart.client.ChartViewContentDisplay::visualItemsJsArray.@java.util.ArrayList::size()(); i++) {
                if(isInSelectionBox(d,i)) {
                    thisChart.@org.thechiselgroup.choosel.visualization_component.chart.client.other.DotChartViewContentDisplay::highlightResources(I)(i);
                } else {
                    thisChart.@org.thechiselgroup.choosel.visualization_component.chart.client.other.DotChartViewContentDisplay::dehighlightResources(I)(i);
                }
            }

            chart.context(null, 0, function() {return this.render();});
        }

        function updateMinusPlus(d,i) {
            update(d);
            // XXX broken while changing event hanlder interface
            // thisChart.@org.thechiselgroup.choosel.visualization_component.chart.client.DotChartViewContentDisplay::onEvent(Lcom/google/gwt/user/client/Event;I)($wnd.pv.event,i);
        }
    }-*/;
    // @formatter:on

    @Override
    public String getName() {
        return "Dot Chart";
    }

    @Override
    public Slot[] getSlots() {
        return new Slot[] { BarChart.BAR_LABEL, BarChart.BAR_LENGTH };
    }

    private void highlightResources(int i) {
        // XXX outdated - forward events instead
        // chartItemsJsArray.get(i).getVisualItem().getHighlightingManager()
        // .setHighlighting(true);
    }

    private boolean isInSelectionBox(int x, int y, int dx, int dy, int i) {
        double dotY = chartHeight - calculateDotBottom(i) + 20;
        int dotX = calculateDotX(i);
        return dotX >= x && dotX <= x + dx && dotY >= y && dotY <= y + dy;
    }

    private boolean isSelected(int i) {
        return !visualItemsJsArray.get(i).getResources(Subset.SELECTED).isEmpty();
    }

    @Override
    protected void registerEventHandler(String eventType, PVEventHandler handler) {
        regularDot.event(eventType, handler);
    }

    private void selectResources(int i) {
        // XXX outdated - forward events instead
        // chartItemsJsArray
        // .get(i)
        // .getView()
        // .getCallback()
        // .switchSelection(
        // chartItemsJsArray.get(i).getVisualItem().getResourceSet());
    }

    private void setChartParameters() {
        getChart().left(BORDER_WIDTH).bottom(BORDER_HEIGHT);
    }

}