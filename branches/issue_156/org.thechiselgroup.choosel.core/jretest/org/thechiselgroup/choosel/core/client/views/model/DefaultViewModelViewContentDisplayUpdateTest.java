/*******************************************************************************
 * Copyright (C) 2011 Lars Grammel 
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
package org.thechiselgroup.choosel.core.client.views.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.thechiselgroup.choosel.core.client.test.HamcrestResourceMatchers.containsExactly;
import static org.thechiselgroup.choosel.core.client.test.ResourcesTestHelper.containsViewItemsForResourceSets;
import static org.thechiselgroup.choosel.core.client.test.ResourcesTestHelper.emptyLightweightCollection;
import static org.thechiselgroup.choosel.core.client.test.ResourcesTestHelper.eqViewItems;
import static org.thechiselgroup.choosel.core.client.test.TestResourceSetFactory.TYPE_1;
import static org.thechiselgroup.choosel.core.client.test.TestResourceSetFactory.TYPE_2;
import static org.thechiselgroup.choosel.core.client.test.TestResourceSetFactory.createResource;
import static org.thechiselgroup.choosel.core.client.test.TestResourceSetFactory.createResources;
import static org.thechiselgroup.choosel.core.client.test.TestResourceSetFactory.toResourceSet;
import static org.thechiselgroup.choosel.core.client.views.model.ViewItemValueResolverTestUtils.mockResolverThatCanResolveExactResourceSet;
import static org.thechiselgroup.choosel.core.client.views.model.ViewItemValueResolverTestUtils.mockResolverThatCanAlwaysResolve;
import static org.thechiselgroup.choosel.core.client.views.model.ViewItemWithResourcesMatcher.containsEqualResources;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.thechiselgroup.choosel.core.client.resources.DataType;
import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.resources.ResourceSet;
import org.thechiselgroup.choosel.core.client.test.TestResourceSetFactory;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.views.model.ViewItem.Subset;
import org.thechiselgroup.choosel.core.client.views.resolvers.FixedValueResolver;
import org.thechiselgroup.choosel.core.client.views.resolvers.ViewItemValueResolver;

/**
 * <p>
 * Tests that the {@link DefaultViewModel} calls
 * {@link ViewItemRenderer#update(LightweightCollection, LightweightCollection, LightweightCollection, LightweightCollection)}
 * correctly.
 * </p>
 * <p>
 * As part of this, we test that it filters the {@link ViewItemContainerDelta}
 * that is passed into the {@link ViewContentDisplay} by the content of the
 * {@link DefaultViewItemResolutionErrorModel}.
 * </p>
 * <p>
 * We distinguish the following cases:
 * <ul>
 * <li>delta=added, current_state=valid ==&gt; add
 * {@link #addingTwoViewItemsInTwoStepsTriggersTwoUpdateCalls()}</li>
 * <li>delta=added, current_state=errors ==&gt; ignore
 * {@link #addedViewItemsWithErrorsGetIgnored()}</li>
 * <li>delta=removed, old_state=valid ==&gt; remove
 * {@link DefaultViewModelTest#updateCalledWhenResourcesRemoved}</li>
 * <li>delta=removed, old_state=errors ==&gt; ignore
 * {@link #removedViewItemsWithErrorsGetIgnored()}</li>
 * <li>delta=updated, old_state=valid, current_state=valid ==&gt; update
 * {@link #updatedViewItemsThatAreValidNowAndBeforeGetUpdated()}</li>
 * <li>delta=updated, old_state=valid, current_state=errors ==&gt; remove
 * {@link #updatedViewItemsChangingFromValidToErrorsGetRemoved()}</li>
 * <li>delta=updated, old_state=errors, current_state=valid ==&gt; add
 * {@link #updatedViewItemsChangingFromErrorsToValidGetAdded()}</li>
 * <li>delta=updated, old_state=errors, current_state=errors ==&gt; ignore
 * {@link #updatedWithErrorsNowAndBeforeGetsIgnore()}</li>
 * </ul>
 * Other items are ignored, because they cannot have changed (otherwise they
 * would be in the updated set), and thus their state cannot have changed.
 * </p>
 * 
 * @author Lars Grammel
 * @author Patrick Gorman
 */
// TODO extract AbstractDefaultViewModelTest superclass
public class DefaultViewModelViewContentDisplayUpdateTest {

    /**
     * Convert input to {@code LightWeightCollection<ViewItem>}
     */
    @SuppressWarnings("rawtypes")
    private static List<LightweightCollection<ViewItem>> cast(
            List<LightweightCollection> allValues) {

        List<LightweightCollection<ViewItem>> result = new ArrayList<LightweightCollection<ViewItem>>();
        for (LightweightCollection<ViewItem> lightweightCollection : allValues) {
            result.add(lightweightCollection);
        }
        return result;
    }

    private Slot textSlot;

    private DefaultViewModel underTest;

    private DefaultViewModelTestHelper helper;

    private Slot numberSlot;

    private static final String RESOURCE_TYPE_2 = "type2";

    private static final String RESOURCE_TYPE_1 = "type1";

    /**
     * <p>
     * delta=added, current_state=errors ==&gt; ignore
     * </p>
     * <p>
     * 2 view items get added to trigger call, we check that one is ignored
     * </p>
     */
    @Test
    public void addedViewItemsWithErrorsGetIgnored() {
        Resource validResource = createResource(RESOURCE_TYPE_1, 1);

        setCanResolverIfContainsResourceExactlyResolver(toResourceSet(validResource));

        helper.addToContainedResources(toResourceSet(validResource,
                createResource(RESOURCE_TYPE_2, 1)));

        assertThat(captureAddedViewItems(),
                containsEqualResources(validResource));
    }

    @Test
    public void addingTwoViewItemsInOneStepCallsUpdateOnce() {
        ResourceSet resources1 = createResources(TYPE_1, 1);
        ResourceSet resources2 = createResources(TYPE_2, 2);

        helper.addToContainedResources(toResourceSet(resources1, resources2));

        verify(helper.getViewContentDisplay(), times(1))
                .update(argThat(containsViewItemsForResourceSets(resources1,
                        resources2)),
                        emptyLightweightCollection(ViewItem.class),
                        emptyLightweightCollection(ViewItem.class),
                        emptyLightweightCollection(Slot.class));
    }

    @Test
    public void addingTwoViewItemsInTwoStepsCallsUpdateTwice() {
        ResourceSet resources1 = createResources(TYPE_1, 1);
        ResourceSet resources2 = createResources(TYPE_2, 2);

        helper.addToContainedResources(resources1);
        helper.addToContainedResources(resources2);

        List<LightweightCollection<ViewItem>> allValues = captureAddedViewItems(2);

        assertThat(allValues.get(0),
                containsViewItemsForResourceSets(resources1));
        assertThat(allValues.get(1),
                containsViewItemsForResourceSets(resources2));
    }

    private LightweightCollection<ViewItem> captureAddedViewItems() {
        return captureAddedViewItems(1).get(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<LightweightCollection<ViewItem>> captureAddedViewItems(
            int wantedNumberOfInvocation) {

        ArgumentCaptor<LightweightCollection> captor = ArgumentCaptor
                .forClass(LightweightCollection.class);
        verify(helper.getViewContentDisplay(), times(wantedNumberOfInvocation))
                .update(captor.capture(),
                        emptyLightweightCollection(ViewItem.class),
                        emptyLightweightCollection(ViewItem.class),
                        emptyLightweightCollection(Slot.class));

        return cast(captor.getAllValues());
    }

    private LightweightCollection<ViewItem> captureRemovedViewItems() {
        return captureRemovedViewItems(1).get(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<LightweightCollection<ViewItem>> captureRemovedViewItems(
            int wantedNumberOfInvocation) {

        ArgumentCaptor<LightweightCollection> captor = ArgumentCaptor
                .forClass(LightweightCollection.class);
        verify(helper.getViewContentDisplay(), times(wantedNumberOfInvocation))
                .update(emptyLightweightCollection(ViewItem.class),
                        emptyLightweightCollection(ViewItem.class),
                        captor.capture(),
                        emptyLightweightCollection(Slot.class));

        return cast(captor.getAllValues());
    }

    private LightweightCollection<ViewItem> captureUpdatedViewItems() {
        return captureUpdatedViewItems(1).get(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<LightweightCollection<ViewItem>> captureUpdatedViewItems(
            int wantedNumberOfInvocation) {

        ArgumentCaptor<LightweightCollection> captor = ArgumentCaptor
                .forClass(LightweightCollection.class);
        verify(helper.getViewContentDisplay(), times(wantedNumberOfInvocation))
                .update(emptyLightweightCollection(ViewItem.class),
                        captor.capture(),
                        emptyLightweightCollection(ViewItem.class),
                        emptyLightweightCollection(Slot.class));

        return cast(captor.getAllValues());
    }

    @Test
    public void highlightingChangeOnSeveralResourcesTriggersSingleUpdate() {
        ResourceSet resources = createResources(1, 2);

        helper.getContainedResources().addAll(resources);
        helper.getHighlightedResources().addAll(resources);

        LightweightCollection<ViewItem> viewItems = captureUpdatedViewItems();

        assertThat(
                viewItems.getFirstElement().getResources(Subset.HIGHLIGHTED),
                containsExactly(resources));
    }

    /**
     * delta=removed, old_state=errors ==&gt; ignore
     */
    @Test
    public void removedViewItemsWithErrorsGetIgnored() {
        Resource validResource = createResource(RESOURCE_TYPE_1, 1);
        Resource errorResource = createResource(RESOURCE_TYPE_2, 1);

        setCanResolverIfContainsResourceExactlyResolver(TestResourceSetFactory
                .toResourceSet(validResource));

        helper.addToContainedResources(TestResourceSetFactory.toResourceSet(
                validResource, errorResource));

        /*
         * at this point, the view item with errorResource is invalid as per
         * invalidViewItemDoesNotGetAddedToAddedDelta test
         */

        helper.getContainedResources().removeAll(
                TestResourceSetFactory.toResourceSet(validResource,
                        errorResource));

        assertThat(captureRemovedViewItems(),
                containsEqualResources(validResource));
    }

    private void setCanResolverIfContainsResourceExactlyResolver(
            ResourceSet resourceSet) {

        ViewItemValueResolver resolver = mockResolverThatCanResolveExactResourceSet(resourceSet);
        underTest.setResolver(textSlot, resolver);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        textSlot = new Slot("1", "Description", DataType.TEXT);
        numberSlot = new Slot("id-2", "number-slot", DataType.NUMBER);

        helper = new DefaultViewModelTestHelper();
        helper.setSlots(textSlot, numberSlot);
        underTest = helper.createTestViewModel();

        underTest.setResolver(textSlot, new FixedValueResolver("a",
                DataType.TEXT));
        underTest.setResolver(numberSlot, new FixedValueResolver(1d,
                DataType.NUMBER));
    }

    // TODO check highlighted resources in resource item
    @Test
    public void updateCalledWhenHighlightingChanges() {
        ResourceSet resources = createResources(1);

        helper.getContainedResources().addAll(resources);
        LightweightCollection<ViewItem> addedViewItems = captureAddedViewItems();

        helper.getHighlightedResources().addAll(resources);

        verify(helper.getViewContentDisplay(), times(1)).update(
                emptyLightweightCollection(ViewItem.class),
                eqViewItems(addedViewItems),
                emptyLightweightCollection(ViewItem.class),
                emptyLightweightCollection(Slot.class));
    }

    @Test
    public void updateCalledWhenResourcesRemoved() {
        ResourceSet resources1 = createResources(TYPE_1, 1);
        ResourceSet resources2 = createResources(TYPE_2, 2);
        ResourceSet resources = toResourceSet(resources1, resources2);

        helper.getContainedResources().addAll(resources);
        LightweightCollection<ViewItem> addedViewItems = captureAddedViewItems();

        helper.getContainedResources().removeAll(resources);
        verify(helper.getViewContentDisplay(), times(1)).update(
                emptyLightweightCollection(ViewItem.class),
                emptyLightweightCollection(ViewItem.class),
                eqViewItems(addedViewItems),
                emptyLightweightCollection(Slot.class));
    }

    @Test
    public void updateCalledWhenSelectionChanges() {
        ResourceSet resources = createResources(1);

        helper.getContainedResources().addAll(resources);
        LightweightCollection<ViewItem> addedViewItems = captureAddedViewItems();

        helper.getSelectedResources().add(createResource(1));

        verify(helper.getViewContentDisplay(), times(1)).update(
                emptyLightweightCollection(ViewItem.class),
                eqViewItems(addedViewItems),
                emptyLightweightCollection(ViewItem.class),
                emptyLightweightCollection(Slot.class));
    }

    /**
     * delta=updated, old_state=errors, current_state=valid ==&gt; add
     */
    @Test
    public void updatedViewItemsChangingFromErrorsToValidGetAdded() {
        Resource resource1 = createResource(RESOURCE_TYPE_1, 1);
        Resource resource2 = createResource(RESOURCE_TYPE_1, 2);

        setCanResolverIfContainsResourceExactlyResolver(toResourceSet(
                resource1, resource2));

        helper.addToContainedResources(resource1);

        /* should not have been added yet - 0 update calls so far */

        helper.addToContainedResources(resource2);

        assertThat(captureAddedViewItems(),
                containsEqualResources(resource1, resource2));
    }

    /**
     * delta=updated, old_state=valid, current_state=errors ==&gt; remove
     */
    @Test
    public void updatedViewItemsChangingFromValidToErrorsGetRemoved() {
        Resource resource1 = createResource(RESOURCE_TYPE_1, 1);
        Resource resource2 = createResource(RESOURCE_TYPE_1, 2);

        setCanResolverIfContainsResourceExactlyResolver(toResourceSet(
                resource1, resource2));

        helper.addToContainedResources(toResourceSet(resource1, resource2));

        /* should now have 1 valid view item */

        helper.getContainedResources().remove(resource2);

        assertThat(captureRemovedViewItems(), containsEqualResources(resource1));
    }

    /**
     * delta=updated, old_state=valid, current_state=valid ==&gt; updated
     */
    @Test
    public void updatedViewItemsThatAreValidNowAndBeforeGetUpdated() {
        Resource resource1 = createResource(RESOURCE_TYPE_1, 1);
        Resource resource2 = createResource(RESOURCE_TYPE_1, 2);

        underTest.setResolver(textSlot, mockResolverThatCanAlwaysResolve());

        helper.addToContainedResources(resource1);

        // update call
        helper.addToContainedResources(resource2);

        assertThat(captureUpdatedViewItems(),
                containsEqualResources(resource1, resource2));
    }

    /**
     * delta=updated, old_state=errors, current_state=errors ==&gt; ignore
     */
    @SuppressWarnings("unchecked")
    @Test
    public void updatedViewItemsWithErrorsNowAndBeforeGetIgnored() {
        Resource validResource = createResource(RESOURCE_TYPE_1, 3);

        setCanResolverIfContainsResourceExactlyResolver(toResourceSet(validResource));

        // adds error view item and correct view item
        helper.addToContainedResources(createResource(RESOURCE_TYPE_1, 1));

        // updates error view item
        helper.addToContainedResources(createResource(RESOURCE_TYPE_1, 2));

        // neither adding nor updating view item should have triggered calls to
        // update
        verify(helper.getViewContentDisplay(), never()).update(
                any(LightweightCollection.class),
                any(LightweightCollection.class),
                any(LightweightCollection.class),
                emptyLightweightCollection(Slot.class));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void updateNeverCalledOnHighlightingChangeThatDoesNotAffectViewResources() {
        helper.getContainedResources().add(createResource(2));
        helper.getHighlightedResources().add(createResource(1));

        verify(helper.getViewContentDisplay(), never()).update(
                emptyLightweightCollection(ViewItem.class),
                any(LightweightCollection.class),
                emptyLightweightCollection(ViewItem.class),
                emptyLightweightCollection(Slot.class));
    }

    /**
     * Shows the bug that happens when the default view gets notified of the
     * slot update before the {@link ViewItem}s are cleaned (by getting
     * notification of the slot update). The view items need to get the
     * notification first to clean their caching.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void viewItemsReturnCorrectValuesOnViewContentDisplayUpdate() {
        helper.addToContainedResources(createResource(RESOURCE_TYPE_1, 1));

        final ViewItem viewItem = underTest.getViewItems().getFirstElement();
        viewItem.getValue(numberSlot); // caches values

        /*
         * We assert the value here, because this is what happens during the
         * update call. If we would check the value on the view item later, the
         * bug would not show up (it is important that the viewItem returns the
         * new values when the content display is updated.
         */
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                assertEquals(5d, viewItem.getValue(numberSlot));
                return null;
            }
        }).when(helper.getViewContentDisplay()).update(
                any(LightweightCollection.class),
                any(LightweightCollection.class),
                any(LightweightCollection.class),
                any(LightweightCollection.class));

        underTest.setResolver(numberSlot, new FixedValueResolver(5d,
                DataType.NUMBER));
    }
}