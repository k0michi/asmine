package com.koyomiji.asmine.stencil;

import java.util.List;

public class Stencils {
  public static <T> AnyStencil<T> any() {
    return new AnyStencil<>();
  }

  public static <T> BindStencil<T> bind(Object key) {
    return new BindStencil<>(key);
  }

  public static <T> BindStencil<T> bind(Object key, IStencil<T> child) {
    return new BindStencil<>(key, child);
  }

  public static <T> BoundStencil<T> bound(Object key) {
    return new BoundStencil<>(key);
  }

  public static <T> ConstStencil<T> const_(T value) {
    return new ConstStencil<>(value);
  }

  public static <T> ListStencil<T> list(List<IStencil<T>> children) {
    return new ListStencil<>(children);
  }

  @SafeVarargs
  public static <T> ListStencil<T> list(IStencil<T>... children) {
    return new ListStencil<>(children);
  }

  public static <T> ListSetStencil<T> listSet(IStencil<List<T>> list, int index, T value) {
    return new ListSetStencil<>(list, index, value);
  }

  public static <T> ListSetStencil<T> listSet(IStencil<List<T>> list, int index, T value, T defaultValue) {
    return new ListSetStencil<>(list, index, value, defaultValue);
  }
}
