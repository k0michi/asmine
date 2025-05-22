package com.koyomiji.asmine.tuple;

import java.util.Objects;

public class Quartet<T1, T2, T3, T4> {
  public final T1 first;
  public final T2 second;
  public final T3 third;
  public final T4 fourth;

  private Quartet(T1 first, T2 second, T3 third, T4 fourth) {
    this.first = first;
    this.second = second;
    this.third = third;
    this.fourth = fourth;
  }

  public static <T1, T2, T3, T4> Quartet<T1, T2, T3, T4> of(T1 first, T2 second, T3 third, T4 fourth) {
    return new Quartet<>(first, second, third, fourth);
  }

  @Override
  public String toString() {
    return "Quartet{" +
            "first=" + first +
            ", second=" + second +
            ", third=" + third +
            ", fourth=" + fourth +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Quartet<?, ?, ?, ?> quartet = (Quartet<?, ?, ?, ?>) o;
    return Objects.equals(first, quartet.first) && Objects.equals(second, quartet.second) && Objects.equals(third, quartet.third) && Objects.equals(fourth, quartet.fourth);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second, third, fourth);
  }
}
