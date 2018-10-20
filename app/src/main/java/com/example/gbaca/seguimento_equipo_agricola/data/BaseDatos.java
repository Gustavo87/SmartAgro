package com.example.gbaca.seguimento_equipo_agricola.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;


import com.example.gbaca.seguimento_equipo_agricola.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BaseDatos extends SQLiteOpenHelper{

    Context contexto;
    static final String name = "datos.db";
    static final int version = 1;

    public BaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BaseDatos(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        if(db.isWriteAheadLoggingEnabled() == false ){
            // Establece el modo WAL : SQLITE> PRAGMA journal_mode = WAL
            Log.i("Configuracion","Configurado Modo WAL");
            db.enableWriteAheadLogging();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creamos tablas maestras
        db.execSQL("CREATE TABLE IF NOT EXISTS Frentes(" +
                "id INTEGER PRIMARY KEY," +
                "Codigo TEXT," +
                "Descripcion TEXT" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS OS(" +
                "id INTEGER PRIMARY KEY," +
                "Codigo_OS TEXT," +
                "Plantio TEXT," +
                "Plantio_N  TEXT," +
                "Actividad TEXT," +
                "Actividad_N TEXT," +
                "Tipo_Actividad TEXT," +
                "Inicio_Actividad TEXT," +
                "Fin_Actividad TEXT" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS ActividadesInactivas(" +
                "id INTEGER PRIMARY KEY," +
                "Actividad TEXT," +
                "Actividad_N TEXT," +
                "Tipo_Actividad  TEXT" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS Implementos(" +
                "id INTEGER PRIMARY KEY," +
                "Codigo TEXT," +
                "Descripcion TEXT" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS Empleados(" +
                "id INTEGER PRIMARY KEY," +
                "Codigo TEXT," +
                "Empleado TEXT" +
                ")");
        // Creamos tablas para recolectar datos desde la app
        db.execSQL("CREATE TABLE IF NOT EXISTS ACTIVIDADES_X_EQUIPO(" +
                "id INTEGER PRIMARY KEY," +
                "AXE_ID INTEGER," +
                "Orden_Servicio TEXT," +
                "AXE_Actividad_Inactiva TEXT," +
                "DXA_Objeto TEXT," +
                "Implemento_1 TEXT," +
                "Implemento_2 TEXT," +
                "Implemento_3 TEXT," +
                "Frente TEXT," +
                "Plantio TEXT," +
                "Equipo TEXT," +
                "Operador TEXT," +
                "Actividad TEXT," +
                "Fecha_Inicio TEXT," +
                "Fecha_Fin TEXT," +
                "Sincronizado INTEGER," +
                "Fecha_Modificacion TEXT," +
                "Usuario TEXT," +
                "Version_APP REAL," +
                "Dispositivo TEXT," +
                "Area_Trabajada REAL," +
                "AutoGuiado INTEGER," +
                "AXE_Timestamp TEXT" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS DETALLE_X_ACTIVIDAD(" +
                "id INTEGER PRIMARY KEY," +
                "DXA_ID INTEGER," +
                "Id_Actividad INTEGER REFERENCES ACTIVIDADES_X_EQUIPO(id) ON DELETE CASCADE ON UPDATE CASCADE," +
                "Latitud TEXT," +
                "Longitud TEXT," +
                "Velocidad REAL," +
                "RPM REAL," +
                "Distancia REAL," +
                "Distancia_GPS REAL," +
                "Horas_Motor REAL," +
                "Nivel_Combustible REAL," +
                "Temperatura_Motor REAL," +
                "Tiempo_Activo REAL," +
                "Tiempo_Ralenti REAL," +
                "Tiempo_Apagado REAL," +
                "Fecha_Modificacion TEXT," +
                "DXA_Objeto TEXT," +
                "DXA_Cantidad_Eventos INTEGER," +
                "Temperatura_Motor_timestamp TEXT," +
                "Nivel_Combustible_timestamp TEXT," +
                "Horas_Motor_timestamp TEXT," +
                "Tiempo_Disponibilidad REAL," +
                "Consumo_Combustible REAL," +
                "Porcentaje_Bateria REAL," +
                "Tipo_Senal TEXT," +
                "Intensidad_Senal REAL" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS EVENTOS_DETALLE_ACTIVIDAD(" +
                "id INTEGER PRIMARY KEY," +
                "Id_Detalle_Actividad INTEGER REFERENCES DETALLE_X_ACTIVIDAD(id) ON DELETE CASCADE ON UPDATE CASCADE," +
                "Velocidad REAL," +
                "RPM REAL," +
                "Distancia REAL," +
                "Fecha_Modificacion TEXT," +
                "Velocidad_timestamp TEXT," +
                "RPM_timestamp TEXT" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS Configuracion(" +
                "id INTEGER PRIMARY KEY," +
                "Actividad TEXT," +
                "Actividad_N TEXT," +
                "ActividadInactiva TEXT," +
                "ActividadInactiva_N TEXT," +
                "Plantio TEXT," +
                "Plantio_N TEXT," +
                "OS TEXT," +
                "Frente TEXT," +
                "Frente_N TEXT," +
                "Implemento1 TEXT," +
                "Implemento2 TEXT," +
                "Implemento3 TEXT," +
                "Empleado TEXT," +
                "Empleado_N TEXT," +
                "Cambios INTEGER," +
                "Distancia REAL," +
                "Latitud REAL," +
                "Longitud REAL," +
                "Flag_Frente INTEGER," +
                "Flag_OS INTEGER," +
                "Flag_Actividad_Inactiva INTEGER," +
                "Flag_Implemento INTEGER," +
                "Flag_Empleado INTEGER," +
                "Equipo TEXT," +
                "DIGI_Conectado INTEGER," +
                "Ultima_Version_App REAL," +
                "URL_Descarga_APK TEXT," +
                "Tipo_Senal TEXT," +
                "Intensidad_Senal REAL" +
                ")");



        // Para insertar el registro de Configuración en caso de que este no se encuentre...
        db.execSQL("INSERT INTO Configuracion" +
                " ( Actividad," +
                "   Actividad_N," +
                "   ActividadInactiva," +
                "   ActividadInactiva_N," +
                "   Plantio," +
                "   Plantio_N," +
                "   OS," +
                "   Frente," +
                "   Frente_N," +
                "   Implemento1," +
                "   Implemento2," +
                "   Implemento3," +
                "   Empleado," +
                "   Empleado_N," +
                "   Cambios," +
                "   Distancia," +
                "   Latitud," +
                "   Longitud," +
                "   Flag_Frente," +
                "   Flag_OS," +
                "   Flag_Actividad_Inactiva," +
                "   Flag_Implemento," +
                "   Flag_Empleado," +
                "   Equipo," +
                "   DIGI_Conectado," +
                "   Ultima_Version_App," +
                "   URL_Descarga_APK," +
                "   Tipo_Senal," +
                "   Intensidad_Senal" +
                ") " +
                "       SELECT " +
                "           \"\"        AS Actividad," +
                "            \"\"       AS Actividad_N," +
                "            \"\"       AS ActividadInactiva," +
                "            \"Activo\" AS ActividadInactiva_N," +
                "             0         AS Plantio," +
                "           \"\"        AS Plantio_N," +
                "             0         AS OS, " +
                "           \"\"        AS Frente," +
                "           \"\"        AS Frente_N," +
                "           \"\"        AS Implemento1," +
                "           \"\"        AS Implemento2," +
                "           \"\"        AS Implemento3," +
                "           \"\"        AS Empleado," +
                "           \"\"        AS Empleado_N," +
                "             0         AS Cambios, " +
                "             0.0       AS Distancia," +
                "             null      AS Latitud," +
                "             null      AS Longitud," +
                "              0," +
                "              0," +
                "              0," +
                "              0," +
                "              0," +
                "           \"\"        AS Equipo," +
                "             0         AS DIGI_Conectado," +
                "            0.0        AS Ultima_Version_App," +
                "           \"\"        AS URL_Descarga_APK," +
                "           \"\"        AS Tipo_Senal," +
                "           0.0         AS Intensidad_Senal" +
                "       FROM " +
                "           android_metadata " +
                "       WHERE (SELECT count(*) from Configuracion) = 0 " +
                "       LIMIT 1;");

        // Creación de indices
        db.execSQL("CREATE INDEX INDICE_DXA ON DETALLE_X_ACTIVIDAD(DXA_ID);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    // Metodo de borrado
    public void borrar_tabla(String tabla){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(tabla,null,null);

        }catch (SQLiteException e){

            Toast.makeText(contexto,contexto.getString(R.string.borrando_maestro_error),Toast.LENGTH_SHORT).show();
        }
    }

    // Consultas...
    public ArrayList BuscarOS(String busqueda){

        ArrayList<OS> OSs = new ArrayList<>();
        OS os;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery(" SELECT Codigo_OS," +
                                   "        Plantio," +
                                   "        Actividad," +
                                   "        Actividad_N," +
                                   "        Plantio_N " +
                                   "   FROM OS " +
                                   "   WHERE  " +
                                   "        Codigo_OS LIKE '%"+ busqueda +"%' OR " +
                                   "        Plantio like '%"+busqueda+"%'     OR " +
                                   "        Actividad like '%"+busqueda+"%'   OR " +
                                   "        Actividad_N like '%"+busqueda+"%' OR " +
                                   "        Plantio_N like '%"+busqueda+"%'      ",null);
            if(c.moveToFirst()){
                do{
                    os = new OS();
                    os.setCodigo_OS(c.getString(0));
                    os.setPlantio(c.getString(1));
                    os.setActividad(c.getString(2));
                    os.setActividad_N(c.getString(3));
                    os.setPlantio_N(c.getString(4));
                    OSs.add(os);
                }while(c.moveToNext());
            }
            c.close();
        }
        return OSs;
    }

    public boolean existeActividadesxEquipo(String Equipo) {

        Boolean hayDatos = false;

        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM ACTIVIDADES_X_EQUIPO WHERE Equipo = '"+ Equipo +"'",null);
            if(c.moveToFirst()){
                do{
                    if(c.getInt(0) > 0){
                        hayDatos = true;
                    }
                    else{
                        hayDatos = false;
                    }
                }while(c.moveToNext());
            }
            c.close();
        }
        return hayDatos;

    }

    public Boolean MaestroTieneDatos(String maestro){

        Boolean hayDatos = false;

        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + maestro,null);
            if(c.moveToFirst()){
                do{
                    if(c.getInt(0) > 0){
                        hayDatos = true;
                    }
                    else{
                        hayDatos = false;
                    }
                }while(c.moveToNext());
            }
            c.close();
        }
        return hayDatos;
    }

    public ArrayList BuscarImplemento(String busqueda){

        ArrayList<Implementos> Implementos = new ArrayList<>();
        Implementos Implemento;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){

            Configuracion configuracion = ObtenerConfiguracion();

            String sql = " SELECT Codigo," +
                         "        Descripcion" +
                         " FROM Implementos " +
                         " WHERE " +
                         " ( " +
                         "      Codigo LIKE '%"+ busqueda +"%'" +
                         "      OR Descripcion like '%"+busqueda+"%' " +
                         " )  " +
                         " AND Codigo NOT IN ('" + configuracion.getImplemento1() + "'," +
                                             "'" + configuracion.getImplemento2() + "'," +
                                             "'" + configuracion.getImplemento3() + "'" +
                                             ")";
            Cursor c = db.rawQuery(sql,null);
            if(c.moveToFirst()){
                do{
                    Implemento = new Implementos();
                    Implemento.setCodigo(c.getString(0));
                    Implemento.setDescripcion(c.getString(1));
                    Implementos.add(Implemento);
                }while(c.moveToNext());
            }
            c.close();
        }
        return Implementos;
    }

    public ArrayList BuscarActividadInactiva(String busqueda){

        ArrayList<ActividadesInactivas> ActividadesInactivas = new ArrayList<>();
        ActividadesInactivas actividadesInactivas;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery(" SELECT Actividad,Actividad_N FROM ActividadesInactivas WHERE Actividad LIKE '%"+ busqueda +"%' OR Actividad_N like '%"+busqueda+"%'",null);
            if(c.moveToFirst()){
                do{
                    actividadesInactivas = new ActividadesInactivas();
                    actividadesInactivas.setActividad(c.getString(0));
                    actividadesInactivas.setActividad_N(c.getString(1));
                    ActividadesInactivas.add(actividadesInactivas);
                }while(c.moveToNext());
            }
            c.close();
        }
        return ActividadesInactivas;
    }


    public Detalle_X_Actividad OBTENER_VELOCIDAD_RPM_DISTANCIA(Long id){

        Detalle_X_Actividad detalle_x_actividad = new Detalle_X_Actividad();
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT AVG(velocidad) AS velocidad," +
                                   " AVG(rpm) AS rpm," +
                                   " SUM(distancia) AS distancia" +
                                   " FROM EVENTOS_DETALLE_ACTIVIDAD " +
                                   " WHERE " +
                                   " velocidad IS NOT NULL AND " +
                                   " velocidad >= 0        AND " +
                                   " rpm IS NOT NULL       AND " +
                                   "Id_Detalle_Actividad=" + id,null);
            if(c.moveToFirst()){
                do{
                    detalle_x_actividad.setVelocidad(c.getDouble(0));
                    detalle_x_actividad.setRPM(c.getDouble(1));
                    detalle_x_actividad.setDistancia(c.getDouble(2));
                    detalle_x_actividad.setDistancia_GPS(ObtenerConfiguracion().getDistancia());
                }while(c.moveToNext());
            }
            c.close();
        }

        Configuracion configuracion = new Configuracion();
        configuracion.setDistancia(0.0);
        actualizar_configuracion_Distancia(configuracion);

        return detalle_x_actividad;
    }


    public Integer OBTENER_CANTIDAD_EVENTOS(Long id){

        Integer Cantidad_Eventos = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery(" SELECT COUNT(*) AS cantidad_eventos " +
                                   "   FROM EVENTOS_DETALLE_ACTIVIDAD    " +
                                   "   WHERE " +
                                   "   Id_Detalle_Actividad=" + id,null);
            if(c.moveToFirst()){
                do{
                    Cantidad_Eventos = c.getInt(0);
                }while(c.moveToNext());
            }
            c.close();
        }

        return Cantidad_Eventos;
    }



    public Double OBTENER_TIEMPO_ACTIVO(Long id){

        Double tiempo = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM EVENTOS_DETALLE_ACTIVIDAD WHERE Id_Detalle_Actividad=" + id + " AND velocidad>0 AND rpm>0",null);
            if(c.moveToFirst()){
                do{
                    tiempo = c.getDouble(0);
                }while(c.moveToNext());
            }
            c.close();
        }
        return tiempo;
    }


    public Integer OBTENER_ENVIOS_PENDIENTES(){

        Integer cantidad = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM DETALLE_X_ACTIVIDAD WHERE DXA_ID = 0 AND DXA_Cantidad_Eventos <> 0",null);
            if(c.moveToFirst()){
                do{
                    cantidad = c.getInt(0);
                }while(c.moveToNext());
            }
            c.close();
        }
        return cantidad;
    }

    public String OBTENER_FECHA_ULTIMO_ENVIO(){

        String fecha = "-";
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT MAX(Fecha_Modificacion) \n" +
                    "                               FROM DETALLE_X_ACTIVIDAD WHERE \n" +
                    "                               DXA_ID != 0;",null);
            if(c.moveToFirst()){
                do{
                    fecha = c.getString(0);
                }while(c.moveToNext());
            }
            c.close();
        }
        return fecha;
    }

    public Detalle_X_Actividad OBTENER_CONSOLIDADO_EVENTOS(Long ID_Actividad){

        Detalle_X_Actividad detalle_x_actividad = null;

        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery(" SELECT  \n" +
                                    "\t Id_Detalle_Actividad,\n" +
                                    "\t AVG(CASE WHEN velocidad IS NOT NULL THEN velocidad END) AS velocidad,\n" +
                                    "\t AVG(CASE WHEN rpm IS NOT NULL THEN rpm END) AS rpm,\n" +
                                    "\t SUM(CASE WHEN distancia IS NOT NULL THEN distancia END) AS distancia,\n" +
                                    "\t SUM(CASE WHEN velocidad>0 AND rpm>0 THEN 1 END) AS tiempo_activo,\n" +
                                    "\t SUM(CASE WHEN velocidad=0 and rpm>0 THEN 1 END) AS tiempo_ralenti,\n" +
                                    "\t SUM(CASE WHEN velocidad=0 and rpm=0 THEN 1 END) AS tiempo_apagado,\n" +
                                    "\t COUNT(*) AS cantidad_eventos  \n" +
                                    " FROM \n" +
                                    "\tEVENTOS_DETALLE_ACTIVIDAD     \n" +
                                    " WHERE \n" +
                                    "\tId_Detalle_Actividad=" + ID_Actividad,null);
            if(c.moveToFirst()){
                do{
                    detalle_x_actividad = new Detalle_X_Actividad();
                    detalle_x_actividad.setId(c.getLong(0));
                    detalle_x_actividad.setVelocidad(c.getDouble(1));
                    detalle_x_actividad.setRPM(c.getDouble(2));
                    detalle_x_actividad.setDistancia(c.getDouble(3));
                    detalle_x_actividad.setTiempo_Activo(c.getDouble(4));
                    detalle_x_actividad.setTiempo_Ralenti(c.getDouble(5));
                    detalle_x_actividad.setTiempo_Apagado(c.getDouble(6));
                    detalle_x_actividad.setDXA_Cantidad_Eventos(c.getInt(7));
                }while(c.moveToNext());
            }
            c.close();
        }
        return detalle_x_actividad;
    }


    public Double OBTENER_TIEMPO_RALENTI(Long id){

        Double tiempo = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT count(*) FROM EVENTOS_DETALLE_ACTIVIDAD WHERE Id_Detalle_Actividad=" + id + " and velocidad=0 and rpm>0",null);
            if(c.moveToFirst()){
                do{
                    tiempo = c.getDouble(0);
                }while(c.moveToNext());
            }
            c.close();
        }
        return tiempo;
    }

    public Double OBTENER_TIEMPO_APAGADO(Long id){

        Double tiempo = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT count(*) FROM EVENTOS_DETALLE_ACTIVIDAD WHERE Id_Detalle_Actividad=" + id + " and velocidad=0 and rpm=0",null);
            if(c.moveToFirst()){
                do{
                    tiempo = c.getDouble(0);
                }while(c.moveToNext());
            }
            c.close();
        }
        return tiempo;
    }

    // Consultas...
    public Long ObtenerUltimoID(String tabla){

        Long id = 0L;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT id FROM "+ tabla +" ORDER BY id DESC LIMIT 1",null);
            if(c.moveToFirst()){
                do{
                    id = c.getLong(0);
                }while(c.moveToNext());
            }
            c.close();
        }
        return id;
    }

    public ArrayList BuscarFrente(String busqueda){

        ArrayList<Frentes> Frentes = new ArrayList<>();
        Frentes Frente;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery(" SELECT Codigo,Descripcion" +
                                   " FROM Frentes " +
                                   " WHERE Codigo LIKE '%"+ busqueda +"%'" +
                                   " OR Descripcion LIKE '%"+ busqueda +"%'",null);
            if(c.moveToFirst()){
                do{
                    Frente = new Frentes();
                    Frente.setCodigo(c.getString(0));
                    Frente.setDescripcion(c.getString(1));
                    Frentes.add(Frente);
                }while(c.moveToNext());
            }
            c.close();
        }
        return Frentes;
    }


    public String ObtenerFechaInicioActividadVigente(){

        String fecha_inicio = null;

        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT AXE_Timestamp FROM ACTIVIDADES_X_EQUIPO ORDER BY Fecha_Inicio DESC LIMIT 1;",null);
            if(c.moveToFirst()){
                do{
                    fecha_inicio = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(c.getString(0)));
                }while(c.moveToNext());
            }
            c.close();
        }
        return fecha_inicio;
    }


    public ArrayList ObtenerCabecera(){

        ArrayList<com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado.Actividades_X_Equipo> actividades_x_equipos = new ArrayList<>();
        com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado.Actividades_X_Equipo  actividades_x_equipo;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){

            try{
                String query = "SELECT DISTINCT " +
                        "       Frente AS AXE_Frente,\n" +
                        "       Plantio AS AXE_Plantio,\n" +
                        "       '01' AS AXE_Cuadro,\n" +
                        "       Equipo AS AXE_Equipo,\n" +
                        "       Operador AS AXE_Operador,\n" +
                        "       Actividad AS AXE_Actividad,\n" +
                        "       Fecha_Inicio AS AXE_Fecha_Inicio,\n" +
                        "       Fecha_Fin AS AXE_Fecha_Fin,\n" +
                        "       Implemento_1 AS AXE_Implemento1,\n" +
                        "       Implemento_2 AS AXE_Implemento2,\n" +
                        "       Implemento_3 AS AXE_Implemento3,\n" +
                        "       \"\" AS AXE_Implemento4,\n" +
                        "       \"\" AS AXE_Implemento5,\n" +
                        "       AXE.id AS AXE_Dispositivo_Registro_ID,\n" +
                        "       Fecha_Inicio AS AXE_Dispositivo_Fecha_Transaccion,\n" +
                        "       AXE_ID AS AXE_ID," +
                        "       Orden_Servicio AS OS," +
                        "       AXE_Actividad_Inactiva," +
                        "       Area_Trabajada," +
                        "       AutoGuiado" +
                        " FROM ACTIVIDADES_X_EQUIPO AS AXE " +
                        " JOIN DETALLE_X_ACTIVIDAD AS DXA ON AXE.id = DXA.Id_Actividad " +
                        " WHERE DXA.DXA_ID = 0  AND DXA_Cantidad_Eventos <> 0 \n";


                Cursor c = db.rawQuery(query,null);
                if(c.moveToFirst()){
                    do{
                        actividades_x_equipo = new com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado.Actividades_X_Equipo();

                        actividades_x_equipo.setAXE_Frente(c.getString(0));
                        actividades_x_equipo.setAXE_Plantio(c.getString(1));
                        actividades_x_equipo.setAXE_Cuadro(c.getString(2));
                        actividades_x_equipo.setAXE_Equipo(c.getString(3));
                        actividades_x_equipo.setAXE_Operador(c.getString(4));
                        actividades_x_equipo.setAXE_Actividad(c.getString(5));
                        actividades_x_equipo.setAXE_Fecha_Inicio(c.getString(6));
                        actividades_x_equipo.setAXE_Fecha_Fin(c.getString(7));
                        actividades_x_equipo.setAXE_Implemento1(c.getString(8));
                        actividades_x_equipo.setAXE_Implemento2(c.getString(9));
                        actividades_x_equipo.setAXE_Implemento3(c.getString(10));
                        actividades_x_equipo.setAXE_Implemento4(c.getString(11));
                        actividades_x_equipo.setAXE_Implemento5(c.getString(12));
                        actividades_x_equipo.setAXE_Dispositivo_Registro_ID(c.getLong(13));
                        actividades_x_equipo.setAXE_Dispositivo_Fecha_Transaccion(c.getString(14));
                        actividades_x_equipo.setAXE_ID(c.getLong(15));
                        actividades_x_equipo.setOS(c.getString(16));
                        actividades_x_equipo.setAXE_Actividad_Inactiva(c.getString(17));
                        actividades_x_equipo.setAXE_Area(c.getFloat(18));
                        actividades_x_equipo.setAXE_AutoGuiado(c.getInt(19));
                        actividades_x_equipos.add(actividades_x_equipo);
                    }while(c.moveToNext());
                }
                c.close();
            }catch (Exception e){

            }

        }

        return actividades_x_equipos;
    }


    public ArrayList ObtenerDetalle(Long id){

        ArrayList<com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado.Detalle_X_Actividad> Detalle_X_Actividad = new ArrayList<>();
        com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado.Detalle_X_Actividad objeto;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){

            String query = "SELECT Latitud AS DXA_Latitud,\n" +
                            "       Longitud AS DXA_Longitud,\n" +
                            "       Velocidad AS DXA_Velocidad,\n" +
                            "       RPM AS DXA_RPM,\n" +
                            "       Distancia AS DXA_Distancia,\n" +
                            "       Horas_Motor AS DXA_HorasMotor,\n" +
                            "       Nivel_Combustible AS DXA_NivelCombustible,\n" +
                            "       Temperatura_Motor AS DXA_TemperaturaMotor,\n" +
                            "       Tiempo_Activo AS DXA_TiempoActivo,\n" +
                            "       Tiempo_Ralenti AS DXA_TiempoRalenti,\n" +
                            "       Tiempo_Apagado AS DXA_TiempoApagado,\n" +
                            "       Fecha_Modificacion AS DXA_Fecha_Modificacion,\n" +
                            "       id AS DXA_Dispositivo_Registro_ID,\n" +
                            "       Fecha_Modificacion AS DXA_Dispositivo_Fecha_Transaccion," +
                            "       DXA_Objeto," +
                            "       DXA_Cantidad_Eventos," +
                            "       Distancia_GPS," +
                            "       Tiempo_Disponibilidad," +
                            "       Consumo_Combustible AS DXA_ConsumoCombustible," +
                            "       Porcentaje_Bateria  AS DXA_Dispositivo_Nivel_Bateria," +
                            "       Tipo_Senal          AS DXA_SIM_Tipo_Red," +
                            "       Intensidad_Senal    AS DXA_SIM_Intensidad_Senal" +
                            "   FROM DETALLE_X_ACTIVIDAD\n" +
                            "   WHERE Id_Actividad = " + id + " AND DXA_ID = 0 AND DXA_Cantidad_Eventos <> 0";

            Cursor c = db.rawQuery(query,null);
            if(c.moveToFirst()){
                do{

                    Double  Latitud               = c.isNull(c.getColumnIndex("DXA_Latitud"))                   ? null :  c.getDouble(0);
                    Double  Longitud              = c.isNull(c.getColumnIndex("DXA_Longitud"))                  ? null :  c.getDouble(1);
                    Double  Velocidad             = c.isNull(c.getColumnIndex("DXA_Velocidad"))                 ? null :  c.getDouble(2);
                    Double  RPM                   = c.isNull(c.getColumnIndex("DXA_RPM"))                       ? null :  c.getDouble(3);
                    Double  Distancia             = c.isNull(c.getColumnIndex("DXA_Distancia"))                 ? null :  c.getDouble(4);
                    Double  Horas_Motor           = c.isNull(c.getColumnIndex("DXA_HorasMotor"))                ? null :  c.getDouble(5);
                    Double  NivelCombustible      = c.isNull(c.getColumnIndex("DXA_NivelCombustible"))          ? null :  c.getDouble(6);
                    Double  TemperaturaMotor      = c.isNull(c.getColumnIndex("DXA_TemperaturaMotor"))          ? null :  c.getDouble(7);
                    Double  TiempoActivo          = c.isNull(c.getColumnIndex("DXA_TiempoActivo"))              ? null :  c.getDouble(8);
                    Double  TiempoRalenti         = c.isNull(c.getColumnIndex("DXA_TiempoRalenti"))             ? null :  c.getDouble(9);
                    Double  TiempoApagado         = c.isNull(c.getColumnIndex("DXA_TiempoApagado"))             ? null :  c.getDouble(10);
                    String  Fecha_Modificacion    = c.getString(11);
                    Long    DispositivoRegistro   = c.getLong(12);
                    String  Fecha_Transaccion     = c.getString(13);
                    String  Objeto                = c.getString(14);
                    Integer Cantidad_Eventos      = c.getInt(15);
                    Double  Distancia_GPS         = c.isNull(c.getColumnIndex("Distancia_GPS"))                 ? null :  c.getDouble(16);
                    Double Tiempo_Disponibilidad         = c.isNull(c.getColumnIndex("Tiempo_Disponibilidad"))         ? null :  c.getDouble(17);
                    Double DXA_Porcentaje_Disponibilidad = (Tiempo_Disponibilidad / Cantidad_Eventos) * 100;
                    Double Consumo_Combustible           = c.isNull(c.getColumnIndex("DXA_ConsumoCombustible"))        ? null :  c.getDouble(18);
                    Double DXA_PorcentajeBateria = c.isNull(c.getColumnIndex("DXA_PorcentajeBateria"))        ? null :  c.getDouble(19);
                    String DXA_TipoSenal = c.getString(20);
                    Double DXA_IntensidadSenal = c.isNull(c.getColumnIndex("DXA_IntensidadSenal"))        ? null :  c.getDouble(21);
                    objeto = new com.example.gbaca.seguimento_equipo_agricola.data.objeto_enviado.Detalle_X_Actividad();

                    objeto.setDXA_Latitud(Latitud);
                    objeto.setDXA_Longitud(Longitud);
                    objeto.setDXA_Velocidad(Velocidad);
                    objeto.setDXA_RPM(RPM);
                    objeto.setDXA_Distancia(Distancia);
                    objeto.setDXA_HorasMotor(Horas_Motor);
                    objeto.setDXA_NivelCombustible(NivelCombustible);
                    objeto.setDXA_TemperaturaMotor(TemperaturaMotor);
                    objeto.setDXA_TiempoActivo(TiempoActivo);
                    objeto.setDXA_TiempoRalenti(TiempoRalenti);
                    objeto.setDXA_TiempoApagado(TiempoApagado);
                    objeto.setDXA_Fecha_Modificacion(Fecha_Modificacion);
                    objeto.setDXA_Dispositivo_Registro_ID(DispositivoRegistro);
                    objeto.setDXA_Dispositivo_Fecha_Transaccion(Fecha_Transaccion);
                    objeto.setDXA_Objeto(Objeto);
                    objeto.setDXA_Cantidad_Eventos(Cantidad_Eventos);
                    objeto.setDXA_Distancia_GPS(Distancia_GPS);
                    objeto.setDXA_Porcentaje_Disponibilidad(DXA_Porcentaje_Disponibilidad);
                    objeto.setDXA_ConsumoCombustible(Consumo_Combustible);
                    objeto.setDXA_Dispositivo_Nivel_Bateria(DXA_PorcentajeBateria);
                    objeto.setDXA_SIM_Tipo_Red(DXA_TipoSenal);
                    objeto.setDXA_SIM_Intensidad_Senal(DXA_IntensidadSenal);
                    Detalle_X_Actividad.add(objeto);
                }while(c.moveToNext());
            }
            c.close();
        }
        return Detalle_X_Actividad;
    }

    public Actividades_X_Equipo ObtenerUltimaActividad_X_Equipo(){

        Actividades_X_Equipo actividad = null;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT " +
                                  "Orden_Servicio," +
                                  "Implemento_1," +
                                  "Implemento_2," +
                                  "Implemento_3," +
                                  "Frente," +
                                  "Plantio," +
                                  "Actividad," +
                                  "Operador," +
                                  "AXE_Actividad_Inactiva," +
                                  "id," +
                                  "Equipo," +
                                  "AutoGuiado," +
                                  "AXE_Timestamp," +
                                  "Version_App," +
                                  "Dispositivo," +
                                  "Area_Trabajada " +
                                  " FROM ACTIVIDADES_X_EQUIPO ORDER BY id DESC LIMIT 1",null);
            if(c.moveToFirst()){
                do{
                    actividad = new Actividades_X_Equipo();
                    actividad.setOrden_Servicio(c.getString(0));
                    actividad.setImplemento_1(c.getString(1));
                    actividad.setImplemento_2(c.getString(2));
                    actividad.setImplemento_3(c.getString(3));
                    actividad.setFrente(c.getString(4));
                    actividad.setPlantio(c.getString(5));
                    actividad.setActividad(c.getString(6));
                    actividad.setOperador(c.getString(7));
                    actividad.setUsuario(c.getString(7));
                    actividad.setAXE_Actividad_Inactiva(c.getString(8));
                    actividad.setId(c.getLong(9));
                    actividad.setEquipo(c.getString(10));
                    actividad.setAutoGuiado(c.getInt(11));
                    actividad.setAXE_Timestamp(c.getString(12));
                    actividad.setVersion_APP(c.getFloat(13));
                    actividad.setDispositivo(c.getString(14));
                    actividad.setArea_Trabajada(c.getFloat(15));
                }while(c.moveToNext());
            }
            c.close();
        }
        return actividad;
    }


    public Empleados ObtenerEmpleadoxCodigo(String CodigoEmpleado){

        Empleados empleado = null;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Log.i("query","SELECT Codigo,Empleado FROM Empleados WHERE CAST(Codigo as Integer)  = CAST('" + CodigoEmpleado + "' as Integer) LIMIT 1;");
            Cursor c = db.rawQuery("SELECT Codigo,Empleado FROM Empleados WHERE CAST(Codigo as Integer)  = CAST('" + CodigoEmpleado + "' as Integer) LIMIT 1;",null);
            if(c.moveToFirst()){
                do{
                    empleado = new Empleados();
                    empleado.setCodigo(c.getString(0));
                    empleado.setEmpleado(c.getString(1));
                }while(c.moveToNext());
            }
            c.close();
        }
        return empleado;
    }

    public Configuracion ObtenerConfiguracion(){

        Configuracion configuracion = null;
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT Actividad," +
                                   "Actividad_N," +
                                   "Plantio," +
                                   "Plantio_N," +
                                   "OS," +
                                   "Frente," +
                                   "Implemento1," +
                                   "Implemento2," +
                                   "Implemento3," +
                                   "Empleado," +
                                   "Empleado_N," +
                                   "Cambios," +
                                   "Distancia," +
                                   "Latitud," +
                                   "Longitud," +
                                   "ActividadInactiva," +
                                   "ActividadInactiva_N," +
                                   "Flag_Frente," +
                                   "Flag_OS," +
                                   "Flag_Actividad_Inactiva," +
                                   "Flag_Implemento," +
                                   "Flag_Empleado," +
                                   "Frente_N," +
                                   "Equipo," +
                                   "DIGI_Conectado," +
                                   "Ultima_Version_App," +
                                   "URL_Descarga_APK," +
                                   "Tipo_Senal," +
                                   "Intensidad_Senal" +
                                   "    FROM Configuracion" +
                                   "    LIMIT 1",null);
            if(c.moveToFirst()){
                do{
                    configuracion = new Configuracion();
                    configuracion.setActividad(c.getString(0));
                    configuracion.setActividad_N(c.getString(1));
                    configuracion.setPlantio(c.getString(2));
                    configuracion.setPlantio_N(c.getString(3));
                    configuracion.setOS(c.getString(4));
                    configuracion.setFrente(c.getString(5));
                    configuracion.setImplemento1(c.getString(6));
                    configuracion.setImplemento2(c.getString(7));
                    configuracion.setImplemento3(c.getString(8));
                    configuracion.setEmpleado(c.getString(9));
                    configuracion.setEmpleado_N(c.getString(10));
                    configuracion.setCambios(c.getInt(11));
                    configuracion.setDistancia(c.getDouble(12));
                    configuracion.setLatitud(c.getDouble(13));
                    configuracion.setLongitud(c.getDouble(14));
                    configuracion.setActividadInactiva(c.getString(15));
                    configuracion.setActividadInactiva_N(c.getString(16));
                    configuracion.setFlag_Frente(c.getInt(17));
                    configuracion.setFlag_OS(c.getInt(18));
                    configuracion.setFlag_Actividad_Inactiva(c.getInt(19));
                    configuracion.setFlag_Implemento(c.getInt(20));
                    configuracion.setFlag_Empleado(c.getInt(21));
                    configuracion.setFrente_N(c.getString(22));
                    configuracion.setEquipo(c.getString(23));
                    configuracion.setDIGI_Conectado(c.getInt(24));
                    configuracion.setUltima_Version_App(c.getString(25));
                    configuracion.setURL_Descarga_APK(c.getString(26));
                    configuracion.setTipo_Senal(c.getString(27));
                    configuracion.setIntensidad_Senal(c.getDouble(28));
                }while(c.moveToNext());
            }
            c.close();
        }
        return configuracion;
    }


    public String ObtenerNombreActividadxCodigo(String codigoActividad){
        String Actividad_N = "";
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT Actividad_N FROM OS WHERE Actividad = \""+codigoActividad+"\"LIMIT 1\n",null);
            if(c.moveToFirst()){
                do{
                    Actividad_N = c.getString(0);
                }while(c.moveToNext());
            }
            c.close();
        }
        return Actividad_N;
    }


    public String ObtenerNombreActividadInactivaxCodigo(String codigoActividad){
        String Actividad_N = "";
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT Actividad_N FROM ActividadesInactivas WHERE Actividad = \""+codigoActividad+"\" LIMIT 1\n",null);
            if(c.moveToFirst()){
                do{
                    Actividad_N = c.getString(0);
                }while(c.moveToNext());
            }
            c.close();
        }
        return Actividad_N;
    }

    public String ObtenerNombreImplementoxCodigo(String implemento) {

        String Implemento_N = "";
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT Descripcion FROM Implementos WHERE Codigo = \""+implemento+"\" LIMIT 1\n",null);
            if(c.moveToFirst()){
                do{
                    Implemento_N = c.getString(0);
                }while(c.moveToNext());
            }
            c.close();
        }
        return Implemento_N;
    }

    public String ObtenerNombrePlantioxCodigo(String codigoPlantio){
        String Plantio_N = "";
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT Plantio_N FROM OS WHERE Plantio = \""+codigoPlantio+"\" LIMIT 1\n",null);
            if(c.moveToFirst()){
                do{
                    Plantio_N = c.getString(0);
                }while(c.moveToNext());
            }
            c.close();
        }
        return Plantio_N;
    }

    public String ObtenerNombreFrentexCodigo(String codigoFrente){
        String Frente_N = "";
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null){
            Cursor c = db.rawQuery("SELECT Descripcion FROM Frentes WHERE Codigo = \""+codigoFrente+"\" LIMIT 1\n",null);
            if(c.moveToFirst()){
                do{
                    Frente_N = c.getString(0);
                }while(c.moveToNext());
            }
            c.close();
        }
        return Frente_N;
    }


    // Metodos de insersion
    public void insertar_ActividadesInactivas(List<ActividadesInactivas> listaActividadesInactivas){

        SQLiteDatabase db = this.getWritableDatabase();
        try {

            db.beginTransaction();
            String query = " INSERT INTO " +
                    " ActividadesInactivas(Actividad,Actividad_N,Tipo_Actividad) " +
                    " VALUES (?,?,?)";

            SQLiteStatement stmt = db.compileStatement(query);

            for (ActividadesInactivas actividadinactiva : listaActividadesInactivas) {
                stmt.bindString(1, actividadinactiva.getActividad());
                stmt.bindString(2, actividadinactiva.getActividad_N());
                stmt.bindString(3, actividadinactiva.getTipo_Actividad());
                stmt.execute();
            }
            stmt.close();
            db.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void insertar_Frentes(List<Frentes> listaFrentes){

        SQLiteDatabase db = this.getWritableDatabase();
        try {

            db.beginTransaction();
            String query = " INSERT INTO " +
                    " Frentes(Codigo,Descripcion) " +
                    " VALUES (?,?)";

            SQLiteStatement stmt = db.compileStatement(query);

            for (Frentes frente : listaFrentes) {
                stmt.bindString(1, frente.getCodigo());
                stmt.bindString(2, frente.getDescripcion());
                stmt.execute();
            }
            stmt.close();
            db.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
    public void insertar_OS(List<OS> listaos){

        SQLiteDatabase db = this.getWritableDatabase();
        try {

            db.beginTransaction();
            String query = " INSERT INTO " +
                    " OS(" +
                    "       Codigo_OS," +
                    "       Plantio," +
                    "       Plantio_N," +
                    "       Actividad," +
                    "       Actividad_N," +
                    "       Tipo_Actividad," +
                    "       Inicio_Actividad," +
                    "       Fin_Actividad" +
                    ") " +
                    " VALUES (?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = db.compileStatement(query);

            for (OS os : listaos) {
                stmt.bindString(1, os.getCodigo_OS());
                stmt.bindString(2, os.getPlantio());
                stmt.bindString(3, os.getPlantio_N());
                stmt.bindString(4, os.getActividad());
                stmt.bindString(5, os.getActividad_N());
                stmt.bindString(6, os.getTipo_Actividad());
                stmt.bindString(7, os.getInicio_Actividad());
                stmt.bindString(8, os.getFin_Actividad());
                stmt.execute();
            }
            stmt.close();
            db.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    public void insertar_Implementos(List<Implementos> listaImplementos){

        SQLiteDatabase db = this.getWritableDatabase();
        try {

            db.beginTransaction();
            String query = " INSERT INTO " +
                    " Implementos(" +
                    "       Codigo," +
                    "       Descripcion" +
                    ") " +
                    " VALUES (?,?)";

            SQLiteStatement stmt = db.compileStatement(query);

            for (Implementos implemento : listaImplementos) {
                stmt.bindString(1, implemento.getCodigo());
                stmt.bindString(2, implemento.getDescripcion());
                stmt.execute();
            }
            stmt.close();
            db.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void insertar_Empleados(List<Empleados> listaEmpleados){

        SQLiteDatabase db = this.getWritableDatabase();
        try {

            db.beginTransaction();
            String query = " INSERT INTO " +
                    " Empleados(" +
                    "       Codigo," +
                    "       Empleado" +
                    ") " +
                    " VALUES (?,?)";

            SQLiteStatement stmt = db.compileStatement(query);

            for (Empleados empleado : listaEmpleados) {
                stmt.bindString(1, empleado.getCodigo());
                stmt.bindString(2, empleado.getEmpleado());
                stmt.execute();
            }
            stmt.close();
            db.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    public Long insertar_Actividades_X_Equipo(Actividades_X_Equipo actividad_x_equipo){

        Long registro = 0L;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){

                db.beginTransaction();//db.beginTransactionNonExclusive(); //
                try{
                    ContentValues values = new ContentValues();
                    values.put("AXE_ID",actividad_x_equipo.getAXE_ID());
                    values.put("Frente",actividad_x_equipo.getFrente());
                    values.put("Plantio",actividad_x_equipo.getPlantio());
                    values.put("Orden_Servicio",actividad_x_equipo.getOrden_Servicio());
                    values.put("Implemento_1",actividad_x_equipo.getImplemento_1());
                    values.put("Implemento_2",actividad_x_equipo.getImplemento_2());
                    values.put("Implemento_3",actividad_x_equipo.getImplemento_3());
                    values.put("Equipo",actividad_x_equipo.getEquipo());
                    values.put("Operador",actividad_x_equipo.getOperador());
                    values.put("Actividad",actividad_x_equipo.getActividad());
                    values.put("AXE_Actividad_Inactiva",actividad_x_equipo.getAXE_Actividad_Inactiva());
                    values.put("Fecha_Inicio",actividad_x_equipo.getFecha_Inicio());
                    values.put("Fecha_Fin",actividad_x_equipo.getFecha_Fin());
                    values.put("Sincronizado",actividad_x_equipo.getSincronizado());
                    values.put("Fecha_Modificacion",actividad_x_equipo.getFecha_Modificacion());
                    values.put("Usuario",actividad_x_equipo.getUsuario());
                    values.put("Version_APP",actividad_x_equipo.getVersion_APP());
                    values.put("Dispositivo",actividad_x_equipo.getDispositivo());
                    values.put("Area_Trabajada",actividad_x_equipo.getArea_Trabajada());
                    values.put("AutoGuiado",actividad_x_equipo.getAutoGuiado());
                    values.put("AXE_Timestamp",actividad_x_equipo.getAXE_Timestamp());
                    registro=db.insert("Actividades_X_Equipo",null,values);
                    db.setTransactionSuccessful();
                }catch (Exception e){
                    Toast.makeText(contexto,contexto.getString(R.string.guardando_actividades_x_equipo_error),Toast.LENGTH_SHORT).show();
                }finally{
                    db.endTransaction();
                }
            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,contexto.getString(R.string.guardando_actividades_x_equipo_error),Toast.LENGTH_SHORT).show();
        }
        return registro;
    }

    public Long insertar_Detalle_X_Actividad(Detalle_X_Actividad detalle_x_actividad){
        Long registro = 0L;
        try{
            SQLiteDatabase db = this.getWritableDatabase();

            if(db!=null){
                db.beginTransaction(); //db.beginTransactionNonExclusive(); //
                try{
                    ContentValues values = new ContentValues();
                    values.put("DXA_ID",detalle_x_actividad.getDXA_ID());
                    values.put("Id_Actividad",detalle_x_actividad.getId_Actividad());
                    values.put("Latitud",detalle_x_actividad.getLatitud());
                    values.put("Longitud",detalle_x_actividad.getLongitud());
                    values.put("Velocidad",detalle_x_actividad.getVelocidad());
                    values.put("RPM",detalle_x_actividad.getRPM());
                    values.put("Distancia",detalle_x_actividad.getDistancia());
                    values.put("Distancia_GPS",detalle_x_actividad.getDistancia_GPS());
                    values.put("Horas_Motor",detalle_x_actividad.getHoras_Motor());
                    values.put("Nivel_Combustible",detalle_x_actividad.getNivel_Combustible());
                    values.put("Temperatura_Motor",detalle_x_actividad.getTemperatura_Motor());
                    values.put("Tiempo_Activo",detalle_x_actividad.getTiempo_Activo());
                    values.put("Tiempo_Ralenti",detalle_x_actividad.getTiempo_Ralenti());
                    values.put("Tiempo_Apagado",detalle_x_actividad.getTiempo_Apagado());
                    values.put("Fecha_Modificacion",detalle_x_actividad.getFecha_Modificacion());
                    values.put("DXA_Objeto",detalle_x_actividad.getDXA_Objeto());
                    values.put("Tiempo_Disponibilidad",detalle_x_actividad.getTiempo_Disponibilidad());
                    values.put("Consumo_Combustible",detalle_x_actividad.getConsumo_Combustible());
                    values.put("Porcentaje_Bateria",detalle_x_actividad.getPorcentaje_Bateria());
                    values.put("Tipo_Senal",detalle_x_actividad.getTipo_Senal());
                    values.put("Intensidad_Senal",detalle_x_actividad.getIntensidad_Senal());
                    registro = db.insert("Detalle_X_Actividad",null,values);
                    db.setTransactionSuccessful();
                }catch (Exception e){
                    Toast.makeText(contexto,contexto.getString(R.string.guardando_detalle_x_actividad_error),Toast.LENGTH_SHORT).show();
                }finally {
                    db.endTransaction();
                }
            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,contexto.getString(R.string.guardando_detalle_x_actividad_error),Toast.LENGTH_SHORT).show();
        }
        return registro;
    }

    public Long insertar_Eventos_Detalle_Actividad(Eventos_Detalle_Actividad eventos_detalle_actividad){
        Long registro = 0L;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Id_Detalle_Actividad",eventos_detalle_actividad.getId_Detalle_Actividad());
                values.put("Velocidad",eventos_detalle_actividad.getVelocidad());
                values.put("RPM",eventos_detalle_actividad.getRPM());
                values.put("Distancia",eventos_detalle_actividad.getDistancia());
                values.put("Velocidad_timestamp",eventos_detalle_actividad.getVelocidad_timestamp());
                values.put("RPM_timestamp",eventos_detalle_actividad.getRPM_timestamp());
                values.put("Fecha_Modificacion",eventos_detalle_actividad.getFecha_Modificacion());

                registro = db.insert("Eventos_Detalle_Actividad",null,values);
                db.beginTransaction(); //db.beginTransactionNonExclusive();//
                try{
                    db.setTransactionSuccessful();
                }catch (Exception e){
                    Toast.makeText(contexto,contexto.getString(R.string.guardando_detalle_x_actividad_error),Toast.LENGTH_SHORT).show();
                }finally {
                    db.endTransaction();
                }
            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,contexto.getString(R.string.guardando_detalle_x_actividad_error),Toast.LENGTH_SHORT).show();
        }
        return registro;
    }

    // Metodos de actualizacion
    public void actualizar_detalle_x_actividad(Detalle_X_Actividad detalle_x_actividad){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){

                Double Tiempo_Activo         = detalle_x_actividad.getTiempo_Activo();
                Double Tiempo_Ralenti        = detalle_x_actividad.getTiempo_Ralenti();
                Double Tiempo_Apagado        = detalle_x_actividad.getTiempo_Apagado();
                Double Tiempo_Disponibilidad = Tiempo_Activo + Tiempo_Ralenti + Tiempo_Apagado;

                ContentValues values = new ContentValues();
                values.put("Velocidad",detalle_x_actividad.getVelocidad());
                values.put("RPM",detalle_x_actividad.getRPM());
                values.put("Distancia",detalle_x_actividad.getDistancia());
                values.put("Distancia_GPS",detalle_x_actividad.getDistancia_GPS());
                values.put("Tiempo_Activo",Tiempo_Activo);
                values.put("Tiempo_Ralenti",Tiempo_Ralenti);
                values.put("Tiempo_Apagado",Tiempo_Apagado);
                values.put("Tiempo_Disponibilidad",Tiempo_Disponibilidad);
                values.put("DXA_Cantidad_Eventos",detalle_x_actividad.getDXA_Cantidad_Eventos());
                db.update("DETALLE_X_ACTIVIDAD",values,"id="+detalle_x_actividad.getId(),null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando en la db",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_actividades_x_equipo(Actividades_X_Equipo actividades_x_equipo){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Fecha_Fin",actividades_x_equipo.getFecha_Fin());
                values.put("Fecha_Modificacion",actividades_x_equipo.getFecha_Modificacion());
                values.put("Area_Trabajada",actividades_x_equipo.getArea_Trabajada());
                db.update("ACTIVIDADES_X_EQUIPO",values,"id="+actividades_x_equipo.getId(),null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando en la db",Toast.LENGTH_SHORT).show();
        }

    }

    public void marcar_enviada_actividad_x_equipo(Actividades_X_Equipo actividades_x_equipo){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("AXE_ID",actividades_x_equipo.getAXE_ID());
                db.update("ACTIVIDADES_X_EQUIPO",values,"id="+actividades_x_equipo.getId(),null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando en la db",Toast.LENGTH_SHORT).show();
        }

    }

    public void marcar_enviada_detalle_x_actividad(Detalle_X_Actividad detalle_x_actividad){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("DXA_ID",detalle_x_actividad.getDXA_ID());
                db.update("DETALLE_X_ACTIVIDAD",values,"id="+detalle_x_actividad.getId(),null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando en la db",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_eventos_detalle_x_actividad_velocidad_distancia(Eventos_Detalle_Actividad eventos_detalle_actividad){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Velocidad",eventos_detalle_actividad.getVelocidad());
                values.put("Velocidad_timestamp",eventos_detalle_actividad.getVelocidad_timestamp());
                values.put("Distancia",eventos_detalle_actividad.getDistancia());
                db.update("EVENTOS_DETALLE_ACTIVIDAD",values,"id="+eventos_detalle_actividad.getId(),null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando la velocidad en Eventos Detalle por Actividad",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_eventos_detalle_x_actividad_rpm(Eventos_Detalle_Actividad eventos_detalle_actividad){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("RPM",eventos_detalle_actividad.getRPM());
                values.put("RPM_timestamp",eventos_detalle_actividad.getRPM_timestamp());
                db.update("EVENTOS_DETALLE_ACTIVIDAD",values,"id="+eventos_detalle_actividad.getId(),null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando las RPM en Eventos Detalle por Actividad",Toast.LENGTH_SHORT).show();
        }

    }



    public void actualizar_detalle_x_actividad_DXA_Objeto(Detalle_X_Actividad detalle_x_actividad){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("DXA_Objeto",detalle_x_actividad.getDXA_Objeto());
                db.update("DETALLE_X_ACTIVIDAD",values,"id="+detalle_x_actividad.getId(),null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando la GPS en Detalle por Actividad",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_detalle_x_actividad_temperatura_motor(Detalle_X_Actividad detalle_x_actividad){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Temperatura_Motor",detalle_x_actividad.getTemperatura_Motor());
                values.put("Temperatura_Motor_timestamp",detalle_x_actividad.getTemperatura_Motor_timestamp());
                db.update("DETALLE_X_ACTIVIDAD",values,"id="+detalle_x_actividad.getId(),null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando la Temperatura Motor en Detalle por Actividad",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_detalle_x_actividad_nivel_combustible(Detalle_X_Actividad detalle_x_actividad){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Nivel_Combustible",detalle_x_actividad.getNivel_Combustible());
                values.put("Nivel_Combustible_timestamp",detalle_x_actividad.getNivel_Combustible_timestamp());
                db.update("DETALLE_X_ACTIVIDAD",values,"id="+detalle_x_actividad.getId(),null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Nivel Combustible en Detalle por Actividad",Toast.LENGTH_SHORT).show();
        }

    }


    public void actualizar_detalle_x_actividad_consumo_combustible(Detalle_X_Actividad detalle_x_actividad){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Consumo_Combustible",detalle_x_actividad.getConsumo_Combustible());
                db.update("DETALLE_X_ACTIVIDAD",values,"id="+detalle_x_actividad.getId(),null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Consumo Combustible en Detalle por Actividad",Toast.LENGTH_SHORT).show();
        }

    }


    public void actualizar_detalle_x_actividad_horas_motor(Detalle_X_Actividad detalle_x_actividad){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Horas_Motor",detalle_x_actividad.getHoras_Motor());
                values.put("Horas_Motor_timestamp",detalle_x_actividad.getHoras_Motor_timestamp());
                db.update("DETALLE_X_ACTIVIDAD",values,"id="+detalle_x_actividad.getId(),null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Horas Motor en Detalle por Actividad",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_ActividadInactiva(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("ActividadInactiva",configuracion.getActividadInactiva());
                values.put("ActividadInactiva_N",configuracion.getActividadInactiva_N());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_DIGI_Conectado(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("DIGI_Conectado",configuracion.getDIGI_Conectado());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Ultima_Version_App(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Ultima_Version_App",configuracion.getUltima_Version_App());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_URL_Descarga_APK(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("URL_Descarga_APK",configuracion.getURL_Descarga_APK());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Tipo_Senal(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Tipo_Senal",configuracion.getTipo_Senal());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Intensidad_Senal(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Intensidad_Senal",configuracion.getIntensidad_Senal());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_OS(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Actividad",configuracion.getActividad());
                values.put("Actividad_N",configuracion.getActividad_N());
                values.put("Plantio",configuracion.getPlantio());
                values.put("Plantio_N",configuracion.getPlantio_N());
                values.put("OS",configuracion.getOS());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Frente(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Frente",configuracion.getFrente());
                values.put("Frente_N",configuracion.getFrente_N());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Implemento1(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Implemento1",configuracion.getImplemento1());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Implemento2(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Implemento2",configuracion.getImplemento2());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Implemento3(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Implemento3",configuracion.getImplemento3());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Empleado(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Empleado",configuracion.getEmpleado());
                values.put("Empleado_N",configuracion.getEmpleado_N());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Cambios(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Cambios",configuracion.getCambios());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Distancia(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Distancia",configuracion.getDistancia());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Location(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Latitud",configuracion.getLatitud());
                values.put("Longitud",configuracion.getLongitud());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_flag_frente(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Flag_Frente",configuracion.getFlag_Frente());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Flag_OS(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Flag_OS",configuracion.getFlag_OS());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Flag_Actividad_Inactiva(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Flag_Actividad_Inactiva",configuracion.getFlag_Actividad_Inactiva());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Flag_Implemento(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Flag_Implemento",configuracion.getFlag_Implemento());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Flag_Empleado(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Flag_Empleado",configuracion.getFlag_Empleado());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar_configuracion_Equipo(Configuracion configuracion){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if(db!=null){
                ContentValues values = new ContentValues();
                values.put("Equipo",configuracion.getEquipo());
                db.update("Configuracion",values,null,null);

            }

        }catch (SQLiteException e){

            Toast.makeText(contexto,"Error actualizando Configuracion",Toast.LENGTH_SHORT).show();
        }

    }



}
