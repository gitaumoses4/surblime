package com.mg.surblime.tasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ProgressBar;

/**
 * Created by moses on 5/1/18.
 */

public class SetImageAsTask extends DownloadImageTask {
    public SetImageAsTask(Context context) {
        super(context);
    }

    public SetImageAsTask(Context context, ProgressBar progressBar) {
        super(context, progressBar);
    }

    public SetImageAsTask(Context context, Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {
        super(context, bitmap, compressFormat, quality);
    }

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("mimeType", "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getContext().startActivity(Intent.createChooser(intent, "Set as:"));
    }
}
