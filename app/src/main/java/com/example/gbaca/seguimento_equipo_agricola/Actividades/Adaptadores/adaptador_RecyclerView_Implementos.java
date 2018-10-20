package com.example.gbaca.seguimento_equipo_agricola.Actividades.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gbaca.seguimento_equipo_agricola.Actividades.MainActivity;
import com.example.gbaca.seguimento_equipo_agricola.Procedimientos_Globales.Constantes;
import com.example.gbaca.seguimento_equipo_agricola.R;
import com.example.gbaca.seguimento_equipo_agricola.Servicios.WVAApplication;
import com.example.gbaca.seguimento_equipo_agricola.data.BaseDatos;
import com.example.gbaca.seguimento_equipo_agricola.data.Configuracion;
import com.example.gbaca.seguimento_equipo_agricola.data.Implementos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GBaca on 6/26/2018.
 */

public class adaptador_RecyclerView_Implementos extends RecyclerView.Adapter<adaptador_RecyclerView_Implementos.MyViewHolder>{

    private int numeroImplemento;
    private List<Implementos> list_item ;
    public Context mcontext;
    public WVAApplication app;

    public adaptador_RecyclerView_Implementos(Context context, int numero, WVAApplication contexto_app) {

        list_item = new ArrayList<>();
        mcontext = context;
        numeroImplemento = numero;
        app = contexto_app;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView view_descripcion_textview;
        public TextView view_codigo_textview;
        public ConstraintLayout view_contenedor;
        public Button btnActivar;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            view_contenedor = (ConstraintLayout) itemLayoutView.findViewById(R.id.contenedor);
            view_descripcion_textview = (TextView) itemLayoutView.findViewById(R.id.view_descripcion_textview);
            view_codigo_textview = (TextView) itemLayoutView.findViewById(R.id.view_codigo_textview);
            btnActivar = (Button) itemLayoutView.findViewById(R.id.btnActivar);
        }
    }

    @Override
    public adaptador_RecyclerView_Implementos.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(adaptador_RecyclerView_Implementos.MyViewHolder viewHolder, final int position) {
        viewHolder.view_descripcion_textview.setText(list_item.get(position).getCodigo());
        viewHolder.view_codigo_textview.setText(list_item.get(position).getDescripcion());

        viewHolder.btnActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Implemento = list_item.get(position).getCodigo();

//                BaseDatos db = new BaseDatos(mcontext);
                BaseDatos db = app.getdb();

                Configuracion configuracion = new Configuracion();
                if(numeroImplemento == 1){

                    configuracion.setImplemento1(Implemento);
                    db.actualizar_configuracion_Implemento1(configuracion);

                }
                if(numeroImplemento == 2){
                    configuracion.setImplemento2(Implemento);
                    db.actualizar_configuracion_Implemento2(configuracion);
                }
                if(numeroImplemento == 3){
                    configuracion.setImplemento3(Implemento);
                    db.actualizar_configuracion_Implemento3(configuracion);
                }

                // Para determinar si mostramos el boton de aplicar o no.
                configuracion.setCambios(Constantes.TRUE);
                db.actualizar_configuracion_Cambios(configuracion);


                Intent intent = new Intent(mcontext, MainActivity.class);
                mcontext.startActivity(intent);
                ((Activity)mcontext).finish();


            }
        });

    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public void setFilter(ArrayList<Implementos> newList){
        list_item=new ArrayList<>();
        list_item.addAll(newList);
        notifyDataSetChanged();
    }

    public void setEmptyList(){
        ArrayList<Implementos> emptyList = new ArrayList<>();
        setFilter(emptyList);
    }

}
