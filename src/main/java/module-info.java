module de.alshikh.haw.tron {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires org.json;
    requires view.library;

    exports de.alshikh.haw.tron;
    opens de.alshikh.haw.tron to javafx.fxml;
    exports de.alshikh.haw.tron.app.models.lobby;
    exports de.alshikh.haw.tron.app.models.lobby.datatypes;
}
