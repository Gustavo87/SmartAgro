package com.example.gbaca.seguimento_equipo_agricola.data;

/**
 * Created by GBaca on 6/26/2018.
 */

public class Empleados {

    private String Codigo;
    private String Empleado;

    public Empleados(String codigo, String empleado) {
        Codigo = codigo;
        Empleado = empleado;
    }

    public Empleados() {}

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getEmpleado() {
        return Empleado;
    }

    public void setEmpleado(String empleado) {
        Empleado = empleado;
    }
}
