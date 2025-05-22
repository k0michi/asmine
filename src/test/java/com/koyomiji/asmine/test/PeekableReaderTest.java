package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.PeekableReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

public class PeekableReaderTest {
  @Test
  void test_0() throws IOException {
    PeekableReader r = new PeekableReader(new StringReader("01234567890123456789"));
    Assertions.assertEquals('0', r.peek());
    Assertions.assertEquals('0',r.read());
    Assertions.assertEquals('1', r.peek());
    Assertions.assertEquals('1', r.peek());
    Assertions.assertEquals('1',r.read());

    char[] buf;
    buf = new char[4];
    r.peek(buf, 0, 4);
    Assertions.assertArrayEquals(new char[]{'2','3','4','5'}, buf);

    buf = new char[2];
    r.peek(buf, 0, 2);
    Assertions.assertArrayEquals(new char[]{'2','3'}, buf);

    buf = new char[2];
    r.read(buf, 0, 2);
    Assertions.assertArrayEquals(new char[]{'2','3'}, buf);

    buf = new char[2];
    r.peek(buf, 0, 2);
    Assertions.assertArrayEquals(new char[]{'4','5'}, buf);

    buf = new char[2];
    r.read(buf, 0, 2);
    Assertions.assertArrayEquals(new char[]{'4','5'}, buf);

    Assertions.assertEquals('6', r.peek());
    Assertions.assertEquals('6',r.read());

    buf = new char[2];
    r.read(buf, 0, 2);
    Assertions.assertArrayEquals(new char[]{'7','8'}, buf);
  }

  @Test
  void test_1() throws IOException {
    PeekableReader r = new PeekableReader(new StringReader("0"));
    Assertions.assertEquals('0', r.peek());
    Assertions.assertEquals('0',r.read());
    Assertions.assertEquals(-1, r.peek());
    Assertions.assertEquals(-1, r.read());
  }

  @Test
  void test_2() throws IOException {
    PeekableReader r = new PeekableReader(new StringReader(""));
    Assertions.assertEquals(-1, r.peek());
    Assertions.assertEquals(-1, r.read());
  }

  @Test
  void test_3() throws IOException {
    PeekableReader r = new PeekableReader(new StringReader(""));
    char[] buf;
    buf = new char[4];
    r.peek(buf, 0, 4);
    Assertions.assertEquals(-1, r.peek(buf, 0, 4));
    Assertions.assertEquals(-1, r.read(buf, 0, 4));
  }

  @Test
  void test_4() throws IOException {
    PeekableReader r = new PeekableReader(new StringReader("01234567890123456789"));
    Assertions.assertEquals('0', r.peek());
    r.skip(1);
    Assertions.assertEquals('1', r.peek());
    r.skip(1);
    Assertions.assertEquals('2', r.peek());
  }

  @Test
  void test_longerPeek_0() throws IOException {
    PeekableReader r = new PeekableReader(new StringReader("01234567890123456789"));
    char[] buf;
    buf = new char[1];
    r.peek(buf);
    Assertions.assertArrayEquals(new char[]{'0'}, buf);
    buf = new char[2];
    r.peek(buf);
    Assertions.assertArrayEquals(new char[]{'0','1'}, buf);
    buf = new char[3];
    r.peek(buf);
    Assertions.assertArrayEquals(new char[]{'0','1','2'}, buf);
    buf = new char[4];
    r.peek(buf);
    Assertions.assertArrayEquals(new char[]{'0','1','2','3'}, buf);
    buf = new char[5];
    r.peek(buf);
    Assertions.assertArrayEquals(new char[]{'0','1','2','3', '4'}, buf);
  }

  @Test
  void test_eof_0() throws IOException {
    PeekableReader r = new PeekableReader(new StringReader("0"));
    char[] buf;
    buf = new char[1];
    r.peek(buf);
    Assertions.assertArrayEquals(new char[]{'0'},buf);
    r.read(buf);
    Assertions.assertArrayEquals(new char[]{'0'}, buf);
    Assertions.assertEquals(-1, r.read());
  }
}
