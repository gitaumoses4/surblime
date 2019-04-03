package com.mg.surblime;

import android.content.res.Configuration;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.LayoutManagers;

/**
 * Created by Moses Gitau on 3/20/18 at 10:21 AM.
 */

public abstract class FixedRecyclerViewModel<T extends BaseModel> extends BaseViewModel implements RecyclerViewModelInterface<T> {

    public List<T> list = new ArrayList<>();

    public ItemBinding<T> itemBinding;

    public FixedRecyclerViewModel() {
        itemBinding = ItemBinding.of(getVariableId(), getItemLayoutResource());
    }

    public LayoutManagers.LayoutManagerFactory getLayoutManagerFactory() {
        return getLayoutManagerFactory(Configuration.ORIENTATION_PORTRAIT);
    }

    @Override
    public List<T> getItems() {
        return list;
    }

    @Override
    public void addItems(List<T> items) {
        list.addAll(items);
    }

    @Override
    public void setItems(List<T> items) {
        this.list = items;
    }

    @Override
    public void addItem(T t) {
        list.add(t);
    }

    @Override
    public void updateItem(T t, int index) {
        list.set(index, t);
    }

    @Override
    public int getLayoutResource() {
        return 0;
    }

    @Override
    public BindingRecyclerViewAdapter<T> getAdapter() {
        return new BindingRecyclerViewAdapter<>();
    }
}
