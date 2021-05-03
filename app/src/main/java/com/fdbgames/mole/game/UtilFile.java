package com.fdbgames.mole.game;

import android.os.Environment;
import android.util.Log;

import com.fdbgames.mole.sa.io.files.Chart;

import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.opencv.imgproc.Imgproc.LINE_8;

public class UtilFile {
    static String TAG ="UtilFile";

    public static void createFolder(String dirFolder ){
        String dir = Environment.getExternalStorageDirectory() + "/"+dirFolder+"/";
        File mydir = new File( dir );
        if(!mydir.exists()){
            mydir.mkdirs();
            Log.d(TAG, "*****dir. juego creado ");
            Log.i(TAG, "******dir. juego creado ");
        } else {
            Log.d("error", "****** dir. already exists");
        }

            //mydir.createNewFile();
            //create file
            File file = new File(dir, "testfile.txt");
            Log.d(TAG, "***** archivo creado en "+dir);

    }
    public static void createFolder2(String dirFolder ){
        File externalStorageDir = Environment.getExternalStorageDirectory();
        File playNumbersDir = new File(externalStorageDir, "PlayNumbers");
        File myFile = new File(playNumbersDir, "mysdfile.xml");

        if (!playNumbersDir.exists()) {
            playNumbersDir.mkdirs();
            Log.d(TAG, "***** se hace playNumbersDir.mkdirs() ");
        }
        if(!myFile.exists()){
            try {
                Log.d(TAG, "***** 22 segundo forma de crear archivo ");
                myFile.createNewFile();
            } catch (IOException e) {
                Log.d(TAG, "***** error creando archivo en "+e.getMessage());
                for( int i=0;i < e.getStackTrace().length;i++){
                    Log.d(TAG, "***** error creando archivo en "+e.getStackTrace()[i].toString());
                }
                e.printStackTrace();
            }
        }

    }
    public static boolean existeFolder(String dirFolder ){
        File mydir = new File(Environment.getExternalStorageDirectory() + "/"+dirFolder+"/");
        if(!mydir.exists())
            return false;
        else
            return true;
    }
    public static File getFolderDirectory( String dirFolder){
        File mydir = new File(Environment.getExternalStorageDirectory() + "/"+dirFolder+"/");
        if(!mydir.exists())
            return null;
        else
            return mydir;
    }
    public static void mostrarCharts(Map<String, Chart> mapCharts){
        for (Map.Entry<String, Chart> entry : mapCharts.entrySet()) {
            Chart unChart = entry.getValue();
            Log.d(TAG, "***** chart "+unChart.toString());
        }
    }
}
