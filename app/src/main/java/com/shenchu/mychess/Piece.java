package com.shenchu.mychess;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class Piece extends ImageView {
    private final String TAG = getClass().getSimpleName();
    public final static int BLACK = 1;//黑子
    public final static int WHITE = -1;//白子
    public final static int OUT = -2;//出界

    private int mPointX;
    private int mPointY;
    private int mColor;

    public Piece(Context context) {
        super(context);
    }

    public Piece(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Piece(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Piece(Context context, int x, int y, int color) {
        super(context);
        this.mPointX = x;
        this.mPointY = y;
        this.mColor = color;
        if (Piece.BLACK == color) {
            setImageResource(R.drawable.piece_black);
        } else {
            setImageResource(R.drawable.piece_white);
        }
        setScaleType(ScaleType.FIT_XY);
    }

    public int getPointX() {
        return mPointX;
    }

    public void setPointX(int x) {
        this.mPointX = x;
    }

    public int getPointY() {
        return mPointY;
    }

    public void setPointY(int y) {
        this.mPointY = y;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
