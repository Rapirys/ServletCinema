package com.servlet.cinema.framework.data;

public class Sort {

    private StringBuilder sqlOrder;
    private Sort(String sqlOrder){
        this.sqlOrder = new StringBuilder(sqlOrder);
    }

    public static Sort by(String column){
        StringBuilder sb = new StringBuilder(" ");
        sb.append(column).append(" ");
        return new Sort(sb.toString());
    }

    public Sort descending() {
        sqlOrder.append(Direction.DESC);
        return this;
    }

    public Sort ascending() {
        sqlOrder.append(Direction.ASC);
        return this;
    }

    public Sort and(Sort sort){
        this.sqlOrder.append(" , ").append(sort.sqlOrder).toString();
        return this;
    }

    @Override
    public String toString() {
        return " ORDER BY" + sqlOrder.toString();

    }

    public static enum Direction {
        ASC,
        DESC;
    }

    public String getSqlOrder() {
        return sqlOrder.toString();
    }
}
