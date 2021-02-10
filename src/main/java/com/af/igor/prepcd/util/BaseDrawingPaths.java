package com.af.igor.prepcd.util;

public enum BaseDrawingPaths {
    I("002 - Plan d'installation"),
    E("001 - Plan d'ensemble"),
    FS("003 - Flow sheet"),
    M10("004 - Montage/M10 - AIR COMPRESSEUR"),
    M20("004 - Montage/M20 - EAU COMPRESSEUR"),
    M30("004 - Montage/M30 - SORTIE SECHEUR"),
    M40("004 - Montage/M40 - COLLECTEUR PURGE"),
    M50("004 - Montage/M50 - AERO ET TOUR"),
    M60("004 - Montage/M60 - RESERVOIR HAUTE PRESSION APRES SECHEUR"),
    M70("004 - Montage/M70 - RESERVOIR BASSE PRESSION APRES SECHEUR"),
    M80("004 - Montage/M80 - LIAISON RBACK"),
    M90("004 - Montage/M90 - MACHINE DECAPITEE"),
    M100("004 - Montage/M100 - RESERVOIR AVANT SECHEUR"),
    M110("004 - Montage/M110 - RESERVOIR RBACK"),
    M120("004 - Montage/M120 - RESERVOIRS UCL"),
    M130("004 - Montage/M130 - REFROISISSEMENT EAU ECHANGEUR A PLAQUES"),
    M140("004 - Montage/M140 - ALL IN ONE");

    private String path;

    private BaseDrawingPaths(String path){
        this.path=path;
    }
    @Override
    public String toString() {
        return path;
    }
}
