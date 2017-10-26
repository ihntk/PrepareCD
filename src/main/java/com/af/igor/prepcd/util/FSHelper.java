package com.af.igor.prepcd.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FSHelper {
    private ObservableList<Path> list = FXCollections.observableArrayList();

    public static FSHelper getInstance() {
        return new FSHelper();
    }

    public ObservableList<Path> getList() {
        return list;
    }

    public void getFiles(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            DirectoryStream<Path> stream = Files.newDirectoryStream(path);
            for (Path pathSt : stream) {
                if (!Files.isHidden(pathSt))
                    list.add(pathSt.getFileName());
            }
        }
        // else call alert window with message: path is not directory
    }

    public void getCkdFiles(Path path) {
    }

    public void getPdfFiles(Path path) {
    }
}
