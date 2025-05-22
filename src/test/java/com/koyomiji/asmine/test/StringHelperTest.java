package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.ArrayHelper;
import com.koyomiji.asmine.common.StringHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class StringHelperTest {
  @Test
  void test_codePoints_0() {
    Assertions.assertArrayEquals(ArrayHelper.ofInt('a'), StringHelper.codePoints("a").get());
  }

  @Test
  void test_codePoints_1() {
    Assertions.assertArrayEquals(ArrayHelper.ofInt(0x1FABF), StringHelper.codePoints("🪿").get());
  }

  @Test
  void test_codePoints_2() {
    Assertions.assertEquals(Optional.empty(), StringHelper.codePoints("🪿".substring(0, 1)));
  }
}
