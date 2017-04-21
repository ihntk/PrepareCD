package com.af.igor.prepcd.util;

/**
 * Created by igor on 03.09.16.
 */
public enum MachinesCode {
    MC244(700),
    M2A(800),
    CE2A(1000),
    CE2B(1500),
    CE24A(2000),
    CE24B(2500),
    CE46XS(3000),
    L4(3050),
    CE46S(3500),
    L4A(3550),
    CE46A(4000),
    L5(4050),
    CE46B(4500),
    L5A(4550),
    CE6(5000),
    L6(5050),
    CE68A(5500),
    L6A(5525),
    L6B(5550),
    CE68S(6500),
    CE68B(7000),
    CE680A(7500),
    CE680B(8000),
    OPC55(10500),
    OPC75(11000),
    OPC90(11500),
    OPC110(12000),
    OPC132(12500),
    OPC160(13000),
    OPC200(13500);


    private String code;

    private MachinesCode(int code) {
        this.code = String.valueOf(code);
    }

    @Override
    public String toString() {
        return code;
    }
}
