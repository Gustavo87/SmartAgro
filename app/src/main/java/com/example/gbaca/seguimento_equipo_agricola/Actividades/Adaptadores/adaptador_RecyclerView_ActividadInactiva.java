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
import com.example.gbaca.seguimento_equipo_agricola.data.ActividadesInactivas;
import com.example.gbaca.seguimento_equipo_agricola.data.BaseDatos;
import com.example.gbaca.seguimento_equipo_agricola.data.Configuracion;
import com.example.gbaca.seguimento_equipo_agricola.data.Implementos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GBaca on 7/10/2018.
 */

public class adaptador_RecyclerView_ActividadInactiva extends RecyclerView.Adapter<adaptador_RecyclerView_ActividadInactiva.MyViewHolder> {

    private List<ActividadesInactivas> list_item ;
    public Context mcontext;
    public WVAApplication app;

    public adaptador_RecyclerView_ActividadInactiva(Context context,WVAApplication contexto_app) {
        list_item = new ArrayList<>();
        mcontext = context;
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
    public adaptador_RecyclerView_ActividadInactiva.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        adaptador_RecyclerView_ActividadInactiva.MyViewHolder myViewHolder = new adaptador_RecyclerView_ActividadInactiva.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(adaptador_RecyclerView_ActividadInactiva.MyViewHolder viewHolder, final int position) {
        viewHolder.view_descripcion_textview.setText(list_item.get(position).getActividad_N());
        viewHolder.view_codigo_textview.setText(list_item.get(position).getActividad());

        viewHolder.btnActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ActividadInactiva = list_item.get(position).getActividad();
                String ActividadInactiva_N = list_item.get(position).getActividad_N();

//                BaseDatos db = new BaseDatos(mcontext);
                BaseDatos db = app.getdb();

                Configuracion configuracion = new Configuracion();
                configuracion.setActividadInactiva(ActividadInactiva);
                configuracion.setActividadInactiva_N(ActividadInactiva_N);
                configuracion.setCambios(Constantes.TRUE);
                db.actualizar_configuracion_ActividadInactiva(configuracion);
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

    public void setFilter(ArrayList<ActividadesInactivas> newList){
        list_item=new ArrayList<>();
        list_item.addAll(newList);
        notifyDataSetChanged();
    }

    public void setEmptyList(){
        ArrayList<ActividadesInactivas> emptyList = new ArrayList<>();
        setFilter(emptyList);
    }
}
