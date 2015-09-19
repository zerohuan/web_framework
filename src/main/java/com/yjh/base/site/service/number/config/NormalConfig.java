package com.yjh.base.site.service.number.config;

import com.yjh.base.site.service.number.Generator;

/**
 * Created by yjh on 2015/9/13.
 */
public abstract class NormalConfig extends GeneratorConfig {
    private final boolean isSynchronized;

    public NormalConfig(boolean isSynchronized, int start, int step, int end, Class<? extends Generator> generatorClass) {
        super(start, step, end, generatorClass);
        this.isSynchronized = isSynchronized;
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }
}