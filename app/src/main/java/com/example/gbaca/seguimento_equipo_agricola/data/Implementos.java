package com.example.gbaca.seguimento_equipo_agricola.data;

/**
 * Created by GBaca on 6/26/2018.
 */

public class Implementos {

    private String Codigo;
    private String Descripcion;

    public Implementos() {}

    public Implementos(String codigo, String descripcion) {
        Codigo = codigo;
        Descripcion = descripcion;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
