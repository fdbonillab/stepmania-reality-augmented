package com.fdbgames.mole.stepmaniac;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.StreamUtils;
import com.fdbgames.Vector2;
import com.fdbgames.mole.SMAssets;
import com.fdbgames.mole.game.GameEngine;
import com.fdbgames.mole.game.Juego;
import com.fdbgames.mole.game.LinksDowloads;
import com.fdbgames.mole.game.Receptor;
import com.fdbgames.mole.game.Resultado;
import com.fdbgames.mole.game.SMApplication;
import com.fdbgames.mole.game.Score;
import com.fdbgames.mole.game.UtilFile;
import com.fdbgames.mole.game.UtilZip;
import com.fdbgames.mole.global.Config;
import com.fdbgames.mole.global.constants.Direction;
import com.fdbgames.mole.sa.io.files.Measure;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import com.badlogic.gdx.assets.AssetManager;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.max;
import static org.opencv.core.Core.meanStdDev;
import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.imgproc.Imgproc.LINE_8;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.circle;
import static org.opencv.imgproc.Imgproc.contourArea;
import static org.opencv.imgproc.Imgproc.getRotationMatrix2D;
import static org.opencv.imgproc.Imgproc.rectangle;
import static org.opencv.imgproc.Imgproc.resize;
import static org.opencv.imgproc.Imgproc.warpAffine;


public class MainActivity extends AndroidApplication implements CvCameraViewListener2 , View.OnClickListener, View.OnTouchListener, AppCompatCallback  {
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;
    private Mat                    mRgba;
    private Mat                    mGray;
    private Mat  copiaRgba;
    private Mat imgUltimoAcierto;
    private Mat base;
    int toques;
    Random rand = new Random();
    //Topo unTopo = null;
    Topo topoPlantilla;
    ArrayList<Topo> lstTopos;
    List<Juego> lstJuego;
    int LIMITE_LISTA_TOPOS = 10;
    int sextoDeAnchoPantalla;
    int DIVISION_PANTALLA_ANCHO = 8;
    int duracionFrame;
    final static int LIMITE_DURACION_FRAME = 4;
    Mat frameAnterior;
    long startTime = 0;
    long inicioEspera = 0;
    static Long tiempoFinEspera = 0l;
    static Long tiempoFinMusica = 0l;
    double offsetY = 0;
    double offsetX = 0;
    Context contexto;
    int idJuego = 0;
    final int SEGUNDOS_LIMITE = 40;////AKI ESTAN LOS SEGUNDOS LIMITES PARA AJUSTAR TIEMPOS DE PRUEBAS
    final int SEGUNDOS_ESPERA_LIMITE = 3;
    final static int NUMERO_NIVELES = 8;
    static boolean inicioJuego = false;
    boolean nivelEscogido = false;
    String textTime;
    int ultimaDeteccion = NADA;
    String ultimoMensaje;
    volatile  boolean endGame = false;
    GridLayout layoutNiveles;
    Button botonStart = null;
    Button botonSongs = null;
    Button botonSelectFile = null;
    Button botonOkLinks = null;
    TextView textLinksDowload = null;
    ScrollView scrollTextoLinks = null;
    ScrollView scrollLayoutTable = null;
    boolean existeHiloRevisandoEndGame = false;
    SMAssets assets;
    static MediaPlayer mpIntro ;
    static boolean introYaSonando = false;
    String nombreFolderJuego = "BeatAR";
    ImageButton botonShare = null;
    android.content.res.AssetManager assManager;
    ArrayList<Button> lstBotonesNiveles;
    TableLayout tableLayoutScores;
    LinearLayout idForSaveView;
    static int CODE_FOR_RESULT=981;
    private MenuItem[] tmEffectMenuItems;
    private SubMenu mColorEffectsMenu;
    private MenuItem[] mResolutionMenuItems;
    private SubMenu mResolutionMenu;
    private AppCompatDelegate delegate;
    Measure measureActual;
    Toolbar toolbar;
    int SPEED = 1;
    //final SharedPreferences prefs = getSharedPreferences("beatar", Context.MODE_PRIVATE);/// como tiene que ser final no se puede usar globar
    boolean hayUnAcierto = false;
    int duracionMensaje;
    List<Mat> lstSecuenciaGolpe;
    List<Mat> lstSecuenciaGolpeMask;
    Map<Integer,Flecha> mapFlechas;
    Map<Integer,Flecha> mapArrows;
    List<Flecha> lstArrows;
    SecuenciaEstados secuenciaEstadosNormal;
    SecuenciaEstados secuenciaEstadosGolpe;
    Map<Integer,Boolean> mapNivelesSuperados;
    SMApplication smApplication;
    static final String MSG_ACIERTO= "Acierto";
    static final String MSG_FALLA = "Falla";
    static final String MSG_VACIO = "";
    static final int NADA = 0;
    static final int ACIERTO = 1;
    static final int FALLA = 2;
    static final int LIMITE_DURACION_MENSAJE = 10;
    static final String ULTIMO_NIVEL_SUPERADO= "ultimoNivel";
    static boolean enEspera = false;
    private Bitmap bitmapForShare;
    Nivel nivel;
    GameEngine game;
    boolean saltarNotes = false;
    int notasASaltar = 0;
    PackSong packSongDescargado;
    List<MatOfPoint> listTopRight = null;
    List<MatOfPoint> listTopLeft = null;
    List<MatOfPoint> listDownRight = null;
    List<MatOfPoint> listDownLeft = null;
    Map<Integer,Receptor> mapaReceptores;
    List<Scalar> lstColoresFlechas = null;
    Map<Integer,Score> mapScores = null;
    List<Score> lstScoresRecientes;
    List<PointMeasure> lstPuntosRecientes;
    static final int ESQUINA_TOP_LEFT = 0;
    static final int ESQUINA_TOP_RIGHT = 1;
    static final int ESQUINA_DOWN_LEFT= 2;
    static final int ESQUINA_DOWN_RIGHT = 3;
    static final Scalar COLOR_RED= new Scalar(255,0,0);
    static final Scalar COLOR_GREEN = new Scalar(0,255,0);
    static final Scalar COLOR_BLUE = new Scalar(0,0,255);
    static final Scalar COLOR_YELLOW = new Scalar(238 , 213 , 26);
    static final Scalar COLOR_WHITE = Scalar.all(255);
    AssetManager manager = null;
    String urlFileAtlas = "gfx/ui/mdpi/gameskin.atlas";
    String urlFileAtlas2 = "gfx/ui/gameskin.atlas";
    String urlFileArrow = "arrow.png";
    DialogProperties properties = new DialogProperties();
    FilePickerDialog dialog;
    int maxRight;
    int maxDown;
    static int idUltimoMeasure = 0;
    static int contadorNotasUltimoMeasure = 0;
    static int maximoNotasUltimoMeasure = 0;
    float puntajeAcumulado = 0;
    int maxPosYFlecha = 0;
    boolean flechaMostrandose = false;
    boolean ultimaFlechaMostrada = false;

    int face[] = {Core.FONT_HERSHEY_SIMPLEX, Core.FONT_HERSHEY_PLAIN, Core.FONT_HERSHEY_DUPLEX, Core.FONT_HERSHEY_COMPLEX,
            Core.FONT_HERSHEY_TRIPLEX, Core.FONT_HERSHEY_COMPLEX_SMALL, Core.FONT_HERSHEY_SCRIPT_SIMPLEX,
            Core.FONT_HERSHEY_SCRIPT_COMPLEX, Core.FONT_ITALIC};
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public synchronized void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            seconds = SEGUNDOS_LIMITE -seconds;
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int sizeMeasure = 30;
            if ( game != null){
                //////Log.i(TAG, "****** info game  "+game.chart.toString());
                sizeMeasure =  game.chart.noteData.measures.size();
            }
            if( endGame && !game.songPlayer.isMusicPlaying() ){
                ////////Log.i(TAG, "****** timer reconoce final del juego " );
                //layoutNiveles.setVisibility(View.VISIBLE);
                botonShare.setVisibility(View.VISIBLE   );
                botonSelectFile.setVisibility(View.VISIBLE);
                botonSongs.setVisibility(View.VISIBLE);
                ///todo guarda resultado de este juego para el historico
                Juego miJuego = new Juego(game.smFile.title, Juego.NIVEL_1, new Date(), puntajeAcumulado,true );
                if( tableLayoutScores.getVisibility() == View.GONE){
                    guardarJuegosHistorico(miJuego);
                }
                tableLayoutScores.setVisibility(View.VISIBLE);
                scrollLayoutTable.setVisibility(View.VISIBLE        );
            }
            if( imgUltimoAcierto != null && endGame ){//&& imgUltimoAcierto != null && (seconds > SEGUNDOS_LIMITE*0.5) ){
                //Log.i(TAG, "****** en guardarImagen seconds "+seconds);
                guardarImagenUltimoACierto();
                imgUltimoAcierto = null;
            }/// tal vez el error del bug de segundo juegos tanga algo que ver con que ya esta corriendo un hilo periodico este hilo desde el primer click
            /// que pasaria si solo lo ejecuto una vez ???
            /// se me ocurre que este hilo periodico solo es necesario en el juego de los topos porque alla el que manda es el contador de tiempo hacia atras, aki
            /// manda es el final de la cancion
            ////////Log.i(TAG, "****** en timer endGame  "+endGame+" inicio juego "+inicioJuego+" gameEngine song time "+game.songPlayer.time+" idjuego este "+game.getIdJuego());
            boolean juegoEnProgreso = (idUltimoMeasure > 0)? true:false;
            //////Log.i(TAG, "****** en timer endGame  idUltimoMeasure "+idUltimoMeasure+" endGame  "+endGame+" inicio juego "+inicioJuego+" enEspera "+enEspera+" thread "+Thread.currentThread().getName()+" id "+Thread.currentThread().getId());
            if( endGame && botonStart != null && !enEspera && !game.songPlayer.isMusicPlaying()){
                botonStart.setText("New Game");
                if( !enEspera ){
                    botonStart.setVisibility(View.VISIBLE  );
                }
                endGame = false;
                inicioJuego = false;
                //enEspera = true;
            } /*else if ( idUltimoMeasure > 0 ) {
                botonStart.setVisibility(View.GONE);
            }*/
            //if ( endGame ){
            //timerHandler.postDelayed(this, 1000);/// como lo que manda es el final de la cancion no es necesario que este hilo se ejecute tan seguido
            //}
        }
    };
    Runnable timerEsperaRunnable = new Runnable() {

        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            inicioJuego = true;/// incioJuego es lo que dispara primeraIntegracion
            enEspera = false;
            inicioEspera = 0;
            tiempoFinEspera = startTime;
            timerHandler.removeCallbacksAndMessages(null);/// remueve todos los hilos relacionados a este manager
            //////Log.i(TAG, "****** desde timerEsperaRunnable ");
            //timerHandler.hasMessages()
            /// espero que esta jugada del removecallbacks remueva el hilo que venia revisando el final del primer juego y con eso solo queda vivo
            /// el que se lanza despues de este comentario
            //if(!existeHiloRevisandoEndGame){
            timerHandler.postDelayed(timerRunnable,0);/// quito y pongo esto para ver si se soluciona con un solo timer
            existeHiloRevisandoEndGame = true;
            //}
            botonStart.setVisibility(View.GONE);
            idJuego++;
            if(packSongDescargado != null && game != null ){
                //game = new GameEngine(contexto, packSongDescargado );
                game.play();
            } else {
                game = new GameEngine(contexto, idJuego );
            }
        }
    };
    Runnable timerUltimoMeasure = new Runnable() {

        @Override
        public void run() {
            endGame = true;//// no me gusto esta solucion pero no veo porque en endgame se termina antes de terminar de pintar las ultimas flechas
            //////Log.i(TAG, "****** desde timerUltimoMeasure ");
            timerHandler.postDelayed(timerRunnable,0);
        }
    };

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    ////////Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    public boolean hasChanges(Mat current) {
        int PIXEL_DIFF_THRESHOLD = 5;
        int IMAGE_DIFF_THRESHOLD = 100;//5;
        //Mat base = new Mat();
        ////////Log.i(TAG, "****** en hasChanges ");
        Mat bg = new Mat();
        Mat cg = new Mat();
        Mat diff = new Mat();
        Mat tdiff = new Mat();
        if (base == null) {
            base = current.clone();
            return false;
        }
        Imgproc.cvtColor(base, bg, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(current, cg, Imgproc.COLOR_BGR2GRAY);
        Core.absdiff(bg, cg, diff);
        Imgproc.threshold(diff, tdiff, PIXEL_DIFF_THRESHOLD, 0.0, Imgproc.THRESH_TOZERO);
        //////////Log.i(TAG, "****** count non zero " +Core.countNonZero(tdiff));
        if (Core.countNonZero(tdiff) <= IMAGE_DIFF_THRESHOLD) {
            return false;
        }
        Imgproc.threshold(diff, diff, PIXEL_DIFF_THRESHOLD, 255, Imgproc.THRESH_BINARY);
        Imgproc.dilate(diff, diff, new Mat());
        Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5,5));
        Imgproc.morphologyEx(diff, diff, Imgproc.MORPH_CLOSE, se);
        List<MatOfPoint> points = new ArrayList<MatOfPoint>();
        Mat contours = new Mat();
        Imgproc.findContours(diff, points, contours, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        int n = 0;
        for (Mat pm: points) {
            //log(lvl, "(%d) %s", n++, pm);
            ////////Log.i(TAG, "****** points " + n+++" "+pm);
            //printMatI(pm);
        }
        //log(lvl, "contours: %s", contours);
        ////////Log.i(TAG, "****** countours " +contours);
        //printMatI(contours);
        //the largest contour is found at the end of the contours vector
        //we will simply assume that the biggest contour is the object we are looking for.
        //vector< vector<Point> > largestContourVec;
        //largestContourVec.push_back(contours.at(contours.size()-1));
        MatOfPoint largestContour = points.get(points.size()-1);
        Point[] cornerpoints = points.get(points.size()-1).toArray();
        //double x = cornerpoints[0].x;
        //double y = ;
        //Core.rectangle(current,);
        // Get bounding rect of contour
        Rect rect = Imgproc.boundingRect(largestContour);

        // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
        rectangle(current, new Point(rect.x,rect.y), new Point(rect.x+rect.width
                ,rect.y+rect.height), new Scalar(255, 0, 0, 255), 3);
        Scalar color =  new Scalar( 255, 255, 255 );
        Imgproc.drawContours( current, points,-1,color,3);
        Imgproc.putText(current, "intento 1",
                new Point(1, 1),
                Core.FONT_HERSHEY_PLAIN, 1.0, new Scalar(255,0,0));
        base = current.clone();
        return true;
    }
   /* public static List<Element> detectChanges(Mat base, Mat mChanged) {
        int PIXEL_DIFF_THRESHOLD = 3;
        int IMAGE_DIFF_THRESHOLD = 5;
        Mat mBaseGray = Element.getNewMat();
        Mat mChangedGray = Element.getNewMat();
        Mat mDiffAbs = Element.getNewMat();
        Mat mDiffTresh = Element.getNewMat();
        Mat mChanges = Element.getNewMat();
        List<Element> rectangles = new ArrayList<>();
        Imgproc.cvtColor(base, mBaseGray, toGray);
        Imgproc.cvtColor(mChanged, mChangedGray, toGray);
        Core.absdiff(mBaseGray, mChangedGray, mDiffAbs);
        Imgproc.threshold(mDiffAbs, mDiffTresh, PIXEL_DIFF_THRESHOLD, 0.0, Imgproc.THRESH_TOZERO);
        if (Core.countNonZero(mDiffTresh) > IMAGE_DIFF_THRESHOLD) {
            Imgproc.threshold(mDiffAbs, mDiffAbs, PIXEL_DIFF_THRESHOLD, 255, Imgproc.THRESH_BINARY);
            Imgproc.dilate(mDiffAbs, mDiffAbs, Element.getNewMat());
            Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
            Imgproc.morphologyEx(mDiffAbs, mDiffAbs, Imgproc.MORPH_CLOSE, se);
            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Mat mHierarchy = Element.getNewMat();
            Imgproc.findContours(mDiffAbs, contours, mHierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
            rectangles = contoursToRectangle(contours);
            Core.subtract(mDiffAbs, mDiffAbs, mChanges);
            Imgproc.drawContours(mChanges, contours, -1, new Scalar(255));
            //logShow(mDiffAbs);
        }
        return rectangles;
    }*/

    public Mat cambiarCurrent(Mat current) {
        int PIXEL_DIFF_THRESHOLD = 5;
        int IMAGE_DIFF_THRESHOLD = 5;//5;//1000
        //Mat base = new Mat();
        ////////Log.i(TAG, "****** en cambiarCurrent version 8.8 ");
        Mat bg = new Mat();
        Mat cg = new Mat();
        Mat diff = new Mat();
        Mat tdiff = new Mat();
        if (base == null && current != null) {
            base = current.clone();
            return current;
        }
        Imgproc.cvtColor(base, bg, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(current, cg, Imgproc.COLOR_BGR2GRAY);

        //Imgproc.cvtColor(frame, firstFrame, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.GaussianBlur(firstFrame, firstFrame, new Size(21, 21), 0);

        Imgproc.GaussianBlur(bg, bg, new Size(21, 21), 0);
        Imgproc.GaussianBlur(cg, cg, new Size(21, 21), 0);
        ///Core.absdiff(bg, cg, diff);////parece que resalta el objeto estatico
        Core.absdiff(cg, bg, diff);
        //Imgproc.threshold(diff, tdiff, PIXEL_DIFF_THRESHOLD, 0.0, Imgproc.THRESH_TOZERO);
        ////////Log.i(TAG, "****** count non zero " + Core.countNonZero(tdiff));
        Imgproc.putText(current, "intento 2",
                new Point(40, 40),
                Core.FONT_HERSHEY_PLAIN, 4.0, new Scalar(255,0,0));
        base = current.clone();
        return diff;
    }

    public Mat hasChanges3(Mat current) {
        int PIXEL_DIFF_THRESHOLD = 5;
        int IMAGE_DIFF_THRESHOLD = 5;//5;//1000
        //Mat base = new Mat();
        String version = "1.2";
        long inicioHasChanges = System.currentTimeMillis();
        //Log.i(TAG, "****** en hasChanges version "+version);
        Mat bg = new Mat();
        Mat cg = new Mat();
        Mat diff = new Mat();
        Mat currentCopy = current.clone();
        if (base == null && current != null) {
            base = current.clone();
            //return false;
            return current;
        }
        Rect roiArriba = new Rect( new Point(0,0),new Point(current.cols()/2,current.rows()/2));
        //Mat currentPart = current.clone().submat(roiArriba);
        //Mat basePart = base.clone().submat(roiArriba);
        Mat currentPart = current.clone();
        Mat basePart = base.clone();
        Imgproc.cvtColor(basePart, bg, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(currentPart, cg, Imgproc.COLOR_BGR2GRAY);

        //Imgproc.cvtColor(frame, firstFrame, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.GaussianBlur(firstFrame, firstFrame, new Size(21, 21), 0);
        double kernelSize = 21;
        Imgproc.GaussianBlur(bg, bg, new Size(kernelSize, kernelSize), 0);
        Imgproc.GaussianBlur(cg, cg, new Size(kernelSize, kernelSize), 0);
        ///Core.absdiff(bg, cg, diff);////parece que resalta el objeto estatico
        Core.absdiff(cg, bg, diff);
        long despuesabsdiff = System.currentTimeMillis();
        Log.i(TAG, "****** demora kernel size  "+kernelSize);
        Log.i(TAG, "****** demora hasta absdiff "+(despuesabsdiff-inicioHasChanges));
        //Imgproc.threshold(diff, tdiff, PIXEL_DIFF_THRESHOLD, 0.0, Imgproc.THRESH_TOZERO);
        ////////Log.i(TAG, "****** count non zero " +Core.countNonZero(diff));
        if (Core.countNonZero(diff) <= IMAGE_DIFF_THRESHOLD) {
            //current = diff;
            Imgproc.putText(current, "intento 8 no dif",
                    new Point(40, 40),
                    Core.FONT_HERSHEY_PLAIN, 5.0, new Scalar(255,0,0));
            //return false;
            return current;//descomentar despues porque puede fallar
            //return diff;
        }
        Imgproc.threshold(diff, diff, PIXEL_DIFF_THRESHOLD, 255, Imgproc.THRESH_BINARY);
        Imgproc.dilate(diff, diff, new Mat());
        //Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5,5));
        //Imgproc.morphologyEx(diff, diff, Imgproc.MORPH_CLOSE, se);
        List<MatOfPoint> points = new ArrayList<MatOfPoint>();
        Mat contours = new Mat();
        long antesFindContours = System.currentTimeMillis();
        Log.i(TAG, "****** demora inicio sin absdiff "+(antesFindContours-despuesabsdiff));
        Log.i(TAG, "****** demora inicio hasta antesFindContours  "+(antesFindContours-inicioHasChanges));
        Imgproc.findContours(diff, points, contours, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        long despuesFindContours = System.currentTimeMillis();
        Log.i(TAG, "****** demora findContours "+(despuesFindContours-antesFindContours));
        int n = 0;
        ////////Log.i(TAG, "****** points size " + points.size()+"  contours size"+contours.size());
        if ( points != null && points.size() > 0 && false ){
            for (Mat pm: points) {
                //log(lvl, "(%d) %s", n++, pm);
                ////////Log.i(TAG, "****** points version 3" + n+++" "+pm);
                //printMatI(pm);
                Mat unContour = pm;
                Rect rect = Imgproc.boundingRect(unContour);


            }
            //log(lvl, "contours: %s", contours);
            ////////Log.i(TAG, "****** countours " +contours);

            MatOfPoint largestContour = points.get(points.size()-1);

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(largestContour);


            Scalar color =  new Scalar( 0, 255, 0 );
            //Imgproc.drawContours( diff, points,-1,color,3);
        }
        long despuesLargContour = System.currentTimeMillis();
        Log.i(TAG, "****** demora LargeBloqueContour "+(despuesLargContour-despuesFindContours));
        Log.i(TAG, "****** demora inicio hasta despuesLargContour "+(despuesLargContour-inicioHasChanges));
        //Imgproc.drawContours( currentCopy, points,-1,new Scalar(0, 255, 0, 255),3); //// descomentar para debug este drawcontours
        points = borrarContoursMenores2( contours, currentCopy, points );
        //Imgproc.drawContours( currentCopy, points,-1,new Scalar(0, 255, 0, 255),3); //// descomentar para debug este drawcontours
        base = current.clone();
        //current = diff;
        flip(diff,diff,1);
        flip(currentCopy,currentCopy,1);/// descomentar para debug estos dos puttexts
        //detectarToqueStepmania(diff);
        //ultimaDeteccion = detectarToqueStepmania(diff);
        long antesDetectarToque = System.currentTimeMillis();
        List<Resultado> lstResultados = detectarToqueStepmania(diff);
        long despuesDetectarToque = System.currentTimeMillis();
        Log.i(TAG, "********  demora detectarToqueStepmani "+(despuesDetectarToque-antesDetectarToque));
        Log.i(TAG, "********  demora inicio hasta despuesDetectarToque "+(despuesDetectarToque-inicioHasChanges));
        Score unScore;
        for( Resultado resultado: lstResultados){
            int segundosRestantes = 3;
            if (lstScoresRecientes == null)lstScoresRecientes = new ArrayList<Score>();
            unScore = new Score(resultado,segundosRestantes);
            lstScoresRecientes.add( unScore );
            if( resultado.getGradoAcierto() != Score.MISS){
                hayUnAcierto = true;
            }
            switch (resultado.getGradoAcierto()){
                case Score.BAD:game.agregarPuntajeGradoAcierto(Score.BAD_SUMAR);break;
                case Score.GOOD:game.agregarPuntajeGradoAcierto(Score.GOOD_SUMAR);break;
                case Score.GREAT:game.agregarPuntajeGradoAcierto(Score.GREAT_SUMAR);break;
                case Score.PERFECT:game.agregarPuntajeGradoAcierto(Score.PERFECT_SUMAR);break;
                case Score.MARVELOUS:game.agregarPuntajeGradoAcierto(Score.MARVELOUS_SUMAR);break;
            }
        }
        if( hayUnAcierto ){
            if( mapaReceptores.get(ESQUINA_DOWN_LEFT).isAcertado()){
                game.inputDown(Direction.LEFT);
            }
            if( mapaReceptores.get(ESQUINA_TOP_LEFT).isAcertado()){
                game.inputDown(Direction.UP);
            }
            if( mapaReceptores.get(ESQUINA_TOP_RIGHT).isAcertado()){
                game.inputDown(Direction.DOWN);
            }
            if( mapaReceptores.get(ESQUINA_DOWN_RIGHT).isAcertado()){
                game.inputDown(Direction.RIGHT);
            }
        }
        long finHasChanges = System.currentTimeMillis();
        Log.i(TAG, "********  demora fin hasChanges "+(finHasChanges-despuesDetectarToque));
        return currentCopy;
        //return diff;
    }

    public Mat hasChangesPartialTest(Mat current) {
        int PIXEL_DIFF_THRESHOLD = 5;
        int IMAGE_DIFF_THRESHOLD = 5;//5;//1000
        //Mat base = new Mat();
        String version = "9.6";
        long inicioHasChanges = System.currentTimeMillis();
        //Log.i(TAG, "****** en hasChanges version "+version);
        Mat bg = new Mat();
        Mat cg = new Mat();
        Mat diff = new Mat();
        Mat currentCopy = current.clone();
        if (base == null && current != null) {
            base = current.clone();
            //return false;
            return current;
        }
        Mat currentPart = current.clone().rowRange(1,current.cols()/4);
        Mat basePart = base.clone().rowRange(1,current.cols()/4);
        Imgproc.cvtColor(base, bg, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(current, cg, Imgproc.COLOR_BGR2GRAY);

        //Imgproc.cvtColor(frame, firstFrame, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.GaussianBlur(firstFrame, firstFrame, new Size(21, 21), 0);

        Imgproc.GaussianBlur(bg, bg, new Size(21, 21), 0);
        Imgproc.GaussianBlur(cg, cg, new Size(21, 21), 0);
        ///Core.absdiff(bg, cg, diff);////parece que resalta el objeto estatico
        Core.absdiff(cg, bg, diff);


        return diff;
    }
    private void primeraIntegracionConStepMania(){
        char note;
        // do game logic
        int notesRestantes = 10;
        int limiteNotesFaciles = 4;
        int fraccionVisibilidadY = 1;/// en la modalidad de esquinas seria 4, para que salga desde el centro
        if( game == null){
            game = new GameEngine(this);
        }
        game.step(Gdx.graphics.getDeltaTime());
        Vector2 arrowPos = new Vector2(0f, 0f);/// esto es de libgdx
        ////////Log.i(TAG, "********2 datos de stepmania measuere size "+game.chart.noteData.measures.size());
        boolean seMostraronFlechas = false;
        saltarNotes = true;
        notasASaltar = 3;
        for (Measure measure : game.chart.noteData.measures) {
            //////////Log.i(TAG, "******** datos de stepmania neasure. notes size "+measure.notes.size()+" id "+measure.id);
            if( true ){ //measure.notes.size() <= limiteNotesFaciles || (measure.id == (game.chart.noteData.measures.size()-1))){
                for(int i = 0; i < measure.notes.size(); i++) {/// intento de poner menos lineas y hacer que sean mas faciles de lograr, pero parece que estan
                    ////muy lejos como y ya se ven muy encima como para prepara una combinacion por adelantado, parece que va tocar dejarlas de abajo arriba como estaban.shit
                    if (saltarNotes && i%notasASaltar > 0){
                        continue;
                    }
                    //////////Log.i(TAG, "******** datos de stepmania  indice i "+i+" mRgba.cols() "+mRgba.cols());
                    arrowPos.y = measure.getBeatForNote(i);
                    arrowPos.y -= game.smFile.timingData.getBeat(game.songPlayer.time + game.smFile.offset);
                    arrowPos.y *= SPEED;//Config.SPEED;
                    measureActual = measure;
                    notesRestantes = measure.notes.size()-i;
                    for(Direction dir : Direction.values()) {
                        arrowPos.x = dir.laneXPos();
                        arrowPos.x= arrowPos.x-0.5f; /// para que de lo mismo que en el de gdx -2,-1,0,1 que fue en lo que me base para las condiciones
                        note = measure.notes.get(i).data[dir.ordinal()];
                        if(note == '1' || note == '2') {
                            double posY =(Math.round( (arrowPos.y)*((mRgba.cols()-0)/Config.DRAW_DISTANCE)));//// por el flip el 0 del x esta a la derecha
                            //// como toca flipear la imagen en x , entonces el 0 esta a la derecha
                            double posX = (mRgba.cols()-((mRgba.cols()/5))*(arrowPos.x+3));/// el +2 es para anular el -2 conel que empiezan las x // 3*(mRgba.rows()*0.4);
                            Point resPos = new Point(posX, posY);/// con 3 sobra uno despues de neutralizar el -2
                            //Point resPos = transformarPosModEsquinas(arrowPos);
                            ////////Log.i(TAG, "********  datos de stepmania file: " + measure.getOrder(i)+" posx "+arrowPos.x+" posy "+posY +" arrowposy "+arrowPos.y);
                            ////////Log.i(TAG, "********  idMeasure " +measure.id+" size notes del measure "+measure.notes.size());
                            if ( posY < mRgba.cols()/1){/// en la pantalla se ve como en si fuera en la mitad pero en realidad como tiene medio lado negativo que no se ve entonces
                                //// esta es la 4 cuarta parte lo que es congrutente con el valor de division
                                //mostrarCircleSegunX(resPos, arrowPos);
                                mostrarFlechaPolArribaSegunX(resPos,arrowPos,i,measure);
                                registrarPuntosRecientes(resPos,measure);
                                //mostrarFlechaSegunX(resPos,arrowPos);
                                seMostraronFlechas = true;
                                if( i == measure.notes.size()-1 ){
                                    measure.setAllNotesMostradas( true );
                                }
                            }
                            //Imgproc.circle(mRgba, new Point(resPos.x, resPos.y ), 20, Scalar.all(255), 2, 8, 0);;
                            //batch.draw(assets.arrow[measure.getOrder(i)], arrowPos.x - 0.5f, arrowPos.y - 0.5f, 0.5f, 0.5f, 1, 1, 1, 1, dir.rotation());
                            ///uiShapes.rectLine(arrowPos.cpy().sub(0.5f, 0f), arrowPos.cpy().add(0.5f, 0f), 0f); ///pinta una linea en libgdx
                        }
                    }
                }
            }
            if(arrowPos.y > Config.DRAW_DISTANCE) continue;
        }
        ///// parece que aunque el ultimo measure ya se haya alcanzado en este punto igual las flechas que se estan mostrando van muy atras todavia
        //////Log.i(TAG, "****** a desde primera integracion idUltimoMeasure "+idUltimoMeasure+" endGame  "+endGame+" contadorNotasUltimoMeasure '"+contadorNotasUltimoMeasure);
        if( idUltimoMeasure >= game.chart.noteData.measures.size()-1 && !flechaMostrandose && !endGame && contadorNotasUltimoMeasure == 0 ){/// perdi 5 horas por no fijarme en esta condicion???
            ////////Log.i(TAG, "****** endgame detectado en base flechas mostradas y size measure restando 3");
            //endGame = true; ///¿¿¿¿¿¿¿¿ pasara algo que mientras el el mostrar flechas lanza el hilo que cambia el endGame a true, este hilo llega primero y vuelve a entrar
            /// ve que el endGame sigue en false y vuelve y desecadena un fin del juego lo que hace que el juego que ya empezo acabe prematuramente ?????
            if( contadorNotasUltimoMeasure == 0){/// como este contador notas solo lo escribe y lo revisa este hilo tengo la esperanza que impida que este bloque se ejecute dos veces seguidas
                contadorNotasUltimoMeasure++;
            }
            int idUltimoMeasure = game.chart.noteData.measures.size()- 1;
            maximoNotasUltimoMeasure = game.chart.noteData.measures.get( idUltimoMeasure ).notes.size();
            //////Log.i(TAG, "****** desde primera integracion timerRunnable thread "+Thread.currentThread().getName()+" "+Thread.currentThread().getId()+
            //" contadorNotasUltimoMeasure "+contadorNotasUltimoMeasure);
            //timerHandler.postDelayed(timerUltimoMeasure, 4000);/// toco con timer de delay porque no se me ocurrio otra solucion
            //timerHandler.postDelayed(timerRunnable, 4000);/// toco con timer de delay porque no se me ocurrio otra solucion
        } /// desde que se muestra un camino mas largo para las flechas las ultimas notas se cortan anticipadamente para mostrar la imagen de precauciones, por eso le subo el delay a 4s
        ////////Log.i(TAG, "****** 4 endgame detectando notesRestantes "+notesRestantes+" idultimomeasure "+idUltimoMeasure);
        //mostrarReceptores();
        /*mostrarReceptoresPoligono();
        mostrarReceptoresPoligonoTocado();
        mostrarReceptoresPoligonoAcertado();*/
        //mostrarScore();
    }
    private void registrarPuntosRecientes( Point resPos, Measure measure ){
        //////////Log.i(TAG, "****** measure id en registrar "+measure.id+" size notes "+measure.notes.size()+" isAllNotesMostradas "+measure.isAllNotesMostradas());
        if (lstPuntosRecientes == null ){
            lstPuntosRecientes = new ArrayList<PointMeasure>();
        }
        if ( false ){//measure.isAllNotesMostradas()){//idUltimoMeasure != measure.id && lstPuntosRecientes != null ){
            lstPuntosRecientes.clear();
        }
        //mostrarListaPuntos();
        //borrarPuntosYNegativosDeLista();
        if ( maxPosYFlecha > 0 && resPos.y < maxPosYFlecha && resPos.y >= 0 ){//// esto aunque no resuelve el problema de la deteccion de flechas, reduce el size de la lista puntos para comparar
            PointMeasure pointMeasure = new PointMeasure(resPos,measure);//// y hace mas facil la revision del log visualmente
            lstPuntosRecientes.add(pointMeasure);
        }
        //idUltimoMeasure = measure.id;
    }
    private void mostrarListaPuntos(){
        String strPuntos = "";
        for(PointMeasure elPunto : lstPuntosRecientes ){
            strPuntos+= " "+elPunto.point.y+" ";
        }
        //////////Log.i(TAG, "****** lstPuntosRecientes "+strPuntos);
    }
    private void borrarPuntosYNegativosDeLista(){
        for(PointMeasure elPunto : lstPuntosRecientes ){
            if( elPunto.measure.id < (measureActual.id - 3) ){//elPunto.point.y < 0 ){
                lstPuntosRecientes.remove(elPunto);
            }
        }
    }
    private void borrarPuntosMeasuresViejos(){
        int toleracia = 4;
        ///si funciona es gracias a esto https://stackoverflow.com/questions/223918/iterating-through-a-collection-avoiding-concurrentmodificationexception-when-re
        for (Iterator<PointMeasure> iterator = lstPuntosRecientes.iterator(); iterator.hasNext();) {
            PointMeasure elPunto  = iterator.next();
            if( elPunto.measure.id < (idUltimoMeasure - toleracia) ){//elPunto.point.y < 0 ){
                //lstPuntosRecientes.remove(elPunto);
                iterator.remove();
            }
        }
    }
    private void mostrarCircleSegunX( Point resPos, Vector2 vector){
        //// verde y rojo los esta mostrando para la derecha osea -2 y -1
        Scalar color = new Scalar(255,255,255);
        switch ( (int)vector.x){
            case-2:
                color = new Scalar(255,0,0);
                break;
            case -1:
                color = new Scalar(0,255,0);
                break;
            case 0:
                color = new Scalar(0,0,255);
                break;
            case 1:
                color = new Scalar(255,255,0);
                break;
        }
        if( copiaRgba != null){
            Imgproc.circle(mRgba, resPos, 20, color, 2, 8, 0);
        }
    }
    private void mostrarFlechaSegunX( Point resPos, Vector2 vector){
        //// verde y rojo los esta mostrando para la derecha osea -2 y -1
        //Imgproc.circle(mRgba, new Point(resPos.x, resPos.y ), 20, color, 2, 8, 0);
        int offsetTopLeft = 0;
        int offsetTopRight = 0;
        int offsetDownLeft = 0;
        int offsetDownRight = 0;
        if( lstArrows != null ){
            int grados = 0;
            Flecha unaFlecha = lstArrows.get(0);
            Mat imgArrow = unaFlecha.getImagen();
            Mat imgResize = new Mat();
            resize(imgArrow.clone(), imgResize,new Size(100,100));
            switch ( (int)vector.x){
                case-2:
                    grados = -135;///red arriba derecha
                    break;
                case -1:
                    grados = -45;///green abajo derecha
                    break;
                case 0:
                    grados = 135;/// blue arriba izquierda
                    break;
                case 1:
                    grados = 45;//// yellow abajo izquierda
                    break;
            }
            resPos.x = resPos.x-imgArrow.width()/2;
            resPos.y = resPos.y-imgArrow.height()/2;
            Point pT1 = new Point(0,0);
            Point pt2 = new Point(resPos.x+imgResize.cols(),resPos.y+imgResize.rows());
            Point pt3 = new Point(100,100);
            Rect roi =  new Rect( resPos,pt2);//Rect(cx,cy,imgArrow.cols(),imgArrow.rows());
            ////////Log.i(TAG, "******* roi flechas a pintar roi x"+roi.x+" roi y "+roi.y+" width "+roi.width+" height "+roi.height+" maxrows "+mRgba.rows()+" maxcols "+mRgba.cols());
            rectangle(mRgba,resPos,pt2,Scalar.all(255),2);
            Rect roiScreen = new Rect( new Point(0,0),mRgba.size());
            if ( roiScreen.contains(resPos) && roiScreen.contains(pt2) ){
                Mat rot_mat = getRotationMatrix2D (new Point(imgResize.cols()/2,imgResize.rows()/2), grados, 1.0);
                Mat dst = new Mat();
                warpAffine (imgResize, dst, rot_mat, imgResize.size());
                Mat submat= mRgba.submat( roi );
                dst.copyTo(submat);
            }
            /*if ( roi.y > 0 ){
                Mat submat= mRgba.submat( roi );
                imgResize.copyTo(submat);
            }*/
        }
    }
    private void mostrarFlechaPoligonoSegunX( Point resPos, Vector2 vector, int indice , Measure measure ){
        List lstPtsFlecha = new ArrayList();
        double maxRight = resPos.x;
        double maxDown = resPos.y;
        int grados;
        flechaMostrandose = true;
        Scalar color = Scalar.all(255);
        if( lstColoresFlechas != null ){
            color = lstColoresFlechas.get(measure.getOrder(indice));
        }
        switch ( (int)vector.x){
            case-2:
                grados = -135;///red arriba derecha
                lstPtsFlecha.add(
                        new MatOfPoint (
                                new Point(maxRight, maxDown),
                                new Point(maxRight+100, maxDown),
                                new Point(maxRight+ 71,maxDown+ 28),
                                new Point(maxRight +92,maxDown + 48),
                                new Point(maxRight +51,maxDown+ 92),
                                new Point(maxRight+29,maxDown+ 70),
                                new Point(maxRight,maxDown + 100),
                                new Point(maxRight,maxDown)

                                //new Point(), new Point(350, 200),
                                //new Point(75, 250), new Point(350, 250)
                        )
                );

                break;
            case -1:
                grados = -45;///green abajo derecha
                lstPtsFlecha.add(
                        new MatOfPoint (
                                new Point(maxRight, maxDown),
                                new Point(maxRight+100, maxDown),
                                new Point(maxRight+ 71,maxDown- 28),
                                new Point(maxRight +92,maxDown - 48),
                                new Point(maxRight +51,maxDown- 92),
                                new Point(maxRight+29,maxDown- 70),
                                new Point(maxRight,maxDown - 100),
                                new Point(maxRight,maxDown)

                                //new Point(), new Point(350, 200),
                                //new Point(75, 250), new Point(350, 250)
                        )
                );
                break;
            case 0:
                grados = 135;/// blue arriba izquierda
                lstPtsFlecha.add(
                        new MatOfPoint (
                                new Point(maxRight, maxDown),
                                new Point(maxRight-100, maxDown),
                                new Point(maxRight- 71,maxDown+ 28),
                                new Point(maxRight -92,maxDown + 48),
                                new Point(maxRight -51,maxDown+ 92),
                                new Point(maxRight-29,maxDown+ 70),
                                new Point(maxRight,maxDown + 100),
                                new Point(maxRight,maxDown)

                                //new Point(), new Point(350, 200),
                                //new Point(75, 250), new Point(350, 250)
                        )
                );
                break;
            case 1:
                grados = 45;//// yellow abajo izquierda
                lstPtsFlecha.add(
                        new MatOfPoint (
                                new Point(maxRight, maxDown),/// vuelve a este punto en este caso abajo del cuadro marco
                                new Point(maxRight-100, maxDown),
                                new Point(maxRight- 71,maxDown- 28),
                                new Point(maxRight -92,maxDown - 48),
                                new Point(maxRight -51,maxDown- 92),
                                new Point(maxRight-29,maxDown- 70),
                                new Point(maxRight,maxDown - 100),
                                new Point(maxRight,maxDown)

                                //new Point(), new Point(350, 200),
                                //new Point(75, 250), new Point(350, 250)
                        )
                );
                break;
        }

        // Drawing polylines
        Imgproc.fillPoly (
                mRgba,                    // Matrix obj of the image
                lstPtsFlecha,                    // isClosed0
                color,    // Scalar object for color
                LINE_8                          // Thickness of the line
        );
        flechaMostrandose = false;
    }
    private void mostrarFlechaPolArribaSegunX( Point resPos, Vector2 vector, int indice , Measure measure ){
        List<MatOfPoint> lstPtsFlecha = new ArrayList();
        double maxRight = resPos.x;
        double maxDown = resPos.y;
        int grados;
        flechaMostrandose = true;
        Scalar color = Scalar.all(255);
        if( lstColoresFlechas != null ){
            //color = lstColoresFlechas.get(measure.getOrder(indice));
            double div5 = mRgba.cols()/5;
            double vecesDiv5 =  resPos.x/(div5);
            ////////Log.i(TAG, "****** color para flechas arriba "+vecesDiv5);
            switch ( (int)vecesDiv5  ){
                case 1 : color = COLOR_YELLOW;break;
                case 2 : color = COLOR_GREEN;break;
                case 3 : color = COLOR_BLUE;break;
                case 4 : color = COLOR_RED;break;
                default: color = COLOR_WHITE;
            }
        }
        //int[] arrX = {50,36,47,28,17};
        //int[] arrY = {21,32,51,41,51};/// el max de y es 75
        int[] arrX = {50,100,75,75,27,27,0,50};
        int[] arrY = {50,50,75,75,50,50};
        maxPosYFlecha = 75;
        /*for(int i = 0; i < arrY.length ;i++){
            arrY[i] = arrY[i]+30;
        }*/

        lstPtsFlecha.add(/// para este caso que es como stepmania clasico pero tirando mas guitar hero todas las flechas apuntan hacia arriba
                new MatOfPoint (
                        new Point(maxRight+arrX[0], maxDown),/// la punta superior de la flecha
                        new Point(maxRight+arrX[1], maxDown+arrY[0])    ,
                        new Point(maxRight+ arrX[2],maxDown+ arrY[1]),
                        new Point(maxRight +arrX[3],maxDown + arrY[2]),
                        new Point(maxRight +arrX[4],maxDown+ arrY[3]),
                        new Point(maxRight+arrX[5],maxDown+ arrY[4]),
                        new Point(maxRight+arrX[6],maxDown + arrY[5]),
                        new Point(maxRight+arrX[7],maxDown)
                )
        );

        // Drawing polylines
        Imgproc.fillPoly (
                mRgba,                    // Matrix obj of the image
                lstPtsFlecha,                    // isClosed0
                color,    // Scalar measureActualobject for color
                LINE_8                          // Thickness of the line
        );
        /*Imgproc.putText(mRgba, ""+measure.id+"-"+idUltimoMeasure,
                lstPtsFlecha.get(0).toList().get(0),
                face[3], 1, color);*/
        /*Imgproc.putText(mRgba, " y "+lstPtsFlecha.get(0).toList().get(0).y,
                lstPtsFlecha.get(0).toList().get(0),
                face[3], 1, color);*/
        flechaMostrandose = false;
        if ( measure.id > idUltimoMeasure ){
            idUltimoMeasure = measure.id;
        }
        //// cada note tiene  un alcance como 287 en y que se saca de measure.getBeatForNote(indice)
        if( idUltimoMeasure >= game.chart.noteData.measures.size()- 2){
            //////Log.d(TAG, "****** a contadorNotasUltimoMeasure "+contadorNotasUltimoMeasure+" maximoNotasUltimoMeasure "+maximoNotasUltimoMeasure+
            //"  measure.getBeatForNote(i)  "+ measure.getBeatForNote(indice));
            if( contadorNotasUltimoMeasure > 0 ){
                contadorNotasUltimoMeasure++;
            }
            if( verificarBeatsEnMaximo(measure) && maximoNotasUltimoMeasure > 0 ){
                //endGame = true;
                long delay = ((2-SPEED)+1)*4000;
                //////Log.d(TAG, "****** delay "+delay);
                timerHandler.postDelayed(timerUltimoMeasure, delay );
            }
        }
    }

    private boolean verificarBeatsEnMaximo( Measure measure ){
        boolean todosEnMaximo = false;
        int maximo = 270;
        int contadorMaximos = 0;
        for( int i =0; i < maximoNotasUltimoMeasure; i++ ){
            if(measure.getBeatForNote(i) > maximo  ) {
                contadorMaximos++;
            }
        }
        if( contadorMaximos == maximoNotasUltimoMeasure){
            todosEnMaximo = true;
        }
        return todosEnMaximo;
    }
    private void mostrarFlechaPolSize2SegunX( Point resPos, Vector2 vector, int indice , Measure measure ){
        List lstPtsFlecha = new ArrayList();
        double maxRight = resPos.x;
        double maxDown = resPos.y;
        int grados;
        flechaMostrandose = true;
        Scalar color = Scalar.all(255);
        if( lstColoresFlechas != null ){
            color = lstColoresFlechas.get(measure.getOrder(indice));
        }
        int[] arrX = {50,36,47,28,17};
        int[] arrY = {21,32,51,41,51};
        switch ( (int)vector.x){
            case-2:
                grados = -135;///red arriba derecha
                lstPtsFlecha.add(
                        new MatOfPoint (
                                new Point(maxRight, maxDown),
                                new Point(maxRight+arrX[0], maxDown),
                                new Point(maxRight+ arrX[1],maxDown+ arrY[0]),
                                new Point(maxRight +arrX[2],maxDown + arrY[1]),
                                new Point(maxRight +arrX[3],maxDown+ arrY[2]),
                                new Point(maxRight+arrX[4],maxDown+ arrY[3]),
                                new Point(maxRight,maxDown + arrY[4]),
                                new Point(maxRight,maxDown)

                                //new Point(), new Point(350, 200),
                                //new Point(75, 250), new Point(350, 250)
                        )
                );

                break;
            case -1:
                grados = -45;///green abajo derecha
                lstPtsFlecha.add(
                        new MatOfPoint (
                                new Point(maxRight, maxDown),
                                new Point(maxRight+arrX[0], maxDown),
                                new Point(maxRight+ arrX[1],maxDown- arrY[0]),
                                new Point(maxRight +arrX[2],maxDown - arrY[1]),
                                new Point(maxRight +arrX[3],maxDown- arrY[2]),
                                new Point(maxRight+arrX[4],maxDown- arrY[3]),
                                new Point(maxRight,maxDown - arrY[4]),
                                new Point(maxRight,maxDown)

                                //new Point(), new Point(350, 200),
                                //new Point(75, 250), new Point(350, 250)
                        )
                );
                break;
            case 0:
                grados = 135;/// blue arriba izquierda
                lstPtsFlecha.add(
                        new MatOfPoint (
                                new Point(maxRight, maxDown),
                                new Point(maxRight-arrX[0], maxDown),
                                new Point(maxRight- arrX[1],maxDown+ arrY[0]),
                                new Point(maxRight -arrX[2],maxDown + arrY[1]),
                                new Point(maxRight -arrX[3],maxDown+ arrY[2]),
                                new Point(maxRight-arrX[4],maxDown+ arrY[3]),
                                new Point(maxRight,maxDown + arrY[4]),
                                new Point(maxRight,maxDown)

                                //new Point(), new Point(350, 200),
                                //new Point(75, 250), new Point(350, 250)
                        )
                );
                break;
            case 1:
                grados = 45;//// yellow abajo izquierda
                lstPtsFlecha.add(
                        new MatOfPoint (
                                new Point(maxRight, maxDown),/// vuelve a este punto en este caso abajo del cuadro marco
                                new Point(maxRight-arrX[0], maxDown),
                                new Point(maxRight- arrX[1],maxDown- arrY[0]),
                                new Point(maxRight -arrX[2],maxDown -arrY[1]),
                                new Point(maxRight -arrX[3],maxDown- arrY[2]),
                                new Point(maxRight-arrX[4],maxDown- arrY[3]),
                                new Point(maxRight,maxDown - arrY[4]),
                                new Point(maxRight,maxDown)

                                //new Point(), new Point(350, 200),
                                //new Point(75, 250), new Point(350, 250)
                        )
                );
                break;
        }

        // Drawing polylines
        Imgproc.fillPoly (
                mRgba,                    // Matrix obj of the image
                lstPtsFlecha,                    // isClosed0
                color,    // Scalar object for color
                LINE_8                          // Thickness of the line
        );
        flechaMostrandose = false;
    }
    private void mostrarBodyOutline(){
        int resizeX = (int)(mRgba.cols()*0.5);
        int resizeY = (int)(mRgba.rows()*0.6);
        int posx =  0;
        double inicioTextoX = 0.1;
        double sizeFont =1.4;
        double offsetX = 5;
        int posy =  (int)Math.round(mRgba.rows()*0.3);
        Mat img = null;
        if( !inicioJuego ){
            try {
                //img = Utils.loadResource(this, R.drawable.miss, CvType.CV_8UC4); para prueba error que sale solo en release
                img = Utils.loadResource(this, R.drawable.fondoblanco4, CvType.CV_8UC4);
                //Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);
                Mat imgMask = Utils.loadResource(this, R.drawable.imgsiluetamedia, CvType.CV_8UC4);
                Mat imgResize = new Mat();  /// osea que paila no se puede usar assets, solo se puede usar drawable
                Mat imgResizeMask = new Mat();
                resize(img.clone(), imgResize,new Size(resizeX,resizeY));
                resize(imgMask.clone(), imgResizeMask,new Size(resizeX,resizeY ));
                posx =  (int)Math.round(mRgba.cols()*0.5)-(imgResize.cols()/2);
                Rect roi = new Rect(posx,posy, imgResize.cols(),imgResize.rows());
                Log.i(TAG, "****** 5 mostrarBodyOutline size "+imgResize.size());
                if ( img != null){
                    Log.i(TAG, "****** 3 img not null ");
                }
                if ( imgMask != null){
                    Log.i(TAG, "****** 3 imgMask not null ");
                }
                Mat subMat = mRgba.submat(roi);
                imgResize.copyTo(subMat,imgResizeMask);
                //imgResize.copyTo(subMat);
                Imgproc.putText(mRgba, ""+getString(R.string.ubicacion),
                        new Point(mRgba.width()*inicioTextoX,mRgba.height()*0.2),
                        face[3], sizeFont, new Scalar(0,255,0),3,1);
                Imgproc.putText(mRgba, ""+getString(R.string.ubicacion2),
                        new Point(mRgba.width()*inicioTextoX,mRgba.height()*0.3),
                        face[3], sizeFont, new Scalar(0,255,0),3,1);
                Imgproc.putText(mRgba, ""+getString(R.string.precauciones1),
                        new Point(mRgba.width()*inicioTextoX,mRgba.height()*0.4),
                        face[3], sizeFont, new Scalar(0,255,0),3,1);
                Imgproc.putText(mRgba, ""+getString(R.string.precauciones2),
                        new Point(mRgba.width()*inicioTextoX,mRgba.height()*0.5),
                        face[3], sizeFont, new Scalar(0,255,0),3,1);
                Imgproc.putText(mRgba, ""+getString(R.string.precauciones3),
                        new Point(mRgba.width()*inicioTextoX,mRgba.height()*0.6),
                        face[3], sizeFont, new Scalar(0,255,0),3,1);
                //return copyScreen;
            } catch (IOException e) {
                e.printStackTrace();
            } catch ( Exception ex){
                ex.printStackTrace();
            }
        }
    }
    private void mostrarReceptoresPoligonoEsquinas ( ){
        //// verde y rojo los esta mostrando para la derecha osea -2 y -1
        //Imgproc.circle(mRgba, new Point(resPos.x, resPos.y ), 20, color, 2, 8, 0);
        if ( listTopRight == null ){
            maxRight = mRgba.cols();
            maxDown = mRgba.rows();
            listTopRight = new ArrayList();
            listTopRight.add(
                    new MatOfPoint (
                            new Point(0, 0),
                            new Point(100, 0),
                            new Point(71,28),
                            new Point(92,48),
                            new Point(51,92),
                            new Point(29,70),
                            new Point(0,100),
                            new Point(0,0)

                            //new Point(), new Point(350, 200),
                            //new Point(75, 250), new Point(350, 250)
                    )
            );
            listTopLeft = new ArrayList();
            listTopLeft.add(
                    new MatOfPoint (
                            new Point(maxRight, 0),
                            new Point(maxRight-100, 0),
                            new Point(maxRight-71,28),
                            new Point(maxRight- 92,48),
                            new Point(maxRight-51,92),
                            new Point(maxRight-29,70),
                            new Point(maxRight,100),
                            new Point(maxRight,0)

                            //new Point(), new Point(350, 200),
                            //new Point(75, 250), new Point(350, 250)
                    )
            );
            listDownRight = new ArrayList();
            listDownRight.add(
                    new MatOfPoint (
                            new Point(0, maxDown),
                            new Point(100, maxDown),
                            new Point(71,maxDown- 28),
                            new Point(92,maxDown - 48),
                            new Point(51,maxDown- 92),
                            new Point(29,maxDown- 70),
                            new Point(0,maxDown - 100),
                            new Point(0,maxDown)

                            //new Point(), new Point(350, 200),
                            //new Point(75, 250), new Point(350, 250)
                    )
            );
            listDownLeft = new ArrayList();
            listDownLeft.add(
                    new MatOfPoint (
                            new Point(maxRight, maxDown),
                            new Point(maxRight-100, maxDown),
                            new Point(maxRight- 71,maxDown- 28),
                            new Point(maxRight -92,maxDown - 48),
                            new Point(maxRight -51,maxDown- 92),
                            new Point(maxRight-29,maxDown- 70),
                            new Point(maxRight,maxDown - 100),
                            new Point(maxRight,maxDown)

                            //new Point(), new Point(350, 200),
                            //new Point(75, 250), new Point(350, 250)
                    )
            );
            ///red arriba derecha
            ///green abajo derecha
            /// blue arriba izquierda
            /// yellow abajo izquierda
            mapaReceptores = new HashMap<Integer, Receptor>();
            mapaReceptores.put(ESQUINA_TOP_LEFT, new Receptor(listTopLeft,ESQUINA_TOP_LEFT,false,COLOR_BLUE));
            mapaReceptores.put(ESQUINA_TOP_RIGHT,  new Receptor(listTopRight,ESQUINA_TOP_RIGHT,false,COLOR_RED));
            mapaReceptores.put(ESQUINA_DOWN_LEFT,  new Receptor(listDownLeft,ESQUINA_DOWN_LEFT, false,COLOR_YELLOW ));
            mapaReceptores.put(ESQUINA_DOWN_RIGHT,  new Receptor(listDownRight,ESQUINA_DOWN_RIGHT,false, COLOR_GREEN));

            lstColoresFlechas = new ArrayList<>();
            lstColoresFlechas.add(new Scalar(206,63,45));///orden rojo azul verde amarillo violeta verde claro rojo oscuro otro verde
            lstColoresFlechas.add(new Scalar( 2 , 113,223));///
            lstColoresFlechas.add(new Scalar( 77, 239, 2));///
            lstColoresFlechas.add(new Scalar(  238 , 213 , 26));///,
            lstColoresFlechas.add(new Scalar(   113 , 14 , 236));///
            lstColoresFlechas.add(new Scalar( 0 , 224 , 116));///
            lstColoresFlechas.add(new Scalar(  242 , 7 , 101));///
            lstColoresFlechas.add(new Scalar(  103 , 138 , 80));///
        }

        // Drawing polylines
        Imgproc.polylines (
                mRgba,                    // Matrix obj of the image
                listTopRight,true,                    // isClosed0
                new Scalar(255, 255, 255) ,    // Scalar object for color
                LINE_8                          // Thickness of the line
        );
        // Drawing polylines
        Imgproc.polylines (
                mRgba,                    // Matrix obj of the image
                listTopLeft,true,                    // isClosed0
                new Scalar(255, 255, 255) ,    // Scalar object for color
                LINE_8                          // Thickness of the line
        );
        // Drawing polylines
        Imgproc.polylines (
                mRgba,                    // Matrix obj of the image
                listDownRight,true,                    // isClosed0
                new Scalar(255, 255, 255) ,    // Scalar object for color
                LINE_8                          // Thickness of the line
        );
        // Drawing polylines
        Imgproc.polylines (
                mRgba,                    // Matrix obj of the image
                listDownLeft,true,                    // isClosed0
                new Scalar(255, 255, 255) ,    // Scalar object for color
                LINE_8                          // Thickness of the line
        );
        mostrarBoundsReceptores();
        ///// arriba derecha
        Rect roi = new Rect(0,0,100,100);
        Mat submatCopiarOrigen = mRgba.submat(roi);
        /// arriba izquierda

    }
    private void mostrarReceptoresPoligono ( ){
        //// verde y rojo los esta mostrando para la derecha osea -2 y -1
        //Imgproc.circle(mRgba, new Point(resPos.x, resPos.y ), 20, color, 2, 8, 0);
        int[] arrX = {50,100,75,75,27,27,0,50};
        int[] arrY = {50,50,75,75,50,50};
        Vector2 arrowPos = new Vector2(0f, 0f);/// esto es de libgdx pero modificado
        double posX;
        List<List> listaReceptorPuntos = new ArrayList<List>();
        for(Direction dir : Direction.values()) {
            arrowPos.x = dir.laneXPos();
            arrowPos.x = arrowPos.x - 0.5f;
            posX = (mRgba.cols() - ((mRgba.cols() / 5)) * (arrowPos.x + 3));/// el +2 es para anular el -2 conel que empiezan las x // 3*(mRgba.rows()*0.4);
            maxRight = mRgba.cols();
            maxDown = mRgba.rows();
            List lstReceptor = new ArrayList();
            lstReceptor.add(
                    new MatOfPoint (
                            new Point(posX+arrX[0], 0),
                            new Point(posX+arrX[1], arrY[0]),
                            new Point(posX+arrX[2],arrY[1]),
                            new Point(posX+arrX[3],arrY[2]),
                            new Point(posX+arrX[4],arrY[3]),
                            new Point(posX+arrX[5],arrY[4]),
                            new Point(posX+arrX[6],arrY[5]),
                            new Point(posX+arrX[7],0)

                            //new Point(), new Point(350, 200),
                            //new Point(75, 250), new Point(350, 250)
                    )
            );
            listaReceptorPuntos.add(lstReceptor);
        }
        if ( listTopRight == null ){
            listTopRight = listaReceptorPuntos.get(0);
            listTopLeft = listaReceptorPuntos.get(1);
            listDownRight = listaReceptorPuntos.get(2);
            listDownLeft = listaReceptorPuntos.get(3);
        }

        ///red arriba derecha
        ///green abajo derecha
        /// blue arriba izquierda
        /// yellow abajo izquierda
        if( mapaReceptores == null ){
            mapaReceptores = new HashMap<Integer, Receptor>();
            mapaReceptores.put(ESQUINA_TOP_LEFT, new Receptor(listTopLeft,ESQUINA_TOP_LEFT,false,COLOR_BLUE));
            mapaReceptores.put(ESQUINA_TOP_RIGHT,  new Receptor(listTopRight,ESQUINA_TOP_RIGHT,false,COLOR_RED));
            mapaReceptores.put(ESQUINA_DOWN_LEFT,  new Receptor(listDownLeft,ESQUINA_DOWN_LEFT, false,COLOR_YELLOW ));
            mapaReceptores.put(ESQUINA_DOWN_RIGHT,  new Receptor(listDownRight,ESQUINA_DOWN_RIGHT,false, COLOR_GREEN));
        }
        if ( lstColoresFlechas == null ){
            lstColoresFlechas = new ArrayList<>();/// set tocado por defecto era false y aki siempre se volvia a crear otra vez
            lstColoresFlechas.add(new Scalar(206,63,45));///orden rojo azul verde amarillo violeta verde claro rojo oscuro otro verde
            lstColoresFlechas.add(new Scalar( 2 , 113,223));///
            lstColoresFlechas.add(new Scalar( 77, 239, 2));///
            lstColoresFlechas.add(new Scalar(  238 , 213 , 26));///,
            lstColoresFlechas.add(new Scalar(   113 , 14 , 236));///
            lstColoresFlechas.add(new Scalar( 0 , 224 , 116));///
            lstColoresFlechas.add(new Scalar(  242 , 7 , 101));///
            lstColoresFlechas.add(new Scalar(  103 , 138 , 80));///
        }

        // Drawing polylines
        Imgproc.polylines (
                mRgba,                    // Matrix obj of the image
                listTopRight,true,                    // isClosed0
                mapaReceptores.get(ESQUINA_TOP_RIGHT).getColor() ,    // Scalar object for color
                LINE_8                          // Thickness of the line
        );
        // Drawing polylines
        Imgproc.polylines (
                mRgba,                    // Matrix obj of the image
                listTopLeft,true,                    // isClosed0
                mapaReceptores.get(ESQUINA_TOP_LEFT).getColor() ,    // Scalar object for color
                LINE_8                          // Thickness of the line
        );
        // Drawing polylines
        Imgproc.polylines (
                mRgba,                    // Matrix obj of the image
                listDownRight,true,                    // isClosed0
                mapaReceptores.get(ESQUINA_DOWN_RIGHT).getColor() ,    // Scalar object for color
                LINE_8                          // Thickness of the line
        );
        // Drawing polylines
        Imgproc.polylines (
                mRgba,                    // Matrix obj of the image
                listDownLeft,true,                    // isClosed0
                mapaReceptores.get(ESQUINA_DOWN_LEFT).getColor() ,    // Scalar object for color
                LINE_8                          // Thickness of the line
        );
        //mostrarBoundsReceptores();
        ///// arriba derecha
        Rect roi = new Rect(0,0,100,100);
        Mat submatCopiarOrigen = mRgba.submat(roi);
        /// arriba izquierda

    }
    private void mostrarReceptoresPoligonoTocado ( ){
        //// verde y rojo los esta mostrando para la derecha osea -2 y -1
        //Imgproc.circle(mRgba, new Point(resPos.x, resPos.y ), 20, color, 2, 8, 0);
        if (listTopRight != null){
            //////////Log.i(TAG, "******** listTopRight != null ");
            // Drawing polylines
            for (Map.Entry<Integer, Receptor> entry : mapaReceptores.entrySet()) {
                Receptor unReceptor = entry.getValue();
                //////////Log.i(TAG, "******** recorriendo mapreceptores en mostrarpoligonotocado istocado "+unReceptor.isTocado());
                if( unReceptor.isTocado() && !unReceptor.isAcertado() ){
                    Imgproc.fillPoly (
                            mRgba,                    // Matrix obj of the image
                            unReceptor.getListaPoligono(),
                            // isClosed0
                            new Scalar(255, 255, 255) ,    // Scalar object for color
                            LINE_8                          // Thickness of the line
                    );
                    unReceptor.setTocado(false);//// no estoy seguro si este sea el mejor sitio para devolverlo a false
                }
            }
        }
    }
    private void mostrarReceptoresPoligonoAcertado ( ){
        //// verde y rojo los esta mostrando para la derecha osea -2 y -1
        //Imgproc.circle(mRgba, new Point(resPos.x, resPos.y ), 20, color, 2, 8, 0);
        if (listTopRight != null){
            // Drawing polylines
            for (Map.Entry<Integer, Receptor> entry : mapaReceptores.entrySet()) {
                Receptor unReceptor = entry.getValue();
                if( unReceptor.isAcertado() ){
                    List<MatOfPoint> mopAumentado = flechaAumentada(unReceptor.getListaPoligono());
                    Imgproc.fillPoly (
                            mRgba,                    // Matrix obj of the image
                            mopAumentado,
                            // isClosed0
                            unReceptor.getColor() ,    // Scalar object for color
                            LINE_8                          // Thickness of the line
                    );
                    unReceptor.setAcertado(false);//// no estoy seguro si este sea el mejor sitio para devolverlo a false
                }
            }
        }
    }
    private void mostrarBoundsReceptores(){
        Rect roiScreen = new Rect(0,0,mRgba.cols(),mRgba.rows());
        /////////////////// bounding top left
        MatOfPoint mop = mapaReceptores.get(ESQUINA_TOP_LEFT).getListaPoligono().get(0);
        Rect r = boundingRect(mop);
        double xMovido = r.tl().x+5;
        double yMovido = r.tl().y+5;
        r = new Rect( new Point(xMovido,yMovido),new Point(r.br().x,r.br().y));
        if ( roiScreen.contains(r.tl()) && roiScreen.contains(r.br())){
            Imgproc.rectangle(mRgba, r.tl(), r.br(), new Scalar(255,0,0), 2);
        }
        ///////////////////// bounding top right
        mop = mapaReceptores.get(ESQUINA_TOP_RIGHT).getListaPoligono().get(0);
        r = Imgproc.boundingRect(mop);
        xMovido = r.tl().x+5;yMovido = r.tl().y+5;
        r = new Rect( new Point(xMovido,yMovido),new Point(r.br().x,r.br().y));
        if ( roiScreen.contains(r.tl()) && roiScreen.contains(r.br())) {
            Imgproc.rectangle(mRgba, r.tl(), r.br(), new Scalar(0,255,0), 2);
        }////////////////////////////////////////// down let
        mop = mapaReceptores.get(ESQUINA_DOWN_LEFT).getListaPoligono().get(0);
        r = Imgproc.boundingRect(mop);
        xMovido = r.tl().x+5;yMovido = r.tl().y+5;
        r = new Rect( new Point(xMovido,yMovido),new Point(r.br().x,r.br().y));
        if ( roiScreen.contains(r.tl()) && roiScreen.contains(r.br())) {
            Imgproc.rectangle(mRgba, r.tl(), r.br(), new Scalar(0.50,255), 2);
        }/////////////////////////////////down right
        mop = mapaReceptores.get(ESQUINA_DOWN_RIGHT).getListaPoligono().get(0);
        r = Imgproc.boundingRect(mop);
        xMovido = r.tl().x+5;yMovido = r.tl().y+5;
        r = new Rect( new Point(xMovido,yMovido),new Point(r.br().x,r.br().y));
        if ( roiScreen.contains(r.tl()) && roiScreen.contains(r.br())) {
            Imgproc.rectangle(mRgba, r.tl(), r.br(), new Scalar(100,100,50), 2);
        }
    }
    private void mostrarReceptores(){
        try {
            Mat imgArrow = null;
            if( mapFlechas == null ){
                mapFlechas = new HashMap<Integer, Flecha>();
                imgArrow = Utils.loadResource(this, R.drawable.arrowtopleft, CvType.CV_8UC4);
                ///joder ojala esto no traiga problemas despues todoo quedo de derecha a izquierda por un tema de flip
                Rect roi = new Rect(mRgba.cols()-imgArrow.rows(),0,imgArrow.cols(),imgArrow.rows());
                Mat submat= mRgba.submat(roi);
                Mat imgResize = new Mat();
                resize(imgArrow.clone(), imgResize,new Size(100,100));
                Mat resizeimageMask = Mat.ones( new Size(submat.rows(),submat.cols()), CV_8UC1); //;
                //flip(imgArrowArribaDerecha,imgArrowArribaDerecha,1);
                /////////arriba izquierda
                Mat rot_mat = getRotationMatrix2D (new Point(imgResize.cols()/2,imgResize.rows()/2), -90, 1.0);
                Mat dst = new Mat();
                warpAffine (imgResize, dst, rot_mat, imgResize.size());
                dst.copyTo(submat);
                Flecha laFlecha = new Flecha(Flecha.TOP_LEFT,dst, roi );
                mapFlechas.put(laFlecha.getUbicacion(),laFlecha);
                ////////////arriba derecha
                rot_mat = getRotationMatrix2D (new Point(imgArrow.cols()/2,imgArrow.rows()/2), 0, 1.0);
                dst = new Mat();
                warpAffine (imgArrow, dst, rot_mat, imgArrow.size());
                roi = new Rect(0,0,imgArrow.cols(),imgArrow.rows());
                submat= mRgba.submat(roi);
                dst.copyTo(submat);
                laFlecha = new Flecha(Flecha.TOP_RIGHT,dst, roi);
                mapFlechas.put(laFlecha.getUbicacion(),laFlecha);
                ///////////abajo izquierda
                rot_mat = getRotationMatrix2D (new Point(imgArrow.cols()/2,imgArrow.rows()/2), 90, 1.0);
                dst = new Mat();
                warpAffine (imgArrow, dst, rot_mat, imgArrow.size());
                roi = new Rect(0,mRgba.rows()-imgArrow.cols(),imgArrow.cols(),imgArrow.rows());
                submat= mRgba.submat(roi);
                dst.copyTo(submat);
                laFlecha = new Flecha(Flecha.BOTTOM_LEFT,dst,roi);
                mapFlechas.put(laFlecha.getUbicacion(),laFlecha);
                ///////////abajo derecha
                rot_mat = getRotationMatrix2D (new Point(imgArrow.cols()/2,imgArrow.rows()/2), 180, 1.0);
                dst = new Mat();
                warpAffine (imgArrow, dst, rot_mat, imgArrow.size());
                roi = new Rect(mRgba.cols()-imgArrow.cols(),mRgba.rows()-imgArrow.rows(),imgArrow.cols(),imgArrow.rows());
                submat= mRgba.submat(roi);
                dst.copyTo(submat);
                laFlecha = new Flecha(Flecha.BOTTOM_RIGHT,dst, roi);
                mapFlechas.put(laFlecha.getUbicacion(),laFlecha);
                if( lstArrows == null ){
                    lstArrows = new ArrayList<Flecha>();
                    Mat imgArrows = Utils.loadResource(this, R.drawable.arrow, CvType.CV_8UC4);
                    int octava = imgArrows.rows()/8;
                    for(int i = 0; i < 8;i++){
                        roi = new Rect( 0,0,imgArrows.cols(),octava);
                        laFlecha = new Flecha(imgArrows.submat(roi),roi);
                        lstArrows.add( laFlecha );
                    }
                }
            } else {
                for (Map.Entry<Integer, Flecha> entry : mapFlechas.entrySet()) {
                    Flecha unaFlecha = entry.getValue();
                    imgArrow = unaFlecha.getImagen();
                    Mat submat= mRgba.submat(unaFlecha.getRoi());
                    imgArrow.copyTo(submat);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Point transformarPosModEsquinas( Vector2 posOriginal){
        //double posY = Math.round( arrowPos.y*(mRgba.cols()/Config.DRAW_DISTANCE));
        //double posX =  (mRgba.rows()/4)*(arrowPos.x+2)+1;
        Point posResul = new Point();
        double recorridoXMax = mRgba.cols()/2;
        double maxYMundoGdx = Config.DRAW_DISTANCE;
        double inicioCaminoX = mRgba.cols()/2;
        double inicioCaminoY = mRgba.rows()/2;
        //double deltaX = (((posOriginal.y+Config.DRAW_DISTANCE)/maxYMundoGdx)*recorridoXMax);
        double deltaX = ((( (Math.abs(posOriginal.y)))/maxYMundoGdx)*recorridoXMax);//ajustar
        double deltaY = (Math.round( (posOriginal.y)*((mRgba.cols()-0)/Config.DRAW_DISTANCE)));
        deltaX = deltaY*2;
        /// el valor de (y) en lugar de aumentar disminuye??
        //////////Log.i(TAG, "********1 datos de stepmania delta x : " + deltaX);
        if( posOriginal.x < 0){ /// estas dos arrows van para la izquierda
            //// para conseguir llegar al extremo derecho hay que restar el max recorrido : inicioCaminoX -recorridoXMax;
            posResul.x = inicioCaminoX -(recorridoXMax-deltaX);//(deltaX)-(offsetX*0.5);
        } else { /// los otros caminos son para la derecha
            posResul.x = inicioCaminoX +(recorridoXMax-deltaX);
        }
        //posResul.x = posResul.x*2;
        //////////Log.i(TAG, "********3 datos posResul : " + posResul.x+" inicioCaminoX "+inicioCaminoX);
        //posResul.x = inicioCaminoX;
        //deltaX = 20;/// delta fijo para hacer seguimiento solo de la coordenada x

        if( posOriginal.y < mRgba.cols()/4){
            if (offsetX == 0 ){
                if( posOriginal.x < 0){
                    offsetX = posResul.x - mRgba.cols()/2;
                }
            }
            if( posOriginal.x == -2 || posOriginal.x == 0){
                //posResul.y = posResul.x;
                posResul.y = deltaY;//inicioCaminoY+(deltaX);
            } else {
                //posResul.y = posResul.x*(-1);
                posResul.y = inicioCaminoY + ((mRgba.rows()/2)-deltaY);//inicioCaminoY-(deltaY*2);//;deltaY;//-(deltaY*2);
            }
        }

        return posResul;
    }
    private void borrarContoursMenores( Mat contours, Mat currentCopy, List<MatOfPoint> points ){
        //double MaxAreaContour = 10;
        //double MinAreaContour = 80;
        double MaxAreaContour = Integer.MAX_VALUE;
        double MinAreaContour = 200;
        for(int i=0;i<points.size();i++)
        {
            MatOfPoint contour = points.get(i);
            double AreaContour= contourArea(contour);
            if( AreaContour < MinAreaContour)
                points.remove(i);
        }
    }
    private List<MatOfPoint>  borrarContoursMenores2( Mat contours, Mat currentCopy, List<MatOfPoint> points ){
        //double MaxAreaContour = 10;
        //double MinAreaContour = 80;
        if( points.size() > 2 ){
            TreeMap<Double,MatOfPoint> mapContoursMayores = new TreeMap();
            List<MatOfPoint> pointsCopia = new ArrayList<>();
            for( int i = 0; i < points.size();i++){
                MatOfPoint contour = points.get(i);
                double areaContour= contourArea(contour);
                mapContoursMayores.put(areaContour,contour);
            }
            //mapContoursMayoresCopia.put(mapContoursMayores.lastKey(),mapContoursMayores.get(mapContoursMayores.lastKey()));
            pointsCopia.add(mapContoursMayores.get(mapContoursMayores.lastKey()));
            mapContoursMayores.remove(mapContoursMayores.lastKey());
            //mapContoursMayoresCopia.put(mapContoursMayores.lastKey(),mapContoursMayores.get(mapContoursMayores.lastKey()));
            pointsCopia.add(mapContoursMayores.get(mapContoursMayores.lastKey()));
            points = pointsCopia;

            ////////Log.i(TAG, "******** enborrar points size: " + points.size()+" - "+points);

        }

        return points;

    }
    private void mostrarAreasContours( Mat contours, Mat currentCopy, List<MatOfPoint> points ){
        double MaxAreaContour = Integer.MAX_VALUE;
        double MinAreaContour = 200;
        String sizeContours = "***** area contours";
        Scalar red = new Scalar(255,0,0);
        Scalar green = new Scalar(0,255,0);
        Scalar blue = new Scalar(0,0,255);
        int indiceAreaMayor = 0;
        double areaMax = 0;

        for(int i=0;i<points.size();i++)
        {
            MatOfPoint contour = points.get(i);
            double areaContour= contourArea(contour);
            if( areaContour > areaMax ){
                areaMax = areaContour;
                indiceAreaMayor = i;
            }
            sizeContours+=" ("+i+")"+areaContour;
            if(areaContour<MaxAreaContour && areaContour>MinAreaContour)
                //Imgproc.drawContours(currentCopy,contours,i,Scalar(DrawColor),Thickness,LineType,cv::noArray(),2147483647,cv::Point(DrawOffset_x,DrawOffset_y));
                Imgproc.drawContours( currentCopy, points,i,new Scalar(255, 100, 0, 255),3); //// descomentar para debug este drawcontours
        }
        Imgproc.drawContours( currentCopy, points,indiceAreaMayor,blue,3); ////
        ////////Log.i(TAG, sizeContours);
    }

    private void verificarAccesoArchivos( String[] files ){
        String fileStepmania = files[0];
        fileStepmania = fileStepmania.replace("/mnt/sdcard/","");
        //////Log.i(TAG, "****** *  fileStepmania "+fileStepmania);
        //String fileSong = files[1];/// solo se escoge el .sm y de ahi se deduce la cancion de acuerdo al directorio
        PackSong packSong = new PackSong(fileStepmania);
        game = new GameEngine( contexto, packSong);
    }
    private void testVolumen(){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.whack);
        int maxVolume = 50;
        int currVolume = -6;
        try {
            for(int i = 0 ;i < 10; i++){
                currVolume++;
                float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
                float volumen = 1-log1;
                //mp.setVolume(volumen,volumen);
                mp.setVolume(volumen,volumen);
                mp.start();
                i++;
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private int detectarToque( Mat diff, Rect roiImage, int indiceTopo){
        //Mat submatDiff= diff.submat(0,img.rows(),0, img.cols() );
        Mat submatDiff= diff.submat( roiImage);
        int elAcierto = FALLA;
        if(  Core.countNonZero(submatDiff) > 0 ){/// aki es donde se verifica si se traslapa la sombra del movimiento con la imagen del topo
            int maxVolume = 50;
            int currVolume = 1;
            boolean sonidoActivado = true;
            Topo  unTopo;
            final SharedPreferences prefs =
                    this.getSharedPreferences("molegame", Context.MODE_PRIVATE);
            String prefVolume = prefs.getString("volume","off");
            ////////Log.i(TAG, "****** prefvolume "+prefVolume);
            sonidoActivado = (prefVolume.equals("on"))?true:false;
            MediaPlayer mp = MediaPlayer.create(this, R.raw.whack);
            float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
            float volumen = 1-log1;
            //mp.setVolume(volumen,volumen);
            //sonidoActivado = true;
            mp.setVolume(0.00f,0.00f);
            //for(int i =0; i < nivel.getToposSimultaneos();i++ ){
            unTopo = lstTopos.get(indiceTopo);
            int unEstado =  unTopo.getSecuenciaAnimacion().getEstadoActual().idEstado;
            if( unEstado == Estado.SALIENDO ||  unEstado == Estado.AFUERA ){
                //if(unTopo.getEstado()== Estado.SALIENDO)toques++;
                //if(unTopo.getEstado()== Estado.AFUERA)toques+=2;
                //unTopo.setEstado(Estado.GOLPEADO);
                if( unEstado == Estado.SALIENDO)toques++;
                if( unEstado == Estado.AFUERA)toques+=2;
                //reiniciarSecuencia( unTopo.getSecuenciaAnimacion());
                //unTopo.setSecuenciaAnimacion(secuenciaEstadosGolpe);
                unTopo.getSecuenciaAnimacion().setIdSecuencia(SecuenciaEstados.GOLPE);
                //////////Log.i(TAG, "****** secuenciaEstadosGolpe "+unTopo.getSecuenciaAnimacion()+" secuenciaEstadosGolpe.estadoActual id"+unTopo.getSecuenciaAnimacion().getEstadoActual().idEstado);
                mp = MediaPlayer.create(this, R.raw.whack);
                //imgUltimoAcierto = mRgba.clone();
                if(sonidoActivado)mp.start();
                elAcierto = ACIERTO;
            } else if(unTopo.getEstado() == Estado.SIN_SALIR ){
                mp = MediaPlayer.create(this, R.raw.wrong);
                if(sonidoActivado)mp.start();
            }
            //}
            //return elAcierto;
        } else {
            elAcierto = NADA;
        }
        //elAcierto = NADA;
        return elAcierto;
    }
    private List<Resultado> detectarToqueStepmania(Mat diff){
        Rect roiDiff = new Rect(0,0,diff.cols(),diff.rows());
        int elAcierto = NADA;
        List<Resultado> lstResultados = new ArrayList<>();
        Imgproc.rectangle(mRgba, roiDiff.tl(), roiDiff.br(), Scalar.all(255), 2);
        ////////Log.i(TAG, "****** detectando toque  stepmania diff size  "+diff.size()+" mapaReceptores size "+mapaReceptores.size() );
        for (Map.Entry<Integer, Receptor> entry : mapaReceptores.entrySet()) {
            Receptor unReceptor = entry.getValue();
            ////////Log.i(TAG, "****** 1 verificando receptor "+unReceptor.getUbicacion()+" lstPoligono size "+unReceptor.getListaPoligono().size());
            List<MatOfPoint> listaReceptor = unReceptor.getListaPoligono();
            for( int i = 0; i < listaReceptor.size(); i++){
                MatOfPoint mpoint = listaReceptor.get(i);
                Rect rPrev = Imgproc.boundingRect(mpoint);//// cuando se trata de las esquinas hay que tomar un subcuadro para evitar desborde pero cuando se hace por carriles
                //// paralelos si el rango de x es estrecho va hacer que nunca haya coincidencia de acierto porque la esquina topleft queda por fuera
                Rect r = new Rect( new Point (rPrev.tl().x-2,rPrev.tl().y+5) , new Point(rPrev.br().x, rPrev.br().y));
                ////////Log.i(TAG, "****** 2  verificando receptor "+unReceptor.getUbicacion()+" tl "+r.tl()+" br "+r.br());
                Imgproc.rectangle(mRgba, r.tl(), r.br(), Scalar.all(255), 2);
                Imgproc.rectangle(mRgba, new Point(mRgba.cols()/2, mRgba.rows()/2), new Point(mRgba.cols(),mRgba.rows()), Scalar.all(255), 2);
                if( roiDiff.contains( r.tl()) && roiDiff.contains(r.br())){/// esta verificacion es para saber que no desbordo la pantalla
                    if( lstPuntosRecientes != null ){
                        ////////Log.i(TAG, "****** 3 -- verificando receptor "+unReceptor.getUbicacion()+" size pts recientes "+lstPuntosRecientes.size());
                        Mat submatDiff= diff.submat( r );
                        if(  Core.countNonZero(submatDiff) > 0 ){/// se verifica  primero la sombra del detector de movimiento sobre el receptor de la esquina
                            unReceptor.setTocado(true);
                            elAcierto = FALLA;//// toco el cuadro pero la flecha no estaba ahi
                            Resultado resultado = new Resultado(unReceptor,Score.MISS);
                            //////Log.i(TAG, "****** 4c verificando receptor "+unReceptor.getUbicacion()+" r tl "+r.tl()+" br "+r.br()+" lstPuntosRecientes size "+lstPuntosRecientes.size());
                            int gradoAcierto = rectGradoAcierto(r,lstPuntosRecientes);/// y de ultimo se verifica si el receptor y la flecha se traslaparon
                            if( gradoAcierto != Score.MISS ){
                                unReceptor.setAcertado(true);
                            }
                            unReceptor.setGradoAcierto(gradoAcierto);
                            resultado.setGradoAcierto(gradoAcierto);
                            resultado.setReceptor(unReceptor);
                            lstResultados.add(resultado);
                        }
                    }
                }
            }
        }
        borrarPuntosMeasuresViejos();
        ////////Log.i(TAG, "****** detectando toque stepmania acierto "+elAcierto);
        return lstResultados;
    }
    private boolean rectContienePunts( Rect roi, List<PointMeasure> lstPuntosRecientes ){
        /// el measure que esta dentro del for del metodo primera integracion siempre esta en el ultimo como si fuera el size raro toco con otra variable
        // int ultimoMeasure =
        int toleracia = 5;/// lo que se ve por pantalla es 4 pero...
        ////////Log.i(TAG, "****** size lstPuntosRecientes "+lstPuntosRecientes.size());
        for( PointMeasure elPoint : lstPuntosRecientes){
            ////////Log.i(TAG, "****** 3 rectContienePunts point measure id "+elPoint.measure.id+" measureActual id "+idUltimoMeasure );
            //////Log.i(TAG, "****** 3 puntos para revisar del roi tl "+roi.tl()+" br "+roi.br()+" elpoint x "+elPoint.point.x+" y "+elPoint.point.y+" measure point "+elPoint.measure.id+" m actual "+measureActual.id+" idultimomeasure "+idUltimoMeasure );
            if( elPoint.point.y < roi.br().y ){
                if( Math.abs( elPoint.point.x-roi.x ) < roi.width/4 ){
                    return true;
                }
            } else {//// problemas de concurrentexception si dejo esto aca
                //lstPuntosRecientes.remove(elPoint);
            }
        }
        return false;
    }
    private int rectGradoAcierto( Rect roi, List<PointMeasure> lstPuntosRecientes ){
        /// el measure que esta dentro del for del metodo primera integracion siempre esta en el ultimo como si fuera el size raro toco con otra variable
        // int ultimoMeasure =
        int divAciertos = 6;//// solo las que tiene exactitud de decimas o milesimas https://strategywiki.org/wiki/StepMania/Terminology
        int unidadAcierto = (int)(roi.br().y/divAciertos);
        int toleracia = 5;/// lo que se ve por pantalla es 4 pero...
        ////////Log.i(TAG, "****** size lstPuntosRecientes "+lstPuntosRecientes.size());
        for( PointMeasure elPoint : lstPuntosRecientes){
            ////////Log.i(TAG, "****** 3 rectContienePunts point measure id "+elPoint.measure.id+" measureActual id "+idUltimoMeasure );
            //////Log.i(TAG, "****** 3 puntos para revisar del roi tl "+roi.tl()+" br "+roi.br()+" elpoint x "+elPoint.point.x+" y "+elPoint.point.y+" measure point "+elPoint.measure.id+" m actual "+measureActual.id+" idultimomeasure "+idUltimoMeasure );
            if( elPoint.point.y < roi.br().y ){
                if( Math.abs( elPoint.point.x-roi.x ) < roi.width/4 ){
                    int gradoAcierto = (int)((roi.br().y - elPoint.point.y)/unidadAcierto );
                    //Log.i(TAG, "****** grado acierto "+gradoAcierto);
                    switch (gradoAcierto){
                        case 6: return Score.BOO;
                        case 5: return Score.BAD;
                        case 4: return Score.GOOD;
                        case 3: return Score.GREAT;
                        case 2: return Score.PERFECT;
                        case 1: return Score.MARVELOUS;
                        default: return Score.MISS;
                    }
                }
            } else {//// problemas de concurrentexception si dejo esto aca
                //lstPuntosRecientes.remove(elPoint);
            }
        }
        return Score.MISS;
    }
    public void reiniciarSecuencia( SecuenciaEstados secuenciaEstados){
        secuenciaEstados.setEstadoActual(secuenciaEstados.getDesaparece());
        secuenciaEstados.setFinalizada(true);
    }
    public void onClickShare(View view){

        //Bitmap bitmap =getBitmapFromView(view);
        ////////Log.i(TAG, "****** en onclickshare 1");
        //Bitmap bitmap =getBitmapFromMat();
        Bitmap bitmap = bitmapForShare;
        //Bitmap bitmap =getBitmapScreenShoot();
        //Bitmap bitmap =getBitmapFromView(findViewById(R.id.main_layout));
        ////Log.i(TAG, "****** en onclickshare height "+bitmap.getHeight());
        try {
            //File file = new File(this.getExternalCacheDir(),File.separator+ "logicchip.png");
            File file = new File(this.getExternalCacheDir(),"logicchip.png");
            // File file = new File(this.getCacheDir(),File.separator+ "logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            ///////
            String urlJuego ="fdbgames";
            String shareBody = "Te reto a que superes mi puntaje, descargalo en "+urlJuego;
            //Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Beat AR Game");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            ///////
            intent.setType("image/png");
            //startActivity(Intent.createChooser(intent, "Share image via"));
            startActivityForResult(Intent.createChooser(intent, "Share image via"),CODE_FOR_RESULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void guardarImagenUltimoACierto(){
        //Bitmap bitmap =getBitmapFromView(view);
        ////////Log.i(TAG, "****** en guardarImagenUltimoACierto 1 toques "+toques);
        bitmapForShare =getBitmapFromMat();
    }
    private void guardarJuegosHistorico( Juego elJuego){
        if( lstJuego == null){
            lstJuego = new ArrayList<>();
        }
        todaListaAUltimoFalse(lstJuego);
        lstJuego.add(elJuego);
        Log.d(TAG,"****** 1 size juego historico " +lstJuego.size());
        lstJuego = quitarRepetidosLista(lstJuego);
        Log.d(TAG,"****** size juego historico luego de quitar repetidos " +lstJuego.size());
        //Preference gender = findPreference("volume");
        final SharedPreferences prefs = getSharedPreferences("beatar", Context.MODE_PRIVATE);
        Juego.guardarJuegoEnPreferences(lstJuego, prefs);
        lstJuego = Juego.getHistoricoFromJson(prefs);
        //lstJuego = quitarCancionesEjemplo(lstJuego);

        if( lstJuego != null ){
            Log.d(TAG,"****** Converted JSONObject ==> " +lstJuego);
            llenarTablaScores(lstJuego);
        }
    }
    private List<Juego> quitarRepetidosLista(List<Juego> lstJuego){
        Set<Juego> set = new HashSet<>(lstJuego);
        lstJuego.clear();
        lstJuego.addAll(set);
        return lstJuego;
    }
    private List<Juego> todaListaAUltimoFalse(List<Juego> lstJuego){
       for( Juego elJuego: lstJuego){
           elJuego.setUltimo(false);
       }
       return lstJuego;
    }
    private List<Juego> quitarCancionesEjemplo( List<Juego> lstJuego ){
        for (Iterator<Juego> iterator = lstJuego.iterator(); iterator.hasNext();) {
            Juego elJuego  = iterator.next();
            if( elJuego.getCancion().contains("cancion")){
                iterator.remove();
            }
        }
        return lstJuego;
    }
    private void cargarDialogoFilePicker(){
        String[] arrExtensions = {"zip","sm"};
        final String extensionZip = ".zip";
        final String pathStoreEmulated = "/storage/emulated/0/";
        final String extensionStepmania = ".sm";
        String dirDefault = Environment.getExternalStorageDirectory() +"/"+ nombreFolderJuego ;//DialogConfigs.DEFAULT_DIR"
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.DIR_SELECT;
        properties.root = new File(Environment.getExternalStorageDirectory()+"" );//DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(dirDefault);
        properties.offset = new File(dirDefault);
        properties.extensions = arrExtensions;
        dialog = new FilePickerDialog(MainActivity.this,properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                String fileZipStepmania = files[0];
                String dirFile = "";
                if( files.length > 1 ){
                    dirFile = files[1];
                }
                //String fileStepmania = files[0];
                /*fileStepmania = fileStepmania.replace("/mnt/sdcard/","");*/
                ////Log.i(TAG, "****** 1  file or dir Stepmania "+fileZipStepmania+"  dir files "+dirFile);
                //verificarAccesoArchivos(files);
                /*packSongDescargado =  new PackSong(fileStepmania);
                 */
                if ( !UtilFile.existeFolder(nombreFolderJuego)){ /// si no existe el folder se crea
                    UtilFile.createFolder(nombreFolderJuego);
                    //UtilFile.createFolder2(nombreFolderJuego);
                }
                if( fileZipStepmania.endsWith(extensionZip) ){/// si es un zip se descomprime en el folder del juego
                    //////Log.d(TAG, "****** es un file zip ");
                    UtilZip.unzip(fileZipStepmania,nombreFolderJuego);
                } else if( fileZipStepmania.endsWith(extensionStepmania ) ){ /// si es un archivo de stempamnia (.sm) se reproduce, lo cual es poco probable, ya que los usuarios no estan famiiarizados con la extensione
                    //////Log.d(TAG, "****** es un file stepmania ");
                    fileZipStepmania = fileZipStepmania.replace(pathStoreEmulated,"");
                    packSongDescargado =  new PackSong(fileZipStepmania);
                } else if ( !fileZipStepmania.contains(".")){ /// si no se asume que solo escogieron el directorio luego tambien se asume que el archivo .sm se llama igual que el directorio
                    fileZipStepmania = fileZipStepmania.replace(pathStoreEmulated,"");/// esto mientras se piensa si iterar el directorio en busca de un sm , pero si hay varios podria falla , tocaria revisar
                    String nameFile = fileZipStepmania.substring(fileZipStepmania.lastIndexOf("/"),fileZipStepmania.length());
                    ////Log.i(TAG, "****** nameFile  "+nameFile);
                    fileZipStepmania = fileZipStepmania+"/"+nameFile+extensionStepmania;
                    packSongDescargado =  new PackSong(fileZipStepmania);
                    game = new GameEngine(contexto, packSongDescargado );
                }
            }
        });
    }
    private void captureBitmap(){
        /*Mat mBitmap = new Mat(null);
        Bitmap bitmap = Bitmap.createBitmap(mOpenCvCameraView.getWidth()/4,mOpenCvCameraView.getHeight()/4, Bitmap.Config.ARGB_8888);
        try {
            bitmap = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mRgba, bitmap);
            mBitmap.setImageBitmap(bitmap);
            mBitmap.invalidate();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }*/
    }
    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        //Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
    //create bitmap from view and returns it
    private Bitmap getBitmapFromMat() {
        ////////Log.i(TAG, "****** en getBitmapFromMat 11  ");
        Mat matLocal = imgUltimoAcierto;//mRgba.clone();
        //Define a bitmap with the same size as the view
        //Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        int height = matLocal.height();//unTopo.getImgPintar().height();
        int width = matLocal.width();//unTopo.getImgPintar().width();
        //Mat tmp = new Mat (height, width, CvType.CV_8UC1, new Scalar(4));
        Mat tmp = new Mat (height, width, CvType.CV_8UC1 );
        //2) Imgproc.cvtColor can change the dimensions of the tmp object. So it is safe to create a bitmap after the color conversion:

        Bitmap bmp = null;
        //Mat tmp = new Mat (height, width, CvType.CV_8U, new Scalar(4));
        try {
            Imgproc.cvtColor(matLocal, matLocal, Imgproc.COLOR_BGRA2RGB);
            //Imgproc.cvtColor(matLocal, tmp, Imgproc.COLOR_BGRA2RGB);
            //Imgproc.cvtColor(matLocal, tmp, Imgproc.COLOR_RGB2BGR);/// asi COLOR_BGRA2RGB mostro el verde
            //Imgproc.cvtColor(mRgba, tmp, Imgproc.COLOR_RGB2BGRA);/// el que esta en mostrar topo es COLOR_RGB2BGRA
            //Imgproc.cvtColor(mRgba, tmp, Imgproc.COLOR_BGRA2RGB, 4);/// este funciona casi, queda de color azul
            //Imgproc.cvtColor(mRgba, tmp, Imgproc.COLOR_BGRA2RGB, 4);
            //bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            bmp = Bitmap.createBitmap(matLocal.cols(), matLocal.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(matLocal, bmp);
            bmp = swapRed2blue(bmp);/// esta jugada es la que puede demorar en procesamiento
        }
        catch (CvException e){
            //////Log.d("Exception",e.getMessage());
            e.printStackTrace();
        }

        //return the bitmap
        return bmp;
    }
    private Bitmap swapRed2blue( Bitmap bitmap ){
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int len = bitmap.getWidth() * bitmap.getHeight();

        ////////////////
        //int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        //bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int[] finalArray = new int[bitmap.getWidth() * bitmap.getHeight()];

        for(int i = 0; i < len; i++) {

            int red = Color.red(pixels[i]);
            int green = Color.green(pixels[i]);
            int blue = Color.blue(pixels[i]);
            finalArray[i] = Color.rgb(blue, green, red);//invert sequence here.
        }
        bitmap.setPixels( finalArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return bitmap;
        //len = bitmap.getWidth() * bitmap.getHeight();
        //onFacebookImageNative(pixels, len, bitmap.getWidth(), bitmap.getHeight(), bitsPerComponent);
    }
    private Bitmap getBitmapScreenShoot() {
        ////////Log.i(TAG, "****** en getBitmapScreenShoot 3");
        // create bitmap screen capture
        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        //return the bitmap
        return bitmap;
    }
    private void actualizarScore( Mat screen){
        float size = 2.0f;
        Imgproc.putText(screen, " 3"+getString(R.string.score)+" "+toques,
                new Point(screen.width()*0.5, screen.height()*0.7),
                face[3], size, new Scalar(0,255,0),3,1);
    }
    private void mostrarScore(){
        TextureAtlas atlas;
        FileHandle fh = Gdx.files.internal( urlFileAtlas );
        //manager.load("data/mytexture.png", Texture.class);
        //manager.load(urlFile,TextureAtlas.class);/// esto debe ir en el oncreate
        InputStream is = null;
        ////////Log.i(TAG, "****** load receptor con assmanager de android  is not null "+(  is != null) );
        try {
            is = this.assManager.open("good.png");

            Bitmap  bitmap = BitmapFactory.decodeStream(is);
            Mat img = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
            ////////Log.i(TAG, "****** load receptor con assmanager de android  is not null "+(  is != null) + " size cols "+img.cols()+" rows "+img.rows());
            //displayBitmap(img);
            Mat imgResize = new Mat();
            resize(img.clone(), imgResize,new Size(200,200));
            Mat subMat = mRgba.submat(0,0,imgResize.cols(),imgResize.rows());
            imgResize.copyTo(subMat);
            img = Utils.loadResource(this, R.drawable.arrowtopleft, CvType.CV_8UC4);// arrowtopleft  //good
            imgResize = new Mat();
            resize(img.clone(), imgResize,new Size(200,200));
            subMat = mRgba.submat(5,5,imgResize.cols()-5,imgResize.rows()-5);
            imgResize.copyTo(subMat);
            Rect r = Imgproc.boundingRect(imgResize);
            rectangle(mRgba,r.tl(),r.br(),Scalar.all(255),3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        if( manager != null ){
            //Texture  receptorTexture = manager.get("receptor.png", Texture.class);
            ////////Log.i(TAG, "****** load arrow with assetmanager isloaded "+manager.isLoaded("receptor.png",  Texture.class));
            ////////Log.i(TAG, "****** bitmap from atlas , exists "+fh.exists()+"  isLoaded "+manager.isLoaded(urlFileAtlas));
            if( fh != null && fh.exists() && manager.isLoaded(urlFileAtlas)){
                atlas = manager.get(urlFileAtlas,TextureAtlas.class);
                TextureAtlas.AtlasRegion region = atlas.findRegion("arMarvelous");
                Pixmap pixmap = region.getTexture().getTextureData().consumePixmap();
                Bitmap bitmap = pixMap2BitMap(pixmap);
                ////////Log.i(TAG, "****** bitmap from atlas  "+bitmap.getWidth()+" x "+bitmap.getHeight());
                Mat img = new Mat(bitmap.getHeight(), bitmap.getWidth(),
                        CvType.CV_8UC4);
                //displayBitmap(img);
                Mat subMat = mRgba.submat(0,0,img.cols(),img.rows());
                subMat.copyTo(mRgba);
            }
        }*/

    }
    private void mostrarScoreRecientes(){
        if( lstScoresRecientes != null ){//todo pendiente revisar si agrego otra condicion para que no evalue como acierto las ultimas jugadas cuando no hay flechas en movimiento
            for( Score elScore : lstScoresRecientes){/// lo que tal vez seria agregar aca la condicion de inicioJuego true
                if( elScore.getTiempoVisibleRestante() > 0 ){
                    mostrarScore2(elScore);
                    elScore.setTiempoVisibleRestante(elScore.getTiempoVisibleRestante()-1);
                }
            }
        }
    }
    private void mostrarScore2(Score elScore){
        int idScore = elScore.getIdScore();
        Mat img = null;// arrowtopleft  //good
        Mat imgMask = null;
        InputStream is = null;
        int resize = 200;
        int posx =  (int)Math.round(mRgba.cols()*0.3);
        if ( elScore.getReceptor() != null ){
            posx = (int)elScore.getReceptor().getListaPoligono().get(0).toList().get(0).x;
        }
        int posy =  (int)Math.round(mRgba.rows()*0.5);
        Score score  = null;
        try {//////todo este codigo va quedar tan largo que a futuro toca pasarlo a otra clase de cargue inicial junto con todos las otras imagenes
            if( mapScores == null ){
                mapScores = new HashMap<Integer, Score>();
                /// GOOD
                img = Utils.loadResource(this, R.drawable.good2, CvType.CV_8UC4);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);
                imgMask = Utils.loadResource(this, R.drawable.goodmask, CvType.CV_8UC4);
                Mat imgResize = new Mat();  /// osea que paila no se puede usar assets, solo se puede usar drawable
                Mat imgResizeMask = new Mat();
                resize(img.clone(), imgResize,new Size(resize,resize));
                resize(imgMask.clone(), imgResizeMask,new Size(resize,resize ));
                score = new Score(imgResize,imgResizeMask,Score.GOOD);
                mapScores.put(Score.GOOD,score);
                ////// BAD
                img = Utils.loadResource(this, R.drawable.bad, CvType.CV_8UC4);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);
                imgMask = Utils.loadResource(this, R.drawable.badmask, CvType.CV_8UC4);
                imgResize = new Mat();  /// osea que paila no se puede usar assets, solo se puede usar drawable
                imgResizeMask = new Mat();
                resize(img.clone(), imgResize,new Size(resize,resize));
                resize(imgMask.clone(), imgResizeMask,new Size(resize,resize ));
                score = new Score(imgResize,imgResizeMask,Score.BAD);
                mapScores.put(Score.BAD,score);
                ////// BOO /// FALTA HACERLE LA MASCARA , SOLO LA PUEDO HACER EN EL EQUIPO DEL APTO, NO SE PORQUE
                img = Utils.loadResource(this, R.drawable.boo, CvType.CV_8UC4);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);
                imgMask = Utils.loadResource(this, R.drawable.boo, CvType.CV_8UC4);
                imgResize = new Mat();  /// osea que paila no se puede usar assets, solo se puede usar drawable
                imgResizeMask = new Mat();
                resize(img.clone(), imgResize,new Size(resize,resize));
                resize(imgMask.clone(), imgResizeMask,new Size(resize,resize ));
                score = new Score(imgResize,imgResizeMask,Score.BOO);
                mapScores.put(Score.BOO,score);
                ////// PERFECT
                img = Utils.loadResource(this, R.drawable.perfect, CvType.CV_8UC4);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);
                imgMask = Utils.loadResource(this, R.drawable.perfectmask, CvType.CV_8UC4);
                imgResize = new Mat();  /// osea que paila no se puede usar assets, solo se puede usar drawable
                imgResizeMask = new Mat();
                resize(img.clone(), imgResize,new Size(resize,resize));
                resize(imgMask.clone(), imgResizeMask,new Size(resize,resize ));
                score = new Score(imgResize,imgResizeMask,Score.PERFECT);
                mapScores.put(Score.PERFECT,score);
                ////// MARVELOUS
                img = Utils.loadResource(this, R.drawable.marvelous, CvType.CV_8UC4);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);
                imgMask = Utils.loadResource(this, R.drawable.marvelousmask, CvType.CV_8UC4);
                imgResize = new Mat();  /// osea que paila no se puede usar assets, solo se puede usar drawable
                imgResizeMask = new Mat();
                resize(img.clone(), imgResize,new Size(resize,resize));
                resize(imgMask.clone(), imgResizeMask,new Size(resize,resize ));
                score = new Score(imgResize,imgResizeMask,Score.MARVELOUS);
                mapScores.put(Score.MARVELOUS,score);
                ////// GREAT
                img = Utils.loadResource(this, R.drawable.great, CvType.CV_8UC4);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);
                imgMask = Utils.loadResource(this, R.drawable.greatmask, CvType.CV_8UC4);
                imgResize = new Mat();  /// osea que paila no se puede usar assets, solo se puede usar drawable
                imgResizeMask = new Mat();
                resize(img.clone(), imgResize,new Size(resize,resize));
                resize(imgMask.clone(), imgResizeMask,new Size(resize,resize ));
                score = new Score(imgResize,imgResizeMask,Score.GREAT);
                mapScores.put(Score.GREAT,score);
                ////// MISS
                img = Utils.loadResource(this, R.drawable.miss, CvType.CV_8UC4);
                Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);
                imgMask = Utils.loadResource(this, R.drawable.miss, CvType.CV_8UC4);
                imgResize = new Mat();  /// osea que paila no se puede usar assets, solo se puede usar drawable
                imgResizeMask = new Mat();
                resize(img.clone(), imgResize,new Size(resize,resize));
                resize(imgMask.clone(), imgResizeMask,new Size(resize,resize ));
                score = new Score(imgResize,imgResizeMask,Score.MISS);
                mapScores.put(Score.MISS,score);
            }
            //Log.i(TAG, "****** idscore a mostrar "+idScore);
            Score scoreRes = mapScores.get( idScore );
            //Mat img2 = Utils.loadResource(this, R.drawable.good2, CvType.CV_8UC4);
            //Imgproc.cvtColor(img2, img2, Imgproc.COLOR_RGB2BGRA);
            //scoreRes.setImg(img);
            img = scoreRes.getImg();
            imgMask = scoreRes.getImgMask();
            Rect roi = new Rect(posx,posy, img.cols(),img.rows());
            ////////Log.i(TAG, "****** 2 imagen score para pintar size "+img.size());
            //Log.i(TAG, "****** roi tl "+roi.tl()+" br "+roi.br()+" idScore "+idScore);
            //if( roi.br().x > mRgba.cols()){
            roi = new Rect(posx-100,posy, img.cols(),img.rows());
            //}
            Mat subMat = mRgba.submat(roi);
            ///Rect rPrev = Imgproc.boundingRect(mpoint);
            img.copyTo(subMat,imgMask);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Bitmap pixMap2BitMap(Pixmap pixmap){
        PixmapIO.PNG writer = new PixmapIO.PNG((int)(pixmap.getWidth() * pixmap.getHeight() * 1.5f));
        writer.setFlipY(false);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            writer.write(output, pixmap);
        } catch ( IOException ioex ){
            ioex.printStackTrace();
        }finally {
            StreamUtils.closeQuietly(output);
            writer.dispose();
            pixmap.dispose();
        }
        byte[] bytes = output.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }
    private void mostrarNivel ( Mat screen){
        float size = 2.0f;
        if( nivel != null ){
            Imgproc.putText(screen, ""+getString(R.string.level)+" "+nivel.getIdNivel(),
                    new Point(screen.width()*0.2, screen.height()*0.8),
                    face[3], size, new Scalar(0,255,0),3,1);
        }
    }
    private void mostrarPuntajeStepmania ( Mat screen){
        float size = 2.0f;
        if( game != null && game.getLstPuntajes() != null && game.getLstPuntajes().size() > 0 ){
            float ultimoPuntaje = game.getLstPuntajes().get( game.getLstPuntajes().size()-1 );
            puntajeAcumulado = 0;
            for( Float elPuntaje : game.getLstPuntajes()){
                puntajeAcumulado+=elPuntaje;
            }//// aki se cambia numero para verificar cambio
            Imgproc.putText(screen, " "+puntajeAcumulado,
                    new Point(screen.width()*0.2, screen.height()*0.8),
                    face[3], size, new Scalar(0,255,0),3,1);
        }
    }
    private void mostrarInfoCancion ( Mat screen){
        float size = 2.0f;
        /*String nombreCancion  = game.smFile.title;
          if( packSongDescargado != null ){
            nombreCancion = packSongDescargado.getFileStepmania().
        }*/
        if( game != null && game.smFile != null ){
            Imgproc.putText(screen, "Song "+game.smFile.title,
                    new Point(screen.width()*0.2, screen.height()*0.6),
                    face[3], size, new Scalar(0,255,0),3,1);
        }
    }
    private void mostrarMensajeEspera( Mat screen){
        float size = 5.0f;
        if( enEspera){
            long millis = System.currentTimeMillis() - inicioEspera;
            int seconds = (int) (millis / 1000);
            seconds = SEGUNDOS_ESPERA_LIMITE -seconds;
            Imgproc.putText(screen, ""+seconds,
                    new Point(screen.width()*0.5, screen.height()*0.5),
                    face[3], size, new Scalar(255,100,0),3,1);
        }
    }
    private void mostrarMensajeAciertoOError( Mat screen ){
        String mensaje= MSG_VACIO;
        if( duracionMensaje > LIMITE_DURACION_MENSAJE){
            if( ultimaDeteccion != NADA  ){
                mensaje = (ultimaDeteccion == ACIERTO )?MSG_ACIERTO:(ultimaDeteccion == FALLA )?MSG_FALLA:MSG_VACIO;
                float size = 3.0f;
                Scalar red = new Scalar(255,0,0);
                Scalar green = new Scalar(0,255,0);
                Scalar color = (ultimaDeteccion == ACIERTO )?green:red;
                //String mensaje = (ultimaDeteccion == ACIERTO )?MSG_ACIERTO:(ultimaDeteccion == FALLA )?MSG_FALLA:MSG_VACIO;
                Imgproc.putText(screen, mensaje,
                        new Point(screen.width()*0.5, screen.height()*0.5),
                        face[3], size, color,3,1);
                duracionMensaje = 0;
            }
            ultimoMensaje = (ultimaDeteccion == ACIERTO )?MSG_ACIERTO:(ultimaDeteccion == FALLA )?MSG_FALLA:MSG_VACIO;
        } else {
            if( ultimoMensaje == null ){
                ultimoMensaje = (ultimaDeteccion == ACIERTO )?MSG_ACIERTO:(ultimaDeteccion == FALLA )?MSG_FALLA:MSG_VACIO;
            }
            float size = 3.0f;
            Scalar red = new Scalar(255,0,0);
            Scalar green = new Scalar(0,255,0);
            Scalar color = (ultimoMensaje.equals(MSG_ACIERTO) )?green:red;
            //String mensaje = (ultimaDeteccion == ACIERTO )?MSG_ACIERTO:(ultimaDeteccion == FALLA )?MSG_FALLA:MSG_VACIO;
            Imgproc.putText(screen, ultimoMensaje,
                    new Point(screen.width()*0.5, screen.height()*0.5),
                    face[3], size, color,3,1);
            duracionMensaje++;
        }
        /*
            if( duracionFrame > 4 ){
                mostrarTopoSegunEstado(unTopo);///unTopo.lstSecuencia.get(0);
                duracionFrame = 0;
            } else {
                if( unTopo.getImgPintar() == null ){// si es la primera vez, la imagen para pintar es null
                    mostrarTopoSegunEstado(unTopo);///unTopo.lstSecuencia.get(0);
                }
                duracionFrame++;
            }
         */
    }
    private void mostrarTiempo( Mat screen){
        //Mat copyScreen = screen.clone();
        if ( textTime == null){
            textTime = ""+SEGUNDOS_LIMITE;
        }
        if( textTime.contains("-")){
            textTime = String.format("%d:%02d", 0, 0);
        }
        Imgproc.putText(screen, ""+getString(R.string.time)+" "+textTime,
                new Point(screen.width()*0.5,screen.height()*0.2),
                face[3], 2.0, new Scalar(0,255,0),3,1);
        //return copyScreen;
    }
    public void llenarTablaScores(List<Juego> lstHistoricoJuegos){
        //https://technotzz.wordpress.com/2011/11/04/android-dynamically-add-rows-to-table-layout/
        /****SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

         String dateString = format.format( new Date()   );
         * ***/
        //tableLayoutScores.removeViewAt()
        while ( tableLayoutScores.getChildCount() > 1 ) //IN this code, please assume the child count is 10;
        {
            View v = tableLayoutScores.getChildAt(0);
            //Standard.Loge("REMOVING: " + 0 + " " + (v == null));
            //if( v.getId() != R.id.titulosScores){
                tableLayoutScores.removeViewAt(1);
                //tableLayoutScores.removeViewAt(2);
            //}
        }
        Log.d(TAG,"****** 4 size lista juegos " + lstHistoricoJuegos.size());
        TreeMap<Float,Juego> mapScores = new TreeMap(Collections.reverseOrder());
        for( Juego unJuego : lstHistoricoJuegos ){
            mapScores.put(unJuego.getScore(),unJuego);
        }
        List<Juego> listOrdenados = new ArrayList<Juego>(mapScores.values());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
        long tiempoActual = System.currentTimeMillis();
        int padding = 10;
        //TableRow = tableLayoutScores.get
        for(Juego unJuego: listOrdenados){
            int color = Color.WHITE;
            if( ( tiempoActual - unJuego.getFecha().getTime() ) < 3000 ){
                color = Color.GREEN;
            }
            TableRow tr = new TableRow(this);
            tr.setId(10);
            tr.setBackgroundColor(Color.GRAY);
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            TextView label_Cancion = new TextView(this);
            label_Cancion.setId(20);
            label_Cancion.setText(unJuego.getCancion());
            label_Cancion.setTextColor(color);
            label_Cancion.setPadding(padding, padding, padding, padding);
            tr.addView(label_Cancion);// add the column to the table row here
            TextView label_Nivel = new TextView(this);
            label_Nivel.setId(30);
            label_Nivel.setText(Integer.toString(unJuego.getNivel()));
            label_Nivel.setTextColor(color);
            label_Nivel.setPadding(padding, padding, padding, padding);
            tr.addView(label_Nivel);// add the column to the table row here
            TextView label_Puntaje = new TextView(this);
            label_Puntaje.setId(40);
            label_Puntaje.setText(Float.toString(unJuego.getScore()));
            label_Puntaje.setTextColor(color);
            label_Puntaje.setPadding(padding, padding, padding, padding);
            tr.addView(label_Puntaje);// add the column to the table row here
            TextView label_fecha = new TextView(this);
            label_fecha.setId(50);
            label_fecha.setText(format.format( unJuego.getFecha() ));
            label_fecha.setTextColor(color);
            label_fecha.setPadding(padding, padding, padding, padding);
            tr.addView(label_fecha);// add the column to the table row here
            tableLayoutScores.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT   ,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }
    /**Mat m = ... // your smaller mat
     Mat submat= matOrig.submat(new Rect(x,y, m.cols(), m.rows()) );
     m.copyTo(submat);
     * **/
    private List<MatOfPoint> flechaAumentada( List<MatOfPoint> lstOriginal ){
        int aumento = 5;
        int desfaseLateral = 50;
        MatOfPoint mopMod = null;
        int[] arrX = {50,100+aumento,75+aumento,75+aumento,27-aumento,27-aumento,0-aumento,50};
        int[] arrY = {0,50+aumento,50+aumento,75+aumento,75+aumento,50+aumento,50+aumento,0};
        ///* int[] arrX = {50,100,75,75,27,27,0,50};
        //        int[] arrY = {50,50,75,75,50,50};
        /// new Point(maxRight+arrX[0], maxDown),/// la punta superior de la flecha
        //                        new Point(maxRight+arrX[1], maxDown+arrY[0])    ,
        //                        new Point(maxRight+ arrX[2],maxDown+ arrY[1]),
        //                        new Point(maxRight +arrX[3],maxDown + arrY[2]),
        //                        new Point(maxRight +arrX[4],maxDown+ arrY[3]),
        //                        new Point(maxRight+arrX[5],maxDown+ arrY[4]),
        //                        new Point(max Right+arrX[6],maxDown + arrY[5]),
        //                        new Point(maxRight+arrX[7],maxDown)
        /// **/
        MatOfPoint mpoint = new MatOfPoint();
        List<Point> lstPoints = new ArrayList<>();
        for(MatOfPoint mpoints: lstOriginal){
            Point[] arrPoint = mpoints.toArray();
            for( int i =0; i < arrPoint.length;i++ ){
                arrPoint[i].x = arrPoint[i].x + arrX[i];arrPoint[i].y =  arrPoint[i].y + arrY[i];
                arrPoint[i].x = arrPoint[i].x - desfaseLateral; /// pinta de derecha a izquierda por el flip
                lstPoints.add(arrPoint[i]);
            }
        }
        List<MatOfPoint> lstMod = new ArrayList<>();
        mpoint = new MatOfPoint(lstPoints.get(0),lstPoints.get(1),lstPoints.get(2),lstPoints.get(3),lstPoints.get(4),lstPoints.get(5),lstPoints.get(6),lstPoints.get(7));
        lstMod.add(mpoint);
        return  lstMod;/// se cambiara por referencia
    }
    public static Mat RectangleSubROI(Mat input, Rect rect) {
        final Mat maskCopyTo = Mat.zeros(input.size(), CV_8UC1); // ����copyTo������mask����С��ԭͼ����һ��
        // floodFill��mask��width��height�����������ͼ��������������أ��������ᱨ��
        final Mat maskFloodFill = Mat.zeros(new Size(input.cols() + 2, input.rows() + 2), CV_8UC1); // ����floodFill������mask���ߴ��ԭͼ��һЩ
        // Imgproc.circle(maskCopyTo, new Point(cc.x, cc.y), radius, Scalar.all(255), 2,
        // 8, 0); // ����Բ������
        Imgproc.rectangle(maskCopyTo, rect.tl(), rect.br(), Scalar.all(255), 2, 8, 0);
        Imgproc.floodFill(maskCopyTo, maskFloodFill,
                new Point((rect.tl().x + rect.br().x) / 2, (rect.tl().y + rect.br().y) / 2), Scalar.all(255), null,
                Scalar.all(20), Scalar.all(20), 4); // ��ˮ��䷨���Բ���ڲ�
        // MatView.imshow(maskFloodFill, "Mask of floodFill"); // ����floodFill������mask
        // MatView.imshow(maskCopyTo, "Mask of copyTo"); // ����copyTo������mask
        final Mat imgRectROI = new Mat();
        input.copyTo(imgRectROI, maskCopyTo); // ��ȡԲ�ε�ROI
        // MatView.imshow(imgCircularROI, "Circular ROI"); // ��ʾԲ�ε�ROI
        return imgRectROI;
    }
    public Mat hasChanges2(Mat current) {
        int PIXEL_DIFF_THRESHOLD = 5;
        int IMAGE_DIFF_THRESHOLD = 5;
        //Mat base = new Mat();
        ////////Log.i(TAG, "****** en hasChanges ");
        Mat bg = new Mat();
        Mat cg = new Mat();
        Mat diff = new Mat();
        Mat tdiff = new Mat();
        if (base == null) {
            base = current.clone();
            return current;
        }
        Imgproc.cvtColor(base, bg, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(current, cg, Imgproc.COLOR_BGR2GRAY);
        Core.absdiff(bg, cg, diff);
        Imgproc.threshold(diff, tdiff, PIXEL_DIFF_THRESHOLD, 0.0, Imgproc.THRESH_TOZERO);
        if (Core.countNonZero(tdiff) <= IMAGE_DIFF_THRESHOLD) {
            return current;
        }
        Imgproc.threshold(diff, diff, PIXEL_DIFF_THRESHOLD, 255, Imgproc.THRESH_BINARY);
        Imgproc.dilate(diff, diff, new Mat());
        //Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5,5));
        //Imgproc.morphologyEx(diff, diff, Imgproc.MORPH_CLOSE, se);
        List<MatOfPoint> points = new ArrayList<MatOfPoint>();
        Mat contours = new Mat();
        Imgproc.findContours(diff, points, contours, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        int n = 0;
        for (Mat pm: points) {
            //log(lvl, "(%d) %s", n++, pm);
            ////////Log.i(TAG, "****** points " + n+++" "+pm);
            //printMatI(pm);
        }
        //log(lvl, "contours: %s", contours);
        ////////Log.i(TAG, "****** countours " +contours);
        //printMatI(contours);
        //the largest contour is found at the end of the contours vector
        //we will simply assume that the biggest contour is the object we are looking for.
        //vector< vector<Point> > largestContourVec;
        //largestContourVec.push_back(contours.at(contours.size()-1));
        MatOfPoint largestContour = points.get(points.size()-1);
        Point[] cornerpoints = points.get(points.size()-1).toArray();
        //double x = cornerpoints[0].x;
        //double y = ;
        //Core.rectangle(current,);
        // Get bounding rect of contour
        Rect rect = Imgproc.boundingRect(largestContour);

        // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
        rectangle(current, new Point(rect.x,rect.y), new Point(rect.x+rect.width
                ,rect.y+rect.height), new Scalar(255, 0, 0, 255), 3);
        Scalar color =  new Scalar( 255, 255, 0 );
        Imgproc.drawContours( current, points,-1,color,3);
        base = current.clone();
        return current;
    }

    public Mat hasChanges2a(Mat current) {
        int PIXEL_DIFF_THRESHOLD = 5;
        int IMAGE_DIFF_THRESHOLD = 5;
        //Mat base = new Mat();
        ////////Log.i(TAG, "****** en hasChanges ");
        Mat bg = new Mat();
        Mat cg = new Mat();
        Mat diff = new Mat();
        Mat tdiff = new Mat();
        if (base == null) {
            base = current.clone();
            return current;
        }
        Imgproc.cvtColor(base, bg, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(current, cg, Imgproc.COLOR_BGR2GRAY);
        Core.absdiff(bg, cg, diff);
        Imgproc.threshold(diff, tdiff, PIXEL_DIFF_THRESHOLD, 0.0, Imgproc.THRESH_TOZERO);
        if (Core.countNonZero(tdiff) <= IMAGE_DIFF_THRESHOLD) {
            return current;
        }
        Imgproc.threshold(diff, diff, PIXEL_DIFF_THRESHOLD, 255, Imgproc.THRESH_BINARY);
        Imgproc.dilate(diff, diff, new Mat());
        Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5,5));
        Imgproc.morphologyEx(diff, diff, Imgproc.MORPH_CLOSE, se);
        List<MatOfPoint> points = new ArrayList<MatOfPoint>();
        Mat contours = new Mat();
        Imgproc.findContours(diff, points, contours, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        int n = 0;
        for (Mat pm: points) {
            //log(lvl, "(%d) %s", n++, pm);
            ////////Log.i(TAG, "****** points " + n+++" "+pm);
            //printMatI(pm);
        }
        //log(lvl, "contours: %s", contours);
        ////////Log.i(TAG, "****** countours " +contours);
        //printMatI(contours);
        //the largest contour is found at the end of the contours vector
        //we will simply assume that the biggest contour is the object we are looking for.
        //vector< vector<Point> > largestContourVec;
        //largestContourVec.push_back(contours.at(contours.size()-1));
        MatOfPoint largestContour = points.get(points.size()-1);
        Point[] cornerpoints = points.get(points.size()-1).toArray();
        //double x = cornerpoints[0].x;
        //double y = ;
        //Core.rectangle(current,);
        // Get bounding rect of contour
        Rect rect = Imgproc.boundingRect(largestContour);

        // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
        rectangle(current, new Point(rect.x,rect.y), new Point(rect.x+rect.width
                ,rect.y+rect.height), new Scalar(255, 0, 0, 255), 3);
        Scalar color =  new Scalar( 255, 255, 255 );
        Imgproc.drawContours( current, points,-1,color,3);
        base = current.clone();
        return current;
    }

    public MainActivity() {
        ////////Log.i(TAG, "Instantiated new " + this.getClass());
    }
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ////////Log.i(TAG, "****** called onCreate v33 ");
        // Here, thisActivity is the current activity
        //// PERMISO PARA CAMARA
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        //// PERMISO PARA ESCRIBIR EN STORAGE EXTERNO
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = true;
        cfg.useCompass = true;

        cfg.r = 8;
        cfg.g = 8;
        cfg.b = 8;
        cfg.a = 8;

        //initialize(new CameraDemo(this), cfg);
        //smApplication = new SMApplication();
        initialize( smApplication, cfg);
        //initialize( new Main(), cfg);
        ///FileHandle fh =  Gdx.files.internal("SILVER DREAM.sm");
        //InputStream is = readAssetsFileToStream("SILVER DREAM.sm",this);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);/// falla si pongo esto suena doble y ahora vuelve a salir el error del segundo juego

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        /////////////////////////////////
        ////////////////////////////////

        botonStart = findViewById(R.id.buttonStart);
        botonShare = findViewById(R.id.buttonShare);
        botonSongs = findViewById(R.id.buttonSongs);
        botonSelectFile = findViewById(R.id.buttonSelectFile);
        botonOkLinks = findViewById(R.id.buttonOkLinks);

        ///botonSongs.setLeft(R.id.buttonStart);/// no funciono para lo que se queria
        botonStart.setText("Start");
        botonStart.setOnClickListener(this);
        botonShare.setOnClickListener(this);
        botonSelectFile.setOnClickListener(this);
        botonSongs.setOnClickListener(this);
        botonOkLinks.setOnClickListener(this);
        botonShare.setVisibility(View.GONE  );
        //inicializarBotonesNiveles();
        //////////////////////////////////////////////////
        //setSupportActionBar(toolbar);//// esto sirve con AppCompatActivity en el extends
        //let's create the delegate, passing the activity at both arguments (Activity, AppCompatCallback)
        delegate = AppCompatDelegate.create(this, this);

        //we need to call the onCreate() of the AppCompatDelegate
        delegate.onCreate(savedInstanceState);

        //we use the delegate to inflate the layout
        //delegate.setContentView(R.layout.activity_main);

        //Finally, let's add the Toolbar
        //Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        //toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        //delegate.setSupportActionBar(toolbar);
        ////////////////////////////////////////////

        //cargarSecuenciaGolpe();
        ///// para load stepmania files con libgdx
        // load assets
        //assets = new SMAssets();
        // load game
        ///game = new GameEngine(this);//// voy a cambiarlo de sitio pero me preocupa que falle si no va aki
        //game.loadSyncAssets();
        manager = new AssetManager();
        /*manager.load(urlFileAtlas, TextureAtlas.class);
        manager.load(urlFileArrow, Texture.class);
        manager.finishLoading();**/
        assManager = this.getAssets();
        contexto = this;
        cargarDialogoFilePicker();
        textLinksDowload = (TextView) findViewById(R.id.display_html_string);
        textLinksDowload.setMovementMethod(LinkMovementMethod.getInstance());
        textLinksDowload.setText(Html.fromHtml(LinksDowloads.htmlString1));
        textLinksDowload.setVisibility(View.GONE);
        scrollTextoLinks = (ScrollView)findViewById(R.id.scrollLayout1);
        scrollTextoLinks.setVisibility(View.GONE);
        tableLayoutScores = (TableLayout) findViewById(R.id.tableLayoutScores);
        tableLayoutScores.setVisibility(View.GONE);
        scrollLayoutTable = (ScrollView) findViewById(R.id.scrollLayoutTable);
        scrollLayoutTable.setVisibility(View.GONE);
        final SharedPreferences prefs = getSharedPreferences("beatar", Context.MODE_PRIVATE);
        //prefs.edit().putString("historico",null);
        //prefs.edit().clear();
        //prefs.edit().commit();
        //List<Juego> lstJuego = Juego.testHistoricoJuegosMock(prefs);
        lstJuego = Juego.getHistoricoFromJson(prefs);
        if( lstJuego != null){
            llenarTablaScores(lstJuego);
        }
        /*RelativeLayout layoutRotar =  (RelativeLayout) findViewById( R.id.LinearARotar );
        CoordinatorLayout mainLayout =  (CoordinatorLayout) findViewById( R.id.main_layout );
        int w = layoutRotar.getWidth();
        int h = layoutRotar.getHeight();
        //////Log.d(TAG, "******b  width "+w+" h "+h+" mainlayout "+findViewById(R.id.main_layout).getHeight()+" "+findViewById(R.id.main_layout).getWidth());
        layoutRotar.setRotation(90.0f);
        //layoutRotar.setTranslationX((w - h) / 2);
        //layoutRotar.setTranslationY((h - w) / 2);
        layoutRotar.setTranslationX(250);
        layoutRotar.setTranslationY(-100.0f);
        ViewGroup.LayoutParams params = layoutRotar.getLayoutParams();
        ViewGroup.LayoutParams paramsMain = mainLayout.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        paramsMain.height = 1200;
        //params.width = 500;/// esto distorsiona el ancho/alto del ultimo layout anidado donde estan los botones
        //params.width = 200;
        layoutRotar.setLayoutParams(params);*/

        /*ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) layoutRotar.getLayoutParams();
        lp.height = w;
        lp.width = h;
        //layoutRotar.requestLayout();*/
        //Juego.testGuardarPreferences();
        ///dialog.show();//// se muestra el dialogo del file picker, la idea es poner como accion de un boton
        if ( assets == null ){
            //   assets = new SMAssets(); /// los assets fallan y asi no fallaran es un gallo pasarlos al mat de opencv
        }
        if( !introYaSonando){
            mpIntro = MediaPlayer.create(this, R.raw.musicintroloop);
            mpIntro.start();
            //////Log.d(TAG,"******* a reproduciendo intro en oncreate");
            introYaSonando = true;
        }
    }
    public static InputStream readAssetsFileToStream(String fileName, Context context) {
        android.content.res.AssetManager assManager = context.getAssets();
        InputStream is = null;
        try {
            is = assManager.open(fileName);
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        InputStream isOutput = new BufferedInputStream(is);
        return isOutput;
    }
    private void inicializarBotonesNiveles(){

    }
    private int getFrontCameraId(){
        int camId = -1;
        //int numberOfCameras = new Camera2
        /*Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0;i < numberOfCameras;i++){
            Camera.getCameraInfo(i,ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                camId = i;
            }
        }*/

        return camId;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            //////Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, mLoaderCallback);
        } else {
            //////Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        ////////Log.i(TAG, "*********** posible salida onresume ");
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        ////////Log.i(TAG, "*********** posible salida ondestroy ");
    }
    @Override
    public void onBackPressed() {
        // your code.
        game.getLstPuntajes();
        ////////Log.i(TAG, "*********** posible salida onbackpressed ");
        finish();
        System.exit(0);
    }
    private Mat mFiltrada = null;
    public void onCameraViewStarted(int width, int height) {
        if ( mFiltrada == null){
            mFiltrada = new Mat(width, height, CvType.CV_8UC2);
        }
    }

    public void onCameraViewStopped() {
        if (mFiltrada != null){
            mFiltrada.release();
        }
    }

    public Mat onCameraFrameTest(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mRgba = hasChangesPartialTest(mRgba);
        /*if( mFiltrada != null){
            Imgproc.Canny();
        } else {
            return inputFrame.rgba();
        }*/
        mGray = inputFrame.gray();
        return mRgba.clone();
    }
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        /*if( mFiltrada != null){
            Imgproc.Canny();
        } else {
            return inputFrame.rgba();
        }*/
        //mGray = inputFrame.gray();
        boolean flipado = false;
        //testVolumen();
        copiaRgba = mRgba.clone();
        long tiempoActual = System.currentTimeMillis();
        long difTempEspera =  tiempoActual - tiempoFinEspera;
        long difTempMusica =  tiempoActual - tiempoFinMusica;
        long holguraEspera = 3000;// toco aumentar la holgura porque con la cancion de pacman falla
        //Log.i(TAG, "****** difTempMusica "+difTempMusica+" holguraEspera "+holguraEspera+" inicioJuego "+inicioJuego);
        boolean isMusicPlaying = true;
        if ( game != null ){
            //Log.i(TAG, "****** time song "+game.songPlayer.time+" is musicPlaying "+game.songPlayer.isMusicPlaying());
            isMusicPlaying = game.songPlayer.isMusicPlaying();
            if ( isMusicPlaying && difTempMusica > holguraEspera ){
                tiempoFinMusica = System.currentTimeMillis();
            }
        }
        if( inicioJuego  ){
            long tmpAntesHasChanges = System.currentTimeMillis();
            mRgba =  hasChanges3(mRgba);
            long tmpAntesStepmania = System.currentTimeMillis();
            Log.i(TAG, "****** time demora hasChanges "+(tmpAntesStepmania-tmpAntesHasChanges));
            primeraIntegracionConStepMania();
            long tmpDespuesStepmania = System.currentTimeMillis();
            Log.i(TAG, "****** time demora primeraAIntegracion "+(tmpDespuesStepmania-tmpAntesStepmania));
            flipado = true;///se flipea dentro del haschanges
        } else if ( !inicioJuego){
            flip(mRgba,mRgba,1);
            flipado = true;
        }
        //copiaRgba = mRgba.clone();
        if( !flipado){
            flip(mRgba,mRgba,1);
        }
        ///// toda la logica de mostrar informacion que modifique la pantalla debe ir despues de la logica del detector de movimiento para que no interfiera en el resultado
        //primeraIntegracionConStepMania();
        //mostrarTiempo(mRgba);
        //actualizarScore(mRgba);
        long tmpAntesBloqueMostrar = System.currentTimeMillis();
        mostrarScoreRecientes();
        mostrarBodyOutline();
        //mostrarScore2(Score.MARVELOUS);
        //mostrarReceptores();
        mostrarMensajeEspera(mRgba);
        //mostrarMensajeAciertoOError(mRgba);
        //mostrarNivel(mRgba);
        mostrarPuntajesStepmania();
        mostrarPuntajeStepmania(mRgba);
        mostrarInfoCancion(mRgba);
        mostrarReceptoresPoligono();
        mostrarReceptoresPoligonoTocado();
        mostrarReceptoresPoligonoAcertado();
        long tmpDespuesBloqueMostrar = System.currentTimeMillis();
        Log.i(TAG, "****** time demora bloque mostrar "+(tmpDespuesBloqueMostrar-tmpAntesBloqueMostrar));
        if( hayUnAcierto ){
            ////Log.i(TAG, "****** hayUnAcierto copiando mrgba a mat de imagen ");
            imgUltimoAcierto = mRgba.clone();
            //guardarImagenUltimoACierto();/// si guardo aki se relentiza y bajan los fps entonces toca en un hilo aparte
            hayUnAcierto = false;
            //mRgba =  hasChanges3(mRgba);/// se llama mostrar el topo aki para que sea mas inmediata la retroalimentacion de la animacion
            flipado = true;///se flipea dentro del haschanges
        }
        long tiempoFinal = System.currentTimeMillis();
        Log.i(TAG, "****** time demora onCameraFrame total "+(tiempoFinal-tiempoActual));
        return mRgba;
    }
    private void ocultarBotonesMenu(){
        layoutNiveles.setVisibility(View.GONE);
    }
    private void mostrarPuntajesStepmania(){
        if( game != null && game.getLstPuntajes() != null ){
            List<Float> lstPuntajes = game.getLstPuntajes();
            String strPuntajesStep = " ";
            if( lstPuntajes != null){
                for(Float puntaje : lstPuntajes){
                    strPuntajesStep+=puntaje+" ";
                }
                // //Log.i(TAG, "****** puntajes stepmania "+strPuntajesStep);
            }
        }
    }
    private void  inicializarNivel( int toposSimultaneos, int idNivel ){
        int velocidad = 1; int cantidadTopos = toposSimultaneos;
        if ( nivel == null ){
            nivel = new Nivel(velocidad, cantidadTopos, toposSimultaneos);
        }
        nivel.setIdNivel(idNivel);
        nivel.setToposSimultaneos(toposSimultaneos);
        ocultarBotonesMenu();
        nivelEscogido = true;
    }
    private ImageButton agregarBotonShare(){
        int height = 100;
        int width = 100;
        Bitmap bmp;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.btnshare);
        bmp = Bitmap.createScaledBitmap(bmp, width, height, true);
        ImageButton imageButton = new ImageButton(this);
        imageButton.setImageBitmap(bmp);
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        return  imageButton;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonStart:
                // do your code
                //inicioJuego = true;
                //startTime = System.currentTimeMillis();
                //timerRunnable.run();
                if( !inicioJuego && ! enEspera){
                    inicioEspera = System.currentTimeMillis();
                    timerHandler.removeCallbacksAndMessages(null);/// a ver si aki si sirve
                    timerHandler.postDelayed(timerEsperaRunnable, 3000);
                    botonStart.setVisibility(View.GONE);
                    botonSongs.setVisibility(View.GONE);
                    botonSelectFile.setVisibility(View.GONE);
                    textLinksDowload.setVisibility(View.GONE);
                    scrollTextoLinks.setVisibility(View.GONE);
                    tableLayoutScores.setVisibility(View.GONE);
                    scrollLayoutTable.setVisibility(View.GONE);
                    toques = 0;
                    puntajeAcumulado = 0;
                    contadorNotasUltimoMeasure = 0;
                    idUltimoMeasure = 0;//// dificil de pillar que esta variable no se reiniciaba para el new game y entonces el juego no se mostraba mientras que la musica si sonaba
                    ////game = new GameEngine(this);///para recordar, no puede ir aca minimo cuando empieze el hilo principal despues del hilo esperar
                    //toolbar.setVisibility(View.GONE);
                    if (lstPuntosRecientes != null){
                        lstPuntosRecientes.clear();
                    }
                    enEspera = true;
                    //ocultarBotonesMenu();///depronto esta logica se reuse con cosas de stepmania por eso no la vuelo
                    botonShare.setVisibility(View.GONE  );
                    nivelEscogido = true;
                    if( mpIntro != null ){
                        mpIntro.stop();
                    }
                }
                break;

            case R.id.buttonShare:
                // do your code
                onClickShare(findViewById(R.id.tutorial1_activity_java_surface_view));
                break;

            case R.id.buttonSelectFile:
                // do your code
                dialog.show();
                break;
            case R.id.buttonSongs:
                // do your code
                textLinksDowload.setVisibility(View.VISIBLE);
                scrollTextoLinks.setVisibility(View.VISIBLE);
                break;
            case R.id.buttonOkLinks:
                textLinksDowload.setVisibility(View.GONE);
                scrollTextoLinks.setVisibility(View.GONE);
            default:
                break;
        }

    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       /* menu.add("about");
        menu.add("compartir");
        return true;*/
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //menu.add(R.menu.menu_main);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v;
        final SharedPreferences prefs = getSharedPreferences("molegame", Context.MODE_PRIVATE);
        switch (item.getItemId()) {
            case R.id.action_settings:
                /*getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new Fragment_Settings()).addToBackStack(null)
                        .commit();*/
                //////Log.i("Fragment Settings "," elegido volume");
                builder = new AlertDialog.Builder(this);
                v = this.getLayoutInflater().inflate(R.layout.volume, null);
                final RadioGroup volume = (RadioGroup) v.findViewById(R.id.volume);
                final String selVolume = prefs.getString("volume", "off");
                //////Log.i("Fragment Settings "," elegido volume con anterioridad "+selVolume);
                if ( selVolume.equals("on")){
                    volume.check( R.id.on);
                } else if (selVolume.equals("off") ){
                    volume.check( R.id.off);
                }
                /*gender.check(
                        prefs.getString("gender", "notparticipate").equals("male") ? R.id.male :
                                R.id.female);*/

                builder.setView(v);
                builder.setTitle(R.string.volume);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int selVolumerInt = volume.getCheckedRadioButtonId();
                            //////Log.i("Fragment Settings ","******* elegido volume onclick "+selVolumerInt+" on "+R.id.on+" off "+R.id.off);
                            if ( selVolumerInt == R.id.on){
                                //////Log.i("Fragment Settings "," elegido volume on");
                                prefs.edit()
                                        .putString("volume","on").apply();
                            }else if ( selVolumerInt == R.id.off){
                                prefs.edit()
                                        .putString("volume","off").apply();
                            }
                            prefs.edit()
                                    .putString("volume :",
                                            volume.getCheckedRadioButtonId() == R.id.on ? "on" : "off")
                                    .apply();/*
                            preference.setSummary(getString(R.string.gender_summary,
                                    prefs.getString("gender", DEFAULT_GENDER)));
                            //prefs.setSummary(getString(R.string.step_size_summary, prefs.getString("gender",Fragment_Settings.DEFAULT_GENDER)));*/
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;

            case R.id.action_about:
                builder.setTitle(R.string.about);
                TextView tv = new TextView(this);
                tv.setPadding(10, 10, 10, 10);
                tv.setText(R.string.about_text_links);

                tv.setMovementMethod(LinkMovementMethod.getInstance());
                builder.setView(tv);
                builder.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSupportActionModeStarted(ActionMode actionMode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode actionMode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
}


