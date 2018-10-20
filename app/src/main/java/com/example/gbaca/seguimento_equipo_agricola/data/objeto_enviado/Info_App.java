package com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado;

public class Info_App {

    private String Compania;
    private String Dispositivo;
    private String Version;

    public Info_App() {
    }


    public String getCompania() {
        return Compania;
    }

    public void setCompania(String compania) {
        Compania = compania;
    }

    public String getDispositivo() {
        return Dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        Dispositivo = dispositivo;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }
}
