package com.koyomiji.asmine.tuple;

import java.util.Objects;

public class Triplet<T1,T2,T3> {
  public final T1 first;
  public final T2 second;
  public final T3 third;

  private Triplet(T1 first, T2 second, T3 third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  public static <T1, T2,T3> Triplet<T1, T2,T3> of(T1 first, T2 second, T3 third) {
    return new Triplet<>(first, second, third);
  }

  @Override
  public String toString() {
    return "Triple{" +
            "first=" + first +
            ", second=" + second +
            ", third=" + third +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
    return Objects.equals(first, triplet.first) && Objects.equals(second, triplet.second) && Objects.equals(third, triplet.third);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second, third);
  }
}
