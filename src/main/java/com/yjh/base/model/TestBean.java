package com.yjh.base.model;

import java.time.DayOfWeek;

/**
 * Created by lenovo on 2015/9/9.
 */
public class TestBean {
    DayOfWeek dayOfWeek = DayOfWeek.SATURDAY;

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
