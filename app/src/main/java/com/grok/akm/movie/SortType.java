package com.grok.akm.movie;

public enum SortType {

    MOST_POPULAR(0), HIGHEST_RATED(1), FAVORITES(2), NEWEST(3);

    private final int value;

    SortType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
