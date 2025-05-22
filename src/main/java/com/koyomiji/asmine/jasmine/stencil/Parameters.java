package com.koyomiji.asmine.stencil;

import java.util.List;

public class Parameters {
  public static <T> AnyParameter<T> any() {
    return new AnyParameter<>();
  }

  public static <T> BindParameter<T> bind(Object key) {
    return new BindParameter<>(key);
  }

  public static <T> BindParameter<T> bind(Object key, AbstractParameter<T> child) {
    return new BindParameter<>(key, child);
  }

  public static <T> BoundParameter<T> bound(Object key) {
    return new BoundParameter<>(key);
  }

  public static <T> ConstParameter<T> const_(T value) {
    return new ConstParameter<>(value);
  }

  public static <T> ListParameter<T> list(List<AbstractParameter<T>> children) {
    return new ListParameter<>(children);
  }

  public static <T> ListParameter<T> list(AbstractParameter<T>... children) {
    return new ListParameter<>(children);
  }
}
