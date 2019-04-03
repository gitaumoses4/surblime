package com.mg.surblime.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by moses on 5/1/18.
 */

public class DownloadImageTask extends DownloadFileTask {
    private int quality;
    private Bitmap.CompressFormat compressionFormat;
    private Bitmap bitmap;

    public DownloadImageTask(Context context) {
        super(context);
    }

    public DownloadImageTask(Context context, ProgressBar progressBar) {
        super(context, progressBar);
    }

    public DownloadImageTask(Context context, Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {
        super(context);
        this.bitmap = bitmap;
        this.compressionFormat = compressFormat;
        this.quality = quality > 100 || quality < 0 ? 100 : quality;
    }

    /**
     * To saveList image from bitmap, pass in the destination only,
     * <p>
     * To saveList image from the internet, pass in the source (Internet URL) and the destination to saveList the file
     *
     * @param strings
     * @return
     */
    @Override
    protected Uri doInBackground(String... strings) {
        if (bitmap == null) {
            return super.doInBackground(strings);
        } else {
            try {
                File file;
                FileOutputStream fileOutputStream = new FileOutputStream(file = new File(strings[0]));
                bitmap.compress(compressionFormat, quality, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                return Uri.fromFile(file);
            } catch (Exception ex) {
                Log.e(DownloadImageTask.class.getSimpleName(), "doInBackground", ex);
                return null;
            }
        }
    }
}
