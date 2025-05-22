package com.koyomiji.asmine.test;

import com.koyomiji.asmine.sexpr.SExprReader;
import com.koyomiji.asmine.sexpr.SExprWriter;
import com.koyomiji.asmine.sexpr.SyntaxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;

@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class SExprReaderTest {
  void roundtrip(String sexpr) throws IOException {
    trip(sexpr, sexpr);
  }

  void trip(String sexpr, String result) throws IOException {
    SExprReader reader =new SExprReader(sexpr);
    SExprWriter writer = new SExprWriter();
    reader.accept(writer);
    Assertions.assertEquals(result, writer.toString());
  }

  @Test
  void test_roundtrip_0() throws IOException {
    roundtrip("(a)");
  }

  @Test
  void test_roundtrip_1() throws IOException {
    roundtrip("(a b c d)");
  }

  @Test
  void test_roundtrip_2() throws IOException {
    roundtrip("(a \"b\" c d)");
  }

  @Test
  void test_roundtrip_3() throws IOException {
    roundtrip("(a \"b\" (c) d)");
  }

  @Test
  void test_roundtrip_4() throws IOException {
    roundtrip("(((((a)))) \"b\" (c) d)");
  }

  @Test
  void test_roundtrip_5() throws IOException {
    roundtrip("(((((a)))) \" b \" (c) d)");
  }

//  @Test
//  void test_roundtrip_6() throws IOException {
//    roundtrip("(((((a)))) \" b \" (c) d\\  e)");
//  }
//
//  @Test
//  void test_roundtrip_7() throws IOException {
//    roundtrip("(((((a)))) \" b \" (c) d\\  e (f (g (h (i)))))");
//  }

  @Test
  void test_roundtrip_8() throws IOException {
    roundtrip("123");
  }

  @Test
  void test_roundtrip_9() throws IOException {
    roundtrip("\"abc\"");
  }

  @Test
  void test_roundtrip_string_0() throws IOException {
    roundtrip("(\"\")");
  }

  @Test
  void test_roundtrip_string_1() throws IOException {
    roundtrip("(\"\\n\")");
  }

  @Test
  void test_roundtrip_string_2() throws IOException {
    roundtrip("(\"\\u{0}\")");
  }

  @Test
  void test_roundtrip_string_3() throws IOException {
    roundtrip("(\"\\u{10}\")");
  }

  @Test
  void test_roundtrip_string_4() throws IOException {
    roundtrip("(\"\\t\")");
  }

  @Test
  void test_roundtrip_string_5() throws IOException {
    roundtrip("(\"\\r\")");
  }

  @Test
  void test_roundtrip_string_6() throws IOException {
    roundtrip("(\"'\")");
  }

  @Test
  void test_roundtrip_string_7() throws IOException {
    roundtrip("(\"\\\"\")");
  }

  @Test
  void test_roundtrip_float_0() throws IOException {
    roundtrip("nan");
  }

  @Test
  void test_roundtrip_float_1() throws IOException {
    roundtrip("-nan");
  }

  @Test
  void test_roundtrip_float_2() throws IOException {
    roundtrip("1.0");
  }

  @Test
  void test_roundtrip_float_3() throws IOException {
    roundtrip("inf");
  }

  @Test
  void test_roundtrip_float_4() throws IOException {
    roundtrip("-inf");
  }

  @Test
  void test_roundtrip_float_5() throws IOException {
    roundtrip("nan:0x1234");
  }

  @Test
  void test_trip_0() throws IOException {
    trip("(()1())", "(() 1 ())");
  }

  @Test
  void test_trip_1() throws IOException {
    trip("(()\"1\"())", "(() \"1\" ())");
  }

  @Test
  void test_trip_2() throws IOException {
    trip("(()\"1\"2())", "(() \"1\" 2 ())");
  }

  @Test
  void test_trip_3() throws IOException {
    trip("(()\"1\"2\"3\"())", "(() \"1\" 2 \"3\" ())");
  }

  @Test
  void test_trip_4() throws IOException {
    trip("(0x1)", "(1)");
  }

  @Test
  void test_trip_5() throws IOException {
    trip("(0xFf)", "(255)");
  }

  @Test
  void test_trip_6() throws IOException {
    trip("(0x1.)", "(1.0)");
  }

  @Test
  void test_trip_7() throws IOException {
    trip("(1_1)", "(11)");
  }

  @Test
  void test_trip_8() throws IOException {
    trip("(_1_1)", "(_1_1)");
  }

  @Test
  void test_trip_9() throws IOException {
    trip("(1. 1.0 1E1 1.E1 1.E+1 1.E-1 1.0E1)", "(1.0 1.0 10.0 10.0 10.0 0.1 10.0)");
  }

  @Test
  void test_trip_10() throws IOException {
    trip("(0xA. 0xA.0 0xAp1 0xA.p1 0xA.p+1 0xA.p-1 0xA.0p+1)", "(10.0 10.0 20.0 20.0 20.0 5.0 20.0)");
  }

  @Test
  void test_trip_11() throws IOException {
    trip("(0x0_A. 0x0_A.0 0x0_Ap1 0x0_A.p1 0x0_A.p+1 0x0_A.p-1 0x0_A.0p+1)", "(10.0 10.0 20.0 20.0 20.0 5.0 20.0)");
  }

  @Test
  void test_trip_12() throws IOException {
    trip("(0x1_0_A_a.1_0p+1_1)", "(8736896.0)");
  }

  @Test
  void test_trip_13() throws IOException {
    trip("(0x1.P0 0x1.p0 1E0 1e0)", "(1.0 1.0 1.0 1.0)");
  }

  @Test
  void test_trip_spaces_0() throws IOException {
    trip(" ( a  b  c ) ", "(a b c)");
  }

  @Test
  void test_trip_comment_0() throws IOException {
    trip("(()(;;))", "(())");
  }

  @Test
  void test_trip_comment_1() throws IOException {
    trip("(()(;(;(;(;;);););)())", "(() ())");
  }

  @Test
  void test_trip_comment_2() throws IOException {
    trip("(()\n;;\n())", "(() ())");
  }

  @Test
  void test_trip_comment_3() throws IOException {
    trip("(());;", "(())");
  }

  @Test
  void test_fail_0() {
    SExprReader reader = new SExprReader("(a b c d");
    SyntaxException e = Assertions.assertThrows(SyntaxException.class, () -> {
      reader.accept(new SExprWriter());
    });
    Assertions.assertEquals(8, e.getSourceLocation().getOffset());
  }

  @Test
  void test_fail_1() {
    SExprReader reader = new SExprReader("(");
    SyntaxException e = Assertions.assertThrows(SyntaxException.class, () -> {
      reader.accept(new SExprWriter());
    });
    Assertions.assertEquals(1, e.getSourceLocation().getOffset());
  }

  @Test
  void test_fail_2() {
    SExprReader reader = new SExprReader(")");
    SyntaxException e = Assertions.assertThrows(SyntaxException.class, () -> {
      reader.accept(new SExprWriter());
    });
    Assertions.assertEquals(0, e.getSourceLocation().getOffset());
  }

  @Test
  void test_fail_3() {
    SExprReader reader = new SExprReader("");
    SyntaxException e = Assertions.assertThrows(SyntaxException.class, () -> {
      reader.accept(new SExprWriter());
    });
    Assertions.assertEquals(0, e.getSourceLocation().getOffset());
  }

  @Test
  void test_fail_4() {
    SExprReader reader = new SExprReader("(;)");
    SyntaxException e = Assertions.assertThrows(SyntaxException.class, () -> {
      reader.accept(new SExprWriter());
    });
    Assertions.assertEquals(3, e.getSourceLocation().getOffset());
  }

  @Test
  void test_fail_5() {
    SExprReader reader = new SExprReader("(;;)");
    SyntaxException e = Assertions.assertThrows(SyntaxException.class, () -> {
      reader.accept(new SExprWriter());
    });
    Assertions.assertEquals(4, e.getSourceLocation().getOffset());
  }

  @Test
  void test_fail_6() {
    SExprReader reader = new SExprReader("\"");
    SyntaxException e = Assertions.assertThrows(SyntaxException.class, () -> {
      reader.accept(new SExprWriter());
    });
    Assertions.assertEquals(1, e.getSourceLocation().getOffset());
  }

  @Test
  void test_fail_7() {
    SExprReader reader = new SExprReader("\"\"\"");
    SyntaxException e = Assertions.assertThrows(SyntaxException.class, () -> {
      reader.accept(new SExprWriter());
    });
    Assertions.assertEquals(2, e.getSourceLocation().getOffset());
  }
}
