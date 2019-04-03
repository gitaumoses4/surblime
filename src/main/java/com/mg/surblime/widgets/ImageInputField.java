package com.mg.surblime.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.cloudinary.android.UploadRequest;
import com.mg.surblime.R;
import com.mg.surblime.util.CloudinaryImage;

import java.util.HashMap;

import static com.mg.surblime.widgets.CloudinaryImageView.DEFAULT_PADDING;

public class ImageInputField extends FormFieldLayout implements CloudinaryImageView.ImageUploadListener {


    private HashMap<Uri, CloudinaryImageView> images = new HashMap<>();
    private Uri imageAdded = null;
    private ViewGroup.LayoutParams imageSizeParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private int imageWidth, imageHeight, progressWidth;
    private int progressColor, successColor;

    private int maxImages;

    private HorizontalScrollView horizontalScrollView;
    private LinearLayout mainLayout;

    private AddNewImage addNewImage;
    private ImageInputFieldListener imageInputFieldListener;
    private boolean setupComplete = false;
    private FrameLayout addImageLayout;

    private ImageChangeListener imageChangeListener;


    public ImageInputField(Context context) {
        super(context);
    }

    public ImageInputField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageInputField(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageInputField(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void addImage(Uri uri, UploadRequest uploadRequest) {
        imageAdded = uri;
        CloudinaryImageView cloudinaryImageView;
        images.put(uri, cloudinaryImageView = new CloudinaryImageView(getContext()));
        cloudinaryImageView.setUploadRequest(uploadRequest);
        this.requestLayout();

        if (images.size() == maxImages) {
            addImageLayout.setVisibility(GONE);
        }
    }

    @Override
    public void init(AttributeSet attributeSet) {
        super.init(attributeSet);
        horizontalScrollView = new HorizontalScrollView(getContext());
        addNewImage = new AddNewImage(getContext());
        mainLayout = new LinearLayout(getContext()) {
            @Override
            public void onViewAdded(View child) {
                super.onViewAdded(child);
                new Handler().postDelayed(() -> horizontalScrollView.smoothScrollBy(addNewImage.getWidth() + (DEFAULT_PADDING * 2), 0), 200);
            }
        };

        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.ImageInputField);
        TypedValue accent = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorAccent, accent, true);
        imageWidth = typedArray.getDimensionPixelSize(R.styleable.ImageInputField_imageWidth, 200);
        imageHeight = typedArray.getDimensionPixelSize(R.styleable.ImageInputField_imageHeight, 200);
        progressWidth = typedArray.getDimensionPixelSize(R.styleable.ImageInputField_progressWidth, 5);
        progressColor = typedArray.getColor(R.styleable.ImageInputField_progressColor, accent.data);
        successColor = typedArray.getColor(R.styleable.ImageInputField_successColor, getResources().getColor(R.color.md_green_500));
        maxImages = typedArray.getInt(R.styleable.ImageInputField_maxImages, 20);

        imageSizeParams = new LayoutParams(imageWidth, imageHeight);
        setOrientation(HORIZONTAL);
        typedArray.recycle();
    }

    public void remove(Uri uri) {
        CloudinaryImageView cloudinaryImageView = images.get(uri);
        mainLayout.removeView(cloudinaryImageView);
        images.remove(uri);
        addImageLayout.setVisibility(VISIBLE);
        requestLayout();

        if (imageChangeListener != null) {
            imageChangeListener.imageRemoved(cloudinaryImageView.getCloudinaryImage());
        }
    }

    public void setImageChangeListener(ImageChangeListener imageChangeListener) {
        this.imageChangeListener = imageChangeListener;
    }


    public ImageChangeListener getImageChangeListener() {
        return imageChangeListener;
    }

    private void addPlaceholder() {
        addImageLayout = new FrameLayout(new ContextThemeWrapper(getContext(), R.style.Surblime_Clickable));
        addImageLayout.setLayoutParams(imageSizeParams);
        addImageLayout.addView(addNewImage);
        addImageLayout.getLayoutParams().width = imageWidth;
        addImageLayout.getLayoutParams().height = imageHeight;

        int framePadding = DEFAULT_PADDING;
        addImageLayout.setPadding(framePadding, framePadding, framePadding, framePadding);

        addImageLayout.setOnClickListener((view -> this.loadImage()));
        mainLayout.addView(addImageLayout, mainLayout.getChildCount() - 1, imageSizeParams);
    }

    private void drawImage() {
        CloudinaryImageView cloudinaryImageView = images.get(imageAdded);
        cloudinaryImageView.setLayoutParams(imageSizeParams);
        cloudinaryImageView.getLayoutParams().width = imageWidth;
        cloudinaryImageView.getLayoutParams().height = imageHeight;
        cloudinaryImageView.setSuccessColor(successColor);
        cloudinaryImageView.setProgressWidth(progressWidth);
        cloudinaryImageView.setProgressColor(progressColor);
        cloudinaryImageView.setImage(imageAdded);
        cloudinaryImageView.setImageUploadListener(this);
        cloudinaryImageView.setOnClickListener(view -> {
            if (imageInputFieldListener != null) {
                imageInputFieldListener.onClickImage(cloudinaryImageView.getUri());
            }
        });

        mainLayout.addView(cloudinaryImageView, mainLayout.getChildCount() - 1, imageSizeParams);
    }

    private void loadImage() {
        if (imageInputFieldListener != null) {
            imageInputFieldListener.onImageRequest();
        }
    }

    public void setResult(Uri uri, UploadRequest uploadRequest) {
        addImage(uri, uploadRequest);
    }

    public void setImageInputFieldListener(ImageInputFieldListener imageInputFieldListener) {
        this.imageInputFieldListener = imageInputFieldListener;
    }

    public ImageInputFieldListener getImageInputFieldListener() {
        return imageInputFieldListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (!setupComplete) {
            this.addView(horizontalScrollView);
            horizontalScrollView.addView(mainLayout);
            mainLayout.setOrientation(HORIZONTAL);
            this.addPlaceholder();
            setupComplete = true;
        }

        if (imageAdded != null) {
            drawImage();
            imageAdded = null;
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onSuccess(CloudinaryImage cloudinaryImage) {
        if (this.imageChangeListener != null) {
            this.imageChangeListener.imageAdded(cloudinaryImage);
        }
    }

    @Override
    public void onProgress(double percentage) {

    }

    @Override
    public void onError() {

    }

    public interface ImageChangeListener {
        void imageAdded(CloudinaryImage cloudinaryImage);

        void imageRemoved(CloudinaryImage cloudinaryImage);
    }

    public interface ImageInputFieldListener {
        void onImageRequest();

        void onClickImage(Uri uri);
    }
}
