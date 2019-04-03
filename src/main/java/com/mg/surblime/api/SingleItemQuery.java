package com.mg.surblime.api;

import android.util.Log;

import com.mg.surblime.BaseModel;
import com.mg.surblime.SingleViewModel;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by moses on 5/3/18.
 */

public class SingleItemQuery<T extends SingleViewModel<S>, S> extends SurblimeAPIQuery<S> {

    private Class<T> itemModelClass;
    private ViewModelDataListener<T, S> viewModelDataListener;

    public SingleItemQuery(Class<S> errorClass) {
        super(errorClass);
    }

    public SingleItemQuery viewModelClass(Class<T> classOfT) {
        this.itemModelClass = classOfT;
        return this;
    }

    public SingleItemQuery<T, S> with(SurblimeAPI surblimeAPI) {
        super.with(surblimeAPI);
        return this;
    }

    @Override
    public SingleItemQuery<T, S> addQueryParameter(String key, String value) {
        super.addQueryParameter(key, value);
        return this;
    }

    public SingleItemQuery<T, S> invoke(String apiMethodName, Class... parameterTypes) {
        super.invoke(apiMethodName, parameterTypes);
        return this;
    }

    public SingleItemQuery<T, S> callback(ViewModelDataListener<T, S> modelDataListener) {
        this.viewModelDataListener = modelDataListener;
        return this;
    }

    public SingleItemQuery<T, S> args(Object... args) {
        super.args(args);
        return this;
    }

    @Override
    public SingleItemQuery<T, S> subscribe(String apiMethodName, Class<?>[] parameterTypes, Object... args) {
        super.subscribe(apiMethodName, parameterTypes, args);
        return this;
    }

    @Override
    public SingleItemQuery<T, S> cacheMode(CacheMode cacheMode) {
        super.cacheMode(cacheMode);
        return this;
    }

    @Override
    public void execute() {
        checkNonNull(itemModelClass, "Class of View Model cannot be null");
        super.execute();
    }

    @Override
    public void onResponse(Call<S> call, Response<S> response) {
        if (response.isSuccessful()) {
            try {
                T t = itemModelClass.newInstance();
                t.setModel(response.body());
                viewModelDataListener.onSuccess(t);
            } catch (InstantiationException | IllegalAccessException e) {
                viewModelDataListener.onFailure();
                e.printStackTrace();
                Log.e(TAG, "getSingleItemCallback", e);
            }
        } else {
            try {
                T t = itemModelClass.newInstance();
                t.setModel(getError(response));
                viewModelDataListener.onError(t);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<S> call, Throwable t) {
        viewModelDataListener.onFailure();
        Log.e(TAG, "getSingleItemCallback", t);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(S s) {
        try {
            T t = itemModelClass.newInstance();
            t.setModel(s);
            viewModelDataListener.onSuccess(t);
        } catch (InstantiationException | IllegalAccessException e) {
            viewModelDataListener.onFailure();
            e.printStackTrace();
            Log.e(TAG, "getSingleItemCallback", e);
        }
    }

    @Override
    public void onError(Throwable e) {
        viewModelDataListener.onFailure();
    }

    @Override
    public void onComplete() {

    }
}
