package org.harvanir.sonarqube.share.util;

import java.util.function.Supplier;

/**
 * @author Harvan Irsyadi
 */
public class ObjectUtils {

    private ObjectUtils() {
    }

    public static Defer defer(Supplier<Object> supplier) {
        return new Defer(supplier);
    }

    static class Defer {

        private final Supplier<Object> supplier;

        public Defer(Supplier<Object> supplier) {
            this.supplier = supplier;
        }

        @Override
        public String toString() {
            return String.valueOf(supplier.get());
        }
    }
}