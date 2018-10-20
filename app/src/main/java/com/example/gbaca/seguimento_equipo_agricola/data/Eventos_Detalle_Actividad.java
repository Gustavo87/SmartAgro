package com.example.gbaca.seguimento_equipo_agricola.data;

/**
 * Created by GBaca on 6/29/2018.
 */

public class Eventos_Detalle_Actividad {
    private Long id;
     private Long Id_Detalle_Actividad;
     private Double Velocidad;
     private Double RPM;
     private Double Distancia;
     private String Fecha_Modificacion;
     private String Velocidad_timestamp;
     private String RPM_timestamp;

    public Eventos_Detalle_Actividad() {
    }

    public String getVelocidad_timestamp() {
        return Velocidad_timestamp;
    }

    public void setVelocidad_timestamp(String velocidad_timestamp) {
        Velocidad_timestamp = velocidad_timestamp;
    }

    public String getRPM_timestamp() {
        return RPM_timestamp;
    }

    public void setRPM_timestamp(String RPM_timestamp) {
        this.RPM_timestamp = RPM_timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_Detalle_Actividad() {
        return Id_Detalle_Actividad;
    }

    public void setId_Detalle_Actividad(Long id_Detalle_Actividad) {
        Id_Detalle_Actividad = id_Detalle_Actividad;
    }

    public Double getVelocidad() {
        return Velocidad;
    }

    public void setVelocidad(Double velocidad) {
        Velocidad = velocidad;
    }

    public Double getRPM() {
        return RPM;
    }

    public void setRPM(Double RPM) {
        this.RPM = RPM;
    }

    public Double getDistancia() {
        return Distancia;
    }

    public void setDistancia(Double distancia) {
        Distancia = distancia;
    }

    public String getFecha_Modificacion() {
        return Fecha_Modificacion;
    }

    public void setFecha_Modificacion(String fecha_Modificacion) {
        Fecha_Modificacion = fecha_Modificacion;
    }
}
