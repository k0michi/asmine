package com.koyomiji.asmine.common;

import java.util.Collections;
import java.util.HashSet;

public class HashSetHelper {
  @SafeVarargs
  public static <T> HashSet<T> of(T... elements) {
    HashSet<T> set = new HashSet<>();

    Collections.addAll(set, elements);

    return set;
  }
}
