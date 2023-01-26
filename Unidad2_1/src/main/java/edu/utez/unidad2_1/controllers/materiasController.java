package edu.utez.unidad2_1.controllers;

import com.db4o.ObjectSet;
import edu.utez.unidad2_1.dao.materiaDao;
import edu.utez.unidad2_1.models.Materia;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class materiasController implements Initializable {
    @FXML
    private Button btnGoToAlumnos;
    @FXML
    private Button btnGuardarCambios;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField txtMateria;
    @FXML
    private TableView<Materia> tablrMaterias;
    private materiaDao td;
    private ContextMenu cmOpciones;
    private Materia materiaSeleccionada;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.td = new materiaDao();
        cargarMaterias();
        cmOpciones = new ContextMenu();
        MenuItem miEditar = new MenuItem("editar");
        MenuItem miEliminar = new MenuItem("Eliminar");
        cmOpciones.getItems().addAll(miEditar);
        cmOpciones.getItems().addAll(miEliminar);

        tablrMaterias.setContextMenu(cmOpciones);
        miEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int index = tablrMaterias.getSelectionModel().getSelectedIndex();
                materiaSeleccionada =  tablrMaterias.getItems().get(index);
                txtMateria.setText(materiaSeleccionada.getNombre());

                btnCancelar.setDisable(false);
            }
        });
        miEliminar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int index = tablrMaterias.getSelectionModel().getSelectedIndex();
                materiaSeleccionada =  tablrMaterias.getItems().get(index);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmacion");
                alert.setHeaderText(null);
                alert.setContentText("Realmente deseas eliminar "+materiaSeleccionada.getNombre()+"?");
                alert.initStyle(StageStyle.UTILITY);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK){
                    boolean rsp = td.delete(materiaSeleccionada);
                    if (rsp){
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Exito");
                        alert.setHeaderText(null);
                        alert.setContentText("Los datos de "+materiaSeleccionada.getNombre()+" se han eliminado");
                        alert.initStyle(StageStyle.UTILITY);
                        alert.showAndWait();
                        cleanForm();
                        cargarMaterias();
                        materiaSeleccionada = null;
                    }else{
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("No se pudo eliminar :(");
                        alert.initStyle(StageStyle.UTILITY);
                        alert.showAndWait();
                    }
                }else{
                    materiaSeleccionada = null;
                }
            }
        });
    }

    @FXML
    void btnGuardarOnAction(ActionEvent event) {
        if (materiaSeleccionada == null){
            Materia mat = new Materia();
            mat.setNombre(txtMateria.getText());
            boolean rsp = td.insert(mat);
            if (rsp){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Exito");
                alert.setHeaderText(null);
                alert.setContentText("Los datos de "+mat.getNombre()+" se han registrado");
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();
                cleanForm();
                cargarMaterias();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo guardar :(");
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();
            }
        }else{
            materiaSeleccionada.setNombre(txtMateria.getText());
            boolean rsp = td.update(materiaSeleccionada);
            if (rsp){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Exito");
                alert.setHeaderText(null);
                alert.setContentText("Los datos de "+materiaSeleccionada.getNombre()+" se han guardado");
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();
                cleanForm();
                cargarMaterias();
                materiaSeleccionada = null;
                btnCancelar.setDisable(true);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo guardar :(");
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();
                materiaSeleccionada = null;
            }
        }
    }

    @FXML
    void btnCancelarOnAction(ActionEvent event) {
        materiaSeleccionada = null;
        btnCancelar.setDisable(true);
        cleanForm();
    }

    private void cleanForm (){
        txtMateria.setText("");
    }

    public void cargarMaterias(){
        tablrMaterias.getItems().clear();
        tablrMaterias.getColumns().clear();

        ObjectSet<Materia> consultaMat = td.consultaGen();

        ObservableList<Materia> datos = FXCollections.observableArrayList(consultaMat);

        TableColumn claveCol = new TableColumn("Clave");
        claveCol.setCellValueFactory(new PropertyValueFactory("clave"));

        TableColumn materiaCol = new TableColumn("Materia");
        materiaCol.setCellValueFactory(new PropertyValueFactory("nombre"));

        tablrMaterias.setItems(datos);
        tablrMaterias.getColumns().addAll(claveCol);
        tablrMaterias.getColumns().addAll(materiaCol);


    }

    @FXML
    void btnGoYoAlumnosOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        //Cargas el FXML que queres que abra en un Parent
        URI uri = Paths.get("src/main/resources/vistas/alumnoFxml.fxml").toAbsolutePath().toUri();
        System.out.println(uri);
        Parent root = FXMLLoader.load(uri.toURL());
        Scene scene = new Scene(root);;
        stage.setScene(scene);
        stage.show();

        //Cerramos la ventana anterior de Login. La obtenemos a partir de un control (Button)
        Stage old = (Stage) btnGoToAlumnos.getScene().getWindow();
        old.close();
    }
}
