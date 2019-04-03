package com.mg.surblime.api;

import android.util.Log;

import com.mg.surblime.BaseModel;
import com.mg.surblime.ObservableRecyclerViewModel;

import java.io.IOException;
import java.lang.annotation.Annotation;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by moses on 5/3/18.
 */

public class ListQuery<T extends ObservableRecyclerViewModel<S>, S> extends SurblimeAPIQuery<T> {
    private ViewModelDataListener<T, S> viewModelDataListener;

    public ListQuery(Class<T> errorClass) {
        super(errorClass);
    }


    public ListQuery<T, S> with(SurblimeAPI surblimeAPI) {
        super.with(surblimeAPI);
        return this;
    }

    @Override
    public ListQuery<T, S> addQueryParameter(String key, String value) {
        super.addQueryParameter(key, value);
        return this;
    }

    public ListQuery<T, S> invoke(String apiMethodName, Class... parameterTypes) {
        super.invoke(apiMethodName, parameterTypes);
        return this;
    }

    public ListQuery<T, S> callback(ViewModelDataListener<T, S> modelDataListener) {
        this.viewModelDataListener = modelDataListener;
        return this;
    }

    public ListQuery<T, S> args(Object... args) {
        super.args(args);
        return this;
    }

    @Override
    public ListQuery<T, S> subscribe(String apiMethodName, Class<?>[] parameterTypes, Object... args) {
        super.subscribe(apiMethodName, parameterTypes, args);
        return this;
    }

    @Override
    public ListQuery<T, S> cacheMode(CacheMode cacheMode) {
        super.cacheMode(cacheMode);
        return this;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            viewModelDataListener.onSuccess(response.body());
        } else {
            viewModelDataListener.onError(getError(response));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        viewModelDataListener.onFailure();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        viewModelDataListener.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        viewModelDataListener.onFailure();
    }

    @Override
    public void onComplete() {

    }
}
