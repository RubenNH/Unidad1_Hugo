package edu.utez.unidad2_1.models;

import java.io.Serializable;

public class Materia implements Serializable {
    private Integer clave;
    private String nombre;

    public Materia() {
    }

    public Materia(Integer clave, String nombre) {
        this.clave = clave;
        this.nombre = nombre;
    }

    public Integer getClave() {
        return clave;
    }

    public void setClave(Integer clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Materia{" +
                "clave=" + clave +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
