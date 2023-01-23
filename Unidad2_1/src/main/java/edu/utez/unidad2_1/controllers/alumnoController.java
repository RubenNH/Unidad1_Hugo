package edu.utez.unidad2_1.controllers;

import com.db4o.ObjectSet;
import edu.utez.unidad2_1.dao.alumnoDao;
import edu.utez.unidad2_1.models.Alumno;
import edu.utez.unidad2_1.models.Materia;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

public class alumnoController implements Initializable {
    @FXML
    private Button btnGoToMaterias;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidos;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextField txtEdad;
    @FXML
    private Button btnCancelar;
    @FXML
    private TableView<Alumno> tableAlumno;
    private alumnoDao td;
    private ContextMenu cmOpciones;
    private Alumno alumnoSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarAlumnos();

        this.td = new alumnoDao();
        cmOpciones = new ContextMenu();
        MenuItem miEditar = new MenuItem("editar");
        MenuItem miEliminar = new MenuItem("Eliminar");
        cmOpciones.getItems().addAll(miEditar);
        cmOpciones.getItems().addAll(miEliminar);

        tableAlumno.setContextMenu(cmOpciones);
        miEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int index = tableAlumno.getSelectionModel().getSelectedIndex();
                alumnoSeleccionado =  tableAlumno.getItems().get(index);
                txtNombre.setText(alumnoSeleccionado.getNombres());
                txtApellidos.setText(alumnoSeleccionado.getApellidos());
                txtEdad.setText(String.valueOf(alumnoSeleccionado.getEdad()));

                btnCancelar.setDisable(false);
            }
        });
        miEliminar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int index = tableAlumno.getSelectionModel().getSelectedIndex();
                alumnoSeleccionado =  tableAlumno.getItems().get(index);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmacion");
                alert.setHeaderText(null);
                alert.setContentText("Realmente deseas eliminar "+alumnoSeleccionado.getNombres()+"?");
                alert.initStyle(StageStyle.UTILITY);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK){
                    boolean rsp = td.delete(alumnoSeleccionado);
                    if (rsp){
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Exito");
                        alert.setHeaderText(null);
                        alert.setContentText("Los datos de "+alumnoSeleccionado.getNombres()+" se han eliminado");
                        alert.initStyle(StageStyle.UTILITY);
                        alert.showAndWait();
                        cleanForm();
                        cargarAlumnos();
                        alumnoSeleccionado = null;
                    }else{
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("No se pudo eliminar :(");
                        alert.initStyle(StageStyle.UTILITY);
                        alert.showAndWait();
                    }
                }else{
                    alumnoSeleccionado = null;
                }
            }
        });
    }
    @FXML
    void btnGuardarOnAction(ActionEvent event) {
        if (alumnoSeleccionado == null){
            Alumno alm = new Alumno();
            alm.setNombres(txtNombre.getText());
            alm.setApellidos(txtApellidos.getText());
            alm.setEdad(Integer.parseInt(txtEdad.getText()));
            boolean rsp = td.insert(alm);
            if (rsp){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Exito");
                alert.setHeaderText(null);
                alert.setContentText("Los datos de "+alm.getNombres()+" se han registrado");
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();
                cleanForm();
                cargarAlumnos();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo guardar :(");
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();
            }
        }else{
            alumnoSeleccionado.setNombres(txtNombre.getText());
            alumnoSeleccionado.setApellidos(txtApellidos.getText());
            alumnoSeleccionado.setEdad(Integer.parseInt(txtEdad.getText()));
            boolean rsp = td.update(alumnoSeleccionado);
            if (rsp){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Exito");
                alert.setHeaderText(null);
                alert.setContentText("Los datos de "+alumnoSeleccionado.getNombres()+" se han guardado");
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();
                cleanForm();
                cargarAlumnos();
                alumnoSeleccionado = null;
                btnCancelar.setDisable(true);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo guardar :(");
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();
                alumnoSeleccionado = null;
            }
        }
    }

    @FXML
    void btnCancelarOnAction(ActionEvent event) {
        alumnoSeleccionado = null;
        btnCancelar.setDisable(true);
        cleanForm();
    }

    private void cleanForm (){
        txtNombre.setText("");
        txtApellidos.setText("");
        txtEdad.setText("");
    }

    public void cargarAlumnos(){
        tableAlumno.getItems().clear();
        tableAlumno.getColumns().clear();

        ObjectSet<Alumno> consultaAlumno = td.consultaGen();

        ObservableList<Alumno> datos = FXCollections.observableArrayList(consultaAlumno);

        TableColumn idCol = new TableColumn("Id");
        idCol.setCellValueFactory(new PropertyValueFactory("id"));

        TableColumn nombreCol = new TableColumn("Nombre del alumno");
        nombreCol.setCellValueFactory(new PropertyValueFactory("nombres"));

        TableColumn apellidosCol = new TableColumn("Apellidos del alumno");
        apellidosCol.setCellValueFactory(new PropertyValueFactory("apellidos"));

        TableColumn edadCol = new TableColumn("Edad del alumno");
        edadCol.setCellValueFactory(new PropertyValueFactory("edad"));

        tableAlumno.setItems(datos);
        tableAlumno.getColumns().addAll(idCol);
        tableAlumno.getColumns().addAll(nombreCol);
        tableAlumno.getColumns().addAll(apellidosCol);
        tableAlumno.getColumns().addAll(edadCol);


    }

    @FXML
    void goToMateriasOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        //Cargas el FXML que queres que abra en un Parent
        URI uri = Paths.get("src/main/resources/vistas/materiasFxml.fxml").toAbsolutePath().toUri();
        System.out.println(uri);
        Parent root = FXMLLoader.load(uri.toURL());
        Scene scene = new Scene(root);;
        stage.setScene(scene);
        stage.show();

        //Cerramos la ventana anterior de Login. La obtenemos a partir de un control (Button)
        Stage old = (Stage) btnGoToMaterias.getScene().getWindow();
        old.close();
    }

}
