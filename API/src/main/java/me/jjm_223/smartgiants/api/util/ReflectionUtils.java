package me.jjm_223.smartgiants.api.util;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Objects;

public class ReflectionUtils {

    public static void setAccessible(AccessibleObject... objects) {
        for (AccessibleObject object : objects) {
            object.setAccessible(true);
        }
    }

    public static <O> FieldReference<O> getDeclaredField(@NonNull final Object o, @Nullable Class<?> clazz, @NonNull final String fieldName) {
        if(clazz == null) clazz = o.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return new FieldReference<>(o, field);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("o");
        }
    }

    private ReflectionUtils() {
        // Hide the public constructor
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FieldReference<O> {

        private final Object o;
        private final Field field;

        @SuppressWarnings("unchecked")
        public O get() {
            try {
                return (O) field.get(o);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void set(O newValue) {
            try {
                field.set(o, newValue);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public boolean equals(Object o) {
            if(o instanceof FieldReference<?> fr)
                return Objects.equals(get(), fr.get());
            return get() == o;
        }

        public boolean isNull() {
            return get() == null;
        }

        public Field field() {
            return field;
        }

        public Object object() {
            return o;
        }
    }
}
