package com.mg.surblime;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.LayoutManagers;

/**
 * Created by Moses Gitau on 3/22/18 at 4:14 PM.
 */

public interface RecyclerViewModelInterface<T> {

    @IdRes
    int getVariableId();

    @LayoutRes
    int getItemLayoutResource();

    @IdRes
    int getItemListenerId();

    LayoutManagers.LayoutManagerFactory getLayoutManagerFactory(int orientation);

    LayoutManagers.LayoutManagerFactory getLayoutManagerFactory();

    List<T> getItems();

    void addItems(List<T> items);

    void setItems(List<T> items);

    void addItem(T t);

    void updateItem(T t, int index);

    BindingRecyclerViewAdapter<T> getAdapter();
}
