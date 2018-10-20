package com.example.gbaca.seguimento_equipo_agricola.Actividades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.Metodos;
import com.example.gbaca.seguimento_equipo_agricola.R;
import com.example.gbaca.seguimento_equipo_agricola.Servicios.WVAApplication;
import com.example.gbaca.seguimento_equipo_agricola.data.BaseDatos;

public class pantallaBienvenida extends AppCompatActivity {

    private WVAApplication app;
    private Context contexto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Modo pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pantalla_bienvenida);

        app = (WVAApplication) getApplicationContext();
        contexto=this;

        TextView tvDispositivo = (TextView) findViewById(R.id.tvDispositivo);
        tvDispositivo.setText("ID Dispositivo: " + Metodos.Obtener_UUID_dispositivo(contexto));

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
}
