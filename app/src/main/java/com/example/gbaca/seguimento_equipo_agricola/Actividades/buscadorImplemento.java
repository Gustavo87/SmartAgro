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

import com.example.gbaca.seguimento_equipo_agricola.Actividades.Adaptadores.adaptador_RecyclerView_Implementos;
import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.Constantes;
import com.example.gbaca.seguimento_equipo_agricola.R;
import com.example.gbaca.seguimento_equipo_agricola.Servicios.WVAApplication;
import com.example.gbaca.seguimento_equipo_agricola.data.BaseDatos;
import com.example.gbaca.seguimento_equipo_agricola.data.Configuracion;
import com.example.gbaca.seguimento_equipo_agricola.data.Implementos;

import java.util.ArrayList;

public class buscadorImplemento extends AppCompatActivity {

    private BaseDatos db;
    private int numero;
    private RecyclerView mRecyclerView;
    public adaptador_RecyclerView_Implementos adaptadorRecyclerViewImplementos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Modo pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_buscador_implemento);

        //Obtenemos la instancia del Singleton
        WVAApplication app;
        app = (WVAApplication) getApplicationContext();

        // Recibimos el numero de implemento
        Bundle datos = this.getIntent().getExtras();
        numero = datos.getInt("numero");

        // Configuramos el recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerviewImplementos);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptadorRecyclerViewImplementos = new adaptador_RecyclerView_Implementos(this,numero,app);
        mRecyclerView.setAdapter(adaptadorRecyclerViewImplementos);
        // Damos memoria a la refencia a la db
        //db = new BaseDatos(getApplicationContext());
        db = app.getdb();

        // Mostramos la opción implemento
        Configuracion configuracion = db.ObtenerConfiguracion();
        String Implemento = "";
        switch (numero){
            case 1:
                Implemento = configuracion.getImplemento1();
                break;
            case 2:
                Implemento = configuracion.getImplemento2();
                break;
            case 3:
                Implemento = configuracion.getImplemento3();
                break;
        }
        if(!Implemento.equals("")){
            String Implemento_N = db.ObtenerNombreImplementoxCodigo(Implemento);
            Button btnRemoverImplemento = (Button) findViewById(R.id.btnRemoverImplemento);
            btnRemoverImplemento.setVisibility(View.VISIBLE);
            TextView seleccion_actual = (TextView) findViewById(R.id.seleccion_actual);
            seleccion_actual.setVisibility(View.VISIBLE);
            seleccion_actual.setText(Implemento + " " + Implemento_N);
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
                    adaptadorRecyclerViewImplementos.setEmptyList();
                    contadorBusqueda.setText("");
                }else{
                    ArrayList<Implementos> lista = db.BuscarImplemento(busqueda.toString());
                    adaptadorRecyclerViewImplementos.setFilter(lista);
                    contadorBusqueda.setText("Mostrando las "+ lista.size() +" implementos para activar");
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

        switch (numero){
            case 1:
                configuracion.setImplemento1("");
                db.actualizar_configuracion_Implemento1(configuracion);
                break;
            case 2:
                configuracion.setImplemento2("");
                db.actualizar_configuracion_Implemento2(configuracion);
                break;
            case 3:
                configuracion.setImplemento3("");
                db.actualizar_configuracion_Implemento3(configuracion);
                break;
        }

        configuracion.setCambios(Constantes.TRUE);
        db.actualizar_configuracion_Cambios(configuracion);

        Button btnRemoverImplemento = (Button) findViewById(R.id.btnRemoverImplemento);
        btnRemoverImplemento.setVisibility(View.GONE);
        TextView seleccion_actual = (TextView) findViewById(R.id.seleccion_actual);
        seleccion_actual.setVisibility(View.GONE);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
