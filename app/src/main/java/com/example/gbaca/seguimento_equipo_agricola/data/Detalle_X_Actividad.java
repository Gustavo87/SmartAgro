package com.example.gbaca.seguimento_equipo_agricola.data;

/**
 * Created by GBaca on 6/29/2018.
 */

public class Detalle_X_Actividad {

     private Long id;
     private Long DXA_ID;
     private Long Id_Actividad;
     private Double Latitud;
     private Double Longitud;
     private Double Velocidad;
     private Double RPM;
     private Double Distancia;
     private Double Distancia_GPS;
     private Double Horas_Motor;
     private Double Nivel_Combustible;
     private Double Temperatura_Motor;
     private Double Tiempo_Activo;
     private Double Tiempo_Ralenti;
     private Double Tiempo_Apagado;
     private String Fecha_Modificacion;
     private String DXA_Objeto;
     private Integer DXA_Cantidad_Eventos;
     private String Temperatura_Motor_timestamp;
     private String Nivel_Combustible_timestamp;
     private String Horas_Motor_timestamp;
     private Double Tiempo_Disponibilidad;
     private Double Consumo_Combustible;
     private Double Porcentaje_Bateria;
     private String Tipo_Senal;
     private Double Intensidad_Senal;

    public Detalle_X_Actividad() {
    }

    public Double getPorcentaje_Bateria() {
        return Porcentaje_Bateria;
    }

    public void setPorcentaje_Bateria(Double porcentaje_Bateria) {
        Porcentaje_Bateria = porcentaje_Bateria;
    }

    public String getTipo_Senal() {
        return Tipo_Senal;
    }

    public void setTipo_Senal(String tipo_Senal) {
        Tipo_Senal = tipo_Senal;
    }

    public Double getIntensidad_Senal() {
        return Intensidad_Senal;
    }

    public void setIntensidad_Senal(Double intensidad_Senal) {
        Intensidad_Senal = intensidad_Senal;
    }

    public Double getConsumo_Combustible() {
        return Consumo_Combustible;
    }

    public void setConsumo_Combustible(Double consumo_Combustible) {
        Consumo_Combustible = consumo_Combustible;
    }

    public Double getTiempo_Disponibilidad() {
        return Tiempo_Disponibilidad;
    }

    public void setTiempo_Disponibilidad(Double tiempo_Disponibilidad) {
        Tiempo_Disponibilidad = tiempo_Disponibilidad;
    }

    public String getTemperatura_Motor_timestamp() {
        return Temperatura_Motor_timestamp;
    }

    public void setTemperatura_Motor_timestamp(String temperatura_Motor_timestamp) {
        Temperatura_Motor_timestamp = temperatura_Motor_timestamp;
    }

    public String getNivel_Combustible_timestamp() {
        return Nivel_Combustible_timestamp;
    }

    public void setNivel_Combustible_timestamp(String nivel_Combustible_timestamp) {
        Nivel_Combustible_timestamp = nivel_Combustible_timestamp;
    }

    public String getHoras_Motor_timestamp() {
        return Horas_Motor_timestamp;
    }

    public void setHoras_Motor_timestamp(String horas_Motor_timestamp) {
        Horas_Motor_timestamp = horas_Motor_timestamp;
    }

    public Double getDistancia_GPS() {
        return Distancia_GPS;
    }

    public void setDistancia_GPS(Double distancia_GPS) {
        Distancia_GPS = distancia_GPS;
    }

    public Integer getDXA_Cantidad_Eventos() {
        return DXA_Cantidad_Eventos;
    }

    public void setDXA_Cantidad_Eventos(Integer DXA_Cantidad_Eventos) {
        this.DXA_Cantidad_Eventos = DXA_Cantidad_Eventos;
    }

    public String getDXA_Objeto() {
        return DXA_Objeto;
    }

    public void setDXA_Objeto(String DXA_Objeto) {
        this.DXA_Objeto = DXA_Objeto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDXA_ID() {
        return DXA_ID;
    }

    public void setDXA_ID(Long DXA_ID) {
        this.DXA_ID = DXA_ID;
    }

    public Long getId_Actividad() {
        return Id_Actividad;
    }

    public void setId_Actividad(Long id_Actividad) {
        Id_Actividad = id_Actividad;
    }

    public Double getLatitud() {
        return Latitud;
    }

    public void setLatitud(Double latitud) {
        Latitud = latitud;
    }

    public Double getLongitud() {
        return Longitud;
    }

    public void setLongitud(Double longitud) {
        Longitud = longitud;
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

    public Double getHoras_Motor() {
        return Horas_Motor;
    }

    public void setHoras_Motor(Double horas_Motor) {
        Horas_Motor = horas_Motor;
    }

    public Double getNivel_Combustible() {
        return Nivel_Combustible;
    }

    public void setNivel_Combustible(Double nivel_Combustible) {
        Nivel_Combustible = nivel_Combustible;
    }

    public Double getTemperatura_Motor() {
        return Temperatura_Motor;
    }

    public void setTemperatura_Motor(Double temperatura_Motor) {
        Temperatura_Motor = temperatura_Motor;
    }

    public Double getTiempo_Activo() {
        return Tiempo_Activo;
    }

    public void setTiempo_Activo(Double tiempo_Activo) {
        Tiempo_Activo = tiempo_Activo;
    }

    public Double getTiempo_Ralenti() {
        return Tiempo_Ralenti;
    }

    public void setTiempo_Ralenti(Double tiempo_Ralenti) {
        Tiempo_Ralenti = tiempo_Ralenti;
    }

    public Double getTiempo_Apagado() {
        return Tiempo_Apagado;
    }

    public void setTiempo_Apagado(Double tiempo_Apagado) {
        Tiempo_Apagado = tiempo_Apagado;
    }

    public String getFecha_Modificacion() {
        return Fecha_Modificacion;
    }

    public void setFecha_Modificacion(String fecha_Modificacion) {
        Fecha_Modificacion = fecha_Modificacion;
    }
}
