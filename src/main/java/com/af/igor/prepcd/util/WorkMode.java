package com.af.igor.prepcd.util;

public enum WorkMode {
    GENERAL("General"),
    REMOTE("Remote"),
    OFFLINE("Offline");

    private final String displayName;

    WorkMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
