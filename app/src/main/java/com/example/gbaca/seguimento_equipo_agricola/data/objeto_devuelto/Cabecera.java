package com.example.gbaca.seguimento_equipo_agricola.data.objeto_devuelto;

import java.util.ArrayList;

public class Cabecera {

    private Boolean Exito;
    private Long ID;
    private Long Dispositivo_Registro_ID;
    private String MensajeError;
    private ArrayList<Detalle> Resultado;

    public Cabecera() {
    }

    public Boolean getExito() {
        return Exito;
    }

    public void setExito(Boolean exito) {
        Exito = exito;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getDispositivo_Registro_ID() {
        return Dispositivo_Registro_ID;
    }

    public void setDispositivo_Registro_ID(Long dispositivo_Registro_ID) {
        Dispositivo_Registro_ID = dispositivo_Registro_ID;
    }

    public String getMensajeError() {
        return MensajeError;
    }

    public void setMensajeError(String mensajeError) {
        MensajeError = mensajeError;
    }

    public ArrayList<Detalle> getResultado() {
        return Resultado;
    }

    public void setResultado(ArrayList<Detalle> resultado) {
        Resultado = resultado;
    }
}
