package com.mg.surblime.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.mg.surblime.api.ModelDataListener;
import com.mg.surblime.forms.FormContext;
import com.mg.surblime.R;
import com.mg.surblime.forms.FormResponse;

/**
 * Created by moses on 2/21/19.
 */

public abstract class FormFragment<T extends FormContext<T, S>, S extends FormResponse> extends ResourceFragment<T>
    implements ModelDataListener<S>, FormContext.FormListener<T> {
    public FormFragment(Class<T> viewModelClass) {
        super(viewModelClass);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.getResource() != null) {
            this.getResource().addFormListener(this);
        }
    }

    @Override
    public void onSubmit(View view, T t) {
        getResource().loading();
        getResource().setHasErrors(false);
        this.submitForm(view, t);
    }

    public abstract void submitForm(View view, T t);


    @Override
    public int fragmentResourceLayout() {
        return R.layout.fragment_form;
    }

    @Override
    public void onSuccess(S s) {
        getResource().setResponseData(s);
        getResource().success();
        getResource().setHasErrors(false);
        this.handleSuccess(getResource(), s);
    }

    @Override
    public void onFailure() {
        getResource().setHasErrors(true);
        getResource().error();
        this.handleFailure(getResource());
    }

    @Override
    public void onError(S s) {
        getResource().setHasErrors(true);
        getResource().error();
        this.handleError(getResource(), s);
    }

    public abstract void handleSuccess(T t, S s);

    public abstract void handleFailure(T t);

    public abstract void handleError(T t, S s);
}
