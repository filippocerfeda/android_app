/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.filippocerfeda.savingdatatutorial.slidingtabs;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

class SlidingTabStrip extends LinearLayout {

    private static final int DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 2;
    private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 0x26;
    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 8;
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF33B5E5;

    private static final int DEFAULT_DIVIDER_THICKNESS_DIPS = 1;
    private static final byte DEFAULT_DIVIDER_COLOR_ALPHA = 0x20;
    private static final float DEFAULT_DIVIDER_HEIGHT = 0.5f;

    private final int bottomBorderThickness;
    private final Paint bottomBorderPaint;

    private final int selectedIndicatorThickness;
    private final Paint selectedIndicatorPaint;

    private final int defaultBottomBorderColor;

    private final Paint dividerPaint;
    private final float mividerHeight;

    private int selectedPosition;
    private float selectionOffset;

    private SlidingTabLayout.TabColorizer customTabColorizer;
    private final SimpleTabColorizer defaultTabColorizer;

    SlidingTabStrip(Context context) {
        this(context, null);
    }

    SlidingTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        final float density = getResources().getDisplayMetrics().density;

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorForeground, outValue, true);
        final int themeForegroundColor =  outValue.data;

        defaultBottomBorderColor = setColorAlpha(themeForegroundColor,
                DEFAULT_BOTTOM_BORDER_COLOR_ALPHA);

        defaultTabColorizer = new SimpleTabColorizer();
        defaultTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR);
        defaultTabColorizer.setDividerColors(setColorAlpha(themeForegroundColor,
                DEFAULT_DIVIDER_COLOR_ALPHA));

        bottomBorderThickness = (int) (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density);
        bottomBorderPaint = new Paint();
        bottomBorderPaint.setColor(defaultBottomBorderColor);

        selectedIndicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
        selectedIndicatorPaint = new Paint();

        mividerHeight = DEFAULT_DIVIDER_HEIGHT;
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth((int) (DEFAULT_DIVIDER_THICKNESS_DIPS * density));
    }

    void setCustomTabColorizer(SlidingTabLayout.TabColorizer customTabColorizer) {
        this.customTabColorizer = customTabColorizer;
        invalidate();
    }

    void setSelectedIndicatorColors(int... colors) {
        this.customTabColorizer = null;
        defaultTabColorizer.setIndicatorColors(colors);
        invalidate();
    }

    void setDividerColors(int... colors) {
        this.customTabColorizer = null;
        defaultTabColorizer.setDividerColors(colors);
        invalidate();
    }

    void onViewPagerPageChanged(int position, float positionOffset) {
        selectedPosition = position;
        selectionOffset = positionOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight();
        final int childCount = getChildCount();
        final int dividerHeightPx = (int) (Math.min(Math.max(0f, mividerHeight), 1f) * height);
        final SlidingTabLayout.TabColorizer tabColorizer = customTabColorizer != null
                ? customTabColorizer
                : defaultTabColorizer;

        if (childCount > 0) {
            View selectedTitle = getChildAt(selectedPosition);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();
            int color = tabColorizer.getIndicatorColor(selectedPosition);

            if (selectionOffset > 0f && selectedPosition < (getChildCount() - 1)) {
                int nextColor = tabColorizer.getIndicatorColor(selectedPosition + 1);
                if (color != nextColor) {
                    color = blendColors(nextColor, color, selectionOffset);
                }

                View nextTitle = getChildAt(selectedPosition + 1);
                left = (int) (selectionOffset * nextTitle.getLeft() +
                        (1.0f - selectionOffset) * left);
                right = (int) (selectionOffset * nextTitle.getRight() +
                        (1.0f - selectionOffset) * right);
            }

            selectedIndicatorPaint.setColor(color);

            canvas.drawRect(left, height - selectedIndicatorThickness, right,
                    height, selectedIndicatorPaint);
        }

        canvas.drawRect(0, height - bottomBorderThickness, getWidth(), height, bottomBorderPaint);

        int separatorTop = (height - dividerHeightPx) / 2;
        for (int i = 0; i < childCount - 1; i++) {
            View child = getChildAt(i);
            dividerPaint.setColor(tabColorizer.getDividerColor(i));
            canvas.drawLine(child.getRight(), separatorTop, child.getRight(),
                    separatorTop + dividerHeightPx, dividerPaint);
        }
    }

    private static int setColorAlpha(int color, byte alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    private static class SimpleTabColorizer implements SlidingTabLayout.TabColorizer {
        private int[] indicatorColors;
        private int[] dividerColors;

        @Override
        public final int getIndicatorColor(int position) {
            return indicatorColors[position % indicatorColors.length];
        }

        @Override
        public final int getDividerColor(int position) {
            return dividerColors[position % dividerColors.length];
        }

        void setIndicatorColors(int... colors) {
            indicatorColors = colors;
        }

        void setDividerColors(int... colors) {
            dividerColors = colors;
        }
    }
}