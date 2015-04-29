package com.drl.lutz.lullabyapp.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.drl.lutz.lullabyapp.R;

/**
 * TODO: document your custom view class.
 */
public class TitleTextView extends TextView {

    public TitleTextView(Context context) {
        super(context);
        init(context);
    }

    public TitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/berliner-grotesk-light.ttf"));
    }
}
