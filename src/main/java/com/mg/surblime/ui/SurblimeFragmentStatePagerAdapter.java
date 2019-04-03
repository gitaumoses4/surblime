package com.mg.surblime.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by moses on 5/1/18.
 * <p>
 * This viewpager adapter allows you to get the current registered fragment in the position
 */

public abstract class SurblimeFragmentStatePagerAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    private SparseArray<T> registeredFragments = new SparseArray<>();

    public SurblimeFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public T instantiateItem(ViewGroup container, int position) {
        T fragment = (T) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public T getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
