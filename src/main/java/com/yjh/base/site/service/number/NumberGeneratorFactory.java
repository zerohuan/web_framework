package com.yjh.base.site.service.number;

import com.yjh.base.site.service.number.config.GeneratorConfig;

/**
 * don't ask me why do like this using this complicated method, I just wanna play.
 * a simple factory of generator
 * Created by yjh on 2015/9/13.
 */
public class NumberGeneratorFactory {
    public static Generator create(GeneratorConfig config) throws NumberGenerateException {
        Generator generator;

        try {
            generator = config.getGeneratorClass().getConstructor(GeneratorConfig.class).newInstance(config);
        } catch (Exception e) { //TODO use multi-catch
            throw new NumberGenerateException(e);
        }

        return generator;
    }
}