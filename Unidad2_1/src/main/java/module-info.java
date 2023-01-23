module edu.utez.unidad2_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires db4o;


    opens edu.utez.unidad2_1 to javafx.fxml;
    opens edu.utez.unidad2_1.controllers to javafx.fxml;
    opens edu.utez.unidad2_1.models to db4o;
    exports edu.utez.unidad2_1;
    exports edu.utez.unidad2_1.controllers;
    exports edu.utez.unidad2_1.models;
}