module de.alshikh.haw.tron {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires org.json;
    requires view.library;

    exports de.alshikh.haw.tron;
    opens de.alshikh.haw.tron to javafx.fxml;
    exports de.alshikh.haw.tron.app.model.lobby;
    exports de.alshikh.haw.tron.app.model.lobby.datatypes;
    exports de.alshikh.haw.tron.manager.overlays;
    exports de.alshikh.haw.tron.app;
    opens de.alshikh.haw.tron.app to javafx.fxml;
}
