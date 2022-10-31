package net.cequals.lib.nullsafe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Kt {

    public static <T> void let(@Nullable T nonNull, @Nonnull Consumer<T> letClosure) {
        let(nonNull, letClosure, null);
    }

    public static <T> void let(@Nullable T nonNull, @Nonnull Consumer<T> letClosure, @Nullable Consumer<Exception> nullClosure) {
        try {
            if (nonNull != null) letClosure.execute(nonNull);
        } catch (Exception e) {
            if (nullClosure != null) nullClosure.execute(e);
        }
    }

    public static <T> T ifElse(Boolean condition, T whenNotNull, T whenNull) {
        if (condition) return whenNotNull; else return whenNull;
    }

    public interface Consumer<T> {
        @Nonnull
        void execute(T it);
    }

    public interface Producer<T> {
        @Nonnull
        T execute();
    }

    public interface Transformer<T,R> {
        @Nonnull
        R execute(T it);
    }

    public interface BiTransformer<T,E,R> {
        @Nonnull
        R execute(T value, E error);
    }

}
