package com.shenchu.mychess;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ChessAI {
    private final String TAG = getClass().getSimpleName();
//    private final int PRIORITY_WIN = 999999;//再落一子胜利
//    private final int PRIORITY_HANDCUT_FOUR = 99999;//四四禁手（后手）
//    private final int PRIORITY_HANDCUT_THREE = 999;//三三禁手（后手）
//    private final int PRIORITY_FOUR_LIVE_THREE = 9999;//四+活三
//    private final int PRIORITY_FOUR_HALF_THREE = 6666;//四+半活三
//    private final int PRIORITY_LIVE_FOUR = 99998;//活四
//    private final int PRIORITY_FOUR_TWO_LIVE = 997;//四+活二

    private List<Priority> mPriorityList;//最高优先级列表

    private int getPriority(int x, int y, int color) {// 得到某个坐标的优先级
        int handCut = GameRule.checkHandCut(x, y, color);

        if (color == Piece.WHITE) {
            if (GameRule.checkWin(x, y, color) != 0 || handCut == GameRule.RESULT_HAND_CUT_LONG) {
                return 10000001;
            }
            if (handCut == GameRule.RESULT_HAND_CUT_FOUR) {
                return 5000000;
            }
            int lp4 = GameRule.getLive4(x, y, color);
            int jp4 = GameRule.getJump4(x, y, color);
            int lp3 = GameRule.getLive3(x, y, color);
            if (lp4 >= 1) {
                return 2000001;
            }
            if ((lp4 >= 1 || jp4 >= 1) && lp3 >= 1) {
                return 2000000;
            }
            if (handCut == GameRule.RESULT_HAND_CUT_THREE) {
                return 100002;
            }
            int hp3 = GameRule.getHalf3(x, y, color);
            if (jp4 >= 1 && hp3 >= 1) {
                return 100001;
            }
            int lp2 = GameRule.getLive2(x, y, color);
            if (jp4 >= 1 && lp2 >= 1) {
                return 100000;
            }
            int hp2 = GameRule.getHalf2(x, y, color);
            if (jp4 >= 1 && hp2 >= 1) {
                return 1005;
            }
            if (jp4 >= 1) {
                return 1004;
            }
            if (lp3 >= 1 && hp3 >= 1) {
                return 1003;
            }
            if (lp3 >= 1 && lp2 >= 1) {
                return 1002;
            }
            if (lp3 >= 1 && hp2 >= 1) {
                return 1001;
            }
            if (lp3 >= 1) {
                return 1000;
            }
            if (hp3 >= 2) {
                return 18;
            }
            if (hp3 >= 1 && lp2 >= 1) {
                return 17;
            }
            if (hp3 >= 1 && hp2 >= 1) {
                return 16;
            }
            if (hp3 >= 1) {
                return 15;
            }
            if (lp2 >= 2) {
                return 14;
            }
            if (lp2 >= 1 && hp2 >= 1) {
                return 13;
            }
            if (lp2 >= 1) {
                return 12;
            }
            if (hp2 >= 2) {
                return 11;
            }
            if (hp2 >= 1) {
                return 10;
            }
        } else {
            if (GameRule.checkWin(x, y, color) != 0) {
                return 10000000;
            }
            if (handCut == GameRule.RESULT_HAND_CUT_LONG) {
                return 0;
            }
            if (handCut == GameRule.RESULT_HAND_CUT_FOUR) {
                return 0;
            }
            int lp4 = GameRule.getLive4(x, y, color);
            int jp4 = GameRule.getJump4(x, y, color);
            int lp3 = GameRule.getLive3(x, y, color);
            int hp3 = GameRule.getHalf3(x, y, color);
            if (lp4 == 1 && hp3 >= 1) {
                return 1000004;
            }
            int lp2 = GameRule.getLive2(x, y, color);
            if (lp4 == 1 && lp2 >= 1) {
                return 1000003;
            }
            int hp2 = GameRule.getHalf2(x, y, color);
            if (lp4 == 1 && hp2 >= 1) {
                return 1000002;
            }
            if (lp4 == 1) {
                return 1000001;
            }
            if ((lp4 == 1 || jp4 == 1) && lp3 == 1) {
                return 1000000;
            }
            if (handCut == GameRule.RESULT_HAND_CUT_THREE) {
                return 0;
            }
            if (jp4 >= 1 && hp3 >= 1) {
                return 10001;
            }
            if (jp4 >= 1 && lp2 >= 1) {
                return 10000;
            }
            if (jp4 >= 1 && hp2 >= 1) {
                return 105;
            }
            if (jp4 >= 1) {
                return 104;
            }
            if (lp3 >= 1 && hp3 >= 1) {
                return 103;
            }
            if (lp3 >= 1 && lp2 >= 1) {
                return 102;
            }
            if (lp3 >= 1 && hp2 >= 1) {
                return 101;
            }
            if (lp3 >= 1) {
                return 100;
            }
            if (hp3 >= 2) {
                return 9;
            }
            if (hp3 >= 1 && lp2 >= 1) {
                return 8;
            }
            if (hp3 >= 1 && hp2 >= 1) {
                return 7;
            }
            if (hp3 >= 1) {
                return 6;
            }
            if (lp2 >= 2) {
                return 5;
            }
            if (lp2 >= 1 && hp2 >= 1) {
                return 4;
            }
            if (lp2 >= 1) {
                return 3;
            }
            if (hp2 >= 2) {
                return 2;
            }
            if (hp2 >= 1) {
                return 1;
            }
        }
        return 0;
    }

    public void AI() {// 人工智能
        int p1, p2, max = -1, index;
        Priority priority;
        for (int x = 0; x < ChessPad.LINE; x++) {
            for (int y = 0; y < ChessPad.LINE; y++) {
                if (GameManager.getInstance().getColorXY(x, y) == 0) {
                    p1 = getPriority(x, y, Piece.WHITE);
                    p2 = getPriority(x, y, Piece.BLACK);
                    p1 = Math.max(p1, p2);
                    if (p1 == max) {
                        priority = new Priority(x, y);
                        mPriorityList.add(priority);
                    } else if (p1 > max) {
                        mPriorityList = new ArrayList<>();
                        priority = new Priority(x, y);
                        mPriorityList.add(priority);
                        max = p1;
                    }
                }
            }
        }
        index = (int) (Math.random() * (mPriorityList.size() - 1));
        priority = mPriorityList.get(index);
        int x = priority.getX();
        int y = priority.getY();
        GameManager.getInstance().addPiece(x, y);
    }
}
