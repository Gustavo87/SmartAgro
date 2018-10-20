package com.example.gbaca.seguimento_equipo_agricola.data;

/**
 * Created by GBaca on 6/26/2018.
 */

public class ActividadesInactivas {

    private String Actividad;
    private String Actividad_N;
    private String Tipo_Actividad;

    public ActividadesInactivas() {}

    public ActividadesInactivas(String actividad, String actividad_N, String tipo_Actividad) {
        Actividad = actividad;
        Actividad_N = actividad_N;
        Tipo_Actividad = tipo_Actividad;
    }

    public String getActividad() {
        return Actividad;
    }

    public void setActividad(String actividad) {
        Actividad = actividad;
    }

    public String getActividad_N() {
        return Actividad_N;
    }

    public void setActividad_N(String actividad_N) {
        Actividad_N = actividad_N;
    }

    public String getTipo_Actividad() {
        return Tipo_Actividad;
    }

    public void setTipo_Actividad(String tipo_Actividad) {
        Tipo_Actividad = tipo_Actividad;
    }
}
