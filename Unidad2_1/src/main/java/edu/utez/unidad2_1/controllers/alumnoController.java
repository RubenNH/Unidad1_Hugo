package edu.utez.unidad2_1.controllers;

import com.db4o.ObjectSet;
import com.db4o.ext.ExtObjectSet;
import edu.utez.unidad2_1.dao.alumnoDao;
import edu.utez.unidad2_1.dao.materiaDao;
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
import java.util.*;

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
    @FXML
    private TableView<Materia> tablSelec;
    @FXML
    private TableView<Materia> tableOpc;
    private alumnoDao td;
    private materiaDao tdMateria;
    private ContextMenu cmOpciones;
    private ContextMenu cmOpcionesMatOpc;
    private ContextMenu cmOpcionesMatSele;
    private Alumno alumnoSeleccionado;
    private Materia materiaSeleccionada;
    private List<Materia> materiasSeleccionadas = new ArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarAlumnos();

        this.td = new alumnoDao();
        cmOpciones = new ContextMenu();
        cmOpcionesMatOpc = new ContextMenu();
        cmOpcionesMatSele = new ContextMenu();
        MenuItem miEditar = new MenuItem("editar");
        MenuItem miSeleccion = new MenuItem("Elegir materia");
        MenuItem miEliminar = new MenuItem("Eliminar");
        MenuItem miQuitar = new MenuItem("Quitar Materias");
        cmOpciones.getItems().addAll(miEditar);
        cmOpciones.getItems().addAll(miEliminar);
        cmOpcionesMatOpc.getItems().addAll(miSeleccion);
        cmOpcionesMatSele.getItems().addAll(miQuitar);

        tableAlumno.setContextMenu(cmOpciones);
        tableOpc.setContextMenu(cmOpcionesMatOpc);
        tablSelec.setContextMenu(cmOpcionesMatSele);
        miEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int index = tableAlumno.getSelectionModel().getSelectedIndex();
                alumnoSeleccionado =  tableAlumno.getItems().get(index);
                txtNombre.setText(alumnoSeleccionado.getNombres());
                txtApellidos.setText(alumnoSeleccionado.getApellidos());
                txtEdad.setText(String.valueOf(alumnoSeleccionado.getEdad()));
                btnCancelar.setDisable(false);
                cargarNuevasMaterias(alumnoSeleccionado.getMaterias());
            }
        });
        miSeleccion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int index = tableOpc.getSelectionModel().getSelectedIndex();
                materiaSeleccionada =  tableOpc.getItems().get(index);
                materiasSeleccionadas.add(materiaSeleccionada);
                cargarNuevasMaterias(materiasSeleccionadas);
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

        miQuitar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                materiasSeleccionadas = new ArrayList<>();
                cargarNuevasMaterias(materiasSeleccionadas);
                btnCancelar.setDisable(false);
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
            alm.setMaterias(materiasSeleccionadas);
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

            alumnoSeleccionado.setMaterias(materiasSeleccionadas);

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
        tablSelec.getColumns().clear();
        tablSelec.getItems().clear();
        materiasSeleccionadas = new ArrayList();
    }

    public void cargarAlumnos(){
        tableAlumno.getItems().clear();
        tableAlumno.getColumns().clear();
        tablSelec.getItems().clear();
        tableOpc.getItems().clear();
        tableOpc.getColumns().clear();
        tablSelec.getColumns().clear();

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

        TableColumn MateriasCol = new TableColumn("Materias del alumno");
        MateriasCol.setCellValueFactory(new PropertyValueFactory("materias"));

        tableAlumno.setItems(datos);
        tableAlumno.getColumns().addAll(idCol);
        tableAlumno.getColumns().addAll(nombreCol);
        tableAlumno.getColumns().addAll(apellidosCol);
        tableAlumno.getColumns().addAll(edadCol);
        tableAlumno.getColumns().addAll(MateriasCol);

        ObjectSet<Materia> consultaMaterias = tdMateria.consultaGen();
        ObservableList<Materia> datosMat = FXCollections.observableArrayList(consultaMaterias);
        TableColumn materiaCol = new TableColumn("Materias Elegibls");
        materiaCol.setCellValueFactory(new PropertyValueFactory("nombre"));
        tableOpc.setItems(datosMat);
        tableOpc.getColumns().addAll(materiaCol);

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

    public void cargarNuevasMaterias(List<Materia> Materia){
        tablSelec.getColumns().clear();
        tablSelec.getItems().clear();

        ObservableList<Materia> datosMat = FXCollections.observableArrayList(Materia);
        TableColumn materiaCol = new TableColumn("Materias Elegidas");
        materiaCol.setCellValueFactory(new PropertyValueFactory("nombre"));


        tablSelec.setItems(datosMat);
        tablSelec.getColumns().addAll(materiaCol);
    }
}
