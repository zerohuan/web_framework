package com.yjh.base.site.model;

import java.time.DayOfWeek;

/**
 * Created by yjh on 2015/9/9.
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
