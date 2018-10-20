package com.example.gbaca.seguimento_equipo_agricola.Actividades;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.digi.wva.async.WvaCallback;
import com.example.gbaca.seguimento_equipo_agricola.Servicios.EVENTOS_X_DETALLE;
import com.example.gbaca.seguimento_equipo_agricola.Servicios.WVAApplication;
import com.example.gbaca.seguimento_equipo_agricola.data.Configuracion;
import com.facebook.stetho.Stetho;
import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.Constantes;
import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.Metodos;
import com.example.gbaca.seguimento_equipo_agricola.R;
import com.example.gbaca.seguimento_equipo_agricola.Servicios.Envio_Datos_Timer;
import com.example.gbaca.seguimento_equipo_agricola.data.Actividades_X_Equipo;
import com.example.gbaca.seguimento_equipo_agricola.data.BaseDatos;
import com.example.gbaca.seguimento_equipo_agricola.data.Empleados;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  {
    private WVAApplication app;
    private Context contexto;
    private BaseDatos db;
    final Handler handler = new Handler();

    // Para obtener información de intensidad y tipo señal celular
    TelephonyManager tempManager;
    // Inner class para manejar el Listener de la señal celular
    class PhoneCustomStateListener extends PhoneStateListener {

        public int signalSupport = 0;

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            signalSupport = signalStrength.getGsmSignalStrength();

            // Obtenemos el tipo de señal celular
            int networkType = tempManager.getNetworkType();
            String TipoRed = "";
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT: TipoRed = "1xRTT";
                    break;
                case TelephonyManager.NETWORK_TYPE_CDMA: TipoRed = "CDMA";
                    break;
                case TelephonyManager.NETWORK_TYPE_EDGE: TipoRed = "EDGE";
                    break;
                case TelephonyManager.NETWORK_TYPE_EHRPD: TipoRed = "eHRPD";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0: TipoRed = "EVDO rev. 0";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A: TipoRed = "EVDO rev. A";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_B: TipoRed = "EVDO rev. B";
                    break;
                case TelephonyManager.NETWORK_TYPE_GPRS: TipoRed = "GPRS";
                    break;
                case TelephonyManager.NETWORK_TYPE_HSDPA: TipoRed = "HSDPA";
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPA: TipoRed = "HSPA";
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPAP: TipoRed = "HSPA+";
                    break;
                case TelephonyManager.NETWORK_TYPE_HSUPA: TipoRed = "HSUPA";
                    break;
                case TelephonyManager.NETWORK_TYPE_IDEN: TipoRed = "iDen";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE: TipoRed = "LTE";
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS: TipoRed = "UMTS";
                    break;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN: TipoRed = "Unknown";
                    break;
            }

            Configuracion configuracion = new Configuracion();
            configuracion.setIntensidad_Senal(new Double(signalSupport));
            configuracion.setTipo_Senal(TipoRed);
            db.actualizar_configuracion_Intensidad_Senal(configuracion);
            db.actualizar_configuracion_Tipo_Senal(configuracion);

        }
    }



    Timer timer_Conectado_DIGI;

    private IntentIntegrator qrScan;

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 0;
    private static final float LOCATION_DISTANCE = 0; //20
    private PowerManager.WakeLock wakeLock;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void revertir(View view) {

        Actividades_X_Equipo actividades_x_equipo = db.ObtenerUltimaActividad_X_Equipo();

        String OS = null;
        String Actividad = null;
        String Actividad_N = null;
        String ActividadInactiva = null;
        String ActividadInactiva_N = null;
        String Plantio = null;
        String Plantio_N = null;
        String Frente = null;
        String Frente_N = null;
        String Empleado = null;
        String Empleado_N = null;
        String Implemento1 = null;
        String Implemento2 = null;
        String Implemento3 = null;

        if(actividades_x_equipo!=null){

             OS = actividades_x_equipo.getOrden_Servicio();
             Actividad = actividades_x_equipo.getActividad();
             Actividad_N = db.ObtenerNombreActividadxCodigo(actividades_x_equipo.getActividad());
             ActividadInactiva = actividades_x_equipo.getAXE_Actividad_Inactiva();
             ActividadInactiva_N = db.ObtenerNombreActividadInactivaxCodigo(actividades_x_equipo.getAXE_Actividad_Inactiva());
             Plantio = actividades_x_equipo.getPlantio();
             Plantio_N = db.ObtenerNombrePlantioxCodigo(actividades_x_equipo.getPlantio());
             Frente = actividades_x_equipo.getFrente();
             Frente_N = db.ObtenerNombreFrentexCodigo(Frente);
             Empleado = actividades_x_equipo.getOperador();
             Empleado_N = db.ObtenerEmpleadoxCodigo(Empleado).getEmpleado();
             Implemento1 = actividades_x_equipo.getImplemento_1();
             Implemento2 = actividades_x_equipo.getImplemento_2();
             Implemento3 = actividades_x_equipo.getImplemento_3();

        }else{

             OS = "0";
             Actividad = "";
             Actividad_N = "";
             ActividadInactiva = "";
             ActividadInactiva_N = "Activo";
             Plantio = "0";
             Plantio_N = "";
             Frente = "";
             Frente_N = "";
             Empleado = "";
             Empleado_N = "";
             Implemento1 = "";
             Implemento2 = "";
             Implemento3 = "";

        }
            Configuracion configuracion = new Configuracion();
            configuracion.setCambios(Constantes.FALSE);
            configuracion.setFrente(Frente);
            configuracion.setFrente_N(Frente_N);
            configuracion.setOS(OS);
            configuracion.setPlantio(Plantio);
            configuracion.setPlantio_N(Plantio_N);
            configuracion.setActividad(Actividad);
            configuracion.setActividad_N(Actividad_N);
            configuracion.setActividadInactiva(ActividadInactiva);
            configuracion.setActividadInactiva_N(ActividadInactiva_N);
            configuracion.setImplemento1(Implemento1);
            configuracion.setImplemento2(Implemento2);
            configuracion.setImplemento3(Implemento3);
            configuracion.setEmpleado(Empleado);
            configuracion.setEmpleado_N(Empleado_N);
            db.actualizar_configuracion_Frente(configuracion);
            db.actualizar_configuracion_OS(configuracion);
            db.actualizar_configuracion_Implemento1(configuracion);
            db.actualizar_configuracion_Implemento2(configuracion);
            db.actualizar_configuracion_Implemento3(configuracion);
            db.actualizar_configuracion_Empleado(configuracion);
            db.actualizar_configuracion_ActividadInactiva(configuracion);
            db.actualizar_configuracion_Cambios(configuracion);

            TextView tvOrdenServicio = (TextView) findViewById(R.id.tvOrdenServicio);
            TextView tvPlantio = (TextView) findViewById(R.id.tvPlantio);
            TextView tvActividad = (TextView) findViewById(R.id.tvActividad);
            TextView tvImplemento1 = (TextView) findViewById(R.id.tvImplemento1);
            TextView tvImplemento2 = (TextView) findViewById(R.id.tvImplemento2);
            TextView tvImplemento3 = (TextView) findViewById(R.id.tvImplemento3);
            TextView tv_ficha_empleado = (TextView) findViewById(R.id.tv_ficha_empleado);
            TextView tv_nombre_empleado = (TextView) findViewById(R.id.tv_nombre_empleado);
            TextView tv_codigo_frente = (TextView) findViewById(R.id.tv_codigo_frente);

            tvOrdenServicio.setText(OS);
            tvPlantio.setText(Plantio +" - "+Plantio_N);
            tvActividad.setText(Actividad+" - "+Actividad_N);
            mostrarActividadInactiva(configuracion);
            tvImplemento1.setText(Implemento1);
            tvImplemento2.setText(Implemento2);
            tvImplemento3.setText(Implemento3);
            tv_ficha_empleado.setText(Empleado);
            tv_nombre_empleado.setText(Empleado_N);
            tv_codigo_frente.setText(Frente_N);

            startActivity(new Intent(MainActivity.this,MainActivity.class));
            finish();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void mostrarActividadInactiva(Configuracion configuracion) {

        ImageView icono_actividad_inactiva = (ImageView) findViewById(R.id.icono_actividad_inactiva);
        FrameLayout background_Inactividad = (FrameLayout) findViewById(R.id.bk_inactiva);

        TextView tvActividadInactiva = (TextView) findViewById(R.id.tvActividadInactiva);
        if(configuracion.getActividadInactiva().equals("")){
            icono_actividad_inactiva.setImageResource(R.drawable.ic_activo);
            tvActividadInactiva.setText("Activo");
            tvActividadInactiva.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            background_Inactividad.setBackground(ContextCompat.getDrawable(contexto, R.drawable.osbackground_fila_inferior));
        }else{
            tvActividadInactiva.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            icono_actividad_inactiva.setImageResource(R.drawable.ic_danio);
            background_Inactividad.setBackground(ContextCompat.getDrawable(contexto, R.drawable.osbackground_fila_inferior_danado));
            tvActividadInactiva.setText(configuracion.getActividadInactiva() + " - " + configuracion.getActividadInactiva_N());
        }

    }

    private void mostrarEquipo(Configuracion configuracion){
        TextView tv_equipo = (TextView) findViewById(R.id.tv_equipo);
        tv_equipo.setText(configuracion.getEquipo());
    }


    public void ScanCodigoEmpleado(View view) {
        qrScan.initiateScan();
    }


    public void cambiarActividadInactiva(View view) {

        Intent intent = new Intent(this, buscadorActividadInactiva.class);
        startActivity(intent);
        finish();

    }


    public void mostrarPendientes(View view) {

        String Fecha_Ultimo_Envio = db.OBTENER_FECHA_ULTIMO_ENVIO();
        Fecha_Ultimo_Envio = Fecha_Ultimo_Envio == null ? "-" : Fecha_Ultimo_Envio;

        String mensajeDialogo =  "Registros Pendientes:    \n" + db.OBTENER_ENVIOS_PENDIENTES()             + "\n\n";
               mensajeDialogo += "Ultimo Envio:            \n" + Fecha_Ultimo_Envio                         + "\n\n";
               mensajeDialogo += "ID Dispositivo:          \n " + Metodos.Obtener_UUID_dispositivo(contexto);

                AlertDialog dialogo =
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.app_name))
                        .setMessage(mensajeDialogo)
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setCancelable(false)
                        .show();

        TextView textodialogo = (TextView) dialogo.findViewById(android.R.id.message);
        Button btnSI = (Button) dialogo.findViewById(android.R.id.button1);
        Button btnNO = (Button) dialogo.findViewById(android.R.id.button2);
        btnSI.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));
        btnNO.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));
        textodialogo.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));



    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void MostrarPorcentajeBateria(View view) {

        // Bateria
        BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        // Conectividad a Internet
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        Toast.makeText(contexto,"Porcentaje de Bateria: " + batLevel + "%. Conectado: " + isConnected,Toast.LENGTH_SHORT).show();


    }


    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {

            mLastLocation = new Location(provider);
        }



        @Override
        public void onLocationChanged(Location location) {

            Configuracion configuracion = db.ObtenerConfiguracion();
            boolean hayActividadesxEquipo = db.existeActividadesxEquipo(configuracion.getEquipo());
            if (location != null && hayActividadesxEquipo) {

                Double Distancia = 0.0;

                if(configuracion.getLatitud() != null){
                    Location location_anterior = new Location("");
                    location_anterior.setLatitude(configuracion.getLatitud());
                    location_anterior.setLongitude(configuracion.getLongitud());
                    Distancia = configuracion.getDistancia();
                    Distancia += location.distanceTo(location_anterior);
                }
                configuracion.setLatitud(location.getLatitude());
                configuracion.setLongitud(location.getLongitude());
                configuracion.setDistancia(Distancia);

                db.actualizar_configuracion_Location(configuracion);
                db.actualizar_configuracion_Distancia(configuracion);
            }
        }


        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {


        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };


    private void initializeLocationManager() {

        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    @Override
    public void onDestroy()
    {

        handler.removeCallbacksAndMessages(null);

        wakeLock.acquire();

        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {

                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        initializeLocationManager();

        // Modo pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Para inspeccionar la db SQLite desde Chrome
        Stetho.initializeWithDefaults(this);

        setContentView(R.layout.activity_main);

        app = (WVAApplication) getApplicationContext();
        db = app.getdb();
        contexto = this;
        final Configuracion configuracion = db.ObtenerConfiguracion();

        // Mantener la CPU Encendida
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();

        // Validamos si la app esta actualizada
//        if(!(configuracion.getUltima_Version_App().equals(Metodos.Obtener_Version_Name()))){
//            Intent intent = new Intent(contexto, pantallaActualizarApp.class);
//            startActivity(intent);
//            finish();
//        }

        // Validamos si hay datos de maestros
        if(db.MaestroTieneDatos("OS") == false){

            Intent intent = new Intent(contexto, pantallaBienvenida.class);
            startActivity(intent);
            finish();

        }

        // Asociamos el Listener de la señal celular
        tempManager= (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        tempManager.listen(new PhoneCustomStateListener(),
                PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        // Verificamos si GPS está encendido, si no está encendido damos la opción para encenderlo
        LocationManager  locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean  GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(GpsStatus == false){
            AlertDialog dialogo =
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.titulo_dialogo_gps))
                    .setMessage(getString(R.string.mensaje_dialogo_gps))
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try{

                                Intent  intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent1);

                            }catch (Exception e){
                            }finally {
                                finish();
                            }

                        }
                    }).setCancelable(false)
                    .show();
            TextView textodialogo = (TextView) dialogo.findViewById(android.R.id.message);
            Button btnSI = (Button) dialogo.findViewById(android.R.id.button1);
            Button btnNO = (Button) dialogo.findViewById(android.R.id.button2);
            btnSI.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));
            btnNO.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));
            textodialogo.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));

        }

        if(db.existeActividadesxEquipo(configuracion.getEquipo())){
            // Lanzamos el servicio de 20 segundo: Detalle
//            startService(new Intent(this, Detalle_x_Actividad_Timer.class));
            startService(new Intent(this, EVENTOS_X_DETALLE.class));

            // Lanzamos el servicio de 2 minutos: Envio
            startService(new Intent(this, Envio_Datos_Timer.class));
        }


        final TextView contadorActividd = (TextView) findViewById(R.id.contadorActividd);
        final String fechaInicioActividadVigente = db.ObtenerFechaInicioActividadVigente();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,1000);

                if(fechaInicioActividadVigente!=null){
                    if(configuracion.getCambios() == Constantes.TRUE){
                        contadorActividd.setText("--:--:--");
                    }else{
                        String date1 = fechaInicioActividadVigente;
                        String date2 = Metodos.fecha_actual();

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        Date d1 = null;
                        Date d2 = null;
                        try {
                            d1 = format.parse(date1);
                            d2 = format.parse(date2);
                        } catch (ParseException e) {
                            e.printStackTrace();

                        }

                        long diff = d2.getTime() - d1.getTime();
                        diff = diff / 1000;
                        long hours = diff / 3600;
                        long minutes = diff / 60 % 60;
                        long seconds = diff % 60;

                        contadorActividd.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                    }
                }

            }
        }, 0);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Escuchamos la GPS
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                mLocationListeners[0]);


        qrScan = new IntentIntegrator(this);
        qrScan.setCameraId(Constantes.CAMARA_FRONTAL);

        // Mostramos la seleccion de usuario en la UI

        if(configuracion!=null){
            TextView tv_codigo_frente = (TextView) findViewById(R.id.tv_codigo_frente);
            tv_codigo_frente.setText(configuracion.getFrente_N());

            TextView tvImplemento1 = (TextView) findViewById(R.id.tvImplemento1);
            tvImplemento1.setText(configuracion.getImplemento1());

            TextView tvImplemento2 = (TextView) findViewById(R.id.tvImplemento2);
            tvImplemento2.setText(configuracion.getImplemento2());

            TextView tvImplemento3 = (TextView) findViewById(R.id.tvImplemento3);
            tvImplemento3.setText(configuracion.getImplemento3());

            TextView tvOrdenServicio = (TextView) findViewById(R.id.tvOrdenServicio);
            tvOrdenServicio.setText(configuracion.getOS());

            TextView tvPlantio = (TextView) findViewById(R.id.tvPlantio);
            tvPlantio.setText(configuracion.getPlantio() + " - " + configuracion.getPlantio_N());

            TextView tvActividad = (TextView) findViewById(R.id.tvActividad);
            tvActividad.setText(configuracion.getActividad() + " - " + configuracion.getActividad_N());

            TextView tv_ficha_empleado = (TextView) findViewById(R.id.tv_ficha_empleado);
            TextView tv_nombre_empleado = (TextView) findViewById(R.id.tv_nombre_empleado);
            tv_ficha_empleado.setText(configuracion.getEmpleado());
            tv_nombre_empleado.setText(configuracion.getEmpleado_N());

            mostrarActividadInactiva(configuracion);

            mostrarEquipo(configuracion);

            ponerVisibilidadBotonesGuardar();

            // Cambiamos font family...
//            TextView contadorActividd = (TextView) findViewById(R.id.contadorActividd);
            TextView tvtextoOrdenServicio = (TextView) findViewById(R.id.tvtextoOrdenServicio);
            TextView tvActividadInactiva = (TextView) findViewById(R.id.tvActividadInactiva);
            TextView tvTextoPlantio = (TextView) findViewById(R.id.tvTextoPlantio);
            TextView tvTextoFrente = (TextView) findViewById(R.id.tvTextoFrente);
            TextView tvTextoImplemento = (TextView) findViewById(R.id.tvTextoImplemento);

            Typeface EuphemiaCASRegular = Typeface.createFromAsset(getAssets(),  "fonts/EuphemiaCASRegular.ttf");
            Typeface EuphemiaCASBold = Typeface.createFromAsset(getAssets(),  "fonts/EuphemiaCASBold.ttf");
            Typeface MontserratUltraLight = Typeface.createFromAsset(getAssets(),  "fonts/MontserratUltraLight.ttf");
            Typeface MontserratRegular = Typeface.createFromAsset(getAssets(),  "fonts/MontserratRegular.ttf");
            Typeface TahomaBold = Typeface.createFromAsset(getAssets(),  "fonts/TahomaBold.ttf");
            Typeface Tahoma = Typeface.createFromAsset(getAssets(),  "fonts/Tahoma.ttf");

            tvOrdenServicio.setTypeface(EuphemiaCASRegular);
            tvPlantio.setTypeface(EuphemiaCASRegular);
            contadorActividd.setTypeface(EuphemiaCASBold);
            tvtextoOrdenServicio.setTypeface(MontserratUltraLight);
            tvActividadInactiva.setTypeface(MontserratUltraLight);
            tvTextoPlantio.setTypeface(MontserratUltraLight);
            tv_ficha_empleado.setTypeface(MontserratRegular);
            tv_nombre_empleado.setTypeface(MontserratUltraLight);
            tv_codigo_frente.setTypeface(MontserratRegular);
            tvTextoFrente.setTypeface(MontserratUltraLight);
            tvTextoImplemento.setTypeface(Tahoma);
            tvImplemento1.setTypeface(TahomaBold);
            tvImplemento2.setTypeface(TahomaBold);
            tvImplemento3.setTypeface(TahomaBold);

            tvActividadInactiva.bringToFront();

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        // Petición inicial para verificar la conectividad contra el DIGI WVA
        app.getWva().isWVA(new WvaCallback<Boolean>() {
            @Override
            public void onResponse(Throwable error, Boolean success) {
                Configuracion configuracion = new Configuracion();
                if(error != null) {
                    configuracion.setDIGI_Conectado(Constantes.FALSE);
                }
                else {
                    if (success) {
                        configuracion.setDIGI_Conectado(Constantes.TRUE);
                    }
                    else {
                        configuracion.setDIGI_Conectado(Constantes.FALSE);

                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // Caemos aquí si el usuario cancela el escaneo
            } else {

                String ficha_escaneada = result.getContents();

                if(ficha_escaneada.split("-").length > 1){
                    ficha_escaneada = ficha_escaneada.split("-")[1];
                }


                Empleados empleado = db.ObtenerEmpleadoxCodigo(ficha_escaneada);

                if(empleado!=null){
                    TextView tv_ficha_empleado = (TextView) findViewById(R.id.tv_ficha_empleado);
                    TextView tv_nombre_empleado = (TextView) findViewById(R.id.tv_nombre_empleado);
                    tv_ficha_empleado.setText(empleado.getCodigo());
                    tv_nombre_empleado.setText(empleado.getEmpleado());

                    Configuracion configuracion = new Configuracion();
                    configuracion.setEmpleado(empleado.getCodigo());
                    configuracion.setEmpleado_N(empleado.getEmpleado());
                    configuracion.setCambios(Constantes.TRUE);

                    db.actualizar_configuracion_Empleado(configuracion);
                    db.actualizar_configuracion_Cambios(configuracion);

                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"La ficha del empleado no es valida",Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void AsynchTaskTimer_Conectado_DIGI() {
        final Handler handler = new Handler();

        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        // Mostramos el estado de la conexión contra el DIGI en la UI
                        Configuracion configuracion = db.ObtenerConfiguracion();
                        if(configuracion.getDIGI_Conectado() == Constantes.FALSE){
                               ImageView icono_canbus = (ImageView) findViewById(R.id.icono_canbus);
                               TextView tvestadocanbus = (TextView) findViewById(R.id.tvestadocanbus);
                               icono_canbus.setImageResource(R.drawable.ic_desconectado_canbus);
                               tvestadocanbus.setText("Desconectado");
                        }else{
                              ImageView icono_canbus = (ImageView) findViewById(R.id.icono_canbus);
                              TextView tvestadocanbus = (TextView) findViewById(R.id.tvestadocanbus);
                              icono_canbus.setImageResource(R.drawable.ic_conectado_canbus);
                              tvestadocanbus.setText("Conectado");
                        }

                        // Si el Hostpot esta deshabilitado, lo habilitamos
                        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        final int apState;
                        try {

                            apState = (Integer) wifiManager.getClass().getMethod("getWifiApState").invoke(wifiManager);
                            Integer HOSTPOT_ENCENDIDO = 13;
                            if (apState != HOSTPOT_ENCENDIDO) {

                                Method[] methods = wifiManager.getClass().getDeclaredMethods();
                                for (Method method : methods) {
                                    if (method.getName().equals("setWifiApEnabled")) {
                                        try {
                                            method.invoke(wifiManager, null, true);

                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        break;
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        };
      // timer_Conectado_DIGI = new Timer();
      //  timer_Conectado_DIGI.schedule(timertask, 0, 5000);
    }


//    public void AsynchTaskTimer() {
//        final Handler handler = new Handler();
//
//        final TextView contadorActividd = (TextView) findViewById(R.id.contadorActividd);
//        final Configuracion configuracion = db.ObtenerConfiguracion();
//        final String fechaInicioActividadVigente = db.ObtenerFechaInicioActividadVigente();
//
//        TimerTask timertask = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//
//                            if(fechaInicioActividadVigente!=null){
//                                if(configuracion.getCambios() == Constantes.TRUE){
//                                    contadorActividd.setText("--:--:--");
//                                }else{
//                                    String date1 = fechaInicioActividadVigente;
//                                    String date2 = Metodos.fecha_actual();
//
//                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//                                    Date d1 = null;
//                                    Date d2 = null;
//                                    try {
//                                        d1 = format.parse(date1);
//                                        d2 = format.parse(date2);
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//
//                                    }
//
//
//
//                                    long diff = d2.getTime() - d1.getTime();
//
//                                    diff = diff / 1000;
//
//                                    long hours = diff / 3600;
//                                    long minutes = diff / 60 % 60;
//                                    long seconds = diff % 60;
//
//                                    contadorActividd.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
//                                }
//                            }
//
//                    }
//                });
//            }
//        };
//        timer = new Timer();
//        timer.schedule(timertask, 0, 1000);
//    }

    public void ponerVisibilidadBotonesGuardar() {

        // Determina si muestra el botón activar, deshacer
        ImageView btnaplicar = (ImageView) findViewById(R.id.btnAplicar);
        ImageView btnrevertir = (ImageView) findViewById(R.id.btnRevertir);
        TextView tv_aplicar = (TextView) findViewById(R.id.tv_aplicar);
        TextView tv_revertir = (TextView) findViewById(R.id.tv_revertir);
        FrameLayout fondo_hay_cambios = (FrameLayout) findViewById(R.id.fondo_hay_cambios);

        Configuracion configuracion = db.ObtenerConfiguracion();
        if (configuracion.getCambios() == Constantes.TRUE) {
            btnaplicar.setVisibility(View.VISIBLE);
            btnrevertir.setVisibility(View.VISIBLE);
            tv_aplicar.setVisibility(View.VISIBLE);
            tv_revertir.setVisibility(View.VISIBLE);
            fondo_hay_cambios.setVisibility(View.VISIBLE);

        } else {
            btnaplicar.setVisibility(View.INVISIBLE);
            btnrevertir.setVisibility(View.INVISIBLE);
            tv_aplicar.setVisibility(View.INVISIBLE);
            tv_revertir.setVisibility(View.INVISIBLE);
            fondo_hay_cambios.setVisibility(View.INVISIBLE);
        }

    }

    public void bajar_maestros(View view) {

        AlertDialog dialogo =
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.titulo_dialogo_sincronizacion))
                .setMessage(getString(R.string.mensaje_dialogo_sincronizacion))
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        BaseDatos db = app.getdb();

                        Metodos.GuardarFrentes(contexto,db,app.GET_SERVICIO_OBTENER_FRENTES());
                        Metodos.GuardarOS(contexto,db,app.GET_SERVICIO_OBTENER_OS());
                        Metodos.GuardarActividadesInactivas(contexto,db,app.GET_OBTENER_ACTIVIDADESINACTIVAS());
                        Metodos.GuardarImplementos(contexto,db,app.GET_SERVICIO_OBTENER_IMPLEMENTOS());
                        Metodos.GuardarEmpleados(contexto,db,app.GET_SERVICIO_OBTENER_EMPLEADOS());
                        Metodos.GuardarDispositivo(contexto,db,app.GET_SERVICIO_OBTENER_DISPOSITIVO());
                        Metodos.GuardarVersion(contexto,db,app.GET_SERVICIO_OBTENER_VERSION());

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false)
          .show();

        TextView textodialogo = (TextView) dialogo.findViewById(android.R.id.message);
        Button btnSI = (Button) dialogo.findViewById(android.R.id.button1);
        Button btnNO = (Button) dialogo.findViewById(android.R.id.button2);
        btnSI.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));
        btnNO.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));
        textodialogo.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));


    }


    public void buscadorFrente(View view) {

        Intent intent = new Intent(contexto, buscadorFrente.class);
        startActivity(intent);
        finish();

    }

    public void cambiarOrden(View view) {


        Intent intent = new Intent(contexto, buscadorOS.class);
        startActivity(intent);
        finish();

    }

    public void cambiarImplemento1(View view) {

        Intent intent = new Intent(this, buscadorImplemento.class);
        intent.putExtra("numero", 1);
        startActivity(intent);
        finish();

    }

    public void cambiarImplemento2(View view) {
        Intent intent = new Intent(this, buscadorImplemento.class);
        intent.putExtra("numero", 2);
        startActivity(intent);
        finish();
    }

    public void cambiarImplemento3(View view) {
        Intent intent = new Intent(this, buscadorImplemento.class);
        intent.putExtra("numero", 3);
        startActivity(intent);
        finish();
    }


    public void guardar(View view) {

        String mensajeFiltrosNoValidos = Metodos.VerificarFiltrosValidos(db);
        if( mensajeFiltrosNoValidos.length() > 0 ){

            AlertDialog dialogo =
            new AlertDialog.Builder(contexto)
                    .setTitle("Seguimiento de Labores Agricolas")
                    .setMessage("No se guardó la nueva actividad:\n" + mensajeFiltrosNoValidos)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setCancelable(false).show();
            TextView textodialogo = (TextView) dialogo.findViewById(android.R.id.message);
            Button btnSI = (Button) dialogo.findViewById(android.R.id.button1);
            Button btnNO = (Button) dialogo.findViewById(android.R.id.button2);
            btnSI.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));
            btnNO.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));
            textodialogo.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));
        }else{


            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            View dialogView = li.inflate(R.layout.layoutdialogo,null);

            Boolean mostrar_input_area_trabajada = false;

            Actividades_X_Equipo ultima_actividad_x_equipo = db.ObtenerUltimaActividad_X_Equipo();
            if(ultima_actividad_x_equipo!=null){
                String ultima_actividad_inactiva = ultima_actividad_x_equipo.getAXE_Actividad_Inactiva();
                if(ultima_actividad_inactiva.equals("")){
                    mostrar_input_area_trabajada = true;
                }
            }

            final EditText inputAreaTrabajada = (EditText) dialogView
                    .findViewById(R.id.inputAreaTrabajada);
            TextView tvAreaTrabajada = (TextView) dialogView.findViewById(R.id.tvAreaTrabajada);
            final CheckBox auto_guiado = (CheckBox) dialogView.findViewById(R.id.autoguiado);

            if(mostrar_input_area_trabajada){

                inputAreaTrabajada.setVisibility(View.VISIBLE);
                tvAreaTrabajada.setVisibility(View.VISIBLE);
            }

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
                    .setTitle("Seguimiento de Labores Agricolas")
                    .setMessage("Está seguro que desea guardar la Actividad?\n")
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                }
            }).setCancelable(false).setView(dialogView);


            alertDialogBuilder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Float valorAreaTrabajada = inputAreaTrabajada.getText().toString().equals("") ?
                            0 :
                            Float.parseFloat(inputAreaTrabajada.getText().toString());
                    Integer usa_auto_guiado = auto_guiado.isChecked() ? Constantes.TRUE : Constantes.FALSE;
                    Long registro = Metodos.Insertar_Actividad_X_Equipo(contexto,db,valorAreaTrabajada,usa_auto_guiado);

                    if (registro > 0) {
                        startActivity(new Intent(MainActivity.this,MainActivity.class));
                        finish();
                    }
                }
            });

            AlertDialog dialogo = alertDialogBuilder.create();
            dialogo.show();

            TextView textodialogo = (TextView) dialogo.findViewById(android.R.id.message);
            Button btnSI = (Button) dialogo.findViewById(android.R.id.button1);
            Button btnNO = (Button) dialogo.findViewById(android.R.id.button2);
            btnSI.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));
            btnNO.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos));
            float espacioParaNoSolaparTeclado = 5;
            textodialogo.setTextSize(contexto.getResources().getDimension(R.dimen.textSizeDialogos) - espacioParaNoSolaparTeclado);

        }

    }
}
