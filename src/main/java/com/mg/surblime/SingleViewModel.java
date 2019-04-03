package com.mg.surblime;

import androidx.databinding.Bindable;


/**
 * Created by Moses Gitau on 3/17/18 at 11:03 AM.
 */

public abstract class SingleViewModel<T> extends ObservableViewModel<T> {

    @Bindable
    public T model;

    public SingleViewModel(){

    }

    public SingleViewModel(int status){
        super(status);
    }

    public void setModel(T model) {
        this.model = model;
        notifyPropertyChanged(BR.model);
    }

}
