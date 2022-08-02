package com.af.igor.prepcd.util;

public enum AddutuinalOptions {
    BASE_FRAME("Base Frame"),
    SURBAISE("Surbaise"),
    INDUSTRIALIZATION("Industrialization");

    private final String option;

    private AddutuinalOptions(String option){
        this.option = option;
    }

    @Override
    public String toString() {
        return option;
    }
}
