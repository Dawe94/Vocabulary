module com.vocabulary {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.vocabulary to javafx.fxml;
    exports com.vocabulary;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
}
