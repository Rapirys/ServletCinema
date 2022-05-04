package com.servlet.cinema.framework.data;

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

    public static Pageable of(Integer page, Integer pageCapacity, Sort sort) {
        return new Pageable(page, pageCapacity, sort);
    }

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
