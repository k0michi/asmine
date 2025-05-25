package com.koyomiji.asmine.tuple;

import java.util.Objects;

public class Pair<T1, T2> {
  public final T1 first;
  public final T2 second;

  private Pair(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }

  public static <T1, T2> Pair<T1, T2> of(T1 first, T2 second) {
    return new Pair<>(first, second);
  }

  @Override
  public String toString() {
    return "Pair{" +
            "first=" + first +
            ", second=" + second +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }
}
