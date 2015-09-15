package com.yjh.base.util.number;

import com.yjh.base.util.number.config.GeneratorConfig;

/**
 * Created by yjh on 2015/9/13.
 */
public abstract class Generator {
    protected GeneratorConfig config;

    public Generator() {
    }

    public Generator(GeneratorConfig config) {
        this.config = config;
    }

    public GeneratorConfig getConfig() {
        return config;
    }

    public abstract  int next();

}