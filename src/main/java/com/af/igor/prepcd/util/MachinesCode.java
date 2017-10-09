package com.af.igor.prepcd.util;

/**
 * Created by igor on 03.09.16.
 */
public enum MachinesCode {
    MC244(700),
    M2XS(800),
    M2S(820),
    M2A(840),
    M2B(860),

    CE2A(1000),
    CE2B(1500),

    CE24XS(1800),
    CE24S(2000),
    CE24A(2400),
    CE24B(2500),
    CE24C(2550),

    L3A(2600),
    L3B(2700),

    CE46XS(3000),
    CE46S(3500),
    CE46A(4000),
    CE46B(4500),
    CE46C(4400),

    L4(3050),
    L4A(3550),

    L5XS(4050),
    L5S(4200),
    L5A(4550),
    L5B(4575),

    CE6(5000),
    CE68A(5500),

    L6(5050),
    L6A(5100),
    L6B(5200),
    L7(5300),

    CE68S(6500),
    CE68B(7000),
    CE680A(7500),
    CE680B(8000),

    L8A(7100),
    L8B(7200),
    L9A(9000),
    L9B(9200),

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
