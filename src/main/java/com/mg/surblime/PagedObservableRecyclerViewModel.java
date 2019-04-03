package com.mg.surblime;


/**
 * Created by Moses Gitau on 3/22/18 at 4:23 PM.
 */

public abstract class PagedObservableRecyclerViewModel<T extends BaseModel> extends ObservableRecyclerViewModel<T> {

    public abstract int getPage();

    public abstract int getTotalPages();

    public abstract int getTotalResults();

    public abstract void setPage(int page);

}
