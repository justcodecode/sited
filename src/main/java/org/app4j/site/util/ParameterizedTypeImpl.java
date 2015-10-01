package org.app4j.site.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

public class ParameterizedTypeImpl implements ParameterizedType {
    final Class<?> rawType;
    final Type[] arguments;
    final Type ownerType;

    public ParameterizedTypeImpl(Class<?> rawType, Type[] arguments, Type ownerType) {
        this.rawType = rawType;
        this.arguments = arguments;
        this.ownerType = ownerType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return arguments;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public int hashCode() {
        return (ownerType == null ? 0 : ownerType.hashCode())
                ^ Arrays.hashCode(arguments)
                ^ rawType.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ParameterizedType)) {
            return false;
        }
        ParameterizedType that = (ParameterizedType) other;
        return rawType.equals(that.getRawType())
                && Objects.equals(ownerType, that.getOwnerType())
                && Arrays.equals(arguments, that.getActualTypeArguments());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (ownerType != null) {
            builder.append(ownerType.getTypeName()).append('.');
        }
        builder.append(rawType.getName())
                .append('<');

        int i = 0;
        for (Type argument : arguments) {
            if (i > 0) builder.append(", ");
            builder.append(argument.getTypeName());
            i++;
        }

        builder.append('>');
        return builder.toString();
    }
}