package com.shenchu.mychess;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2017/9/7.
 */

public class GameManager {
    private final String TAG = getClass().getSimpleName();
    private static GameManager mInstance;
    private int mColor;// 颜色,黑棋1,白棋-1,无子0,出界-2
    private Context mContext;
    private ChessPad mChessPad;
    private Piece[][] mPieceHash;//记录棋盘上的方位
    private List<Piece> mPieceStep;//记录每一步
    private int mWinner = 0;
    private ChessAI mChessAI;
    private Timer mTimer;

    private GameManager() {
    }

    public static GameManager getInstance() {
        if (null == mInstance)
            mInstance = new GameManager();
        return mInstance;
    }

    public void initGameManager(Context context, ChessPad chessPad) {
        mChessPad = chessPad;
        mContext = context;
        mPieceHash = new Piece[ChessPad.LINE][ChessPad.LINE];
        mPieceStep = new ArrayList<>();
        mColor = Piece.BLACK;
        mWinner = 0;
        mChessAI = new ChessAI();
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = msg.what;
            if (mPieceStep.get(i).getImageAlpha() == 0xff) {
                mPieceStep.get(i).setImageAlpha(0);
            } else {
                mPieceStep.get(i).setImageAlpha(0xff);
            }
        }
    };

    public int getWinner() {
        return mWinner;
    }

    public void setWinner(int winner) {
        this.mWinner = winner;
    }

    public Piece addPiece(int x, int y) {
//        Coordinate mCoordinate = new Coordinate(x, y, mColor, 1, 0);
//        if (GameRule.isHalf2(mCoordinate))
//            Log.d(TAG, "addPiece: 是半活二");
        Piece piece = new Piece(mContext, x, y, mColor);
        mChessPad.setPiece(piece);
        mPieceHash[x][y] = piece;
        mPieceStep.add(piece);
        for (Piece item : mPieceStep) {
            item.setImageAlpha(0xff);
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mPieceStep.size() > 0)
                    mHandler.sendEmptyMessage(mPieceStep.size() - 1);
            }
        }, 1000, 1000);
        checkWin(piece);
        if (mWinner == 0)
            checkHandCut(x, y);
        mColor = mColor * -1;//改变颜色
        return piece;
    }

    public void AI() {
        if (mColor == Piece.WHITE && mWinner == 0) {
            mChessAI.AI();
        }
    }

    public void removeAllPiece() {
        mChessPad.removeAllViews();
        initGameManager(mContext, mChessPad);
    }

    public void removeLastPiece() {
        Piece piece = getLastPiece();
        if (null == piece) {
            return;
        }
        int x = piece.getPointX();
        int y = piece.getPointY();
        mPieceHash[x][y] = null;
        mChessPad.removeView(piece);
        mPieceStep.remove(mPieceStep.size() - 1);
        mColor = mColor * -1;//改变颜色
        mWinner = 0;
    }

    public Piece getLastPiece() {
        if (mPieceStep.size() <= 0) {
            return null;
        }
        return mPieceStep.get(mPieceStep.size() - 1);
    }

    public Piece getPiece(int x, int y) {
        if (isOutSide(x, y))
            return null;
        return mPieceHash[x][y];
    }

    public int getColorXY(int x, int y) {
        Piece piece = getPiece(x, y);
        if (isOutSide(x, y))
            return Piece.OUT;
        if (null == piece)
            return 0;
        return piece.getColor();
    }

    public boolean isOutSide(int x, int y) {
        if (x >= 0 && x < ChessPad.LINE && y >= 0 && y < ChessPad.LINE) {
            return false;
        }
        return true;
    }

    public int getCurrentColor() {
        return mColor;
    }

    public void checkWin(Piece piece) {
        int x = piece.getPointX();
        int y = piece.getPointY();
        int color = piece.getColor();
        mWinner = GameRule.checkWin(x, y, color);
        if (0 != mWinner)
            ((MainActivity) mContext).showWinner(mWinner);
    }

    public void checkHandCut(int x, int y) {
        if (Piece.BLACK == mColor) {//先手才判断禁手
            int handCut = GameRule.checkHandCut(x, y, mColor);
            if (handCut > 0) {
                mWinner = -1;
                ((MainActivity) mContext).showWinner(handCut);
            }
        }
    }
}
