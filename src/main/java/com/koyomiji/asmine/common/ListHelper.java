package com.koyomiji.asmine.common;

import com.koyomiji.asmine.tuple.Pair;

import java.util.*;
import java.util.function.Predicate;

public class ListHelper {
  public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
    List<T> result = new ArrayList<>();

    for (T t : list) {
      if (predicate.test(t)) {
        result.add(t);
      }
    }

    return result;
  }

  public static <T> Optional<T> findOptional(List<T> list, Predicate<T> predicate) {
    for (T t : list) {
      if (predicate.test(t)) {
        return Optional.of(t);
      }
    }

    return Optional.empty();
  }

  public static <T> T findOrNull(List<T> list, Predicate<T> predicate) {
    return findOptional(list, predicate).orElse(null);
  }

  public static <T> Optional<T> findLastOptional(List<T> list, Predicate<T> predicate) {
    for (int i = list.size() - 1; i >= 0; i--) {
      if (predicate.test(list.get(i))) {
        return Optional.of(list.get(i));
      }
    }

    return Optional.empty();
  }

  public static <T> T findLastOrNull(List<T> list, Predicate<T> predicate) {
    return findLastOptional(list, predicate).orElse(null);
  }

  public static <T> int findIndex(List<T> list, Predicate<T> predicate) {
    for (int i = 0; i < list.size(); i++) {
      if (predicate.test(list.get(i))) {
        return i;
      }
    }

    return -1;
  }

  public static <T> int findLastIndex(List<T> list, Predicate<T> predicate) {
    for (int i = list.size() - 1; i >= 0; i--) {
      if (predicate.test(list.get(i))) {
        return i;
      }
    }

    return -1;
  }

  public static <T> Optional<T> atOptional(List<T> list, int index) {
    if (index >=0 && index < list.size()) {
      return Optional.of(list.get(index));
    }

    if (index<0 && index >= -list.size()) {
      return Optional.of(list.get(list.size() + index));
    }

    return Optional.empty();
  }

  public static <T> T at(List<T> list, int index) {
    return atOptional(list, index).get();
  }

  public static <T> T atOrNull(List<T> list, int index) {
    return atOptional(list, index).orElse(null);
  }

  public static <T> Optional<T> getOptional(List<T> list, int index) {
    if (index < 0 || index >= list.size()) {
      return Optional.empty();
    }

    return Optional.ofNullable(list.get(index));
  }

  public static <T> T getOrNull(List<T> list, int index) {
    return getOptional(list, index).orElse(null);
  }

  public static <T> Optional<T> getFirstOptional(List<T> list) {
    return getOptional(list, 0);
  }

  public static <T> Optional<T> getLastOptional(List<T> list) {
    return getOptional(list, list.size() - 1);
  }

  public static <E> List<E> concat(List<E> a, List<E> b) {
    List<E> result = new ArrayList<>(a);
    result.addAll(b);
    return result;
  }

  public static <T> List<T> unique(List<T> list) {
    Set<T> set = new HashSet<>();
    ArrayList<T> result = new ArrayList<>();

    for (T t : list) {
      if (!set.contains(t)) {
        set.add(t);
        result.add(t);
      }
    }

    return result;
  }

  public static <T> void resize(List<T> list, int size) {
    while (list.size() < size) {
      list.add(null);
    }

    while (list.size() > size) {
      list.remove(list.size() - 1);
    }
  }

  public static char[] toCharArray(List<Character> list) {
    char[] result = new char[list.size()];

    for (int i = 0; i < list.size(); i++) {
      result[i] = list.get(i);
    }

    return result;
  }

  public static byte[] toByteArray(List<Byte> list) {
    byte[] result = new byte[list.size()];

    for (int i = 0; i < list.size(); i++) {
      result[i] = list.get(i);
    }

    return result;
  }

  public static short[] toShortArray(List<Short> list) {
    short[] result = new short[list.size()];

    for (int i = 0; i < list.size(); i++) {
      result[i] = list.get(i);
    }

    return result;
  }

  public static int[] toIntArray(List<Integer> list) {
    int[] result = new int[list.size()];

    for (int i = 0; i < list.size(); i++) {
      result[i] = list.get(i);
    }

    return result;
  }

  public static long[] toLongArray(List<Long> list) {
    long[] result = new long[list.size()];

    for (int i = 0; i < list.size(); i++) {
      result[i] = list.get(i);
    }

    return result;
  }

  public static float[] toFloatArray(List<Float> list) {
    float[] result = new float[list.size()];

    for (int i = 0; i < list.size(); i++) {
      result[i] = list.get(i);
    }

    return result;
  }

  public static double[] toDoubleArray(List<Double> list) {
    double[] result = new double[list.size()];

    for (int i = 0; i < list.size(); i++) {
      result[i] = list.get(i);
    }

    return result;
  }

  public static <T> List<T> sublist(List<T> self, Pair<Integer, Integer> range) {
    return self.subList(range.first, range.second);
  }

  public static <T> void removeRange(List<T> self, int begin, int end) {
    self.subList(begin, end).clear();
  }
}
