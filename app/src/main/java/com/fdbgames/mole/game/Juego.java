package com.fdbgames.mole.game;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.prefs.Preferences;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;
//@RunWith(RobolectricTestRunner.class)
public class Juego {
    String cancion;
    int nivel;
    Date fecha;
    float score;
    boolean ultimo = false;

    public static final int NIVEL_1 = 1;
    public static final int NIVEL_2 = 2;
    public static String TAG = "Juego";
    public static final String LISTA_JUEGOS = "lista";
    static public String htmlStringHistorico  =
            "<h1>Historico ultimos puntajes </h1>\n"+
                    "<p>cancion 1 \n" +
                    "                    <a href=\"https://search.stepmaniaonline.net\">Stepmania ALL</a><br><br>\n" +
                    "                    <a href=\"https://violentvioletsims.wixsite.com/zerolaggaming\">[Pop]Violent sims</a> <br><br>\n" +
                    "                    <a href=\"https://search.stepmaniaonline.net/packs/Gpop's%20Pack%20of%20Original%20Pad%20Sims\">[Dubstep] Gpop's Pack of Original...</a><br><br>\n" +
                    "                    <a href=\"https://search.stepmaniaonline.net/packs/Vocaloid%20Project%20Pad%20Pack\">[Dubstep] Vocaloid project pack...</a><br><br>\n" +
                    "                    <a href=\"https://search.stepmaniaonline.net/packs/Ninjafar's%20Insanely%20Cute%20Electronic%20Simfile%20Troop\">[Dubstep] Ninjafar's  insanely...</a><br><br>\n" +
                    "                    <a href=\"https://search.stepmaniaonline.net/packs/StepMix\">Stepmix</a><br><br>\n" +
                    "                    <a href=\"https://www.google.com/search?q=stepmania+song+archive\">Search archives @Google</a></p>"+
                    "<p>After the smzip/zip is downloaded tap the notification or open the downloaded file by this app</p>\n"+
                    "<p>Game will unpack the smzip in to your game folder</p>\n" ;

    public Juego(String cancion, int nivel, Date fecha, float score) {
        this.cancion = cancion;
        this.nivel = nivel;
        this.fecha = fecha;
        this.score = score;
    }
    public Juego(String cancion, int nivel, Date fecha, float score, boolean ultimo) {
        this.cancion = cancion;
        this.nivel = nivel;
        this.fecha = fecha;
        this.score = score;
        this.ultimo = ultimo;
    }
    public static void main(String args[]){
        //testGuardarPreferences(null);
    }
    public static void testGuardarPreferences(SharedPreferences prefs){
        List<Juego> lstJuego = new ArrayList<>();
        Juego elJuego = new Juego("cancion1",1,new Date(),12000);
        lstJuego.add(elJuego);
        guardarJuegoEnPreferences(lstJuego, prefs);
        elJuego = new Juego("cancion2",1,new Date(),14000);
        lstJuego.add(elJuego);
        guardarJuegoEnPreferences(lstJuego, prefs);

    }
    public static List<Juego>  testHistoricoJuegosMock(SharedPreferences prefs){
        List<Juego> lstJuego = new ArrayList<>();
        Juego elJuego = new Juego("cancion1",1,new Date(),12000);
        lstJuego.add(elJuego);
        guardarJuegoEnPreferences(lstJuego,prefs);
        elJuego = new Juego("cancion2",1,new Date(),14000);
        lstJuego.add(elJuego);
        guardarJuegoEnPreferences(lstJuego,prefs);
        getHistoricoFromJson(prefs);
        return lstJuego;
    }
    public static List<Juego> getHistoricoFromJson(SharedPreferences prefs){
        String strJsonHistorico = prefs.getString("historico",null);
        Log.d(TAG,"****** 3 leyendo historico de preferences JSONObject ==> " + strJsonHistorico);
        Type listType = new TypeToken<ArrayList<Juego>>(){}.getType();
        List<Juego> fromJsonList = new Gson().fromJson(strJsonHistorico, listType);
        return fromJsonList;
    }
    public static void guardarJuegoEnPreferences(List<Juego> lstJuegos, SharedPreferences prefs){
        //lstJuegos.add(elJuego);//json.toJson(elJuego, Juego.class)));
        JSONObject jobject = null;
        List<String> lstStrJuegos = new ArrayList<>();
        // Crear nuevo objeto Json basado en el mapa
        try {
            jobject = new JSONObject();
            jobject.put(LISTA_JUEGOS,lstJuegos);
            // Depurando objeto Json...
            Log.d(TAG,"****** 3 str json listJuegos ");
            //Gson gson = gsonBuilder.create();

            //String JSONObject = gson.toJson(crunchify);
            String jsonObject = json.toJson(lstJuegos.get(0), Juego.class);
            lstStrJuegos.add(jsonObject);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String JSONObject = gson.toJson(lstJuegos);
            Log.d(TAG,"****** Converted JSONObject ==> " + JSONObject);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("historico",JSONObject);
            editor.apply();
            //JSONArray jArray = jobject.getJSONArray( LISTA_JUEGOS );
            Type listType = new TypeToken<ArrayList<Juego>>(){}.getType();
            List<Juego> fromJsonList = new Gson().fromJson(JSONObject, listType);
            Log.d(TAG,"****** from json (0) cancion "+fromJsonList.get(0).getCancion());
            getHistoricoFromJson(prefs);
            //jArray = jobject.getJSONArray(LISTA_JUEGOS);
            //Log.d(TAG,"****** str json listJuegos length "+jArray.length());

            /*for(int ii=0; ii < jArray.length(); ii++)
                Log.d(TAG,"****** str json listJuegos "+(jArray.getJSONObject(ii).getString("value")));*/
        }catch ( Exception ex){
            ex.printStackTrace();
        }
    }

    public String getCancion() {
        return cancion;
    }

    public void setCancion(String cancion) {
        this.cancion = cancion;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isUltimo() {
        return ultimo;
    }

    public void setUltimo(boolean ultimo) {
        this.ultimo = ultimo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Juego juego = (Juego) o;
        return getNivel() == juego.getNivel() &&
                Float.compare(juego.getScore(), getScore()) == 0 &&
                Objects.equals(getCancion(), juego.getCancion()) &&
                Objects.equals(getFecha(), juego.getFecha());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCancion(), getNivel(), getFecha(), getScore());
    }
}
