package com.servlet.cinema.framework.data;

/**
 * Sort option for queries.
 * You have to provide at least a list of properties to sort for
 * that must not include null or empty strings.
 * The direction defaults to ACS.
 */
public class Sort {

    private final StringBuilder sqlOrder;

    private Sort(String sqlOrder) {
        this.sqlOrder = new StringBuilder(sqlOrder);
    }


    /**
     * @param column – must not be null.
     * @return new Sort sorted by colum.
     */
    public static Sort by(String column) {
        return new Sort(" " + column + " ");
    }

    /**
     * @return a new Sort with the current setup but descending order direction.
     */
    public Sort descending() {
        sqlOrder.append(Direction.DESC);
        return this;
    }

    /**
     * @return a new Sort with the current setup but descending order ascending.
     */
    public Sort ascending() {
        sqlOrder.append(Direction.ASC);
        return this;
    }


    /**
     * @param sort – must not be null.
     * @return a new Sort consisting of the Sort.Orders of the current Sort combined with the given ones.
     */
    public Sort and(Sort sort) {
        this.sqlOrder.append(" , ").append(sort.sqlOrder);
        return this;
    }

    /**
     * @return a part of SQL quarry.
     */
    @Override
    public String toString() {
        return " ORDER BY" + sqlOrder;

    }

    public enum Direction {
        ASC,
        DESC
    }

    public String getSqlOrder() {
        return sqlOrder.toString();
    }
}
