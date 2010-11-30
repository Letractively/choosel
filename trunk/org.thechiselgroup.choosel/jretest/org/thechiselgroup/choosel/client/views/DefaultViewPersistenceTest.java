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
package org.thechiselgroup.choosel.client.views;

import static org.junit.Assert.assertEquals;
import static org.thechiselgroup.choosel.client.test.TestResourceSetFactory.toResourceSet;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.client.calculation.AverageCalculation;
import org.thechiselgroup.choosel.client.calculation.Calculation;
import org.thechiselgroup.choosel.client.calculation.CountCalculation;
import org.thechiselgroup.choosel.client.calculation.MaxCalculation;
import org.thechiselgroup.choosel.client.calculation.MinCalculation;
import org.thechiselgroup.choosel.client.calculation.SumCalculation;
import org.thechiselgroup.choosel.client.persistence.Memento;
import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.resources.ResourceByPropertyMultiCategorizer;
import org.thechiselgroup.choosel.client.resources.ResourceByUriMultiCategorizer;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.persistence.DefaultResourceSetCollector;

public class DefaultViewPersistenceTest {

    private TestView originalView;

    private TestView restoredView;

    private Slot textSlot;

    private Slot numberSlot;

    @Test
    public void restoreAverageCalculationOverGroup() {
        testRestoreCalculationOverGroup(4d, new AverageCalculation());
    }

    @Test
    public void restoreChangedTextSlot() {
        // 1. create view and configure it - resources, settings...
        Resource resource = new Resource("test:1");
        resource.putValue("property1", "value1");
        resource.putValue("property2", "value2");

        originalView.getSlotMappingConfiguration().setMapping(textSlot,
                new FirstResourcePropertyResolver("property1"));
        originalView.getResourceModel().addResources(toResourceSet(resource));
        originalView.getSlotMappingConfiguration().setMapping(textSlot,
                new FirstResourcePropertyResolver("property2"));

        // 2. save first view
        DefaultResourceSetCollector collector = new DefaultResourceSetCollector();
        Memento memento = originalView.save(collector);

        // 3. restore other view
        restoredView.restore(memento, collector);

        // 4. check resource items and control settings
        List<ResourceItem> resourceItems = restoredView.getResourceItems();
        assertEquals(1, resourceItems.size());
        ResourceItem resourceItem = resourceItems.get(0);
        assertEquals("value2", resourceItem.getResourceValue(textSlot));
    }

    @Test
    public void restoreCountCalculationOverGroup() {
        testRestoreCalculationOverGroup(3d, new CountCalculation());
    }

    @Test
    public void restoreMaxCalculationOverGroup() {
        testRestoreCalculationOverGroup(8d, new MaxCalculation());
    }

    @Test
    public void restoreMinCalculationOverGroup() {
        testRestoreCalculationOverGroup(0d, new MinCalculation());
    }

    @Test
    public void restorePropertyGrouping() {
        // 1. create view and configure it - resources, settings...
        Resource r1 = new Resource("test:1");
        r1.putValue("property1", "value1-1");
        r1.putValue("property2", "value2");

        Resource r2 = new Resource("test:2");
        r2.putValue("property1", "value1-2");
        r2.putValue("property2", "value2");

        originalView.getResourceModel().addResources(toResourceSet(r1, r2));
        originalView.getResourceGrouping().setCategorizer(
                new ResourceByPropertyMultiCategorizer("property2"));

        // 2. save first view
        DefaultResourceSetCollector collector = new DefaultResourceSetCollector();
        Memento memento = originalView.save(collector);

        // 3. restore other view - set by uri categorization first
        restoredView.getResourceGrouping().setCategorizer(
                new ResourceByUriMultiCategorizer());
        restoredView.restore(memento, collector);

        // 4. check resource items and control settings
        List<ResourceItem> resourceItems = restoredView.getResourceItems();
        assertEquals(1, resourceItems.size());
        ResourceSet resourceItemResources = resourceItems.get(0)
                .getResourceSet();
        assertEquals(2, resourceItemResources.size());
        assertEquals(true, resourceItemResources.contains(r1));
        assertEquals(true, resourceItemResources.contains(r2));
    }

    @Test
    public void restoreSumCalculationOverGroup() {
        testRestoreCalculationOverGroup(12d, new SumCalculation());
    }

    @Test
    public void restoreUriGrouping() {
        // 1. create view and configure it - resources, settings...
        Resource r1 = new Resource("test:1");
        r1.putValue("property1", "value1-1");
        r1.putValue("property2", "value2");

        Resource r2 = new Resource("test:2");
        r2.putValue("property1", "value1-2");
        r2.putValue("property2", "value2");

        originalView.getResourceModel().addResources(toResourceSet(r1, r2));
        originalView.getResourceGrouping().setCategorizer(
                new ResourceByUriMultiCategorizer());

        // 2. save first view
        DefaultResourceSetCollector collector = new DefaultResourceSetCollector();
        Memento memento = originalView.save(collector);

        // 3. restore other view - set by uri categorization first
        restoredView.getResourceGrouping().setCategorizer(
                new ResourceByPropertyMultiCategorizer("property2"));
        restoredView.restore(memento, collector);

        // 4. check resource items and control settings
        List<ResourceItem> resourceItems = restoredView.getResourceItems();
        assertEquals(2, resourceItems.size());
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        textSlot = new Slot("id-1", "text-slot", DataType.TEXT);
        numberSlot = new Slot("id-2", "number-slot", DataType.NUMBER);

        originalView = TestView.createTestView(textSlot, numberSlot);
        restoredView = TestView.createTestView(textSlot, numberSlot);
    }

    protected void testRestoreCalculationOverGroup(double expectedResult,
            Calculation calculation) {

        // 1. create view and configure it - resources, settings...
        Resource r1 = new Resource("test:1");
        r1.putValue("property1", new Double(0));
        r1.putValue("property2", "value2");

        Resource r2 = new Resource("test:2");
        r2.putValue("property1", new Double(4));
        r2.putValue("property2", "value2");

        Resource r3 = new Resource("test:3");
        r3.putValue("property1", new Double(8));
        r3.putValue("property2", "value2");

        originalView.getResourceModel().addResources(toResourceSet(r1, r2, r3));
        originalView.getResourceGrouping().setCategorizer(
                new ResourceByPropertyMultiCategorizer("property2"));
        originalView.getSlotMappingConfiguration().setMapping(
                numberSlot,
                new CalculationResourceSetToValueResolver("property1",
                        calculation));

        // 2. save first view
        DefaultResourceSetCollector collector = new DefaultResourceSetCollector();
        Memento memento = originalView.save(collector);

        // 3. restore other view - set by uri categorization first
        restoredView.getResourceGrouping().setCategorizer(
                new ResourceByUriMultiCategorizer());
        restoredView.restore(memento, collector);

        // 4. check resource items and control settings
        List<ResourceItem> resourceItems = restoredView.getResourceItems();
        assertEquals(1, resourceItems.size());
        ResourceItem resourceItem = resourceItems.get(0);
        assertEquals(expectedResult, resourceItem.getResourceValue(numberSlot));
    }

}