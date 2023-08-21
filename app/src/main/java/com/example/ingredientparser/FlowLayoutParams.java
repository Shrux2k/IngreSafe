package com.example.ingredientparser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FlowLayoutParams extends ViewGroup.MarginLayoutParams {
    public FlowLayoutParams(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    public FlowLayoutParams(int width, int height) {
        super(width, height);
    }
}
