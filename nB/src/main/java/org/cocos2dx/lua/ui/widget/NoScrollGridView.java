package org.cocos2dx.lua.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Jiang on 2017/3/16.
 */

public class NoScrollGridView extends GridView {

    public NoScrollGridView(Context context) {
        this(context, null);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.gridViewStyle);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setSelector(android.R.color.transparent);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
