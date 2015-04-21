package com.drl.lutz.lullabyapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.drl.lutz.lullabyapp.R;

/**
 * TODO: document your custom view class.
 */
public class WaveformVisualizerView extends View {

    int paddingLeft, paddingTop, paddingRight, paddingBottom;

    int contentWidth, contentHeight;

    byte[] buffer;

    public WaveformVisualizerView(Context context) {
        super(context);
        init(null, 0);
    }

    public WaveformVisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WaveformVisualizerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        this.buffer = null;


        // caclulcate sizes
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (buffer != null ) {
            float[] pts = new float[buffer.length*4];

            float x = 0;
            float y = 0;
            for (int i=0;i<buffer.length;i++) {

                pts[i*4+0] = x;
                pts[1*4+1] = y;

                x = ((float)i / buffer.length) * contentWidth;
                y = ((float)buffer[i] / 255) * contentHeight;

                pts[i*4+2] = x;
                pts[1*4+3] = y;

            }

            canvas.drawLines(pts,0,pts.length,new Paint());
        }


    }

    void displayAudioBuffer(byte[] audioBuffer) {
        this.buffer = audioBuffer;
    }
}
