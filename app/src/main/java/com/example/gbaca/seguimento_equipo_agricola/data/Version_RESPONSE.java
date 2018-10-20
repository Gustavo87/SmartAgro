package com.example.gbaca.seguimento_equipo_agricola.data;

import java.util.List;

/**
 * Created by GBaca on 10/2/2018.
 */

public class Version_RESPONSE {

    private boolean Exito;
    private int ID;
    private int Dispositivo_Registro_ID;
    private String MensajeError;
    private List<Version> Resultado;

    public Version_RESPONSE() {
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

    public int getDispositivo_Registro_ID() {
        return Dispositivo_Registro_ID;
    }

    public void setDispositivo_Registro_ID(int dispositivo_Registro_ID) {
        Dispositivo_Registro_ID = dispositivo_Registro_ID;
    }

    public String getMensajeError() {
        return MensajeError;
    }

    public void setMensajeError(String mensajeError) {
        MensajeError = mensajeError;
    }

    public List<Version> getResultado() {
        return Resultado;
    }

    public void setResultado(List<Version> resultado) {
        Resultado = resultado;
    }
}
