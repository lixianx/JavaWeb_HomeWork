package com.oa.learn.bean;

public enum Level {
    GOLD("金牌"),
    SILVER("银牌"),
    BRONZE("铜牌");

    private String levelName;

    Level(String levelName) {
        this.levelName = levelName;
    }

    @Override
    public String toString() {
        return levelName;
    }
}
