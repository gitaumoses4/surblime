package com.mg.surblime;

import androidx.annotation.LayoutRes;

import java.io.Serializable;

/**
 * Created by Moses Gitau on 3/20/18 at 10:18 AM.
 */

public abstract class BaseViewModel<T> extends BaseModel implements Serializable {
    public abstract @LayoutRes int getLayoutResource();
}
