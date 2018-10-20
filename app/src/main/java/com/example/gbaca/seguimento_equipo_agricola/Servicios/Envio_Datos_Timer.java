package com.example.gbaca.seguimento_equipo_agricola.Servicios;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.ConexionHTTP;
import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.ConnectionInterface;
import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.Constantes;
import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.Metodos;
import com.example.gbaca.seguimento_equipo_agricola.R;
import com.example.gbaca.seguimento_equipo_agricola.data.Actividades_X_Equipo;
import com.example.gbaca.seguimento_equipo_agricola.data.BaseDatos;
import com.example.gbaca.seguimento_equipo_agricola.data.Detalle_X_Actividad;
import com.example.gbaca.seguimento_equipo_agricola.data.objeto_devuelto.Cabecera;
import com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado.Info_App;
import com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado.objeto_enviado;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Envio_Datos_Timer extends Service {

//    private static Timer timer = new Timer();
    private Context ctx;
    private BaseDatos db;
    WVAApplication wvaapp;
    final Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        Integer ID_Notificacion = 2;
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Envio de Datos")
                .setTicker("Envio de Datos")
                .setContentText("Envio de Datos")
                .setSmallIcon(R.mipmap.ic_launcher).build();
        startForeground(ID_Notificacion,notification);

        ctx = this;
        wvaapp = (WVAApplication) getApplication();
        db = wvaapp.getdb();
        startService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    private void startService()
    {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,120_000);
                Enviar_Datos_Servicio();
            }
        }, 120_000);

    }

    @Override
    public void onDestroy()
    {
        stopForeground(true);
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void Enviar_Datos_Servicio(){

        // Armamos el objeto que sera enviado al servicio.
        objeto_enviado datosSalida = new objeto_enviado();
        Info_App info_app = new Info_App();
        info_app.setCompania(Constantes.COMPANIA_SSA);
        info_app.setDispositivo(Metodos.Obtener_UUID_dispositivo(ctx));
        info_app.setVersion(Metodos.Obtener_Version_Name());
        datosSalida.setInfo_App(info_app);
        datosSalida.setActividades_X_Equipo(db.ObtenerCabecera());

        for(int i=0;i<datosSalida.getActividades_X_Equipo().size();i++){
            datosSalida.getActividades_X_Equipo().get(i).setDetalle_X_Actividad(db.ObtenerDetalle(datosSalida.getActividades_X_Equipo().get(i).getAXE_Dispositivo_Registro_ID()));
        }

        // Hacemos el envio del objeto
        WVAApplication app = (WVAApplication)getApplicationContext();

        final Gson gson = new Gson();
        String salida_string = gson.toJson(datosSalida);

        String url = app.GET_SERVICIO_ENVIO_DATOS();
        if(datosSalida.getActividades_X_Equipo().size()!=0){
            new ConexionHTTP(ctx,getString(R.string.enviando_datos_msg),url, Constantes.POST, salida_string, new ConnectionInterface() {
                @Override
                public void doProcess(String output) {
                    if(output!=null){
                        Toast.makeText(ctx,"Enviado",Toast.LENGTH_SHORT).show();
                        ArrayList<Cabecera> respuesta;
                    try{
                        respuesta = gson.fromJson(output, new TypeToken<List<Cabecera>>(){}.getType());
                        for(int i=0;i<respuesta.size();i++){
                            if(respuesta.get(i).getExito()){
                                //Actualizada Cabecera
                                Actividades_X_Equipo actividad_x_equipo = new Actividades_X_Equipo();
                                actividad_x_equipo.setId(respuesta.get(i).getDispositivo_Registro_ID());
                                actividad_x_equipo.setAXE_ID(respuesta.get(i).getID());
                                db.marcar_enviada_actividad_x_equipo(actividad_x_equipo);
                                for(int j=0;j<respuesta.get(i).getResultado().size();j++){

                                    Detalle_X_Actividad detalle_x_actividad = new Detalle_X_Actividad();
                                    if(respuesta.get(i).getResultado().get(j).getExito()){
                                        //Actualizada Detalle
                                        detalle_x_actividad.setId(respuesta.get(i).getResultado().get(j).getDispositivo_Registro_ID());
                                        detalle_x_actividad.setDXA_ID(respuesta.get(i).getResultado().get(j).getID());
                                        db.marcar_enviada_detalle_x_actividad(detalle_x_actividad);
                                    }

                                }
                            }
                        }
                    }catch (Exception e){
                            Toast.makeText(ctx, "Ocurrió un error al enviar los datos",Toast.LENGTH_SHORT).show();
                    }

                    }
                }
            }).execute();
        }



    }


}
