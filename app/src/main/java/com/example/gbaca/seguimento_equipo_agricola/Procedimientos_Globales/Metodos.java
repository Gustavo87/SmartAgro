package com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.digi.wva.WVA;
import com.digi.wva.async.VehicleDataResponse;
import com.digi.wva.async.WvaCallback;
import com.example.gbaca.seguimento_equipo_agricola.Actividades.MainActivity;
import com.example.gbaca.seguimento_equipo_agricola.BuildConfig;
import com.example.gbaca.seguimento_equipo_agricola.R;
import com.example.gbaca.seguimento_equipo_agricola.Servicios.EVENTOS_X_DETALLE;
import com.example.gbaca.seguimento_equipo_agricola.Servicios.WVAApplication;
import com.example.gbaca.seguimento_equipo_agricola.data.ActividadesInactivas;
import com.example.gbaca.seguimento_equipo_agricola.data.ActividadesInactivas_RESPONSE;
import com.example.gbaca.seguimento_equipo_agricola.data.Actividades_X_Equipo;
import com.example.gbaca.seguimento_equipo_agricola.data.BaseDatos;
import com.example.gbaca.seguimento_equipo_agricola.data.Configuracion;
import com.example.gbaca.seguimento_equipo_agricola.data.Detalle_X_Actividad;
import com.example.gbaca.seguimento_equipo_agricola.data.Dispositivo;
import com.example.gbaca.seguimento_equipo_agricola.data.Dispositivo_RESPONSE;
import com.example.gbaca.seguimento_equipo_agricola.data.Empleados;
import com.example.gbaca.seguimento_equipo_agricola.data.Empleados_RESPONSE;
import com.example.gbaca.seguimento_equipo_agricola.data.Frentes;
import com.example.gbaca.seguimento_equipo_agricola.data.Frentes_RESPONSE;
import com.example.gbaca.seguimento_equipo_agricola.data.Implementos;
import com.example.gbaca.seguimento_equipo_agricola.data.Implementos_RESPONSE;
import com.example.gbaca.seguimento_equipo_agricola.data.OS;
import com.example.gbaca.seguimento_equipo_agricola.data.OS_RESPONSE;
import com.example.gbaca.seguimento_equipo_agricola.data.Version;
import com.example.gbaca.seguimento_equipo_agricola.data.Version_RESPONSE;
import com.example.gbaca.seguimento_equipo_agricola.data.endpoint;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by GBaca on 6/26/2018.
 */

public class Metodos {

    public static long Calcular_Diferencia_Segundos(Date fechaInicial, Date fechaFinal) {
        TimeUnit timeUnit = TimeUnit.SECONDS;
        long diferenciaMilisegundos = fechaFinal.getTime() - fechaInicial.getTime();
        return timeUnit.convert(diferenciaMilisegundos,TimeUnit.MILLISECONDS);
    }

    public static String Obtener_UUID_dispositivo(Context contexto_actual){

        return Settings.Secure.getString(contexto_actual.getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    public static String Obtener_Version_Name(){

        return BuildConfig.VERSION_NAME;

    }

    public static String fecha_actual(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static Long Insertar_Actividad_X_Equipo(Context contexto,
                                                   BaseDatos db,
                                                   Float valorAreaTrabajada,
                                                   Integer usa_auto_guiado
                                                   ){

        contexto.stopService(new Intent(contexto, EVENTOS_X_DETALLE.class));
        Long id = db.ObtenerUltimoID("ACTIVIDADES_X_EQUIPO");

        if (id != 0) {
            Actividades_X_Equipo actividades_x_equipo = new Actividades_X_Equipo();
            actividades_x_equipo.setId(id);
            actividades_x_equipo.setFecha_Fin(Metodos.fecha_actual());
            actividades_x_equipo.setFecha_Modificacion(Metodos.fecha_actual());
            actividades_x_equipo.setArea_Trabajada(valorAreaTrabajada);
            Metodos.Update_Detalle_X_Actividad(db,id);
            db.actualizar_actividades_x_equipo(actividades_x_equipo);
        }


        Actividades_X_Equipo actividades_x_equipo = new Actividades_X_Equipo();

        Configuracion configuracion = db.ObtenerConfiguracion();
        actividades_x_equipo.setAXE_ID(0L);
        actividades_x_equipo.setFrente(configuracion.getFrente());
        actividades_x_equipo.setPlantio(configuracion.getPlantio());
        actividades_x_equipo.setActividad(configuracion.getActividad());
        actividades_x_equipo.setAXE_Actividad_Inactiva(configuracion.getActividadInactiva());
        actividades_x_equipo.setImplemento_1(configuracion.getImplemento1());
        actividades_x_equipo.setImplemento_2(configuracion.getImplemento2());
        actividades_x_equipo.setImplemento_3(configuracion.getImplemento3());
        actividades_x_equipo.setOrden_Servicio(configuracion.getOS());
        actividades_x_equipo.setEquipo(configuracion.getEquipo());
        actividades_x_equipo.setOperador(configuracion.getEmpleado());
        actividades_x_equipo.setUsuario(configuracion.getEmpleado());
        actividades_x_equipo.setFecha_Inicio(Metodos.fecha_actual());
        actividades_x_equipo.setFecha_Fin(null);
        actividades_x_equipo.setSincronizado(0);
        actividades_x_equipo.setFecha_Modificacion(null);
        actividades_x_equipo.setVersion_APP(Float.parseFloat(Metodos.Obtener_Version_Name()));
        actividades_x_equipo.setDispositivo(Metodos.Obtener_UUID_dispositivo(contexto));
        actividades_x_equipo.setArea_Trabajada(0F);
        actividades_x_equipo.setAutoGuiado(usa_auto_guiado);
        actividades_x_equipo.setAXE_Timestamp(String.valueOf(new Date().getTime()));

        Long registro = db.insertar_Actividades_X_Equipo(actividades_x_equipo);

        if (registro > 0) {
            configuracion = new Configuracion();
            configuracion.setCambios(Constantes.FALSE);
            db.actualizar_configuracion_Cambios(configuracion);
        } else {
            Toast.makeText(contexto, "Error", Toast.LENGTH_SHORT).show();
        }

        return registro;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void Duplicar_Actividad_X_Equipo(final Context contexto, BaseDatos db,String fechaActual) {
        contexto.stopService(new Intent(contexto, EVENTOS_X_DETALLE.class));
        Actividades_X_Equipo actividades_x_equipo_ultimo = db.ObtenerUltimaActividad_X_Equipo();
        //Insertamos la nueva actividad
        actividades_x_equipo_ultimo.setAXE_ID(0L);
        actividades_x_equipo_ultimo.setFecha_Inicio(fechaActual);
        actividades_x_equipo_ultimo.setFecha_Fin(null);
        actividades_x_equipo_ultimo.setSincronizado(0);
        actividades_x_equipo_ultimo.setFecha_Modificacion(null);
        actividades_x_equipo_ultimo.setArea_Trabajada(0F);
        db.insertar_Actividades_X_Equipo(actividades_x_equipo_ultimo);
        contexto.startService(new Intent(contexto, EVENTOS_X_DETALLE.class));

        if (actividades_x_equipo_ultimo.getId() != 0) {
            Actividades_X_Equipo actividades_x_equipo = new Actividades_X_Equipo();
            actividades_x_equipo.setId(actividades_x_equipo_ultimo.getId());
            actividades_x_equipo.setFecha_Fin(fechaActual);
            actividades_x_equipo.setFecha_Modificacion(fechaActual);
            actividades_x_equipo.setArea_Trabajada(0F);
            db.actualizar_actividades_x_equipo(actividades_x_equipo);
        }

    }


    public static void Update_Detalle_X_Actividad(BaseDatos db, Long ID_Actividad){

        db.actualizar_detalle_x_actividad(db.OBTENER_CONSOLIDADO_EVENTOS(ID_Actividad));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Double MostrarPorcentajeBateria(Context ctx) {

        BatteryManager bm = (BatteryManager)ctx.getSystemService(BATTERY_SERVICE);
        Integer batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        return new Double(batLevel);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Long Guardar_Detalle_X_Actividad(final Context ctx, final BaseDatos db, final WVAApplication wvaapp, Long Id_Actividad, String fechaActual) {

        Long registro = 0L;
        Long registro_detalle_insertado = 0L;
        Metodos.Update_Detalle_X_Actividad(db,Id_Actividad);
        db.borrar_tabla("EVENTOS_DETALLE_ACTIVIDAD");
        final Long id = db.ObtenerUltimoID("ACTIVIDADES_X_EQUIPO");
        if(id != 0){

            Detalle_X_Actividad detalle_x_actividad = new Detalle_X_Actividad();

            detalle_x_actividad.setDXA_ID(0L);
            detalle_x_actividad.setId_Actividad(id);
            Configuracion configuracion = db.ObtenerConfiguracion();
            detalle_x_actividad.setLatitud(configuracion.getLatitud());
            detalle_x_actividad.setLongitud(configuracion.getLongitud());
            detalle_x_actividad.setHoras_Motor(null);
            detalle_x_actividad.setNivel_Combustible(null);
            detalle_x_actividad.setConsumo_Combustible(null);
            detalle_x_actividad.setTemperatura_Motor(null);
            detalle_x_actividad.setDXA_Objeto("N/D");
            detalle_x_actividad.setDXA_Cantidad_Eventos(0);
            detalle_x_actividad.setFecha_Modificacion(fechaActual);
            detalle_x_actividad.setPorcentaje_Bateria(MostrarPorcentajeBateria(ctx));
            detalle_x_actividad.setTipo_Senal(configuracion.getTipo_Senal());
            detalle_x_actividad.setIntensidad_Senal(configuracion.getIntensidad_Senal());

            registro_detalle_insertado = db.insertar_Detalle_X_Actividad(detalle_x_actividad);
            final Long registro_detalle_insertado_final = registro_detalle_insertado;

            wvaapp.getWva().isWVA(new WvaCallback<Boolean>() {
                @Override
                public void onResponse(Throwable error, Boolean success) {
                    if(error == null && success) {
                        RegistrarDatosDIGI_Detalle(registro_detalle_insertado_final,db,wvaapp);
                    }
                }
            });

        }

        return registro_detalle_insertado;
    }

    public static void RegistrarDatosDIGI_Detalle(final Long registro_detalle_insertado_final,
                                                  final BaseDatos db,
                                                  final WVAApplication wvaapp){
        final WVA wva = wvaapp.getWva();
        // Leemos la Temperatura del motor
        wva.fetchVehicleData("EngineCoolantTemp", new WvaCallback<VehicleDataResponse>() {
            @Override
            public void onResponse(Throwable error, VehicleDataResponse responseTemperatura) {

                Double temperatura_motor_tmp = null;
                if (error != null) {

                    // Error Leyendo Temperatura de Motor
                } else {
                    temperatura_motor_tmp = responseTemperatura.getValue();
                    Double temperaturaMotor = temperatura_motor_tmp;

                    Detalle_X_Actividad detalle_x_actividad = new Detalle_X_Actividad();
                    detalle_x_actividad.setId(registro_detalle_insertado_final);
                    detalle_x_actividad.setTemperatura_Motor(temperaturaMotor);
                    db.actualizar_detalle_x_actividad_temperatura_motor(detalle_x_actividad);

                }

            }
        });

        // Leemos el consumo de combustible
        wva.fetchVehicleData("TotalFuelUsed", new WvaCallback<VehicleDataResponse>() {
            @Override
            public void onResponse(Throwable error, VehicleDataResponse responseCombustible) {

                Double consumo_combustible_tmp = null;

                if (error != null) {

                    // Error leyendo nivel de combustible
                } else {
                    consumo_combustible_tmp = responseCombustible.getValue();
                    final Double consumoCombustible = consumo_combustible_tmp;

                    Detalle_X_Actividad detalle_x_actividad = new Detalle_X_Actividad();
                    detalle_x_actividad.setId(registro_detalle_insertado_final);
                    detalle_x_actividad.setConsumo_Combustible(consumoCombustible);
                    db.actualizar_detalle_x_actividad_consumo_combustible(detalle_x_actividad);
                }

            }
        });

        // Leemos el nivel de combustible
        wva.fetchVehicleData("FuelLevel", new WvaCallback<VehicleDataResponse>() {
            @Override
            public void onResponse(Throwable error, VehicleDataResponse responseCombustible) {

                Double nivelCombustible = null;

                if (error != null) {

                } else {
                    nivelCombustible = responseCombustible.getValue();
                    Detalle_X_Actividad detalle_x_actividad = new Detalle_X_Actividad();
                    detalle_x_actividad.setId(registro_detalle_insertado_final);
                    detalle_x_actividad.setNivel_Combustible(nivelCombustible);
                    db.actualizar_detalle_x_actividad_nivel_combustible(detalle_x_actividad);
                }

            }
        });


        // Leemos la horas motor del DIGI...
        wva.fetchVehicleData("TotalEngineHours", new WvaCallback<VehicleDataResponse>() {
            @Override
            public void onResponse(Throwable error, VehicleDataResponse responseHoras) {

                Double horas_motor_tmp = null;

                if (error != null) {

                    // Error leyendo horas motor
                } else {
                    horas_motor_tmp = responseHoras.getValue();

                    final Double horasMotor = horas_motor_tmp;

                    Detalle_X_Actividad detalle_x_actividad = new Detalle_X_Actividad();
                    detalle_x_actividad.setId(registro_detalle_insertado_final);
                    detalle_x_actividad.setHoras_Motor(horasMotor);
                    db.actualizar_detalle_x_actividad_horas_motor(detalle_x_actividad);
                }

            }
        });
    }


    /*Metodos para la descarga de Maestros*/
    public static void GuardarActividadesInactivas(final Context ctx, final BaseDatos db, String Maestro) {

        final Date TiempoInicioBajada = new Date();

        String url = ArmarURLMaestro(ctx,Maestro);
        final Gson gson = new Gson();
//        final BaseDatos db = new BaseDatos(ctx);

        final ProgressDialog progressDialog_Maestro;
        progressDialog_Maestro = new ProgressDialog(ctx,R.style.TemaDialogoProgreso);
        progressDialog_Maestro.setMessage(ctx.getString(R.string.bajando_maestro_actividadesinactivas_msg));
        progressDialog_Maestro.show();

        new ConexionHTTP(ctx, ctx.getString(R.string.bajando_maestro_actividadesinactivas_msg), url, Constantes.GET, "", new ConnectionInterface() {
            @Override
            public void doProcess(String output) {

                if (progressDialog_Maestro != null && progressDialog_Maestro.isShowing()) {
                    progressDialog_Maestro.dismiss();
                }

                if (output == null) {
                    Toast.makeText(ctx, "Sin red", Toast.LENGTH_SHORT).show();
                } else {

                    final ActividadesInactivas_RESPONSE respuesta;
                    try {
                        respuesta = gson.fromJson(output, new TypeToken<ActividadesInactivas_RESPONSE>() {
                        }.getType());
                        if (respuesta.isExito()) {

                            new AsyncTask<Void, Void, Void>() {
                                private ProgressDialog progressDialog;

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    progressDialog = new ProgressDialog(ctx,R.style.TemaDialogoProgreso);
                                    progressDialog.setMessage(ctx.getString(R.string.guardando_maestro_actividadesinactivas_msg));
                                    progressDialog.show();
                                }

                                @Override
                                protected Void doInBackground(Void... params) {

                                    db.borrar_tabla("ActividadesInactivas");

                                    List<ActividadesInactivas> listaActividadesInactivas = respuesta.getResultado();
                                    db.insertar_ActividadesInactivas(listaActividadesInactivas);

                                    // Medimos el tiempo de bajada y guardado de datos
                                    Date TiempoFinBajada = new Date();
                                    Long duracion = Calcular_Diferencia_Segundos(TiempoInicioBajada,TiempoFinBajada);
                                    Log.i("Tiempo","Actividad Inactiva: " + duracion + " segundos");

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    Verificar_cambio_pantalla(ctx,db,"Actividad Inactiva");

                                }
                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

                        } else {
                            Toast.makeText(ctx, ctx.getString(R.string.bajando_maestro_actividadesinactivas_error), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                        Toast.makeText(ctx, "Ocurrió un problema al convertir la información proveniente de los servicios, favor, intente nuevamente", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

    }

    public static void GuardarFrentes(final Context ctx, final BaseDatos db, String Maestro) {

        final Date TiempoInicioBajada = new Date();

        String url = ArmarURLMaestro(ctx,Maestro);
        final Gson gson = new Gson();
//        final BaseDatos db = new BaseDatos(ctx);

        final ProgressDialog progressDialog_Maestro;
        progressDialog_Maestro = new ProgressDialog(ctx,R.style.TemaDialogoProgreso);
        progressDialog_Maestro.setMessage(ctx.getString(R.string.bajando_maestro_frentes_msg));
        progressDialog_Maestro.show();

        new ConexionHTTP(ctx, ctx.getString(R.string.bajando_maestro_frentes_msg), url, Constantes.GET, "", new ConnectionInterface() {
            @Override
            public void doProcess(String output) {

                if (progressDialog_Maestro != null && progressDialog_Maestro.isShowing()) {
                    progressDialog_Maestro.dismiss();
                }

                if (output == null) {
                    Toast.makeText(ctx, "Sin red", Toast.LENGTH_SHORT).show();
                } else {

                    final Frentes_RESPONSE respuesta;
                    try {
                        respuesta = gson.fromJson(output, new TypeToken<Frentes_RESPONSE>() {
                        }.getType());
                        if (respuesta.isExito()) {

                            new AsyncTask<Void, Void, Void>() {
                                private ProgressDialog progressDialog;

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    progressDialog = new ProgressDialog(ctx,R.style.TemaDialogoProgreso);
                                    progressDialog.setMessage(ctx.getString(R.string.guardando_maestro_frentes_msg));
                                    progressDialog.show();
                                }

                                @Override
                                protected Void doInBackground(Void... params) {
                                    db.borrar_tabla("Frentes");

                                    List<Frentes> listaFrentes = respuesta.getResultado();
                                    db.insertar_Frentes(listaFrentes);

                                    // Medimos el tiempo de bajada y guardado de datos
                                    Date TiempoFinBajada = new Date();
                                    Long duracion = Calcular_Diferencia_Segundos(TiempoInicioBajada,TiempoFinBajada);
                                    Log.i("Tiempo","Frentes: " + duracion + " segundos");

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    Verificar_cambio_pantalla(ctx,db,"Frente");

                                }
                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

                        } else {
                            Toast.makeText(ctx, ctx.getString(R.string.bajando_maestro_frentes_error), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                        Toast.makeText(ctx, "Ocurrió un problema al convertir la información proveniente de los servicios, favor, intente nuevamente", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

    }


    public static void GuardarOS(final Context ctx,final BaseDatos db, String Maestro) {

        final Date TiempoInicioBajada = new Date();

        String url = ArmarURLMaestro(ctx,Maestro);
        final Gson gson = new Gson();
//        final BaseDatos db = new BaseDatos(ctx);

        final ProgressDialog progressDialog_Maestro;
        progressDialog_Maestro = new ProgressDialog(ctx,R.style.TemaDialogoProgreso);
        progressDialog_Maestro.setMessage(ctx.getString(R.string.bajando_maestro_os_msg));
        progressDialog_Maestro.show();

        new ConexionHTTP(ctx, ctx.getString(R.string.bajando_maestro_os_msg), url, Constantes.GET, "", new ConnectionInterface() {
            @Override
            public void doProcess(String output) {

                if (progressDialog_Maestro != null && progressDialog_Maestro.isShowing()) {
                    progressDialog_Maestro.dismiss();
                }

                if (output == null) {
                    Toast.makeText(ctx, "Sin red", Toast.LENGTH_SHORT).show();
                } else {


                    final OS_RESPONSE respuesta;
                    respuesta = gson.fromJson(output, new TypeToken<OS_RESPONSE>() {
                    }.getType());
                    if (respuesta.isExito()) {

                        new AsyncTask<Void, Void, Void>() {
                            private ProgressDialog progressDialog;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog = new ProgressDialog(ctx,R.style.TemaDialogoProgreso);
                                progressDialog.setMessage(ctx.getString(R.string.guardando_maestro_os_msg));
                                progressDialog.show();
                            }

                            @Override
                            protected Void doInBackground(Void... params) {
                                db.borrar_tabla("OS");

                                List<OS> listaos = respuesta.getResultado();
                                db.insertar_OS(listaos);

                                // Medimos el tiempo de bajada y guardado de datos
                                Date TiempoFinBajada = new Date();
                                Long duracion = Calcular_Diferencia_Segundos(TiempoInicioBajada,TiempoFinBajada);
                                Log.i("Tiempo","OS: " + duracion + " segundos");

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Verificar_cambio_pantalla(ctx,db,"OS");

                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);


                    } else {
                        Toast.makeText(ctx, ctx.getString(R.string.bajando_maestro_os_error), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

    }

    public static void GuardarImplementos(final Context ctx, final BaseDatos db, String Maestro) {


        final Date TiempoInicioBajada = new Date();

        String url = ArmarURLMaestro(ctx,Maestro);

        final Gson gson = new Gson();
//        final BaseDatos db = new BaseDatos(ctx);

        final ProgressDialog progressDialog_Maestro;
        progressDialog_Maestro = new ProgressDialog(ctx,R.style.TemaDialogoProgreso);
        progressDialog_Maestro.setMessage(ctx.getString(R.string.bajando_maestro_implementos_msg));
        progressDialog_Maestro.show();

        new ConexionHTTP(ctx, ctx.getString(R.string.bajando_maestro_implementos_msg), url, Constantes.GET, "", new ConnectionInterface() {
            @Override
            public void doProcess(String output) {

                if (progressDialog_Maestro != null && progressDialog_Maestro.isShowing()) {
                    progressDialog_Maestro.dismiss();
                }

                if (output == null) {
                    Toast.makeText(ctx, "Sin red", Toast.LENGTH_SHORT).show();
                } else {

                    final Implementos_RESPONSE respuesta;
                    respuesta = gson.fromJson(output, new TypeToken<Implementos_RESPONSE>() {
                    }.getType());
                    if (respuesta.isExito()) {

                        new AsyncTask<Void, Void, Void>() {
                            private ProgressDialog progressDialog;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog = new ProgressDialog(ctx,R.style.TemaDialogoProgreso);
                                progressDialog.setMessage(ctx.getString(R.string.guardando_maestro_implementos_msg));
                                progressDialog.show();
                            }

                            @Override
                            protected Void doInBackground(Void... params) {
                                db.borrar_tabla("Implementos");

                                List<Implementos> listaImplementos = respuesta.getResultado();
                                db.insertar_Implementos(listaImplementos);

                                // Medimos el tiempo de bajada y guardado de datos
                                Date TiempoFinBajada = new Date();
                                Long duracion = Calcular_Diferencia_Segundos(TiempoInicioBajada,TiempoFinBajada);
                                Log.i("Tiempo","Implementos: " + duracion + " segundos");

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Verificar_cambio_pantalla(ctx,db,"Implemento");
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);


                    } else {
                        Toast.makeText(ctx, ctx.getString(R.string.bajando_maestro_implementos_error), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

    }

    public static void GuardarEmpleados(final Context ctx,final BaseDatos db, String Maestro) {

        final Date TiempoInicioBajada = new Date();

        String url = ArmarURLMaestro(ctx,Maestro);

        final Gson gson = new Gson();
//        final BaseDatos db = new BaseDatos(ctx);

        final ProgressDialog progressDialog_Maestro;
        progressDialog_Maestro = new ProgressDialog(ctx,R.style.TemaDialogoProgreso);
        progressDialog_Maestro.setMessage(ctx.getString(R.string.bajando_maestro_empleados_msg));
        progressDialog_Maestro.show();

        new ConexionHTTP(ctx, ctx.getString(R.string.bajando_maestro_empleados_msg), url, Constantes.GET, "", new ConnectionInterface() {
            @Override
            public void doProcess(String output) {

                if (progressDialog_Maestro != null && progressDialog_Maestro.isShowing()) {
                    progressDialog_Maestro.dismiss();
                }

                if (output == null) {
                    Toast.makeText(ctx, "Sin red", Toast.LENGTH_SHORT).show();
                } else {

                    final Empleados_RESPONSE respuesta;
                    respuesta = gson.fromJson(output, new TypeToken<Empleados_RESPONSE>() {
                    }.getType());
                    if (respuesta.isExito()) {

                        new AsyncTask<Void, Void, Void>() {
                            private ProgressDialog progressDialog;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog = new ProgressDialog(ctx,R.style.TemaDialogoProgreso);
                                progressDialog.setMessage(ctx.getString(R.string.guardando_maestro_empleados_msg));
                                progressDialog.show();
                            }

                            @Override
                            protected Void doInBackground(Void... params) {
                                db.borrar_tabla("Empleados");

                                List<Empleados> listaEmpleados = respuesta.getResultado();
                                db.insertar_Empleados(listaEmpleados);

                                // Medimos el tiempo de bajada y guardado de datos
                                Date TiempoFinBajada = new Date();
                                Long duracion = Calcular_Diferencia_Segundos(TiempoInicioBajada,TiempoFinBajada);
                                Log.i("Tiempo","Empleados: " + duracion + " segundos");

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Verificar_cambio_pantalla(ctx,db,"Empleado");
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);


                    } else {
                        Toast.makeText(ctx, ctx.getString(R.string.bajando_maestro_empleados_error), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

    }


    //---
    public static void GuardarDispositivo(final Context ctx,final BaseDatos db, String Maestro) {

        final Date TiempoInicioBajada = new Date();

        String url = ArmarURLMaestro(ctx,Maestro);

        final Gson gson = new Gson();

        new ConexionHTTP(ctx, ctx.getString(R.string.bajando_maestro_dispositivo_msg), url, Constantes.GET, "", new ConnectionInterface() {
            @Override
            public void doProcess(String output) {
                if (output == null) {
                    Toast.makeText(ctx, "Sin red", Toast.LENGTH_SHORT).show();
                } else {

                    final Dispositivo_RESPONSE respuesta;
                    respuesta = gson.fromJson(output, new TypeToken<Dispositivo_RESPONSE>() {
                    }.getType());
                    if (respuesta.isExito()) {

                        new AsyncTask<Void, Void, Void>() {
                            private ProgressDialog progressDialog;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog = new ProgressDialog(ctx,R.style.TemaDialogoProgreso);
                                progressDialog.setMessage(ctx.getString(R.string.guardando_maestro_dispositivo_msg));
                                progressDialog.show();
                            }

                            @Override
                            protected Void doInBackground(Void... params) {

                                Configuracion configuracion = new Configuracion();

                                for (Dispositivo dispositivo : respuesta.getResultado()) {
                                    configuracion.setEquipo(dispositivo.getEquipo());
                                    configuracion.setUltima_Version_App(dispositivo.getVersion_App());
                                    configuracion.setURL_Descarga_APK(dispositivo.getURL_Descarga_APK());
                                    db.actualizar_configuracion_Equipo(configuracion);
                                    db.actualizar_configuracion_Ultima_Version_App(configuracion);
                                    db.actualizar_configuracion_URL_Descarga_APK(configuracion);
                                }

                                Date TiempoFinBajada = new Date();
                                Long duracion = Calcular_Diferencia_Segundos(TiempoInicioBajada,TiempoFinBajada);
                                Log.i("Tiempo","Dispositivo: " + duracion + " segundos");

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);


                    } else {
                        Toast.makeText(ctx, ctx.getString(R.string.bajando_maestro_dispositivo_error), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

    }
    public static void GuardarVersion(final Context ctx,final BaseDatos db, String Maestro) {

        final Date TiempoInicioBajada = new Date();

        //String url = ArmarURLMaestro(ctx,Maestro);
        String url = String.format(Maestro + Constantes.PARAMETROS_SERVICIOS_BAJAR_MAESTROS,
                Constantes.COMPANIA_SSA,
                Metodos.Obtener_UUID_dispositivo(ctx),
                "1.1");

        final Gson gson = new Gson();

        new ConexionHTTP(ctx, ctx.getString(R.string.bajando_maestro_version_msg), url, Constantes.GET, "", new ConnectionInterface() {
            @Override
            public void doProcess(String output) {
                if (output == null) {
                    Toast.makeText(ctx, "Sin red", Toast.LENGTH_SHORT).show();
                } else {

                    final Version_RESPONSE respuesta;
                    respuesta = gson.fromJson(output, new TypeToken<Version_RESPONSE>() {
                    }.getType());
                    if (respuesta.isExito()) {

                        new AsyncTask<Void, Void, Void>() {
                            private ProgressDialog progressDialog;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog = new ProgressDialog(ctx,R.style.TemaDialogoProgreso);
                                progressDialog.setMessage(ctx.getString(R.string.guardando_maestro_version_msg));
                                progressDialog.show();
                            }

                            @Override
                            protected Void doInBackground(Void... params) {

                                Configuracion configuracion = new Configuracion();

                                for (Version version : respuesta.getResultado()) {
                                    configuracion.setUltima_Version_App(version.getVersion());
                                    configuracion.setURL_Descarga_APK(version.getUrl());
                                    db.actualizar_configuracion_Ultima_Version_App(configuracion);
                                    db.actualizar_configuracion_URL_Descarga_APK(configuracion);
                                }

                                Date TiempoFinBajada = new Date();
                                Long duracion = Calcular_Diferencia_Segundos(TiempoInicioBajada,TiempoFinBajada);
                                Log.i("Tiempo","Version: " + duracion + " segundos");

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);


                    } else {
                        Toast.makeText(ctx, ctx.getString(R.string.bajando_maestro_version_error), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

    }
    //---

    public static String ArmarURLMaestro(Context ctx, String Maestro) {

        return String.format(Maestro + Constantes.PARAMETROS_SERVICIOS_BAJAR_MAESTROS,
                Constantes.COMPANIA_SSA,
                Metodos.Obtener_UUID_dispositivo(ctx),
                Metodos.Obtener_Version_Name()
        );

    }


    public static void Verificar_cambio_pantalla(Context ctx, BaseDatos db, String Maestro){

        Configuracion configuracion = new Configuracion();
        switch (Maestro){
            case "Frente":
                configuracion.setFlag_Frente(Constantes.TRUE);
                db.actualizar_configuracion_flag_frente(configuracion);
                break;
            case "OS":
                configuracion.setFlag_OS(Constantes.TRUE);
                db.actualizar_configuracion_Flag_OS(configuracion);
                break;
            case "Actividad Inactiva":
                configuracion.setFlag_Actividad_Inactiva(Constantes.TRUE);
                db.actualizar_configuracion_Flag_Actividad_Inactiva(configuracion);
                break;
            case "Implemento":
                configuracion.setFlag_Implemento(Constantes.TRUE);
                db.actualizar_configuracion_Flag_Implemento(configuracion);
                break;
            case "Empleado":
                configuracion.setFlag_Empleado(Constantes.TRUE);
                db.actualizar_configuracion_Flag_Empleado(configuracion);
                break;
        }

        configuracion = db.ObtenerConfiguracion();
        if(
           configuracion.getFlag_Frente().equals(Constantes.TRUE) &&
           configuracion.getFlag_OS().equals(Constantes.TRUE) &&
           configuracion.getFlag_Actividad_Inactiva().equals(Constantes.TRUE) &&
           configuracion.getFlag_Implemento().equals(Constantes.TRUE) &&
           configuracion.getFlag_Empleado().equals(Constantes.TRUE)
                ){
            Intent intent = new Intent(ctx, MainActivity.class);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();

        }

    }

    public static String VerificarFiltrosValidos(BaseDatos db){
        // Validamos los filtros obligatorios
        String filtros = "";
        Configuracion conf = db.ObtenerConfiguracion();
        String os        = conf.getOS();
        String actividad = conf.getActividad();
        String plantio   = conf.getPlantio();
        String empleado  = conf.getEmpleado();
        String frente    = conf.getFrente();
        Boolean seleccionNoValidaFiltro1   = os.equals("0") && actividad.equals("") && plantio.equals("0");
        Boolean seleccionNoValidaFiltro2   = empleado.equals("");
        Boolean seleccionNoValidaFiltro3   = frente.equals("");
        Boolean NopasaValidacionFiltros = seleccionNoValidaFiltro1 || seleccionNoValidaFiltro2 || seleccionNoValidaFiltro3;
        if(NopasaValidacionFiltros){
            filtros += seleccionNoValidaFiltro1 ? " * Falta el filtro de OS, Actividad y Plantio\n" :"";
            filtros += seleccionNoValidaFiltro2 ? " * Falta el filtro de Empleado\n" :"";
            filtros += seleccionNoValidaFiltro3 ? " * Falta el filtro de Frente\n" :"";
        }
        return filtros;
    }

}
