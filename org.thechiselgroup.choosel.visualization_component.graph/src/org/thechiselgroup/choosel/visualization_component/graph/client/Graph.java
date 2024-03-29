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
package org.thechiselgroup.choosel.visualization_component.graph.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.thechiselgroup.choosel.core.client.command.CommandManager;
import org.thechiselgroup.choosel.core.client.geometry.DefaultSize;
import org.thechiselgroup.choosel.core.client.geometry.Point;
import org.thechiselgroup.choosel.core.client.geometry.Size;
import org.thechiselgroup.choosel.core.client.persistence.Memento;
import org.thechiselgroup.choosel.core.client.persistence.PersistableRestorationService;
import org.thechiselgroup.choosel.core.client.resources.DefaultResourceSet;
import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.resources.ResourceCategorizer;
import org.thechiselgroup.choosel.core.client.resources.ResourceManager;
import org.thechiselgroup.choosel.core.client.resources.ResourceSet;
import org.thechiselgroup.choosel.core.client.resources.UnionResourceSet;
import org.thechiselgroup.choosel.core.client.resources.persistence.ResourceSetAccessor;
import org.thechiselgroup.choosel.core.client.resources.persistence.ResourceSetCollector;
import org.thechiselgroup.choosel.core.client.ui.SidePanelSection;
import org.thechiselgroup.choosel.core.client.util.DataType;
import org.thechiselgroup.choosel.core.client.util.NoSuchAdapterException;
import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.core.client.util.collections.Delta;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.visualization.model.AbstractViewContentDisplay;
import org.thechiselgroup.choosel.core.client.visualization.model.Slot;
import org.thechiselgroup.choosel.core.client.visualization.model.ViewContentDisplayCallback;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemContainer;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemInteraction;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemInteraction.Type;
import org.thechiselgroup.choosel.core.client.visualization.model.extensions.RequiresAutomaticResourceSet;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.GraphDisplay;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.GraphDisplayLoadingFailureEvent;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.GraphDisplayLoadingFailureEventHandler;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.GraphDisplayReadyEvent;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.GraphDisplayReadyEventHandler;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.GraphLayouts;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.GraphWidget;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.Node;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeDragEvent;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeDragHandleMouseDownEvent;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeDragHandleMouseDownHandler;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeDragHandleMouseMoveEvent;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeDragHandleMouseMoveHandler;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeDragHandler;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeEvent;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeMenuItemClickedHandler;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeMouseClickEvent;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeMouseClickHandler;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeMouseOutEvent;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeMouseOutHandler;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeMouseOverEvent;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.NodeMouseOverHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

// TODO separate out ncbo specific stuff and service calls
// TODO register listener for double click on node --> change expansion state
public class Graph extends AbstractViewContentDisplay implements
        RequiresAutomaticResourceSet, GraphLayoutSupport, GraphLayoutCallback {

    public static class DefaultDisplay extends GraphWidget implements
            GraphDisplay {

        // TODO why is size needed in the first place??
        public DefaultDisplay() {
            this(400, 300);
        }

        public DefaultDisplay(int width, int height) {
            super(width, height);
        }

    }

    private class GraphEventHandler implements NodeMouseOverHandler,
            NodeMouseOutHandler, NodeMouseClickHandler, MouseMoveHandler,
            NodeDragHandler, NodeDragHandleMouseDownHandler,
            NodeDragHandleMouseMoveHandler {

        /**
         * Node that the mouse is currently over. Set by mouse out and mouse
         * over. Not over a node if null.
         */
        private Node currentNode = null;

        @Override
        public void onDrag(NodeDragEvent event) {
            commandManager.execute(new MoveNodeCommand(graphDisplay, event
                    .getNode(),
                    new Point(event.getStartX(), event.getStartY()), new Point(
                            event.getEndX(), event.getEndY())));
        }

        @Override
        public void onMouseClick(NodeMouseClickEvent event) {
            reportInteraction(Type.CLICK, event);
        }

        @Override
        public void onMouseDown(NodeDragHandleMouseDownEvent event) {
            reportInteraction(Type.MOUSE_DOWN, event);
        }

        @Override
        public void onMouseMove(MouseMoveEvent event) {
            if (currentNode != null) {
                getVisualItem(currentNode).reportInteraction(
                        new VisualItemInteraction(Type.MOUSE_MOVE, event
                                .getClientX(), event.getClientY()));
            }
        }

        @Override
        public void onMouseMove(NodeDragHandleMouseMoveEvent event) {
            reportInteraction(Type.MOUSE_MOVE, event);
        }

        @Override
        public void onMouseOut(NodeMouseOutEvent event) {
            currentNode = null;
            reportInteraction(Type.MOUSE_OUT, event);
        }

        @Override
        public void onMouseOver(NodeMouseOverEvent event) {
            currentNode = event.getNode();
            reportInteraction(Type.MOUSE_OVER, event);
        }

        private void reportInteraction(Type eventType, NodeEvent<?> event) {
            int clientX = event.getMouseX() + asWidget().getAbsoluteLeft();
            int clientY = event.getMouseY() + asWidget().getAbsoluteTop();

            getVisualItem(event).reportInteraction(
                    new VisualItemInteraction(eventType, clientX, clientY));
        }

    }

    public class GraphLayoutAction implements ViewContentDisplayAction {

        private String layout;

        public GraphLayoutAction(String layout) {
            this.layout = layout;
        }

        @Override
        public void execute() {
            commandManager.execute(new GraphLayoutCommand(graphDisplay, layout,
                    getAllNodes()));
        }

        @Override
        public String getLabel() {
            return layout;
        }
    }

    public static final Slot NODE_BORDER_COLOR = new Slot("nodeBorderColor",
            "Node Border Color", DataType.COLOR);

    public static final Slot NODE_BACKGROUND_COLOR = new Slot(
            "nodeBackgroundColor", "Node Color", DataType.COLOR);

    public static final Slot NODE_LABEL_SLOT = new Slot("nodeLabel",
            "Node Label", DataType.TEXT);

    public final static String ID = "org.thechiselgroup.choosel.visualization_component.graph.Graph";

    private static final String MEMENTO_ARC_ITEM_CONTAINERS_CHILD = "arcItemContainers";

    private static final String MEMENTO_NODE_LOCATIONS_CHILD = "nodeLocations";

    private static final String MEMENTO_X = "x";

    private static final String MEMENTO_Y = "y";

    // TODO move
    public static String getArcId(String arcType, String sourceId,
            String targetId) {
        // FIXME this needs escaping of special characters to work properly
        return arcType + ":" + sourceId + "_" + targetId;
    }

    private ArcTypeProvider arcStyleProvider;

    private final CommandManager commandManager;

    // advanced node class: (incoming, outgoing, expanded: state machine)

    private final GraphDisplay graphDisplay;

    private boolean ready = false;

    private GraphExpansionRegistry registry;

    private ResourceCategorizer resourceCategorizer;

    private ResourceManager resourceManager;

    private UnionResourceSet nodeResources = new UnionResourceSet(
            new DefaultResourceSet());

    private Map<String, ArcItemContainer> arcItemContainersByArcTypeID = CollectionFactory
            .createStringMap();

    private ResourceSet automaticResources;

    /*
     * TODO The callback is meant to check whether the graph is initialized (and
     * not disposed) when methods are called (to prevent errors in asynchronous
     * callbacks that return after the graph has been disposed or before it has
     * been initialized).
     */
    private GraphNodeExpansionCallback expansionCallback = new GraphNodeExpansionCallback() {

        @Override
        public void addAutomaticResource(Resource resource) {
            Graph.this.addAutomaticResource(resource);
        }

        @Override
        public boolean containsResourceWithUri(String resourceUri) {
            return Graph.this.containsResourceWithUri(resourceUri);
        }

        @Override
        public String getCategory(Resource resource) {
            return Graph.this.getCategory(resource);
        }

        @Override
        public GraphDisplay getDisplay() {
            return Graph.this.getDisplay();
        }

        @Override
        public Resource getResourceByUri(String value) {
            return Graph.this.getResourceByUri(value);
        }

        @Override
        public ResourceManager getResourceManager() {
            return Graph.this.getResourceManager();
        }

        @Override
        public LightweightCollection<VisualItem> getVisualItems(
                Iterable<Resource> resources) {
            return Graph.this.getVisualItems(resources);
        }

        @Override
        public boolean isInitialized() {
            return Graph.this.isInitialized();
        }

        @Override
        public boolean isRestoring() {
            return Graph.this.isRestoring();
        }

        @Override
        public void updateArcsForResources(Iterable<Resource> resources) {
            Graph.this.updateArcsForResources(resources);
        }

        @Override
        public void updateArcsForVisuaItems(
                LightweightCollection<VisualItem> visualItems) {
            Graph.this.updateArcsForVisuaItems(visualItems);
        }
    };

    @Inject
    public Graph(GraphDisplay display, CommandManager commandManager,
            ResourceManager resourceManager,
            ResourceCategorizer resourceCategorizer,
            ArcTypeProvider arcStyleProvider, GraphExpansionRegistry registry) {

        assert display != null;
        assert commandManager != null;
        assert resourceManager != null;
        assert resourceCategorizer != null;
        assert arcStyleProvider != null;
        assert registry != null;

        this.arcStyleProvider = arcStyleProvider;
        this.resourceCategorizer = resourceCategorizer;
        graphDisplay = display;
        this.commandManager = commandManager;
        this.resourceManager = resourceManager;
        this.registry = registry;

        /*
         * we init the arc type containers early so they are available for UI
         * customization in Choosel applications.
         */
        initArcTypeContainers();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T adaptTo(Class<T> clazz) throws NoSuchAdapterException {
        if (GraphLayoutSupport.class.equals(clazz)) {
            return (T) this;
        }

        return super.adaptTo(clazz);
    }

    private void addAutomaticResource(Resource resource) {
        automaticResources.add(resource);
    }

    private boolean containsResourceWithUri(String resourceUri) {
        return nodeResources.containsResourceWithUri(resourceUri);
    }

    private NodeItem createGraphNodeItem(VisualItem visualItem) {
        // TODO get from group id
        String type = getCategory(visualItem.getResources().getFirstElement());

        NodeItem graphItem = new NodeItem(visualItem, type, graphDisplay);

        graphDisplay.addNode(graphItem.getNode());
        positionNode(graphItem.getNode());

        // TODO re-enable
        // TODO remove once new drag and drop mechanism works...
        graphDisplay
                .setNodeStyle(graphItem.getNode(), "showDragImage", "false");

        graphDisplay.setNodeStyle(graphItem.getNode(), "showArrow", registry
                .getNodeMenuEntries(type).isEmpty() ? "false" : "true");

        nodeResources.addResourceSet(visualItem.getResources());
        visualItem.setDisplayObject(graphItem);

        /*
         * NOTE: all node configuration should be done when calling the
         * automatic expanders, since they rely on returning the correct graph
         * contents etc.
         * 
         * NOTE: we do not execute the expanders if we are restoring the graph
         */
        if (ready) {
            registry.getAutomaticExpander(type).expand(visualItem,
                    expansionCallback);
        }

        return graphItem;
    }

    // TODO encapsulate in display, use dependency injection
    @Override
    protected Widget createWidget() {
        return graphDisplay.asWidget();
    }

    // TODO better caching?
    // default visibility for test case use
    List<Node> getAllNodes() {
        List<Node> result = new ArrayList<Node>();
        for (VisualItem visualItem : getVisualItems()) {
            result.add(getNode(visualItem));
        }
        return result;
    }

    public ResourceSet getAllResources() {
        return nodeResources;
    }

    public ArcItemContainer getArcItemContainer(String arcTypeID) {
        assert arcTypeID != null;
        assert arcItemContainersByArcTypeID.containsKey(arcTypeID);

        return arcItemContainersByArcTypeID.get(arcTypeID);
    }

    public Iterable<ArcItemContainer> getArcItemContainers() {
        return arcItemContainersByArcTypeID.values();
    }

    private ArcItem[] getArcItems() {
        Iterable<ArcItemContainer> arcItemContainers = getArcItemContainers();
        int size = 0;
        for (ArcItemContainer arcItemContainer : arcItemContainers) {
            size += arcItemContainer.getArcItems().size();
        }
        ArcItem[] arcs = new ArcItem[size];
        int i = 0;
        for (ArcItemContainer arcItemContainer : arcItemContainers) {
            for (ArcItem arcItem : arcItemContainer.getArcItems()) {
                arcs[i++] = arcItem;
            }
        }
        return arcs;
    }

    private String getCategory(Resource resource) {
        return resourceCategorizer.getCategory(resource);
    }

    private GraphDisplay getDisplay() {
        return graphDisplay;
    }

    @Override
    public Size getDisplayArea() {
        Widget displayWidget = graphDisplay.asWidget();
        int height = displayWidget.getOffsetHeight();
        int width = displayWidget.getOffsetWidth();
        return new DefaultSize(width, height);
    }

    @Override
    public String getName() {
        return "Graph";
    }

    private Node getNode(VisualItem visualItem) {
        return visualItem.<NodeItem> getDisplayObject().getNode();
    }

    private NodeItem[] getNodeItems() {
        LightweightCollection<VisualItem> visualItems = getVisualItems();
        NodeItem[] nodeItems = new NodeItem[visualItems.size()];
        int i = 0;
        for (VisualItem visualItem : visualItems) {
            nodeItems[i++] = visualItem.getDisplayObject();
        }
        return nodeItems;
    }

    public Resource getResourceByUri(String value) {
        return nodeResources.getByUri(value);
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    // TODO cleanup
    @Override
    public SidePanelSection[] getSidePanelSections() {
        List<ViewContentDisplayAction> actions = new ArrayList<ViewContentDisplayAction>();

        actions.add(new GraphLayoutAction(GraphLayouts.CIRCLE_LAYOUT));
        actions.add(new GraphLayoutAction(GraphLayouts.HORIZONTAL_TREE_LAYOUT));
        actions.add(new GraphLayoutAction(GraphLayouts.VERTICAL_TREE_LAYOUT));
        actions.add(new GraphLayoutAction(GraphLayouts.RADIAL_LAYOUT));
        actions.add(new GraphLayoutAction(GraphLayouts.SPRING_LAYOUT));
        actions.add(new GraphLayoutAction(GraphLayouts.INDENTED_TREE_LAYOUT));
        actions.add(new GraphLayoutAction(GraphLayouts.GRID_LAYOUT_BY_NODE_ID));
        actions.add(new GraphLayoutAction(GraphLayouts.GRID_LAYOUT_BY_NODE_TYPE));
        actions.add(new GraphLayoutAction(GraphLayouts.GRID_LAYOUT_ALPHABETICAL));
        actions.add(new GraphLayoutAction(GraphLayouts.GRID_LAYOUT_BY_ARC_COUNT));
        actions.add(new GraphLayoutAction(GraphLayouts.HORIZONTAL_LAYOUT));
        actions.add(new GraphLayoutAction(GraphLayouts.VERTICAL_LAYOUT));
        actions.add(new GraphLayoutAction(GraphLayouts.FORCE_DIRECTED_LAYOUT));

        VerticalPanel layoutPanel = new VerticalPanel();
        for (final ViewContentDisplayAction action : actions) {
            Button w = new Button(action.getLabel());
            w.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    action.execute();
                }
            });
            layoutPanel.add(w);
        }

        return new SidePanelSection[] { new SidePanelSection("Layouts",
                layoutPanel), };
    }

    @Override
    public Slot[] getSlots() {
        return new Slot[] { NODE_LABEL_SLOT, NODE_BORDER_COLOR,
                NODE_BACKGROUND_COLOR };
    }

    private VisualItem getVisualItem(Node node) {
        return getVisualItem(node.getId());
    }

    private VisualItem getVisualItem(NodeEvent<?> event) {
        return getVisualItem(event.getNode());
    }

    @Override
    public void init(VisualItemContainer container,
            ViewContentDisplayCallback callback) {

        super.init(container, callback);

        initStateChangeHandlers();
    }

    private void initArcTypeContainers() {
        for (ArcType arcType : arcStyleProvider.getArcTypes()) {
            arcItemContainersByArcTypeID.put(arcType.getArcTypeID(),
                    new ArcItemContainer(arcType, graphDisplay, this));
        }
    }

    private void initNodeMenuItems() {
        for (Entry<String, List<NodeMenuEntry>> entry : registry
                .getNodeMenuEntriesByCategory()) {

            String category = entry.getKey();
            for (NodeMenuEntry nodeMenuEntry : entry.getValue()) {
                registerNodeMenuItem(category, nodeMenuEntry.getLabel(),
                        nodeMenuEntry.getExpander());
            }
        }
    }

    private void initStateChangeHandlers() {
        graphDisplay
                .addGraphDisplayReadyHandler(new GraphDisplayReadyEventHandler() {
                    @Override
                    public void onWidgetReady(GraphDisplayReadyEvent event) {
                        ready = true;

                        GraphEventHandler handler = new GraphEventHandler();

                        graphDisplay.addEventHandler(
                                NodeDragHandleMouseDownEvent.TYPE, handler);
                        graphDisplay.addEventHandler(NodeMouseOverEvent.TYPE,
                                handler);
                        graphDisplay.addEventHandler(NodeMouseOutEvent.TYPE,
                                handler);
                        graphDisplay.addEventHandler(NodeMouseClickEvent.TYPE,
                                handler);
                        graphDisplay.addEventHandler(NodeDragEvent.TYPE,
                                handler);
                        graphDisplay.addEventHandler(MouseMoveEvent.getType(),
                                handler);

                        initNodeMenuItems();
                    }
                });
        graphDisplay
                .addGraphDisplayLoadingFailureHandler(new GraphDisplayLoadingFailureEventHandler() {
                    @Override
                    public void onLoadingFailure(
                            GraphDisplayLoadingFailureEvent event) {
                        // TODO handle loading failures
                    }
                });
    }

    @Override
    public boolean isAdaptableTo(Class<?> clazz) {
        if (GraphLayoutSupport.class.equals(clazz)) {
            return true;
        }

        return super.isAdaptableTo(clazz);
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    private void positionNode(Node node) {
        // FIXME positioning: FlexVis takes care of positioning nodes into empty
        // space except for first node - if the node is the first node, we put
        // it in the center
        // TODO improve interface to access all resources

        assert node != null;

        if (getVisualItems().size() > 1) {
            return;
        }

        Widget displayWidget = graphDisplay.asWidget();
        if (displayWidget == null) {
            return; // for tests
        }

        int height = displayWidget.getOffsetHeight();
        int width = displayWidget.getOffsetWidth();

        graphDisplay.setLocation(node, new Point(width / 2, height / 2));
    }

    private void registerNodeMenuItem(String category, String menuLabel,
            final GraphNodeExpander nodeExpander) {

        graphDisplay.addNodeMenuItemHandler(menuLabel,
                new NodeMenuItemClickedHandler() {
                    @Override
                    public void onNodeMenuItemClicked(Node node) {
                        if (!ready) {
                            return;
                        }

                        nodeExpander.expand(getVisualItem(node),
                                expansionCallback);
                    }
                }, category);
    }

    private void removeGraphNode(VisualItem visualItem) {
        assert visualItem != null;

        nodeResources.removeResourceSet(visualItem.getResources());
        for (ArcItemContainer arcItemContainer : arcItemContainersByArcTypeID
                .values()) {
            arcItemContainer.removeVisualItem(visualItem);
        }
        graphDisplay.removeNode(getNode(visualItem));
    }

    @Override
    public void restore(Memento state,
            PersistableRestorationService restorationService,
            ResourceSetAccessor accessor) {

        restoreArcItemContainers(restorationService, accessor,
                state.getChild(MEMENTO_ARC_ITEM_CONTAINERS_CHILD));
        restoreNodeLocations(state.getChild(MEMENTO_NODE_LOCATIONS_CHILD));
    }

    private void restoreArcItemContainers(
            PersistableRestorationService restorationService,
            ResourceSetAccessor accessor, Memento child) {
        for (Entry<String, Memento> entry : child.getChildren().entrySet()) {
            arcItemContainersByArcTypeID.get(entry.getKey()).restore(
                    entry.getValue(), restorationService, accessor);
        }
    }

    private void restoreNodeLocations(Memento state) {
        for (VisualItem visualItem : getVisualItems()) {
            NodeItem item = visualItem.getDisplayObject();
            Memento nodeMemento = state.getChild(visualItem.getId());
            Point location = new Point(
                    (Integer) nodeMemento.getValue(MEMENTO_X),
                    (Integer) nodeMemento.getValue(MEMENTO_Y));

            setLocation(item, location);
        }
    }

    @Override
    public void runLayout(GraphLayout layout) {
        assert layout != null;
        layout.run(getNodeItems(), getArcItems(), this);
    }

    @Override
    public void runLayout(String layout) {
        assert layout != null;
        graphDisplay.runLayout(layout);
    }

    @Override
    public Memento save(ResourceSetCollector resourceSetCollector) {
        Memento result = new Memento();

        result.addChild(MEMENTO_NODE_LOCATIONS_CHILD, saveNodeLocations());
        result.addChild(MEMENTO_ARC_ITEM_CONTAINERS_CHILD,
                saveArcTypeContainers(resourceSetCollector));

        return result;
    }

    private Memento saveArcTypeContainers(
            ResourceSetCollector resourceSetCollector) {

        Memento memento = new Memento();
        for (Entry<String, ArcItemContainer> entry : arcItemContainersByArcTypeID
                .entrySet()) {
            memento.addChild(entry.getKey(),
                    entry.getValue().save(resourceSetCollector));
        }
        return memento;
    }

    private Memento saveNodeLocations() {
        Memento state = new Memento();

        for (VisualItem visualItem : getVisualItems()) {
            NodeItem nodeItem = visualItem.getDisplayObject();

            Point location = graphDisplay.getLocation(nodeItem.getNode());

            Memento nodeMemento = new Memento();
            nodeMemento.setValue(MEMENTO_X, location.getX());
            nodeMemento.setValue(MEMENTO_Y, location.getY());

            state.addChild(visualItem.getId(), nodeMemento);
        }
        return state;
    }

    /**
     * If the arc type becomes invisible, all arcs of this arcType from the view
     * and arcs of this arc type are not shown any more. If the arc types
     * becomes visible, all arcs of this type are added.
     */
    // TODO expose arc type configurations and use listener mechanism
    public void setArcTypeVisible(String arcTypeId, boolean visible) {
        assert arcTypeId != null;
        assert arcItemContainersByArcTypeID.containsKey(arcTypeId);

        arcItemContainersByArcTypeID.get(arcTypeId).setVisible(visible);
    }

    @Override
    public void setAutomaticResources(ResourceSet automaticResources) {
        this.automaticResources = automaticResources;
    }

    @Override
    public void setLocation(NodeItem node, Point location) {
        graphDisplay.setLocation(node.getNode(), location);
    }

    @Override
    public void update(Delta<VisualItem> delta,
            LightweightCollection<Slot> updatedSlots) {

        for (VisualItem addedItem : delta.getAddedElements()) {
            createGraphNodeItem(addedItem);
            updateNode(addedItem);
        }

        updateArcsForVisuaItems(delta.getAddedElements());

        for (VisualItem updatedItem : delta.getUpdatedElements()) {
            updateNode(updatedItem);
        }

        for (VisualItem visualItem : delta.getRemovedElements()) {
            removeGraphNode(visualItem);
        }

        if (!updatedSlots.isEmpty()) {
            for (VisualItem visualItem : getVisualItems()) {
                updateNode(visualItem);
            }
        }
    }

    public void updateArcsForResources(Iterable<Resource> resources) {
        updateArcsForVisuaItems(getVisualItems(resources));
    }

    public void updateArcsForVisuaItems(
            LightweightCollection<VisualItem> visualItems) {
        assert visualItems != null;
        for (ArcItemContainer container : arcItemContainersByArcTypeID.values()) {
            container.update(visualItems);
        }
    }

    private void updateNode(VisualItem visualItem) {
        visualItem.<NodeItem> getDisplayObject().updateNode();
    }

}