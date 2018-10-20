package com.example.gbaca.seguimento_equipo_agricola.data;

/**
 * Created by GBaca on 6/29/2018.
 */

public class Actividades_X_Equipo {

    private Long id;
    private Long AXE_ID;
    private String Orden_Servicio;
    private String AXE_Actividad_Inactiva;
    private String Implemento_1;
    private String Implemento_2;
    private String Implemento_3;
    private String Frente;
    private String Plantio;
    private String Equipo;
    private String Operador;
    private String Actividad;
    private String Fecha_Inicio;
    private String Fecha_Fin;
    private Integer Sincronizado;
    private String Fecha_Modificacion;
    private String Usuario;
    private Float Version_APP;
    private String Dispositivo;
    private Float Area_Trabajada;
    private Integer AutoGuiado;
    private String AXE_Timestamp;

    public Actividades_X_Equipo() {
    }

    public String getAXE_Timestamp() {
        return AXE_Timestamp;
    }

    public void setAXE_Timestamp(String AXE_Timestamp) {
        this.AXE_Timestamp = AXE_Timestamp;
    }

    public Integer getAutoGuiado() {
        return AutoGuiado;
    }

    public void setAutoGuiado(Integer autoGuiado) {
        AutoGuiado = autoGuiado;
    }

    public Float getArea_Trabajada() {
        return Area_Trabajada;
    }

    public void setArea_Trabajada(Float area_Trabajada) {
        Area_Trabajada = area_Trabajada;
    }

    public String getAXE_Actividad_Inactiva() {
        return AXE_Actividad_Inactiva;
    }

    public void setAXE_Actividad_Inactiva(String AXE_Actividad_Inactiva) {
        this.AXE_Actividad_Inactiva = AXE_Actividad_Inactiva;
    }

    public Long getId() {
        return id;
    }

    public Long getAXE_ID() {
        return AXE_ID;
    }

    public void setAXE_ID(Long AXE_ID) {
        this.AXE_ID = AXE_ID;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrden_Servicio() {
        return Orden_Servicio;
    }

    public void setOrden_Servicio(String orden_Servicio) {
        Orden_Servicio = orden_Servicio;
    }

    public String getImplemento_1() {
        return Implemento_1;
    }

    public void setImplemento_1(String implemento_1) {
        Implemento_1 = implemento_1;
    }

    public String getImplemento_2() {
        return Implemento_2;
    }

    public void setImplemento_2(String implemento_2) {
        Implemento_2 = implemento_2;
    }

    public String getImplemento_3() {
        return Implemento_3;
    }

    public void setImplemento_3(String implemento_3) {
        Implemento_3 = implemento_3;
    }

    public String getFrente() {
        return Frente;
    }

    public void setFrente(String frente) {
        Frente = frente;
    }

    public String getPlantio() {
        return Plantio;
    }

    public void setPlantio(String plantio) {
        Plantio = plantio;
    }

    public String getEquipo() {
        return Equipo;
    }

    public void setEquipo(String equipo) {
        Equipo = equipo;
    }

    public String getOperador() {
        return Operador;
    }

    public void setOperador(String operador) {
        Operador = operador;
    }

    public String getActividad() {
        return Actividad;
    }

    public void setActividad(String actividad) {
        Actividad = actividad;
    }

    public String getFecha_Inicio() {
        return Fecha_Inicio;
    }

    public void setFecha_Inicio(String fecha_Inicio) {
        Fecha_Inicio = fecha_Inicio;
    }

    public String getFecha_Fin() {
        return Fecha_Fin;
    }

    public void setFecha_Fin(String fecha_Fin) {
        Fecha_Fin = fecha_Fin;
    }

    public Integer getSincronizado() {
        return Sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        Sincronizado = sincronizado;
    }

    public String getFecha_Modificacion() {
        return Fecha_Modificacion;
    }

    public void setFecha_Modificacion(String fecha_Modificacion) {
        Fecha_Modificacion = fecha_Modificacion;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public Float getVersion_APP() {
        return Version_APP;
    }

    public void setVersion_APP(Float version_APP) {
        Version_APP = version_APP;
    }

    public String getDispositivo() {
        return Dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        Dispositivo = dispositivo;
    }
}
