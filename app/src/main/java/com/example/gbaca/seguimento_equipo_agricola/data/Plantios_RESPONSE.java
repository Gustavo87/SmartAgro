package com.example.gbaca.seguimento_equipo_agricola.data;

import java.util.List;

/**
 * Created by GBaca on 6/26/2018.
 */

public class Plantios_RESPONSE {

    private boolean Exito;
    private int ID;
    private String MensajeError;
    private List<Plantios> Resultado;

    public Plantios_RESPONSE() {}

    public Plantios_RESPONSE(boolean exito, int ID, String mensajeError, List<Plantios> resultado) {
        Exito = exito;
        this.ID = ID;
        MensajeError = mensajeError;
        Resultado = resultado;
    }

    public boolean isExito() {
        return Exito;
    }

    public void setExito(boolean exito) {
        Exito = exito;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMensajeError() {
        return MensajeError;
    }

    public void setMensajeError(String mensajeError) {
        MensajeError = mensajeError;
    }

    public List<Plantios> getResultado() {
        return Resultado;
    }

    public void setResultado(List<Plantios> resultado) {
        Resultado = resultado;
    }
}
