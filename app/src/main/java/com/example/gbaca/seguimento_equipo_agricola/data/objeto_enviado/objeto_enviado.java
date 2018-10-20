package com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado;

import java.util.ArrayList;

public class objeto_enviado {

    private Info_App Info_App;
    private ArrayList<Actividades_X_Equipo> Actividades_X_Equipo;

    public objeto_enviado() {
    }

    public com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado.Info_App getInfo_App() {
        return Info_App;
    }

    public void setInfo_App(com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado.Info_App info_App) {
        Info_App = info_App;
    }

    public ArrayList<com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado.Actividades_X_Equipo> getActividades_X_Equipo() {
        return Actividades_X_Equipo;
    }

    public void setActividades_X_Equipo(ArrayList<com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado.Actividades_X_Equipo> actividades_X_Equipo) {
        Actividades_X_Equipo = actividades_X_Equipo;
    }
}
