package com.example.gbaca.seguimento_equipo_agricola.data;

/**
 * Created by GBaca on 6/26/2018.
 */

public class Plantios {

    private String Codigo;
    private String Plantio;
    private String Cuadro;

    public Plantios(String codigo, String plantio, String cuadro) {
        Codigo = codigo;
        Plantio = plantio;
        Cuadro = cuadro;
    }

    public Plantios() {}


    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getPlantio() {
        return Plantio;
    }

    public void setPlantio(String plantio) {
        Plantio = plantio;
    }

    public String getCuadro() {
        return Cuadro;
    }

    public void setCuadro(String cuadro) {
        Cuadro = cuadro;
    }
}
