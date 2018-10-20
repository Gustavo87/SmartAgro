package com.example.gbaca.seguimento_equipo_agricola.Servicios;

import android.app.Application;
import com.digi.wva.WVA;
import com.digi.wva.async.EventChannelStateListener;
import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.Constantes;
import com.example.gbaca.seguimento_equipo_agricola.data.BaseDatos;

import java.io.IOException;


public class WVAApplication extends Application {

    private BaseDatos db;
    private WVA wva;
    private String wva_ip = "192.168.43.160";

    public BaseDatos getdb(){return  db;}
    public WVA getWva() {
        return wva;
    }

    private boolean produccion;
    private String IPSERVER;

    //Definimos los servicios...
    private String SERVICIO_OBTENER_FRENTES;
    private String SERVICIO_OBTENER_OS;
    private String SERVICIO_OBTENER_IMPLEMENTOS;
    private String SERVICIO_OBTENER_EMPLEADOS;
    private String SERVICIO_ENVIO_DATOS;
    private String SERVICIO_OBTENER_ACTIVIDADESINACTIVAS;
    private String SERVICIO_OBTENER_DISPOSITIVO;
    private String SERVICIO_OBTENER_VERSION;


    @Override
    public void onCreate() {
        super.onCreate();

        produccion=false;
        DefinirServidor();
        DefinirServicios();

        db = new BaseDatos(this);

        wva = new WVA(wva_ip);
        // Replace the username and password to match that of your WVA.
        wva.useBasicAuth("admin", "admin").useSecureHttp(true);

        // Set the listener used to report the state of the event channel connection.
//        wva.setEventChannelStateListener(new EventChannelStateListener() {
//            @Override
//            public void onConnected(WVA device) {
////                Toast.makeText(getApplicationContext(), "Event channel connected!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(WVA device, IOException error) {
//                error.printStackTrace();
//                device.disconnectEventChannel(true);
//            }
//
//            @Override
//            public void onRemoteClose(WVA device, int port) {
////                Toast.makeText(getApplicationContext(), "Event channel closed on remote side. Reconnecting...", Toast.LENGTH_SHORT).show();
//
//                reconnectAfter(device, 15000, port);
//            }
//
//            @Override
//            public void onFailedConnection(WVA device, int port) {
////                Toast.makeText(getApplicationContext(), "Could not connect to event channel", Toast.LENGTH_SHORT).show();
//            }
//        });

        wva.connectEventChannel(5000);
    }


    public boolean esAmbienteProduccion() {
        return produccion;
    }

    private void DefinirServidor(){

        if(esAmbienteProduccion()==true){
            IPSERVER= Constantes.PRODUCCION_URL_PUBLICO;
        }
        else{
            IPSERVER=Constantes.PILOTO_URL_PUBLICO;
        }
    }

    private void DefinirServicios(){

        SERVICIO_OBTENER_FRENTES=IPSERVER+Constantes.PATH_SERVICIOS+Constantes.SERVICIO_OBTENER_FRENTES;
        SERVICIO_OBTENER_OS=IPSERVER+Constantes.PATH_SERVICIOS+Constantes.SERVICIO_OBTENER_OS;
        SERVICIO_OBTENER_IMPLEMENTOS=IPSERVER+Constantes.PATH_SERVICIOS+Constantes.SERVICIO_OBTENER_IMPLEMENTOS;
        SERVICIO_OBTENER_EMPLEADOS=IPSERVER+Constantes.PATH_SERVICIOS+Constantes.SERVICIO_OBTENER_EMPLEADOS;
        SERVICIO_ENVIO_DATOS = IPSERVER+Constantes.PATH_SERVICIOS+Constantes.SERVICIO_ENVIO_DATOS;
        SERVICIO_OBTENER_ACTIVIDADESINACTIVAS = IPSERVER+Constantes.PATH_SERVICIOS+Constantes.SERVICIO_OBTENER_ACTIVIDADESINACTIVAS;
        SERVICIO_OBTENER_DISPOSITIVO = IPSERVER+Constantes.PATH_SERVICIOS+Constantes.SERVICIO_OBTENER_DISPOSITIVO;
        SERVICIO_OBTENER_VERSION = IPSERVER+Constantes.PATH_SERVICIOS+Constantes.SERVICIO_OBTENER_VERSION;
    }

    //Getters para retornar servicios
    public String GET_SERVICIO_OBTENER_FRENTES(){
        return  SERVICIO_OBTENER_FRENTES;
    }
    public String GET_SERVICIO_OBTENER_OS(){return  SERVICIO_OBTENER_OS;}
    public String GET_SERVICIO_OBTENER_IMPLEMENTOS(){
        return  SERVICIO_OBTENER_IMPLEMENTOS;
    }
    public String GET_SERVICIO_OBTENER_EMPLEADOS(){
        return  SERVICIO_OBTENER_EMPLEADOS;
    }
    public String GET_OBTENER_ACTIVIDADESINACTIVAS(){return SERVICIO_OBTENER_ACTIVIDADESINACTIVAS;}
    public String GET_SERVICIO_ENVIO_DATOS(){return SERVICIO_ENVIO_DATOS;}
    public String GET_SERVICIO_OBTENER_DISPOSITIVO(){return SERVICIO_OBTENER_DISPOSITIVO;}
    public String GET_SERVICIO_OBTENER_VERSION(){return SERVICIO_OBTENER_VERSION;}

}
