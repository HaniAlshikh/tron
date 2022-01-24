module de.alshikh.haw.tron {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires org.json;

    exports de.alshikh.haw.tron;
    opens de.alshikh.haw.tron to javafx.fxml;
    exports de.alshikh.haw.tron.app.views.view_library;
    exports de.alshikh.haw.tron.app.views.game.overlayes;
    opens de.alshikh.haw.tron.app.views.game.overlayes to javafx.fxml;
    exports de.alshikh.haw.tron.app.models.game.data.datatypes;
    exports de.alshikh.haw.tron.app.models.game.data.entities;
    opens de.alshikh.haw.tron.app.models.game.data.datatypes to javafx.fxml;
    opens de.alshikh.haw.tron.app.models.game.data.entities to javafx.fxml;
    exports de.alshikh.haw.tron.app.models.game.data.exceptions;
    opens de.alshikh.haw.tron.app.models.game.data.exceptions to javafx.fxml;
    exports de.alshikh.haw.tron.app.models.game;
    opens de.alshikh.haw.tron.app.models.game to javafx.fxml;
    exports de.alshikh.haw.tron.app.controllers.game.inputhandlers;
    opens de.alshikh.haw.tron.app.controllers.game.inputhandlers to javafx.fxml;
}
