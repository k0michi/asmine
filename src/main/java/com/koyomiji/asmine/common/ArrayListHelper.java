package com.koyomiji.asmine.common;

import java.util.ArrayList;
import java.util.Collections;

public class ArrayListHelper {
  @SafeVarargs
  public static <T> ArrayList<T> of(T... elements) {
    ArrayList<T> list = new ArrayList<>();
    Collections.addAll(list, elements);
    return list;
  }
}
