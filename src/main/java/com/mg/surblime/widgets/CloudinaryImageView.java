package com.mg.surblime.widgets;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cloudinary.android.UploadRequest;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.mg.surblime.R;
import com.mg.surblime.util.CloudinaryImage;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CloudinaryImageView extends FrameLayout implements UploadCallback {

    private boolean uploading = false;

    private int progressWidth, progressColor, successColor;


    private View uploadingView;
    private int index = 0;

    public static final int DEFAULT_PADDING = 30;
    private Uri uri;
    private UploadRequest uploadRequest;
    private CircularProgressBar circularProgressBar;
    private CloudinaryImage cloudinaryImage;

    private ImageUploadListener imageUploadListener;

    public CloudinaryImageView(@NonNull Context context) {
        super(context);
    }

    public CloudinaryImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CloudinaryImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CloudinaryImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    public void setSuccessColor(int successColor) {
        this.successColor = successColor;
    }


    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setImageUploadListener(ImageUploadListener imageUploadListener) {
        this.imageUploadListener = imageUploadListener;
    }

    public ImageUploadListener getImageUploadListener() {
        return imageUploadListener;
    }

    public CloudinaryImage getCloudinaryImage() {
        return cloudinaryImage;
    }

    private void setup(Uri uri) {
        CircleImageView circleImageView = new CircleImageView(getContext());
        circularProgressBar = (CircularProgressBar)
            LayoutInflater.from(getContext())
                .inflate(R.layout.image_upload_progress, this, false);
        circularProgressBar.setProgressBarWidth(progressWidth);
        circularProgressBar.setProgress(0f);
        circularProgressBar.setColor(progressColor);

        Picasso.with(this.getContext())
            .load(uri)
            .into(circleImageView);

        this.addView(circleImageView);
        circleImageView.setImageResource(R.drawable.test);

        uploadingView = new View(getContext());
        uploadingView.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
        uploadingView.setAlpha(0.6f);

        this.addView(uploadingView);

        this.addView(circularProgressBar, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        int framePadding = DEFAULT_PADDING - progressWidth;
        this.setPadding(framePadding, framePadding, framePadding, framePadding);

        int imagePadding = progressWidth + 2;
        circleImageView.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
    }

    public void setImage(Uri uri) {
        this.uri = uri;
        setup(uri);
        if (this.uploadRequest != null) {
            this.uploadRequest.callback(this)
                .dispatch();
        }
    }

    public Uri getUri() {
        return uri;
    }

    @Override
    public void onStart(String s) {
        uploadingView.setVisibility(VISIBLE);
        uploading = true;
        if (imageUploadListener != null) {
            this.imageUploadListener.onStart();
        }
    }

    @Override
    public void onProgress(String s, long l, long l1) {
        double percentage = Math.ceil((double) l / l1);
        circularProgressBar.setProgress((float) percentage * 100);
        if (imageUploadListener != null) {
            this.imageUploadListener.onProgress(percentage * 100);
        }
    }

    @Override
    public void onSuccess(String s, Map map) {
        this.uploading = false;
        uploadingView.setVisibility(GONE);
        circularProgressBar.setColor(successColor);

        CloudinaryImage cloudinaryImage = new CloudinaryImage();
        cloudinaryImage.signature = String.valueOf(map.get("signature"));
        cloudinaryImage.format = String.valueOf(map.get("format"));
        cloudinaryImage.resourceType = String.valueOf(map.get("resourceType"));
        cloudinaryImage.secureUrl = String.valueOf(map.get("secure_url"));
        try {
            cloudinaryImage.createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(String.valueOf(map.get("created_at")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cloudinaryImage.type = String.valueOf(map.get("type"));
        cloudinaryImage.version = String.valueOf(map.get("version"));
        cloudinaryImage.accessMode = String.valueOf(map.get("access_mode"));
        cloudinaryImage.url = String.valueOf(map.get("url"));
        cloudinaryImage.publicId = String.valueOf(map.get("public_id"));
        cloudinaryImage.tags = String.valueOf(map.get("tags")).split(",");
        cloudinaryImage.originalFileName = String.valueOf(map.get("original_filename"));
        cloudinaryImage.bytes = Long.parseLong(String.valueOf(map.get("bytes")));
        cloudinaryImage.width = Integer.parseInt(String.valueOf(map.get("width")));
        cloudinaryImage.height = Integer.parseInt(String.valueOf(map.get("height")));
        cloudinaryImage.eTag = String.valueOf(map.get("etag"));
        cloudinaryImage.placeholder = Boolean.parseBoolean(String.valueOf("placeholder"));

        this.cloudinaryImage = cloudinaryImage;
        if (this.imageUploadListener != null) {
            this.imageUploadListener.onSuccess(cloudinaryImage);
        }
    }

    @Override
    public void onError(String s, ErrorInfo errorInfo) {
        if (this.imageUploadListener != null) {
            this.imageUploadListener.onError();
        }
    }

    @Override
    public void onReschedule(String s, ErrorInfo errorInfo) {

    }

    public void setUploadRequest(UploadRequest uploadRequest) {
        this.uploadRequest = uploadRequest;
    }

    public interface ImageUploadListener {
        void onStart();

        void onSuccess(CloudinaryImage cloudinaryImage);

        void onProgress(double percentage);

        void onError();
    }
}
