package com.mg.surblime.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.renderscript.ScriptIntrinsicBlur;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mg.surblime.R;
import com.mg.surblime.activities.SurblimeFragmentActivity;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Moses Gitau on 3/16/18 at 7:24 AM.
 */

public class Tools {
    public static final String PREFERENCE_FIRST_OPEN = "first_open";
    private static final String PREFERENCE_DARK_THEME = "dark_theme";


    public static int getWindowBackgroundColor(Activity activity) {
        TypedValue a = new TypedValue();
        activity.getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // windowBackground is a color
            return a.data;
        } else {
            // windowBackground is not a color, probably a drawable
            return 0;
        }
    }

    public static void startActivityFragment(Context context, Class<? extends Fragment> fragmentClass, String title) {
        Intent intent = new Intent(context, SurblimeFragmentActivity.class);
        intent.putExtra(SurblimeFragmentActivity.FRAGMENT_NAME, fragmentClass.getName());
        intent.putExtra(SurblimeFragmentActivity.ACTIVITY_TITLE, title);

        context.startActivity(intent);
    }

    public static void startActivityFragment(Context context, Class<? extends Fragment> fragmentClass) {
        startActivityFragment(context, fragmentClass, null);
    }

    @ColorInt
    public static int resolveColor(final Context context, @StyleRes final int style, @AttrRes final int attr, @ColorInt final int fallback) {
        final TypedArray ta = obtainTypedArray(context, style, attr);
        try {
            return ta.getColor(0, fallback);
        } finally {
            ta.recycle();
        }
    }

    @ColorInt
    public static int resolveColor(final Context context, @AttrRes final int attr, @ColorInt final int fallback) {
        return resolveColor(context, 0, attr, fallback);
    }

    public static void rateAppOnPlayStore(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    private static TypedArray obtainTypedArray(final Context context, @StyleRes final int style, @AttrRes final int attr) {
        final int[] tempArray = getTempArray();
        tempArray[0] = attr;
        return context.obtainStyledAttributes(style, tempArray);
    }

    private static final ThreadLocal<int[]> TEMP_ARRAY = new ThreadLocal<>();

    private static final int[] getTempArray() {
        int[] tempArray = TEMP_ARRAY.get();
        if (tempArray == null) {
            tempArray = new int[1];
            TEMP_ARRAY.set(tempArray);
        }
        return tempArray;
    }

    public static String getDeviceID(Context context) {
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return (md5(android_id) + "").toUpperCase();
    }

    public static void shareApp(Context context, String appName, String packageName) {
        shareText(context, appName, "Check out this app http://play.google.com/store/apps/details?id=" + packageName, "Share " + appName);
    }

    public static void sendEmail(Context context, String recipient, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", recipient, null));

        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, true);

        context.startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public static String getHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isFirstOpen(Context context) {
        return getSharedPreferences(context).getBoolean(PREFERENCE_FIRST_OPEN, true);
    }

    public static void setNotFirstOpen(Context context) {
        getSharedPreferences(context)
            .edit().putBoolean(PREFERENCE_FIRST_OPEN, false)
            .apply();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isDarkTheme(Context context) {
        return getSharedPreferences(context).getBoolean(PREFERENCE_DARK_THEME, false);
    }

    public static void setDarkTheme(Context context, boolean value) {
        getSharedPreferences(context)
            .edit()
            .putBoolean(PREFERENCE_DARK_THEME, value)
            .apply();
    }

    public static String getFirstChar(String string) {
        if (string == null || string.isEmpty()) {
            return "";
        } else {
            return String.valueOf(string.charAt(0));
        }
    }

    public static void shareText(Context context, String subject, String text, String title) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        context.startActivity(Intent.createChooser(intent, title));
    }

    public static void getOpenFacebookIntent(Context context, String fbID, String userName) {
        Intent intent;
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + fbID));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + userName));
        }
        context.startActivity(intent);
    }

    public enum ConnectivityStatus {
        WIFI, MOBILE_DATA, NOT_CONNECTED
    }

    public static final int[] PRIMARY_COLORS = new int[]{
        R.color.md_red_500,
        R.color.md_green_500,
        R.color.md_pink_500,
        R.color.md_blue_grey_500,
        R.color.md_purple_500,
        R.color.md_pink_500,
        R.color.md_teal_500,
        R.color.md_deep_purple_500,
        R.color.md_blue_500,
        R.color.md_brown_500,
        R.color.md_deep_orange_500,
        R.color.md_cyan_500,
        R.color.md_indigo_500
    };
    private static Picasso picasso;

    public static void restartApplication(Context context, Class<?> activityClass) {
        Intent mStartActivity = new Intent(context, activityClass);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public static ConnectivityStatus getConnectivityStatus(Context context) {
        final ConnectivityManager contextSystemService = (ConnectivityManager)
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = contextSystemService.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = contextSystemService.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting()) {
            return ConnectivityStatus.WIFI;
        } else if (mobile.isConnectedOrConnecting()) {
            return ConnectivityStatus.MOBILE_DATA;
        } else {
            return ConnectivityStatus.NOT_CONNECTED;
        }
    }

    public static Picasso getPicasso(Context context) {
        if (picasso == null) {
            picasso = Picasso.with(context.getApplicationContext());
        }
        return picasso;
    }

    public static int getWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    public static int getHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }


    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static int getDominantColor(Drawable drawable) {
        return getDominantColor(drawableToBitmap(drawable));
    }

    public static void saveToFile(String string, File file, boolean append) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        if (append) {
            fileWriter.append(string);
        } else {
            fileWriter.write(string);
        }
        fileWriter.flush();
        fileWriter.close();
    }

    public static String readFromFile(File file) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "";
        while ((line = reader.readLine()) != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }

    public static String md5(String string) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(string.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static int getDominantColor(Bitmap bitmap) {
        List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).generate().getSwatches();
        List<Palette.Swatch> swatches = new ArrayList<>(swatchesTemp);
        Collections.sort(swatches, new Comparator<Palette.Swatch>() {
            @Override
            public int compare(Palette.Swatch swatch1, Palette.Swatch swatch2) {
                return swatch2.getPopulation() - swatch1.getPopulation();
            }
        });
        return swatches.size() > 0 ? swatches.get(0).getRgb() : 0;
    }

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
            Math.min(r, 255),
            Math.min(g, 255),
            Math.min(b, 255));
    }

    public static class BlurBuilder {
        private static final float BITMAP_SCALE = 0.4f;
        private static final float BLUR_RADIUS = 7.5f;

        public static Bitmap blur(View v) {
            return blur(v.getContext(), getScreenshot(v));
        }

        public static Bitmap blur(Context context, Drawable drawable) {
            return blur(context, drawableToBitmap(drawable));
        }

        public static Bitmap blur(Context ctx, Bitmap image) {
            int width = Math.round(image.getWidth() * BITMAP_SCALE);
            int height = Math.round(image.getHeight() * BITMAP_SCALE);

            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript rs = RenderScript.create(ctx);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);

            Canvas canvas = new Canvas(outputBitmap);
            canvas.drawColor(Color.argb(128, 0, 0, 0));
            return outputBitmap;
        }

        private static Bitmap getScreenshot(View v) {
            Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.draw(c);
            return b;
        }
    }

    public static void insertImageIntoGallery(ContentResolver cr, Bitmap source, String title, String description, String filePath) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATA, filePath);

        Uri url;
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            source.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            long id = ContentUris.parseId(url);
            // Wait until MINI_KIND thumbnail is generated.
            Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
            // This is for backward compatibility.
            storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
        } catch (Exception ignored) {
            Log.e(Tools.class.getSimpleName(), "insertImageToGallery", ignored);
        }
    }

    private static void storeThumbnail(ContentResolver cr, Bitmap source, long id, float width, float height, int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
            source.getWidth(),
            source.getHeight(), matrix,
            true
        );

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND, kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            assert thumbOut != null;
            thumbOut.close();
        } catch (FileNotFoundException ex) {
            Log.e("IMAGE_COMPRESSION_ERROR", "File not found");
            ex.printStackTrace();
        } catch (IOException ex) {
            Log.e("IMAGE_COMPRESSION_ERROR", "IO Exception");
            ex.printStackTrace();
        }
    }
}