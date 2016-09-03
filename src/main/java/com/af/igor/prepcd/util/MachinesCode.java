package com.af.igor.prepcd.util;

/**
 * Created by igor on 03.09.16.
 */
public enum MachinesCode {
    _46XS(3000),
    _46S(3500),
    _46A(4000),
    _46B(4500);


    private String code;

    MachinesCode(int code) {
        this.code = String.valueOf(code);
    }

    @Override
    public String toString() {
        return code;
    }
}
