package com.koyomiji.asmine.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayHelper {
  @SafeVarargs
  public static <T> T[] ofObject(T... array) {
    return array;
  }

  public static char[] ofChar(char... array) {
    return array;
  }

  public static byte[] ofByte(byte... array) {
    return array;
  }

  public static short[] ofShort(short... array) {
    return array;
  }

  public static int[] ofInt(int... array) {
    return array;
  }

  public static long[] ofLong(long... array) {
    return array;
  }

  public static float[] ofFloat(float... array) {
    return array;
  }

  public static double[] ofDouble(double... array) {
    return array;
  }

  public static <T> List<T> toList(T[] array) {
    List<T> result = new ArrayList<>(array.length);
    Collections.addAll(result, array);
    return result;
  }

  public static List<Character> toList(char[] array) {
    List<Character> result = new ArrayList<>(array.length);

    for (char e : array) {
      result.add(e);
    }

    return result;
  }

  public static List<Byte> toList(byte[] array) {
    List<Byte> result = new ArrayList<>(array.length);

    for (byte e : array) {
      result.add(e);
    }

    return result;
  }

  public static List<Short> toList(short[] array) {
    List<Short> result = new ArrayList<>(array.length);

    for (short e : array) {
      result.add(e);
    }

    return result;
  }

  public static List<Integer> toList(int[] array) {
    List<Integer> result = new ArrayList<>(array.length);

    for (int e : array) {
      result.add(e);
    }

    return result;
  }

  public static List<Long> toList(long[] array) {
    List<Long> result = new ArrayList<>(array.length);

    for (long e : array) {
      result.add(e);
    }

    return result;
  }

  public static List<Float> toList(float[] array) {
    List<Float> result = new ArrayList<>(array.length);

    for (float e : array) {
      result.add(e);
    }

    return result;
  }

  public static List<Double> toList(double[] array) {
    List<Double> result = new ArrayList<>(array.length);

    for (double e : array) {
      result.add(e);
    }

    return result;
  }

  public static <T> T[] add(T[] array, T element) {
    List<T> list = new ArrayList<>(Arrays.asList(array));
    list.add(element);
    return list.toArray(array);
  }

  public static char[] add(char[] array, char element) {
    List<Character> list = new ArrayList<>(toList(array));
    list.add(element);
    return ListHelper.toCharArray(list);
  }

  public static byte[] add(byte[] array, byte element) {
    List<Byte> list = new ArrayList<>(toList(array));
    list.add(element);
    return ListHelper.toByteArray(list);
  }

  public static short[] add(short[] array, short element) {
    List<Short> list = new ArrayList<>(toList(array));
    list.add(element);
    return ListHelper.toShortArray(list);
  }

  public static int[] add(int[] array, int element) {
    List<Integer> list = new ArrayList<>(toList(array));
    list.add(element);
    return ListHelper.toIntArray(list);
  }

  public static long[] add(long[] array, long element) {
    List<Long> list = new ArrayList<>(toList(array));
    list.add(element);
    return ListHelper.toLongArray(list);
  }

  public static float[] add(float[] array, float element) {
    List<Float> list = new ArrayList<>(toList(array));
    list.add(element);
    return ListHelper.toFloatArray(list);
  }

  public static double[] add(double[] array, double element) {
    List<Double> list = new ArrayList<>(toList(array));
    list.add(element);
    return ListHelper.toDoubleArray(list);
  }
}
