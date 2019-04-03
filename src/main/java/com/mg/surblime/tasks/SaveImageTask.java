package com.mg.surblime.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ProgressBar;


/**
 * Created by moses on 5/1/18.
 */

public class SaveImageTask extends DownloadImageTask {

    private final ContentResolver contentResolver;
    private final boolean addToGallery;
    private String title;
    private String description;
    private String filePath;

    public SaveImageTask(Context context, boolean addToGallery) {
        this(context, addToGallery, null);
    }

    public SaveImageTask(Context context, boolean addToGallery, ProgressBar progressBar) {
        super(context, progressBar);
        contentResolver = context.getContentResolver();
        this.addToGallery = addToGallery;
    }

    public SaveImageTask(Context context, boolean addToGallery, Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {
        super(context, bitmap, compressFormat, quality);
        this.addToGallery = addToGallery;
        contentResolver = context.getContentResolver();
    }

    public String getDescription() {
        return description;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getTitle() {
        return title;
    }

    /**
     * The third and fourth strings will represent the Image Title and Description respectively
     *
     * @param strings
     * @return
     */
    @Override
    protected Uri doInBackground(String... strings) {
        filePath = strings[1];
        title = strings[2];
        description = strings[3];
        return super.doInBackground(strings);
    }

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);
        if (addToGallery) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, title);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
            values.put(MediaStore.Images.Media.DESCRIPTION, description);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.DATA, filePath);
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
    }
}
