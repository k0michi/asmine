package com.koyomiji.asmine.common;

public class BooleanHelper {
  public static int toInt(boolean value) {
    return value ? 1 : 0;
  }

  public static boolean fromInt(int value) {
    return value != 0;
  }
}
