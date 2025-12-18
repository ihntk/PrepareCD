module com.af.igor.prepcd.preparecd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.prefs;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.commons.collections4;
//    requires com.af.igor.prepcd.preparecd;

    opens com.af.igor.prepcd to javafx.fxml;
    exports com.af.igor.prepcd;
    opens com.af.igor.prepcd.controller;
    exports com.af.igor.prepcd.controller;
}