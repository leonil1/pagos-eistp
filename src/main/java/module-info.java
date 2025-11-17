module com.iestpdj.iestpdjpagos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires org.apache.pdfbox;
    requires kernel;
    requires layout;
    requires org.slf4j;
    requires io;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.iestpdj.iestpdjpagos to javafx.fxml;
    exports com.iestpdj.iestpdjpagos;

    // Exporta el paquete para que otros módulos puedan usarlo
    exports com.iestpdj.iestpdjpagos.controller;

    // Permite a JavaFX usar reflexión en los controladores
    opens com.iestpdj.iestpdjpagos.controller to javafx.fxml;

    // Si tienes otros paquetes con modelos o vistas
    opens com.iestpdj.iestpdjpagos.model to javafx.base;
}