package com.yjh.base.util.number.config;

import com.yjh.base.util.number.Generator;

/**
 * Created by yjh on 2015/9/15.
 */
public class StepNormalConfig extends NormalConfig {
    public StepNormalConfig(boolean isSynchronized, int start, int step, int end, Class<? extends Generator> generatorClass) {
        super(isSynchronized, start, step, end, generatorClass);
    }

    @Override
    public int computeNext(int value) {
        if(value < end)
            return value + step;
        else
            return value;
    }
}
