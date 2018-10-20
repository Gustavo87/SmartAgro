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
import android.widget.EditText;
import android.widget.TextView;

import com.example.gbaca.seguimento_equipo_agricola.Actividades.Adaptadores.adaptador_RecyclerView_Frentes;
import com.example.gbaca.seguimento_equipo_agricola.R;
import com.example.gbaca.seguimento_equipo_agricola.Servicios.WVAApplication;
import com.example.gbaca.seguimento_equipo_agricola.data.BaseDatos;
import com.example.gbaca.seguimento_equipo_agricola.data.Frentes;

import java.util.ArrayList;

public class buscadorFrente extends AppCompatActivity {

    private BaseDatos db;
    private RecyclerView mRecyclerView;
    public adaptador_RecyclerView_Frentes adaptadorRecyclerViewFrentes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtenemos la instancia del Singleton
        WVAApplication app;
        app = (WVAApplication) getApplicationContext();

        // Modo pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_buscador_frente);
        // Configuramos el recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerviewFrentes);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptadorRecyclerViewFrentes = new adaptador_RecyclerView_Frentes(this,app);
        mRecyclerView.setAdapter(adaptadorRecyclerViewFrentes);
        // Damos memoria a la refencia a la db
//        db = new BaseDatos(getApplicationContext());
        db = app.getdb();

        // Ponemos el evento a buscadorFrente
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
                    adaptadorRecyclerViewFrentes.setEmptyList();
                    contadorBusqueda.setText("");
                }else{
                    ArrayList<Frentes> lista = db.BuscarFrente(busqueda.toString());
                    adaptadorRecyclerViewFrentes.setFilter(lista);
                    contadorBusqueda.setText("Mostrando los "+ lista.size() +" frentes para activar");
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

}
