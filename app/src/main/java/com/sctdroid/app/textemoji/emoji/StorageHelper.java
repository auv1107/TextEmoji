package com.sctdroid.app.textemoji.emoji;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.sctdroid.app.textemoji.utils.compact.Compact;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lixindong on 4/20/17.
 */

public class StorageHelper {
    public static final String ROOT = Environment.getExternalStorageDirectory() + "/TextEmoji/";
    public static final String DIR_TMP = ROOT + "tmp/";
    public static final String DIR_GALLERY = ROOT + "gallery/";


    public static boolean checkAndMkdir(String path) {
        // make app dirs
        File dir = new File(path);
        return dir.exists() || dir.mkdirs();
    }

    public static Uri saveBitmap(Bitmap bitmap, String filename, String DIR) {
        if (!checkAndMkdir(DIR)) {
            return Uri.EMPTY;
        }
        // save emoji
        File f = new File(DIR + filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Uri.EMPTY;
        }
        return Compact.getInstance().fromFile(f);
    }

    public static Uri saveBitmap(Bitmap bitmap, String filename) {
        return saveBitmap(bitmap, filename, DIR_TMP);
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
