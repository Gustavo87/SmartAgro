package com.example.gbaca.seguimento_equipo_agricola.Actividades;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gbaca.seguimento_equipo_agricola.Actividades.Adaptadores.adaptador_RecyclerView_ActividadInactiva;
import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.Constantes;
import com.example.gbaca.seguimento_equipo_agricola.R;
import com.example.gbaca.seguimento_equipo_agricola.Servicios.WVAApplication;
import com.example.gbaca.seguimento_equipo_agricola.data.ActividadesInactivas;
import com.example.gbaca.seguimento_equipo_agricola.data.BaseDatos;
import com.example.gbaca.seguimento_equipo_agricola.data.Configuracion;
import com.example.gbaca.seguimento_equipo_agricola.data.Implementos;

import java.util.ArrayList;

public class buscadorActividadInactiva extends AppCompatActivity {

    private BaseDatos db;
    private RecyclerView mRecyclerView;
    public adaptador_RecyclerView_ActividadInactiva adaptadorRecyclerViewActividadInactiva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtenemos la instancia del Singleton
        WVAApplication app;
        app = (WVAApplication) getApplicationContext();

        // Modo pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_buscador_actividad_inactiva);

        // Configuramos el recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerviewActividadInactiva);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptadorRecyclerViewActividadInactiva = new adaptador_RecyclerView_ActividadInactiva(this,app);
        mRecyclerView.setAdapter(adaptadorRecyclerViewActividadInactiva);

        // Damos memoria a la refencia a la db
//        db = new BaseDatos(getApplicationContext());
        db = app.getdb();

        // Mostramos la opción quitar actividad inactiva
        Configuracion configuracion = db.ObtenerConfiguracion();
        String ActividadInactiva = configuracion.getActividadInactiva();
        String ActividadInactiva_N = db.ObtenerNombreActividadInactivaxCodigo(ActividadInactiva);
        if(!ActividadInactiva_N.equals("")){
            ImageView icono_actividad_inactiva = (ImageView) findViewById(R.id.icono_actividad_inactiva);
            icono_actividad_inactiva.setVisibility(View.VISIBLE);
            Button btnActividadInactiva = (Button) findViewById(R.id.btnActividadInactiva);
            btnActividadInactiva.setVisibility(View.VISIBLE);
            TextView seleccion_actual = (TextView) findViewById(R.id.seleccion_actual);
            seleccion_actual.setVisibility(View.VISIBLE);
            seleccion_actual.setText(ActividadInactiva + " " + ActividadInactiva_N);
        }

        // Ponemos el evento a buscadorOS
        EditText buscador = (EditText) findViewById(R.id.editTextSearch);
        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable busqueda) {
                TextView contadorBusqueda = (TextView) findViewById(R.id.contadorBusqueda);
                if(busqueda.length()==0){
                    adaptadorRecyclerViewActividadInactiva.setEmptyList();
                    contadorBusqueda.setText("");
                }else{
                    ArrayList<ActividadesInactivas> lista = db.BuscarActividadInactiva(busqueda.toString());
                    adaptadorRecyclerViewActividadInactiva.setFilter(lista);
                    contadorBusqueda.setText("Mostrando las "+ lista.size() +" activadades inactivas para activar");
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void BorrarSeleccion(View view) {


        Configuracion configuracion = new Configuracion();
        configuracion.setActividadInactiva("");
        configuracion.setActividadInactiva_N("Activo");
        configuracion.setCambios(Constantes.TRUE);
        db.actualizar_configuracion_ActividadInactiva(configuracion);
        db.actualizar_configuracion_Cambios(configuracion);

        ImageView icono_actividad_inactiva = (ImageView) findViewById(R.id.icono_actividad_inactiva);
        icono_actividad_inactiva.setVisibility(View.GONE);
        Button btnActividadInactiva = (Button) findViewById(R.id.btnActividadInactiva);
        btnActividadInactiva.setVisibility(View.GONE);
        TextView seleccion_actual = (TextView) findViewById(R.id.seleccion_actual);
        seleccion_actual.setVisibility(View.GONE);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }
}
