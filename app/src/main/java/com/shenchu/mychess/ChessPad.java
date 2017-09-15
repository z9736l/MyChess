package com.shenchu.mychess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class ChessPad extends RelativeLayout {
    public static final int LINE = 15;// 五子棋标准棋盘为15*15格

    private final String TAG = getClass().getSimpleName();
    private int gridSize = 0; // 五子棋盘每格的大小
    private Context mContext;

    public ChessPad(Context context) {
        super(context);
        mContext = context;
    }

    public ChessPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ChessPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int childCount = getChildCount();
//        int width;
//        for (int i = 0; i < childCount; i++) {
//            View view = getChildAt(i);
//            width = view.getMeasuredWidth();
//            view.layout(l+width/2, t, top + l + 60, t + 60);
//        }
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽高设为一样
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawChessboard(canvas);// 画棋盘
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {// 初始化和屏幕大小改变时调用该函数
        gridSize = Math.min(w, h) / LINE;
        super.onSizeChanged(w, h, oldw, oldh);
        GameManager.getInstance().initGameManager(mContext, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {// 触摸屏事件
        if (0 != GameManager.getInstance().getWinner()) {
            return super.onTouchEvent(event);
        }
        int x = (int) event.getX() / gridSize;
        int y = ((int) event.getY() - getTopY() + gridSize / 2) / gridSize;
        if (GameManager.getInstance().getCurrentColor() == Piece.BLACK) {
            GameManager.getInstance().addPiece(x, y);
        }
        GameManager.getInstance().AI();
        //setPiece(piece, x, y);
        return super.onTouchEvent(event);
    }

    public void setPiece(Piece piece) {
        int x = piece.getPointX();
        int y = piece.getPointY();
        LayoutParams params = new LayoutParams(gridSize, gridSize);
        addView(piece, params);
        params.leftMargin = getTopX() + x * gridSize - gridSize / 2;
        params.topMargin = getTopY() + y * gridSize - gridSize / 2;
    }

    private void drawChessboard(Canvas canvas) {
        // 画棋盘
        int x = getTopX();
        int y = getTopY();
        int num = LINE - 1;
        Paint paint = new Paint();
        paint.setStrokeWidth(3);
        for (int i = 0; i < LINE; i++) {
            canvas.drawLine(x, y + i * gridSize, x + gridSize * num, y + i
                    * gridSize, paint);
            canvas.drawLine(x + i * gridSize, y, x + i * gridSize, y + gridSize
                    * num, paint);
        }
        canvas.drawCircle(x + 3 * gridSize, y + 3 * gridSize, gridSize / 10,
                paint);
        canvas.drawCircle(x + 11 * gridSize, y + 3 * gridSize, gridSize / 10,
                paint);
        canvas.drawCircle(x + 7 * gridSize, y + 7 * gridSize, gridSize / 10,
                paint);
        canvas.drawCircle(x + 3 * gridSize, y + 11 * gridSize, gridSize / 10,
                paint);
        canvas.drawCircle(x + 11 * gridSize, y + 11 * gridSize, gridSize / 10,
                paint);

        int block = 12;
        paint.setStrokeWidth(6);
        canvas.drawLine(x - block, y - block, getBottomX() + block, y - block, paint);
        canvas.drawLine(x - block, y - block, x - block, getBottomY() + block, paint);
        canvas.drawLine(getBottomX() + block, y - block, getBottomX() + block, getBottomY() + block, paint);
        canvas.drawLine(x - block, getBottomY() + block, getBottomX() + block, getBottomY() + block, paint);
    }

    private int getTopX() {
        return gridSize / 2;
    }

    private int getTopY() {
        return gridSize / 2 + Math.abs(getWidth() - getHeight()) / 2;
    }

    private int getBottomX() {
        return getTopX() + gridSize * (LINE - 1);
    }

    private int getBottomY() {
        return getTopY() + gridSize * (LINE - 1);
    }
}