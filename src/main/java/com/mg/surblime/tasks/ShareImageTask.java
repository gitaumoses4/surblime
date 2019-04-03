package com.mg.surblime.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ProgressBar;

/**
 * Created by moses on 5/1/18.
 */
public class ShareImageTask extends DownloadImageTask {

    public ShareImageTask(Context context) {
        super(context);
    }

    public ShareImageTask(Context context, ProgressBar progressBar) {
        super(context, progressBar);
    }

    public ShareImageTask(Context context, Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality){
        super(context, bitmap, compressFormat, quality);
    }

    @Override
    protected void onPostExecute(android.net.Uri uri) {
        super.onPostExecute(uri);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        getContext().startActivity(shareIntent);
    }
}
