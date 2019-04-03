package com.mg.surblime;

import android.content.res.Configuration;
import androidx.databinding.Bindable;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import android.view.View;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.LayoutManagers;

/**
 * Created by Moses Gitau on 3/16/18 at 11:48 AM.
 */

public abstract class ObservableRecyclerViewModel<T> extends ObservableViewModel<T> implements RecyclerViewModelInterface<T> {


    public static transient final int LOADING_MORE = 4;
    public transient ItemBinding<T> itemBinding;
    private transient @LayoutRes
    int emptyLayout = 0;
    @Bindable
    private transient String emptyMessage = "No items";


    @Bindable
    public transient OnItemClickListener<T> onItemClickListener = new OnItemClickListener<T>() {
        @Override
        public void onItemClick(View view, T t) {

        }

        @Override
        public boolean onItemLongClick(View view, T t) {
            return false;
        }
    };

    @Bindable
    public transient OnFilterTextChangedListener onFilterTextChangedListener = new OnFilterTextChangedListener() {
        @Override
        public void onTextChanged(String text) {
        }
    };

    public ObservableRecyclerViewModel() {
        itemBinding = ItemBinding.of(getVariableId(), getItemLayoutResource());
    }

    public ObservableRecyclerViewModel(int status) {
        super(status);
        itemBinding = ItemBinding.of(getVariableId(), getItemLayoutResource());
    }


    public void loadingMore() {
        status = LOADING_MORE;
        notifyPropertyChanged(BR.status);
    }


    public ObservableRecyclerViewModel<T> setOnItemClickListener(OnItemClickListener<T> listener) {
        notifyPropertyChanged(BR.onItemClickListener);
        this.onItemClickListener = listener;
        itemBinding = itemBinding.bindExtra(getItemListenerId(), listener);
        return this;
    }


    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }


    public interface OnItemClickListener<T> {
        void onItemClick(View view, T t);

        boolean onItemLongClick(View view, T t);
    }

    public interface OnFilterTextChangedListener {
        void onTextChanged(String text);
    }

    public LayoutManagers.LayoutManagerFactory getLayoutManagerFactory() {
        return getLayoutManagerFactory(Configuration.ORIENTATION_PORTRAIT);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_resource_collection;
    }


    public @LayoutRes
    int getEmptyLayout() {
        return emptyLayout;
    }

    public void setEmptyLayout(int emptyLayout) {
        this.emptyLayout = emptyLayout;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    @Override
    public BindingRecyclerViewAdapter<T> getAdapter() {
        return new BindingRecyclerViewAdapter<>();
    }

    public @IdRes
    int getFilterTextChangedListenerId() {
        return -1;
    }


    public String[] filterKeys(T t) {
        return new String[]{};
    }
}
