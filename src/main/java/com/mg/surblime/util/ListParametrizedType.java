package com.mg.surblime.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by moses on 5/3/18.
 */

public class ListParametrizedType implements ParameterizedType {

    private Type type;

    public ListParametrizedType(Type type) {
        this.type = type;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{type};
    }

    @Override
    public Type getRawType() {
        return ArrayList.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
