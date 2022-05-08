package com.servlet.cinema.framework.data;


/**
 * The class that is used for pagination in sql.
 */
public class Pageable {
    private Sort sort;
    private Integer page;
    private Integer pageCapacity;

    private Pageable(Integer page, Integer pageCapacity, Sort sort) {
        this.page = page;
        this.pageCapacity = pageCapacity;
        this.sort = sort;
    }

    ;

    /**
     * Creates a new Pageable for the first page (page number 0) given pageSize .
     * Params:
     * pageSize – the size of the page to be returned, must be greater than 0.
     * @param page  - zero-based page index, must not be negative.
     * @param pageCapacity - the size of the page to be returned, must be greater than 0.
     * @param sort - must not bu null.
     * @return a new Pageable.
     */
    public static Pageable of(Integer page, Integer pageCapacity, Sort sort) {
        return new Pageable(page, pageCapacity, sort);
    }

    /**
     * @return a part of SQL quarry.
     */
    @Override
    public String toString() {
        return new StringBuilder(" ORDER BY ")
                .append(sort.getSqlOrder())
                .append(" LIMIT ")
                .append(pageCapacity)
                .append(" OFFSET ")
                .append(page * pageCapacity)
                .toString();
    }
}
