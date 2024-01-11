module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.jsoup;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
//    requires org.apache.logging.log4j;
    requires org.apache.commons.text;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.firefox_driver;
    requires org.seleniumhq.selenium.support;

    requires dev.failsafe.core;
    requires org.apache.commons.lang3;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.model;
}