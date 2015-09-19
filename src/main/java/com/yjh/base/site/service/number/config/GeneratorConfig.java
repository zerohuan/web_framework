package com.yjh.base.site.service.number.config;

import com.yjh.base.site.service.number.Computer;
import com.yjh.base.site.service.number.Generator;

/**
 * Created by yjh on 2015/9/13.
 */
public abstract class GeneratorConfig {
    protected int start;
    protected int step;
    protected int end = -1;
    private Class<? extends Generator> generatorClass;
    protected Computer computer;

    public GeneratorConfig(int start, int step, int end, Class<? extends Generator> generatorClass) {
        this.start = start;
        this.step = step;
        this.end = end - (end % step); //ensure value could exceed the end value
        this.generatorClass = generatorClass;
    }

    public Class<? extends Generator> getGeneratorClass() {
        return generatorClass;
    }

    public int getStart() {
        return start;
    }

    public int getStep() {
        return step;
    }

    public int getEnd() {
        return end;
    }

    public abstract int computeNext(int value);
}
