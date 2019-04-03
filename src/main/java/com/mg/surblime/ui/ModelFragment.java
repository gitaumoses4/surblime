package com.mg.surblime.ui;

import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.mg.surblime.BaseModel;
import com.mg.surblime.ObservableViewModel;
import com.mg.surblime.R;
import com.mg.surblime.api.ViewModelDataListener;
import com.mg.surblime.events.NetworkConnectionInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Moses Gitau on 3/20/18 at 10:46 AM.
 */

public abstract class ModelFragment<T extends ObservableViewModel<S>, S extends BaseModel> extends ResourceFragment<T> implements ViewModelDataListener<T, S>, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean swipeToRefreshEnabled = true;

    public ModelFragment(Class<T> viewModelClass) {
        super(viewModelClass);
    }

    public boolean canLoadResource() {
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (canLoadResource()) {
            swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setEnabled(false);
            swipeRefreshLayout.setOnRefreshListener(this);
            loadResource();
        }
    }

    public ViewDataBinding getViewModelDataBinding() {
        return viewModelDataBinding;
    }

    public T getResource() {
        return current;
    }

    @Override
    public void onSuccess(T t) {
        current = t;
        if (current != null) {
            current.success();
            bind(current);
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(swipeToRefreshEnabled);
        }
    }

    public void loadResource() {
        loadResource(true);
    }

    public abstract void loadResource(boolean loadFromCache);

    @Override
    public void onFailure() {
        try {
            swipeRefreshLayout.setRefreshing(false);
            current = viewModelClass.newInstance();
            current.error();
            current.setOnRefreshListener(() -> {
                loadResource();
                current.loading();
                bind(current);
            });
            bind(current);
        } catch (java.lang.InstantiationException | IllegalAccessException e) {
            Log.e(TAG, "onFailure", e);
        }
    }

    public boolean isSwipeToRefreshEnabled() {
        return swipeToRefreshEnabled;
    }

    public void setSwipeToRefreshEnabled(boolean swipeToRefreshEnabled) {
        this.swipeToRefreshEnabled = swipeToRefreshEnabled;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChange(NetworkConnectionInfo networkConnectionInfo) {
        if (networkConnectionInfo.isConnected() && this.canLoadResource()) {
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        loadResource();
    }
}
