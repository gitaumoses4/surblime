package com.mg.surblime.ui;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.mg.surblime.R;
import com.squareup.picasso.Picasso;

public class PreviewImageBottomFragment extends BottomSheetDialogFragment {
    public static final String BUNDLE_IMAGE_URI = "image_uri";


    private ImageView imageView;
    private Button cancel, delete;
    private ActionListener actionListener;

    private Uri uri;


    public static PreviewImageBottomFragment getInstance(Uri uri) {
        PreviewImageBottomFragment previewImageBottomFragment = new PreviewImageBottomFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_IMAGE_URI, uri.toString());

        previewImageBottomFragment.setArguments(bundle);
        return previewImageBottomFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uri = Uri.parse(getArguments().getString(BUNDLE_IMAGE_URI));
        imageView = view.findViewById(R.id.imageView);

        if (uri != null) {
            Picasso.with(getContext())
                .load(uri)
                .into(imageView);
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.image_placeholder));
        }

        cancel = view.findViewById(R.id.cancelButton);
        delete = view.findViewById(R.id.removeButton);

        cancel.setOnClickListener((view1 -> dismiss()));
        delete.setOnClickListener((view1 -> {
            dismiss();
            if (actionListener != null) {
                actionListener.onRemove(uri);
            }
        }));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_image_or_remove, container, false);
    }


    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public ActionListener getActionListener() {
        return actionListener;
    }

    public interface ActionListener {
        void onRemove(Uri uri);
    }
}

