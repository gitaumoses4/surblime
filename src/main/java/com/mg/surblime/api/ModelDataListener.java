package com.mg.surblime.api;

/**
 * Created by Moses Gitau on 3/22/18 at 6:26 PM.
 */
public interface ModelDataListener<T> {
    void onSuccess(T t);

    void onFailure();

    void onError(T t);
}
