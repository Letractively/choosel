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
package org.thechiselgroup.choosel.core.shared.test.matchers.collections;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;

public final class LightweightCollectionContentMatcher<T> extends
        TypeSafeMatcher<LightweightCollection<T>> {

    private final LightweightCollection<T> expected;

    public LightweightCollectionContentMatcher(LightweightCollection<T> expected) {
        this.expected = expected;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("contains exactly").appendValue(expected);
    }

    @Override
    public boolean matchesSafely(LightweightCollection<T> set) {
        if (set.size() != expected.size()) {
            return false;
        }

        return set.toList().containsAll(expected.toList());
    }
}