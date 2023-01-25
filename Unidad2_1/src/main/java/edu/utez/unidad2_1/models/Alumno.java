package edu.utez.unidad2_1.models;

import edu.utez.unidad2_1.models.Materia;


import java.io.Serializable;
import java.util.List;

public class Alumno implements Serializable {


    //estudiar ORM y JPA en db4o
    //ORM transforma
    //atributos
    //constructor vacio y especifico
    //encapsulamiento
    //tostring
    //implementes serializable, es lo mismo que tostring que transforma las cosas para poder hacer uso de ellas sin que nos de un error
    private int id;
    private String nombres;
    private String apellidos;
    private int edad;

    private List<Materia> materias;

    public Alumno() {
    }

    public Alumno(int id, String nombres, String apellidos, int edad, List<Materia> materias) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.materias = materias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    @Override  //esto hace que en vez de que te devuelva espacio en memoria te devuelva lo que esta dentro del objeto
    public String toString() {
        return "Alumno{" +
                "id=" + id +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", edad=" + edad +
                ", materias=" + materias +
                '}';
    }
}
