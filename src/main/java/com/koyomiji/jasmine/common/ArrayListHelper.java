package com.koyomiji.jasmine.common;

import java.util.ArrayList;
import java.util.Collections;

public class ArrayListHelper {
  public static <T> ArrayList<T> of(T... elements) {
    ArrayList<T> list = new ArrayList<>();
    Collections.addAll(list, elements);
    return list;
  }
}
