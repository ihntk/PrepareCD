package com.af.igor.prepcd.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;

public class FSHelper {
    private ObservableList<Path> list= FXCollections.observableArrayList();

    public static FSHelper getInstance(){
        return new FSHelper();
    }

    public ObservableList<Path> getList() {
        return list;
    }

    public void getFiles(Path path){
    }

    public void getCkdFiles(Path path){
    }

    public void getPdfFiles(Path path){
    }
}
