package com.mg.surblime;


import androidx.databinding.BaseObservable;
import android.view.View;

import java.io.Serializable;

/**
 * Created by Moses Gitau on 3/16/18 at 1:29 PM.
 */

public class BaseModel extends BaseObservable implements Serializable {

    /**
     * Override this to detect clicks in child views
     *
     * @param view
     */
    public void onClick(View view) {

    }
}
