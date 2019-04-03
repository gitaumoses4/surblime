package com.mg.surblime.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.mg.surblime.R;

/**
 * Created by Moses Gitau on 4/3/18 at 12:38 PM.
 */

public class STabLayout extends FrameLayout {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private int count = 0;
    private AppCompatRadioButton[] radioButtons;

    private int tabTextColor = Color.BLACK;
    private int selectedTabTextColor = Color.WHITE;
    private int tabBackgroundColor = Color.WHITE;
    private int selectedTabBackgroundColor = Color.BLACK;
    private int strokeWidth;

    public STabLayout(@NonNull Context context) {
        super(context);
    }

    public STabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setAttrs(context, attrs);

    }

    public STabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttrs(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public STabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setAttrs(context, attrs);
    }

    private void setAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.STabLayout);

        tabTextColor = typedArray.getColor(R.styleable.STabLayout_tabTextColor, tabTextColor);
        selectedTabTextColor = typedArray.getColor(R.styleable.STabLayout_selectedTabTextColor, selectedTabTextColor);
        tabBackgroundColor = typedArray.getColor(R.styleable.STabLayout_tabBackgroundColor, tabBackgroundColor);
        selectedTabBackgroundColor = typedArray.getColor(R.styleable.STabLayout_selectedTabBackgroundColor, selectedTabBackgroundColor);
        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.STabLayout_strokeWidth, getResources().getDimensionPixelSize(R.dimen.one_dp));

        typedArray.recycle();
    }

    public void setUpWithViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        initialize();
        viewPager.addOnAdapterChangeListener((viewPager1, oldAdapter, newAdapter) -> initialize());
    }

    private void initialize() {
        this.pagerAdapter = viewPager.getAdapter();
        removeAllViews();
        if (pagerAdapter != null) {
            count = pagerAdapter.getCount();
            radioButtons = new AppCompatRadioButton[count];
            createUI();
            synchronizeViewPager();
        }
    }

    @SuppressWarnings("deprecation")
    private void createUI() {
        RadioGroup radioGroup = new RadioGroup(getContext());
        HorizontalScrollView.LayoutParams radioGroupLayoutParams;
        radioGroup.setLayoutParams(radioGroupLayoutParams = new HorizontalScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        radioGroup.setGravity(Gravity.CENTER);

        for (int i = 0; i < count; i++) {
            final AppCompatRadioButton radioButton = new AppCompatRadioButton(new ContextThemeWrapper(getContext(), R.style.STab));
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioButton.setLayoutParams(layoutParams);

            GradientDrawable gradientDrawable;
            if (i == 0) {
                gradientDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.custom_tab_left);
            } else if (i == count - 1) {
                gradientDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.custom_tab_right);
            } else {
                gradientDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.custom_tab_center);
            }
            radioButton.setBackground(gradientDrawable);
            radioButton.setButtonDrawable(R.color.transparent);
            checkButton(false, radioButton);
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkButton(isChecked, radioButton);
                }
            });
            if (i > 0 && i < count - 1) {
                layoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.stab_center_margin), 0, getResources().getDimensionPixelSize(R.dimen.stab_center_margin), 0);
            }
            radioButton.setText(pagerAdapter.getPageTitle(i));
            radioGroup.addView(radioButton);
            radioButtons[i] = radioButton;
            final int index = i;
            radioButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPager.setCurrentItem(index, true);
                }
            });
        }
        radioButtons[0].setChecked(true);

        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getContext());
        horizontalScrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = getResources().getDimensionPixelSize(R.dimen.padding_medium);
        horizontalScrollView.setPadding(0, padding, 0, padding);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);


        radioGroupLayoutParams.gravity = Gravity.CENTER;

        horizontalScrollView.addView(radioGroup);
        addView(horizontalScrollView);
    }

    private void checkButton(boolean isChecked, AppCompatRadioButton radioButton) {
        GradientDrawable background = (GradientDrawable) radioButton.getBackground();
        if (isChecked) {
            background.setColor(selectedTabBackgroundColor);
            radioButton.setTextColor(selectedTabTextColor);
        } else {
            background.setColor(tabBackgroundColor);
            background.setStroke(strokeWidth, selectedTabBackgroundColor);
            radioButton.setTextColor(tabTextColor);
        }
    }

    private void synchronizeViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                radioButtons[position].setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
