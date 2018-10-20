package com.example.gbaca.seguimento_equipo_agricola.data.objeto_devuelto;


public class Detalle {

    private Boolean Exito;
    private Long ID;
    private Long Dispositivo_Registro_ID;
    private String MensajeError;
    private Integer Resultado;


    public Detalle() {
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

    public Integer getResultado() {
        return Resultado;
    }

    public void setResultado(Integer resultado) {
        Resultado = resultado;
    }
}
