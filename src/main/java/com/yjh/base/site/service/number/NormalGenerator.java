package com.yjh.base.site.service.number;

import com.yjh.base.site.service.number.config.GeneratorConfig;
import com.yjh.base.site.service.number.config.NormalConfig;

/**
 * a generator using "int" type to create next number
 * Created by yjh on 2015/9/13.
 */
public class NormalGenerator extends Generator {
    private int value;

    public NormalGenerator() {
    }

    public NormalGenerator(GeneratorConfig config) {
        super(config);
    }

    /**
     * @return
     */
    public int next() {
        int nextValue; //use a local variable

        if (getConfig().isSynchronized()) {
            synchronized (this) {
                nextValue = value = config.computeNext(value);
            }
        } else {
            nextValue = value = config.computeNext(value);
        }

        return nextValue;
    }

    @Override
    public NormalConfig getConfig() {
        return (NormalConfig)config;
    }
}
