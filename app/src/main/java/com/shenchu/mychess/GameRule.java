package com.shenchu.mychess;

/**
 * Created by admin on 2017/9/8.
 */

public class GameRule {
    public final static int RESULT_LOSE = -1;
    public final static int RESULT_WIN = 1;
    public final static int RESULT_HAND_CUT_THREE = 2;//三三禁手
    public final static int RESULT_HAND_CUT_FOUR = 3;//四四禁手
    public final static int RESULT_HAND_CUT_LONG = 4;//长连禁手
    private final static String TAG = "GameRule";

    private static Coordinate[] mCoordinateArray = new Coordinate[4];//四个方向坐标系

    /**
     * 得到一条线上颜色相同的连子数
     *
     * @param coordinate 坐标系
     * @return
     */
    public static int getLineNum(Coordinate coordinate) {
        int max = getDirectionNum(coordinate);
        coordinate.reverse();
        max += getDirectionNum(coordinate);
        return max;
    }

    // 只获取单方向的连续数目
    public static int getDirectionNum(Coordinate coordinate) {
        int max = 0;
        while (getNextColor(coordinate) == coordinate.getColor()) {
            max++;
        }
        coordinate.minusIndex();
        return max;
    }

    // 只获取单方向的数目(最多跳过一个)
    public static int getDirectionJumpNum(Coordinate coordinate) {
        int max = 0;
        while (true) {
            int color = getNextColor(coordinate);
            if (color == coordinate.getColor()) {
                max++;
            } else if (color == 0) {
                if (coordinate.getJump() >= 1)
                    break;
                coordinate.addJump();
            } else {
                coordinate.addBlock();
                break;
            }
        }
        coordinate.minusIndex();
        return max;
    }

    public static int getNextColor(Coordinate coordinate) {
        int x = coordinate.getX();
        int y = coordinate.getY();
        int dx = coordinate.getDx();
        int dy = coordinate.getDy();
        coordinate.addIndex();
        int index = coordinate.getIndex();
        return GameManager.getInstance().getColorXY(x + dx * index, y + dy * index);
    }

    public static void setNewCoordinate(int x, int y, int color) {
        mCoordinateArray[0] = new Coordinate(x, y, color, 1, 0);
        mCoordinateArray[1] = new Coordinate(x, y, color, 0, 1);
        mCoordinateArray[2] = new Coordinate(x, y, color, 1, 1);
        mCoordinateArray[3] = new Coordinate(x, y, color, 1, -1);
    }

    // 判断是否胜利
    public static int checkWin(int x, int y, int color) {
        int winner = 0;
        setNewCoordinate(x, y, color);
        if (color == Piece.BLACK) {
            for (Coordinate coordinate : mCoordinateArray) {
                if (getLineNum(coordinate) == 4) {
                    winner = color;
                    break;
                }
            }
        } else if (color == Piece.WHITE) {
            for (Coordinate coordinate : mCoordinateArray) {
                if (getLineNum(coordinate) >= 4) {
                    winner = color;
                    break;
                }
            }
        }
        return winner;
    }

    public static int checkHandCut(int x, int y, int color) {
        if (isLongHandCut(x, y, color)) {
            return RESULT_HAND_CUT_LONG;
        } else if (isFourHandCut(x, y, color)) {
            return RESULT_HAND_CUT_FOUR;
        } else if (isThreeHandCut(x, y, color)) {
            return RESULT_HAND_CUT_THREE;
        }
        return 0;
    }

    public static boolean isLongHandCut(int x, int y, int color) {
        setNewCoordinate(x, y, color);
        for (Coordinate coordinate : mCoordinateArray) {
            if (getLineNum(coordinate) > 4)
                return true;
        }
        return false;
    }

    public static boolean isFourHandCut(int x, int y, int color) {
        int num = 0;//活四或跳四数
        setNewCoordinate(x, y, color);
        for (Coordinate coordinate : mCoordinateArray) {
            if (isLive4(coordinate) || isJump4(coordinate)) {
                num++;
            }
            if (num >= 2)
                return true;
        }
        for (Coordinate coordinate : mCoordinateArray) {
            if (isSpecialHandCut(coordinate))
                return true;
        }
        return false;
    }

    public static boolean isSpecialHandCut(Coordinate coordinate) {
        coordinate.setIndex(0);
        int color = coordinate.getColor();
        // 判断四种双四1,形式为0101X1010
        if (getNextColor(coordinate) == color
                && getNextColor(coordinate) == 0
                && getNextColor(coordinate) == color
                && getNextColor(coordinate) != color) {
            coordinate.reverse();
            if (getNextColor(coordinate) == color
                    && getNextColor(coordinate) == 0
                    && getNextColor(coordinate) == color
                    && getNextColor(coordinate) != color) {
                return true;
            }
        }
        // 判断四种双四2,形式为01011X010和其镜像
        for (int i = 0; i < 2; i++) {
            coordinate.reverse();
            if (getNextColor(coordinate) == color
                    && getNextColor(coordinate) == color
                    && getNextColor(coordinate) == 0
                    && getNextColor(coordinate) == color
                    && getNextColor(coordinate) != color) {
                coordinate.reverse();
                if (getNextColor(coordinate) == 0
                        && getNextColor(coordinate) == color
                        && getNextColor(coordinate) != color)
                    return true;
            }
        }
        // 判断四种双四3,形式为01101X0110和其镜像
        for (int i = 0; i < 2; i++) {
            coordinate.reverse();
            if (getNextColor(coordinate) == color
                    && getNextColor(coordinate) == 0
                    && getNextColor(coordinate) == color
                    && getNextColor(coordinate) == color
                    && getNextColor(coordinate) != color) {
                coordinate.reverse();
                if (getNextColor(coordinate) == 0
                        && getNextColor(coordinate) == color
                        && getNextColor(coordinate) == color
                        && getNextColor(coordinate) != color)
                    return true;
            }
        }
        coordinate.setIndex(0);
        // 判断四种双四4,形式为01110X01110
        if (getNextColor(coordinate) == 0
                && getNextColor(coordinate) == color
                && getNextColor(coordinate) == color
                && getNextColor(coordinate) == color
                && getNextColor(coordinate) != color) {
            coordinate.reverse();
            if (getNextColor(coordinate) == 0
                    && getNextColor(coordinate) == color
                    && getNextColor(coordinate) == color
                    && getNextColor(coordinate) == color
                    && getNextColor(coordinate) != color) {
                return true;
            }
        }
        return false;
    }

    public static boolean isThreeHandCut(int x, int y, int color) {
        int num = 0;//活三或跳三数
        setNewCoordinate(x, y, color);
        for (Coordinate coordinate : mCoordinateArray) {
            if (isLive3(coordinate)) {
                num++;
            }
            if (num >= 2)
                return true;
        }
        return false;
    }

    //得到方向上的活子数
    public static int getDirectionLive(Coordinate coordinate, int space) {
        int max = getDirectionNum(coordinate);
        for (int i = 0; i < space; i++) {
            if (getNextColor(coordinate) == 0) {
                coordinate.addSpace();
            } else {
                coordinate.minusIndex();
                break;
            }
        }
        if (getNextColor(coordinate) != coordinate.getColor()) {
            coordinate.addBlock();
        }
        return max;
    }

    //得到方向上的活子数(跳过一个)
    public static int getDirectionJump(Coordinate coordinate, int space) {
        int max = getDirectionJumpNum(coordinate);
        for (int i = 0; i < space; i++) {
            if (getNextColor(coordinate) == 0) {
                coordinate.addSpace();
            } else {
                coordinate.minusIndex();
                break;
            }
        }
        if (getNextColor(coordinate) != coordinate.getColor()) {
            coordinate.addBlock();
        }
        return max;
    }

    // 判断是否活四
    public static boolean isLive4(Coordinate coordinate) {
        int max = getDirectionLive(coordinate, 1);
        coordinate.reverse();
        max += getDirectionLive(coordinate, 1);
        if (max == 3 && coordinate.getSpace() == 2 && coordinate.getBlock() == 2)
            return true;// 活四
        return false;
    }

    // 判断是否跳四
    public static boolean isJump4(Coordinate coordinate) {
        for (int j = 0; j < 2; j++) {// 分两个方向判断，先左后右和先右后左是不一样的
            coordinate.clear();
            int max = getDirectionJumpNum(coordinate);
            coordinate.reverse();
            max += getDirectionJumpNum(coordinate);
            if (max == 3 && coordinate.getJump() == 1)
                return true;// 跳四
        }
        return false;
    }

    // 判断是否活三
    public static boolean isLive3(Coordinate coordinate) {
        for (int j = 0; j < 2; j++) {// 分两个方向判断，先左后右和先右后左是不一样的
            coordinate.clear();
            int max = getDirectionJump(coordinate, 1);
            coordinate.reverse();
            max += getDirectionJump(coordinate, 1);
            if (max == 2 && (coordinate.getSpace() + coordinate.getJump()) == 3 && coordinate.getBlock() == 2)
                return true;// 活三
        }
        return false;
    }

    // 判断是半活三
    public static boolean isHalf3(Coordinate coordinate) {
        for (int j = 0; j < 2; j++) {// 分两个方向判断，先左后右和先右后左是不一样的
            coordinate.clear();
            int max = getDirectionJump(coordinate, 1);
            coordinate.reverse();
            max += getDirectionJump(coordinate, 1);
            if (max == 2 && coordinate.getSpace() >= 1 && coordinate.getJump() == 1 && coordinate.getBlock() >= 2)
                return true;// 半活三
        }
        return false;
    }

    // 判断是活二
    public static boolean isLive2(Coordinate coordinate) {
        for (int j = 0; j < 2; j++) {// 分两个方向判断，先左后右和先右后左是不一样的
            coordinate.clear();
            int max = getDirectionJump(coordinate, 2);
            coordinate.reverse();
            max += getDirectionJump(coordinate, 2);
            if (max == 1 && (coordinate.getSpace() + coordinate.getJump()) >= 4 && coordinate.getBlock() >= 2)
                return true;// 活二
        }
        return false;
    }

    // 判断是半活二
    public static boolean isHalf2(Coordinate coordinate) {
        for (int j = 0; j < 2; j++) {// 分两个方向判断，先左后右和先右后左是不一样的
            coordinate.clear();
            int max = getDirectionJump(coordinate, 2);
            coordinate.reverse();
            max += getDirectionJump(coordinate, 2);
            if (max == 1 && (coordinate.getSpace() + coordinate.getJump()) >= 3 && coordinate.getBlock() >= 2)
                return true;// 活二
        }
        return false;
    }

    public static int getLive4(int x, int y, int color) {
        int num = 0;
        setNewCoordinate(x, y, color);
        for (Coordinate coordinate : mCoordinateArray) {
            if (isLive4(coordinate)) {
                num++;
            }
        }
        return num;
    }

    public static int getJump4(int x, int y, int color) {
        int num = 0;
        setNewCoordinate(x, y, color);
        for (Coordinate coordinate : mCoordinateArray) {
            if (isJump4(coordinate)) {
                num++;
            }
        }
        return num;
    }

    public static int getLive3(int x, int y, int color) {
        int num = 0;
        setNewCoordinate(x, y, color);
        for (Coordinate coordinate : mCoordinateArray) {
            if (isLive3(coordinate)) {
                num++;
            }
        }
        return num;
    }

    public static int getHalf3(int x, int y, int color) {
        int num = 0;
        setNewCoordinate(x, y, color);
        for (Coordinate coordinate : mCoordinateArray) {
            if (isHalf3(coordinate)) {
                num++;
            }
        }
        return num;
    }

    public static int getLive2(int x, int y, int color) {
        int num = 0;
        setNewCoordinate(x, y, color);
        for (Coordinate coordinate : mCoordinateArray) {
            if (isLive2(coordinate)) {
                num++;
            }
        }
        return num;
    }

    public static int getHalf2(int x, int y, int color) {
        int num = 0;
        setNewCoordinate(x, y, color);
        for (Coordinate coordinate : mCoordinateArray) {
            if (isHalf2(coordinate)) {
                num++;
            }
        }
        return num;
    }
}
