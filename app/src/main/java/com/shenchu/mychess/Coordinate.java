package com.shenchu.mychess;

/**
 * Created by admin on 2017/9/9.
 */

public class Coordinate {
    private int x;
    private int y;
    private int color;
    private int dx;//x方向
    private int dy;//y方向
    private int index;//离原点第几个
    private int space;//两端有空出一个
    private int block;//两端有堵死
    private int jump;//中间有跳过

    public Coordinate(int x, int y, int color, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.dx = dx;
        this.dy = dy;
        this.index = 0;
        this.space = 0;
        this.block = 0;
        this.jump = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void addIndex() {
        index++;
    }

    public void minusIndex() {
        index--;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public void addSpace() {
        space++;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public void addBlock() {
        block++;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public void addJump() {
        jump++;
    }

    //方向翻转
    public void reverse() {
        dx = dx * -1;
        dy = dy * -1;
        index = 0;
    }

    //清除计数器
    public void clear() {
        index = 0;
        space = 0;
        block = 0;
        jump = 0;
    }
}
