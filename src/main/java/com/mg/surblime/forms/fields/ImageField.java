package com.mg.surblime.forms.fields;

import com.mg.surblime.forms.validators.ImageValidator;
import com.mg.surblime.widgets.ImageInputField;

import java.util.ArrayList;

public class ImageField extends FormField<ArrayList<String>, ImageValidator> {
    private ImageInputField imageInputField;

    private ImageFieldListener imageFieldListener;

    private boolean single = false;

    public ImageField(ArrayList<String> value) {
        super(value);
    }

    public ImageField() {
        this(new ArrayList<>());
    }


    public void setSingle(boolean single) {
        this.single = single;
    }

    public boolean isSingle() {
        return single;
    }

    public void setImageFieldListener(ImageFieldListener imageFieldListener) {
        this.imageFieldListener = imageFieldListener;
    }

    public ImageFieldListener getImageFieldListener() {
        return imageFieldListener;
    }

    public void setImageInputField(ImageInputField imageInputField) {
        this.imageInputField = imageInputField;
        if (imageFieldListener != null) {
            imageFieldListener.onInputAdded(imageInputField);
        }
    }

    public ImageInputField getImageInputField() {
        return imageInputField;
    }

    @Override
    public void setRawValue(String string) {

    }

    public void add(String imageUrl) {
        this.get().add(imageUrl);
        this.notifyValueChange();
    }

    public void remove(String imageUrl) {
        this.get().remove(imageUrl);
        this.notifyValueChange();
    }

    @Override
    public void set(ArrayList<String> strings) {
        if (strings != null) {
            super.set(strings);
        }
        this.notifyValueChange();
    }

    @Override
    public String getRawValue() {
        return null;
    }

    @Override
    public ImageValidator getValidator() {
        return new ImageValidator();
    }

    public interface ImageFieldListener {
        void onInputAdded(ImageInputField imageInputField);
    }
}
