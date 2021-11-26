module de.alshikh.haw.tron {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;

    opens de.alshikh.haw.tron.client to javafx.fxml;
    opens de.alshikh.haw.tron.client.views.overlayes to javafx.fxml;

    exports de.alshikh.haw.tron.client;
    exports de.alshikh.haw.tron.client.views.view_library;
    exports de.alshikh.haw.tron.client.views.overlayes;
    exports de.alshikh.haw.tron.client.models.data.datatypes;
    exports de.alshikh.haw.tron.client.models.data.entities;
    opens de.alshikh.haw.tron.client.models.data.datatypes to javafx.fxml;
    opens de.alshikh.haw.tron.client.models.data.entities to javafx.fxml;
    exports de.alshikh.haw.tron.client.models.data.exceptions;
    opens de.alshikh.haw.tron.client.models.data.exceptions to javafx.fxml;
    exports de.alshikh.haw.tron.client.models;
    opens de.alshikh.haw.tron.client.models to javafx.fxml;
    exports de.alshikh.haw.tron.client.models.InputHandler;
    opens de.alshikh.haw.tron.client.models.InputHandler to javafx.fxml;
    exports de.alshikh.haw.tron.client.common.data.entites;
    opens de.alshikh.haw.tron.client.common.data.entites to javafx.fxml;
}
