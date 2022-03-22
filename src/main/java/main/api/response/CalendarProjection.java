package main.api.response;

public interface CalendarProjection {
    String getDate();
    long getCount();
    void setDate(long date);
    void setCount(long count);
}
