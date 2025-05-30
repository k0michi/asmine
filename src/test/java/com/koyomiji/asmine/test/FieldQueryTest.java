package com.koyomiji.asmine.test;

import com.koyomiji.asmine.query.FieldQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FieldQueryTest {
  @Test
  void test_0() {
    Object result = FieldQuery.ofNew()
            .setAccess(1)
            .addAccess(2)
            .removeAccess(2)
            .getAccess();
    Assertions.assertEquals(1, result);
  }

  @Test
  void test_1() {
    Object result = FieldQuery.ofNew()
            .setName("A")
            .getName();
    Assertions.assertEquals("A", result);
  }

  @Test
  void test_2() {
    Object result = FieldQuery.ofNew()
            .setDescriptor("I")
            .getDescriptor();
    Assertions.assertEquals("I", result);
  }

  @Test
  void test_3() {
    Object result = FieldQuery.ofNew()
            .setSignature("A")
            .getSignature();
    Assertions.assertEquals("A", result);
  }

  @Test
  void test_4() {
    Object result = FieldQuery.ofNew()
            .setValue(1)
            .getValue();
    Assertions.assertEquals(1, result);
  }
}
