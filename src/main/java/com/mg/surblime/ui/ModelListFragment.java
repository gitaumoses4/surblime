package com.mg.surblime.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mg.surblime.BaseModel;
import com.mg.surblime.ObservableRecyclerViewModel;
import com.mg.surblime.R;
import com.mg.surblime.api.ViewModelDataListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moses Gitau on 3/16/18 at 6:41 PM.
 */

public abstract class ModelListFragment<T extends ObservableRecyclerViewModel<S>, S extends BaseModel> extends ModelFragment<T, S> implements ViewModelDataListener<T, S>,
    ObservableRecyclerViewModel.OnItemClickListener<S>, ObservableRecyclerViewModel.OnFilterTextChangedListener {

    public static final String TAG = ModelListFragment.class.getSimpleName();
    protected RecyclerView recyclerView;
    private Activity activity;

    private List<S> items = new ArrayList<>();

    public ModelListFragment(Class<T> viewModelClass) {
        super(viewModelClass);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerView);

        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onItemClick(View view, S s) {
        onItemClick(view, s, getIndex(s));
    }

    @Override
    public boolean onItemLongClick(View view, S s) {
        onItemLongClick(view, s, getIndex(s));
        return false;
    }

    public void onItemLongClick(View view, S s, int index) {

    }

    public abstract void onItemClick(View view, S s, int index);

    @Override
    public void onSuccess(T t) {
        super.onSuccess(t);
        items = t.getItems();
        checkOrientation();
        if (current != null) {
            current.setOnItemClickListener(this);
            if (current.getFilterTextChangedListenerId() != -1) {
                getViewModelDataBinding().setVariable(current.getFilterTextChangedListenerId(), this);
            }
        }
    }

    public void setEmptyLayout(@LayoutRes int layoutId) {
        if (current.getItems().isEmpty()) {
            current.setEmptyLayout(layoutId);
            bind(current);
        }
    }

    public void setEmptyMessage(String message) {
        if (current.getItems().isEmpty()) {
            current.setEmptyMessage(message);
            bind(current);
        }
    }

    public int getIndex(S s) {
        return current.getItems().indexOf(s);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public void checkOrientation() {
        if (recyclerView != null && activity != null && isAdded() && current != null)
            recyclerView.setLayoutManager(current.getLayoutManagerFactory(getResources().getConfiguration().orientation).create(recyclerView));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        checkOrientation();

    }

    public RecyclerView.Adapter getAdapter() {
        if (recyclerView == null) {
            return null;
        }
        return recyclerView.getAdapter();
    }

    public void notifyDataSetChanged() {
        getAdapter().notifyDataSetChanged();
        bind(current);
    }

    public void notifyItemRemoved(int index) {
        getAdapter().notifyItemRemoved(index);
        bind(current);
    }

    public void notifyItemChanged(int index) {
        getAdapter().notifyItemChanged(index);
        bind(current);
    }

    public void notifyItemInserted(int index) {
        getAdapter().notifyItemInserted(index);
        bind(current);
    }

    @Override
    public void onTextChanged(String text) {
        List<S> result = new ArrayList<>();
        for (S s : items) {
            boolean found = false;
            for (String string : current.filterKeys(s)) {
                if (string != null && string.toLowerCase().contains(text.toLowerCase())) {
                    found = true;
                }
            }
            if (found) {
                result.add(s);
            }
        }
        current.setItems(result);
        notifyDataSetChanged();
    }

}
