package com.example.gbaca.seguimento_equipo_agricola.data;

import java.util.List;

/**
 * Created by GBaca on 6/26/2018.
 */

public class Implementos_RESPONSE {

    private boolean Exito;
    private int ID;
    private String MensajeError;
    private List<Implementos> Resultado;

    public Implementos_RESPONSE() {}

    public Implementos_RESPONSE(boolean exito, int ID, String mensajeError, List<Implementos> resultado) {
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

    public List<Implementos> getResultado() {
        return Resultado;
    }

    public void setResultado(List<Implementos> resultado) {
        Resultado = resultado;
    }
}
