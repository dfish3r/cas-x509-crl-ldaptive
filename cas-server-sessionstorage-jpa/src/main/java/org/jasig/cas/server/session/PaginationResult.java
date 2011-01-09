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
