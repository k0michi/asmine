package com.koyomiji.asmine.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class PeekableReader extends Reader {
  private final BufferedReader reader;
  private char[] buf = new char[0];
  private int bufEnd = 0;
  private int bufBegin = 0;

  public PeekableReader(Reader reader) {
    this(new BufferedReader(reader));
  }

  public PeekableReader(BufferedReader bufferedReader) {
    this.reader = bufferedReader;
  }

  public int peek() throws IOException {
    char[] cbuf = new char[1];
    if (peek(cbuf, 0, 1) == -1) {
      return -1;
    }else {
      return cbuf[0];
    }
  }

  public int peek(char[] cbuf) throws IOException {
    return peek(cbuf, 0, cbuf.length);
  }

  public int peek(char[] cbuf, int off, int len) throws IOException {
    if (len == 0) {
      return 0;
    }

    if ((bufEnd - bufBegin) >= len) {
      int readLen = Math.min(len, bufEnd - bufBegin);
      System.arraycopy(buf, bufBegin, cbuf, off, readLen);
      return readLen;
    } else {
      if ((buf.length - bufBegin) < len) {
        char[] newBuf = new char[Math.max(len, buf.length)];
        System.arraycopy(buf, bufBegin, newBuf, 0, bufEnd - bufBegin);
        buf = newBuf;
        bufEnd -= bufBegin;
        bufBegin = 0;
      }

      int read = reader.read(buf, bufEnd, len - (bufEnd - bufBegin));

      if (read != -1) {
        bufEnd += read;
      }

      int peeked = bufEnd - bufBegin;

      if (peeked == 0) {
        return -1;
      }

      System.arraycopy(buf, bufBegin, cbuf, off, peeked);
      return peeked;
    }
  }

  public int read() throws IOException {
    char[] cbuf = new char[1];
    if (read(cbuf, 0, 1) == -1) {
      return -1;
    }else {
      return cbuf[0];
    }
  }

  public int read(char[] cbuf) throws IOException {
    return read(cbuf, 0, cbuf.length);
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    if (len == 0) {
      return 0;
    }

    int readLen = 0;

    if (bufBegin < bufEnd) {
      readLen = Math.min(len, bufEnd - bufBegin);
      System.arraycopy(buf, bufBegin, cbuf, off, readLen);
      bufBegin += readLen;
    }

    if (len -readLen > 0) {
      readLen += reader.read(cbuf,off + readLen,len-readLen);
    }

    return readLen;
  }

  @Override
  public void close() throws IOException {
    reader.close();
  }
}
