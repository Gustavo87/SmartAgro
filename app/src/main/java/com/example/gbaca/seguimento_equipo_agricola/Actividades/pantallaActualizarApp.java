package com.example.gbaca.seguimento_equipo_agricola.Actividades;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.gbaca.seguimento_equipo_agricola.R;
import com.example.gbaca.seguimento_equipo_agricola.Servicios.WVAApplication;
import com.example.gbaca.seguimento_equipo_agricola.data.BaseDatos;
import com.example.gbaca.seguimento_equipo_agricola.data.Configuracion;

import java.io.File;

public class pantallaActualizarApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_actualizar_app);
    }

    public void actualizar_app(View view) {

        final WVAApplication app = (WVAApplication) getApplicationContext();
        final BaseDatos db = app.getdb();
        final Configuracion configuracion = db.ObtenerConfiguracion();

        String nombre_apk_file_a_descargar = getApplicationContext().getString(R.string.app_name)
                + "(version " + configuracion.getUltima_Version_App() + ").apk";
        String url_apk_file = configuracion.getURL_Descarga_APK();
        String titulo_de_descarga = nombre_apk_file_a_descargar;
        String descripcion_descarga = getApplicationContext().getString(R.string.bajando_apk_msg,
                nombre_apk_file_a_descargar);

        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";

        // Elimina todos los archivos en la carpeta downloads
        DeleteRecursive(new File(destination));

        destination += nombre_apk_file_a_descargar;
        final Uri uri_file = Uri.parse("file://" + destination);
        String url = url_apk_file;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(descripcion_descarga);
        request.setTitle(titulo_de_descarga);
        request.setDestinationUri(uri_file);

        final DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(getApplicationContext().DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {

                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setDataAndType(uri_file,
                        "application/vnd.android.package-archive");
                getApplicationContext().startActivity(install);

            }
        };

       getApplicationContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    void DeleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();

    }
}
