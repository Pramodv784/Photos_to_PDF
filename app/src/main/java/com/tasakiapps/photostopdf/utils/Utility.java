package com.tasakiapps.photostopdf.utils;

import java.io.File;

public class Utility {
    public static void runGc(){
        try {
            System.gc();
        } catch(Exception e){}
        try {
            Runtime.getRuntime().gc();
        } catch(Exception e){}
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (null != fileOrDirectory) {
            if (fileOrDirectory.isDirectory()) {
                for (File child : fileOrDirectory.listFiles()) {
                    deleteRecursive(child);
                }
            }
            if (fileOrDirectory.exists()) {
                final File to = new File(fileOrDirectory.getAbsolutePath() + System.currentTimeMillis());
                fileOrDirectory.renameTo(to);
                to.delete();
                //fileOrDirectory.delete();
            }
        }
    }
}
