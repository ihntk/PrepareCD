package com.af.igor.prepcd.util;

public enum BaseDrawingPaths {
    I("I (Installation layout)/I - HO"),
    E("E (Machines layout)/E - HO"),
    FS("FS (Flow sheet)/FS HO"),
    M10("M (Mounting drawings)/M - HO/M10 - AIR COMPRESSEUR"),
    M20("M (Mounting drawings)/M - HO/M20 - EAU COMPRESSEUR"),
    M30("M (Mounting drawings)/M - HO/M30 - SORTIE SECHEUR"),
    M40("M (Mounting drawings)/M - HO/M40 - COLLECTEUR PURGE"),
    M50("M (Mounting drawings)/M - HO/M50 - AERO ET TOUR"),
    M60("M (Mounting drawings)/M - HO/M60 - RESERVOIR HAUTE PRESSION APRES SECHEUR"),
    M70("M (Mounting drawings)/M - HO/M70 - RESERVOIR BASSE PRESSION APRES SECHEUR"),
    M80("M (Mounting drawings)/M - HO/"),
    M90("M (Mounting drawings)/M - HO/M90 - MACHINE DECAPITEE"),
    M100("M (Mounting drawings)/M - HO/M100- RESERVOIR AVANT SECHEUR"),
    M110("M (Mounting drawings)/M - HO/"),
    M120("M (Mounting drawings)/M - HO/"),
    M130("M (Mounting drawings)/M - HO/"),
    M140("M (Mounting drawings)/M - HO/"),
    M210("M (Mounting drawings)/M - HO/M210 - ENERGY AUDIT"),
    M220("M (Mounting drawings)/M - HO/M220 - DEW POINT CHECK");

    private String path;

    private BaseDrawingPaths(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
