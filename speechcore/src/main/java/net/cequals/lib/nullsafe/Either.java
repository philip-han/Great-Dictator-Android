package net.cequals.lib.nullsafe;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

public class Either<E,V> {

  @Nullable
  public final V value;
  @Nullable
  public final E error;

  public Either(@Nullable E error, @Nullable V value) {
    this.error = error;
    this.value = value;
  }

  public static <E1, V1> Either<E1, V1> value(V1 value) {
    return new Either<>(null, value);
  }

  public static <E1, V1> Either<E1, V1> error(E1 error) {
    return new Either<>(error, null);
  }

/*
  public static <T1,E1> Val<T1,E1> check(T1 value) {
    return check(value, null);
  }

  public static <T1,E1> Val<T1,E1> check(T1 value, Class<E1> eClass) {
    return new Val<>(value, null);
  }
*/

  public final boolean isError() {
    return error != null;
  }

  public void fold(@Nonnull Kt.Consumer<V> consumer) {
    fold(null, consumer);
  }

  public void fold(@Nullable Kt.Consumer<E> errorConsumer, @Nonnull Kt.Consumer<V> consumer) {
      if (isError()) requireNonNull(errorConsumer).execute(error);
      else consumer.execute(value);
  }

  @Nonnull
  public V fold(@Nonnull V defaultValue) {
    if (isError()) return defaultValue;
    return value;
  }

  @Nonnull
  public E foldError() {
    requireNonNull(error);
    return error;
  }

  @Nullable
  public V fold() {
    if (isError()) return null;
    return value;
  }

  @Nonnull
  public V foldOrThrow() {
    if (isError()) throw new RuntimeException(Objects.requireNonNull(error.toString(), "unknown error"));
    return value;
  }

  @Nonnull
  public V foldOrHandleError(@Nonnull Kt.Transformer<E,V> errorHandler) {
    if (isError()) return errorHandler.execute(error);
    return value;
  }

  @Nullable
  public V foldOrError(@Nonnull Kt.Consumer<E> errorHandler) {
    if (isError()) {
      errorHandler.execute(error);
      return null;
    }
    return value;
  }

/*
  @Nonnull
  public <R> Val<R,E> $(@Nonnull Kt.Transformer<T,R> transformer) {
    return $(transformer, null);
  }

  @Nonnull
  public <R> Val<R,E> $(@Nonnull Kt.Transformer<T,R> transformer, @Nullable Kt.Transformer<Exception ,E> eTransformer) {
    if (value != null) {
      try {
        return new Val<>(transformer.execute(value), null);
      } catch (Exception e) {
        if (eTransformer != null) return new Val<>(null, eTransformer.execute(e));
        else throw e;
      }
    } else return new Val<>(null, error);
  }

  // TODO how to add 'this'
  @Nonnull
  public Val<T,E> also(Kt.Consumer<T> closure) {
    closure.execute(value);
    return this;
  }

  public void fold(@Nonnull Kt.Consumer<T> consumer) {
    fold(consumer, null);
  }

  public void fold(@Nonnull Kt.Consumer<T> consumer, @Nullable Kt.Consumer<Exception> run) {
    Kt.let(value, consumer, run);
  }
*/

}
