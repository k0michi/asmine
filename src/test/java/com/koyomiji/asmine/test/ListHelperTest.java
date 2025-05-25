package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.ListHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ListHelperTest {
  @Test
  void test_unique_0() {
    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");
    list.add("c");
    list.add("d");
    list.add("a");
    List<String> result = ListHelper.unique(list);

    List<String> expected = new ArrayList<>();
    expected.add("a");
    expected.add("b");
    expected.add("c");
    expected.add("d");

    Assertions.assertEquals(expected, result);
  }

  @Test
  void test_unique_1() {
    List<String> list = new ArrayList<>();
    List<String> result = ListHelper.unique(list);

    List<String> expected = new ArrayList<>();

    Assertions.assertEquals(expected, result);
  }

  @Test
  void test_unique_2() {
    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("a");
    list.add("a");
    list.add("a");
    List<String> result = ListHelper.unique(list);

    List<String> expected = new ArrayList<>();
    expected.add("a");

    Assertions.assertEquals(expected, result);
  }
}
