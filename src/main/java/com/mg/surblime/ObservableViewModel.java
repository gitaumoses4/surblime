package com.mg.surblime;

import androidx.databinding.Bindable;
import androidx.annotation.LayoutRes;

/**
 * Created by Moses Gitau on 3/17/18 at 11:05 AM.
 */

public abstract class ObservableViewModel<T> extends BaseViewModel<T> {

    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int LOADING = 3;

    @Bindable
    protected transient int status = LOADING;

    @Bindable
    private transient OnRefreshListener refreshListener = () -> {
    };

    public void setStatus(int status) {
        this.status = status;
    }

    public ObservableViewModel() {
        super();
    }

    public ObservableViewModel(int status) {
        this();
        this.status = status;
    }


    public int getStatus() {
        return status;
    }

    public void loading() {
        status = LOADING;
        notifyPropertyChanged(BR.status);
    }

    public void success() {
        status = SUCCESS;
        notifyPropertyChanged(BR.status);
    }

    public void error() {
        status = ERROR;
        notifyPropertyChanged(BR.status);
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public OnRefreshListener getOnRefreshListener() {
        return refreshListener;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.refreshListener = listener;
        notifyPropertyChanged(BR.refreshListener);
    }

    public @LayoutRes
    int getErrorLayout() {
        return R.layout.error;
    }

    public @LayoutRes
    int getLoadingLayout() {
        return R.layout.loading;
    }
}
