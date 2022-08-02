package com.af.igor.prepcd.util;

public enum AdditionalOptions {
    BASE_FRAME("Base Frame", 0),
    SURBAISE("Surbaise", 1),
    INDUSTRIALIZATION("Industrialization", 2);

    private final String option;

    private final int increment;

    private AdditionalOptions(String option, int increment) {
        this.option = option;
        this.increment = increment;
    }

    public int getIncrement() {
        return increment;
    }

}
