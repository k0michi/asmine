package com.koyomiji.asmine.common;

import java.util.Optional;

public class LongHelper {
  public static Optional<Integer> toInt(long value) {
    if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
      return Optional.empty();
    }

    return Optional.of((int) value);
  }
}
