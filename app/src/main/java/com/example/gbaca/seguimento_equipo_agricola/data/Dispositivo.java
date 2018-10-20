package com.example.gbaca.seguimento_equipo_agricola.data;

/**
 * Created by GBaca on 8/16/2018.
 */

public class Dispositivo {
    private String Compania;
    private String Equipo;
    private String Dispositivo;
    private String Version_App;
    private String URL_Descarga_APK;

    public Dispositivo() {
    }

    public String getURL_Descarga_APK() {
        return URL_Descarga_APK;
    }

    public void setURL_Descarga_APK(String URL_Descarga_APK) {
        this.URL_Descarga_APK = URL_Descarga_APK;
    }

    public String getVersion_App() {
        return Version_App;
    }

    public void setVersion_App(String version_App) {
        Version_App = version_App;
    }

    public String getCompania() {
        return Compania;
    }

    public void setCompania(String compania) {
        Compania = compania;
    }

    public String getEquipo() {
        return Equipo;
    }

    public void setEquipo(String equipo) {
        Equipo = equipo;
    }

    public String getDispositivo() {
        return Dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        Dispositivo = dispositivo;
    }
}
