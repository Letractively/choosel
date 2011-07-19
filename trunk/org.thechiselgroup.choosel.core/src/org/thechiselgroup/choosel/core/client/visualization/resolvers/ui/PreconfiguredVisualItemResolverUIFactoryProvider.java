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
package org.thechiselgroup.choosel.core.client.visualization.resolvers.ui;

import static org.thechiselgroup.choosel.core.client.visualization.resolvers.managed.PreconfiguredVisualItemValueResolverFactoryProvider.AVERAGE_RESOLVER_FACTORY_ID;
import static org.thechiselgroup.choosel.core.client.visualization.resolvers.managed.PreconfiguredVisualItemValueResolverFactoryProvider.FIXED_0_RESOLVER_FACTORY_ID;
import static org.thechiselgroup.choosel.core.client.visualization.resolvers.managed.PreconfiguredVisualItemValueResolverFactoryProvider.FIXED_1_RESOLVER_FACTORY_ID;
import static org.thechiselgroup.choosel.core.client.visualization.resolvers.managed.PreconfiguredVisualItemValueResolverFactoryProvider.FIXED_DATE_TODAY_FACTORY_ID;
import static org.thechiselgroup.choosel.core.client.visualization.resolvers.managed.PreconfiguredVisualItemValueResolverFactoryProvider.FIXED_STDBLUE_RESOLVER_FACTORY_ID;
import static org.thechiselgroup.choosel.core.client.visualization.resolvers.managed.PreconfiguredVisualItemValueResolverFactoryProvider.MAX_RESOLVER_FACTORY_ID;
import static org.thechiselgroup.choosel.core.client.visualization.resolvers.managed.PreconfiguredVisualItemValueResolverFactoryProvider.MIN_RESOLVER_FACTORY_ID;
import static org.thechiselgroup.choosel.core.client.visualization.resolvers.managed.PreconfiguredVisualItemValueResolverFactoryProvider.SUM_RESOLVER_FACTORY_ID;
import static org.thechiselgroup.choosel.core.client.visualization.resolvers.managed.PreconfiguredVisualItemValueResolverFactoryProvider.TEXT_PROPERTY_RESOLVER_FACTORY_ID;

import org.thechiselgroup.choosel.core.client.visualization.resolvers.managed.PreconfiguredVisualItemValueResolverFactoryProvider;

import com.google.inject.Inject;

public class PreconfiguredVisualItemResolverUIFactoryProvider extends
        DefaultVisualItemResolverUIFactoryProvider {

    @Inject
    public void registerFactories() {
        register(new VisualItemStatusIdUIControllerFactory());

        register(new ResourceCountResolverUIControllerFactory());
        register(new CalculationResolverUIControllerFactory(
                SUM_RESOLVER_FACTORY_ID));
        register(new CalculationResolverUIControllerFactory(
                AVERAGE_RESOLVER_FACTORY_ID));
        register(new CalculationResolverUIControllerFactory(
                MAX_RESOLVER_FACTORY_ID));
        register(new CalculationResolverUIControllerFactory(
                MIN_RESOLVER_FACTORY_ID));

        register(new FixedValueVisualItemResolverUIControllerFactory(
                FIXED_1_RESOLVER_FACTORY_ID));
        register(new FirstResourcePropertyResolverUIControllerFactory(
                TEXT_PROPERTY_RESOLVER_FACTORY_ID));

        // registering factories for ChooselWorkbecnchViewWindowContentProducers
        register(new FixedValueVisualItemResolverUIControllerFactory(
                FIXED_0_RESOLVER_FACTORY_ID));
        register(new FixedValueVisualItemResolverUIControllerFactory(
                FIXED_STDBLUE_RESOLVER_FACTORY_ID));
        register(new FixedValueVisualItemResolverUIControllerFactory(
                FIXED_DATE_TODAY_FACTORY_ID));
        register(new FixedValueVisualItemResolverUIControllerFactory(
                PreconfiguredVisualItemValueResolverFactoryProvider.FIXED_EMPTY_STRING_FACTORY_ID));
    }
}