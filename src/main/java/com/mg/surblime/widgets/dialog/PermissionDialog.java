package com.mg.surblime.widgets.dialog;

import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mg.surblime.R;

/**
 * Created by moses on 4/30/18.
 */

public class PermissionDialog extends SurblimeDialogFragment {

    private ImageView iconView;
    private TextView messageTextView;
    private Button notNowButton;
    private Button continueButton;
    private View.OnClickListener continueListener;
    private View.OnClickListener notNowListener;

    private @DrawableRes
    int iconId = 0;
    private String message = null;
    private boolean cancellable = true;

    public PermissionDialog setIcon(@DrawableRes int iconId) {
        this.iconId = iconId;
        return this;
    }

    public PermissionDialog setMessage(@StringRes int messageId) {
        return setMessage(getString(messageId));
    }

    public PermissionDialog setMessage(String string) {
        this.message = string;
        return this;
    }

    public PermissionDialog onContinue(final View.OnClickListener onClickListener) {
        continueListener = onClickListener;
        return this;
    }

    public PermissionDialog onCancel(final View.OnClickListener onClickListener) {
        notNowListener = onClickListener;
        return this;
    }

    public PermissionDialog cancelable(boolean cancellable) {
        this.cancellable = cancellable;
        return this;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        iconView = view.findViewById(R.id.icon);
        messageTextView = view.findViewById(R.id.message);
        continueButton = view.findViewById(R.id.continueButton);
        notNowButton = view.findViewById(R.id.notNowButton);

        if (iconId != 0) {
            iconView.setImageResource(iconId);
        }
        if (message != null) {
            messageTextView.setText(message);
        }
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (continueListener != null) {
                    continueListener.onClick(v);
                }
                dismiss();
            }
        });

        notNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notNowListener != null) {
                    notNowListener.onClick(v);
                }
                dismiss();
            }
        });
        setCancelable(cancellable);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_request_permissions, container, false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
