package com.mg.surblime.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by moses on 5/1/18.
 */

public class DownloadFileTask extends AsyncTask<String, Integer, Uri> {
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private final ProgressBar progressBar;

    public DownloadFileTask(Context context) {
        this(context, null);
    }

    public DownloadFileTask(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }


    @Override
    protected void onPreExecute() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (progressBar != null) {
            progressBar.setIndeterminate(false);
            progressBar.setProgress(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Requires two strings, on for the image url, and the other for the file path to store the image
     *
     * @param strings
     * @return
     */
    @Override
    protected Uri doInBackground(String... strings) {
        try {
            String source = strings[0];
            String destination = strings[1];
            File file = new File(destination);
            Uri uri;
            if (!file.exists()) {
                URL url = new URL(source);
                int fileSize = url.openConnection().getContentLength();

                if (fileSize < 0) {
                    fileSize = 1000000;
                }
                InputStream inputStream = new BufferedInputStream(url.openStream(), 10240);
                OutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[4096];
                int dataSize;
                int loadedSize = 0;
                while ((dataSize = inputStream.read(buffer)) > 0) {
                    loadedSize += dataSize;
                    publishProgress((int) (Math.round(loadedSize * 100.0 / fileSize)));
                    outputStream.write(buffer, 0, dataSize);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            }
            uri = Uri.fromFile(file);
            return uri;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Context getContext() {
        return context;
    }

}
