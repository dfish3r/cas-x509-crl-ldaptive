/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.cas.server.authentication;

import org.jasig.services.persondir.IPersonAttributeDao;
import org.jasig.services.persondir.IPersonAttributes;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractAttributePrincipalTests {

    private static final String NAME = "battags";

    private static final String NO_ATTRIBUTE_NAME = "no_attributes";

    private static final String NAME_NOT_FOUND = "name_not_found";

    private static final String NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE = "notNullAttributeOneValue";

    private static final String NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE = "notNullAttributeMultiValue";

    private static final String NULL_ATTRIBUTE_NAME = "nullAttributeName";

    private static final String ONE_VALUE = "foo";

    private static final String FIRST_VALUE = "hello";

    private AttributePrincipal nameWithAttributes;

    private AttributePrincipal nameWithoutAttributes;

    private AttributePrincipal nameNotFound;

    protected abstract AttributePrincipal getAttributePrincipal(final String name, final IPersonAttributeDao dao);

    @Before
    public final void setUp() throws Exception {
        final IPersonAttributeDao dao = mock(IPersonAttributeDao.class);

        final IPersonAttributes noAttributes = mock(IPersonAttributes.class);

        when(noAttributes.getAttributes()).thenReturn(Collections.<String, List<Object>>emptyMap());
        when(noAttributes.getAttributeValue(NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE)).thenReturn(null);
        when(noAttributes.getAttributeValue(NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE)).thenReturn(null);
        when(noAttributes.getAttributeValue(NULL_ATTRIBUTE_NAME)).thenReturn(null);

        when(noAttributes.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE)).thenReturn(null);
        when(noAttributes.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE)).thenReturn(null);
        when(noAttributes.getAttributeValues(NULL_ATTRIBUTE_NAME)).thenReturn(null);

        final IPersonAttributes attributes = mock(IPersonAttributes.class);

        final Map<String, List<Object>> map = new HashMap<String, List<Object>>();
        map.put(NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE, Arrays.asList((Object) ONE_VALUE));
        map.put(NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE, Arrays.asList((Object) ONE_VALUE, "foo"));

        when(attributes.getAttributes()).thenReturn(map);
        when(attributes.getAttributeValue(NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE)).thenReturn(ONE_VALUE);
        when(attributes.getAttributeValue(NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE)).thenReturn(FIRST_VALUE);
        when(attributes.getAttributeValue(NULL_ATTRIBUTE_NAME)).thenReturn(null);

        when(attributes.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE)).thenReturn(Arrays.asList((Object) ONE_VALUE));
        when(attributes.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE)).thenReturn(Arrays.asList((Object) FIRST_VALUE, "foo"));
        when(attributes.getAttributeValues(NULL_ATTRIBUTE_NAME)).thenReturn(null);

        when(dao.getPerson(NAME_NOT_FOUND)).thenReturn(null);
        when(dao.getPerson(NO_ATTRIBUTE_NAME)).thenReturn(noAttributes);
        when(dao.getPerson(NAME)).thenReturn(attributes);

        this.nameWithAttributes = getAttributePrincipal(NAME, dao);
        this.nameNotFound = getAttributePrincipal(NAME_NOT_FOUND, dao);
        this.nameWithoutAttributes = getAttributePrincipal(NO_ATTRIBUTE_NAME, dao);
    }

    @Test
    public final void correctName() {
        assertEquals(NAME, this.nameWithAttributes.getName());
        assertEquals(NO_ATTRIBUTE_NAME, this.nameWithoutAttributes.getName());
        assertEquals(NAME_NOT_FOUND, this.nameNotFound.getName());
    }

    @Test
    public final void attributeValues() {
        // person that was not found
        assertNull(this.nameNotFound.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE));
        assertNull(this.nameNotFound.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE));
        assertNull(this.nameNotFound.getAttributeValues(NULL_ATTRIBUTE_NAME));

        // no attribute dude
        assertNull(this.nameWithoutAttributes.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE));
        assertNull(this.nameWithoutAttributes.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE));
        assertNull(this.nameWithoutAttributes.getAttributeValues(NULL_ATTRIBUTE_NAME));

        // attributes!!
        assertNotNull(this.nameWithAttributes.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE));
        assertEquals(1, this.nameWithAttributes.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE).size());
        assertNotNull(this.nameWithAttributes.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE));
        assertEquals(2, this.nameWithAttributes.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE).size());
        assertNull(this.nameWithAttributes.getAttributeValues(NULL_ATTRIBUTE_NAME));
    }

    @Test
    public final void attributeValue() {
        // person that was not found
        assertNull(this.nameNotFound.getAttributeValue(NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE));
        assertNull(this.nameNotFound.getAttributeValue(NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE));
        assertNull(this.nameNotFound.getAttributeValue(NULL_ATTRIBUTE_NAME));

        // no attribute dude
        assertNull(this.nameWithoutAttributes.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE));
        assertNull(this.nameWithoutAttributes.getAttributeValues(NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE));
        assertNull(this.nameWithoutAttributes.getAttributeValues(NULL_ATTRIBUTE_NAME));

        // attributes!!
        final Object oneValue = this.nameWithAttributes.getAttributeValue(NOT_NULL_ATTRIBUTE_NAME_ONE_VALUE);
        assertNotNull(oneValue);
        assertEquals(ONE_VALUE, oneValue);

        final Object firstValue = this.nameWithAttributes.getAttributeValue(NOT_NULL_ATTRIBUTE_NAME_MULTI_VALUE);
        assertEquals(FIRST_VALUE, firstValue);

        assertNull(this.nameWithAttributes.getAttributeValues(NULL_ATTRIBUTE_NAME));
    }

    @Test
    public final void attributes() {
        assertNotNull(this.nameNotFound.getAttributes());
        assertTrue(this.nameNotFound.getAttributes().isEmpty());

        assertNotNull(this.nameWithoutAttributes.getAttributes());
        assertTrue(this.nameWithoutAttributes.getAttributes().isEmpty());

        assertNotNull(this.nameWithAttributes.getAttributes());
        assertFalse(this.nameWithAttributes.getAttributes().isEmpty());
    }
}
