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

package org.jasig.cas.server.session;

import java.util.Collections;
import java.util.List;

/**
 * Represents the results of a pagination request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class PaginationResult<T> {

    private static final PaginationResult EMPTY_PAGINATION_RESULT = new PaginationResult(Collections.emptyList(), 0, 0, 0);

    private final int totalCount;

    private final int count;

    private final int numberOfPages;

    private final int currentPage;

    private final List<T> items;

    public PaginationResult(final List<T> items, final int totalCount, final int numberOfPages, final int currentPage) {
        this.items = items;
        this.totalCount = totalCount;
        this.count = items.size();
        this.numberOfPages = numberOfPages;
        this.currentPage = currentPage;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public int getCount() {
        return this.count;
    }

    public int getNumberOfPages() {
        return this.numberOfPages;
    }

    public List<T> getItems() {
        return this.items;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public static <T> PaginationResult<T> emptyPaginationResult() {
        return (PaginationResult<T>) EMPTY_PAGINATION_RESULT;
    }
}
