package com.example.popularmoviesapp.draweble;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.StaticLayout;
import android.text.TextPaint;

public class TextDrawable extends Drawable {
    private static final int DEFAULT_COLOR = Color.WHITE;
    private static final int DEFAULT_TEXTSIZE = 15;
    private TextPaint mPaint;
    private String mText;
    private int width;
    private int height;
    private int textSize;

    StaticLayout myStaticLayout;

    public TextDrawable(String text) {
        mText = text;
        textSize = 12;
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(textSize);
        width = 185;
        height = 278;
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
        return width;
    }
    @Override
    public int getIntrinsicHeight() {
        return height;
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