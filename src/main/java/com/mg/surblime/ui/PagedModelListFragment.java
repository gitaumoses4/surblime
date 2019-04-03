package com.mg.surblime.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.mg.surblime.BaseModel;
import com.mg.surblime.PagedObservableRecyclerViewModel;


/**
 * Created by Moses Gitau on 3/22/18 at 4:41 PM.
 */

public abstract class PagedModelListFragment<T extends PagedObservableRecyclerViewModel<S>, S extends BaseModel> extends ModelListFragment<T, S> {

    public PagedModelListFragment(Class<T> classOfT) {
        super(classOfT);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (recyclerView != null) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        if (current.getStatus() != T.LOADING_MORE && current.getPage() < current.getTotalPages()) {
                            current.loadingMore();
                            current.setPage(current.getPage() + 1);
                            loadResource();
                        }
                    }
                }
            });
        }

    }

    @Override
    public void onSuccess(T t) {
        if (current.getStatus() == T.LOADING_MORE) {
            current.addItems(t.getItems());
            recyclerView.smoothScrollBy(0, 200);
            current.success();

            recyclerView.getAdapter().notifyItemRangeInserted(current.getItems().size() - t.getItems().size(), t.getItems().size());
        } else {
            super.onSuccess(t);
        }
    }

    @Override
    public void loadResource(boolean loadFromCache) {
        loadResource(loadFromCache, current.getPage());
    }

    @Override
    public void loadResource() {
        loadResource(true, current.getPage());
    }

    @Override
    public void onFailure() {
        if (current.getPage() <= 1) {
            super.onFailure();
        } else {
            current.success();
        }
    }

    public abstract void loadResource(boolean useCache, int page);
}
