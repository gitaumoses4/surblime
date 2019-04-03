package com.mg.surblime.api;

import android.util.Log;

import com.mg.surblime.BaseModel;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by moses on 5/3/18.
 */

@SuppressWarnings("unchecked")
public class SimpleQuery<T> extends SurblimeAPIQuery<T> {

    private ModelDataListener<T> modelDataListener;

    public SimpleQuery(Class<T> errorClass) {
        super(errorClass);
    }

    public SimpleQuery<T> with(SurblimeAPI surblimeAPI) {
        super.with(surblimeAPI);
        return this;
    }

    @Override
    public SimpleQuery<T> addQueryParameter(String key, String value) {
        super.addQueryParameter(key, value);
        return this;
    }

    public SimpleQuery<T> invoke(String apiMethodName, Class... parameterTypes) {
        super.invoke(apiMethodName, parameterTypes);
        return this;
    }

    public SimpleQuery<T> callback(ModelDataListener<T> modelDataListener) {
        this.modelDataListener = modelDataListener;
        return this;
    }

    public SimpleQuery<T> args(Object... args) {
        super.args(args);
        return this;
    }

    @Override
    public SimpleQuery<T> subscribe(String apiMethodName, Class<?>[] parameterTypes, Object... args) {
        super.subscribe(apiMethodName, parameterTypes, args);
        return this;
    }

    @Override
    public SimpleQuery<T> cacheMode(CacheMode cacheMode) {
        super.cacheMode(cacheMode);
        return this;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (modelDataListener != null) {
            if (response.isSuccessful())
                modelDataListener.onSuccess(response.body());
            else
                modelDataListener.onError(getError(response));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (modelDataListener != null) {
            modelDataListener.onFailure();
        }
        Log.e("SurblimeAPI", "API Failure", t);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        modelDataListener.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        modelDataListener.onFailure();
        Log.e("Moses", "onError", e);
    }

    @Override
    public void onComplete() {

    }
}
