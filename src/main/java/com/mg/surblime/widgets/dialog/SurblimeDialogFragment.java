package com.mg.surblime.widgets.dialog;

import androidx.fragment.app.DialogFragment;
import android.view.ViewGroup;

/**
 * Created by moses on 5/1/18.
 */

public class SurblimeDialogFragment extends DialogFragment {

    @Override
    public void onStart() {
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        super.onStart();
    }
}
