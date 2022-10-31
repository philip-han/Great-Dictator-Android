package net.cequals.lib.nullsafe;

import javax.annotation.Nonnull;

import javax.annotation.Nullable;

public class Op<T> extends Either<Throwable, T> {

  //private final Transformer<Exception, Exception> exceptionExceptionTransformer = it -> it;

  public Op(@Nullable Throwable error, @Nullable T value) {
    super(error, value);
  }

/*
  @Nonnull
  @Override
  public <R> OptionE<R> $(@Nonnull Transformer<T, R> transformer) {
    return super.$(transformer, exceptionExceptionTransformer);
  }
*/

  @Nonnull
  public static <T1> Op<T1> valueOp(@Nonnull T1 item) {
    return new Op<>(null, item);
  }

  @Nonnull
  public static <T1> Op<T1> errorOp(@Nonnull Throwable e) {
    return new Op<>(e, null);
  }

  @Nonnull
  @Override
  public T foldOrThrow() {
    if (isError()) throw new RuntimeException(error);
    else return value;
  }

}
