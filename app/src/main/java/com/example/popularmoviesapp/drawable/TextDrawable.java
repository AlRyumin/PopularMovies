package com.example.popularmoviesapp.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.StaticLayout;
import android.text.TextPaint;

public class TextDrawable extends Drawable {
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXTSIZE = 42;
    private static final int DEFAULT_WIDTH = 185;
    private static final int DEFAULT_HEIGHT = 278;

    private final TextPaint mPaint;
    private final String mText;

    public TextDrawable(String text) {
        mText = text;
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setTextSize(DEFAULT_TEXTSIZE);
    }
    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawARGB(10, 0, 0, 0);

        canvas.drawText(mText, 0, mText.length(),
                bounds.left + 10, bounds.centerY(), mPaint);

    }
    @Override
    public int getOpacity() {
        return mPaint.getAlpha();
    }
    @Override
    public int getIntrinsicWidth() {
        return DEFAULT_WIDTH;
    }
    @Override
    public int getIntrinsicHeight() {
        return DEFAULT_HEIGHT;
    }
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }
    @Override
    public void setColorFilter(ColorFilter filter) {
        mPaint.setColorFilter(filter);
    }
}