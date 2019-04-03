package com.mg.surblime.activities;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.mg.surblime.R;
import com.mg.surblime.util.Tools;

/**
 * Created by Moses Gitau on 3/25/18 at 11:04 AM.
 */

public abstract class DrawerActivity extends ToolbarActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(toolbarId());

        drawer = findViewById(drawerId());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(navigationViewId());
        navigationView.setNavigationItemSelectedListener(this);

        setDarkThemeSwitcher();
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public @IdRes
    int getDarkThemeSwitchId() {
        return -1;
    }

    public void setDarkThemeSwitcher() {
        if (getDarkThemeSwitchId() == -1) {
            return;
        }
        MenuItem darkThemeSwitch = ((NavigationView) findViewById(navigationViewId())).getMenu().findItem(getDarkThemeSwitchId());

        if (darkThemeSwitch.getActionView() != null && darkThemeSwitch.getActionView() instanceof SwitchCompat) {
            SwitchCompat switchCompat = (SwitchCompat) darkThemeSwitch.getActionView();
            switchCompat.setChecked(Tools.isDarkTheme(this));
            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Tools.setDarkTheme(DrawerActivity.this, isChecked);
                    recreate();
                    closeDrawer();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(drawerId());
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void openDrawer() {
        DrawerLayout drawer = findViewById(drawerId());
        drawer.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        DrawerLayout drawer = findViewById(drawerId());
        drawer.closeDrawer(GravityCompat.START);
    }

    public abstract @IdRes
    int drawerId();

    public abstract @IdRes
    int navigationViewId();

    @Override
    public int getStyleTheme() {
        return Tools.isDarkTheme(this) ? getDarkTheme() : getLightTheme();
    }

    public abstract @StyleRes
    int getDarkTheme();

    public abstract @StyleRes
    int getLightTheme();
}
