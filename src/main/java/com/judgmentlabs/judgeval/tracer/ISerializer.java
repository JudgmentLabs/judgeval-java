package com.judgmentlabs.judgeval.tracer;

import java.lang.reflect.Type;

public interface ISerializer {
    String serialize(Object obj);

    default String serialize(Object obj, Type type) {
        return serialize(obj);
    }
}
