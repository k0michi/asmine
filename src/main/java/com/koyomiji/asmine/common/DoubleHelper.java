package com.koyomiji.asmine.common;

import java.util.Optional;

public class DoubleHelper {
  public static Optional<Float> toFloat(double value) {
    if (Double.isFinite(value) && Float.isInfinite((float)value)){
      return Optional.empty();
    } else {
      return Optional.of((float) value);
    }
  }
}
