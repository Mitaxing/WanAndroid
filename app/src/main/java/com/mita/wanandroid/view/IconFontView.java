package com.mita.wanandroid.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * @author MiTa
 * @date 2017/11/13.
 */
public class IconFontView extends AppCompatTextView {

    private String source;

    public IconFontView(Context context) {
        super(context);
        initFont();
    }

    public IconFontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont();
    }

    public IconFontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont();
    }

    private void initFont() {
        if (TextUtils.isEmpty(source)) {
            source = "iconfont.ttf";
        }
        Typeface cachedTypeFace = Typeface.createFromAsset(getContext().getAssets(), source);
        setTypeface(cachedTypeFace);
    }
}
