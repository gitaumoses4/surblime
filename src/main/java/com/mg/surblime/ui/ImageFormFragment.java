package com.mg.surblime.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.view.View;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.UploadRequest;
import com.mg.surblime.forms.FormContext;
import com.mg.surblime.forms.FormResponse;
import com.mg.surblime.forms.fields.FormField;
import com.mg.surblime.forms.fields.ImageField;
import com.mg.surblime.widgets.ImageInputField;

import static android.app.Activity.RESULT_OK;

public abstract class ImageFormFragment<T extends FormContext<T, S>, S extends FormResponse> extends FormFragment<T, S> {

    private static final int ACTION_CHOOSE_IMAGE = 1204;
    private static final int ACTION_REQUEST_EXTERNAL_PERMISSION = 1783;

    private ImageInputField current;

    public ImageFormFragment(Class<T> viewModelClass) {
        super(viewModelClass);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (FormField field : getResource().fields.values()) {
            if (field instanceof ImageField) {
                ImageField imageField = (ImageField) field;

                imageField.setImageFieldListener(imageInputField -> {
                    imageInputField.setImageInputFieldListener(new ImageInputField.ImageInputFieldListener() {
                        @Override
                        public void onImageRequest() {
                            chooseImage(imageInputField);
                        }

                        @Override
                        public void onClickImage(Uri uri) {
                            PreviewImageBottomFragment previewImageBottomFragment = PreviewImageBottomFragment
                                .getInstance(uri);
                            previewImageBottomFragment.show(getChildFragmentManager(), PreviewImageBottomFragment.class.getName());
                            previewImageBottomFragment.setActionListener(imageInputField::remove);
                        }
                    });

                });

            }
        }
    }

    private void chooseImage(ImageInputField imageInputField) {
        current = imageInputField;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            proceedWithImageRequest();
        } else {
            requestExternalPermission();
        }
    }

    private void requestExternalPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ACTION_REQUEST_EXTERNAL_PERMISSION);
    }

    private void proceedWithImageRequest() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ACTION_CHOOSE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTION_CHOOSE_IMAGE: {
                if (resultCode == RESULT_OK) {
                    Uri selected = data.getData();
                    if (current != null) {
                        current.setResult(selected, configureCloudinary(
                            MediaManager.get()
                                .upload(selected)
                        ));
                    }
                }
            }
        }
    }


    public UploadRequest configureCloudinary(UploadRequest uploadRequest) {
        return uploadRequest;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACTION_REQUEST_EXTERNAL_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceedWithImageRequest();
                } else {
                    requestExternalPermission();
                }
            }
            break;
        }
    }
}
