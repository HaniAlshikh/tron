module de.alshikh.haw.tron {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires org.json;
    requires view.library;

    exports de.alshikh.haw.tron.manager;
    opens de.alshikh.haw.tron.manager to javafx.fxml;
    exports de.alshikh.haw.tron.app.model.lobby;
    exports de.alshikh.haw.tron.manager.overlays;
    exports de.alshikh.haw.tron.app.controller.game.helpers;
    opens de.alshikh.haw.tron.app to javafx.fxml;
    exports de.alshikh.haw.tron.app.model.lobby.data.entities;
    exports de.alshikh.haw.tron.app.model.lobby.data.datatypes;
    exports de.alshikh.haw.tron.app.model.game.data.entities;
    exports de.alshikh.haw.tron.app.controller.game;
    exports de.alshikh.haw.tron.app.model.game.data.datatypes;
    exports de.alshikh.haw.tron.app.view.game;
}
