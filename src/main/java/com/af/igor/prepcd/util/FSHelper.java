package com.af.igor.prepcd.util;

import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FSHelper {
    private ObservableList<Path> list;
    private Path currentPath;

    public static FSHelper getInstance(ObservableList<Path> list) {
        return new FSHelper(list);
    }

    private FSHelper(ObservableList<Path> list) {
        this.list = list;
    }

    public void getFiles(Path path) throws IOException {
        Path previousPath = currentPath;
        currentPath = path.isAbsolute() ? path : currentPath.resolve(path);
        if (Files.isDirectory(currentPath)) {
            list.clear();
            list.add(currentPath.relativize(currentPath.getParent()));
            DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath);
            for (Path pathSt : stream) {
                if (!Files.isHidden(pathSt))
                    list.add(pathSt.getFileName());
            }
        } else currentPath = previousPath;
    }

    public void getCkdFiles(Path path) {
    }

    public void getPdfFiles(Path path) {
    }
}
