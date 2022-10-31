package net.cequals.lib.nullsafe;

//import net.cequals.lib.Predicate;

import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Scoped<T> {

  @Nullable
  private final T value;

  public static <T1> Scoped<T1> of(T1 value) {
    return new Scoped<>(value);
  }

  @Nullable
  public T value() { return value; }

  @Nonnull
  public T valueOr(T defaultValue) {
    return value != null ? value : defaultValue;
  }

  @Nonnull
  public T valueOr(Kt.Producer<T> defaultValueProducer) {
    return value != null ? value : defaultValueProducer.execute();
  }

  public void runWhenNull(@Nonnull Runnable nullRunnable) {
    if (value == null) nullRunnable.run();
  }

  private Scoped(@Nullable T value) {
    this.value = value;
  }

  @Nonnull
  public <R> Scoped<R> $(Kt.Transformer<T,R> transformer) {
    return $(transformer, null);
  }

  @Nonnull
  public <R> Scoped<R> $(Kt.Transformer<T,R> transformer, @Nullable Kt.Consumer<Exception> run) {
    if (value != null && transformer != null) {
      try {
        return new Scoped<>(transformer.execute(value));
      } catch (Exception e) {
        if (run != null) run.execute(e);
        return new Scoped<>(null);
      }
    } else return new Scoped<>(null);
  }

  public Scoped<T> filter(@Nonnull Predicate<T> predicate) {
    return predicate.test(value) ? this : new Scoped<>(null);
  }

  public void let(@Nonnull Kt.Consumer<T> consumer) {
    let(consumer, null);
  }

  public void let(@Nonnull Kt.Consumer<T> consumer, @Nullable Kt.Consumer<Exception> run) {
    Kt.let(value, consumer, run);
  }

  public Scoped<T> also(Kt.Consumer<T> closure) {
    closure.execute(value);
    return this;
  }

}
