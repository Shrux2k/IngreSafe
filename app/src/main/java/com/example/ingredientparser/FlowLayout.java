package com.example.ingredientparser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {
    private int horizontalSpacing = 16; // Adjust as needed
    private int verticalSpacing = 16;   // Adjust as needed

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();

        int x = 0;
        int y = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (x + childWidth > width) {
                // Move to the next row
                x = 0;
                y += childHeight + verticalSpacing;
            }

            child.layout(x, y, x + childWidth, y + childHeight);
            x += childWidth + horizontalSpacing;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int x = 0;
        int y = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (x + childWidth > widthSize) {
                // Move to the next row
                x = 0;
                y += childHeight + verticalSpacing;
            }

            width = Math.max(width, x + childWidth);
            height = y + childHeight;
            x += childWidth + horizontalSpacing;
        }

        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height
        );
    }
}
