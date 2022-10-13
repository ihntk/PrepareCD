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

    OPC45_8(10100),
    OPC45_10(10200),
    OPC55_8(10400),
    OPC55_10(10500),
    OPC75_8(10900),
    OPC75_10(11000),
    OPC90_8(11400),
    OPC90_10(11500),
    OPC110_8(119000),
    OPC110_10(12000),
    OPC132_8(12400),
    OPC132_10(12500),
    OPC160_8(12900),
    OPC160_10(13000),
    OPC200_8(13400),
    OPC200_10(13500),
    OPC280_8(14200),
    OPC280_10(14300),
    OPC315_8(14400),
    OPC315_10(14500),
    OPC355_8(14900),
    OPC355_10(15000);


    private String code;

    private MachinesCode(int code) {
        this.code = String.valueOf(code);
    }

    @Override
    public String toString() {
        return code;
    }
}
