package com.oa.learn.utils;

import com.oa.learn.bean.Goods;

import java.util.Comparator;

public class TimesCompare implements Comparator<Goods> {

    @Override
    public int compare(Goods o1, Goods o2) {
        return Integer.compare(o2.getTimes(), o1.getTimes());
    }
}
