package com.af.igor.prepcd.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum CdLangFiles {
    ANGLAIS("Anglais", "01_ANGLAIS"),
    FRANCAIS("Français", "02_FRANCAIS"),
    CHINOIS("Chinois", "03_CHINOIS"),
    RUSSE("Russe", "04_RUSSE"),
    ALLEMAND("Allemand", "05_ALLEMAND"),
    ESPAGNOL("Espagnol", "06_ESPAGNOL"),
    POLONAIS("Polonais", "07_POLONAIS"),
    ITALIEN("Italien", "08_ITALIEN"),
    PORTUGAIS("Portugais", "09_PORTUGAIS"),
    LITHUANIEN("Lituanien", "10_LITHUANIEN"),
    TURC("Turc", "11_TURC"),
    NEERLANDAIS("Néerlandai", "12_NEERLANDAIS"),
    SLOVAQUE("Slovaque", "13_SLOVAQUE"),
    CROATE("Croate", "14_CROATE"),
    ROUMAIN("Roumain", "16_ROUMAIN"),
    GREC("Grec", "17_GREC"),
    TCHEQUE("Tchèque", "18_TCHEQUE"),
    HONGROIS("Hongrois", "19_HONGROIS"),
    NORVEGIEN("Norvégien", "20_NORVEGIEN"),
    FINNOIS("Finlandais", "21_FINNOIS");

    private static String[] regularFileNames = {"css", "images", "js", "index.html", "runfile.exe"};

    private final String description;
    private String fileName;

    private CdLangFiles(String description, String fileName) {
        this.description = description;
        this.fileName = fileName;
    }

    public static Path[] getRegularFiles() {
        Path[] regularFiles = new Path[regularFileNames.length];
        for (int i = 0; i < regularFileNames.length; i++) {
            regularFiles[i] = Paths.get(regularFileNames[i]);
        }
        return regularFiles;
    }

    public String getDescription() {
        return description;
    }

    public String getFileName() {
        return fileName;
    }
}
