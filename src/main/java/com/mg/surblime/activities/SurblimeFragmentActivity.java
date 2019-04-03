package com.mg.surblime.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mg.surblime.R;

public class SurblimeFragmentActivity extends ToolbarActivity {
    public static final String FRAGMENT_NAME = "fragment_name";
    public static final String ACTIVITY_TITLE = "title";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String fragmentName = getIntent().getStringExtra(FRAGMENT_NAME);
        String title = getIntent().getStringExtra(ACTIVITY_TITLE);
        if (title != null && !title.isEmpty()) {
            getSupportActionBar().setTitle(title);
        }
        try {
            Fragment fragment = (Fragment) Class.forName(fragmentName).newInstance();

            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContent, fragment, fragmentName)
                .commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.fragment_activity);
    }

    @Override
    public int toolbarId() {
        return R.id.toolbar;
    }

    @Override
    public boolean overrideTheme() {
        return false;
    }

    @Override
    public int getStyleTheme() {
        return R.style.Surblime_NoActionBar;
    }
}
