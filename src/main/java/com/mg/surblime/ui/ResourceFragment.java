
package com.mg.surblime.ui;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mg.surblime.BR;
import com.mg.surblime.BaseViewModel;
import com.mg.surblime.R;
import com.mg.surblime.events.NetworkConnectionInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Moses Gitau on 3/20/18 at 10:46 AM.
 */

public abstract class ResourceFragment<T extends BaseViewModel> extends Fragment {

    public static final String TAG = ModelFragment.class.getSimpleName();
    protected final Class<T> viewModelClass;
    protected ViewDataBinding viewDataBinding;
    protected ViewDataBinding viewModelDataBinding;
    protected T current;

    public ResourceFragment(Class<T> viewModelClass) {
        this.viewModelClass = viewModelClass;
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public @LayoutRes
    int fragmentResourceLayout() {
        return R.layout.fragment_resource;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mainView = inflater.inflate(this.fragmentResourceLayout(), container, false);
        RelativeLayout include = mainView.findViewById(R.id.viewModelView);


        try {
            viewDataBinding = DataBindingUtil.bind(mainView);
            viewDataBinding.setVariable(BR.viewModel, current = viewModelClass.newInstance());

            try {
                View viewModelView;
                viewModelView = inflater.inflate(current.getLayoutResource(), null);
                viewModelView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                include.addView(viewModelView);
                viewModelDataBinding = DataBindingUtil.bind(viewModelView);
                viewModelDataBinding.setVariable(BR.viewModel, current);
            } catch (Exception e) {
            }

        } catch (java.lang.InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return mainView;
    }


    public ViewDataBinding getViewModelDataBinding() {
        return viewModelDataBinding;
    }

    public T getResource() {
        return current;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChange(NetworkConnectionInfo networkConnectionInfo) {
    }

    public void bind(T t) {
        viewDataBinding.setVariable(BR.viewModel, t);
        viewModelDataBinding.setVariable(BR.viewModel, t);
        viewDataBinding.executePendingBindings();
        viewModelDataBinding.executePendingBindings();
    }


    public View findViewById(@IdRes int id) {
        View first = viewModelDataBinding.getRoot().findViewById(id);
        return first == null ? viewDataBinding.getRoot().findViewById(id) : first;
    }
}

