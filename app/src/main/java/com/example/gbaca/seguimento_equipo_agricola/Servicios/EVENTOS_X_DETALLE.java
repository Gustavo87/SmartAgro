package com.example.gbaca.seguimento_equipo_agricola.Servicios;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;

import com.digi.wva.WVA;
import com.digi.wva.async.VehicleDataResponse;
import com.digi.wva.async.WvaCallback;
import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.Constantes;
import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.Metodos;
import com.example.gbaca.seguimento_equipo_agricola.R;
import com.example.gbaca.seguimento_equipo_agricola.data.BaseDatos;
import com.example.gbaca.seguimento_equipo_agricola.data.Configuracion;
import com.example.gbaca.seguimento_equipo_agricola.data.Eventos_Detalle_Actividad;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EVENTOS_X_DETALLE extends Service {


    private Context ctx;
    private BaseDatos db;
    WVAApplication wvaapp;
    private final Integer segundos_lectura = 2;
    private final Integer intervalo_inserccion_detalle = 20;
    private final Integer Cantidad_Eventos = intervalo_inserccion_detalle / segundos_lectura;
    private final Integer ULTIMOS_TIMESTAMP = 3;
    String[] timestamp_anteriores;
    final Handler handler = new Handler();
    List<Long> Lista_EXA_ID = new ArrayList<Long>();


    private void Guardar_Eventos_Detalle_Actividad(Long id,String fechaActual) {
        Long registro = 0L;

        if(id != 0){
            Eventos_Detalle_Actividad eventos_detalle_actividad = new Eventos_Detalle_Actividad();
            eventos_detalle_actividad.setId_Detalle_Actividad(id);
            eventos_detalle_actividad.setVelocidad(null);
            eventos_detalle_actividad.setVelocidad_timestamp(null);
            eventos_detalle_actividad.setRPM(null);
            eventos_detalle_actividad.setRPM_timestamp(null);
            eventos_detalle_actividad.setDistancia(0.0);
            eventos_detalle_actividad.setFecha_Modificacion(fechaActual);
            final Long id_registro = db.insertar_Eventos_Detalle_Actividad(eventos_detalle_actividad);
            Lista_EXA_ID.add(id_registro);
            wvaapp.getWva().isWVA(new WvaCallback<Boolean>() {
                @Override
                public void onResponse(Throwable error, Boolean success) {
                    if(error == null && success) {
                        RegistrarDatosDIGI_Eventos(id_registro);
                    }
                }
            });

        }

    }

    void RegistrarDatosDIGI_Eventos(final Long id_registro){

        final WVA wva = wvaapp.getWva();
        // Leemos la velocidad del motor
        wva.fetchVehicleData("VehicleSpeed", new WvaCallback<VehicleDataResponse>() {
            @Override
            public void onResponse(Throwable error, VehicleDataResponse responseVelocidad) {
                Double velocidad_tmp = null;
                String velocidad_timestamp = null;
                if (error != null) {
                    // Error DIGI Velocidad del Motor
                } else {

                    // Leemos la Velocidad
                    velocidad_tmp =responseVelocidad.getValue();
                    try{
                        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss.SSS");
                        velocidad_timestamp = formatter.print(responseVelocidad.getTime());
                    }catch (Exception e){
                        velocidad_timestamp = responseVelocidad.getTime().toString();
                    }

                }
                Double velocidad = velocidad_tmp;
                Double distancia;
                if(velocidad==null){
                    distancia = 0.0;
                }else{
                    velocidad = Math.abs(velocidad);
                    distancia = (velocidad * 1000 / 3600) * segundos_lectura;
                }
                Eventos_Detalle_Actividad eventos_detalle_actividad = new Eventos_Detalle_Actividad();
                eventos_detalle_actividad.setId(id_registro);
                eventos_detalle_actividad.setVelocidad(velocidad);
                eventos_detalle_actividad.setVelocidad_timestamp(velocidad_timestamp);
                eventos_detalle_actividad.setDistancia(distancia);
                db.actualizar_eventos_detalle_x_actividad_velocidad_distancia(eventos_detalle_actividad);
            }
        });

        wva.fetchVehicleData("EngineSpeed", new WvaCallback<VehicleDataResponse>() {
            @Override
            public void onResponse(Throwable error, VehicleDataResponse responseRPM) {
                Double rpm_tmp = null;
                String rpm_timestamp = "N/D";

                Configuracion configuracion = new Configuracion();

                if (error != null) {
                    configuracion.setDIGI_Conectado(Constantes.FALSE);
                } else {
                    configuracion.setDIGI_Conectado(Constantes.TRUE);
                    rpm_tmp = responseRPM.getValue();
                    try{
                        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss.SSS");
                        rpm_timestamp = formatter.print(responseRPM.getTime());
                    }catch (Exception e){
                        rpm_timestamp = responseRPM.getTime().toString();
                    }
                }

                db.actualizar_configuracion_DIGI_Conectado(configuracion);
                if(VerificarultimosTimestamp(rpm_timestamp)){
                    rpm_tmp = 0.0;
                }
                final Double rpm = rpm_tmp;
                Eventos_Detalle_Actividad eventos_detalle_actividad = new Eventos_Detalle_Actividad();
                eventos_detalle_actividad.setId(id_registro);
                eventos_detalle_actividad.setRPM(rpm);
                eventos_detalle_actividad.setRPM_timestamp(rpm_timestamp);
                db.actualizar_eventos_detalle_x_actividad_rpm(eventos_detalle_actividad);

            }
        });


    }

    Boolean VerificarultimosTimestamp(String timestamp){
        String elemento0 = timestamp_anteriores[0];
        String elemento1 = timestamp_anteriores[1];
        timestamp_anteriores[1] = elemento0;
        timestamp_anteriores[2] = elemento1;
        timestamp_anteriores[0] = timestamp;
        Boolean ultimosiguales = true;

        for(int i=0;i<ULTIMOS_TIMESTAMP;i++){
            if(!timestamp.equals(timestamp_anteriores[i])){
                ultimosiguales = false;
                break;
            }
        }

        return ultimosiguales;
    }


    public EVENTOS_X_DETALLE() {
    }

    private void startService()
    {
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                handler.postDelayed(this,segundos_lectura * 1000);
                Long fecha_actual = System.currentTimeMillis();
                String fecha_actual_formateada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(fecha_actual));
                final Long Id_Actividad = db.ObtenerUltimoID("DETALLE_X_ACTIVIDAD");
                // El unico momento que sera cero, es al crear el servicio
                Integer total_EXA = Lista_EXA_ID.size();
                Long id_actividad_nueva = Id_Actividad;
                if( total_EXA == 0){
                        id_actividad_nueva = Metodos.Guardar_Detalle_X_Actividad(ctx, db, wvaapp,Id_Actividad,fecha_actual_formateada);
                }else if(total_EXA == Cantidad_Eventos){
                        Lista_EXA_ID.clear();
                        id_actividad_nueva = Metodos.Guardar_Detalle_X_Actividad(ctx, db, wvaapp,Id_Actividad,fecha_actual_formateada);
                }
                Guardar_Eventos_Detalle_Actividad(id_actividad_nueva,fecha_actual_formateada);

            }
        }, 0);

    }
    @Override
    public void onCreate() {

        super.onCreate();

        Integer ID_Notificacion = 1;
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Registro Eventos")
                .setTicker("Registro Eventos")
                .setContentText("Registro Eventos")
                .setSmallIcon(R.mipmap.ic_launcher).build();
        startForeground(ID_Notificacion,notification);

        ctx = this;
        wvaapp = (WVAApplication) getApplication();
        db = wvaapp.getdb();
        timestamp_anteriores = new String[ULTIMOS_TIMESTAMP];
        for(int i=0;i<ULTIMOS_TIMESTAMP;i++){
            timestamp_anteriores[i] = "";
        }
        startService();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

}
