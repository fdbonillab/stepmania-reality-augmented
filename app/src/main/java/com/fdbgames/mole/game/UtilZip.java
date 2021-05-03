package com.fdbgames.mole.game;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UtilZip {
    static String TAG = "UtilZip";
    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs()){
                    Log.d(TAG, "***** dir isDirectory "+dir.isDirectory()+" dir mkdirs "+dir.mkdirs());
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());}
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }
    }
    public static void unzip(String strFileZip , String strDirectory){
        Log.d(TAG, "***** unzip en "+strDirectory);
        File folderDirectory = UtilFile.getFolderDirectory( strDirectory);
        File fileZip = new File(strFileZip);
        try {
            unzip(fileZip,folderDirectory);
            Log.d(TAG, "***** archivo descomprimido en "+strDirectory);
        } catch (IOException e) {
            Log.d(TAG, "***** fallo archivo descomprimido en "+strDirectory+" error "+e.getMessage());
            e.printStackTrace();
        }
    }
}
