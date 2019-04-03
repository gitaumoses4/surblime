package com.mg.surblime.api;

import com.mg.surblime.BaseModel;
import com.mg.surblime.ObservableViewModel;

/**
 * Created by Moses Gitau on 3/22/18 at 6:26 PM.
 */
public interface ViewModelDataListener<T extends ObservableViewModel<S>, S> extends ModelDataListener<T> {
}
