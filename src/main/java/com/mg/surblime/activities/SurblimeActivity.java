package com.mg.surblime.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

import com.mg.surblime.events.NetworkConnectionInfo;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Moses Gitau on 3/25/18 at 3:00 PM.
 */

public abstract class SurblimeActivity extends AppCompatActivity implements InternetConnectivityListener {

    private boolean fullScreen = false;

    private static final int REQUEST_CODE_PERMISSION = 1001;


    private SparseArray<OnPermissionResult> permissionResultSparseArray = new SparseArray<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (this.overrideTheme()) {
            setTheme(getStyleTheme());
        }
        super.onCreate(savedInstanceState);
        InternetAvailabilityChecker.init(this);
        InternetAvailabilityChecker.getInstance().addInternetConnectivityListener(this);
    }


    public void setFullscreen(boolean fullscreen) {
        if (fullscreen) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
            this.fullScreen = true;

        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            this.fullScreen = false;
        }
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void toggleFullscreenMode() {
        setFullscreen(!fullScreen);
    }

    public boolean checkForPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean hasPermissions(String... permissions) {
        for (String permission : permissions) {
            if (!checkForPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    public void requestPermission(OnPermissionResult onPermissionResult, @NonNull String... permissions) {
        int requestCode = REQUEST_CODE_PERMISSION + permissionResultSparseArray.size();
        permissionResultSparseArray.put(requestCode, onPermissionResult);
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int index = 0; index < permissionResultSparseArray.size(); index++) {
            int key = permissionResultSparseArray.keyAt(index);
            if (requestCode == key) {
                ArrayList<String> denied = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        denied.add(permissions[i]);
                    }
                }
                if (denied.isEmpty()) {
                    permissionResultSparseArray.get(key).permissionGranted();
                } else {
                    permissionResultSparseArray.get(key).permissionDenied(denied);
                }
            }
        }
    }

    public interface OnPermissionResult {
        void permissionGranted();

        void permissionDenied(ArrayList<String> denied);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealShow(View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        viewRoot.setVisibility(View.VISIBLE);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
    }

    public boolean overrideTheme() {
        return true;
    }

    public abstract @StyleRes
    int getStyleTheme();


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        EventBus.getDefault().post(new NetworkConnectionInfo(isConnected));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(overrideDefaultFont() ? CalligraphyContextWrapper.wrap(newBase) : newBase);
    }

    public boolean overrideDefaultFont() {
        return true;
    }
}
