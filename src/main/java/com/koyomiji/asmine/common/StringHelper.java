package com.koyomiji.asmine.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StringHelper {
  public static Optional<int[]> codePoints(String s) {
    List<Integer> result = new ArrayList<>();

    for(int i =0 ; i < s.length(); i++) {
      if (Character.isHighSurrogate(s.charAt(i))) {
        if (i + 1 < s.length() && Character.isLowSurrogate(s.charAt(i+1))) {
          result.add(Character.toCodePoint(s.charAt(i), s.charAt(i+1)));
          i++;
        } else {
          return Optional.empty();
        }
      } else {
        result.add((int)s.charAt(i));
      }
    }

    return Optional.of(ListHelper.toIntArray(result));
  }
}
