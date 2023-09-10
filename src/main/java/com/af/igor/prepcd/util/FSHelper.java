package com.af.igor.prepcd.util;

import com.af.igor.prepcd.MainApp;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FSHelper {
    private MainApp app = MainApp.getInstance();
    private ObservableList<Path> list;
    private Path currentPath;

    public static void copyDirPath(Path sourcePath, Path targetPath) throws IOException {
        Files.walk(sourcePath).forEach(source -> {
            try {
                Files.copy(source, targetPath.resolve(sourcePath.relativize(source)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public static FSHelper getInstance(ObservableList<Path> list) {
        return new FSHelper(list);
    }

    private FSHelper(ObservableList<Path> list) {
        this.list = list;
    }

    public void getFiles(Path path) throws IOException {
        if (path == null)
            return;
        Path previousPath = currentPath;

        if (path.toString().startsWith("..  ["))
            path = Paths.get("..");

        currentPath = path.isAbsolute() ? path : currentPath.resolve(path).normalize();

        if (Files.isDirectory(currentPath)) {
            list.clear();
            list.add(Paths.get(currentPath.relativize(currentPath.getParent()) + "  [" + currentPath.getFileName() + "]"));
            DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath);
            try {
                for (Path pathSt : stream) {
                    if (!Files.isHidden(pathSt))
                        list.add(pathSt.getFileName());
                }
            } catch (AccessDeniedException e) {
                app.logger.log("AccessDeniedException - file is hidden");
            }
        } else currentPath = previousPath;
    }

    public void getFiles(List<String> stringList) {
        list.clear();
        for (int i = 0; i < stringList.size(); i++) {
            list.add(Paths.get(stringList.get(i)));
        }
    }

    public void getCkdFiles(Path path) {
    }

    public void getPdfFiles(Path path) {
    }
}
