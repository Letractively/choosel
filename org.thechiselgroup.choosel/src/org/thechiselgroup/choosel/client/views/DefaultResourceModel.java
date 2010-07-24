package org.thechiselgroup.choosel.client.views;

import java.util.List;

import org.thechiselgroup.choosel.client.persistence.Memento;
import org.thechiselgroup.choosel.client.persistence.Persistable;
import org.thechiselgroup.choosel.client.resources.CombinedResourceSet;
import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ResourceSetFactory;
import org.thechiselgroup.choosel.client.resources.persistence.ResourceSetAccessor;
import org.thechiselgroup.choosel.client.resources.persistence.ResourceSetCollector;
import org.thechiselgroup.choosel.client.util.Disposable;
import org.thechiselgroup.choosel.client.util.Initializable;

public class DefaultResourceModel implements ResourceModel, Disposable,
        Persistable, Initializable {

    private CombinedResourceSet allResources;

    private ResourceSet automaticResources;

    private CombinedResourceSet combinedUserResourceSets;

    static final String MEMENTO_RESOURCE_SET_COUNT = "resourceSetCount";

    static final String MEMENTO_RESOURCE_SET_PREFIX = "resourceSet-";

    static final String MEMENTO_AUTOMATIC_RESOURCES = "automaticResources";

    private ResourceSetFactory resourceSetFactory;

    public DefaultResourceModel(ResourceSetFactory resourceSetFactory) {
        assert resourceSetFactory != null;
        this.resourceSetFactory = resourceSetFactory;
    }

    @Override
    public void addResources(Iterable<Resource> resources) {
        assert resources != null;
        automaticResources.addAll(resources);
    }

    @Override
    public void addResourceSet(ResourceSet resourceSet) {
        if (!resourceSet.hasLabel()) {
            automaticResources.addAll(resourceSet);
        } else {
            combinedUserResourceSets.addResourceSet(resourceSet);
        }
    }

    @Override
    public void clear() {
        automaticResources.clear();
        combinedUserResourceSets.clear();
    }

    @Override
    public boolean containsResources(Iterable<Resource> resources) {
        assert resources != null;
        return allResources.containsAll(resources);
    }

    @Override
    public boolean containsResourceSet(ResourceSet resourceSet) {
        assert resourceSet != null;
        assert resourceSet.hasLabel() : resourceSet.toString()
                + " has no label";

        return combinedUserResourceSets.containsResourceSet(resourceSet);
    }

    @Override
    public void dispose() {
        combinedUserResourceSets.clear();
        combinedUserResourceSets = null;
    }

    @Override
    public ResourceSet getAutomaticResourceSet() {
        return automaticResources;
    }

    @Override
    public CombinedResourceSet getCombinedUserResourceSets() {
        return combinedUserResourceSets;
    }

    @Override
    public ResourceSet getResources() {
        return allResources;
    }

    @Override
    public void init() {
        initResourceCombinator();
        initAutomaticResources();
        initAllResources();
    }

    private void initAllResources() {
        allResources = new CombinedResourceSet(
                resourceSetFactory.createResourceSet());
        allResources.setLabel("All"); // TODO add & update view name
        allResources.addResourceSet(automaticResources);
        allResources.addResourceSet(combinedUserResourceSets);
    }

    private void initAutomaticResources() {
        automaticResources = resourceSetFactory.createResourceSet();
    }

    private void initResourceCombinator() {
        combinedUserResourceSets = new CombinedResourceSet(
                resourceSetFactory.createResourceSet());
    }

    @Override
    public void removeResources(Iterable<Resource> resources) {
        assert resources != null;
        automaticResources.removeAll(resources);
    }

    @Override
    public void removeResourceSet(ResourceSet resourceSet) {
        assert resourceSet != null;
        assert resourceSet.hasLabel();

        combinedUserResourceSets.removeResourceSet(resourceSet);
    }

    @Override
    public void restore(Memento state, ResourceSetAccessor accessor) {
        // TODO remove user sets, automatic resources
        addResources(restoreAutomaticResources(state, accessor));
        restoreUserResourceSets(state, accessor);
    }

    private ResourceSet restoreAutomaticResources(Memento state,
            ResourceSetAccessor accessor) {
        return restoreResourceSet(state, accessor, MEMENTO_AUTOMATIC_RESOURCES);
    }

    private ResourceSet restoreResourceSet(Memento state,
            ResourceSetAccessor accessor, String key) {
        int id = (Integer) state.getValue(key);
        ResourceSet resourceSet = accessor.getResourceSet(id);
        return resourceSet;
    }

    private void restoreUserResourceSets(Memento state,
            ResourceSetAccessor accessor) {
        int resourceSetCount = (Integer) state
                .getValue(MEMENTO_RESOURCE_SET_COUNT);
        for (int i = 0; i < resourceSetCount; i++) {
            addResourceSet(restoreResourceSet(state, accessor,
                    MEMENTO_RESOURCE_SET_PREFIX + i));
        }
    }

    @Override
    public Memento save(ResourceSetCollector resourceSetCollector) {
        Memento memento = new Memento();
        storeAutomaticResources(resourceSetCollector, memento);
        storeUserResourceSets(resourceSetCollector, memento);
        return memento;
    }

    private void storeAutomaticResources(
            ResourceSetCollector persistanceManager, Memento memento) {

        storeResourceSet(persistanceManager, memento,
                MEMENTO_AUTOMATIC_RESOURCES, automaticResources);
    }

    private void storeResourceSet(ResourceSetCollector persistanceManager,
            Memento memento, String key, ResourceSet resources) {
        memento.setValue(key, persistanceManager.storeResourceSet(resources));
    }

    private void storeUserResourceSets(ResourceSetCollector persistanceManager,
            Memento memento) {
        List<ResourceSet> resourceSets = combinedUserResourceSets
                .getResourceSets();
        memento.setValue(MEMENTO_RESOURCE_SET_COUNT, resourceSets.size());
        for (int i = 0; i < resourceSets.size(); i++) {
            storeResourceSet(persistanceManager, memento,
                    MEMENTO_RESOURCE_SET_PREFIX + i, resourceSets.get(i));
        }
    }

}
