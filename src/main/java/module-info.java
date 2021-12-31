module de.alshikh.haw.tron {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires org.json;

    exports de.alshikh.haw.tron.client;
    opens de.alshikh.haw.tron.client to javafx.fxml;
    exports de.alshikh.haw.tron.client.views.view_library;
    exports de.alshikh.haw.tron.client.views.game.overlayes;
    opens de.alshikh.haw.tron.client.views.game.overlayes to javafx.fxml;
    exports de.alshikh.haw.tron.client.models.game.data.datatypes;
    exports de.alshikh.haw.tron.client.models.game.data.entities;
    opens de.alshikh.haw.tron.client.models.game.data.datatypes to javafx.fxml;
    opens de.alshikh.haw.tron.client.models.game.data.entities to javafx.fxml;
    exports de.alshikh.haw.tron.client.models.game.data.exceptions;
    opens de.alshikh.haw.tron.client.models.game.data.exceptions to javafx.fxml;
    exports de.alshikh.haw.tron.client.models.game;
    opens de.alshikh.haw.tron.client.models.game to javafx.fxml;
    exports de.alshikh.haw.tron.client.controllers.game.inputhandlers;
    opens de.alshikh.haw.tron.client.controllers.game.inputhandlers to javafx.fxml;
}
